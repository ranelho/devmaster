package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.PedidoService;
import com.devmaster.domain.*;
import com.devmaster.domain.enums.StatusPagamento;
import com.devmaster.domain.enums.StatusPedido;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoApplicationService implements PedidoService {

    private static final Random RANDOM = new Random();

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final EnderecoClienteRepository enderecoClienteRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    private final ProdutoRepository produtoRepository;
    private final GrupoOpcaoRepository grupoOpcaoRepository;
    private final OpcaoRepository opcaoRepository;
    private final CupomRepository cupomRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final OpcaoItemPedidoRepository opcaoItemPedidoRepository;
    private final CupomPedidoRepository cupomPedidoRepository;
    private final EntregadorRepository entregadorRepository;
    private final HistoricoStatusPedidoRepository historicoStatusPedidoRepository;

    @Override
    @Transactional
    public PedidoResponse criarPedido(UUID usuarioId, PedidoRequest request) {
        var cliente = buscarClienteOuFalhar(request.clienteId());
        var restaurante = buscarRestauranteOuFalhar(request.restauranteId());
        var endereco = buscarEnderecoOuFalhar(request.enderecoEntregaId());
        var tipoPagamento = buscarTipoPagamentoOuFalhar(request.tipoPagamentoId());

        validarEnderecoDoCliente(endereco, cliente);
        validarRestauranteAtivo(restaurante);

        var numeroPedido = gerarNumeroPedido();
        var subtotal = BigDecimal.ZERO;
        var taxaEntrega = request.taxaEntrega() != null ? request.taxaEntrega() : restaurante.getTaxaEntrega();
        var desconto = BigDecimal.ZERO;

        var pedido = Pedido.builder()
                .numeroPedido(numeroPedido)
                .cliente(cliente)
                .restaurante(restaurante)
                .enderecoEntrega(endereco)
                .tipoPagamento(tipoPagamento)
                .status(StatusPedido.AGUARDANDO_CONFIRMACAO)
                .statusPagamento(StatusPagamento.PENDENTE)
                .valorTroco(request.valorTroco())
                .subtotal(subtotal)
                .taxaEntrega(taxaEntrega)
                .desconto(desconto)
                .valorTotal(BigDecimal.ZERO)
                .observacoes(request.observacoes())
                .previsaoEntrega(calcularPrevisaoEntrega(restaurante))
                .build();

        pedido = pedidoRepository.save(pedido);

        for (var itemRequest : request.itens()) {
            subtotal = subtotal.add(criarItemPedido(pedido, itemRequest));
        }

        if (request.codigoCupom() != null && !request.codigoCupom().isBlank()) {
            desconto = aplicarCupom(pedido, request.codigoCupom(), subtotal);
        }

        if (subtotal.compareTo(restaurante.getValorMinimoPedido()) < 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST,
                    "Valor mínimo do pedido é " + restaurante.getValorMinimoPedido());
        }

        pedido.atualizarTotais(subtotal, taxaEntrega, desconto);
        pedido = pedidoRepository.save(pedido);

        registrarHistorico(pedido, StatusPedido.AGUARDANDO_CONFIRMACAO, "Pedido criado", toStringOrSystem(usuarioId));

        return montarPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPedido(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        return montarPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPedidoPorNumero(UUID usuarioId, String numeroPedido) {
        var pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return montarPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> listarPedidosRestaurante(UUID usuarioId, Long restauranteId, StatusPedido status) {
        buscarRestauranteOuFalhar(restauranteId);

        var pedidos = status != null
                ? pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, status)
                : pedidoRepository.findByRestauranteIdOrderByCriadoEmDesc(restauranteId);

        return pedidos.stream()
                .map(PedidoResumoResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> listarPedidosCliente(UUID usuarioId, Long clienteId, StatusPedido status) {
        buscarClienteOuFalhar(clienteId);

        var pedidos = status != null
                ? pedidoRepository.findByClienteIdAndStatusOrderByCriadoEmDesc(clienteId, status)
                : pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);

        return pedidos.stream()
                .map(PedidoResumoResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> listarPedidosPorTelefone(UUID usuarioId, String telefone) {
        var telefoneNormalizado = telefone.replaceAll("\\D", "");
        
        var cliente = clienteRepository.findByTelefone(telefoneNormalizado)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        return pedidoRepository.findByClienteIdOrderByCriadoEmDesc(cliente.getId())
                .stream()
                .map(PedidoResumoResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PedidoResumoResponse> listarPedidosRestauranteComPaginacao(
            UUID usuarioId,
            Long restauranteId,
            StatusPedido status,
            Pageable pageable
    ) {
        buscarRestauranteOuFalhar(restauranteId);

        var pedidos = status != null
                ? pedidoRepository.findByRestauranteIdAndStatus(restauranteId, status, pageable)
                : pedidoRepository.findByRestauranteId(restauranteId, pageable);

        return pedidos.map(PedidoResumoResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResumoResponse> listarPedidosClienteComPaginacao(
            UUID usuarioId,
            Long clienteId,
            Pageable pageable
    ) {
        buscarClienteOuFalhar(clienteId);
        return pedidoRepository.findByClienteId(clienteId, pageable)
                .map(PedidoResumoResponse::from);
    }

    @Override
    @Transactional
    public PedidoResponse atualizarStatus(UUID usuarioId, Long pedidoId, AtualizarStatusPedidoRequest request) {
        var pedido = buscarPedidoOuFalhar(pedidoId);

        switch (request.status()) {
            case CONFIRMADO -> pedido.confirmar();
            case PREPARANDO -> pedido.iniciarPreparo();
            case PRONTO -> pedido.marcarComoPronto();
            case DESPACHADO -> pedido.despachar();
            case ENTREGUE -> pedido.entregar();
            case CANCELADO -> {
                if (!pedido.podeSerCancelado()) {
                    throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser cancelado neste status");
                }
                pedido.cancelar(request.observacoes());
            }
            default -> throw APIException.build(HttpStatus.BAD_REQUEST, "Status inválido");
        }

        pedido = pedidoRepository.save(pedido);
        registrarHistorico(pedido, request.status(), request.observacoes(), toStringOrSystem(usuarioId));

        return montarPedidoResponse(pedido);
    }

    @Transactional
    public void confirmarPedido(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.confirmar();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.CONFIRMADO, null, toStringOrSystem(usuarioId));
    }

    @Transactional
    public void iniciarPreparo(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.iniciarPreparo();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.PREPARANDO, null, toStringOrSystem(usuarioId));
    }

    @Transactional
    public void marcarComoPronto(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.marcarComoPronto();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.PRONTO, null, toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void despacharPedido(UUID usuarioId, Long pedidoId, Long entregadorId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        var entregador = buscarEntregadorOuFalhar(entregadorId);
        
        if (Boolean.FALSE.equals(entregador.getAtivo())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Entregador inativo");
        }
        
        pedido.vincularEntregador(entregador);
        pedido.despachar();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.DESPACHADO, "Entregador: " + entregador.getNomeCompleto(), toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void entregarPedido(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.entregar();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.ENTREGUE, null, toStringOrSystem(usuarioId));
    }

    @Transactional
    public void cancelarPedido(UUID usuarioId, Long pedidoId, CancelarPedidoRequest request) {
        var pedido = buscarPedidoOuFalhar(pedidoId);

        if (!pedido.podeSerCancelado()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser cancelado neste status");
        }

        pedido.cancelar(request.motivo());
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.CANCELADO, request.motivo(), toStringOrSystem(usuarioId));
    }

    @Transactional
    public void aprovarPagamento(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.aprovarPagamento();
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void recusarPagamento(UUID usuarioId, Long pedidoId) {
        var pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.recusarPagamento();
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoStatusPedidoResponse> buscarHistorico(UUID usuarioId, Long pedidoId) {
        buscarPedidoOuFalhar(pedidoId);

        return historicoStatusPedidoRepository.findByPedidoIdOrderByCriadoEmAsc(pedidoId)
                .stream()
                .map(HistoricoStatusPedidoResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> buscarPedidosPorPeriodo(
            UUID usuarioId,
            Long restauranteId,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        buscarRestauranteOuFalhar(restauranteId);

        return pedidoRepository.findByRestauranteIdAndCriadoEmBetween(restauranteId, inicio, fim)
                .stream()
                .map(PedidoResumoResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> buscarPedidosAtivos(UUID usuarioId, Long restauranteId) {
        buscarRestauranteOuFalhar(restauranteId);

        var statusAtivos = List.of(
                StatusPedido.AGUARDANDO_CONFIRMACAO,
                StatusPedido.CONFIRMADO,
                StatusPedido.PREPARANDO,
                StatusPedido.PRONTO,
                StatusPedido.DESPACHADO
        );

        return pedidoRepository.findByRestauranteIdOrderByCriadoEmDesc(restauranteId)
                .stream()
                .filter(p -> statusAtivos.contains(p.getStatus()))
                .map(PedidoResumoResponse::from)
                .toList();
    }

    // Métodos auxiliares

    private void validarEnderecoDoCliente(EnderecoCliente endereco, Cliente cliente) {
        if (!endereco.getCliente().getId().equals(cliente.getId())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Endereço não pertence ao cliente");
        }
    }

    private void validarRestauranteAtivo(Restaurante restaurante) {
        if (Boolean.FALSE.equals(restaurante.getAtivo())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Restaurante inativo");
        }
        if (Boolean.FALSE.equals(restaurante.getAberto())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Restaurante fechado");
        }
    }

    private BigDecimal criarItemPedido(Pedido pedido, ItemPedidoRequest request) {
        var produto = buscarProdutoOuFalhar(request.produtoId());

        if (Boolean.FALSE.equals(produto.getDisponivel())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Produto indisponível: " + produto.getNome());
        }

        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Produto não pertence ao restaurante");
        }

        // Validar grupos de opções obrigatórios
        validarGruposObrigatorios(produto.getId(), request.opcoes());

        var precoUnitario = produto.getPrecoFinal();
        var totalOpcoesAdicionais = BigDecimal.ZERO;

        var item = ItemPedido.builder()
                .pedido(pedido)
                .produto(produto)
                .quantidade(request.quantidade())
                .precoUnitario(precoUnitario)
                .subtotal(BigDecimal.ZERO)
                .observacoes(request.observacoes())
                .build();

        item = itemPedidoRepository.save(item);

        if (request.opcoes() != null && !request.opcoes().isEmpty()) {
            totalOpcoesAdicionais = criarOpcoesItem(item, request.opcoes());
        }

        item.calcularSubtotal(totalOpcoesAdicionais);
        item = itemPedidoRepository.save(item);

        return item.getSubtotal();
    }

    private void validarGruposObrigatorios(Long produtoId, List<OpcaoItemRequest> opcoesRequest) {
        var gruposObrigatorios = grupoOpcaoRepository
                .findByProdutoIdOrderByOrdemExibicao(produtoId)
                .stream()
                .filter(GrupoOpcao::getObrigatorio)
                .toList();

        if (gruposObrigatorios.isEmpty()) {
            return;
        }

        var gruposComOpcoes = opcoesRequest != null
                ? opcoesRequest.stream()
                .map(OpcaoItemRequest::grupoOpcaoId)
                .distinct()
                .toList()
                : List.of();

        for (var grupo : gruposObrigatorios) {
            if (!gruposComOpcoes.contains(grupo.getId())) {
                throw APIException.build(HttpStatus.BAD_REQUEST,
                        "Grupo de opção obrigatório não selecionado: " + grupo.getNome());
            }

            long quantidadeSelecionada = gruposComOpcoes.stream()
                    .filter(id -> id.equals(grupo.getId()))
                    .count();

            if (!grupo.isSelecaoValida((int) quantidadeSelecionada)) {
                throw APIException.build(HttpStatus.BAD_REQUEST,
                        String.format("Grupo '%s' requer entre %d e %d seleções",
                                grupo.getNome(), grupo.getMinimoSelecoes(), grupo.getMaximoSelecoes()));
            }
        }
    }

    private BigDecimal criarOpcoesItem(ItemPedido item, List<OpcaoItemRequest> opcoesRequest) {
        var totalAdicional = BigDecimal.ZERO;

        for (var opcaoRequest : opcoesRequest) {
            var grupo = buscarGrupoOpcaoOuFalhar(opcaoRequest.grupoOpcaoId());
            var opcao = buscarOpcaoOuFalhar(opcaoRequest.opcaoId());

            if (!opcao.getGrupoOpcao().getId().equals(grupo.getId())) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Opção não pertence ao grupo");
            }

            if (Boolean.FALSE.equals(opcao.getDisponivel())) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Opção indisponível: " + opcao.getNome());
            }

            var opcaoItem = OpcaoItemPedido.builder()
                    .itemPedido(item)
                    .grupoOpcao(grupo)
                    .opcao(opcao)
                    .precoAdicional(opcao.getPrecoAdicional())
                    .build();

            opcaoItemPedidoRepository.save(opcaoItem);
            totalAdicional = totalAdicional.add(opcao.getPrecoAdicional());
        }

        return totalAdicional;
    }

    private BigDecimal aplicarCupom(Pedido pedido, String codigoCupom, BigDecimal subtotal) {
        var cupom = cupomRepository.findByCodigo(codigoCupom)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cupom não encontrado"));

        if (!cupom.isValido()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Cupom inválido ou expirado");
        }

        if (subtotal.compareTo(cupom.getValorMinimoPedido()) < 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST,
                    "Valor mínimo para usar o cupom é " + cupom.getValorMinimoPedido());
        }

        var desconto = cupom.calcularDesconto(subtotal);

        var cupomPedido = CupomPedido.builder()
                .pedido(pedido)
                .cupom(cupom)
                .descontoAplicado(desconto)
                .build();

        cupomPedidoRepository.save(cupomPedido);
        cupom.incrementarUso();
        cupomRepository.save(cupom);

        return desconto;
    }

    private void registrarHistorico(Pedido pedido, StatusPedido status, String observacoes, String criadoPor) {
        var historico = HistoricoStatusPedido.builder()
                .pedido(pedido)
                .status(status)
                .observacoes(observacoes)
                .criadoPor(criadoPor)
                .build();

        historicoStatusPedidoRepository.save(historico);
    }

    private PedidoResponse montarPedidoResponse(Pedido pedido) {
        var itens = itemPedidoRepository.findByPedidoId(pedido.getId())
                .stream()
                .map(item -> {
                    var opcoes = opcaoItemPedidoRepository
                            .findByItemPedidoId(item.getId())
                            .stream()
                            .map(OpcaoItemPedidoResponse::from)
                            .toList();
                    return ItemPedidoResponse.from(item, opcoes);
                })
                .toList();

        var historico = historicoStatusPedidoRepository
                .findByPedidoIdOrderByCriadoEmAsc(pedido.getId())
                .stream()
                .map(HistoricoStatusPedidoResponse::from)
                .toList();

        var codigoCupom = cupomPedidoRepository.findByPedidoId(pedido.getId())
                .map(cp -> cp.getCupom().getCodigo())
                .orElse(null);

        return PedidoResponse.from(
            pedido,
            pedido.getCliente().getId(),
            pedido.getCliente().getTelefone(),
            pedido.getRestaurante().getNome(),
            EnderecoClienteResponse.from(pedido.getEnderecoEntrega()),
            pedido.getTipoPagamento().getNome(),
            pedido.getTipoPagamento().getRequerTroco(),
            codigoCupom,
            itens,
            historico
        );
    }

    private String gerarNumeroPedido() {
        String numero;
        do {
            numero = String.format("%d%04d",
                    System.currentTimeMillis() % 1000000,
                    RANDOM.nextInt(10000));
        } while (pedidoRepository.existsByNumeroPedido(numero));
        return numero;
    }

    private LocalDateTime calcularPrevisaoEntrega(Restaurante restaurante) {
        int tempoMedio = restaurante.getTempoMedioEntrega() != null
                ? restaurante.getTempoMedioEntrega()
                : 45;
        return LocalDateTime.now().plusMinutes(tempoMedio);
    }

    private Cliente buscarClienteOuFalhar(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private Restaurante buscarRestauranteOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
    }

    private EnderecoCliente buscarEnderecoOuFalhar(Long enderecoId) {
        return enderecoClienteRepository.findById(enderecoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
    }

    private TipoPagamento buscarTipoPagamentoOuFalhar(Long tipoPagamentoId) {
        return tipoPagamentoRepository.findById(tipoPagamentoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tipo de pagamento não encontrado"));
    }

    private Produto buscarProdutoOuFalhar(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private GrupoOpcao buscarGrupoOpcaoOuFalhar(Long grupoId) {
        return grupoOpcaoRepository.findById(grupoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Grupo de opção não encontrado"));
    }

    private Opcao buscarOpcaoOuFalhar(Long opcaoId) {
        return opcaoRepository.findById(opcaoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Opção não encontrada"));
    }

    private Pedido buscarPedidoOuFalhar(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
    
    private Entregador buscarEntregadorOuFalhar(Long entregadorId) {
        return entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
    }
    
    /**
     * Converte UUID para String ou retorna "SYSTEM" se for null.
     * Usado para endpoints públicos onde não há usuário autenticado.
     */
    private String toStringOrSystem(UUID usuarioId) {
        return usuarioId != null ? usuarioId.toString() : "SYSTEM";
    }
}
