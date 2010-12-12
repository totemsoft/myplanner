#ifdef MSSQL
ALTER TABLE Object ADD CONSTRAINT PK_ObjectID PRIMARY KEY (ObjectID)
#endif MSSQL;
ALTER TABLE ObjectType ADD CONSTRAINT PK_ObjectTypeID PRIMARY KEY (ObjectTypeID) GO
ALTER TABLE Link ADD CONSTRAINT PK_LinkID PRIMARY KEY (LinkID) GO
ALTER TABLE LinkObjectType ADD CONSTRAINT PK_LinkObjectTypeID PRIMARY KEY (LinkObjectTypeID) GO

ALTER TABLE SexCode ADD CONSTRAINT PK_SexCodeID PRIMARY KEY (SexCodeID) GO
ALTER TABLE MaritalCode ADD CONSTRAINT PK_MaritalCodeID PRIMARY KEY (MaritalCodeID) GO
ALTER TABLE TitleCode ADD CONSTRAINT PK_TitleCodeID PRIMARY KEY (TitleCodeID) GO
ALTER TABLE RelationshipCode ADD CONSTRAINT PK_RelationshipCodeID PRIMARY KEY (RelationshipCodeID) GO
ALTER TABLE RelationshipFinanceCode ADD CONSTRAINT PK_RelationshipFinanceCodeID PRIMARY KEY (RelationshipFinanceCodeID) GO
ALTER TABLE AddressCode ADD CONSTRAINT PK_AddressCodeID PRIMARY KEY (AddressCodeID) GO
ALTER TABLE SuburbPostCode ADD CONSTRAINT PK_SuburbPostCode PRIMARY KEY (PostCode, Suburb) GO
ALTER TABLE StateCode ADD CONSTRAINT PK_StateCodeID PRIMARY KEY (StateCodeID) GO
ALTER TABLE CountryCode ADD CONSTRAINT PK_CountryCodeID PRIMARY KEY (CountryCodeID) GO
ALTER TABLE ContactMediaCode ADD CONSTRAINT PK_ContactMediaCodeID PRIMARY KEY (ContactMediaCodeID) GO
ALTER TABLE LanguageCode ADD CONSTRAINT PK_LanguageCodeID PRIMARY KEY (LanguageCodeID) GO
ALTER TABLE IndustryCode ADD CONSTRAINT PK_IndustryCodeID PRIMARY KEY (IndustryCodeID) GO
ALTER TABLE OccupationCode ADD CONSTRAINT PK_OccupationCodeID PRIMARY KEY (OccupationCodeID) GO
ALTER TABLE ContactOccupationCode ADD CONSTRAINT PK_ContactOccupationCodeID PRIMARY KEY (ContactOccupationCodeID) GO
ALTER TABLE BusinessStructureCode ADD CONSTRAINT PK_BusinessStructureCodeID PRIMARY KEY (BusinessStructureCodeID) GO
ALTER TABLE ReferalSourceCode ADD CONSTRAINT PK_ReferalSourceCodeID PRIMARY KEY (ReferalSourceCodeID) GO
ALTER TABLE ResidenceStatusCode ADD CONSTRAINT PK_ResidenceStatusCodeID PRIMARY KEY (ResidenceStatusCodeID) GO
ALTER TABLE HealthStateCode ADD CONSTRAINT PK_HealthStateCodeID PRIMARY KEY (HealthStateCodeID) GO
ALTER TABLE EmploymentStatusCode ADD CONSTRAINT PK_EmploymentStatusCodeID PRIMARY KEY (EmploymentStatusCodeID) GO
ALTER TABLE AdviserTypeCode ADD CONSTRAINT PK_AdviserTypeCodeID PRIMARY KEY (AdviserTypeCodeID) GO
ALTER TABLE StatusCode ADD CONSTRAINT PK_StatusCodeID PRIMARY KEY (StatusCodeID) GO
ALTER TABLE WillStatusCode ADD CONSTRAINT PK_WillStatusCodeID PRIMARY KEY (WillStatusCodeID) GO
ALTER TABLE WillChangeCode ADD CONSTRAINT PK_WillChangeCodeID PRIMARY KEY (WillChangeCodeID) GO
ALTER TABLE SupporterCode ADD CONSTRAINT PK_SupporterCodeID PRIMARY KEY (SupporterCodeID) GO
ALTER TABLE AllowanceCode ADD CONSTRAINT PK_AllowanceCodeID PRIMARY KEY (AllowanceCodeID) GO


