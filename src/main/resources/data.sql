INSERT INTO user_t (id, login, email, password, business_key)
VALUES(1, 'Romek', 'Romek', '$2y$10$jwAj/Hone1I8MZw2jcbu7uxS6kLCO9cRHvP.XhzUz7uw2GUnC.xSW', '6a27c4f4-4505-46e1-a510-bf55290cebbe'),
(2, 'Bill', 'Bill', 'Gates', 'd97af2e7-31da-4547-9847-07fcd5d4f87f'),
(3, 'Folrunsho', 'Folrunsho', 'Alakija', 'da6c3098-522d-4878-bcdc-b207d3154077');

INSERT INTO user_details_t (user_id, first_name, last_name, phone_number)
VALUES(1, 'Aliko', 'Dangote', '123'),
(2, 'Bill', 'Gates', '345'),
(3, 'Folrunsho', 'Alakija', '456');

INSERT INTO role_t(id, code, name, is_enabled, business_key)
VALUES
    (1, 'CLIENT', 'Klient', TRUE, '58ec21b0-4f8e-4160-b3ee-461baa8b79b3'),
    (2, 'ADMINISTRATOR', 'Admin', TRUE, '78dcbc08-1fee-438e-a324-60c9453e56af'),
    (3, 'MECHANIC', 'Mechanik', TRUE, 'dcdc4e48-4e80-49df-b67a-734da2ae087b'),
    (4, 'OFFICE', 'Biuro', TRUE, 'ff7801b7-28de-4fd3-be4f-c533a3e5b8b8');

INSERT INTO user_role_t(user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(3, 2),
(3, 3);

INSERT INTO employee_t(user_id, birth_date)
VALUES
(1, parsedatetime('17-09-2012', 'dd-MM-yyyy')),
(3, parsedatetime('05-02-1999', 'dd-MM-yyyy'));

INSERT INTO status_t(id, business_key, name, code)
VALUES
(1, '0218db7f-5a5f-4a57-9a74-620737f36cf6', 'Oczękująca', 'WAITING'),
(2, '222b66a4-681a-4907-8b88-5d2fcc52334e', 'Zaakceptowana', 'ACCEPTED'),
(3, '11e49668-6f32-45eb-9338-be17feeac82c', 'Rozpoczęta', 'STARTED'),
(4, 'b5f2d1ff-bb74-41da-9e2a-24e1b966ca75', 'Zakończona', 'FINISHED')