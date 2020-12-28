INSERT INTO user_t (id, version, email, activated, locked, password, business_key, create_date)
VALUES (1, 1, 'pawelzyrafa@gmail.com', true, false, '$2a$10$mXqMy26cyQKbnMed/.hQgO2XhzoxlZLmR1Xs49mCtlzTxcb9LXO4C',
        '0bb8fe12-55d6-491d-ba02-7343d4b39d98', to_timestamp('17-09-2019 18:47:52.69', 'DD-MM-YYYY HH24:MI:SS.MS')),
       (2, 1, 'test@test.com', true, false, '$2a$10$mXqMy26cyQKbnMed/.hQgO2XhzoxlZLmR1Xs49mCtlzTxcb9LXO4C',
        '44e385b2-659c-48db-8a62-0fb18a47c78c', to_timestamp('07-02-2020 10:03:12.13', 'DD-MM-YYYY HH24:MI:SS.MS')),
       (3, 1, 'test2@test.com', true, false, 'Alakija', '0a1c4194-4c4b-4e98-a575-b3618d8e3d65',
        to_timestamp('01-08-2020 03:33:55.19', 'DD-MM-YYYY HH24:MI:SS.MS')),
       (4, 1, 'test2@test.com', true, false, 'Alakija', '055f2807-3113-4c6d-82c1-ab64504bb0ba',
        to_timestamp('01-08-2019 03:33:55.19', 'DD-MM-YYYY HH24:MI:SS.MS'));

INSERT INTO user_details_t (user_id, first_name, last_name, phone_number)
VALUES (1, 'Aliko', 'Dangote', '123356789'),
       (2, 'Bill', 'Gates', '345555555'),
       (3, 'Folrunsho', 'Alakija', '456123123'),
       (4, 'Paweł', 'Białek', '123456456');

INSERT INTO role_t(id, version, code, name, is_enabled, business_key)
VALUES (1, 1, 'CLIENT', 'Klient', TRUE, 'eb3a1f06-d1fd-4b09-85ac-25cd7e0231fc'),
       (2, 1, 'ADMINISTRATOR', 'Admin', TRUE, 'b76bccc6-264c-4c18-9d72-a6667f39a356'),
       (3, 1, 'MECHANIC', 'Mechanik', TRUE, '6f4dbfd7-b620-4cd0-a3ee-a75bba4eaaea'),
       (4, 1, 'DISPATCHER', 'Dyspozytor', TRUE, '0ac6b2e9-0229-4e70-b242-93b43edd04e3');

INSERT INTO user_role_t(user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 1),
       (2, 3),
       (3, 2),
       (3, 3),
       (4, 1);

INSERT INTO employee_t(id, user_id, birth_date)
VALUES (1, 1, to_timestamp('17-09-2012', 'dd-MM-yyyy')),
       (2, 2, to_timestamp('05-02-1999', 'dd-MM-yyyy')),
       (3, 3, to_timestamp('05-02-1999', 'dd-MM-yyyy'));

INSERT INTO status_t(id, version, business_key, name, code, colour, status_type)
VALUES (1, 1, 'b59e807f-d92c-4784-9d08-be93aad7d73b', 'Oczękująca', 'WAITING', '#31d6c8', 'BEFORE'),
       (2, 1, '1a727d77-2fb1-4120-a662-4f7086362c41', 'Przypisana', 'ASSIGNED', '#126d94', 'BEFORE'),
       (3, 1, '7de2bce0-a496-4cc7-99b4-e80a064b8769', 'Rozpoczęta', 'STARTED', '#ede351', 'DURING'),
       (4, 1, '63a31e7b-5bab-4fd0-acc1-d04ab7f848fe', 'Zakończona', 'FINISHED', '#22b531', 'AFTER'),
       (5, 1, '866749aa-e9fc-4647-b8c1-caa0beb5f5bd', 'Anulowana', 'CANCELLED', '#999999', 'AFTER');

INSERT INTO work_order_type_t(id, version, business_key, name, code, required_time, active)
VALUES (1, 1, '4a04fddb-89c3-404a-99ae-f3b98eabfda0', 'Naprawa Klimatyzacji', 'CLIME', 120, true),
       (2, 1, '787f9797-df59-4fb9-b4a7-90ada5187cc9', 'Uzupełnianie czynnika', 'CLIME_REFUEL', 60, true),
       (3, 1, '0cd4ec18-be16-4932-aadc-0c53f6901bb4', 'Wymiana opon', 'TIRE_CHANGE', 30, true),
       (4, 1, '16db8454-b29b-4f26-958c-4812c4eca699', 'Czyszczenie wnętrza', 'INTERIOR_CLEANING', 90, true);

INSERT INTO work_order_flow_t(id, status_from_id, status_to_id, can_be_scheduled)
VALUES (1, 1, 2, false),
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
VALUES (1, 1, 'bbb167d1-6114-4283-ab5a-1a0941f0b527', to_timestamp('25-10-2021 15:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        to_timestamp('25-10-2021 17:00:00', 'DD-MM-YYYY HH24:MI:SS'), 1, 3, 1, null),
       (2, 1, 'd39d21b9-5bf4-4e41-9986-b2f3bf95dbc7', to_timestamp('13-05-2023', 'dd-MM-yyyy'),
        to_timestamp('13-05-2023', 'dd-MM-yyyy'), 1, 4, 2, 1),
       (3, 1, '7592f062-bb06-4a6c-b015-f956e23d8af9', to_timestamp('25-10-2020 20:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        to_timestamp('25-10-2020 20:00:00', 'DD-MM-YYYY HH24:MI:SS'), 1, 4, 5, null),
       (4, 1, '063be721-38d3-4dbb-9331-d30972e6a2c6', to_timestamp('25-10-2010 20:00:00', 'DD-MM-YYYY HH24:MI:SS'),
        to_timestamp('25-10-2020 20:00:00', 'DD-MM-YYYY HH24:MI:SS'), 1, 4, 5, null),
       (5, 1, '5ea63ba8-bbb7-4bba-a4f8-2f1c7e3a2fee', to_timestamp('13-05-2023', 'dd-MM-yyyy'),
        to_timestamp('13-05-2023', 'dd-MM-yyyy'), 1, 4, 2, 1),
       (6, 1, '1e6182bb-eb9b-42b1-96e5-b61700c5d6b9', to_timestamp('09-03-2020', 'dd-MM-yyyy'),
        to_timestamp('09-03-2020', 'dd-MM-yyyy'), 2, 1, 3, 1);