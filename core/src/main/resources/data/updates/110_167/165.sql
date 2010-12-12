IF ( exists ( SELECT * FROM DBVersion WHERE CurrentVersion = 'FID.01.65' ) )
	RAISERROR( 'FID.01.65 already exists', 16, 1 ) 
	--WITH NOWAIT, SETERROR
GO


---------------------------------------------------------------------------------------
-- 	Required for deletion
---------------------------------------------------------------------------------------
--ALTER TABLE FinancialCode DROP COLUMN LogicallyDeleted
ALTER TABLE FinancialCode ADD LogicallyDeleted char(1) NULL
GO


----------------------------------------------------------------------------
--
----------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[getUnitPrice]') and xtype in (N'FN', N'IF', N'TF'))
 drop function [dbo].[getUnitPrice]
GO
CREATE FUNCTION dbo.getUnitPrice (@code int)
 RETURNS float AS  
 BEGIN 
	DECLARE @UnitPrice float
	SET @UnitPrice = NULL

	SELECT TOP 1 @UnitPrice = [exit-price] 
	FROM [unit-price-data]
	WHERE code = @code
	AND [price-date] = (
		SELECT MAX( [price-date] )
		FROM [unit-price-data]
		WHERE code = @code
		AND [exit-price] IS NOT NULL AND [exit-price] <> 0
		
	)

	RETURN @UnitPrice

 END

GO
--SELECT dbo.getUnitPrice( 6790 )


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[getUnitPriceDate]') and xtype in (N'FN', N'IF', N'TF'))
 drop function [dbo].[getUnitPriceDate]
GO
CREATE FUNCTION dbo.getUnitPriceDate (@code int)
 RETURNS datetime AS  
 BEGIN 
	DECLARE @UnitPriceDate datetime
	SET @UnitPriceDate = NULL

	SELECT TOP 1 @UnitPriceDate = [price-date] 
	FROM [unit-price-data]
	WHERE code = @code
	AND [price-date] = (
		SELECT MAX( [price-date] )
		FROM [unit-price-data]
		WHERE code = @code
		AND [exit-price] IS NOT NULL AND [exit-price] <> 0
		
	)

	RETURN @UnitPriceDate

 END

GO
--SELECT dbo.getUnitPriceDate( 6790 )


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[vAssetInformation]') and OBJECTPROPERTY(id, N'IsView') = 1)
  drop view [dbo].[vAssetInformation]
GO
CREATE VIEW dbo.vAssetInformation
  AS

  SELECT DISTINCT 
	dbo.[apir-pic].[apir-pic] AS apir, 
	dbo.[apir-pic].code AS code,
	dbo.[product-information].[full-name] AS AssetName, 
	dbo.[manager-data].[full-name] AS Institition, 
	dbo.getUnitPrice( dbo.[apir-pic].code ) AS UnitPrice,
	dbo.getUnitPriceDate( dbo.[apir-pic].code ) AS UnitPriceDate
  FROM	dbo.[apir-pic], dbo.[product-information], dbo.[manager-data]
  WHERE dbo.[apir-pic].code = dbo.[product-information].code
	AND dbo.[product-information].[manager-code] = dbo.[manager-data].code

GO
--SELECT * FROM vAssetInformation WHERE code = 6790
--SELECT * FROM vAssetInformation WHERE APIR = 'PRU0271AU'



---------------------------------------------------------------------------------------
-- 	JOURNAL TABLES
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[AssetAllocationJournal]') and OBJECTPROPERTY(id, N'IsTable') = 1)
 drop table AssetAllocationJournal
GO
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FinancialJournal]') and OBJECTPROPERTY(id, N'IsTable') = 1)
 drop table FinancialJournal
GO
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[Journal]') and OBJECTPROPERTY(id, N'IsTable') = 1)
 drop table Journal
GO
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[JournalStatus]') and OBJECTPROPERTY(id, N'IsTable') = 1)
 drop table JournalStatus
