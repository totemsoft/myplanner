ALTER TABLE PersonSurvey ADD SelectedRiskProfile int NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.44', 'FPS.01.43');

