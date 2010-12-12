

------------------------------------------------
--	START changing existing data structure
------------------------------------------------

--
--		
--

CREATE TABLE PlanData ( 
  PlanDataID		int 		NOT NULL,
  PlanDataDesc		varchar(255)	NOT NULL,
  PlanDataText		text
);

------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--
ALTER TABLE PlanData ADD 
  CONSTRAINT PK_PlanData PRIMARY KEY (PlanDataID);


--
--				FOREIGN KEY
--
ALTER TABLE PlanData ADD
  CONSTRAINT FK_PlanData_Object FOREIGN KEY (PlanDataID) REFERENCES Object (ObjectID);


--
--				UNIQUE INDEX
--



--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;


-- 
--                       Changed Timeframe Details
--



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.11', 'FID.01.10');

