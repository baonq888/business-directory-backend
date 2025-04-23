DROP SEQUENCE IF EXISTS confirmation_token_sequence;

CREATE SEQUENCE confirmation_token_sequence INCREMENT BY 1;

CREATE TABLE confirmation_token (
    id BIGINT NOT NULL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    user_id UUID NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_user(user_id)
);