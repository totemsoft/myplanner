------------------------------------------------------------------
--	to keep track of db updates				--
--	version control	(XXX.01.01)				--
--		XXX - client org code --
--		01 - major version number			--
--		01 - minor version number			--
------------------------------------------------------------------
CREATE TABLE DBVersion (
  CurrentVersion	varchar(9)	NOT NULL,
  PreviousVersion	varchar(9)	NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)

-- to mark first update script (sql\update\001\ directory) 
INSERT DBVersion (CurrentVersion)
VALUES ('FID.01.01')




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
--		Anticipated
--			AnticipatedPayment
--			AnticipatedTransfer
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
  DateAcquired			datetime	NULL
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
  UnitsShares			numeric(19,8)	NULL
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
  UnitsShares			numeric(19,8)	NULL
)



-- Regular extends Financial
CREATE TABLE Regular (
  RegularID			int		NOT NULL,
  RegularAmount			numeric(15,4)	NULL,
  FrequencyCodeID		int		NULL,
  DateNext			datetime	NULL
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



-- Anticipated extends Financial
CREATE TABLE Anticipated (
  AnticipatedID			int		NOT NULL,
  PeriodCodeID			int		NULL  
)

-- AnticipatedPayment extends Anticipated
CREATE TABLE AnticipatedPayment (
  AnticipatedPaymentID		int		NOT NULL,
  SourceCodeID			int		NULL
)

-- AnticipatedTransfer extends Anticipated
CREATE TABLE AnticipatedTransfer (
  AnticipatedTransferID		int		NOT NULL,
  ETPComponentCodeID		int		NULL,
  ETPAmount			numeric(15,4)	NULL  
)



CREATE TABLE Institution (
  InstitutionID		int 		NOT NULL,
  InstitutionName	varchar(100)	NOT NULL
)


--
--	Codes/Types lookup tables
--
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
  ac_num		varchar(8)	NOT NULL, --  (PK) 3+5
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
  QuestionTypeDesc	varchar(50)	NOT NULL,
)



