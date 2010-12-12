-- 'Y' means active client, 'N' means inactive client
ALTER TABLE ClientPerson ADD Active char(1) NOT NULL DEFAULT 'N'; 

-- 'Y' means has private hospital cover, 'N' means not
ALTER TABLE PersonHealth ADD HospitalCover char(1) NOT NULL DEFAULT 'N'; 

--
-- Reference data updates
--
--select * from ObjectType;
--select * from FinancialType order by ObjectTypeID, FinancialTypeID;
--select * from FinancialCode order by FinancialTypeID, FinancialCodeID;

if exists (select * from ObjectType WHERE ObjectTypeID=18)
 UPDATE ObjectType SET ObjectTypeDesc='Tax Offsets & Credits' WHERE (ObjectTypeID=18)
else
 INSERT ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (18, 'Tax Offsets & Credits');

if exists (select * from ObjectType WHERE ObjectTypeID=1018)
 UPDATE ObjectType SET ObjectTypeDesc='person to Regular(Tax Offsets & Credits)' WHERE (ObjectTypeID=1018)
else
 INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (1018, 'person to Regular(Tax Offsets & Credits)');

if exists (select * from LinkObjectType WHERE LinkObjectTypeID=1018)
 UPDATE LinkObjectType SET ObjectTypeID1=1, ObjectTypeID2=18, LinkObjectTypeDesc='Link table (person to Regular(Tax Offsets & Credits))' WHERE (LinkObjectTypeID=1018)
else
 INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
 VALUES (1018, 1, 18, 'Link table (person to Regular(Tax Offsets & Credits))');


if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=88)
 UPDATE FinancialType SET FinancialTypeDesc='Salary and wages income type', ObjectTypeID=18 WHERE (FinancialTypeID=88)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (88, 'Salary and wages income type', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=89)
 UPDATE FinancialType SET FinancialTypeDesc='Imputation Credits', ObjectTypeID=18 WHERE (FinancialTypeID=89)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (89, 'Imputation Credits', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=90)
 UPDATE FinancialType SET FinancialTypeDesc='Other', ObjectTypeID=18 WHERE (FinancialTypeID=90)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (90, 'Other', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=91)
 UPDATE FinancialType SET FinancialTypeDesc='Early payment interest credit', ObjectTypeID=18 WHERE (FinancialTypeID=91)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (91, 'Early payment interest credit', 18);


if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=92)
 UPDATE FinancialType SET FinancialTypeDesc='Spouse/child-housekeeper/housekeeper', ObjectTypeID=18 WHERE (FinancialTypeID=92)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (92, 'Spouse/child-housekeeper/housekeeper', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=93)
 UPDATE FinancialType SET FinancialTypeDesc='Senior Australian (Low income aged persons)', ObjectTypeID=18 WHERE (FinancialTypeID=93)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (93, 'Senior Australian (Low income aged persons)', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=94)
 UPDATE FinancialType SET FinancialTypeDesc='Superannuation', ObjectTypeID=18 WHERE (FinancialTypeID=94)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (94, 'Superannuation', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=95)
 UPDATE FinancialType SET FinancialTypeDesc='30% private health insurance', ObjectTypeID=18 WHERE (FinancialTypeID=95)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (95, '30% private health insurance', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=96)
 UPDATE FinancialType SET FinancialTypeDesc='Zone or overseas forces', ObjectTypeID=18 WHERE (FinancialTypeID=96)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (96, 'Zone or overseas forces', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=97)
 UPDATE FinancialType SET FinancialTypeDesc='Medical expenses', ObjectTypeID=18 WHERE (FinancialTypeID=97)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (97, 'Medical expenses', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=98)
 UPDATE FinancialType SET FinancialTypeDesc='Parent/parent in law/invalid relative', ObjectTypeID=18 WHERE (FinancialTypeID=98)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (98, 'Parent/parent in law/invalid relative', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=99)
 UPDATE FinancialType SET FinancialTypeDesc='Landcare and water facility', ObjectTypeID=18 WHERE (FinancialTypeID=99)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (99, 'Landcare and water facility', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=100)
 UPDATE FinancialType SET FinancialTypeDesc='Goverment loan interest', ObjectTypeID=18 WHERE (FinancialTypeID=100)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (100, 'Goverment loan interest', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=101)
 UPDATE FinancialType SET FinancialTypeDesc='Heritage conservation', ObjectTypeID=18 WHERE (FinancialTypeID=101)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (101, 'Heritage conservation', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=102)
 UPDATE FinancialType SET FinancialTypeDesc='Beneficiary or pensioner', ObjectTypeID=18 WHERE (FinancialTypeID=102)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (102, 'Beneficiary or pensioner', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=103)
 UPDATE FinancialType SET FinancialTypeDesc='Short term life assurance policies', ObjectTypeID=18 WHERE (FinancialTypeID=103)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (103, 'Short term life assurance policies', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=104)
 UPDATE FinancialType SET FinancialTypeDesc='Lump Sum payments and ETP', ObjectTypeID=18 WHERE (FinancialTypeID=104)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (104, 'Lump Sum payments and ETP', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=105)
 UPDATE FinancialType SET FinancialTypeDesc='Foreign tax credit used (Available)', ObjectTypeID=18 WHERE (FinancialTypeID=105)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (105, 'Foreign tax credit used (Available)', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=106)
 UPDATE FinancialType SET FinancialTypeDesc='Low income', ObjectTypeID=18 WHERE (FinancialTypeID=106)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (106, 'Low income', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=107)
 UPDATE FinancialType SET FinancialTypeDesc='Share of credit for tax paid by trustee', ObjectTypeID=18 WHERE (FinancialTypeID=107)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (107, 'Share of credit for tax paid by trustee', 18);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=108)
 UPDATE FinancialType SET FinancialTypeDesc='Baby bonus claim', ObjectTypeID=18 WHERE (FinancialTypeID=108)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (108, 'Baby bonus claim', 18);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.38', 'FID.01.37');