/*
 * InvRisk.java
 *
 * Created on 10 October 2001, 11:31
 */
/* ============================================================================
 * Modified: 30/40/2003
 * By:       shibaevv
 * Comment:  - Changed class to use com.argus.activex.Reportable interface.
 *             (see generateReport(...) and getReportData(...) )
 *           - Manual selected strategy wasn't saved, because InvRisk survey wasn't 
 *             updated, when QuickQuest survey was changed 
 *            (see stateChange(...) comments!!! )
 * ============================================================================
 */

package com.argus.financials.ui.iso;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.Survey;
import com.argus.financials.report.ReportFields;
import com.argus.financials.report.data.InvRiskData;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.AbstractPanel;

public class InvRisk
    extends AbstractPanel
    implements
        com.argus.financials.ui.Viewable, javax.swing.event.ChangeListener {
    // for report
    protected static InvRisk view;

    // map of survey questions/answers and person answers to these questions
    protected Survey survey;

    protected int currQuestionID = -1;

    protected Survey.Question currQuestion = null;

    protected Object primaryKey;

    public Object getPrimaryKey() {
        return primaryKey;
    }

    /** Creates new form InvRisk */
    protected InvRisk() {
        initComponents();
        initComponents2();
    }

    public static InvRisk getInstance() {
        /*
         * if ( view == null ) view = new InvRisk(); return view;
         */
        if (view != null) {
            com.argus.financials.swing.SwingUtil.setVisible(view, false);
        }

        view = new InvRisk();

        return view;
    }

    public String getViewCaption() {
        return "Risk Evaluation/ISO";
    }

    protected void initComponents2() {

        updateComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelQuestionAnswersOld = new javax.swing.JPanel();
        jPanelQuestionControls = new javax.swing.JPanel();
        jButtonPrevQuestion = new javax.swing.JButton();
        jButtonNextQuestion = new javax.swing.JButton();
        jScrollPaneQuestionAnswers = new javax.swing.JScrollPane();
        jPanelQuestionAnswers = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jPanelMain = new javax.swing.JPanel();
        jPanelQuestionNumberControl = new javax.swing.JPanel();
        jPanelQuestionNumber = new javax.swing.JPanel();
        jLabelQuestionNumber = new javax.swing.JLabel();
        jPanelQuestion = new javax.swing.JPanel();
        jLabelQuestion = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanelAnswers = new javax.swing.JPanel();
        jPanelBottom = new javax.swing.JPanel();
        jPanelRight = new javax.swing.JPanel();
        jPanelResults = new QuickQuest();
        jPanelControls = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(730, 500));
        jTabbedPane.setPreferredSize(new java.awt.Dimension(730, 650));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelQuestionAnswersOld.setLayout(new javax.swing.BoxLayout(
                jPanelQuestionAnswersOld, javax.swing.BoxLayout.Y_AXIS));

        jPanelQuestionControls.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.RIGHT, 20, 5));

        jButtonPrevQuestion.setText("< Previous Question");
        jButtonPrevQuestion.setPreferredSize(new java.awt.Dimension(150, 21));
        jButtonPrevQuestion.setEnabled(false);
        jButtonPrevQuestion
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonPrevQuestionActionPerformed(evt);
                    }
                });

        jPanelQuestionControls.add(jButtonPrevQuestion);

        jButtonNextQuestion.setText("Next Question >");
        jButtonNextQuestion.setPreferredSize(new java.awt.Dimension(150, 21));
        jButtonNextQuestion.setEnabled(false);
        jButtonNextQuestion
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonNextQuestionActionPerformed(evt);
                    }
                });

        jPanelQuestionControls.add(jButtonNextQuestion);

        jPanelQuestionAnswersOld.add(jPanelQuestionControls);

        jPanelQuestionAnswers.setLayout(new javax.swing.BoxLayout(
                jPanelQuestionAnswers, javax.swing.BoxLayout.X_AXIS));

        jPanelQuestionAnswers
                .setPreferredSize(new java.awt.Dimension(453, 250));
        jPanelQuestionAnswers.setMaximumSize(new java.awt.Dimension(65574,
                98301));
        jPanelLeft.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelLeft.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanelLeft.setMaximumSize(new java.awt.Dimension(20, 10));
        jPanelQuestionAnswers.add(jPanelLeft);

        jPanelMain.setLayout(new javax.swing.BoxLayout(jPanelMain,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelQuestionNumberControl.setLayout(new javax.swing.BoxLayout(
                jPanelQuestionNumberControl, javax.swing.BoxLayout.X_AXIS));

        jPanelQuestionNumber.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT, 0, 5));

        jLabelQuestionNumber
                .setText("<HTML><B><FONT FACE=\"Arial\" SIZE=4><P>Question N\n</P></B></FONT></HTML>");
        jPanelQuestionNumber.add(jLabelQuestionNumber);

        jPanelQuestionNumberControl.add(jPanelQuestionNumber);

        jPanelMain.add(jPanelQuestionNumberControl);

        jPanelQuestion.setLayout(new javax.swing.BoxLayout(jPanelQuestion,
                javax.swing.BoxLayout.X_AXIS));

        jPanelQuestion.add(jLabelQuestion);

        jPanelMain.add(jPanelQuestion);

        jSeparator1.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanelMain.add(jSeparator1);

        jPanelAnswers.setLayout(new javax.swing.BoxLayout(jPanelAnswers,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelAnswers.setPreferredSize(new java.awt.Dimension(1000, 1000));
        jPanelAnswers.add(jPanelBottom);

        jPanelMain.add(jPanelAnswers);

        jPanelQuestionAnswers.add(jPanelMain);

        jPanelRight.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelRight.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanelRight.setMaximumSize(new java.awt.Dimension(20, 10));
        jPanelQuestionAnswers.add(jPanelRight);

        jScrollPaneQuestionAnswers.setViewportView(jPanelQuestionAnswers);

        jPanelQuestionAnswersOld.add(jScrollPaneQuestionAnswers);

        jTabbedPane.addTab("Risk Profile", null, jPanelQuestionAnswersOld, "");

        jPanelResults.setLayout(new java.awt.GridLayout(1, 0));

        jTabbedPane.addTab("Results", null, jPanelResults, "");

        add(jTabbedPane);

        jPanelControls.setLayout(new javax.swing.BoxLayout(jPanelControls,
                javax.swing.BoxLayout.X_AXIS));

        jButtonReport.setText("Report");
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanel3.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel3.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanel3.add(jButtonSave);

        jPanelControls.add(jPanel3);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanel4.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanel4.add(jButtonNext);

        jPanelControls.add(jPanel4);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        updateComponents();
    }// GEN-LAST:event_jTabbedPaneStateChanged

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        doSave(evt);
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (isModified()) {
                int result = JOptionPane.showConfirmDialog(this,
                        "Do you want to save data before closing?",
                        "Close dialog", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    doSave(evt);
                    // result = OK_OPTION;
                    doClose(evt);

                } else if (result == JOptionPane.NO_OPTION) {
                    // result = OK_OPTION;
                    doClose(evt);

                } else {
                    // result = CANCEL_OPTION;
                    // keep open
                }

            } else {
                // result = OK_OPTION;
                doClose(evt);
            }

        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    private void jButtonPrevQuestionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPrevQuestionActionPerformed
        jButtonNextQuestion.setEnabled(currQuestion != null);
        currQuestion = survey.getQuestionByNumber(--currQuestionID);
        jButtonPrevQuestion.setEnabled(currQuestion != null
                && (currQuestionID + 1) != 0);

        updateView();
    }// GEN-LAST:event_jButtonPrevQuestionActionPerformed

    private void jButtonNextQuestionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextQuestionActionPerformed
        jButtonPrevQuestion.setEnabled(currQuestion != null
                && (currQuestionID + 1) != 0);
        currQuestion = survey.getQuestionByNumber(++currQuestionID);
        jButtonNextQuestion.setEnabled(currQuestion != null);

        updateView();
    }// GEN-LAST:event_jButtonNextQuestionActionPerformed

    public static void display(java.awt.event.FocusListener[] listeners) {

        InvRisk view = InvRisk.getInstance();

        SwingUtil.add2Frame(view, listeners, view.getViewCaption(),
                ViewSettings.getInstance().getViewImage(
                        view.getClass().getName()), true, true, true);

        try {
            view.updateView(clientService);
        } catch (ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

    /**
     * 
     */
    protected void doSave(java.awt.event.ActionEvent evt) {
        if (survey != null) {
            try {
                saveView(person);
                jButtonSave.setEnabled(survey.isModified());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        // setObject(null);
    }

    protected void doDelete(java.awt.event.ActionEvent evt) {
        // do nothing
    }

    protected void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    protected boolean isModified() {
        return survey != null && survey.isModified();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPrevQuestion;

    private javax.swing.JLabel jLabelQuestionNumber;

    private javax.swing.JPanel jPanelMain;

    private javax.swing.JPanel jPanelQuestionNumber;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JButton jButtonNextQuestion;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JPanel jPanelQuestionAnswersOld;

    private javax.swing.JPanel jPanelBottom;

    private javax.swing.JButton jButtonNext;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.JLabel jLabelQuestion;

    private javax.swing.JPanel jPanelQuestionNumberControl;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanelAnswers;

    private javax.swing.JPanel jPanelQuestion;

    private javax.swing.JScrollPane jScrollPaneQuestionAnswers;

    private javax.swing.JPanel jPanelQuestionControls;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JPanel jPanelQuestionAnswers;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JPanel jPanelResults;

    // End of variables declaration//GEN-END:variables

    /**
     * Viewble interface
     */
    public Integer getObjectType() {
        return null;
    }

    private com.argus.financials.service.PersonService person;

    public void updateView(com.argus.financials.service.PersonService person)
            throws ServiceException {

        this.person = person;
        Integer surveyID = null;
        this.survey = null;

        try {
            surveyID = person
                    .getSurveyID(LinkObjectTypeConstant.SURVEY_2_RISKPROFILE);
            setObject(person.getSurvey(surveyID));

            this.survey = person.getSurvey(surveyID); // ???
        } catch (ObjectNotFoundException e) {
            e.printStackTrace(System.err);
        }

    }

    public void saveView(com.argus.financials.service.PersonService person)
            throws ServiceException {

        ((QuickQuest) jPanelResults).saveView(person);

        if (survey == null) {
            System.err.println("survey == null");
            return;
        }

        // save changes to database
        person.setSurvey(survey);
        person.storeSurveys();

        survey.setModified(false);
    }

    public void clearView() {
        SwingUtil.clear(this);
        ((QuickQuest) jPanelResults).clearView();
    }

    public Object getObject() {
        return survey;
    }

    public void setObject(Object value) {
        if (value == null) {
            if (survey != null) {
                survey.removeChangeListener(this);
                survey.removeChangeListener((QuickQuest) jPanelResults);
            }

            survey = null;
            ((QuickQuest) jPanelResults).setObject(null);

            clearView();
            return;
        }

        if (value.equals(survey))
            return;

        setObject(null);
        survey = (Survey) value;

        jButtonPrevQuestion.setEnabled(survey != null
                && (currQuestionID + 1) != 0);
        jButtonNextQuestion.setEnabled(survey != null);

        SwingUtil.setTitle(this, getViewCaption() + " - "
                + survey.getSurveyTitle());

        currQuestionID = 0;
        currQuestion = survey.getQuestionByNumber(currQuestionID);

        survey.addChangeListener(this);

        updateView();

        // update
        ((QuickQuest) jPanelResults).setObject(survey);

        survey.setModified(false);

    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        if (changeEvent.getSource() instanceof Survey) {
            Survey s = (Survey) changeEvent.getSource();
            // DATE: 30/04/2003
            // ATTENTION: Strange behaviour of the survey object here, setting
            // the investment
            // strategy manually in the QuickQuest view doesn't update the
            // strategy
            // in the InvRisk view. Both use the same Survey object!
            // FIX: Setting the investment strategy again!
            // BEGIN-FIX: 30/04/2003
            survey = s;
            // END-FIX: 30/04/2003
            jButtonSave.setEnabled(s.isModified());
        }
    }

    /**
     * helper methods
     */
    protected void updateView() {

        if (currQuestion == null)
            return;

        jLabelQuestionNumber.setText(currQuestion.getQuestionNumberHTML());
        jLabelQuestion.setText(currQuestion.getQuestionDescHTML());

        jPanelAnswers.removeAll();
        jPanelAnswers.repaint();

        Component[] answers = currQuestion.getAnswerComponents();
        if (answers != null) {
            for (int i = 0; i < answers.length; i++)
                jPanelAnswers.add(answers[i]);
            jPanelAnswers.add(jPanelBottom);
        }

        // BEGIN BUG FIX 479 - 17/07/2002
        // by shibaevv
        // Question number 18 and 19:
        // - question + answers text are to long to be displayed on one page
        // JScrollPane (jScrollPaneQuestionAnswers) was added to fix that
        // problem
        // 
        // ATTENTION: The prefered size of the panel which holds the questions
        // needs to be set manually!!!
        jScrollPaneQuestionAnswers.remove(jPanelQuestionAnswers);

        // Displaying question 18 or 19?
        if (currQuestion.getQuestionNumber() == 18
                || currQuestion.getQuestionNumber() == 19) {
            // yes, increase display size => scrollbar will appear
            jPanelQuestionAnswers.setPreferredSize(new java.awt.Dimension(500,
                    960));
        } else {
            // no, everything fits on one page
            jPanelQuestionAnswers.setPreferredSize(new java.awt.Dimension(500,
                    400));
        }

        jScrollPaneQuestionAnswers.add(jPanelQuestionAnswers);
        jScrollPaneQuestionAnswers.setViewportView(jPanelQuestionAnswers);
        // END: BUG FIX 479 - 17/07/2002

        SwingUtil.pack(this);

    }

    protected void updateComponents() {
        jButtonPrevQuestion.setEnabled(currQuestion != null
                && (currQuestionID + 1) != 0);
        jButtonPrevious.setEnabled(jTabbedPane.getSelectedIndex() > 0);
        jButtonNext.setEnabled(jTabbedPane.getSelectedIndex() < jTabbedPane
                .getTabCount() - 1);
    }

    public Survey getSurvey() {
        return this.survey;
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getISOReport();
    }

    public ReportFields getReportData(PersonService person) throws java.io.IOException {

        // new reporting ( using pre-defined map(field,value) )
        ReportFields reportFields = ReportFields.getInstance();
        InvRiskData _data = new InvRiskData();

        // _data.init( person );

        if (survey != null && survey.getRiskProfileID() != null) {
            _data.init(survey);
        } else if (survey != null) {
            _data.init(survey.getSelectedInvestmentStrategy());
        }
        _data.initializeReportData(reportFields, person);

        return reportFields;

    }

    protected void doReport() {

        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(person),
                    getDefaultReport());

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
        }

    }

}
