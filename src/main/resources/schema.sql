CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50)
);

-- Adicionando novas colunas para o Painel Admin e Senhas
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS email VARCHAR(255);
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS last_login TIMESTAMP;
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS is_banned BOOLEAN DEFAULT FALSE;
ALTER TABLE app_user ADD COLUMN IF NOT EXISTS profile_picture TEXT;

CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS shop_item (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DECIMAL(19, 2),
    image_url TEXT,
    category VARCHAR(255)
);

-- Novas colunas da Expansão do E-commerce
ALTER TABLE shop_item ADD COLUMN IF NOT EXISTS team_abbrev VARCHAR(10);
ALTER TABLE shop_item ADD COLUMN IF NOT EXISTS featured BOOLEAN DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS user_purchase (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    item_name VARCHAR(255),
    price DECIMAL(19, 2),
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS favorite_goal (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    game_id BIGINT NOT NULL,
    event_id INT NOT NULL,
    player_name VARCHAR(255),
    player_photo TEXT,
    team VARCHAR(10),
    period INT,
    time_in_period VARCHAR(10),
    x DOUBLE PRECISION,
    y DOUBLE PRECISION,
    shot_type VARCHAR(50),
    UNIQUE(user_id, game_id, event_id)
);

CREATE TABLE IF NOT EXISTS favorite_player (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    player_id BIGINT NOT NULL,
    player_name VARCHAR(255),
    team VARCHAR(100),
    position VARCHAR(50),
    player_number INT,
    image TEXT,
    UNIQUE(user_id, player_id)
);
