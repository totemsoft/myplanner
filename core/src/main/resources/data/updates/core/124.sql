ALTER TABLE StrategyFinancial DROP CONSTRAINT FK_StrategyFinancial_FinancialPool;

drop table FinancialPool;

ALTER TABLE Financial ADD PoolAmount numeric(15,4) NULL;

DELETE FROM StrategyFinancial;

ALTER TABLE StrategyFinancial ADD
  CONSTRAINT FK_StrategyFinancial_Financial FOREIGN KEY (StrategyFinancialID) REFERENCES Financial (FinancialID);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.24', 'FPS.01.23');