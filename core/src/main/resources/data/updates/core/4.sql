INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.00.04', NULL)
 GO

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
 VALUES (0, '***** reserved *****');

--IF EXISTS (SELECT name 
--	   FROM   sysobjects 
--	   WHERE  name = N'sp_init_object' 
--	   AND 	  type = 'P')
--    DROP PROCEDURE sp_init_object
--GO

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


-- =================================================================================== --
-- Reserve first 10000 ID's in object table (ObjectTypeID = 0)
-- =================================================================================== --
EXECUTE sp_init_object;

DROP PROCEDURE sp_init_object;


--
-- this should be the last statement in any update script
--
UPDATE DBVersion 
 SET PreviousVersion = 'FPS.00.03'
 WHERE CurrentVersion = 'FPS.00.04'
 GO
