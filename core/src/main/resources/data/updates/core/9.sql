INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (1, 'Gold');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (2, 'Other Metals');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (3, 'Diversified Resources');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (4, 'Energy');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (5, 'Infrastructure and Utilities');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (6, 'Developers and Contractors');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (7, 'Building Materials');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (8, 'Alcohol and Tobacco');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (9, 'Food and Household');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (10, 'Chemicals');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (11, 'Engineering');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (12, 'Paper and Packaging');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (13, 'Retail');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (14, 'Transport');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (15, 'Media');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (16, 'Banks and Finance');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (17, 'Insurance');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (18, 'Investment and Financial Services');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (19, 'Investment and Financial Services');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (20, 'Property Trusts');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (21, 'Healthcare and Biotechnology');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (22, 'Miscellaneous Industrials');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (23, 'Diversified Industrials');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (24, 'Tourism and Leisure');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (25, 'Debt Issuer');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (26, 'Capital Safe');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (27, 'Capital Stable');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (28, 'Balanced');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (29, 'Growth');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (30, 'Australian Shares');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (31, 'Australian Smaller Company Shares');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (32, 'International Shares');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (33, 'Technology Shares');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (34, 'Property Securities');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (35, 'Mortgage Trusts');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (36, 'Geared Funds');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (37, 'Managed Share Portfolios');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (38, 'Other');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (39, 'Personal Share Admin Service - ASX Shares Only');

INSERT INTO InvestmentType (InvestmentTypeID, InvestmentTypeDesc)
VALUES (40, 'Cash');



-- Institutions
INSERT INTO Institution (InstitutionID, InstitutionName)
VALUES (1, 'Portofolio Services');



-- Fund Types
INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (1, 'Employer Superannuation - Accumulation');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (2, 'Employer Superannuation - Defined Benefit');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (3, 'Personal Superannuation');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (4, 'Allocated Pension');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (5, 'Allocated Pension');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (6, 'Complying Superannuation Pension');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (7, 'Complying Annuity');

INSERT INTO FundType (FundTypeID, FundTypeDesc)
VALUES (8, 'Non-Complying Annuity');



-- Frequency Details
DELETE FROM FrequencyCode
GO

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (1, 'Only Once');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (2, 'Daily');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (3, 'Weekly');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (4, 'Fortnightly');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (5, 'Twice Monthly');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (6, 'Monthly');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (7, 'Every Other Month');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (8, 'Every Three Months');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (9, 'Every Four Months');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (10, 'Half Yearly');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (11, 'Yearly');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (12, 'Every Other Year');

INSERT INTO FrequencyCode (FrequencyCodeID, FrequencyCodeDesc)
VALUES (13, 'Every Three Years');



-- Receipt Timeframe Details
INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (0, 'Custom');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (1, 'Within 1 Week');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (2, 'Within 2 Weeks');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (3, 'Within 1 Month');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (4, 'Within 3 Months');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (5, 'Within 6 Months');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (6, 'Within 1 Year');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (7, 'Within 3 Years');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (8, 'Within 5 Years');

INSERT INTO PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (9, 'Within 10 Years');



-- Receipt Source Details
INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (1, 'Farther');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (2, 'Mother');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (3, 'Gradparaent');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (4, 'Aunt');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (5, 'Uncle');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (6, 'Partner');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (7, 'Child');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (8, 'Friend');

INSERT INTO SourceCode (SourceCodeID, SourceCodeDesc)
VALUES (9, 'Other');


-- ETP Component Details
INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (1, 'Undeducted');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (2, 'Pre 1 July 1983');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (3, 'Post 30 June 1983 (Taxed)');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (4, 'Post 30 June 1983 (Un-Taxed)');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (5, 'Concessional');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (6, 'Post 30 June 1994 Invalidity');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (7, 'CGT Exempt');

INSERT INTO ETPComponentCode (ETPComponentCodeID, ETPComponentCodeDesc)
VALUES (8, 'Non-Qualifying');


-- Owner Types
INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (1, 'Client');

INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (2, 'Partner');

INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (3, 'Joint');

INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (4, 'Client''s Company');

INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (5, 'Partner''s Company');

INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (6, 'Joint Company');

INSERT INTO OwnerCode (OwnerCodeID, OwnerCodeDesc)
VALUES (7, 'Family Trust');



--------------------------------------------
--	SURVEY
--------------------------------------------

-- 1 = RadioBox (SA), 2 = CheckBox (MA), 3 = ComboBox (SA), 4 = ListBox (MA), 5 = FreeText
-- SA - Single Answer, MA - Multiple Answer
INSERT INTO QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (1, 'RadioBox (Single Answer)');
INSERT INTO QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (2, 'CheckBox (Multiple Answer)');
INSERT INTO QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (3, 'ComboBox (Single Answer)');
INSERT INTO QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (4, 'ListBox (Multiple Answer)');
INSERT INTO QuestionType (QuestionTypeID, QuestionTypeDesc)
VALUES (5, 'FreeText');


--
--
--
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (1, 'High Blood Pressure');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (2, 'Mental Disorder');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (3, 'Back Problems');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (4, 'Arthritis');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (5, 'Asthma');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (6, 'Neurological Disorder');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (7, 'Digestive/Bowl Disorder');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (8, 'Hepatitis, Cirrosis or Liver/Gall Bladder Disease');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (9, 'Kidney or Bladder Disorder');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (10, 'Cancer, Cyst or Tumor');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (11, 'Skin Condition');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (12, 'Blood Disorder');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (13, 'Over/Under Weight');
INSERT INTO HealthConditionCode (HealthConditionCodeID, HealthConditionCodeDesc)
VALUES (14, 'Fluctuation of 5kg or More Within 12mths');

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.00.09', 'FPS.00.08')
 GO
