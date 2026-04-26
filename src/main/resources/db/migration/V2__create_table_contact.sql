CREATE TABLE tb_contact (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    user_id BIGINT NOT NULL,
    creation_time TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT fk_contacts_user FOREIGN KEY (user_id) REFERENCES tb_user (id) ON DELETE CASCADE
);

CREATE INDEX idx_contacts_user_id ON tb_contact(user_id);
