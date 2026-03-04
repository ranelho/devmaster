-- Tabela de vínculo entre restaurantes e tipos de pagamento
CREATE TABLE IF NOT EXISTS restaurante_tipo_pagamento (
    id BIGSERIAL PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    tipo_pagamento_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ordem_exibicao INTEGER DEFAULT 0,
    vinculado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_restaurante_tipo_pagamento_restaurante 
        FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id) ON DELETE CASCADE,
    
    CONSTRAINT fk_restaurante_tipo_pagamento_tipo_pagamento 
        FOREIGN KEY (tipo_pagamento_id) REFERENCES tipos_pagamento(id) ON DELETE CASCADE,
    
    CONSTRAINT uk_restaurante_tipo_pagamento 
        UNIQUE (restaurante_id, tipo_pagamento_id)
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_restaurante_tipo_pagamento_restaurante 
    ON restaurante_tipo_pagamento(restaurante_id);

CREATE INDEX IF NOT EXISTS idx_restaurante_tipo_pagamento_tipo_pagamento 
    ON restaurante_tipo_pagamento(tipo_pagamento_id);

CREATE INDEX IF NOT EXISTS idx_restaurante_tipo_pagamento_ativo 
    ON restaurante_tipo_pagamento(ativo);

-- Comentários
COMMENT ON TABLE restaurante_tipo_pagamento IS 'Vínculo entre restaurantes e tipos de pagamento disponíveis';
COMMENT ON COLUMN restaurante_tipo_pagamento.restaurante_id IS 'ID do restaurante';
COMMENT ON COLUMN restaurante_tipo_pagamento.tipo_pagamento_id IS 'ID do tipo de pagamento';
COMMENT ON COLUMN restaurante_tipo_pagamento.ativo IS 'Indica se o tipo de pagamento está ativo para o restaurante';
COMMENT ON COLUMN restaurante_tipo_pagamento.ordem_exibicao IS 'Ordem de exibição do tipo de pagamento';
COMMENT ON COLUMN restaurante_tipo_pagamento.vinculado_em IS 'Data e hora do vínculo';
