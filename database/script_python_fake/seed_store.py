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
        host=os.getenv("PGHOST", "localhost"),
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

                # Brands
                brands = ["IronSkate", "BlackLabel", "RetroShades", "SkateHaus", "VintageThreads"]
                brand_rows = [(str(uuid.uuid4()), b, created, None) for b in brands]
                insert_many(conn,
                            "INSERT INTO brand (id, name, created_at, updated_at) VALUES %s ON CONFLICT DO NOTHING",
                            brand_rows)

                cur.execute("SELECT id, name FROM brand")
                brand_map = {row[1]: row[0] for row in cur.fetchall()}

                # Categories (self-referencing)
                categories = [
                    ("Clothing", None),
                    ("Shirts", "Clothing"),
                    ("Accessories", None),
                    ("Sunglasses", "Accessories"),
                    ("Caps", "Accessories"),
                    ("Skateboards", None),
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

                # Attributes and attribute values
                attributes = {
                    "Size": ["S", "M", "L", "XL"],
                    "Color": ["Black", "White", "Red", "Blue"],
                    "Material": ["Cotton", "Polyester", "Denim"]
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

                # 20 products (masculine store themed)
                product_templates = [
                    ("Classic Band Tee - Metallica", "Official style metallica tee, soft cotton" , "Shirts", "BlackLabel", ["band","metal","tee"]),
                    ("Vintage Rock Tee - The Doors", "Retro fit, screen-printed design", "Shirts", "VintageThreads", ["band","vintage","tee"]),
                    ("Skate Hoodie - IronSkate", "Warm hoodie with skate logo", "Clothing", "IronSkate", ["hoodie","skate"]),
                    ("Retro Sunglasses - Pilot", "UV protection, metal frame", "Sunglasses", "RetroShades", ["sunglasses","retro"]),
                    ("Flat Brim Cap - SkateHaus", "Flat brim cap with embroidered logo", "Caps", "SkateHaus", ["cap","skate"]),
                    ("Denim Jacket - Vintage", "Slim fit denim jacket", "Clothing", "VintageThreads", ["jacket","denim"]),
                    ("Skate Deck - Pro Series", "Maple wood deck, concave pro", "Skateboards", "SkateHaus", ["skateboard","deck"]),
                    ("Sunglasses - Mirrored", "Polarized mirrored lenses", "Sunglasses", "RetroShades", ["sunglasses","polarized"]),
                    ("Bandana - Classic", "Multi-use bandana", "Accessories", "BlackLabel", ["bandana","accessory"]),
                    ("Graphic Tee - Indie Band", "Limited edition indie band tee", "Shirts", "BlackLabel", ["band","indie"]),
                    ("Cargo Shorts - Urban", "Comfort fit cargo shorts", "Clothing", "IronSkate", ["shorts","cargo"]),
                    ("Pilot Shades - Matte", "Matte frame pilot sunglasses", "Sunglasses", "RetroShades", ["sunglasses","matte"]),
                    ("Wool Beanie - Winter", "Warm wool beanie", "Caps", "VintageThreads", ["beanie","winter"]),
                    ("Skate Bearings - Pro", "High speed bearings", "Skateboards", "SkateHaus", ["bearings","skate"]),
                    ("Long Sleeve Tee - Striped", "Striped long sleeve shirt", "Shirts", "VintageThreads", ["longsleeve","striped"]),
                    ("Sunglasses - Aviator", "Classic aviator sunglasses", "Sunglasses", "RetroShades", ["sunglasses","aviator"]),
                    ("Snapback Cap - Logo", "Adjustable snapback with logo", "Caps", "BlackLabel", ["cap","snapback"]),
                    ("Skate Tool - Multi", "3-in-1 skate tool", "Skateboards", "IronSkate", ["tool","skate"]),
                    ("Sunglasses - Clip On", "Clip-on sunglasses for prescription frames", "Sunglasses", "RetroShades", ["sunglasses","clipon"]),
                    ("Band Tee - Classic Rock", "High quality print band tee", "Shirts", "BlackLabel", ["band","classic"]),
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

                # Collections
                collections = [
                    (str(uuid.uuid4()), "Summer Sale", created, None),
                    (str(uuid.uuid4()), "Rock Band Collection", created, None),
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
