if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[vPersonRegular]') and OBJECTPROPERTY(id, N'IsView') = 1)
drop view [dbo].[vPersonRegular]
GO

SET QUOTED_IDENTIFIER ON 
GO
SET ANSI_NULLS ON 
GO

CREATE VIEW dbo.vPersonRegular
AS
SELECT     TOP 100 PERCENT dbo.Link.LinkID, dbo.Link.ObjectID1 AS PersonID, dbo.Regular.RegularID, dbo.Regular.AssetID AS AssociatedAssetID, 
                      dbo.Financial.DateCreated, dbo.Link.LinkObjectTypeID
FROM         dbo.Financial INNER JOIN
                      dbo.Regular ON dbo.Financial.FinancialID = dbo.Regular.RegularID INNER JOIN
                      dbo.Object ON dbo.Financial.FinancialID = dbo.Object.ObjectID INNER JOIN
                      dbo.Link ON dbo.Object.ObjectID = dbo.Link.ObjectID2
WHERE     (dbo.Object.ObjectTypeID = 14) AND (dbo.Link.LinkObjectTypeID = 1020) OR
                      (dbo.Object.ObjectTypeID = 15) AND (dbo.Link.LinkObjectTypeID = 1022) OR
                      (dbo.Link.LinkObjectTypeID = 1014) OR
                      (dbo.Link.LinkObjectTypeID = 1015)
ORDER BY dbo.Link.ObjectID1, dbo.Financial.DateCreated

GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO



SELECT * FROM vPersonRegular
 WHERE (AssociatedAssetID IS NOT NULL)
 AND (DateCreated <= CONVERT(DATETIME, '2002-10-24 00:00:00', 102))


--UPDATE Link SET LogicallyDeleted = 'Y'
--WHERE LinkID IN
--	( SELECT LinkID FROM vPersonRegular
-- 	WHERE (AssociatedAssetID IS NOT NULL)
--	AND (DateCreated <= CONVERT(DATETIME, '2002-10-24 00:00:00', 102)) )
