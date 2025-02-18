-- Create user_detail table in the current database
CREATE TABLE user_detail (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    date_of_birth DATE
);

CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create user_detail_roles join table in the current database
CREATE TABLE user_detail_roles (
    user_detail_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_detail_id, role_id),
    FOREIGN KEY (user_detail_id) REFERENCES user_detail(id) ON DELETE CASCADE
    -- We can't reference role table in a different DB directly
);