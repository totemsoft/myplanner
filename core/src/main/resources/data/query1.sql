SELECT CurrentVersion FROM DBVersion ORDER BY DateCreated DESC


-- links as person

SELECT * --ObjectID2
FROM Link 
WHERE ( ObjectID1 = 10014 ) AND ( LinkObjectTypeID = 1200 )


-- links as dependent

SELECT * --ObjectID2
FROM Link
WHERE ( ObjectID1 = 10012 ) AND ( LinkObjectTypeID = 1200 )
AND ( LinkID IN
	(SELECT ObjectID1
	FROM Link 
	WHERE ( ObjectID2 = 10 ) AND ( LinkObjectTypeID = 1251 ) 
	)
)


-- get PersonDependent(1200) RelationshipCode (10014 <- 10017)

SELECT ObjectID2
FROM Link
WHERE ( ObjectID1 IN (
	SELECT LinkID
	FROM Link 
	WHERE ( ObjectID2 = 10 ) AND ( LinkObjectTypeID = 1251 ) 
	AND ( ObjectID1 IN
		(SELECT LinkID
		FROM Link 
		WHERE ( ObjectID1 = 10012 ) AND ( ObjectID2 = 10017 ) AND ( LinkObjectTypeID = 1200 ) 
		)
	) )
)


SELECT * --ObjectID2
 FROM Link
 WHERE ( ObjectID1 IN (
     SELECT LinkID
     FROM Link
     WHERE ( ObjectID2=10 ) AND ( LinkObjectTypeID=1251 )
     AND ( ObjectID1 IN
         (SELECT LinkID
         FROM Link
         WHERE ( ObjectID1=10012 ) AND ( ObjectID2=10017 ) AND ( LinkObjectTypeID=1200 )
         )
     ) )
) AND ( LinkObjectTypeID=1250 )
            



-- get dependents

SELECT dp.*, p.*
, df.AnnualIncome, df.AllowanceCodeID, df.AllowanceAmount
FROM DependentPerson dp, Person p, DependentFinancials df
WHERE ( dp.DependentPersonID IN
	(SELECT ObjectID2
		FROM Link
		WHERE ( ObjectID1 = 10014 ) AND ( LinkObjectTypeID = 1200 )
		AND ( LinkID IN
		(SELECT ObjectID1
			FROM Link
				WHERE ( ObjectID2 = 10 ) AND ( LinkObjectTypeID = 1251 ) 
				)
			)
		)
	)
	AND ( dp.DependentPersonID = p.PersonID )
	AND ( dp.DependentPersonID *= df.DependentPersonID ) AND ( df.NextID IS NULL )


-- get contacts(11)/dependents(10)
SELECT PersonID
FROM Person
WHERE ( PersonID IN
	(SELECT ObjectID2
	FROM Link
	WHERE ( ObjectID1 = 10012 ) AND ( LinkObjectTypeID = 1200 )
		AND ( LinkID IN
			(SELECT ObjectID1
			FROM Link
			WHERE ( ObjectID2 = 10 ) AND ( LinkObjectTypeID = 1251 )
			)
		AND ( LogicallyDeleted IS NULL ) 
		)
	)
)

-- get addresses
SELECT l.*, a.*
 FROM Link l, Address a
 WHERE ( l.ObjectID1 = 10005 ) AND ( l.LinkObjectTypeID = 1005 )
 AND ( l.ObjectID2 = a.AddressID )
 AND ( LogicallyDeleted IS NULL )
--select * from clientperson



-- get financial
SELECT FinancialID, ObjectID, ObjectTypeID
FROM Financial, Object
WHERE 
( FinancialID IN
	(SELECT ObjectID2
		FROM Link
		WHERE ( ObjectID1 = 10012 ) AND ( LinkObjectTypeID = 1004 )
	)
) AND (NextID IS NULL)
AND (FinancialID = ObjectID)


