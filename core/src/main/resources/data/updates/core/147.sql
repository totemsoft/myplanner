ALTER TABLE StrategyGroup ADD StrategyGroupData 
#ifdef MSSQL
	image NULL
#endif MSSQL
#ifdef HSQLDB
    varchar(8192)
#endif HSQLDB
; 
ALTER TABLE StrategyGroup ADD DateCreated datetime NOT NULL 
#ifdef MSSQL
	DEFAULT getDate()
#endif MSSQL
#ifdef HSQLDB

#endif HSQLDB
; 
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
VALUES ('FPS.01.47', 'FPS.01.46');

