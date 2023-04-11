
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE EXTENSION IF NOT EXISTS "postgres_fdw";

CREATE SERVER IF NOT EXISTS ocr FOREIGN DATA WRAPPER postgres_fdw OPTIONS (
    host 'host.docker.internal', dbname 'nfce', port '5433'
);

-- DROP TABLE IF EXISTS tb_nfce;

CREATE TABLE IF NOT EXISTS tb_nfce(
                       cupom_id UUID DEFAULT uuid_generate_v4 (),
                       url varchar(255),
                       nome_empresa varchar(120),
                       cnpj varchar(20),
                       endereco varchar(120),
                       items text,
                       qtd_total_itens integer,
                       valor_pagar float,
                       cartao_credito float,
                       cartao_debito float,
                       dinheiro float,
                       pix float,
                       outros float,
                       troco float,
                       tributos float,
                       emissao timestamp,
                       PRIMARY KEY (cupom_id)
);