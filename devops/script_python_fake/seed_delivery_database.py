"""
Script para popular banco de dados PostgreSQL com dados fake para testes de API de delivery.
Cria 10 tenants diferentes com produtos, categorias, variantes, etc.
"""

import psycopg2
from psycopg2.extras import execute_values
import uuid
from datetime import datetime, timedelta
import random
from decimal import Decimal

# Configuração da conexão com o banco de dados
DB_CONFIG = {
    'host': 'localhost',
    'database': 'mscatalog_db',
    'user': 'postgres',
    'password': '2AkByM4NfHFkeJz',
    'port': 5432
}

# Definição dos 10 tipos de lojas
TENANTS_CONFIG = [
    {
        'name': 'FitStyle Brasil',
        'type': 'roupas_fitness',
        'brands': ['Nike', 'Adidas', 'Puma', 'Under Armour', 'Olympikus', 'Fila'],
        'categories': {
            'Roupas Femininas': ['Leggings', 'Tops', 'Shorts', 'Calças', 'Macacões'],
            'Roupas Masculinas': ['Camisetas', 'Shorts', 'Calças', 'Regatas', 'Jaquetas'],
            'Acessórios': ['Bonés', 'Meias', 'Luvas', 'Faixas']
        }
    },
    {
        'name': 'Skate Shop Brasil',
        'type': 'skate',
        'brands': ['Element', 'Santa Cruz', 'Vans', 'DC Shoes', 'Flip', 'Almost'],
        'categories': {
            'Shapes': ['Street', 'Cruiser', 'Longboard'],
            'Rodas': ['Street 52mm', 'Street 54mm', 'Cruiser'],
            'Trucks': ['Low', 'Mid', 'High'],
            'Roupas': ['Camisetas', 'Moletons', 'Bonés', 'Tênis']
        }
    },
    {
        'name': 'Suplementos Max',
        'type': 'suplementos',
        'brands': ['Optimum Nutrition', 'Max Titanium', 'Integralmedica', 'Growth', 'Probiotica', 'Atlhetica'],
        'categories': {
            'Proteínas': ['Whey Protein', 'Caseína', 'Albumina', 'Proteína Vegetal'],
            'Pré-Treino': ['Estimulantes', 'Sem Estimulantes', 'Pump'],
            'Aminoácidos': ['BCAA', 'Creatina', 'Glutamina'],
            'Vitaminas': ['Multivitamínicos', 'Ômega 3', 'Vitamina D']
        }
    },
    {
        'name': 'TechStore Brasil',
        'type': 'eletronicos',
        'brands': ['Samsung', 'Apple', 'Xiaomi', 'Motorola', 'LG', 'Sony'],
        'categories': {
            'Smartphones': ['Android', 'iPhone'],
            'Acessórios': ['Fones', 'Capas', 'Carregadores', 'Películas'],
            'Smart Home': ['Lâmpadas', 'Tomadas', 'Câmeras'],
            'Audio': ['Fones Bluetooth', 'Caixas de Som', 'Soundbars']
        }
    },
    {
        'name': 'Petshop Amigo Fiel',
        'type': 'petshop',
        'brands': ['Pedigree', 'Royal Canin', 'Golden', 'Premier', 'Magnus', 'Whiskas'],
        'categories': {
            'Alimentação Cães': ['Ração Filhote', 'Ração Adulto', 'Ração Senior', 'Petiscos'],
            'Alimentação Gatos': ['Ração Filhote', 'Ração Adulto', 'Sachê'],
            'Acessórios': ['Coleiras', 'Comedouros', 'Brinquedos', 'Camas'],
            'Higiene': ['Shampoo', 'Antipulgas', 'Areia Sanitária']
        }
    },
    {
        'name': 'Livros & Cultura',
        'type': 'livraria',
        'brands': ['Companhia das Letras', 'Record', 'Intrinseca', 'Rocco', 'Suma', 'Darkside'],
        'categories': {
            'Ficção': ['Romance', 'Fantasia', 'Suspense', 'Terror'],
            'Não-Ficção': ['Biografia', 'História', 'Ciências', 'Filosofia'],
            'Desenvolvimento': ['Autoajuda', 'Negócios', 'Produtividade'],
            'Infantil': ['0-3 anos', '4-7 anos', '8-12 anos']
        }
    },
    {
        'name': 'Gamer Zone',
        'type': 'games',
        'brands': ['Razer', 'Logitech', 'HyperX', 'Corsair', 'Redragon', 'Dazz'],
        'categories': {
            'Periféricos': ['Teclados', 'Mouses', 'Headsets', 'Mousepad'],
            'Cadeiras': ['Gamer Executiva', 'Gamer Racing', 'Gamer Pro'],
            'Console': ['PlayStation', 'Xbox', 'Nintendo'],
            'PC Gaming': ['Placas de Vídeo', 'Processadores', 'Memória RAM']
        }
    },
    {
        'name': 'Cosméticos Bella',
        'type': 'cosmeticos',
        'brands': ['Natura', 'Boticário', 'Avon', 'Mary Kay', 'Eudora', 'Quem Disse Berenice'],
        'categories': {
            'Maquiagem': ['Base', 'Batom', 'Máscara', 'Sombra', 'Blush'],
            'Cuidados Pele': ['Hidratante', 'Sérum', 'Protetor Solar', 'Limpeza'],
            'Cabelos': ['Shampoo', 'Condicionador', 'Máscara', 'Finalizador'],
            'Perfumaria': ['Feminino', 'Masculino', 'Unissex']
        }
    },
    {
        'name': 'Casa & Decoração',
        'type': 'casa_decoracao',
        'brands': ['Tramontina', 'Brinox', 'Tok&Stok', 'Camicado', 'Santista', 'Buddemeyer'],
        'categories': {
            'Cozinha': ['Panelas', 'Talheres', 'Copos', 'Pratos'],
            'Cama': ['Jogo de Lençol', 'Edredom', 'Travesseiros', 'Cobertor'],
            'Banho': ['Toalhas', 'Tapetes', 'Cortinas'],
            'Decoração': ['Quadros', 'Vasos', 'Almofadas', 'Espelhos']
        }
    },
    {
        'name': 'Instrumentos Musicais Pro',
        'type': 'instrumentos',
        'brands': ['Yamaha', 'Fender', 'Gibson', 'Tagima', 'Giannini', 'Roland'],
        'categories': {
            'Cordas': ['Violões', 'Guitarras', 'Baixos', 'Ukuleles'],
            'Teclas': ['Teclados', 'Pianos', 'Sintetizadores'],
            'Percussão': ['Baterias', 'Cajóns', 'Pandeiros'],
            'Acessórios': ['Cordas', 'Palhetas', 'Cabos', 'Amplificadores']
        }
    }
]

