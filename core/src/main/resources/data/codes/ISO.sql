--------------------------------------------------------------
-- create first sample survey/questions/answers
--------------------------------------------------------------
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
VALUES (1, 
'What is your opinion for the Australian economy over the next 3 to 5 years?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 1
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Negative', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('No Opinion', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Expect the same', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Positive', 4, @QuestionID)

-- question 2
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'How concerned are you that the returns on your investment do not exceed the rate of inflation?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 2
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Not Concerned', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Slightly Concerned', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Moderately Concerned', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Highly Concerned', 4, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Very Highly Concerned', 5, @QuestionID)

-- question 3
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'What in your opinion will be the average inflation rate in Australia for the next 3 years?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 3
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Inflation will increase by over 4-5%', 4, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Inflation will increase by 3-4%', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Inflation will stay in a 1-3% band', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Inflation will be low and fall below 1%', 1, @QuestionID)

-- question 4
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Please indicate the level of return you would target?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 4
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Keep pace with inflation', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Return above inflation by 2%', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Return above Inflation by 6%', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Return above Inflation by 8%', 4, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Return above Inflation by 8% plus', 5, @QuestionID)

-- question 5
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Keeping in mind this fact: "the higher the return, the higher the risk", approximately what annual rate of return would you expect to meet your goals?'
, @SurveyID)
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
VALUES (1, 
'How would you react if your long-term investments declined by 10% in one year?'
, @SurveyID)
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
VALUES (1, 
'Share investments are subject to price volatility. What would you do with a share that declined by more than 20% in a year? (Assuming your overall portfolio fundamentals are the same)'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 7
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Sell', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Hold ', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Buy more', 3, @QuestionID)

-- question 8
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'What would you do with a share that increased by more than 20% in a year? (Assuming your overall portfolio fundamentals are the same)'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 8
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Sell', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Hold', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Buy more', 1, @QuestionID)

-- question 9
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Have you ever invested in shares or managed funds before?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 9
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('No, but if I had, the fluctuation in asset values would make me uncomfortable.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('No, but if I had, I would be comfortable with the fluctuations in order to receive the potential for higher returns.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Yes I have, but I was uncomfortable with the fluctuation, despite the potential for higher returns.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Yes I have, and I felt comfortable with the fluctuation, in order to receive the potential for higher returns.', 4, @QuestionID)

-- question 10
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'For the assets you are investing, please identify which of the following investment objectives is most important to you?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 10
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Preservation of Assets - Growth to equal inflation. Minimum Investment time period of 3-5 years.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Income - Generate Income while limiting risk of capital losses. Minimum Investment Period 3 to 5 years.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Capital Growth plus Income - Accept that growth may differ to markets growth. Minimum investment time period 5 years.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Capital Growth - Maximise long-term returns, while accepting the possibility of short term negative growth. Minimum investment time period over 5 years.', 4, @QuestionID)

-- question 11
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Assuming you have an amount of money to invest, how would you invest it?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 11
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('The safety of my money is my primary objective. I would rather have a low rate of return than risk the loss of any part of my capital.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('I want my investment to produce the current income I need and my capital should remain relatively stable.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('I am willing to accept some short-term fluctuation in my capital, but expect higher returns over the long term.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('In order to receive the maximum return on my investment, I am willing to accept a higher degree of market volatility.', 4, @QuestionID)

-- question 12
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'If you held assets managed by professional fund managers, what reasons do you think could cause you to wish to change investment managers?'
, @SurveyID)
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
VALUES (1, 
'Do you believe that professionally trained fund managers have a better probability of delivering expected returns?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 13
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Yes.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('No.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('I can do better myself.', 3, @QuestionID)

-- question 14
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'What is the shortest length of time you are able to commit to holding your investments, without needing to sell them and withdraw capital?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 14
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('0-3  years.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('3-5 years.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('5-10 years.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('More than 10 years.', 4, @QuestionID)

-- question 15
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Is there any reason you may need to access more than 20% of your funds in the next 12 months?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 15
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('No.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Yes.', 2, @QuestionID)

-- question 16
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Assuming that the overall trend is positive, how often would a negative return be acceptable to you.'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 16
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('1 year out of 3.', 4, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('1 year out of  5.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('1 year out of  7.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('No negative return.', 1, @QuestionID)

-- question 17
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'When do you expect to retire?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 17
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Within 1 year.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Within 1 to 5 years.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Within 5 to 10  years.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Over 10 years.', 4, @QuestionID)

-- question 18
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'Which one of the following statements describes your feelings towards choosing an investment?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 18
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES (
'<HTML><FONT SIZE=3><P ALIGN="LEFT">
<B>Conservative:</B> I would select investments with a high level of secure income with a strong emphasis on security and preservation of capital.
</P></FONT></HTML>'
, 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES (
'<HTML><FONT SIZE=3><P ALIGN="LEFT">
<B>Conservative / Balanced:</B> I would select investments with secure and stable income although there would also be an expectation of some capital growth over the medium to longer term.  I would be looking for some tax effectiveness of income, with some exposure to shares and property.
</P></FONT></HTML>'
, 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES (
'<HTML><FONT SIZE=3><P ALIGN="LEFT">
<B>Balanced / Growth:</B> I would select investments giving a combination of capital growth and income. I would be prepared for some fluctuations in capital value and understands that there can be a negative return on the portfolio.  I can tolerate some fluctuation of income returns. Generally, I would hold my investments for over five years.
</P></FONT></HTML>'
, 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES (
'<HTML><FONT SIZE=3><P ALIGN="LEFT">
<B>Growth:</B> I would expect to receive capital growth with some income over the longer term.  Short-term asset protection is relevant, but not a serious consideration.  I would select a relatively high weighting towards growth assets such as shares and property. I would select investments for the long-term and understand the volatility inherently accompanies investment in shares and property.  I would be prepared to accept fluctuations in capital value and the possibility of negative returns in the short-term.
</P></FONT></HTML>'
, 4, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES (
'<HTML><FONT SIZE=3><P ALIGN="LEFT">
<B>Aggressive:</B> I would be looking to maximise my returns over a long period of time, at least seven to ten years.  I would also be seeking very high exposure to growth assets, such as shares and property, and would be prepared to accept considerable fluctuations (negative and positive) in capital value over short intervals as a result of changes in market conditions.  Income is not a priority for this investor.  Exposure would be predominantly in growth assets that could comprise different classes of shares, property and specialist funds.
</P></FONT></HTML>'
, 5, @QuestionID)

-- question 19
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'<HTML>
<FONT SIZE=3><P ALIGN="LEFT">
Below is a table outlining the performance of hypothetical investment portfolio strategies.
</FONT>

<TABLE BORDER CELLSPACING=1 CELLPADDING=0.5 WIDTH=690>
<TR><TD WIDTH="10%" VALIGN="TOP" COLSPAN=2>&nbsp;</TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 1</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 2</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 3</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 4</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 5</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 6</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 7</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Average</B></FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP" HEIGHT=21>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">1</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Conservative</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">4.5%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.5%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">4.5%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.5%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.3%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.0%</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">2</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Conservative-Balanced</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">-2.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">5.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">14.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">11.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">6.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">11.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">8.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">7.5%</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">3</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Balanced-Growth</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">-4.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">8.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">15.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">19.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">1.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">16.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">13.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">9.4%</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP" HEIGHT=21>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">4</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Growth</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">-6.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">10.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">18.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">22.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">-1.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">18.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">16.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">10.5%</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">5</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Aggressive Growth</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">-12.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">25.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">20.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">37.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">-8.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">20.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">25.0%</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">14.0%</FONT></TD>
</TR>
</TABLE>

<FONT SIZE=3><P ALIGN="LEFT">
The table below shows the value of $500,000 invested in each strategy.
</FONT>

<TABLE BORDER CELLSPACING=1 CELLPADDING=0.5 WIDTH=690>
<TR><TD WIDTH="10%" VALIGN="TOP" COLSPAN=2>&nbsp;</TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 1</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 2</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 3</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 4</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 5</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 6</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="CENTER">Year 7</B></FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP" HEIGHT=21>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">1</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Conservative</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">522,500</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">548,625</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">578,799</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">607,739</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">635,087</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">670,017</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">705,528</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">2</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Conservative-Balanced</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">490,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">514,500</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">586,530</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">651,048</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">690,111</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">766,023</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">827,305</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">3</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Balanced-Growth</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">480,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">518,400</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">596,160</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">709,430</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">716,524</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">831,168</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">939,220</FONT></TD>
</TR>
<TR><TD WIDTH="4%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">4</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Growth</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">470,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">517,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">610,060</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">744,273</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">736,830</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">869,460</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">100,8570</FONT></TD>
</TR>
<TR><TD WIDTH="5.5%" VALIGN="TOP">
<B><FONT SIZE=3><P ALIGN="JUSTIFY">5</B></FONT></TD>
<B><FONT SIZE=3><P ALIGN="JUSTIFY">Aggressive Growth</B></FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">440,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">550,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">660,000</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">904,200</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">831,864</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">998,237</FONT></TD>
<FONT SIZE=2><P ALIGN="CENTER">1,247,796</FONT></TD>
</TR>
</TABLE>

<B><FONT SIZE=3>
<P>Based on this information above, which investment strategy would you choose?</P>
</FONT></B>
</HTML>'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 19
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('1. Conservative.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('2. Conservative-Balanced.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('3. Balanced-Growth.', 3, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('4. Growth.', 4, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('5. Aggressive Growth.', 5, @QuestionID)

-- question 20
INSERT Question (QuestionTypeID, QuestionDesc, SurveyID)
VALUES (1, 
'How often would you like to meet with your financial planner to review and discuss your portfolio?'
, @SurveyID)
SELECT @@IDENTITY
SET @QuestionID = @@IDENTITY

-- answer 20
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Quarterly.', 1, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Half Yearly.', 2, @QuestionID)
INSERT QuestionAnswer (QuestionAnswerDesc, QuestionAnswerScore, QuestionID)
VALUES ('Yearly.', 3, @QuestionID)
