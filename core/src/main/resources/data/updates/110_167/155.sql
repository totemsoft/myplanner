-- if exists (select FinancialTypeID from FinancialType WHERE ObjectTypeID=10 AND FinancialTypeID=8)
-- UPDATE FinancialType SET FinancialTypeDesc='CMT' WHERE ObjectTypeID=10 AND FinancialTypeID=8
-- else
-- INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (8, 'CMT', 10);

-- Automobile
UPDATE FinancialType SET FinancialTypeDesc='Motor Vehicle' WHERE ObjectTypeID=12 AND FinancialTypeID=14;
-- Home Furnishings
UPDATE FinancialType SET FinancialTypeDesc='Home Contents' WHERE ObjectTypeID=12 AND FinancialTypeID=19;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.55', 'FID.01.54');
