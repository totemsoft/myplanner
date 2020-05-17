--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.63', 'FPS.01.62');


---------------------------------------------------------------------------------------
-- 	initialize/uninitialize
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[IMPORT]') and OBJECTPROPERTY(id, N'IsTable') = 1)
 drop table IMPORT
GO
CREATE TABLE IMPORT (
  SessionID		int		NOT NULL,
  ValueName		varchar(32)	NOT NULL,
  ValueNew		varchar(32)	NULL,
  ValueOld		varchar(32)	NULL
)
GO

-- SELECT * FROM IMPORT

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_uninitialize]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_uninitialize]
GO
CREATE PROCEDURE [dbo].[sp_import_uninitialize]
	@UserPersonID int = 0
 AS
	DELETE FROM IMPORT WHERE SessionID = @@SPID
GO
-- EXEC sp_import_uninitialize 11815


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_initialize]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_initialize]
GO
CREATE PROCEDURE [dbo].[sp_import_initialize]
	@UserPersonID int
 AS
	EXEC sp_import_uninitialize @UserPersonID
GO
-- EXEC sp_import_initialize 11815
-- SELECT * FROM IMPORT


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_get]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_get]
GO
CREATE PROCEDURE [dbo].[sp_import_get]
	@ValueName varchar(32),
	@@ValueNew varchar(32) OUTPUT,
	@ValueOld varchar(32) = ''
 AS
	SET @@ValueNew = 0
	IF ( @ValueOld = '' )
		SELECT @@ValueNew = ValueNew FROM IMPORT WHERE SessionID = @@SPID AND ValueName = @ValueName
	ELSE
		SELECT @@ValueNew = ValueNew FROM IMPORT WHERE SessionID = @@SPID AND ValueName = @ValueName AND ValueOld = @ValueOld

GO
--DECLARE @@ValueNew varchar(32)
--EXEC sp_import_get 'UserPersonID', @@ValueNew


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_set]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_set]
GO
CREATE PROCEDURE [dbo].[sp_import_set]
	@ValueName varchar(32),
	@ValueNew varchar(32),
	@ValueOld varchar(32) = ''
 AS
	IF ( @ValueOld = '' )
	BEGIN
		IF exists ( SELECT * FROM IMPORT WHERE SessionID = @@SPID AND ValueName = @ValueName )
		 UPDATE IMPORT SET ValueNew = @ValueNew WHERE SessionID = @@SPID AND ValueName = @ValueName
		ELSE
		 INSERT INTO IMPORT ( SessionID, ValueName, ValueNew ) VALUES ( @@SPID, @ValueName, @ValueNew )
	END
	ELSE
	BEGIN
		IF exists ( SELECT * FROM IMPORT WHERE SessionID = @@SPID AND ValueName = @ValueName AND ValueOld = @ValueOld )
		 UPDATE IMPORT SET ValueNew = @ValueNew WHERE SessionID = @@SPID AND ValueName = @ValueName AND ValueOld = @ValueOld
		ELSE
		 INSERT INTO IMPORT ( SessionID, ValueName, ValueNew, ValueOld ) VALUES ( @@SPID, @ValueName, @ValueNew, @ValueOld )
	END

GO


---------------------------------------------------------------------------------------
-- 	TABLE	Link
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_create_Link]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_create_Link]
GO
CREATE PROCEDURE [dbo].[sp_create_Link] 
   @@LinkID int OUTPUT, 
   @ObjectID1 int, 
   @ObjectID2 int, 
   @LinkObjectTypeID int 
AS	
	INSERT INTO Object (ObjectTypeID) VALUES (@LinkObjectTypeID)
	SELECT @@LinkID = @@IDENTITY

	INSERT INTO Link ( 
	  LinkID, ObjectID1, ObjectID2, LinkObjectTypeID 
	) VALUES ( 
	  @@LinkID, @ObjectID1, @ObjectID2, @LinkObjectTypeID
	)
	
GO

--DECLARE @@LinkID int
--EXEC sp_create_Link @@LinkID OUTPUT, 31345, 31346, 1001
--SELECT @@LinkID AS 'LinkID'


---------------------------------------------------------------------------------------
-- 	TABLE	Reference Tables:
---------------------------------------------------------------------------------------
--		1).	FinancialService
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_FinancialService]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_FinancialService]
GO
CREATE PROCEDURE [dbo].[sp_import_FinancialService] 
	@FinancialServiceCode varchar(10),
	@FinancialServiceDesc varchar(255),
	@DateCreated datetime = NULL,
	@LogicallyDeleted char(1) = NULL
AS
	IF exists ( SELECT * FROM FinancialService WHERE FinancialServiceCode = @FinancialServiceCode )
		RETURN

	IF ( @DateCreated IS NULL )
		SET @DateCreated = getdate()

	INSERT INTO FinancialService 
	(FinancialServiceCode, FinancialServiceDesc, DateCreated, LogicallyDeleted) 
	VALUES 
	(@FinancialServiceCode, @FinancialServiceDesc, @DateCreated, @LogicallyDeleted)
GO

-- EXEC [dbo].[sp_import_FinancialService] 'FSS', 'Superannuation Services'


---------------------------------------------------------------------------------------
--		2).	Category
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Category]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Category]
GO
CREATE PROCEDURE [dbo].[sp_import_Category] 
	@CategoryID int,
	@CategoryName varchar(50),
	@CategoryDesc varchar(50)
AS
	IF exists ( SELECT * FROM Category 
		    WHERE CategoryName = @CategoryName AND CategoryDesc = @CategoryDesc )
		RETURN

	INSERT INTO Category 
	(CategoryName, CategoryDesc) 
	VALUES 
	(@CategoryName, @CategoryDesc)
GO


---------------------------------------------------------------------------------------
--		3).	PlanData templates
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_PlanDataTemplate]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_PlanDataTemplate]
GO
CREATE PROCEDURE [dbo].[sp_import_PlanDataTemplate] 
	@PlanDataID int,
	@PlanDataDesc varchar(255),
	@PlanDataText text,
	@PlanDataText2 text,
	@PlanTypeID int
AS
	IF exists ( SELECT * FROM PlanData 
		    WHERE PlanDataDesc = @PlanDataDesc AND PlanTypeID = @PlanTypeID )
		RETURN

	-- PLAN = 31
	INSERT INTO Object (ObjectTypeID) VALUES (31)
	SELECT @PlanDataID = @@IDENTITY

	-- GLOBAL_PLAN_TEMPLATE = 26
	-- PERSON_2_PLAN = 1031
	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, 26, @PlanDataID, 1031


	INSERT INTO PlanData ( 
	  PlanDataID
	, PlanDataDesc
	, PlanDataText
	, PlanDataText2
	, PlanTypeID
	) VALUES (
	  @PlanDataID
	, @PlanDataDesc
	, @PlanDataText
	, @PlanDataText2
	, @PlanTypeID
	)

GO


---------------------------------------------------------------------------------------
-- 	UserPerson
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_UserPerson]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_UserPerson]
GO
CREATE PROCEDURE [dbo].[sp_import_UserPerson] 
	@OwnerType1 varchar(32) = '', -- no owner for user
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32) = 'User',
	@FamilyName varchar(32),
	@FirstName varchar(32),
	@UserPersonID int = 0,
	@AdviserTypeCodeID int = 1, -- Franchise = 1
	@LoginName varchar(32) = '',
	@LoginPassword varchar(32) = NULL,
	@ActiveUser char(1) = NULL
