-- Add active user flag if 'N' user inactive otherwise - active
--if not exists (select * from dbo.syscolumns where id = object_id(N'[dbo].[UserPerson]') AND name= 'ActiveUser')
ALTER TABLE UserPerson ADD ActiveUser char(1) NULL;

UPDATE UserPerson SET ActiveUser = 'Y';

UPDATE AdviserTypeCode SET AdviserTypeCodeDesc = 'Office' WHERE AdviserTypeCodeID = 1;

UPDATE AdviserTypeCode SET AdviserTypeCodeDesc = 'Financial Adviser' WHERE AdviserTypeCodeID = 3;

UPDATE AdviserTypeCode SET AdviserTypeCodeDesc = 'Support' WHERE AdviserTypeCodeID = 5;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.56', 'FPS.01.55');
	