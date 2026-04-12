CREATE TABLE users.refresh_tokens (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL REFERENCES users.users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked    BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_address INET,
    CONSTRAINT uq_refresh_token_hash UNIQUE (token_hash)
);
CREATE INDEX idx_refresh_tokens_user_id ON users.refresh_tokens (user_id);
CREATE INDEX idx_refresh_tokens_active  ON users.refresh_tokens (user_id, expires_at) WHERE revoked = FALSE;
