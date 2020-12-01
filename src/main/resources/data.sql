INSERT INTO user_t (id, version, email, activated, locked, password, business_key, create_date)
VALUES(1, 1, 'pawelzyrafa@gmail.com', true, false, '$2a$10$mXqMy26cyQKbnMed/.hQgO2XhzoxlZLmR1Xs49mCtlzTxcb9LXO4C', random_uuid(), parsedatetime('17-09-2019 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS')),
(2, 1,'test@test.com', true, false, '$2a$10$mXqMy26cyQKbnMed/.hQgO2XhzoxlZLmR1Xs49mCtlzTxcb9LXO4C', random_uuid(), parsedatetime('07-02-2020 10:03:12.13', 'dd-MM-yyyy hh:mm:ss.SS')),
(3, 1,'test2@test.com', true, false, 'Alakija', random_uuid(), parsedatetime('01-08-2020 03:33:55.19', 'dd-MM-yyyy hh:mm:ss.SS')),
(4, 1,'test2@test.com', true, false, 'Alakija', random_uuid(), parsedatetime('01-08-2019 03:33:55.19', 'dd-MM-yyyy hh:mm:ss.SS'));

INSERT INTO user_details_t (user_id, first_name, last_name, phone_number)
VALUES(1, 'Aliko', 'Dangote', '123356789'),
(2, 'Bill', 'Gates', '345555555'),
(3, 'Folrunsho', 'Alakija', '456123123'),
(4, 'Paweł', 'Białek', '123456456');

INSERT INTO role_t(id, version, code, name, is_enabled, business_key)
VALUES
    (1, 1,'CLIENT', 'Klient', TRUE, random_uuid()),
    (2, 1,'ADMINISTRATOR', 'Admin', TRUE, random_uuid()),
    (3, 1,'MECHANIC', 'Mechanik', TRUE, random_uuid()),
    (4, 1,'DISPATCHER', 'Dyspozytor', TRUE, random_uuid());

INSERT INTO user_role_t(user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(2, 3),
(3, 2),
(3, 3),
(4, 1);

INSERT INTO employee_t(id, user_id, birth_date)
VALUES
(1, 1, parsedatetime('17-09-2012', 'dd-MM-yyyy')),
(2, 2, parsedatetime('05-02-1999', 'dd-MM-yyyy')),
(3, 3, parsedatetime('05-02-1999', 'dd-MM-yyyy'));

INSERT INTO status_t(id, version, business_key, name, code, colour, status_type)
VALUES
(1, 1, random_uuid(), 'Oczękująca', 'WAITING', '#31d6c8', 'BEFORE'),
(2, 1, random_uuid(), 'Przypisana', 'ASSIGNED', '#126d94', 'BEFORE'),
(3, 1, random_uuid(), 'Rozpoczęta', 'STARTED', '#ede351', 'DURING'),
(4, 1, random_uuid(), 'Zakończona', 'FINISHED', '#22b531', 'AFTER'),
(5, 1, random_uuid(), 'Anulowana', 'CANCELLED', '#999999', 'AFTER');

INSERT INTO work_order_type_t(id, version, business_key, name, code, required_time)
VALUES
(1, 1, random_uuid(), 'Naprawa Klimatyzacji', 'CLIME', 120),
(2, 1, random_uuid(), 'Uzupełnianie czynnika', 'CLIME_REFUEL', 60),
(3, 1, random_uuid(), 'Wymiana opon', 'TIRE_CHANGE', 30),
(4, 1, random_uuid(), 'Czyszczenie wnętrza', 'INTERIOR_CLEANING', 90);

INSERT INTO work_order_flow_t(id, status_from_id, status_to_id, can_be_scheduled)
VALUES
(1, 1, 2, false),
(2, 2, 3, true),
(3, 1, 5, true),
(4, 3, 4, true),
(5, 2, 1, false);

INSERT INTO employee_skill_t(employee_id, work_order_type_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 4);

INSERT INTO work_order_t(id, version, business_key, start_date, end_date, employee_id, type_id, status_id, CUSTOMER_ID)
VALUES
(1, 1, random_uuid(), parsedatetime('25-10-2021 15:00:00', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('25-10-2021 17:00:00', 'dd-MM-yyyy HH:mm:ss'), 1, 3, 1, null),
(2, 1, random_uuid(), parsedatetime('13-05-2023', 'dd-MM-yyyy'), parsedatetime('13-05-2023', 'dd-MM-yyyy'), 1, 4, 2, 1),
(3, 1, random_uuid(), parsedatetime('25-10-2020 20:00:00', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('25-10-2020 20:00:00', 'dd-MM-yyyy HH:mm:ss'), 1, 4, 5, null),
(4, 1, random_uuid(), parsedatetime('25-10-2010 20:00:00', 'dd-MM-yyyy HH:mm:ss'), parsedatetime('25-10-2020 20:00:00', 'dd-MM-yyyy HH:mm:ss'),1, 4, 5, null),
(5, 1, random_uuid(), parsedatetime('13-05-2023', 'dd-MM-yyyy'), parsedatetime('13-05-2023', 'dd-MM-yyyy'), 1, 4, 2, 1),
(6, 1, random_uuid(), parsedatetime('09-03-2020', 'dd-MM-yyyy'), parsedatetime('09-03-2020', 'dd-MM-yyyy'), 2, 1, 3, 1)