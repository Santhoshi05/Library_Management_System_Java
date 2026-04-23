CREATE DATABASE library_db;

USE library_db;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE login (
    login_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    role VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT INTO login (user_id, username, password, role) VALUES
(1, 'admin', 'admin123', 'ADMIN');

CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    author VARCHAR(100),
    quantity INT
);

INSERT INTO books (title, author, quantity) VALUES
('Java Programming', 'Herbert Schildt', 5),
('Clean Code', 'Robert Martin', 3);

CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    book_id INT,
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    fine DOUBLE,
    status VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

INSERT INTO users (name, email) VALUES
('Admin User', 'admin@gmail.com'),
('santhu', 'santhu@gmail.com');

INSERT INTO login (user_id, username, password, role) VALUES
(1, 'admin', 'admin123', 'ADMIN'),
(2, 'user1', 'user123', 'USER');

select * from users;