AS
	-- search existing user by first/last names
	DECLARE @UserPersonID_Old int
	SELECT @UserPersonID_Old = MIN(UserPersonID) FROM vUserPerson 
	WHERE FamilyName = @FamilyName AND FirstName = @FirstName

	IF ( @UserPersonID_Old > 0 )
	BEGIN
		-- save to IMPORT table for future reference
		EXEC sp_import_set @OwnerType2, @UserPersonID_Old, @UserPersonID

		EXEC sp_import_set 'STATUS', 'UPDATE', @UserPersonID

	END
	ELSE
	BEGIN
		SET @UserPersonID_Old = @UserPersonID
	
		-- USER_PERSON = 2
		INSERT INTO Object (ObjectTypeID) VALUES (2)
		SELECT @UserPersonID = @@IDENTITY
	
		-- save to IMPORT table for future reference
		EXEC sp_import_set @OwnerType2, @UserPersonID, @UserPersonID_Old
	
		INSERT INTO Person (
		  PersonID
		) VALUES (
		  @UserPersonID
		)
	
		-- search new user by login name
		IF exists ( SELECT * FROM UserPerson WHERE LoginName = @LoginName )
		BEGIN
			SET @LoginName = @FirstName + @FamilyName
		  	--SET @LoginName = @LoginName + '_' + CAST( @UserPersonID AS varchar(10) )
		END

		INSERT INTO UserPerson (
		  UserPersonID
		, AdviserTypeCodeID
		, LoginName
		, LoginPassword
		, ActiveUser
		) VALUES (
		  @UserPersonID
		, @AdviserTypeCodeID
		, @LoginName
		, @LoginPassword
		, @ActiveUser
		)

		EXEC sp_import_set 'STATUS', 'INSERT', @UserPersonID

	END

GO


---------------------------------------------------------------------------------------
-- 	TABLE	ClientPerson
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_ClientPerson]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_ClientPerson]
GO
CREATE PROCEDURE [dbo].[sp_import_ClientPerson] 
	@OwnerType1 varchar(32) = 'User',
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32) = 'Client',
	--@FamilyName varchar(32) = '',
	--@FirstName varchar(32) = '',
	@ClientPersonID int = 0,
	@Active char(1) = 'N',
	@FeeDate datetime = NULL,
	@ReviewDate datetime = NULL
 AS
	DECLARE @ClientPersonID_Old int
	SET @ClientPersonID_Old = @ClientPersonID

	--SELECT @ClientPersonID_Old = MIN(UserPersonID) FROM vClientPerson 
	--  WHERE FamilyName = @FamilyName AND FirstName = @FirstName
	--IF ( @ClientPersonID_Old > 0 )
	--	SET @ClientPersonID_Old = @ClientPersonID_Old


	-- CLIENT_PERSON = 3
	INSERT INTO Object (ObjectTypeID) VALUES (3)
	SELECT @ClientPersonID = @@IDENTITY

	-- save to IMPORT table for future reference
	EXEC sp_import_set @OwnerType2, @ClientPersonID, @ClientPersonID_Old

	INSERT INTO Person (
	  PersonID
	) VALUES (
	  @ClientPersonID
	)

	INSERT INTO ClientPerson (
	  ClientPersonID
	, Active
	, FeeDate
	, ReviewDate
	) VALUES (
	  @ClientPersonID
	, @Active
	, @FeeDate
	, @ReviewDate
	)

	DECLARE @@UserPersonID int
	EXEC sp_import_get @OwnerType1, @@UserPersonID OUTPUT, @OwnerID

	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 2003 -- USER_2_CLIENT = 2003

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @@UserPersonID, @ClientPersonID, @LinkObjectTypeID

	EXEC sp_import_set 'STATUS', 'INSERT', @ClientPersonID

GO


---------------------------------------------------------------------------------------
-- 	TABLE	Person
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Person]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Person]
GO
CREATE PROCEDURE [dbo].[sp_import_Person] 
	@OwnerType1 varchar(32),
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32),
	@PersonID int = 0,
	@DateCreated datetime = NULL,
	@DateModified datetime = NULL,
	@SexCodeID int = NULL,
	@TitleCodeID int = NULL,
	@FamilyName varchar(32) = NULL,
	@FirstName varchar(32) = NULL,
	@OtherGivenNames varchar(32) = NULL,
	@PreferredName varchar(32) = NULL,
	@MaritalCodeID int = NULL,
	@DateOfBirth datetime = NULL,
	@DOBCountryID int = NULL,
	@TaxFileNumber varchar(11) = NULL,
	@PreferredLanguageID int = NULL,
	@PreferredLanguage varchar(20) = NULL,
	@ResidenceCountryCodeID int = NULL,
	@ResidenceStatusCodeID int = NULL,
	@ReferalSourceCodeID int = NULL,
	@SupportToAge int = NULL,
	@DSSRecipient char(1) = NULL,
	@RelationType int = NULL
 AS

	DECLARE @OwnerID_Old int
	SET @OwnerID_Old = @OwnerID
	EXEC sp_import_get @OwnerType1, @OwnerID OUTPUT, @OwnerID_Old
	-- we do not insert user copy if already exists
	IF ( @OwnerID_Old = @OwnerID AND @OwnerType1 = 'User' AND @OwnerType2 = 'User' ) 
	BEGIN
		-- TODO update
		RETURN
	END


	DECLARE @PersonID_Old int
	SET @PersonID_Old = @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID_Old
	IF ( @PersonID_Old = @PersonID )
	BEGIN
		-- TODO update
		--SET @PersonID_Old = @PersonID_Old
		RETURN
	END


	IF ( @OwnerType1 = 'User' )
	BEGIN
		--EXEC sp_import_get @OwnerType1, @OwnerID OUTPUT, @OwnerID

		IF ( @OwnerType2 = 'User' OR @OwnerType2 = 'Client' )
		BEGIN
			--EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID
			SET @PersonID = @PersonID
		END
		ELSE
		BEGIN 
			--RAISERROR
			SET @PersonID = 0
		END

		-- blank record already inserted and linked in sp_import_UserPerson/sp_import_ClientPerson
		UPDATE Person SET
		  SexCodeID = @SexCodeID
		, TitleCodeID = @TitleCodeID
		, FamilyName = @FamilyName
		, FirstName = @FirstName
		, OtherGivenNames = @OtherGivenNames
		, PreferredName = @PreferredName
		, MaritalCodeID = @MaritalCodeID
		, DateOfBirth = @DateOfBirth
		, DOBCountryID = @DOBCountryID
		, TaxFileNumber = @TaxFileNumber
		, PreferredLanguageID = @PreferredLanguageID
		, PreferredLanguage = @PreferredLanguage
		, ResidenceCountryCodeID = @ResidenceCountryCodeID
		, ResidenceStatusCodeID = @ResidenceStatusCodeID
		, ReferalSourceCodeID = @ReferalSourceCodeID
		, DateCreated = @DateCreated
		, DateModified = @DateModified
		, SupportToAge = @SupportToAge
		, DSSRecipient = @DSSRecipient
		WHERE PersonID = @PersonID

	END
	ELSE 
	BEGIN
		--DECLARE @PersonID_Old int
		--SET @PersonID_Old = @PersonID
	
		-- PERSON = 1
		INSERT INTO Object (ObjectTypeID) VALUES (1)
		SELECT @PersonID = @@IDENTITY
	
		DECLARE @LinkObjectTypeID int
		SET @LinkObjectTypeID = 0 -- no linkage
	
		DECLARE @LinkedObjectID2 int
		SET @LinkedObjectID2 = 0
	
		DECLARE @LinkObjectTypeID2 int
		SET @LinkObjectTypeID2 = 0 -- no linkage

		DECLARE @LinkObjectTypeID3 int
		SET @LinkObjectTypeID3 = 0 -- no linkage

		IF ( @OwnerType1 = 'Client' )
		BEGIN
			--EXEC sp_import_get @OwnerType1, @OwnerID OUTPUT, @OwnerID
	
			IF ( @OwnerType2 = 'Partner' )
			BEGIN
				SET @LinkObjectTypeID = 3001 -- CLIENT_2_PERSON = 3001
				SET @LinkedObjectID2   = 9	-- PARTNER = 9
				SET @LinkObjectTypeID2 = 3001008	-- CLIENT$PERSON_2_RELATIONSHIP_FINANCE = 3001008
				EXEC sp_import_set @OwnerType2, @PersonID, @PersonID_Old
			END
			ELSE 
			IF ( @OwnerType2 = 'Dependent' )
			BEGIN
				SET @LinkObjectTypeID = 1001 -- PERSON_2_PERSON = 1001
				SET @LinkedObjectID2   = 10	-- DEPENDENT = 10
				SET @LinkObjectTypeID2 = 1008	-- PERSON_2_RELATIONSHIP_FINANCE = 1008
				SET @LinkObjectTypeID3 = 1007	-- PERSON_2_RELATIONSHIP = 1007
				EXEC sp_import_set @OwnerType2, @PersonID, @PersonID_Old
			END
			ELSE 
			IF ( @OwnerType2 = 'Contact' )
			BEGIN
				SET @LinkObjectTypeID = 1001 -- PERSON_2_PERSON = 1001
				SET @LinkedObjectID2   = 11	-- CONTACT = 11
				SET @LinkObjectTypeID2 = 1008	-- PERSON_2_RELATIONSHIP_FINANCE = 1008
				SET @LinkObjectTypeID3 = 1007	-- PERSON_2_RELATIONSHIP = 1007
				EXEC sp_import_set @OwnerType2, @PersonID, @PersonID_Old
			END
			ELSE 
			BEGIN
				-- ERROR
				SET @OwnerID = 0
			END
	
		END
		ELSE 
		IF ( @OwnerType1 = 'Partner' )
		BEGIN
			--EXEC sp_import_get @OwnerType1, @OwnerID OUTPUT, @OwnerID
	
			SET @LinkObjectTypeID = 1001 -- PERSON_2_PERSON = 1001
	
			IF ( @OwnerType2 = 'Dependent' )
			BEGIN
				SET @LinkedObjectID2   = 10	-- DEPENDENT = 10
				SET @LinkObjectTypeID2 = 1008	-- PERSON_2_RELATIONSHIP_FINANCE = 1008
				SET @LinkObjectTypeID3 = 1007	-- PERSON_2_RELATIONSHIP = 1007
				EXEC sp_import_set @OwnerType2, @PersonID, @PersonID_Old
			END
			ELSE 
			IF ( @OwnerType2 = 'Contact' )
			BEGIN
				SET @LinkedObjectID2   = 11	-- CONTACT = 11
				SET @LinkObjectTypeID2 = 1008	-- PERSON_2_RELATIONSHIP_FINANCE = 1008
				SET @LinkObjectTypeID3 = 1007	-- PERSON_2_RELATIONSHIP = 1007
				EXEC sp_import_set @OwnerType2, @PersonID, @PersonID_Old
			END
			ELSE 
			BEGIN
				-- ERROR
				SET @OwnerID = 0
			END
	
		END
		ELSE
		BEGIN
			-- ERROR
			SET @OwnerID = 0
		END
	
		INSERT INTO Person (
		  PersonID
		, SexCodeID
		, TitleCodeID
		, FamilyName
		, FirstName
		, OtherGivenNames
		, PreferredName
		, MaritalCodeID
		, DateOfBirth
		, DOBCountryID
		, TaxFileNumber
		, PreferredLanguageID
		, PreferredLanguage
		, ResidenceCountryCodeID
		, ResidenceStatusCodeID
		, ReferalSourceCodeID
		, DateCreated
		, DateModified
		, SupportToAge
		, DSSRecipient
		) VALUES (
		  @PersonID
		, @SexCodeID
		, @TitleCodeID
		, @FamilyName
		, @FirstName
		, @OtherGivenNames
		, @PreferredName
		, @MaritalCodeID
		, @DateOfBirth
		, @DOBCountryID
		, @TaxFileNumber
		, @PreferredLanguageID
		, @PreferredLanguage
		, @ResidenceCountryCodeID
		, @ResidenceStatusCodeID
		, @ReferalSourceCodeID
		, @DateCreated
		, @DateModified
		, @SupportToAge
		, @DSSRecipient
		)
	
		-- this is just a person
		IF ( @LinkObjectTypeID > 0 )
		BEGIN
			DECLARE @@LinkID int
			EXEC sp_create_Link @@LinkID OUTPUT, @OwnerID, @PersonID, @LinkObjectTypeID
	
			IF ( @LinkObjectTypeID2 > 0 )
			BEGIN
				DECLARE @@LinkID2 int
				EXEC sp_create_Link @@LinkID2 OUTPUT, @@LinkID, @LinkedObjectID2, @LinkObjectTypeID2

				IF ( @LinkObjectTypeID3 > 0 )
				BEGIN
					IF ( @RelationType <= 0 ) SET @RelationType = NULL

					DECLARE @@LinkID3 int
					EXEC sp_create_Link @@LinkID3 OUTPUT, @@LinkID2, @RelationType, @LinkObjectTypeID3
				END

			END
	
		END

	END

