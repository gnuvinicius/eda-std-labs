-- =====================================================
-- 1️⃣  POPULAR TABELA PLANS
-- =====================================================
INSERT INTO plans (name, description, price, durationInDays) VALUES
('Plano Básico', 'Acesso limitado a funcionalidades básicas.', 29.90, 30),
('Plano Premium', 'Acesso completo com suporte prioritário.', 59.90, 90),
('Plano Empresarial', 'Plano voltado para empresas com múltiplos usuários.', 199.90, 365);


-- =====================================================
-- 2️⃣  POPULAR TABELA USERS (nomes e e-mails brasileiros realistas)
-- =====================================================
-- Listas de nomes e sobrenomes com índices
WITH primeiros AS (
  SELECT ROW_NUMBER() OVER () AS id, nome FROM (
    VALUES
      ('Ana'),('Bruno'),('Carlos'),('Daniela'),('Eduardo'),('Fernanda'),
      ('Gabriel'),('Helena'),('Isabela'),('João'),('Karina'),('Leonardo'),
      ('Mariana'),('Natália'),('Otávio'),('Paula'),('Rafael'),('Sabrina'),
      ('Thiago'),('Vanessa'),('Gustavo'),('Lívia'),('Mateus'),('Renata'),
      ('Pedro'),('Camila'),('Felipe'),('Juliana'),('Rodrigo'),('Beatriz')
  ) AS t(nome)
),
sobrenomes AS (
  SELECT ROW_NUMBER() OVER () AS id, sobrenome FROM (
    VALUES
      ('Silva'),('Souza'),('Oliveira'),('Santos'),('Pereira'),('Ferreira'),
      ('Almeida'),('Carvalho'),('Lima'),('Gomes'),('Ribeiro'),('Martins'),
      ('Barbosa'),('Rocha'),('Correia'),('Monteiro'),('Moraes'),('Teixeira'),
      ('Azevedo'),('Castro'),('Mendes'),('Costa'),('Nunes'),('Cardoso'),
      ('Tavares'),('Rezende'),('Batista'),('Campos'),('Vieira'),('Cunha')
  ) AS t(sobrenome)
)

INSERT INTO users (name, email, cpf, birthDate, phone, createdAt, updatedAt, active)
SELECT
    (SELECT nome FROM primeiros WHERE id = ((FLOOR(RANDOM()*30)+1)::INT)) ||
    ' ' ||
    (SELECT sobrenome FROM sobrenomes WHERE id = ((FLOOR(RANDOM()*30)+1)::INT)) AS name,

    LOWER(
        REPLACE((SELECT nome FROM primeiros WHERE id = ((FLOOR(RANDOM()*30)+1)::INT)),' ','') ||
        '.' ||
        REPLACE((SELECT sobrenome FROM sobrenomes WHERE id = ((FLOOR(RANDOM()*30)+1)::INT)),' ','') ||
        (FLOOR(RANDOM() * 100)::INT)::TEXT ||
        '@' ||
        (ARRAY['gmail.com','hotmail.com','yahoo.com','outlook.com','uol.com.br'])[FLOOR(RANDOM() * 5 + 1)]
    ) AS email,

    LPAD((10000000000 + gs)::TEXT, 11, '0') AS cpf,
    (DATE '1970-01-01' + (RANDOM() * 12784)::INT) AS birthDate,
    '(11) 9' || LPAD((RANDOM() * 99999999)::INT::TEXT, 8, '0') AS phone,
    NOW(),
    NOW(),
    TRUE
FROM generate_series(1, 1000) AS g(gs);

-- =====================================================
-- 3️⃣  POPULAR TABELA ADDRESSES (1 endereço por usuário)
-- =====================================================
INSERT INTO addresses (userId, street, city, state, zipCode, country)
SELECT
    u.id AS userId,
    'Rua ' || (ARRAY['das Flores', 'dos Sonhos', 'da Paz', 'das Laranjeiras', 'do Sol', 'das Acácias', 'da Liberdade', 'dos Pinheiros'])[FLOOR(RANDOM() * 8 + 1)] || ', nº ' || (FLOOR(RANDOM() * 999 + 1)) AS street,
    cities[idx] AS city,
    states[idx] AS state,
    LPAD((10000000 + FLOOR(RANDOM() * 8999999))::TEXT, 8, '0') AS zipCode,
    'Brasil' AS country
FROM users u
CROSS JOIN LATERAL (
    SELECT
        -- arrays paralelos: cities[i] corresponde a states[i]
        ARRAY[
            'São Paulo','Rio de Janeiro','Belo Horizonte','Curitiba','Salvador',
            'Fortaleza','Brasília','Porto Alegre','Recife','Manaus'
        ] AS cities,
        ARRAY[
            'SP','RJ','MG','PR','BA','CE','DF','RS','PE','AM'
        ] AS states,
        -- índice aleatório coerente com o tamanho da array (1..array_length)
        (FLOOR(RANDOM() * array_length(ARRAY[
            'São Paulo','Rio de Janeiro','Belo Horizonte','Curitiba','Salvador',
            'Fortaleza','Brasília','Porto Alegre','Recife','Manaus'
        ], 1)) + 1)::INT AS idx
) lists;


-- =====================================================
-- 4️⃣  POPULAR TABELA SUBSCRIPTIONS (relacionamento entre usuários e planos)
-- =====================================================
INSERT INTO subscriptions (userId, planId, startDate, endDate, status, createdAt, updatedAt)
SELECT
    u.id AS userId,
    (FLOOR(RANDOM() * 3) + 1)::INT AS planId, -- 3 planos existentes
    NOW() - (INTERVAL '1 day' * (RANDOM() * 365)::INT) AS startDate,
    NOW() + (INTERVAL '1 day' * ((RANDOM() * 365)::INT)) AS endDate,
    (ARRAY['active', 'inactive', 'canceled'])[FLOOR(RANDOM() * 3 + 1)] AS status,
    NOW(),
    NOW()
FROM (
    SELECT id FROM users
) u;
