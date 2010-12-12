if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[iress-asset-name]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[iress-asset-name];

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[share-price-data]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[share-price-data];

--------------------------------------------------------------
-- create tables 
--------------------------------------------------------------
CREATE TABLE [dbo].[iress-asset-name] (
	[code] [nvarchar] (10)  NULL ,
	[asset-full-name] [nvarchar] (60)  NULL ,
	[IssuerName] [nvarchar] (60)  NULL ,
	[IssuerAbbName] [nvarchar] (40)  NULL ,
	[IssuerShortName] [nvarchar] (40)  NULL ,
	[IssuerType] [nvarchar] (20)  NULL ,
	[SecurityType] [float] NULL ,
	[Exchange] [nvarchar] (10)  NULL ,
	[IndustrySubgroup] [float] NULL ,
	[Description] [nvarchar] (60)  NULL ,
	[ShortDescription] [nvarchar] (40)  NULL ,
	[AbbDescription] [nvarchar] (20)  NULL ,
	[AssetBacking] [float] NULL ,
	[IssuerCode] [nvarchar] (20)  NULL ,
	[IssuerExchange] [nvarchar] (20)  NULL ,
	[IndustrySubgroupDesc] [nvarchar] (20)  NULL ,
	[IndustryGroupDesc] [nvarchar] (20)  NULL ,
	[ISIN] [nvarchar] (20)  NULL ,
	[GICS] [float] NULL 
) ON [PRIMARY];

CREATE TABLE [dbo].[share-price-data] (
	[code] [varchar] (10)  NULL ,
	[price-date] [datetime] NULL ,
	[open-price] [float] NULL ,
	[close-price] [float] NULL 
) ON [PRIMARY];



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.19', 'FID.01.18');