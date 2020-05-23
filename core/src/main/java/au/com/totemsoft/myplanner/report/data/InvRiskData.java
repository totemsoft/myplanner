/*
 * InvRiskData.java
 *
 * Created on 18 September 2002, 09:30
 */
/* ============================================================================
 * Modified: 30/40/2003
 * By:       shibaevv
 * Comment:  Changed class to support au.com.totemsoft.activex.Reportable interface
 * ============================================================================
 */
package au.com.totemsoft.myplanner.report.data;

import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.code.InvestmentStrategyData;
import au.com.totemsoft.myplanner.etc.GrowthRate;
import au.com.totemsoft.myplanner.etc.Survey;
import au.com.totemsoft.myplanner.service.PersonService;

public class InvRiskData // extends BaseData
        extends au.com.totemsoft.myplanner.bean.AbstractBase implements
        au.com.totemsoft.myplanner.report.Reportable,
        javax.swing.event.ChangeListener {
    private static final String STRING_EMPTY = "";

    static final int MAX_REPORT_LENGTH_QUESTIONS = 256; // need to limit strings
                                                        // to 256, if not you'll
                                                        // get an exception
                                                        // "com.ibm.bridge2java.ComException:
                                                        // String parameter too
                                                        // long."

    static final int MAX_REPORT_LENGTH_ANSWERS = 256; // need to limit strings
                                                        // to 256, if not you'll
                                                        // get an exception
                                                        // "com.ibm.bridge2java.ComException:
                                                        // String parameter too
                                                        // long."

    protected QuestionAnswerTableModel qa_table = null;

    public RiskRating rr;

    public RecommendedAssetAllocation raa;

    public String InvRiskProfileClientFullName = STRING_EMPTY;

    protected au.com.totemsoft.math.Percent percent = new au.com.totemsoft.math.Percent();

    /** Creates new DSSData */
    public InvRiskData() {
        rr = new RiskRating();
        raa = new RecommendedAssetAllocation();
    }

    public class RiskRating implements java.io.Serializable {
        public String riskProfileRating = STRING_EMPTY;

        public String incomeReturn = STRING_EMPTY;

        public String capitalGrowthReturn = STRING_EMPTY;

        public String totalReturn = STRING_EMPTY;

        public String defensiveAsset = STRING_EMPTY;

        public String growthAsset = STRING_EMPTY;

        public String rangeOfOutcomes_1yr = STRING_EMPTY;

        public String rangeOfOutcomes_in_2_out_of_3_Years = STRING_EMPTY;
    }

    public class RecommendedAssetAllocation implements java.io.Serializable {
        public String cash = STRING_EMPTY;

        public String fixedInterest = STRING_EMPTY;

        public String listedProperty = STRING_EMPTY;

        public String australianShares = STRING_EMPTY;

        public String internationalShares = STRING_EMPTY;

        public String total = STRING_EMPTY;
    }

    public void init(au.com.totemsoft.myplanner.service.PersonService person)
            throws java.io.IOException {
        init(person, true);
    }

    /*
     * qa_table - true = create question/answer table, false = no table (faster,
     * used in planwriter)
     */
    public void init(au.com.totemsoft.myplanner.service.PersonService person,
            boolean create_qa_table) throws java.io.IOException {
        if (person != null) {
            InvRiskProfileClientFullName = person.getPersonName().getFullName();

            /*
             * get a person's survey
             */
            try {
                Integer surveyID = person
                        .getSurveyID(LinkObjectTypeConstant.SURVEY_2_RISKPROFILE);
                Survey survey = person.getSurvey(surveyID);

                if (survey != null && survey.getRiskProfileID() != null) {
                    init(survey, create_qa_table);
                } else if (survey != null) {
                    init(survey.getSelectedInvestmentStrategy());
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace(System.err);
            }
        } else {
            qa_table = new QuestionAnswerTableModel();
        }
    }

    public void init(au.com.totemsoft.myplanner.etc.Survey survey)
            throws java.io.IOException {
        init(survey, true);
    }

    /*
     * qa_table - true = create question/answer table, false = no table (faster,
     * used in planwriter)
     */
    public void init(au.com.totemsoft.myplanner.etc.Survey survey,
            boolean create_qa_table) throws java.io.IOException {

        if (create_qa_table) {
            // initialise questions/answers table
            qa_table = new QuestionAnswerTableModel(survey);
        } else {
            qa_table = new QuestionAnswerTableModel();
        }

        Integer investmentStrategyCodeID = survey.getRiskProfileID();
        this.initStrategyValues(investmentStrategyCodeID);
    }

    public void init(Integer investmentStrategyCodeID)
            throws java.io.IOException {
        // !!!ATTENTION: The copying of the questions and their answers to the
        // "InvRiskData" class is NOT dynamic, so when the number
        // of question changes, this code has to be changed too!!!
        qa_table = new QuestionAnswerTableModel();

        this.initStrategyValues(investmentStrategyCodeID);
    }

    private void initStrategyValues(Integer investmentStrategyCodeID) {

        if (investmentStrategyCodeID != null) {
            InvestmentStrategyData isd = new InvestmentStrategyData();

            rr.riskProfileRating = isd
                    .getCodeDescription(investmentStrategyCodeID);

            double[] qq_data = null;
            double[][] qq_data2 = null;

            GrowthRate gr = isd.getGrowthRate(investmentStrategyCodeID);
            isd.getAllocationData2(investmentStrategyCodeID);

            // rr.incomeReturn = new Double( gr.getIncomeRate() ).toString() +
            // "%";
            // rr.capitalGrowthReturn = new Double( gr.getGrowthRate()
            // ).toString() + "%";
            // rr.totalReturn = new Double( gr.getRate() ).toString() + "%";
            rr.incomeReturn = percent.toString(gr.getIncomeRate());
            rr.capitalGrowthReturn = percent.toString(gr.getGrowthRate());
            rr.totalReturn = percent.toString(gr.getRate());

            qq_data2 = isd.getAllocationHistData2(investmentStrategyCodeID);
            rr.rangeOfOutcomes_1yr = percent.toString(qq_data2[0][0]) + ".."
                    + percent.toString(qq_data2[1][0]);
            rr.rangeOfOutcomes_in_2_out_of_3_Years = percent
                    .toString(qq_data2[0][1])
                    + ".." + percent.toString(qq_data2[1][1]);

            // jTextFieldROO5Years.setText(
            // data2[0][2] == data2[1][2] ? data2[0][2] + "%.." : "" +
            // data2[0][2] + "%.." + data2[1][2] + "%" );

            // rr.defensiveAsset = (percent.toString( gr.getDefensiveRate() ) ==
            // null ) ? percent.toString( 0.0 ) : percent.toString(
            // gr.getDefensiveRate() );
            // rr.growthAsset = (percent.toString( 100 - gr.getDefensiveRate() )
            // == null ) ? percent.toString( 0.0 ) : percent.toString( 100 -
            // gr.getDefensiveRate() );

            qq_data = isd.getAllocationData(investmentStrategyCodeID);

            // qqdata[n] can have value like 1.79...E308
            double defensiveAsset = 0.0;
            if (qq_data[0] <= 100.0) {
                defensiveAsset += qq_data[0];
            }
            if (qq_data[1] <= 100.0) {
                defensiveAsset += qq_data[1];
            }

            double growthAsset = 0.0;
            if (qq_data[2] <= 100.0) {
                growthAsset += qq_data[2];
            }
            if (qq_data[3] <= 100.0) {
                growthAsset += qq_data[3];
            }
            if (qq_data[4] <= 100.0) {
                growthAsset += qq_data[4];
            }

            rr.defensiveAsset = (percent.toString(defensiveAsset) == null) ? percent
                    .toString(0.0)
                    : percent.toString(defensiveAsset);
            rr.growthAsset = (percent.toString(growthAsset) == null) ? percent
                    .toString(0.0) : percent.toString(growthAsset);

            raa.cash = (percent.toString(qq_data[0]) == null) ? percent
                    .toString(0.0) : percent.toString(qq_data[0]);
            raa.fixedInterest = (percent.toString(qq_data[1]) == null) ? percent
                    .toString(0.0)
                    : percent.toString(qq_data[1]);
            raa.listedProperty = (percent.toString(qq_data[2]) == null) ? percent
                    .toString(0.0)
                    : percent.toString(qq_data[2]);
            raa.australianShares = (percent.toString(qq_data[3]) == null) ? percent
                    .toString(0.0)
                    : percent.toString(qq_data[3]);
            raa.internationalShares = (percent.toString(qq_data[4]) == null) ? percent
                    .toString(0.0)
                    : percent.toString(qq_data[4]);

            // calculate total in perecent
            // total = Cash + FixedInterest + Property + Australian Shares +
            // International Shares
            // !!! data array isn't initialized to 0.0, fields can contain
            // 1.7976931348623157E308 = infinte !!!
            double total = 0.0;
            // add only if we have a percent value
            for (int i = 0; i < qq_data.length; i++) {
                if (qq_data[i] >= 0.0 && qq_data[i] <= 100.0) {
                    total += qq_data[i];
                }
            }
            raa.total = (percent.toString(total) == null) ? percent
                    .toString(0.0) : percent.toString(total);

        } else {
            // not all questions are answere, so we haven't any risk profile
            // rating
            rr.riskProfileRating = STRING_EMPTY;
            rr.incomeReturn = STRING_EMPTY;
            rr.capitalGrowthReturn = STRING_EMPTY;
            rr.totalReturn = STRING_EMPTY;
            rr.rangeOfOutcomes_1yr = STRING_EMPTY;
            rr.rangeOfOutcomes_in_2_out_of_3_Years = STRING_EMPTY;
            rr.defensiveAsset = STRING_EMPTY;
            rr.growthAsset = STRING_EMPTY;

            raa.cash = STRING_EMPTY;
            raa.fixedInterest = STRING_EMPTY;
            raa.listedProperty = STRING_EMPTY;
            raa.australianShares = STRING_EMPTY;
            raa.internationalShares = STRING_EMPTY;
            raa.total = STRING_EMPTY;
        }

    }

    /**
     * Check a given question if it's not null and if it's description is not
     * null.
     * 
     * @param a
     *            question
     * @return the question's description or ""
     */
    private String checkQuestion(Survey.Question q) {
        String help = (q == null && q.getQuestionDesc() == null) ? "" : q
                .getQuestionDesc();
        return deleteHTMLTags(truncateString(help, MAX_REPORT_LENGTH_QUESTIONS));
    }

    /**
     * Gets all answers to a given question and copys them into one String, each
     * answers is in a new line.
     * 
     * @param a
     *            question
     * @return all answers to a question
     */
    private String getAllAnswers(Survey.Question q) {
        StringBuffer answers = new StringBuffer();

        if (q != null && q.getSelectedAnswers() != null) {
            Object[] obj = q.getSelectedAnswers();
            for (int i = 0; i < obj.length; i++) {
                if (i != 0) {
                    answers.append("\n");
                }
                Survey.Question.Answer a = (Survey.Question.Answer) obj[i];
                answers.append(a.getAnswerDesc());
            }
        }

        return deleteHTMLTags(truncateString(answers.toString(),
                MAX_REPORT_LENGTH_ANSWERS));
    }

    /**
     * Deletes HTML tags from a String. It looks for a '<' and ignores every
     * character until '>'.
     * 
     * @param a
     *            String
     * @return the given String without HTML tags
     */
    private String deleteHTMLTags(String str) {
        StringBuffer help = new StringBuffer();
        char c;
        boolean html_tag = false;

        if (str != null && str.length() > 0) {
            for (int i = 0; i < str.length(); i++) {
                c = str.charAt(i);

                if (c == '<') {
                    html_tag = true;
                    continue;
                }
                if (c == '>') {
                    html_tag = false;
                    continue;
                }

                if (!html_tag) {
                    // replace new line with space
                    c = (c == '\n') ? ' ' : c;
                    help.append(c);
                }
            }
        }
        return help.toString();
    }

    /**
     * Truncates a given String and returns a new String with only the given
     * number of chars.
     * 
     * @param a
     *            String
     * @param number
     *            of chars for the "new" String
     * @return a truncated String
     */
    private String truncateString(String str, int no_chars) {
        String help = "";

        if (str != null && str.length() > no_chars) {
            help = str.substring(0, no_chars);
            help = help.concat("...");
        } else {
            help = str;
        }

        return help;

    }

    /*
     * ============================================================================
     * BEGIN: TableModels
     * ============================================================================
     */
    /***************************************************************************
     * Questions/Anwers TableModel
     **************************************************************************/
    public class QuestionAnswerTableModel extends
            au.com.totemsoft.myplanner.table.swing.SmartTableModel {
        protected Integer surveyID = null;

        protected Integer investmentStrategyCodeID = null;

        protected au.com.totemsoft.myplanner.etc.Survey survey = null;

        protected java.util.Vector columnNames = new java.util.Vector();

        protected java.util.Vector columnClasses = new java.util.Vector();

        public final int COLUMN_ONE = 0;

        /** Creates a new instance of AssetAllocationTableModel */
        public QuestionAnswerTableModel(PersonService person)
                throws java.io.IOException {
            /*
             * get a person's survey
             */
            try {
                surveyID = person
                        .getSurveyID(LinkObjectTypeConstant.SURVEY_2_RISKPROFILE);
                survey = person.getSurvey(surveyID);
                investmentStrategyCodeID = survey.getRiskProfileID();
            } catch (ObjectNotFoundException e) {
                e.printStackTrace(System.err);
            }

            generalInit();
            questionAnswersInit();
        }

        public QuestionAnswerTableModel(au.com.totemsoft.myplanner.etc.Survey survey)
                throws java.io.IOException {
            if (survey != null) {
                this.survey = survey;
                this.surveyID = survey.getId();
                this.investmentStrategyCodeID = survey.getRiskProfileID();
            }

            generalInit();
            questionAnswersInit();
        }

        public QuestionAnswerTableModel() throws java.io.IOException {
            generalInit();
            questionAnswersInit();
        }

        /*
         * Initialize the table column names, classes and data vector
         */
        private void generalInit() {
            columnNames.add("Questions/Answers");
            columnClasses.add(java.lang.String.class);

            setColumnNames(columnNames);
            setColumnClasses(columnClasses);

            // init tablemodel structure
            java.util.Vector data = new java.util.Vector();
            setData(data);
        }

        /*
         * Populate the table with the questions and answers. One question per
         * row, one question per answer. Question row type is HEADER, answere
         * row type is BODY
         */
        private void questionAnswersInit() throws java.io.IOException {
            /*
             * transfer data from survey to table
             */
            if (survey != null) {
                // get table's data
                java.util.Vector data = getData();

                QuestionAnswerTableRow irtr_question = null;
                QuestionAnswerTableRow irtr_answer = null;
                // QuestionAnswerTableRow irtr_empty = null;

                int n = 0; // TODO: survey.getQuestionsCount();
                Survey.Question q;
                while ((q = survey.getQuestionByNumber(n++)) != null) {

                    String id = STRING_EMPTY;
                    String question = STRING_EMPTY;
                    String answer = STRING_EMPTY;

                    id = "" + n;

                    // question 18 needs to be added manual,
                    // because it's based completely on HTML and it's to long
                    if (n == 19) {
                        question = "Based on this information above, which investment strategy would you choose?";
                    } else {
                        question = (checkQuestion(q) == null) ? STRING_EMPTY
                                : checkQuestion(q);
                    }
                    answer = (getAllAnswers(q) == null) ? STRING_EMPTY
                            : getAllAnswers(q);

                    // create, populate and add new table row (question)
                    irtr_question = new QuestionAnswerTableRow(id + ". "
                            + question, QuestionAnswerTableRow.HEADER1);
                    data.add(irtr_question);
                    // create, populate and add new table row (answere)
                    irtr_answer = new QuestionAnswerTableRow("\n" + answer
                            + "\n", QuestionAnswerTableRow.BODY);
                    data.add(irtr_answer);
                    // create, populate and add new table row (an empty row)
                    // irtr_empty = new QuestionAnswerTableRow( "",
                    // QuestionAnswerTableRow.BODY );
                    // data.add( irtr_empty );

                } // end-while
            } // end-if
        }

        public class QuestionAnswerTableRow extends AbstractSmartTableRow {
            private java.util.Vector rowData; // String,
                                                // Numeric(Percent/Money)

            /** Creates a new instance of AssetAllocationRow */
            public QuestionAnswerTableRow(String str, int type) {
                super(type);

                rowData = new java.util.Vector(getData().size());
                rowData.add(COLUMN_ONE, str);
                for (int c = 1; c < getData().size(); c++)
                    rowData.add(null);
            }

            public String toString() {
                return (String) rowData.elementAt(COLUMN_ONE);
            }

            protected java.util.Vector getRowData() {
                return rowData;
            }

            public Object getValueAt(int columnIndex) {
                return toString();
            }

            public void setValueAt(Object obj, int columnIndex) {
                rowData.setElementAt(obj, columnIndex);
            }

        }

        protected au.com.totemsoft.myplanner.etc.Survey getSurvey() {
            return survey;
        }

        protected Integer getSurveyID() {
            return surveyID;
        }

        protected Integer getInvestmentStrategyCodeID() {
            return investmentStrategyCodeID;
        }

    }

    /*
     * ============================================================================
     * END: TableModels
     * ============================================================================
     */

    /***************************************************************************
     * au.com.totemsoft.activex.Reportable interface
     **************************************************************************/
    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields, clientService);
    }

    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields,
            au.com.totemsoft.myplanner.service.PersonService person)
            throws java.io.IOException {
        if (person != null) {
            // we have to initialize ReportFields with the client person
            reportFields.initialize(clientService);
            // and we use the "parameter" person object to set the name for this
            // risk profile report,
            // because it can be the client or client's partner
            InvRiskProfileClientFullName = person.getPersonName().getFullName();
        }

        // this.init( person );

        reportFields.setValue(
                reportFields.InvRisk_InvRiskProfileClientFullName,
                InvRiskProfileClientFullName);

        reportFields.setValue(reportFields.InvRisk_QuestionaryTable, qa_table);

        reportFields.setValue(
                reportFields.InvRisk_RiskRating_RiskProfileRating,
                rr.riskProfileRating);
        reportFields.setValue(reportFields.InvRisk_RiskRating_IncomeReturn,
                rr.incomeReturn);
        reportFields.setValue(
                reportFields.InvRisk_RiskRating_CapitalGrowthReturn,
                rr.capitalGrowthReturn);
        reportFields.setValue(reportFields.InvRisk_RiskRating_TotalReturn,
                rr.totalReturn);

        reportFields.setValue(reportFields.InvRisk_RiskRating_DefensiveAsset,
                rr.defensiveAsset);
        reportFields.setValue(reportFields.InvRisk_RiskRating_GrowthAsset,
                rr.growthAsset);
        reportFields.setValue(
                reportFields.InvRisk_RiskRating_RangeOfOutcomes_1yr,
                rr.rangeOfOutcomes_1yr);
        reportFields
                .setValue(
                        reportFields.InvRisk_RiskRating_RangeOfOutcomes_in_2_out_of_3_Years,
                        rr.rangeOfOutcomes_in_2_out_of_3_Years);

        reportFields.setValue(reportFields.InvRisk_AA_Recommended_Cash,
                raa.cash);
        reportFields.setValue(
                reportFields.InvRisk_AA_Recommended_FixedInterest,
                raa.fixedInterest);
        reportFields.setValue(
                reportFields.InvRisk_AA_Recommended_ListedProperty,
                raa.listedProperty);
        reportFields.setValue(
                reportFields.InvRisk_AA_Recommended_AustralianShares,
                raa.australianShares);
        reportFields.setValue(
                reportFields.InvRisk_AA_Recommended_InternationalShares,
                raa.internationalShares);
        reportFields.setValue(reportFields.InvRisk_AA_Recommended_Total,
                raa.total);

    }

}
