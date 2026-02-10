# Script de Carga de Dados - API Delivery

## 📋 Descrição

Script Python para popular banco de dados PostgreSQL com dados fictícios realistas para testes de API de delivery. Cria 10 tenants diferentes representando diversos tipos de lojas brasileiras.

## 🏪 Tipos de Lojas Criadas

1. **FitStyle Brasil** - Roupas Fitness
2. **Skate Shop Brasil** - Artigos de Skate
3. **Suplementos Max** - Suplementos Alimentares
4. **TechStore Brasil** - Eletrônicos
5. **Petshop Amigo Fiel** - Pet Shop
6. **Livros & Cultura** - Livraria
7. **Gamer Zone** - Produtos para Gamers
8. **Cosméticos Bella** - Cosméticos
9. **Casa & Decoração** - Artigos para Casa
10. **Instrumentos Musicais Pro** - Instrumentos Musicais

## 📊 Dados Gerados

Para cada tenant:
- **6 marcas** reais relacionadas ao segmento
- **Categorias e subcategorias** específicas do tipo de loja
- **Centenas de produtos** com nomes realistas em português
- **Múltiplas variantes** por produto (3-8 variantes)
- **Atributos** como cor, tamanho, sabor, voltagem
- **Preços em Reais (BRL)** com valores realistas
- **30% dos produtos** com preço promocional
- **Tags** para cada produto
- **4 coleções** sazonais

### Volume Total Aproximado:
- ~10 tenants
- ~60 marcas
- ~100 categorias/subcategorias
- ~640 produtos
- ~3200+ variantes de produtos
- ~50 atributos e valores

## 🚀 Como Usar

### 1. Instalar Dependências

```bash
pip install -r requirements.txt
```

### 2. Configurar Conexão com Banco de Dados

Edite as configurações no arquivo `seed_delivery_database.py`:

```python
DB_CONFIG = {
    'host': 'localhost',        # Seu host
    'database': 'delivery_db',  # Nome do seu banco
    'user': 'postgres',         # Seu usuário
    'password': 'postgres',     # Sua senha
    'port': 5432                # Porta do PostgreSQL
}
```

### 3. Executar o Script

```bash
python seed_delivery_database.py
```

## ⚠️ Importante

- O script **limpa todas as tabelas** antes de inserir os dados
- Certifique-se de que as tabelas já foram criadas com o DDL fornecido
- O script usa transações para garantir integridade dos dados
- Todos os produtos têm descrições em português brasileiro

## 📝 Estrutura dos Dados

### Produtos
- Nome: Combinação de marca + categoria + qualificador (Pro, Max, Ultra, etc)
- Descrição: Texto marketing em português
- Slug: Gerado automaticamente a partir do nome
- Tags: Categoria + marca + status (promoção, lançamento, etc)

### Variantes
- SKU único alfanumérico
- Código de barras (EAN-13 brasileiro começando com 789)
- Preço em Reais (R$ 29,90 a R$ 999,90)
- Dimensões e peso
- Atributos específicos do tipo de produto

### Atributos
- **Cor**: Preto, Branco, Azul, Vermelho, etc
- **Tamanho**: PP, P, M, G, GG, XG (para roupas)
- **Sabor**: Chocolate, Baunilha, Morango, etc (para suplementos)
- **Voltagem**: 110V, 220V, Bivolt (para eletrônicos)

## 🔍 Exemplos de Produtos Gerados

**Roupas Fitness:**
- Nike Leggings Pro
- Adidas Shorts Ultra
- Puma Tops Premium

**Suplementos:**
- Optimum Nutrition Whey Protein Max
- Max Titanium BCAA Elite

**Eletrônicos:**
- Samsung Smartphones Plus
- Apple iPhone Premium

**Pet Shop:**
- Pedigree Ração Adulto Premium
- Royal Canin Ração Filhote Pro

## 📈 Saída do Script

O script exibe:
- Progresso de cada tenant sendo populado
- Estatísticas finais de registros criados
- Lista de Tenant IDs gerados para uso nos testes

## 🛠️ Customização

Você pode facilmente customizar:
- Número de produtos por categoria (variável `products_per_category`)
- Número de variantes por produto (função `insert_products_and_variants`)
- Adicionar novos tipos de lojas em `TENANTS_CONFIG`
- Modificar atributos em `COMMON_ATTRIBUTES`

## 📞 Suporte

Para problemas ou dúvidas, verifique:
1. Conexão com banco de dados está correta
2. Tabelas foram criadas com o DDL fornecido
3. Usuário do banco tem permissões de escrita
4. PostgreSQL está rodando e acessível

## 📄 Licença

Livre para uso em projetos de teste e desenvolvimento.