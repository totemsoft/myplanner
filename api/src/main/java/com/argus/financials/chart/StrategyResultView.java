/*
 * StrategyResultView.java
 *
 * Created on 19 December 2001, 14:20
 */

package com.argus.financials.chart;

import com.argus.format.Currency;
import com.argus.util.RateUtils;
import com.klg.jclass.chart.JCChart;
import com.klg.jclass.chart.beans.SimpleChart;

public class StrategyResultView extends GraphView {

    /** Creates new StrategyResultView */
    public StrategyResultView() {
        super();

        // jPanelChart.setPreferredSize( new java.awt.Dimension( 400, 1000 ) );

        initComponents();

    }

    private void initComponents() {
        jPanelOthers = new javax.swing.JPanel();
        jPanelStrategy = new javax.swing.JPanel();
        jLabelActualIncome = new javax.swing.JLabel();
        jLabelRequiredIncome = new javax.swing.JLabel();
        jTextFieldActualIncome = new javax.swing.JTextField();
        jTextFieldRequiredIncome = new javax.swing.JTextField();
        jTextFieldActualIncome2 = new javax.swing.JTextField();
        jTextFieldRequiredIncome2 = new javax.swing.JTextField();
        jScrollPaneComments = new javax.swing.JScrollPane();
        jTextAreaComment = new javax.swing.JTextArea();

        jPanelOthers.setLayout(new java.awt.GridLayout(1, 2));

        jPanelStrategy.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        int gridy = 0;

        jLabelRequiredIncome.setText("Required Income");
        jLabelRequiredIncome.setPreferredSize(new java.awt.Dimension(120, 17));
        jLabelRequiredIncome.setMinimumSize(new java.awt.Dimension(120, 17));
        jLabelRequiredIncome.setMaximumSize(new java.awt.Dimension(120, 17));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = gridy;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelStrategy.add(jLabelRequiredIncome, gridBagConstraints1);

        jTextFieldRequiredIncome.setToolTipText("Today's Value");
        jTextFieldRequiredIncome.setEditable(false);
        jTextFieldRequiredIncome.setBackground(java.awt.Color.lightGray);
        jTextFieldRequiredIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRequiredIncome
                .setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldRequiredIncome.setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = gridy;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelStrategy.add(jTextFieldRequiredIncome, gridBagConstraints1);

        jTextFieldRequiredIncome2.setToolTipText("Future Value");
        jTextFieldRequiredIncome2.setEditable(false);
        jTextFieldRequiredIncome2.setBackground(java.awt.Color.lightGray);
        jTextFieldRequiredIncome2
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRequiredIncome2.setPreferredSize(new java.awt.Dimension(90,
                21));
        jTextFieldRequiredIncome2
                .setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = gridy++;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelStrategy.add(jTextFieldRequiredIncome2, gridBagConstraints1);

        jPanelStrategy
                .setBorder(new javax.swing.border.TitledBorder("Strategy"));
        jLabelActualIncome.setText("Actual Income");
        jLabelActualIncome.setPreferredSize(new java.awt.Dimension(120, 17));
        jLabelActualIncome.setMinimumSize(new java.awt.Dimension(120, 17));
        jLabelActualIncome.setMaximumSize(new java.awt.Dimension(120, 17));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = gridy;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelStrategy.add(jLabelActualIncome, gridBagConstraints1);

        jTextFieldActualIncome.setToolTipText("Today's Value");
        jTextFieldActualIncome.setEditable(false);
        jTextFieldActualIncome.setBackground(java.awt.Color.lightGray);
        jTextFieldActualIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldActualIncome.setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldActualIncome.setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = gridy;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelStrategy.add(jTextFieldActualIncome, gridBagConstraints1);

        jTextFieldActualIncome2.setToolTipText("Future Value");
        jTextFieldActualIncome2.setEditable(false);
        jTextFieldActualIncome2.setBackground(java.awt.Color.lightGray);
        jTextFieldActualIncome2
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldActualIncome2
                .setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldActualIncome2.setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = gridy++;
        gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanelStrategy.add(jTextFieldActualIncome2, gridBagConstraints1);

        jPanelOthers.add(jPanelStrategy);

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

    }

    // Variables declaration - do not modify
    private javax.swing.JPanel jPanelOthers;

    private javax.swing.JPanel jPanelStrategy;

    private javax.swing.JLabel jLabelActualIncome;

    private javax.swing.JLabel jLabelRequiredIncome;

    private javax.swing.JTextField jTextFieldActualIncome;

    private javax.swing.JTextField jTextFieldRequiredIncome;

    private javax.swing.JTextField jTextFieldActualIncome2;

    private javax.swing.JTextField jTextFieldRequiredIncome2;

    private javax.swing.JScrollPane jScrollPaneComments;

    private javax.swing.JTextArea jTextAreaComment;

    // End of variables declaration

    public void setActualIncome(double value, double indexRate, int years) {
        Currency curr = Currency.getCurrencyInstance();

        jTextFieldActualIncome.setText(curr.toString(value));
        jTextFieldActualIncome2.setText(curr.toString(RateUtils
                .getCompoundedAmount(value, indexRate / 100., years)));
    }

    public void setRequiredIncome(double value, double indexRate, int years) {
        Currency curr = Currency.getCurrencyInstance();

        jTextFieldRequiredIncome.setText(curr.toString(value));
        jTextFieldRequiredIncome2.setText(curr.toString(RateUtils
                .getCompoundedAmount(value, indexRate / 100., years)));
    }

    public void setComment(String comment) {
        jTextAreaComment.setText(comment);
    }

    public void setRetirementView(boolean retirementView) {
        jPanelOthers.setVisible(retirementView);
    }

    /**
     * 
     */
    public void setChartType(int chartType) {
        if (chart instanceof SimpleChart)
            ((SimpleChart) chart).setChartType(chartType);
        else
            super.setChartType(chartType);
    }

    /*
     * protected com.klg.jclass.chart.JCChart createChart() {
     * com.klg.jclass.chart.beans.SimpleChart _chart = new
     * com.klg.jclass.chart.beans.SimpleChart();
     * 
     * _chart.setLegendVisible(true); _chart.setLegendAnchor( JCLegend.NORTH );
     * _chart.setPreferredSize( new java.awt.Dimension(100, 800) );
     * _chart.setBorder( new javax.swing.border.TitledBorder("Graph") );
     * _chart.setXAxisTitleText("Age"); _chart.setLegendOrientation(
     * JCLegend.HORIZONTAL ); _chart.setView3D( "5,18,18" );
     * _chart.setYAxisGridVisible(true); //_chart.setData("ARRAY 'Sample
     * JCChart' 4 5 'Point Label0' 'Point Label1' 'Point Label2' 'Point Label3'
     * 'Point Label4' '' 0.0 1.0 2.0 3.0 4.0 'Series1' 20.0 22.0 19.0 24.0 25.0
     * 'Series2' 8.0 12.0 10.0 12.0 15.0 'Series3' 10.0 16.0 17.0 15.0 23.0
     * 'Series4' 16.0 19.0 15.0 22.0 18.0 ");
     * _chart.getDataView(0).setDataSource(null);
     *  // jRadioButtonPlot.setVisible( false ); jRadioButtonArea.setVisible(
     * false ); jRadioButtonStackingArea.setVisible( false );
     * 
     * return _chart; }
     */

    protected JCChart createChart() {

        JCChart _chart = super.createChart();

        setTitleAxisX("Age");

        return _chart;

    }

}
