ALTER TABLE Object ADD CONSTRAINT PK_ObjectID PRIMARY KEY (ObjectID)
ALTER TABLE ObjectType ADD CONSTRAINT PK_ObjectTypeID PRIMARY KEY (ObjectTypeID)
ALTER TABLE Link ADD CONSTRAINT PK_LinkID PRIMARY KEY (LinkID)
ALTER TABLE LinkObjectType ADD CONSTRAINT PK_LinkObjectTypeID PRIMARY KEY (LinkObjectTypeID)


ALTER TABLE SexCode ADD CONSTRAINT PK_SexCodeID PRIMARY KEY (SexCodeID)
ALTER TABLE MaritalCode ADD CONSTRAINT PK_MaritalCodeID PRIMARY KEY (MaritalCodeID)
ALTER TABLE TitleCode ADD CONSTRAINT PK_TitleCodeID PRIMARY KEY (TitleCodeID)
ALTER TABLE RelationshipCode ADD CONSTRAINT PK_RelationshipCodeID PRIMARY KEY (RelationshipCodeID)
ALTER TABLE RelationshipFinanceCode ADD CONSTRAINT PK_RelationshipFinanceCodeID PRIMARY KEY (RelationshipFinanceCodeID)
ALTER TABLE AddressCode ADD CONSTRAINT PK_AddressCodeID PRIMARY KEY (AddressCodeID)
ALTER TABLE SuburbPostCode ADD CONSTRAINT PK_SuburbPostCode PRIMARY KEY (PostCode, Suburb)
ALTER TABLE StateCode ADD CONSTRAINT PK_StateCodeID PRIMARY KEY (StateCodeID)
ALTER TABLE CountryCode ADD CONSTRAINT PK_CountryCodeID PRIMARY KEY (CountryCodeID)
ALTER TABLE ContactMediaCode ADD CONSTRAINT PK_ContactMediaCodeID PRIMARY KEY (ContactMediaCodeID)
ALTER TABLE LanguageCode ADD CONSTRAINT PK_LanguageCodeID PRIMARY KEY (LanguageCodeID)
ALTER TABLE IndustryCode ADD CONSTRAINT PK_IndustryCodeID PRIMARY KEY (IndustryCodeID)
ALTER TABLE OccupationCode ADD CONSTRAINT PK_OccupationCodeID PRIMARY KEY (OccupationCodeID)
ALTER TABLE ContactOccupationCode ADD CONSTRAINT PK_ContactOccupationCodeID PRIMARY KEY (ContactOccupationCodeID)
ALTER TABLE BusinessStructureCode ADD CONSTRAINT PK_BusinessStructureCodeID PRIMARY KEY (BusinessStructureCodeID)
ALTER TABLE ReferalSourceCode ADD CONSTRAINT PK_ReferalSourceCodeID PRIMARY KEY (ReferalSourceCodeID)
ALTER TABLE ResidenceStatusCode ADD CONSTRAINT PK_ResidenceStatusCodeID PRIMARY KEY (ResidenceStatusCodeID)
ALTER TABLE HealthStateCode ADD CONSTRAINT PK_HealthStateCodeID PRIMARY KEY (HealthStateCodeID)
ALTER TABLE EmploymentStatusCode ADD CONSTRAINT PK_EmploymentStatusCodeID PRIMARY KEY (EmploymentStatusCodeID)
ALTER TABLE AdviserTypeCode ADD CONSTRAINT PK_AdviserTypeCodeID PRIMARY KEY (AdviserTypeCodeID)
ALTER TABLE StatusCode ADD CONSTRAINT PK_StatusCodeID PRIMARY KEY (StatusCodeID)
ALTER TABLE WillStatusCode ADD CONSTRAINT PK_WillStatusCodeID PRIMARY KEY (WillStatusCodeID)
ALTER TABLE WillChangeCode ADD CONSTRAINT PK_WillChangeCodeID PRIMARY KEY (WillChangeCodeID)
ALTER TABLE SupporterCode ADD CONSTRAINT PK_SupporterCodeID PRIMARY KEY (SupporterCodeID)
ALTER TABLE AllowanceCode ADD CONSTRAINT PK_AllowanceCodeID PRIMARY KEY (AllowanceCodeID)


