--SELECT * FROM Params

-- ParamTypeID
-- ParamName  varchar(30)
-- ParamValue varchar(100)
-- ParamDesc  varchar(200)

-- ParamTypeID = 1	Investment Strategies
DELETE FROM Params WHERE ParamTypeID=1;


-- Data for Ultra Conservative Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Ultra Conservative', '1', 'Ultra Conservative Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.1', '3.32',  'Ultra Conservative, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.2', '0.0',   'Ultra Conservative, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.3', '100.0', 'Ultra Conservative, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.4', '90.0',   'Ultra Conservative, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.5', '10.0',   'Ultra Conservative, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.6', NULL,   'Ultra Conservative, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.7', NULL,   'Ultra Conservative, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.8', NULL,   'Ultra Conservative, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.9',  '2.1',   'Ultra Conservative, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.10', '4.6',   'Ultra Conservative, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.11', '2.7',   'Ultra Conservative, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.12', '4.0',   'Ultra Conservative, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.13', '3.3',   'Ultra Conservative, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.14', '3.3',   'Ultra Conservative, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.15', '4.5',   'Ultra Conservative, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.16', '5.0',   'Ultra Conservative, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.17', '5.5',   'Ultra Conservative, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.18', '5.0',   'Ultra Conservative, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.19', '4.5',   'Ultra Conservative, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.20', '5.5',   'Ultra Conservative, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '1.21', '5.3',   'Ultra Conservative, Year 7' );


-- Data for Conservative Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Conservative', '2', 'Conservative Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.1', '3.56',  'Conservative, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.2', '1.67',  'Conservative, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.3', '70.3',  'Conservative, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.4', '45.0',   'Conservative, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.5', '25.0',   'Conservative, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.6', '7.0',   'Conservative, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.7', '15.0',   'Conservative, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.8', '8.0',   'Conservative, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.9', '-2.0',   'Conservative, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.10', '12.5',   'Conservative, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.11', '1.6',   'Conservative, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.12', '8.9',   'Conservative, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.13', '5.2',   'Conservative, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.14', '5.2',   'Conservative, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.15', '-2.0',   'Conservative, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.16', '5.0',   'Conservative, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.17', '12.0',   'Conservative, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.18', '11.0',   'Conservative, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.19', '6.0',   'Conservative, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.20', '11.0',   'Conservative, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '2.21', '8.0',   'Conservative, Year 7' );


-- Data for Conservative Balanced Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Conservative Balanced', '3', 'Conservative Balanced Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.1', '3.37',   'Conservative Balanced, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.2', '3.02',   'Conservative Balanced, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.3', '49.8',   'Conservative Balanced, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.4', '30.0',   'Conservative Balanced, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.5', '20.0',   'Conservative Balanced, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.6', '8.0',   'Conservative Balanced, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.7', '27.0',   'Conservative Balanced, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.8', '15.0',   'Conservative Balanced, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.9', '-5.0',   'Conservative Balanced, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.10', '17.7',   'Conservative Balanced, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.11', '0.7',   'Conservative Balanced, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.12', '12.1',   'Conservative Balanced, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.13', '6.4',   'Conservative Balanced, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.14', '6.4',   'Conservative Balanced, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.15', '-4.0',   'Conservative Balanced, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.16', '8.0',   'Conservative Balanced, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.17', '15.0',   'Conservative Balanced, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.18', '13.0',   'Conservative Balanced, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.19', '1.0',   'Conservative Balanced, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.20', '16.0',   'Conservative Balanced, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '3.21', '13.0',   'Conservative Balanced, Year 7' );


-- Data for Balanced Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Balanced', '4', 'Balanced Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.1', '3.19',   'Balanced, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.2', '4.34',   'Balanced, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.3', '30.2',   'Balanced, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.4', '14.0',   'Balanced, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.5', '16.0',   'Balanced, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.6', '9.0',   'Balanced, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.7', '38.0',   'Balanced, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.8', '23.0',   'Balanced, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.9', '-8.0',   'Balanced, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.10', '23.0',   'Balanced, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.11', '-0.2',   'Balanced, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.12', '15.3',   'Balanced, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.13', '7.5',   'Balanced, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.14', '7.5',   'Balanced, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.15', '-6.0',   'Balanced, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.16', '10.0',   'Balanced, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.17', '16.2',   'Balanced, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.18', '21.0',   'Balanced, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.19', '-1.0',   'Balanced, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.20', '15.0',   'Balanced, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '4.21', '16.0',   'Balanced, Year 7' );


