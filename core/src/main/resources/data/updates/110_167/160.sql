ALTER TABLE Model ADD ModelData2 ntext NULL;
UPDATE Model SET ModelData2 = ModelData;

ALTER TABLE Model DROP COLUMN ModelData;
ALTER TABLE Model ADD ModelData ntext NULL;
UPDATE Model SET ModelData = ModelData2;

ALTER TABLE Model DROP COLUMN ModelData2;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FID.01.60', 'FID.01.59');
