------------------------------------------------
--	START changing existing data structure
------------------------------------------------

--DROP TABLE FinancialModel;

--
--		StrategyModel::Model (Strategy extension to Model table AS 0..1)
--
CREATE  TABLE StrategyModel ( 
  StrategyModelID		int 		NOT NULL,
  -- calculated total on all StrategyFinancial Assets
  TotalInitialAmount		numeric(15,4),
  -- calculated CashFlow amount (has to be >= 0)
  TotalContributionAmount	numeric(15,4)
);

--
--		StrategyFinancial::FinancialPool (Strategy extension to FinancialPool table AS n..1)
--
CREATE  TABLE StrategyFinancial (
  StrategyFinancialID		int 		NOT NULL,
  StrategyModelID		int 		NOT NULL,
  Amount			numeric(15,4)	NULL
);

------------------------------------------------
--	END changing existing data structure
------------------------------------------------




--
--				PRIMARY KEY
--
ALTER TABLE StrategyModel ADD 
  CONSTRAINT PK_StrategyModel PRIMARY KEY (StrategyModelID);
ALTER TABLE StrategyFinancial 
  ADD CONSTRAINT PK_StrategyFinancial_StrategyModel PRIMARY KEY (StrategyFinancialID, StrategyModelID);


--
--				FOREIGN KEY
--
ALTER TABLE StrategyModel ADD
  CONSTRAINT FK_StrategyModel_Model FOREIGN KEY (StrategyModelID) REFERENCES Model (ModelID);
ALTER TABLE StrategyFinancial ADD
  CONSTRAINT FK_StrategyFinancial_FinancialPool FOREIGN KEY (StrategyFinancialID) REFERENCES FinancialPool (FinancialPoolID),
  CONSTRAINT FK_StrategyFinancial_StrategyModel FOREIGN KEY (StrategyModelID) REFERENCES StrategyModel (StrategyModelID);


--
--				UNIQUE INDEX
--

--CREATE UNIQUE INDEX IDX_Model_1 ON Model ( ModelTypeID, ModelTitle );


--
--				INDEX
--


--
--				INSERT/UPDATE
--
--SELECT * FROM ObjectType;



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.09', 'FID.01.08');