GO
CREATE TABLE JournalStatus (
	JournalStatusID int NOT NULL,
	JournalStatusDesc varchar(50) NOT NULL
)
GO
ALTER TABLE JournalStatus ADD CONSTRAINT PK_JournalStatusID PRIMARY KEY (JournalStatusID)
GO

INSERT INTO JournalStatus ( JournalStatusID, JournalStatusDesc )
VALUES ( 0, '[None]' )

INSERT INTO JournalStatus ( JournalStatusID, JournalStatusDesc )
VALUES ( 1, 'Assets Swaped' )

INSERT INTO JournalStatus ( JournalStatusID, JournalStatusDesc )
VALUES ( 2, 'Assets Recovered' )

INSERT INTO JournalStatus ( JournalStatusID, JournalStatusDesc )
VALUES ( 3, 'Backup (Assets Recovered)' )


---------------------------------------------------------------------------------------
-- 	THESE TABLES IS EXTENED COPY OF...
---------------------------------------------------------------------------------------
CREATE TABLE Journal (
	JournalID	int		IDENTITY (1, 1) NOT NULL,
	JournalDesc	varchar(255)	NOT NULL,
	JournalStatusID	int		NOT NULL,
	FinancialTypeID int		NULL,
	FinancialCodeID int		NOT NULL,
	DateCreated	datetime 	NOT NULL DEFAULT getdate()
)
GO
ALTER TABLE Journal ADD CONSTRAINT PK_JournalID PRIMARY KEY (JournalID)
GO
ALTER TABLE Journal ADD
  CONSTRAINT FK_Journal_JournalStatus FOREIGN KEY (JournalStatusID) REFERENCES JournalStatus (JournalStatusID)
GO
ALTER TABLE Journal ADD
  CONSTRAINT FK_Journal_FinancialType FOREIGN KEY (FinancialTypeID) REFERENCES FinancialType (FinancialTypeID)
GO
ALTER TABLE Journal ADD
  CONSTRAINT FK_Journal_FinancialCode FOREIGN KEY (FinancialCodeID) REFERENCES FinancialCode (FinancialCodeID)
GO



---------------------------------------------------------------------------------------
-- 	THESE TABLES IS EXTENED COPY OF...
---------------------------------------------------------------------------------------
CREATE TABLE [FinancialJournal] (
	JournalID	int	NOT NULL,
	FinancialID 	int 	NOT NULL ,
	[FinancialTypeID] [int] NULL ,
	[FinancialCodeID] [int] NULL ,
	[InstitutionID] [int] NULL ,
	[OwnerCodeID] [int] NULL ,
	[CountryCodeID] [int] NULL ,
	[Amount] [numeric](15, 4) NULL ,
	[FinancialDesc] [varchar] (200) NULL ,
	[DateCreated] [datetime] NULL,
	[NextID] [int] NULL ,
	[StrategyGroupID] [int] NULL ,
	[DateStart] [datetime] NULL ,
	[DateEnd] [datetime] NULL ,
	[Franked] [numeric](15, 4) NULL ,
	[TaxFreeDeferred] [numeric](15, 4) NULL ,
	[CapitalGrowth] [numeric](15, 4) NULL ,
	[Income] [numeric](15, 4) NULL ,
	[UpfrontFee] [numeric](15, 4) NULL ,
	[Deductible] [numeric](15, 4) NULL ,
	[Rebateable] [numeric](15, 4) NULL ,
	[Institution] [varchar] (200) NULL ,
	[AssetAllocationID] [int] NULL ,
	[ComplyingForDSS] [char] (1) NULL ,
	[DeductibleDSS] [numeric](15, 4) NULL ,
	[Indexation] [numeric](15, 4) NULL ,
	[OngoingFee] [numeric](15, 4) NULL ,
	[Expense] [numeric](15, 4) NULL ,
	[FinancialServiceCode] [char] (10) NULL
)
GO
ALTER TABLE FinancialJournal ADD 
  CONSTRAINT PK_FinancialJournalID PRIMARY KEY (JournalID,FinancialID)
