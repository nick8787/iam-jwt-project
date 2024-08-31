-- Увеличиваем длину поля image в таблице posts
ALTER TABLE posts
    ALTER COLUMN image TYPE TEXT;