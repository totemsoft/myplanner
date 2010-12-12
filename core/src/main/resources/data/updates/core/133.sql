UPDATE FrequencyCode SET FrequencyCodeDesc='Quarterly' WHERE FrequencyCodeID=8;

CREATE  INDEX IX_apir_pic ON apir_pic(code);

CREATE  INDEX IX_share_price_data ON share_price_data(code, price_date);

CREATE  INDEX IX_unit_price_data ON unit_price_data(code, price_date);


CREATE TABLE AssetAllocationData
(
	code 			int,
	InCash 		numeric(15,4),
	InFixedInterest 	numeric(15,4),
	InAustShares 		numeric(15,4),
	InIntnlShares 	numeric(15,4),
	InProperty 		numeric(15,4),
	InOther 		numeric(15,4),
	DataDate		datetime,
	id 			numeric(18, 0)
);

CREATE  INDEX IX_code ON AssetAllocationData(code);
CREATE  INDEX IX_DataDate ON AssetAllocationData(DataDate);


ALTER TABLE AssetAllocation ADD InOther numeric(15,4) NULL;

UPDATE AssetAllocation SET InOther = 0;

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.33', 'FPS.01.32');
