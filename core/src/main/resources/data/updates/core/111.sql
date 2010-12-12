CREATE TABLE PlanData ( 
  PlanDataID		int 		NOT NULL,
  PlanDataDesc		varchar(255)	NOT NULL,
#ifdef MSSQL
  PlanDataText		text
#endif MSSQL
#ifdef HSQLDB
  PlanDataText		varchar(8192)
#endif HSQLDB
#ifdef MYSQL
  PlanDataText		TEXT
#endif MYSQL
);

ALTER TABLE PlanData ADD 
  CONSTRAINT PK_PlanData PRIMARY KEY (PlanDataID);

ALTER TABLE PlanData ADD
  CONSTRAINT FK_PlanData_Object FOREIGN KEY (PlanDataID) REFERENCES Object (ObjectID);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.11', 'FPS.01.10')
 GO
