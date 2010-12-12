--
-- this should be the first statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.17', NULL)
 GO

-- Risk Profile Survey created in 140.sql

--
-- this should be the last statement in any update script
--
UPDATE DBVersion 
 SET PreviousVersion = 'FPS.01.16'
 WHERE CurrentVersion = 'FPS.01.17'
 GO
