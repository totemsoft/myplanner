#ifdef MSSQL
if exists (select * from dbo.sysobjects where id = object_id(N'sp_delete_financials') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_delete_financials
#endif MSSQL
;

#ifdef MSSQL
CREATE PROCEDURE sp_delete_financials 
	@PersonID int 
AS
/*
	DECLARE c CURSOR FOR
	SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 )
	FOR UPDATE
	
	OPEN c 
	
	DECLARE @FinancialID int 
	
	FETCH NEXT FROM c INTO @FinancialID
	WHILE @@FETCH_STATUS = 0
	BEGIN
		EXEC sp_delete_financial @FinancialID
	END
	
	CLOSE c 
	DEALLOCATE c
*/
	DELETE FROM Liability WHERE LiabilityID IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )

	DELETE FROM Regular WHERE RegularID IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )

	UPDATE Regular SET AssetID=NULL WHERE AssetID IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )
	DELETE FROM Asset WHERE AssetID IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )

	DELETE FROM Financial WHERE FinancialID IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )

	SELECT LinkID INTO #Link FROM Link WHERE ObjectID2 IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )
	DELETE FROM Link WHERE ObjectID2 IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )
	DELETE FROM Object WHERE ObjectID IN ( SELECT LinkID FROM #Link )

	DELETE FROM Object WHERE ObjectID IN ( SELECT ObjectID2 FROM Link 
	WHERE ( ObjectID1 = @PersonID ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 ) )

	DROP TABLE #Link
#endif MSSQL
;

-- SELECT * FROM Person WHERE FamilyName='Ediot'
-- SELECT * FROM ClientPerson
-- EXEC sp_delete_financials 32811
-- SELECT * FROM Link WHERE ( ObjectID1 = 32811 ) AND ( LinkObjectTypeID >= 1010 AND LinkObjectTypeID <= 1022 )

#ifdef MSSQL
if exists (select * from dbo.sysobjects where id = object_id(N'sp_delete_financial') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure sp_delete_financial
#endif MSSQL
;

#ifdef MSSQL
CREATE PROCEDURE sp_delete_financial 
	@FinancialID int 
AS

	DELETE FROM Liability WHERE LiabilityID = @FinancialID

	DELETE FROM Regular WHERE RegularID = @FinancialID

	UPDATE Regular SET AssetID=NULL WHERE AssetID = @FinancialID
	DELETE FROM Asset WHERE AssetID = @FinancialID

	DELETE FROM Financial WHERE FinancialID = @FinancialID

	SELECT LinkID INTO #Link FROM Link WHERE ObjectID2 = @FinancialID
	DELETE FROM Link WHERE ObjectID2 = @FinancialID
	DELETE FROM Object WHERE ObjectID IN ( SELECT LinkID FROM #Link )

	DELETE FROM Object WHERE ObjectID = @FinancialID

	DROP TABLE #Link
#endif MSSQL
;

-- SELECT * FROM Financial
-- SELECT * FROM Link WHERE ObjectID2 = 203493

-- EXEC sp_delete_financial 203493


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.50', 'FPS.01.49');