GO
ALTER TABLE FinancialJournal ADD
  CONSTRAINT FK_FinancialJournal_Financial FOREIGN KEY (FinancialID) REFERENCES Financial (FinancialID)
GO
ALTER TABLE FinancialJournal ADD
  CONSTRAINT FK_FinancialJournal_Journal FOREIGN KEY (JournalID) REFERENCES Journal (JournalID)
GO


CREATE TABLE [AssetAllocationJournal] (
	JournalID		int	NOT NULL,
	AssetAllocationID 	int 	NOT NULL,
	[Amount] [numeric](15, 4) NULL ,
	[InCash] [numeric](15, 4) NULL ,
	[InFixedInterest] [numeric](15, 4) NULL ,
	[InAustShares] [numeric](15, 4) NULL ,
	[InIntnlShares] [numeric](15, 4) NULL ,
	[InProperty] [numeric](15, 4) NULL ,
	[Include] [char] (1) NULL ,
	[InOther] [numeric](15, 4) NULL
)
GO
ALTER TABLE AssetAllocationJournal ADD 
  CONSTRAINT PK_AssetAllocationJournalID PRIMARY KEY (JournalID,AssetAllocationID)
GO
ALTER TABLE AssetAllocationJournal ADD
  CONSTRAINT FK_AssetAllocationJournal_Financial FOREIGN KEY (AssetAllocationID) REFERENCES AssetAllocation (AssetAllocationID)
GO
ALTER TABLE AssetAllocationJournal ADD
  CONSTRAINT FK_AssetAllocationJournal_Journal FOREIGN KEY (JournalID) REFERENCES Journal (JournalID)
GO


---------------------------------------------------------------------------------------
-- 	Required sp(s)
---------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_journal_create]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_journal_create]
GO
CREATE PROCEDURE [dbo].[sp_journal_create] 
	@@JournalID int OUTPUT,
	@JournalDesc varchar(255),
	@JournalStatusID int = 0,
	@FinancialTypeID int = NULL,
	@FinancialCodeID int
 AS

  DECLARE @NONE int
  SET @NONE = 0

	INSERT INTO Journal (
	  JournalDesc, JournalStatusID, FinancialTypeID, FinancialCodeID
	) VALUES (
	  @JournalDesc, @JournalStatusID, @FinancialTypeID, @FinancialCodeID
	)

	SELECT @@JournalID = @@IDENTITY

GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_select_Journal]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_select_Journal]
GO
CREATE PROCEDURE [dbo].[sp_select_Journal] 
	@JournalStatusID int = 1 -- 1 = 'Assets Swaped'
 AS
	SELECT
	  JournalID
	, JournalDesc
	, JournalStatusID
	, FinancialTypeID
	, FinancialCodeID
	, DateCreated
	, DateCreated AS TimeCreated
	FROM 
	  Journal
	WHERE 
	  JournalStatusID = @JournalStatusID

