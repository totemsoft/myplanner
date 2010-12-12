--------------------------------------------------------------
-- BUG FIX 483 - 07/08/2002
-- Add list of titles in attached list
-- Author: Joanne Wang
--------------------------------------------------------------
UPDATE PersonOccupation set OccupationCodeID = null;

DELETE FROM ContactOccupationCode;

DELETE FROM OccupationCode;

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('1', 'Administration & Secretarial');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('2', 'Apprenticeships');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('3', 'Arts & Design');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('4', 'Auditing');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('5', 'Building & Construction');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('6', 'Business Analysis');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('7', 'Cleaning');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('8', 'Community Care');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('9', 'Counselling/Social Work');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('10', 'Economics & Statistics');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('11', 'Education & Teaching');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('12', 'Engineering & Surveying');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('13', 'Environment/Health & Safety');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('14', 'Farming');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('15', 'Finance & Accounting');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('16', 'Fishery & Marine');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('17', 'Health Care');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('18', 'Hospitality');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('19', 'Human Resources');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('20', 'I.T. & Telecoms');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('21', 'Legal');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('22', 'Library & Information Mgmt');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('23', 'Management');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('24', 'Marketing & Sales');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('25', 'Policy, Planning & Research');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('26', 'Property & Real Estate');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('27', 'Public Affairs');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('28', 'Sciences');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('29', 'Tradesmen');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('30', 'Traineeships');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('31', 'Vehicle/Machine Operation');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('32', 'Doctor');

INSERT INTO OccupationCode (OccupationCodeID, OccupationCodeDesc)
 VALUES ('33', 'Other');

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.21', 'FPS.01.20');