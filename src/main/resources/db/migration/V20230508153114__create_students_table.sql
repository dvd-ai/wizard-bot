create table students (
   d_uid BIGINT PRIMARY KEY,
   gold_balance REAL,
   is_engaged BOOLEAN NOT NULL,
   balance_defrost_date DATE,
   is_in_guild BOOLEAN NOT NULL
);