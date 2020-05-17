--Author:        Valeri Chibaev
--Revision Number:    5
--Date Revised:     12 January 2011

USE master

-- Database Creation
IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'MyPlanner')
    DROP DATABASE [MyPlanner]
GO
IF @@ERROR <> 0 RAISERROR('Database MyPlanner could not be dropped', 20, 1) WITH LOG
GO

CREATE DATABASE [MyPlanner] 
ON
    (
    NAME = N'MyPlanner_Data',
    FILENAME = N'D:\RDSDBDATA\DATA\MyPlanner_Data.MDF',
    SIZE = 50,
    FILEGROWTH = 10%
    ) 
LOG ON 
    (
    NAME = N'MyPlanner_Log', 
    FILENAME = N'D:\RDSDBDATA\DATA\MyPlanner_Log.LDF',
    SIZE = 5,
    FILEGROWTH = 10%
    )
--COLLATE Latin1_General_CI_AS
GO

IF @@ERROR <> 0 RAISERROR('Database MyPlanner could not be created', 20, 1) WITH LOG
GO

USE MyPlanner
GO

IF @@ERROR <> 0 RAISERROR('Database MyPlanner could not be accessed', 20, 1) WITH LOG
GO
