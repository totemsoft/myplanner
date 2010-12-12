------------------------------------------------
--	START changing existing data structure
------------------------------------------------

ALTER TABLE Financial DROP COLUMN Amount2Disperse;


------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--


--
--				FOREIGN KEY
--
ALTER TABLE FinancialPool DROP
  CONSTRAINT FK_FinancialPool_Object;

ALTER TABLE FinancialPool ADD
  CONSTRAINT FK_FinancialPool_Financial FOREIGN KEY (FinancialPoolID) REFERENCES Financial (FinancialID);


--
--				UNIQUE INDEX
--



--
--				INDEX
--


--
--				INSERT/UPDATE
--

DELETE LinkObjectType WHERE LinkObjectTypeID=1031;
DELETE ObjectType WHERE ObjectTypeID=1031;
DELETE ObjectType WHERE ObjectTypeID=31;



INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) 
VALUES (31, 'FinancialPool table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) 
VALUES (29031, 'Link table (Model to FinancialPool');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (29031, 29, 31, 'Link Model to FinancialPool');

