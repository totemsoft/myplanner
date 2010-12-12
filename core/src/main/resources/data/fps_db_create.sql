--Title:		FPS Data Warehouse
--Physical Model: 	FPSsql
--Author:		Valeri Chibaev
--Revision Number:	5
--Date Revised: 	16 April 2002

USE master


-- Database Creation
IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'FPSsql')
	DROP DATABASE [FPSsql]
GO
IF @@ERROR <> 0 RAISERROR('Database FPSsql could not be dropped', 20, 1) WITH LOG
GO


CREATE DATABASE [FPSsql] 
ON 
	(
	NAME = N'FPSsql_Data', 
	FILENAME = N'C:\database\mssql\FPSsql_Data.MDF' , 
	SIZE = 5, 
	FILEGROWTH = 10%
	--), (
	--NAME = N'FPSsql_Data2', 
	--FILENAME = N'C:\database\mssql\FPSsql_Data2_Data.NDF' , 
	--SIZE = 5, 
	--FILEGROWTH = 10%
	) 
LOG ON 
	(
	NAME = N'FPSsql_Log', 
	FILENAME = N'C:\database\mssql\FPSsql_Log.LDF' , 
	SIZE = 5, 
	FILEGROWTH = 10%
	--), (
	--NAME = N'FPSsql_Log2', 
	--FILENAME = N'C:\database\mssql\FPSsql_Log2_Log.LDF' , 
	--SIZE = 5, 
	--FILEGROWTH = 10%
	)
--COLLATE Latin1_General_CI_AS
GO

IF @@ERROR <> 0 RAISERROR('Database FPSsql could not be created', 20, 1) WITH LOG
GO

USE FPSsql
GO

IF @@ERROR <> 0 RAISERROR('Database FPSsql could not be accessed', 20, 1) WITH LOG
GO

