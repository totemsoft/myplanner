INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 0, '[None]', 0 );

UPDATE FinancialCode SET FinancialTypeID = 0 WHERE FinancialTypeID IS NULL;
UPDATE FinancialCode SET FinancialTypeID = 0 WHERE FinancialTypeID = 4;
UPDATE FinancialCode SET FinancialTypeID = 0 WHERE FinancialTypeID = 5;

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.35', 'FPS.01.34');