--
--	Survey tables
--
--	use Link table to associate this SurveyID with another ObjectType
--	(e.g. Financial's survey)
--
CREATE TABLE Survey (
  SurveyID		int 		NOT NULL,
  SurveyTitle		varchar(100)	NOT NULL,
  SurveyDesc		varchar(200)	NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)

CREATE TABLE Question (
  QuestionID		int	IDENTITY (1,1)	NOT NULL,
  QuestionTypeID	int 		NOT NULL,
  QuestionDesc		text		NOT NULL,
  SurveyID		int 		NOT NULL
)

CREATE TABLE QuestionAnswer (
  QuestionAnswerID	int	IDENTITY (1,1)	NOT NULL,
  QuestionAnswerDesc	text		NOT NULL,
  QuestionAnswerScore	int		NOT NULL,
  QuestionID		int 		NOT NULL
)

--
--	Person Surveys
--	person can answre survey more than one time
CREATE TABLE PersonSurvey (
  PersonSurveyID	int	IDENTITY (1,1)	NOT NULL,
  PersonID		int 		NOT NULL,
  SurveyID		int 		NOT NULL,
  DateCreated		datetime	NOT NULL DEFAULT getDate()
)

--	Person Survey answers
CREATE TABLE PersonSurveyAnswer (
  PersonSurveyAnswerID	int	IDENTITY (1,1)	NOT NULL,
  PersonSurveyID	int 		NOT NULL,
  QuestionID		int 		NOT NULL,
  QuestionAnswerID	int 		NOT NULL
)

--	Person Survey answers for QuestionTypeID = 5 ( FreeText )
CREATE TABLE PersonSurveyAnswerText (
  PersonSurveyAnswerTextID	int		NOT NULL,
  AnswerText			text		NOT NULL
)

--
--				PRIMARY KEY
--
ALTER TABLE DBVersion ADD CONSTRAINT PK_CurrentVersion PRIMARY KEY (CurrentVersion)

ALTER TABLE Financial ADD CONSTRAINT PK_FinancialID PRIMARY KEY (FinancialID)
ALTER TABLE Asset ADD CONSTRAINT PK_AssetID PRIMARY KEY (AssetID)
ALTER TABLE AssetCash ADD CONSTRAINT PK_AssetCashID PRIMARY KEY (AssetCashID)
ALTER TABLE AssetInvestment ADD CONSTRAINT PK_AssetInvestmentID PRIMARY KEY (AssetInvestmentID)
ALTER TABLE AssetPersonal ADD CONSTRAINT PK_AssetPersonalID PRIMARY KEY (AssetPersonalID)
ALTER TABLE AssetSuperannuation ADD CONSTRAINT PK_AssetSuperannuationID PRIMARY KEY (AssetSuperannuationID)
ALTER TABLE Regular ADD CONSTRAINT PK_RegularID PRIMARY KEY (RegularID)
ALTER TABLE RegularIncome ADD CONSTRAINT PK_RegularIncomeID PRIMARY KEY (RegularIncomeID)
ALTER TABLE RegularExpense ADD CONSTRAINT PK_RegularExpenseID PRIMARY KEY (RegularExpenseID)
ALTER TABLE Liability ADD CONSTRAINT PK_LiabilityID PRIMARY KEY (LiabilityID)
ALTER TABLE Insurance ADD CONSTRAINT PK_InsuranceID PRIMARY KEY (InsuranceID)
ALTER TABLE Anticipated ADD CONSTRAINT PK_AnticipatedID PRIMARY KEY (AnticipatedID)
ALTER TABLE AnticipatedPayment ADD CONSTRAINT PK_AnticipatedPaymentID PRIMARY KEY (AnticipatedPaymentID)
ALTER TABLE AnticipatedTransfer ADD CONSTRAINT PK_AnticipatedTransferID PRIMARY KEY (AnticipatedTransferID)

ALTER TABLE Institution ADD CONSTRAINT PK_InstitutionID PRIMARY KEY (InstitutionID)

ALTER TABLE FinancialType ADD CONSTRAINT PK_FinancialTypeID PRIMARY KEY (FinancialTypeID)
ALTER TABLE FinancialCode ADD CONSTRAINT PK_FinancialCodeType PRIMARY KEY (FinancialCodeID,FinancialTypeID)
ALTER TABLE FinancialMapSV2 ADD CONSTRAINT PK_ac_num PRIMARY KEY (ac_num)
ALTER TABLE OwnerCode ADD CONSTRAINT PK_OwnerCodeID PRIMARY KEY (OwnerCodeID)
ALTER TABLE InvestmentType ADD CONSTRAINT PK_InvestmentTypeID PRIMARY KEY (InvestmentTypeID)
ALTER TABLE FundType ADD CONSTRAINT PK_FundTypeID PRIMARY KEY (FundTypeID)
ALTER TABLE FrequencyCode ADD CONSTRAINT PK_FrequencyCodeID PRIMARY KEY (FrequencyCodeID)
ALTER TABLE PeriodCode ADD CONSTRAINT PK_PeriodCodeID PRIMARY KEY (PeriodCodeID)
ALTER TABLE SourceCode ADD CONSTRAINT PK_SourceCodeID PRIMARY KEY (SourceCodeID)
ALTER TABLE ETPComponentCode ADD CONSTRAINT PK_ETPComponentCodeID PRIMARY KEY (ETPComponentCodeID)

ALTER TABLE QuestionType ADD CONSTRAINT PK_QuestionTypeID PRIMARY KEY (QuestionTypeID)
ALTER TABLE Survey ADD CONSTRAINT PK_SurveyID PRIMARY KEY (SurveyID)
ALTER TABLE Question ADD CONSTRAINT PK_QuestionID PRIMARY KEY (QuestionID)
ALTER TABLE QuestionAnswer ADD CONSTRAINT PK_QuestionAnswerID PRIMARY KEY (QuestionAnswerID)
ALTER TABLE PersonSurvey ADD CONSTRAINT PK_PersonSurveyID PRIMARY KEY (PersonSurveyID)
ALTER TABLE PersonSurveyAnswer ADD CONSTRAINT PK_PersonSurveyAnswerID PRIMARY KEY (PersonSurveyAnswerID)
ALTER TABLE PersonSurveyAnswerText ADD CONSTRAINT PK_PersonSurveyAnswerTextID PRIMARY KEY (PersonSurveyAnswerTextID)


--
--				FOREIGN KEY
--
ALTER TABLE DBVersion ADD
  CONSTRAINT FK_DBVersion_PreviousVersion FOREIGN KEY (PreviousVersion) REFERENCES DBVersion (CurrentVersion)


ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_Object FOREIGN KEY (FinancialID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Financial_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID),
  CONSTRAINT FK_Financial_Institution FOREIGN KEY (InstitutionID) REFERENCES Institution (InstitutionID),
  CONSTRAINT FK_Financial_OwnerCode FOREIGN KEY (OwnerCodeID) REFERENCES OwnerCode (OwnerCodeID),
  CONSTRAINT FK_Financial_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID),
  CONSTRAINT FK_Financial_Next FOREIGN KEY (NextID) REFERENCES Financial (FinancialID)


ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Financial FOREIGN KEY (AssetID) REFERENCES Financial (FinancialID)

ALTER TABLE AssetCash ADD
  CONSTRAINT FK_AssetCash_Asset FOREIGN KEY (AssetCashID) REFERENCES Asset (AssetID)

ALTER TABLE AssetInvestment ADD
  CONSTRAINT FK_AssetInvestment_Asset FOREIGN KEY (AssetInvestmentID) REFERENCES Asset (AssetID),
  CONSTRAINT FK_AssetInvestment_InvestmentType FOREIGN KEY (InvestmentTypeID) REFERENCES InvestmentType (InvestmentTypeID)

ALTER TABLE AssetPersonal ADD
  CONSTRAINT FK_AssetPersonal_Asset FOREIGN KEY (AssetPersonalID) REFERENCES Asset (AssetID)

