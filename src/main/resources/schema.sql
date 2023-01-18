
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE EXTENSION IF NOT EXISTS "postgres_fdw";

CREATE SERVER IF NOT EXISTS ocr FOREIGN DATA WRAPPER postgres_fdw OPTIONS (
    host 'localhost', dbname 'ocr', port '5432'
);

CREATE TABLE IF NOT EXISTS tb_ocr(
                       ocr_id UUID DEFAULT uuid_generate_v4 (),
                       ocr_text varchar(2024),
                       image varchar(3145820),
                       PRIMARY KEY (ocr_id)
);