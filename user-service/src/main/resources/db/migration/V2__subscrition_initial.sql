-- Sequence para User
CREATE SEQUENCE subscriptions_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE subscriptions (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('subscriptions_seq'),
    userId BIGINT NOT NULL,
    planName VARCHAR(255) NOT NULL,
    startDate TIMESTAMP,
    endDate TIMESTAMP,
    status VARCHAR(20)
);