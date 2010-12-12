--------------------------------------------------------------
-- delete old FundTypes
--------------------------------------------------------------
UPDATE FundType SET FundTypeDesc = 'Superannuation - Employer' 	WHERE FundTypeID = 1;
UPDATE FundType SET FundTypeDesc = 'Superannuation - Personal' 	WHERE FundTypeID = 2;
UPDATE FundType SET FundTypeDesc = 'SMSF' 				WHERE FundTypeID = 3;
UPDATE FundType SET FundTypeDesc = 'Pension - Allocated' 		WHERE FundTypeID = 4;
UPDATE FundType SET FundTypeDesc = 'Annuity - Life Expectancy' 	WHERE FundTypeID = 5;
UPDATE FundType SET FundTypeDesc = 'Annuity - Lifetime' 		WHERE FundTypeID = 6;
UPDATE FundType SET FundTypeDesc = 'Annuity - Term' 			WHERE FundTypeID = 7;
UPDATE FundType SET FundTypeDesc = 'Pension - Public Service Lifetime' WHERE FundTypeID = 8;

INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES (  9, 'Pension -  Fixed Term');
INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES ( 10, 'Pension - Lifetime (Defined Benefit)');
INSERT INTO FundType (FundTypeID, FundTypeDesc) VALUES ( 11, 'Annuity - Allocated');


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.18', 'FPS.01.17');