-- create user 'user' with password 'user'
INSERT INTO t_user (uuid, datecreated, lastmodified, email, firstname, lastname, login, password, enabled) VALUES
  ('ac06a743-3c72-4dea-828c-c23861d12040', '2015-01-02 17:33:45.154024', '2015-01-02 17:33:45.154024',
   'no-reply@uht-traktor.ch', 'Test', 'User', 'user',
   '86e7ff3561f51631d2f394fb632b138d1b3110ea1554e3821e9bf4e88f9b71d8f97ff4c82bd42375', TRUE);

-- set roles: ROLE_USER and ROLE_NEWS
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('ac06a743-3c72-4dea-828c-c23861d12040', 'ROLE_USER');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('ac06a743-3c72-4dea-828c-c23861d12040', 'ROLE_NEWS');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('ac06a743-3c72-4dea-828c-c23861d12040', 'ROLE_TEAMS');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('ac06a743-3c72-4dea-828c-c23861d12040', 'ROLE_DOCUMENTS');

-- create user 'admin' with password 'admin'
INSERT INTO t_user (uuid, datecreated, lastmodified, email, firstname, lastname, login, password, enabled) VALUES
  ('24fca0be-b19a-45c5-a76d-bce521c27fff', '2015-01-02 17:33:45.154024', '2015-01-02 17:33:45.154024',
   'no-reply@uht-traktor.ch', 'Test', 'Admin', 'admin',
   'e6d6182679dc4753a561975f7caa7c2af394711c7edceac97323599a0b0e8e84ea1a6636d3b3775c', TRUE);

-- set roles: ROLE_USER and ROLE_ADMIN
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('24fca0be-b19a-45c5-a76d-bce521c27fff', 'ROLE_USER');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('24fca0be-b19a-45c5-a76d-bce521c27fff', 'ROLE_NEWS');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('24fca0be-b19a-45c5-a76d-bce521c27fff', 'ROLE_TEAMS');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('24fca0be-b19a-45c5-a76d-bce521c27fff', 'ROLE_DOCUMENTS');
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('24fca0be-b19a-45c5-a76d-bce521c27fff', 'ROLE_ADMIN');