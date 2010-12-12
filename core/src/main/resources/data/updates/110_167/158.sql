CREATE TABLE FinancialService (
  FinancialServiceCode	char(10)	NOT NULL,
  FinancialServiceDesc	varchar(255)	NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate(),
  LogicallyDeleted	char		NULL
);

ALTER TABLE FinancialService
 ADD CONSTRAINT PK_FinancialServiceCode PRIMARY KEY (FinancialServiceCode);


ALTER TABLE Financial 
 ADD FinancialServiceCode char(10) NULL;

ALTER TABLE Financial ADD
 CONSTRAINT FK_Financial_FinancialServiceCode 
 FOREIGN KEY (FinancialServiceCode) 
 REFERENCES FinancialService (FinancialServiceCode);


ALTER TABLE Asset 
 ADD Reinvest char(1) DEFAULT NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.58', 'FID.01.57');

