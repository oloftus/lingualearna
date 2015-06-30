-- --------------------------------------
-- Schema
-- --------------------------------------

CREATE SCHEMA lingua_schema;
SET search_path = 'lingua_schema';


-- --------------------------------------
-- Objects
-- --------------------------------------

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
  first_name VARCHAR(45),
  last_name VARCHAR(45),
  email_address VARCHAR(254) NOT NULL UNIQUE,
  email_key CHAR(62) UNIQUE,
  password CHAR(60) NOT NULL,
  role VARCHAR(45) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT false,
  created TIMESTAMP NOT NULL,
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
  additional_notes VARCHAR(10000),
  source_context VARCHAR(500),
  source_url VARCHAR(2000) ,
  translation_source VARCHAR(20) NOT NULL,
  page INT NOT NULL,
  include_test BOOLEAN NOT NULL DEFAULT true,
  starred BOOLEAN NOT NULL DEFAULT false,
  position INT NOT NULL,
  UNIQUE (page, position) DEFERRABLE INITIALLY DEFERRED,
  CHECK(translation_source in ('Manual','Google','Collins','CollinsGoogle'))
);

CREATE TABLE translation_request_log (
  entry_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  email_address VARCHAR(254) NOT NULL,
  time TIMESTAMP NOT NULL,
  provider VARCHAR(20) NOT NULL,
  CHECK(provider in ('Google','Collins'))
);


-- --------------------------------------
-- Indexes
-- --------------------------------------

CREATE INDEX users_role_ix ON users (role);
CREATE INDEX users_email_address_ix ON users (email_address);
CREATE INDEX users_email_key ON users (email_key);
CREATE INDEX notebooks_users_ix ON notebooks (owner);
CREATE INDEX pages_notebook_ix ON pages (notebook);
CREATE INDEX notes_page_ix ON notes (page);


-- --------------------------------------
-- Foreign keys
-- --------------------------------------

ALTER TABLE users ADD CONSTRAINT users_pages_fk
    FOREIGN KEY (last_used)
    REFERENCES pages (page_id)
    ON DELETE SET NULL
    ON UPDATE CASCADE;

ALTER TABLE users ADD CONSTRAINT users_roles_fk
    FOREIGN KEY (role)
    REFERENCES roles (role_name)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE notebooks ADD CONSTRAINT notebooks_users_fk
    FOREIGN KEY (owner)
    REFERENCES users (user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE pages ADD CONSTRAINT pages_notebooks_fk
    FOREIGN KEY (notebook)
    REFERENCES notebooks (notebook_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE notes ADD CONSTRAINT notes_pages_fk
    FOREIGN KEY (page)
    REFERENCES pages (page_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;


-- --------------------------------------
-- Inserts
-- --------------------------------------

INSERT INTO roles VALUES ('ROLE_ADMIN'), ('ROLE_USER');
INSERT INTO supported_languages (language_code) VALUES ('en'), ('es');