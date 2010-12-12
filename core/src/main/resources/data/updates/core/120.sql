--------------------------------------------------------------
-- BUG FIX 491 - 06/08/2002
-- Add list of titles in attached list
-- Author: Joanne Wang
--------------------------------------------------------------

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('5', 'Captain');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('6', 'Dame');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('7', 'Director');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('8', 'Father');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('9', 'Judge');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('10', 'Lady');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('11', 'Madam');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('12', 'Maj.Gen.');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('13', 'Manager');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('14', 'Master');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('15', 'Messrs');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('16', 'Miss');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('17', 'Pastor');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('18', 'Paymaster');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('19', 'Professor');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('20', 'Rear Admiral');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('21', 'Reverend');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('22', 'Secretary');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('23', 'Sir');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('24', 'The Ven.');

INSERT INTO TitleCode (TitleCodeID, TitleCodeDesc)
 VALUES ('25', 'Venerable');


-- create new UserTypeCodes

-- to look after clients of group of advisers
INSERT INTO AdviserTypeCode (AdviserTypeCodeID, AdviserTypeCodeDesc)
 VALUES (5, 'Adviser Support'); 

-- to look after all clients (of all advisers)
UPDATE AdviserTypeCode SET AdviserTypeCodeDesc='Dealer' WHERE AdviserTypeCodeID=2;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.20', 'FPS.01.19');