GO
--EXEC sp_select_Journal 2
--EXEC sp_restore_FinancialCode 6
--SELECT @@ERROR


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_update_FinancialCode]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_update_FinancialCode]
GO
CREATE PROCEDURE [dbo].[sp_update_FinancialCode] 
	@FinancialTypeID_Old int = NULL,
	@FinancialCodeID_Old int,
	@FinancialTypeID_New int = NULL,
	@FinancialCodeID_New int,
	@JournalDesc varchar(255)
 AS

  DECLARE @ASSET_SWAPPED int
  SET @ASSET_SWAPPED = 1
	
	-- create new journal
	DECLARE @JournalID int
	EXEC sp_journal_create 
	  @JournalID OUTPUT, @JournalDesc, @ASSET_SWAPPED, @FinancialTypeID_Old, @FinancialCodeID_Old
	

	-- save old data
	INSERT INTO FinancialJournal
	SELECT 
	  'JournalID' = @JournalID
	, FinancialID
	, FinancialTypeID
	, FinancialCodeID
	, InstitutionID
	, OwnerCodeID
	, CountryCodeID
	, Amount
	, FinancialDesc
	, DateCreated
	, NextID
	, StrategyGroupID
	, DateStart
	, DateEnd
	, Franked
	, TaxFreeDeferred
	, CapitalGrowth
	, Income
	, UpfrontFee
	, Deductible
	, Rebateable
	, Institution
	, AssetAllocationID
	, ComplyingForDSS
	, DeductibleDSS
	, Indexation
	, OngoingFee
	, Expense
	, FinancialServiceCode
	FROM Financial
	WHERE 
	  FinancialCodeID = @FinancialCodeID_Old
	  --AND FinancialTypeID = @FinancialTypeID_Old 

	IF @@ERROR <> 0 RETURN 0

	INSERT INTO AssetAllocationJournal
	SELECT 
	  'JournalID' = @JournalID
	, aa.AssetAllocationID
	, aa.Amount
	, aa.InCash
	, aa.InFixedInterest
	, aa.InAustShares
	, aa.InIntnlShares
	, aa.InProperty
	, aa.Include
	, aa.InOther
	FROM AssetAllocation aa, Financial f
	WHERE 
	  aa.AssetAllocationID = f.AssetAllocationID
	  AND f.FinancialCodeID = @FinancialCodeID_Old
	  --AND f.FinancialTypeID = @FinancialTypeID_Old

	IF @@ERROR <> 0 RETURN 0


	-- find new FinancialCode ( [apir-pic] )
	DECLARE @FinancialCode varchar(32)
	SET @FinancialCode = NULL

	SELECT @FinancialCode = FinancialCode
	FROM FinancialCode
	WHERE
	  FinancialCodeID = @FinancialCodeID_New
	  --AND FinancialTypeID = @FinancialTypeID_New

	IF ( @FinancialCode IS NULL )
	BEGIN
		RAISERROR( 'Failed to find new FinancialCode ( [apir-pic] )', 16, 1 ) 
		RETURN 0
	END


	---------------------------------------------------------------
	-- UPDATE
	-- 	FinancialType
	-- 	FinancialCode
	--	Institution
	-- 	UnitSharePrice
	---------------------------------------------------------------
	DECLARE @Institution varchar(200)
	SET @Institution = NULL

	DECLARE @UnitSharesPrice float
	SET @UnitSharesPrice = NULL
	
	DECLARE @UnitSharesPriceDate datetime
	SET @UnitSharesPriceDate = NULL

	SELECT 
	  @Institution = Institition,
	  @UnitSharesPrice = UnitPrice,
	  @UnitSharesPriceDate = UnitPriceDate
	FROM vAssetInformation 
	WHERE apir = @FinancialCode

	UPDATE Financial SET
	  FinancialTypeID = @FinancialTypeID_New,
	  FinancialCodeID = @FinancialCodeID_New,
	  Institution     = @Institution
	WHERE 
	  FinancialCodeID = @FinancialCodeID_Old
	  --AND FinancialTypeID = @FinancialTypeID_Old 

	-- update Asset(s)
	UPDATE Asset SET
	  UnitsSharesPrice = @UnitSharesPrice,
	  PriceDate = @UnitSharesPriceDate
	WHERE  AssetID IN (
	  SELECT FinancialID FROM Financial
	  WHERE
	    FinancialCodeID = @FinancialCodeID_New -- already updated !!!
	    --AND FinancialTypeID = @FinancialTypeID_New -- already updated !!!
	)


	RETURN @JournalID

GO

