/*
 * StrategyFinancialGraphView.java
 *
 * Created on 22 April 2002, 10:08
 */

package com.argus.financials.ui.strategy;

import com.argus.financials.chart.IGraphView;
import com.klg.jclass.chart.JCChart;

public class StrategyFinancialGraphView extends
        com.argus.financials.chart.GraphView {

    /** Creates new StrategyFinancialGraphView */
    public StrategyFinancialGraphView() {
        super();

        initComponents();
    }

    private void initComponents() {

        setChartType(IGraphView.BAR);
        jPanelChartControls.setVisible(false);

    }

    /**
     * 
     */
    protected JCChart createChart() {

        JCChart _chart = super.createChart();

        setTitleAxisX("Financials");

        return _chart;

    }

}
