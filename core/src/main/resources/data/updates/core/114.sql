--
-- this should be the first statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.14', NULL)
 GO


ALTER TABLE Person ALTER COLUMN FamilyName	varchar(32)	NULL;
ALTER TABLE Person ALTER COLUMN FirstName	varchar(32)	NULL;
ALTER TABLE Person ALTER COLUMN OtherGivenNames	varchar(32)	NULL;
ALTER TABLE Person ALTER COLUMN PreferredName	varchar(32)	NULL;


--
-- this should be the last statement in any update script
--
UPDATE DBVersion 
 SET PreviousVersion = 'FPS.01.13'
 WHERE CurrentVersion = 'FPS.01.14'
 GO
