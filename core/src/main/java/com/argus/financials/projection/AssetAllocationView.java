/*
 * AssetAllocationView.java
 *
 * Created on 20 September 2001, 12:08
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.metal.MetalProgressBarUI;
import javax.swing.table.TableModel;

import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.InvestmentStrategyData;
import com.argus.financials.etc.GrowthRate;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.Currency;
import com.argus.io.ImageUtils;
import com.argus.swing.SwingUtils;
import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.ChartDataViewSeries;
import com.klg.jclass.chart.ChartText;
import com.klg.jclass.chart.JCAxis;
import com.klg.jclass.chart.JCChart;
import com.klg.jclass.chart.JCChartLabel;
import com.klg.jclass.chart.JCChartLabelManager;
import com.klg.jclass.chart.JCChartStyle;
import com.klg.jclass.chart.JCDataIndex;
import com.klg.jclass.chart.JCHiloChartFormat;
import com.klg.jclass.chart.data.JCDefaultDataSource;
import com.klg.jclass.util.legend.JCLegend;

public class AssetAllocationView extends javax.swing.JPanel implements
        com.argus.io.ImageEncoder {

    private Integer strategyCodeID;

    private String title;

    private TableModel chartDataTableModel;

    // some useful colors
    java.awt.Color light_bg = new java.awt.Color(0xbb, 0xbb, 0xf0);

    java.awt.Color dark_bg = new java.awt.Color(0x70, 0x70, 0xc0);

    private Object antiAlias;

    /** Creates new form AssetAllocationView */
    public AssetAllocationView(Integer investmentStrategyCodeID) {

        setAntiAlias(true);

        initComponents();

        setInvestmentStrategyCodeID(investmentStrategyCodeID);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTextFieldGrowthRate.setBackground(java.awt.Color.white);
                jTextFieldIncomeRate.setBackground(java.awt.Color.white);
                jTextFieldTotal.setBackground(java.awt.Color.white);
                jTextAreaComment.setBackground(java.awt.Color.white);
            }
        });

    }

    private void setAntiAlias(boolean on) {
        antiAlias = on ? RenderingHints.VALUE_ANTIALIAS_ON
                : RenderingHints.VALUE_ANTIALIAS_OFF;
    }

    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    public String getViewCaption() {
        return title;
    }

    public Integer getInvestmentStrategyCodeID() {
        return strategyCodeID;
    }

    public void setInvestmentStrategyCodeID(Integer investmentStrategyCodeID) {
        if (equals(strategyCodeID, investmentStrategyCodeID))
            return;

        initComponents2(investmentStrategyCodeID);
        strategyCodeID = investmentStrategyCodeID;

    }

    private void initComponents2(Integer investmentStrategyCodeID) {

        // set outter border title
        title = new InvestmentStrategyData()
                .getCodeDescription(investmentStrategyCodeID);

        /**
         * 
         */
        double[][] xvalues = null;
        double[][] yvalues = InvestmentStrategyData
                .getAllocationData2(investmentStrategyCodeID);
        String[] pointLabels = null;// new String [] { title };
        String[] seriesLabels = InvestmentStrategyData
                .getAllocationDataLabels();
        String dataSourceName = "AllocationData";
        Color[] colors = InvestmentStrategyData.getColors();

        /**
         * chart1
         */
        int chartType1 = chart1.getChartType();// JCChart.PIE;
        ChartDataView view1 = new ChartDataView();
        // ChartDataView view1 = chart1.getDataView(0);
        view1.setDataSource(new JCDefaultDataSource(xvalues, yvalues,
                pointLabels, seriesLabels, dataSourceName));
        chart1.getChartArea().setAxisBoundingBox(true);

        // set series colors
        for (int i = 0; i < view1.getNumSeries(); i++) {
            JCChartStyle style = view1.getSeries(i).getStyle();

            if (colors != null && i < colors.length) {
                style.setLineColor(colors[i]);
                style.setSymbolColor(colors[i]);
                style.setFillColor(colors[i]);
            }

        }

        boolean was_batched = chart1.isBatched();
        chart1.setBatched(true);
        view1.setBatched(true);

        chart1.setDataView(0, view1);
        view1.setChartType(chartType1);
        addChartLabels(chart1);

        view1.setBatched(false);
        chart1.setBatched(was_batched);

        /**
         * chart2
         */
        int chartType2 = chart2.getChartType();// JCChart.HILO;
        xvalues = null;
        yvalues = InvestmentStrategyData
                .getAllocationHistData2(investmentStrategyCodeID);
        pointLabels = InvestmentStrategyData.getAllocationHistDataLabels();
        seriesLabels = new String[] { "min", "max" };
        dataSourceName = "AllocationHistData";

        ChartDataView view2 = new ChartDataView();
        view2.setDataSource(new JCDefaultDataSource(xvalues,
                // yvalues,
                new double[][] {
                        new double[] { yvalues[0][0], yvalues[0][1],
                                MoneyCalc.HOLE },
                        new double[] { yvalues[1][0], yvalues[1][1],
                                MoneyCalc.HOLE } }, pointLabels, seriesLabels,
                dataSourceName));

        ChartDataView view2_2 = new ChartDataView();
        view2_2.setDataSource(new JCDefaultDataSource(xvalues,
                new double[][] {
                        new double[] { MoneyCalc.HOLE, MoneyCalc.HOLE,
                                yvalues[0][2] },
                        new double[] { MoneyCalc.HOLE, MoneyCalc.HOLE,
                                yvalues[1][2] } }, pointLabels, seriesLabels,
                dataSourceName + "_2"));

        chart2.getChartArea().setAxisBoundingBox(true);

        chart2.getLegend().setVisible(false);

        JCAxis yAxis = chart2.getChartArea().getYAxis(0);
        yAxis.getTitle().setPlacement(JCLegend.WEST);
        yAxis.getTitle().setText("percent (%)"
        // "<html><body><font color=#804040 size=-1>" +
                // "percent (%)" +
                // "</font></body></html>"
                );
        yAxis.getTitle().setRotation(ChartText.DEG_270);

        JCAxis xAxis = chart2.getChartArea().getXAxis(0);
        xAxis.setAnnotationMethod(JCAxis.POINT_LABELS);
        xAxis.setAnnotationRotation(JCAxis.ROTATE_270);
        xAxis.setForeground(Color.red);
        xAxis.setPlacement(yAxis, 0.);

        was_batched = chart2.isBatched();
        chart2.setBatched(true);
        view2.setBatched(true);
        view2_2.setBatched(true);

        chart2.setDataView(0, view2);
        view2.getSeries(0).getStyle().setLineColor(java.awt.Color.blue);
        view2.setChartType(chartType2);
        if (chart2.getChartType() == JCChart.HILO) {
            JCChartStyle cs = ((JCHiloChartFormat) view2.getChartFormat())
                    .getHiloStyle(0);

            cs.setLineWidth(50);
        }

        chart2.setDataView(1, view2_2);
        view2_2.getSeries(0).getStyle().setLineColor(java.awt.Color.blue);
        view2_2.setChartType(chartType2);
        if (chart2.getChartType() == JCChart.HILO) {
            JCChartStyle cs = ((JCHiloChartFormat) view2_2.getChartFormat())
                    .getHiloStyle(0);

            cs.setLineWidth(10);
            cs.setLineCap(BasicStroke.CAP_ROUND); // CAP_BUTT, CAP_ROUND,
                                                    // CAP_SQUARE
        }

        addChartLabels(chart2);

        view2.setBatched(false);
        view2_2.setBatched(false);
        chart2.setBatched(was_batched);

        /*
         * set other reference params
         */
        GrowthRate gr = InvestmentStrategyCode
                .getGrowthRate(investmentStrategyCodeID);
        jTextFieldIncomeRate.setText(Currency.getCurrencyInstance().toString(
                gr.getIncomeRate()));
        jTextFieldGrowthRate.setText(Currency.getCurrencyInstance().toString(
                gr.getGrowthRate()));
        jTextFieldTotal.setText(Currency.getCurrencyInstance().toString(
                gr.getRate()));

        double defensiveRate = gr.getDefensiveRate();
        jLabelDefensive.setText(String.valueOf(defensiveRate));
        jLabelDefensive.getInputVerifier().verify(jLabelDefensive);

        jLabelGrowth.setText(String.valueOf(100 - defensiveRate));
        jLabelGrowth.getInputVerifier().verify(jLabelGrowth);

        jProgressBarDefensive.setValue((int) defensiveRate);
        jProgressBarDefensive
                .setUI(new javax.swing.plaf.metal.MetalProgressBarUI());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelDetails = new javax.swing.JPanel();
        jPanelAssetAllocation = new javax.swing.JPanel();
        jPanelChart1 = new javax.swing.JPanel();
        chart1 = new com.klg.jclass.chart.beans.SimpleChart() {
            public void paintComponent(Graphics g) {

                // System.out.println("!!!!!!!!!!!!!!!!!REQUEST TO
                // paintComponent!!!!!!!!!!!!!!!!!!!\n" + g);

                if (g instanceof Graphics2D) {
                    Graphics2D g2 = (Graphics2D) g;

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            antiAlias);

                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

                    // g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    // RenderingHints.VALUE_RENDER_SPEED);

                    // g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                    // RenderingHints.VALUE_COLOR_RENDER_QUALITY );

                    // g2.setRenderingHint(RenderingHints.KEY_DITHERING,
                    // RenderingHints.VALUE_DITHER_ENABLE );

                    // g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    // RenderingHints.VALUE_FRACTIONALMETRICS_ON );

                }

                super.paintComponent(g); // paint background

            }

        };
        jPanel3 = new javax.swing.JPanel();
        jPanelBar = new javax.swing.JPanel();
        jProgressBarDefensive = new javax.swing.JProgressBar() {
            private ProgressBarUI progressBarUI;

            // public void updateUI() {
            // setUI( getProgressBarUI() );
            // }
            public void setUI(ProgressBarUI newUI) {
                if (super.getUI() instanceof MetalProgressBarUI)
                    return;
                // System.out.println( "-----> setUI() " + newUI );
                super.setUI(getProgressBarUI());
                // System.out.println( "-----> setUI() " + getUI() );
            }

            public ProgressBarUI getUI() {
                // System.out.println( "-----> getUI() " + getProgressBarUI() );
                return getProgressBarUI();
            }

            private ProgressBarUI getProgressBarUI() {
                if (progressBarUI == null)
                    progressBarUI = super.getUI() instanceof MetalProgressBarUI ? super
                            .getUI()
                            : new MetalProgressBarUI();
                return progressBarUI;
            }
        };
        jLabelDefensive = new javax.swing.JLabel();
        jLabelGrowth = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanelForecastReturns = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldIncomeRate = new javax.swing.JTextField();
        jTextFieldGrowthRate = new javax.swing.JTextField();
        jTextFieldTotal = new javax.swing.JTextField();
        jSeparatorTotal = new javax.swing.JSeparator();
        jPanelPortofolioRisk = new javax.swing.JPanel();
        chart2 = new com.klg.jclass.chart.beans.SimpleChart();
        jTextAreaComment = new javax.swing.JTextArea();
        jPanelControls = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(700, 400));
        jPanelDetails.setLayout(new java.awt.GridLayout(1, 2));

        jPanelDetails.setPreferredSize(new java.awt.Dimension(10, 1000));
        jPanelAssetAllocation.setLayout(new javax.swing.BoxLayout(
                jPanelAssetAllocation, javax.swing.BoxLayout.Y_AXIS));

        jPanelChart1.setLayout(new javax.swing.BoxLayout(jPanelChart1,
                javax.swing.BoxLayout.X_AXIS));

        jPanelChart1.setPreferredSize(new java.awt.Dimension(10, 1000));
        chart1.setLegendVisible(true);
        chart1
                .setBorder(new javax.swing.border.TitledBorder(
                        "Asset Allocation"));
        chart1.setView3D("10,25,25");
        chart1.setChartType(com.klg.jclass.chart.JCChart.PIE);
        chart1
                .setXAxisAnnotationMethod(com.klg.jclass.chart.JCAxis.VALUE_LABELS);
        chart1.setBackground(java.awt.Color.white);
        jPanelChart1.add(chart1);

        jPanelAssetAllocation.add(jPanelChart1);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        jPanelBar.setLayout(new java.awt.GridBagLayout());

        jPanelBar.setBorder(new javax.swing.border.TitledBorder("Indicator"));
        jPanelBar.setBackground(java.awt.Color.white);
        jProgressBarDefensive.setForeground(java.awt.Color.red);
        jProgressBarDefensive.setBackground(java.awt.Color.green);
        jProgressBarDefensive.setValue(55);
        jProgressBarDefensive.setBorderPainted(false);
        jProgressBarDefensive.setPreferredSize(new java.awt.Dimension(148, 28));
        jProgressBarDefensive.setMinimumSize(new java.awt.Dimension(148, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelBar.add(jProgressBarDefensive, gridBagConstraints);

        jLabelDefensive.setText("jLabel10");
        jLabelDefensive.setInputVerifier(PercentInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelBar.add(jLabelDefensive, gridBagConstraints);

        jLabelGrowth.setText("jLabel11");
        jLabelGrowth.setInputVerifier(PercentInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelBar.add(jLabelGrowth, gridBagConstraints);

        jLabel12.setText("Defensive");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelBar.add(jLabel12, gridBagConstraints);

        jLabel13.setText("Growth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelBar.add(jLabel13, gridBagConstraints);

        jPanel3.add(jPanelBar);

        jPanelForecastReturns.setLayout(new java.awt.GridBagLayout());

        jPanelForecastReturns.setBorder(new javax.swing.border.TitledBorder(
                "Long Term Forecast Returns p.a."));
        jPanelForecastReturns.setBackground(java.awt.Color.white);
        jLabel1.setText("Income Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelForecastReturns.add(jLabel1, gridBagConstraints);

        jLabel4.setText("Growth Return");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelForecastReturns.add(jLabel4, gridBagConstraints);

        jLabel7.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        jPanelForecastReturns.add(jLabel7, gridBagConstraints);

        jTextFieldIncomeRate.setEditable(false);
        jTextFieldIncomeRate.setText("0.00");
        jTextFieldIncomeRate.setBackground(java.awt.Color.white);
        jTextFieldIncomeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelForecastReturns.add(jTextFieldIncomeRate, gridBagConstraints);

        jTextFieldGrowthRate.setEditable(false);
        jTextFieldGrowthRate.setText("0.00");
        jTextFieldGrowthRate.setBackground(java.awt.Color.white);
        jTextFieldGrowthRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelForecastReturns.add(jTextFieldGrowthRate, gridBagConstraints);

        jTextFieldTotal.setEditable(false);
        jTextFieldTotal.setText("0.00");
        jTextFieldTotal.setBackground(java.awt.Color.white);
        jTextFieldTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 10);
        jPanelForecastReturns.add(jTextFieldTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 10);
        jPanelForecastReturns.add(jSeparatorTotal, gridBagConstraints);

        jPanel3.add(jPanelForecastReturns);

        jPanelAssetAllocation.add(jPanel3);

        jPanelDetails.add(jPanelAssetAllocation);

        jPanelPortofolioRisk.setLayout(new javax.swing.BoxLayout(
                jPanelPortofolioRisk, javax.swing.BoxLayout.Y_AXIS));

        chart2.setLegendVisible(true);
        chart2.setPreferredSize(new java.awt.Dimension(20, 1000));
        chart2.setBorder(new javax.swing.border.TitledBorder(
                "Likely Range of Outcomes"));
        chart2.setChartType(com.klg.jclass.chart.JCChart.HILO);
        chart2.setBackground(java.awt.Color.white);
        chart2.setYAxisGridVisible(true);
        jPanelPortofolioRisk.add(chart2);

        jTextAreaComment.setWrapStyleWord(true);
        jTextAreaComment.setLineWrap(true);
        jTextAreaComment.setEditable(false);
        jTextAreaComment
                .setText("The above ranges of outcomes are expected returns, calculated with a 90% probability of occurance, based on data generally calculated over periods of 7 years or greater. Long Term returns have a higher probability of occuring over a 7 - 10 year period.");
        jTextAreaComment.setBackground(new java.awt.Color(255, 255, 255));
        jTextAreaComment.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jTextAreaComment.setMinimumSize(new java.awt.Dimension(20, 100));
        jPanelPortofolioRisk.add(jTextAreaComment);

        jPanelDetails.add(jPanelPortofolioRisk);

        add(jPanelDetails);

        jPanelControls.setBackground(java.awt.Color.white);
        jButtonReport.setText("Report");
        jButtonReport.setEnabled(false);
        jPanelControls.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonClose);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed
        SwingUtil.setVisible(this, false);
    }// GEN-LAST:event_jButtonCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator jSeparatorTotal;

    private javax.swing.JPanel jPanelAssetAllocation;

    private javax.swing.JProgressBar jProgressBarDefensive;

    private javax.swing.JPanel jPanelPortofolioRisk;

    private javax.swing.JTextField jTextFieldGrowthRate;

    private javax.swing.JLabel jLabelGrowth;

    private javax.swing.JLabel jLabelDefensive;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JTextField jTextFieldIncomeRate;

    private javax.swing.JTextField jTextFieldTotal;

    private javax.swing.JPanel jPanelDetails;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanelBar;

    private javax.swing.JPanel jPanelChart1;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel1;

    private com.klg.jclass.chart.beans.SimpleChart chart2;

    private javax.swing.JPanel jPanel3;

    private com.klg.jclass.chart.beans.SimpleChart chart1;

    private javax.swing.JTextArea jTextAreaComment;

    private javax.swing.JPanel jPanelForecastReturns;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel12;

    // End of variables declaration//GEN-END:variables

    public void saveChart(java.io.File file) throws java.io.IOException {
        // jPanelControls.setVisible(false);
        try {
            ImageUtils.encodeAsJPEG(this.jPanelDetails, file);
        } catch (Exception e) {
            throw new java.io.IOException(e.getMessage());
        }
        // finally { jPanelControls.setVisible(true); }
    }

    /*
     * <?xml version="1.0"?> <!DOCTYPE JCChartData SYSTEM "JCChartData.dtd">
     * <JCChartData Name="My Chart" Hole="MAX"> <PointLabel>Point Label 1</PointLabel>
     * <PointLabel>Point Label 2</PointLabel> <PointLabel>Point Label 3</PointLabel>
     * <PointLabel>Point Label 4</PointLabel> <Series> <SeriesLabel>Y Axis #1
     * Data</SeriesLabel> <XData>1</XData> <XData>2</XData> <XData>3</XData>
     * <XData>4</XData> <YData>1</YData> <YData>2</YData> <YData>3</YData>
     * <YData>4</YData> </Series> <Series> <SeriesLabel>Y Axis #2 Data</SeriesLabel>
     * <YData>1</YData> <YData>4</YData> <YData>9</YData> <YData>16</YData>
     * </Series> </JCChartData>
     */

    /**
     * Create chart labels for every pie slice, including the other slice.
     */
    public void addChartLabels(JCChart chart) {

        removeChartLabels(chart);

        JCChartLabelManager clm = chart.getChartLabelManager();

        java.util.List list = chart.getDataView();
        java.util.Iterator iter = list.iterator();
        while (iter.hasNext()) {
            ChartDataView cdv = (ChartDataView) iter.next();

            for (int i = 0; i < cdv.getNumSeries(); i++) {
                ChartDataViewSeries series = cdv.getSeries(i);

                for (int j = 0; j < series.maxYIndex(); j++) {
                    JCChartLabel cl = new JCChartLabel(series.getLabel() + ": "
                            + series.getY(j) + "%");
                    cl.setDwellLabel(true);
                    cl.setAttachMethod(JCChartLabel.ATTACH_DATAINDEX);
                    cl.setDataIndex(new JCDataIndex(cdv, series, i, j));
                    JLabel labelComp = ((JLabel) cl.getComponent());
                    labelComp.setOpaque(true);
                    labelComp.setBackground(light_bg);
                    labelComp.setForeground(java.awt.Color.black);
                    labelComp.setBorder(BorderFactory.createEtchedBorder());
                    clm.addChartLabel(cl);
                }
            }
        }

    }

    /**
     * Remove all chart labels from the chart.
     */
    public void removeChartLabels(JCChart chart) {
        chart.getChartLabelManager().removeAllChartLabels();
    }

    public void encodeAsJPEG(java.io.File file) throws java.io.IOException {
        // remove background
        Color oldBackground = jPanelDetails.getBackground();
        jPanelDetails.setBackground(Color.white);

        javax.swing.border.Border oldBorder = jPanelDetails.getBorder();
        jPanelDetails.setBorder(BorderFactory.createEmptyBorder());

        try {
            encodeAsJPEG(file, jPanelDetails);
        } finally {
            // jPanelChart.add( chart );
            jPanelDetails.setBackground(oldBackground);
            jPanelDetails.setBorder(oldBorder);
        }

    }

    public void encodeAsJPEG(java.io.File file, javax.swing.JComponent comp)
            throws java.io.IOException {

        JDialog window = new JDialog();
        comp.setBorder(new javax.swing.border.LineBorder(java.awt.Color.black));
        window.getContentPane().add(comp, java.awt.BorderLayout.CENTER);

        java.awt.Dimension ps = getPreferredSize();

        // set best size
        double w = SwingUtils.DIM_SCREEN.getWidth() * 0.7;
        if (ps.getWidth() > w)
            w = ps.getWidth();
        double h = w / 1.62;
        if (ps.getHeight() > h)
            h = ps.getHeight();
        comp.setPreferredSize(new java.awt.Dimension((int) w, (int) h));
        window.pack();

        try {
            ImageUtils.encodeAsJPEG(comp, file);
        } catch (Exception e) {
            throw new java.io.IOException(e.getMessage());
        } finally {
            window.getContentPane().remove(comp);
            window.dispose();
            window = null;
        }

    }

}
