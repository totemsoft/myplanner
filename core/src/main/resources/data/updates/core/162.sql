--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.62', 'FPS.01.61');


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_export_initialize]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_export_initialize]
GO
CREATE PROCEDURE [dbo].[sp_export_initialize]
	@UserPersonID int
 AS
	SELECT UserPersonID
	FROM UserPerson initialize
	WHERE UserPersonID = @UserPersonID
	FOR XML AUTO
GO

-- EXEC sp_export_initialize 11815

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_export_uninitialize]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_export_uninitialize]
GO
CREATE PROCEDURE [dbo].[sp_export_uninitialize]
	@UserPersonID int
 AS
	SELECT UserPersonID
	FROM UserPerson uninitialize
	WHERE UserPersonID = @UserPersonID
	FOR XML AUTO
GO

-- EXEC sp_export_uninitialize 11815


---------------------------------------------------------------------------------------
-- 	TABLE	Reference Tables:
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_export_References]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_export_References]
GO
CREATE PROCEDURE [dbo].[sp_export_References] AS

	SELECT 
	FinancialServiceCode, FinancialServiceDesc, DateCreated, LogicallyDeleted 
	FROM FinancialService
	--WHERE LogicallyDeleted IS NULL
	FOR XML AUTO

	SELECT 
	CategoryID, CategoryName, CategoryDesc
	FROM Category
	FOR XML AUTO

	SELECT 
	PlanDataID, PlanDataDesc, PlanDataText, PlanDataText2, PlanTypeID
	FROM PlanData PlanDataTemplate
	WHERE PlanTypeID IS NOT NULL -- templates
	FOR XML AUTO

GO

-- EXEC [dbo].[sp_export_References]


---------------------------------------------------------------------------------------
-- 	export	Person
---------------------------------------------------------------------------------------
--SELECT * FROM dbo.vLink3 
--WHERE 
--	LinkObjectTypeID = 1001
--	--AND OwnerPrimaryKeyID = 32811--210679
--	AND ( LinkedObjectID2 = 10 OR LinkedObjectID2 = 11 ) AND LinkObjectTypeID2 = 1008
--	AND LinkObjectTypeID3 = 1007

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_export_Person]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_export_Person]
GO
CREATE PROCEDURE [dbo].[sp_export_Person] 
	@OwnerType1 varchar(32), @OwnerID int,
	@OwnerType2 varchar(32), @PersonID int
 AS
	DECLARE @RelationType int
	SET @RelationType = 0
	IF ( @OwnerType2 = 'Dependent' ) -- DEPENDENT = 10
	BEGIN
		SELECT @RelationType = LinkedObjectID3 FROM dbo.vLink3 
		WHERE OwnerPrimaryKeyID = @OwnerID AND LinkedObjectID = @PersonID AND LinkObjectTypeID = 1001 -- PERSON_2_PERSON = 1001
			AND ( LinkedObjectID2 = 10 ) AND LinkObjectTypeID2 = 1008 -- PERSON_2_RELATIONSHIP_FINANCE = 1008
			AND LinkObjectTypeID3 = 1007 -- PERSON_2_RELATIONSHIP = 1007
	END
	ELSE 
	IF ( @OwnerType2 = 'Contact' ) -- CONTACT = 11
	BEGIN
		SELECT @RelationType = LinkedObjectID3 FROM dbo.vLink3 
		WHERE OwnerPrimaryKeyID = @OwnerID AND LinkedObjectID = @PersonID AND LinkObjectTypeID = 1001 -- PERSON_2_PERSON = 1001
			AND ( LinkedObjectID2 = 11 ) AND LinkObjectTypeID2 = 1008 -- PERSON_2_RELATIONSHIP_FINANCE = 1008
			AND LinkObjectTypeID3 = 1007 -- PERSON_2_RELATIONSHIP = 1007
	END
	

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		Person.*,
		'RelationType' = @RelationType
	FROM 	dbo.vPerson Person
	WHERE 	PersonID = @PersonID
	FOR XML AUTO

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		Address.*
	FROM 	dbo.vPersonAddress Address
	WHERE 	Address.PersonID = @PersonID
		--AND AddressCodeID IS NOT NULL
	FOR XML AUTO
