IF ( exists ( SELECT * FROM DBVersion WHERE CurrentVersion = 'FID.01.61' ) )
	RAISERROR( 'FID.01.61 already exists', 16, 1 ) 
	--WITH NOWAIT, SETERROR
GO

--DELETE FROM DBVersion WHERE CurrentVersion > 'FID.01.60'

--ALTER TABLE Model DROP COLUMN ModelData2;
ALTER TABLE Model ADD ModelData2 text NULL;

--ALTER TABLE StrategyGroup DROP COLUMN StrategyGroupData2;
ALTER TABLE StrategyGroup ADD StrategyGroupData2 text NULL;

--ALTER TABLE PlanData DROP COLUMN PlanDataText2;
ALTER TABLE PlanData ADD PlanDataText2 text NULL;


---------------------------------------------------------------------------------------------
--				STORED PROCEDURES
---------------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[sp_get_Link]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
 drop procedure [dbo].[sp_get_Link]
GO
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

GO

-- CLIENT_2_PERSON = 3001, PARTNER = 9, CLIENT$PERSON_2_RELATIONSHIP_FINANCE = 3001008
-- EXEC [dbo].[sp_get_Link] 21761, 3001, 9, 3001008

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vLink') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vLink
GO
CREATE VIEW dbo.vLink
 AS
 SELECT 
	Link.LinkID AS LinkID, 
	Link.ObjectID1 AS OwnerPrimaryKeyID, 
	Link.ObjectID2 AS LinkedObjectID,
	Link.LinkObjectTypeID AS LinkObjectTypeID
 FROM Link Link
 WHERE	Link.LogicallyDeleted IS NULL
GO

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vLink2') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vLink2
GO
CREATE VIEW dbo.vLink2
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

--SELECT LinkedObjectID FROM dbo.vLink2 
--WHERE OwnerPrimaryKeyID = 21761 AND LinkObjectTypeID = 3001
--	AND LinkedObjectID2 = 9 AND LinkObjectTypeID2 = 3001008

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vLink3') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vLink3
GO
CREATE VIEW dbo.vLink3
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

--SELECT LinkedObjectID3 FROM dbo.vLink3 
--WHERE OwnerPrimaryKeyID = 201483 AND LinkObjectTypeID = 1001
--	AND ( LinkedObjectID2 = 9 OR LinkedObjectID2 = 10 ) AND LinkObjectTypeID2 = 1008
--	AND LinkObjectTypeID3 = 1007

---------------------------------------------------------------------------------------------
--					VIEWS
---------------------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vUserPerson') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vUserPerson
GO
CREATE VIEW dbo.vUserPerson
 AS
 SELECT     dbo.UserPerson.*, dbo.Person.*
 FROM       dbo.UserPerson, dbo.Person
 WHERE      dbo.UserPerson.UserPersonID = dbo.Person.PersonID
GO


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vClientPerson') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vClientPerson
GO
CREATE VIEW dbo.vClientPerson
 AS
 SELECT     dbo.ClientPerson.*, dbo.Person.*
 FROM       dbo.ClientPerson, dbo.Person
 WHERE      dbo.ClientPerson.ClientPersonID = dbo.Person.PersonID
GO


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPerson') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPerson
GO
CREATE VIEW dbo.vPerson
 AS
 SELECT     dbo.Person.*
 FROM       dbo.Person
GO


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonAddress') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonAddress
GO
CREATE VIEW dbo.vPersonAddress
 AS
 SELECT dbo.Address.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.Address INNER JOIN
	dbo.Link ON dbo.Address.AddressID = dbo.Link.ObjectID2 LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonAddress WHERE PersonID = 68836
-- SELECT * FROM vLink WHERE OwnerPrimaryKeyID = 68836 AND LinkObjectTypeID = 1005
-- SELECT * FROM vPersonAddress WHERE PersonID = 68840
-- SELECT * FROM vLink WHERE LinkObjectTypeID = 1005 AND OwnerPrimaryKeyID = 68840
-- SELECT * FROM vPersonAddress WHERE PersonID = 32811


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonContactMedia') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonContactMedia
GO
CREATE VIEW dbo.vPersonContactMedia
 AS
 SELECT dbo.ContactMedia.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.ContactMedia INNER JOIN
	dbo.Link ON dbo.ContactMedia.ContactMediaID = dbo.Link.ObjectID2 LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonContactMedia WHERE PersonID = 31345
