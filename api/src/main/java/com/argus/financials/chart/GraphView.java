/*
 * GraphView.java
 *
 */

package com.argus.financials.chart;

/**
 * 
 * @author valeri chibaev
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import com.argus.financials.swing.SwingUtil;
import com.argus.format.Currency;
import com.argus.format.Formatable;
import com.argus.format.Number2;
import com.argus.io.IOUtils;
import com.argus.io.ImageUtils;
import com.argus.swing.SwingUtils;
import com.argus.util.DateTimeUtils;
import com.klg.jclass.chart.ChartDataModel;
import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.ChartDataViewSeries;
import com.klg.jclass.chart.JCAxis;
import com.klg.jclass.chart.JCAxisTitle;
import com.klg.jclass.chart.JCChart;
import com.klg.jclass.chart.JCChartArea;
import com.klg.jclass.chart.JCChartLabel;
import com.klg.jclass.chart.JCChartLabelManager;
import com.klg.jclass.chart.JCChartStyle;
import com.klg.jclass.chart.JCDataIndex;
import com.klg.jclass.chart.JCFillStyle;
import com.klg.jclass.chart.JCLabelGenerator;
import com.klg.jclass.chart.JCLineStyle;
import com.klg.jclass.chart.data.JCDefaultDataSource;
import com.klg.jclass.util.legend.JCLegend;

// You can now put raw HTML into headers and footers,
// as long as the text starts with "&lt;html&gt;". <html>
// HTML is also valid in axis annotations, axis titles and legend elements.

public class GraphView extends javax.swing.JPanel implements IGraphView {

    // some useful colors
    public static final Color white = Color.white;

    public static final Color bg_gray = new Color(0xe5, 0xe5, 0xe5);

    public static final Color light_bg = new Color(0xbb, 0xbb, 0xf0);

    public static final Color dark_bg = new Color(0x70, 0x70, 0xc0);

    public static final Color lightBlue = new Color(173, 206, 250);

    public static final Color lightBlue2 = new Color(173, 206, 255);

    public static final Color lightSteelBlue = new Color(132, 112, 255);

    public static final Color darkGreen = Color.green.darker();

    public static final Color darkBlue = Color.blue.darker();

    // JCLineStyle.*
    // EMPTY, SOLID, LONG_DASH, SHORT_DASH, LSL_DASH or DASH_DOT
    public static final int lsNONE = JCLineStyle.NONE;

    public static final int lsSOLID = JCLineStyle.SOLID;

    public static final int lsLONG_DASH = JCLineStyle.LONG_DASH;

    public static final int lsSHORT_DASH = JCLineStyle.SHORT_DASH;

    public static final int lsLSL_DASH = JCLineStyle.LSL_DASH;

    public static final int lsDASH_DOT = JCLineStyle.DASH_DOT;

    public static String PROTECTED = " ";

    private javax.swing.JFileChooser jFileChooser;

    private Object antiAlias = RenderingHints.VALUE_ANTIALIAS_ON;

    /** Creates new form StrategyResult */
    public GraphView() {
        initComponents();
        initComponents2();
    }

    private void initComponents2() {
        SwingUtils.setDefaultFont(jPopupMenu);
        _addMouseListener(chart);
    }

    protected void _addMouseListener(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public void paintComponent(Graphics g) {

        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias);
            // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            // RenderingHints.VALUE_ANTIALIAS_ON);

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

        super.paintComponent(g);

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setPreferredSize(int, int)
     */
    public void setPreferredSize(int width, int height) {
        setPreferredSize(new java.awt.Dimension(width, height));
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setAntiAlias(boolean)
     */
    public void setAntiAlias(boolean on) {
        antiAlias = jPanel3DView.isVisible() ? (on ? RenderingHints.VALUE_ANTIALIAS_ON
                : RenderingHints.VALUE_ANTIALIAS_OFF)
                : RenderingHints.VALUE_ANTIALIAS_ON;

        repaint();
    }

    protected javax.swing.JFileChooser getFileChooser() {
        if (jFileChooser == null) {
            jFileChooser = new javax.swing.JFileChooser();
            jFileChooser.setCurrentDirectory(new java.io.File(IOUtils.getHomeDirectory()));
            SwingUtils.setDefaultFont(jFileChooser);
            jFileChooser.setFileFilter(new com.argus.io.JPEGFileFilter());
            jFileChooser.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ;
                }
            });
        }
        return jFileChooser;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemSave2File = new javax.swing.JMenuItem();
        buttonGroupChart = new javax.swing.ButtonGroup();
        jPanelChart = new javax.swing.JPanel();
        jPanelChartControls = new javax.swing.JPanel();
        jPanelChartType = new javax.swing.JPanel();
        jRadioButtonPlot = new javax.swing.JRadioButton();
        jRadioButtonArea = new javax.swing.JRadioButton();
        jRadioButtonStackingArea = new javax.swing.JRadioButton();
        jRadioButtonBar = new javax.swing.JRadioButton();
        jRadioButtonStackingBar = new javax.swing.JRadioButton();
        jPanel3DView = new javax.swing.JPanel();
        jCheckBox3DView = new javax.swing.JCheckBox();
        jCheckBoxAntiAliasing = new javax.swing.JCheckBox();

        jMenuItemSave2File.setText("Save to file");
        jMenuItemSave2File
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemSave2FileActionPerformed(evt);
                    }
                });

        jPopupMenu.add(jMenuItemSave2File);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanelChart.setLayout(new javax.swing.BoxLayout(jPanelChart,
                javax.swing.BoxLayout.Y_AXIS));

        chart = createChart();
        jPanelChart.setPreferredSize(new java.awt.Dimension(10, 1000));
        jPanelChart.add(chart);
        add(jPanelChart);

        jPanelChartControls.setLayout(new javax.swing.BoxLayout(
                jPanelChartControls, javax.swing.BoxLayout.X_AXIS));

        jRadioButtonPlot.setMnemonic(JCChart.PLOT);
        jRadioButtonPlot.setText("Plot");
        buttonGroupChart.add(jRadioButtonPlot);
        jRadioButtonPlot.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonPlotItemStateChanged(evt);
            }
        });

        jPanelChartType.add(jRadioButtonPlot);

        jRadioButtonArea.setMnemonic(JCChart.AREA);
        jRadioButtonArea.setText("Area");
        buttonGroupChart.add(jRadioButtonArea);
        jRadioButtonArea.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonPlotItemStateChanged(evt);
            }
        });

        jPanelChartType.add(jRadioButtonArea);

        jRadioButtonStackingArea.setMnemonic(JCChart.STACKING_AREA);
        jRadioButtonStackingArea.setText("Stacking Area");
        buttonGroupChart.add(jRadioButtonStackingArea);
        jRadioButtonStackingArea
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPlotItemStateChanged(evt);
                    }
                });

        jPanelChartType.add(jRadioButtonStackingArea);

        jRadioButtonBar.setMnemonic(JCChart.BAR);
        jRadioButtonBar.setText("Bar");
        buttonGroupChart.add(jRadioButtonBar);
        jRadioButtonBar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonPlotItemStateChanged(evt);
            }
        });

        jPanelChartType.add(jRadioButtonBar);

        jRadioButtonStackingBar.setMnemonic(JCChart.STACKING_BAR);
        jRadioButtonStackingBar.setText("Stacking Bar");
        buttonGroupChart.add(jRadioButtonStackingBar);
        jRadioButtonStackingBar
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPlotItemStateChanged(evt);
                    }
                });

        jPanelChartType.add(jRadioButtonStackingBar);

        jPanelChartControls.add(jPanelChartType);

        jPanel3DView.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.RIGHT, 10, 5));

        jCheckBox3DView.setText("3D View");
        jCheckBox3DView.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox3DViewItemStateChanged(evt);
            }
        });

        jPanel3DView.add(jCheckBox3DView);

        jCheckBoxAntiAliasing.setSelected(true);
        jCheckBoxAntiAliasing.setText("Anti-Aliasing");
        jCheckBoxAntiAliasing
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBoxAntiAliasingItemStateChanged(evt);
                    }
                });

        jPanel3DView.add(jCheckBoxAntiAliasing);

        jPanelChartControls.add(jPanel3DView);

        add(jPanelChartControls);

    }// GEN-END:initComponents

    private void jCheckBoxAntiAliasingItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxAntiAliasingItemStateChanged
        setAntiAlias(evt.getStateChange() == evt.SELECTED);
    }// GEN-LAST:event_jCheckBoxAntiAliasingItemStateChanged

    private void jCheckBox3DViewItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBox3DViewItemStateChanged
        _set3DView(((AbstractButton) evt.getSource()).isSelected());
    }// GEN-LAST:event_jCheckBox3DViewItemStateChanged

    private void jMenuItemSave2FileActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemSave2FileActionPerformed
        saveChart();
    }// GEN-LAST:event_jMenuItemSave2FileActionPerformed

    private void jRadioButtonPlotItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonPlotItemStateChanged
        int chartType = ((AbstractButton) evt.getSource()).getMnemonic();
        _setChartType(chartType);
    }// GEN-LAST:event_jRadioButtonPlotItemStateChanged

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton1ActionPerformed

    }// GEN-LAST:event_jRadioButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu jPopupMenu;

    private javax.swing.JMenuItem jMenuItemSave2File;

    private javax.swing.ButtonGroup buttonGroupChart;

    protected javax.swing.JPanel jPanelChart;

    protected javax.swing.JPanel jPanelChartControls;

    private javax.swing.JPanel jPanelChartType;

    protected javax.swing.JRadioButton jRadioButtonPlot;

    protected javax.swing.JRadioButton jRadioButtonArea;

    protected javax.swing.JRadioButton jRadioButtonStackingArea;

    protected javax.swing.JRadioButton jRadioButtonBar;

    protected javax.swing.JRadioButton jRadioButtonStackingBar;

    private javax.swing.JPanel jPanel3DView;

    private javax.swing.JCheckBox jCheckBox3DView;

    private javax.swing.JCheckBox jCheckBoxAntiAliasing;

    // End of variables declaration//GEN-END:variables

    // My Variables declaration
    protected com.klg.jclass.chart.JCChart chart;

    protected int nextTriggerID = 0;

    private String dateTimeFormat = DateTimeUtils.DEFAULT_INPUT;

    // End of my variables declaration

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#isChartControlsVisible()
     */
    public boolean isChartControlsVisible() {
        return jPanelChartControls.isVisible();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setChartControlsVisible(boolean)
     */
    public void setChartControlsVisible(boolean value) {
        jPanelChartControls.setVisible(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#getDateTimeFormat()
     */
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setDateTimeFormat(java.lang.String)
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    protected com.klg.jclass.chart.JCChart createChart() {

        JCChart _chart = new JCChart();

        // Set Colors
        _chart.setBackground(bg_gray);
        // _chart.setBackground(Color.white);
        _chart.getChartArea().setOpaque(true);
        // _chart.getChartArea().getPlotArea().setBackground( new Color( 0xcc,
        // 0xcc, 0xcc ) );
        _chart.getChartArea().getPlotArea().setBackground(Color.white);
        _chart.setBorder(BorderFactory.createLoweredBevelBorder());

        // X Axis
        if (axisX == null)
            axisX = _chart.getChartArea().getXAxis(0);
        axisX.setEditable(false);
        axisX.setPlacement(JCAxis.MIN);
        axisX.setAnnotationMethod(JCAxis.POINT_LABELS);
        axisX.setGridVisible(true);
        axisX.getGridStyle().getLineStyle().setColor(bg_gray);

        // Header, Footer
        _chart.getHeader().setFont(new Font("TimesRoman", Font.BOLD, 18));
        _chart.getHeader().setVisible(true);

        /*
         * ( (JLabel) _chart.getFooter() ).setText( "<html><center><font
         * color=black><p>" + "Drag to zoom along '<I>left Y axis</I>' axis;
         * Type '<b>r</b>' to reset zoom;" + "<p>Use scrollbar to translate
         * horizontally</center></html>" );
         * _chart.getFooter().setVisible(true);
         */

        // Legend
        _chart.getLegend().setVisible(true);
        _chart.getLegend().setAnchor(com.klg.jclass.util.legend.JCLegend.NORTH);
        _chart.getLegend().setOrientation(
                com.klg.jclass.util.legend.JCLegend.HORIZONTAL);

        // Borders
        _chart.getChartArea().setBorder(
                BorderFactory.createLoweredBevelBorder());

        // Chart fonts
        _chart.getChartArea().setFont(new Font("Helvetica", Font.PLAIN, 12));

        // Tighten up spacing; force the header into the GraphArea
        // _chart.setLayoutHints(_chart.getChartArea(), new Rectangle(
        // Integer.MAX_VALUE, 5, Integer.MAX_VALUE, Integer.MAX_VALUE));
        // _chart.setLayoutHints(_chart.getHeader(), new Rectangle(
        // Integer.MAX_VALUE, 10, Integer.MAX_VALUE, Integer.MAX_VALUE));

        /*
         * Interactive Control
         */
        // _chart.setTrigger(nextTriggerID++, new EventTrigger( 0,
        // EventTrigger.ZOOM));
        // _chart.setTrigger(nextTriggerID++, new EventTrigger( Event.META_MASK,
        // EventTrigger.CUSTOMIZE));
        // Enable editing - implement EditableChartDataModel
        // _chart.setTrigger(nextTriggerID++, new EventTrigger(0,
        // EventTrigger.EDIT));
        // _chart.setTrigger(nextTriggerID++, new
        // EventTrigger(InputEvent.CTRL_MASK, EventTrigger.EDIT));
        // _chart.setAllowUserChanges(true);
        _chart.setForeground(Color.black);
        // _chart.addChartListener(this);

        return _chart;

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setGraphData(com.argus.financials.projection.GraphData)
     */
    public void setGraphData(GraphData graphData) {
        setChartType(graphData.getCharType());

        set3DView(graphData.is3DView());
        setAntiAlias(graphData.isAntiAliasing());
    }

    private void _setChartType(int chartType) {
        java.util.List views = chart.getDataView();
        java.util.Iterator iter = views.iterator();
        while (iter.hasNext()) {
            ChartDataView cdw = (ChartDataView) iter.next();

            if (!cdw.getName().equalsIgnoreCase(PROTECTED))
                cdw.setChartType(chartType); // JCChart.PLOT
        }
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#set3DView(boolean)
     */
    public void set3DView(boolean value) {
        jCheckBox3DView.setSelected(jPanel3DView.isVisible() ? value : false);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setChartType(int)
     */
    public void setChartType(int charType) {
        switch (charType) {
        case BAR:
            jRadioButtonBar.setSelected(true);
            break;
        case PLOT:
            jRadioButtonPlot.setSelected(true);
            break;
        case AREA:
            jRadioButtonArea.setSelected(true);
            break;
        case STACKING_AREA:
            jRadioButtonStackingArea.setSelected(true);
            break;
        case STACKING_BAR:
            jRadioButtonStackingBar.setSelected(true);
            break;
        default:
            setChartType(PLOT);
        }
    }

    private void _set3DView(boolean value) {

        JCChartArea chartArea = chart.getChartArea();

        if (value) {
            chartArea.setDepth(5);
            chartArea.setElevation(18);
            chartArea.setRotation(18);
        } else {
            chartArea.setDepth(0);
            chartArea.setElevation(0);
            chartArea.setRotation(0);
        }

        // *
        java.awt.Graphics gc = chart.getComponentArea().getGraphics();
        if (gc == null)
            gc = chart.getChartArea().getGraphics();
        if (gc == null)
            gc = chart.getGraphics();

        try {
            ChartDataView view = chart.getDataView(0);
            for (int i = 0; i < view.getNumSeries(); i++) {
                JCChartStyle style = view.getSeries(i).getStyle();
                JCFillStyle fillStyle = style.getFillStyle();

                if (gc != null) {
                    // Update the graphics object with the shadow elements of
                    // this line style
                    // -- paint fills and colors -- centered around the anchor
                    // rectangle that may have been set.
                    fillStyle.updateGraphicsToShadow(gc);
                }

            }
        } finally {
            if (gc != null)
                gc.dispose();
        }
        // */
    }

    // Variables declaration
    protected JCAxis axisX;

    protected JCAxis axisY1;

    protected JCAxis axisY2;

    // End of variables declaration

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#getMinAxisY1()
     */
    public double getMinAxisY1() {
        return axisY1 == null ? 0. : axisY1.getMin();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setMinAxisY1(double)
     */
    public void setMinAxisY1(double value) {
        if (axisY1 != null)
            axisY1.setMin(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#getMinAxisY2()
     */
    public double getMinAxisY2() {
        return axisY2 == null ? 0. : axisY2.getMin();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setMinAxisY2(double)
     */
    public void setMinAxisY2(double value) {
        if (axisY2 != null)
            axisY2.setMin(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#getMaxAxisY1()
     */
    public double getMaxAxisY1() {
        return axisY1 == null ? 0. : axisY1.getMax();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setMaxAxisY1(double)
     */
    public void setMaxAxisY1(double value) {
        if (axisY1 != null)
            axisY1.setMax(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#getMaxAxisY2()
     */
    public double getMaxAxisY2() {
        return axisY2 == null ? 0. : axisY2.getMax();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setMaxAxisY2(double)
     */
    public void setMaxAxisY2(double value) {
        if (axisY2 != null)
            axisY2.setMax(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setTitleAxisX(java.lang.String)
     */
    public void setTitleAxisX(String title) {
        JCAxisTitle axisTitle = axisX.getTitle();
        axisTitle.setText(title, true);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setTitleAxisY1(java.lang.String)
     */
    public void setTitleAxisY1(String title) {
        if (axisY1 == null)
            return;

        axisY1.setVisible(title != null);

        JCAxisTitle axisTitle = axisY1.getTitle();
        if (axisTitle == null) {
            if (title != null)
                return;

            axisTitle = new JCAxisTitle(title);
            axisTitle.setPlacement(JCLegend.NORTH);
            axisY1.setTitle(axisTitle);
        } else {
            axisTitle.setText(title, true);
        }

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setTitleAxisY2(java.lang.String)
     */
    public void setTitleAxisY2(String title) {
        if (axisY2 == null)
            return;

        axisY2.setVisible(title != null);

        if (title == null) {
            if (chart.getDataView(1) != null)
                chart.removeDataView(1);
        }

        JCAxisTitle axisTitle = axisY2.getTitle();
        if (axisTitle == null) {
            if (title != null)
                return;

            axisTitle = new JCAxisTitle(title);
            axisTitle.setPlacement(JCLegend.NORTH);
            axisY2.setTitle(axisTitle);
        } else {
            axisTitle.setText(title, true);
        }

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setAxisY1Visible(boolean)
     */
    public void setAxisY1Visible(boolean value) {
        if (axisY1 != null)
            axisY1.setVisible(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setAxisY2Visible(boolean)
     */
    public void setAxisY2Visible(boolean value) {
        if (axisY2 != null)
            axisY2.setVisible(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setAxisXVisible(boolean)
     */
    public void setAxisXVisible(boolean value) {
        if (axisX != null)
            axisX.setVisible(value);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setLabelGeneratorAxisY1(com.klg.jclass.chart.JCLabelGenerator)
     */
    public void setLabelGeneratorAxisY1(JCLabelGenerator generator) {
        if (axisY1 == null)
            return;
        // axisY1.setPrecision(-3);
        axisY1.setLabelGenerator(generator);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#setLabelGeneratorAxisY2(com.klg.jclass.chart.JCLabelGenerator)
     */
    public void setLabelGeneratorAxisY2(JCLabelGenerator generator) {
        if (axisY2 == null)
            return;
        // axisY2.setPrecision(-3);
        axisY2.setLabelGenerator(generator);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#customizeChart(double[][], java.lang.String[], java.lang.String[], java.awt.Color[], int[], int[], boolean, boolean)
     */
    public ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, java.awt.Color[] colors, int[] linePaterns,
            int[] symbolPaterns, boolean leftAxisY, boolean inverted) {

        double[][] xvalues = null;
        double[][] yvalues = values;
        String[] pointLabels = labels;
        String[] seriesLabels = legends;
        String dataSourceName = "";

        ChartDataModel cdm = new JCDefaultDataSource(xvalues, yvalues,
                pointLabels, seriesLabels, dataSourceName);

        chart.getLegend().setVisible(seriesLabels != null);

        ChartDataView view = setDataView(cdm, colors, linePaterns,
                symbolPaterns, leftAxisY);
        view.setInverted(inverted);

        // chart.invalidate();

        return view;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#customizeChart(double[][], java.lang.String[], java.lang.String[], java.awt.Color[], int[], boolean, boolean)
     */
    public ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, java.awt.Color[] colors, int[] linePaterns,
            boolean leftAxisY, boolean inverted) {
        return customizeChart(values, labels, legends, colors, linePaterns,
                null, leftAxisY, inverted);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#customizeChart(double[][], java.util.Date[], java.lang.String[], java.awt.Color[], int[], boolean, boolean)
     */
    public ChartDataView customizeChart(double[][] values,
            java.util.Date[] labels, String[] legends, java.awt.Color[] colors,
            int[] linePaterns, boolean leftAxisY, boolean inverted) {
        String[] pointLabels = new String[labels.length];
        for (int i = 0; i < labels.length; i++)
            pointLabels[i] = DateTimeUtils.asString(labels[i],
                    getDateTimeFormat());
        return customizeChart(values, pointLabels, legends, colors,
                linePaterns, leftAxisY, inverted);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#customizeChart(double[][], java.lang.String[], java.lang.String[], java.awt.Color[], int[], boolean)
     */
    public ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, java.awt.Color[] colors, int[] linePaterns,
            boolean leftAxisY) {
        return customizeChart(values, labels, legends, colors, linePaterns,
                leftAxisY, false);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#customizeChart(double[][], java.lang.String[], java.lang.String[], java.awt.Color[], boolean)
     */
    public ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, java.awt.Color[] colors, boolean leftAxisY) {
        return customizeChart(values, labels, legends, colors, null, leftAxisY);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#customizeChart(double[][], java.lang.String[], java.lang.String[], boolean)
     */
    public ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, boolean leftAxisY) {
        return customizeChart(values, labels, legends, null, null, leftAxisY);
    }

    protected ChartDataView setDataView(ChartDataModel model,
            java.awt.Color[] colors, int[] linePaterns, int[] symbolPaterns,
            boolean leftAxisY) {

        JCAxis axisY = leftAxisY ? axisY1 : axisY2;

        if (axisY == null) {
            // Initialize axis
            if (leftAxisY) {
                axisY = chart.getChartArea().getYAxis(0);
                axisY.setPlacement(JCAxis.MIN);

                // Set Grids
                axisY.setGridVisible(true);
                axisY.getGridStyle().getLineStyle().setColor(bg_gray);

                // assign for future use
                axisY1 = axisY;
            } else {
                axisY = new JCAxis();
                axisY.setPlacement(JCAxis.MAX);

                // Disallow zooming on this axis
                axisY.setEditable(false);
                chart.getChartArea().setYAxis(1, axisY);

                // assign for future use
                axisY2 = axisY;
            }
            axisY.setVertical(true);
            axisY.setAnnotationMethod(JCAxis.VALUE);

            // axisY.getTitle().setRotation(ChartText.DEG_270);
        }

        if (axisX == null)
            axisX = chart.getChartArea().getXAxis(0);

        ChartDataView view = new ChartDataView();
        view.setName("View");

        axisY.setMaxIsDefault(true);

        view.setXAxis(axisX);
        view.setYAxis(axisY);

        view.setOutlineColor(Color.darkGray);
        view.setDataSource(model);
        view.setVisibleInLegend(true);

        // set series colors
        for (int i = 0; i < view.getNumSeries(); i++) {
            JCChartStyle style = view.getSeries(i).getStyle();

            if (colors != null && i < colors.length) {
                style.setLineColor(colors[i]);
                style.setSymbolColor(colors[i]);
                style.setFillColor(colors[i]);
            }

            if (linePaterns != null && i < linePaterns.length) {
                style.setLinePattern(linePaterns[i]);
            }
            // style.setLineWidth( 2 );

            if (symbolPaterns != null && i < symbolPaterns.length) {
                style.setSymbolShape(symbolPaterns[i]);
            }
            style.setSymbolSize(10);

        }

        boolean was_batched = chart.isBatched();
        chart.setBatched(true);
        view.setBatched(true);

        if (leftAxisY) {
            chart.setDataView(0, view);
        } else {
            chart.setDataView(1, view);
        }

        ButtonModel bm = buttonGroupChart.getSelection();
        if (bm == null) {
            view.setChartType(JCChart.BAR);
            jRadioButtonPlot.doClick();
            /*
             * view.setChartType(JCChart.PIE); chart.getChartArea().setDepth(5);
             * chart.getChartArea().setElevation(45);
             * chart.getChartArea().setRotation(0);
             */
        } else {
            view.setChartType(bm.getMnemonic());
        }

        view.setBatched(false);
        chart.setBatched(was_batched);

        return view;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#initialiseChart()
     */
    public void initialiseChart() {

        int views = chart.getNumData();
        for (int i = views; i > 0; i--)
            chart.removeDataView(i);

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#addChartLabels()
     */
    public void addChartLabels() {
        addChartLabels(chart);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#addChartLabels(com.klg.jclass.chart.JCChart)
     */
    public void addChartLabels(JCChart _chart) {

        if (_chart == null)
            return;
        removeChartLabels(_chart);

        Currency format = Currency.getCurrencyInstance();
        JCChartLabelManager clm = _chart.getChartLabelManager();

        java.util.Iterator iter = (_chart.getDataView()).iterator();
        while (iter.hasNext()) {
            ChartDataView view = (ChartDataView) iter.next();

            addChartLabels(view, clm, format);
        }

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#addChartLabels(com.klg.jclass.chart.ChartDataView, com.argus.format.Formatable)
     */
    public void addChartLabels(ChartDataView view, Formatable format) {
        addChartLabels(view, chart.getChartLabelManager(), format);
    }

    protected void addChartLabels(ChartDataView view, JCChartLabelManager clm,
            Formatable format) {

        if (view == null || clm == null)
            return;
        clm.removeAutoLabels(view);

        if (format == null)
            format = Number2.getNumberInstance();

        for (int i = 0; i < view.getNumSeries(); i++) {
            ChartDataViewSeries series = view.getSeries(i);
            String label = series.getLabel();

            for (int j = 0; j < series.maxYIndex(); j++) {
                JCChartLabel cl = new JCChartLabel();

                String l;
                int p = label.indexOf("</html>");
                if (p > 0) {
                    l = label.substring(0, p) + ": "
                            + format.toString(series.getY(j)) + "</html>";
                } else {
                    l = label + ": " + format.toString(series.getY(j));
                }
                cl.setText(l);

                cl.setDwellLabel(true);
                cl.setAttachMethod(JCChartLabel.ATTACH_DATAINDEX);
                cl.setDataIndex(new JCDataIndex(view, series, i, j));
                JLabel labelComp = (JLabel) cl.getComponent();
                labelComp.setOpaque(true);
                labelComp.setBackground(light_bg);
                labelComp.setForeground(java.awt.Color.black);
                labelComp.setBorder(BorderFactory.createEtchedBorder());
                clm.addChartLabel(cl);
            }

        }

    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#removeChartLabels()
     */
    public void removeChartLabels() {
        removeChartLabels(chart);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#removeChartLabels(com.klg.jclass.chart.JCChart)
     */
    public void removeChartLabels(JCChart _chart) {
        if (_chart == null)
            return;
        _chart.getChartLabelManager().removeAllChartLabels();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#saveChart()
     */

    public String saveChart() {
        // create javax.swing.JFileChooser if null
        getFileChooser();

        jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // jFileChooser.setFileFilter( new JPEGFileFilter() );
        jFileChooser.setDialogTitle("Save chart as ...");

        if (jFileChooser.showOpenDialog(SwingUtil.getJFrame(this)) != JFileChooser.APPROVE_OPTION)
            return null;

        try {
            String fileName = jFileChooser.getSelectedFile().getCanonicalPath();
            // if ( fileName.indexOf( '.' ) <= 0 )
            // fileName += "." + IOUtils.JPG;
            saveChart(fileName);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // using JClass encoder
    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#saveChart(java.lang.String)
     */
    public void saveChart(String fileName) throws Exception {
        saveChart(new java.io.File(fileName));
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#saveChart(java.io.File)
     */
    public void saveChart(java.io.File file) throws Exception {
        try {
            ImageUtils.encodeAsJPEG(chart, file);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // using Sun encoder
    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#saveChart2(java.lang.String)
     */
    public void saveChart2(String fileName) throws Exception {
        saveChart2(new java.io.File(fileName));
    }

    /* (non-Javadoc)
     * @see com.argus.financials.ui.IGraphView#saveChart2(java.io.File)
     */
    public void saveChart2(java.io.File file) throws Exception {
//        BufferedImage image = (BufferedImage) chart.snapshot(BufferedImage.SCALE_SMOOTH);
//        ImageUtils.encodeAsJPEG(image, file);
        ImageUtils.encodeAsJPEG(chart, file);
    }

    // prepare chart for save (set optimal size, background color, etc.)
    /* (non-Javadoc)
     * @see com.argus.io.Encoder#encodeAsJPEG(java.io.File)
     */
    public void encodeAsJPEG(java.io.File file) throws Exception {
        // remove background
        Color oldBackground = chart.getBackground();
        chart.setBackground(Color.white);

        javax.swing.border.Border oldChartAreaBorder = chart.getChartArea()
                .getBorder();
        chart.getChartArea().setBorder(BorderFactory.createEmptyBorder());

        try {
            encodeAsJPEG(file, chart);
        } finally {
            jPanelChart.add(chart);
            chart.setBackground(oldBackground);
            chart.getChartArea().setBorder(oldChartAreaBorder);
        }

    }

    /* (non-Javadoc)
     * @see com.argus.io.Encoder#encodeAsJPEG(java.io.File, javax.swing.JComponent)
     */
    public void encodeAsJPEG(java.io.File file, JComponent comp)
            throws Exception {

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
        } finally {
            window.getContentPane().remove(comp);
            window.dispose();
            window = null;
        }

    }

}