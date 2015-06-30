-- --------------------------------------
-- Roles & users
-- --------------------------------------

CREATE ROLE lingua_role;
CREATE USER lingua_user PASSWORD 'lingua_password' VALID UNTIL '2114-07-10 00:00:00';
GRANT lingua_role TO lingua_user;
ALTER ROLE lingua_user IN DATABASE lingua_db SET search_path = 'lingua_schema';
