--drop table iress_asset_name;
--drop table share_price_data;

CREATE TABLE iress_asset_name (
	code varchar (10)  NULL ,
	asset_full_name varchar (60)  NULL ,
	IssuerName varchar (60)  NULL ,
	IssuerAbbName varchar (40)  NULL ,
	IssuerShortName varchar (40)  NULL ,
	IssuerType varchar (20)  NULL ,
	SecurityType float NULL ,
	Exchange varchar (10)  NULL ,
	IndustrySubgroup float NULL ,
	Description varchar (60)  NULL ,
	ShortDescription varchar (40)  NULL ,
	AbbDescription varchar (20)  NULL ,
	AssetBacking float NULL ,
	IssuerCode varchar (20)  NULL ,
	IssuerExchange varchar (20)  NULL ,
	IndustrySubgroupDesc varchar (20)  NULL ,
	IndustryGroupDesc varchar (20)  NULL ,
	ISIN varchar (20)  NULL ,
	GICS float NULL 
);

CREATE TABLE share_price_data (
	code varchar (10)  NULL ,
	price_date datetime NULL ,
	open_price float NULL ,
	close_price float NULL 
);



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.19', 'FPS.01.18');