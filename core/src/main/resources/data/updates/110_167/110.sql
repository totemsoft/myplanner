
------------------------------------------------
--	START changing existing data structure
------------------------------------------------

--DROP TABLE FinancialModel;

--
--		
--

ALTER TABLE Regular ADD Taxable char(1);

------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--


--
--				FOREIGN KEY
--


--
--				UNIQUE INDEX
--



--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;

--INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
--VALUES (13, 'Every Three Years');

-- 
--                       Changed Timeframe Details
--

DELETE FROM PeriodCode ;


INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (0, 'Custom');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (1, 'Within 1 Week');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (2, 'Within 2 Weeks');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (3, 'Within 1 Month');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (4, 'Within 3 Months');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (5, 'Within 6 Months');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (6, 'Within 1 Year');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (7, 'Within 3 Years');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (8, 'Within 5 Years');

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (9, 'Within 10 Years');


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.10', 'FID.01.09');

