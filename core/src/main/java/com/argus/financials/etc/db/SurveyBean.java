/*
 * SurveyBean.java
 *
 * Created on 16 October 2001, 14:35
 */

package com.argus.financials.etc.db;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.etc.Survey;

public class SurveyBean extends AbstractPersistable {

    protected Survey survey; // aggregation

    private int linkID = 0;

    // First level of linkage (e.g. objectTypeID1 = PERSON or BUSINESS),
    // objectTypeID2 = SURVEY
    private int[] linkObjectTypes = new int[1];

    /** Creates new SurveyBean */
    public SurveyBean() {
    }

    public SurveyBean(Survey survey) {
        this.survey = survey;
    }

    public Class getSurveyClass() {
        return Survey.class;
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.SURVEY;
    }

    protected int getLinkObjectTypeID(int level) {
        switch (level) {
        case 1:
            return linkObjectTypes[0];
        default:
            throw new RuntimeException(
                    "SurveyBean.getLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    public void setLinkObjectTypeID(int level, int value) {
        switch (level) {
        case 1: {
            setModified(linkObjectTypes[0] != value);
            linkObjectTypes[0] = value;
            break;
        }
        default:
            throw new RuntimeException(
                    "SurveyBean.setLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    /**
     * IPersistable methods
     */
    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getId(), con);
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        boolean newConnection = (con == null);
        PreparedStatement sql = null;
        ResultSet rs = null;

        if (newConnection)
            con = getConnection();

        try {
            sql = con.prepareStatement(
                  "SELECT SurveyTitle"
                + " , q.QuestionID, q.QuestionDesc, q.QuestionTypeID"
                + " , qa.QuestionAnswerID, qa.QuestionAnswerDesc, qa.QuestionAnswerScore"
                + " , l.LinkID"
                + " FROM Survey s LEFT OUTER JOIN Link l ON s.SurveyID = l.ObjectID2"
                + ", Question q, QuestionAnswer qa"
                + " WHERE"
                + " (s.SurveyID = ?)"
                + " AND (l.ObjectID1=?) AND (l.LinkObjectTypeID=?) AND (l.LogicallyDeleted IS NULL)"
                + " AND (s.SurveyID = q.SurveyID) AND (q.QuestionID = qa.QuestionID)"
                + " ORDER BY q.QuestionID, qa.QuestionAnswerID"
            );

            int i = 0;
            sql.setInt(++i, primaryKeyID.intValue()); //10003
            sql.setInt(++i, getOwnerId().intValue()); //10013
            sql.setInt(++i, getLinkObjectTypeID(1)); //1024
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException(
                    "Can not find survey: " + primaryKeyID + " for Owner: " + getOwnerId());

            setSurveyTitle(rs.getString("SurveyTitle"));

            load(rs);
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (sql != null) {
                sql.close();
                sql = null;
            }

            setQuestionAnswers(con);

            // has to be last (to be safe)
            setId(primaryKeyID);

            // get SelectedRiskProfile
            int for_person_survey_linkID = linkObjectDao
                    .getLinkID(getOwnerId(), // e.g. PersonID
                            primaryKeyID, // e.g. SurveyID
                            getLinkObjectTypeID(1), // e.g. PERSON_2_SURVEY
                            con);

            sql = con.prepareStatement("SELECT" + " SelectedRiskProfile"
                    + " FROM" + " PersonSurvey" + " WHERE"
                    + " (PersonSurveyID = ?)");
            i = 0;
            sql.setInt(++i, for_person_survey_linkID);

            rs = sql.executeQuery();

            if (rs.next()) {
                this.survey.setSelectedInvestmentStrategy((Integer) rs
                        .getObject("SelectedRiskProfile"));
            } else {
                this.survey.setSelectedInvestmentStrategy(null);
            }
        } finally {
            close(rs, sql);
            if (newConnection && con != null)
                con.close();
        }

        setModified(false);

    }

    /**
     * STRUCTURE Map( QuestionID, Question ) Question = Map( QuestionAnswerID,
     * Answer )
     */
    public void load(ResultSet rs) throws SQLException {

        // find this survey link to another object (e.g. person)
        linkID = 0;

        // this record
        Integer questionID = null;
        String questionDesc = null;
        Integer questionTypeID = null;
        Integer questionAnswerID = null;
        String questionAnswerDesc = null;
        int questionAnswerScore = 0;
        // prev record
        Integer prevQuestionID = null;

        Survey.Question q = null;

        while (rs.next()) {

            questionID = (Integer) rs.getObject("QuestionID");
            questionDesc = rs.getString("QuestionDesc");
            questionTypeID = (Integer) rs.getObject("QuestionTypeID");
            questionAnswerID = (Integer) rs.getObject("QuestionAnswerID");
            questionAnswerDesc = rs.getString("QuestionAnswerDesc");
            questionAnswerScore = rs.getInt("QuestionAnswerScore");

            // get XXXSurveyID
            if (linkID == 0)
                linkID = rs.getInt("LinkID");

            if (prevQuestionID == null)
                prevQuestionID = questionID;

            // reset this question if we moved to next question
            if (!questionID.equals(prevQuestionID))
                q = null;

            // .. and create new one
            if (q == null) {
                q = getSurvey().new Question(questionID);
                q.questionTypeID = questionTypeID;
                q.questionDesc = questionDesc;
                getSurvey().addQuestion(q);
            }

            q.addAnswer(questionAnswerID, questionAnswerDesc,
                    questionAnswerScore);

            // update prev value
            prevQuestionID = questionID;

        }

        setModified(false);

    }

    // set answers selected/text property
    private void setQuestionAnswers(Connection con) throws SQLException {

        if (linkID <= 0)
            return;

        PreparedStatement sql = null;
        ResultSet rs = null;

        int personSurveyAnswerID;
        Integer questionID = null;
        Integer questionAnswerID = null;
        String questionAnswerText = null;

        try {
            sql = con
                    .prepareStatement("SELECT psa.PersonSurveyAnswerID, QuestionID, QuestionAnswerID, QuestionAnswerText"
                            + " FROM PersonSurveyAnswer psa, PersonSurveyAnswerText psat"
                            + " WHERE PersonSurveyID=? AND psa.PersonSurveyAnswerID*=psat.PersonSurveyAnswerID");

            sql.setInt(1, linkID);

            rs = sql.executeQuery();

            while (rs.next()) {
                personSurveyAnswerID = rs.getInt("PersonSurveyAnswerID");
                questionID = (Integer) rs.getObject("QuestionID");
                questionAnswerID = (Integer) rs.getObject("QuestionAnswerID");
                questionAnswerText = rs.getString("QuestionAnswerText");

                getSurvey().setQuestionAnswer(questionID, questionAnswerID,
                        true, questionAnswerText);
            }

        } finally {
            linkID = 0;
            close(rs, sql);
        }

    }

    public int store(Connection con) throws SQLException {

        if (survey == null)
            return 0;

        if (!isModified())
            return 0;

        // get link (PersonSurveyID)
        linkID = linkObjectDao.getLinkID(getOwnerId(), // e.g.
                                                                                // PersonID
                getId(), // e.g. SurveyID
                getLinkObjectTypeID(1), // e.g. PERSON_2_SURVEY
                con);

        if (linkID > 0) { // 04/12/2002 && isReady() ) {
            // logically delete this person survey
            remove(con);
            linkID = 0;
        }

        if (linkID <= 0)
            linkID = linkObjectDao.link(getOwnerId(), // e.g.
                                                                                // PersonID
                    getId(), // e.g. SurveyID
                    getLinkObjectTypeID(1), // e.g. PERSON_2_SURVEY
                    false, // create new link (this survey is modified or not
                            // saved yet)
                    con);

        PreparedStatement sql = null;

        try {
            // do insert into PersonSurvey table
            sql = con.prepareStatement("INSERT INTO PersonSurvey"
                    + " (PersonSurveyID, SelectedRiskProfile)" + " VALUES"
                    + " (?,?)");
            sql.setInt(1, linkID);

            if (survey.getSelectedInvestmentStrategy() == null)
                sql.setNull(2, java.sql.Types.DECIMAL);
            else
                sql
                        .setInt(2, survey.getSelectedInvestmentStrategy()
                                .intValue());

            sql.executeUpdate();
            close(null, sql);
            sql = null;

            // do insert into PersonSurveyAnswer table
            sql = con.prepareStatement("INSERT INTO PersonSurveyAnswer"
                    + " (PersonSurveyID,QuestionID,QuestionAnswerID)"
                    + " VALUES" + " (?,?,?)");
            sql.setInt(1, linkID);

            int questionNum = 0;

            Survey.Question q = survey.getQuestionByNumber(questionNum = 0);
            while (q != null) {
                sql.setInt(2, q.questionID.intValue());

                Object[] answers = q.getSelectedAnswers();
                if (answers != null) {
                    for (int i = 0; i < answers.length; i++) {
                        Survey.Question.Answer a = (Survey.Question.Answer) answers[i];

                        sql.setInt(3, a.answerID.intValue());
                        sql.executeUpdate();

                        if (a.getAnswerText() != null)
                            a.personSurveyAnswerID = getIdentityID(con);
                    }
                }

                q = survey.getQuestionByNumber(++questionNum);
            }
            close(null, sql);
            sql = null;

            // do insert into PersonSurveyAnswerText table
            sql = con.prepareStatement("INSERT INTO PersonSurveyAnswerText"
                    + " (PersonSurveyAnswerID,QuestionAnswerText)" + " VALUES"
                    + " (?,?)");

            q = survey.getQuestionByNumber(questionNum = 0);
            while (q != null) {
                Object[] answers = q.getSelectedAnswers();
                if (answers != null) {
                    for (int i = 0; i < answers.length; i++) {
                        Survey.Question.Answer a = (Survey.Question.Answer) answers[i];

                        if (a.personSurveyAnswerID == 0)
                            continue;

                        sql.setInt(1, a.personSurveyAnswerID);
                        sql.setString(2, a.getAnswerText());
                        sql.executeUpdate();
                    }
                }

                q = survey.getQuestionByNumber(++questionNum);
            }
        } finally {
            close(null, sql);
        }

        setModified(false);

        return linkID;

    }

    public void remove(Connection con) throws SQLException {
        linkObjectDao.unlink(getOwnerId(), // e.g.
                                                                    // PersonID
                getId(), // e.g. SurveyID
                getLinkObjectTypeID(1), // e.g. PERSON_2_SURVEY
                con);
    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * get/set methods
     */
    public Survey getSurvey() {
        if (survey == null)
            survey = new Survey();
        return survey;
    }

    public void setSurvey(Survey value) {
        if (value != null && !value.getClass().equals(getSurveyClass()))
            throw new RuntimeException(
                    "SurveyBean.setSurvey( incompatible Class )");

        survey = value;
    }

    public boolean isModified() {
        return getSurvey().isModified();
    }

    public void setModified(boolean value) {
        getSurvey().setModified(value);
    }

    public boolean isReady() {
        return getSurvey().isReady();
    }

    public Integer getId() {
        return getSurvey().getId();
    }

    public void setId(Integer value) {
        getSurvey().setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getSurvey().getOwnerPrimaryKeyID();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getSurvey().setOwnerPrimaryKeyID(value);
    }

    public String getSurveyTitle() {
        return getSurvey().getSurveyTitle();
    }

    public void setSurveyTitle(String value) {
        getSurvey().setSurveyTitle(value);
    }

}