if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[vUserPersonAddress]') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view [dbo].[vUserPersonAddress]
;

CREATE VIEW dbo.vUserPersonAddress
 AS
 SELECT     TOP 100 PERCENT dbo.UserPerson.*, dbo.Address.*
 FROM         dbo.Address INNER JOIN
                      dbo.Link ON dbo.Address.AddressID = dbo.Link.ObjectID2 RIGHT OUTER JOIN
                      dbo.UserPerson ON dbo.Link.ObjectID1 = dbo.UserPerson.UserPersonID
 ORDER BY dbo.UserPerson.UserPersonID
;





if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[vUserPersonContactMedia]') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view [dbo].[vUserPersonContactMedia]
;

CREATE VIEW dbo.vUserPersonContactMedia
 AS
 SELECT     TOP 100 PERCENT dbo.UserPerson.*, dbo.ContactMedia.*
 FROM         dbo.ContactMedia INNER JOIN
                      dbo.Link ON dbo.ContactMedia.ContactMediaID = dbo.Link.ObjectID2 RIGHT OUTER JOIN
                      dbo.UserPerson ON dbo.Link.ObjectID1 = dbo.UserPerson.UserPersonID
 ORDER BY dbo.UserPerson.UserPersonID
;



----------------------------------------------------------------------------------------------
--					address 					    --
----------------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'sp_create_address') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_create_address
;

CREATE PROCEDURE sp_create_address 
	@PersonID int 
AS
	
	INSERT INTO Object (ObjectTypeID) VALUES (5)
	SELECT @@IDENTITY

	DECLARE @AddressID int
	SET @AddressID = @@IDENTITY

	INSERT INTO Address ( AddressID ) VALUES ( @AddressID )


	DECLARE @LinkID int
	
	INSERT INTO Object (ObjectTypeID) VALUES (1005)
	SELECT @@IDENTITY
	SET @LinkID = @@IDENTITY

	INSERT INTO Link ( LinkID, ObjectID1, ObjectID2, LinkObjectTypeID ) VALUES ( @LinkID, @PersonID, @AddressID, 1005 )

;

if exists (select * from dbo.sysobjects where id = object_id(N'sp_temp') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_temp
;

CREATE PROCEDURE sp_temp 
AS
	SELECT UserPersonID INTO #tblUP FROM vUserPersonAddress WHERE AddressID IS NULL

	DECLARE c CURSOR FOR
	SELECT UserPersonID FROM #tblUP
	FOR UPDATE

	OPEN c 
	
	DECLARE @UserPersonID int 
	
	FETCH NEXT FROM c INTO @UserPersonID
	WHILE @@FETCH_STATUS = 0
	BEGIN
		EXEC sp_create_address @UserPersonID
		FETCH NEXT FROM c INTO @UserPersonID
	END
	
	CLOSE c 
	DEALLOCATE c
	
	DROP TABLE #tblUP

;

EXEC sp_temp;

----------------------------------------------------------------------------------------------
--					contact_media					    --
----------------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'sp_create_contact_media') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_create_contact_media
;

CREATE PROCEDURE sp_create_contact_media 
	@PersonID int 
AS
	
	INSERT INTO Object (ObjectTypeID) VALUES (6)
	SELECT @@IDENTITY

	DECLARE @ContactMediaID int
	SET @ContactMediaID = @@IDENTITY

	INSERT INTO ContactMedia ( ContactMediaID ) VALUES ( @ContactMediaID )


	DECLARE @LinkID int
	
	INSERT INTO Object (ObjectTypeID) VALUES (1006)
	SELECT @@IDENTITY
	SET @LinkID = @@IDENTITY

	INSERT INTO Link ( LinkID, ObjectID1, ObjectID2, LinkObjectTypeID ) VALUES ( @LinkID, @PersonID, @ContactMediaID, 1006 )

;

if exists (select * from dbo.sysobjects where id = object_id(N'sp_temp') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_temp
;

CREATE PROCEDURE sp_temp 
AS
	SELECT UserPersonID INTO #tblCM FROM vUserPersonContactMedia WHERE ContactMediaID IS NULL

	DECLARE c CURSOR FOR
	SELECT UserPersonID FROM #tblCM
	FOR UPDATE

	OPEN c 
	
	DECLARE @UserPersonID int 
	
	FETCH NEXT FROM c INTO @UserPersonID
	WHILE @@FETCH_STATUS = 0
	BEGIN
		EXEC sp_create_contact_media @UserPersonID
		FETCH NEXT FROM c INTO @UserPersonID
	END
	
	CLOSE c 
	DEALLOCATE c
	
	DROP TABLE #tblCM
	
;

EXEC sp_temp;

if exists (select * from dbo.sysobjects where id = object_id(N'sp_temp') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_temp
;


-- SELECT * FROM Person WHERE OtherGivenNames = 'null';
UPDATE Person SET OtherGivenNames = NULL WHERE OtherGivenNames = 'null';

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.59', 'FID.01.58');
