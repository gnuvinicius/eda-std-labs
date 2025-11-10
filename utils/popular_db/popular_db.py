import psycopg2
from faker import Faker
from tqdm import tqdm
import random
from datetime import datetime, timedelta

# Configuração do banco
DB_CONFIG = {
    "dbname": "garage474_db",
    "user": "postgres",
    "password": "2AkByM4NfHFkeJz",
    "host": "localhost",
    "port": 5432,
}

# Quantidade de dados
NUM_USERS = 250_000      # 1 milhão de usuários
NUM_ADDRESSES = NUM_USERS  # 1 endereço por usuário
NUM_SUBSCRIPTIONS = int(NUM_USERS * 0.6)  # 60% dos usuários terão assinatura
BATCH_SIZE = 10_000        # Tamanho do lote de inserção

fake = Faker('pt_BR')

# -------------------------------------------------------
# Funções auxiliares
# -------------------------------------------------------

def create_plans(cur):
    """Cria alguns planos fixos."""
    plans = [
        ('Básico', 'Acesso básico por 30 dias', 29.90, 30),
        ('Padrão', 'Acesso completo por 90 dias', 79.90, 90),
        ('Premium', 'Acesso ilimitado por 365 dias', 249.90, 365),
    ]
    cur.executemany("""
        INSERT INTO plans (name, description, price, durationInDays)
        VALUES (%s, %s, %s, %s)
    """, plans)


def random_cpf():
    """Gera CPF válido (mas fake)."""
    def dv(n):
        s = sum([int(a) * b for a, b in zip(n, range(len(n)+1, 1, -1))])
        d = 11 - s % 11
        return '0' if d > 9 else str(d)

    n = ''.join([str(random.randint(0, 9)) for _ in range(9)])
    return f"{n[:3]}.{n[3:6]}.{n[6:9]}-{dv(n)}{dv(n + dv(n))}"


def generate_users(batch_size):
    """Gera um lote de usuários com nome e e-mail consistentes."""
    users = []
    for _ in range(batch_size):
        full_name = fake.name()
        parts = full_name.split()
        first = parts[0].lower()
        last = parts[-1].lower()

        # cria e-mail a partir do mesmo nome
        email = f"{first}.{last}.{random.randint(1, 10_000_000)}@example.com"

        cpf = random_cpf()
        birth = fake.date_of_birth(minimum_age=18, maximum_age=80)
        phone = fake.phone_number()

        users.append((full_name, email, cpf, birth, phone))
    return users


def generate_addresses(user_ids):
    """Gera endereços para os usuários."""
    addresses = []
    for user_id in user_ids:
        addr = (
            user_id,
            fake.street_name(),
            fake.city(),
            fake.state(),
            fake.postcode(),
            "Brasil"
        )
        addresses.append(addr)
    return addresses


def generate_subscriptions(user_ids, plan_ids):
    """Gera assinaturas com base em usuários e planos."""
    subscriptions = []
    for user_id in random.sample(user_ids, int(len(user_ids) * 0.6)):
        plan_id = random.choice(plan_ids)
        start_date = fake.date_time_between(start_date='-2y', end_date='now')
        duration = random.randint(30, 365)
        end_date = start_date + timedelta(days=duration)
        status = random.choice(['active', 'expired', 'canceled'])
        subscriptions.append((user_id, plan_id, start_date, end_date, status))
    return subscriptions


# -------------------------------------------------------
# Execução principal
# -------------------------------------------------------

def main():
    conn = psycopg2.connect(**DB_CONFIG)
    cur = conn.cursor()

    print("🧩 Inserindo planos...")
    create_plans(cur)
    conn.commit()

    # Obter os IDs de planos
    cur.execute("SELECT id FROM plans;")
    plan_ids = [r[0] for r in cur.fetchall()]

    print(f"👥 Gerando {NUM_USERS:,} usuários e endereços...")

    all_user_ids = []
    for _ in tqdm(range(0, NUM_USERS, BATCH_SIZE)):
        users = generate_users(BATCH_SIZE)
        cur.executemany("""
            INSERT INTO users (name, email, cpf, birthDate, phone)
            VALUES (%s, %s, %s, %s, %s)
        """, users)
        cur.execute("SELECT currval('users_seq')")
        last_id = cur.fetchone()[0]
        first_id = last_id - len(users) + 1
        user_ids = list(range(first_id, last_id + 1))
        all_user_ids.extend(user_ids)

        addresses = generate_addresses(user_ids)
        cur.executemany("""
            INSERT INTO addresses (userId, street, city, state, zipCode, country)
            VALUES (%s, %s, %s, %s, %s, %s)
        """, addresses)

        conn.commit()

    print("💳 Gerando assinaturas...")
    for i in tqdm(range(0, len(all_user_ids), BATCH_SIZE)):
        user_ids_batch = all_user_ids[i:i + BATCH_SIZE]
        subs = generate_subscriptions(user_ids_batch, plan_ids)
        cur.executemany("""
            INSERT INTO subscriptions (userId, planId, startDate, endDate, status)
            VALUES (%s, %s, %s, %s, %s)
        """, subs)
        conn.commit()

    cur.close()
    conn.close()
    print("✅ População concluída com sucesso!")


if __name__ == "__main__":
    main()
