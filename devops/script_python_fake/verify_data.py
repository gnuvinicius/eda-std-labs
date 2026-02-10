"""
Script para verificar os dados carregados no banco de dados
"""

import psycopg2
from tabulate import tabulate

# Configuração da conexão com o banco de dados
DB_CONFIG = {
    'host': 'localhost',
    'database': 'delivery_db',
    'user': 'postgres',
    'password': 'postgres',
    'port': 5432
}


def create_connection():
    """Cria conexão com o banco de dados"""
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        return conn
    except Exception as e:
        print(f"Erro ao conectar ao banco: {e}")
        raise


def get_tenants_summary(conn):
    """Obtém resumo por tenant"""
    cursor = conn.cursor()

    query = """
            SELECT
                t.tenant_id,
                COUNT(DISTINCT b.id) as total_brands,
                COUNT(DISTINCT c.id) as total_categories,
                COUNT(DISTINCT p.id) as total_products,
                COUNT(DISTINCT pv.id) as total_variants,
                MIN(pv.price_amount) as min_price,
                MAX(pv.price_amount) as max_price,
                AVG(pv.price_amount) as avg_price
            FROM (
                     SELECT DISTINCT tenant_id FROM brand
                 ) t
                     LEFT JOIN brand b ON t.tenant_id = b.tenant_id
                     LEFT JOIN category c ON t.tenant_id = c.tenant_id
                     LEFT JOIN productEntity p ON t.tenant_id = p.tenant_id
                     LEFT JOIN product_variant pv ON t.tenant_id = pv.tenant_id
            GROUP BY t.tenant_id
            ORDER BY total_products DESC \
            """

    cursor.execute(query)
    results = cursor.fetchall()
    cursor.close()

    return results


def get_products_by_category(conn, tenant_id=None):
    """Obtém quantidade de produtos por categoria"""
    cursor = conn.cursor()

    if tenant_id:
        query = """
                SELECT
                    c.name as category,
                    COUNT(p.id) as total_products
                FROM category c
                         LEFT JOIN productEntity p ON c.id = p.category_id
                WHERE c.tenant_id = %s AND c.parent_id IS NOT NULL
                GROUP BY c.name
                ORDER BY total_products DESC
                    LIMIT 10 \
                """
        cursor.execute(query, (tenant_id,))
    else:
        query = """
                SELECT
                    c.name as category,
                    COUNT(p.id) as total_products
                FROM category c
                         LEFT JOIN productEntity p ON c.id = p.category_id
                WHERE c.parent_id IS NOT NULL
                GROUP BY c.name
                ORDER BY total_products DESC
                    LIMIT 20 \
                """
        cursor.execute(query)

    results = cursor.fetchall()
    cursor.close()

    return results


def get_sample_products(conn, limit=10):
    """Obtém amostra de produtos com suas variantes"""
    cursor = conn.cursor()

    query = """
            SELECT
                p.name,
                b.name as brand,
                c.name as category,
                COUNT(pv.id) as variants,
                MIN(pv.price_amount) as min_price,
                MAX(pv.price_amount) as max_price
            FROM productEntity p
                     JOIN brand b ON p.brand_id = b.id
                     JOIN category c ON p.category_id = c.id
                     LEFT JOIN product_variant pv ON p.id = pv.product_id
            GROUP BY p.id, p.name, b.name, c.name
            ORDER BY RANDOM()
                LIMIT %s \
            """

    cursor.execute(query, (limit,))
    results = cursor.fetchall()
    cursor.close()

    return results


def get_variants_with_attributes(conn, product_name=None):
    """Obtém variantes com seus atributos"""
    cursor = conn.cursor()

    if product_name:
        query = """
                SELECT
                    p.name as product,
                    pv.sku_code,
                    pv.price_amount,
                    pv.promotional_price_amount,
                    STRING_AGG(CONCAT(a.name, ': ', av.value), ', ') as attributes
                FROM product_variant pv
                         JOIN productEntity p ON pv.product_id = p.id
                         LEFT JOIN product_variant_attribute_values pvav ON pv.id = pvav.variant_id
                         LEFT JOIN attribute_value av ON pvav.attribute_value_id = av.id
                         LEFT JOIN attribute a ON av.attribute_id = a.id
                WHERE p.name ILIKE %s
                GROUP BY p.name, pv.id, pv.sku_code, pv.price_amount, pv.promotional_price_amount
                ORDER BY pv.price_amount \
                """
        cursor.execute(query, (f'%{product_name}%',))
    else:
        query = """
                SELECT
                    p.name as product,
                    pv.sku_code,
                    pv.price_amount,
                    pv.promotional_price_amount,
                    STRING_AGG(CONCAT(a.name, ': ', av.value), ', ') as attributes
                FROM product_variant pv
                         JOIN productEntity p ON pv.product_id = p.id
                         LEFT JOIN product_variant_attribute_values pvav ON pv.id = pvav.variant_id
                         LEFT JOIN attribute_value av ON pvav.attribute_value_id = av.id
                         LEFT JOIN attribute a ON av.attribute_id = a.id
                GROUP BY p.name, pv.id, pv.sku_code, pv.price_amount, pv.promotional_price_amount
                ORDER BY RANDOM()
                    LIMIT 15 \
                """
        cursor.execute(query)

    results = cursor.fetchall()
    cursor.close()

    return results