# Atributos comuns para variantes
COMMON_ATTRIBUTES = {
    'Tamanho': ['PP', 'P', 'M', 'G', 'GG', 'XG'],
    'Cor': ['Preto', 'Branco', 'Azul', 'Vermelho', 'Verde', 'Amarelo', 'Rosa', 'Cinza', 'Roxo'],
    'Voltagem': ['110V', '220V', 'Bivolt'],
    'Sabor': ['Chocolate', 'Baunilha', 'Morango', 'Banana', 'Cookies'],
    'Peso': ['1kg', '2kg', '3kg', '900g', '450g']
}


def generate_product_descriptions(product_name, category, tenant_type):
    """Gera descrições realistas para produtos"""
    descriptions = {
        'roupas_fitness': f'{product_name} de alta performance, ideal para treinos intensos. Tecido com tecnologia dry-fit que mantém o corpo seco e confortável.',
        'skate': f'{product_name} profissional para skatistas de todos os níveis. Construção durável e design exclusivo.',
        'suplementos': f'{product_name} de alta qualidade para potencializar seus resultados. Fórmula premium com ingredientes importados.',
        'eletronicos': f'{product_name} com tecnologia de ponta. Design moderno e funcionalidades avançadas para o dia a dia.',
        'petshop': f'{product_name} desenvolvido especialmente para o bem-estar do seu pet. Ingredientes selecionados e nutrição balanceada.',
        'livraria': f'{product_name} - Uma leitura envolvente e transformadora que vai prender sua atenção do início ao fim.',
        'games': f'{product_name} gamer com performance excepcional. RGB personalizável e construção premium.',
        'cosmeticos': f'{product_name} com fórmula exclusiva e fragrância marcante. Testado dermatologicamente.',
        'casa_decoracao': f'{product_name} que combina funcionalidade e design elegante para sua casa.',
        'instrumentos': f'{product_name} com qualidade profissional. Excelente timbre e acabamento impecável.'
    }
    return descriptions.get(tenant_type, f'{product_name} de excelente qualidade.')


def create_connection():
    """Cria conexão com o banco de dados"""
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        return conn
    except Exception as e:
        print(f"Erro ao conectar ao banco: {e}")
        raise


