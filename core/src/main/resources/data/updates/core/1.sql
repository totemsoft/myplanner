------------------------------------------------------------------
--	to keep track of db updates				--
--	version control	(XXX.01.01)				--
--		XXX - client org code	(e.g. FPS)	--
--		01 - major version number			--
--		01 - minor version number			--
------------------------------------------------------------------
CREATE TABLE DBVersion (
  CurrentVersion	varchar(9)	NOT NULL,
  PreviousVersion	varchar(9)	NULL,
  DateCreated		datetime	NULL --DEFAULT getDate()
)
GO

--
-- this should be the first statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.00.01', null)
 GO

ALTER TABLE DBVersion ADD CONSTRAINT
 PK_CurrentVersion PRIMARY KEY (CurrentVersion)
GO
ALTER TABLE DBVersion ADD CONSTRAINT
 FK_DBVersion_PreviousVersion FOREIGN KEY (PreviousVersion)
 REFERENCES DBVersion (CurrentVersion)
GO


--
-- this should be the last statement in any update script
--
UPDATE DBVersion 
 SET PreviousVersion = NULL
 WHERE CurrentVersion = 'FPS.00.01'
 GO
