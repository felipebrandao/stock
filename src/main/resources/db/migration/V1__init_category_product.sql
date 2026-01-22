-- Cria schema inicial para Category e Product
-- Observação: evitamos CREATE EXTENSION para manter compatibilidade com H2 nos testes.

CREATE TABLE IF NOT EXISTS category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Unicidade de nome (case-insensitive) via índice em lower(name)
CREATE UNIQUE INDEX IF NOT EXISTS ux_category_name_lower
    ON category (lower(name));

CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE INDEX IF NOT EXISTS ix_product_category_id
    ON product (category_id);

-- Ajuda na regra de "nome por categoria" (case-insensitive)
CREATE UNIQUE INDEX IF NOT EXISTS ux_product_name_category_lower
    ON product (lower(name), category_id);
