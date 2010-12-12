--
-- add new column
--
ALTER TABLE LicenseeRecord ADD FiducianReference VARCHAR(128)

--------------------------------------------------------------
-- add/update financial types for superannuation
--------------------------------------------------------------
if exists (select * from FinancialType WHERE FinancialTypeID=2001)
 UPDATE FinancialType SET FinancialTypeDesc='Listed Shares', ObjectTypeID=13 WHERE (FinancialTypeID=2001)
else
 INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (  2001, 'Listed Shares'     , 13 );

if exists (select * from FinancialType WHERE FinancialTypeID=2002)
 UPDATE FinancialType SET FinancialTypeDesc='Listed Unit Trust', ObjectTypeID=13 WHERE (FinancialTypeID=2002)
else
 INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (  2002, 'Listed Unit Trust' , 13 );

if exists (select * from FinancialType WHERE FinancialTypeID=2003)
 UPDATE FinancialType SET FinancialTypeDesc='Other', ObjectTypeID=13 WHERE (FinancialTypeID=2003)
else
 INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (  2003, 'Other'             , 13 );

if exists (select * from FinancialType WHERE FinancialTypeID=2004)
 UPDATE FinancialType SET FinancialTypeDesc='Shares Australian', ObjectTypeID=13 WHERE (FinancialTypeID=2004)
else
 INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (  2004, 'Shares Australian' , 13 );

if exists (select * from FinancialType WHERE FinancialTypeID=2005)
 UPDATE FinancialType SET FinancialTypeDesc='Listed Unit Trusts', ObjectTypeID=13 WHERE (FinancialTypeID=2005)
else
 INSERT INTO FinancialType (FinancialTypeID, FinancialTypeDesc, ObjectTypeID) VALUES (  2005, 'Listed Unit Trusts', 13 );

--------------------------------------------------------------
-- BUG-FIX: 694
--------------------------------------------------------------
UPDATE AdviserTypeCode SET AdviserTypeCodeDesc = 'Smart Franchise' WHERE AdviserTypeCodeID = 1;

