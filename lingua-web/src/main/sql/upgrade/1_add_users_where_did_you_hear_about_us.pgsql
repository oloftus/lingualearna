SET search_path = 'lingua_schema';

CREATE TABLE wdyhau_sources (
    wdyhau_id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

GRANT SELECT ON TABLE wdyhau_sources TO lingua_role;

ALTER TABLE users ADD COLUMN wdyhau INT;

ALTER TABLE users ADD CONSTRAINT users_wdyhau_sources_fk
    FOREIGN KEY (wdyhau)
    REFERENCES wdyhau_sources (wdyhau_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;
