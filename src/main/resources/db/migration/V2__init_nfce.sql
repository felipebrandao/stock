-- Schema inicial da parte de NFC-e
-- Tabelas baseadas nas JPA entities existentes
-- Observação: evitamos CREATE EXTENSION para manter compatibilidade com H2 nos testes.

CREATE TABLE IF NOT EXISTS nfce (
    id UUID PRIMARY KEY,
    access_key VARCHAR(44) NOT NULL,
    qr_code_url VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL,
    error_message VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    processed_at TIMESTAMP,

    CONSTRAINT ux_nfce_access_key UNIQUE (access_key)
);

CREATE TABLE IF NOT EXISTS nfce_scrape (
    id UUID PRIMARY KEY,
    access_key VARCHAR(44) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    fonte VARCHAR(100) NOT NULL,
    scraped_at TIMESTAMP NOT NULL,
    modelo INT,
    serie INT,
    numero INT,
    data_emissao DATE,
    data_saida_entrada DATE,
    valor_total_nota NUMERIC(19,2),

    CONSTRAINT ux_nfce_scrape_access_key UNIQUE (access_key)
);

CREATE TABLE IF NOT EXISTS nfce_scrape_emitente (
    id UUID PRIMARY KEY,
    scrape_id UUID NOT NULL,
    razao_social VARCHAR(200),
    nome_fantasia VARCHAR(200),
    cnpj VARCHAR(14),
    endereco VARCHAR(300),
    bairro VARCHAR(120),
    cep VARCHAR(8),
    municipio VARCHAR(120),
    uf VARCHAR(2),
    telefone VARCHAR(30),

    CONSTRAINT ux_nfce_scrape_emitente_scrape_id UNIQUE (scrape_id),
    CONSTRAINT fk_nfce_scrape_emitente_scrape
        FOREIGN KEY (scrape_id) REFERENCES nfce_scrape(id)
);

CREATE TABLE IF NOT EXISTS nfce_scrape_item (
    id UUID PRIMARY KEY,
    scrape_id UUID NOT NULL,
    numero INT,
    descricao VARCHAR(500),
    quantidade NUMERIC(19,6),
    unidade VARCHAR(20),
    valor NUMERIC(19,2),
    valor_unitario NUMERIC(19,6),
    ncm VARCHAR(20),
    cest VARCHAR(20),
    cfop VARCHAR(10),
    ean VARCHAR(30),

    CONSTRAINT fk_nfce_scrape_item_scrape
        FOREIGN KEY (scrape_id) REFERENCES nfce_scrape(id)
);

CREATE INDEX IF NOT EXISTS ix_nfce_scrape_item_scrape_id
    ON nfce_scrape_item (scrape_id);
