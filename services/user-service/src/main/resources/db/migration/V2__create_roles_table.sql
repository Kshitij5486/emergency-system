CREATE TABLE users.roles (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT uq_roles_name UNIQUE (name)
);
INSERT INTO users.roles (name, description) VALUES
    ('CITIZEN',   'Regular user - can report emergencies'),
    ('RESPONDER', 'Emergency responder'),
    ('ADMIN',     'Platform administrator');
CREATE TABLE users.user_roles (
    user_id    UUID    NOT NULL REFERENCES users.users(id) ON DELETE CASCADE,
    role_id    INTEGER NOT NULL REFERENCES users.roles(id) ON DELETE CASCADE,
    granted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, role_id)
);
CREATE INDEX idx_user_roles_user_id ON users.user_roles (user_id);