def clear_database(conn):
    """Limpa todas as tabelas antes de popular"""
    cursor = conn.cursor()
    tables = [
        'product_variant_attribute_values',
        'product_tags',
        'product_variant',
        'product',
        'attribute_value',
        'attribute',
        'collection',
        'category',
        'brand'
    ]

    for table in tables:
        cursor.execute(f"DELETE FROM {table}")

    conn.commit()
    cursor.close()
    print("✓ Banco de dados limpo")


def insert_brands(conn, tenant_id, brands):
    """Insere marcas para um tenant"""
    cursor = conn.cursor()
    brand_ids = {}

    for brand_name in brands:
        brand_id = str(uuid.uuid4())
        cursor.execute("""
            INSERT INTO brand (id, tenant_id, name, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s)
        """, (brand_id, str(tenant_id), brand_name, datetime.now(), datetime.now()))
        brand_ids[brand_name] = brand_id

    conn.commit()
    cursor.close()
    return brand_ids


def insert_categories(conn, tenant_id, categories):
    """Insere categorias e subcategorias"""
    cursor = conn.cursor()
    category_ids = {}

    for parent_name, subcategories in categories.items():
        # Insere categoria pai
        parent_id = str(uuid.uuid4())
        cursor.execute("""
            INSERT INTO category (id, tenant_id, name, parent_id, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s)
        """, (parent_id, str(tenant_id), parent_name, None, datetime.now(), datetime.now()))

        category_ids[parent_name] = {'id': parent_id, 'subcategories': {}}

        # Insere subcategorias
        for subcat_name in subcategories:
            subcat_id = str(uuid.uuid4())
            cursor.execute("""
                INSERT INTO category (id, tenant_id, name, parent_id, created_at, updated_at)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (subcat_id, str(tenant_id), subcat_name, parent_id, datetime.now(), datetime.now()))
            category_ids[parent_name]['subcategories'][subcat_name] = subcat_id

    conn.commit()
    cursor.close()
    return category_ids


def insert_attributes(conn, tenant_id):
    """Insere atributos e valores de atributos"""
    cursor = conn.cursor()
    attributes_data = {}

    for attr_name, values in COMMON_ATTRIBUTES.items():
        attr_id = str(uuid.uuid4())
        cursor.execute("""
            INSERT INTO attribute (id, tenant_id, name, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s)
        """, (attr_id, str(tenant_id), attr_name, datetime.now(), datetime.now()))

        attributes_data[attr_name] = {'id': attr_id, 'values': {}}

        for value in values:
            value_id = str(uuid.uuid4())
            cursor.execute("""
                INSERT INTO attribute_value (id, tenant_id, value, attribute_id, created_at, updated_at)
                VALUES (%s, %s, %s, %s, %s, %s)
            """, (value_id, str(tenant_id), value, attr_id, datetime.now(), datetime.now()))
            attributes_data[attr_name]['values'][value] = value_id

    conn.commit()
    cursor.close()
    return attributes_data
    return attributes_data


def generate_slug(name):
    """Gera slug a partir do nome do produto"""
    slug = name.lower()
    slug = slug.replace(' ', '-')
    slug = slug.replace('ã', 'a').replace('á', 'a').replace('à', 'a').replace('â', 'a')
    slug = slug.replace('é', 'e').replace('ê', 'e')
    slug = slug.replace('í', 'i')
    slug = slug.replace('ó', 'o').replace('õ', 'o').replace('ô', 'o')
    slug = slug.replace('ú', 'u').replace('ü', 'u')
    slug = slug.replace('ç', 'c')
    return f"{slug}-{str(uuid.uuid4())[:8]}"


def insert_products_and_variants(conn, tenant_id, tenant_config, brand_ids, category_ids, attributes_data):
    """Insere produtos e suas variantes"""
    cursor = conn.cursor()
    tenant_type = tenant_config['type']

    products_per_category = 8  # Quantidade de produtos por subcategoria
    product_count = 0

    for parent_cat, subcats in tenant_config['categories'].items():
        for subcat_name, subcat_id in category_ids[parent_cat]['subcategories'].items():

            for i in range(products_per_category):
                brand_name = random.choice(tenant_config['brands'])
                brand_id = brand_ids[brand_name]

                # Nome do produto
                product_name = f"{brand_name} {subcat_name} {random.choice(['Pro', 'Premium', 'Elite', 'Max', 'Ultra', 'Plus', 'Advanced', 'Classic'])}"
                description = generate_product_descriptions(product_name, subcat_name, tenant_type)
                slug = generate_slug(product_name)

                product_id = str(uuid.uuid4())
                cursor.execute("""
                    INSERT INTO product (id, tenant_id, name, description, brand_id, category_id, slug, created_at, updated_at)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, (product_id, str(tenant_id), product_name, description, brand_id, subcat_id, slug, datetime.now(), datetime.now()))

                # Tags do produto
                tags = [subcat_name.lower(), brand_name.lower(), random.choice(['promocao', 'lancamento', 'destaque', 'novo'])]
                for tag in tags:
                    cursor.execute("""
                        INSERT INTO product_tags (product_id, tag)
                        VALUES (%s, %s)
                    """, (product_id, tag))

                # Criar variantes do produto
                num_variants = random.randint(3, 8)

                for v in range(num_variants):
                    variant_id = str(uuid.uuid4())
                    sku_code = f"{brand_name[:3].upper()}-{str(uuid.uuid4())[:8].upper()}"
                    barcode = f"789{random.randint(1000000000, 9999999999)}"

                    # Preço base
                    base_price = Decimal(random.uniform(29.90, 999.90))
                    price_amount = round(base_price, 2)

                    # Preço promocional (30% dos produtos)
                    promotional_price = None
                    if random.random() < 0.3:
                        promotional_price = round(price_amount * Decimal(random.uniform(0.7, 0.9)), 2)

                    # Dimensões
                    weight = round(random.uniform(0.1, 5.0), 2)
                    height = round(random.uniform(5.0, 50.0), 2)
                    width = round(random.uniform(5.0, 50.0), 2)
                    depth = round(random.uniform(5.0, 50.0), 2)

                    cursor.execute("""
                        INSERT INTO product_variant (
                            id, tenant_id, sku_code, barcode, price_amount, price_currency,
                            promotional_price_amount, promotional_price_currency,
                            weight, height, width, depth, product_id, created_at, updated_at
                        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                    """, (
                        variant_id, str(tenant_id), sku_code, barcode, price_amount, 'BRL',
                        promotional_price, 'BRL' if promotional_price else None,
                        weight, height, width, depth, product_id, datetime.now(), datetime.now()
                    ))

                    # Associar atributos à variante
                    # Cor (sempre)
                    if 'Cor' in attributes_data:
                        color = random.choice(list(attributes_data['Cor']['values'].keys()))
                        color_value_id = attributes_data['Cor']['values'][color]
                        cursor.execute("""
                            INSERT INTO product_variant_attribute_values (variant_id, attribute_value_id)
                            VALUES (%s, %s)
                        """, (variant_id, color_value_id))

                    # Tamanho (para roupas e alguns outros)
                    if tenant_type in ['roupas_fitness', 'skate'] and 'Tamanho' in attributes_data:
                        size = random.choice(list(attributes_data['Tamanho']['values'].keys()))
                        size_value_id = attributes_data['Tamanho']['values'][size]
                        cursor.execute("""
                            INSERT INTO product_variant_attribute_values (variant_id, attribute_value_id)
                            VALUES (%s, %s)
                        """, (variant_id, size_value_id))

                    # Sabor (para suplementos)
                    if tenant_type == 'suplementos' and 'Sabor' in attributes_data:
                        flavor = random.choice(list(attributes_data['Sabor']['values'].keys()))
                        flavor_value_id = attributes_data['Sabor']['values'][flavor]
                        cursor.execute("""
                            INSERT INTO product_variant_attribute_values (variant_id, attribute_value_id)
                            VALUES (%s, %s)
                        """, (variant_id, flavor_value_id))

                    # Voltagem (para eletrônicos)
                    if tenant_type == 'eletronicos' and 'Voltagem' in attributes_data:
                        voltage = random.choice(list(attributes_data['Voltagem']['values'].keys()))
                        voltage_value_id = attributes_data['Voltagem']['values'][voltage]
                        cursor.execute("""
                            INSERT INTO product_variant_attribute_values (variant_id, attribute_value_id)
                            VALUES (%s, %s)
                        """, (variant_id, voltage_value_id))

                product_count += 1

    conn.commit()
    cursor.close()
    return product_count


