------------------------------------------------
--	START changing existing data structure
------------------------------------------------

DROP TABLE DependentFinancials
DROP TABLE DependentPerson

-- to allow link ObjectID1 to null value (e.g. relationship code - none)
ALTER TABLE Link ALTER COLUMN ObjectID2 int NULL

-- if NULL then active link
-- else (any character) - logically deleted
ALTER TABLE Link ADD LogicallyDeleted	char	NULL

-- was NOT NULL DEFAULT 'N'
ALTER TABLE FinancialMapSV2 ALTER COLUMN LogicallyDeleted char NULL

-- VSH:	02/11/2001
-- set primary key on FinancialCode (FinancialCodeID,FinancialTypeID)
ALTER TABLE Financial DROP CONSTRAINT FK_Financial_FinancialCode		--FK_Financial_FinancialCodeType
ALTER TABLE FinancialMapSV2 DROP CONSTRAINT FK_FinancialMapSV2_FinancialCode	--FK_FinancialMapSV2_FinancialCodeType
ALTER TABLE FinancialCode DROP CONSTRAINT FK_FinancialCode_FinancialType
ALTER TABLE FinancialCode DROP CONSTRAINT PK_FinancialCodeID			--PK_FinancialCodeType

ALTER TABLE FinancialCode ADD CONSTRAINT PK_FinancialCodeType PRIMARY KEY (FinancialCodeID,FinancialTypeID)
ALTER TABLE FinancialCode ADD
  CONSTRAINT FK_FinancialCode_FinancialType FOREIGN KEY (FinancialTypeID) REFERENCES FinancialType (FinancialTypeID)
ALTER TABLE FinancialMapSV2 ADD
  CONSTRAINT FK_FinancialMapSV2_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID)
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID)


-- VSH:	07/11/2001
-- Asset - Beneficiary person
ALTER TABLE Asset ADD BeneficiaryID int	NULL
ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Person FOREIGN KEY (BeneficiaryID) REFERENCES Person (PersonID)

-- Asset - Associated Liability item
ALTER TABLE Asset ADD LiabilityID int NULL
ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Liability FOREIGN KEY (LiabilityID) REFERENCES Liability (LiabilityID)

-- Regular - Associated Asset item
ALTER TABLE Regular ADD AssetID int NULL
ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_Asset FOREIGN KEY (AssetID) REFERENCES Asset (AssetID)

-- VSH:	12/11/2001
-- AnticipatedPayment - Associated Liability item
ALTER TABLE AnticipatedPayment ADD LiabilityID int NULL
ALTER TABLE AnticipatedPayment ADD
  CONSTRAINT FK_AnticipatedPayment_Liability FOREIGN KEY (LiabilityID) REFERENCES Liability (LiabilityID)

-- AnticipatedTransfer - Associated Asset item
ALTER TABLE AnticipatedTransfer ADD AssetID int NULL
ALTER TABLE AnticipatedTransfer ADD
  CONSTRAINT FK_AnticipatedTransfer_Asset FOREIGN KEY (AssetID) REFERENCES Asset (AssetID)

------------------------------------------------
--	END changing existing data structure
------------------------------------------------



--
--	lookup tables
--
CREATE TABLE ParamType (
  ParamTypeID		int		NOT NULL,
  ParamTypeDesc		varchar(200)	NOT NULL
)

CREATE TABLE LifeExpectancy (
  Age			int		NOT NULL,
  SexCodeID		int		NOT NULL,
  Value			numeric(5,2)	NOT NULL,
  CountryCodeID		int		NOT NULL,
  DateModified		datetime	NOT NULL
)

-- VSH:	12/11/2001
CREATE TABLE HealthConditionCode (
  HealthConditionCodeID		int		NOT NULL,
  HealthConditionCodeDesc	varchar(50)	NOT NULL
)


--
--	tables
--
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




--
--				PRIMARY KEY
--
ALTER TABLE ParamType ADD CONSTRAINT PK_ParamType PRIMARY KEY (ParamTypeID)
ALTER TABLE Params ADD CONSTRAINT PK_Params PRIMARY KEY (ParamName, ParamTypeID)
ALTER TABLE LifeExpectancy ADD CONSTRAINT PK_LifeExpectancy PRIMARY KEY (Age, SexCodeID)
ALTER TABLE HealthConditionCode ADD CONSTRAINT PK_HealthConditionCode PRIMARY KEY (HealthConditionCodeID)

-- VSH:	12/11/2001
ALTER TABLE Comment ADD CONSTRAINT PK_Comment PRIMARY KEY (CommentID)
-- VSH:	14/11/2001
ALTER TABLE FinancialGoal ADD CONSTRAINT PK_FinancialGoal PRIMARY KEY (FinancialGoalID)



--
--				FOREIGN KEY
--
ALTER TABLE Params ADD
  CONSTRAINT FK_Params_ParamType FOREIGN KEY (ParamTypeID) REFERENCES ParamType (ParamTypeID)

