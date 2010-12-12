#ifdef MSSQL
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[vUserPersonAddress]') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view [dbo].[vUserPersonAddress]
#endif MSSQL
;

CREATE VIEW vUserPersonAddress
 AS
 SELECT     
#ifdef MSSQL
 TOP 100 PERCENT 
#endif MSSQL
 UserPerson.*, Address.*
 FROM         Address INNER JOIN
                      Link ON Address.AddressID = Link.ObjectID2 RIGHT OUTER JOIN
                      UserPerson ON Link.ObjectID1 = UserPerson.UserPersonID
 ORDER BY UserPerson.UserPersonID
;


#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'[dbo].[vUserPersonContactMedia]') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view [dbo].[vUserPersonContactMedia]
#endif MSSQL
;

CREATE VIEW vUserPersonContactMedia
 AS
 SELECT     
#ifdef MSSQL
 TOP 100 PERCENT 
#endif MSSQL
 UserPerson.*, ContactMedia.*
 FROM         ContactMedia INNER JOIN
                      Link ON ContactMedia.ContactMediaID = Link.ObjectID2 RIGHT OUTER JOIN
                      UserPerson ON Link.ObjectID1 = UserPerson.UserPersonID
 ORDER BY UserPerson.UserPersonID
;



----------------------------------------------------------------------------------------------
--					address 					    --
----------------------------------------------------------------------------------------------
#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'sp_create_address') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_create_address
#endif MSSQL
;

#ifdef MSSQL
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

#endif MSSQL
;

#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'sp_temp') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_temp
#endif MSSQL
;

#ifdef MSSQL
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

#endif MSSQL
;

#ifdef MSSQL
	EXEC sp_temp
#endif MSSQL

----------------------------------------------------------------------------------------------
--					contact_media					    --
----------------------------------------------------------------------------------------------
#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'sp_create_contact_media') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_create_contact_media
#endif MSSQL
;

#ifdef MSSQL
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

#endif MSSQL
;

#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'sp_temp') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_temp
#endif MSSQL
;

#ifdef MSSQL
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
	
#endif MSSQL
;

#ifdef MSSQL
	EXEC sp_temp
#endif MSSQL
;

#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'sp_temp') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_temp
#endif MSSQL
;


-- SELECT * FROM Person WHERE OtherGivenNames = 'null';
UPDATE Person SET OtherGivenNames = NULL WHERE OtherGivenNames = 'null';

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.59', 'FPS.01.58');
