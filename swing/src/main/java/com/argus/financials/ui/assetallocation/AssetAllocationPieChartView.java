/*
 * AssetAllocationPieChartView.java
 *
 * Created on 4 October 2002, 08:37
 */

package com.argus.financials.ui.assetallocation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.argus.financials.assetallocation.AssetAllocationTableRow;
import com.argus.format.Percent;
import com.argus.io.ImageUtils;
import com.argus.swing.SwingUtils;
import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.ChartDataViewSeries;
import com.klg.jclass.chart.JCChart;
import com.klg.jclass.chart.JCChartLabel;
import com.klg.jclass.chart.JCChartLabelManager;
import com.klg.jclass.chart.JCChartStyle;
import com.klg.jclass.chart.JCDataIndex;
import com.klg.jclass.chart.data.JCDefaultDataSource;

/**
 * This class creates and displays a pie chart for a given asset allocation.
 * 
 * @author shibaevv
 */
public class AssetAllocationPieChartView extends javax.swing.JPanel implements
        com.argus.io.ImageEncoder {

    private Object antiAlias;

    private static AssetAllocationPieChartView view;

    private AssetAllocationTableRow _aatr;

    private String _title;

    private static String[] _rawAllocationDataLabels = new String[] { "Cash",
            "Fixed Interest", "Property", "Aust. Shares", "Intnl. Shares",
            "Other", "Unallocated" };

    /*
     * same values like com.argus.code.InvestmentStrategyData, plus an
     * additional color for unallocated assets
     */
    public static final Color COLOR_CASH = new Color(0xff, 0x00, 0x00);

    public static final Color COLOR_AUS_FIXED = new Color(0xff, 0x80, 0x80);

    public static final Color COLOR_INTNL_FIXED = new Color(0xff, 0xff, 0xcc);

    public static final Color COLOR_CPI_FIXED = new Color(0xcc, 0x99, 0xff);

    public static final Color COLOR_AUS_SHARES = new Color(0x00, 0xff, 0x00);

    public static final Color COLOR_INTNL_SHARES = new Color(0xcc, 0xff, 0xcc);

    public static final Color COLOR_PROPERTY = new Color(0x33, 0xcc, 0xcc);

    public static final Color COLOR_MORTGAGES = new Color(0xff, 0x99, 0x00);

    public static final Color COLOR_TRADED_POLICIES = new Color(0xff, 0xff,
            0x00);

    public static final Color COLOR_UNALLOCATED = new Color(0xff, 0xff, 0xff);

    public static final Color COLOR_OTHER = new Color(0xbb, 0xbb, 0xbb);

    /*
     * same values like com.argus.code.InvestmentStrategyData, plus an
     * additional color for unallocated assets
     */
    private static Color[] _colors = new Color[] { COLOR_CASH,
            COLOR_INTNL_FIXED, COLOR_PROPERTY, COLOR_AUS_SHARES,
            COLOR_INTNL_SHARES, COLOR_OTHER, COLOR_UNALLOCATED };

    // some useful colors
    java.awt.Color light_bg = new java.awt.Color(0xbb, 0xbb, 0xf0);

    java.awt.Color dark_bg = new java.awt.Color(0x70, 0x70, 0xc0);

    /** Creates new form AssetAllocationPieChartView */
    public AssetAllocationPieChartView(String title,
            AssetAllocationTableRow aatr) {
        this._aatr = aatr;
        this._title = title;

        setAntiAlias(true);
        initComponents();
        initComponents2();
    }

    private void setAntiAlias(boolean on) {
        antiAlias = on ? RenderingHints.VALUE_ANTIALIAS_ON
                : RenderingHints.VALUE_ANTIALIAS_OFF;
    }

    private void initComponents2() {
        /**
         * create the pie chart
         */
        double[][] xvalues = null;
        double[][] yvalues = new double[_rawAllocationDataLabels.length][1];

        String[] pointLabels = null;
        String[] seriesLabels = new String[_rawAllocationDataLabels.length]; // _rawAllocationDataLabels;
        String dataSourceName = _title;
        Color[] colors = _colors;

        if (_aatr != null) {
            // we have some asset allocation, so get the values of cash, fixed
            // interest, property,
            // aus_shares and intnl. shares and copy them into an array for the
            // pie chart bean
            yvalues[0][0] = (_aatr.percent_in_cash == null) ? 0.0
                    : _aatr.percent_in_cash.doubleValue();
            yvalues[1][0] = (_aatr.percent_in_fixed_interest == null) ? 0.0
                    : _aatr.percent_in_fixed_interest.doubleValue();
            yvalues[2][0] = (_aatr.percent_in_property == null) ? 0.0
                    : _aatr.percent_in_property.doubleValue();
            yvalues[3][0] = (_aatr.percent_in_aust_shares == null) ? 0.0
                    : _aatr.percent_in_aust_shares.doubleValue();
            yvalues[4][0] = (_aatr.percent_in_intnl_shares == null) ? 0.0
                    : _aatr.percent_in_intnl_shares.doubleValue();
            yvalues[5][0] = (_aatr.percent_in_other == null) ? 0.0
                    : _aatr.percent_in_other.doubleValue();

            // check if we have unallocated assets/money
            if (_aatr.total_in_percent != null
                    && _aatr.total_in_percent.doubleValue() < 100.0) {
                // yes, there is some unallocated money, calculated the
                // unallocated amount
                // java.math.BigDecimal unallocated = new java.math.BigDecimal(
                // 100.0 - _aatr.total_in_percent.doubleValue() );
                // unallocated.setScale(2, unallocated.ROUND_HALF_UP);

                // yvalues[5][0] = unallocated.doubleValue();
                yvalues[6][0] = rint(100.0 - _aatr.total_in_percent
                        .doubleValue(), 2);
            } else {
                // no unallocated money
                yvalues[6][0] = 0.0;
            }

            // add percent values to labels
            Percent percent = Percent.getPercentInstance();

            for (int i = 0; i < seriesLabels.length; i++) {
                seriesLabels[i] = new String(_rawAllocationDataLabels[i] + " "
                        + percent.toString(yvalues[i][0]) + "%");
            }
        }

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
    }

    public static AssetAllocationPieChartView getInstance(String title,
            AssetAllocationTableRow aatr) {
        if (view == null)
            view = new AssetAllocationPieChartView(title, aatr);
        return view;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {
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

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        setPreferredSize(new java.awt.Dimension(400, 250));
        chart1.setLegendVisible(true);
        chart1
                .setBorder(new javax.swing.border.TitledBorder(
                        "Asset Allocation"));
        chart1.setView3D("10,25,25");
        chart1.setChartType(com.klg.jclass.chart.JCChart.PIE);
        chart1
                .setXAxisAnnotationMethod(com.klg.jclass.chart.JCAxis.VALUE_LABELS);
        chart1.setBackground(new java.awt.Color(255, 255, 255));
        add(chart1);

    }

    // Variables declaration - do not modify
    private com.klg.jclass.chart.beans.SimpleChart chart1;

    // End of variables declaration

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
                    JCChartLabel cl = new JCChartLabel(series.getLabel()); // + ":
                                                                            // " +
                                                                            // series.getY(j)
                                                                            // +
                                                                            // "%"
                                                                            // );
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
     * 
     * @params chart - JCChart
     */
    public void removeChartLabels(JCChart chart) {
        chart.getChartLabelManager().removeAllChartLabels();
    }

    /**
     * Update the current pie chart. Todo this, we initialze the object again.
     * 
     * @param aatr -
     *            an object which contains the asset allocation
     */
    public void updateView(AssetAllocationTableRow aatr) {
        this._aatr = aatr;

        this.initComponents2();
    }

    public void saveChart(String fileName) throws java.io.IOException {
        saveChart(new java.io.File(fileName));
    }

    public void saveChart(java.io.File file) throws java.io.IOException {
        // jPanelControls.setVisible(false);
        try {
            com.argus.io.ImageUtils.encodeAsJPEG(this, file);
        } catch (Exception e) {
            throw new java.io.IOException(e.getMessage());
        }
        // finally { jPanelControls.setVisible(true); }
    }

    // prepare chart for save (set optimal size, background color, etc.)
    public void encodeAsJPEG(java.io.File file) throws java.io.IOException {
        // remove background
        Color oldBackground = chart1.getBackground();
        chart1.setBackground(Color.white);

        javax.swing.border.Border oldChartAreaBorder = chart1.getChartArea()
                .getBorder();
        chart1.getChartArea().setBorder(BorderFactory.createEmptyBorder());

        try {
            encodeAsJPEG(file, chart1);
        } finally {
            this.add(chart1);
            chart1.setBackground(oldBackground);
            chart1.getChartArea().setBorder(oldChartAreaBorder);
        }

    }

    public void encodeAsJPEG(java.io.File file, JComponent comp)
            throws java.io.IOException {

        JDialog window = new JDialog();
        comp.setBorder(new javax.swing.border.LineBorder(java.awt.Color.black));
        window.getContentPane().add(comp, java.awt.BorderLayout.CENTER);

        java.awt.Dimension ps = getPreferredSize();

        // set best size
        double w = SwingUtils.DIM_SCREEN.getWidth() * 0.7;
        if (ps.getWidth() < w)
            w = ps.getWidth();
        double h = w / 1.62;
        if (ps.getHeight() < h)
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

    public static double rint(double x, int decimals) { // rounds to the nearest
                                                        // integer
        int factor = 1;
        for (int i = 0; i < Math.abs(decimals); i++)
            factor *= 10;
        if (decimals < 0)
            return factor * Math.rint(x / factor);
        else
            return Math.rint(factor * x) / factor;
    }

}