--select * from userperson

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_select_Financial]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_select_Financial]
GO
CREATE PROCEDURE [dbo].[sp_select_Financial] 
	@FinancialCodeID int,
	@FinancialTypeID int = NULL
 AS
	IF ( @FinancialTypeID IS NULL OR @FinancialTypeID <= 0 )
	BEGIN
		SELECT
		  FinancialID, -- FinancialDesc,
		  FinancialTypeID, FinancialCodeID,
		  Institution,
		  UnitsShares, UnitsSharesPrice, PriceDate
		  , FirstName + ' ' + FamilyName AS PersonName
		FROM 
		  vAsset, Person
		WHERE 
		  FinancialCodeID = @FinancialCodeID
		  --AND FinancialTypeID IS NULL
		  AND FinancialID IN (
		    SELECT ObjectID2 FROM Link
		    WHERE ObjectID1 = PersonID
		    AND LinkObjectTypeID = 1020
		    AND LogicallyDeleted IS NULL
		  ) 

	END
	ELSE
	BEGIN
		SELECT
		  FinancialID, -- FinancialDesc,
		  FinancialTypeID, FinancialCodeID,
		  Institution,
		  UnitsShares, UnitsSharesPrice, PriceDate
		  , FirstName + ' ' + FamilyName AS PersonName
		FROM 
		  vAsset, Person
		WHERE 
		  FinancialCodeID = @FinancialCodeID
		  --AND FinancialTypeID = @FinancialTypeID 
		  AND FinancialID IN (
		    SELECT ObjectID2 FROM Link
		    WHERE ObjectID1 = PersonID
		    AND LinkObjectTypeID = 1020
		    AND LogicallyDeleted IS NULL
		  ) 
	END

GO
-- EXEC sp_select_Financial 751
-- EXEC sp_select_Financial 852, 4
-- EXEC sp_select_Financial 792, 5


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vAssetJournal') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vAssetJournal
GO
CREATE VIEW dbo.vAssetJournal
 AS
 SELECT 
	  dbo.Object.ObjectTypeID
	, dbo.Asset.*
	, dbo.FinancialJournal.*
	, dbo.FinancialType.FinancialTypeDesc AS FinancialTypeDesc
	, dbo.FinancialCode.FinancialCode AS FinancialCode
	, dbo.FinancialCode.FinancialCodeDesc AS FinancialCodeDesc
	, dbo.AssetAllocation.Amount AS AssetAllocationAmount
	, dbo.AssetAllocation.InCash
	, dbo.AssetAllocation.InFixedInterest
	, dbo.AssetAllocation.InAustShares
	, dbo.AssetAllocation.InIntnlShares
	, dbo.AssetAllocation.InProperty
	, dbo.AssetAllocation.Include
	, dbo.AssetAllocation.InOther
 FROM	dbo.Object, dbo.Asset, dbo.FinancialJournal
	, dbo.FinancialType
	, dbo.FinancialCode
	, dbo.AssetAllocation
 WHERE  dbo.Object.ObjectID = dbo.Asset.AssetID
	AND dbo.Asset.AssetID = dbo.FinancialJournal.FinancialID 
	AND dbo.FinancialJournal.FinancialTypeID *= dbo.FinancialType.FinancialTypeID
	AND dbo.FinancialJournal.FinancialCodeID *= dbo.FinancialCode.FinancialCodeID
 	AND dbo.FinancialJournal.AssetAllocationID *= dbo.AssetAllocation.AssetAllocationID
