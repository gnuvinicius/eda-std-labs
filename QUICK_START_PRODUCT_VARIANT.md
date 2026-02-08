# 🚀 QUICK START - PRODUCT VARIANT CRUD

## ⚡ 5 Minutos para Começar

### **Passo 1: Compilar o Projeto**
```bash
cd mscatalog
./mvnw clean package -DskipTests
```

### **Passo 2: Iniciar a Aplicação**
```bash
./mvnw spring-boot:run
```

A aplicação estará rodando em `http://localhost:8080`

---

## 🧪 Teste os Endpoints

### **1️⃣ Listar Produtos (para obter o ID)**
```bash
curl -X GET http://localhost:8080/api/v1/products \
  -H "X-Tenant-ID: 550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json"
```

**Copie o `id` do produto retornado**

---

### **2️⃣ Criar uma Variante** (POST)
```bash
curl -X POST http://localhost:8080/api/v1/products/{productId}/variants \
  -H "Content-Type: application/json" \
  -d '{
    "skuCode": "PROD-001",
    "barcode": "1234567890123",
    "price": 99.99,
    "priceCurrency": "BRL",
    "promotionalPrice": 79.99,
    "promotionalPriceCurrency": "BRL",
    "weight": 0.5,
    "height": 10.0,
    "width": 5.0,
    "depth": 3.0
  }'
```

**Status Esperado: 201 Created**
**Copie o `id` da resposta para usar depois**

---

### **3️⃣ Listar Variantes** (GET)
```bash
curl -X GET http://localhost:8080/api/v1/products/{productId}/variants \
  -H "Content-Type: application/json"
```

**Status Esperado: 200 OK**

---

### **4️⃣ Atualizar Variante** (PUT)
```bash
curl -X PUT http://localhost:8080/api/v1/products/{productId}/variants/{variantId} \
  -H "Content-Type: application/json" \
  -d '{
    "skuCode": "PROD-001-UPDATED",
    "barcode": "1234567890123",
    "price": 89.99,
    "priceCurrency": "BRL",
    "promotionalPrice": 69.99,
    "promotionalPriceCurrency": "BRL",
    "weight": 0.5,
    "height": 10.0,
    "width": 5.0,
    "depth": 3.0
  }'
```

**Status Esperado: 200 OK**

---

### **5️⃣ Deletar Variante** (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/v1/products/{productId}/variants/{variantId} \
  -H "Content-Type: application/json"
```

**Status Esperado: 204 No Content**

---

## 📋 Validações Testadas Automaticamente

### ✅ SKU Code
```bash
# ❌ Falha: SKU vazio
"skuCode": ""

# ❌ Falha: SKU com 2 caracteres (mínimo 3)
"skuCode": "AB"

# ❌ Falha: SKU com caracteres inválidos (deve ser maiúsculo)
"skuCode": "prod-001"

# ✅ Sucesso: SKU válido
"skuCode": "PROD-001"
```

### ✅ Preço
```bash
# ❌ Falha: Preço zero
"price": 0

# ❌ Falha: Preço negativo
"price": -99.99

# ❌ Falha: Preço promocional > preço regular
"price": 99.99,
"promotionalPrice": 199.99

# ✅ Sucesso: Preço válido
"price": 99.99,
"promotionalPrice": 79.99
```

### ✅ Dimensões
```bash
# ❌ Falha: Peso negativo
"weight": -0.5

# ✅ Sucesso: Dimensões válidas
"weight": 0.5,
"height": 10.0,
"width": 5.0,
"depth": 3.0
```

---

## 🧪 Executar Testes

```bash
# Todos os testes
./mvnw clean test

# Apenas ProductVariantService
./mvnw clean test -Dtest=ProductVariantServiceTest

# Apenas CreateProductVariantUseCase
./mvnw clean test -Dtest=CreateProductVariantUseCaseTest

# Apenas Controller
./mvnw clean test -Dtest=ProductControllerVariantTest
```

---

## 🔍 Troubleshooting

### ❌ `Produto não encontrado`
```
Erro: "Produto não encontrado: {productId}"

Solução:
1. Certifique-se de que o {productId} está correto
2. Verifique se o produto foi realmente criado
3. Use GET /api/v1/products para listar
```

### ❌ `SKU Code já existe no catálogo`
```
Erro: "SKU Code já existe no catálogo: PROD-001"

Solução:
1. Use um SKU diferente (ex: PROD-002)
2. Ou delete a variante existente e recrie
```

### ❌ `Preço deve ser maior que zero`
```
Erro: "Preço deve ser maior que zero"

Solução:
1. Verifique se price > 0
2. Não use valores zero ou negativos
```

### ❌ `Status 500 Internal Server Error`
```
Solução:
1. Verifique os logs da aplicação
2. Certifique-se de que o banco de dados está rodando
3. Reinicie a aplicação
```

---

## 📚 Estrutura do Projeto

```
mscatalog/
├── src/
│   ├── main/java/br/dev/garage474/mscatalog/
│   │   ├── adapter/
│   │   │   ├── in/web/controller/
│   │   │   │   └── ProductController.java ⭐
│   │   │   ├── in/web/dto/
│   │   │   │   ├── CreateProductVariantRequest.java ⭐
│   │   │   │   └── ProductVariantResponse.java ⭐
│   │   │   └── out/persistence/
│   │   ├── application/
│   │   │   └── usecase/
│   │   │       ├── CreateProductVariantUseCase.java ⭐
│   │   │       ├── ListProductVariantsByProductUseCase.java ⭐
│   │   │       ├── UpdateProductVariantUseCase.java ⭐
│   │   │       └── DeleteProductVariantUseCase.java ⭐
│   │   └── domain/
│   │       ├── service/
│   │       │   └── ProductVariantService.java ⭐
│   │       └── repository/
│   │           └── ProductRepository.java (interface)
│   └── test/java/.../
│       ├── CreateProductVariantUseCaseTest.java ⭐
│       ├── ProductVariantServiceTest.java ⭐
│       └── ProductControllerVariantTest.java ⭐
```

⭐ = Novos arquivos ou modificações

---

## 📞 Documentação Adicional

- 📖 `PRODUCT_VARIANT_IMPLEMENTATION.md` - Documentação técnica completa
- 📊 `RESUMO_EXECUTIVO_PRODUCT_VARIANT.md` - Resumo executivo

---

## ✅ Checklist de Verificação

- [ ] Projeto compila sem erros
- [ ] Testes passam
- [ ] Aplicação inicia
- [ ] Endpoint POST funciona (201 Created)
- [ ] Endpoint GET funciona (200 OK)
- [ ] Endpoint PUT funciona (200 OK)
- [ ] Endpoint DELETE funciona (204 No Content)
- [ ] Validações funcionam corretamente
- [ ] Banco de dados persist os dados

---

## 🎉 Pronto!

Você agora tem um CRUD completo de ProductVariant funcionando com:
- ✅ 4 Endpoints REST
- ✅ Validações de negócio
- ✅ Testes unitários
- ✅ Documentação completa
- ✅ Clean Architecture
- ✅ DDD

**Bom desenvolvimento! 🚀**

