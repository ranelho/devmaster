-- Inserção Simples de Tipos de Pagamento
-- Execute este script no seu banco de dados

INSERT INTO tipos_pagamento (nome, codigo, descricao, ativo, requer_troco, ordem_exibicao, criado_em) VALUES 
('Dinheiro', 'DINHEIRO', 'Pagamento em dinheiro na entrega', true, true, 1, NOW()),
('Cartão de Crédito', 'CARTAO_CREDITO', 'Pagamento com cartão de crédito', true, false, 2, NOW()),
('Cartão de Débito', 'CARTAO_DEBITO', 'Pagamento com cartão de débito', true, false, 3, NOW()),
('PIX', 'PIX', 'Pagamento via PIX', true, false, 4, NOW());

-- Verificar
SELECT * FROM tipos_pagamento ORDER BY ordem_exibicao;
