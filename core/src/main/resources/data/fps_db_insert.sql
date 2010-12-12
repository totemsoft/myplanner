DELETE FROM LinkObjectType
GO
DELETE FROM ObjectType
GO

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (0, '***** reserved *****');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1, 'Person table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (2, 'UserPerson table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3, 'ClientPerson table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (4, 'Business table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (5, 'Address table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (6, 'ContactMedia table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (7, 'RelationshipCode table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (8, 'RelationshipFinanceCode table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (9, 'DependentPerson table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (10, 'AssetCash table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (11, 'AssetInvestment table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (12, 'AssetPersonal table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (13, 'AssetSuperannuation table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (14, 'RegularIncome table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (15, 'RegularExpense table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (16, 'Liability table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (17, 'Insurance table');

--INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
--VALUES (18, 'AnticipatedPayment table');

--INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
--VALUES (19, 'AnticipatedTransfer table');

-- abstract object types START
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (20, 'Financial table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (21, 'Asset table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (22, 'Regular table');

--INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
--VALUES (23, 'Anticipated table');
-- abstract object types END

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (24, 'Survey table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (25, 'Comment table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (26, 'FinancialGoal table');

-- VSH: 21/11/2001
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (27, 'RiskProfile table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (28, 'PersonEstate table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (29, 'Model table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (30, 'PersonOccupation table');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) 
VALUES (31, 'FinancialPool table');


--
--	PERSON (link table) specific object types (1000-1999)
--
-- XXXXXXYYY (9 = 6 + 3 digits max for int)
--
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1001, 'Link table (Person to Person');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1001, 1, 1, 'Link Person to Person');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1004, 'Link table (person to business)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1004, 1, 4, 'Link person to business');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1004030, 'Link table (Person_Business to Occupation)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1004030, 1004, 30, 'Link Person_Business to Occupation');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1005, 'Link table (person to address)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1005, 1, 5, 'Link person to address');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1006, 'Link table (person to ContactMedia)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1006, 1, 6, 'Link person to ContactMedia');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1007, 'Link table (Person to Relationship');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1007, 1, 7, 'Link Person to Relationship');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1008, 'Link table (Person to RelationshipFinance');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1008, 1, 8, 'Link Person to RelationshipFinance');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1005006, 'Link table (person-address to ContactMedia)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1005006, 1005, 6, 'Link person-address to ContactMedia');

------------------------------------------------
-- financial object types
------------------------------------------------
-- abstract object types START
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1020, 'Link table (person to financial)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1020, 1, 20, 'Link table (person to financial)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1021, 'Link table (person to asset)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1021, 1, 21, 'Link table (person to asset)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1022, 'Link table (person to regular)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1022, 1, 22, 'Link table (person to regular)');

-- abstract object types END

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1010, 'Link table (person to AssetCash)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1010, 1, 10, 'Link table (person to AssetCash)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1011, 'Link table (person to AssetInvestment)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1011, 1, 11, 'Link table (person to AssetInvestment)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1012, 'Link table (person to AssetPersonal)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1012, 1, 12, 'Link table (person to AssetPersonal)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1013, 'Link table (person to AssetSuperannuation)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1013, 1, 13, 'Link table (person to AssetSuperannuation)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1014, 'Link table (person to RegularExpence)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1014, 1, 14, 'Link table (person to RegularExpence)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1015, 'Link table (person to RegularIncome)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1015, 1, 15, 'Link table (person to RegularIncome)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1016, 'Link table (person to Liability)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1016, 1, 16, 'Link table (person to Liability)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1017, 'Link table (person to Insurance)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1017, 1, 17, 'Link table (person to Insurance)');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1029, 'Link table (Person to Model');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1029, 1, 29, 'Link Person to Model');



INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (2003, 'Link table (UserPerson to ClientPerson)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (2003, 2, 3, 'Link UserPerson to ClientPerson');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3001, 'Link table (ClientPerson to Person)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (3001, 3, 1, 'Link ClientPerson to Person');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3003, 'Link table (ClientPerson to ClientPerson)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (3003, 3, 3, 'Link ClientPerson to ClientPerson');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3001007, 'Link table (ClientPerson-Person to Relationship)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (3001007, 3001, 7, 'Link ClientPerson-Person to Relationship');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3001008, 'Link table (ClientPerson-Person to RelationshipFinance)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (3001008, 3001, 8, 'Link ClientPerson-Person to RelationshipFinance');



--
--	BUSINESS (link table) specific object types (2000-2999)
--
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (4001, 'Link table (business to person)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (4001, 4, 1, 'Link business to person');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (4005, 'Link table (business to address)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (4005, 4, 5, 'Link business to address');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (4005006, 'Link table (business-address to ContactMedia)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (4005006, 4005, 6, 'Link business-address to ContactMedia');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1024, 'Link table (Person to Survey)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1024, 1, 24, 'Link Person to Survey');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1025, 'Link table (Person to Comment)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1025, 1, 25, 'Link Person to Comment');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1026, 'Link table (Person to FinancialGoal)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1026, 1, 26, 'Link Person to FinancialGoal');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1025026, 'Link table (Person-Comment to FinancialGoal)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1025026, 1025, 26, 'Link Person-Comment to FinancialGoal');


-- VSH: 21/11/2001
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (3028, 'Link table (Client to Estate)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES(3028, 3, 28, 'Link Client to Estate');

INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (24027, 'Link table (Survey to RiskProfile)');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (24027, 24, 27, 'Link Survey to RiskProfile');


INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) 
VALUES (29031, 'Link table (Model to FinancialPool');
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (29031, 29, 31, 'Link Model to FinancialPool');



-- DELETE FROM CountryCode
-- GO
-- @exec Countries.sql
-- @exec SuburbPostCodes_AU.sql


-- 1 = Employee; 2 = Self Employed; 3 = Unemployed; 4 = Retired
DELETE FROM EmploymentStatusCode
GO
INSERT INTO EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (1, 'Employee');
INSERT INTO EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (2, 'Self Employed');
INSERT INTO EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (3, 'Unemployed');
INSERT INTO EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (4, 'Retired');


DELETE FROM IndustryCode
GO
INSERT INTO IndustryCode (IndustryCodeID, IndustryCodeDesc) VALUES (1, 'Financial Planning');


DELETE FROM ContactOccupationCode
GO
DELETE FROM OccupationCode
GO
INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc) VALUES (1, 'Legal Adviser');
INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc) VALUES (2, 'Accountant');

INSERT INTO ContactOccupationCode (ContactOccupationCodeID) VALUES (1)
INSERT INTO ContactOccupationCode (ContactOccupationCodeID) VALUES (2)


DELETE FROM TitleCode
GO

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
VALUES (1, 'Mr.');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
VALUES (2, 'Ms.');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
VALUES (3, 'Mrs.');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
VALUES (4, 'Dr.');



DELETE FROM SexCode
GO

INSERT INTO SexCode (SexCodeID, SexCodeDesc)
VALUES (1, 'Female');

INSERT INTO SexCode (SexCodeID, SexCodeDesc)
VALUES (2, 'Male');



DELETE FROM MaritalCode
GO

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (1, 'Single');

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (2, 'De-facto');

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (3, 'Married');

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (4, 'Separated');

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (5, 'Divorced');

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (6, 'Widowed');

INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc)
VALUES (7, 'Separated-Health');



DELETE FROM ContactMediaCode
GO

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (1, 'Phone');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (2, 'Phone work');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (3, 'Fax');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (4, 'Fax work');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (5, 'Mobile');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (6, 'Mobile work');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (7, 'E-mail');

INSERT INTO ContactMediaCode (ContactMediaCodeID, ContactMediaCodeDesc)
VALUES (8, 'E-mail work');



INSERT INTO AddressCode (AddressCodeID, AddressCodeDesc)
VALUES (1, 'Residential Address');

INSERT INTO AddressCode (AddressCodeID, AddressCodeDesc)
VALUES (2, 'Postal Address');

INSERT INTO AddressCode (AddressCodeID, AddressCodeDesc)
VALUES (3, 'Employer Address');

INSERT INTO AddressCode (AddressCodeID, AddressCodeDesc)
VALUES (4, 'Will Executor Address');



INSERT INTO AdviserTypeCode (AdviserTypeCodeID, AdviserTypeCodeDesc)
VALUES (1, 'Fiducain Franchise');

INSERT INTO AdviserTypeCode (AdviserTypeCodeID, AdviserTypeCodeDesc)
VALUES (2, 'Independent Dealer');

INSERT INTO AdviserTypeCode (AdviserTypeCodeID, AdviserTypeCodeDesc)
VALUES (3, 'Proper Authority');



-- 1 = Family Member; 2 = Friend; 3 = Professional Adviser; 4 = Other
INSERT INTO ReferalSourceCode (ReferalSourceCodeID, ReferalSourceCodeDesc)
VALUES (1, 'Family Member');

INSERT INTO ReferalSourceCode (ReferalSourceCodeID, ReferalSourceCodeDesc)
VALUES (2, 'Friend');

INSERT INTO ReferalSourceCode (ReferalSourceCodeID, ReferalSourceCodeDesc)
VALUES (3, 'Professional Adviser');

INSERT INTO ReferalSourceCode (ReferalSourceCodeID, ReferalSourceCodeDesc)
VALUES (4, 'Other');


-- 1 = Resident; 2 = Non-Resident
INSERT INTO ResidenceStatusCode (ResidenceStatusCodeID, ResidenceStatusCodeDesc)
VALUES (1, 'Resident');

INSERT INTO ResidenceStatusCode (ResidenceStatusCodeID, ResidenceStatusCodeDesc)
VALUES (2, 'Non-Resident');


-- 1 = Excelent; 2 = Very Good; 3 = Good; 4 = Poor; 5 = Very Poor
INSERT INTO HealthStateCode (HealthStateCodeID, HealthStateCodeDesc)
VALUES (1, 'Excelent');

INSERT INTO HealthStateCode (HealthStateCodeID, HealthStateCodeDesc)
VALUES (2, 'Very Good');

INSERT INTO HealthStateCode (HealthStateCodeID, HealthStateCodeDesc)
VALUES (3, 'Good');

INSERT INTO HealthStateCode (HealthStateCodeID, HealthStateCodeDesc)
VALUES (4, 'Poor');

INSERT INTO HealthStateCode (HealthStateCodeID, HealthStateCodeDesc)
VALUES (5, 'Very Poor');


-- 1 = In Place;2 = Not in Place
INSERT INTO StatusCode (StatusCodeID, StatusCodeDesc) VALUES (1, 'In Place');
INSERT INTO StatusCode (StatusCodeID, StatusCodeDesc) VALUES (2, 'Not in Place');


-- 1 = In Place; 2 = Being Drafted; 3 = Not in Place
INSERT INTO WillStatusCode (WillStatusCodeID, WillStatusCodeDesc) VALUES (1, 'In Place');
INSERT INTO WillStatusCode (WillStatusCodeID, WillStatusCodeDesc) VALUES (2, 'Being Drafted');
INSERT INTO WillStatusCode (WillStatusCodeID, WillStatusCodeDesc) VALUES (3, 'Not in Place');

-- 1 = No Changes Planned; 2 = Include Children; 3 = Remove Partner
INSERT INTO WillChangeCode (WillChangeCodeID, WillChangeCodeDesc) VALUES (1, 'No Changes Planned');
INSERT INTO WillChangeCode (WillChangeCodeID, WillChangeCodeDesc) VALUES (2, 'Include Children');
INSERT INTO WillChangeCode (WillChangeCodeID, WillChangeCodeDesc) VALUES (3, 'Remove Partner');


-- 1 = Client; 2 = Not Client
INSERT INTO SupporterCode (SupporterCodeID, SupporterCodeDesc) VALUES (1, 'Client');
INSERT INTO SupporterCode (SupporterCodeID, SupporterCodeDesc) VALUES (2, 'Not Client');



-- 1 = Job Search; 2 = Child Support
INSERT INTO AllowanceCode (AllowanceCodeID, AllowanceCodeDesc) VALUES (1, 'Job Search');
INSERT INTO AllowanceCode (AllowanceCodeID, AllowanceCodeDesc) VALUES (2, 'Child Support');



-- =================================================================================== --
-- Reserve first 10000 ID's in object table (ObjectTypeID = 0)
-- =================================================================================== --
EXECUTE sp_init_object
GO

--
-- start using them
--
DELETE FROM RelationshipCode
GO

-- 1 = Wife; 2 = Defacto; 3 = Son; 4= Daughter; 5 = Husband; 6 = Other

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (1, 'Wife');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 1

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (2, 'Defacto');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 2

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (3, 'Son');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 3

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (4, 'Daughter');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 4

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (5, 'Husband');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 5

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (6, 'Other');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 6

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (7, '?');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 7

INSERT INTO RelationshipCode (RelationshipCodeID, RelationshipCodeDesc) VALUES (8, '??');
UPDATE Object SET ObjectTypeID = 7 WHERE ObjectID = 8


DELETE FROM RelationshipFinanceCode
GO

INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (9, 'Partner');
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 9

INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (10, 'Dependent');
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 10

INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (11, 'Contact');
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 11

INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (12, '?');
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 12

INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (13, 'Will Executor');
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 13



------------------------------------------------------------------
--	FROM 103
------------------------------------------------------------------
DELETE FROM ParamType
GO

INSERT INTO ParamType (ParamTypeID, ParamTypeDesc)
VALUES (1, 'General parameter'); -- some of these params will be stored locally

INSERT INTO ParamType (ParamTypeID, ParamTypeDesc)
VALUES (2, 'DSS parameter');


------------------------------------------------------------------
--	FROM 105
------------------------------------------------------------------
DELETE FROM ModelType;
GO

INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (1, 'QuickView');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (2, 'Insurance Needs');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (3, 'Premium Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (4, 'Investment Gearing');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (5, 'Projected Wealth');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (6, 'Investment Properties');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (7, 'Loan & Mortgage Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (8, 'Allocated Pension');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (9, 'ETP & Rollover');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (10, 'Superannuation RBL');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (11, 'Retirement Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (12, 'Retirement Home');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (13, 'PAYG Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (14, 'Capital Gains Tax (CGT)');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (15, 'Age Pension');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (16, 'Pension Calculator');
INSERT INTO ModelType (ModelTypeID, ModelTypeDesc) VALUES (17, 'Allowance Calculator');
