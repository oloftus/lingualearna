-- --------------------------------------
-- Grants
-- --------------------------------------

GRANT CONNECT ON DATABASE lingua_db to lingua_role;
GRANT USAGE ON SCHEMA lingua_schema TO lingua_role;

GRANT SELECT ON TABLE roles TO lingua_role;
GRANT SELECT ON TABLE supported_languages TO lingua_role;
GRANT SELECT, INSERT ON TABLE translation_request_log TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE users TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE notes TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE pages TO lingua_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE notebooks TO lingua_role;

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA lingua_schema TO lingua_role;
