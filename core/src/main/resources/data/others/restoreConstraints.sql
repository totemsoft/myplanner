ALTER TABLE Object ADD
  CONSTRAINT FK_Object_ObjectType FOREIGN KEY (ObjectTypeID) REFERENCES ObjectType (ObjectTypeID);



ALTER TABLE FinancialType ADD
  CONSTRAINT FK_FinancialType_ObjectType FOREIGN KEY (ObjectTypeID) REFERENCES ObjectType (ObjectTypeID);

SELECT * FROM FinancialCode c, FinancialType t WHERE c.FinancialTypeID *= t.FinancialTypeID ORDER BY t.FinancialTypeID;
DELETE FROM FinancialCode WHERE FinancialTypeID IN ( 2000, 4000 );
ALTER TABLE FinancialCode ADD
  CONSTRAINT FK_FinancialCode_FinancialType FOREIGN KEY (FinancialTypeID) REFERENCES FinancialType (FinancialTypeID);



--ALTER TABLE FinancialMapSV2 ADD
--  CONSTRAINT FK_FinancialMapSV2_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID);




--SELECT * FROM PersonSurvey, Object WHERE PersonSurveyID *= ObjectID;
DELETE FROM PersonSurveyAnswer WHERE PersonSurveyID IN ( SELECT PersonSurveyID FROM PersonSurvey, Object WHERE PersonSurveyID *= ObjectID );
DELETE FROM PersonSurvey WHERE PersonSurveyID IN ( SELECT PersonSurveyID FROM PersonSurvey, Object WHERE PersonSurveyID *= ObjectID );

--SELECT * FROM Link, Object WHERE LinkID *= ObjectID;
DELETE FROM Link WHERE LinkID IN ( SELECT LinkID FROM Link, Object WHERE LinkID *= ObjectID );

ALTER TABLE Link ADD
  CONSTRAINT FK_Link_Link FOREIGN KEY (LinkID) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Link_Object1 FOREIGN KEY (ObjectID1) REFERENCES Object (ObjectID),
  CONSTRAINT FK_Link_Object2 FOREIGN KEY (ObjectID2) REFERENCES Object (ObjectID);


DELETE FROM StrategyFinancial;
--SELECT * FROM Financial, Object WHERE FinancialID *= ObjectID ORDER BY ObjectID;
DELETE FROM Financial WHERE FinancialID IN ( SELECT FinancialID FROM Financial, Object WHERE FinancialID *= ObjectID );

ALTER TABLE Institution ADD CONSTRAINT PK_InstitutionID PRIMARY KEY (InstitutionID);
--SELECT * FROM Financial f, Institution i 
--WHERE f.InstitutionID IS NOT NULL AND f.InstitutionID *= i.InstitutionID
--ORDER BY i.InstitutionID
UPDATE Financial SET InstitutionID = NULL WHERE InstitutionID = 295; 
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_Institution FOREIGN KEY (InstitutionID) REFERENCES Institution (InstitutionID);

ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_OwnerCode FOREIGN KEY (OwnerCodeID) REFERENCES OwnerCode (OwnerCodeID),
  CONSTRAINT FK_Financial_CountryCode FOREIGN KEY (CountryCodeID) REFERENCES CountryCode (CountryCodeID),
  CONSTRAINT FK_Financial_Next FOREIGN KEY (NextID) REFERENCES Financial (FinancialID);


--SELECT * FROM Financial f, FinancialCode c 
--WHERE f.FinancialCodeID IS NOT NULL AND f.FinancialTypeID IS NOT NULL AND 
--f.FinancialCodeID *= c.FinancialCodeID AND f.FinancialTypeID *= c.FinancialTypeID
--ORDER BY c.FinancialCodeID,c.FinancialTypeID

SELECT * FROM FinancialCode WHERE FinancialTypeID IN 
( 2001, 2004, 2007, 2009, 2030, 2032, 2036, 2043, 2044, 2045, 2079, 4015, 4017, 4018, 4042)
ORDER BY FinancialTypeID;

--UPDATE Financial SET FinancialCodeID = NULL, FinancialTypeID = NULL WHERE FinancialTypeID IN 
--( 2001, 2004, 2007, 2009, 2030, 2032, 2036, 2043, 2044, 2045, 2079, 4015, 4017, 4018, 4042); 
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_FinancialCodeType FOREIGN KEY (FinancialCodeID,FinancialTypeID) REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID);

ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_Object FOREIGN KEY (FinancialID) REFERENCES Object (ObjectID);