def get_price_ranges(conn):
    """Obtém distribuição de preços"""
    cursor = conn.cursor()

    query = """
            SELECT
                CASE
                    WHEN price_amount < 50 THEN 'R$ 0-50'
                    WHEN price_amount < 100 THEN 'R$ 50-100'
                    WHEN price_amount < 200 THEN 'R$ 100-200'
                    WHEN price_amount < 500 THEN 'R$ 200-500'
                    ELSE 'R$ 500+'
                    END as price_range,
                COUNT(*) as total_variants,
                ROUND(AVG(price_amount), 2) as avg_price
            FROM product_variant
            GROUP BY
                CASE
                    WHEN price_amount < 50 THEN 'R$ 0-50'
                    WHEN price_amount < 100 THEN 'R$ 50-100'
                    WHEN price_amount < 200 THEN 'R$ 100-200'
                    WHEN price_amount < 500 THEN 'R$ 200-500'
                    ELSE 'R$ 500+'
                    END
            ORDER BY
                CASE
                    WHEN price_amount < 50 THEN 1
                    WHEN price_amount < 100 THEN 2
                    WHEN price_amount < 200 THEN 3
                    WHEN price_amount < 500 THEN 4
                    ELSE 5
                    END \
            """

    cursor.execute(query)
    results = cursor.fetchall()
    cursor.close()

    return results


def get_promotional_stats(conn):
    """Estatísticas de produtos em promoção"""
    cursor = conn.cursor()

    query = """
            SELECT
                COUNT(*) FILTER (WHERE promotional_price_amount IS NOT NULL) as with_promo,
                COUNT(*) FILTER (WHERE promotional_price_amount IS NULL) as without_promo,
                ROUND(
                        100.0 * COUNT(*) FILTER (WHERE promotional_price_amount IS NOT NULL) / COUNT(*),
                        2
                ) as promo_percentage
            FROM product_variant \
            """

    cursor.execute(query)
    result = cursor.fetchone()
    cursor.close()

    return result


def main():
    """Função principal"""
    print("\n" + "="*80)
    print("VERIFICAÇÃO DOS DADOS - API DELIVERY")
    print("="*80)

    try:
        conn = create_connection()

        # 1. Resumo Geral por Tenant
        print("\n📊 RESUMO POR TENANT")
        print("-"*80)
        tenants = get_tenants_summary(conn)
        headers = ['Tenant ID', 'Marcas', 'Categorias', 'Produtos', 'Variantes', 'Preço Mín', 'Preço Máx', 'Preço Médio']

        formatted_data = []
        for row in tenants:
            formatted_row = list(row[:5])  # tenant_id, brands, categories, products, variants
            formatted_row.append(f"R$ {row[5]:.2f}")  # min_price
            formatted_row.append(f"R$ {row[6]:.2f}")  # max_price
            formatted_row.append(f"R$ {row[7]:.2f}")  # avg_price
            formatted_data.append(formatted_row)

        print(tabulate(formatted_data, headers=headers, tablefmt='grid'))

        # 2. Distribuição de Preços
        print("\n💰 DISTRIBUIÇÃO DE PREÇOS")
        print("-"*80)
        price_ranges = get_price_ranges(conn)
        headers = ['Faixa de Preço', 'Total Variantes', 'Preço Médio']

        formatted_prices = []
        for row in price_ranges:
            formatted_prices.append([row[0], row[1], f"R$ {row[2]:.2f}"])

        print(tabulate(formatted_prices, headers=headers, tablefmt='grid'))

        # 3. Estatísticas de Promoção
        print("\n🏷️  ESTATÍSTICAS DE PROMOÇÃO")
        print("-"*80)
        promo_stats = get_promotional_stats(conn)
        print(f"Variantes com promoção: {promo_stats[0]}")
        print(f"Variantes sem promoção: {promo_stats[1]}")
        print(f"Percentual em promoção: {promo_stats[2]}%")

        # 4. Top Categorias
        print("\n📦 TOP 20 CATEGORIAS COM MAIS PRODUTOS")
        print("-"*80)
        categories = get_products_by_category(conn)
        headers = ['Categoria', 'Total Produtos']
        print(tabulate(categories, headers=headers, tablefmt='grid'))

        # 5. Amostra de Produtos
        print("\n🛍️  AMOSTRA DE PRODUTOS (10 aleatórios)")
        print("-"*80)
        products = get_sample_products(conn, 10)
        headers = ['Produto', 'Marca', 'Categoria', 'Variantes', 'Preço Mín', 'Preço Máx']

        formatted_products = []
        for row in products:
            formatted_row = list(row[:4])
            formatted_row.append(f"R$ {row[4]:.2f}")
            formatted_row.append(f"R$ {row[5]:.2f}")
            formatted_products.append(formatted_row)

        print(tabulate(formatted_products, headers=headers, tablefmt='grid'))

        # 6. Variantes com Atributos
        print("\n🏷️  VARIANTES COM ATRIBUTOS (15 aleatórias)")
        print("-"*80)
        variants = get_variants_with_attributes(conn)
        headers = ['Produto', 'SKU', 'Preço', 'Promoção', 'Atributos']

        formatted_variants = []
        for row in variants:
            formatted_row = [
                row[0][:40] + '...' if len(row[0]) > 40 else row[0],
                row[1],
                f"R$ {row[2]:.2f}",
                f"R$ {row[3]:.2f}" if row[3] else '-',
                row[4] if row[4] else 'Sem atributos'
            ]
            formatted_variants.append(formatted_row)

        print(tabulate(formatted_variants, headers=headers, tablefmt='grid'))

        print("\n" + "="*80)
        print("✓ Verificação concluída!")
        print("="*80 + "\n")

        conn.close()

    except Exception as e:
        print(f"\n✗ Erro durante a verificação: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()