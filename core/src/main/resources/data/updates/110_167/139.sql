-- spelling mistake
if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=100)
 UPDATE FinancialType SET FinancialTypeDesc='Government loan interest', ObjectTypeID=18 WHERE (FinancialTypeID=100)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (100, 'Government loan interest', 18);



--
-- Reference data updates
--
--select * from ObjectType;
--select * from FinancialType WHERE ObjectTypeID = 13 order by ObjectTypeID, FinancialTypeID;
--select * from FinancialCode order by FinancialTypeID, FinancialCodeID;

if exists (select * from ObjectType WHERE ObjectTypeID=19)
 UPDATE ObjectType SET ObjectTypeDesc='Income Streams' WHERE (ObjectTypeID=19)
else
 INSERT ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (19, 'Income Streams');

if exists (select * from ObjectType WHERE ObjectTypeID=1019)
 UPDATE ObjectType SET ObjectTypeDesc='person to Asset(Income Streams)' WHERE (ObjectTypeID=1019)
else
 INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (1019, 'person to Asset(Income Streams)');

if exists (select * from LinkObjectType WHERE LinkObjectTypeID=1019)
 UPDATE LinkObjectType SET ObjectTypeID1=1, ObjectTypeID2=19, LinkObjectTypeDesc='Link table (person to Asset(Income Streams))' WHERE (LinkObjectTypeID=1019)
else
 INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
 VALUES (1019, 1, 19, 'Link table (person to Asset(Income Streams))');


if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=22)
 UPDATE FinancialType SET FinancialTypeDesc='Pension Account', ObjectTypeID=19 WHERE (FinancialTypeID=22)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (22, 'Pension Account', 19);

if exists (select FinancialTypeID from FinancialType WHERE FinancialTypeID=23)
 UPDATE FinancialType SET FinancialTypeDesc='Annuity Policy', ObjectTypeID=19 WHERE (FinancialTypeID=23)
else
 INSERT FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (23, 'Annuity Policy', 19);


ALTER TABLE Asset ADD FrequencyCodeID int NULL; 
ALTER TABLE Asset ADD
  CONSTRAINT FK_Asset_FrequencyCode FOREIGN KEY (FrequencyCodeID) REFERENCES FrequencyCode (FrequencyCodeID);

ALTER TABLE Asset ADD AnnualAmount numeric(15,4) NULL; 
ALTER TABLE Asset ADD RegularAmount numeric(15,4) NULL; 

-- 'Y' means ComplyinfForDSS, 'N' or NULL otherwise
ALTER TABLE Financial ADD ComplyingForDSS char NULL;

ALTER TABLE Financial ADD DeductibleDSS	numeric(15,4) NULL;
ALTER TABLE Financial ADD Indexation	numeric(15,4) NULL;


ALTER TABLE Person ADD SupportToAge int NULL; 
-- 'Y' means DSSRecipient, 'N' or NULL otherwise
ALTER TABLE Person ADD DSSRecipient char NULL; 


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.39', 'FID.01.38');