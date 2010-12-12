UPDATE FrequencyCode SET FrequencyCodeDesc='Quarterly' WHERE FrequencyCodeID=8;

CREATE  INDEX [IX_apir-pic] ON [dbo].[apir-pic]([code]) ON [PRIMARY];

CREATE  INDEX [IX_share-price-data] ON [dbo].[share-price-data]([code], [price-date]) ON [PRIMARY];

CREATE  INDEX [IX_unit-price-data] ON [dbo].[unit-price-data]([code], [price-date]) ON [PRIMARY];


CREATE TABLE AssetAllocationData
(
	"code" 			int,
	"InCash" 		numeric(15,4),
	"InFixedInterest" 	numeric(15,4),
	"InAustShares" 		numeric(15,4),
	"InIntnlShares" 	numeric(15,4),
	"InProperty" 		numeric(15,4),
	"InOther" 		numeric(15,4),
	"DataDate"		datetime,
	"id" 			numeric(18, 0)
);

CREATE  INDEX [IX_code] ON [dbo].[AssetAllocationData]([code]) ON [PRIMARY];
CREATE  INDEX [IX_DataDate] ON [dbo].[AssetAllocationData]([DataDate]) ON [PRIMARY];


ALTER TABLE AssetAllocation ADD InOther numeric(15,4) NULL;

UPDATE AssetAllocation SET InOther = 0;

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.33', 'FID.01.32');
