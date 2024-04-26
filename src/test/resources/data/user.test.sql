DROP TABLE users;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) UNIQUE,
    phone_number VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (user_id, name, password, nickname, phone_number, email)
VALUES
    ('user1', 'John Doe', 'password1', 'johndoe', '1234567890', 'john.doe@example.com'),
    ('user2', 'Jane Smith', 'password2', 'janesmith', '0987654321', 'jane.smith@example.com'),
    ('user3', 'Michael Johnson', 'password3', 'mikejohn', '5555555555', 'michael.johnson@example.com'),
    ('user4', 'Emily Davis', 'password4', 'emilydavis', '9999999999', 'emily.davis@example.com'),
    ('user5', 'David Brown', 'password5', 'davidbrown', '1111111111', 'david.brown@example.com'),
    ('user6', 'Sarah Wilson', 'password6', 'sarahwilson', '7777777777', 'sarah.wilson@example.com'),
    ('user7', 'James Taylor', 'password7', 'jamestaylor', '2222222222', 'james.taylor@example.com'),
    ('user8', 'Emma Martinez', 'password8', 'emmamartinez', '8888888888', 'emma.martinez@example.com'),
    ('user9', 'Daniel Anderson', 'password9', 'danielanderson', '3333333333', 'daniel.anderson@example.com'),
    ('userForUpdateTest', 'Olivia Garcia', 'password10', 'oliviagarcia', '4444444444', 'olivia.garcia@example.com');
