--------------------------------------------------------------
-- create survey/questions/answers
--------------------------------------------------------------
#ifdef MSSQL
CREATE PROCEDURE [dbo].[sp_temp]
 AS 

 DECLARE @SurveyID int 
 DECLARE @QuestionID int 

 INSERT Object (ObjectTypeID) VALUES (24)
 SELECT @@IDENTITY
 SET @SurveyID = @@IDENTITY

-- survey
 INSERT Survey (SurveyID, SurveyTitle, SurveyDesc)
 VALUES (@SurveyID, 'Risk Profile', 'Questionnaire to measure the client''s investment risk tolerance')

-- Link this @SurveyID to RiskProfile survey type
 DECLARE @LinkID int 
 DECLARE @Survey_2_RiskProfile int
 SET @Survey_2_RiskProfile = 24027
 INSERT Object (ObjectTypeID) VALUES (@Survey_2_RiskProfile)
 SELECT @@IDENTITY
 SET @LinkID = @@IDENTITY

 INSERT Link (LinkID, ObjectID1, ObjectID2, LinkObjectTypeID)
 VALUES (@LinkID, @SurveyID, NULL, @Survey_2_RiskProfile)


-- question 1
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'What is your opinion for the direction of the Australian economy for the next 3 to 5 years?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 1
----------------------------------------------------------------------
-- BUG FIX 479
-- !!!The first answer for the first question is not displayed,
-- INSERT it twice and it's displayed!!!
-- (the bug is somewhere in the java source code ("SurveyBean.java")
----------------------------------------------------------------------
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I think the economy will slow down.', 		1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I think the economy will slow down.', 		1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I have No Opinion of what the economy may do in the next 3-5 years.', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I expect the economy to Remain the Same.', 		3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I have Positive Expectations of the economy.', 	4, @QuestionID)

-- question 2
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'How important is it that the returns on your investments exceed the rate of inflation over the longer term?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 2
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Not Concerned.', 		1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Slightly Concerned.', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Moderately Concerned.', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Highly Concerned.', 		4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Very Highly Concerned.', 	5, @QuestionID)

-- question 3
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'What in your opinion will be the average inflation rate in Australia for the next 3-5 years?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 3
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Inflation will increase to over 5%.', 	6, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Inflation will increase to 4-5%.', 		5, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Inflation will stay about the same.', 	4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Inflation will be low and fall below 1%.', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I have no opinion.', 				2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I don''t understand inflation.  I require further explanation of inflation.', 1, @QuestionID)

-- question 4
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Please indicate the level of return you would target for your investments?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 4
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would aim for my investments to keep pace with inflation.', 	1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would aim to out perform inflation by 2%.', 			2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would aim to out perform inflation by 4%.', 			3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would aim to out perform inflation by 6%.', 			4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would aim to out perform inflation by 8%.', 			5, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would aim to out perform inflation by more than 8%.', 		6, @QuestionID)

-- question 5
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Keeping in mind this fact: ''the higher the return, the higher the risk'', approximately what annual rate of return would you expect would meet your goals?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 5
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Less than 5%', 1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('5% to 10%', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Above 10%', 3, @QuestionID)

-- question 6
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'How would you react if your long-term investment portfolio declined in value by 10% in one year?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 6
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I can''t accept any declines in the value of my investments.', 1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('If I continued to receive the income I need, I would not be too concerned about my capital declining in the short term.', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I generally invest for the long term, but would be concerned with this level of decline.', 3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I invest for the long term and accept that these fluctuations occur due to short-term market and economic influences.', 4, @QuestionID)

-- question 7
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Most investments are subject to price volatility. What would you do if one of your investments declined by more than 20% in a year? (Assuming your total investment portfolio fundamentals still remained the same)', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 7
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would sell the investment.', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would hold the investment.', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I would buy more of that investment.', 4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I am not sure what I would do.', 	1, @QuestionID)

-- question 8
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'What would you do with an investment that increased by more than 20% in a year? (Assuming your overall investment portfolio fundamentals are the same)', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 8
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Sell that investment', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Hold that investment', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Buy more of that investment', 1, @QuestionID)

-- question 9
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Have you ever invested in shares or managed funds before?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 9
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('No, but if I had, the fluctuation in asset values would make me uncomfortable.', 						1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('No, but if I had, I would be comfortable with the fluctuations in order to receive the potential for higher returns.', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Yes I have, but I was uncomfortable with the fluctuation, despite the potential for higher returns.', 			3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Yes I have, and I felt comfortable with the fluctuation, in order to receive the potential for higher returns.', 		4, @QuestionID)

-- question 10
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'For the funds you have to invest, please identify which of the following investment objectives is most important to you?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 10
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Preservation of Funds</B>. Cash funds only, with secure income. No growth to keep up with inflation. Investment  period of up to 3 years.', 1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Income Producing Funds</B>. Growth keeps up with inflation. Generates income while limiting risk of capital losses. Minimum Investment Period of 3 to 5 years.', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Capital Growth plus Income</B> - Accept that growth may differ to markets growth. Minimum investment time period of 5 years.', 3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Capital Growth</B> - Income not required. Maximise long-term returns, while accepting the possibility of short-term negative growth. Minimum investment time period of over 5 years.', 4, @QuestionID)

