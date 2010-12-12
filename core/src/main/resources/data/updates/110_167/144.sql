ALTER TABLE PersonSurvey ADD SelectedRiskProfile int NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.44', 'FID.01.43');

