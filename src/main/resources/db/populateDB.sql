DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
('User', 'user@yandex.ru', 'password'),
('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_USER', 100000),
('ROLE_ADMIN', 100001);

--yyyy-MM-dd HH:mm
INSERT INTO meals (user_id, date_time, description, calories) VALUES
(100000, to_timestamp('2019-01-01 09:00', 'YYYY-MM-DD HH24:MI'), 'USER завтрак1', 500),
(100000, to_timestamp('2019-01-01 12:00', 'YYYY-MM-DD HH24:MI'), 'USER обед1', 700),
(100000, to_timestamp('2019-01-01 19:00', 'YYYY-MM-DD HH24:MI'), 'USER ужин1', 700),
(100000, to_timestamp('2019-01-02 09:00', 'YYYY-MM-DD HH24:MI'), 'USER завтрак2', 300),
(100000, to_timestamp('2019-01-02 12:30', 'YYYY-MM-DD HH24:MI'), 'USER обед2', 300),
(100000, to_timestamp('2019-01-02 18:00', 'YYYY-MM-DD HH24:MI'), 'USER ужин2', 300),
(100000, to_timestamp('2025-07-03 09:00', 'YYYY-MM-DD HH24:MI'), 'USER завтрак FUTURE', 555),
(100000, to_timestamp('2025-07-03 12:11', 'YYYY-MM-DD HH24:MI'), 'USER обед FUTURE', 777),
(100000, to_timestamp('2025-07-03 18:11', 'YYYY-MM-DD HH24:MI'), 'USER ужин FUTURE', 777)
;

INSERT INTO meals (user_id, date_time, description, calories) VALUES
(100001, to_timestamp('2019-01-01 09:00', 'YYYY-MM-DD HH24:MI'), 'ADMIN завтрак', 500),
(100001, to_timestamp('2019-01-01 12:00', 'YYYY-MM-DD HH24:MI'), 'ADMIN обед', 700),
(100001, to_timestamp('2019-01-01 19:00', 'YYYY-MM-DD HH24:MI'), 'ADMIN ужин', 700);