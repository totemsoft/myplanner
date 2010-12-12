--Title:		FPS Data Warehouse
--Physical Model: 	FPS
--Author:		Valeri Chibaev
--Revision Number:	4
--Date Revised: 	22 January 2002


CREATE TABLE Object (
  ObjectID		int IDENTITY (1,1) NOT NULL,
  ObjectTypeID		int		NOT NULL
)

CREATE TABLE ObjectType (
  ObjectTypeID		int 		NOT NULL,
  ObjectTypeDesc	varchar(100)	NOT NULL
)

-- if LogicallyDeleted NULL then link is active
-- else (any character) - logically deleted
CREATE TABLE Link (
  LinkID		int		NOT NULL,
  ObjectID1		int		NOT NULL,
  ObjectID2		int		NULL,
  LinkObjectTypeID	int 		NOT NULL,
  LogicallyDeleted	char		NULL
)

CREATE TABLE LinkObjectType (
  LinkObjectTypeID	int 		NOT NULL,
  ObjectTypeID1		int 		NOT NULL,
  ObjectTypeID2		int 		NOT NULL,
  LinkObjectTypeDesc	varchar(100)	NOT NULL
)




CREATE TABLE SexCode (
  SexCodeID		int 		NOT NULL,
  SexCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE TitleCode (
  TitleCodeID		int 		NOT NULL,
  TitleCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE MaritalCode (
  MaritalCodeID		int 		NOT NULL,
  MaritalCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE RelationshipCode (
  RelationshipCodeID		int 		NOT NULL,
  RelationshipCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE RelationshipFinanceCode (
  RelationshipFinanceCodeID	int 		NOT NULL,
  RelationshipFinanceCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE AddressCode (
  AddressCodeID		int 		NOT NULL,
  AddressCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE SuburbPostCode (
  PostCode		int		NOT NULL,
  Suburb		varchar(50)	NOT NULL,
  StateCodeID		int 		NULL,
  CountryCodeID		int 		NULL
)

CREATE TABLE StateCode (
  StateCodeID		int 		NOT NULL,
  StateCode		varchar(3)	NOT NULL,
  StateCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE CountryCode (
  CountryCodeID		int 		NOT NULL,
  CountryCode		varchar(2)	NOT NULL,
  CountryCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE LanguageCode (
  LanguageCodeID	int 		NOT NULL,
  LanguageCode		varchar(3)	NOT NULL,
  LanguageCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE ContactMediaCode (
  ContactMediaCodeID	int 		NOT NULL,
  ContactMediaCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE IndustryCode (
  IndustryCodeID	int 		NOT NULL,
  IndustryCodeDesc	varchar(50)	NOT NULL
)

-- full list of all occupations
CREATE TABLE OccupationCode (
  OccupationCodeID	int 		NOT NULL,
  OccupationCodeDesc	varchar(50)	NOT NULL
)

-- subset of OccupationCode (contacts specific)
CREATE TABLE ContactOccupationCode (
  ContactOccupationCodeID	int 		NOT NULL
)

CREATE TABLE BusinessStructureCode (
  BusinessStructureCodeID	int 		NOT NULL,
  BusinessStructureCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE ReferalSourceCode (
  ReferalSourceCodeID		int 		NOT NULL,
  ReferalSourceCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE ResidenceStatusCode (
  ResidenceStatusCodeID		int 		NOT NULL,
  ResidenceStatusCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE HealthStateCode (
  HealthStateCodeID		int 		NOT NULL,
  HealthStateCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE EmploymentStatusCode (
  EmploymentStatusCodeID	int 		NOT NULL,
  EmploymentStatusCodeDesc	varchar(50)	NOT NULL
)

CREATE TABLE AdviserTypeCode (
  AdviserTypeCodeID		int 		NOT NULL,
  AdviserTypeCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE StatusCode (
  StatusCodeID			int 		NOT NULL,
  StatusCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE WillStatusCode (
  WillStatusCodeID		int 		NOT NULL,
  WillStatusCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE WillChangeCode (
  WillChangeCodeID		int 		NOT NULL,
  WillChangeCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE SupporterCode (
  SupporterCodeID		int 		NOT NULL,
  SupporterCodeDesc		varchar(50)	NOT NULL
)

CREATE TABLE AllowanceCode (
  AllowanceCodeID		int 		NOT NULL,
  AllowanceCodeDesc		varchar(50)	NOT NULL
)

CREATE  TABLE ModelType (
  ModelTypeID		int 		NOT NULL,
  ModelTypeDesc		varchar(50)	NOT NULL
);

-----------------------------------------------------------------
--	FROM 002
-----------------------------------------------------------------
CREATE TABLE FinancialType (
  FinancialTypeID	int 		NOT NULL,
  FinancialTypeDesc	varchar(50)	NOT NULL,
  ObjectTypeID		int 		NOT NULL
)

CREATE TABLE FinancialCode (
  FinancialCodeID	int	 	NOT NULL,
  FinancialTypeID	int 		NOT NULL,
  FinancialCode		varchar(10) 	NULL,
  FinancialCodeDesc	varchar(100)	NOT NULL
)

-- map SuperVisor II gl-chart.ac-num field to my fintype/fincode
CREATE TABLE FinancialMapSV2 (
  ac_num		varchar(9)	NOT NULL, --  (PK) 3+5
  FinancialTypeID	int 		NOT NULL, --  (UNIQUE 
  FinancialCodeID	int	 	NOT NULL, --   COMBINATION)
  LogicallyDeleted	char		NULL
)

CREATE TABLE InvestmentType (
  InvestmentTypeID	int 		NOT NULL,
  InvestmentTypeDesc	varchar(50)	NOT NULL
)

-- 1 = Complying Super Pension; 2 = Complying Annuity; 3 = Allocated Pension; 4 = Super - Accumulation 5 = Super - Defined Benefit
CREATE TABLE FundType (
  FundTypeID		int 		NOT NULL,
  FundTypeDesc		varchar(50)	NOT NULL
)

-- 1 = Client; 2 = Joint; 3 = Partner
CREATE TABLE OwnerCode (
  OwnerCodeID		int 		NOT NULL,
  OwnerCodeDesc		varchar(50)	NOT NULL
)

-- 1 = Weekly; 2 = Fortnightly; 3 = Monthly; 4 = Quarterly; 5 = Yearly
CREATE TABLE FrequencyCode (
  FrequencyCodeID	int 		NOT NULL,
  FrequencyCodeDesc	varchar(50)	NOT NULL
)

-- None, 1, 2, 6, 12 month
CREATE TABLE PeriodCode (
  PeriodCodeID		int 		NOT NULL,
  PeriodCodeDesc	varchar(50)	NOT NULL
)

-- 1 = Farther; 2 = Mother; 3 = Grandparent; 4 = Child; 5 = Other
CREATE TABLE SourceCode (
  SourceCodeID		int 		NOT NULL,
  SourceCodeDesc	varchar(50)	NOT NULL
)

-- 1 = Concessional; 2 = Post June 1994 Disability; 3 = Pre July 1983; 4 = Post June 1983; 5 = Undeducted; 6 = CGT Exempt 
CREATE TABLE ETPComponentCode (
  ETPComponentCodeID	int 		NOT NULL,
  ETPComponentCodeDesc	varchar(50)	NOT NULL
)

-- 1 = RadioBox (SA), 2 = CheckBox (MA), 3 = ComboBox (SA), 4 = ListBox (MA), 5 = FreeText
-- SA - Single Answer, MA - Multiple Answer
CREATE TABLE QuestionType (
  QuestionTypeID	int 		NOT NULL,
  QuestionTypeDesc	varchar(50)	NOT NULL
)

------------------------------------------------------------------
--	FROM 103
------------------------------------------------------------------
CREATE TABLE ParamType (
  ParamTypeID		int		NOT NULL,
  ParamTypeDesc		varchar(200)	NOT NULL
)

CREATE TABLE LifeExpectancy (
  Age			int		NOT NULL,
  SexCodeID		int		NOT NULL,
  LEValue		numeric(5,2)	NOT NULL,
  CountryCodeID		int		NOT NULL,
  DateModified		datetime	NOT NULL
)

-- VSH:	12/11/2001
CREATE TABLE HealthConditionCode (
  HealthConditionCodeID		int		NOT NULL,
  HealthConditionCodeDesc	varchar(50)	NOT NULL
)





--
--
--
CREATE TABLE Person (
  PersonID			int		NOT NULL,
  SexCodeID			int		NULL,
  TitleCodeID			int		NULL,
  FamilyName			varchar(30)	NULL,
  FirstName			varchar(15)	NULL,
  OtherGivenNames		varchar(30)	NULL,
  PreferredName			varchar(15)	NULL,
  MaritalCodeID 		int		NULL,
  DateOfBirth			datetime	NULL,
  DOBCountryID			int		NULL,
  TaxFileNumber			varchar(9)	NULL,
  PreferredLanguageID		int		NULL,
  PreferredLanguage		varchar(20)	NULL,
  ResidenceCountryCodeID	int		NULL,
  ResidenceStatusCodeID		int		NULL,
  ReferalSourceCodeID		int		NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate(),
  DateModified			datetime	NULL 
)

CREATE TABLE PersonHealth (
  PersonHealthID		int IDENTITY (1,1) NOT NULL,
  PersonID			int		NOT NULL,
  IsSmoker			char		NULL,
  HealthStateCodeID		int		NULL,
  NextID			int		NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate()
)

CREATE TABLE PersonOccupation (
  PersonOccupationID		int IDENTITY (1,1) NOT NULL,
  PersonID			int		NOT NULL,
  JobDescription		varchar(50)	NULL,
  EmploymentStatusCodeID	int		NULL,
  IndustryCodeID		int		NULL,
  OccupationCodeID		int 		NULL,
  NextID			int		NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate()
)

CREATE TABLE PersonTrustDIYStatus (
  PersonTrustDIYStatusID	int IDENTITY (1,1) NOT NULL,
  PersonID				int			NOT NULL,
  TrustStatusCodeID		int,
  DIYStatusCodeID			int,
  CompanyStatusCodeID		int,
  Comment				varchar(200),
  NextID				int,
  DateCreated			datetime	NOT NULL DEFAULT getDate()
)

-- VSH: 21/11/2001
CREATE TABLE PersonEstate (
  PersonEstateID		int		NOT NULL,
  StatusCodeID			int		NULL,
  DateLastReviewed		int		NULL,
  ExpectedChangesCodeID		int		NULL,
  ExecutorStatusCodeID		int		NULL,
  AttorneyStatusCodeID		int		NULL,
  PrenuptualCodeID		int		NULL,
  InsolvencyRiskCodeID		int		NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate()
)




CREATE TABLE ClientPerson (
  ClientPersonID	int		NOT NULL
)



CREATE TABLE UserPerson (
  UserPersonID		int		NOT NULL,
  AdviserTypeCodeID	int		NOT NULL,
  LoginName		varchar(32)	NOT NULL,
  LoginPassword		varchar(32)	NULL
)

CREATE TABLE Business (
  BusinessID			int		NOT NULL,
  ParentBusinessID		int		NULL,
  IndustryCodeID		int		NULL,
  BusinessStructureCodeID	int		NULL,
  LegalName			varchar(200)	NULL,
  TradingName			varchar(200)	NULL,
  BusinessNumber			varchar(9)	NULL,
  TaxFileNumber			varchar(9)	NULL,
  WebSiteName			varchar(50)	NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate(),
  DateModified			datetime	NULL 
)

CREATE TABLE Address (
  AddressID		int		NOT NULL,
  AddressCodeID		int		NULL,
  StreetNumber		varchar(100)	NULL,
  StreetNumber2		varchar(100)	NULL,
  Suburb		varchar(50)	NULL,
  Postcode		int		NULL,
  StateCodeID		int		NULL,
  CountryCodeID		int		NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate(),
  DateModified		datetime	NULL 
)

CREATE TABLE ContactMedia (
  ContactMediaID	int		NOT NULL,
  ContactMediaCodeID	int		NULL,
  Value1		varchar(16)	NULL,	-- country/area code, email name
  Value2		varchar(32)	NULL,	-- number, email host name
  ContactMediaDesc	varchar(50)	NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate(),
  DateModified		datetime	NULL 
)



------------------------------------------------------------------
--	FROM 002
------------------------------------------------------------------

------------------------------------------------------------------
--	to keep track of db updates				--
--	version control	(XXX.01.01)				--
--		XXX - client org code	(e.g. FPS)	--
--		01 - major version number			--
--		01 - minor version number			--
------------------------------------------------------------------
CREATE TABLE DBVersion (
  CurrentVersion	varchar(9)	NOT NULL,
  PreviousVersion	varchar(9)	NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)



--
-- Base table for all financial details
--
--	Financial
--		Asset
--			AssetCash
--			AssetInvestment
--			AssetPersonal
--			AssetSuperannuation
--		Regular
--			RegularIncome
--			RegularExpense
--				Liability
--				Insurance
--			
--
CREATE TABLE Financial (
  FinancialID			int		NOT NULL,
  FinancialTypeID		int		NULL,
  FinancialCodeID		int		NULL,
  InstitutionID                 int		NULL, -- (e.g. ASX)
  OwnerCodeID			int		NULL,
  CountryCodeID			int		NULL,
  Amount			numeric(15,4)	NULL,
  FinancialDesc			varchar(200)	NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate(),
  -- if NextID==FinancialID, this object is closed
  NextID			int		NULL 
)



-- Asset extends Financial
CREATE TABLE Asset (
  AssetID			int		NOT NULL,
  AccountNumber			varchar(20)	NULL,
  DateAcquired			datetime	NULL,
  BeneficiaryID 		int		NULL,
  LiabilityID 			int 		NULL
)

-- AssetCash extends Asset
CREATE TABLE AssetCash (
  AssetCashID			int		NOT NULL,
  MaturityDate			datetime	NULL
)

-- AssetInvestment extends Asset
CREATE TABLE AssetInvestment (
  AssetInvestmentID		int		NOT NULL,
  InvestmentTypeID		int		NULL,
  UnitsShares			numeric(18,4)	NULL
)

-- AssetPersonal extends Asset
CREATE TABLE AssetPersonal (
  AssetPersonalID		int		NOT NULL,
  ReplacementValue		numeric(15,4)	NULL,
  MarketValue			numeric(15,4)	NULL
)

-- AssetSuperannuation extends Asset
CREATE TABLE AssetSuperannuation (
  AssetSuperannuationID		int		NOT NULL,
  FundTypeID			int		NULL,
  UnitsShares			numeric(18,4)	NULL
)



-- Regular extends Financial
CREATE TABLE Regular (
  RegularID			int		NOT NULL,
  RegularAmount			numeric(15,4)	NULL,
  FrequencyCodeID		int		NULL,
  DateNext			datetime	NULL,
  AssetID			int		NULL
)

-- RegularIncome extends Regular
CREATE TABLE RegularIncome (
  RegularIncomeID		int		NOT NULL
)

-- RegularExpense extends Regular
CREATE TABLE RegularExpense (
  RegularExpenseID		int		NOT NULL
)

-- Liability extends RegularExpense
CREATE TABLE Liability (
  LiabilityID			int		NOT NULL,
  AccountNumber			varchar(20)	NULL,
  DateStart			datetime	NULL,
  DateEnd			datetime	NULL,
  InterestRate			numeric(7,4)	NULL
)

-- Insurance extends RegularExpense
CREATE TABLE Insurance (
  InsuranceID			int		NOT NULL,
  PolicyNumber			varchar(20)	NULL,
  DateStart			datetime	NULL,
  DateRenewal			datetime	NULL,
  CashValue			numeric(15,4)	NULL,
  WaitingPeriodCodeID		int		NULL,
  BenefitPeriodCodeID		int		NULL
)


CREATE TABLE FinancialPool (
  FinancialPoolID	int 		NOT NULL,
  Amount		numeric(15,4)	NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
);

--
-- StrategyFinancial::FinancialPool (Strategy extension to FinancialPool table AS n..1)
--
CREATE  TABLE StrategyFinancial (
  StrategyFinancialID		int 		NOT NULL,
  StrategyModelID		int 		NOT NULL,
  Amount			numeric(15,4)	NULL
);



--
--
--
CREATE TABLE Institution (
  InstitutionID		int 		NOT NULL,
  InstitutionName	varchar(100)	NOT NULL
)


--
--	Survey tables
--
--	use Link table to associate this SurveyID with another ObjectType
--	(e.g. Financial's survey)
--
CREATE  TABLE Survey (
  SurveyID		int 		NOT NULL,
  SurveyTitle		varchar(100)	NOT NULL,
  SurveyDesc		text		NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)

CREATE TABLE Question (
  QuestionID		int	IDENTITY (1,1) NOT NULL,
  QuestionTypeID	int 		NOT NULL,
  QuestionDesc		text		NOT NULL,
  SurveyID		int 		NOT NULL
)

CREATE TABLE QuestionAnswer (
  QuestionAnswerID	int	IDENTITY (1,1) NOT NULL,
  QuestionAnswerDesc	text		NOT NULL,
  QuestionAnswerScore	int		NOT NULL,
  QuestionID		int 		NOT NULL
)

--
--	Person Surveys ( PersonSurveyID = LinkID from Link table Person to Survey )
--	person can answer survey more than one time
CREATE TABLE PersonSurvey (
  PersonSurveyID	int		NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
  --DateModified		datetime	NULL 
)

--	Person Survey answers ( QuestionAnswerID or QuestionAnswerText or both )
CREATE TABLE PersonSurveyAnswer (
  PersonSurveyAnswerID	int 	IDENTITY (1,1) NOT NULL,
  PersonSurveyID	int 		NOT NULL,
  QuestionID		int 		NOT NULL,
  QuestionAnswerID	int 		NOT NULL
)

CREATE TABLE PersonSurveyAnswerText (
  PersonSurveyAnswerID	int 		NOT NULL,
  QuestionAnswerText	text		NOT NULL
)


------------------------------------------------------------------
--	FROM 103
------------------------------------------------------------------
CREATE TABLE Comment (
  CommentID		int	NOT NULL,
  CommentText		text	NOT NULL
)

CREATE TABLE FinancialGoal (
  FinancialGoalID	int		NOT NULL,
  GoalCodeID		int		NULL, -- e.g. WealthAccumulation, Retirement
  TargetAge		int		NULL,
  TargetIncome		numeric(15,4)	NULL,
  YearsIncomeReq	int		NULL,
  ResidualReq		numeric(15,4)	NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)


------------------------------------------------------------------
--	FROM 104
------------------------------------------------------------------
CREATE  TABLE Model (
  ModelID		int 		NOT NULL,
  ModelTypeID		int 		NOT NULL,
  ModelTitle		varchar(64)	NOT NULL,
  ModelDesc		varchar(512),
  ModelData		text		NOT NULL,
  DateModified		datetime,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
);

--
--		StrategyModel::Model (Strategy extension to Model table AS 0..1)
--
CREATE  TABLE StrategyModel ( 
  StrategyModelID		int 		NOT NULL,
  -- calculated total on all StrategyFinancial Assets
  TotalInitialAmount		numeric(15,4),
  -- calculated CashFlow amount (has to be >= 0)
  TotalContributionAmount	numeric(15,4)
);



--
--	store application specific parameters here
--
CREATE TABLE Params (
  ParamName		varchar(30)	NOT NULL,
  ParamTypeID		int		NOT NULL,
  ParamValue		varchar(100)	NULL,
  ParamDesc		varchar(200)	NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)

