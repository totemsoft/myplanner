ALTER TABLE [iress-asset-name] ALTER COLUMN [asset-full-name] varchar(81) NULL;


CREATE TABLE LicenseData
(
	"LicenseKey" 		text,
	"LastDownloadDate"	text,
	"LastRunDate" 		text
);

INSERT INTO LicenseData (LicenseKey, LastDownloadDate, LastRunDate) VALUES ( null, null, null);

ALTER TABLE LicenseeRecord ADD MorningStarLicenseExpireDate varchar(128) NULL;
ALTER TABLE LicenseeRecord ADD MorningStarDownloadPermission int NULL;
ALTER TABLE LicenseeRecord ADD IRESSLicenseExpireDate varchar(128) NULL;
ALTER TABLE LicenseeRecord ADD IRESSLicenseDownloadPermission int NULL;
ALTER TABLE LicenseeRecord ADD Active int NULL;
ALTER TABLE LicenseeRecord ADD Notes text NULL;

ALTER TABLE LicenseeRecord ADD ClientType int NULL;
ALTER TABLE LicenseeRecord ADD NumberOfAdvisers int NULL;
ALTER TABLE LicenseeRecord ADD NumberOfSupportPersons int NULL;
ALTER TABLE LicenseeRecord ADD NumberOfAdministrators int NULL;

ALTER TABLE LicenseeRecord ADD LicenseKey text NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.53', 'FID.01.52');

