--------------------------------------------------------------
-- BUG FIX 646 - 07/08/2002
-- Make column Value2 50 characters.
-- Author: Joanne Wang
--------------------------------------------------------------

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[morning-star-data]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[morning-star-data];

ALTER TABLE [iress-asset-name] ADD id numeric (18, 0) NULL;
ALTER TABLE [share-price-data] ADD id numeric (18, 0) NULL;

ALTER TABLE ContactMedia ALTER COLUMN Value2	varchar(50)	NULL;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.22', 'FID.01.21');