ALTER TABLE AssetSuperannuation ADD
  CONSTRAINT FK_AssetSuperannuation_Asset FOREIGN KEY (AssetSuperannuationID) REFERENCES Asset (AssetID),
  CONSTRAINT FK_AssetSuperannuation_FundType FOREIGN KEY (FundTypeID) REFERENCES FundType (FundTypeID)


ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_Financial FOREIGN KEY (RegularID) REFERENCES Financial (FinancialID),
  CONSTRAINT FK_Regular_FrequencyCode FOREIGN KEY (FrequencyCodeID) REFERENCES FrequencyCode (FrequencyCodeID)

ALTER TABLE RegularIncome ADD
  CONSTRAINT FK_RegularIncome_Regular FOREIGN KEY (RegularIncomeID) REFERENCES Regular (RegularID)

ALTER TABLE RegularExpense ADD
  CONSTRAINT FK_RegularExpense_Regular FOREIGN KEY (RegularExpenseID) REFERENCES Regular (RegularID)

ALTER TABLE Liability ADD
  CONSTRAINT FK_Liability_RegularExpense FOREIGN KEY (LiabilityID) REFERENCES RegularExpense (RegularExpenseID)

ALTER TABLE Insurance ADD
  CONSTRAINT FK_Insurance_RegularExpense FOREIGN KEY (InsuranceID) REFERENCES RegularExpense (RegularExpenseID),
  CONSTRAINT FK_Insurance_WaitingPeriodCode FOREIGN KEY (WaitingPeriodCodeID) REFERENCES PeriodCode (PeriodCodeID),
  CONSTRAINT FK_Insurance_BenefitPeriodCode FOREIGN KEY (BenefitPeriodCodeID) REFERENCES PeriodCode (PeriodCodeID)


ALTER TABLE Anticipated ADD
  CONSTRAINT FK_Anticipated_Financial FOREIGN KEY (AnticipatedID) REFERENCES Financial (FinancialID),
  CONSTRAINT FK_Anticipated_PeriodCode FOREIGN KEY (PeriodCodeID) REFERENCES PeriodCode (PeriodCodeID)

ALTER TABLE AnticipatedPayment ADD
  CONSTRAINT FK_AnticipatedPayment_Anticipated FOREIGN KEY (AnticipatedPaymentID) REFERENCES Anticipated (AnticipatedID),
  CONSTRAINT FK_AnticipatedPayment_SourceCode FOREIGN KEY (SourceCodeID) REFERENCES SourceCode (SourceCodeID)

ALTER TABLE AnticipatedTransfer ADD
  CONSTRAINT FK_AnticipatedTransfer_Anticipated FOREIGN KEY (AnticipatedTransferID) REFERENCES Anticipated (AnticipatedID),
  CONSTRAINT FK_AnticipatedTransfer_ETPComponentCode FOREIGN KEY (ETPComponentCodeID) REFERENCES ETPComponentCode (ETPComponentCodeID)



ALTER TABLE FinancialType ADD
  CONSTRAINT FK_FinancialType_ObjectType FOREIGN KEY (ObjectTypeID) REFERENCES ObjectType (ObjectTypeID)
ALTER TABLE FinancialCode ADD
  CONSTRAINT FK_FinancialCode_FinancialType FOREIGN KEY (FinancialTypeID) REFERENCES FinancialType (FinancialTypeID)



ALTER TABLE FinancialMapSV2 ADD
  CONSTRAINT FK_FinancialMapSV2_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID)



ALTER TABLE Survey ADD
  CONSTRAINT FK_Survey_Object FOREIGN KEY (SurveyID) REFERENCES Object (ObjectID)

ALTER TABLE Question ADD
  CONSTRAINT FK_Question_QuestionType FOREIGN KEY (QuestionTypeID) REFERENCES QuestionType (QuestionTypeID),
  CONSTRAINT FK_Question_SurveyID FOREIGN KEY (SurveyID) REFERENCES Survey (SurveyID)

ALTER TABLE QuestionAnswer ADD
  CONSTRAINT FK_QuestionAnswer_Question FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID)

ALTER TABLE PersonSurvey ADD
  CONSTRAINT FK_PersonSurvey_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID),
  CONSTRAINT FK_PersonSurvey_Survey FOREIGN KEY (SurveyID) REFERENCES Survey (SurveyID)

ALTER TABLE PersonSurveyAnswer ADD
  CONSTRAINT FK_PersonSurveyAnswer_PersonSurvey FOREIGN KEY (PersonSurveyID) REFERENCES PersonSurvey (PersonSurveyID),
  CONSTRAINT FK_PersonSurveyAnswer_Question FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID),
  CONSTRAINT FK_PersonSurveyAnswer_QuestionAnswer FOREIGN KEY (QuestionAnswerID) REFERENCES QuestionAnswer (QuestionAnswerID)

ALTER TABLE PersonSurveyAnswerText ADD
  CONSTRAINT FK_PersonSurveyAnswerText_PersonSurveyAnswer FOREIGN KEY (PersonSurveyAnswerTextID) REFERENCES PersonSurveyAnswer (PersonSurveyAnswerID)


--
--					INDEX
--
CREATE UNIQUE INDEX IDX_FinancialMapSV2_1 ON FinancialMapSV2 ( FinancialTypeID, FinancialCodeID )
