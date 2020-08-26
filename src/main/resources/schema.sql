CREATE TABLE user_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    email VARCHAR(250),
    login VARCHAR(250),
    password VARCHAR(250),
    activated BOOLEAN,
    locked BOOLEAN,
    invalid_login_attempts INT DEFAULT 0
);

CREATE TABLE user_details_t (
    user_id INT, --AUTO_INCREMENT  PRIMARY KEY, -- to samo co user_t
    --version INT, -- to samo co user_t (?)
    first_name VARCHAR(250) NOT NULL,
    last_name VARCHAR(250) NOT NULL,
    phone_number VARCHAR(250)
);

CREATE TABLE role_t(
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    name VARCHAR(4000) NOT NULL,
    code VARCHAR(4000) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_role_t(
    --id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT REFERENCES user_t(id),
    role_id INT REFERENCES role_t(id)
);

CREATE TABLE employee_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    user_id INT REFERENCES user_t(id),
    birth_date DATE,
    avg_opinion DOUBLE
);

/*CREATE TABLE skill_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    name VARCHAR(4000) NOT NULL,
    code VARCHAR(4000) NOT NULL
);*/

CREATE TABLE status_t (
     id INT AUTO_INCREMENT  PRIMARY KEY,
     business_key UUID,
     version INT DEFAULT 1,
     name VARCHAR(4000) NOT NULL,
     code VARCHAR(4000) NOT NULL
);

CREATE TABLE position_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    name VARCHAR(4000) NOT NULL
);

CREATE TABLE work_order_type_t (
   id INT AUTO_INCREMENT  PRIMARY KEY,
   business_key UUID,
   version INT DEFAULT 1,
   name VARCHAR(4000) NOT NULL,
   code VARCHAR(4000) NOT NULL,
   required_time INT NOT NULL DEFAULT 60
   --required_skill INT REFERENCES skill_t(id)
);

CREATE TABLE employee_skill_t (
    employee_id INT REFERENCES employee_t(user_id),
    work_order_type_id INT REFERENCES work_order_type_t(id)
);

CREATE TABLE work_order_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    customer_id INT REFERENCES user_t(id),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    description VARCHAR(4000),
    position_id INT REFERENCES position_t(id),
    employee_id INT REFERENCES employee_t(user_id),
    type_id INT REFERENCES work_order_type_t(id)
);

CREATE TABLE opinion_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    rating INT DEFAULT 5,
    description VARCHAR,
    work_order_id INT REFERENCES work_order_t(id)
);


CREATE TABLE work_order_status_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    business_key UUID,
    version INT DEFAULT 1,
    name VARCHAR(4000) NOT NULL,
    code VARCHAR(4000) NOT NULL
);


/*
CREATE TABLE user_vechicle_t (
   user INT REFERENCES user_t(id),
   vechicle INT REFERENCES vechicle_t(id),
   PRIMARY KEY (user, vechicle)
);

CREATE TABLE service_type_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(4000) NOT NULL,
    code VARCHAR(4000) NOT NULL,
    required_time INT NOT NULL,
    required_skill INT REFERENCES skills_t(id)
);

CREATE TABLE service_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    employee INT REFERENCES user_t(id),
    work_order INT REFERENCES work_order_t(id),
    details VARCHAR(4000)
);

CREATE TABLE work_order_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    customer INT REFERENCES user_t(id),
    begining_date TIMESTAMP,
    end_date TIMESTAMP,
    details VARCHAR(4000)
);



CREATE TABLE employee_skills_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
);

CREATE TABLE skills_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(4000) NOT NULL,
    code VARCHAR(4000) NOT NULL
);

CREATE TABLE vechicle_t (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(4000) NOT NULL,
    code VARCHAR(4000) NOT NULL,
    model VARCHAR(4000) NOT NULL,
    manufacturer VARCHAR(4000) NOT NULL,
    year INT,
    register_number VARCHAR(4000)
);*/