GO


--SELECT * FROM Link WHERE ObjectID1 = 11815
--DELETE FROM Link WHERE ObjectID1 = 11815 AND LinkObjectTypeID = 1006
--DELETE FROM Link WHERE ObjectID1 = 11815 AND LinkObjectTypeID = 1005


---------------------------------------------------------------------------------------
-- 	TABLE	Address
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Address]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Address]
GO
CREATE PROCEDURE [dbo].[sp_import_Address] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@AddressID int = 0,
	@DateCreated datetime = NULL,
	@DateModified datetime = NULL,
	@AddressCodeID int = NULL,
	@StreetNumber varchar(100) = NULL,
	@StreetNumber2 varchar(100) = NULL,
	@Suburb varchar(50) = NULL,
	@Postcode int = NULL,
	@ParentAddressID int = NULL,
	@StateCodeID int = NULL,
	@State varchar(50) = NULL,
	@CountryCodeID int = NULL,
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1005 -- PERSON_2_ADDRESS = 1005

	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- ADDRESS = 5
	DECLARE @AddressID_Old int
	SET @AddressID_Old = @AddressID
	INSERT INTO Object (ObjectTypeID) VALUES (5)
	SELECT @AddressID = @@IDENTITY
	EXEC sp_import_set 'Address', @AddressID, @AddressID_Old

	
	EXEC sp_import_get 'Address', @ParentAddressID OUTPUT, @ParentAddressID
	IF ( @ParentAddressID = 0 )
		SET @ParentAddressID = NULL


	INSERT INTO Address (
	  AddressID
	, AddressCodeID
	, StreetNumber
	, StreetNumber2
	, Suburb
	, Postcode
	, StateCodeID
	, State
	, CountryCodeID
	, ParentAddressID
	, DateCreated
	, DateModified
	) VALUES (
	  @AddressID
	, @AddressCodeID
	, @StreetNumber
	, @StreetNumber2
	, @Suburb
	, @Postcode
	, @StateCodeID
	, @State
	, @CountryCodeID
	, @ParentAddressID
	, @DateCreated
	, @DateModified
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @PersonID, @AddressID, @LinkObjectTypeID

GO


---------------------------------------------------------------------------------------
-- 	TABLE	ContactMedia
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_ContactMedia]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_ContactMedia]
GO
CREATE PROCEDURE [dbo].[sp_import_ContactMedia] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@ContactMediaID int = 0,
	@DateCreated datetime = NULL,
	@DateModified datetime = NULL,
	@ContactMediaCodeID int = NULL,
	@Value1 varchar(16) = NULL,
	@Value2 varchar(50) = NULL,
	@ContactMediaDesc varchar(50) = NULL,
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1006 -- PERSON_2_CONTACT_MEDIA = 1006

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- CONTACT_MEDIA = 6
	INSERT INTO Object (ObjectTypeID) VALUES (6)
	SELECT @ContactMediaID = @@IDENTITY

	INSERT INTO ContactMedia (
	  ContactMediaID
	, ContactMediaCodeID
	, Value1
	, Value2
	, ContactMediaDesc
	, DateCreated
	, DateModified
	) VALUES (
	  @ContactMediaID
	, @ContactMediaCodeID
	, @Value1
	, @Value2
	, @ContactMediaDesc
	, @DateCreated
	, @DateModified
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @PersonID, @ContactMediaID, @LinkObjectTypeID

GO



---------------------------------------------------------------------------------------
-- 	TABLE	Business
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Business]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Business]
GO
CREATE PROCEDURE [dbo].[sp_import_Business] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@BusinessID int = 0,
	@DateCreated datetime = NULL,
	@DateModified datetime = NULL,
	@ParentBusinessID int = NULL,
	@IndustryCodeID int = NULL,
	@BusinessStructureCodeID int = NULL,
	@LegalName varchar(200) = NULL,
	@TradingName varchar(200) = NULL,
	@BusinessNumber varchar(9) = NULL,
	@TaxFileNumber varchar(9) = NULL,
	@WebSiteName varchar(50) = NULL,
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1004 -- PERSON_2_BUSINESS = 1004

	DECLARE @LinkObjectTypeID2 int
	SET @LinkObjectTypeID2 = 1004030 -- PERSON$BUSINESS_2_OCCUPATION = 1004030

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- BUSINESS = 4
	INSERT INTO Object (ObjectTypeID) VALUES (4)
	SELECT @BusinessID = @@IDENTITY

	INSERT INTO Business (
	  BusinessID
	--, ParentBusinessID
	, IndustryCodeID
	, BusinessStructureCodeID
	, LegalName
	, TradingName
	, BusinessNumber
	, TaxFileNumber
	, WebSiteName
	, DateCreated
	, DateModified
	) VALUES (
	  @BusinessID
	--, @ParentBusinessID
	, @IndustryCodeID
	, @BusinessStructureCodeID
	, @LegalName
	, @TradingName
	, @BusinessNumber
	, @TaxFileNumber
	, @WebSiteName
	, @DateCreated
	, @DateModified
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @PersonID, @BusinessID, @LinkObjectTypeID

	DECLARE @@LinkID2 int
	EXEC sp_create_Link @@LinkID2 OUTPUT, @@LinkID, NULL, @LinkObjectTypeID2

GO



---------------------------------------------------------------------------------------
-- 	TABLE	PersonOccupation
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_PersonOccupation]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_PersonOccupation]
GO
CREATE PROCEDURE [dbo].[sp_import_PersonOccupation] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@PersonOccupationID int = 0,
	@DateCreated datetime = NULL,
	@PersonID int,
	@JobDescription varchar(50) = NULL,
	@EmploymentStatusCodeID int = NULL,
	@IndustryCodeID int = NULL,
	@OccupationCodeID int = NULL,
	@NextID int = NULL
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	--EXEC sp_import_get_owner @OwnerType1, @PersonID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	INSERT INTO PersonOccupation (
	  DateCreated
	, PersonID
	, JobDescription
	, EmploymentStatusCodeID
	, IndustryCodeID
	, OccupationCodeID
	--, NextID
	) VALUES (
	  @DateCreated
	, @PersonID
	, @JobDescription
	, @EmploymentStatusCodeID
	, @IndustryCodeID
	, @OccupationCodeID
	--, @NextID
	)

