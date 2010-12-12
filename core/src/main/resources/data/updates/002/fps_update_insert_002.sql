--
--  add new financial object types
--
INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (10, 'AssetCash table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (11, 'AssetInvestment table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (12, 'AssetPersonal table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (13, 'AssetSuperannuation table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (14, 'RegularIncome table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (15, 'RegularExpense table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (16, 'Liability table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (17, 'Insurance table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (18, 'AnticipatedPayment table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (19, 'AnticipatedTransfer table')

-- abstract object types START
INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (20, 'Financial table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (21, 'Asset table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (22, 'Regular table')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (23, 'Anticipated table')
-- abstract object types END



-- abstract link object types START
INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1004, 'Link table (person to financial)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1005, 'Link table (person to asset)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1006, 'Link table (person to regular)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1007, 'Link table (person to anticipated)')
-- abstract link object types END

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1008, 'Link table (person to AssetCash)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1009, 'Link table (person to AssetInvestment)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1010, 'Link table (person to AssetPersonal)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1011, 'Link table (person to AssetSuperannuation)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1012, 'Link table (person to RegularExpence)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1013, 'Link table (person to RegularIncome)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1014, 'Link table (person to Liability)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1015, 'Link table (person to Insurance)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1016, 'Link table (person to AnticipatedPayment)')

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (1017, 'Link table (person to AnticipatedTransfer)')


------------------------------------------------
-- financial link object types
------------------------------------------------
-- abstract link object types START
INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1004, 1, 20, 'Link table (person to financial)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1005, 1, 21, 'Link table (person to asset)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1006, 1, 22, 'Link table (person to regular)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1007, 1, 23, 'Link table (person to anticipated)')
-- abstract link object types END

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1008, 1, 10, 'Link table (person to AssetCash)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1009, 1, 11, 'Link table (person to AssetInvestment)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1010, 1, 12, 'Link table (person to AssetPersonal)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1011, 1, 13, 'Link table (person to AssetSuperannuation)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1012, 1, 14, 'Link table (person to RegularExpence)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1013, 1, 15, 'Link table (person to RegularIncome)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1014, 1, 16, 'Link table (person to Liability)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1015, 1, 17, 'Link table (person to Insurance)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1016, 1, 18, 'Link table (person to AnticipatedPayment)')

INSERT LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
VALUES (1017, 1, 19, 'Link table (person to AnticipatedTransfer)')



DELETE FROM FinancialCode
GO
DELETE FROM FinancialType
GO


--
-- Cash Asset Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (1, 'Savings/Cheque Account', 10)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (2, 'Mortgage Account', 10)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (3, 'Term Deposit', 10)

--
-- Investment Asset Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (4, 'Listed Shares', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (5, 'Listed Unit Trust', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (6, 'Unlisted External Unit Trust', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (7, 'Unlisted Internal Unit Trust', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (8, 'Debenture', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (9, 'Aust Govenment Bond', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (10, 'Derivative', 11)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (11, 'Life Policy', 11)

--
-- Personal Asset Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (12, 'Investment Property', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (13, 'Family Home', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (14, 'Automobile', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (15, 'Motor Bike', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (16, 'Recreational Vehicle', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (17, 'Sporting Equipment', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (18, 'Jewelry', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (19, 'Home Furnishings', 12)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (20, 'Tools and Machinery', 12)

--
-- Superannuation/Pension Asset Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (21, 'Superannuation Account', 13)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (22, 'Pension Account', 13)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (23, 'Annuity Policy', 13)


--
-- Regular Income Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (24, 'Salary & Wages', 14)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (25, 'Investment Income', 14)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (26, 'Social Security', 14)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (27, 'Retirement Income', 14)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (28, 'Other Income', 14)

--
-- Regular Expense Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (29, 'Automobile', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (30, 'Bank Charges', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (31, 'Bills', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (32, 'Donations', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (33, 'Child Care', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (34, 'Clothing', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (35, 'Education', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (36, 'Food', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (37, 'Gifts', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (38, 'Healthcare', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (39, 'Household', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (40, 'Insurance', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (41, 'Job Expense', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (43, 'Leisure', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (44, 'Loan', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (45, 'Personal Care', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (46, 'Pet Care', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (47, 'Taxes', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (49, 'Vacation', 15)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (50, 'Cultural Events', 15)

--
-- Liability Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (51, 'Credit Card', 16)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (52, 'Personal Loan', 16)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (53, 'Mortgage', 16)

--
-- Insurance Types
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (54, 'General Insurance', 17)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (55, 'Life Insurance', 17)

--
-- Anticipated Receipts
--
INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (56, 'Inheritance', 18)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (57, 'Sperannuation Lump Sum', 18)

INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
VALUES (58, 'Rollover', 18)



--------------------------------------------------------------------------
-- 			Cash Asset Codes			    	--
--------------------------------------------------------------------------
INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (1, 'Westpac Classic Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (2, 'Westpac Classic Plus Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (3, 'Westpac Basic Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (4, 'Westpac Bonus Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (5, 'Westpac Term Deposit', 3)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (6, 'Cash Management Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (7, 'Westpac Retirement Saver Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (8, 'Westpac Money Market Fund', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (9, 'ANZ Access Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (10, 'ANZ Progress Saver Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (11, 'ANZ Cash Management Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (12, 'ANZ Deeming Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (13, 'ANZ Term Deposit', 3)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (14, 'NAB Flexi Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (15, 'NAB Flexi Direct Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (16, 'NAB Cash Management Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (17, 'NAB Term Deposit', 3)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (18, 'St George Incentive Saver Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (19, 'St George Portfolio Cash Management Account', 1)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (20, 'St George Term Deposit', 3)

--
-- Regular Income Codes - Salary and Wages Type
--
INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (21, 'Bonus', 24)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (22, 'Commission', 24)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (23, 'Gross Pay', 24)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (24, 'Net Pay', 24)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (25, 'Overtime', 24)

-- Regular Income Codes - Investment Income Type

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (26, 'Capital Gains', 25)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (27, 'Dividend', 25)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (28, 'Distribution', 25)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (29, 'Interest', 25)

-- Regular Income Codes - Social Security Type

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (30, 'Carer Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (31, 'Carer Payment', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (32, 'Parenting Payment', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (33, 'Maternity Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (34, 'Double Orphan Pension', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (35, 'Maternity Immunisation Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (36, 'Youth Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (37, 'Newstart Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (38, 'Austudy Payment', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (39, 'ABSTUDY Payment', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (40, 'Assistance for Isolated Children', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (41, 'Disability Support Pension', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (42, 'Sickness Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (43, 'Mobility Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (44, 'Bereavement Payment', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (45, 'Bereavement Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (46, 'Widow Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (47, 'Widow Pension', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (48, 'Age Pension', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (49, 'On-Off Payment For The Aged', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (50, 'Mature Aged Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (51, 'Mature Aged Partner Allowance', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (52, 'Wife Pension', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (53, 'Aged Persons Saving Bonus', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (54, 'Self Funded Retirees Supplementary Bonus', 26)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (55, 'Pension Bonus Scheme', 26)

-- Regular Income Codes - Retirement Income Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (56, 'Allocated Pension Payments', 27)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (57, 'Complying Pension Payments', 27)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (58, 'Non-Complying Pension Payments', 27)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (59, 'Complying Annuity Payments', 27)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (60, 'Non-Complying Annuity Payments', 27)

-- Regular Income Codes - Other Income Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (61,'Gifts Received', 28)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (62, 'Loan Principal Received', 28)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (63, 'Lotteries', 28)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (64, 'Taxation Refund', 28)

-- Regular Expense Codes - Automotive Expense Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (65, 'Car Loan Payment', 29)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (66, 'Car Lease Payment', 29)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (67, 'Car Petrol', 29)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (68, 'Car Maintenance', 29)

-- Regular Expense Codes - Bank Charge Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (69, 'Service Charge', 30)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (70, 'Govenment Charges', 30)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (71, 'Interest Paid', 30)

-- Regular Expense Codes - Bill Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (72, 'Cable/Satellite Television', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (73, 'Mobile Phone', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (74, 'Telephone', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (75, 'Electricity', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (76, 'Garbage & Recycle', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (77, 'Health Club', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (78, 'Membership Fees', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (79, 'Mortgage Payment', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (80, 'Loan Payment', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (81, 'Credit Card Payment', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (82, 'Natural Gas/Oil', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (83, 'Newspaper', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (84, 'Internet Service', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (85, 'Rent', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (86, 'Council Rates', 31)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (87, 'Water & Sewage', 31)

-- Regular Expense Codes - Donation Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (88, 'Charitable Donation', 32)

-- Regular Expense Codes - Child Care Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (89, 'Day Care', 33)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (90, 'Nannie', 33)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (91, 'Baby-sitter', 33)

-- Regular Expense Codes - Clothing Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (92, 'Work Clothing', 34)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (93, 'School Clothing', 34)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (94, 'Other Clothing', 34)

-- Regular Expense Codes - Education Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (95, 'Primary School Fees', 35)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (96, 'Secondary School Fees', 35)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (97, 'University Fees', 35)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (98, 'Post Graduate Fees', 35)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (99, 'Other Fees', 35)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (100, 'Books', 35)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (101, 'Tuition', 35)

-- Regular Expense Codes - Food Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (102, 'Dining Out', 36)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (103, 'Groceries', 36)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (104, 'School Lunches', 36)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (105, 'Work Lunches', 36)

-- Regular Expense Codes - Gift Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (106, 'Birthday Gift', 37)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (107, 'Anniversary Gift', 37)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (108, 'Christmas Gift', 37)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (109, 'Easter Gift', 37)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (110, 'Wedding Gift', 37)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (111, 'Other Gift', 37)

-- Regular Expense Codes - Healthcare Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (112, 'Eyecare', 38)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (113, 'Dental', 38)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (114, 'Hospital', 38)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (115, 'Physician', 38)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (116, 'Prescriptions', 38)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (117, 'Vitamins', 38)

-- Regular Expense Codes - Household Expense Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (118, 'Furnishings', 39)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (119, 'House Cleaning', 39)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (120, 'Gardening', 39)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (121, 'Home Maintenance', 39)

-- Regular Expense Codes - Insurance Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (122, 'Automobile', 40)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (123, 'Home & Contents', 40)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (124, 'Private Health Cover', 40)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (125, 'Life Cover', 40)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (126, 'Income Protection', 40)

-- Regular Expense Codes - Job Expense Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (127, 'Reimbursed', 41)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (128, 'Non-Reimbursed', 41)

-- Regular Expense Codes - Leasure Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (129, 'Books & Magazines', 43)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (130, 'Cultural Events', 43)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (131, 'Entertaining', 43)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (132, 'Movies & Video Rentals', 43)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (133, 'Sporting Events', 43)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (134, 'Tapes $ Cd''s', 43)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (135, 'Toys & Games', 43)

-- Regular Expense Codes - Loan Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (136, 'Personal Loan Interest', 44)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (137, 'Mortgage Interest', 44)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (138, 'Mortgage Interest & Capital', 44)

-- Regular Expense Codes - Personal Care Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (139, 'Personal Care', 45)

-- Regular Expense Codes - Pet Care

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (140, 'Food', 46)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (141, 'Supplies', 46)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (142, 'Veterinarian', 46)

-- Regular Expense Codes - Tax Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (143, 'Income Tax (PAYG)', 47)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (144, 'Medicare Levy', 47)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (145, 'Capital Gains Tax', 47)

-- Regular Expense Codes - Vacation Expenses

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (146, 'Travel Expense', 49)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (147, 'Accomodation Expense', 49)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (148, 'Spending Money', 49)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (149, 'Entertainment', 49)

-- Regular Expense Codes - Cultural Event Types

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (150, 'Wedding', 50)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (151, 'Christening', 50)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (152, 'Debut', 50)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (153, 'Funeral', 50)

-- Liability Codes - Personal Loan

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (154, 'Secured', 52)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (155, 'Un-Secured', 52)

-- Liability Codes - Mortgage

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (156, 'Residence', 53)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (157, 'Holiday Home', 53)

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (158, 'Investment Property', 53)

-- Liability Codes - Credit Cards

INSERT FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
VALUES (159, 'Repayment', 51)



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
VALUES (1, 'Fiducian portofolio services')



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



-- Receipt Timeframe Details
DELETE FROM PeriodCode 
GO

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (1, 'Within 1 Month')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (2, 'Within 3 Months')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (3, 'Within 6 Months')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (4, 'Within 1 Year')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (5, 'Within 3 Years')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (6, 'Within 5 Years')

INSERT PeriodCode (PeriodCodeID, PeriodCodeDesc)
VALUES (7, 'Within 10 Years')



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

INSERT ObjectType (ObjectTypeID, ObjectTypeDesc)
VALUES (24, 'Survey table')

-- 1 = RadioBox (SA), 2 = CheckBox (MA), 3 = ComboBox (SA), 4 = ListBox (MA), 5 = FreeText
-- SA - Single Answer, MA - Multiple Answer
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
