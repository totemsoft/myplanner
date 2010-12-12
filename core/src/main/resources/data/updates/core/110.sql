ALTER TABLE Regular ADD Taxable char(1);
-- 
--    Changed Timeframe Details
--
DELETE FROM PeriodCode ;


INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (0, 'Custom');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (1, 'Within 1 Week');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (2, 'Within 2 Weeks');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (3, 'Within 1 Month');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (4, 'Within 3 Months');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (5, 'Within 6 Months');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (6, 'Within 1 Year');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (7, 'Within 3 Years');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (8, 'Within 5 Years');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (9, 'Within 10 Years');


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.10', 'FPS.01.09')
 GO
 