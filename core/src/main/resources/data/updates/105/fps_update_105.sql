------------------------------------------------
--	START changing existing data structure
------------------------------------------------

CREATE TABLE Model (
  ModelID		int 		NOT NULL,
  ModelTypeID		int 		NOT NULL,
  ModelTitle		varchar(64)	NOT NULL,
  ModelDesc		varchar(512),
  ModelData		text		NOT NULL,
  DateModified		datetime,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
);

CREATE TABLE ModelType (
  ModelTypeID		int 		NOT NULL,
  ModelTypeDesc		varchar(50)	NOT NULL
);

------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--
ALTER TABLE Model ADD CONSTRAINT PK_ModelID PRIMARY KEY (ModelID);
ALTER TABLE ModelType ADD CONSTRAINT PK_ModelTypeID PRIMARY KEY (ModelTypeID);


--
--				FOREIGN KEY
--
ALTER TABLE Model ADD
  CONSTRAINT FK_Model_Object FOREIGN KEY (ModelID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Model_ModelType FOREIGN KEY (ModelTypeID) REFERENCES ModelType (ModelTypeID);


--
--				UNIQUE INDEX
--

-- will be removed in 106 update
CREATE UNIQUE INDEX IDX_Model_1 ON Model ( ModelTypeID, ModelTitle );


--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (29, 'Model table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1029, 'Link table (Person to Model');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1029, 1, 29, 'Link Person to Model');


DELETE FROM ModelType;
GO

INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (1, 'QuickView');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (2, 'Insurance Needs');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (3, 'Premium Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (4, 'Investment Gearing');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (5, 'Projected Wealth');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (6, 'Investment Properties');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (7, 'Loan & Mortgage Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (8, 'Allocated Pension');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (9, 'ETP & Rollover');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (10, 'Superannuation RBL');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (11, 'Retirement Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (12, 'Retirement Home');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (13, 'PAYG Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (14, 'Capital Gains Tax (CGT)');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (15, 'Age Pension');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (16, 'Pension Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (17, 'Allowance Calculator');

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.05', 'FID.01.04');

