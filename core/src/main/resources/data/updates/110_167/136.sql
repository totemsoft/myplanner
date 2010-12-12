ALTER TABLE FinancialCode ALTER COLUMN FinancialCode varchar(32) NULL;

CREATE  INDEX [IX_iress-asset-name] ON [dbo].[iress-asset-name]([code]) ON [PRIMARY];

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.36', 'FID.01.35');