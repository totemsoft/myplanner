DELETE FROM RelationshipFinanceCode
GO

INSERT RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (9, 'Partner')
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 9

INSERT RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (10, 'Dependent')
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 10

INSERT RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (11, 'Contact')
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 11

INSERT RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (12, '?')
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 12

INSERT RelationshipFinanceCode (RelationshipFinanceCodeID, RelationshipFinanceCodeDesc) VALUES (13, '??')
UPDATE Object SET ObjectTypeID = 8 WHERE ObjectID = 13


DROP TABLE ContactOccupationCode
DROP TABLE PersonOccupation
DROP TABLE OccupationCode
GO

-- full list of all occupations
CREATE TABLE OccupationCode (
  OccupationCodeID	int 		NOT NULL,
  OccupationCodeDesc	varchar(50)	NOT NULL
)

-- subset of OccupationCode (contacts specific)
CREATE TABLE ContactOccupationCode (
  ContactOccupationCodeID	int 		NOT NULL
)
GO

ALTER TABLE OccupationCode ADD CONSTRAINT PK_OccupationCodeID PRIMARY KEY (OccupationCodeID)
ALTER TABLE ContactOccupationCode ADD CONSTRAINT PK_ContactOccupationCodeID PRIMARY KEY (ContactOccupationCodeID)
ALTER TABLE ContactOccupationCode ADD
  CONSTRAINT FK_ContactOccupationCode_OccupationCode FOREIGN KEY (ContactOccupationCodeID) REFERENCES OccupationCode (OccupationCodeID)

INSERT OccupationCode (OccupationCodeID, OccupationCodeDesc) VALUES (1, 'Legal Adviser')
INSERT OccupationCode (OccupationCodeID, OccupationCodeDesc) VALUES (2, 'Accountant')

INSERT ContactOccupationCode (ContactOccupationCodeID) VALUES (1)
INSERT ContactOccupationCode (ContactOccupationCodeID) VALUES (2)


CREATE TABLE PersonOccupation (
  PersonOccupationID		int IDENTITY (1,1) NOT NULL,
  PersonID			int		NOT NULL,
  JobDescription		varchar(50)	NULL,
  EmploymentStatusCodeID	int		NULL,
  IndustryCodeID		int		NULL,
  OccupationCodeID		int 		NULL,
  NextID			int		NULL,
  DateCreated			datetime	NOT NULL DEFAULT getDate()
)
GO
ALTER TABLE PersonOccupation ADD CONSTRAINT PK_PersonOccupationID PRIMARY KEY (PersonOccupationID)
ALTER TABLE PersonOccupation ADD
  CONSTRAINT FK_PersonOccupation_Person FOREIGN KEY (PersonID) REFERENCES Person (PersonID),
  CONSTRAINT FK_PersonOccupation_EmploymentStatusCode FOREIGN KEY (EmploymentStatusCodeID) REFERENCES EmploymentStatusCode (EmploymentStatusCodeID),
  CONSTRAINT FK_PersonOccupation_IndustryCode FOREIGN KEY (IndustryCodeID) REFERENCES IndustryCode (IndustryCodeID),
  CONSTRAINT FK_PersonOccupation_OccupationCode FOREIGN KEY (OccupationCodeID) REFERENCES OccupationCode (OccupationCodeID),
  CONSTRAINT FK_PersonOccupation_Next FOREIGN KEY (NextID) REFERENCES PersonOccupation (PersonOccupationID)


-- 1 = Employee; 2 = Self Employed; 3 = Unemployed; 4 = Retired
DELETE FROM EmploymentStatusCode
GO
INSERT EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (1, 'Employee')
INSERT EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (2, 'Self Employed')
INSERT EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (3, 'Unemployed')
INSERT EmploymentStatusCode (EmploymentStatusCodeID, EmploymentStatusCodeDesc) VALUES (4, 'Retired')


DROP TABLE WillStatusCode
GO

CREATE TABLE WillStatusCode (
  WillStatusCodeID	int 		NOT NULL,
  WillStatusCodeDesc	varchar(50)	NOT NULL
)
GO

ALTER TABLE WillStatusCode ADD CONSTRAINT PK_WillStatusCodeID PRIMARY KEY (WillStatusCodeID)

-- 1 = In Place; 2 = Being Drafted; 3 = Not in Place
INSERT WillStatusCode (WillStatusCodeID, WillStatusCodeDesc) VALUES (1, 'In Place')
INSERT WillStatusCode (WillStatusCodeID, WillStatusCodeDesc) VALUES (2, 'Being Drafted')
INSERT WillStatusCode (WillStatusCodeID, WillStatusCodeDesc) VALUES (3, 'Not in Place')



DROP TABLE WillChangeCode
GO

CREATE TABLE WillChangeCode (
  WillChangeCodeID	int 		NOT NULL,
  WillChangeCodeDesc	varchar(50)	NOT NULL
)
GO

ALTER TABLE WillChangeCode ADD CONSTRAINT PK_WillChangeCodeID PRIMARY KEY (WillChangeCodeID)

-- 1 = No Changes Planned; 2 = Include Children; 3 = Remove Partner
INSERT WillChangeCode (WillChangeCodeID, WillChangeCodeDesc) VALUES (1, 'No Changes Planned')
INSERT WillChangeCode (WillChangeCodeID, WillChangeCodeDesc) VALUES (2, 'Include Children')
INSERT WillChangeCode (WillChangeCodeID, WillChangeCodeDesc) VALUES (3, 'Remove Partner')
