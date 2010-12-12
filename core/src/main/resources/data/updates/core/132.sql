DELETE FROM LinkObjectType WHERE LinkObjectTypeID=29031; --Link table (Model to FinancialPool
DELETE FROM ObjectType WHERE ObjectTypeID=29031; --Link table (Model to FinancialPool

UPDATE ObjectType SET ObjectTypeDesc='PlanData table' WHERE ObjectTypeID=31;

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1031, 'Link table (Person to PlanData)');

INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1031, 1, 31, 'Link table (Person to PlanData)');


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.32', 'FPS.01.31');
