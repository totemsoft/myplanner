IF ( exists ( SELECT * FROM DBVersion WHERE CurrentVersion = 'FPS.01.04' ) )
	RAISERROR( 'FPS.01.04 already exists', 16, 1 ) 
	--WITH NOWAIT, SETERROR
GO


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.04', 'FPS.01.03');
