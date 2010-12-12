DELETE FROM FinancialType WHERE (FinancialTypeID=100);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (100, 'Government loan interest', 18);


--
-- Reference data updates
--
--select * from ObjectType;
--select * from FinancialType WHERE ObjectTypeID = 13 order by ObjectTypeID, FinancialTypeID;
--select * from FinancialCode order by FinancialTypeID, FinancialCodeID;

DELETE FROM ObjectType WHERE (ObjectTypeID=19);
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (19, 'Income Streams');

DELETE FROM ObjectType WHERE (ObjectTypeID=1019);
INSERT INTO ObjectType (ObjectTypeID, ObjectTypeDesc) VALUES (1019, 'person to Asset(Income Streams)');

DELETE FROM LinkObjectType WHERE (LinkObjectTypeID=1019);
INSERT INTO LinkObjectType (LinkObjectTypeID, ObjectTypeID1, ObjectTypeID2, LinkObjectTypeDesc)
 VALUES (1019, 1, 19, 'Link table (person to Asset(Income Streams))');

DELETE FROM FinancialType WHERE (FinancialTypeID=22);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (22, 'Pension Account', 19);

DELETE FROM FinancialType WHERE (FinancialTypeID=23);
INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (23, 'Annuity Policy', 19);


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
-- this should be the last statement in any script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.39', 'FPS.01.38');