ALTER TABLE Person ADD CONSTRAINT PK_PersonID PRIMARY KEY (PersonID) GO
#ifdef MSSQL
ALTER TABLE PersonHealth ADD CONSTRAINT PK_PersonHealthID PRIMARY KEY (PersonHealthID)
#endif MSSQL;
#ifdef MSSQL
ALTER TABLE PersonOccupation ADD CONSTRAINT PK_PersonOccupationID PRIMARY KEY (PersonOccupationID)
#endif MSSQL;
#ifdef MSSQL
ALTER TABLE PersonTrustDIYStatus ADD CONSTRAINT PK_PersonTrustDIYStatusID PRIMARY KEY (PersonTrustDIYStatusID)
#endif MSSQL;
ALTER TABLE ClientPerson ADD CONSTRAINT PK_ClientPersonID PRIMARY KEY (ClientPersonID) GO
ALTER TABLE UserPerson ADD CONSTRAINT PK_UserPersonID PRIMARY KEY (UserPersonID) GO
ALTER TABLE Business ADD CONSTRAINT PK_BusinessID PRIMARY KEY (BusinessID) GO
ALTER TABLE Address ADD CONSTRAINT PK_AddressID PRIMARY KEY (AddressID) GO
ALTER TABLE ContactMedia ADD CONSTRAINT PK_ContactMediaID PRIMARY KEY (ContactMediaID) GO


ALTER TABLE Financial ADD CONSTRAINT PK_FinancialID PRIMARY KEY (FinancialID) GO
ALTER TABLE Asset ADD CONSTRAINT PK_AssetID PRIMARY KEY (AssetID) GO
ALTER TABLE AssetCash ADD CONSTRAINT PK_AssetCashID PRIMARY KEY (AssetCashID) GO
ALTER TABLE AssetInvestment ADD CONSTRAINT PK_AssetInvestmentID PRIMARY KEY (AssetInvestmentID) GO
ALTER TABLE AssetPersonal ADD CONSTRAINT PK_AssetPersonalID PRIMARY KEY (AssetPersonalID) GO
ALTER TABLE AssetSuperannuation ADD CONSTRAINT PK_AssetSuperannuationID PRIMARY KEY (AssetSuperannuationID) GO
ALTER TABLE Regular ADD CONSTRAINT PK_RegularID PRIMARY KEY (RegularID) GO
ALTER TABLE RegularIncome ADD CONSTRAINT PK_RegularIncomeID PRIMARY KEY (RegularIncomeID) GO
ALTER TABLE RegularExpense ADD CONSTRAINT PK_RegularExpenseID PRIMARY KEY (RegularExpenseID) GO
ALTER TABLE Liability ADD CONSTRAINT PK_LiabilityID PRIMARY KEY (LiabilityID) GO
ALTER TABLE Insurance ADD CONSTRAINT PK_InsuranceID PRIMARY KEY (InsuranceID) GO

ALTER TABLE Institution ADD CONSTRAINT PK_InstitutionID PRIMARY KEY (InstitutionID) GO

ALTER TABLE FinancialType ADD CONSTRAINT PK_FinancialTypeID PRIMARY KEY (FinancialTypeID) GO
ALTER TABLE FinancialCode ADD CONSTRAINT PK_FinancialCodeType PRIMARY KEY (FinancialCodeID,FinancialTypeID) GO

