--
-- this should be the first statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.02.00', null)
 GO

--------------------------------------------------------------
-- create user - [trial] 9998
--------------------------------------------------------------
--INSERT INTO Object (ObjectTypeID) VALUES (2);
UPDATE Object SET ObjectTypeID = 2 WHERE ObjectID = 9998;
INSERT INTO Person (PersonID,FamilyName) VALUES (9998,'TRIAL');
INSERT INTO UserPerson (UserPersonID,AdviserTypeCodeID,LoginName,LoginPassword) VALUES (9998, 1,'trial',null);

--------------------------------------------------------------
-- create client - [trial] 9997
--------------------------------------------------------------
--INSERT INTO Object (ObjectTypeID) VALUES (3);
UPDATE Object SET ObjectTypeID = 3 WHERE ObjectID = 9997;
INSERT INTO Person (PersonID,FamilyName,FirstName) VALUES (9997,'TEST','TESTER');
INSERT INTO ClientPerson (ClientPersonID, ACTIVE) VALUES (9997, 'Y');
 
 -- link [trial] user to [TEST] client, USER_2_CLIENT = 2003
--INSERT INTO Object (ObjectTypeID) VALUES (2003);
UPDATE Object SET ObjectTypeID = 2003 WHERE ObjectID = 9996;
INSERT INTO Link (LinkID, ObjectID1, ObjectID2, LinkObjectTypeID) VALUES (
 9996, --LinkID
 9998, --(SELECT UserPersonID FROM UserPerson WHERE LoginName = 'trial'), 
 9997, --(SELECT PersonID FROM Person WHERE FamilyName = 'TEST' AND FirstName = 'TESTER'), 
 2003);

--
-- this should be the last statement in any update script
--
UPDATE DBVersion 
 SET PreviousVersion = 'FPS.01.99'
 WHERE CurrentVersion = 'FPS.02.00'
 GO
