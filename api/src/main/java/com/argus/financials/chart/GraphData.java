/*
 * GraphData.java
 *
 * Created on 27 August 2002, 09:24
 */

package com.argus.financials.chart;

import com.argus.io.JPEGFileFilter;
import com.klg.jclass.chart.JCChart;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class GraphData {

    public static final String PLOT = "Plot";

    public static final String AREA = "Area";

    public static final String STACKING_AREA = "Stacking Area";

    public static final String BAR = "Bar";

    public static final String STACKING_BAR = "Stacking Bar";

    public static final String THREE_D_VIEW = "3D View";

    public static final String ANTI_ALIASING = "Anti-Aliasing";

    private int charType;

    private boolean threeDView;

    private boolean antiAliasing;

    /** Creates new GraphData */
    public GraphData() {
    }

    public boolean update(Object property, String value) {

        if (PLOT.equals(property)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                charType = JCChart.PLOT;

        } else if (AREA.equals(property)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                charType = JCChart.AREA;

        } else if (STACKING_AREA.equals(property)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                charType = JCChart.STACKING_AREA;

        } else if (BAR.equals(property)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                charType = JCChart.BAR;

        } else if (STACKING_BAR.equals(property)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                charType = JCChart.STACKING_BAR;

        } else if (THREE_D_VIEW.equals(property)) {
            threeDView = Boolean.TRUE.toString().equalsIgnoreCase(value) ? true
                    : false;

        } else if (ANTI_ALIASING.equals(property)) {
            antiAliasing = Boolean.TRUE.toString().equalsIgnoreCase(value) ? true
                    : false;

        } else
            return false;

        return true;

    }

    public int getCharType() {
        return charType;
    }

    public boolean is3DView() {
        return threeDView;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public java.io.File encodeAsJPEG(String title, double[][] values,
            String[] labels, String[] legends, java.awt.Color[] colors,
            int[] linePaterns, boolean leftAxisY, boolean inverted,
            com.klg.jclass.chart.JCLabelGenerator labelGenerator)
            throws Exception {

        if (title == null || title.trim().length() < 3)
            title = "___"; // 3 or more, restriction on
                            // java.io.File.createTempFile
        java.io.File file = java.io.File.createTempFile(title, "."
                + JPEGFileFilter.JPG);
        file.deleteOnExit();

        IGraphView graphView = new GraphView();
        graphView.customizeChart(values, labels, legends, colors, linePaterns,
                leftAxisY, inverted);

        // graphView.setTitleAxisX1("");

        graphView.setTitleAxisY1("");
        graphView.setLabelGeneratorAxisY1(labelGenerator);

        // graphView.setTitleAxisY2(null);

        graphView.encodeAsJPEG(file);

        return file; // .getCanonicalPath();

    }

}
