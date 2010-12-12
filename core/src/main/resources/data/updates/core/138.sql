-- 'Y' means active client, 'N' means inactive client
ALTER TABLE ClientPerson ADD Active char(1) NOT NULL 
#ifdef MSSQL
	DEFAULT 'N'
#endif MSSQL
; 

-- 'Y' means has private hospital cover, 'N' means not
ALTER TABLE PersonHealth ADD HospitalCover char(1) NOT NULL 
#ifdef MSSQL
	DEFAULT 'N'
#endif MSSQL
; 

--
-- Reference data updates
--
--select * from ObjectType;
--select * from FinancialType order by ObjectTypeID, FinancialTypeID;
--select * from FinancialCode order by FinancialTypeID, FinancialCodeID;

DELETE FROM ObjectType WHERE (ObjectTypeID=18);
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (18, 'Tax Offsets & Credits');

DELETE FROM ObjectType WHERE (ObjectTypeID=1018);
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (1018, 'person to Regular(Tax Offsets & Credits)');

DELETE FROM LinkObjectType WHERE (LinkObjectTypeID=1018);
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
 VALUES (1018, 1, 18, 'Link table (person to Regular(Tax Offsets & Credits))');

DELETE FROM FinancialType WHERE (FinancialTypeID=88);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (88, 'Salary and wages income type', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=89);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (89, 'Imputation Credits', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=90);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (90, 'Other', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=91);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (91, 'Early payment interest credit', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=92);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (92, 'Spouse/child-housekeeper/housekeeper', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=93);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (93, 'Senior Australian (Low income aged persons)', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=94);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (94, 'Superannuation', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=95);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (95, '30% private health insurance', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=96);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (96, 'Zone or overseas forces', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=97);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (97, 'Medical expenses', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=98);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (98, 'Parent/parent in law/invalid relative', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=99);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (99, 'Landcare and water facility', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=100);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (100, 'Goverment loan interest', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=101);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (101, 'Heritage conservation', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=102);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (102, 'Beneficiary or pensioner', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=103);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (103, 'Short term life assurance policies', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=104);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (104, 'Lump Sum payments and ETP', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=105);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (105, 'Foreign tax credit used (Available)', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=106);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (106, 'Low income', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=107);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (107, 'Share of credit for tax paid by trustee', 18);

DELETE FROM FinancialType WHERE (FinancialTypeID=108);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (108, 'Baby bonus claim', 18);


--
-- this should be the last statement in any script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.38', 'FPS.01.37');