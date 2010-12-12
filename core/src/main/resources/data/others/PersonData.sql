if exists (select * from dbo.sysobjects where id = object_id(N'Business_VIEW') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view Business_VIEW;

CREATE VIEW Business_VIEW
AS
SELECT     TOP 100 PERCENT dbo.MaritalCode.MaritalCodeDesc, dbo.Person.PersonID, dbo.Person.FamilyName, dbo.Person.FirstName, 
                      dbo.PersonOccupation.PersonOccupationID, dbo.SexCode.SexCodeDesc, dbo.TitleCode.TitleCodeDesc, dbo.Person.DateOfBirth, 
                      dbo.EmploymentStatusCode.EmploymentStatusCodeDesc, dbo.OccupationCode.OccupationCodeDesc, dbo.Business.LegalName
FROM         dbo.PersonOccupation INNER JOIN
                      dbo.EmploymentStatusCode ON dbo.PersonOccupation.EmploymentStatusCodeID = dbo.EmploymentStatusCode.EmploymentStatusCodeID INNER JOIN
                      dbo.OccupationCode ON dbo.PersonOccupation.OccupationCodeID = dbo.OccupationCode.OccupationCodeID RIGHT OUTER JOIN
                      dbo.Person LEFT OUTER JOIN
                      dbo.Business ON force.get_business_id(dbo.Person.PersonID) = dbo.Business.BusinessID ON 
                      dbo.PersonOccupation.PersonID = dbo.Person.PersonID LEFT OUTER JOIN
                      dbo.MaritalCode ON dbo.Person.MaritalCodeID = dbo.MaritalCode.MaritalCodeID LEFT OUTER JOIN
                      dbo.TitleCode ON dbo.Person.TitleCodeID = dbo.TitleCode.TitleCodeID LEFT OUTER JOIN
                      dbo.SexCode ON dbo.Person.SexCodeID = dbo.SexCode.SexCodeID
WHERE     (dbo.PersonOccupation.NextID IS NULL)
ORDER BY dbo.Person.FamilyName, dbo.Person.FirstName;



if exists (select * from dbo.sysobjects where id = object_id(N'get_business_id') and xtype in (N'FN', N'IF', N'TF'))
 drop function get_business_id;

CREATE FUNCTION get_business_id  (@PersonID int)
RETURNS int
AS
BEGIN
	DECLARE @PERSON_2_BUSINESS int
	SET @PERSON_2_BUSINESS = 1004
	
	DECLARE @PERSON_2_BUSINESS_2_OCCUPATION int
	SET @PERSON_2_BUSINESS_2_OCCUPATION = 1004030

	DECLARE c CURSOR
	FOR
	SELECT ObjectID2 AS BusinessID
	FROM Link
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = @PERSON_2_BUSINESS )
	 AND ( LinkID IN
	     (SELECT ObjectID1
	     FROM Link
	     WHERE
	     	( ObjectID2 IS NULL )
	        AND ( LinkObjectTypeID = @PERSON_2_BUSINESS_2_OCCUPATION )
	        AND ( LogicallyDeleted IS NULL )
	     )
	 AND ( LogicallyDeleted IS NULL )
	 )

   	DECLARE @ID int

	OPEN c 
	FETCH NEXT FROM c INTO @ID
	CLOSE c 
	DEALLOCATE c

   	RETURN(@ID)
END
;


SELECT * FROM Business 
WHERE 
BusinessID = force.get_business_id( 10968 );
