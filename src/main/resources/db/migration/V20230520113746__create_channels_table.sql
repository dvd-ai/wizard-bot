create table channels (
   c_id BIGINT PRIMARY KEY,
   is_for_report BOOLEAN NOT NULL,
   is_ignored_for_currency_operations BOOLEAN NOT NULL
);