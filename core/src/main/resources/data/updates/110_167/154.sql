-- use StateCodeID for AU and State for others
ALTER TABLE Address ADD ParentAddressID int NULL;
ALTER TABLE Address ADD State varchar(50) NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FID.01.54', 'FID.01.53');
