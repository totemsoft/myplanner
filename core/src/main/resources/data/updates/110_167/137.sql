if exists (select * from dbo.sysobjects where id = object_id(N'get_business_id') and xtype in (N'FN', N'IF', N'TF'))
 drop function get_business_id;

CREATE FUNCTION get_business_id (@PersonID int)
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


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.37', 'FID.01.36');