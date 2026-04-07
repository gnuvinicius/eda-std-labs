import os
import uuid
import random
from datetime import datetime
from decimal import Decimal
import psycopg2
from psycopg2.extras import execute_values
from tabulate import tabulate


def get_conn():
    return psycopg2.connect(
        host=os.getenv("PGHOST", "192.168.56.101"),
        port=int(os.getenv("PGPORT", 5432)),
        user=os.getenv("PGUSER", "postgres"),
        password=os.getenv("PGPASSWORD", "2AkByM4NfHFkeJz"),
        dbname=os.getenv("PGDATABASE", "mscatalog_db"),
    )


def now():
    return datetime.utcnow()


def insert_many(conn, query, values):
    with conn.cursor() as cur:
        execute_values(cur, query, values)


def main():
    conn = get_conn()
    try:
        with conn:
            with conn.cursor() as cur:
                created = now()

                # Marcas
                brands = [
                    "Cultura do Vinil",
                    "RiffWear Brasil",
                    "Som Pesado Discos",
                    "Tropicália Store",
                    "Underground Brasil",
                ]
                brand_rows = [(str(uuid.uuid4()), b, created, None) for b in brands]
                insert_many(conn,
                            "INSERT INTO brand (id, name, created_at, updated_at) VALUES %s ON CONFLICT DO NOTHING",
                            brand_rows)

                cur.execute("SELECT id, name FROM brand")
                brand_map = {row[1]: row[0] for row in cur.fetchall()}

                # Categorias (auto-referenciadas)
                categories = [
                    ("Vestuário", None),
                    ("Camisetas", "Vestuário"),
                    ("Moletons", "Vestuário"),
                    ("Jaquetas", "Vestuário"),
                    ("Discos e Mídias", None),
                    ("Vinil", "Discos e Mídias"),
                    ("CD", "Discos e Mídias"),
                    ("Acessórios", None),
                    ("Bonés", "Acessórios"),
                    ("Pôsteres", "Acessórios"),
                ]
                category_ids = {}
                for name, parent in categories:
                    cid = str(uuid.uuid4())
                    pid = category_ids.get(parent)
                    cur.execute(
                        "INSERT INTO category (id, name, parent_id, created_at, updated_at) VALUES (%s,%s,%s,%s,%s) ON CONFLICT DO NOTHING",
                        (cid, name, pid, created, None),
                    )
                    category_ids[name] = cid

                # Atributos e valores de atributos
                attributes = {
                    "Tamanho": ["PP", "P", "M", "G", "GG"],
                    "Cor": ["Preto", "Branco", "Vermelho", "Cinza"],
                    "Formato de Mídia": ['LP 12"', 'LP 7"', "CD", "CD Duplo"],
                }
                attr_map = {}
                attr_val_map = {}
                for aname, vals in attributes.items():
                    aid = str(uuid.uuid4())
                    cur.execute("INSERT INTO attribute (id, name, created_at, updated_at) VALUES (%s,%s,%s,%s) ON CONFLICT DO NOTHING",
                                (aid, aname, created, None))
                    attr_map[aname] = aid
                    for v in vals:
                        avid = str(uuid.uuid4())
                        cur.execute("INSERT INTO attribute_value (id, value, attribute_id, created_at, updated_at) VALUES (%s,%s,%s,%s,%s) ON CONFLICT DO NOTHING",
                                    (avid, v, aid, created, None))
                        attr_val_map.setdefault(aname, []).append(avid)

                # 30 produtos — loja de vestuário e discos de rock nacional e internacional
                product_templates = [
                    # ── Camisetas ─────────────────────────────────────────────────────────
                    ("Camiseta Iron Maiden - The Trooper",
                     "Camiseta 100% algodão com estampa oficial The Trooper",
                     "Camisetas", "RiffWear Brasil",
                     ["iron-maiden", "camiseta", "heavy-metal", "internacional"]),

                    ("Camiseta Metallica - Master of Puppets",
                     "Camiseta com arte do álbum Master of Puppets, corte regular",
                     "Camisetas", "RiffWear Brasil",
                     ["metallica", "camiseta", "heavy-metal", "internacional"]),

                    ("Camiseta Slipknot - Iowa",
                     "Camiseta preta com logo e arte do álbum Iowa",
                     "Camisetas", "Som Pesado Discos",
                     ["slipknot", "camiseta", "nu-metal", "internacional"]),

                    ("Camiseta Legião Urbana - Dois",
                     "Camiseta com arte do icônico álbum Dois",
                     "Camisetas", "Tropicália Store",
                     ["legiao-urbana", "camiseta", "rock-nacional", "brasil"]),

                    ("Camiseta Raul Seixas - Maluco Beleza",
                     "Camiseta vintage com estampa Maluco Beleza, algodão penteado",
                     "Camisetas", "Tropicália Store",
                     ["raul-seixas", "camiseta", "rock-nacional", "brasil"]),

                    ("Camiseta Dead Fish - À Deriva",
                     "Camiseta com arte do álbum À Deriva, corte slim",
                     "Camisetas", "Underground Brasil",
                     ["dead-fish", "camiseta", "hardcore", "brasil"]),

                    ("Camiseta Cazuza - O Tempo Não Para",
                     "Camiseta com estampa da turnê O Tempo Não Para",
                     "Camisetas", "Tropicália Store",
                     ["cazuza", "camiseta", "rock-nacional", "brasil"]),

                    ("Camiseta Sepultura - Roots",
                     "Camiseta com artwork do álbum Roots, 100% algodão",
                     "Camisetas", "Som Pesado Discos",
                     ["sepultura", "camiseta", "metal", "brasil"]),

                    ("Camiseta Belchior - Alucinação",
                     "Camiseta com arte da capa do álbum Alucinação",
                     "Camisetas", "Tropicália Store",
                     ["belchior", "camiseta", "mpb", "brasil"]),

                    # ── Moletons ──────────────────────────────────────────────────────────
                    ("Moletom Metallica - The Black Album",
                     "Moletom com capuz e logo do The Black Album, flanela interna",
                     "Moletons", "RiffWear Brasil",
                     ["metallica", "moletom", "heavy-metal", "internacional"]),

                    ("Moletom Slipknot - Nonuple S",
                     "Moletom pesado com logo clássico Nonuple S do Slipknot",
                     "Moletons", "Som Pesado Discos",
                     ["slipknot", "moletom", "nu-metal", "internacional"]),

                    ("Moletom Legião Urbana - Que País É Este",
                     "Moletom canguru com estampa Que País É Este, tecido grosso",
                     "Moletons", "Tropicália Store",
                     ["legiao-urbana", "moletom", "rock-nacional", "brasil"]),

                    # ── Jaquetas ──────────────────────────────────────────────────────────
                    ("Jaqueta Jeans Cazuza - Exagerado",
                     "Jaqueta jeans com patch bordado Cazuza, estilo anos 80",
                     "Jaquetas", "Tropicália Store",
                     ["cazuza", "jaqueta", "rock-nacional", "brasil"]),

                    ("Jaqueta Iron Maiden - Eddie Tour",
                     "Jaqueta bomber preta com patches bordados da turnê de Iron Maiden",
                     "Jaquetas", "RiffWear Brasil",
                     ["iron-maiden", "jaqueta", "heavy-metal", "internacional"]),

                    # ── Vinil ─────────────────────────────────────────────────────────────
                    ("Vinil Iron Maiden - The Number of the Beast",
                     "LP 12'' remasterizado, edição comemorativa 40 anos, 180g",
                     "Vinil", "Cultura do Vinil",
                     ["iron-maiden", "vinil", "heavy-metal", "internacional"]),

                    ("Vinil Metallica - Master of Puppets",
                     "LP 12'' edição especial 180g com encarte ampliado",
                     "Vinil", "Cultura do Vinil",
                     ["metallica", "vinil", "heavy-metal", "internacional"]),

                    ("Vinil Raul Seixas - Krig-ha, Bandolo!",
                     "LP 12'' reedição comemorativa 50 anos, prensagem brasileira",
                     "Vinil", "Cultura do Vinil",
                     ["raul-seixas", "vinil", "rock-nacional", "brasil"]),

                    ("Vinil Belchior - Alucinação",
                     "LP 12'' edição limitada remasterizada, 1976",
                     "Vinil", "Tropicália Store",
                     ["belchior", "vinil", "mpb", "brasil"]),

                    ("Vinil Caetano Veloso - Transa",
                     "LP 12'' edição remasterizada, gravado em Londres, 1972",
                     "Vinil", "Tropicália Store",
                     ["caetano-veloso", "vinil", "tropicalia", "brasil"]),

                    ("Vinil Sepultura - Roots",
                     "LP 12'' edição especial 180g com gatefold sleeve",
                     "Vinil", "Som Pesado Discos",
                     ["sepultura", "vinil", "metal", "brasil"]),

                    ("Vinil Os Mutantes - Os Mutantes",
                     "LP 12'' reedição do clássico psicodélico, 1968, prensagem colorida",
                     "Vinil", "Tropicália Store",
                     ["os-mutantes", "vinil", "tropicalia", "brasil"]),

                    ("Vinil Legião Urbana - Dois",
                     "LP 12'' reedição especial com livreto de letras, 180g",
                     "Vinil", "Cultura do Vinil",
                     ["legiao-urbana", "vinil", "rock-nacional", "brasil"]),

                    ("Vinil Cazuza - Exagerado",
                     "LP 12'' edição comemorativa com fotos inéditas",
                     "Vinil", "Cultura do Vinil",
                     ["cazuza", "vinil", "rock-nacional", "brasil"]),

                    # ── CD ────────────────────────────────────────────────────────────────
                    ("CD Slipknot - Iowa",
                     "CD edição deluxe com DVD ao vivo em São Paulo",
                     "CD", "Som Pesado Discos",
                     ["slipknot", "cd", "nu-metal", "internacional"]),

                    ("CD Metallica - Metallica (The Black Album)",
                     "CD remasterizado em 2021 com faixas bônus",
                     "CD", "Cultura do Vinil",
                     ["metallica", "cd", "heavy-metal", "internacional"]),

                    ("CD Cazuza - Exagerado",
                     "CD remasterizado com faixas bônus exclusivas e encarte completo",
                     "CD", "Cultura do Vinil",
                     ["cazuza", "cd", "rock-nacional", "brasil"]),

                    ("CD Legião Urbana - Dois",
                     "CD edição especial com encarte ampliado e fotos da banda",
                     "CD", "Tropicália Store",
                     ["legiao-urbana", "cd", "rock-nacional", "brasil"]),

                    ("CD Dead Fish - À Deriva",
                     "CD com encarte booklet e letra das músicas",
                     "CD", "Underground Brasil",
                     ["dead-fish", "cd", "hardcore", "brasil"]),

                    # ── Acessórios ────────────────────────────────────────────────────────
                    ("Boné Dead Fish - Logo Bordado",
                     "Boné preto aba reta com logo bordado Dead Fish, ajuste snapback",
                     "Bonés", "Underground Brasil",
                     ["dead-fish", "bone", "hardcore", "brasil"]),

                    ("Pôster Iron Maiden - Donnie Trooper Vintage",
                     "Pôster 60x90cm papel couché 180g, arte oficial da turnê",
                     "Pôsteres", "RiffWear Brasil",
                     ["iron-maiden", "poster", "heavy-metal", "internacional"]),
                ]

                product_rows = []
                product_tag_rows = []
                variant_rows = []
                variant_attr_rows = []

                for tpl in product_templates:
                    name, desc, cat_name, brand_name, tags = tpl
                    pid = str(uuid.uuid4())
                    slug = name.lower().replace(" ", "-").replace("/", "-")[:250]
                    bid = brand_map.get(brand_name) or str(uuid.uuid4())
                    cid = category_ids.get(cat_name) or list(category_ids.values())[0]
                    product_rows.append((pid, name, desc, bid, cid, slug, created, None))
                    for t in tags:
                        product_tag_rows.append((pid, t))

                    # variant
                    vid = str(uuid.uuid4())
                    sku = f"SKU-{str(uuid.uuid4())[:8].upper()}"
                    barcode = f"{random.randint(100000000000,999999999999)}"
                    price = Decimal(random.choice([49.90, 79.90, 99.90, 129.90, 199.90]))
                    currency = "BRL"
                    variant_rows.append((vid, sku, barcode, price, currency, None, None, None, None, pid, created, None))

                    # assign some attribute values randomly
                    for aname in attr_val_map:
                        avid = random.choice(attr_val_map[aname])
                        variant_attr_rows.append((vid, avid))

                insert_many(conn,
                            "INSERT INTO product (id, name, description, brand_id, category_id, slug, created_at, updated_at) VALUES %s ON CONFLICT DO NOTHING",
                            product_rows)

                insert_many(conn,
                            "INSERT INTO product_tags (product_id, tag) VALUES %s ON CONFLICT DO NOTHING",
                            product_tag_rows)

                insert_many(conn,
                            "INSERT INTO product_variant (id, sku_code, barcode, price_amount, price_currency, promotional_price_amount, promotional_price_currency, width, height, product_id, created_at, updated_at) VALUES %s ON CONFLICT DO NOTHING",
                            variant_rows)

                insert_many(conn,
                            "INSERT INTO product_variant_attribute_values (variant_id, attribute_value_id) VALUES %s ON CONFLICT DO NOTHING",
                            variant_attr_rows)

                # Coleções
                collections = [
                    (str(uuid.uuid4()), "Rock Nacional Clássico", created, None),
                    (str(uuid.uuid4()), "Heavy Metal Internacional", created, None),
                ]
                insert_many(conn, "INSERT INTO collection (id, name, start_date, end_date, created_at, updated_at) VALUES %s ON CONFLICT DO NOTHING",
                            [(c[0], c[1], None, None, c[2], c[3]) for c in collections])

                # Link half products to first collection, some to second
                cur.execute("SELECT id FROM product")
                all_pids = [r[0] for r in cur.fetchall()]
                random.shuffle(all_pids)
                coll_prod_rows = []
                for i, pid in enumerate(all_pids):
                    if i < len(all_pids)//2:
                        coll_prod_rows.append((collections[0][0], pid))
                    if i % 5 == 0:
                        coll_prod_rows.append((collections[1][0], pid))

                insert_many(conn, "INSERT INTO collection_products (collection_id, product_id) VALUES %s ON CONFLICT DO NOTHING",
                            coll_prod_rows)

                # Summary print
                cur.execute("SELECT COUNT(*) FROM product")
                pcount = cur.fetchone()[0]
                cur.execute("SELECT COUNT(*) FROM product_variant")
                vcount = cur.fetchone()[0]
                cur.execute("SELECT COUNT(*) FROM brand")
                bcount = cur.fetchone()[0]
                print(tabulate([[bcount, pcount, vcount]], headers=["brands","products","variants"]))

        print("Seeding completo.")
    finally:
        conn.close()


if __name__ == '__main__':
    main()
