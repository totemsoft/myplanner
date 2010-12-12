if exists (select * from sysobjects where id = object_id(N'[vPersonAddress]') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view [vPersonAddress];

--SET QUOTED_IDENTIFIER ON 
--GO
--SET ANSI_NULLS ON 
--GO

CREATE VIEW vPersonAddress
 AS
 SELECT     Address.AddressID, Person.PersonID, Link.LinkObjectTypeID, Address.AddressCodeID
 FROM       Link INNER JOIN
                      Person ON Link.ObjectID1 = Person.PersonID INNER JOIN
                      Address ON Link.ObjectID2 = Address.AddressID
 WHERE     (Link.LinkObjectTypeID = 1005);

--SET QUOTED_IDENTIFIER OFF 
--GO
--SET ANSI_NULLS ON 
--GO


DELETE  FROM Address
 WHERE AddressID IN (
 SELECT AddressID
 FROM Link INNER JOIN
	Person ON ObjectID1 = PersonID INNER JOIN
	Address ON ObjectID2 = AddressID
 WHERE   (LinkObjectTypeID = 1005)
	AND ( AddressID <> (SELECT max(AddressID) FROM vPersonAddress WHERE (PersonID = Person.PersonID) AND (AddressCodeID = Address.AddressCodeID) ) )
);

if exists (select * from sysobjects where id = object_id(N'[vPersonAddress]') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view [vPersonAddress];


ALTER TABLE Financial ADD OngoingFee numeric(15,4) NULL;

ALTER TABLE Asset ADD TaxDeductibleAnnualAmount numeric(15,4) NULL; 
ALTER TABLE Asset ADD TaxDeductibleRegularAmount numeric(15,4) NULL; 


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.41', 'FID.01.40');
