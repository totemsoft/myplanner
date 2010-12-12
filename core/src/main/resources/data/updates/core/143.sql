ALTER TABLE Asset ADD ContributionAnnualAmount numeric(15,4) NULL;
ALTER TABLE Asset ADD ContributionIndexation numeric(15,4) NULL;
ALTER TABLE Asset ADD ContributionStartDate datetime NULL;
ALTER TABLE Asset ADD ContributionEndDate datetime NULL;

ALTER TABLE Asset ADD DrawdownAnnualAmount numeric(15,4) NULL;
ALTER TABLE Asset ADD DrawdownIndexation numeric(15,4) NULL;
ALTER TABLE Asset ADD DrawdownStartDate datetime NULL;
ALTER TABLE Asset ADD DrawdownEndDate datetime NULL;

ALTER TABLE Asset DROP COLUMN RegularAmount;

ALTER TABLE Financial DROP COLUMN Indexation2;

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.43', 'FPS.01.42');