ALTER TABLE LifeExpectancy ADD
  CONSTRAINT FK_LifeExpectancy_SexCode FOREIGN KEY (SexCodeID) REFERENCES SexCode (SexCodeID),
  CONSTRAINT FK_LifeExpectancy_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID)

-- VSH:	12/11/2001
ALTER TABLE Comment ADD
  CONSTRAINT FK_Comment_Object FOREIGN KEY (CommentID) REFERENCES Object (ObjectID)

-- VSH:	14/11/2001
ALTER TABLE FinancialGoal ADD
  CONSTRAINT FK_FinancialGoal_Object FOREIGN KEY (FinancialGoalID) REFERENCES Object (ObjectID)


--
--				INDEX
--
--CREATE UNIQUE INDEX IDX_table_1 ON table ( FinancialTypeID, FinancialCodeID )



--
--				INSERT/UPDATE
--
DELETE FROM ParamType
GO

INSERT ParamType (ParamTypeID, ParamTypeDesc)
VALUES (1, 'General parameter') -- some of these params will be stored locally

INSERT ParamType (ParamTypeID, ParamTypeDesc)
VALUES (2, 'DSS parameter')



DELETE FROM LifeExpectancy
GO

DECLARE @CountryCodeID int
SET @CountryCodeID = 13	-- Australia

DECLARE @DateModified datetime
SET @DateModified = getDate()

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 15, 2, 61.38, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 15, 1, 66.97, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 16, 2, 60.41, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 16, 1, 65.98, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 17, 2, 59.44, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 17, 1, 65, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 18, 2, 58.49, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 18, 1, 64.02, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 19, 2, 57.55, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 19, 1, 63.04, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 20, 2, 56.61, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 20, 1, 62.07, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 21, 2, 55.68, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 21, 1, 61.09, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 22, 2, 54.75, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 22, 1, 60.12, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 23, 2, 53.81, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 23, 1, 59.14, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 24, 2, 52.88, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 24, 1, 58.16, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 25, 2, 51.94, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 25, 1, 57.18, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 26, 2, 51.01, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 26, 1, 56.21, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 27, 2, 50.07, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 27, 1, 55.23, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 28, 2, 49.14, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 28, 1, 54.25, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 29, 2, 48.2, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 29, 1, 53.27, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 30, 2, 47.26, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 30, 1, 52.3, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 31, 2, 46.32, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 31, 1, 51.32, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 32, 2, 45.38, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 32, 1, 50.35, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 33, 2, 44.44, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 33, 1, 49.38, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 34, 2, 43.5, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 34, 1, 48.41, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 35, 2, 42.57, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 35, 1, 47.44, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 36, 2, 41.63, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 36, 1, 46.47, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 37, 2, 40.69, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 37, 1, 45.5, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 38, 2, 39.75, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 38, 1, 44.53, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 39, 2, 38.81, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 39, 1, 43.56, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 40, 2, 37.88, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 40, 1, 42.6, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 41, 2, 36.94, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 41, 1, 41.64, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 42, 2, 36.01, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 42, 1, 40.68, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 43, 2, 35.08, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 43, 1, 39.72, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 44, 2, 34.15, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 44, 1, 38.76, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 45, 2, 33.22, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 45, 1, 37.81, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 46, 2, 32.3, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 46, 1, 36.86, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 47, 2, 31.38, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 47, 1, 35.92, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 48, 2, 30.46, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 48, 1, 34.98, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 49, 2, 29.55, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 49, 1, 34.04, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 50, 2, 28.64, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 50, 1, 33.11, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 51, 2, 27.74, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 51, 1, 32.18, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 52, 2, 26.85, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 52, 1, 31.26, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 53, 2, 25.1, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 53, 1, 30.34, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 54, 2, 25.09, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 54, 1, 29.43, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 55, 2, 24.22, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 55, 1, 28.53, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 56, 2, 23.36, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 56, 1, 27.63, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 57, 2, 22.52, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 57, 1, 26.74, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 58, 2, 21.68, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 58, 1, 25.86, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 59, 2, 20.86, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 59, 1, 24.98, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 60, 2, 20.05, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 60, 1, 24.11, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 61, 2, 19.25, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 61, 1, 23.25, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 62, 2, 18.46, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 62, 1, 22.39, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 63, 2, 17.7, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 63, 1, 21.54, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 64, 2, 16.94, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 64, 1, 20.7, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 65, 2, 16.21, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 65, 1, 19.88, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 66, 2, 15.49, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 66, 1, 19.06, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 67, 2, 14.79, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 67, 1, 18.25, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 68, 2, 14.11, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 68, 1, 17.46, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 69, 2, 13.44, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 69, 1, 16.67, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 70, 2, 12.8, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 70, 1, 15.9, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 71, 2, 12.17, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 71, 1, 15.14, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 72, 2, 11.56, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 72, 1, 14.4, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 73, 2, 10.96, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 73, 1, 13.67, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 74, 2, 10.38, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 74, 1, 12.96, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 75, 2, 9.82, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 75, 1, 12.26, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 76, 2, 9.27, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 76, 1, 11.58, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 77, 2, 8.74, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 77, 1, 10.92, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 78, 2, 8.24, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 78, 1, 10.28, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 79, 2, 7.76, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 79, 1, 9.67, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 80, 2, 7.3, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 80, 1, 9.09, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 81, 2, 6.87, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 81, 1, 8.53, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 82, 2, 6.46, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 82, 1, 8, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 83, 2, 6.08, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 83, 1, 7.48, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 84, 2, 5.71, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 84, 1, 7, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 85, 2, 5.4, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 85, 1, 6.53, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 86, 2, 5.1, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 86, 1, 6.1, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 87, 2, 4.82, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 87, 1, 5.69, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 88, 2, 4.57, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 88, 1, 5.32, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 89, 2, 4.53, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 89, 1, 4.98, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 90, 2, 4.16, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 90, 1, 4.67, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 91, 2, 3.99, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 91, 1, 4.39, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 92, 2, 3.86, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 92, 1, 4.15, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 93, 2, 3.73, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 93, 1, 3.93, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 94, 2, 3.62, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 94, 1, 3.72, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 95, 2, 3.5, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 95, 1, 3.54, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 96, 2, 3.39, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 96, 1, 3.37, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 97, 2, 3.28, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 97, 1, 3.21, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 98, 2, 3.18, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 98, 1, 3.07, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 99, 2, 3.07, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 99, 1, 2.93, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 100, 2, 2.98, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 100, 1, 2.81, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 101, 2, 2.88, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 101, 1, 2.7, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 102, 2, 2.79, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 102, 1, 2.59, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 103, 2, 2.71, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 103, 1, 2.5, @CountryCodeID, @DateModified )	-- female

INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 104, 2, 2.64, @CountryCodeID, @DateModified )	-- male
INSERT LifeExpectancy (Age, SexCodeID, Value, CountryCodeID, DateModified)
VALUES ( 104, 1, 2.41, @CountryCodeID, @DateModified )	-- female


--
--
--
UPDATE RelationshipFinanceCode SET RelationshipFinanceCodeDesc = 'Will Executor' WHERE RelationshipFinanceCodeID = 13

-- VSH:	12/11/2001
INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (25, 'Comment table')

-- VSH:	14/11/2001
INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (26, 'FinancialGoal table')

-- XXXXXXYYY (9 = 6 + 3 digits max for int)
INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1025, 'Link table (Person to Comment)')
INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1025, 1, 25, 'Link Person to Comment')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1026, 'Link table (Person to FinancialGoal)')
INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1026, 1, 26, 'Link Person to FinancialGoal')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1025026, 'Link table (Person-Comment to FinancialGoal)')
INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1025026, 1025, 26, 'Link Person-Comment to FinancialGoal')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1025018, 'Link table (Person-Comment to AnticipatedPayment)')
INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1025018, 1025, 18, 'Link Person-Comment to AnticipatedPayment')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1025019, 'Link table (Person-Comment to AnticipatedTransfer)')
INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1025019, 1025, 19, 'Link Person-Comment to AnticipatedTransfer')



-- KM		13/11/2001

--
-- Anticipated Payment Types
--	Debt Reduction
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (59, 'Debt Reduction', 19)

--
-- Anticipated Payment Codes - Debt Reduction Type
--
INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (1, 'Personal Loan', 59)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (2, 'Credit Cards', 59)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (3, 'Investment Loan', 59)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (4, 'Mortgage', 59)

--
-- Anticipated Payment Types
--	Short/Long Term Expenditure
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (60, 'Expenditure', 19)

--
-- Anticipated Payment Codes - Expenditure Type
--
INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (1, 'Holiday', 60)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (2, 'Car', 60)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (3, 'Gifts', 60)

--
--
--
INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (1, 'High Blood Pressure')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (2, 'Mental Disorder')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (3, 'Back Problems')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (4, 'Arthritis')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (5, 'Asthma')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (6, 'Neurological Disorder')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (7, 'Digestive/Bowl Disorder')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (8, 'Hepatitis, Cirrosis or Liver/Gall Bladder Disease')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (9, 'Kidney or Bladder Disorder')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (10, 'Cancer, Cyst or Tumor')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (11, 'Skin Condition')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (12, 'Blood Disorder')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (13, 'Over/Under Weight')

INSERT HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (14, 'Fluctuation of 5kg or More Within 12mths')



--
-- this should be the last statement in any update script
--
INSERT DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.03', 'FID.01.02')

