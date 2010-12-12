------------------------------------------------
--	START changing existing data structure
------------------------------------------------

ALTER TABLE PersonTrustDIYStatus ADD CompanyStatusCodeID int;



CREATE  TABLE Model (
  ModelID		int 		NOT NULL,
  ModelTypeID		int 		NOT NULL,
  ModelTitle		varchar(100)	NOT NULL,
  ModelData		text		NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
);

CREATE  TABLE ModelType (
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
ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_CompanyStatusCode FOREIGN KEY (CompanyStatusCodeID) REFERENCES StatusCode (StatusCodeID);

ALTER TABLE Model ADD
  CONSTRAINT FK_Model_Object FOREIGN KEY (ModelID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Model_ModelType FOREIGN KEY (ModelTypeID) REFERENCES ModelType (ModelTypeID);

--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1001, 'Link table (Person to Person');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1001, 1, 1, 'Link Person to Person');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1007, 'Link table (Person to Relationship');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1007, 1, 7, 'Link Person to Relationship');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1008, 'Link table (Person to RelationshipFinance');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1008, 1, 8, 'Link Person to RelationshipFinance');



INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (29, 'Model table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1029, 'Link table (Person to Model');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1029, 1, 29, 'Link Person to Model');

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.04', 'FID.01.03');

COMMIT;
