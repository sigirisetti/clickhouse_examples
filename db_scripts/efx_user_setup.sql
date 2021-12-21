CREATE USER pae_user IDENTIFIED WITH sha256_password BY 'peasg@Jan2022!';
CREATE USER pae_dba IDENTIFIED WITH sha256_password BY 'peasg@Dec2021!';

GRANT SHOW TABLES, SELECT ON database.* TO pae_user;
GRANT ALL ON database.pae TO pae_dba;

GRANT SHOW DATABASES ON pae.* to pae_user;
GRANT SHOW TABLES ON pae.* to pae_user;
GRANT SHOW COLUMNS ON pae.* to pae_user;
GRANT CREATE TABLE ON pae.* to pae_user;
GRANT SELECT ON pae.* to pae_user;

GRANT SHOW DATABASES ON pae.* to pae_dba;
GRANT SHOW TABLES ON pae.* to pae_dba;
GRANT SHOW COLUMNS ON pae.* to pae_dba;
GRANT CREATE TABLE ON pae.* to pae_dba;
GRANT ALTER TABLE ON pae.* to pae_dba;
GRANT DROP TABLE ON pae.* to pae_dba;
GRANT SELECT ON pae.* to pae_dba;
GRANT INSERT ON pae.* to pae_dba;
