DELETE FROM FundType WHERE FundTypeID=1;
INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (1, 'Annuity (Medium Term)');

INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (4, 'Annuity (Long Term)');

INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (5, 'Annuity (Short Term)');


-- default is Medium Term
--UPDATE Asset SET FundTypeID = 1 WHERE AssetID IN ( SELECT FinancialID FROM Financial WHERE ComplyingForDSS IS NULL );
-- Long Term
UPDATE Asset SET FundTypeID = 4 WHERE AssetID IN ( SELECT FinancialID FROM Financial WHERE ComplyingForDSS IS NOT NULL );

-- Farming Property and Home of Relative map to Other (87) and delete
UPDATE Financial SET FinancialTypeID = 87 WHERE FinancialTypeID = 17;
UPDATE Financial SET FinancialTypeID = 87 WHERE FinancialTypeID = 18;
DELETE FROM FinancialType WHERE FinancialTypeID IN (17, 18);

-- 6 = Other Investment
UPDATE Financial SET FinancialTypeID = 6 WHERE FinancialTypeID IS NULL AND FinancialID IN ( SELECT ObjectID FROM Object WHERE ObjectTypeID = 11 );


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.57', 'FPS.01.56');

