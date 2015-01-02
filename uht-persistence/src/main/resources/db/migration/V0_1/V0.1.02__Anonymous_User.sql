-- create user anonymous
INSERT INTO t_user (uuid, datecreated, lastmodified, email, firstname, lastname, login, password, enabled) VALUES
  ('f4ca99c6-1c75-41ce-87ad-ff5432c95514', '2015-01-02 15:33:45.154024', '2015-01-02 15:33:45.154024',
   'anonymous@uht-traktor.ch', 'anonymous', 'anonymous', 'anonymous',
   '898ba6905a2fd2a4a0c2841ea9b117fd66a23ef41754ddc25df8b7548e90c2db3c6b849ac6d6232f', TRUE);

-- set role ROLE_ANONYMOUS
INSERT INTO t_user_authority (user_uuid, authority) VALUES ('f4ca99c6-1c75-41ce-87ad-ff5432c95514', 'ROLE_ANONYMOUS');