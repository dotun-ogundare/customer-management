CREATE SEQUENCE customer_id_seq;

CREATE TABLE customer(
    id INT DEFAULT nextval('customer_id_seq') PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    age INT NOT NULL
)