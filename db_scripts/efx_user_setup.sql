CREATE USER efx_user IDENTIFIED WITH sha256_password BY 'peasg@Jan2022!';
CREATE USER efx_dba IDENTIFIED WITH sha256_password BY 'peasg@Dec2021!';

GRANT SHOW TABLES, SELECT ON database.* TO efx_user;
GRANT ALL ON database.efx TO efx_dba;

GRANT SHOW DATABASES ON efx.* to efx_user;
GRANT SHOW TABLES ON efx.* to efx_user;
GRANT SHOW COLUMNS ON efx.* to efx_user;
GRANT CREATE TABLE ON efx.* to efx_user;
GRANT SELECT ON efx.* to efx_user;

GRANT SHOW DATABASES ON efx.* to efx_dba;
GRANT SHOW TABLES ON efx.* to efx_dba;
GRANT SHOW COLUMNS ON efx.* to efx_dba;
GRANT CREATE TABLE ON efx.* to efx_dba;
GRANT ALTER TABLE ON efx.* to efx_dba;
GRANT DROP TABLE ON efx.* to efx_dba;
GRANT SELECT ON efx.* to efx_dba;
GRANT INSERT ON efx.* to efx_dba;
