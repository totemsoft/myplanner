ALTER TABLE iress_asset_name ALTER COLUMN asset_full_name varchar(81) NULL;


CREATE TABLE LicenseData
(
#ifdef MSSQL
	LicenseKey 			text,
	LastDownloadDate	text,
	LastRunDate 		text
#endif MSSQL
#ifdef HSQLDB
	LicenseKey 			varchar(2048),
	LastDownloadDate	varchar(64),
	LastRunDate 		varchar(64)
#endif HSQLDB
);

INSERT INTO LicenseData (LicenseKey, LastDownloadDate, LastRunDate) VALUES ( null, null, null);

ALTER TABLE LicenseeRecord ADD MorningStarLicenseExpireDate varchar(128) NULL;
ALTER TABLE LicenseeRecord ADD MorningStarDownloadPermission int NULL;
ALTER TABLE LicenseeRecord ADD IRESSLicenseExpireDate varchar(128) NULL;
ALTER TABLE LicenseeRecord ADD IRESSLicenseDownloadPermission int NULL;
ALTER TABLE LicenseeRecord ADD Active int NULL;
ALTER TABLE LicenseeRecord ADD Notes 
#ifdef MSSQL
	text NULL
#endif MSSQL
#ifdef HSQLDB
	varchar(2048) NULL
#endif HSQLDB
;

ALTER TABLE LicenseeRecord ADD ClientType int NULL;
ALTER TABLE LicenseeRecord ADD NumberOfAdvisers int NULL;
ALTER TABLE LicenseeRecord ADD NumberOfSupportPersons int NULL;
ALTER TABLE LicenseeRecord ADD NumberOfAdministrators int NULL;

ALTER TABLE LicenseeRecord ADD LicenseKey 
#ifdef MSSQL
	text NULL
#endif MSSQL
#ifdef HSQLDB
	varchar(2048) NULL
#endif HSQLDB
;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.53', 'FPS.01.52');