SELECT ac.MaturityDate
, a.AccountNumber, a.DateAcquired
, f.FinancialTypeID, f.FinancialCodeID, f.InstitutionID, f.OwnerCodeID, f.CountryCodeID, f.Amount, f.FinancialDesc, f.DateCreated
, fp.FinancialPoolID, fp.Amount AS PoolAmount
 FROM AssetCash ac, Asset a, Financial f, FinancialPool fp
 WHERE (ac.AssetCashID = 10225)
 AND (ac.AssetCashID = a.AssetID) AND (a.AssetID = f.FinancialID) AND (f.NextID IS NULL)
 AND (f.FinancialID *= fp.FinancialPoolID)



-- insert into object (objecttypeid) values (0)
-- update object set objecttypeid = 1250 where objectid = 10056
-- SELECT @@IDENTITY





SELECT ClientPersonID
FROM Link l, ClientPerson c
WHERE ( l.ObjectID1 = 10001 ) AND ( l.LinkObjectTypeID = 1100 )
AND ( l.ObjectID2 = c.ClientPersonID )

SELECT ClientPersonID
FROM Link l, ClientPerson c
WHERE ( l.ObjectID1 = 10001 ) AND ( l.LinkObjectTypeID = 1100 )
AND ( l.ObjectID2 = c.ClientPersonID )

SELECT p.*
, ph.PersonID AS ph_PersonID, ph.PersonHealthID, ph.IsSmoker, ph.HealthStateCodeID
, pts.PersonID AS pts_PersonID, pts.PersonTrustDIYStatusID, pts.TrustStatusCodeID, pts.DIYStatusCodeID, pts.Comment
, po.PersonID AS po_PersonID, po.PersonOccupationID, po.JobDescription, po.EmploymentStatusCodeID, po.IndustryCodeID, po.OccupationCodeID
FROM Person p, PersonHealth ph, PersonTrustDIYStatus pts, PersonOccupation po
WHERE (p.PersonID = 10012)
AND (p.PersonID *= ph.PersonID) AND (ph.NextID IS NULL)
AND (p.PersonID *= pts.PersonID) AND (pts.NextID IS NULL)
AND (p.PersonID *= po.PersonID) AND (po.NextID IS NULL)


SELECT 
	ft.ObjectTypeID, ft.FinancialTypeID, ft.FinancialTypeDesc,
	fc.FinancialCodeID, fc.FinancialCode, fc.FinancialCodeDesc
FROM FinancialType ft, FinancialCode fc
WHERE
	ft.FinancialTypeID = fc.FinancialTypeID
ORDER BY ft.ObjectTypeID, ft.FinancialTypeID, fc.FinancialCodeID


--
--	SURVEY
--
SELECT 
	s.SurveyID, SurveyTitle
	, q.QuestionID, q.QuestionDesc, q.QuestionTypeID
	, qa.QuestionAnswerID, qa.QuestionAnswerDesc, qa.QuestionAnswerScore
	, l.LinkID
FROM
	Survey s, Question q, QuestionAnswer qa, Link l
WHERE
	--(s.SurveyID = 10514)
	(s.SurveyID = 10005)
	AND (s.SurveyID = q.SurveyID)
	AND (q.QuestionID = qa.QuestionID)
        --AND (l.ObjectID1=10011) AND (l.LinkObjectTypeID=1024) AND (s.SurveyID*=l.ObjectID2) AND (l.LogicallyDeleted IS NULL)
        AND (l.ObjectID1=10007) AND (l.LinkObjectTypeID=1024) AND (s.SurveyID*=l.ObjectID2) AND (l.LogicallyDeleted IS NULL)
ORDER BY
	q.QuestionID, qa.QuestionAnswerID


--exec "d:\projects\sql\test.sql"


--
--	
--
SELECT * FROM FinancialType
SELECT * FROM FinancialCode
SELECT * FROM FinancialMapSV2