GO



---------------------------------------------------------------------------------------
-- 	TABLE	PersonHealth
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_PersonHealth]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_PersonHealth]
GO
CREATE PROCEDURE [dbo].[sp_import_PersonHealth] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@PersonHealthID int = 0,
	@DateCreated datetime = NULL,
	@PersonID int,
	@IsSmoker char(1) = NULL,
	@HealthStateCodeID int = NULL,
	@NextID int = NULL,
	@HospitalCover char(1) = 'N'
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	--EXEC sp_import_get_owner @OwnerType1, @PersonID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	INSERT INTO PersonHealth (
	  PersonID
	, IsSmoker
	, HealthStateCodeID
	--, NextID
	, HospitalCover
	, DateCreated
	) VALUES (
	  @PersonID
	, @IsSmoker
	, @HealthStateCodeID
	--, @NextID
	, @HospitalCover
	, @DateCreated
	)

GO



---------------------------------------------------------------------------------------
-- 	TABLE	PersonTrustDIYStatus
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_PersonTrustDIYStatus]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_PersonTrustDIYStatus]
GO
CREATE PROCEDURE [dbo].[sp_import_PersonTrustDIYStatus] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@PersonTrustDIYStatusID int = 0,
	@DateCreated datetime = NULL,
	@PersonID int,
	@TrustStatusCodeID int = NULL,
	@DIYStatusCodeID int = NULL,
	@Comment varchar(200) = NULL,
	@NextID int = NULL,
	@CompanyStatusCodeID int = NULL
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	INSERT INTO PersonTrustDIYStatus (
	  PersonID
	, TrustStatusCodeID
	, DIYStatusCodeID
	, Comment
	--, NextID
	, CompanyStatusCodeID
	, DateCreated
	) VALUES (
	  @PersonID
	, @TrustStatusCodeID
	, @DIYStatusCodeID
	, @Comment
	--, @NextID
	, @CompanyStatusCodeID
	, @DateCreated
	)

GO



---------------------------------------------------------------------------------------
-- 	TABLE	SelectedCategory
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_SelectedCategory]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_SelectedCategory]
GO
CREATE PROCEDURE [dbo].[sp_import_SelectedCategory] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@CategoryID int = 0,
	@CategoryName varchar(50),
	@CategoryDesc varchar(50) = NULL,
	@PersonID int = 0
 AS

	IF ( @CategoryName IS NULL )
		RETURN

	SET @CategoryID = 0

	SELECT @CategoryID = CategoryID FROM Category
	WHERE CategoryName = @CategoryName

	IF ( @CategoryID IS NULL OR @CategoryID = 0 )
		RETURN

	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	INSERT INTO SelectedCategory (
	  CategoryID
	, PersonID
	) VALUES (
	  @CategoryID
	, @PersonID
	)

GO