-- use OccupationCode table istead
if exists (select * from dbo.sysobjects where id = object_id(N'ContactOccupationCode') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
 drop table ContactOccupationCode;

-- put new institutions
UPDATE Financial SET InstitutionID = NULL;

DELETE FROM Institution;

ALTER TABLE Institution ALTER COLUMN InstitutionName	varchar(150) NOT NULL;
				
INSERT Institution (InstitutionID, InstitutionName) VALUES (	1	, 'Adelaide Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	2	, 'AMP Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	3	, 'Australia and New Zealand Banking Group Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	4	, 'Bank of Queensland Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	5	, 'Bendigo Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	6	, 'Commonwealth Bank of Australia' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	7	, 'Commonwealth Development Bank of Australia Limited (a subsidiary of Commonwealth Bank of Australia)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	8	, 'Elders Rural Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	9	, 'Macquarie Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	10	, 'Members Equity Pty Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	11	, 'National Australia Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	12	, 'St. George Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	13	, 'Suncorp-Metway Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	14	, 'Westpac Banking Corporation' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	15	, 'Arab Bank Australia Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	16	, 'Bank of Cyprus Australia Pty Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	17	, 'Bank of Tokyo - Mitsubishi (Australia) Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	18	, 'BankWest (the trading name of Bank of Western Australia Limited, a foreign subsidiary bank following its sale to Bank of Scotland in December 1995)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	19	, 'Citibank Pty Limited (a subsidiary of Citibank N.A.)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	20	, 'HSBC Bank Australia Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	21	, 'IBJ Australia Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	22	, 'ING Bank (Australia) Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	23	, 'Laiki Bank (Australia) Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	24	, 'N M Rothschild & Sons (Australia) Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	25	, 'Primary Industry Bank of Australia Limited (a subsidiary of Rabobank Nederland from October 1994)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	26	, 'Standard Chartered Grindlays Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	27	, 'ABN AMRO Bank N.V.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	28	, 'Bank of America, National Association' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	29	, 'Bank of China (subject to depositor protection provisions of the Banking Act 1959)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	30	, 'Bank One, National Association' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	31	, 'Barclays Capital (the trading name of Barclays Bank plc)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	32	, 'BNP Paribas' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	33	, 'Citibank N.A.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	34	, 'Credit Suisse First Boston' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	35	, 'Deutsche Bank AG' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	36	, 'Dresdner Bank AG' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	37	, 'HSBC Bank plc' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	38	, 'ING Bank NV' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	39	, 'JPMorgan Chase Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	40	, 'Mizuho Corporate Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	41	, 'Oversea-Chinese Banking Corporation Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	42	, 'Rabobank Nederland (the trading name of Co-operative Central Raiffeisen-Boerenleenbank B.A.)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	43	, 'Royal Bank of Canada' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	44	, 'Standard Chartered Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	45	, 'State Street Bank and Trust Company' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	46	, 'The International Commercial Bank of China' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	47	, 'The Toronto-Dominion Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	48	, 'Taiwan Business Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	49	, 'United Overseas Bank Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	50	, 'WestLB (the trading name of Westdeutsche Landesbank Girozentrale)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	51	, 'ABS Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	52	, 'B & E Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	53	, 'Greater Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	54	, 'Heritage Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	55	, 'Home Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	56	, 'HSBC Building Society (Australia)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	57	, 'Hume Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	58	, 'IMB Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	59	, 'Lifeplan Australia Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	60	, 'Mackay Permanent Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	61	, 'Maitland Mutual Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	62	, 'Newcastle Permanent Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	63	, 'Pioneer Permanent Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	64	, 'The Rock Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	65	, 'Wide Bay Capricorn Building Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	66	, 'Albury Murray Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	67	, 'Amcor Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	68	, 'AMP Employees'' & Agents Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	69	, 'Australian Central Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	70	, 'Australian Defence Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	71	, 'Australian National Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	72	, 'AWA Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	73	, 'Bananacoast Community Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	74	, 'Bankstown City Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	75	, 'Bemboka Community Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	76	, 'Berrima District Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	77	, 'BHP Group Employees Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	78	, 'Big River Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	79	, 'Blue Mountains and Riverlands Community Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	80	, 'BP Employees'' Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	81	, 'Broadway Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	82	, 'C.D.H. Staff Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	83	, 'Calare Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	84	, 'Capital Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	85	, 'Capricornia Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	86	, 'Carboy (SA) Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	87	, 'Central Murray Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	88	, 'Central West Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	89	, 'Circle Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	90	, 'City Coast Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	91	, 'Coastline Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	92	, 'Collie Miners Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	93	, 'Combined Australian Petroleum Employees'' Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	94	, 'Community First Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	95	, 'Companion Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	96	, 'Comtax Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	97	, 'Connect Credit Union of Tasmania Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	98	, 'Country First Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	99	, 'CPS Credit Union (SA) Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	100	, 'CPS Credit Union Co-operative (ACT) Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	101	, 'Credit Union Australia Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	102	, 'Credit Union Incitec Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	103	, 'Croatian Community Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	104	, 'CSR Employees'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	105	, 'Dairy Farmers Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	106	, 'Defence Force Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	107	, 'Discovery Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	108	, 'Dnister Ukrainian Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	109	, 'Education Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	110	, 'ELCOM Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	111	, 'Electricity Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	112	, 'Encompass Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	113	, 'Ericsson Employees Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	114	, 'Esso Employees'' Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	115	, 'Eurobodalla Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	116	, 'Family First Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	117	, 'Fire Brigades Employees'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	118	, 'Fire Service Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	119	, 'Firefighters Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	120	, 'First Gas Employee''s Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	121	, 'First Pacific Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	122	, 'Fitzroy & Carlton Community Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	123	, 'Flying Horse Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	124	, 'Ford Co-operative Credit Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	125	, 'Gateway Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	126	, 'Geelong & District Credit Co-operative Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	127	, 'GMH (Employees) Q.W.L. Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	128	, 'Gold Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	129	, 'Goldfields Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	130	, 'Gosford City Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	131	, 'Goulburn Murray Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	132	, 'H.M.C. Staff Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	133	, 'Health Services Credit Union Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	134	, 'Herald Austral Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	135	, 'Heritage Isle Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	136	, 'Hibernian Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	137	, 'Holiday Coast Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	138	, 'Horizon Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	139	, 'Hoverla Ukrainian Credit Co-operative Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	140	, 'Hunter United Employees'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	141	, 'IMG Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	142	, 'Intech Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	143	, 'IOOF South Australia Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	144	, 'Island State Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	145	, 'Karpaty Ukrainian Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	146	, 'La Trobe Country Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	147	, 'La Trobe University Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	148	, 'Laboratories Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	149	, 'Latvian Australian Credit Co-operative Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	150	, 'Lithuanian Co-operative Society (Talka) Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	151	, 'Lysaght Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	152	, 'M.S.B. Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	153	, 'Macarthur Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	154	, 'Macaulay Community Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	155	, 'Macquarie Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	156	, 'Maitland City Council Employees'' Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	157	, 'Maleny and District Community Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	158	, 'Manly Warringah Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	159	, 'Maritime Workers of Australia Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	160	, 'Maroondah Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	161	, 'Media Credit Union Queensland Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	162	, 'Melbourne Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	163	, 'Melbourne University Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	164	, 'Memberfirst Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	165	, 'Members Australia Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	166	, 'Metropolitan Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	167	, 'Money Wise Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	168	, 'Muslim Community Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	169	, 'N.R.M.A. Employees'' Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	170	, 'NACOS Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	171	, 'New England Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	172	, 'Newcastle Bus Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	173	, 'Newcastle City Council Employees'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	174	, 'Newcom Colliery Employees'' Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	175	, 'North East Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	176	, 'North West Country Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	177	, 'Northern Districts Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	178	, 'Northern Inland Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	179	, 'Nova Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	180	, 'NSW Teachers Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	181	, 'Old Gold Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	182	, 'Orana Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	183	, 'Orange Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	184	, 'Parkes District Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	185	, 'Peel Valley Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	186	, 'Phoenix (NSW) Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	187	, 'Plenty Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	188	, 'Police & Nurses Credit Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	189	, 'Police Association Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	190	, 'Police Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	191	, 'Polish Community Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	192	, 'Post-Tel Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	193	, 'Power Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	194	, 'Powerstate Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	195	, 'Prospect Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	196	, 'Pulse Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	197	, 'Punchbowl Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	198	, 'Qantas Staff Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	199	, 'Queensland Community Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	200	, 'Queensland Country Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	201	, 'Queensland Police Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	202	, 'Queensland Professional Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	203	, 'Queensland Teachers'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	204	, 'Queenslanders Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	205	, 'RACV Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	206	, 'Railways Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	207	, 'Randwick Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	208	, 'Reliance Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	209	, 'Resources Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	210	, 'RTA Staff Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	211	, 'Satisfac Direct Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	212	, 'Savings and Loans Credit Union (SA) Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	213	, 'Security Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	214	, 'Select Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	215	, 'Service One Credit Union Ltd ' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	216	, 'SGE Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	217	, 'Shell Employees'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	218	, 'Shoalhaven Paper Mill Employee''s Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	219	, 'Softwoods Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	220	, 'South East Community Credit Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	221	, 'South West Slopes Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	222	, 'Southern Cross Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	223	, 'South-West Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	224	, 'Spicer Employees Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	225	, 'St Mary''s Swan Hill Co-operative Credit Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	226	, 'St Patrick''s Mentone Co-operative Credit Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	227	, 'St Philip''s Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	228	, 'StateHealth Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	229	, 'Statewest Credit Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	230	, 'Sutherland Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	231	, 'Sutherland Shire Council Employees'' Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	232	, 'Sydney Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	233	, 'TAB Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	234	, 'Tartan Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	235	, 'Telstra Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	236	, 'The Breweries Union Credit Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	237	, 'The Broken Hill Community Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	238	, 'The Gympie Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	239	, 'The Illawarra Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	240	, 'The Manly Vale Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	241	, 'The Police Department Employees'' Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	242	, 'The Scallop Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	243	, 'The Summerland Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	244	, 'The TAFE and Community Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	245	, 'The University Credit Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	246	, 'Traditional Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	247	, 'TransComm Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	248	, 'Transport Industries Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	249	, 'Uni Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	250	, 'Unicom Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	251	, 'United Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	252	, 'Upper Hunter Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	253	, 'Victoria Teachers Credit Union Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	254	, 'Wagga Mutual Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	255	, 'Warwick Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	256	, 'Waverley Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	257	, 'WAW Credit Union Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	258	, 'Westax Credit Society Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	259	, 'Western City Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	260	, 'Woolworths/Safeway Employees'' Credit Co-operative Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	261	, 'Wyong Council Credit Union Ltd' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	262	, 'Yennora Credit Union Ltd.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	263	, 'Australian Settlements Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	264	, 'Credit Union Services Corporation (Australia) Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	265	, 'Creditlink Services Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	266	, 'Creditlink Treasury Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	267	, 'Cornerstone Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	268	, 'Countrywide Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	269	, 'Geelong Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	270	, 'Household Building Society Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	271	, 'Pyramid Building Society Limited (Tas)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	272	, 'Pyramid Building Society Limited (Vic)' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	273	, 'Allied Banking Corporation' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	274	, 'Banca Intesa Banca Commerciale Italiana S.p.A.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	275	, 'Banca Nazionale del Lavoro SpA' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	276	, 'Banco Santander Central Hispano SA' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	277	, 'Bank Leumi Le-Israel BM' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	278	, 'Bank of New York' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	279	, 'Bank of New York-Inter Maritime Bank, Geneva' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	280	, 'Bank of Valletta p.l.c' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	281	, 'Banque Transatlantique S.A.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	282	, 'Bayerische Hypo- Und Vereinsbank Aktiengesellschaft' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	283	, 'Credit Agricole Lazard Financial Products Bank' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	284	, 'Credit Lyonnais' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	285	, 'Fortis Bank N.V./S.A.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	286	, 'HSBC Bank Malta p.l.c.' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	287	, 'Industrial and Commercial Bank of China' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	288	, 'Japan Bank for International Cooperation' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	289	, 'Natexis Banques Populaires' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	290	, 'National Bank of Greece SA' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	291	, 'Standard Bank London Limited' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	292	, 'State Bank of India' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	293	, 'UBS AG' );
INSERT Institution (InstitutionID, InstitutionName) VALUES (	294	, 'Wachovia Bank, National Association' );


--------------------------------------------------------------
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FID.01.23', 'FID.01.22');