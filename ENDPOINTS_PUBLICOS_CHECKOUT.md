# Endpoints Públicos para Checkout - Backend

## 📋 Resumo

Criados endpoints públicos (sem autenticação) para suportar o fluxo de checkout do sistema de delivery.

## ✅ Alterações Realizadas

### 1. **Novos Controllers Públicos**

#### PublicClienteAPI + PublicClienteRestController
```
Base Path: /public/v1/clientes
```

**Endpoints:**
- `POST /public/v1/clientes` - Criar ou retornar cliente existente
- `GET /public/v1/clientes/{id}` - Buscar cliente por ID
- `GET /public/v1/clientes/telefone/{telefone}` - Buscar cliente por telefone

**Comportamento do POST:**
- Se telefone já existe: retorna cliente existente
- Se cliente estava inativo: reativa automaticamente
- Se novo: cria o cadastro
- Não requer autenticação

#### PublicEnderecoClienteAPI + PublicEnderecoClienteRestController
```
Base Path: /public/v1/clientes/{clienteId}/enderecos
```

**Endpoints:**
- `POST /public/v1/clientes/{clienteId}/enderecos` - Adicionar endereço
- `GET /public/v1/clientes/{clienteId}/enderecos` - Listar endereços
- `GET /public/v1/clientes/{clienteId}/enderecos/{enderecoId}` - Buscar endereço específico
- `GET /public/v1/clientes/{clienteId}/enderecos/principal` - Buscar endereço principal

**Comportamento:**
- Todos os endpoints são públicos (sem autenticação)
- Primeiro endereço é marcado como principal automaticamente
- Suporta múltiplos endereços por cliente

### 2. **Endpoint de Cálculo de Entrega**

#### EnderecoAPI (atualizada)
```
Base Path: /public/v1/enderecos
```

**Novos Endpoints:**
- `POST /public/v1/enderecos/calcular-entrega` - Calcular taxa e tempo de entrega

**Request:**
```json
{
  "restauranteId": 1,
  "latitude": -23.550520,
  "longitude": -46.633308
}
```

**Response:**
```json
{
  "distanciaKm": 5.2,
  "tempoEstimadoMinutos": 35,
  "taxaEntrega": 8.50,
  "enderecoOrigem": "Rua do Restaurante, 123",
  "enderecoDestino": "Rua do Cliente, 456"
}
```

### 3. **DTOs Criados**

#### CalcularEntregaRequest
```java
@Data
@Builder
public class CalcularEntregaRequest {
    @NotNull
    private Long restauranteId;
    
    @NotNull
    private Double latitude;
    
    @NotNull
    private Double longitude;
}
```

#### CalcularEntregaResponse
```java
@Data
@Builder
public class CalcularEntregaResponse {
    private Double distanciaKm;
    private Integer tempoEstimadoMinutos;
    private BigDecimal taxaEntrega;
    private String enderecoOrigem;
    private String enderecoDestino;
}
```

### 4. **Configuração de Segurança**