GO


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_select_FinancialJournal]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_select_FinancialJournal]
GO
CREATE PROCEDURE [dbo].[sp_select_FinancialJournal] 
	@JournalID int,
	@FinancialCodeID int = NULL,
	@FinancialTypeID int = NULL
 AS
	IF ( @FinancialCodeID IS NULL OR @FinancialCodeID <= 0 )
	BEGIN
		SELECT @FinancialCodeID = FinancialCodeID
		FROM Journal
		WHERE JournalID = @JournalID
	END


	IF ( @FinancialTypeID IS NULL OR @FinancialTypeID <= 0 )
	BEGIN
		SELECT
		  FinancialID, -- FinancialDesc,
		  FinancialTypeID, FinancialCodeID,
		  Institution,
		  UnitsShares, UnitsSharesPrice, PriceDate
		  , FirstName + ' ' + FamilyName AS PersonName
		FROM 
		  vAssetJournal, Person
		WHERE 
		  JournalID = @JournalID
		  AND FinancialCodeID = @FinancialCodeID
		  --AND FinancialTypeID IS NULL
		  AND AssetID IN (  	
		    SELECT ObjectID2 FROM Link   	
		    WHERE ObjectID1 = PersonID   	
		    AND LinkObjectTypeID = 1021   	
		    AND LogicallyDeleted IS NULL   
		  ) 
	END
	ELSE
	BEGIN
		SELECT
		  FinancialID, -- FinancialDesc,
		  FinancialTypeID, FinancialCodeID,
		  Institution,
		  UnitsShares, UnitsSharesPrice, PriceDate
		  , FirstName + ' ' + FamilyName AS PersonName
		FROM 
		  vAssetJournal, Person
		WHERE 
		  JournalID = @JournalID
		  AND FinancialCodeID = @FinancialCodeID
		  --AND FinancialTypeID = @FinancialTypeID 
		  AND AssetID IN (
		    SELECT ObjectID2 FROM Link
		    WHERE ObjectID1 = PersonID
		    AND LinkObjectTypeID = 1021
		    AND LogicallyDeleted IS NULL
		  ) 
	END

GO
--SELECT * FROM FinancialCode WHERE FinancialCode = 'FPS0001AU'
--SELECT * FROM FinancialCode WHERE FinancialCodeID = 823
--SELECT * FROM Journal WHERE JournalID = 32
--EXEC sp_select_FinancialJournal 30, 823

