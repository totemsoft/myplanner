ALTER TABLE StrategyGroup ADD StrategyGroupData image NULL; 
ALTER TABLE StrategyGroup ADD DateCreated datetime NOT NULL DEFAULT getDate(); 
ALTER TABLE StrategyGroup ADD DateModified datetime NULL; 


DELETE FROM StrategyFinancial;
DELETE FROM StrategyModel;
DELETE FROM Strategy;

UPDATE Financial SET StrategyGroupID = NULL;

--select * from LinkObjectType
--select * from StrategyGroup
DELETE FROM StrategyGroup;
DELETE FROM Link WHERE LinkObjectTypeID = 1032;
DELETE FROM Object WHERE ObjectTypeID = 1032;
DELETE FROM Object WHERE ObjectTypeID = 32;

DROP TABLE StrategyFinancial;
DROP TABLE StrategyModel;
DROP TABLE Strategy;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.47', 'FID.01.46');

