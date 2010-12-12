ALTER TABLE Financial ADD Institution varchar(200) NULL;

ALTER TABLE Financial DROP COLUMN PoolAmount;

ALTER TABLE AssetPersonal ADD PurchaseCost numeric(15,4) NULL;


--
-- Reference data updates
--
--select * from FinancialType order by ObjectTypeID, FinancialTypeID;
--select * from FinancialCode order by FinancialTypeID, FinancialCodeID;

-- Financial
UPDATE Financial SET FinancialTypeID = NULL, FinancialCodeID = NULL WHERE FinancialTypeID IN ( 61, 62 );
-- Liability (16)
DELETE FROM FinancialCode WHERE FinancialTypeID IN ( 61, 62 ); 
DELETE FROM FinancialType WHERE FinancialTypeID IN ( 61, 62 ) AND ObjectTypeID = 16; 
-- Insurance (17) 54, 55
DELETE FROM FinancialType WHERE ObjectTypeID = 17; 

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 54, 'Car Loan', 16);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 55, 'Lease', 16);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 62, 'Other', 16);


-- Investment (11)
if exists (select * from FinancialType WHERE FinancialTypeID=4)
 UPDATE FinancialType SET FinancialTypeDesc='Shares', ObjectTypeID=11 WHERE (FinancialTypeID=4)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (4, 'Shares', 11);

if exists (select * from FinancialType WHERE FinancialTypeID=5)
 UPDATE FinancialType SET FinancialTypeDesc='Managed Funds', ObjectTypeID=11 WHERE (FinancialTypeID=5)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (5, 'Managed Funds', 11);

if exists (select * from FinancialType WHERE FinancialTypeID=6)
 UPDATE FinancialType SET FinancialTypeDesc='Other', ObjectTypeID=11 WHERE (FinancialTypeID=6)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (6, 'Other', 11);

if exists (select * from FinancialType WHERE FinancialTypeID=7)
 UPDATE FinancialType SET FinancialTypeDesc='Investment Property', ObjectTypeID=11 WHERE (FinancialTypeID=7)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (7, 'Investment Property', 11);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.29', 'FID.01.28');
