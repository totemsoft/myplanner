--
-- SP request:
-- move/add these columns to Financial
--
ALTER TABLE Liability DROP COLUMN DateStart;
ALTER TABLE Liability DROP COLUMN DateEnd;

ALTER TABLE Financial ADD DateStart		datetime	NULL;
ALTER TABLE Financial ADD DateEnd		datetime	NULL;

-- for AssetInvestment
ALTER TABLE Financial ADD Franked		numeric(15,4)	NULL;
ALTER TABLE Financial ADD TaxFreeDeferred	numeric(15,4)	NULL;
ALTER TABLE Financial ADD CapitalGrowth		numeric(15,4)	NULL;
ALTER TABLE Financial ADD Income		numeric(15,4)	NULL;
ALTER TABLE Financial ADD UpfrontFee		numeric(15,4)	NULL;

-- for AssetSuperannuation
ALTER TABLE Financial ADD Deductible		numeric(15,4)	NULL;
ALTER TABLE Financial ADD Rebateable		numeric(15,4)	NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.25', 'FID.01.24');