DELETE FROM Asset WHERE AssetID IN ( SELECT AssetID FROM Asset, Financial WHERE AssetID *= FinancialID );
ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_Financial FOREIGN KEY (AssetID) REFERENCES Financial (FinancialID);

DELETE FROM AssetCash WHERE AssetCashID IN ( SELECT AssetCashID FROM AssetCash, Asset WHERE AssetCashID *= AssetID );
ALTER TABLE AssetCash ADD
  CONSTRAINT FK_AssetCash_Asset FOREIGN KEY (AssetCashID) REFERENCES Asset (AssetID);

DELETE FROM AssetInvestment WHERE AssetInvestmentID IN ( SELECT AssetInvestmentID FROM AssetInvestment, Asset WHERE AssetInvestmentID *= AssetID );
ALTER TABLE AssetInvestment ADD
  CONSTRAINT FK_AssetInvestment_Asset FOREIGN KEY (AssetInvestmentID) REFERENCES Asset (AssetID),
  CONSTRAINT FK_AssetInvestment_InvestmentType FOREIGN KEY (InvestmentTypeID) REFERENCES InvestmentType (InvestmentTypeID);

DELETE FROM AssetPersonal WHERE AssetPersonalID IN ( SELECT AssetPersonalID FROM AssetPersonal, Asset WHERE AssetPersonalID *= AssetID );
ALTER TABLE AssetPersonal ADD
  CONSTRAINT FK_AssetPersonal_Asset FOREIGN KEY (AssetPersonalID) REFERENCES Asset (AssetID);

DELETE FROM AssetSuperannuation WHERE AssetSuperannuationID IN ( SELECT AssetSuperannuationID FROM AssetSuperannuation, Asset WHERE AssetSuperannuationID *= AssetID );
ALTER TABLE AssetSuperannuation ADD
  CONSTRAINT FK_AssetSuperannuation_Asset FOREIGN KEY (AssetSuperannuationID) REFERENCES Asset (AssetID),
  CONSTRAINT FK_AssetSuperannuation_FundType FOREIGN KEY (FundTypeID) REFERENCES FundType (FundTypeID);


DELETE FROM Regular WHERE RegularID IN ( SELECT RegularID FROM Regular, Financial WHERE RegularID *= FinancialID );
ALTER TABLE Regular ADD
  CONSTRAINT FK_Regular_Financial FOREIGN KEY (RegularID) REFERENCES Financial (FinancialID),
  CONSTRAINT FK_Regular_FrequencyCode FOREIGN KEY (FrequencyCodeID) REFERENCES FrequencyCode (FrequencyCodeID);

DELETE FROM RegularIncome WHERE RegularIncomeID IN ( SELECT RegularIncomeID FROM RegularIncome, Regular WHERE RegularIncomeID *= RegularID );
ALTER TABLE RegularIncome ADD
  CONSTRAINT FK_RegularIncome_Regular FOREIGN KEY (RegularIncomeID) REFERENCES Regular (RegularID);

DELETE FROM RegularExpense WHERE RegularExpenseID IN ( SELECT RegularExpenseID FROM RegularExpense, Regular WHERE RegularExpenseID *= RegularID );
ALTER TABLE RegularExpense ADD
  CONSTRAINT FK_RegularExpense_Regular FOREIGN KEY (RegularExpenseID) REFERENCES Regular (RegularID);

DELETE FROM Liability WHERE LiabilityID IN ( SELECT LiabilityID FROM Liability, RegularExpense WHERE LiabilityID *= RegularExpenseID );
ALTER TABLE Liability ADD
  CONSTRAINT FK_Liability_RegularExpense FOREIGN KEY (LiabilityID) REFERENCES RegularExpense (RegularExpenseID);



--SELECT * FROM FinancialGoal, Object WHERE FinancialGoalID *= ObjectID ORDER BY ObjectID;
DELETE FROM FinancialGoal WHERE FinancialGoalID IN ( SELECT FinancialGoalID FROM FinancialGoal, Object WHERE FinancialGoalID *= ObjectID );
ALTER TABLE FinancialGoal ADD
  CONSTRAINT FK_FinancialGoal_Object FOREIGN KEY (FinancialGoalID) REFERENCES Object (ObjectID);
