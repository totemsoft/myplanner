--------------------------------------------------------------
-- BUG FIX 453 - 01/08/2002
--
-- Author: Joanne Wang
--------------------------------------------------------------

--------------------------------------------------------------
-- delete all records in Person.TaxFileNumber
-- since this column format has been changed
--------------------------------------------------------------
UPDATE person SET TaxFileNumber=null;

--------------------------------------------------------------
-- change column TaxFileNumber length from 9 to 11
--------------------------------------------------------------

ALTER TABLE person ALTER COLUMN [TaxFileNumber] [nvarchar] (11);

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.16', 'FID.01.15');