/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.financials;

import com.argus.io.ImageEncoder;
import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.JCLabelGenerator;

/*
 * Created on 11/07/2006
 * 
 */

public interface IGraphView extends ImageEncoder {

    // chart type
    int NONE = -1;
    int PLOT = 0;
    int SCATTER_PLOT = 1;
    int POLAR = 2;
    int RADAR = 3;
    int FILLED_RADAR = 4;
    int HILO = 5;
    int HILO_OPEN_CLOSE = 6;
    int CANDLE = 7;
    int AREA = 8;
    int BAR = 9;
    int STACKING_BAR = 10;
    int PIE = 11;
    int STACKING_AREA = 12;

    void setPreferredSize(int width, int height);

    void setAntiAlias(boolean on);

    boolean isChartControlsVisible();

    void setChartControlsVisible(boolean value);

    String getDateTimeFormat();
    void setDateTimeFormat(String dateTimeFormat);

//    void setGraphData(GraphData graphData);

    void set3DView(boolean value);

    void setChartType(int charType);

    double getMinAxisY1();
    void setMinAxisY1(double value);

    double getMinAxisY2();
    void setMinAxisY2(double value);

    double getMaxAxisY1();
    void setMaxAxisY1(double value);

    double getMaxAxisY2();
    void setMaxAxisY2(double value);

    void setTitleAxisX(String title);

    void setTitleAxisY1(String title);

    void setTitleAxisY2(String title);

    void setAxisY1Visible(boolean value);

    void setAxisY2Visible(boolean value);

    void setAxisXVisible(boolean value);

    /**
     * Use a custom label generator ( e.g. CurrencyLabelGenerator.getInstance() )
     * to scale the Volume axis
     */
    void setLabelGeneratorAxisY1(JCLabelGenerator generator);

    void setLabelGeneratorAxisY2(JCLabelGenerator generator);

//    /**
//     * set dual data view, two Y axis (controlled by leftAxisY parameter)
//     * 
//     * JCLineStyle.* NONE, SOLID, LONG_DASH, SHORT_DASH, LSL_DASH or DASH_DOT
//     * JCSymbolStyle.* NONE, DOT, BOX, TRIANGLE, DIAMOND, STAR, VERT_LINE,
//     * HORIZ_LINE, CROSS, CIRCLE, SQUARE
//     */
//    ChartDataView customizeChart(double[][] values, String[] labels,
//            String[] legends, java.awt.Color[] colors, int[] linePaterns,
//            int[] symbolPaterns, boolean leftAxisY, boolean inverted);

    ChartDataView customizeChart(double[][] values, String[] labels,
            String[] legends, java.awt.Color[] colors, int[] linePaterns,
            boolean leftAxisY, boolean inverted);

    ChartDataView customizeChart(double[][] values,
            java.util.Date[] labels, String[] legends, java.awt.Color[] colors,
            int[] linePaterns, boolean leftAxisY, boolean inverted);

//    ChartDataView customizeChart(double[][] values, String[] labels,
//            String[] legends, java.awt.Color[] colors, int[] linePaterns,
//            boolean leftAxisY);
//
//    ChartDataView customizeChart(double[][] values, String[] labels,
//            String[] legends, java.awt.Color[] colors, boolean leftAxisY);
//
//    ChartDataView customizeChart(double[][] values, String[] labels,
//            String[] legends, boolean leftAxisY);
//
//    void initialiseChart();

    /**
     * Create chart labels for every pie slice, including the other slice.
     */
    void addChartLabels();

//    void addChartLabels(JCChart _chart);
//
//    void addChartLabels(ChartDataView view, Formatable format);

    /**
     * Remove all chart labels from the chart.
     */
    void removeChartLabels();

//    void removeChartLabels(JCChart _chart);

    /**
     * 
     */
    String saveChart();

    // using JClass encoder
    void saveChart(String fileName) throws java.io.IOException;

    void saveChart(java.io.File file) throws java.io.IOException;

    // using Sun encoder
    void saveChart2(String fileName) throws java.io.IOException;

    void saveChart2(java.io.File file) throws java.io.IOException;

}
