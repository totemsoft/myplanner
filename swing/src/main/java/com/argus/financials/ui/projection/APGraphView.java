/*
 * StrategyResultView.java
 *
 * Created on 19 December 2001, 14:20
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.klg.jclass.chart.JCChart;

public class APGraphView extends com.argus.financials.chart.GraphView {

    /** Creates new StrategyResultView */
    public APGraphView() {
        super();
        jPanelChart.setPreferredSize(new java.awt.Dimension(400, 1000));

        initComponents2();
    }

    private void initComponents2() {
        jPanelOthers = new javax.swing.JPanel();

        jScrollPaneComments = new javax.swing.JScrollPane();
        jTextAreaComment = new javax.swing.JTextArea();

        jPanelOthers.setLayout(new java.awt.GridLayout(1, 2));

        jScrollPaneComments.setBorder(new javax.swing.border.TitledBorder(
                "Comments"));
        jScrollPaneComments.setMinimumSize(new java.awt.Dimension(10, 10));
        jTextAreaComment.setWrapStyleWord(true);
        jTextAreaComment.setLineWrap(true);
        jTextAreaComment.setEditable(false);
        jTextAreaComment.setBackground(java.awt.Color.lightGray);
        jTextAreaComment.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPaneComments.setViewportView(jTextAreaComment);

        jPanelOthers.add(jScrollPaneComments);

        add(jPanelOthers);

        //
        // jRadioButtonPlot.setVisible( false );
        // jRadioButtonArea.setVisible( false );
        // jRadioButtonStackingArea.setVisible( false );
    }

    // Variables declaration - do not modify
    javax.swing.JPanel jPanelOthers;

    private javax.swing.JScrollPane jScrollPaneComments;

    private javax.swing.JTextArea jTextAreaComment;

    // End of variables declaration

    public void setComment(String comment) {
        jTextAreaComment.setText(comment);
    }

    /**
     * 
     */
    protected JCChart createChart() {

        JCChart _chart = super.createChart();

        setTitleAxisX("Years");

        return _chart;

    }

}
