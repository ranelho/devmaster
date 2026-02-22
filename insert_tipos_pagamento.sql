-- =====================================================
-- Script de Inserção de Tipos de Pagamento
-- =====================================================
-- Autor: DevMaster Team
-- Data: 2026-02-21
-- Descrição: Insere os tipos de pagamento padrão do sistema
-- =====================================================

-- Limpar tipos de pagamento existentes (opcional - comentar se não quiser limpar)
-- DELETE FROM tipos_pagamento;

-- Inserir tipos de pagamento
INSERT INTO tipos_pagamento (
    nome, 
    codigo, 
    descricao, 
    icone_url, 
    ativo, 
    requer_troco, 
    ordem_exibicao, 
    criado_em
) VALUES 
-- 1. Dinheiro
(
    'Dinheiro',
    'DINHEIRO',
    'Pagamento em dinheiro na entrega. Informe se precisa de troco.',
    '💵',
    true,
    true,  -- Requer informar troco
    1,
    NOW()
),

-- 2. Cartão de Crédito
(
    'Cartão de Crédito',
    'CARTAO_CREDITO',
    'Pagamento com cartão de crédito na entrega. Aceita todas as bandeiras.',
    '💳',
    true,
    false,
    2,
    NOW()
),

-- 3. Cartão de Débito
(
    'Cartão de Débito',
    'CARTAO_DEBITO',
    'Pagamento com cartão de débito na entrega. Aceita todas as bandeiras.',
    '💳',
    true,
    false,
    3,
    NOW()
),

-- 4. PIX
(
    'PIX',
    'PIX',
    'Pagamento via PIX. QR Code será enviado após confirmação do pedido.',
    '📱',
    true,
    false,
    4,
    NOW()
),

-- 5. Vale Refeição
(
    'Vale Refeição',
    'VALE_REFEICAO',
    'Pagamento com vale refeição (Sodexo, Alelo, VR, etc).',
    '🎫',
    true,
    false,
    5,
    NOW()
),

-- 6. Vale Alimentação
(
    'Vale Alimentação',
    'VALE_ALIMENTACAO',
    'Pagamento com vale alimentação (Sodexo, Alelo, VR, etc).',
    '🎫',
    true,
    false,
    6,
    NOW()
);

-- =====================================================
-- Verificar inserção
-- =====================================================
SELECT 
    id,
    nome,
    codigo,
    ativo,
    requer_troco,
    ordem_exibicao
FROM tipos_pagamento
ORDER BY ordem_exibicao;

-- =====================================================
-- Queries úteis
-- =====================================================

-- Listar apenas tipos ativos
-- SELECT * FROM tipos_pagamento WHERE ativo = true ORDER BY ordem_exibicao;

-- Desativar um tipo de pagamento
-- UPDATE tipos_pagamento SET ativo = false WHERE codigo = 'VALE_REFEICAO';

-- Ativar um tipo de pagamento
-- UPDATE tipos_pagamento SET ativo = true WHERE codigo = 'VALE_REFEICAO';

-- Alterar ordem de exibição
-- UPDATE tipos_pagamento SET ordem_exibicao = 10 WHERE codigo = 'PIX';

-- Adicionar novo tipo de pagamento
-- INSERT INTO tipos_pagamento (nome, codigo, descricao, icone_url, ativo, requer_troco, ordem_exibicao, criado_em)
-- VALUES ('Boleto', 'BOLETO', 'Pagamento via boleto bancário', '🧾', true, false, 7, NOW());