ALTER TABLE Person ADD CONSTRAINT PK_PersonID PRIMARY KEY (PersonID)
ALTER TABLE PersonHealth ADD CONSTRAINT PK_PersonHealthID PRIMARY KEY (PersonHealthID)
ALTER TABLE PersonOccupation ADD CONSTRAINT PK_PersonOccupationID PRIMARY KEY (PersonOccupationID)
ALTER TABLE PersonTrustDIYStatus ADD CONSTRAINT PK_PersonTrustDIYStatusID PRIMARY KEY (PersonTrustDIYStatusID)
ALTER TABLE ClientPerson ADD CONSTRAINT PK_ClientPersonID PRIMARY KEY (ClientPersonID)
ALTER TABLE UserPerson ADD CONSTRAINT PK_UserPersonID PRIMARY KEY (UserPersonID)
ALTER TABLE Business ADD CONSTRAINT PK_BusinessID PRIMARY KEY (BusinessID)
ALTER TABLE Address ADD CONSTRAINT PK_AddressID PRIMARY KEY (AddressID)
ALTER TABLE ContactMedia ADD CONSTRAINT PK_ContactMediaID PRIMARY KEY (ContactMediaID)


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
ALTER TABLE PersonSurveyAnswerText ADD CONSTRAINT PK_PersonSurveyAnswerTextID PRIMARY KEY (PersonSurveyAnswerID)

ALTER TABLE ParamType ADD CONSTRAINT PK_ParamType PRIMARY KEY (ParamTypeID)
ALTER TABLE Params ADD CONSTRAINT PK_Params PRIMARY KEY (ParamName, ParamTypeID)
ALTER TABLE LifeExpectancy ADD CONSTRAINT PK_LifeExpectancy PRIMARY KEY (Age, SexCodeID)
ALTER TABLE HealthConditionCode ADD CONSTRAINT PK_HealthConditionCode PRIMARY KEY (HealthConditionCodeID)
ALTER TABLE Comment ADD CONSTRAINT PK_Comment PRIMARY KEY (CommentID)
ALTER TABLE FinancialGoal ADD CONSTRAINT PK_FinancialGoal PRIMARY KEY (FinancialGoalID)

ALTER TABLE PersonEstate ADD CONSTRAINT PK_PersonEstate PRIMARY KEY (PersonEstateID)

ALTER TABLE Model ADD CONSTRAINT PK_ModelID PRIMARY KEY (ModelID);
ALTER TABLE ModelType ADD CONSTRAINT PK_ModelTypeID PRIMARY KEY (ModelTypeID);

ALTER TABLE FinancialPool ADD CONSTRAINT PK_FinancialPoolID PRIMARY KEY (FinancialPoolID);

ALTER TABLE StrategyModel ADD CONSTRAINT PK_StrategyModel PRIMARY KEY (StrategyModelID);
ALTER TABLE StrategyFinancial ADD CONSTRAINT PK_StrategyFinancial_StrategyModel PRIMARY KEY (StrategyFinancialID, StrategyModelID);




ALTER TABLE Object ADD
  CONSTRAINT FK_Object_ObjectType FOREIGN KEY (ObjectTypeID) REFERENCES ObjectType (ObjectTypeID)