--DELETE FROM Address WHERE AddressCodeID IS NULL
--DELETE FROM Address WHERE AddressID > 211000

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		ContactMedia.* 
	FROM 	dbo.vPersonContactMedia ContactMedia
	WHERE 	ContactMedia.PersonID = @PersonID
		--AND ContactMediaCodeID IS NOT NULL
	FOR XML AUTO
--DELETE FROM ContactMedia WHERE ContactMediaCodeID IS NULL
--DELETE FROM ContactMedia WHERE ContactMediaID > 211000
--DELETE FROM PersonOccupation WHERE PersonOccupationID > 260
--DELETE FROM PersonHealth WHERE PersonHealthID > 7600
--DELETE FROM PersonTrustDIYStatus WHERE PersonTrustDIYStatusID > 140
--DELETE FROM Business WHERE BusinessID > 211000
--DELETE FROM PersonSurveyAnswer WHERE PersonSurveyAnswerID > 100
--DELETE FROM PersonSurvey WHERE PersonSurveyID > 211000

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		Business.* 
	FROM 	dbo.vPersonBusiness Business
	WHERE 	Business.PersonID = @PersonID
	FOR XML AUTO

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		PersonOccupation.* 
	FROM 	dbo.PersonOccupation PersonOccupation
	WHERE 	PersonOccupation.PersonID = @PersonID
		AND NextID IS NULL
	FOR XML AUTO

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		PersonHealth.* 
	FROM 	dbo.PersonHealth PersonHealth
	WHERE 	PersonHealth.PersonID = @PersonID
		AND NextID IS NULL
	FOR XML AUTO

	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		PersonTrustDIYStatus.* 
	FROM 	dbo.PersonTrustDIYStatus PersonTrustDIYStatus
	WHERE 	PersonTrustDIYStatus.PersonID = @PersonID
		AND NextID IS NULL
	FOR XML AUTO

-- FinancialGoal
	SELECT 	'OwnerType1' = @OwnerType1, 'OwnerID' = @OwnerID, 
		'OwnerType2' = @OwnerType2,
		FinancialGoal.*
	FROM 	dbo.vPersonFinancialGoal FinancialGoal
	WHERE 	FinancialGoal.PersonID = @PersonID
	FOR XML AUTO

GO


---------------------------------------------------------------------------------------
-- 			UserPerson
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_export_UserPerson]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_export_UserPerson]
GO
CREATE PROCEDURE [dbo].[sp_export_UserPerson]
	@UserPersonID int 
 AS

   DECLARE @User varchar(32)
   SET @User = 'User'

-- User Personal Information
	SELECT 	'OwnerType1' = @User, 'OwnerID' = @UserPersonID, 
		'OwnerType2' = @User,
		FamilyName,
		FirstName,
		UserPersonID,
		AdviserTypeCodeID,
		LoginName,
		LoginPassword,
		ActiveUser
	FROM 	dbo.vUserPerson UserPerson
	WHERE 	UserPersonID = @UserPersonID
	FOR XML AUTO

	EXEC [dbo].[sp_export_Person] @User, @UserPersonID, @User, @UserPersonID

GO


---------------------------------------------------------------------------------------
-- 			ClientPerson
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_export_ClientPerson]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_export_ClientPerson]
GO
CREATE PROCEDURE [dbo].[sp_export_ClientPerson] 
	@ClientPersonID int,
	@UserPersonID int = 0
 AS

   DECLARE @User varchar(32)
   SET @User = 'User'

   DECLARE @Client varchar(32)
   SET @Client = 'Client'

   DECLARE @Partner varchar(32)
   SET @Partner = 'Partner'

   DECLARE @Dependent varchar(32)
   SET @Dependent = 'Dependent'

   DECLARE @Contact varchar(32)
   SET @Contact = 'Contact'

-- Client Personal Information
	SELECT 	'OwnerType1' = @User, 'OwnerID' = @UserPersonID, 
		'OwnerType2' = @Client,
		ClientPerson.*
	FROM 	dbo.ClientPerson ClientPerson
	WHERE 	ClientPersonID = @ClientPersonID
	FOR XML AUTO

	EXEC [dbo].[sp_export_Person] @User, @UserPersonID, @Client, @ClientPersonID


