-- 1. Adicionar colunas para armazenar o endereço embedded na tabela de pedidos
ALTER TABLE pedidos 
ADD COLUMN endereco_logradouro VARCHAR(255),
ADD COLUMN endereco_numero VARCHAR(20),
ADD COLUMN endereco_complemento VARCHAR(255),
ADD COLUMN endereco_bairro VARCHAR(100),
ADD COLUMN endereco_cidade VARCHAR(100),
ADD COLUMN endereco_estado VARCHAR(2),
ADD COLUMN endereco_cep VARCHAR(10),
ADD COLUMN endereco_latitude DECIMAL(10,8),
ADD COLUMN endereco_longitude DECIMAL(11,8);

-- 2. Migrar dados existentes da tabela enderecos_cliente para as novas colunas
UPDATE pedidos p
SET 
    endereco_logradouro = e.logradouro,
    endereco_numero = e.numero,
    endereco_complemento = e.complemento,
    endereco_bairro = e.bairro,
    endereco_cidade = e.cidade,
    endereco_estado = e.estado,
    endereco_cep = e.cep,
    endereco_latitude = e.latitude,
    endereco_longitude = e.longitude
FROM enderecos_cliente e
WHERE p.endereco_entrega_id = e.id;

-- 3. Remover a constraint de chave estrangeira
-- Tentamos remover pelo nome padrão do Hibernate ou pelo nome específico reportado no erro
ALTER TABLE pedidos DROP CONSTRAINT IF EXISTS fkoruawughpdbgmf1bvdshcmn5j;
ALTER TABLE pedidos DROP CONSTRAINT IF EXISTS fk_pedidos_endereco_entrega;
ALTER TABLE pedidos DROP CONSTRAINT IF EXISTS pedidos_endereco_entrega_id_fkey;

-- 4. Remover a coluna de ID do endereço antigo
ALTER TABLE pedidos DROP COLUMN endereco_entrega_id;

-- 5. Adicionar constraints de não nulo para as novas colunas obrigatórias
-- Só aplicamos se houver garantia de dados migrados, mas como precaução vamos aplicar
-- Se a migração falhar aqui, significa que havia pedidos sem endereço válido
ALTER TABLE pedidos ALTER COLUMN endereco_logradouro SET NOT NULL;
ALTER TABLE pedidos ALTER COLUMN endereco_numero SET NOT NULL;
ALTER TABLE pedidos ALTER COLUMN endereco_bairro SET NOT NULL;
ALTER TABLE pedidos ALTER COLUMN endereco_cidade SET NOT NULL;
ALTER TABLE pedidos ALTER COLUMN endereco_estado SET NOT NULL;
ALTER TABLE pedidos ALTER COLUMN endereco_cep SET NOT NULL;
