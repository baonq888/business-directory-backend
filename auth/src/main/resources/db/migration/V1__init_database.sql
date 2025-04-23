DROP TABLE IF EXISTS app_user_roles CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS confirmation_token CASCADE;

CREATE TABLE if not exists app_user (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE if not exists app_user_roles (
    app_user_user_id UUID NOT NULL,
    roles_id BIGINT NOT NULL,
    PRIMARY KEY (app_user_user_id, roles_id),
    FOREIGN KEY (app_user_user_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (roles_id) REFERENCES role(id) ON DELETE CASCADE
);