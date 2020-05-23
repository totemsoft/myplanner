/*
 * StrategyResultView.java
 *
 * Created on 19 December 2001, 14:20
 */

package au.com.totemsoft.myplanner.chart;

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
