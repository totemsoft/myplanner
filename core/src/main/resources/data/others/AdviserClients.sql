SELECT     *
FROM         vAdviserClients
--WHERE     (ClientFamilyName = 'bruce')
ORDER BY AdviserID, PersonID

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
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Phone         FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 1 AND   linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as Phone,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         PhoneWork     FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 2 AND        linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as PhoneWork,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Fax           FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 3 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as Fax,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         FaxWork       FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 4 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as FaxWork,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Mobile        FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 5 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as Mobile,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         MobileWork    FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 6 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as MobileWork,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         Email         FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 7 AND         linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as Email,  
(SELECT TOP 1 Value2+CASE WHEN b.contactMediaDesc IS NOT NULL THEN '('+b.contactMediaDesc+')' ELSE '' END AS         EmailWork     FROM contactMedia b, link c  WHERE b.contactMediaCodeID = 8 AND        linkobjecttypeid = 1006 AND ObjectID2 = ContactMediaID and c.ObjectID1 = client.PersonId  ) as EmailWork   
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