--SELECT * FROM FinancialCode WHERE FinancialCodeID = 662
--SELECT * FROM FinancialCode WHERE FinancialCodeID = 494
--EXEC sp_select_Financial 662
--EXEC sp_select_FinancialJournal 32, 662


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_restore_FinancialCode]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_restore_FinancialCode]
GO
CREATE PROCEDURE [dbo].[sp_restore_FinancialCode] 
	@JournalID int
 AS

  DECLARE @ASSET_SWAPPED int
  SET @ASSET_SWAPPED = 1
	
  DECLARE @ASSET_RECOVERED int
  SET @ASSET_RECOVERED = 2

  DECLARE @BACKUP_ASSET_RECOVERED int
  SET @BACKUP_ASSET_RECOVERED = 3

	-- check if @@JournalID exists
	DECLARE @JournalDesc varchar(255)
	SET @JournalDesc = NULL

	DECLARE @FinancialTypeID int
	SET @FinancialTypeID = NULL

	DECLARE @FinancialCodeID int
	SET @FinancialCodeID = NULL

	SELECT 
	  @JournalDesc = JournalDesc, 
	  --@FinancialTypeID = FinancialTypeID, 
	  @FinancialCodeID = FinancialCodeID
	FROM Journal 
	WHERE JournalID = @JournalID 
	AND JournalStatusID = @ASSET_SWAPPED

	IF ( @JournalDesc IS NULL 
	  -- OR @FinancialTypeID IS NULL 
	  OR @FinancialCodeID IS NULL )
	BEGIN
		RAISERROR( 'This Journal does not exists or already recovered', 16, 1 ) 
		RETURN
	END


	-- create backup journal
	DECLARE @JournalID_Backup int
	EXEC sp_journal_create 
	  @JournalID_Backup OUTPUT, @JournalDesc, @BACKUP_ASSET_RECOVERED, @FinancialTypeID, @FinancialCodeID
	

	-- backup old data
	INSERT INTO FinancialJournal
	SELECT 
	  'JournalID' = @JournalID_Backup
	, FinancialID
	, FinancialTypeID
	, FinancialCodeID
	, InstitutionID
	, OwnerCodeID
	, CountryCodeID
	, Amount
	, FinancialDesc
	, DateCreated
	, NextID
	, StrategyGroupID
	, DateStart
	, DateEnd
	, Franked
	, TaxFreeDeferred
	, CapitalGrowth
	, Income
	, UpfrontFee
	, Deductible
	, Rebateable
	, Institution
	, AssetAllocationID
	, ComplyingForDSS
	, DeductibleDSS
	, Indexation
	, OngoingFee
	, Expense
	, FinancialServiceCode
	FROM Financial
	WHERE 
	  FinancialCodeID = @FinancialCodeID
	  --AND FinancialTypeID = @FinancialTypeID

	IF @@ERROR <> 0 RETURN

	INSERT INTO AssetAllocationJournal
	SELECT 'JournalID' = @JournalID_Backup
	, aa.AssetAllocationID
	, aa.Amount
	, aa.InCash
	, aa.InFixedInterest
	, aa.InAustShares
	, aa.InIntnlShares
	, aa.InProperty
	, aa.Include
	, aa.InOther
	FROM AssetAllocation aa, Financial f
	WHERE 
	  aa.AssetAllocationID = f.AssetAllocationID
	  AND f.FinancialCodeID = @FinancialCodeID
	  --AND f.FinancialTypeID = @FinancialTypeID

	IF @@ERROR <> 0 RETURN


	-- restore
	UPDATE Financial
	SET 
	  FinancialTypeID = fj.FinancialTypeID
	, FinancialCodeID = fj.FinancialCodeID
	, InstitutionID = fj.InstitutionID
	, OwnerCodeID = fj.OwnerCodeID
	, CountryCodeID = fj.CountryCodeID
	, Amount = fj.Amount
	, FinancialDesc = fj.FinancialDesc
	, DateCreated = fj.DateCreated
	, NextID = fj.NextID
	, StrategyGroupID = fj.StrategyGroupID
	, DateStart = fj.DateStart
	, DateEnd = fj.DateEnd
	, Franked = fj.Franked
	, TaxFreeDeferred = fj.TaxFreeDeferred
	, CapitalGrowth = fj.CapitalGrowth
	, Income = fj.Income
	, UpfrontFee = fj.UpfrontFee
	, Deductible = fj.Deductible
	, Rebateable = fj.Rebateable
	, Institution = fj.Institution
	, AssetAllocationID = fj.AssetAllocationID
	, ComplyingForDSS = fj.ComplyingForDSS
	, DeductibleDSS = fj.DeductibleDSS
	, Indexation = fj.Indexation
	, OngoingFee = fj.OngoingFee
	, Expense = fj.Expense
	, FinancialServiceCode = fj.FinancialServiceCode
	FROM Financial f, FinancialJournal fj
	WHERE f.FinancialID = fj.FinancialID
	AND fj.JournalID = @JournalID

	IF @@ERROR <> 0 RETURN

	UPDATE AssetAllocation
	SET 
	  Amount = aaj.Amount
	, InCash = aaj.InCash
	, InFixedInterest = aaj.InFixedInterest
	, InAustShares = aaj.InAustShares
	, InIntnlShares = aaj.InIntnlShares
	, InProperty = aaj.InProperty
	, Include = aaj.Include
	, InOther = aaj.InOther
	FROM AssetAllocation aa, AssetAllocationJournal aaj
	WHERE aa.AssetAllocationID = aaj.AssetAllocationID
	AND aaj.JournalID = @JournalID

	UPDATE Journal
	SET
	  JournalStatusID = @ASSET_RECOVERED
	WHERE
	  JournalID = @JournalID

GO


