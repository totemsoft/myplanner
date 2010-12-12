IF ( exists ( SELECT * FROM DBVersion WHERE CurrentVersion = 'FID.01.66' ) )
	RAISERROR( 'FID.01.66 already exists', 16, 1 ) 
	--WITH NOWAIT, SETERROR
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
	@DateCreated datetime,
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


	-- save old value, will be used for mapping in Regular::AssetID field
	DECLARE @AssetID_Old int
	SET @AssetID_Old = @AssetID

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

	-- this mapping will be used in Regular::AssetID field
	EXEC sp_import_set 'Asset', @AssetID, @AssetID_Old

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
	@DateCreated datetime,
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
 

	-- get new asset id from mapping table
	EXEC sp_import_get 'Asset', @AssetID OUTPUT, @AssetID
	IF ( @AssetID = 0 )
		SET @AssetID = NULL


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


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.66', 'FID.01.65');
