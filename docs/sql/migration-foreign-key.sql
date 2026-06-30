USE personal_finance;

ALTER TABLE transaction_category
    ADD CONSTRAINT fk_category_user
        FOREIGN KEY (user_id)
        REFERENCES `user` (id);

ALTER TABLE transaction_record
    ADD CONSTRAINT fk_record_user
        FOREIGN KEY (user_id)
        REFERENCES `user` (id);

ALTER TABLE transaction_record
    ADD CONSTRAINT fk_record_category
        FOREIGN KEY (category_id)
        REFERENCES transaction_category (id);