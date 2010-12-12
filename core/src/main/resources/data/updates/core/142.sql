--if exists (select * from sysobjects where id = object_id(N'[vAdvisorClients]') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view [vAdvisorClients]
--GO


CREATE VIEW vAdviserClients
 AS
 SELECT     Adviser.PersonID AS AdviserID, Client.PersonID, Client.SexCodeID, Client.TitleCodeID, Client.FamilyName AS ClientFamilyName, 
                      Client.FirstName AS ClientFirstName, Client.OtherGivenNames, Client.PreferredName, Client.MaritalCodeID, Client.DateOfBirth, Address.AddressID,
                       AddressCode.AddressCodeID, Address.StreetNumber, Address.StreetNumber2, Address.Suburb, Address.Postcode, 
                      Address.CountryCodeID, Address.StateCodeID, Adviser.FamilyName AS AdviserFamilyName, Adviser.FirstName AS AdviserFirstName,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 1 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS Phone,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 2 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS PhoneWork,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 3 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS Fax,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 4 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS FaxWork,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 5 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS Mobile,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 6 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS MobileWork,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 7 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS Email,
                          (SELECT     TOP 1 Value2
                            FROM          ContactMedia, Link l
                            WHERE      ContactMediaCodeID = 8 AND LinkObjecttypeID = 1006 AND ObjectID2 = ContactMediaID AND l.ObjectID1 = Client.PersonID
                            ORDER BY ContactMediaID DESC) AS EmailWork
 FROM         Person Adviser INNER JOIN
                      Link LinkAdvisorClient ON Adviser.PersonID = LinkAdvisorClient.ObjectID1 INNER JOIN
                      Person Client ON LinkAdvisorClient.ObjectID2 = Client.PersonID INNER JOIN
                      Link LinkClientAddress ON Client.PersonID = LinkClientAddress.ObjectID1 INNER JOIN
                      Address ON LinkClientAddress.ObjectID2 = Address.AddressID INNER JOIN
                      AddressCode ON Address.AddressCodeID = AddressCode.AddressCodeID
 WHERE     (LinkAdvisorClient.LinkObjectTypeID = 2003) AND (LinkAdvisorClient.LogicallyDeleted IS NULL) AND 
                      (LinkClientAddress.LinkObjectTypeID = 1005) AND (LinkClientAddress.LogicallyDeleted IS NULL)
;


ALTER TABLE Financial ADD Indexation2 numeric(15,4) NULL;
ALTER TABLE Financial ADD Expense numeric(15,4) NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.42', 'FPS.01.41');