-- SELECT * FROM vPersonContactMedia WHERE PersonID = 32811


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonBusiness') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonBusiness
GO
CREATE VIEW dbo.vPersonBusiness
 AS
 SELECT dbo.Business.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.Business INNER JOIN
        dbo.Link ON dbo.Business.BusinessID = dbo.Link.ObjectID2 INNER JOIN
        dbo.Link LinkOccupation ON dbo.Link.LinkID = LinkOccupation.ObjectID1 LEFT OUTER JOIN
        dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  (dbo.Link.LogicallyDeleted IS NULL) AND (dbo.Link.LinkObjectTypeID = 1004) AND (LinkOccupation.LinkObjectTypeID = 1004030) AND 
        (LinkOccupation.ObjectID2 IS NULL) AND (LinkOccupation.LogicallyDeleted IS NULL)
GO
-- SELECT * FROM vPersonBusiness WHERE PersonID = 32811


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonFinancialGoal') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonFinancialGoal
GO
CREATE VIEW dbo.vPersonFinancialGoal
 AS
 SELECT dbo.FinancialGoal.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.FinancialGoal INNER JOIN
	dbo.Link ON dbo.FinancialGoal.FinancialGoalID = dbo.Link.ObjectID2 LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonFinancialGoal WHERE PersonID = 21761


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonSurvey') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonSurvey
GO
CREATE VIEW dbo.vPersonSurvey
 AS
 SELECT dbo.PersonSurvey.PersonSurveyID, dbo.PersonSurvey.DateCreated, dbo.PersonSurvey.SelectedRiskProfile, 
	dbo.Survey.SurveyID, dbo.Survey.SurveyTitle, dbo.Survey.SurveyDesc,
	dbo.Question.QuestionID, dbo.Question.QuestionTypeID, dbo.Question.QuestionDesc, 
	dbo.QuestionAnswer.QuestionAnswerID, dbo.QuestionAnswer.QuestionAnswerDesc, dbo.QuestionAnswer.QuestionAnswerScore, 
	dbo.PersonSurveyAnswerText.QuestionAnswerText,
	dbo.Person.PersonID AS PersonID
 FROM   dbo.PersonSurveyAnswer INNER JOIN
	dbo.QuestionAnswer ON dbo.PersonSurveyAnswer.QuestionAnswerID = dbo.QuestionAnswer.QuestionAnswerID RIGHT OUTER JOIN
	dbo.PersonSurvey INNER JOIN
	dbo.Link ON dbo.PersonSurvey.PersonSurveyID = dbo.Link.LinkID INNER JOIN
	dbo.Survey ON dbo.Link.ObjectID2 = dbo.Survey.SurveyID INNER JOIN
	dbo.Question ON dbo.Survey.SurveyID = dbo.Question.SurveyID ON dbo.QuestionAnswer.QuestionID = dbo.Question.QuestionID AND 
	dbo.PersonSurveyAnswer.QuestionID = dbo.Question.QuestionID AND 
	dbo.PersonSurveyAnswer.PersonSurveyID = dbo.PersonSurvey.PersonSurveyID LEFT OUTER JOIN
	dbo.PersonSurveyAnswerText ON 
	dbo.PersonSurveyAnswer.PersonSurveyAnswerID = dbo.PersonSurveyAnswerText.PersonSurveyAnswerID LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
	AND (dbo.Link.LinkObjectTypeID = 1024)
	AND dbo.QuestionAnswer.QuestionAnswerID IS NOT NULL
GO
-- SELECT * FROM vPersonSurvey WHERE PersonID = 32811


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonCategory') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonCategory
GO
CREATE VIEW dbo.vPersonCategory
 AS
 SELECT     
	dbo.Category.*,
	dbo.Person.PersonID AS PersonID
 FROM   dbo.SelectedCategory INNER JOIN
        dbo.Person ON dbo.SelectedCategory.PersonID = dbo.Person.PersonID INNER JOIN
        dbo.Category ON dbo.SelectedCategory.CategoryID = dbo.Category.CategoryID
GO
-- SELECT * FROM vPersonCategory WHERE PersonID = 32811


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vAsset') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vAsset
GO
CREATE VIEW dbo.vAsset
 AS
 SELECT 
	  dbo.Object.ObjectTypeID
	, dbo.Asset.*
	, dbo.Financial.*
	, dbo.FinancialType.FinancialTypeDesc AS FinancialTypeDesc
	, dbo.FinancialCode.FinancialCode AS FinancialCode
	, dbo.FinancialCode.FinancialCodeDesc AS FinancialCodeDesc
	, dbo.AssetAllocation.Amount AS AssetAllocationAmount
	, dbo.AssetAllocation.InCash
	, dbo.AssetAllocation.InFixedInterest
	, dbo.AssetAllocation.InAustShares
	, dbo.AssetAllocation.InIntnlShares
	, dbo.AssetAllocation.InProperty
	, dbo.AssetAllocation.Include
	, dbo.AssetAllocation.InOther
 FROM	dbo.Object, dbo.Asset, dbo.Financial
	, dbo.FinancialType
	, dbo.FinancialCode
	, dbo.AssetAllocation
 WHERE  dbo.Object.ObjectID = dbo.Asset.AssetID
	AND dbo.Asset.AssetID = dbo.Financial.FinancialID 
	AND dbo.Financial.FinancialTypeID *= dbo.FinancialType.FinancialTypeID
	AND dbo.Financial.FinancialCodeID *= dbo.FinancialCode.FinancialCodeID
 	AND dbo.Financial.AssetAllocationID *= dbo.AssetAllocation.AssetAllocationID