ALTER TABLE OwnerCode ADD CONSTRAINT PK_OwnerCodeID PRIMARY KEY (OwnerCodeID) GO
ALTER TABLE InvestmentType ADD CONSTRAINT PK_InvestmentTypeID PRIMARY KEY (InvestmentTypeID) GO
ALTER TABLE FundType ADD CONSTRAINT PK_FundTypeID PRIMARY KEY (FundTypeID) GO
ALTER TABLE FrequencyCode ADD CONSTRAINT PK_FrequencyCodeID PRIMARY KEY (FrequencyCodeID) GO
ALTER TABLE PeriodCode ADD CONSTRAINT PK_PeriodCodeID PRIMARY KEY (PeriodCodeID) GO
ALTER TABLE SourceCode ADD CONSTRAINT PK_SourceCodeID PRIMARY KEY (SourceCodeID) GO
ALTER TABLE ETPComponentCode ADD CONSTRAINT PK_ETPComponentCodeID PRIMARY KEY (ETPComponentCodeID) GO

ALTER TABLE QuestionType ADD CONSTRAINT PK_QuestionTypeID PRIMARY KEY (QuestionTypeID) GO
ALTER TABLE Survey ADD CONSTRAINT PK_SurveyID PRIMARY KEY (SurveyID) GO
#ifdef MSSQL
ALTER TABLE Question ADD CONSTRAINT PK_QuestionID PRIMARY KEY (QuestionID)
#endif MSSQL;
#ifdef MSSQL
ALTER TABLE QuestionAnswer ADD CONSTRAINT PK_QuestionAnswerID PRIMARY KEY (QuestionAnswerID)
#endif MSSQL;
#ifdef MSSQL
ALTER TABLE PersonSurveyAnswer ADD CONSTRAINT PK_PersonSurveyAnswerID PRIMARY KEY (PersonSurveyAnswerID)
#endif MSSQL;
ALTER TABLE PersonSurvey ADD CONSTRAINT PK_PersonSurveyID PRIMARY KEY (PersonSurveyID) GO
ALTER TABLE PersonSurveyAnswerText ADD CONSTRAINT PK_PersonSurveyAnswerTextID PRIMARY KEY (PersonSurveyAnswerID) GO

ALTER TABLE ParamType ADD CONSTRAINT PK_ParamType PRIMARY KEY (ParamTypeID) GO
ALTER TABLE Params ADD CONSTRAINT PK_Params PRIMARY KEY (ParamName, ParamTypeID) GO
ALTER TABLE LifeExpectancy ADD CONSTRAINT PK_LifeExpectancy PRIMARY KEY (Age, SexCodeID) GO
ALTER TABLE HealthConditionCode ADD CONSTRAINT PK_HealthConditionCode PRIMARY KEY (HealthConditionCodeID) GO
ALTER TABLE Comment ADD CONSTRAINT PK_Comment PRIMARY KEY (CommentID) GO
ALTER TABLE FinancialGoal ADD CONSTRAINT PK_FinancialGoal PRIMARY KEY (FinancialGoalID) GO

ALTER TABLE PersonEstate ADD CONSTRAINT PK_PersonEstate PRIMARY KEY (PersonEstateID) GO

ALTER TABLE Model ADD CONSTRAINT PK_ModelID PRIMARY KEY (ModelID) GO
ALTER TABLE ModelType ADD CONSTRAINT PK_ModelTypeID PRIMARY KEY (ModelTypeID) GO

ALTER TABLE FinancialPool ADD CONSTRAINT PK_FinancialPoolID PRIMARY KEY (FinancialPoolID) GO

ALTER TABLE StrategyModel ADD CONSTRAINT PK_StrategyModel PRIMARY KEY (StrategyModelID) GO
ALTER TABLE StrategyFinancial ADD CONSTRAINT PK_StrategyFinancial_StrategyModel PRIMARY KEY (StrategyFinancialID, StrategyModelID) GO




ALTER TABLE Object ADD
  CONSTRAINT FK_Object_ObjectType FOREIGN KEY (ObjectTypeID) REFERENCES ObjectType (ObjectTypeID) GO

ALTER TABLE Link ADD
  CONSTRAINT FK_Link_Link FOREIGN KEY (LinkID) REFERENCES Object (ObjectID) GO
ALTER TABLE Link ADD
  CONSTRAINT FK_Link_Object1 FOREIGN KEY (ObjectID1) REFERENCES Object (ObjectID) GO
ALTER TABLE Link ADD
  CONSTRAINT FK_Link_Object2 FOREIGN KEY (ObjectID2) REFERENCES Object (ObjectID) GO
