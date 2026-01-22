-- Evolução do modelo: StockItem vira um "lote físico" (múltiplos por produto/local)
-- para suportar OPEN/CONVERT/CONSUME por item.

-- 1) Remover unicidade por (produto, local)
DROP INDEX IF EXISTS ux_stock_item_product_location;

-- 2) Adicionar estado e validade
ALTER TABLE stock_item
    ADD COLUMN IF NOT EXISTS state VARCHAR(30) NOT NULL DEFAULT 'CLOSED';

ALTER TABLE stock_item
    ADD COLUMN IF NOT EXISTS expiry_date DATE;

-- 3) Aumentar precisão para permitir frações com mais segurança
ALTER TABLE stock_item
    ALTER COLUMN quantity TYPE NUMERIC(19, 6);

ALTER TABLE stock_movement
    ALTER COLUMN quantity TYPE NUMERIC(19, 6);