--EXEC sp_adduser 'fps'
--EXEC sp_password NULL, 'sa', 'sa'


SELECT a.*
FROM Link l, Address a
WHERE ( l.ObjectID1 = 10012 ) AND ( l.LinkObjectTypeID = 1000 )
AND ( l.ObjectID2 = a.AddressID )
AND ( LogicallyDeleted IS NULL )


SELECT
ft.ObjectTypeID,
ft.FinancialTypeID, ft.FinancialTypeDesc,
fc.FinancialCodeDesc, fc.FinancialCodeID, fc.FinancialCode
FROM FinancialType ft, FinancialCode fc
WHERE
ft.FinancialTypeID *= fc.FinancialTypeID
ORDER BY ft.ObjectTypeID, ft.FinancialTypeID, fc.FinancialCodeID


SELECT FinancialID, ObjectID, ObjectTypeID
 FROM Financial, Object
 WHERE ( FinancialID IN
 (SELECT ObjectID2
 FROM Link
 WHERE ( ObjectID1 = 10012 ) AND ( LinkObjectTypeID = 1004 )
 AND ( LogicallyDeleted IS NULL )
 )
 ) AND (NextID IS NULL)
 AND (FinancialID = ObjectID)

SELECT l.AccountNumber, l.DateStart, l.DateEnd, l.InterestRate
, r.RegularAmount, r.FrequencyCodeID, r.DateNext, r.AssetID
, f.FinancialTypeID, f.FinancialCodeID, f.InstitutionID, f.OwnerCodeID, f.CountryCodeID, f.Amount, f.FinancialDesc, f.DateCreated
 FROM Liability l, RegularExpense re, Regular r, Financial f
 WHERE (l.LiabilityID = 10717)
 AND (l.LiabilityID = re.RegularExpenseID) AND (re.RegularExpenseID = r.RegularID) AND (r.RegularID = f.FinancialID) AND (f.NextID IS NULL)

SELECT ass.FundTypeID, ass.UnitsShares
, a.AccountNumber, a.DateAcquired, a.BeneficiaryID, a.LiabilityID
, f.FinancialTypeID, f.FinancialCodeID, f.InstitutionID, f.OwnerCodeID, f.CountryCodeID, f.Amount, f.FinancialDesc, f.DateCreated
 FROM AssetSuperannuation ass, Asset a, Financial f
 WHERE (ass.AssetSuperannuationID = 10713)
 AND (ass.AssetSuperannuationID = a.AssetID) AND (a.AssetID = f.FinancialID) AND (f.NextID IS NULL)


--SELECT * FROM Comment
SELECT c.* 
 FROM Link l, Comment c
 WHERE
 ( l.ObjectID1 = 10012 ) AND ( l.ObjectID2 = c.CommentID ) AND ( l.LinkObjectTypeID = 1025 )
 AND ( LinkID IN
 (SELECT ObjectID1
 FROM Link
 WHERE ( ObjectID2 IS NULL ) AND ( LinkObjectTypeID = 1025026 )
 )
 )
 AND ( LogicallyDeleted IS NULL )


SELECT     TOP 10 *
FROM       Address


-- survey
SELECT *--ObjectID1
 FROM Link
 WHERE ( LinkObjectTypeID=24027 ) -- SURVEY_2_RISKPROFILE
   AND ( LogicallyDeleted IS NULL )
 ORDER BY ObjectID1 DESC

SELECT *--ObjectID1
 FROM Link
 WHERE ( ObjectID1 IN (
     SELECT ObjectID2
     FROM Link
     WHERE ( ObjectID1=10011 ) AND ( LinkObjectTypeID=1024 ) -- PERSON_2_SURVEY
     --WHERE ( ObjectID1=10007 ) AND ( LinkObjectTypeID=1024 ) -- PERSON_2_SURVEY
       AND ( LogicallyDeleted IS NULL )
     )
) AND ( LinkObjectTypeID=24027 ) -- SURVEY_2_RISKPROFILE
  AND ( LogicallyDeleted IS NULL )
 ORDER BY ObjectID1 DESC

