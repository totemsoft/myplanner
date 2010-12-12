--if exists (select * from sysobjects where id = object_id(N'vUserPersonClients') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vUserPersonClients
--GO
CREATE VIEW vUserPersonClients
 AS

 	SELECT 
	client.AdviserID,
	client.PersonId,
	client.SexCodeID,
	client.TitleCodeID,
	client.FamilyName,
	client.FirstName,
	client.OtherGivenNames,
	client.PreferredName,
	client.MaritalCodeID,
	client.DateOfBirth,
	address.addressID,
	address.addressCodeID,
	address.StreetNumber,
	address.StreetNumber2,
	address.Suburb,
	address.PostCode,
	address.CountryCodeid,
	address.StateCodeId,
	client.AdvisorFamilyName,
	client.AdvisorFirstName,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Phone         FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 1 AND   linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as Phone,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         PhoneWork     FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 2 AND        linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as PhoneWork,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Fax           FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 3 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as Fax,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         FaxWork       FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 4 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as FaxWork,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Mobile        FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 5 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as Mobile,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         MobileWork    FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 6 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as MobileWork,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Email         FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 7 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId ORDER BY ContactMediaID DESC ) as Email,
	(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         EmailWork     FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 8 AND        linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ORDER BY ContactMediaID DESC ) as EmailWork 
	FROM ( SELECT client.SexCodeID, client.TitleCodeID, client.FamilyName, client.FirstName,
	client.OtherGivenNames, client.PreferredName, client.MaritalCodeID, client.DateOfBirth,
	client.PersonId,advisor.FamilyName AdvisorFamilyName, advisor.FirstName AdvisorFirstName,
	advisor.PersonId AdviserID
	FROM Person as client ,Link as c ,ClientPerson as b ,Person as advisor ,UserPerson as d
	WHERE
	ClientPersonID = client.PersonID AND client.PersonID = objectID2 AND
	LinkObjectTypeID = 2003 AND advisor.PersonID = UserPersonID AND
	advisor.PersonID = objectID1 AND c.LogicallyDeleted IS NULL 
	) as Client LEFT OUTER JOIN
	( SELECT
	address.addressID,address.addressCodeID,address.StreetNumber,
	address.StreetNumber2,address.Suburb,address.PostCode,
	address.CountryCodeid,address.StateCodeId,a.ObjectID1 PersonID
	FROM Address as address ,Link as a
	WHERE address.addressCodeID = 1 AND a.objectID2 = address.addressID AND LinkObjectTypeID = 1005 AND a.LogicallyDeleted IS NULL 
	) Address ON client.personID =  Address.personID WHERE client.AdviserID > 0  

GO


--if exists (select * from sysobjects where id = object_id(N'vUserManagmentDataProvider') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vUserManagmentDataProvider
--GO

#ifdef MSSQL
CREATE VIEW vUserManagmentDataProvider
 AS
 SELECT 
 	TOP 100 PERCENT 
    a.UserPersonID AS UserPersonID, a.AdviserTypeCodeID AS AdviserTypeCodeID, a.LoginName AS LoginName, 
	a.LoginPassword AS LoginPassword, b.PersonID AS PersonID, b.TitleCodeID AS TitleCodeID, b.FamilyName AS FamilyName, 
	b.FirstName AS FirstName, b.FamilyName + ' ' + b.FirstName AS FullName, b.OtherGivenNames AS OtherGivenNames, 
	b.PreferredName AS PreferredName, d.AddressID AS AddressID, 2 AS AddressCodeID, d.StreetNumber AS StreetNumber, 
	d.StreetNumber2 AS StreetNumber2, d.Suburb AS Suburb, d.Postcode AS Postcode, d.StateCodeID AS StateCodeID, d.CountryCodeID AS CountryCodeID,
	d.State AS State, d.DateCreated AS DateCreated, 
	GETDATE() AS DateModified, 
	e.ContactMediaID AS PhoneContactMediaID, 
	2 AS PhoneContactMediaCodeID, e.Value1 AS PhoneValue1, e.Value2 AS PhoneValue2, e.ContactMediaDesc AS PhoneContactMediaDesc, 
	e.DateCreated AS PhoneDateCreated, g.ContactMediaID AS MobileContactMediaID, 6 AS MobileContactMediaCodeID, g.Value1 AS MobileValue1, 
	g.Value2 AS MobileValue2, g.ContactMediaDesc AS MobileContactMediaDesc, g.DateCreated AS MobileDateCreated, 
	l.ContactMediaID AS EmailContactMediaID, 8 AS EmailContactMediaCodeID, l.Value1 AS EmailValue1, l.Value2 AS EmailValue2, 
	l.ContactMediaDesc AS EmailContactMediaDesc, l.DateCreated AS EmailDateCreated
 FROM    (SELECT	AddressID, AddressCodeID, StreetNumber, StreetNumber2, Suburb, Postcode, StateCodeID, CountryCodeID, DateCreated, DateModified, 
	        State, ObjectID1 PersonID
	FROM          Address ad, Link ln
	WHERE      ad.AddressID = ln.ObjectID2 AND ln.LinkObjectTypeID = 1005 AND ad.AddressCodeID = 2 AND ln.LogicallyDeleted IS NULL) 
	d RIGHT OUTER JOIN
	  (SELECT     ContactMediaID, ContactMediaCodeID, Value1, Value2, ContactMediaDesc, DateCreated, DateModified, ObjectID1 PersonID
	    FROM          ContactMedia cm, Link ln
	    WHERE      cm.ContactMediaID = ln.ObjectID2 AND ln.LinkObjectTypeID = 1006 AND cm.ContactMediaCodeID = 8 AND ln.LogicallyDeleted IS NULL) 
	l RIGHT OUTER JOIN
	  (SELECT     ContactMediaID, ContactMediaCodeID, Value1, Value2, ContactMediaDesc, DateCreated, DateModified, ObjectID1 PersonID
	    FROM          ContactMedia cm, Link ln
	    WHERE      cm.ContactMediaID = ln.ObjectID2 AND ln.LinkObjectTypeID = 1006 AND cm.ContactMediaCodeID = 6 AND ln.LogicallyDeleted IS NULL) 
	g RIGHT OUTER JOIN
	Person f INNER JOIN
	Person c INNER JOIN
	Person b INNER JOIN
	UserPerson a ON b.PersonID = a.UserPersonID ON c.PersonID = a.UserPersonID ON f.PersonID = a.UserPersonID INNER JOIN
	Person k ON a.UserPersonID = k.PersonID ON g.PersonID = f.PersonID ON l.PersonID = k.PersonID ON d.PersonID = b.PersonID LEFT OUTER JOIN
	  (SELECT     ContactMediaID, ContactMediaCodeID, Value1, Value2, ContactMediaDesc, DateCreated, DateModified, ObjectID1 PersonID
	    FROM          ContactMedia cm, Link ln
	    WHERE      cm.ContactMediaID = ln.ObjectID2 AND ln.LinkObjectTypeID = 1006 AND cm.ContactMediaCodeID = 2 AND ln.LogicallyDeleted IS NULL) 
	e ON c.PersonID = e.PersonID
 WHERE  (a.ActiveUser <> 'N') OR (a.ActiveUser IS NULL)
 ORDER BY k.FamilyName

#endif MSSQL
GO

-- SELECT * FROM vUserManagmentDataProvider


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.64', 'FPS.01.63');
