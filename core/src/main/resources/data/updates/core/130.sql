ALTER TABLE Asset DROP COLUMN DateAcquired;
ALTER TABLE Asset ADD FundTypeID int NULL;
ALTER TABLE Asset ADD InvestmentTypeID int NULL;
ALTER TABLE Asset ADD UnitsShares numeric(18,4)	DEFAULT 1;
ALTER TABLE Asset ADD PurchaseCost numeric(15,4) NULL;
ALTER TABLE Asset ADD ReplacementValue numeric(15,4) NULL;

ALTER TABLE Asset ADD UnitsSharesPrice numeric(15,4) NULL;
ALTER TABLE Asset ADD PriceDate datetime NULL;

DROP TABLE AssetCash;
DROP TABLE AssetInvestment;
DROP TABLE AssetPersonal;
DROP TABLE AssetSuperannuation;

ALTER TABLE Liability DROP CONSTRAINT FK_Liability_RegularExpense;

DROP TABLE Insurance;
DROP TABLE RegularExpense;
DROP TABLE RegularIncome;


ALTER TABLE Liability ADD
  CONSTRAINT FK_Liability_Regular FOREIGN KEY (LiabilityID) REFERENCES Regular (RegularID);

ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_InvestmentType FOREIGN KEY (InvestmentTypeID) REFERENCES InvestmentType (InvestmentTypeID);

ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_FundType FOREIGN KEY (FundTypeID) REFERENCES FundType (FundTypeID);



--SELECT * FROM FinancialType ORDER BY ObjectTypeID
-- ASSET_PERSONAL = 12 'Investment Property' FinancialTypeID = 12
UPDATE Financial SET FinancialTypeID = NULL, FinancialCodeID = NULL WHERE FinancialTypeID = 12;
DELETE FROM FinancialType WHERE FinancialTypeID = 12;



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.30', 'FPS.01.29');
