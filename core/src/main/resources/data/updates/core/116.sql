--
-- this should be the first statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.16', NULL)
 GO

--------------------------------------------------------------
-- delete all records in Person.TaxFileNumber
-- since this column format has been changed
--------------------------------------------------------------
UPDATE person SET TaxFileNumber=null;

--------------------------------------------------------------
-- change column TaxFileNumber length from 9 to 11
--------------------------------------------------------------
ALTER TABLE person ALTER COLUMN TaxFileNumber varchar(11);

--
-- this should be the last statement in any update script
--
UPDATE DBVersion 
 SET PreviousVersion = 'FPS.01.15'
 WHERE CurrentVersion = 'FPS.01.16'
 GO
