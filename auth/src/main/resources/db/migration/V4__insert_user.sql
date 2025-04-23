INSERT INTO app_user (name, email, password)
VALUES ('Admin', 'admin@example.com', '$2a$10$9ue8DbWao.1Z9kfjUD5pv.w2a5I4371Qd6yPBCm4vEi8LrKhv38gq');

WITH new_admin AS (
    SELECT user_id
    FROM app_user
    WHERE email = 'admin@example.com'
)
INSERT INTO app_user_roles (app_user_user_id, roles_id)
SELECT new_admin.user_id, role.id
FROM new_admin, role
WHERE role.name = 'ADMIN';