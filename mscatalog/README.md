### Product (Produto Pai / Modelo)

Representa o conceito abstrato do produto, contendo as informações que são comuns a todas as variações.

Exemplo: "Camiseta Básica Hering" ou "Whey Protein Gold Standard".

Atributos: id, name, description, brandId, categoryId, slug (para URL amigável), tags (para busca).

### ProductVariant (SKU - Stock Keeping Unit)
É o item físico vendável. É aqui que o código de barras e o preço real residem.

Exemplo: "Camiseta Hering Branca Tamanho M" ou "Whey Gold Chocolate 900g".

Atributos:
skuCode (Código único, ex: CAM-BR-M).
ean / upc (Código de barras).
price (Preço de venda).
promotionalPrice (Preço "de/por").
Logística (Crítico): weight (peso), height, width, depth (dimensões para cálculo de frete).

### Attribute & AttributeValue (Grade / Especificações)

Diferente de comida, roupas e suplementos têm atributos fixos que geram as variações.

Attribute: Define o tipo (ex: "Cor", "Tamanho", "Sabor", "Voltagem", "Moagem do Café").

AttributeValue: Define o valor (ex: "Azul", "GG", "Baunilha", "220v", "Grão Inteiro").

Relação: O ProductVariant é uma combinação desses valores (ex: Sabor=Baunilha + Peso=900g).

### Brand (Marca)

Muito importante para suplementos e roupas, pois o usuário frequentemente navega por marca.

Atributos: id, name, logoUrl, description.

### Category (Hierarquia de Categorias)
Geralmente em árvore (Tree structure).

Exemplo: Roupas -> Masculino -> Camisetas.

Atributos: id, name, parentId, level.

### Collection (Coleção / Vitrine)
Agrupamentos de marketing que cruzam categorias.

Exemplo: "Ofertas de Verão", "Kit Iniciante Café", "Black Friday".

Atributos: id, name, bannerUrl, startDate, endDate.

## Domain driven design
No pacote domain do projeto mscatalog, os conceitos de Entidade, Value Object e Aggregate do Domain-Driven Design (DDD) são implementados da seguinte forma:

### 1. Entidade (Entity)

Uma Entidade é um objeto que possui uma identidade única que persiste ao longo do tempo, independentemente de seus atributos mudarem. No seu código, as entidades são identificadas pela anotação @Entity e geralmente estendem BaseEntity.

Características no projeto: Possuem um campo id (identidade) e herdam campos de auditoria e multitenancy (tenantId, createdAt) da BaseEntity.

Exemplos:
   - Product: Representa um produto. Mesmo que o nome mude, o id permanece o mesmo.
   - Category: Uma categoria no catálogo.
   - Brand, Attribute, Collection.


### 2. Value Object (Objeto de Valor)

   Um Value Object é um objeto que não possui identidade própria. Ele é definido inteiramente pelos seus atributos. Se dois Value Objects têm os mesmos valores, eles são considerados iguais. Eles costumam ser imutáveis.
   Características no projeto: São marcados com @Embeddable e, em muitos casos, implementados como record para garantir imutabilidade.
   
   Exemplos:
   - Money: Representa um valor monetário (valor + moeda). Não importa "qual" nota de 10 reais você tem, mas sim o valor em si.
   - Dimensions: Representa as medidas físicas (altura, largura, peso).
   - Tags: Uma coleção de etiquetas associadas a um produto.
 

### 3. Aggregate (Agregado)

   Um Agregado é um agrupamento de entidades e objetos de valor que são tratados como uma única unidade para fins de mudança de dados. Cada agregado possui uma Aggregate Root (Raiz do Agregado), que é a única entidade pela qual o mundo externo deve interagir com o grupo.

   Características no projeto: O ciclo de vida dos objetos internos é controlado pela raiz (usando cascade = CascadeType.ALL e orphanRemoval = true).

   Exemplo Principal:
   - Aggregate Root: Product.
   - Membros do Agregado: Product contém uma lista de ProductVariant e o Value Object Tags.
   - Lógica: Quando você salva um Product, suas variantes (ProductVariant) são gerenciadas junto com ele. A integridade do produto e suas variantes é mantida dentro dessa fronteira.


### 4. Resumo Visual no Projeto:
   
   @Entity class Product -> Aggregate Root / Entidade
   
   @Entity class ProductVariant -> Entidade (dentro do agregado de Product)
   
   record Money / record Tags -> Value Objects (embutidos nas entidades)