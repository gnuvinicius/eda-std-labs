-- Sequence para User
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

-- Tabela User
CREATE TABLE users (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('user_seq'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Sequence para OutboxEvent
CREATE SEQUENCE outbox_event_seq START WITH 1 INCREMENT BY 1;

-- Tabela OutboxEvent
CREATE TABLE outbox_event (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('outbox_event_seq'),
    type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    published BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
