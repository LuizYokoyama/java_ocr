--DROP TABLE IF EXISTS tb_ocr ;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS tb_ocr(
                       ocr_id UUID DEFAULT uuid_generate_v4 (),
                       ocr_text varchar(2024),
                       image varchar(3145820),
                       PRIMARY KEY (ocr_id)
)