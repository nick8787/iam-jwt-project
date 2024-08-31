CREATE TABLE users (
                       id                  BIGSERIAL PRIMARY KEY,
                       username            VARCHAR(30) NOT NULL UNIQUE,
                       password            VARCHAR(80) NOT NULL,
                       email               VARCHAR(50) UNIQUE,
                       phone_number        VARCHAR(16) UNIQUE,
                       created             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       registration_status VARCHAR(32) NOT NULL,
                       last_login          TIMESTAMP,
                       deleted             BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE posts (
                       id          BIGSERIAL PRIMARY KEY,
                       user_id     BIGINT NOT NULL,
                       title       VARCHAR(255) NOT NULL,
                       content     TEXT NOT NULL,
                       created     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deleted     BOOLEAN NOT NULL DEFAULT false,
                       likes       INTEGER NOT NULL DEFAULT 0,
                       image       VARCHAR(2048),
                       FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                       UNIQUE (title)
);

CREATE TABLE refresh_tokens (
                        id          SERIAL PRIMARY KEY,
                        token       VARCHAR(128) NOT NULL,
                        created     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        user_id     BIGINT NOT NULL,
                        CONSTRAINT FK_refresh_tokens_user_pk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                        CONSTRAINT refresh_token_UNIQUE UNIQUE (token),
                        CONSTRAINT refresh_token_user_UNIQUE UNIQUE (user_id)
);

CREATE TABLE end_points (
                        id             SERIAL PRIMARY KEY,
                        method         VARCHAR(8) NOT NULL,
                        path           VARCHAR(256) NOT NULL,
                        UNIQUE (method, path)
);

CREATE TABLE roles (
                        id                SERIAL PRIMARY KEY,
                        name              VARCHAR(50) NOT NULL,
                        user_system_role  VARCHAR(64) NOT NULL,
                        active            BOOLEAN NOT NULL DEFAULT true,
                        created_by        VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles (
                        user_id           BIGINT NOT NULL,
                        role_id           INT NOT NULL,
                        PRIMARY KEY (user_id, role_id),
                        FOREIGN KEY (user_id) REFERENCES users (id),
                        FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles (name, user_system_role, created_by) VALUES
                        ('SUPER_ADMIN', 'SUPER_ADMIN', 'SUPER_ADMIN'),
                        ('ADMIN', 'ADMIN', 'SUPER_ADMIN'),
                        ('USER', 'USER', 'SUPER_ADMIN');

INSERT INTO users (username, password, email, phone_number, created, updated, registration_status) VALUES
                        ('super_admin', '$2a$10$eISN54CjAVp5eCln8NpGNuOwULwgwrbBQcALb8cH0Nb0tnR5qWH9m', 'superadmin@gmail.com', '+111-222-3333', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
                        ('admin', '$2a$10$eISN54CjAVp5eCln8NpGNuOwULwgwrbBQcALb8cH0Nb0tnR5qWH9m', 'admin@gmail.com', '+444-555-6666', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE'),
                        ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com', '+777-888-9999', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ACTIVE');

-- Присвоение ролей пользователям через таблицу users_roles
INSERT INTO users_roles (user_id, role_id) VALUES
                         (1, 1),  -- super_admin с ролью SUPER_ADMIN
                         (2, 2),  -- admin с ролью ROLE_ADMIN
                         (3, 3);  -- user с ролью ROLE_USER
