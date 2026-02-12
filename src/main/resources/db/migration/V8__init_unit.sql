-- Cria tabela de unidades físicas (unit)
-- Unidades de medida como kg, litro, unidade, metro, etc.

CREATE TABLE IF NOT EXISTS unit (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Unicidade de nome (case-insensitive)
CREATE UNIQUE INDEX IF NOT EXISTS ux_unit_name_lower
    ON unit (lower(name));

-- Unicidade de abreviação (case-insensitive)
CREATE UNIQUE INDEX IF NOT EXISTS ux_unit_abbreviation_lower
    ON unit (lower(abbreviation));