ALTER TABLE Link ADD
  CONSTRAINT FK_Link_LinkObjectType FOREIGN KEY (LinkObjectTypeID) REFERENCES LinkObjectType (LinkObjectTypeID) GO

ALTER TABLE LinkObjectType ADD
  CONSTRAINT FK_LinkObjectType_LinkObjectType FOREIGN KEY (LinkObjectTypeID) REFERENCES ObjectType (ObjectTypeID) GO
ALTER TABLE LinkObjectType ADD
  CONSTRAINT FK_LinkObjectType_ObjectType1 FOREIGN KEY (ObjectTypeID1) REFERENCES ObjectType (ObjectTypeID) GO
ALTER TABLE LinkObjectType ADD
  CONSTRAINT FK_LinkObjectType_ObjectType2 FOREIGN KEY (ObjectTypeID2) REFERENCES ObjectType (ObjectTypeID) GO
 
ALTER TABLE ContactOccupationCode ADD
  CONSTRAINT FK_ContactOccupationCode_OccupationCode FOREIGN KEY (ContactOccupationCodeID) REFERENCES OccupationCode (OccupationCodeID) GO

ALTER TABLE Person ADD
  CONSTRAINT FK_Person_Object FOREIGN KEY (PersonID) REFERENCES Object (ObjectID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_SexCode FOREIGN KEY (SexCodeID) REFERENCES SexCode (SexCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_TitleCode FOREIGN KEY (TitleCodeID) REFERENCES TitleCode (TitleCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_DOBCountry FOREIGN KEY (DOBCountryID) REFERENCES CountryCode (CountryCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_MaritalCode FOREIGN KEY (MaritalCodeID) REFERENCES MaritalCode (MaritalCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_PreferredLanguage FOREIGN KEY (PreferredLanguageID) REFERENCES LanguageCode (LanguageCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_ReferalSourceCode FOREIGN KEY (ReferalSourceCodeID) REFERENCES ReferalSourceCode (ReferalSourceCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_ResidenceStatusCode FOREIGN KEY (ResidenceStatusCodeID) REFERENCES ResidenceStatusCode (ResidenceStatusCodeID) GO
ALTER TABLE Person ADD
  CONSTRAINT FK_Person_ResidenceCountryCode FOREIGN KEY (ResidenceCountryCodeID) REFERENCES CountryCode (CountryCodeID) GO

ALTER TABLE ClientPerson ADD
  CONSTRAINT FK_ClientPerson_Person FOREIGN KEY (ClientPersonID) REFERENCES Person (PersonID) GO

ALTER TABLE UserPerson ADD
  CONSTRAINT FK_UserPerson_Person FOREIGN KEY (UserPersonID) REFERENCES Person (PersonID) GO
ALTER TABLE UserPerson ADD
  CONSTRAINT FK_UserPerson_AdviserTypeCode FOREIGN KEY (AdviserTypeCodeID) REFERENCES AdviserTypeCode (AdviserTypeCodeID) GO

ALTER TABLE RelationshipCode ADD
  CONSTRAINT FK_RelationshipCode_Object FOREIGN KEY (RelationshipCodeID) REFERENCES Object (ObjectID) GO

ALTER TABLE RelationshipFinanceCode ADD
  CONSTRAINT FK_RelationshipFinanceCode_Object FOREIGN KEY (RelationshipFinanceCodeID) REFERENCES Object (ObjectID) GO

ALTER TABLE SuburbPostCode ADD
  CONSTRAINT FK_SuburbPostCode_StateCode FOREIGN KEY (StateCodeID) REFERENCES StateCode (StateCodeID) GO
ALTER TABLE SuburbPostCode ADD
  CONSTRAINT FK_SuburbPostCode_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID) GO

ALTER TABLE Business ADD
  CONSTRAINT FK_Business_Object FOREIGN KEY (BusinessID) REFERENCES Object (ObjectID) GO
ALTER TABLE Business ADD
  CONSTRAINT FK_Business_IndustryCode FOREIGN KEY (IndustryCodeID) REFERENCES IndustryCode (IndustryCodeID) GO
ALTER TABLE Business ADD
  CONSTRAINT FK_Business_BusinessStructureCode FOREIGN KEY (BusinessStructureCodeID) REFERENCES BusinessStructureCode (BusinessStructureCodeID) GO
ALTER TABLE Business ADD
  CONSTRAINT FK_Business_Business FOREIGN KEY (ParentBusinessID) REFERENCES Business (BusinessID) GO

ALTER TABLE Address ADD 
  CONSTRAINT FK_Address_Object FOREIGN KEY (AddressID) REFERENCES Object (ObjectID) GO
ALTER TABLE Address ADD 
  CONSTRAINT FK_Address_AddressCode FOREIGN KEY (AddressCodeID) REFERENCES AddressCode (AddressCodeID) GO
ALTER TABLE Address ADD 
  CONSTRAINT FK_Address_SuburbPostCode FOREIGN KEY (PostCode,Suburb) REFERENCES SuburbPostCode (PostCode,Suburb) GO
ALTER TABLE Address ADD 
  CONSTRAINT FK_Address_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID) GO
ALTER TABLE Address ADD 
  CONSTRAINT FK_Address_StateCode FOREIGN KEY (StateCodeID) REFERENCES StateCode (StateCodeID) GO

ALTER TABLE ContactMedia ADD
  CONSTRAINT FK_ContactMedia_Object FOREIGN KEY (ContactMediaID) REFERENCES Object (ObjectID) GO
ALTER TABLE ContactMedia ADD
  CONSTRAINT FK_ContactMedia_ContactMediaCode FOREIGN KEY (ContactMediaCodeID) REFERENCES ContactMediaCode (ContactMediaCodeID) GO

ALTER TABLE PersonHealth ADD
  CONSTRAINT FK_PersonHealth_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID) GO
ALTER TABLE PersonHealth ADD
  CONSTRAINT FK_PersonHealth_HealthStateCode FOREIGN KEY (HealthStateCodeID) REFERENCES HealthStateCode (HealthStateCodeID) GO
ALTER TABLE PersonHealth ADD
  CONSTRAINT FK_PersonHealth_Next FOREIGN KEY (NextID) REFERENCES PersonHealth (PersonHealthID) GO

ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID) GO
ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_EmploymentStatusCode FOREIGN KEY (EmploymentStatusCodeID) REFERENCES EmploymentStatusCode (EmploymentStatusCodeID) GO
ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_IndustryCode FOREIGN KEY (IndustryCodeID) REFERENCES IndustryCode (IndustryCodeID) GO
ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_OccupationCode FOREIGN KEY (OccupationCodeID) REFERENCES OccupationCode (OccupationCodeID) GO
ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_Next FOREIGN KEY (NextID) REFERENCES PersonOccupation (PersonOccupationID) GO

ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID) GO
ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_TrustStatusCode FOREIGN KEY (TrustStatusCodeID) REFERENCES StatusCode (StatusCodeID) GO
ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_DIYStatusCode FOREIGN KEY (DIYStatusCodeID) REFERENCES StatusCode (StatusCodeID) GO
ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_CompanyStatusCode FOREIGN KEY (CompanyStatusCodeID) REFERENCES StatusCode (StatusCodeID) GO
ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_Next FOREIGN KEY (NextID) REFERENCES PersonTrustDIYStatus (PersonTrustDIYStatusID) GO


ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_Object FOREIGN KEY (FinancialID) REFERENCES Object (ObjectID) GO
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID) GO
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_Institution FOREIGN KEY (InstitutionID) REFERENCES Institution (InstitutionID) GO
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_OwnerCode FOREIGN KEY (OwnerCodeID) REFERENCES OwnerCode (OwnerCodeID) GO
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID) GO
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_Next FOREIGN KEY (NextID) REFERENCES Financial (FinancialID) GO


ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Financial FOREIGN KEY (AssetID) REFERENCES Financial (FinancialID) GO

