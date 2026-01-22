-- Dados iniciais usados nos testes com H2
INSERT INTO category (id, name, created_at) VALUES
  ('11111111-1111-1111-1111-111111111111', 'Bebidas', CURRENT_TIMESTAMP);

INSERT INTO product (id, name, category_id, created_at) VALUES
  ('22222222-2222-2222-2222-222222222222', 'Coca Cola', '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP);