-- PersonSurveyIDs
SELECT PersonSurveyID
 FROM PersonSurvey
 ORDER BY PersonSurveyID DESC

SELECT LinkID
 FROM Link
 WHERE ( ObjectID2 IN (
     SELECT ObjectID1
     FROM Link
     WHERE ( LinkObjectTypeID=24027 ) -- SURVEY_2_RISKPROFILE
       AND ( LogicallyDeleted IS NULL )
     )
) AND ( ObjectID1=10011 ) AND ( LinkObjectTypeID=1024 ) -- PERSON_2_SURVEY
  AND ( LogicallyDeleted IS NULL )
 ORDER BY LinkID DESC

-- person surveys
SELECT
     SurveyTitle
     , q.QuestionID, q.QuestionDesc, q.QuestionTypeID
     , qa.QuestionAnswerID, qa.QuestionAnswerDesc, qa.QuestionAnswerScore
     , l.LinkID
 FROM
     Survey s, Question q, QuestionAnswer qa, Link l
 WHERE
     (s.SurveyID = 10514)
     AND (s.SurveyID = q.SurveyID)
     AND (q.QuestionID = qa.QuestionID)
     AND (l.ObjectID1=10001) AND (l.LinkObjectTypeID=1024) AND (s.SurveyID*=l.ObjectID2) AND (l.LogicallyDeleted IS NULL)
 ORDER BY
     q.QuestionID, qa.QuestionAnswerID

SELECT QuestionID, QuestionAnswerID, QuestionAnswerText
 FROM 
     PersonSurveyAnswer psa, PersonSurveyAnswerText psat
 WHERE 
     PersonSurveyID=10641 AND psa.PersonSurveyAnswerID*=psat.PersonSurveyAnswerID


-- partner
SELECT PersonID
 FROM Person
 WHERE ( PersonID IN
    (SELECT ObjectID2
    FROM Link
    WHERE ( ObjectID1 = 10011 ) AND ( LinkObjectTypeID = 3001 )
        AND ( LogicallyDeleted IS NULL )
    )
)

SELECT StateCodeID, PostCode, Suburb
FROM SuburbPostCode WHERE CountryCodeID=13 AND StateCodeID=1-- and postcode is null
ORDER BY StateCodeID, PostCode, Suburb

SELECT StateCodeID, Suburb, PostCode
FROM SuburbPostCode WHERE CountryCodeID=13 AND StateCodeID=1
ORDER BY StateCodeID, Suburb, PostCode

select * from suburbpostcode where countrycodeid=13 and StateCodeID=1 and postcode=2077


SELECT ClientPersonID
FROM Link l, ClientPerson c, Person p
WHERE ( l.ObjectID1 = 10001 ) AND ( l.LinkObjectTypeID = 2003 )
AND ( l.ObjectID2 = c.ClientPersonID ) AND (c.ClientPersonID = p.PersonID)



--
--	BUSINES
--
select * from business --12115

select * from link where linkobjecttypeid=1004

select * from link where objectid1=12116
select * from link where objectid1=12118


--
--	MODEL
--
SELECT sm.*, m.ModelTypeID, sf.StrategyFinancialID, sf.Amount
FROM Model m, StrategyModel sm, StrategyFinancial sf
WHERE sm.StrategyModelID IN (
	SELECT ObjectID2
	FROM Link 
	WHERE ( ObjectID1 = 10011 ) AND ( LinkObjectTypeID = 1029 ) AND ( LogicallyDeleted IS NULL ) -- PERSON_2_MODEL
) 
AND ( m.ModelID = sm.StrategyModelID )
AND ( sm.StrategyModelID *= sf.StrategyModelID )