def insert_collections(conn, tenant_id):
    """Insere coleções sazonais"""
    cursor = conn.cursor()

    collections = [
        ('Verão 2025', datetime(2024, 12, 1), datetime(2025, 3, 31)),
        ('Inverno 2025', datetime(2025, 6, 1), datetime(2025, 9, 30)),
        ('Black Friday', datetime(2025, 11, 20), datetime(2025, 11, 30)),
        ('Natal 2025', datetime(2025, 12, 1), datetime(2025, 12, 25))
    ]

    for name, start_date, end_date in collections:
        collection_id = str(uuid.uuid4())
        cursor.execute("""
            INSERT INTO collection (id, tenant_id, name, start_date, end_date, created_at, updated_at)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        """, (collection_id, str(tenant_id), name, start_date, end_date, datetime.now(), datetime.now()))

    conn.commit()
    cursor.close()


def populate_tenant(conn, tenant_config):
    """Popula todos os dados para um tenant"""
    tenant_id = str(uuid.uuid4())
    tenant_name = tenant_config['name']

    print(f"\n{'='*60}")
    print(f"Populando tenant: {tenant_name}")
    print(f"Tipo: {tenant_config['type']}")
    print(f"Tenant ID: {tenant_id}")
    print(f"{'='*60}")

    # 1. Inserir marcas
    print("→ Inserindo marcas...", end=" ")
    brand_ids = insert_brands(conn, tenant_id, tenant_config['brands'])
    print(f"✓ {len(brand_ids)} marcas inseridas")

    # 2. Inserir categorias
    print("→ Inserindo categorias...", end=" ")
    category_ids = insert_categories(conn, tenant_id, tenant_config['categories'])
    total_subcats = sum(len(cat['subcategories']) for cat in category_ids.values())
    print(f"✓ {len(category_ids)} categorias e {total_subcats} subcategorias inseridas")

    # 3. Inserir atributos
    print("→ Inserindo atributos...", end=" ")
    attributes_data = insert_attributes(conn, tenant_id)
    total_attr_values = sum(len(attr['values']) for attr in attributes_data.values())
    print(f"✓ {len(attributes_data)} atributos e {total_attr_values} valores inseridos")

    # 4. Inserir produtos e variantes
    print("→ Inserindo produtos e variantes...", end=" ")
    product_count = insert_products_and_variants(conn, tenant_id, tenant_config, brand_ids, category_ids, attributes_data)
    print(f"✓ {product_count} produtos inseridos")

    # 5. Inserir coleções
    print("→ Inserindo coleções...", end=" ")
    insert_collections(conn, tenant_id)
    print("✓ 4 coleções inseridas")

    return tenant_id


