-- Investment Types
DELETE FROM InvestmentType 
GO

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (1, 'Gold')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (2, 'Other Metals')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (3, 'Diversified Resources')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (4, 'Energy')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (5, 'Infrastructure and Utilities')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (6, 'Developers and Contractors')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (7, 'Building Materials')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (8, 'Alcohol and Tobacco')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (9, 'Food and Household')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (10, 'Chemicals')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (11, 'Engineering')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (12, 'Paper and Packaging')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (13, 'Retail')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (14, 'Transport')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (15, 'Media')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (16, 'Banks and Finance')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (17, 'Insurance')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (18, 'Investment and Financial Services')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (19, 'Investment and Financial Services')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (20, 'Property Trusts')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (21, 'Healthcare and Biotechnology')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (22, 'Miscellaneous Industrials')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (23, 'Diversified Industrials')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (24, 'Tourism and Leisure')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (25, 'Debt Issuer')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (26, 'Capital Safe')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (27, 'Capital Stable')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (28, 'Balanced')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (29, 'Growth')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (30, 'Australian Shares')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (31, 'Australian Smaller Company Shares')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (32, 'International Shares')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (33, 'Technology Shares')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (34, 'Property Securities')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (35, 'Mortgage Trusts')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (36, 'Geared Funds')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (37, 'Managed Share Portfolios')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (38, 'Other')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (39, 'Personal Share Admin Service - ASX Shares Only')

INSERT InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (40, 'Cash')



-- Institutions
DELETE FROM Institution 
GO

INSERT Institution (InstitutionID, InstitutionName)
VALUES (1, 'portofolio services')



-- Fund Types
DELETE FROM FundType 
GO

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (1, 'Employer Superannuation - Accumulation')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (2, 'Employer Superannuation - Defined Benefit')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (3, 'Personal Superannuation')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (4, 'Allocated Pension')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (5, 'Allocated Pension')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (6, 'Complying Superannuation Pension')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (7, 'Complying Annuity')

INSERT FundType (FundTypeID, FundTypeDesc)
VALUES (8, 'Non-Complying Annuity')



-- Frequency Details
DELETE FROM FrequencyCode
GO

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (1, 'Only Once')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (2, 'Daily')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (3, 'Weekly')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (4, 'Fortnightly')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (5, 'Twice Monthly')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (6, 'Monthly')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (7, 'Every Other Month')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (8, 'Every Three Months')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (9, 'Every Four Months')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (10, 'Half Yearly')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (11, 'Yearly')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (12, 'Every Other Year')

INSERT FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (13, 'Every Three Years')



-- Receipt Timeframe Details
DELETE FROM PeriodCode 
GO

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (0, 'Custom')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (1, 'Within 1 Week')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (2, 'Within 2 Weeks')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (3, 'Within 1 Month')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (4, 'Within 3 Months')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (5, 'Within 6 Months')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (6, 'Within 1 Year')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (7, 'Within 3 Years')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (8, 'Within 5 Years')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (9, 'Within 10 Years')



-- Receipt Source Details
DELETE FROM SourceCode 
GO

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (1, 'Farther')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (2, 'Mother')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (3, 'Gradparaent')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (4, 'Aunt')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (5, 'Uncle')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (6, 'Partner')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (7, 'Child')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (8, 'Friend')

INSERT SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (9, 'Other')


-- ETP Component Details
DELETE FROM ETPComponentCode 
GO

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (1, 'Undeducted')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (2, 'Pre 1 July 1983')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (3, 'Post 30 June 1983 (Taxed)')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (4, 'Post 30 June 1983 (Un-Taxed)')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (5, 'Concessional')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (6, 'Post 30 June 1994 Invalidity')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (7, 'CGT Exempt')

INSERT ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (8, 'Non-Qualifying')


-- Owner Types
DELETE FROM OwnerCode
GO

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (1, 'Client')

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (2, 'Partner')

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (3, 'Joint')

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (4, 'Client''s Company')

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (5, 'Partner''s Company')

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (6, 'Joint Company')

INSERT OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (7, 'Family Trust')



--------------------------------------------
--	SURVEY
--------------------------------------------

-- 1 = RadioBox (SA), 2 = CheckBox (MA), 3 = ComboBox (SA), 4 = ListBox (MA), 5 = FreeText
-- SA - Single Answer, MA - Multiple Answer
DELETE FROM QuestionType
GO

INSERT QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (1, 'RadioBox (Single Answer)')
INSERT QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (2, 'CheckBox (Multiple Answer)')
INSERT QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (3, 'ComboBox (Single Answer)')
INSERT QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (4, 'ListBox (Multiple Answer)')
INSERT QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (5, 'FreeText')


--
--
--
DELETE FROM HealthConditionCode
GO

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

