CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE produtos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    versao INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(255) NOT NULL DEFAULT 'DISPONIVEL',
    tenant_id UUID NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco NUMERIC(14,2) NOT NULL DEFAULT 0,
    criado_em TIMESTAMP WITH TIME ZONE DEFAULT now(),
    atualizado_em TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_produtos_tenant_id ON produtos(tenant_id);