-- Client Categories
	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		SelectedCategory.* 
	FROM   	vPersonCategory SelectedCategory
	WHERE  	PersonID = @ClientPersonID
	FOR XML AUTO
-- SELECT * FROM vPersonCategory WHERE PersonID = 68767



-- Client Dependents
	-- PERSON_2_PERSON, DEPENDENT, PERSON_2_RELATIONSHIP_FINANCE
	DECLARE ccdep CURSOR FOR
	SELECT LinkedObjectID 
	FROM dbo.vLink2 
	WHERE OwnerPrimaryKeyID = @ClientPersonID AND LinkObjectTypeID = 1001
		AND LinkedObjectID2 = 10 AND LinkObjectTypeID2 = 1008
	--FOR UPDATE

	OPEN ccdep 
	
	DECLARE @ClientDependentID int

	FETCH NEXT FROM ccdep INTO @ClientDependentID
	WHILE @@FETCH_STATUS = 0
	BEGIN
		EXEC [dbo].[sp_export_Person] @Client, @ClientPersonID, @Dependent, @ClientDependentID
		FETCH NEXT FROM ccdep INTO @ClientDependentID
	END
	
	CLOSE ccdep 
	DEALLOCATE ccdep


-- Client Proffessional Contacts
	-- PERSON_2_PERSON, CONTACT, PERSON_2_RELATIONSHIP_FINANCE
	DECLARE cccon CURSOR FOR
	SELECT LinkedObjectID 
	FROM dbo.vLink2 
	WHERE OwnerPrimaryKeyID = @ClientPersonID AND LinkObjectTypeID = 1001
		AND LinkedObjectID2 = 11 AND LinkObjectTypeID2 = 1008
	--FOR UPDATE

	OPEN cccon 
	
	DECLARE @ClientContactID int

	FETCH NEXT FROM cccon INTO @ClientContactID
	WHILE @@FETCH_STATUS = 0
	BEGIN
		EXEC [dbo].[sp_export_Person] @Client, @ClientPersonID, @Contact, @ClientContactID
		FETCH NEXT FROM cccon INTO @ClientContactID
	END
	
	CLOSE cccon 
	DEALLOCATE cccon
 
-- Client PersonSurvey
	DECLARE @SurveyID int
	SELECT  @SurveyID = MAX(SurveyID) FROM vPersonSurvey PersonSurvey
	WHERE 	PersonSurvey.PersonID = @ClientPersonID
		--AND SurveyTitle = @SurveyTitle
--SELECT @SurveyID

	IF ( @SurveyID > 0 )
	BEGIN
		SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
			'OwnerType2' = @Client,
			PersonSurvey.*
		FROM 	dbo.vPersonSurvey PersonSurvey
		WHERE 	PersonSurvey.PersonID = @ClientPersonID
			AND PersonSurvey.SurveyID = @SurveyID
		FOR XML AUTO
	END

--SELECT 	PersonSurvey.*
--FROM 	dbo.vPersonSurvey PersonSurvey
--WHERE 	PersonSurvey.PersonID = 32811
--	AND PersonSurvey.SurveyID = 202700


-------------------------------------------------------------------------------------------
-- 				Partner
-------------------------------------------------------------------------------------------
	DECLARE @PartnerID int

	-- CLIENT_2_PERSON, PARTNER, CLIENT$PERSON_2_RELATIONSHIP_FINANCE
	SELECT @PartnerID = MAX(LinkedObjectID) 
	FROM dbo.vLink2 
	WHERE OwnerPrimaryKeyID = @ClientPersonID AND LinkObjectTypeID = 3001
		AND LinkedObjectID2 = 9 AND LinkObjectTypeID2 = 3001008

	IF ( @PartnerID > 0 )
	BEGIN
-- Partner Personal Information
		EXEC [dbo].[sp_export_Person] @Client, @ClientPersonID, @Partner, @PartnerID