---------------------------------------------------------------------------------------
-- 	TABLE	FinancialGoal
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_FinancialGoal]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_FinancialGoal]
GO
CREATE PROCEDURE [dbo].[sp_import_FinancialGoal] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@FinancialGoalID int = 0,
	@DateCreated datetime = NULL,
	--@DateModified datetime = NULL,
	@GoalCodeID int = NULL,
	@TargetAge int = NULL,
	@TargetIncome numeric(15, 4) = NULL,
	@YearsIncomeReq int = NULL,
	@ResidualReq numeric(15, 4) = NULL,
	@LumpSumRequired numeric(15, 4) = NULL,
	@Notes text = NULL,
	@Inflation numeric(15, 4) = NULL,
	@TargetStrategyID int = NULL,
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1026 -- PERSON_2_FINANCIALGOAL = 1026

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- FINANCIAL_GOAL = 26
	INSERT INTO Object (ObjectTypeID) VALUES (26)
	SELECT @FinancialGoalID = @@IDENTITY

	INSERT INTO FinancialGoal (
	  FinancialGoalID
	, GoalCodeID
	, TargetAge
	, TargetIncome
	, YearsIncomeReq
	, ResidualReq
	, LumpSumRequired
	, Notes
	, Inflation
	, TargetStrategyID
	, DateCreated
	) VALUES (
	  @FinancialGoalID
	, @GoalCodeID
	, @TargetAge
	, @TargetIncome
	, @YearsIncomeReq
	, @ResidualReq
	, @LumpSumRequired
	, @Notes
	, @Inflation
	, @TargetStrategyID
	, @DateCreated
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID, @PersonID, @FinancialGoalID, @LinkObjectTypeID

GO



---------------------------------------------------------------------------------------
-- 	TABLE	Asset
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Asset]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Asset]
GO
CREATE PROCEDURE [dbo].[sp_import_Asset] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@ObjectTypeID int = 0,
	@AssetID int = 0,
	@AccountNumber varchar(20) = NULL,
	--@BeneficiaryID int = NULL,
	--@LiabilityID int = NULL,
	@FundTypeID int = NULL,
	@InvestmentTypeID int = NULL ,
	@UnitsShares numeric(18, 4) = NULL,
	@PurchaseCost numeric(15, 4) = NULL,
	@ReplacementValue numeric(15, 4) = NULL,
	@UnitsSharesPrice numeric(15, 4) = NULL,
	@PriceDate datetime = NULL,
	@FrequencyCodeID int = NULL,
	@AnnualAmount numeric(15, 4) = NULL,
	@TaxDeductibleAnnualAmount numeric(15, 4) = NULL,
	@TaxDeductibleRegularAmount numeric(15, 4) = NULL,
	@ContributionAnnualAmount numeric(15, 4) = NULL,
	@ContributionIndexation numeric(15, 4) = NULL,
	@ContributionStartDate datetime = NULL,
	@ContributionEndDate datetime = NULL,
	@DrawdownAnnualAmount numeric(15, 4) = NULL,
	@DrawdownIndexation numeric(15, 4) = NULL,
	@DrawdownStartDate datetime = NULL,
	@DrawdownEndDate datetime = NULL,
	@Reinvest char(1) = NULL,
	-- Financial
	@FinancialID int = 0,
	@DateCreated datetime = NULL,
	@FinancialTypeID int = NULL ,
	@FinancialTypeDesc varchar(50) = NULL ,
	@FinancialCodeID int = NULL ,
	@FinancialCode varchar(32) = NULL ,
	@FinancialCodeDesc varchar(100) = NULL ,
	@InstitutionID int = NULL ,
	@OwnerCodeID int = NULL ,
	@CountryCodeID int = NULL ,
	@Amount numeric(15, 4) = NULL ,
	@FinancialDesc varchar(200) = NULL ,
	--@NextID int = NULL ,
	--@StrategyGroupID int = NULL ,
	@DateStart datetime = NULL ,
	@DateEnd datetime = NULL ,
	@Franked numeric(15, 4) = NULL ,
	@TaxFreeDeferred numeric(15, 4) = NULL ,
	@CapitalGrowth numeric(15, 4) = NULL ,
	@Income numeric(15, 4) = NULL ,
	@UpfrontFee numeric(15, 4) = NULL ,
	@Deductible numeric(15, 4) = NULL ,
	@Rebateable numeric(15, 4) = NULL ,
	@Institution varchar(200) = NULL ,
	@AssetAllocationID int = NULL , -- !!!
	@ComplyingForDSS char(1) = NULL ,
	@DeductibleDSS numeric(15, 4) = NULL ,
	@Indexation numeric(15, 4) = NULL ,
	@OngoingFee numeric(15, 4) = NULL ,
	@Expense numeric(15, 4) = NULL ,
	@FinancialServiceCode char(10) = NULL,
	-- AssetAllocation
	@AssetAllocationAmount numeric(15, 4) = NULL,
	@InCash numeric(15, 4) = NULL,
	@InFixedInterest numeric(15, 4) = NULL,
	@InAustShares numeric(15, 4) = NULL,
	@InIntnlShares numeric(15, 4) = NULL,
	@InProperty numeric(15, 4) = NULL,
	@Include char(1) = NULL,
	@InOther numeric(15, 4) = NULL,
	--
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID1 int
	SET @LinkObjectTypeID1 = 1020 -- PERSON_2_FINANCIAL = 1020

	DECLARE @LinkObjectTypeID2 int
	SET @LinkObjectTypeID2 = 1021 -- PERSON_2_ASSET = 1021

	DECLARE @LinkObjectTypeID3 int -- e.g. PERSON_2_ASSETCASH = 1010
	IF ( @ObjectTypeID = 10 ) -- ASSET_CASH = 10
		SET @LinkObjectTypeID3 = 1010
	ELSE IF ( @ObjectTypeID = 11 ) -- ASSET_INVESTMENT = 11
		SET @LinkObjectTypeID3 = 1011 
	ELSE IF ( @ObjectTypeID = 12 ) -- ASSET_PERSONAL = 12
		SET @LinkObjectTypeID3 = 1012 
	ELSE IF ( @ObjectTypeID = 13 ) -- ASSET_SUPERANNUATION = 13
		SET @LinkObjectTypeID3 = 1013 
	ELSE IF ( @ObjectTypeID = 19 ) -- INCOME_STREAM = 19
		SET @LinkObjectTypeID3 = 1019 
	ELSE
		SET @LinkObjectTypeID3 = 0 -- ERROR

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- e.g. ASSET_CASH = 10
	INSERT INTO Object (ObjectTypeID) VALUES (@ObjectTypeID)
	SELECT @AssetID = @@IDENTITY


	EXEC sp_import_AssetAllocation
		@AssetAllocationID OUTPUT,
		@AssetAllocationAmount,
		@InCash,
		@InFixedInterest,
		@InAustShares,
		@InIntnlShares,
		@InProperty,
		@Include,
		@InOther


	INSERT INTO Financial (
	  FinancialID
	) VALUES (
	  @AssetID
	)

	EXEC sp_import_Financial 
		@AssetID,
		@DateCreated,
		@FinancialTypeID,
		@FinancialTypeDesc,
		@FinancialCodeID,
		@FinancialCode,
		@FinancialCodeDesc,
		@InstitutionID,
		@OwnerCodeID,
		@CountryCodeID,
		@Amount,
		@FinancialDesc,
		--@NextID,
		--@StrategyGroupID,
		@DateStart,
		@DateEnd,
		@Franked,
		@TaxFreeDeferred,
		@CapitalGrowth,
		@Income,
		@UpfrontFee,
		@Deductible,
		@Rebateable,
		@Institution,
		@AssetAllocationID,
		@ComplyingForDSS,
		@DeductibleDSS,
		@Indexation,
		@OngoingFee,
		@Expense,
		@FinancialServiceCode,
		@ObjectTypeID
 

	INSERT INTO Asset (
	  AssetID
	, AccountNumber
	--, BeneficiaryID
	--, LiabilityID
	, FundTypeID
	, InvestmentTypeID
	, UnitsShares
	, PurchaseCost
	, ReplacementValue
	, UnitsSharesPrice
	, PriceDate
	, FrequencyCodeID
	, AnnualAmount
	, TaxDeductibleAnnualAmount
	, TaxDeductibleRegularAmount
	, ContributionAnnualAmount
	, ContributionIndexation
	, ContributionStartDate
	, ContributionEndDate
	, DrawdownAnnualAmount
	, DrawdownIndexation
	, DrawdownStartDate
	, DrawdownEndDate
	, Reinvest
	) VALUES (
	  @AssetID
	, @AccountNumber
	--, @BeneficiaryID
	--, @LiabilityID
	, @FundTypeID
	, @InvestmentTypeID
	, @UnitsShares
	, @PurchaseCost
	, @ReplacementValue
	, @UnitsSharesPrice
	, @PriceDate
	, @FrequencyCodeID
	, @AnnualAmount
	, @TaxDeductibleAnnualAmount
	, @TaxDeductibleRegularAmount
	, @ContributionAnnualAmount
	, @ContributionIndexation
	, @ContributionStartDate
	, @ContributionEndDate
	, @DrawdownAnnualAmount
	, @DrawdownIndexation
	, @DrawdownStartDate
	, @DrawdownEndDate
	, @Reinvest
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID, @PersonID, @AssetID, @LinkObjectTypeID1
	EXEC sp_create_Link @@LinkID, @PersonID, @AssetID, @LinkObjectTypeID2
	EXEC sp_create_Link @@LinkID, @PersonID, @AssetID, @LinkObjectTypeID3

GO



---------------------------------------------------------------------------------------
-- 	TABLE	Regular
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Regular]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Regular]
GO
CREATE PROCEDURE [dbo].[sp_import_Regular] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@ObjectTypeID int = 0,
	@RegularID int = 0,
	@RegularAmount numeric(15, 4) = NULL ,
	@FrequencyCodeID int = NULL ,
	@DateNext datetime = NULL ,
	@AssetID int = NULL ,
	@Taxable char(1) = NULL ,
	-- Financial
	@FinancialID int = 0,
	@DateCreated datetime = NULL,
	@FinancialTypeID int = NULL ,
	@FinancialCodeID int = NULL ,
	@InstitutionID int = NULL ,
	@OwnerCodeID int = NULL ,
	@CountryCodeID int = NULL ,
	@Amount numeric(15, 4) = NULL ,
	@FinancialDesc varchar(200) = NULL ,
	--@NextID int = NULL ,
	--@StrategyGroupID int = NULL ,
	@DateStart datetime = NULL ,
	@DateEnd datetime = NULL ,
	@Franked numeric(15, 4) = NULL ,
	@TaxFreeDeferred numeric(15, 4) = NULL ,
	@CapitalGrowth numeric(15, 4) = NULL ,
	@Income numeric(15, 4) = NULL ,
	@UpfrontFee numeric(15, 4) = NULL ,
	@Deductible numeric(15, 4) = NULL ,
	@Rebateable numeric(15, 4) = NULL ,
	@Institution varchar(200) = NULL ,
	@AssetAllocationID int = NULL , -- !!!
	@ComplyingForDSS char(1) = NULL ,
	@DeductibleDSS numeric(15, 4) = NULL ,
	@Indexation numeric(15, 4) = NULL ,
	@OngoingFee numeric(15, 4) = NULL ,
	@Expense numeric(15, 4) = NULL ,
	@FinancialServiceCode char(10) = NULL,
	--
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID1 int
	SET @LinkObjectTypeID1 = 1020 -- PERSON_2_FINANCIAL = 1020

	DECLARE @LinkObjectTypeID2 int
	SET @LinkObjectTypeID2 = 1022 -- PERSON_2_REGULAR = 1022

	DECLARE @LinkObjectTypeID3 int
	IF ( @ObjectTypeID = 14 ) -- REGULAR_INCOME = 14
		SET @LinkObjectTypeID3 = 1014
	ELSE IF ( @ObjectTypeID = 15 ) -- REGULAR_EXPENSE = 15
		SET @LinkObjectTypeID3 = 1015 
	ELSE IF ( @ObjectTypeID = 18 ) -- TAX_OFFSET = 18
		SET @LinkObjectTypeID3 = 1018 
	ELSE
		SET @LinkObjectTypeID3 = 0 -- ERROR

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- e.g. REGULAR_INCOME = 14
	INSERT INTO Object (ObjectTypeID) VALUES (@ObjectTypeID)
	SELECT @RegularID = @@IDENTITY


	INSERT INTO Financial (
	  FinancialID
	) VALUES (
	  @RegularID
	)

	EXEC sp_import_Financial 
		@RegularID,
		@DateCreated,
		@FinancialTypeID,
		NULL, --@FinancialTypeDesc,
		@FinancialCodeID,
		NULL, --@FinancialCode,
		NULL, --@FinancialCodeDesc,
		@InstitutionID,
		@OwnerCodeID,
		@CountryCodeID,
		@Amount,
		@FinancialDesc,
		--@NextID,
		--@StrategyGroupID,
		@DateStart,
		@DateEnd,
		@Franked,
		@TaxFreeDeferred,
		@CapitalGrowth,
		@Income,
		@UpfrontFee,
		@Deductible,
		@Rebateable,
		@Institution,
		@AssetAllocationID,
		@ComplyingForDSS,
		@DeductibleDSS,
		@Indexation,
		@OngoingFee,
		@Expense,
		@FinancialServiceCode,
		@ObjectTypeID
 

	INSERT INTO Regular (
	  RegularID
	, RegularAmount
	, FrequencyCodeID
	, DateNext
	, AssetID
	, Taxable
	) VALUES (
	  @RegularID
	, @RegularAmount
	, @FrequencyCodeID
	, @DateNext
	, @AssetID
	, @Taxable
	)


	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID, @PersonID, @RegularID, @LinkObjectTypeID1
	EXEC sp_create_Link @@LinkID, @PersonID, @RegularID, @LinkObjectTypeID2
	EXEC sp_create_Link @@LinkID, @PersonID, @RegularID, @LinkObjectTypeID3

