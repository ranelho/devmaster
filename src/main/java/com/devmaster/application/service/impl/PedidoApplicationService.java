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

/**
 * Implementação do serviço de Pedido.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
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
    private final HistoricoStatusPedidoRepository historicoStatusPedidoRepository;

    @Override
    @Transactional
    public PedidoResponse criarPedido(UUID usuarioId, PedidoRequest request) {
        Cliente cliente = buscarClienteOuFalhar(request.clienteId());
        Restaurante restaurante = buscarRestauranteOuFalhar(request.restauranteId());
        EnderecoCliente endereco = buscarEnderecoOuFalhar(request.enderecoEntregaId());
        TipoPagamento tipoPagamento = buscarTipoPagamentoOuFalhar(request.tipoPagamentoId());

        if (!endereco.getCliente().getId().equals(cliente.getId())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Endereço não pertence ao cliente");
        }

        if (Boolean.FALSE.equals(restaurante.getAtivo())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Restaurante inativo");
        }

        if (Boolean.FALSE.equals(restaurante.getAberto())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Restaurante fechado");
        }

        String numeroPedido = gerarNumeroPedido();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxaEntrega = request.taxaEntrega() != null 
            ? request.taxaEntrega() 
            : restaurante.getTaxaEntrega();
        BigDecimal desconto = BigDecimal.ZERO;

        Pedido pedido = Pedido.builder()
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

        for (ItemPedidoRequest itemRequest : request.itens()) {
            subtotal = subtotal.add(criarItemPedido(pedido, itemRequest));
        }

        if (request.codigoCupom() != null && !request.codigoCupom().isBlank()) {
            desconto = aplicarCupom(pedido, request.codigoCupom(), subtotal);
        }

        if (subtotal.compareTo(restaurante.getValorMinimoPedido()) < 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST,
                    "Valor mínimo do pedido é " + restaurante.getValorMinimoPedido());
        }

        BigDecimal total = subtotal.add(taxaEntrega).subtract(desconto);

        pedido.setSubtotal(subtotal);
        pedido.setDesconto(desconto);
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        registrarHistorico(pedido, StatusPedido.AGUARDANDO_CONFIRMACAO, "Pedido criado", toStringOrSystem(usuarioId));

        return montarPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPedido(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        return montarPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPedidoPorNumero(UUID usuarioId, String numeroPedido) {
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return montarPedidoResponse(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> listarPedidosRestaurante(UUID usuarioId, Long restauranteId, StatusPedido status) {
        buscarRestauranteOuFalhar(restauranteId);

        List<Pedido> pedidos = status != null
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

        List<Pedido> pedidos = status != null
                ? pedidoRepository.findByClienteIdAndStatusOrderByCriadoEmDesc(clienteId, status)
                : pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);

        return pedidos.stream()
                .map(PedidoResumoResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> listarPedidosPorTelefone(UUID usuarioId, String telefone) {
        // Normalizar telefone removendo caracteres especiais
        String telefoneNormalizado = telefone.replaceAll("[^0-9]", "");
        
        Cliente cliente = clienteRepository.findByTelefone(telefoneNormalizado)
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

        Page<Pedido> pedidos = status != null
                ? pedidoRepository.findByRestauranteIdAndStatus(restauranteId, status, pageable)
                : pedidoRepository.findByRestauranteId(restauranteId, pageable);

        return pedidos.map(PedidoResumoResponse::from);
    }

    @Override
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
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);

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

    @Override
    @Transactional
    public void confirmarPedido(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.confirmar();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.CONFIRMADO, null, toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void iniciarPreparo(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.iniciarPreparo();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.PREPARANDO, null, toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void marcarComoPronto(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.marcarComoPronto();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.PRONTO, null, toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void despacharPedido(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.despachar();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.DESPACHADO, null, toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void entregarPedido(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.entregar();
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.ENTREGUE, null, toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void cancelarPedido(UUID usuarioId, Long pedidoId, CancelarPedidoRequest request) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);

        if (!pedido.podeSerCancelado()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Pedido não pode ser cancelado neste status");
        }

        pedido.cancelar(request.motivo());
        pedidoRepository.save(pedido);
        registrarHistorico(pedido, StatusPedido.CANCELADO, request.motivo(), toStringOrSystem(usuarioId));
    }

    @Override
    @Transactional
    public void aprovarPagamento(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
        pedido.aprovarPagamento();
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void recusarPagamento(UUID usuarioId, Long pedidoId) {
        Pedido pedido = buscarPedidoOuFalhar(pedidoId);
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

        List<StatusPedido> statusAtivos = List.of(
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

    private BigDecimal criarItemPedido(Pedido pedido, ItemPedidoRequest request) {
        Produto produto = buscarProdutoOuFalhar(request.produtoId());

        if (Boolean.FALSE.equals(produto.getDisponivel())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Produto indisponível: " + produto.getNome());
        }

        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Produto não pertence ao restaurante");
        }

        // Validar grupos de opções obrigatórios
        validarGruposObrigatorios(produto.getId(), request.opcoes());

        BigDecimal precoUnitario = produto.getPrecoFinal();
        BigDecimal totalOpcoesAdicionais = BigDecimal.ZERO;

        ItemPedido item = ItemPedido.builder()
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
        List<GrupoOpcao> gruposObrigatorios = grupoOpcaoRepository
                .findByProdutoIdOrderByOrdemExibicao(produtoId)
                .stream()
                .filter(GrupoOpcao::getObrigatorio)
                .toList();

        if (gruposObrigatorios.isEmpty()) {
            return;
        }

        List<Long> gruposComOpcoes = opcoesRequest != null
                ? opcoesRequest.stream()
                .map(OpcaoItemRequest::grupoOpcaoId)
                .distinct()
                .toList()
                : List.of();

        for (GrupoOpcao grupo : gruposObrigatorios) {
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
        BigDecimal totalAdicional = BigDecimal.ZERO;

        for (OpcaoItemRequest opcaoRequest : opcoesRequest) {
            GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(opcaoRequest.grupoOpcaoId());
            Opcao opcao = buscarOpcaoOuFalhar(opcaoRequest.opcaoId());

            if (!opcao.getGrupoOpcao().getId().equals(grupo.getId())) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Opção não pertence ao grupo");
            }

            if (Boolean.FALSE.equals(opcao.getDisponivel())) {
                throw APIException.build(HttpStatus.BAD_REQUEST, "Opção indisponível: " + opcao.getNome());
            }

            OpcaoItemPedido opcaoItem = OpcaoItemPedido.builder()
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
        Cupom cupom = cupomRepository.findByCodigo(codigoCupom)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cupom não encontrado"));

        if (!cupom.isValido()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Cupom inválido ou expirado");
        }

        if (subtotal.compareTo(cupom.getValorMinimoPedido()) < 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST,
                    "Valor mínimo para usar o cupom é " + cupom.getValorMinimoPedido());
        }

        BigDecimal desconto = cupom.calcularDesconto(subtotal);

        CupomPedido cupomPedido = CupomPedido.builder()
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
        HistoricoStatusPedido historico = HistoricoStatusPedido.builder()
                .pedido(pedido)
                .status(status)
                .observacoes(observacoes)
                .criadoPor(criadoPor)
                .build();

        historicoStatusPedidoRepository.save(historico);
    }

    private PedidoResponse montarPedidoResponse(Pedido pedido) {
        List<ItemPedidoResponse> itens = itemPedidoRepository.findByPedidoId(pedido.getId())
                .stream()
                .map(item -> {
                    List<OpcaoItemPedidoResponse> opcoes = opcaoItemPedidoRepository
                            .findByItemPedidoId(item.getId())
                            .stream()
                            .map(OpcaoItemPedidoResponse::from)
                            .toList();
                    return ItemPedidoResponse.from(item, opcoes);
                })
                .toList();

        List<HistoricoStatusPedidoResponse> historico = historicoStatusPedidoRepository
                .findByPedidoIdOrderByCriadoEmAsc(pedido.getId())
                .stream()
                .map(HistoricoStatusPedidoResponse::from)
                .toList();

        String codigoCupom = cupomPedidoRepository.findByPedidoId(pedido.getId())
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
    
    /**
     * Converte UUID para String ou retorna "SYSTEM" se for null.
     * Usado para endpoints públicos onde não há usuário autenticado.
     */
    private String toStringOrSystem(UUID usuarioId) {
        return usuarioId != null ? usuarioId.toString() : "SYSTEM";
    }
}
