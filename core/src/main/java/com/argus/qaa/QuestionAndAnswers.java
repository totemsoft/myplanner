
package com.argus.qaa;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.text.JTextComponent;

//import com.argus.financials.code.InvestmentStrategyCode;

public class QuestionAndAnswers
{
    private boolean modified = false;

    private String surveyTitle;

    /**
     * STRUCTURE Map( QuestionID, Question ) Question = Map( QuestionAnswerID,
     * Answer )
     */
    private Map questions = new TreeMap();

    transient private SurveyTableModel tableModel;

    transient private SurveyListSelectionListener listSelectionListener;

    transient private javax.swing.JComboBox answerComboBox;

    private static final String alfa = "abcdefghijklmnopqrstuvwxyz";

    /** Creates new Survey */
    public QuestionAndAnswers() {
    }

    public javax.swing.JComboBox getAnswerComboBox() {
        if (answerComboBox == null) {
            answerComboBox = new javax.swing.JComboBox(
                    new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i",
                            "g", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                            "t", "u", "v", "w", "x", "y", "z" });
        }

        return answerComboBox;
    }

    public static String getAsHTML(String text) {
        return "<HTML><FONT SIZE=3><P ALIGN=\"LEFT\">" + text
                + "</P></FONT></HTML>";
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean value) {
        modified = value;
    }

    /**
     * structure to hold question info
     */
    public class Question implements java.io.Serializable {

        public class Answer implements java.io.Serializable {

            public int personSurveyAnswerID;

            public Integer answerID;

            private int answerScore;

            String answerDesc;

            private int answerIndex = -1; // 0, 1, 2, ..

            private boolean selected = false; // serialize this,

            transient private Component comp; // instead of component

            private String answerText;

            // 
            public boolean isReady() {
                return isSelected() || (getAnswerText() != null);
            }

            public int getAnswerIndex() {
                return answerIndex;
            }

            void setAnswerIndex(int value) {
                if (answerIndex >= 0)
                    return;
                answerIndex = value;
            }

            public String getAnswerStringIndex() {
                if (answerIndex < 0)
                    return null;
                return String.valueOf(alfa.charAt(answerIndex));
            }

            public String getAnswerDesc() {
                return answerDesc;
            }

            public String getAnswerDescHTML() {
                return getAsHTML(answerDesc);
            }

            public boolean isSelected() {
                return selected;
            }

            void setSelected(boolean value) {
                if (selected == value)
                    return;

                selected = value;
                if (comp instanceof AbstractButton)
                    ((AbstractButton) comp).setSelected(value);

                setModified(true);
            }

            public String getAnswerText() {
                return answerText;
            }

            void setAnswerText(String value) {
                if (QuestionAndAnswers.this.equals(answerText, value))
                    return;

                answerText = value;
                setModified(true);
            }

            public int getScore() {
                return answerScore;
            }

            void setComponent(Component value) {
                comp = value;

                if (comp instanceof AbstractButton) {
                    ((AbstractButton) comp).setSelected(isSelected());

                    ((AbstractButton) comp)
                            .addActionListener(new java.awt.event.ActionListener() {
                                public void actionPerformed(
                                        java.awt.event.ActionEvent evt) {
                                    componentActionPerformed(evt);
                                }
                            });
                    ((AbstractButton) comp)
                            .addItemListener(new java.awt.event.ItemListener() {
                                public void itemStateChanged(
                                        java.awt.event.ItemEvent evt) {
                                    componentItemStateChanged(evt);
                                }
                            });
                } else if (comp instanceof JTextComponent) {
                    ((JTextComponent) comp).setText(getAnswerText());

                    comp.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusLost(java.awt.event.FocusEvent evt) {
                            componentFocusLost(evt);
                        }
                    });
                }

            }

            private void componentActionPerformed(java.awt.event.ActionEvent evt) {
                Object source = evt.getSource();
                if (source instanceof AbstractButton) {
                    if (tableModel == null)
                        return;
                    tableModel.setValueAt(getAnswerStringIndex(), questionNum,
                            1);
                    tableModel.fireTableDataChanged();
                }
            }

            private void componentItemStateChanged(java.awt.event.ItemEvent evt) {
                Object source = evt.getSource();
                if (source instanceof AbstractButton) {
                    setSelected(((AbstractButton) source).isSelected());
                }
            }

            private void componentFocusLost(java.awt.event.FocusEvent evt) {
                Object source = evt.getSource();
                if (source instanceof JTextComponent)
                    setAnswerText((((JTextComponent) source).getText()).trim());
            }

        }

        public Integer questionID;

        public Integer questionTypeID;

        public String questionDesc;

        private int questionNum;

        /**
         * STRUCTURE Map( QuestionAnswerID, Answer )
         */
        Map answers = new TreeMap();

        public Question(Integer questionID) {
            this.questionID = questionID;
        }

        // at least one answer is ready
        public boolean isReady() {
            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();
                if (a.isReady())
                    return true;
            }

            return false;
        }

        public String getQuestionDesc() {
            return questionDesc;
        }

        public int getQuestionNumber() {
            return questionNum + 1;
        }

        public String getQuestionNumberHTML() {
            return "<HTML><B><FONT FACE=\"Arial\" SIZE=4><P>Question "
                    + (questionNum + 1) + "</P></FONT></B></HTML>";
        }

        public String getQuestionDescHTML() {
            if (questionDesc.startsWith("<HTML>"))
                return questionDesc;

            return "<HTML><B><FONT SIZE=3><P ALIGN=\"LEFT\">" + questionDesc
                    + "</P></FONT></B></HTML>";
        }

        public Answer getAnswer(Integer answerID) {
            return (Answer) answers.get(answerID);
        }

        public Answer getAnswer(String answerStringIndex) {
            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();
                if (answerStringIndex
                        .equalsIgnoreCase(a.getAnswerStringIndex()))
                    return a;
            }
            return null;
        }

        public Answer addAnswer(Integer answerID, String answerDesc,
                int answerScore) {
            Answer a = getAnswer(answerID);
            if (a == null) {
                a = new Answer();
                answers.put(answerID, a);
            }
            a.answerID = answerID;
            a.answerDesc = answerDesc;
            a.answerScore = answerScore;

            a.setAnswerIndex(answers.size() - 1);

            return a;
        }

        public String getAnswerDesc(Integer answerID) {
            Answer a = getAnswer(answerID);
            return a == null ? null : (String) a.getAnswerDesc();
        }

        public int getAnswerScore(Integer answerID) {
            Answer a = getAnswer(answerID);
            return a == null ? 0 : a.getScore();
        }

        public int getScore() {
            int score = 0;

            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();

                if (a.isSelected())
                    score += a.answerScore;
            }

            return score;
        }

        public String[] getAnswerStringIndexes() {
            String[] stringIndexes = new String[answers.size()];

            int i = 0;
            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();
                stringIndexes[i++] = a.getAnswerStringIndex();
            }

            return stringIndexes;
        }

        public Component[] getAnswerComponents() {

            if (answers.size() == 0)
                return null;

            int i = 0;
            ButtonGroup bg = null;
            Component[] components = new Component[answers.size()];

            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();

                if (a.comp == null) {

                    if (QuestionType.RADIO_BOX.equals(questionTypeID)) {
                        JRadioButton rb = new JRadioButton();

                        rb.setText(getAsHTML("" + alfa.charAt(i) + "). "
                                + a.getAnswerDesc()));
                        if (bg == null)
                            bg = new ButtonGroup();
                        bg.add(rb);

                        a.setComponent(rb);
                    } else if (QuestionType.CHECK_BOX.equals(questionTypeID)) {
                        JCheckBox cb = new JCheckBox();

                        cb.setText(a.getAnswerDescHTML());

                        a.setComponent(cb);
                    } else if (QuestionType.COMBO_BOX.equals(questionTypeID)) {
                        // a.comp = new JComboBox();
                        // not implemented
                    } else if (QuestionType.LIST_BOX.equals(questionTypeID)) {
                        // a.comp = new JList();
                        // not implemented
                    } else if (QuestionType.FREE_TEXT.equals(questionTypeID)) {
                        JScrollPane sp = new JScrollPane();
                        sp.setBorder(new javax.swing.border.TitledBorder(a
                                .getAnswerDescHTML()));

                        JTextArea ta = new JTextArea();
                        ta.setWrapStyleWord(true);

                        sp.add(ta);

                        a.setComponent(ta);
                    }

                }

                components[i++] = a.comp;

            }

            return components;

        }

        public Object[] getSelectedAnswers() {
            ArrayList al = null;

            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();

                if (a.isReady()) {
                    if (al == null)
                        al = new ArrayList();
                    al.add(a);
                }

            }

            return al == null ? null : al.toArray();
        }

        public void unselectedAllAnswers() {
            ArrayList al = null;

            Iterator iter = answers.values().iterator();
            while (iter.hasNext()) {
                Answer a = (Answer) iter.next();
                a.setSelected(false);
            }
        }
    }

    /**
     * helper methods
     */

    // all questions are ready
    public boolean isReady() {
        Iterator iter = questions.values().iterator();
        while (iter.hasNext()) {
            Question q = (Question) iter.next();
            if (!q.isReady())
                return false;
        }

        return true;
    }

    public void addQuestion(Question question) {
        // set seq number 0, 1, ..
        question.questionNum = questions.size();
        questions.put(question.questionID, question);
    }

    public Question getQuestionByNumber(int questionNum) { // 0, 1, 2, ...
        if (questionNum < 0 || questionNum >= questions.size())
            return null;
        return (Question) questions.values().toArray()[questionNum];
    }

    public Question getQuestion(Integer questionID) {
        return (Question) questions.get(questionID);
    }

    public void setQuestionAnswer(Integer questionID, Integer questionAnswerID,
            boolean selected, String questionAnswerText) {

        Question q = getQuestion(questionID);
        if (q == null)
            return;

        Question.Answer a = q.getAnswer(questionAnswerID);
        if (a == null)
            return;

        a.setSelected(selected);
        a.setAnswerText(questionAnswerText);

    }

    public int getScore() {
        int score = 0;

        Iterator iter = questions.values().iterator();
        while (iter.hasNext()) {
            Question q = (Question) iter.next();

            score += q.getScore();
        }

        return score;
    }

    /**
     * 
     */
    class SurveyTableModel extends javax.swing.table.DefaultTableModel {

        SurveyTableModel() {
            super(new Object[][] {}, new String[] { "Question Number",
                    "Client Answer" });
        }

        SurveyTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        Class[] types = new Class[] { java.lang.Object.class,
                java.lang.Object.class };

        boolean[] canEdit = new boolean[] { false, true };

        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }

    };

    class SurveyListSelectionListener implements
            javax.swing.event.ListSelectionListener {

        public void valueChanged(javax.swing.event.ListSelectionEvent e) {
            // Ignore extra messages.
            if (e.getValueIsAdjusting())
                return;

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if (lsm.isSelectionEmpty()) {
                // no rows are selected
            } else {
                int row = lsm.getMinSelectionIndex();
                // selectedRow is selected
                // if (DEBUG)
                // System.out.println("ListSelectionListener.valueChanged: " +
                // row);

                // change table combobox model (available answers)
                Question q = getQuestionByNumber(row);
                getAnswerComboBox().setModel(
                        new javax.swing.DefaultComboBoxModel(q
                                .getAnswerStringIndexes()));
            }
        }

    }

    public javax.swing.table.TableModel setTableModel(javax.swing.JTable table) {

        if (tableModel == null) {
            tableModel = new SurveyTableModel();

            tableModel
                    .addTableModelListener(new javax.swing.event.TableModelListener() {
                        public void tableChanged(
                                javax.swing.event.TableModelEvent tableModelEvent) {
                            int row = tableModelEvent.getFirstRow();
                            if (row == -1)
                                return;

                            int column = tableModelEvent.getColumn();
                            if (column != 1)
                                return; // combobox column

                            SurveyTableModel model = (SurveyTableModel) tableModelEvent
                                    .getSource();

                            String columnName = model.getColumnName(column);
                            Object data = model.getValueAt(row, column);

                            // change question answer selection in detailed view
                            // ( radio/check buttons )
                            // if (DEBUG)
                            // System.out.println("TableModelListener.tableChanged:
                            // " + row + ", " + column + ", " + data.toString()
                            // );
                            Question q = getQuestionByNumber(row);
                            q.unselectedAllAnswers();
                            Question.Answer a = data == null ? null : q
                                    .getAnswer(data.toString());
                            if (a != null)
                                a.setSelected(true);
                        }
                    });

            // set initial answers
            Iterator iter = questions.values().iterator();
            while (iter.hasNext()) {
                Question q = (Question) iter.next();
                Object[] a = q.getSelectedAnswers();
                tableModel.addRow(new Object[] {
                        "Question " + (q.questionNum + 1),
                        a == null || a.length == 0 ? null
                                : ((Question.Answer) a[0])
                                        .getAnswerStringIndex() });
            }
        } else {
            int i = -1;
            Iterator iter = questions.values().iterator();
            while (iter.hasNext()) {
                i++;
                Object[] a = ((Question) iter.next()).getSelectedAnswers();
                if (a == null || a.length == 0)
                    continue;
                tableModel.setValueAt(((Question.Answer) a[0])
                        .getAnswerStringIndex(), i, 1);
            }
        }

        table.setModel(tableModel);

        if (listSelectionListener == null)
            listSelectionListener = new SurveyListSelectionListener();
        table.getSelectionModel().removeListSelectionListener(
                listSelectionListener);
        table.getSelectionModel().addListSelectionListener(
                listSelectionListener);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.DefaultCellEditor dce = new javax.swing.DefaultCellEditor(
                getAnswerComboBox());
        dce.setClickCountToStart(2); // need to avoid displaying answers from
                                        // prev selection
        table.getColumnModel().getColumn(1).setCellEditor(dce);

        return tableModel;

    }

    /**
     * get/set methods
     */
    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String value) {

        if (value != null)
            value = value.trim();

        if (value == null && surveyTitle == null)
            return;
        if (value != null && value.equals(surveyTitle))
            return;

        surveyTitle = value;
        setModified(true);

    }

    private boolean equals(String oldValue, String newValue) {
        if (newValue != null && newValue.trim().length() == 0)
            newValue = null;
        return equals((Object) oldValue, (Object) newValue);
    }

    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

}
