ALTER TABLE Model ADD ModelData2 
#ifdef MSSQL
	text 
#endif MSSQL
#ifdef HSQLDB
	varchar(8192) 
#endif HSQLDB
NULL;

ALTER TABLE StrategyGroup ADD StrategyGroupData2 
#ifdef MSSQL
	text 
#endif MSSQL
#ifdef HSQLDB
	varchar(8192) 
#endif HSQLDB
NULL;

ALTER TABLE PlanData ADD PlanDataText2 
#ifdef MSSQL
	text 
#endif MSSQL
#ifdef HSQLDB
	varchar(8192) 
#endif HSQLDB
NULL;


---------------------------------------------------------------------------------------------
--				STORED PROCEDURES
---------------------------------------------------------------------------------------------
#ifdef MSSQL
if exists (select * from sysobjects where id = object_id(N'[dbo].[sp_get_Link]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_get_Link]
#endif MSSQL
GO
#ifdef MSSQL
CREATE PROCEDURE [dbo].[sp_get_Link] 
	@OwnerPrimaryKeyID int,
	@LinkObjectTypeID int,
	@OwnerPrimaryKeyID2 int = 0,
	@LinkObjectTypeID2 int = 0
 AS
	IF ( @OwnerPrimaryKeyID2 > 0 )
	BEGIN
		SELECT ObjectID2 AS LinkedObjectID
		FROM Link
		WHERE	ObjectID1 = @OwnerPrimaryKeyID
			AND LinkObjectTypeID = @LinkObjectTypeID
			AND LinkID IN
				(SELECT ObjectID1
				FROM Link
				WHERE
				ObjectID2 = @OwnerPrimaryKeyID2
				AND ( LinkObjectTypeID = @LinkObjectTypeID2 )
				AND ( LogicallyDeleted IS NULL )
				)
			AND LogicallyDeleted IS NULL
	END
	ELSE
	BEGIN
		IF ( @LinkObjectTypeID2 > 0 )
			BEGIN
			SELECT ObjectID2 AS LinkedObjectID
			FROM Link
			WHERE	ObjectID1 = @OwnerPrimaryKeyID
				AND LinkObjectTypeID = @LinkObjectTypeID
				AND LinkID IN
					(SELECT ObjectID1
					FROM Link
					WHERE
					ObjectID2 IS NULL
					AND ( LinkObjectTypeID = @LinkObjectTypeID2 )
					AND ( LogicallyDeleted IS NULL )
					)
				AND LogicallyDeleted IS NULL
		END
		ELSE
		BEGIN
			SELECT ObjectID2 AS LinkedObjectID
			FROM Link
			WHERE	ObjectID1 = @OwnerPrimaryKeyID
				AND LinkObjectTypeID = @LinkObjectTypeID
				AND LogicallyDeleted IS NULL
		END
	END

#endif MSSQL
GO

-- CLIENT_2_PERSON = 3001, PARTNER = 9, CLIENT$PERSON_2_RELATIONSHIP_FINANCE = 3001008
-- EXEC [dbo].[sp_get_Link] 21761, 3001, 9, 3001008

--if exists (select * from sysobjects where id = object_id(N'vLink') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vLink
--GO
CREATE VIEW vLink
 AS
 SELECT 
	Link.LinkID AS LinkID, 
	Link.ObjectID1 AS OwnerPrimaryKeyID, 
	Link.ObjectID2 AS LinkedObjectID,
	Link.LinkObjectTypeID AS LinkObjectTypeID
 FROM Link Link
 WHERE	Link.LogicallyDeleted IS NULL
GO

--if exists (select * from sysobjects where id = object_id(N'vLink2') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vLink2
--GO
CREATE VIEW vLink2
 AS
 SELECT 
	Link1.LinkID AS LinkID, 
	Link1.ObjectID1 AS OwnerPrimaryKeyID, 
	Link1.ObjectID2 AS LinkedObjectID,
	Link1.LinkObjectTypeID AS LinkObjectTypeID,
	Link2.LinkID AS LinkID2, 
	Link2.ObjectID1 AS OwnerPrimaryKeyID2,
	Link2.ObjectID2 AS LinkedObjectID2,
	Link2.LinkObjectTypeID AS LinkObjectTypeID2
 FROM Link Link1, Link Link2
 WHERE	Link1.LinkID = Link2.ObjectID1
	AND Link1.LogicallyDeleted IS NULL
	AND Link2.LogicallyDeleted IS NULL
GO

--SELECT LinkedObjectID FROM vLink2 
--WHERE OwnerPrimaryKeyID = 21761 AND LinkObjectTypeID = 3001
--	AND LinkedObjectID2 = 9 AND LinkObjectTypeID2 = 3001008

--if exists (select * from sysobjects where id = object_id(N'vLink3') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vLink3
--GO
CREATE VIEW vLink3
 AS
 SELECT 
	Link1.LinkID AS LinkID, 
	Link1.ObjectID1 AS OwnerPrimaryKeyID, 
	Link1.ObjectID2 AS LinkedObjectID,
	Link1.LinkObjectTypeID AS LinkObjectTypeID,
	Link2.LinkID AS LinkID2, 
	Link2.ObjectID1 AS OwnerPrimaryKeyID2,
	Link2.ObjectID2 AS LinkedObjectID2,
	Link2.LinkObjectTypeID AS LinkObjectTypeID2,
	Link3.LinkID AS LinkID3, 
	Link3.ObjectID1 AS OwnerPrimaryKeyID3,
	Link3.ObjectID2 AS LinkedObjectID3,
	Link3.LinkObjectTypeID AS LinkObjectTypeID3
 FROM Link Link1, Link Link2, Link Link3
 WHERE	Link1.LinkID = Link2.ObjectID1 AND Link2.LinkID = Link3.ObjectID1
	AND Link1.LogicallyDeleted IS NULL
	AND Link2.LogicallyDeleted IS NULL
	AND Link3.LogicallyDeleted IS NULL
GO

--SELECT LinkedObjectID3 FROM vLink3 
--WHERE OwnerPrimaryKeyID = 201483 AND LinkObjectTypeID = 1001
--	AND ( LinkedObjectID2 = 9 OR LinkedObjectID2 = 10 ) AND LinkObjectTypeID2 = 1008
--	AND LinkObjectTypeID3 = 1007

---------------------------------------------------------------------------------------------
--					VIEWS
---------------------------------------------------------------------------------------------
--if exists (select * from sysobjects where id = object_id(N'vUserPerson') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vUserPerson
--GO
CREATE VIEW vUserPerson
 AS
 SELECT     UserPerson.*, Person.*
 FROM       UserPerson, Person
 WHERE      UserPerson.UserPersonID = Person.PersonID
GO


--if exists (select * from sysobjects where id = object_id(N'vClientPerson') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vClientPerson
--GO
CREATE VIEW vClientPerson
 AS
 SELECT     ClientPerson.*, Person.*
 FROM       ClientPerson, Person
 WHERE      ClientPerson.ClientPersonID = Person.PersonID
GO


--if exists (select * from sysobjects where id = object_id(N'vPerson') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPerson
--GO
CREATE VIEW vPerson
 AS
 SELECT     Person.*
 FROM       Person
GO


--if exists (select * from sysobjects where id = object_id(N'vPersonAddress') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonAddress
--GO
CREATE VIEW vPersonAddress
 AS
 SELECT Address.*, Person.PersonID AS PersonID
 FROM   Address INNER JOIN
	Link ON Address.AddressID = Link.ObjectID2 LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonAddress WHERE PersonID = 68836
-- SELECT * FROM vLink WHERE OwnerPrimaryKeyID = 68836 AND LinkObjectTypeID = 1005
-- SELECT * FROM vPersonAddress WHERE PersonID = 68840
-- SELECT * FROM vLink WHERE LinkObjectTypeID = 1005 AND OwnerPrimaryKeyID = 68840
-- SELECT * FROM vPersonAddress WHERE PersonID = 32811


--if exists (select * from sysobjects where id = object_id(N'vPersonContactMedia') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view vPersonContactMedia
GO
CREATE VIEW vPersonContactMedia
 AS
 SELECT ContactMedia.*, Person.PersonID AS PersonID
 FROM   ContactMedia INNER JOIN
	Link ON ContactMedia.ContactMediaID = Link.ObjectID2 LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonContactMedia WHERE PersonID = 31345
-- SELECT * FROM vPersonContactMedia WHERE PersonID = 32811


--if exists (select * from sysobjects where id = object_id(N'vPersonBusiness') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonBusiness
--GO
CREATE VIEW vPersonBusiness
 AS
 SELECT Business.*, Person.PersonID AS PersonID
 FROM   Business INNER JOIN
        Link ON Business.BusinessID = Link.ObjectID2 INNER JOIN
        Link LinkOccupation ON Link.LinkID = LinkOccupation.ObjectID1 LEFT OUTER JOIN
        Person ON Link.ObjectID1 = Person.PersonID
 WHERE  (Link.LogicallyDeleted IS NULL) AND (Link.LinkObjectTypeID = 1004) AND (LinkOccupation.LinkObjectTypeID = 1004030) AND 
        (LinkOccupation.ObjectID2 IS NULL) AND (LinkOccupation.LogicallyDeleted IS NULL)
GO
-- SELECT * FROM vPersonBusiness WHERE PersonID = 32811


--if exists (select * from sysobjects where id = object_id(N'vPersonFinancialGoal') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonFinancialGoal
--GO
CREATE VIEW vPersonFinancialGoal
 AS
 SELECT FinancialGoal.*, Person.PersonID AS PersonID
 FROM   FinancialGoal INNER JOIN
	Link ON FinancialGoal.FinancialGoalID = Link.ObjectID2 LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonFinancialGoal WHERE PersonID = 21761


--if exists (select * from sysobjects where id = object_id(N'vPersonSurvey') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonSurvey
--GO
#ifdef MSSQL
CREATE VIEW vPersonSurvey
 AS
 SELECT PersonSurvey.PersonSurveyID, PersonSurvey.DateCreated, PersonSurvey.SelectedRiskProfile, 
	Survey.SurveyID, Survey.SurveyTitle, Survey.SurveyDesc,
	Question.QuestionID, Question.QuestionTypeID, Question.QuestionDesc, 
	QuestionAnswer.QuestionAnswerID, QuestionAnswer.QuestionAnswerDesc, QuestionAnswer.QuestionAnswerScore, 
	PersonSurveyAnswerText.QuestionAnswerText,
	Person.PersonID AS PersonID
 FROM   PersonSurveyAnswer INNER JOIN
	QuestionAnswer ON PersonSurveyAnswer.QuestionAnswerID = QuestionAnswer.QuestionAnswerID RIGHT OUTER JOIN
	PersonSurvey INNER JOIN
	Link ON PersonSurvey.PersonSurveyID = Link.LinkID INNER JOIN
	Survey ON Link.ObjectID2 = Survey.SurveyID INNER JOIN
	Question ON Survey.SurveyID = Question.SurveyID ON QuestionAnswer.QuestionID = Question.QuestionID AND 
	PersonSurveyAnswer.QuestionID = Question.QuestionID AND 
	PersonSurveyAnswer.PersonSurveyID = PersonSurvey.PersonSurveyID LEFT OUTER JOIN
	PersonSurveyAnswerText ON 
	PersonSurveyAnswer.PersonSurveyAnswerID = PersonSurveyAnswerText.PersonSurveyAnswerID LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
	AND (Link.LinkObjectTypeID = 1024)
	AND QuestionAnswer.QuestionAnswerID IS NOT NULL

#endif MSSQL
GO
-- SELECT * FROM vPersonSurvey WHERE PersonID = 32811


--if exists (select * from sysobjects where id = object_id(N'vPersonCategory') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonCategory
--GO
CREATE VIEW vPersonCategory
 AS
 SELECT     
	Category.*,
	Person.PersonID AS PersonID
 FROM   SelectedCategory INNER JOIN
        Person ON SelectedCategory.PersonID = Person.PersonID INNER JOIN
        Category ON SelectedCategory.CategoryID = Category.CategoryID
GO
-- SELECT * FROM vPersonCategory WHERE PersonID = 32811


--if exists (select * from sysobjects where id = object_id(N'vAsset') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vAsset
--GO
#ifdef MSSQL
CREATE VIEW vAsset
 AS
 SELECT 
	  Object.ObjectTypeID
	, Asset.*
	, Financial.*
	, FinancialType.FinancialTypeDesc AS FinancialTypeDesc
	, FinancialCode.FinancialCode AS FinancialCode
	, FinancialCode.FinancialCodeDesc AS FinancialCodeDesc
	, AssetAllocation.Amount AS AssetAllocationAmount
	, AssetAllocation.InCash
	, AssetAllocation.InFixedInterest
	, AssetAllocation.InAustShares
	, AssetAllocation.InIntnlShares
	, AssetAllocation.InProperty
	, AssetAllocation.Include
	, AssetAllocation.InOther
 FROM	Object, Asset, 
    Financial LEFT OUTER JOIN
    FinancialType ON (Financial.FinancialTypeID = FinancialType.FinancialTypeID) LEFT OUTER JOIN
    FinancialCode ON (Financial.FinancialCodeID = FinancialCode.FinancialCodeID) LEFT OUTER JOIN
    AssetAllocation ON (Financial.AssetAllocationID = AssetAllocation.AssetAllocationID)
 WHERE  Object.ObjectID = Asset.AssetID
	AND Asset.AssetID = Financial.FinancialID 

#endif MSSQL
GO

--if exists (select * from sysobjects where id = object_id(N'vPersonAsset') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonAsset
--GO
#ifdef MSSQL
CREATE VIEW vPersonAsset
 AS
 SELECT vAsset.*, Person.PersonID AS PersonID
 FROM   vAsset, Person
 WHERE  AssetID IN (
	SELECT ObjectID2 FROM Link 
	WHERE ObjectID1 = PersonID 
	AND LinkObjectTypeID = 1021 -- PERSON_2_ASSET = 1021
	AND LogicallyDeleted IS NULL
 )

#endif MSSQL
GO
-- SELECT * FROM vPersonAsset WHERE PersonID = 21761


--if exists (select * from sysobjects where id = object_id(N'vRegular') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vRegular
--GO
CREATE VIEW vRegular
 AS
 SELECT 
	  Object.ObjectTypeID
	, Regular.*
	, Financial.*
 FROM	Object, Regular, Financial
 WHERE  Object.ObjectID = Regular.RegularID
	AND Regular.RegularID = Financial.FinancialID 
	AND Object.ObjectTypeID <> 16 -- Liability
GO

--if exists (select * from sysobjects where id = object_id(N'vPersonRegular') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonRegular
--GO
CREATE VIEW vPersonRegular
 AS
 SELECT vRegular.*, Person.PersonID AS PersonID
 FROM   vRegular, Person
 WHERE  RegularID IN (
	SELECT ObjectID2 FROM Link 
	WHERE ObjectID1 = PersonID 
	AND LinkObjectTypeID = 1022 -- PERSON_2_REGULAR = 1022
	AND LogicallyDeleted IS NULL
 )
GO
-- SELECT * FROM vPersonRegular WHERE PersonID = 21761


--if exists (select * from sysobjects where id = object_id(N'vLiability') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vLiability
--GO
CREATE VIEW vLiability
 AS
 SELECT
	  Object.ObjectTypeID
	, Liability.*
	, Regular.*
	, Financial.*
 FROM	Object, Liability, Regular, Financial
 WHERE  Object.ObjectID = Liability.LiabilityID
	AND Liability.LiabilityID = Regular.RegularID 
	AND Regular.RegularID = Financial.FinancialID
GO

--if exists (select * from sysobjects where id = object_id(N'vPersonLiability') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonLiability
--GO
CREATE VIEW vPersonLiability
 AS
 SELECT vLiability.*, Person.PersonID AS PersonID
 FROM   vLiability, Person
 WHERE  LiabilityID IN (
	SELECT ObjectID2 FROM Link 
	WHERE ObjectID1 = PersonID 
	AND LinkObjectTypeID = 1016 -- PERSON_2_LIABILITY = 1016
	AND LogicallyDeleted IS NULL
 )
GO
-- SELECT * FROM vPersonLiability WHERE PersonID = 21761


--if exists (select * from sysobjects where id = object_id(N'vPersonModel') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonModel
--GO
CREATE VIEW vPersonModel
 AS
 SELECT Model.*, Person.PersonID AS PersonID
 FROM   Model INNER JOIN
	Link ON Model.ModelID = Link.ObjectID2 LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonModel WHERE PersonID = 32811

-----------------------------------------------------------------------------------
--
-----------------------------------------------------------------------------------
--if exists (select * from sysobjects where id = object_id(N'vPersonStrategyGroup') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonStrategyGroup
--GO
CREATE VIEW vPersonStrategyGroup
 AS
 SELECT StrategyGroup.*, Person.PersonID AS PersonID
 FROM   StrategyGroup INNER JOIN
	Link ON StrategyGroup.StrategyGroupID = Link.ObjectID2 LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonStrategyGroup WHERE PersonID = 32811


--if exists (select * from sysobjects where id = object_id(N'vPersonPlanData') and OBJECTPROPERTY(id, N'IsView') = 1)
-- drop view vPersonPlanData
--GO
CREATE VIEW vPersonPlanData
 AS
 SELECT PlanData.*, Person.PersonID AS PersonID
 FROM   PlanData INNER JOIN
	Link ON PlanData.PlanDataID = Link.ObjectID2 LEFT OUTER JOIN
	Person ON Link.ObjectID1 = Person.PersonID
 WHERE  Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonPlanData WHERE PersonID = 32811


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FPS.01.61', 'FPS.01.60');
