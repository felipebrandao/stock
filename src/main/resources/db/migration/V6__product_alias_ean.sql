-- Permite mapeamento inteligente por EAN além de descrição normalizada
-- Mantém compatibilidade com H2 (MODE=PostgreSQL)

ALTER TABLE product_alias
    ADD COLUMN IF NOT EXISTS ean VARCHAR(30);

CREATE INDEX IF NOT EXISTS ix_product_alias_ean
    ON product_alias (ean);

-- Garante que não existam dois alias para o mesmo EAN (quando informado)
-- Postgres: múltiplos NULLs são permitidos em unique index. (H2 também)
CREATE UNIQUE INDEX IF NOT EXISTS ux_product_alias_ean
    ON product_alias (ean);
