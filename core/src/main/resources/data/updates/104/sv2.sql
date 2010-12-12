DELETE FROM FinancialMapSV2;
GO

DELETE FROM FinancialCode WHERE FinancialTypeID > 1000;
GO

DELETE FROM FinancialType WHERE FinancialTypeID > 1000;
GO



INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1001, 'Debentures (In Liquidation)', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Debentures (In Liquidation)', 1001 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '232', 1001, 1 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1002, 'Financial Institutions', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Financial Institutions', 1002 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '233', 1002, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'FI1', 'Commonwealth Govt Treasury', 1002 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2330000', 1002, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'FI7', 'NSW Treasury Corporation', 1002 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2330010', 1002, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'FI10', 'QLD Treasury Corporation', 1002 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2330020', 1002, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'FI13', 'VIC Treasury Corporation', 1002 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2330030', 1002, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'FI15', 'WA Treasury Corporation', 1002 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2330040', 1002, 6 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1003, 'Fixed Deposits', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Fixed Deposits', 1003 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '234', 1003, 1 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1004, 'Money Market Deposits', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Money Market Deposits', 1004 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '235', 1004, 1 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1005, 'Shares (Australian)', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Shares (Australian)', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '240', 1005, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'AAP', '_AAPT Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400000', 1005, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'AAA', '_Acacia Resources', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400010', 1005, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'ADA', 'Adacel Technologies Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400020', 1005, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'ADB', 'Adelaide Bank Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400030', 1005, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'ABA', '_Advance Bank Australia Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400040', 1005, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'APF', '_Advance Property Fund', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400050', 1005, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'ALN', 'Alinta Gas Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400060', 1005, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'AHD', 'Amalgamated Holdings', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400070', 1005, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'AMM', 'Amcom Telecommunications Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400080', 1005, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'AMC', 'Amcor Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400090', 1005, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, 'AYO', 'Amity Oil N.L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400100', 1005, 12 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 13, 'AMQHB', '_AMP Group Finance - Income Se', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400110', 1005, 13 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 14, 'AMQHA', 'AMP Group Finance - Income Sec', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400120', 1005, 14 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 15, 'AMP', 'AMP Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400130', 1005, 15 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 16, 'AMPXX', 'AMP Reinsurance Notes - 30/6/0', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400140', 1005, 16 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 17, 'AMPXX', '_AMP Reinsurance Notes - 30/6/', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400140', 1005, 17 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 18, 'ANP', 'Antisense Therapeutics Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400141', 1005, 18 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 19, 'ANPO', 'Antisense Therapeutics Ltd Opt', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400142', 1005, 19 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 20, 'ANZ', 'ANZ Banking Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400150', 1005, 20 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 21, 'APX', 'Apollo Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400160', 1005, 21 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 22, 'ARQ', 'ARC Energy', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400170', 1005, 22 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 23, 'ARG', 'Argo Investments Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400180', 1005, 23 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 24, 'ADM', 'Auridiam Consolidated N.L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400190', 1005, 24 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 25, 'ANE', 'Auspine Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400200', 1005, 25 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 26, 'AFIG', 'Aust Found Invest Co 9% Unsec', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400220', 1005, 26 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 27, 'ATO', '_Austin Oil NL', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400230', 1005, 27 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 28, 'ATE', 'Australasian Gold Mines N.L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400240', 1005, 28 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 29, 'AFI', 'Australian Foundation Investme', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400250', 1005, 29 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 30, 'AGL', 'Australian Gas Light Company', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400260', 1005, 30 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 31, 'ANI', '_Australian National Industrie', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400270', 1005, 31 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 32, 'ASX', 'Australian Stock Exchange Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400280', 1005, 32 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 33, 'AVR', 'Avatar Industries', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400290', 1005, 33 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 34, 'AXA', 'AXA Asia Pacific Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400300', 1005, 34 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 35, 'AXN', 'Axon Instruments Inc (CUFS US', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400310', 1005, 35 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 36, 'AXNO', 'Axon Instuments Inc Option', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400320', 1005, 36 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 37, 'BML', '_Bank of Melbourne Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400330', 1005, 37 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 38, 'BWA', 'Bank West Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400340', 1005, 38 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 39, 'BEN', 'Bendigo Bank Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400350', 1005, 39 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 40, 'BPO', 'Bioprospect Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400360', 1005, 40 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 41, 'BPOO', 'Bioprospect Ltd - Options exp.', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400361', 1005, 41 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 42, 'BTA', 'Biota Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400370', 1005, 42 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 43, 'BLD', 'Boral Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400380', 1005, 43 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 44, 'BOR', '_Boral Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400390', 1005, 44 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 45, 'BIL', 'Brambles Industries Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400400', 1005, 45 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 46, 'BRZ', 'Brazin Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400410', 1005, 46 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 47, 'BRL', 'BRL Hardy Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400420', 1005, 47 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 48, 'BHP', 'BHP Billiton Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400430', 1005, 48 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 49, 'BTE', 'BT Australia Equity Management', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400440', 1005, 49 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 50, 'BTM', 'BT Resources Management Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400450', 1005, 50 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 51, 'BKS', 'Burdekin Pacific Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400460', 1005, 51 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 52, 'BIR', 'Burswood Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400470', 1005, 52 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 53, 'CWO', '_Cable & Wireless Optus Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400480', 1005, 53 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 54, 'CTX', 'Caltex Australia Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400490', 1005, 54 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 55, 'CNG', 'Central Norseman Gold Corp', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400500', 1005, 55 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 56, 'CIR', 'Circadian Technologies Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400510', 1005, 56 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 57, 'CCL', 'Coca-Cola Amatil Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400520', 1005, 57 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 58, 'COH', 'Cochlear Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400530', 1005, 58 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 59, 'CML', 'Coles Myer Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400540', 1005, 59 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 60, 'CMLC', 'Coles Myer Ltd Discount Card', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400541', 1005, 60 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 61, 'CMLPA', 'Coles Myer 6.5% Non-Redeem con', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400542', 1005, 61 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 62, 'CGH', '_Colonial Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400550', 1005, 62 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 63, 'CMC', 'Comalco Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400560', 1005, 63 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 64, 'CBA', 'Commonwealth Bank of Australia', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400570', 1005, 64 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 65, 'CMS', 'Commsecure Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400580', 1005, 65 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 66, 'CPU', 'Computershare Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400590', 1005, 66 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 67, 'CXR', 'Coplex Resources NL', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400600', 1005, 67 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 68, 'CYG', 'Coventry Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400605', 1005, 68 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 69, 'CEW', 'Cranswick Premium Wines Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400610', 1005, 69 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 70, 'CRO', '_Crown Casino', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400620', 1005, 70 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 71, 'CSL', 'CSL Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400630', 1005, 71 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 72, 'CSR', 'CSR Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400640', 1005, 72 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 73, 'CLR', 'Cultus Petroleum', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400650', 1005, 73 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 74, 'DVT', 'Davnet Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400660', 1005, 74 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 75, 'DEV', 'Devex', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400670', 1005, 75 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 76, 'DNI', 'Digital Now Inc (CUFS US Prohi', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400680', 1005, 76 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 77, 'DJW', 'Djerriwarrah Investments', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400690', 1005, 77 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 78, 'DJWG', 'Djerriwarrh Investments 7.5% U', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400700', 1005, 78 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 79, 'DRA', 'Dragon Mining NL', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400710', 1005, 79 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 80, 'DOW', 'Downer EDI Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400711', 1005, 80 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 81, 'ECP', 'Ecorp Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400720', 1005, 81 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 82, 'ELT', 'Eltin Roche Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400730', 1005, 82 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 83, 'EML', '_Email Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400740', 1005, 83 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 84, 'ENE', 'Energy Developments Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400750', 1005, 84 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 85, 'ERG', 'ERG Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400760', 1005, 85 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 86, 'EDI', 'Evans Deakin Industries Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400770', 1005, 86 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 87, 'ETW', 'Evans & Tate Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400780', 1005, 87 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 88, 'ETWPA', 'Evans Tate Ltd Converting Pref', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400790', 1005, 88 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 89, 'FHF', 'FH Faulding Company Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400800', 1005, 89 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 90, 'FPS', 'Fiducian Portfolio Services Lt', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400810', 1005, 90 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 91, 'FOA', 'Foodland Associated Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400820', 1005, 91 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 92, 'FBG', '_Fosters Brewing Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400830', 1005, 92 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 93, 'FGL', 'Fosters Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400831', 1005, 93 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 94, 'FCL', 'Futuris Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400840', 1005, 94 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 95, 'FCLGA', 'Futuris Corporation Ltd 7% Con', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400850', 1005, 95 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 96, 'GIO', '_GIO Australia Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400860', 1005, 96 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 97, 'GMF', 'Goodman Fielder Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400870', 1005, 97 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 98, 'HAN', 'Hanson PLC', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400880', 1005, 98 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 99, 'HRD', 'Harvestroad Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400890', 1005, 99 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 100, 'HVN', 'Harvey Norman', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400900', 1005, 100 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 101, 'HVNR', 'Harvey Norman Rights Issue', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400905', 1005, 101 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 102, 'HWE', 'Henry Walker Eltin Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400910', 1005, 102 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 103, 'HIH', 'HIH Insurance Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400920', 1005, 103 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 104, 'HTA', 'Hutchinson Telecommunications', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400930', 1005, 104 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 105, 'IAM', 'IAMA Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400940', 1005, 105 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 106, 'ICT', 'Incitec Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400950', 1005, 106 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 107, 'INX', 'Inovax Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400960', 1005, 107 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 108, 'IWL', 'Investorweb Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400970', 1005, 108 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 109, 'ISC', 'ISIS Communications Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400980', 1005, 109 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 110, 'IXL', 'IXLA Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2400990', 1005, 110 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 111, 'JUP', 'Jupiters Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401000', 1005, 111 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 112, 'LEI', 'Leighton Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401010', 1005, 112 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 113, 'LLCBN', '_Lend Lease Corp. Ltd. (Bonus)', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401020', 1005, 113 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 114, 'LLC', 'Lend Lease Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401030', 1005, 114 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 115, 'LHG', 'Lihir Gold Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401040', 1005, 115 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 116, 'LNN', 'Lion Nathan Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401050', 1005, 116 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 117, 'LOK', 'Looksmart Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401060', 1005, 117 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 118, 'LTC', 'L-Tel Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401070', 1005, 118 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 119, 'MBL', 'Macquarie Bank Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401080', 1005, 119 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 120, 'MMC', 'Macraes Mining', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401090', 1005, 120 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 121, 'MAY', 'Mayne Nickless Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401100', 1005, 121 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 122, 'MBP', 'Metabolic Pharmaceuticals Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401110', 1005, 122 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 123, 'MLT', 'Milton', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401120', 1005, 123 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 124, 'MIM', 'MIM Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401130', 1005, 124 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 125, 'MTB', 'Mount Burgess Gold Mining Co', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401140', 1005, 125 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 126, 'MUR', 'Murchison United NL', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401150', 1005, 126 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 127, 'NABHA', 'National Australia Bank Income', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401160', 1005, 127 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 128, 'NAB', 'National Australia Bank Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401170', 1005, 128 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 129, 'NCPXX', '_NCP Warrant (Barclays 2500 29', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401180', 1005, 129 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 130, 'NZO', 'New Zealand Oil & Gas Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401190', 1005, 130 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 131, 'NCM', 'Newcrest Mining Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401200', 1005, 131 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 132, 'NCPPA', 'News Corp Convert Preferences', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401210', 1005, 132 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 133, 'NCPDP', 'News Corp Non Voting', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401220', 1005, 133 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 134, 'NCP', 'News Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401230', 1005, 134 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 135, 'NDY', 'Normandy Mining Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401240', 1005, 135 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 136, 'NBH', 'North Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401250', 1005, 136 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 137, 'IAG', 'NRMA Insurance Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401260', 1005, 137 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 138, 'OSH', 'Oil Search Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401270', 1005, 138 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 139, 'OMH', 'OM Holdings', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401280', 1005, 139 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 140, 'OMO', 'Omega Oil', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401290', 1005, 140 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 141, 'OHL', 'Omnitech Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401300', 1005, 141 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 142, 'ONE', 'One.Tel Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401310', 1005, 142 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 143, 'OST', 'Onesteel Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401320', 1005, 143 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 144, 'OPS', 'OPSM Protector Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401330', 1005, 144 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 145, 'ORI', 'Orica Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401340', 1005, 145 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 146, 'ORG', 'Origin Energy Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401350', 1005, 146 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 147, 'OML', 'Orogen Minerals', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401360', 1005, 147 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 148, 'PDP', 'Pacific Dunlop Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401370', 1005, 148 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 149, 'PHY', 'Pacific Hydro Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401371', 1005, 149 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 150, 'PNL', 'Pacific Forest', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401380', 1005, 150 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 151, 'PBB', 'Pacifica Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401390', 1005, 151 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 152, 'PAH', 'Pahth Telecommunications Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401400', 1005, 152 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 153, 'PPP', 'Pan Pacific Petroleum', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401410', 1005, 153 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 154, 'PPX', 'Paperlinx Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401420', 1005, 154 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 155, 'PAS', 'Pasminco Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401430', 1005, 155 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 156, 'PTZ', 'Petroz NL', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401440', 1005, 156 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 157, 'PNI', 'Pioneer International Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401450', 1005, 157 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 158, 'PRL', 'Pirelli Cables Australia Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401460', 1005, 158 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 159, 'PMC', 'Platinum Capital', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401470', 1005, 159 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 160, 'PLX', 'Plexus International Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401480', 1005, 160 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 161, 'PLU', 'Plutonic Resources Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401490', 1005, 161 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 162, 'PMP', 'PMP Communications Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401500', 1005, 162 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 163, 'PWT', 'Powertel Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401510', 1005, 163 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 164, 'PMA', 'Precious Metals Australia Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401520', 1005, 164 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 165, 'PBL', 'Publishing & Broadcasting Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401530', 1005, 165 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 166, 'QAN', 'Qantas Airways Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401540', 1005, 166 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 167, 'QBE', 'QBE Insurance Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401550', 1005, 167 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 168, 'QRL', 'Queensland Coal Trust', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401560', 1005, 168 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 169, 'QNI', 'Queensland Nickel', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401570', 1005, 169 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 170, 'RHC', 'Ramsay Health Care Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401580', 1005, 170 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 171, 'RAC', '_Reinsurance Australia Corp', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401590', 1005, 171 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 172, 'RSG', 'Resolute Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401600', 1005, 172 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 173, 'RSGPA', 'Resolute Ltd - Preference Shar', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401610', 1005, 173 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 174, 'RIC', 'Ridley Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401620', 1005, 174 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 175, 'RIO', 'Rio Tinto Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401630', 1005, 175 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 176, 'ROC', 'Roc Oil Company Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401640', 1005, 176 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 177, 'STO', 'Santos Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401650', 1005, 177 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 178, 'SAS', 'Sausage Software Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401660', 1005, 178 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 179, 'SGBS', '_St George Sell Back Rights', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401665', 1005, 179 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 180, 'SNX', 'Securenet Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401670', 1005, 180 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 181, 'SGI', '_SGIO Insurance Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401680', 1005, 181 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 182, 'SMS', 'Simsmetal Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401690', 1005, 182 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 183, 'SGT', 'Singapore Telecommunications L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401695', 1005, 183 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 184, 'SMA', 'Smarttrans Holdings Ltd.', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401700', 1005, 184 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 185, 'SWC', 'Smartworld Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401710', 1005, 185 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 186, 'SMI', '_Smith (Howard) Industries', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401720', 1005, 186 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 187, 'SSX', 'Smorgan Steel Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401730', 1005, 187 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 188, 'SSXPA', 'Smorgon Steel Group Conv Prefe', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401740', 1005, 188 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 189, 'SGW', 'Sons of Gwalia N L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401750', 1005, 189 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 190, 'SRP', 'Southcorp Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401760', 1005, 190 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 191, 'SGB', 'St George Bank Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401770', 1005, 191 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 192, 'SGBPA', 'St George Bank Ltd (Converting', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401780', 1005, 192 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 193, 'SFM', 'St. Francis Mining N L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401790', 1005, 193 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 194, 'SMPG', 'State of Qld Suncorp-Metway Ex', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401800', 1005, 194 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 195, 'SRL', 'Straits Resources', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401810', 1005, 195 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 196, 'SCE', 'Sun Consolidated Enterprises L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401820', 1005, 196 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 197, 'SMPGA', 'SUNCORP METWAY 8% EXCHANGING N', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401830', 1005, 197 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 198, 'SME', '_Suncorp-Metway Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401840', 1005, 198 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 199, 'SYD', '_Sydney Casino Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401850', 1005, 199 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 200, 'SDT', '_Sydney Oil', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401860', 1005, 200 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 201, 'TAB', 'TAB Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401870', 1005, 201 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 202, 'TAH', 'Tabcorp Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401880', 1005, 202 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 203, 'TAP', 'TAP Oil', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401890', 1005, 203 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 204, 'TSR', 'Techstar Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401900', 1005, 204 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 205, 'TLSCA', '_Telstra Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401910', 1005, 205 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 206, 'TLS', 'Telstra Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401920', 1005, 206 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 207, 'TLSCB', '_Telstra Corporation Ltd Insta', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401930', 1005, 207 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 208, 'TGG', 'Templeton Global Growth Fund L', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401940', 1005, 208 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 209, 'TLSXX', '_TLS Warrant (345 UBS Warburg', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401950', 1005, 209 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 210, 'TNT', '_TNT Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401960', 1005, 210 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 211, 'TWR', 'Tower Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401970', 1005, 211 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 212, 'TCL', 'Transurban Group', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401980', 1005, 212 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 213, 'UEC', 'Uecomm Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2401990', 1005, 213 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 214, 'UEL', 'United Energy Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402000', 1005, 214 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 215, 'VRL', 'Village Roadshow Corp', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402010', 1005, 215 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 216, 'WES', 'Wesfarmers Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402020', 1005, 216 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 217, 'WAN', 'West Aust Newspapers Holdings', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402030', 1005, 217 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 218, 'WANO', '_WEST AUSTRALIAN NEWSPAPERS -', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402040', 1005, 218 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 219, 'WSG', 'Westel Group Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402050', 1005, 219 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 220, 'WSF', 'Westfield Holdings Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402060', 1005, 220 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 221, 'WBC', 'Westpac Banking Corp Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402070', 1005, 221 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 222, 'WBCPA', '_Westpac Banking Corp Ltd Pref', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402080', 1005, 222 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 223, 'WMC', 'WMC Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402090', 1005, 223 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 224, 'WPL', 'Woodside Pertoleum Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402100', 1005, 224 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 225, 'WOW', 'Woolworths Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402110', 1005, 225 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 226, 'YAM', 'Yamarna Goldfields Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402120', 1005, 226 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 227, 'AGZ', 'L-Tel Corporation Ltd', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402130', 1005, 227 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 228, 'FIFO', '_Franked Income Fund - Options', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402140', 1005, 228 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 229, 'SUN', 'Suncorp-Metway Limited', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402150', 1005, 229 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 230, 'CROHB', 'Crown Ltd 9.5% Unsec Note 15/0', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402160', 1005, 230 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 231, 'WOWHA', 'Woolworths Income Securities', 1005 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2402170', 1005, 231 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1006, 'Shares (Imputation)', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Shares (Imputation)', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '241', 1006, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'CBA', 'Imp - Commonwealth Bank of Aus', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410000', 1006, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'CSR', 'Imp - CSR Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410010', 1006, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'FOA', 'Imp - Foodland Associated Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410020', 1006, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'FBG', '_Imp - Fosters Brewing Group L', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410030', 1006, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'NAB', 'Imp - National Australia Bank', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410040', 1006, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'QAN', 'Imp - Qantas Airways Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410050', 1006, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'SSX', 'Imp - Smorgon Steel Group', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410060', 1006, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'SME', '_Imp - Suncorp-Metway Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410070', 1006, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'TAB', 'Imp - Tab Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410080', 1006, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'TLS', 'Imp - Telstra Corporation Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410090', 1006, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, 'WAN', 'Imp - Western Australian Newsp', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410100', 1006, 12 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 13, 'WES', 'Imp - Wesfarmers Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410110', 1006, 13 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 14, 'WOW', 'Imp - Woolworths Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410120', 1006, 14 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 15, 'ANZ', '_Imp - ANZ Bank', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410130', 1006, 15 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 16, 'CTX', '_Imp - Caltex Australia Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410140', 1006, 16 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 17, 'EML', '_Imp - Email Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410150', 1006, 17 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 18, 'EMLS', '_Imp - Email Ltd: Share Put', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410160', 1006, 18 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 19, 'FGL', 'Imp - Fosters Group Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410165', 1006, 19 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 20, 'FIF', '_Imp - Franked Income Fund', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410170', 1006, 20 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 21, 'FIFO', '_Imp - Franked Income Fund - O', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410180', 1006, 21 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 22, 'GIO', '_Imp - GIO Australia Holdings', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410190', 1006, 22 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 23, 'ICT', '_Imp - Incitec Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410200', 1006, 23 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 24, 'LLC', '_Imp - Lend Lease Corporation', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410210', 1006, 24 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 25, 'PMP', '_Imp - PMP Communications Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410220', 1006, 25 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 26, 'QRL', '_Imp - Queensland Coal Trust', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410230', 1006, 26 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 27, 'RAC', '_Imp - Reinsurance Australia C', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410240', 1006, 27 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 28, 'RIO', 'Imp - Rio Tinto Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410250', 1006, 28 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 29, 'SGBS', '_Imp - St George Sell Back Rig', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410251', 1006, 29 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 30, 'SMS', '_Imp - Simsmetal Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410260', 1006, 30 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 31, 'WBC', '_Imp - Westpac Banking Corp Lt', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410280', 1006, 31 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 32, 'STO', '_Imp - Santos Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410290', 1006, 32 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 33, 'SUN', 'Imp - Suncorp-Metway Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410300', 1006, 33 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 34, 'TLSCA', '_Imp - Telstra Corporation Ltd', 1006 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2410310', 1006, 34 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1007, 'Shares (Growth)', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Shares (Growth)', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '242', 1007, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'AGL', '_Gwt - AGL Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420000', 1007, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'AHD', '_Gwt - Amalgamated Holdings', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420010', 1007, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'BIL', 'Gwt - Brambles Industries Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420020', 1007, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'ECP', '_Gwt - Ecorp Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420030', 1007, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'SMI', '_Gwt - Howard Smith Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420040', 1007, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'LLC', 'Gwt - Lend Lease Corporation L', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420050', 1007, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'NAB', 'Gwt - National Australia Bank', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420060', 1007, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'NCP', 'Gwt - News Corporation Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420070', 1007, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'PBB', 'Gwt - Pacifica Group Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420080', 1007, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'PBL', 'Gwt - Publishing & Broadcastin', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420090', 1007, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, 'SRP', 'Gwt - Southcorp Holdings Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420100', 1007, 12 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 13, 'TAH', 'Gwt - Tabcorp Holdings Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420110', 1007, 13 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 14, 'TLS', 'Gwt - Telstra Corporation Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420120', 1007, 14 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 15, 'WPL', 'Gwt - Woodside Petroleum Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420130', 1007, 15 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 16, 'CPH', '_Gwt - CPH Investment Corporat', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420140', 1007, 16 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 17, 'FLT', 'Gwt - Flight Centre Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420150', 1007, 17 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 18, 'HVN', 'Gwt - Harvey Norman', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420160', 1007, 18 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 19, 'LLCBN', '_Gwt - Lend Lease Corp. Ltd. (', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420170', 1007, 19 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 20, 'NBH', '_Gwt - North Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420180', 1007, 20 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 21, 'NDY', '_Gwt - Normandy Mining Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420190', 1007, 21 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 22, 'TLSCA', '_Gwt - Telstra Corporation Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420200', 1007, 22 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 23, 'WSF', 'Gwt - Westfield Holdings Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420210', 1007, 23 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 24, 'AJR', '_Gwt - Armstrong Jones Retail', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420220', 1007, 24 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 25, 'AJS', '_Gwt - Armstrong Jones Industr', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420230', 1007, 25 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 26, 'BTP', '_Gwt - BT Property Trust', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420240', 1007, 26 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 27, 'WES', 'Gwt-Wesfarmers Ltd', 1007 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2420250', 1007, 27 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1008, 'Listed Unit Trusts', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Listed Unit Trusts', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '245', 1008, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'ADP', 'AMP Diversified Property Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450000', 1008, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'AJS', 'Armstrong Jones Industrial', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450010', 1008, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'AJO', 'Armstrong Jones Office Fund', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450020', 1008, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'AJR', 'Armstrong Jones Retail Fund', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450030', 1008, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'BTO', 'BT Office Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450040', 1008, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'BTP', 'BT Property Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450050', 1008, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'CPH', 'CPH Investment Corporation', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450060', 1008, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'CTT', 'CT Retail Investment Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450070', 1008, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'FIF', 'Franked Income Fund', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450080', 1008, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'GPT', 'General Property Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450090', 1008, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, 'MGI', 'Goodman Hardie Industrial Prop', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450100', 1008, 12 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 13, 'GHG', 'Grand Hotel Group', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450110', 1008, 13 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 14, 'HRP', 'Homemaker Retail Prop Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450120', 1008, 14 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 15, 'DIT', 'Industrial Investment Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450130', 1008, 15 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 16, 'IPG', 'Investa Property Group', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450140', 1008, 16 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 17, 'JFG', 'James Fielding Group', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450145', 1008, 17 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 18, 'MGI', 'Macquarie Goodman Industrial T', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450150', 1008, 18 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 19, 'MOF', 'Macquarie Office Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450160', 1008, 19 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 20, 'PRD', 'Meridian Investment Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450170', 1008, 20 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 21, 'PAT', 'PA Property Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450180', 1008, 21 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 22, 'PDC', 'Paladin Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450190', 1008, 22 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 23, 'PRP', 'Prime Credit Property Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450200', 1008, 23 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 24, 'PII', 'Property Inc Investment Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450210', 1008, 24 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 25, 'SGP', 'Stockland Trust Group', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450220', 1008, 25 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 26, 'SDR', 'Sundowner Property Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450230', 1008, 26 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 27, 'TCE', 'Triplecee Retail Invest Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450240', 1008, 27 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 28, 'TMT', 'Tyndall Meridian Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450250', 1008, 28 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 29, 'WFT', 'Westfield Trust', 1008 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2450260', 1008, 29 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1009, 'Property Securities', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Property Securities', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '246', 1009, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'ADP', 'Prp - AMP Diversified Property', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460000', 1009, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'BTO', 'Prp - BT Office Trust', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460010', 1009, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'GHG', 'Prp - Grand Hotel Group', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460020', 1009, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'HRP', 'Prp - Homemaker Retail Group', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460030', 1009, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'IPG', 'Prp - Investa Property Group', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460040', 1009, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'MGI', 'Prp - Macquarie Goodman Indust', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460050', 1009, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'MOF', 'Prp - Macquarie Office Trust', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460060', 1009, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'AJR', 'Prp - Armstrong Jones Retail F', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460070', 1009, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'AJS', 'Prp - Armstrong Jones Industri', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460080', 1009, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'BTP', 'Prp - BT Property Trust', 1009 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2460090', 1009, 11 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1010, 'Unit Trusts (Internal)', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Unit Trusts (Internal)', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '248', 1010, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'SP1', 'Fiducian Capital Safe Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480000', 1010, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'SF1', 'Fiducian Capital Stable Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480010', 1010, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'BF1', 'Fiducian Balanced Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480020', 1010, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'GF1', 'Fiducian Growth Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480030', 1010, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'AE1', 'Fiducian Australian Share Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480040', 1010, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'SC3', 'Fiducian Smaller Co Share Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480050', 1010, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'IE1', 'Fiducian International Shares', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480060', 1010, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'IE1', 'Fiducian International Shares', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480060', 1010, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'IE6', 'Fid Glo Smller Cos & Emerg Mkt', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480070', 1010, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'TF1', 'Fiducian Technology Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480080', 1010, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, 'PS1', 'Fiducian Prop. Securities Fund', 1010 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2480090', 1010, 12 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1011, 'Life Policies', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Life Policies', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '260', 1011, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, '067459K8', 'Traded Life Policy 067459K8', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600000', 1011, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, '07254273R', 'Traded Life Policy 07254273R', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600010', 1011, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, '10090734Y', 'Traded Life Policy 10090734Y', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600020', 1011, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, '10233294L', 'Traded Life Policy 10233294L', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600030', 1011, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, '10353631U', 'Traded Life Policy 10353631U', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600040', 1011, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, '10588947', 'Traded Life Policy 10588947', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600050', 1011, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, '1088463', 'Traded Life Policy 1088463', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600060', 1011, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, '11337620T', 'Traded Life Policy 11337620T', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600070', 1011, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, '1182879N', 'Traded Life Policy 1182879N', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600080', 1011, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, '12660858F', 'Traded Life Policy 12660858F', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600090', 1011, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, '12854082L', 'Traded Life Policy 12854082L', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600100', 1011, 12 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 13, '12913978', 'Traded Life Policy 12913978', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600110', 1011, 13 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 14, '13116386', 'Traded Life Policy 13116386', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600120', 1011, 14 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 15, '13728892S', 'Traded Life Policy 13728892S', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600130', 1011, 15 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 16, '1454798', 'Traded Life Policy 1454798', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600140', 1011, 16 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 17, '14762186E', 'Traded Life Policy 14762186E', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600150', 1011, 17 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 18, '14784826', 'Traded Life Policy 14784826', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600160', 1011, 18 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 19, '15378166', 'Traded Life Policy 15378166', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600170', 1011, 19 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 20, '15982507T', 'Traded Life Policy 15982507T', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600180', 1011, 20 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 21, '242013', 'Traded Life Policy 242013', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600190', 1011, 21 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 22, '304089', 'Traded Life Policy 304089', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600200', 1011, 22 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 23, '670719', 'Traded Life Policy 670719', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600210', 1011, 23 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 24, '910351', 'Traded Life Policy 910351', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600220', 1011, 24 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 25, 'H0347280M', 'Traded Life Policy H0347280M', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600230', 1011, 25 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 26, 'H1551488', 'Traded Life Policy H1551488', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600240', 1011, 26 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 27, '', 'Traded Life Policy', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600250', 1011, 27 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 28, '', 'Traded Life Policy', 1011 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2600260', 1011, 28 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1012, 'Unit Trusts (Non PST) Wholesale', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Unit Trusts (Non PST) Wholesale', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '270', 1012, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'AE9', 'AMP Equity Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700000', 1012, 2 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 3, 'SP4', 'AMP Premium Treasury Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700010', 1012, 3 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 4, 'MF3', 'AXA W/S Australian Income Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700020', 1012, 4 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 5, 'AE4', 'BNP Australian Equity Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700030', 1012, 5 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 6, 'SC1', 'BNP Smaller Companies Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700040', 1012, 6 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 7, 'IE3', 'BT W/S Asian Share Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700050', 1012, 7 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 8, 'BF3', 'BT W/S Balanced Returns Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700060', 1012, 8 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 9, 'SF5', 'BT W/S Conservative Outlook Fu', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700070', 1012, 9 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 10, 'IE2', 'BT W/S International Share Fun', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700080', 1012, 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 11, 'BF2', 'Colonial First State W/S Diver', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700090', 1012, 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 12, 'AE2', 'Colonial First State W/Sale Au', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700100', 1012, 12 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 13, 'SP2', 'Colonial First State W/S Cash', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700110', 1012, 13 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 14, 'AE8', 'Colonial First State W/S Geare', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700120', 1012, 14 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 15, 'SF6', 'Invesco W/S December Protected', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700130', 1012, 15 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 16, 'SF3', 'Invesco W/S June Protected Gro', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700140', 1012, 16 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 17, 'SF8', 'Invesco W/S March Protected Gr', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700150', 1012, 17 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 18, 'SF7', 'Invesco W/S September Protecte', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700160', 1012, 18 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 19, 'GF3', 'Credit Suisse Capital Growth F', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700170', 1012, 19 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 20, 'IE5', 'Credit Suisse International Sh', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700180', 1012, 20 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 21, 'AE3', 'Hambros Hopkins Aust Shares Fu', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700190', 1012, 21 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 22, 'SC2', 'HSBC Aust Small Companies W/S', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700200', 1012, 22 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 23, 'PS3', 'HSBC Property Securities W/S F', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700210', 1012, 23 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 24, 'AE7', 'Investors Mutual Aust Share Fu', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700220', 1012, 24 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 25, 'SC5', 'Investors Mutual Aust Small Co', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700230', 1012, 25 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 26, 'GF2', 'ING W/S Managed Growth Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700240', 1012, 26 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 27, 'IE4', 'Macquarie Master Global Equiti', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700250', 1012, 27 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 28, 'BF4', 'Macquarie Master Balanced Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700260', 1012, 28 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 29, 'SF4', 'Macquarie Master Capital Stabl', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700270', 1012, 29 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 30, 'SF9', 'Portfolio Partners Conservativ', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700280', 1012, 30 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 31, 'MF2', 'Challenger Mortgage Plus Trust', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700290', 1012, 31 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 32, 'SF2', 'Norwich W''sale Capital Stable', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700300', 1012, 32 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 33, 'PS2', 'Deutsche Paladin Property Secu', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700310', 1012, 33 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 34, 'AE10', 'Perpetual''s W/S Industrial Fun', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700320', 1012, 34 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 35, 'IE8', 'Platinum International Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700330', 1012, 35 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 36, 'IE7', '_Rothschild Discovery Global W', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700340', 1012, 36 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 37, 'SC4', 'Rothschild Small Companies W/S', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700350', 1012, 37 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 38, 'AE11', 'Sandhurst IML Industrial Share', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700360', 1012, 38 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 39, 'AE6', 'Tyndall Aust Share W/S Portfol', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700370', 1012, 39 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 40, 'SP3', 'UBS Brinson Cash Plus Fund', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700380', 1012, 40 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 41, 'AE12', 'Colonial First State W/S Imput', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700390', 1012, 41 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 42, 'PS4', 'Citigroup Property Securities', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700395', 1012, 42 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 43, 'PS4', 'Citigroup Property Securties T', 1012 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2700395', 1012, 43 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1013, 'Unit Trusts (Non PST) Retail', 11 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Unit Trusts (Non PST) Retail', 1013 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '271', 1013, 1 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 2, 'MF1', 'Nat Mut Aust Income Fund', 1013 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '2710000', 1013, 2 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1014, 'Cash at Bank', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Cash at Bank', 1014 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '290', 1014, 1 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1015, 'Cash Account', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Cash Account', 1015 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '295', 1015, 1 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1016, 'Fund Pending Asset Account', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Fund Pending Asset Account', 1016 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '298', 1016, 1 );


INSERT INTO FinancialType ( FinancialTypeID, FinancialTypeDesc, ObjectTypeID ) VALUES ( 1017, 'Member Pending Asset Account', 10 );

INSERT INTO FinancialCode ( FinancialCodeID, FinancialCode, FinancialCodeDesc, FinancialTypeID ) VALUES ( 1, '', 'Member Pending Asset Account', 1017 );
INSERT INTO FinancialMapSV2 ( ac_num, FinancialTypeID, FinancialCodeID ) VALUES ( '299', 1017, 1 );

COMMIT;
