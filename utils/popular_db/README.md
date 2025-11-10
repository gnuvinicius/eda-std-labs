Perfeito 👏 — vamos montar **um passo a passo completo** para criar o ambiente, configurar o PostgreSQL e rodar o script Python que popula o banco.

Vou mostrar o processo em três partes:

1 riar o ambiente Python
2 rodar o script de popular dados

---

## 1. Criar o ambiente Python

### Instale o Python (se ainda não tiver)

Verifique:

```bash
python3 --version
```

Se não tiver, instale (no Ubuntu):

```bash
sudo apt install python3 python3-pip python3-venv -y
```

---

### Crie um ambiente virtual

No diretório do seu projeto:

```bash
mkdir populate_pg && cd populate_pg
python3 -m venv venv
source venv/bin/activate     # Linux/macOS
venv\Scripts\activate        # Windows PowerShell
```

---

### Instale as dependências

```bash
pip install --upgrade pip
pip install faker psycopg2-binary tqdm
```

Esses pacotes fazem:

* **Faker** → gera dados falsos realistas
* **psycopg2-binary** → conecta ao PostgreSQL
* **tqdm** → exibe barras de progresso

---

### Crie o script

Salve o script Python (aquele que eu gerei antes) como:

```bash
nano populate_db.py
```

Cole o conteúdo e salve (`Ctrl + O`, `Enter`, `Ctrl + X`).

---

## ▶️ 2. Rodar o script

Com o ambiente ativado, rode:

```bash
python populate_db.py
```

O script vai:

1. Inserir os planos (`plans`)
2. Gerar usuários (`users`)
3. Gerar endereços (`addresses`)
4. Criar assinaturas (`subscriptions`)

Durante o processo, você verá uma **barra de progresso** do `tqdm`.

---

## 🧪 4. Verificar o resultado

Após a execução:

```bash
psql -U postgres -d test_perf
```

E rode:

```sql
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM addresses;
SELECT COUNT(*) FROM subscriptions;
```

Você deve ver milhões de registros populados 🎉

---

## ⚙️ Extra: usando Docker (opcional)

Se quiser isolar o ambiente PostgreSQL num container:

```bash
docker run --name pg-perf -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
```

Depois conecte-se com:

```
psql -h localhost -U postgres
```

E siga o mesmo passo de criar o banco + DDL.

---

Quer que eu te monte um **Docker Compose** completo (PostgreSQL + script Python + dependências), pra você rodar tudo com `docker compose up` e gerar milhões de dados automaticamente?
