if exists (select * from sysobjects where id = object_id(N'[vPersonAddress]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [vPersonAddress]
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW vPersonAddress
AS
SELECT     TOP 100 PERCENT Address.AddressID, Person.PersonID, Link.LinkObjectTypeID, Address.AddressCodeID
FROM         Link INNER JOIN
                      Person ON Link.ObjectID1 = Person.PersonID INNER JOIN
                      Address ON Link.ObjectID2 = Address.AddressID
WHERE     (Link.LinkObjectTypeID = 1005)

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


DELETE  FROM Address
WHERE AddressID IN (
SELECT AddressID
FROM Link INNER JOIN
	Person ON ObjectID1 = PersonID INNER JOIN
	Address ON ObjectID2 = AddressID
WHERE   (LinkObjectTypeID = 1005)
	AND ( AddressID <> (SELECT max(AddressID) FROM vPersonAddress WHERE (PersonID = Person.PersonID) AND (AddressCodeID = Address.AddressCodeID) ) )
)
