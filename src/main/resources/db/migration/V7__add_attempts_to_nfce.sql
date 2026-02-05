-- Adiciona coluna attempts para controle de tentativas de importação
ALTER TABLE nfce ADD COLUMN attempts INTEGER NOT NULL DEFAULT 0;

-- Atualiza registros existentes para terem 0 tentativas se ainda não tiverem sido processados
UPDATE nfce SET attempts = 0 WHERE attempts IS NULL;
