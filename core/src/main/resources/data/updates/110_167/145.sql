ALTER TABLE ClientPerson ADD FeeDate datetime NULL;
ALTER TABLE ClientPerson ADD ReviewDate datetime NULL;

ALTER TABLE FinancialGoal ADD LumpSumRequired numeric(15, 4) NULL;
ALTER TABLE FinancialGoal ADD Notes text NULL;
ALTER TABLE FinancialGoal ADD Inflation numeric(15, 4) NULL;
ALTER TABLE FinancialGoal ADD TargetStrategyID int NULL; 

if exists (select * from dbo.sysobjects where id = object_id(N'Category') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 drop table Category;

CREATE TABLE Category (
	CategoryID int IDENTITY (1, 1) NOT NULL ,
	CategoryName varchar (50) NULL ,
	CategoryDesc varchar (50) NULL 
);

if exists (select * from dbo.sysobjects where id = object_id(N'SelectedCategory') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 drop table SelectedCategory;

CREATE TABLE SelectedCategory (
	CategoryID int NOT NULL ,
	PersonID int NOT NULL 
);

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.45', 'FID.01.44');