GO

--SELECT * FROM IMPORT

---------------------------------------------------------------------------------------
-- 	TABLE	Liability
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Liability]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Liability]
GO
CREATE PROCEDURE [dbo].[sp_import_Liability] 
	@OwnerType1 varchar(32),
	@OwnerID int,
	@OwnerType2 varchar(32),
	@ObjectTypeID int = 0,
	@LiabilityID int = 0,
	@AccountNumber varchar(20) = NULL,
	@InterestRate numeric(7, 4) = NULL,
	-- Regular
	@RegularID int = 0,
	@RegularAmount numeric(15, 4) = NULL ,
	@FrequencyCodeID int = NULL ,
	@DateNext datetime = NULL ,
	@AssetID int = NULL ,
	@Taxable char(1) = NULL ,
	-- Financial
	@FinancialID int = 0,
	@DateCreated datetime = NULL,
	@FinancialTypeID int = NULL ,
	@FinancialCodeID int = NULL ,
	@InstitutionID int = NULL ,
	@OwnerCodeID int = NULL ,
	@CountryCodeID int = NULL ,
	@Amount numeric(15, 4) = NULL ,
	@FinancialDesc varchar(200) = NULL ,
	--@NextID int = NULL ,
	--@StrategyGroupID int = NULL ,
	@DateStart datetime = NULL ,
	@DateEnd datetime = NULL ,
	@Franked numeric(15, 4) = NULL ,
	@TaxFreeDeferred numeric(15, 4) = NULL ,
	@CapitalGrowth numeric(15, 4) = NULL ,
	@Income numeric(15, 4) = NULL ,
	@UpfrontFee numeric(15, 4) = NULL ,
	@Deductible numeric(15, 4) = NULL ,
	@Rebateable numeric(15, 4) = NULL ,
	@Institution varchar(200) = NULL ,
	@AssetAllocationID int = NULL , -- !!!
	@ComplyingForDSS char(1) = NULL ,
	@DeductibleDSS numeric(15, 4) = NULL ,
	@Indexation numeric(15, 4) = NULL ,
	@OngoingFee numeric(15, 4) = NULL ,
	@Expense numeric(15, 4) = NULL ,
	@FinancialServiceCode char(10) = NULL,
	--
	@PersonID int = 0
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @PersonID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID1 int
	SET @LinkObjectTypeID1 = 1020 -- PERSON_2_FINANCIAL = 1020

	DECLARE @LinkObjectTypeID2 int
	SET @LinkObjectTypeID2 = 1022 -- PERSON_2_REGULAR = 1022

	DECLARE @LinkObjectTypeID3 int
	IF ( @ObjectTypeID = 16 ) -- PERSON_2_LIABILITY = 1016
		SET @LinkObjectTypeID3 = 1016
	ELSE
		SET @LinkObjectTypeID3 = 0 -- ERROR


	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @PersonID
	EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

	-- LIABILITY = 16
	INSERT INTO Object (ObjectTypeID) VALUES (@ObjectTypeID)
	SELECT @LiabilityID = @@IDENTITY


	INSERT INTO Financial (
	  FinancialID
	) VALUES (
	  @LiabilityID
	)

	EXEC sp_import_Financial 
		@LiabilityID,
		@DateCreated,
		@FinancialTypeID,
		NULL, --@FinancialTypeDesc,
		@FinancialCodeID,
		NULL, --@FinancialCode,
		NULL, --@FinancialCodeDesc,
		@InstitutionID,
		@OwnerCodeID,
		@CountryCodeID,
		@Amount,
		@FinancialDesc,
		--@NextID,
		--@StrategyGroupID,
		@DateStart,
		@DateEnd,
		@Franked,
		@TaxFreeDeferred,
		@CapitalGrowth,
		@Income,
		@UpfrontFee,
		@Deductible,
		@Rebateable,
		@Institution,
		@AssetAllocationID,
		@ComplyingForDSS,
		@DeductibleDSS,
		@Indexation,
		@OngoingFee,
		@Expense,
		@FinancialServiceCode,
		@ObjectTypeID
 

	INSERT INTO Regular (
	  RegularID
	, RegularAmount
	, FrequencyCodeID
	, DateNext
	, AssetID
	, Taxable
	) VALUES (
	  @LiabilityID
	, @RegularAmount
	, @FrequencyCodeID
	, @DateNext
	, @AssetID
	, @Taxable
	)

	INSERT INTO Liability (
	  LiabilityID
	, AccountNumber
	, InterestRate
	) VALUES (
	  @LiabilityID
	, @AccountNumber
	, @InterestRate
	)


	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID, @PersonID, @LiabilityID, @LinkObjectTypeID1
	EXEC sp_create_Link @@LinkID, @PersonID, @LiabilityID, @LinkObjectTypeID2
	EXEC sp_create_Link @@LinkID, @PersonID, @LiabilityID, @LinkObjectTypeID3


