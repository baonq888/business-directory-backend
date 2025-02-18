

CREATE TABLE if not exists app_user (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE if not exists role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE if not exists app_user_roles (
    app_user_user_id BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    PRIMARY KEY (app_user_user_id, roles_id),
    FOREIGN KEY (app_user_user_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (roles_id) REFERENCES role(id) ON DELETE CASCADE
);

create sequence if not exists role_seq increment by 50;
create sequence if not exists app_user_seq increment by 50;