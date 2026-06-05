CREATE UNIQUE INDEX uk_contact_email_case_insensitive ON tb_contact (user_id, LOWER(email));
