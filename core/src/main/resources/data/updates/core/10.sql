DELETE FROM FinancialCode;
DELETE FROM FinancialType;


--
-- Cash Asset Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (1, 'Savings/Cheque Account', 10);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (2, 'Mortgage Account', 10);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (3, 'Term Deposit', 10);

--
-- Investment Asset Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (4, 'Listed Shares', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (5, 'Listed Unit Trust', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (6, 'Unlisted External Unit Trust', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (7, 'Unlisted Internal Unit Trust', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (8, 'Debenture', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (9, 'Aust Govenment Bond', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (10, 'Derivative', 11);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (11, 'Life Policy', 11);

--
-- Personal Asset Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (12, 'Investment Property', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (13, 'Family Home', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (14, 'Automobile', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (15, 'Motor Bike', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (16, 'Recreational Vehicle', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (17, 'Sporting Equipment', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (18, 'Jewelry', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (19, 'Home Furnishings', 12);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (20, 'Tools and Machinery', 12);

--
-- Superannuation/Pension Asset Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (21, 'Superannuation Account', 13);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (22, 'Pension Account', 13);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (23, 'Annuity Policy', 13);


--
-- Regular Income Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (24, 'Salary & Wages', 14);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (25, 'Investment Income', 14);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (26, 'Social Security', 14);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (27, 'Retirement Income', 14);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (28, 'Other Income', 14);


--
-- Regular Expense Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (29, 'Automobile', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (30, 'Bank Charges', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (31, 'Bills', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (32, 'Donations', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (33, 'Child Care', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (34, 'Clothing', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (35, 'Education', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (36, 'Food', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (37, 'Gifts', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (38, 'Healthcare', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (39, 'Household', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (40, 'Insurance', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (41, 'Job Expense', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (43, 'Leisure', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (44, 'Loan', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (45, 'Personal Care', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (46, 'Pet Care', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (47, 'Taxes', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (49, 'Vacation', 15);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (50, 'Cultural Events', 15);

--
-- Liability Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (51, 'Credit Card', 16);;

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (52, 'Personal Loan', 16);;

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (53, 'Mortgage', 16);;

--
-- Insurance Types
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (54, 'General Insurance', 17);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (55, 'Life Insurance', 17);


--
-- Once only regular income (anticipated)
--
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (56, 'Inheritance', 14);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (57, 'Sperannuation Lump Sum', 14);

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID)
 VALUES (58, 'Rollover', 14);



--------------------------------------------------------------------------
-- 			Cash Asset Codes			    	--
--------------------------------------------------------------------------
INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (1, 'Westpac Classic Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (2, 'Westpac Classic Plus Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (3, 'Westpac Basic Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (4, 'Westpac Bonus Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (5, 'Westpac Term Deposit', 3);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (6, 'Cash Management Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (7, 'Westpac Retirement Saver Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (8, 'Westpac Money Market Fund', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (9, 'ANZ Access Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (10, 'ANZ Progress Saver Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (11, 'ANZ Cash Management Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (12, 'ANZ Deeming Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (13, 'ANZ Term Deposit', 3);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (14, 'NAB Flexi Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (15, 'NAB Flexi Direct Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (16, 'NAB Cash Management Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (17, 'NAB Term Deposit', 3);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (18, 'St George Incentive Saver Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (19, 'St George Portfolio Cash Management Account', 1);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (20, 'St George Term Deposit', 3);

--
-- Regular Income Codes - Salary and Wages Type
--
INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (21, 'Bonus', 24);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (22, 'Commission', 24);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (23, 'Gross Pay', 24);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (24, 'Net Pay', 24);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (25, 'Overtime', 24);

-- Regular Income Codes - Investment Income Type

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (26, 'Capital Gains', 25);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (27, 'Dividend', 25);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (28, 'Distribution', 25);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (29, 'Interest', 25);

-- Regular Income Codes - Social Security Type

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (30, 'Carer Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (31, 'Carer Payment', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (32, 'Parenting Payment', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (33, 'Maternity Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (34, 'Double Orphan Pension', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (35, 'Maternity Immunisation Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (36, 'Youth Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (37, 'Newstart Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (38, 'Austudy Payment', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (39, 'ABSTUDY Payment', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (40, 'Assistance for Isolated Children', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (41, 'Disability Support Pension', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (42, 'Sickness Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (43, 'Mobility Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (44, 'Bereavement Payment', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (45, 'Bereavement Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (46, 'Widow Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (47, 'Widow Pension', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (48, 'Age Pension', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (49, 'On-Off Payment For The Aged', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (50, 'Mature Aged Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (51, 'Mature Aged Partner Allowance', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (52, 'Wife Pension', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (53, 'Aged Persons Saving Bonus', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (54, 'Self Funded Retirees Supplementary Bonus', 26);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (55, 'Pension Bonus Scheme', 26);

-- Regular Income Codes - Retirement Income Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (56, 'Allocated Pension Payments', 27);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (57, 'Complying Pension Payments', 27);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (58, 'Non-Complying Pension Payments', 27);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (59, 'Complying Annuity Payments', 27);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (60, 'Non-Complying Annuity Payments', 27);

-- Regular Income Codes - Other Income Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (61,'Gifts Received', 28);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (62, 'Loan Principal Received', 28);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (63, 'Lotteries', 28);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (64, 'Taxation Refund', 28);

-- Regular Expense Codes - Automotive Expense Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (65, 'Car Loan Payment', 29);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (66, 'Car Lease Payment', 29);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (67, 'Car Petrol', 29);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (68, 'Car Maintenance', 29);

-- Regular Expense Codes - Bank Charge Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (69, 'Service Charge', 30);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (70, 'Govenment Charges', 30);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (71, 'Interest Paid', 30);

-- Regular Expense Codes - Bill Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (72, 'Cable/Satellite Television', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (73, 'Mobile Phone', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (74, 'Telephone', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (75, 'Electricity', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (76, 'Garbage & Recycle', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (77, 'Health Club', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (78, 'Membership Fees', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (79, 'Mortgage Payment', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (80, 'Loan Payment', 31);


INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (81, 'Credit Card Payment', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (82, 'Natural Gas/Oil', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (83, 'Newspaper', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (84, 'Internet Service', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (85, 'Rent', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (86, 'Council Rates', 31);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (87, 'Water & Sewage', 31);

-- Regular Expense Codes - Donation Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (88, 'Charitable Donation', 32);

-- Regular Expense Codes - Child Care Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (89, 'Day Care', 33);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (90, 'Nannie', 33);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (91, 'Baby-sitter', 33);

-- Regular Expense Codes - Clothing Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (92, 'Work Clothing', 34);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (93, 'School Clothing', 34);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (94, 'Other Clothing', 34);

-- Regular Expense Codes - Education Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (95, 'Primary School Fees', 35);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (96, 'Secondary School Fees', 35);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (97, 'University Fees', 35);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (98, 'Post Graduate Fees', 35);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (99, 'Other Fees', 35);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (100, 'Books', 35);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (101, 'Tuition', 35);

-- Regular Expense Codes - Food Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (102, 'Dining Out', 36);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (103, 'Groceries', 36);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (104, 'School Lunches', 36);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (105, 'Work Lunches', 36);

-- Regular Expense Codes - Gift Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (106, 'Birthday Gift', 37);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (107, 'Anniversary Gift', 37);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (108, 'Christmas Gift', 37);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (109, 'Easter Gift', 37);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (110, 'Wedding Gift', 37);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (111, 'Other Gift', 37);

-- Regular Expense Codes - Healthcare Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (112, 'Eyecare', 38);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (113, 'Dental', 38);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (114, 'Hospital', 38);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (115, 'Physician', 38);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (116, 'Prescriptions', 38);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (117, 'Vitamins', 38);

-- Regular Expense Codes - Household Expense Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (118, 'Furnishings', 39);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (119, 'House Cleaning', 39);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (120, 'Gardening', 39);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (121, 'Home Maintenance', 39);

-- Regular Expense Codes - Insurance Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (122, 'Automobile', 40);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (123, 'Home & Contents', 40);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (124, 'Private Health Cover', 40);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (125, 'Life Cover', 40);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (126, 'Income Protection', 40);

-- Regular Expense Codes - Job Expense Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (127, 'Reimbursed', 41);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (128, 'Non-Reimbursed', 41);

-- Regular Expense Codes - Leasure Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (129, 'Books & Magazines', 43);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (130, 'Cultural Events', 43);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (131, 'Entertaining', 43);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (132, 'Movies & Video Rentals', 43);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (133, 'Sporting Events', 43);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (134, 'Tapes $ Cd''s', 43);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (135, 'Toys & Games', 43);

-- Regular Expense Codes - Loan Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (136, 'Personal Loan Interest', 44);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (137, 'Mortgage Interest', 44);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (138, 'Mortgage Interest & Capital', 44);

-- Regular Expense Codes - Personal Care Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (139, 'Personal Care', 45);

-- Regular Expense Codes - Pet Care

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (140, 'Food', 46);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (141, 'Supplies', 46);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (142, 'Veterinarian', 46);

-- Regular Expense Codes - Tax Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (143, 'Income Tax (PAYG)', 47);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (144, 'Medicare Levy', 47);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (145, 'Capital Gains Tax', 47);

-- Regular Expense Codes - Vacation Expenses

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (146, 'Travel Expense', 49);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (147, 'Accomodation Expense', 49);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (148, 'Spending Money', 49);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (149, 'Entertainment', 49);

-- Regular Expense Codes - Cultural Event Types

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (150, 'Wedding', 50);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (151, 'Christening', 50);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (152, 'Debut', 50);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (153, 'Funeral', 50);

-- Liability Codes - Personal Loan

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (154, 'Secured', 52);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (155, 'Un-Secured', 52);

-- Liability Codes - Mortgage

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (156, 'Residence', 53);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (157, 'Holiday Home', 53);

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (158, 'Investment Property', 53);

-- Liability Codes - Credit Cards

INSERT INTO FinancialCode (FinancialCodeID, FinancialCodeDesc, FinancialTypeID)
 VALUES (159, 'Repayment', 51);




--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.00.10', 'FPS.00.09')
 GO
