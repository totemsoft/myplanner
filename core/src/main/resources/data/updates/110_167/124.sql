
ALTER TABLE StrategyFinancial DROP CONSTRAINT FK_StrategyFinancial_FinancialPool;

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[FinancialPool]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 drop table [dbo].[FinancialPool];

ALTER TABLE Financial ADD PoolAmount numeric(15,4) NULL;

DELETE FROM StrategyFinancial;

ALTER TABLE StrategyFinancial ADD
  CONSTRAINT FK_StrategyFinancial_Financial FOREIGN KEY (StrategyFinancialID) REFERENCES Financial (FinancialID);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.24', 'FID.01.23');