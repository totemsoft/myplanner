-----------------------------------
-- New Marital Status: Partnered
-----------------------------------
INSERT INTO MaritalCode (MaritalCodeID, MaritalCodeDesc) VALUES (8, 'Partnered');

-----------------------
-- BUG-FIX 564 (typo)
-----------------------
UPDATE FinancialType SET FinancialTypeDesc = 'Sporting Equipment' WHERE FinancialTypeID = 85 AND ObjectTypeID = 12;


------------------------------------
-- BUG-FIX 703 (new expenses list)
------------------------------------
UPDATE FinancialType SET FinancialTypeDesc='General'            WHERE FinancialTypeID=29 AND ObjectTypeID=15;
UPDATE FinancialType SET FinancialTypeDesc='Savings/Investment' WHERE FinancialTypeID=30 AND ObjectTypeID=15;
UPDATE FinancialType SET FinancialTypeDesc='Holiday'            WHERE FinancialTypeID=31 AND ObjectTypeID=15;
UPDATE FinancialType SET FinancialTypeDesc='Education'          WHERE FinancialTypeID=32 AND ObjectTypeID=15;
UPDATE FinancialType SET FinancialTypeDesc='Other'              WHERE FinancialTypeID=33 AND ObjectTypeID=15;

UPDATE Financial SET FinancialTypeID=33 WHERE FinancialTypeID>=29 AND FinancialTypeID<=41
UPDATE Financial SET FinancialTypeID=33 WHERE FinancialTypeID>=43 AND FinancialTypeID<=45
UPDATE Financial SET FinancialTypeID=33 WHERE FinancialTypeID>=59 AND FinancialTypeID<=82

UPDATE FinancialCode SET FinancialTypeID=33 WHERE FinancialTypeID>=29 AND FinancialTypeID<=41
UPDATE FinancialCode SET FinancialTypeID=33 WHERE FinancialTypeID>=43 AND FinancialTypeID<=45
UPDATE FinancialCode SET FinancialTypeID=33 WHERE FinancialTypeID>=59 AND FinancialTypeID<=82

DELETE FROM FinancialType WHERE (FinancialTypeID>=34 AND FinancialTypeID<=41) AND ObjectTypeID=15;
DELETE FROM FinancialType WHERE (FinancialTypeID>=43 AND FinancialTypeID<=45) AND ObjectTypeID=15;
DELETE FROM FinancialType WHERE (FinancialTypeID>=59 AND FinancialTypeID<=82) AND ObjectTypeID=15;

------------------------------------
-- BUG-FIX 681 (new proffessional contact list)
------------------------------------

--UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=9;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=14;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=15;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=16;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=17;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=18;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=19;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=20;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=21;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=22;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=23;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=24;
UPDATE Object SET ObjectTypeID=8 WHERE ObjectID=25;

--INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES ( 9, 'Partner');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (14, 'Solicitor');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (15, 'Accountant');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (16, 'Authorised Agent');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (17, 'Bank Manager');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (18, 'Barrister');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (19, 'Corporate Trustee');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (20, 'Correspondent');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (21, 'Doctor');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (22, 'Financial Manager');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (23, 'Legal Adviser');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (24, 'Power of Attorney');
INSERT INTO RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (25, 'Secretary');


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.28', 'FPS.01.27');
