USE personal_finance;

CREATE INDEX idx_user_token
    ON `user` (token);

CREATE INDEX idx_category_user_id
    ON transaction_category (user_id);

CREATE UNIQUE INDEX uk_category_user_type_name
    ON transaction_category (user_id, type, name);

CREATE INDEX idx_record_user_id
    ON transaction_record (user_id);

CREATE INDEX idx_record_user_date
    ON transaction_record (user_id, record_date);

CREATE INDEX idx_record_user_type
    ON transaction_record (user_id, type);

CREATE INDEX idx_record_user_category
    ON transaction_record (user_id, category_id);