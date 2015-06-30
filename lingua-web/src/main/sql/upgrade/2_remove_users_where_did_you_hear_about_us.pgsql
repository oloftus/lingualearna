SET search_path = 'lingua_schema';

ALTER TABLE users DROP CONSTRAINT users_wdyhau_sources_fk;
ALTER TABLE users DROP COLUMN wdyhau;
DROP TABLE wdyhau_sources;
