------------------------------------------------
--	START changing existing data structure
------------------------------------------------

-- will be removed in 108_1
ALTER TABLE Financial ADD Amount2Disperse numeric(15,4)	NULL;
--
-- use Amount2Disperse from all Financial's to generate single FinancialPool.Amount
-- use single FinancialPool.Amount to allocate to different FinancialModel(s)
--

--
--		FinancialPool::Financial (usefull extension to Financial table AS 0..1)
--
CREATE TABLE FinancialPool (
  FinancialPoolID	int 		NOT NULL,
  Amount		numeric(15,4)	NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
);
--
--		FinancialModel::Model
--
--
--CREATE TABLE Model (
--  ModelID		int 		NOT NULL,
--  ModelTypeID		int 		NOT NULL,
--  ModelTitle		varchar(64)	NOT NULL,
--  ModelDesc		varchar(512),
--  ModelData		text		NOT NULL,
--  DateModified		datetime,
--  DateCreated		datetime	NOT NULL DEFAULT getDate()
--);

-- will be dropped in 109 (StrategyModel introduced instead)
CREATE TABLE FinancialModel ( 
  FinancialModelID	int 		NOT NULL,
  Amount		numeric(15,4)	NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
);

------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--
ALTER TABLE FinancialPool ADD CONSTRAINT PK_FinancialPoolID PRIMARY KEY (FinancialPoolID);
ALTER TABLE FinancialModel ADD CONSTRAINT PK_FinancialModelID PRIMARY KEY (FinancialModelID);


--
--				FOREIGN KEY
--
ALTER TABLE FinancialPool ADD
  CONSTRAINT FK_FinancialPool_Object FOREIGN KEY (FinancialPoolID) REFERENCES Object (ObjectID);
ALTER TABLE FinancialModel ADD
  CONSTRAINT FK_FinancialModel_Model FOREIGN KEY (FinancialModelID) REFERENCES Model (ModelID);


--
--				UNIQUE INDEX
--

--CREATE UNIQUE INDEX IDX_Model_1 ON Model ( ModelTypeID, ModelTitle );


--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;

-- will be removed in 108_1
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) 
VALUES (31, 'FinancialPool table');
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) 
VALUES (1031, 'Link table (Person to FinancialPool');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1031, 1, 31, 'Link Person to FinancialPool');



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.08', 'FID.01.07');

