/*
 * StrategyFinancialGraphView.java
 *
 * Created on 22 April 2002, 10:08
 */

package au.com.totemsoft.myplanner.swing.strategy;

import com.klg.jclass.chart.JCChart;

import au.com.totemsoft.myplanner.chart.IGraphView;

public class StrategyFinancialGraphView extends
        au.com.totemsoft.myplanner.chart.GraphView {

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