GO


---------------------------------------------------------------------------------------
-- 	TABLE	Financial
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Financial]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Financial]
GO
CREATE PROCEDURE [dbo].[sp_import_Financial] 
	@FinancialID int,
	@DateCreated datetime = NULL,
	@FinancialTypeID int = NULL ,
	@FinancialTypeDesc varchar(50) = NULL ,
	@FinancialCodeID int = NULL ,
	@FinancialCode varchar(32) = NULL ,
	@FinancialCodeDesc varchar(100) = NULL ,
	@InstitutionID int = NULL ,
	@OwnerCodeID int = NULL ,
	@CountryCodeID int = NULL ,
	@Amount numeric(15, 4) = NULL ,
	@FinancialDesc varchar(200) = NULL ,
	--@NextID int = NULL ,
	--@StrategyGroupID int = NULL ,
	@DateStart datetime = NULL ,
	@DateEnd datetime = NULL ,
	@Franked numeric(15, 4) = NULL ,
	@TaxFreeDeferred numeric(15, 4) = NULL ,
	@CapitalGrowth numeric(15, 4) = NULL ,
	@Income numeric(15, 4) = NULL ,
	@UpfrontFee numeric(15, 4) = NULL ,
	@Deductible numeric(15, 4) = NULL ,
	@Rebateable numeric(15, 4) = NULL ,
	@Institution varchar(200) = NULL ,
	@AssetAllocationID int = NULL , -- !!!
	@ComplyingForDSS char(1) = NULL ,
	@DeductibleDSS numeric(15, 4) = NULL ,
	@Indexation numeric(15, 4) = NULL ,
	@OngoingFee numeric(15, 4) = NULL ,
	@Expense numeric(15, 4) = NULL ,
	@FinancialServiceCode char(10) = NULL,
	@ObjectTypeID int = 0
 AS
	
	IF ( @FinancialTypeID IS NOT NULL )
	BEGIN
		IF ( not exists ( SELECT * FROM FinancialType 
				  WHERE FinancialTypeID = @FinancialTypeID ) )
		BEGIN
			IF ( @FinancialTypeDesc IS NULL )
				SET @FinancialTypeDesc = '???'

			INSERT INTO FinancialType (
			  FinancialTypeID
			, FinancialTypeDesc
			, ObjectTypeID
			) VALUES (
			  @FinancialTypeID
			, @FinancialTypeDesc
			, @ObjectTypeID
			)

		END

	END
	

	IF ( @FinancialCode IS NOT NULL )
	BEGIN
		SET @FinancialCodeID = 0
		SELECT @FinancialCodeID = FinancialCodeID FROM FinancialCode 
		WHERE FinancialCode = @FinancialCode AND FinancialTypeID = @FinancialTypeID
	
		IF ( @FinancialCodeID IS NULL OR @FinancialCodeID = 0 )
		BEGIN
			SELECT @FinancialCodeID = MAX(FinancialCodeID) FROM FinancialCode
			SET @FinancialCodeID = @FinancialCodeID + 1
	
			-- AssetInvestment, AssetSuperrannuation, IncomeStream
			DECLARE @FinancialTypeID4Code int
			IF ( @ObjectTypeID = 11 OR @ObjectTypeID = 13 OR @ObjectTypeID = 19 )
				SET @FinancialTypeID4Code = 0
			ELSE
				SET @FinancialTypeID4Code = @FinancialTypeID

			INSERT INTO FinancialCode (
			  FinancialCodeID
			, FinancialTypeID
			, FinancialCode
			, FinancialCodeDesc
			) VALUES (
			  @FinancialCodeID
			, @FinancialTypeID4Code
			, @FinancialCode
			, @FinancialCodeDesc
			)
	
		END

	END


	IF ( @Institution IS NOT NULL )
	BEGIN
		SET @InstitutionID = 0
		SET @Institution = RTRIM( @Institution )

		SELECT @InstitutionID = InstitutionID FROM Institution 
		WHERE InstitutionName LIKE @Institution + '%'
	
		IF ( @InstitutionID IS NULL OR @InstitutionID = 0 )
		BEGIN
			SELECT @InstitutionID = MAX(InstitutionID) FROM Institution
			SET @InstitutionID = @InstitutionID + 1
	
			INSERT INTO Institution (
			  InstitutionID
			, InstitutionName
			, InstitutionCode
			) VALUES (
			  @InstitutionID
			, @Institution
			, NULL
			)
	
			--SET @InstitutionID = NULL

		END

	END


	UPDATE Financial SET
	  DateCreated = @DateCreated
	, FinancialTypeID = @FinancialTypeID
	, FinancialCodeID = @FinancialCodeID
	, InstitutionID = @InstitutionID
	, OwnerCodeID = @OwnerCodeID
	, CountryCodeID = @CountryCodeID
	, Amount = @Amount
	, FinancialDesc = @FinancialDesc
	--, NextID = @NextID
	--, StrategyGroupID = @StrategyGroupID
	, DateStart = @DateStart
	, DateEnd = @DateEnd
	, Franked = @Franked
	, TaxFreeDeferred = @TaxFreeDeferred
	, CapitalGrowth = @CapitalGrowth
	, Income = @Income
	, UpfrontFee = @UpfrontFee
	, Deductible = @Deductible
	, Rebateable = @Rebateable
	, Institution = @Institution
	, AssetAllocationID = @AssetAllocationID
	, ComplyingForDSS = @ComplyingForDSS
	, DeductibleDSS = @DeductibleDSS
	, Indexation = @Indexation
	, OngoingFee = @OngoingFee
	, Expense = @Expense
	, FinancialServiceCode = @FinancialServiceCode
	WHERE FinancialID = @FinancialID

GO



---------------------------------------------------------------------------------------
-- 	TABLE	AssetAllocation
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_AssetAllocation]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_AssetAllocation]
GO
CREATE PROCEDURE [dbo].[sp_import_AssetAllocation] 
	@@AssetAllocationID int OUTPUT,
	@Amount numeric(15, 4) = NULL,
	@InCash numeric(15, 4) = NULL,
	@InFixedInterest numeric(15, 4) = NULL,
	@InAustShares numeric(15, 4) = NULL,
	@InIntnlShares numeric(15, 4) = NULL,
	@InProperty numeric(15, 4) = NULL,
	@Include char(1) = NULL,
	@InOther numeric(15, 4) = NULL
 AS
	INSERT INTO AssetAllocation (
	  Amount
	, InCash
	, InFixedInterest
	, InAustShares
	, InIntnlShares
	, InProperty
	, Include
	, InOther
	) VALUES (
	  @Amount
	, @InCash
	, @InFixedInterest
	, @InAustShares
	, @InIntnlShares
	, @InProperty
	, @Include
	, @InOther
	)

	SELECT @@AssetAllocationID = @@IDENTITY	

GO