def main():
    """Função principal"""
    print("\n" + "="*60)
    print("SCRIPT DE CARGA DE DADOS - API DELIVERY")
    print("="*60)

    try:
        # Conectar ao banco
        print("\n→ Conectando ao banco de dados...", end=" ")
        conn = create_connection()
        print("✓ Conectado")

        # Limpar banco
        print("→ Limpando banco de dados...", end=" ")
        clear_database(conn)

        # Popular cada tenant
        tenant_ids = []
        for i, tenant_config in enumerate(TENANTS_CONFIG, 1):
            tenant_id = populate_tenant(conn, tenant_config)
            tenant_ids.append((tenant_id, tenant_config['name']))

        # Estatísticas finais
        cursor = conn.cursor()
        cursor.execute("SELECT COUNT(*) FROM brand")
        total_brands = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM category")
        total_categories = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM product")
        total_products = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM product_variant")
        total_variants = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM attribute")
        total_attributes = cursor.fetchone()[0]

        cursor.execute("SELECT COUNT(*) FROM collection")
        total_collections = cursor.fetchone()[0]

        cursor.close()

        print("\n" + "="*60)
        print("RESUMO DA CARGA")
        print("="*60)
        print(f"✓ Tenants criados: {len(tenant_ids)}")
        print(f"✓ Total de marcas: {total_brands}")
        print(f"✓ Total de categorias: {total_categories}")
        print(f"✓ Total de produtos: {total_products}")
        print(f"✓ Total de variantes: {total_variants}")
        print(f"✓ Total de atributos: {total_attributes}")
        print(f"✓ Total de coleções: {total_collections}")

        print("\n" + "="*60)
        print("TENANT IDs CRIADOS")
        print("="*60)
        for tenant_id, tenant_name in tenant_ids:
            print(f"{tenant_name}: {tenant_id}")

        print("\n✓ Carga concluída com sucesso!")
        print("="*60 + "\n")

        conn.close()

    except Exception as e:
        print(f"\n✗ Erro durante a execução: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()