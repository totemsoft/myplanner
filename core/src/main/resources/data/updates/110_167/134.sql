if exists (select * from dbo.sysobjects where id = object_id(N'[FinancialMapSV2]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
  drop table [FinancialMapSV2];

if exists (select * from dbo.sysobjects where id = object_id(N'[FinancialMapMorningStar]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
  drop table [FinancialMapMorningStar];

IF EXISTS (SELECT name 
	   FROM   sysobjects 
	   WHERE  name = N'sp_drop_financialType' 
	   AND 	  type = 'P')
    DROP PROCEDURE sp_drop_financialType;

CREATE PROCEDURE sp_drop_financialType
	@ID int 
AS
	UPDATE Financial SET FinancialTypeID = NULL, FinancialCodeID = NULL WHERE FinancialTypeID = @ID

	DELETE FROM FinancialCode WHERE FinancialTypeID = @ID
	DELETE FROM FinancialType WHERE FinancialTypeID = @ID
;

-- CashAsset = 10
EXECUTE sp_drop_financialType 1001;
EXECUTE sp_drop_financialType 1002;
EXECUTE sp_drop_financialType 1003;
EXECUTE sp_drop_financialType 1004;
EXECUTE sp_drop_financialType 1011;
EXECUTE sp_drop_financialType 1014;
EXECUTE sp_drop_financialType 1015;
EXECUTE sp_drop_financialType 1016;
EXECUTE sp_drop_financialType 1017;

-- InvestmentAsset == 11
EXECUTE sp_drop_financialType 1005;
EXECUTE sp_drop_financialType 1006;
EXECUTE sp_drop_financialType 1007;
EXECUTE sp_drop_financialType 1008;
EXECUTE sp_drop_financialType 1009;
EXECUTE sp_drop_financialType 1010;
EXECUTE sp_drop_financialType 1012;
EXECUTE sp_drop_financialType 1013;

-- ASSET_PERSONAL = 12

-- SuperannuationAsset = 13
EXECUTE sp_drop_financialType 2001;
EXECUTE sp_drop_financialType 2002;
EXECUTE sp_drop_financialType 2003;
EXECUTE sp_drop_financialType 2004;
EXECUTE sp_drop_financialType 2005;


ALTER TABLE [dbo].[Financial] DROP CONSTRAINT [FK_Financial_FinancialCodeType];

ALTER TABLE [dbo].[FinancialCode] DROP CONSTRAINT [PK_FinancialCodeType];

ALTER TABLE [dbo].[Financial] ADD CONSTRAINT [FK_Financial_FinancialType] FOREIGN KEY 
	([FinancialTypeID]) REFERENCES [FinancialType] ([FinancialTypeID]);

ALTER TABLE [dbo].[FinancialCode] ADD CONSTRAINT [PK_FinancialCode] PRIMARY KEY
	([FinancialCodeID])  ON [PRIMARY] ;

ALTER TABLE [dbo].[Financial] ADD CONSTRAINT [FK_Financial_FinancialCode] FOREIGN KEY 
	([FinancialCodeID]) REFERENCES [FinancialCode] ([FinancialCodeID]);

ALTER TABLE FinancialCode ALTER COLUMN FinancialTypeID	int	NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.34', 'FID.01.33');
