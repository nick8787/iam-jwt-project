CREATE TABLE comments (
                          id          BIGSERIAL PRIMARY KEY,
                          post_id     BIGINT NOT NULL,
                          user_id     BIGINT NOT NULL,
                          message     TEXT NOT NULL,
                          created     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          deleted     BOOLEAN NOT NULL DEFAULT false,
                          FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
                          FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Индексы для оптимизации запросов
CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);
