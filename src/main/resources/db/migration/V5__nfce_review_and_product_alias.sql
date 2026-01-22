-- Fase 2 NFC-e: revisão + mapeamento por alias
-- Mantemos compatibilidade com H2 (MODE=PostgreSQL) nos testes.

CREATE TABLE IF NOT EXISTS product_alias (
    id UUID PRIMARY KEY,
    alias_normalized VARCHAR(255) NOT NULL,
    product_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_product_alias_product
        FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_product_alias_alias_normalized
    ON product_alias (alias_normalized);

CREATE INDEX IF NOT EXISTS ix_product_alias_product_id
    ON product_alias (product_id);

-- Itens revisáveis da importação NFC-e (relacionados ao job de importação existente)
CREATE TABLE IF NOT EXISTS nfce_import_item (
    id UUID PRIMARY KEY,
    nfce_import_id UUID NOT NULL,

    item_number INT,
    description VARCHAR(500) NOT NULL,
    description_normalized VARCHAR(500) NOT NULL,
    ean VARCHAR(30),
    unit VARCHAR(20),

    quantity NUMERIC(19,6) NOT NULL,
    unit_price NUMERIC(19,6),
    total_price NUMERIC(19,2),

    product_id UUID,
    status VARCHAR(30) NOT NULL,

    expiry_date DATE,
    location_id UUID,

    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP,

    CONSTRAINT fk_nfce_import_item_import
        FOREIGN KEY (nfce_import_id) REFERENCES nfce(id),

    CONSTRAINT fk_nfce_import_item_product
        FOREIGN KEY (product_id) REFERENCES product(id),

    CONSTRAINT fk_nfce_import_item_location
        FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE INDEX IF NOT EXISTS ix_nfce_import_item_import_id
    ON nfce_import_item (nfce_import_id);

CREATE INDEX IF NOT EXISTS ix_nfce_import_item_product_id
    ON nfce_import_item (product_id);

CREATE INDEX IF NOT EXISTS ix_nfce_import_item_location_id
    ON nfce_import_item (location_id);

CREATE UNIQUE INDEX IF NOT EXISTS ux_nfce_import_item_import_item_number
    ON nfce_import_item (nfce_import_id, item_number);
