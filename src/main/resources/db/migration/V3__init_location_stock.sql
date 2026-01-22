-- Location + StockItem + StockMovement (core stock model)
-- Observação: mantemos compatibilidade com H2 nos testes (evitar extensões específicas).

CREATE TABLE IF NOT EXISTS location (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Unicidade de nome (case-insensitive)
CREATE UNIQUE INDEX IF NOT EXISTS ux_location_name_lower
    ON location (lower(name));

CREATE TABLE IF NOT EXISTS stock_item (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    location_id UUID NOT NULL,
    quantity NUMERIC(19, 3) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,

    CONSTRAINT fk_stock_item_product
        FOREIGN KEY (product_id) REFERENCES product(id),

    CONSTRAINT fk_stock_item_location
        FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE INDEX IF NOT EXISTS ix_stock_item_product_id
    ON stock_item (product_id);

CREATE INDEX IF NOT EXISTS ix_stock_item_location_id
    ON stock_item (location_id);

-- Um saldo por (produto, local)
CREATE UNIQUE INDEX IF NOT EXISTS ux_stock_item_product_location
    ON stock_item (product_id, location_id);

CREATE TABLE IF NOT EXISTS stock_movement (
    id UUID PRIMARY KEY,
    type VARCHAR(30) NOT NULL,
    product_id UUID NOT NULL,
    quantity NUMERIC(19, 3) NOT NULL,
    from_location_id UUID,
    to_location_id UUID,
    note VARCHAR(1000),
    occurred_at TIMESTAMP NOT NULL DEFAULT now(),
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_stock_movement_product
        FOREIGN KEY (product_id) REFERENCES product(id),

    CONSTRAINT fk_stock_movement_from_location
        FOREIGN KEY (from_location_id) REFERENCES location(id),

    CONSTRAINT fk_stock_movement_to_location
        FOREIGN KEY (to_location_id) REFERENCES location(id)
);

CREATE INDEX IF NOT EXISTS ix_stock_movement_product_id
    ON stock_movement (product_id);

CREATE INDEX IF NOT EXISTS ix_stock_movement_from_location_id
    ON stock_movement (from_location_id);

CREATE INDEX IF NOT EXISTS ix_stock_movement_to_location_id
    ON stock_movement (to_location_id);

CREATE INDEX IF NOT EXISTS ix_stock_movement_occurred_at
    ON stock_movement (occurred_at);
