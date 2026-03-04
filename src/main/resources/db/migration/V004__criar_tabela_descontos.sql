-- Migration: Criar tabela de descontos
-- Autor: DevMaster Team
-- Data: 2024-12-26

CREATE TABLE descontos (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    percentual_desconto DECIMAL(5,2) NOT NULL CHECK (percentual_desconto > 0 AND percentual_desconto <= 100),
    tipo_intervalo VARCHAR(20) NOT NULL CHECK (tipo_intervalo IN ('DIAS', 'HORAS')),
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_desconto_produto FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE,
    CONSTRAINT chk_periodo_valido CHECK (data_hora_fim > data_hora_inicio)
);

-- Índices para otimização de consultas
CREATE INDEX idx_desconto_produto_id ON descontos(produto_id);
CREATE INDEX idx_desconto_vigencia ON descontos(ativo, data_hora_inicio, data_hora_fim);
CREATE INDEX idx_desconto_periodo ON descontos(data_hora_inicio, data_hora_fim);

-- Comentários
COMMENT ON TABLE descontos IS 'Tabela de descontos temporários para produtos';
COMMENT ON COLUMN descontos.percentual_desconto IS 'Percentual de desconto aplicado (0.01 a 100.00)';
COMMENT ON COLUMN descontos.tipo_intervalo IS 'Tipo de intervalo: DIAS ou HORAS';
COMMENT ON COLUMN descontos.data_hora_inicio IS 'Data e hora de início da vigência do desconto';
COMMENT ON COLUMN descontos.data_hora_fim IS 'Data e hora de fim da vigência do desconto';
COMMENT ON COLUMN descontos.ativo IS 'Indica se o desconto está ativo';
