ALTER TABLE FinancialCode ALTER COLUMN FinancialCode varchar(32) NULL;

CREATE  INDEX IX_iress_asset_name ON iress_asset_name(code);

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.36', 'FPS.01.35');