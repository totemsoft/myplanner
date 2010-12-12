

CREATE TABLE [dbo].[apir-pic] (
	[identifier] [varchar] (3) NULL ,
	[apir-pic] [varchar] (9) NULL ,
	[apir-full-name] [varchar] (70) NULL ,
	[apir-short-name] [varchar] (35) NULL ,
	[apir-brief-name] [varchar] (28) NULL ,
	[code] [int] NULL ,
	[id] [numeric](18, 0) NULL 
) ;
 

CREATE TABLE [dbo].[asset-allocation-data] (
	[identifier] [varchar] (3) NULL ,
	[code] [int] NULL ,
	[country-code] [varchar] (3) NULL ,
	[asset-type-code] [varchar] (3) NULL ,
	[data-date] [datetime] NULL ,
	[comments] [bit] NULL ,
	[comments-text] [varchar] (255) NULL ,
	[benchmark] [float] NULL ,
	[minimum] [float] NULL ,
	[maximum] [float] NULL ,
	[district-code] [varchar] (3) NULL, 
	[id] [numeric] (18, 0) NULL 
) ;


CREATE TABLE [dbo].[asset-types] (
	[identifier] [varchar] (3) NULL ,
	[code] [varchar] (3) NULL ,
	[description] [varchar] (25) NULL , 
	[id] [numeric] (18, 0) NULL 
) ;


CREATE TABLE [dbo].[asset-types-allocations] (
	[identifier] [varchar] (3) NULL ,
	[code] [int] NULL ,
	[country-code] [varchar] (3) NULL ,
	[asset-type-code] [varchar] (3) NULL ,
	[data-date] [datetime] NULL ,
	[percentage] [float] NULL ,
	[district-code] [varchar] (3) NULL ,
	[id] [numeric] (18, 0)  NULL 
) ;


CREATE TABLE [dbo].[country-currency-names-codes] (
	[identifier] [varchar] (3) NULL ,
	[code] [varchar] (3) NULL ,
	[country-name] [varchar] (30) NULL ,
	[currency-code] [varchar] (3) NULL ,
	[currency-symbol] [varchar] (3) NULL ,
	[region-code] [varchar] (2) NULL ,
	[id] [numeric] (18, 0) NULL 
) ;


CREATE TABLE [dbo].[manager-data] (
	[identifier] [varchar] (2) NULL ,
	[country-code] [varchar] (3) NULL ,
	[code] [varchar] (7) NULL ,
	[full-name] [varchar] (50) NULL ,
	[status] [varchar] (30) NULL ,
	[short-name] [varchar] (30) NULL ,
	[brief-name] [varchar] (11) NULL ,
	[very-brief-name] [varchar] (5) NULL ,
	[group-code] [varchar] (7) NULL ,
	[consolidated-code] [varchar] (7) NULL ,
	[id] [numeric] (18, 0) NULL 
) ;


CREATE TABLE [dbo].[manager-group] (
	[identifier] [varchar] (3) NULL ,
	[country-code] [varchar] (3) NULL ,
	[group-code] [varchar] (7) NULL ,
	[description] [varchar] (20) NULL , 
	[id] [numeric] (18, 0) NULL 
) ;


CREATE TABLE [dbo].[performance-returns] (
	[identifier] [varchar] (3) NULL ,
	[code] [int] NULL ,
	[data-date] [datetime] NULL ,
	[return-1-month] [float] NULL ,
	[return-3-month] [float] NULL ,
	[return-6-month] [float] NULL ,
	[return-1-year] [float] NULL ,
	[return-2-year] [float] NULL ,
	[return-3-year] [float] NULL ,
	[return-5-year] [float] NULL ,
	[return-7-year] [float] NULL , 
	[id] [numeric]  (18, 0) NULL 
) ;


CREATE TABLE [dbo].[product-group-identifier-data] (
	[identifier] [varchar] (3) NULL ,
	[country-code] [varchar] (3) NULL ,
	[manager-code] [varchar] (7) NULL ,
	[product-group-code] [int] NULL ,
	[product-group-name] [varchar] (40) NULL ,
	[id] [numeric]  (18, 0) NULL 
) ;


CREATE TABLE [dbo].[product-information] (
	[identifier] [varchar] (3) NULL ,
	[code] [int] NULL ,
	[full-name] [varchar] (50) NULL ,
	[country-code] [varchar] (3) NULL ,
	[tax-group-code] [varchar] (2) NULL ,
	[manager-code] [varchar] (7) NULL ,
	[group-code] [int] NULL ,
	[short-name] [varchar] (30) NULL ,
	[brief-name] [varchar] (18) NULL ,
	[legal-type-code] [varchar] (2) NULL ,
	[region-code] [varchar] (2) NULL ,
	[asset-type-code] [varchar] (3) NULL ,
	[cash-distribution-code] [varchar] (1) NULL ,
	[trustee-code] [varchar] (7) NULL ,
	[custodian-code] [varchar] (7) NULL ,
	[commencement-date] [datetime] NULL ,
	[data-date] [datetime] NULL ,
	[stock-exchange] [bit] NULL ,
	[guarantees] [bit] NULL ,
	[unit-linked] [bit] NULL ,
	[valuation-frequency] [varchar] (20) NULL ,
	[declared-yield] [bit] NULL ,
	[rate-of-return-advance] [bit] NULL ,
	[catery] [varchar] (4) NULL ,
	[subcatery] [varchar] (4) NULL ,
	[manager-group-code] [varchar] (7) NULL ,
	[gearing] [bit] NULL ,
	[gearing-max] [float] NULL ,
	[gearing-comments] [varchar] (255) NULL ,
	[special-features] [varchar] (255) NULL ,
	[id] [numeric]  (18, 0) NULL 
) ;


CREATE TABLE [dbo].[product-legal-type] (
	[identifier] [varchar] (3) NULL ,
	[code] [varchar] (2) NULL ,
	[description] [varchar] (25) NULL ,
	[id] [numeric]  (18, 0) NULL 
) ;


CREATE TABLE [dbo].[product-pool-net-assets] (
	[identifier] [varchar] (3) NULL ,
	[code] [int] NULL ,
	[data-date] [datetime] NULL ,
	[product-net-assets] [float] NULL ,
	[pool-net-assets] [float] NULL ,
	[id] [numeric]  (18, 0) NULL 
) ;


CREATE TABLE [dbo].[unit-price-data] (
	[identifier] [varchar] (3) NULL ,
	[code] [int] NULL ,
	[price-date] [datetime] NULL ,
	[entry-price] [float] NULL ,
	[exit-price] [float] NULL ,
	[id] [numeric]  (18, 0) NULL 
) ;


-- FinancialTypeID Managed Funds
CREATE TABLE FinancialMapMorningStar (
  --  (PK) 
  [code] 		[int]		NOT NULL, 
  --  (UNIQUE COMBINATION)
  FinancialTypeID	int 		NOT NULL, 
  FinancialCodeID	int	 	NOT NULL, 
  LogicallyDeleted	char		NULL ,
	[id] [numeric]  (18, 0) NULL 
);



--
--				CONSTRAINS
--
ALTER TABLE FinancialMapMorningStar ADD 
  CONSTRAINT PK_FinancialMapMorningStar_code PRIMARY KEY (code);

ALTER TABLE FinancialMapMorningStar ADD
  CONSTRAINT FK_FinancialMapMorningStar_FinancialCodeType 
  FOREIGN KEY (FinancialCodeID,FinancialTypeID) 
  REFERENCES FinancialCode (FinancialCodeID,FinancialTypeID);


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.12', 'FID.01.11');