ALTER TABLE AssetCash ADD
  CONSTRAINT FK_AssetCash_Asset FOREIGN KEY (AssetCashID) REFERENCES Asset (AssetID) GO

ALTER TABLE AssetInvestment ADD
  CONSTRAINT FK_AssetInvestment_Asset FOREIGN KEY (AssetInvestmentID) REFERENCES Asset (AssetID) GO
ALTER TABLE AssetInvestment ADD
  CONSTRAINT FK_AssetInvestment_InvestmentType FOREIGN KEY (InvestmentTypeID) REFERENCES InvestmentType (InvestmentTypeID) GO

ALTER TABLE AssetPersonal ADD
  CONSTRAINT FK_AssetPersonal_Asset FOREIGN KEY (AssetPersonalID) REFERENCES Asset (AssetID) GO

ALTER TABLE AssetSuperannuation ADD
  CONSTRAINT FK_AssetSuperannuation_Asset FOREIGN KEY (AssetSuperannuationID) REFERENCES Asset (AssetID) GO
ALTER TABLE AssetSuperannuation ADD
  CONSTRAINT FK_AssetSuperannuation_FundType FOREIGN KEY (FundTypeID) REFERENCES FundType (FundTypeID) GO

ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_Financial FOREIGN KEY (RegularID) REFERENCES Financial (FinancialID) GO
ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_FrequencyCode FOREIGN KEY (FrequencyCodeID) REFERENCES FrequencyCode (FrequencyCodeID) GO

