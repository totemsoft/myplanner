--if exists (select * from sysobjects where id = object_id(N'[vPersonContactMedia]') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view [vPersonContactMedia];

CREATE VIEW vPersonContactMedia
 AS
 SELECT     Person.PersonID, ContactMedia.*, Link.LinkID, Link.LogicallyDeleted
 FROM       Link INNER JOIN
                      Person ON Link.ObjectID1 = Person.PersonID INNER JOIN
                      ContactMedia ON Link.ObjectID2 = ContactMedia.ContactMediaID
 WHERE     (Link.LinkObjectTypeID = 1006);


--SELECT * FROM Person WHERE FamilyName LIKE '%Eth%'
--SELECT * FROM vPersonContactMedia WHERE PersonID = 10712;


--DELETE FROM ContactMedia WHERE ContactMediaID IN (
--SELECT * FROM ContactMedia WHERE ContactMediaID IN (
UPDATE Link SET LogicallyDeleted = 'Y' WHERE LinkID IN (
 SELECT LinkID
-- , LogicallyDeleted
 FROM Link INNER JOIN
	Person ON ObjectID1 = PersonID INNER JOIN
	ContactMedia ON ObjectID2 = ContactMediaID
 WHERE   (LinkObjectTypeID = 1006)
	AND ( ContactMediaID <> ( SELECT max(ContactMediaID) 
		FROM vPersonContactMedia WHERE (PersonID = Person.PersonID)
		AND (ContactMediaCodeID = ContactMedia.ContactMediaCodeID) ) )
);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.52', 'FPS.01.51');

