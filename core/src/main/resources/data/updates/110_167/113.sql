

------------------------------------------------
--	START changing existing data structure
------------------------------------------------


CREATE TABLE LicenseeRecord
(
	"CompanyName" varchar(128) NOT NULL,
	"LicenseeRecordID" int IDENTITY (1,1) NOT NULL,
	"ContactFamilyName" varchar(128),
	"ContactFirstName" varchar(128),
	"ContactOtherNames" varchar(128),
	"ContactPosition" varchar(128),
	"Sex" varchar(128),
	"ResidentialAddressLine1" varchar(128),
	"ResidentialAddressLine2" varchar(128),
	"ResidentialSuburb" varchar(128),
	"ResidentialState" varchar(128),
	"ResidentialPostcode" varchar(128),
	"PostalAddressLine1" varchar(128),
	"PostalAddressLine2" varchar(128),
	"PostalState" varchar(128),
	"PostalSuburb" varchar(128),
	"PostalPostcode" varchar(128),
	"HomePhone" varchar(128),
	"WorkPhone" varchar(128),
	"MobilePhone" varchar(128),
	"HomeFax" varchar(128),
	"WorkFax" varchar(128),
	"HomeEmail" varchar(128),
	"WorkEmail" varchar(128),
	"LicenseTypeId" int,
	"LicenseeUUID" varchar(128),
	"LicenseEffective" varchar(128),
	"LicenseExpiry" varchar(128),
	"LicenseExpiryDate" varchar(128),
	  CONSTRAINT "PK_LicenseeRecord" PRIMARY KEY ("LicenseeRecordID")
);


------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--


--
--				FOREIGN KEY
--


--
--				UNIQUE INDEX
--



--
--				INDEX
--


--
--				INSERT/UPDATE
--


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.13', 'FID.01.12');

