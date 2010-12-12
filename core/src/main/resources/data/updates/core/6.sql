-- creat new AdviserTypeCode for administrator
INSERT INTO AdviserTypeCode (AdviserTypeCodeID, AdviserTypeCodeDesc)
 VALUES (4, 'Administrator');


-- create administrator user for User Maintenance (10000)
-- LoginPassword (blank encrypted = NULL)
INSERT INTO Object (ObjectTypeID) VALUES (4);
INSERT INTO Person (PersonID,FamilyName) VALUES (10000,'Administrator');
INSERT INTO UserPerson (UserPersonID, AdviserTypeCodeID,LoginName,LoginPassword) VALUES (10000,1,'admin',null);

-- create FIRST users - financial adviser (myself=9999)
INSERT INTO Object (ObjectTypeID) VALUES (2);
INSERT INTO Person (PersonID,FamilyName,FirstName) VALUES (9999,'Shibaev','Valera');
INSERT INTO UserPerson (UserPersonID, AdviserTypeCodeID,LoginName,LoginPassword) VALUES (9999,1,'valeri',null);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.00.06', 'FPS.00.05');
