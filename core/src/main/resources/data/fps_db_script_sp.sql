USE MyPlanner
GO

IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_init_object' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_init_object
GO

-- =============================================================
-- Create procedure 
-- to reserve first 10000 ID's in object table (ObjectTypeID = 0)
-- =============================================================
CREATE PROCEDURE sp_init_object
AS
	DECLARE @total int

	SELECT @total = COUNT(ObjectID) FROM Object

	IF (@total > 0) 
	BEGIN
   		RAISERROR ('sp_init_object expects blank database', 16, 1)
		RETURN
	END

	SET @total = 10000
	
	WHILE (@total > 0)
	BEGIN
		INSERT INTO Object (ObjectTypeID) VALUES (0)
		SET @total = @total - 1
	END

GO

-- =============================================
-- example to execute the store procedure
-- =============================================
--EXECUTE sp_init_object
--GO