-- Data for Growth Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Growth', '5', 'Growth Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.1', '3.06',   'Growth, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.2', '5.07',   'Growth, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.3', '19.7',   'Growth, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.4', '8.0',   'Growth, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.5', '12.0',   'Growth, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.6', '9.0',   'Growth, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.7', '42.0',   'Growth, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.8', '29.0',   'Growth, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.9', '-9.4',   'Growth, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.10', '25.7',   'Growth, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.11', '-0.6',   'Growth, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.12', '16.9',   'Growth, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.13', '8.1',   'Growth, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.14', '8.1',   'Growth, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.15', '-6.0',   'Growth, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.16', '10.0',   'Growth, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.17', '18.0',   'Growth, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.18', '22.0',   'Growth, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.19', '-1.0',   'Growth, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.20', '18.0',   'Growth, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '5.21', '16.0',   'Growth, Year 7' );


-- Data for Strong Growth Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Strong Growth', '6', 'Strong Growth Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.1', '2.56',   'Strong Growth, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.2', '6.65',   'Strong Growth, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.3', '9.7',    'Strong Growth, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.4', '4.0',   'Strong Growth, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.5', '6.0',   'Strong Growth, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.6', '9.0',   'Strong Growth, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.7', '45.0',   'Strong Growth, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.8', '36.0',   'Strong Growth, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.9', '-12.7',   'Strong Growth, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.10', '31.1',   'Strong Growth, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.11', '-1.7',   'Strong Growth, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.12', '20.2',   'Strong Growth, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.13', '9.2',   'Strong Growth, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.14', '9.2',   'Strong Growth, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.15', '-12.0',   'Strong Growth, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.16', '25.0',   'Strong Growth, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.17', '15.0',   'Strong Growth, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.18', '28.0',   'Strong Growth, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.19', '-8.0',   'Strong Growth, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.20', '20.8',   'Strong Growth, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '6.21', '18.0',   'Strong Growth, Year 7' );


-- Data for Ultra Growth Investment Strategy
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, 'Ultra Growth', '7', 'Ultra Growth Investment Strategy' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.1', '1.92',   'Ultra Growth, Income Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.2', '8.31',   'Ultra Growth, Growth Rate' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.3', '3.0', 'Ultra Growth, Defensive Rate' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.4', '3.0',   'Ultra Growth, Cash' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.5', NULL,   'Ultra Growth, Fixed Interest' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.6', NULL,   'Ultra Growth, Listed Property' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.7', '52.0',   'Ultra Growth, Aust. Shares' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.8', '45.0',   'Ultra Growth, Intnl. Shares' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.9', '-17.2',   'Ultra Growth, In any 1 year (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.10', '37.6',   'Ultra Growth, In any 1 year (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.11', '-3.5',   'Ultra Growth, In any 2 out of 3 years (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.12', '23.9',   'Ultra Growth, In any 2 out of 3 years (max)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.13', '10.2',   'Ultra Growth, Long Term Return p.a. (min)' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.14', '10.2',   'Ultra Growth, Long Term Return p.a. (max)' );

INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.15', '-14.0',   'Ultra Growth, Year 1' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.16', '25.0',   'Ultra Growth, Year 2' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.17', '20.0',   'Ultra Growth, Year 3' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.18', '32.0',   'Ultra Growth, Year 4' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.19', '-9.0',   'Ultra Growth, Year 5' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.20', '18.0',   'Ultra Growth, Year 6' );
INSERT INTO Params (ParamTypeID,ParamName,ParamValue,ParamDesc)
 VALUES ( 1, '7.21', '22.5',   'Ultra Growth, Year 7' );




UPDATE Financial SET StrategyGroupID = NULL;

--select * from LinkObjectType
--select * from StrategyGroup
DELETE FROM StrategyGroup;
DELETE FROM Link WHERE LinkObjectTypeID = 1032;
DELETE FROM Object WHERE ObjectTypeID = 1032;
DELETE FROM Object WHERE ObjectTypeID = 32;


--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.51', 'FID.01.50');