ALTER TABLE Link ADD
  CONSTRAINT FK_Link_Link FOREIGN KEY (LinkID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Link_Object1 FOREIGN KEY (ObjectID1) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Link_Object2 FOREIGN KEY (ObjectID2) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Link_LinkObjectType FOREIGN KEY (LinkObjectTypeID) REFERENCES LinkObjectType (LinkObjectTypeID)

ALTER TABLE LinkObjectType ADD
  CONSTRAINT FK_LinkObjectType_LinkObjectType FOREIGN KEY (LinkObjectTypeID) REFERENCES ObjectType (ObjectTypeID),
  CONSTRAINT FK_LinkObjectType_ObjectType1 FOREIGN KEY (ObjectTypeID1) REFERENCES ObjectType (ObjectTypeID),
  CONSTRAINT FK_LinkObjectType_ObjectType2 FOREIGN KEY (ObjectTypeID2) REFERENCES ObjectType (ObjectTypeID)

 

 
ALTER TABLE ContactOccupationCode ADD
  CONSTRAINT FK_ContactOccupationCode_OccupationCode FOREIGN KEY (ContactOccupationCodeID) REFERENCES OccupationCode (OccupationCodeID)
 
  


ALTER TABLE Person ADD
  CONSTRAINT FK_Person_Object FOREIGN KEY (PersonID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Person_SexCode FOREIGN KEY (SexCodeID) REFERENCES SexCode (SexCodeID),
  CONSTRAINT FK_Person_TitleCode FOREIGN KEY (TitleCodeID) REFERENCES TitleCode (TitleCodeID),
  CONSTRAINT FK_Person_DOBCountry FOREIGN KEY (DOBCountryID) REFERENCES CountryCode (CountryCodeID),
  CONSTRAINT FK_Person_MaritalCode FOREIGN KEY (MaritalCodeID) REFERENCES MaritalCode (MaritalCodeID),
  CONSTRAINT FK_Person_PreferredLanguage FOREIGN KEY (PreferredLanguageID) REFERENCES LanguageCode (LanguageCodeID),
  CONSTRAINT FK_Person_ReferalSourceCode FOREIGN KEY (ReferalSourceCodeID) REFERENCES ReferalSourceCode (ReferalSourceCodeID),
  CONSTRAINT FK_Person_ResidenceStatusCode FOREIGN KEY (ResidenceStatusCodeID) REFERENCES ResidenceStatusCode (ResidenceStatusCodeID),
  CONSTRAINT FK_Person_ResidenceCountryCode FOREIGN KEY (ResidenceCountryCodeID) REFERENCES CountryCode (CountryCodeID)

ALTER TABLE ClientPerson ADD
  CONSTRAINT FK_ClientPerson_Person FOREIGN KEY (ClientPersonID) REFERENCES Person (PersonID)

ALTER TABLE UserPerson ADD
  CONSTRAINT FK_UserPerson_Person FOREIGN KEY (UserPersonID) REFERENCES Person (PersonID),
  CONSTRAINT FK_UserPerson_AdviserTypeCode FOREIGN KEY (AdviserTypeCodeID) REFERENCES AdviserTypeCode (AdviserTypeCodeID)

ALTER TABLE RelationshipCode ADD
  CONSTRAINT FK_RelationshipCode_Object FOREIGN KEY (RelationshipCodeID) REFERENCES Object (ObjectID)

ALTER TABLE RelationshipFinanceCode ADD
  CONSTRAINT FK_RelationshipFinanceCode_Object FOREIGN KEY (RelationshipFinanceCodeID) REFERENCES Object (ObjectID)

ALTER TABLE SuburbPostCode ADD
  CONSTRAINT FK_SuburbPostCode_StateCode FOREIGN KEY (StateCodeID) REFERENCES StateCode (StateCodeID),
  CONSTRAINT FK_SuburbPostCode_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID)



ALTER TABLE Business ADD
  CONSTRAINT FK_Business_Object FOREIGN KEY (BusinessID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Business_IndustryCode FOREIGN KEY (IndustryCodeID) REFERENCES IndustryCode (IndustryCodeID),
  CONSTRAINT FK_Business_BusinessStructureCode FOREIGN KEY (BusinessStructureCodeID) REFERENCES BusinessStructureCode (BusinessStructureCodeID),
  CONSTRAINT FK_Business_Business FOREIGN KEY (ParentBusinessID) REFERENCES Business (BusinessID)

ALTER TABLE Address ADD 
  CONSTRAINT FK_Address_Object FOREIGN KEY (AddressID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Address_AddressCode FOREIGN KEY (AddressCodeID) REFERENCES AddressCode (AddressCodeID),
  CONSTRAINT FK_Address_SuburbPostCode FOREIGN KEY (PostCode,Suburb) REFERENCES SuburbPostCode (PostCode,Suburb),
  CONSTRAINT FK_Address_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID),
  CONSTRAINT FK_Address_StateCode FOREIGN KEY (StateCodeID) REFERENCES StateCode (StateCodeID)


ALTER TABLE ContactMedia ADD
  CONSTRAINT FK_ContactMedia_Object FOREIGN KEY (ContactMediaID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_ContactMedia_ContactMediaCode FOREIGN KEY (ContactMediaCodeID) REFERENCES ContactMediaCode (ContactMediaCodeID)

ALTER TABLE PersonHealth ADD
  CONSTRAINT FK_PersonHealth_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID),
  CONSTRAINT FK_PersonHealth_HealthStateCode FOREIGN KEY (HealthStateCodeID) REFERENCES HealthStateCode (HealthStateCodeID),
  CONSTRAINT FK_PersonHealth_Next FOREIGN KEY (NextID) REFERENCES PersonHealth (PersonHealthID)

ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID),
  CONSTRAINT FK_PersonOccupation_EmploymentStatusCode FOREIGN KEY (EmploymentStatusCodeID) REFERENCES EmploymentStatusCode (EmploymentStatusCodeID),
  CONSTRAINT FK_PersonOccupation_IndustryCode FOREIGN KEY (IndustryCodeID) REFERENCES IndustryCode (IndustryCodeID),
  CONSTRAINT FK_PersonOccupation_OccupationCode FOREIGN KEY (OccupationCodeID) REFERENCES OccupationCode (OccupationCodeID),
  CONSTRAINT FK_PersonOccupation_Next FOREIGN KEY (NextID) REFERENCES PersonOccupation (PersonOccupationID)

ALTER TABLE PersonTrustDIYStatus ADD
  CONSTRAINT FK_PersonTrustDIYStatus_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID),
  CONSTRAINT FK_PersonTrustDIYStatus_TrustStatusCode FOREIGN KEY (TrustStatusCodeID) REFERENCES StatusCode (StatusCodeID),
  CONSTRAINT FK_PersonTrustDIYStatus_DIYStatusCode FOREIGN KEY (DIYStatusCodeID) REFERENCES StatusCode (StatusCodeID),
  CONSTRAINT FK_PersonTrustDIYStatus_CompanyStatusCode FOREIGN KEY (CompanyStatusCodeID) REFERENCES StatusCode (StatusCodeID),
  CONSTRAINT FK_PersonTrustDIYStatus_Next FOREIGN KEY (NextID) REFERENCES PersonTrustDIYStatus (PersonTrustDIYStatusID)


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
  CONSTRAINT FK_PersonSurvey_Link FOREIGN KEY (PersonSurveyID) REFERENCES Link (LinkID)