-- question 11
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Assuming you have an amount of money to invest, how would you invest it?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 11
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('The safety of my money is my primary objective. I would rather have a low rate of return than risk losing any part of my capital.', 1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I want my investment to produce the current income I need and my capital should remain relatively stable.', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I am willing to accept some short-term fluctuation in my capital, but expect higher returns over the long term.', 3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('In order to receive the maximum return on my investment, I am willing to accept a higher degree of market volatility.', 4, @QuestionID)

-- question 12
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, '
If you held assets managed by professional fund managers, what reasons do you think could cause you to wish to change investment managers?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 12
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Poor investment performance over a reasonable time period (3 to 5 years).', 1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Deviation from the stated investment style and discipline.', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Loss of one or more of the firms key investment management staff.', 3, @QuestionID)

-- question 13
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Do you believe that professionally trained fund managers have a high probability of delivering your expected returns?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 13
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Yes.', 			1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('No.', 			2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('I can do better myself.', 	3, @QuestionID)

-- question 14
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'What length of time are you able to commit to holding your investments, without needing to sell them or withdraw capital?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 14
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('0-3 years.', 			1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('3-5 years.', 			2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('5-10 years.', 		3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('More than 10 years.', 	4, @QuestionID)

-- question 15
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Is there any reason you may need to access more than 20% of your funds in the next 12 months?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 15
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Yes.', 	1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('No.', 	2, @QuestionID)

-- question 16
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Assuming that the overall trend is positive, how often would a negative return be acceptable to you. ', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 16
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('1 year out of 3.', 	4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('1 year out of 5.', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('1 year out of 7.', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('No negative return.', 1, @QuestionID)

-- question 17
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'When do you expect to retire?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 17
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Within 1 year.', 		1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Within 1 to 5 years.', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Within 5 to 10  years.', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Over 10 years.', 		4, @QuestionID)

-- question 18
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'Which one of the following statements describes your feelings towards choosing an investment?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 18
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Ultra Conservative:</B> I would select investments with a high level of secure income with a strong emphasis on security and preservation of capital.', 1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Conservative:</B>  I would select investments with secure and stable income although I would expect some capital growth over the medium to longer term.  I would be looking for some tax effectiveness of income, with some exposure to shares and property.', 2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Conservative / Balanced:</B> I would want a combination of capital growth and income from my investments. I am prepared for some fluctuations in capital value and understand that there can be a negative return on the portfolio if more than one asset class falls in value.  I can tolerate some fluctuation of income returns. Generally, I would hold my investments for over five years.', 3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Balanced:</B> I would select investments giving a combination of capital growth and income with a higher focus on capital growth than for the Conservative Balanced category. I would be prepared for some fluctuations in capital value and understand that there can be a negative return on the portfolio.  I can tolerate some fluctuation of income returns. Generally, I would hold my investments for over five years.', 4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Growth:</B> I would expect to receive capital growth with some income over the longer term.  Short-term asset protection is relevant, but not a serious consideration.  I would select relatively high weighting towards growth assets such as shares and property. I would select investments for the long-term and understand that volatility inherently accompanies investment in shares and property.  I would be prepared to accept fluctuations in capital value and the possibility of negative returns in the short-term in return for higher returns over the longer term.', 5, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Strong Growth:</B> I would be looking to maximise my returns over a long period of time, at least seven to ten years.  I would also be seeking very high exposure to growth assets, such as shares and property, and would be prepared to accept considerable fluctuations (negative and positive) in capital value over short intervals as a result of changes in market conditions.  Income is not a priority for me.  Exposure would be predominantly in growth assets that could comprise different classes of shares, property and specialist managed funds. I understand that investing in growth assets, particularly if held for short periods, could involve considerable volatility in portfolio capital values.', 6, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('<B>Ultra Growth:</B> I would also be looking to maximise returns over a long period of time, at least seven to ten years.  I would be seeking very high exposure to growth assets, such as shares and property, and would be prepared to accept considerable fluctuations (negative and positive) in capital value over short intervals as a result of changes in market conditions.  I would not be interested in income.  Exposure would be totally in growth assets that could comprise different classes of shares, property and specialist managed funds.  I would understand that investing in growth assets, particularly if held for short periods, could involve considerable volatility in portfolio capital.', 7, @QuestionID)

-- question 19
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 
'<HTML>
<HEAD>
<TITLE>Question 19</TITLE>
</HEAD>
<BODY>

<FONT SIZE="3"><P ALIGN="JUSTIFY">
<P>Below is a table outlining the performance of hypothetical investment portfolio strategies over a seven year time period.</P>
<P></P>
<P><B>Strategies Possible Earning Rate Profiles</B></P>
</FONT>
<TABLE BORDER CELLSPACING=1 CELLPADDING=1 WIDTH=648>
<TR><TD WVALIGN="TOP"></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 1</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 2</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 3</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 4</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 5</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 6</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 7</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Expected Return</B></FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP" HEIGHT=27>
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Ultra Conservative</B></FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">4.5%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">5.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">5.5%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">5.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">4.5%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">5.5%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">5.3%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">5.0%</FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Conservative</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-2.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">5.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">12.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">11.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">6.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">11.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">8.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">7.2%</FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Conservative-Balanced</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-4.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">8.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">15.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">13.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">1.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">16.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">13.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">8.6%</FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP" HEIGHT=26>
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Balanced</B></FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">-6.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">10.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">16.2%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">21.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">-1.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">15.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">16.0%</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">9.8%</FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Growth</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-6.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">10.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">18.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">22.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-1.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">18.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">16.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">10.5%</FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Strong Growth</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-12.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">25.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">15.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">28.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-8.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">20.8%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">18.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">11.4%</FONT></TD>
</TR>
<TR>
<TD WVALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Ultra Growth</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-14.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">25.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">20.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">32.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">-9.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">18.0%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">22.5%</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">12.2%</FONT></TD>
</TR>
</TABLE>

<FONT SIZE="3">
<P>The table below shows the value of $500,000 invested in each strategy.</P>
<P></P>
<P><B>Investor Expectation of Portfolio Having a Loss</B></P>
</FONT>
<TABLE BORDER CELLSPACING=1 CELLPADDING=1 WIDTH=648>
<TR>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Investor Strategy</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 1</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 2</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 3</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 4</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 5</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 6</B></FONT></TD>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="CENTER">Year 7</B></FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP" HEIGHT=27>
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Ultra Conservative</B></FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">522,500</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">548,625</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">578,799</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">607,739</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">635,088</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">670,017</FONT></TD>
<TD VALIGN="TOP" HEIGHT=27>
<FONT SIZE="2"><P ALIGN="CENTER">705,528</FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Conservative</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">490,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">514,500</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">576,240</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">639,626</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">678,004</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">752,584</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">812,791</FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Conservative-Balanced</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">480,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">518,400</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">596,160</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">673,661</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">680,397</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">789,261</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">891,865</FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP" HEIGHT=26>
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Balanced</B></FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">470,000</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">517,000</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">600,754</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">726,912</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">719,643</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">827,590</FONT></TD>
<TD VALIGN="TOP" HEIGHT=26>
<FONT SIZE="2"><P ALIGN="CENTER">960,004</FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Growth</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">470,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">517,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">610,060</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">744,273</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">736,830</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">869,460</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">1,008,574</FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Strong Growth</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">440,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">550,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">632,500</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">809,600</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">744,832</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">899,757</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">1,061,713</FONT></TD>
</TR>
<TR>
<TD VALIGN="TOP">
<B><FONT SIZE="2"><P ALIGN="JUSTIFY">Ultra Growth</B></FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">430,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">537,500</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">645,000</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">851,400</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">774,774</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">914,233</FONT></TD>
<TD VALIGN="TOP">
<FONT SIZE="2"><P ALIGN="CENTER">1,119,936</FONT></TD>
</TR>
</TABLE>
<P></P>
<FONT SIZE="3">
<P><B>Based on this information above, which investment strategy would you choose?</B></P>
</FONT>
</BODY>
</HTML>', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 19
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Ultra Conservative.', 	1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Conservative.', 		2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Conservative Balanced.', 	3, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Balanced.', 			4, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Growth.', 			5, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Strong Growth.', 		6, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Ultra Growth.', 		7, @QuestionID)

-- question 20
 INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
 VALUES (1, 'How often would you like to meet with your financial planner to review and discuss your portfolio?', @SurveyID)
 SELECT @@IDENTITY
 SET @QuestionID = @@IDENTITY

-- answer 20
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Quarterly.', 		1, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Half Yearly.', 	2, @QuestionID)
 INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
 VALUES ('Yearly.', 		3, @QuestionID)

#endif MSSQL
;	-- HAS TO BE LAST IN SP (to indicate end of sql statement to execute)

#ifdef MSSQL
	EXECUTE [dbo].[sp_temp]
#endif MSSQL
GO

#ifdef MSSQL
	DROP PROCEDURE [dbo].[sp_temp]
#endif MSSQL
GO



--
-- this should be the last statement in any update script
--
INSERT INTO DBVersion (CurrentVersion, PreviousVersion)
VALUES ('FPS.01.40', 'FPS.01.39');
