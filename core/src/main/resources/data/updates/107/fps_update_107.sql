------------------------------------------------
--	START changing existing data structure
------------------------------------------------

DELETE FROM AnticipatedPayment;
DELETE FROM AnticipatedTransfer;
GO

DELETE FROM Financial WHERE FinancialID IN (SELECT AnticipatedID FROM Anticipated);
--DELETE FROM Financial WHERE FinancialID IN (10682, 11375, 11379, 11383, 11730, 11349, 11734);
--DELETE FROM Financial WHERE FinancialID IN (10106, 10110);
DELETE FROM Anticipated;
GO

DROP TABLE AnticipatedPayment;
DROP TABLE AnticipatedTransfer;
DROP TABLE Anticipated;
GO

DELETE FROM Link WHERE LinkObjectTypeID = 1018;
DELETE FROM Link WHERE LinkObjectTypeID = 1019;
DELETE FROM Link WHERE LinkObjectTypeID = 1023;
--DELETE FROM Link WHERE ObjectID2 IN (10682, 11375, 11379, 11383, 11730, 11349, 11734);
--DELETE FROM Link WHERE ObjectID2 IN (10106, 10110);
GO

DELETE FROM Object WHERE ObjectTypeID = 1018;
DELETE FROM Object WHERE ObjectTypeID = 1019;
DELETE FROM Object WHERE ObjectTypeID = 1023;
--DELETE FROM Object WHERE ObjectID IN (10682, 11375, 11379, 11383, 11730, 11349, 11734);
--DELETE FROM Object WHERE ObjectID IN (10106, 10110);
GO

DELETE FROM Link WHERE LinkObjectTypeID = 1025018;
DELETE FROM Link WHERE LinkObjectTypeID = 1025019;
DELETE FROM Link WHERE LinkObjectTypeID = 1025023;
GO

DELETE FROM Object WHERE ObjectTypeID = 18;
DELETE FROM Object WHERE ObjectTypeID = 19;
DELETE FROM Object WHERE ObjectTypeID = 23;
GO

DELETE FROM Object WHERE ObjectTypeID = 1025018;
DELETE FROM Object WHERE ObjectTypeID = 1025019;
DELETE FROM Object WHERE ObjectTypeID = 1025023;
GO

DELETE FROM LinkObjectType WHERE LinkObjectTypeID = 1025018;
DELETE FROM LinkObjectType WHERE LinkObjectTypeID = 1025019;
DELETE FROM LinkObjectType WHERE LinkObjectTypeID = 1025023;
GO

DELETE FROM ObjectType WHERE ObjectTypeID = 1025018;
DELETE FROM ObjectType WHERE ObjectTypeID = 1025019;
DELETE FROM ObjectType WHERE ObjectTypeID = 1025023;
GO

DELETE FROM LinkObjectType WHERE ObjectTypeID2 = 18;
DELETE FROM LinkObjectType WHERE ObjectTypeID2 = 19;
DELETE FROM LinkObjectType WHERE ObjectTypeID2 = 23;
GO

DELETE FROM FinancialCode WHERE FinancialTypeID IN (SELECT FinancialTypeID FROM FinancialType WHERE ObjectTypeID = 18);
DELETE FROM FinancialCode WHERE FinancialTypeID IN (SELECT FinancialTypeID FROM FinancialType WHERE ObjectTypeID = 19);
GO

DELETE FROM FinancialType WHERE ObjectTypeID = 18;
DELETE FROM FinancialType WHERE ObjectTypeID = 19;
GO

DELETE FROM ObjectType WHERE ObjectTypeID = 18;
DELETE FROM ObjectType WHERE ObjectTypeID = 19;
DELETE FROM ObjectType WHERE ObjectTypeID = 23;
GO

--select * FROM Object WHERE ObjectTypeID IN (18, 19);

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
CREATE INDEX IDX_Object_ObjectTypeID ON Object (ObjectTypeID);

CREATE INDEX IDX_Link_ObjectID1 ON Link (ObjectID1);
CREATE INDEX IDX_Link_ObjectID2 ON Link (ObjectID2);
CREATE INDEX IDX_Link_LinkObjectTypeID ON Link (LinkObjectTypeID);

CREATE INDEX IDX_PersonHealth_PersonID ON PersonHealth (PersonID);
CREATE INDEX IDX_PersonHealth_NextID ON PersonHealth (NextID);

CREATE INDEX IDX_PersonOccupation_PersonID ON PersonOccupation (PersonID);
CREATE INDEX IDX_PersonOccupation_NextID ON PersonOccupation (NextID);

CREATE INDEX IDX_PersonTrustDIYStatus_PersonID ON PersonTrustDIYStatus (PersonID);
CREATE INDEX IDX_PersonTrustDIYStatus_NextID ON PersonTrustDIYStatus (NextID);


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (30, 'PersonOccupation table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1004030, 'Link table (Person_Business to Occupation)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1004030, 1004, 30, 'Link Person_Business to Occupation');



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.07', 'FID.01.06');

