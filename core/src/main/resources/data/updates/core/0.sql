#ifdef MSSQL
USE master
#endif MSSQL
GO

-- Database Creation
#ifdef MSSQL
IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'${DB_NAME}')
	DROP DATABASE [${DB_NAME}]
#endif MSSQL
GO

#ifdef MSSQL
IF @@ERROR <> 0 RAISERROR('Database ${DB_NAME} could not be dropped', 20, 1) WITH LOG
#endif MSSQL
GO

#ifdef MSSQL
CREATE DATABASE [${DB_NAME}] 
ON 
	(
	NAME = N'${DB_NAME}_Data', 
	FILENAME = N'${DB_LOCATION}\${DB_NAME}_Data.MDF' , 
	SIZE = 5, 
	FILEGROWTH = 10%
	--), (
	--NAME = N'${DB_NAME}_Data2', 
	--FILENAME = N'${DB_LOCATION}\${DB_NAME}_Data2_Data.NDF' , 
	--SIZE = 5, 
	--FILEGROWTH = 10%
	) 
LOG ON 
	(
	NAME = N'${DB_NAME}_Log', 
	FILENAME = N'${DB_LOCATION}\${DB_NAME}_Log.LDF' , 
	SIZE = 5, 
	FILEGROWTH = 10%
	--), (
	--NAME = N'${DB_NAME}_Log2', 
	--FILENAME = N'${DB_LOCATION}\${DB_NAME}_Log2_Log.LDF' , 
	--SIZE = 5, 
	--FILEGROWTH = 10%
	)
--COLLATE Latin1_General_CI_AS
#endif MSSQL
#ifdef MYSQL
CREATE DATABASE IF NOT EXISTS ${DB_NAME} 
#endif MYSQL
GO

#ifdef MSSQL
IF @@ERROR <> 0 RAISERROR('Database ${DB_NAME} could not be created', 20, 1) WITH LOG
#endif MSSQL
GO

#ifdef MSSQL
USE ${DB_NAME}
#endif MSSQL
GO

#ifdef MSSQL
IF @@ERROR <> 0 RAISERROR('Database ${DB_NAME} could not be accessed', 20, 1) WITH LOG
#endif MSSQL
GO
