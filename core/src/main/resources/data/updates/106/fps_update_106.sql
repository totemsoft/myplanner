------------------------------------------------
--	START changing existing data structure
------------------------------------------------
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (27, 'RiskProfile table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (28, 'PersonEstate table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3028, 'Link table (Client to Estate)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (3028, 3, 28, 'Link Client to Estate');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (24027, 'Link table (Survey to RiskProfile)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (24027, 24, 27, 'Link Survey to RiskProfile');

ALTER TABLE Model ADD ModelDesc varchar(512);
ALTER TABLE Model ADD DateModified datetime;


DROP INDEX Model.IDX_Model_1;

------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--


--
--				FOREIGN KEY
--


--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.06', 'FID.01.05');

