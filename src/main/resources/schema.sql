CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Limpar tabelas para garantir recarga completa do data.sql
DROP TABLE IF EXISTS shop_item CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;

-- Tabela para ShopItem (Mapeado por JPA)
CREATE TABLE shop_item (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DECIMAL(19, 2),
    image_url TEXT,
    category VARCHAR(255)
);

-- Tabela para AppUser (Mapeado por JPA para Autenticação)
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50)
);