ALTER TABLE RegularIncome ADD
  CONSTRAINT FK_RegularIncome_Regular FOREIGN KEY (RegularIncomeID) REFERENCES Regular (RegularID) GO

ALTER TABLE RegularExpense ADD
  CONSTRAINT FK_RegularExpense_Regular FOREIGN KEY (RegularExpenseID) REFERENCES Regular (RegularID) GO

ALTER TABLE Liability ADD
  CONSTRAINT FK_Liability_RegularExpense FOREIGN KEY (LiabilityID) REFERENCES RegularExpense (RegularExpenseID) GO

ALTER TABLE Insurance ADD
  CONSTRAINT FK_Insurance_RegularExpense FOREIGN KEY (InsuranceID) REFERENCES RegularExpense (RegularExpenseID) GO
ALTER TABLE Insurance ADD
  CONSTRAINT FK_Insurance_WaitingPeriodCode FOREIGN KEY (WaitingPeriodCodeID) REFERENCES PeriodCode (PeriodCodeID) GO
ALTER TABLE Insurance ADD
  CONSTRAINT FK_Insurance_BenefitPeriodCode FOREIGN KEY (BenefitPeriodCodeID) REFERENCES PeriodCode (PeriodCodeID) GO

ALTER TABLE FinancialType ADD
  CONSTRAINT FK_FinancialType_ObjectType FOREIGN KEY (ObjectTypeID) REFERENCES ObjectType (ObjectTypeID) GO
ALTER TABLE FinancialCode ADD
  CONSTRAINT FK_FinancialCode_FinancialType FOREIGN KEY (FinancialTypeID) REFERENCES FinancialType (FinancialTypeID) GO



ALTER TABLE Survey ADD
  CONSTRAINT FK_Survey_Object FOREIGN KEY (SurveyID) REFERENCES Object (ObjectID) GO

ALTER TABLE Question ADD
  CONSTRAINT FK_Question_QuestionType FOREIGN KEY (QuestionTypeID) REFERENCES QuestionType (QuestionTypeID) GO
ALTER TABLE Question ADD
  CONSTRAINT FK_Question_SurveyID FOREIGN KEY (SurveyID) REFERENCES Survey (SurveyID) GO

ALTER TABLE QuestionAnswer ADD
  CONSTRAINT FK_QuestionAnswer_Question FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID) GO

ALTER TABLE PersonSurvey ADD
  CONSTRAINT FK_PersonSurvey_Link FOREIGN KEY (PersonSurveyID) REFERENCES Link (LinkID) GO

ALTER TABLE PersonSurveyAnswer ADD
  CONSTRAINT FK_PersonSurveyAnswer_PersonSurvey FOREIGN KEY (PersonSurveyID) REFERENCES PersonSurvey (PersonSurveyID) GO
ALTER TABLE PersonSurveyAnswer ADD
  CONSTRAINT FK_PersonSurveyAnswer_Question FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID) GO
ALTER TABLE PersonSurveyAnswer ADD
  CONSTRAINT FK_PersonSurveyAnswer_QuestionAnswer FOREIGN KEY (QuestionAnswerID) REFERENCES QuestionAnswer (QuestionAnswerID) GO

