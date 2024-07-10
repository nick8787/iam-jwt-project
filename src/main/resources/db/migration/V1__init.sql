CREATE TABLE users (
                       id                  BIGSERIAL PRIMARY KEY,
                       username            VARCHAR(30) NOT NULL UNIQUE,
                       password            VARCHAR(80) NOT NULL,
                       email               VARCHAR(50) UNIQUE,
                       created             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       registration_status VARCHAR(32) NOT NULL
);

CREATE TABLE posts (
                       id          BIGSERIAL PRIMARY KEY,
                       user_id     BIGINT NOT NULL,
                       title       VARCHAR(255) NOT NULL,
                       content     TEXT NOT NULL,
                       created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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

CREATE TABLE roles (
                        id                SERIAL PRIMARY KEY,
                        name              VARCHAR(50) NOT NULL,
                        user_system_role  VARCHAR(64) NOT NULL,
                        active            BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE users_roles (
                        user_id           BIGINT NOT NULL,
                        role_id           INT NOT NULL,
                        PRIMARY KEY (user_id, role_id),
                        FOREIGN KEY (user_id) REFERENCES users (id),
                        FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles (name, user_system_role) VALUES
                        ('ROLE_USER', 'ROLE_USER'),
                        ('ROLE_ADMIN', 'SUPER_ADMIN');

INSERT INTO users (username, password, email, created, registration_status) VALUES
                        ('user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com', CURRENT_TIMESTAMP, 'ACTIVE'),
                        ('admin', '$2a$10$eISN54CjAVp5eCln8NpGNuOwULwgwrbBQcALb8cH0Nb0tnR5qWH9m', 'admin@gmail.com', CURRENT_TIMESTAMP, 'ACTIVE');

INSERT INTO users_roles (user_id, role_id) VALUES
                        (1, 1),
                        (2, 2);