ALTER TABLE PersonSurveyAnswer ADD
  CONSTRAINT FK_PersonSurveyAnswer_PersonSurvey FOREIGN KEY (PersonSurveyID) REFERENCES PersonSurvey (PersonSurveyID),
  CONSTRAINT FK_PersonSurveyAnswer_Question FOREIGN KEY (QuestionID) REFERENCES Question (QuestionID),
  CONSTRAINT FK_PersonSurveyAnswer_QuestionAnswer FOREIGN KEY (QuestionAnswerID) REFERENCES QuestionAnswer (QuestionAnswerID)

ALTER TABLE PersonSurveyAnswerText ADD
  CONSTRAINT FK_PersonSurveyAnswerText_PersonSurveyAnswer FOREIGN KEY (PersonSurveyAnswerID) REFERENCES PersonSurveyAnswer (PersonSurveyAnswerID)



ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Person FOREIGN KEY (BeneficiaryID) REFERENCES Person (PersonID)
ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Liability FOREIGN KEY (LiabilityID) REFERENCES Liability (LiabilityID)
ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_Asset FOREIGN KEY (AssetID) REFERENCES Asset (AssetID)

ALTER TABLE Params ADD
  CONSTRAINT FK_Params_ParamType FOREIGN KEY (ParamTypeID) REFERENCES ParamType (ParamTypeID)
