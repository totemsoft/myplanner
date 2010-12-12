/*
 * StrategyFinancialGraphView.java
 *
 * Created on 22 April 2002, 10:08
 */

package com.argus.financials.ui.strategy;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.IGraphView;
import com.klg.jclass.chart.JCChart;

public class StrategyFinancialGraphView extends
        com.argus.financials.GraphView {

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
