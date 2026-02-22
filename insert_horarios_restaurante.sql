-- =====================================================
-- Script de Inserção de Horários de Funcionamento
-- =====================================================
-- Autor: DevMaster Team
-- Data: 2026-02-21
-- Descrição: Insere horários de funcionamento para o restaurante ID 1
-- =====================================================

-- Limpar horários existentes do restaurante 1 (opcional - comentar se não quiser limpar)
-- DELETE FROM horarios_restaurante WHERE restaurante_id = 1;

-- =====================================================
-- Horários de Funcionamento - Restaurante ID 1
-- =====================================================
-- Dias da semana: 0=Domingo, 1=Segunda, 2=Terça, 3=Quarta, 4=Quinta, 5=Sexta, 6=Sábado
-- =====================================================

INSERT INTO horarios_restaurante (
    restaurante_id,
    dia_semana,
    horario_abertura,
    horario_fechamento,
    fechado
) VALUES 
-- Domingo (0) - Aberto
(1, 0, '11:00:00', '23:00:00', false),

-- Segunda-feira (1) - Aberto
(1, 1, '11:00:00', '23:00:00', false),

-- Terça-feira (2) - Aberto
(1, 2, '11:00:00', '23:00:00', false),

-- Quarta-feira (3) - Aberto
(1, 3, '11:00:00', '23:00:00', false),

-- Quinta-feira (4) - Aberto
(1, 4, '11:00:00', '23:00:00', false),

-- Sexta-feira (5) - Aberto até mais tarde
(1, 5, '11:00:00', '00:00:00', false),

-- Sábado (6) - Aberto até mais tarde
(1, 6, '11:00:00', '00:00:00', false);

-- =====================================================
-- Verificar inserção
-- =====================================================
SELECT 
    id,
    restaurante_id,
    CASE dia_semana
        WHEN 0 THEN 'Domingo'
        WHEN 1 THEN 'Segunda-feira'
        WHEN 2 THEN 'Terça-feira'
        WHEN 3 THEN 'Quarta-feira'
        WHEN 4 THEN 'Quinta-feira'
        WHEN 5 THEN 'Sexta-feira'
        WHEN 6 THEN 'Sábado'
    END AS dia,
    dia_semana,
    TO_CHAR(horario_abertura, 'HH24:MI') AS abertura,
    TO_CHAR(horario_fechamento, 'HH24:MI') AS fechamento,
    CASE 
        WHEN fechado = true THEN 'Fechado'
        ELSE 'Aberto'
    END AS status
FROM horarios_restaurante
WHERE restaurante_id = 1
ORDER BY dia_semana;

-- =====================================================
-- Queries úteis
-- =====================================================

-- Listar horários de todos os restaurantes
-- SELECT 
--     r.id,
--     r.nome,
--     CASE h.dia_semana
--         WHEN 0 THEN 'Domingo'
--         WHEN 1 THEN 'Segunda'
--         WHEN 2 THEN 'Terça'
--         WHEN 3 THEN 'Quarta'
--         WHEN 4 THEN 'Quinta'
--         WHEN 5 THEN 'Sexta'
--         WHEN 6 THEN 'Sábado'
--     END AS dia,
--     TO_CHAR(h.horario_abertura, 'HH24:MI') AS abertura,
--     TO_CHAR(h.horario_fechamento, 'HH24:MI') AS fechamento,
--     h.fechado
-- FROM restaurantes r
-- LEFT JOIN horarios_restaurante h ON r.id = h.restaurante_id
-- ORDER BY r.id, h.dia_semana;

-- Verificar se restaurante está aberto agora
-- SELECT 
--     CASE 
--         WHEN h.fechado = true THEN 'Fechado'
--         WHEN CURRENT_TIME BETWEEN h.horario_abertura AND h.horario_fechamento THEN 'Aberto'
--         ELSE 'Fechado'
--     END AS status_atual
-- FROM horarios_restaurante h
-- WHERE h.restaurante_id = 1 
-- AND h.dia_semana = EXTRACT(DOW FROM CURRENT_DATE);

-- Marcar um dia como fechado
-- UPDATE horarios_restaurante 
-- SET fechado = true 
-- WHERE restaurante_id = 1 AND dia_semana = 0;

-- Marcar um dia como aberto
-- UPDATE horarios_restaurante 
-- SET fechado = false 
-- WHERE restaurante_id = 1 AND dia_semana = 0;

-- Alterar horário de um dia específico
-- UPDATE horarios_restaurante 
-- SET horario_abertura = '10:00:00', horario_fechamento = '22:00:00'
-- WHERE restaurante_id = 1 AND dia_semana = 1;
