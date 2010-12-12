-------------------------------------------------------
-- BUG-FIX 564: Asset Category ==> Personal/Physical
-------------------------------------------------------
UPDATE FinancialType SET FinancialTypeDesc = 'Boat' WHERE FinancialTypeID = 15 AND ObjectTypeID = 12;
UPDATE FinancialType SET FinancialTypeDesc = 'Caravan' WHERE FinancialTypeID = 16 AND ObjectTypeID = 12;
UPDATE FinancialType SET FinancialTypeDesc = 'Farming Property' WHERE FinancialTypeID = 17 AND ObjectTypeID = 12;
UPDATE FinancialType SET FinancialTypeDesc = 'Home of Relative' WHERE FinancialTypeID = 18 AND ObjectTypeID = 12;
UPDATE FinancialType SET FinancialTypeDesc = 'Holiday Home' WHERE FinancialTypeID = 20 AND ObjectTypeID = 12;


----------------------------------------------------------
-- BUG-FIX 703: New list for expenses
----------------------------------------------------------
-- change FinancialTypeDesc for Expenses
----------------------------------------------------------
UPDATE FinancialType SET FinancialTypeDesc = 'Car Registration' WHERE FinancialTypeID = 32 AND ObjectTypeID = 15;
UPDATE FinancialType SET FinancialTypeDesc = 'Cultural Events' WHERE FinancialTypeID = 41 AND ObjectTypeID = 15;
UPDATE FinancialType SET FinancialTypeDesc = 'Donations & Charities' WHERE FinancialTypeID = 43 AND ObjectTypeID = 15;
UPDATE FinancialType SET FinancialTypeDesc = 'Dry Cleaning' WHERE FinancialTypeID = 44 AND ObjectTypeID = 15;
UPDATE FinancialType SET FinancialTypeDesc = 'Entertainment/Recreation/Leisure' WHERE FinancialTypeID = 45 AND ObjectTypeID = 15;

----------------------------------------------------------
-- delete rows from FinancialCode
----------------------------------------------------------
DELETE FinancialCode WHERE FinancialTypeID = 46;
DELETE FinancialCode WHERE FinancialTypeID = 47;
DELETE FinancialCode WHERE FinancialTypeID = 49;
DELETE FinancialCode WHERE FinancialTypeID = 50;

----------------------------------------------------------
-- keep existing rows, but change there FinancialTypeID 
-- to 45 ('Entertainment/Recreation/Leisure')
----------------------------------------------------------
UPDATE Financial SET FinancialTypeID=45 WHERE FinancialTypeID=46 OR FinancialTypeID=47 OR FinancialTypeID=49 OR FinancialTypeID=50

----------------------------------------------------------
-- delete FinancialTypes for Expenses
----------------------------------------------------------
DELETE FinancialType WHERE FinancialTypeID = 46 AND ObjectTypeID = 15;
DELETE FinancialType WHERE FinancialTypeID = 47 AND ObjectTypeID = 15;
DELETE FinancialType WHERE FinancialTypeID = 49 AND ObjectTypeID = 15;
DELETE FinancialType WHERE FinancialTypeID = 50 AND ObjectTypeID = 15;


----------------------------------------
-- BUG-FIX 704: update financial types
----------------------------------------
UPDATE FinancialType SET FinancialTypeDesc = 'Other' WHERE FinancialTypeID = 1014 AND ObjectTypeID = 10;
UPDATE FinancialType SET FinancialTypeDesc = 'Overseas' WHERE FinancialTypeID = 1016 AND ObjectTypeID = 10;
UPDATE FinancialType SET FinancialTypeDesc = 'Cash Management Trust' WHERE FinancialTypeID = 1017 AND ObjectTypeID = 10;


----------------------------------------
-- BUG-FIX 709: update financial types
----------------------------------------
UPDATE FinancialType SET FinancialTypeDesc = 'Superannuation Lump Sum' WHERE FinancialTypeID = 57 AND ObjectTypeID = 14;


------------------------------------
-- BUG-FIX 716: update fund types
------------------------------------
UPDATE AssetSuperannuation SET FundTypeID = NULL;

DELETE FundType;

INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (1, 'Annuity' );
INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (2, 'Pension' );
INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (3, 'Superannuation' );


---------------------------------------
-- BUG-FIX 700: insert new OwnerCode
---------------------------------------
INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc) VALUES (8, 'Tennants in Common');


--
--	remove this constraint on ADDRESS suburb/postcode
--
ALTER TABLE Address DROP CONSTRAINT FK_Address_SuburbPostCode;

ALTER TABLE Institution ADD InstitutionCode varchar (10) NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.26', 'FID.01.25');