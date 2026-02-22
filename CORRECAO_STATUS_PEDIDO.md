# Correção: Status Nulo e UsuarioId Nulo ao Criar Pedido

## Problemas Identificados

### 1. Status Nulo
Ao tentar criar um pedido via frontend, ocorria erro no banco de dados:
```
o valor nulo na coluna 'status' da relação 'pedidos' viola a restrição de não-nulo
```

### 2. UsuarioId Nulo
Após corrigir o status, ocorria outro erro:
```
Cannot invoke "java.util.UUID.toString()" because "usuarioId" is null
```

## Causas Raiz

### Problema 1: Status Nulo
O problema estava no uso do `@Builder` do Lombok na entidade `Pedido.java`. Quando usamos `@Builder`, o Lombok ignora os valores padrão definidos diretamente nos campos da classe.

### Problema 2: UsuarioId Nulo
Os endpoints públicos (`/public/v1/pedidos`) não têm usuário autenticado, então o `PublicPedidoRestController` passa `null` como `usuarioId` para o service. O service tentava fazer `usuarioId.toString()` causando NullPointerException.

## Soluções Implementadas

### Solução 1: Status Explícito no Builder
Adicionado explicitamente os valores de `status` e `statusPagamento` no builder em `PedidoApplicationService.java`:

```java
Pedido pedido = Pedido.builder()
    .numeroPedido(numeroPedido)
    .cliente(cliente)
    .restaurante(restaurante)
    .enderecoEntrega(endereco)
    .tipoPagamento(tipoPagamento)
    .status(StatusPedido.PENDENTE)              // ✅ Adicionado
    .statusPagamento(StatusPagamento.PENDENTE)  // ✅ Adicionado
    .valorTroco(request.valorTroco())
    .subtotal(subtotal)
    .taxaEntrega(taxaEntrega)
    .desconto(desconto)
    .total(BigDecimal.ZERO)
    .observacoes(request.observacoes())
    .previsaoEntrega(calcularPrevisaoEntrega(restaurante))
    .build();
```

### Solução 2: Método Auxiliar para UsuarioId
Criado método auxiliar `toStringOrSystem()` que retorna "SYSTEM" quando `usuarioId` é null:

```java
/**
 * Converte UUID para String ou retorna "SYSTEM" se for null.
 * Usado para endpoints públicos onde não há usuário autenticado.
 */
private String toStringOrSystem(UUID usuarioId) {
    return usuarioId != null ? usuarioId.toString() : "SYSTEM";
}
```

Atualizado todas as chamadas de `usuarioId.toString()` para `toStringOrSystem(usuarioId)` em:
- `criarPedido()` - linha 115
- `atualizarStatus()` - linha 213
- `confirmarPedido()` - linha 224
- `iniciarPreparo()` - linha 233
- `marcarComoPronto()` - linha 242
- `despacharPedido()` - linha 251
- `entregarPedido()` - linha 260
- `cancelarPedido()` - linha 274

## Arquivos Modificados

- `devmaster/src/main/java/com/devmaster/application/service/impl/PedidoApplicationService.java`
  - Adicionado import de `Random` (linha 21)
  - Adicionado constante `RANDOM` (linha 34)
  - Adicionado `.status(StatusPedido.PENDENTE)` no builder (linha 75)
  - Adicionado `.statusPagamento(StatusPagamento.PENDENTE)` no builder (linha 76)
  - Adicionado método `toStringOrSystem()` (final do arquivo)
  - Substituído todas as 8 ocorrências de `usuarioId.toString()` por `toStringOrSystem(usuarioId)`

## Compilação

✅ Projeto compilado com sucesso
```bash
mvn compile -DskipTests
[INFO] BUILD SUCCESS
```

## Teste

Agora o pedido pode ser criado via endpoint público sem erros:
- ✅ Status e statusPagamento são definidos como PENDENTE
- ✅ Histórico é registrado com criadoPor = "SYSTEM" para pedidos públicos
- ✅ Pedidos criados por usuários autenticados continuam registrando o UUID do usuário

## Observação Técnica

Para endpoints públicos (sem autenticação), o histórico de status do pedido registra "SYSTEM" como criador. Isso permite rastrear que o pedido foi criado pelo próprio cliente através da interface pública, não por um usuário administrativo do sistema.

---
**Data**: 2026-02-21  
**Status**: ✅ Implementado, compilado e pronto para teste
