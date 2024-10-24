-- Create the database
CREATE DATABASE digital_support_portal;


-- Switch to the newly created database
USE digital_support_portal;

-- for auth users
CREATE TABLE auth_users (
    id int PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    unique key username_unique (username),
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);
-- for roles
CREATE TABLE auth_roles (
	id INT PRIMARY KEY auto_increment,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- auth and role many to many relationship
CREATE TABLE auth_user_roles(
	user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    foreign key (user_id) references auth_users(id) on delete cascade,
    foreign key (role_id) references auth_roles(id) on delete cascade
);


-- for report scheduler
CREATE TABLE scheduler_job_list (
    id INT PRIMARY KEY AUTO_INCREMENT,
    end_date_time DATETIME(6) NOT NULL,
    start_date_time DATETIME(6) NOT NULL,
    cron_frequency VARCHAR(25) NOT NULL,
    database_setting_id INT NOT NULL,
    email_body VARCHAR(255) NOT NULL,
    email_subject VARCHAR(255) NOT NULL,
    job_name VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255) NOT NULL DEFAULT "SCHEDULED",
    cc BLOB,
    key_user_email BLOB NOT NULL,
    sql_query BLOB NOT NULL,
    is_deleted BOOLEAN default false,
    created_by VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_by VARCHAR(255),
    updated_at DATETIME(6),
    FOREIGN KEY (database_setting_id) REFERENCES settings_database_setting(id)
);


-- database settings

CREATE TABLE settings_database_setting (
	id INT PRIMARY KEY auto_increment,
    database_setting_name VARCHAR(100) NOT NULL,
    database_name VARCHAR(255) NOT NULL,
    database_password VARCHAR(100) NOT NULL,
    database_url VARCHAR(255) NOT NULL,
    database_username VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_at datetime(6) NOT NULL, 
    updated_by VARCHAR(255) NUll, 
    updated_at datetime(6) null,
    is_deleted boolean default false
);