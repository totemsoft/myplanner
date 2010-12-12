--drop table morning_star_data;

ALTER TABLE iress_asset_name ADD id numeric (18, 0) NULL;
ALTER TABLE share_price_data ADD id numeric (18, 0) NULL;

ALTER TABLE ContactMedia ALTER COLUMN Value2	varchar(50)	NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.22', 'FPS.01.21');