---------------------------------------------------------------------------------------
-- 	TABLE	Model
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_Model]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_Model]
GO
CREATE PROCEDURE [dbo].[sp_import_Model] 
	@OwnerType1 varchar(32),
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32),
	@ModelID int = 0,
	@ModelTypeID int,
	@ModelTitle varchar(64),
	@ModelData2 text = '',
	@DateCreated datetime = NULL,
	@DateModified datetime = NULL,
	@ModelDesc varchar(512) = NULL
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @OwnerID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1029 -- PERSON_2_MODEL = 1029

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @OwnerID
	EXEC sp_import_get @OwnerType2, @OwnerID OUTPUT, @OwnerID

	INSERT INTO Object (ObjectTypeID) VALUES (29) -- MODEL = 29
	SELECT @ModelID = @@IDENTITY

	INSERT INTO Model (
	  ModelID
	, ModelTypeID
	, ModelTitle
	, ModelDesc
	, ModelData2
	, DateModified
	, DateCreated
	) VALUES (
	  @ModelID
	, @ModelTypeID
	, @ModelTitle
	, @ModelDesc
	, @ModelData2
	, @DateModified
	, @DateCreated
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @OwnerID, @ModelID, @LinkObjectTypeID

GO



---------------------------------------------------------------------------------------
-- 	TABLE	StrategyGroup
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_StrategyGroup]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_StrategyGroup]
GO
CREATE PROCEDURE [dbo].[sp_import_StrategyGroup] 
	@OwnerType1 varchar(32),
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32),
	@StrategyGroupID int = 0,
	@StrategyGroupTitle varchar(64),
	@DateCreated datetime = NULL,
	@DateModified datetime = NULL,
	@StrategyGroupData2 text = NULL
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @OwnerID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1032 -- PERSON_2_STRATEGYGROUP = 1032

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @OwnerID
	EXEC sp_import_get @OwnerType2, @OwnerID OUTPUT, @OwnerID

	INSERT INTO Object (ObjectTypeID) VALUES (32) -- -- STRATEGYGROUP = 32
	SELECT @StrategyGroupID = @@IDENTITY

	INSERT INTO StrategyGroup (
	  StrategyGroupID
	, StrategyGroupTitle
	, DateCreated
	, DateModified
	, StrategyGroupData2
	) VALUES (
	  @StrategyGroupID
	, @StrategyGroupTitle
	, @DateCreated
	, @DateModified
	, @StrategyGroupData2
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @OwnerID, @StrategyGroupID, @LinkObjectTypeID

GO



---------------------------------------------------------------------------------------
-- 	TABLE	PlanData
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_PlanData]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_PlanData]
GO
CREATE PROCEDURE [dbo].[sp_import_PlanData] 
	@OwnerType1 varchar(32),
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32),
	@PlanDataID int = 0,
	@PlanDataDesc varchar(255),
	@PlanDataText2 text = NULL,
	@PlanTypeID int = NULL
 AS

	DECLARE @STATUS varchar(32)
	EXEC sp_import_get 'STATUS', @STATUS OUTPUT, @OwnerID
	IF ( @STATUS = 'UPDATE' )
	BEGIN
		-- TODO UPDATE...

		RETURN

	END


	DECLARE @LinkObjectTypeID int
	SET @LinkObjectTypeID = 1031 -- PERSON_2_PLAN = 1031

	--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @OwnerID
	EXEC sp_import_get @OwnerType2, @OwnerID OUTPUT, @OwnerID

	INSERT INTO Object (ObjectTypeID) VALUES (31) -- PLAN = 31
	SELECT @PlanDataID = @@IDENTITY

	INSERT INTO PlanData (
	  PlanDataID
	, PlanDataDesc
	, PlanDataText2
	, PlanTypeID
	) VALUES (
	  @PlanDataID
	, @PlanDataDesc
	, @PlanDataText2
	, @PlanTypeID
	)

	DECLARE @@LinkID int
	EXEC sp_create_Link @@LinkID OUTPUT, @OwnerID, @PlanDataID, @LinkObjectTypeID

GO



if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_import_PersonSurvey]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_import_PersonSurvey]
GO
CREATE PROCEDURE [dbo].[sp_import_PersonSurvey] 
	@OwnerType1 varchar(32),
	@OwnerID int = 0, -- no owner
	@OwnerType2 varchar(32),
	@PersonID int = 0,
	@PersonSurveyID int = 0,
	@DateCreated datetime = NULL,
	@SelectedRiskProfile int = NULL,
	@SurveyID int = 0, 
	@SurveyTitle varchar(100),
	@SurveyDesc text = NULL,
	@QuestionID int = 0, 
	@QuestionTypeID int,
	@QuestionDesc text,
	@QuestionAnswerID int = 0, 
	@QuestionAnswerDesc text = NULL,
	@QuestionAnswerScore int = NULL,
	@QuestionAnswerText text = NULL

 AS
	DECLARE @PersonSurvey varchar(32)
	SET @PersonSurvey = 'PersonSurvey'

	DECLARE @PersonSurveyQuestion varchar(32)
	SET @PersonSurveyQuestion = 'PersonSurveyQuestion'

	DECLARE @PersonSurveyAnswer varchar(32)
	SET @PersonSurveyAnswer = 'PersonSurveyAnswer'


	DECLARE @PersonSurveyID_New int
	SET @PersonSurveyID_New = 0
	EXEC sp_import_get @PersonSurvey, @PersonSurveyID_New OUTPUT, @PersonSurveyID
	
	-- first question/answer
	IF ( @PersonSurveyID_New = 0 )
	BEGIN
		-- unlink current PersonSurvey (if any)
		-- TODO
		

		-- get new person id
		--EXEC sp_import_get_owner @OwnerType1, @OwnerID OUTPUT, @OwnerType2, @OwnerID
		EXEC sp_import_get @OwnerType2, @PersonID OUTPUT, @PersonID

		DECLARE @LinkObjectTypeID int
		SET @LinkObjectTypeID = 1024 -- PERSON_2_SURVEY = 1024
	
		SELECT @SurveyID = MAX(SurveyID) FROM Survey WHERE SurveyTitle = @SurveyTitle

		DECLARE @LinkID int
		EXEC sp_create_Link @LinkID OUTPUT, @PersonID, @SurveyID, @LinkObjectTypeID
	
		-- save to IMPORT table for future reference
		EXEC sp_import_set @PersonSurvey, @LinkID, @PersonSurveyID
		SET @PersonSurveyID = @LinkID

		-- person survey
		INSERT INTO PersonSurvey (
		  PersonSurveyID
		, SelectedRiskProfile
		) VALUES (
		  @PersonSurveyID
		, @SelectedRiskProfile
		)

	END
	ELSE
	BEGIN
		SET @PersonSurveyID = @PersonSurveyID_New
	END


	IF ( @QuestionDesc IS NULL )
		RETURN


	DECLARE @QuestionDesc2 varchar(255)
	SET @QuestionDesc2 = RTRIM( SUBSTRING(@QuestionDesc,0,254) ) + '%'

	DECLARE @QuestionID_Old int
	SET @QuestionID_Old = @QuestionID

	SET @QuestionID = 0
	SELECT @QuestionID = MAX(QuestionID) FROM Question 
	WHERE QuestionDesc LIKE @QuestionDesc2 AND QuestionTypeID = @QuestionTypeID 

	IF ( @QuestionID = 0 OR @QuestionID IS NULL )
		RETURN
	EXEC sp_import_set @PersonSurveyQuestion, @QuestionID, @QuestionID_Old


	IF ( @QuestionAnswerDesc IS NULL )
		RETURN

	DECLARE @QuestionAnswerDesc2 varchar(255)
	SET @QuestionAnswerDesc2 = RTRIM( SUBSTRING(@QuestionAnswerDesc,0,254) ) + '%'

	DECLARE @QuestionAnswerID_Old int
	SET @QuestionAnswerID_Old = @QuestionAnswerID

	SET @QuestionAnswerID = 0
	SELECT @QuestionAnswerID = MAX(QuestionAnswerID) FROM QuestionAnswer 
	WHERE QuestionID = @QuestionID AND QuestionAnswerDesc LIKE @QuestionAnswerDesc2 AND QuestionAnswerScore = @QuestionAnswerScore
	
	IF ( @QuestionAnswerID = 0 OR @QuestionAnswerID IS NULL )
		RETURN
	EXEC sp_import_set @PersonSurveyAnswer, @QuestionAnswerID, @QuestionAnswerID_Old

        INSERT INTO PersonSurveyAnswer (
          PersonSurveyID
	, QuestionID
	, QuestionAnswerID
        ) VALUES (
          @PersonSurveyID
	, @QuestionID
	, @QuestionAnswerID
	)


	IF ( @QuestionAnswerText IS NULL )
		RETURN

	DECLARE @PersonSurveyAnswerID int
	SELECT @PersonSurveyAnswerID = @@IDENTITY
	
	INSERT INTO PersonSurveyAnswerText (
	  PersonSurveyAnswerID
	, QuestionAnswerText
	) VALUES (
	  @PersonSurveyAnswerID
	, @QuestionAnswerText
	)

GO


-- SELECT * FROM IMPORT
