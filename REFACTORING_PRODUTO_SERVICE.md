# 🔧 Refatoração: Divisão do ProdutoApplicationService

## 📊 Problema Identificado
- Classe com **~800 linhas**
- **Múltiplas responsabilidades**: Produtos, Imagens, Grupos de Opções, Opções
- Dificulta manutenção e testes
- Viola o **Single Responsibility Principle (SRP)**

## ✅ Solução Proposta

### 📁 Nova Estrutura de Serviços

```
application/service/
├── ProdutoService.java                    # Interface (refatorada)
├── ImagemProdutoService.java              # Interface (nova)
├── GrupoOpcaoService.java                 # Interface (nova)
├── OpcaoService.java                      # Interface (nova)
└── impl/
    ├── ProdutoApplicationService.java     # ~250 linhas (CRUD Produtos)
    ├── ImagemProdutoApplicationService.java # ~150 linhas (Gestão Imagens)
    ├── GrupoOpcaoApplicationService.java   # ~120 linhas (Grupos)
    └── OpcaoApplicationService.java        # ~150 linhas (Opções)
```

## 🎯 Responsabilidades por Serviço

### 1️⃣ **ProdutoApplicationService** (Core)
**Responsabilidade**: CRUD básico de produtos

**Métodos**:
- ✅ `criarProduto()`
- ✅ `buscarProduto()`
- ✅ `listarProdutos()`
- ✅ `listarProdutosComPaginacao()`
- ✅ `atualizarProduto()`
- ✅ `disponibilizarProduto()`
- ✅ `indisponibilizarProduto()`
- ✅ `destacarProduto()`
- ✅ `removerDestaqueProduto()`
- ✅ `removerProduto()`
- ✅ Métodos públicos (sem autenticação)

**Mantém**:
- Métodos auxiliares de construção de responses
- Validações de restaurante/categoria/produto

---

### 2️⃣ **ImagemProdutoApplicationService** (Novo)
**Responsabilidade**: Gestão de imagens dos produtos

**Métodos**:
- `adicionarImagem()`
- `uploadImagem()`
- `listarImagens()`
- `definirImagemPrincipal()`
- `removerImagem()`

**Dependências**:
- `ImagemProdutoRepository`
- `ProdutoRepository` (validação)
- `RestauranteRepository` (validação)

---

### 3️⃣ **GrupoOpcaoApplicationService** (Novo)
**Responsabilidade**: Gestão de grupos de opções

**Métodos**:
- `adicionarGrupoOpcao()`
- `listarGruposOpcoes()`
- `atualizarGrupoOpcao()`
- `removerGrupoOpcao()`

**Dependências**:
- `GrupoOpcaoRepository`
- `OpcaoRepository` (para listar opções do grupo)
- `ProdutoRepository` (validação)
- `RestauranteRepository` (validação)

---

### 4️⃣ **OpcaoApplicationService** (Novo)
**Responsabilidade**: Gestão de opções individuais

**Métodos**:
- `adicionarOpcao()`
- `listarOpcoes()`
- `atualizarOpcao()`
- `disponibilizarOpcao()`
- `indisponibilizarOpcao()`
- `removerOpcao()`

**Dependências**:
- `OpcaoRepository`
- `GrupoOpcaoRepository` (validação)
- `ProdutoRepository` (validação)
- `RestauranteRepository` (validação)

---

## 🔄 Plano de Migração

### Fase 1: Criar Interfaces ✅
- [x] `ImagemProdutoService.java`
- [x] `GrupoOpcaoService.java`
- [x] `OpcaoService.java`
- [x] Refatorar `ProdutoService.java`

### Fase 2: Criar Implementações
- [ ] Extrair código de imagens → `ImagemProdutoApplicationService`
- [ ] Extrair código de grupos → `GrupoOpcaoApplicationService`
- [ ] Extrair código de opções → `OpcaoApplicationService`
- [ ] Limpar `ProdutoApplicationService`

### Fase 3: Atualizar Controllers
- [ ] Injetar novos serviços nos controllers
- [ ] Atualizar chamadas de métodos
- [ ] Remover dependências antigas

### Fase 4: Testes
- [ ] Testar cada serviço isoladamente
- [ ] Testar integração entre serviços
- [ ] Validar endpoints da API

---

## 💡 Benefícios

### ✅ Manutenibilidade
- Cada classe com **responsabilidade única**
- Código mais fácil de entender
- Mudanças isoladas por contexto

### ✅ Testabilidade
- Testes unitários mais simples
- Mocks mais específicos
- Cobertura de testes melhor

### ✅ Escalabilidade
- Fácil adicionar novas funcionalidades
- Serviços independentes
- Possibilidade de extrair para microserviços no futuro

### ✅ Reusabilidade
- Serviços podem ser reutilizados
- Composição de funcionalidades
- Menos duplicação de código

---

## 🚀 Próximos Passos

1. **Revisar e aprovar** esta proposta
2. **Criar branch** `refactor/split-produto-service`
3. **Implementar** Fase 2 (criar serviços)
4. **Atualizar** controllers (Fase 3)
5. **Testar** e validar (Fase 4)
6. **Merge** para develop

---

## 📝 Notas Importantes

- ⚠️ **Não quebra compatibilidade**: Controllers continuam funcionando
- ⚠️ **Migração gradual**: Pode ser feita em etapas
- ⚠️ **Testes existentes**: Devem continuar passando
- ⚠️ **Documentação**: Atualizar Swagger se necessário

---

**Autor**: DevMaster Team  
**Data**: 2025-01-26  
**Status**: ✅ Fase 1 Concluída | 🔄 Aguardando aprovação para Fase 2
