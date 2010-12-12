------------------------------------------------
--	START changing existing data structure
------------------------------------------------

ALTER TABLE Person ALTER COLUMN FamilyName	varchar(32)	NULL;
ALTER TABLE Person ALTER COLUMN FirstName	varchar(32)	NULL;
ALTER TABLE Person ALTER COLUMN OtherGivenNames	varchar(32)	NULL;
ALTER TABLE Person ALTER COLUMN PreferredName	varchar(32)	NULL;


--IF EXISTS (SELECT name 
--	   FROM   sysobjects 
--	   WHERE  name = N'sp_add_clientperson' 
--	   AND 	  type = 'P')
--    DROP PROCEDURE sp_add_clientperson;
--GO

CREATE PROCEDURE sp_add_clientperson
	@FirstName varchar(30), 
	@FamilyName varchar(30) 
AS
	DECLARE @ID int
	
	INSERT INTO Object (ObjectTypeID) VALUES (3)
	SELECT @@IDENTITY
	SET @ID = @@IDENTITY

	INSERT INTO Person (PersonID, FirstName, FamilyName) 
	VALUES ( @ID, @FirstName, @FamilyName )
	
	INSERT INTO ClientPerson (ClientPersonID) 
	VALUES ( @ID );


--IF EXISTS (SELECT name 
--	   FROM   sysobjects 
--	   WHERE  name = N'sp_add_userperson' 
--	   AND 	  type = 'P')
--    DROP PROCEDURE sp_add_userperson;
--GO

CREATE PROCEDURE sp_add_userperson
	@AdviserTypeCodeID int,
	@LoginName varchar(32), 
	@LoginPassword varchar(32) 
AS
	DECLARE @ID int
	
	INSERT INTO Object (ObjectTypeID) VALUES (2)
	SELECT @@IDENTITY
	SET @ID = @@IDENTITY

	INSERT INTO Person (PersonID, FamilyName) VALUES ( @ID, @LoginName )
	
	INSERT INTO UserPerson (UserPersonID, AdviserTypeCodeID, LoginName, LoginPassword) 
	VALUES ( @ID, @AdviserTypeCodeID, @LoginName, @LoginPassword );


------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--


--
--				FOREIGN KEY
--


--
--				UNIQUE INDEX
--



--
--				INDEX
--


--
--				INSERT/UPDATE
--

-- creat new AdviserTypeCode for administrator
INSERT INTO AdviserTypeCode (AdviserTypeCodeID, AdviserTypeCodeDesc)
VALUES (4, 'Administrator');


-- create administrator user for User Maintenance
-- LoginPassword (blank encrypted = d41d8cd98f00b204e9800998ecf8427e)
EXECUTE sp_add_userperson 4, 'administrator', 'd41d8cd98f00b204e9800998ecf8427e';



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.14', 'FID.01.13');

