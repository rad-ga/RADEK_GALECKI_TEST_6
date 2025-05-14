INSERT INTO employee (id, first_name, last_name) VALUES
(1, 'Tomasz', 'Kowalski'),
(2, 'Piotr', 'Pawłowski'),
(3, 'Adam', 'Nowak'),
(4, 'Anna', 'Szpak'),
(5, 'Katarzyna', 'Wisniewska');
ALTER TABLE employee ALTER COLUMN id RESTART WITH 6;


INSERT INTO meeting_room (id, name) VALUES
(1, 'MR 1'),
(2, 'MR 2'),
(3, 'MR 3'),
(4, 'MR 4'),
(5, 'MR 5');
ALTER TABLE meeting_room ALTER COLUMN id RESTART WITH 6;

INSERT INTO reservation (id, employee_id,meeting_room_id, subject, start_date_time, duration_min) VALUES
 (1, 1, 1, 'Project meeting', '2025-05-12 10:00:00', 15),
 (2, 2, 2, 'Town hall', '2025-05-13 14:30:00', 60),
 (3, 3, 3, 'Team meeting', '2025-05-14 09:00:00', 30),
 (4, 5, 5, 'Internal training', '2025-05-15 13:00:00', 45),
 (5, 1, 3, 'New products presentation', '2025-05-16 10:00:00', 30),
 (6, 2, 2, 'Budget planning', '2025-05-17 09:30:00', 60),
 (7, 4, 4, 'Customer presentation', '2025-05-19 11:00:00', 90),
 (8, 3, 1, 'Year end summary', '2025-05-20 15:00:00', 60);

 ALTER TABLE reservation ALTER COLUMN id RESTART WITH 9
