-- CashAsset = 10
UPDATE Financial SET FinancialTypeID = NULL, FinancialCodeID = NULL WHERE FinancialTypeID IN (1001,1002,1003,1004,1011,1014,1015,1016,1017);
DELETE FROM FinancialCode WHERE FinancialTypeID IN (1001,1002,1003,1004,1011,1014,1015,1016,1017);
DELETE FROM FinancialType WHERE FinancialTypeID IN (1001,1002,1003,1004,1011,1014,1015,1016,1017);

-- InvestmentAsset == 11
UPDATE Financial SET FinancialTypeID = NULL, FinancialCodeID = NULL WHERE FinancialTypeID IN (1005,1006,1007,1008,1009,1010,1012,1013);
DELETE FROM FinancialCode WHERE FinancialTypeID IN (1005,1006,1007,1008,1009,1010,1012,1013);
DELETE FROM FinancialType WHERE FinancialTypeID IN (1005,1006,1007,1008,1009,1010,1012,1013);

-- ASSET_PERSONAL = 12

-- SuperannuationAsset = 13
UPDATE Financial SET FinancialTypeID = NULL, FinancialCodeID = NULL WHERE FinancialTypeID IN (2001,2002,2003,2004,2005);
DELETE FROM FinancialCode WHERE FinancialTypeID IN (2001,2002,2003,2004,2005);
DELETE FROM FinancialType WHERE FinancialTypeID IN (2001,2002,2003,2004,2005);


ALTER TABLE Financial DROP CONSTRAINT FK_Financial_FinancialCodeType;

ALTER TABLE FinancialCode DROP CONSTRAINT PK_FinancialCodeType;

ALTER TABLE Financial ADD CONSTRAINT FK_Financial_FinancialType FOREIGN KEY 
	(FinancialTypeID) REFERENCES FinancialType (FinancialTypeID);

ALTER TABLE FinancialCode ADD CONSTRAINT PK_FinancialCode PRIMARY KEY (FinancialCodeID);

ALTER TABLE Financial ADD CONSTRAINT FK_Financial_FinancialCode FOREIGN KEY 
	(FinancialCodeID) REFERENCES FinancialCode (FinancialCodeID);

ALTER TABLE FinancialCode DROP CONSTRAINT FK_FinancialCode_FinancialType;
ALTER TABLE FinancialCode ALTER COLUMN FinancialTypeID	int	NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.34', 'FPS.01.33');
