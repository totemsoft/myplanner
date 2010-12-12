CREATE TABLE apir_pic (
	identifier varchar (3) NULL ,
	apir_pic varchar (9) NULL ,
	apir_full_name varchar (70) NULL ,
	apir_short_name varchar (35) NULL ,
	apir_brief_name varchar (28) NULL ,
	code int NULL ,
	id numeric(18, 0) NULL 
) ;
 

CREATE TABLE asset_allocation_data (
	identifier varchar (3) NULL ,
	code int NULL ,
	country_code varchar (3) NULL ,
	asset_type_code varchar (3) NULL ,
	data_date datetime NULL ,
	comments bit NULL ,
	comments_text varchar (255) NULL ,
	benchmark float NULL ,
	minimum float NULL ,
	maximum float NULL ,
	district_code varchar (3) NULL, 
	id numeric (18, 0) NULL 
) ;


CREATE TABLE asset_types (
	identifier varchar (3) NULL ,
	code varchar (3) NULL ,
	description varchar (25) NULL , 
	id numeric (18, 0) NULL 
) ;


CREATE TABLE asset_types_allocations (
	identifier varchar (3) NULL ,
	code int NULL ,
	country_code varchar (3) NULL ,
	asset_type_code varchar (3) NULL ,
	data_date datetime NULL ,
	percentage float NULL ,
	district_code varchar (3) NULL ,
	id numeric (18, 0)  NULL 
) ;


CREATE TABLE country_currency_names_codes (
	identifier varchar (3) NULL ,
	code varchar (3) NULL ,
	country_name varchar (30) NULL ,
	currency_code varchar (3) NULL ,
	currency_symbol varchar (3) NULL ,
	region_code varchar (2) NULL ,
	id numeric (18, 0) NULL 
) ;


CREATE TABLE manager_data (
	identifier varchar (2) NULL ,
	country_code varchar (3) NULL ,
	code varchar (7) NULL ,
	full_name varchar (50) NULL ,
	status varchar (30) NULL ,
	short_name varchar (30) NULL ,
	brief_name varchar (11) NULL ,
	very_brief_name varchar (5) NULL ,
	group_code varchar (7) NULL ,
	consolidated_code varchar (7) NULL ,
	id numeric (18, 0) NULL 
) ;


CREATE TABLE manager_group (
	identifier varchar (3) NULL ,
	country_code varchar (3) NULL ,
	group_code varchar (7) NULL ,
	description varchar (20) NULL , 
	id numeric (18, 0) NULL 
) ;


CREATE TABLE performance_returns (
	identifier varchar (3) NULL ,
	code int NULL ,
	data_date datetime NULL ,
	return_1_month float NULL ,
	return_3_month float NULL ,
	return_6_month float NULL ,
	return_1_year float NULL ,
	return_2_year float NULL ,
	return_3_year float NULL ,
	return_5_year float NULL ,
	return_7_year float NULL , 
	id numeric  (18, 0) NULL 
) ;


CREATE TABLE product_group_identifier_data (
	identifier varchar (3) NULL ,
	country_code varchar (3) NULL ,
	manager_code varchar (7) NULL ,
	product_group_code int NULL ,
	product_group_name varchar (40) NULL ,
	id numeric  (18, 0) NULL 
) ;


CREATE TABLE product_information (
	identifier varchar (3) NULL ,
	code int NULL ,
	full_name varchar (50) NULL ,
	country_code varchar (3) NULL ,
	tax_group_code varchar (2) NULL ,
	manager_code varchar (7) NULL ,
	group_code int NULL ,
	short_name varchar (30) NULL ,
	brief_name varchar (18) NULL ,
	legal_type_code varchar (2) NULL ,
	region_code varchar (2) NULL ,
	asset_type_code varchar (3) NULL ,
	cash_distribution_code varchar (1) NULL ,
	trustee_code varchar (7) NULL ,
	custodian_code varchar (7) NULL ,
	commencement_date datetime NULL ,
	data_date datetime NULL ,
	stock_exchange bit NULL ,
	guarantees bit NULL ,
	unit_linked bit NULL ,
	valuation_frequency varchar (20) NULL ,
	declared_yield bit NULL ,
	rate_of_return_advance bit NULL ,
	catery varchar (4) NULL ,
	subcatery varchar (4) NULL ,
	manager_group_code varchar (7) NULL ,
	gearing bit NULL ,
	gearing_max float NULL ,
	gearing_comments varchar (255) NULL ,
	special_features varchar (255) NULL ,
	id numeric  (18, 0) NULL 
) ;


CREATE TABLE product_legal_type (
	identifier varchar (3) NULL ,
	code varchar (2) NULL ,
	description varchar (25) NULL ,
	id numeric  (18, 0) NULL 
) ;


CREATE TABLE product_pool_net_assets (
	identifier varchar (3) NULL ,
	code int NULL ,
	data_date datetime NULL ,
	product_net_assets float NULL ,
	pool_net_assets float NULL ,
	id numeric  (18, 0) NULL 
) ;


CREATE TABLE unit_price_data (
	identifier varchar (3) NULL ,
	code int NULL ,
	price_date datetime NULL ,
	entry_price float NULL ,
	exit_price float NULL ,
	id numeric  (18, 0) NULL 
) ;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.12', 'FPS.01.11')
 GO