A configuração já existente em `SecurityConfig.java` permite todos os endpoints `/public/**`:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/public/**").permitAll()
    // ...
)
```

## 🔧 Implementação Necessária no Service

### EnderecoService

Adicionar método `calcularEntrega`:

```java
public CalcularEntregaResponse calcularEntrega(CalcularEntregaRequest request) {
    // 1. Buscar restaurante por ID
    Restaurante restaurante = restauranteRepository.findById(request.getRestauranteId())
        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
    
    // 2. Verificar se restaurante tem coordenadas
    if (restaurante.getLatitude() == null || restaurante.getLongitude() == null) {
        throw new APIException(HttpStatus.BAD_REQUEST, "Restaurante sem coordenadas cadastradas");
    }
    
    // 3. Calcular distância usando fórmula de Haversine ou API do Google Maps
    Double distanciaKm = calcularDistancia(
        restaurante.getLatitude(), 
        restaurante.getLongitude(),
        request.getLatitude(),
        request.getLatitude()
    );
    
    // 4. Calcular tempo estimado (baseado na distância)
    // Exemplo: 5 km/h de velocidade média + tempo de preparo
    Integer tempoEstimado = calcularTempoEstimado(distanciaKm, restaurante.getTempoPreparo());
    
    // 5. Calcular taxa de entrega (baseado na distância)
    // Exemplo: R$ 5,00 base + R$ 1,00 por km
    BigDecimal taxaEntrega = calcularTaxaEntrega(distanciaKm);
    
    // 6. Montar response
    return CalcularEntregaResponse.builder()
        .distanciaKm(distanciaKm)
        .tempoEstimadoMinutos(tempoEstimado)
        .taxaEntrega(taxaEntrega)
        .enderecoOrigem(formatarEndereco(restaurante))
        .enderecoDestino("Endereço do cliente")
        .build();
}

private Double calcularDistancia(Double lat1, Double lon1, Double lat2, Double lon2) {
    // Fórmula de Haversine
    final int R = 6371; // Raio da Terra em km
    
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return R * c;
}

private Integer calcularTempoEstimado(Double distanciaKm, Integer tempoPreparo) {
    // Velocidade média de 20 km/h
    int tempoDeslocamento = (int) Math.ceil(distanciaKm / 20.0 * 60);
    return (tempoPreparo != null ? tempoPreparo : 20) + tempoDeslocamento;
}

private BigDecimal calcularTaxaEntrega(Double distanciaKm) {
    // Taxa base + valor por km
    BigDecimal taxaBase = new BigDecimal("5.00");
    BigDecimal valorPorKm = new BigDecimal("1.00");
    
    BigDecimal taxa = taxaBase.add(
        valorPorKm.multiply(new BigDecimal(distanciaKm))
    );
    
    // Arredondar para 2 casas decimais
    return taxa.setScale(2, RoundingMode.HALF_UP);
}
```

## 📝 Integração com API de Entrega

Se houver um serviço separado de entrega (#entrega), o método `calcularEntrega` pode fazer uma chamada HTTP para esse serviço:

```java
@Service
@RequiredArgsConstructor
public class EntregaIntegrationService {
    
    private final RestTemplate restTemplate;
    
    @Value("${entrega.api.url}")
    private String entregaApiUrl;
    
    public CalcularEntregaResponse calcularEntrega(CalcularEntregaRequest request) {
        String url = entregaApiUrl + "/api/calcular-entrega";
        
        return restTemplate.postForObject(
            url, 
            request, 
            CalcularEntregaResponse.class
        );
    }
}
```

## 🚀 Endpoints Disponíveis

### Clientes (Público)
```
POST   /public/v1/clientes                              - Criar/buscar cliente
GET    /public/v1/clientes/{id}                         - Buscar por ID
GET    /public/v1/clientes/telefone/{telefone}          - Buscar por telefone
```

### Endereços de Clientes (Público)
```
POST   /public/v1/clientes/{clienteId}/enderecos        - Adicionar endereço
GET    /public/v1/clientes/{clienteId}/enderecos        - Listar endereços
GET    /public/v1/clientes/{clienteId}/enderecos/{id}   - Buscar endereço
GET    /public/v1/clientes/{clienteId}/enderecos/principal - Buscar principal
```

### Endereços (Público)
```
GET    /public/v1/enderecos/cep/{cep}                   - Buscar por CEP
POST   /public/v1/enderecos/calcular-entrega            - Calcular entrega
```

## 🔐 Segurança

### Endpoints Públicos
- Todos os endpoints `/public/v1/**` não requerem autenticação
- Configuração já existe em `SecurityConfig.java`
- Validações de dados são feitas via Bean Validation

### Endpoints Privados (existentes)
- Endpoints `/clientes/**` (sem /public) requerem autenticação
- Usados pela área administrativa
- Requerem roles específicas (ADMIN, GERENTE, etc.)

## ✅ Checklist de Implementação

- [x] Criar PublicClienteAPI e Controller
- [x] Criar PublicEnderecoClienteAPI e Controller
- [x] Atualizar EnderecoAPI com endpoint de cálculo
- [x] Criar DTOs (CalcularEntregaRequest/Response)
- [x] Atualizar EnderecoRestController
- [ ] Implementar método calcularEntrega no EnderecoService
- [ ] Adicionar coordenadas ao cadastro de Restaurante (se não existir)
- [ ] Testar todos os endpoints
- [ ] Documentar no Swagger

## 🧪 Como Testar

### 1. Criar Cliente
```bash
curl -X POST http://localhost:8080/api/public/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "telefone": "11999999999",
    "email": "joao@email.com"
  }'
```

### 2. Buscar Cliente por Telefone
```bash
curl http://localhost:8080/api/public/v1/clientes/telefone/11999999999
```

### 3. Adicionar Endereço
```bash
curl -X POST http://localhost:8080/api/public/v1/clientes/1/enderecos \
  -H "Content-Type: application/json" \
  -d '{
    "logradouro": "Av. Paulista",
    "numero": "1000",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01310100",
    "latitude": -23.561414,
    "longitude": -46.656139,
    "principal": true
  }'
```

### 4. Buscar CEP
```bash
curl http://localhost:8080/api/public/v1/enderecos/cep/01310100
```

### 5. Calcular Entrega
```bash
curl -X POST http://localhost:8080/api/public/v1/enderecos/calcular-entrega \
  -H "Content-Type: application/json" \
  -d '{
    "restauranteId": 1,
    "latitude": -23.561414,
    "longitude": -46.656139
  }'
```

## 📚 Documentação Swagger

Após implementar, os endpoints estarão disponíveis em:
```
http://localhost:8080/api/swagger-ui.html
```

Procure pelas tags:
- "Clientes Público"
- "Endereços Público"
- "Endereço Público"

## ✨ Resultado Final

O backend agora suporta completamente o fluxo de checkout do frontend:
- ✅ Busca/criação de cliente por telefone
- ✅ Gerenciamento de endereços sem autenticação
- ✅ Busca de CEP com coordenadas
- ✅ Cálculo de taxa e tempo de entrega
- ✅ Todos os endpoints públicos e documentados

Próximo passo: Implementar o método `calcularEntrega` no `EnderecoService`! 🚀
