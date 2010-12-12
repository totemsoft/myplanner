--drop view [vPersonAddress];

CREATE VIEW vPersonAddress
 AS
 SELECT     Address.AddressID, Person.PersonID, Link.LinkObjectTypeID, Address.AddressCodeID
 FROM       Link INNER JOIN
                      Person ON Link.ObjectID1 = Person.PersonID INNER JOIN
                      Address ON Link.ObjectID2 = Address.AddressID
 WHERE     (Link.LinkObjectTypeID = 1005)
;

DELETE  FROM Address
 WHERE AddressID IN (
 SELECT AddressID
 FROM Link INNER JOIN
	Person ON ObjectID1 = PersonID INNER JOIN
	Address ON ObjectID2 = AddressID
 WHERE   (LinkObjectTypeID = 1005)
	AND ( AddressID <> (SELECT max(AddressID) FROM vPersonAddress WHERE (PersonID = Person.PersonID) AND (AddressCodeID = Address.AddressCodeID) ) )
)
;

drop view vPersonAddress;


ALTER TABLE Financial ADD OngoingFee numeric(15,4) NULL;
ALTER TABLE Asset ADD TaxDeductibleAnnualAmount numeric(15,4) NULL; 
ALTER TABLE Asset ADD TaxDeductibleRegularAmount numeric(15,4) NULL; 


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.41', 'FPS.01.40');