ALTER TABLE PersonSurveyAnswerText ADD
  CONSTRAINT FK_PersonSurveyAnswerText_PersonSurveyAnswer FOREIGN KEY (PersonSurveyAnswerID) REFERENCES PersonSurveyAnswer (PersonSurveyAnswerID) GO



ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Person FOREIGN KEY (BeneficiaryID) REFERENCES Person (PersonID) GO
ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Liability FOREIGN KEY (LiabilityID) REFERENCES Liability (LiabilityID) GO
ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_Asset FOREIGN KEY (AssetID) REFERENCES Asset (AssetID) GO

ALTER TABLE Params ADD
  CONSTRAINT FK_Params_ParamType FOREIGN KEY (ParamTypeID) REFERENCES ParamType (ParamTypeID) GO

ALTER TABLE LifeExpectancy ADD
  CONSTRAINT FK_LifeExpectancy_SexCode FOREIGN KEY (SexCodeID) REFERENCES SexCode (SexCodeID) GO
ALTER TABLE LifeExpectancy ADD
  CONSTRAINT FK_LifeExpectancy_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID) GO

ALTER TABLE Comment ADD
  CONSTRAINT FK_Comment_Object FOREIGN KEY (CommentID) REFERENCES Object (ObjectID) GO
ALTER TABLE FinancialGoal ADD
  CONSTRAINT FK_FinancialGoal_Object FOREIGN KEY (FinancialGoalID) REFERENCES Object (ObjectID) GO

ALTER TABLE PersonEstate ADD
  CONSTRAINT FK_PersonEstate_Object FOREIGN KEY (PersonEstateID) REFERENCES Object (ObjectID) GO

ALTER TABLE Model ADD
  CONSTRAINT FK_Model_Object FOREIGN KEY (ModelID) REFERENCES Object (ObjectID) GO
ALTER TABLE Model ADD
  CONSTRAINT FK_Model_ModelType FOREIGN KEY (ModelTypeID) REFERENCES ModelType (ModelTypeID) GO

ALTER TABLE FinancialPool ADD
  CONSTRAINT FK_FinancialPool_Financial FOREIGN KEY (FinancialPoolID) REFERENCES Financial (FinancialID) GO

ALTER TABLE StrategyModel ADD
  CONSTRAINT FK_StrategyModel_Model FOREIGN KEY (StrategyModelID) REFERENCES Model (ModelID) GO

ALTER TABLE StrategyFinancial ADD
  CONSTRAINT FK_StrategyFinancial_FinancialPool FOREIGN KEY (StrategyFinancialID) REFERENCES FinancialPool (FinancialPoolID) GO
ALTER TABLE StrategyFinancial ADD
  CONSTRAINT FK_StrategyFinancial_StrategyModel FOREIGN KEY (StrategyModelID) REFERENCES StrategyModel (StrategyModelID) GO


----------------------------------------------------------------------------------------
--							INDEXES
----------------------------------------------------------------------------------------
CREATE INDEX IDX_Object_ObjectTypeID ON Object (ObjectTypeID) GO

CREATE INDEX IDX_Link_ObjectID1 ON Link (ObjectID1) GO
CREATE INDEX IDX_Link_ObjectID2 ON Link (ObjectID2) GO
CREATE INDEX IDX_Link_LinkObjectTypeID ON Link (LinkObjectTypeID) GO

CREATE INDEX IDX_PersonHealth_PersonID ON PersonHealth (PersonID) GO
CREATE INDEX IDX_PersonHealth_NextID ON PersonHealth (NextID) GO

CREATE INDEX IDX_PersonOccupation_PersonID ON PersonOccupation (PersonID) GO
CREATE INDEX IDX_PersonOccupation_NextID ON PersonOccupation (NextID) GO

CREATE INDEX IDX_PersonTrustDIYStatus_PersonID ON PersonTrustDIYStatus (PersonID) GO
CREATE INDEX IDX_PersonTrustDIYStatus_NextID ON PersonTrustDIYStatus (NextID) GO



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.00.03', 'FPS.00.02')
 GO