-- Partner Dependents
		-- PERSON_2_PERSON, DEPENDENT, PERSON_2_RELATIONSHIP_FINANCE
		DECLARE cpdep CURSOR FOR
		SELECT LinkedObjectID 
		FROM dbo.vLink2 
		WHERE OwnerPrimaryKeyID = @PartnerID AND LinkObjectTypeID = 1001
			AND LinkedObjectID2 = 10 AND LinkObjectTypeID2 = 1008
		--FOR UPDATE
	
		OPEN cpdep 
		
		DECLARE @PartnerDependentID int
	
		FETCH NEXT FROM cpdep INTO @PartnerDependentID
		WHILE @@FETCH_STATUS = 0
		BEGIN
			EXEC [dbo].[sp_export_Person] @Partner, @PartnerID, @Dependent, @PartnerDependentID
			FETCH NEXT FROM cpdep INTO @PartnerDependentID
		END
		
		CLOSE cpdep 
		DEALLOCATE cpdep

-- Partner Contacts
		-- PERSON_2_PERSON, CONTACT, PERSON_2_RELATIONSHIP_FINANCE
		DECLARE cpcon CURSOR FOR
		SELECT LinkedObjectID 
		FROM dbo.vLink2 
		WHERE OwnerPrimaryKeyID = @PartnerID AND LinkObjectTypeID = 1001
			AND LinkedObjectID2 = 11 AND LinkObjectTypeID2 = 1008
		--FOR UPDATE
	
		OPEN cpcon 
		
		DECLARE @PartnerContactID int
	
		FETCH NEXT FROM cpcon INTO @PartnerContactID
		WHILE @@FETCH_STATUS = 0
		BEGIN
			EXEC [dbo].[sp_export_Person] @Partner, @PartnerID, @Contact, @PartnerContactID
			FETCH NEXT FROM cpcon INTO @PartnerContactID
		END
		
		CLOSE cpcon 
		DEALLOCATE cpcon

	END

-- Partner PersonSurvey
	SELECT  @SurveyID = MAX(SurveyID) FROM vPersonSurvey PersonSurvey
	WHERE 	PersonSurvey.PersonID = @PartnerID
		--AND SurveyTitle = @SurveyTitle

	IF ( @SurveyID > 0 )
	BEGIN
		SELECT 	'OwnerType1' = @Partner, 'OwnerID' = @PartnerID,
			'OwnerType2' = @Partner,
			PersonSurvey.*
		FROM 	dbo.vPersonSurvey PersonSurvey
		WHERE 	PersonSurvey.PersonID = @PartnerID
			AND PersonSurvey.SurveyID = @SurveyID
		FOR XML AUTO
	END


-- Client Financials
	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		Asset.*
	FROM 	dbo.vPersonAsset Asset
	WHERE 	Asset.PersonID = @ClientPersonID
		AND NextID IS NULL
	FOR XML AUTO

	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		Regular.*
	FROM 	dbo.vPersonRegular Regular
	WHERE 	Regular.PersonID = @ClientPersonID
		AND NextID IS NULL
	FOR XML AUTO

	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		Liability.*
	FROM 	dbo.vPersonLiability Liability
	WHERE 	Liability.PersonID = @ClientPersonID
		AND NextID IS NULL
	FOR XML AUTO


-- Model
	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		ModelID, ModelTypeID, ModelTitle, ModelDesc
		, DateCreated, DateModified
		, ModelData2
	FROM 	dbo.vPersonModel Model
	WHERE 	Model.PersonID = @ClientPersonID
	FOR XML AUTO


-- PlanData
	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		PlanDataID, PlanDataDesc, PlanDataText2, PlanTypeID
	FROM 	dbo.vPersonPlanData PlanData
	WHERE 	PlanData.PersonID = @ClientPersonID
	FOR XML AUTO


-- StrategyGroup
	SELECT 	'OwnerType1' = @Client, 'OwnerID' = @ClientPersonID,
		'OwnerType2' = @Client,
		StrategyGroupID, StrategyGroupTitle
		, DateCreated, DateModified
		, StrategyGroupData2
	FROM 	dbo.vPersonStrategyGroup StrategyGroup
	WHERE 	StrategyGroup.PersonID = @ClientPersonID
		AND StrategyGroupData2 IS NOT NULL
	FOR XML AUTO
	
GO
