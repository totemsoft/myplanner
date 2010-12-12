-----------------
-- BUG-FIX 703
-----------------
UPDATE FinancialType SET FinancialTypeDesc = 'Vacation/Holiday' WHERE FinancialTypeID = 43 AND ObjectTypeID = 15;

INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 59, 'Donations & Charities', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 60, 'Dry Cleaning', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 61, 'Entertainment/Recreation/Leisure', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 62, 'Liquor, Tobacco & Gambling', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 63, 'Maintenance', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 64, 'Medicare Gap', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 65, 'Mortgage Repayments', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 66, 'Newspapers & Magazines', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 67, 'Other', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 68, 'Petrol', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 69, 'Phone/Mobile Bill', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 70, 'Rates', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 71, 'Rental Payments', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 72, 'Superannuation Contributions', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 73, 'Tools', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 74, 'General', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 75, 'Savings/Investment', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 76, 'Holiday', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 77, 'Education', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 78, 'Job Expense', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 79, 'Loan', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 80, 'Personal Care', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 81, 'Pet Care', 15);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 82, 'Taxes', 15);


-----------------
-- BUG-FIX 703
-----------------
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 83, 'Jewellery', 12);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 84, 'Motor Bike', 12);	
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 85, 'Sporting Equipement', 12);	
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 86, 'Tools and Machinery', 12);	
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES ( 87, 'Other', 12);


-----------------
-- BUG-FIX 704
-----------------
UPDATE FinancialType SET FinancialTypeDesc = 'Debentures' WHERE FinancialTypeID = 1001 AND ObjectTypeID = 10;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.27', 'FPS.01.26');
