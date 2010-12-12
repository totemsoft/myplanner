-- create FIRST users - financial adviser (myself=10001)
DECLARE @ID int;

INSERT INTO Object (ObjectTypeID) VALUES (2)
SELECT @@IDENTITY
SET @ID = @@IDENTITY
INSERT INTO Person (PersonID) VALUES (@ID)
INSERT INTO UserPerson (UserPersonID, AdviserTypeCodeID,LoginName,LoginPassword) VALUES (@ID, 1,'valeri',null);
