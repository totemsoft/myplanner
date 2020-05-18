/*
 * StrategyResultView.java
 *
 * Created on 19 December 2001, 14:20
 */

package com.argus.financials.chart;

import com.klg.jclass.chart.JCChart;

public class APGraphViewNew extends GraphView {

    /** Creates new StrategyResultView */
    public APGraphViewNew() {
        super();
    }

    /**
     * 
     */
    protected JCChart createChart() {

        JCChart _chart = super.createChart();

        setTitleAxisX("Age");

        return _chart;

    }

}
