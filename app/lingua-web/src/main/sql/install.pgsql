-- --------------------------------------
-- Roles & users
-- --------------------------------------

CREATE USER lingua PASSWORD 'lingua' VALID UNTIL '2114-07-10 00:00:00';
ALTER ROLE lingua IN DATABASE lingua SET search_path = lingua;
CREATE ROLE lingua_role;
GRANT lingua_role TO lingua;


-- --------------------------------------
-- Database
-- --------------------------------------

CREATE DATABASE lingua WITH OWNER = lingua;


-- --------------------------------------
-- Schema
-- --------------------------------------

CREATE SCHEMA lingua;
SET search_path = 'lingua';


-- --------------------------------------
-- Objects
-- --------------------------------------

CREATE TYPE translation_source AS ENUM('Manual','Google');

CREATE TABLE supported_languages
(
  language_code VARCHAR(5) PRIMARY KEY
);

CREATE TABLE roles
(
  role_name VARCHAR(45) PRIMARY KEY
);

CREATE TABLE users
(
  user_id SERIAL PRIMARY KEY,
  email_address VARCHAR(254) NOT NULL UNIQUE,
  password VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT false,
  last_used INT UNIQUE
);

CREATE TABLE notebooks
(
  notebook_id SERIAL PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  local_lang VARCHAR(5) NOT NULL,
  foreign_lang VARCHAR(5) NOT NULL,
  owner INT NOT NULL,
  UNIQUE (name, owner)
);

CREATE TABLE pages
(
  page_id SERIAL PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  notebook INT NOT NULL,
  position INT NOT NULL,
  UNIQUE (name, notebook),
  UNIQUE (notebook, position) DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE notes
(
  note_id SERIAL PRIMARY KEY,
  foreign_lang VARCHAR(5) NOT NULL,
  foreign_note VARCHAR(2000),
  local_lang VARCHAR(5) NOT NULL,
  local_note VARCHAR(2000),
  additional_notes VARCHAR(2000),
  source_url VARCHAR(2000) ,
  translation_source translation_source NOT NULL,
  page INT NOT NULL,
  include_test BOOLEAN NOT NULL DEFAULT true,
  starred BOOLEAN NOT NULL DEFAULT false,
  position INT NOT NULL,
  UNIQUE (page, position) DEFERRABLE INITIALLY DEFERRED
);


-- --------------------------------------
-- Indexes
-- --------------------------------------

CREATE INDEX users_role_ix ON users (role);
CREATE INDEX users_email_address_ix ON users (email_address);
CREATE INDEX notebooks_users_ix ON notebooks (owner);
CREATE INDEX pages_notebook_ix ON pages (notebook);
CREATE INDEX notes_page_ix ON notes (page);


-- --------------------------------------
-- Foreign keys
-- --------------------------------------

ALTER TABLE users ADD CONSTRAINT users_pages_fk
    FOREIGN KEY (last_used)
    REFERENCES pages (page_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE users ADD CONSTRAINT users_roles_fk
    FOREIGN KEY (role)
    REFERENCES roles (role_name)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE notebooks ADD CONSTRAINT notebooks_users_fk
    FOREIGN KEY (owner)
    REFERENCES users (user_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE pages ADD CONSTRAINT pages_notebooks_fk
    FOREIGN KEY (notebook)
    REFERENCES notebooks (notebook_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE notes ADD CONSTRAINT notes_pages_fk
    FOREIGN KEY (page)
    REFERENCES pages (page_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;


-- --------------------------------------
-- Grants
-- --------------------------------------

GRANT CONNECT ON DATABASE lingua to lingua_role;
GRANT USAGE ON SCHEMA lingua TO lingua_role;

GRANT SELECT ON TABLE roles TO lingua_role;
GRANT SELECT ON TABLE supported_languages TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE notes TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE users TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE pages TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE notebooks TO lingua_role;


-- --------------------------------------
-- Inserts
-- --------------------------------------

INSERT INTO roles VALUES ('ROLE_ADMIN'), ('ROLE_USER');
