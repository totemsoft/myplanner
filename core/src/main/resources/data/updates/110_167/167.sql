IF ( exists ( SELECT * FROM DBVersion WHERE CurrentVersion = 'FPS.01.67' ) )
	RAISERROR( 'FPS.01.67 already exists', 16, 1 ) 
	--WITH NOWAIT, SETERROR
GO

DELETE FROM DBVersion;

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.01', NULL);
