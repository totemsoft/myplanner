--public static final int FixedObjectTypeID::GLOBAL_PLAN_TEMPLATE = 26;
UPDATE PlanType SET PlanTypeDesc='Global Plan Template (All Users)' WHERE PlanTypeID=1;
INSERT INTO PlanType (PlanTypeID, PlanTypeDesc) VALUES (2, 'User Plan Template');

--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.49', 'FID.01.48');

