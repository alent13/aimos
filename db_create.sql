CREATE TABLE contact_list
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_user BIGINT(20) NOT NULL,
    id_friend BIGINT(20) NOT NULL,
    relationship VARCHAR(30) NOT NULL,
    CONSTRAINT contact_list_fk0 FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT contact_list_fk1 FOREIGN KEY (id_friend) REFERENCES users (id)
);
CREATE INDEX contact_list_fk0 ON contact_list (id_user);
CREATE INDEX contact_list_fk1 ON contact_list (id_friend);
CREATE TABLE d_access_mode
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    access_mode VARCHAR(30) NOT NULL
);
CREATE TABLE d_language
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    lang VARCHAR(2)
);
CREATE TABLE d_notification_type
(
    id BIGINT(20),
    type VARCHAR(32)
);
CREATE TABLE d_plans
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    name VARCHAR(128) NOT NULL,
    space BIGINT(20) NOT NULL
);
CREATE TABLE d_show_mode
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    mode VARCHAR(30) NOT NULL
);
CREATE TABLE d_status
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL
);
CREATE TABLE dialog_users
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_dialog BIGINT(20) NOT NULL,
    id_user BIGINT(20) NOT NULL,
    id_status BIGINT(20) NOT NULL,
    CONSTRAINT dialog_users_fk0 FOREIGN KEY (id_dialog) REFERENCES dialogs (id),
    CONSTRAINT dialog_users_fk1 FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT dialog_users_fk2 FOREIGN KEY (id_status) REFERENCES d_status (id)
);
CREATE INDEX dialog_users_fk0 ON dialog_users (id_dialog);
CREATE INDEX dialog_users_fk1 ON dialog_users (id_user);
CREATE INDEX dialog_users_fk2 ON dialog_users (id_status);
CREATE TABLE dialogs
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    img VARCHAR(260),
    active TINYINT(1) NOT NULL,
    public TINYINT(1) NOT NULL
);
CREATE TABLE directories
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    name VARCHAR(64) NOT NULL,
    id_access_mode BIGINT(20) NOT NULL,
    id_user BIGINT(20) NOT NULL,
    id_parent BIGINT(20) NOT NULL,
    CONSTRAINT directories_fk0 FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT directories_fk1 FOREIGN KEY (id_access_mode) REFERENCES d_access_mode (id)
);
CREATE INDEX directories_fk0 ON directories (id_user);
CREATE INDEX directories_fk1 ON directories (id_access_mode);
CREATE TABLE files
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    path VARCHAR(260) NOT NULL,
    size BIGINT(20) NOT NULL,
    add_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    id_user BIGINT(20) NOT NULL,
    last_modified_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_access_mode BIGINT(20) DEFAULT '0' NOT NULL,
    CONSTRAINT files_fk0 FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT files_d_access_mode_id_fk FOREIGN KEY (id_access_mode) REFERENCES d_access_mode (id)
);
CREATE INDEX files_fk0 ON files (id_user);
CREATE INDEX files_d_access_mode_id_fk ON files (id_access_mode);
CREATE TABLE login_log
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_user BIGINT(20) NOT NULL,
    device VARCHAR(20) NOT NULL,
    os VARCHAR(20) NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description VARCHAR(128),
    CONSTRAINT login_log_fk0 FOREIGN KEY (id_user) REFERENCES users (id)
);
CREATE INDEX login_log_fk0 ON login_log (id_user);
CREATE TABLE messages
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_user BIGINT(20) NOT NULL,
    id_dialog BIGINT(20) NOT NULL,
    message_text VARCHAR(1024) NOT NULL,
    encrypt_key VARCHAR(7) NOT NULL,
    CONSTRAINT messages_fk0 FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT messages_fk1 FOREIGN KEY (id_dialog) REFERENCES dialogs (id)
);
CREATE INDEX messages_fk0 ON messages (id_user);
CREATE INDEX messages_fk1 ON messages (id_dialog);
CREATE TABLE notification_stack
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    id_user BIGINT(20) NOT NULL,
    id_message BIGINT(20) NOT NULL,
    id_user_from BIGINT(20) NOT NULL,
    id_dialog_from BIGINT(20) NOT NULL,
    datetime DATETIME NOT NULL,
    CONSTRAINT notification_stack_fk0 FOREIGN KEY (id_user) REFERENCES users (id),
    CONSTRAINT notification_stack_fk1 FOREIGN KEY (id_message) REFERENCES messages (id),
    CONSTRAINT notification_stack_fk2 FOREIGN KEY (id_user_from) REFERENCES users (id),
    CONSTRAINT notification_stack_fk3 FOREIGN KEY (id_dialog_from) REFERENCES dialogs (id)
);
CREATE INDEX notification_stack_fk0 ON notification_stack (id_user);
CREATE INDEX notification_stack_fk1 ON notification_stack (id_message);
CREATE INDEX notification_stack_fk2 ON notification_stack (id_user_from);
CREATE INDEX notification_stack_fk3 ON notification_stack (id_dialog_from);
CREATE TABLE token_user
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_user BIGINT(20) NOT NULL,
    os VARCHAR(20) NOT NULL,
    token VARCHAR(128) NOT NULL,
    device VARCHAR(20) NOT NULL,
    CONSTRAINT tokens_fk0 FOREIGN KEY (id_user) REFERENCES users (id)
);
CREATE INDEX tokens_fk0 ON token_user (id_user);
CREATE TABLE user_settings
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_page_show BIGINT(20) DEFAULT '0',
    id_email_show BIGINT(20) DEFAULT '0',
    id_phone_show BIGINT(20) DEFAULT '0',
    id_about_me_show BIGINT(20) DEFAULT '0',
    id_lang BIGINT(20) DEFAULT '0',
    CONSTRAINT user_settings_fk0 FOREIGN KEY (id_page_show) REFERENCES d_show_mode (id),
    CONSTRAINT user_settings_fk1 FOREIGN KEY (id_email_show) REFERENCES d_show_mode (id),
    CONSTRAINT user_settings_fk2 FOREIGN KEY (id_phone_show) REFERENCES d_show_mode (id),
    CONSTRAINT user_settings_fk3 FOREIGN KEY (id_about_me_show) REFERENCES d_show_mode (id),
    CONSTRAINT user_settings_fk4 FOREIGN KEY (id_lang) REFERENCES d_language (id)
);
CREATE INDEX user_settings_fk0 ON user_settings (id_page_show);
CREATE INDEX user_settings_fk1 ON user_settings (id_email_show);
CREATE INDEX user_settings_fk2 ON user_settings (id_phone_show);
CREATE INDEX user_settings_fk3 ON user_settings (id_about_me_show);
CREATE INDEX user_settings_fk4 ON user_settings (id_lang);
CREATE TABLE users
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    login VARCHAR(10) NOT NULL,
    name VARCHAR(25),
    surname VARCHAR(25),
    password CHAR(128) NOT NULL,
    id_extra BIGINT(20),
    id_user_settings BIGINT(20),
    img VARCHAR(260),
    registration_datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_action_datetime DATETIME NOT NULL,
    active TINYINT(1) NOT NULL,
    id_plan BIGINT(20) NOT NULL,
    CONSTRAINT users_fk0 FOREIGN KEY (id_extra) REFERENCES users_extra (id),
    CONSTRAINT users_fk1 FOREIGN KEY (id_user_settings) REFERENCES user_settings (id),
    CONSTRAINT users_fk3 FOREIGN KEY (id_plan) REFERENCES d_plans (id)
);
CREATE INDEX users_fk0 ON users (id_extra);
CREATE INDEX users_fk1 ON users (id_user_settings);
CREATE INDEX users_fk3 ON users (id_plan);
CREATE TABLE users_extra
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    phone VARCHAR(20),
    email VARCHAR(30),
    about TEXT
);