if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_delete_FinancialCode]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_delete_FinancialCode]
GO
CREATE PROCEDURE [dbo].[sp_delete_FinancialCode] 
	@FinancialCode varchar(32)
 AS
	DECLARE @FinancialCodeID int
	SET @FinancialCodeID = NULL

	SELECT @FinancialCodeID = FinancialCodeID FROM FinancialCode
	WHERE FinancialCode = @FinancialCode
	AND LogicallyDeleted IS NULL

	IF ( @FinancialCodeID IS NULL OR @FinancialCodeID <= 0 )
	BEGIN
		-- will never happenen
		SELECT 'Asset ''' + @FinancialCode + ''' does not exist.' 
		RETURN
	END


	DECLARE @Count int
	SET @Count = NULL

	SELECT @Count = count( FinancialID ) FROM Financial
	WHERE FinancialCodeID = @FinancialCodeID

	IF ( @Count IS NULL OR @Count <= 0 )
	BEGIN
		-- delete logically
		UPDATE FinancialCode
		SET LogicallyDeleted = 'Y'
		WHERE FinancialCode = @FinancialCode

		SELECT 'No Financials for Asset ''' + @FinancialCode + ''' exists. Asset removed.' 
		RETURN
	END


	-- warning
	SELECT 
	    'Can not delete Asset ''' + @FinancialCode + '''.' +
	    --TitleCodeDesc +
	    ' ' + FamilyName + ' ' + FirstName +
	    ' still using it.'
	    -- + ' (' + CAST( PersonID AS varchar(9) ) + ')' 
	FROM Person--, TitleCode
	WHERE PersonID IN (
	   SELECT ObjectID1 FROM Link
	   WHERE LinkObjectTypeID = 1020
	   AND ObjectID2 IN (
	      SELECT FinancialID FROM Financial
	      WHERE FinancialCodeID = @FinancialCodeID
	   )
	) --Person.TitleCodeID = TitleCode.TitleCodeID

GO

--SELECT * FROM FinancialCode WHERE FinancialCode LIKE '#%'
--SELECT * FROM FinancialCode WHERE FinancialCode LIKE 'FPS%'
--SELECT * FROM FinancialCode WHERE FinancialCode = '#108230649'
--EXEC sp_delete_FinancialCode '#1939070937' -- 369



-- fix sp_delete_financials
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_delete_financials]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_delete_financials]
GO
CREATE PROCEDURE [dbo].[sp_delete_financials] 	
	@PersonID int 
AS	
	DELETE FROM Liability WHERE LiabilityID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	

	DELETE FROM Regular WHERE RegularID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	
	UPDATE Regular SET AssetID=NULL WHERE AssetID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	

	DELETE FROM Asset WHERE AssetID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	

	-- !!! add delete from FinancialJournal table
	DELETE FROM FinancialJournal WHERE FinancialID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	

	DELETE FROM Financial WHERE FinancialID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	
	
	DELETE FROM Link WHERE ObjectID2 IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )	
	
	DELETE FROM Object WHERE ObjectID IN ( SELECT LinkID FROM Link WHERE ObjectID2 IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) ) )        	

	DELETE FROM Object WHERE ObjectID IN ( SELECT ObjectID2 FROM Link WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID = 1004 ) )

GO


--SELECT * FROM TitleCode
UPDATE TitleCode SET TitleCodeDesc = 'Mr'  WHERE TitleCodeDesc = 'Mr.';
UPDATE TitleCode SET TitleCodeDesc = 'Ms'  WHERE TitleCodeDesc = 'Ms.';
UPDATE TitleCode SET TitleCodeDesc = 'Mrs' WHERE TitleCodeDesc = 'Mrs.';
UPDATE TitleCode SET TitleCodeDesc = 'Dr'  WHERE TitleCodeDesc = 'Dr.';


-- FIX old logical bug
-- ASSET_CASH = 10
--SELECT FinancialID, Income, CapitalGrowth FROM Financial
--WHERE FinancialID IN ( SELECT ObjectID FROM Object WHERE ObjectTypeID = 10 )

UPDATE Financial SET Income = CapitalGrowth
WHERE Income IS NULL AND CapitalGrowth IS NOT NULL
  AND FinancialID IN ( SELECT ObjectID FROM Object WHERE ObjectTypeID = 10 );

UPDATE Financial SET CapitalGrowth = NULL
WHERE Income IS NOT NULL AND CapitalGrowth IS NOT NULL
  AND FinancialID IN ( SELECT ObjectID FROM Object WHERE ObjectTypeID = 10 );


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FID.01.65', 'FID.01.64');


