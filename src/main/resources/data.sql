INSERT INTO user_t (id, email, activated, locked, password, business_key, create_date)
VALUES(1, 'pawelzyrafa@gmail.com', true, false, '$2a$10$mXqMy26cyQKbnMed/.hQgO2XhzoxlZLmR1Xs49mCtlzTxcb9LXO4C', RAWTOHEX('6a27c4f4-4505-46e1-a510-bf55290cebbe'), parsedatetime('17-09-2019 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS')),
(2, 'test@test.com', true, false, '$2a$10$mXqMy26cyQKbnMed/.hQgO2XhzoxlZLmR1Xs49mCtlzTxcb9LXO4C', RAWTOHEX('d97af2e7-31da-4547-9847-07fcd5d4f87f'), parsedatetime('07-02-2020 10:03:12.13', 'dd-MM-yyyy hh:mm:ss.SS')),
(3, 'test2@test.com', true, false, 'Alakija', RAWTOHEX('da6c3098-522d-4878-bcdc-b207d3154077'), parsedatetime('01-08-2020 03:33:55.19', 'dd-MM-yyyy hh:mm:ss.SS'));

INSERT INTO user_details_t (user_id, first_name, last_name, phone_number)
VALUES(1, 'Aliko', 'Dangote', '123356789'),
(2, 'Bill', 'Gates', '345555555'),
(3, 'Folrunsho', 'Alakija', '456123123');

INSERT INTO role_t(id, code, name, is_enabled, business_key)
VALUES
    (1, 'CLIENT', 'Klient', TRUE, RAWTOHEX('58ec21b0-4f8e-4160-b3ee-461baa8b79b3')),
    (2, 'ADMINISTRATOR', 'Admin', TRUE, RAWTOHEX('78dcbc08-1fee-438e-a324-60c9453e56af')),
    (3, 'MECHANIC', 'Mechanik', TRUE, RAWTOHEX('dcdc4e48-4e80-49df-b67a-734da2ae087b')),
    (4, 'OFFICE', 'Biuro', TRUE, RAWTOHEX('ff7801b7-28de-4fd3-be4f-c533a3e5b8b8'));

INSERT INTO user_role_t(user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(2, 3),
(3, 2),
(3, 3);

INSERT INTO employee_t(id, user_id, birth_date)
VALUES
(1, 1, parsedatetime('17-09-2012', 'dd-MM-yyyy')),
(2, 3, parsedatetime('05-02-1999', 'dd-MM-yyyy'));

INSERT INTO status_t(id, business_key, name, code)
VALUES
(1, RAWTOHEX('0218db7f-5a5f-4a57-9a74-620737f36cf6'), 'Oczękująca', 'WAITING'),
(2, RAWTOHEX('222b66a4-681a-4907-8b88-5d2fcc52334e'), 'Zaakceptowana', 'ACCEPTED'),
(3, RAWTOHEX('11e49668-6f32-45eb-9338-be17feeac82c'), 'Rozpoczęta', 'STARTED'),
(4, RAWTOHEX('b5f2d1ff-bb74-41da-9e2a-24e1b966ca75'), 'Zakończona', 'FINISHED');

INSERT INTO work_order_type_t(id, business_key, name, code, required_time)
VALUES
(1, RAWTOHEX('0218db7f-5a5f-4a57-9a74-620737f36cf6'), 'Naprawa Klimatyzacji', 'CLIME', 120),
(2, RAWTOHEX('222b66a4-681a-4907-8b88-5d2fcc52334e'), 'Uzupełnianie czynnika', 'CLIME_REFUEL', 60),
(3, RAWTOHEX('11e49668-6f32-45eb-9338-be17feeac82c'), 'Wymiana opon', 'TIRE_CHANGE', 30),
(4, RAWTOHEX('b5f2d1ff-bb74-41da-9e2a-24e1b966ca75'), 'Czyszczenie wnętrza', 'INTERIOR_CLEANING', 90);

INSERT INTO employee_skill_t(employee_id, work_order_type_id)
VALUES
(1, 1),
(1, 2),
(1, 3);

INSERT INTO work_order_t(id, business_key, start_date, start_time, employee_id, type_id)
VALUES
(1, RAWTOHEX('0218db7f-5a5f-4a57-9a74-620737f36cf6'), parsedatetime('07-02-2021', 'dd-MM-yyyy'), '14:30', 1, 3),
(2, RAWTOHEX('222b66a4-681a-4907-8b88-5d2fcc52334e'), parsedatetime('13-05-2023', 'dd-MM-yyyy'), '15:00', 1, 4),
(3, RAWTOHEX('11e49668-6f32-45eb-9338-be17feeac82c'), parsedatetime('09-03-2020', 'dd-MM-yyyy'), '12:20', 2, 1)