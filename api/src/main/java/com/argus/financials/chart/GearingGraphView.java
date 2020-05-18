/*
 * GearingGraphView.java
 *
 * Created on 8 January 2002, 17:29
 */

package com.argus.financials.chart;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import com.argus.format.CurrencyLabelGenerator;
import com.argus.format.PercentLabelGenerator;
import com.klg.jclass.chart.ChartDataModel;
import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.JCAxis;
import com.klg.jclass.chart.JCChart;
import com.klg.jclass.chart.JCChartListener;

public class GearingGraphView extends GraphView
        implements JCChartListener, AdjustmentListener {

    /** Creates new GearingGraphView */
    public GearingGraphView() {
        super();

        // jPanelChart.setPreferredSize( new java.awt.Dimension( 400, 1000 ) );

        initComponents2();
    }

    private void initComponents2() {

        // jPanelChartControls.setVisible( false );

        /*
         * jRadioButtonBar.setVisible( false );
         * jRadioButtonStackingBar.setVisible( false );
         * jRadioButtonPlot.setVisible( false ); jRadioButtonArea.setVisible(
         * false ); jRadioButtonStackingArea.setVisible( false );
         */

        // Min must be less than Max.Setting MaxIsDefault to true
        /*
         * slider = new JScrollBar(SwingConstants.HORIZONTAL);
         * slider.addAdjustmentListener(this); add(slider);
         */

    }

    /**
     * JCChartListener interface
     */
    public void changeChart(com.klg.jclass.chart.JCChartEvent jCChartEvent) {
        JCAxis modAxis = jCChartEvent.getModifiedAxis();
        if (modAxis == axisX) {
            // reset was done -- make sure it doesn't effect x axis, which
            // we're controlling with a scroll bar
            updateAxisFromSlider();
        }
    }

    public void paintChart(com.klg.jclass.chart.JCChart jCChart) {
    }

    /**
     * AdjustmentListener interface
     */
    public void adjustmentValueChanged(
            java.awt.event.AdjustmentEvent adjustmentEvent) {
        if (adjustmentEvent.getSource() != slider)
            return;

        updateAxisFromSlider();
    }

    /**
     * Take the current value from the slider and apply it to the x axis.
     */
    public void updateAxisFromSlider() {
        chart.setBatched(true);
        axisMin = slider == null ? 0 : slider.getValue();
        axisMax = axisMin + getLengthVisible();
        axisX.setMin(axisMin);
        axisX.setMax(axisMax);
        chart.setBatched(false);
    }

    // Variables declaration - do not modify
    private JScrollBar slider;

    private int sliderMin = 0;

    private int sliderMax = 100;

    private double percentVisible = 1.00;

    private int axisMin = 0;

    private int axisMax = 1;

    // End of variables declaration

    private int getLengthVisible() {
        return (int) Math.ceil(percentVisible * (sliderMax - sliderMin));
    }

    /**
     * 
     */
    protected com.klg.jclass.chart.JCChart createChart() {

        JCChart _chart = super.createChart();

        // Header, Footer
        /*
         * ( (JLabel) _chart.getFooter() ).setText( "<html><center><font
         * color=black><p>" + "Drag to zoom along '<I>left Y axis</I>' axis;
         * Type '<b>r</b>' to reset zoom;" + "<p>Use scrollbar to translate
         * horizontally</center></html>" );
         * _chart.getFooter().setVisible(true);
         */

        // Interactive Control
        // _chart.setTrigger(nextTriggerID++, new EventTrigger( 0,
        // EventTrigger.ZOOM));
        // _chart.setTrigger(nextTriggerID++, new EventTrigger( Event.META_MASK,
        // EventTrigger.CUSTOMIZE));
        _chart.setAllowUserChanges(true);

        _chart.addChartListener(this);

        setTitleAxisX("Years");

        return _chart;
    }

    public void setChartType(int chartType) {

        super.setChartType(chartType);

    }

    public ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, java.awt.Color[] colors, boolean leftAxisY) {

        ChartDataView cdv = super.customizeChart(values, labels, legends,
                colors, leftAxisY);

        if (values != null && values.length > 0) {
            sliderMin = -1;
            sliderMax = values[0].length;

            if (slider != null) {
                slider.setMinimum(sliderMin);
                slider.setMaximum(sliderMax);
                slider.setVisibleAmount(getLengthVisible());
            }

        }

        return cdv;
    }

    protected ChartDataView setDataView(ChartDataModel model,
            java.awt.Color[] colors, int[] linePaterns, boolean leftAxisY) {
        return setDataView(model, colors, linePaterns, null, leftAxisY);
    }

    protected ChartDataView setDataView(ChartDataModel model,
            java.awt.Color[] colors, int[] linePaterns, int[] symbolPaterns,
            boolean leftAxisY) {

        ChartDataView view = super.setDataView(model, colors, linePaterns,
                symbolPaterns, leftAxisY);

        if (leftAxisY) {
            // Use a custom label generator to scale the Volume axis
            // axisY.setPrecision(-3);
            axisY1.setLabelGenerator(CurrencyLabelGenerator.getInstance());

            // view.setChartType(JCChart.PLOT); default
        } else {
            // Use a custom label generator to scale the Volume axis
            // axisY.setPrecision(-3);
            axisY2.setLabelGenerator(PercentLabelGenerator.getInstance());

            // view.setChartType(JCChart.BAR);
        }

        return view;
    }

}