GO

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonAsset') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonAsset
GO
CREATE VIEW dbo.vPersonAsset
 AS
 SELECT dbo.vAsset.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.vAsset, dbo.Person
 WHERE  AssetID IN (
	SELECT ObjectID2 FROM Link 
	WHERE ObjectID1 = PersonID 
	AND LinkObjectTypeID = 1021 -- PERSON_2_ASSET = 1021
	AND LogicallyDeleted IS NULL
 )
GO
-- SELECT * FROM vPersonAsset WHERE PersonID = 21761


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vRegular') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vRegular
GO
CREATE VIEW dbo.vRegular
 AS
 SELECT 
	  dbo.Object.ObjectTypeID
	, dbo.Regular.*
	, dbo.Financial.*
 FROM	dbo.Object, dbo.Regular, dbo.Financial
 WHERE  dbo.Object.ObjectID = dbo.Regular.RegularID
	AND dbo.Regular.RegularID = dbo.Financial.FinancialID 
	AND dbo.Object.ObjectTypeID <> 16 -- Liability
GO

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonRegular') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonRegular
GO
CREATE VIEW dbo.vPersonRegular
 AS
 SELECT dbo.vRegular.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.vRegular, dbo.Person
 WHERE  RegularID IN (
	SELECT ObjectID2 FROM Link 
	WHERE ObjectID1 = PersonID 
	AND LinkObjectTypeID = 1022 -- PERSON_2_REGULAR = 1022
	AND LogicallyDeleted IS NULL
 )
GO
-- SELECT * FROM vPersonRegular WHERE PersonID = 21761


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vLiability') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vLiability
GO
CREATE VIEW dbo.vLiability
 AS
 SELECT
	  dbo.Object.ObjectTypeID
	, dbo.Liability.*
	, dbo.Regular.*
	, dbo.Financial.*
 FROM	dbo.Object, dbo.Liability, dbo.Regular, dbo.Financial
 WHERE  dbo.Object.ObjectID = dbo.Liability.LiabilityID
	AND dbo.Liability.LiabilityID = dbo.Regular.RegularID 
	AND dbo.Regular.RegularID = dbo.Financial.FinancialID
GO

if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonLiability') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonLiability
GO
CREATE VIEW dbo.vPersonLiability
 AS
 SELECT dbo.vLiability.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.vLiability, dbo.Person
 WHERE  LiabilityID IN (
	SELECT ObjectID2 FROM Link 
	WHERE ObjectID1 = PersonID 
	AND LinkObjectTypeID = 1016 -- PERSON_2_LIABILITY = 1016
	AND LogicallyDeleted IS NULL
 )
GO
-- SELECT * FROM vPersonLiability WHERE PersonID = 21761


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonModel') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonModel
GO
CREATE VIEW dbo.vPersonModel
 AS
 SELECT dbo.Model.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.Model INNER JOIN
	dbo.Link ON dbo.Model.ModelID = dbo.Link.ObjectID2 LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonModel WHERE PersonID = 32811

-----------------------------------------------------------------------------------
--
-----------------------------------------------------------------------------------
if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonStrategyGroup') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonStrategyGroup
GO
CREATE VIEW dbo.vPersonStrategyGroup
 AS
 SELECT dbo.StrategyGroup.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.StrategyGroup INNER JOIN
	dbo.Link ON dbo.StrategyGroup.StrategyGroupID = dbo.Link.ObjectID2 LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonStrategyGroup WHERE PersonID = 32811


if exists (select * from dbo.sysobjects where id = object_id(N'dbo.vPersonPlanData') and OBJECTPROPERTY(id, N'IsView') = 1)
 drop view dbo.vPersonPlanData
GO
CREATE VIEW dbo.vPersonPlanData
 AS
 SELECT dbo.PlanData.*, dbo.Person.PersonID AS PersonID
 FROM   dbo.PlanData INNER JOIN
	dbo.Link ON dbo.PlanData.PlanDataID = dbo.Link.ObjectID2 LEFT OUTER JOIN
	dbo.Person ON dbo.Link.ObjectID1 = dbo.Person.PersonID
 WHERE  dbo.Link.LogicallyDeleted IS NULL
GO
-- SELECT * FROM vPersonPlanData WHERE PersonID = 32811


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
 VALUES ('FID.01.61', 'FID.01.60');
