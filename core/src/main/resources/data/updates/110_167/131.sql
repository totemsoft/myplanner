if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[AssetAllocation]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 drop table [dbo].[AssetAllocation];

CREATE TABLE AssetAllocation
(
	"AssetAllocationID" 	int IDENTITY (1,1) NOT NULL,
	"Amount" 		numeric(15,4),
	"InCash" 		numeric(15,4),
	"InFixedInterest" 	numeric(15,4),
	"InAustShares" 		numeric(15,4),
	"InIntnlShares" 	numeric(15,4),
	"InProperty" 		numeric(15,4),
	"Include" 		char
);
ALTER TABLE AssetAllocation ADD CONSTRAINT PK_AssetAllocationID PRIMARY KEY (AssetAllocationID);

ALTER TABLE Financial ADD AssetAllocationID int NULL;
ALTER TABLE Financial ADD
  CONSTRAINT FK_Financial_AssetAllocation FOREIGN KEY (AssetAllocationID) REFERENCES AssetAllocation (AssetAllocationID);

ALTER TABLE StrategyModel ADD AssetAllocationID int NULL;
ALTER TABLE StrategyModel ADD
  CONSTRAINT FK_StrategyModel_AssetAllocation FOREIGN KEY (AssetAllocationID) REFERENCES AssetAllocation (AssetAllocationID);

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.31', 'FID.01.30');
