/*
 * WealthData.java
 *
 * Created on 12 December 2002, 09:21
 */

package au.com.totemsoft.myplanner.swing.data;

import com.klg.jclass.chart.ChartDataView;
import com.klg.jclass.chart.JCBarChartFormat;
import com.klg.jclass.chart.JCChart;
import com.klg.jclass.chart.JCSymbolStyle;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.CurrencyLabelGenerator;
import au.com.totemsoft.io.ImageUtils;
import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.myplanner.bean.AbstractBase;
import au.com.totemsoft.myplanner.bean.Assumptions;
import au.com.totemsoft.myplanner.chart.GraphView;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.report.Reportable;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.table.model.RetirementTableModel;
import au.com.totemsoft.myplanner.table.model.WealthTableModel;
import au.com.totemsoft.myplanner.table.swing.ProxyTableModel;
import au.com.totemsoft.swing.SwingUtils;

public class WealthData
    extends AbstractBase
    implements FinancialClassID, Reportable,
        javax.swing.event.ChangeListener {

    private String prefix;

    public String getReportFieldsPrefix() {
        return prefix;
    }

    private java.util.Map financials;

    private Assumptions assumptions;

    private WealthTableModel wtm;

    private RetirementTableModel rtm;

    // public String NetAssetBeforeRetirement;
    // public String RealValueBeforeRetirement;

    /**
     * Creates a new instance of WealthData
     * 
     * WealthViev <-- WeathData <-- Assumptions --> AssumptionView
     * 
     */
    public WealthData(java.util.Map financials, Assumptions assumptions,
            String prefix) {

        this.financials = financials;
        this.prefix = prefix == null ? ReportFields.CURRENT_PREFIX : prefix;

        this.assumptions = assumptions;
        this.assumptions.addChangeListener(this);

        // update table
        wtm = new WealthTableModel(financials, assumptions);
        rtm = new RetirementTableModel(wtm.getLastGrandTotal(), assumptions);

    }

    public void clear() {
        super.clear();

    }

    public java.util.Map getFinancials() {
        return financials;
    }

    public Assumptions getAssumptions() {
        return assumptions;
    }

    public WealthTableModel getWealthTableModel() {
        if (wtm == null)
            wtm = new WealthTableModel(financials, assumptions);
        return wtm;
    }

    public ProxyTableModel getWealthProxyTableModel() {
        return getWealthTableModel().getProxy();
    }

    public RetirementTableModel getRetirementTableModel() {
        if (rtm == null)
            rtm = new RetirementTableModel(getWealthTableModel()
                    .getLastGrandTotal(), assumptions);
        return rtm;
    }

    public ProxyTableModel getRetirementProxyTableModel() {
        return getRetirementTableModel().getProxy();
    }

    private PersonService person;

    public void update() throws au.com.totemsoft.myplanner.api.ServiceException {
        assumptions.disableNotify();
        try {
            assumptions.update(person);
        } finally {
            assumptions.enableNotify();
        }
    }

    public void update(PersonService _person, java.util.Map _financials)
            throws au.com.totemsoft.myplanner.api.ServiceException {
        this.person = _person;
        this.financials = _financials;
        update();
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        if (changeEvent != null && changeEvent.getSource() != assumptions)
            ; // throw ???

        wtm = new WealthTableModel(financials, assumptions);
        rtm = new RetirementTableModel(wtm.getLastGrandTotal(), assumptions);
        notifyChangeListeners();

    }

    /***************************************************************************
     * au.com.totemsoft.activex.Reportable interface
     **************************************************************************/
    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields)
            throws Exception {
        initializeReportData(reportFields, clientService);
    }

    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields,
            au.com.totemsoft.myplanner.service.PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        // assumptions
        reportFields.setValue(reportFields.Wealth_YearsToProject, assumptions
                .getYearsToProject());
        reportFields.setValue(reportFields.Wealth_Inflation, assumptions
                .getInflation());
        reportFields.setValue(reportFields.Wealth_RetirementDate, assumptions
                .getRetirementDate());
        reportFields.setValue(reportFields.Wealth_RetirementAge,
                (int) assumptions.getClientAgeAtRetirement());
        reportFields.setValue(reportFields.Wealth_RetirementIncome, curr
                .toString(assumptions.getRetirementIncome()));
        reportFields.setValue(reportFields.Wealth_RetirementIncomeAtRetirement,
                curr.toString(assumptions.getRetirementIncomeAtRetirement()));
        reportFields.setValue(reportFields.Wealth_LumpSumRequired, curr
                .toString(assumptions.getLumpSumRequired()));
        reportFields.setValue(reportFields.Wealth_LumpSumRequiredAtRetirement,
                curr.toString(assumptions.getLumpSumRequiredAtRetirement()));
        reportFields.setValue(reportFields.Wealth_InvestmentStrategy,
                assumptions.getInvestmentStrategyDesc());
        reportFields.setValue(reportFields.Wealth_IncomeRate, assumptions
                .getIncomeRate());
        reportFields.setValue(reportFields.Wealth_GrowthRate, assumptions
                .getGrowthRate());
        reportFields.setValue(reportFields.Wealth_DebtRepayment, assumptions
                .getDebtRepayment() ? "Yes" : "No");

        // //////////////////////////////////////////////////////////////////////
        // WEALTH
        // //////////////////////////////////////////////////////////////////////
        reportFields.setValue(prefix + reportFields.Wealth_Table,
                getWealthProxyTableModel());
        reportFields.setValue(prefix + reportFields.Wealth_GraphName,
                "Wealth Analysis");
        reportFields.setValue(prefix + reportFields.Wealth_Graph, ImageUtils
                .encodeAsJPEG(getGraphView()));

        // //////////////////////////////////////////////////////////////////////
        // RETIREMENT
        // //////////////////////////////////////////////////////////////////////
        reportFields.setValue(prefix + reportFields.Wealth_RetirementTable,
                getRetirementProxyTableModel());
        reportFields.setValue(prefix + reportFields.Wealth_RetirementGraphName,
                "Retirement Analysis");
        reportFields.setValue(prefix + reportFields.Wealth_RetirementGraph,
                ImageUtils.encodeAsJPEG(getGraphViewRetirement()));

    }

    /***************************************************************************
     * 
     **************************************************************************/
    private GraphView graphView;

    public GraphView getGraphView() {

        if (graphView == null) {
            graphView = new GraphView();
            graphView.setAntiAlias(true);
            SwingUtils.setDefaultFont(graphView);
        }

        WealthTableModel.GrandGroupFooterRow footer = wtm.getGrandTotal();
        if (footer == null) {

        } else {
            graphView.removeChartLabels();
            graphView.setPreferredSize(new java.awt.Dimension(800, 500));

            String[] labels = (String[]) wtm.getColumnNames().toArray(
                    new String[0]);

            Object[][] criteria = {
                    { ASSET_CASH, ASSET_INVESTMENT, ASSET_SUPERANNUATION,
                            ASSET_INCOME_STREAM }, { LIABILITY } };
            double[][] totals = footer.getSelectedGroupTotals(criteria);

            ChartDataView cdw = graphView.customizeChart(totals, labels,
                    new String[] { "Assets", "Liabilities" },
                    new java.awt.Color[] { java.awt.Color.green,
                            java.awt.Color.red }, true);

            cdw.setChartType(JCChart.BAR);

            graphView.addChartLabels(cdw, Currency.getCurrencyInstance());
            graphView.setTitleAxisY1("");
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                    .getInstance());
            graphView.setMinAxisY1(0.);

            WealthTableModel.GroupFooterRowInflated footer2 = wtm
                    .getGrandTotalInflated();

            totals = new double[][] { footer.getGroupTotals(),
                    footer2.getGroupTotals() };

            // JCSymbolStyle.* EMPTY, DOT, BOX, TRIANGLE, DIAMOND, STAR,
            // VERT_LINE, HORIZ_LINE, CROSS, CIRCLE, SQUARE
            cdw = graphView
                    .customizeChart(totals, (String[]) wtm.getColumnNames()
                            .toArray(new String[0]), new String[] {
                            "Grand Total", "Real Value (today's Dollars)" },
                            new java.awt.Color[] { java.awt.Color.blue,
                                    new java.awt.Color(102, 0, 102) }, null, // linePaterns
                            new int[] { JCSymbolStyle.DIAMOND,
                                    JCSymbolStyle.TRIANGLE }, // symbolPaterns
                            false, // leftAxisY
                            false // inverted
                    );

            cdw.setChartType(JCChart.PLOT);

            graphView.addChartLabels(cdw, Currency.getCurrencyInstance());
            graphView.setLabelGeneratorAxisY2(CurrencyLabelGenerator
                    .getInstance());

            graphView.setMinAxisY2(graphView.getMinAxisY1());

            double maxY = java.lang.Math.max(graphView.getMaxAxisY1(),
                    graphView.getMaxAxisY2());
            graphView.setMaxAxisY1(maxY);
            graphView.setMaxAxisY2(maxY);
            // graphView.setAxisY2Visible(false);

        }

        return graphView;

    }

    private GraphView graphViewRetirement;

    public GraphView getGraphViewRetirement() {

        if (graphViewRetirement == null) {
            graphViewRetirement = new GraphView();
            graphViewRetirement.setAntiAlias(true);
            SwingUtils.setDefaultFont(graphViewRetirement);
        }

        double[] retirementValues = rtm.getRetirementValues();

        if (retirementValues == null) {

        } else {
            String[] columnNames = retirementValues.length > 0 ? (String[]) rtm
                    .getColumnNames().toArray(new String[0]) : new String[1];

            graphViewRetirement.removeChartLabels();
            graphViewRetirement.setPreferredSize(new java.awt.Dimension(800,
                    500));

            graphViewRetirement.initialiseChart();
            ChartDataView rdw = graphViewRetirement
                    .customizeChart(
                            retirementValues.length > 0 ? new double[][] { retirementValues }
                                    : new double[][] { new double[1] },
                            columnNames,
                            new String[] { "Value at Retirement" },
                            new java.awt.Color[] { java.awt.Color.green }, true // left
                    );

            rdw.setChartType(JCChart.PLOT);

            graphViewRetirement.addChartLabels(rdw, Currency
                    .getCurrencyInstance());
            graphViewRetirement.setTitleAxisY1("");
            graphViewRetirement.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                    .getInstance());
            graphViewRetirement.setMinAxisY1(0.);

            // Changes made according to tasks 10004 & 10007
            if (getAssumptions().getLifeExpectancy()) {
                double[] lifeExpectancy = rtm.getLifeExpectancyValues();
                rdw = graphViewRetirement
                        .customizeChart(
                                retirementValues.length > 0 ? new double[][] { lifeExpectancy }
                                        : new double[][] { new double[1] },
                                columnNames,
                                new String[] { "Life Expectancy" },
                                new java.awt.Color[] { java.awt.Color.blue },
                                null, // linePaterns
                                new int[] { JCSymbolStyle.STAR }, // symbolPaterns
                                false, // leftAxisY
                                false // inverted
                        );

                rdw.setChartType(JCChart.BAR);
                rdw.setName(graphViewRetirement.PROTECTED);

                ((JCBarChartFormat) rdw.getChartFormat(JCChart.BAR))
                        .setClusterWidth(5);

            }

            graphViewRetirement.setAxisY2Visible(false);

        }

        return graphViewRetirement;

    }

}
