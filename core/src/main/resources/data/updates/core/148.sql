CREATE TABLE PlanType (
  PlanTypeID		int 		NOT NULL,
  PlanTypeDesc		varchar(512)	NOT NULL
);
ALTER TABLE PlanType ADD CONSTRAINT PK_PlanTypeID PRIMARY KEY (PlanTypeID);


-- PlanTypeID = NULL means client plan data
ALTER TABLE PlanData ADD PlanTypeID int NULL; 
ALTER TABLE PlanData ADD 
  CONSTRAINT FK_PlanData_PlanType FOREIGN KEY (PlanTypeID) REFERENCES PlanType (PlanTypeID);


INSERT INTO PlanType (PlanTypeID, PlanTypeDesc)
VALUES (1, 'Template');


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.48', 'FPS.01.47');