ALTER TABLE LifeExpectancy ADD
  CONSTRAINT FK_LifeExpectancy_SexCode FOREIGN KEY (SexCodeID) REFERENCES SexCode (SexCodeID),
  CONSTRAINT FK_LifeExpectancy_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID)
ALTER TABLE Comment ADD
  CONSTRAINT FK_Comment_Object FOREIGN KEY (CommentID) REFERENCES Object (ObjectID)
ALTER TABLE FinancialGoal ADD
  CONSTRAINT FK_FinancialGoal_Object FOREIGN KEY (FinancialGoalID) REFERENCES Object (ObjectID)

ALTER TABLE PersonEstate ADD
  CONSTRAINT FK_PersonEstate_Object FOREIGN KEY (PersonEstateID) REFERENCES Object (ObjectID)


ALTER TABLE Model ADD
  CONSTRAINT FK_Model_Object FOREIGN KEY (ModelID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Model_ModelType FOREIGN KEY (ModelTypeID) REFERENCES ModelType (ModelTypeID);


ALTER TABLE FinancialPool ADD
  CONSTRAINT FK_FinancialPool_Financial FOREIGN KEY (FinancialPoolID) REFERENCES Financial (FinancialID);

ALTER TABLE StrategyModel ADD
  CONSTRAINT FK_StrategyModel_Model FOREIGN KEY (StrategyModelID) REFERENCES Model (ModelID);
ALTER TABLE StrategyFinancial ADD
  CONSTRAINT FK_StrategyFinancial_FinancialPool FOREIGN KEY (StrategyFinancialID) REFERENCES FinancialPool (FinancialPoolID),
  CONSTRAINT FK_StrategyFinancial_StrategyModel FOREIGN KEY (StrategyModelID) REFERENCES StrategyModel (StrategyModelID);



----------------------------------------------------------------------------------------
--							INDEXES
----------------------------------------------------------------------------------------
CREATE UNIQUE INDEX IDX_FinancialMapSV2_1 ON FinancialMapSV2 ( FinancialTypeID, FinancialCodeID )




CREATE INDEX IDX_Object_ObjectTypeID ON Object (ObjectTypeID);

CREATE INDEX IDX_Link_ObjectID1 ON Link (ObjectID1);
CREATE INDEX IDX_Link_ObjectID2 ON Link (ObjectID2);
CREATE INDEX IDX_Link_LinkObjectTypeID ON Link (LinkObjectTypeID);

CREATE INDEX IDX_PersonHealth_PersonID ON PersonHealth (PersonID);
CREATE INDEX IDX_PersonHealth_NextID ON PersonHealth (NextID);

CREATE INDEX IDX_PersonOccupation_PersonID ON PersonOccupation (PersonID);
CREATE INDEX IDX_PersonOccupation_NextID ON PersonOccupation (NextID);

CREATE INDEX IDX_PersonTrustDIYStatus_PersonID ON PersonTrustDIYStatus (PersonID);
CREATE INDEX IDX_PersonTrustDIYStatus_NextID ON PersonTrustDIYStatus (NextID);
