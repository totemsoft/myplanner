/*
 * CashFlowData.java
 *
 * Created on 16 June 2002, 17:53
 */

package com.argus.financials.ui.data;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.bean.Assumptions;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.financials.table.CashflowTableModel;

public class CashFlowData extends com.argus.financials.bean.AbstractBase
        implements com.argus.financials.report.Reportable,
        javax.swing.event.ChangeListener {
    private String prefix;

    public String getReportFieldsPrefix() {
        return prefix;
    }

    // used in view/reports
    private java.util.Map financials;

    private Assumptions assumptions;

    private CashflowTableModel cftm;

    /**
     * Creates a new instance of CashFlowData
     * 
     * CashFlowViev <-- CashFlowData <-- Assumptions --> AssumptionView
     * 
     */
    public CashFlowData(java.util.Map financials, Assumptions assumptions,
            String prefix) {

        this.financials = financials;
        this.prefix = prefix == null ? ReportFields.CURRENT_PREFIX : prefix;

        this.assumptions = assumptions;
        this.assumptions.addChangeListener(this);

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

    public CashflowTableModel getTableModel() {
        if (cftm == null)
            cftm = new CashflowTableModel(financials, assumptions);
        return cftm;
    }

    public ProxyTableModel getProxyTableModel() {
        return getTableModel().getProxy();
    }

    private PersonService person;

    public void update() throws com.argus.financials.service.ServiceException {
        assumptions.disableNotify();
        try {
            assumptions.update(person);
        } finally {
            assumptions.enableNotify();
        }
    }

    public void update(PersonService _person, java.util.Map _financials)
            throws com.argus.financials.service.ServiceException {
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

        cftm = new CashflowTableModel(financials, assumptions);
        notifyChangeListeners();

    }

    /***************************************************************************
     * com.argus.activex.Reportable interface
     **************************************************************************/
    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields,
                com.argus.financials.service.ServiceLocator.getInstance()
                        .getClientPerson());
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields,
            com.argus.financials.service.PersonService person)
            throws java.io.IOException {

        if (person != null)
            reportFields.initialize(person);

        // assumptions
        reportFields.setValue(reportFields.Cashflow_YearsToProject, assumptions
                .getYearsToProject());
        reportFields.setValue(reportFields.Cashflow_Inflation, assumptions
                .getInflation());
        reportFields.setValue(reportFields.Cashflow_RetirementDate, assumptions
                .getRetirementDate());
        reportFields.setValue(reportFields.Cashflow_RetirementAge,
                (int) assumptions.getClientAgeAtRetirement());
        reportFields.setValue(reportFields.Cashflow_RetirementIncome, curr
                .toString(assumptions.getRetirementIncome()));
        reportFields.setValue(
                reportFields.Cashflow_RetirementIncomeAtRetirement,
                curr.toString(assumptions.getRetirementIncomeAtRetirement()));
        reportFields.setValue(reportFields.Cashflow_LumpSumRequired, curr
                .toString(assumptions.getLumpSumRequired()));
        reportFields
                .setValue(reportFields.Cashflow_LumpSumRequiredAtRetirement,
                        curr.toString(assumptions
                                .getLumpSumRequiredAtRetirement()));
        reportFields.setValue(reportFields.Cashflow_InvestmentStrategy,
                assumptions.getInvestmentStrategyDesc());
        reportFields.setValue(reportFields.Cashflow_IncomeRate, assumptions
                .getIncomeRate());
        reportFields.setValue(reportFields.Cashflow_GrowthRate, assumptions
                .getGrowthRate());
        reportFields.setValue(reportFields.Cashflow_DebtRepayment, assumptions
                .getDebtRepayment() ? "Yes" : "No");

        // table
        reportFields.setValue(prefix + reportFields.Cashflow_Table,
                getProxyTableModel());
        // graph name
        reportFields.setValue(prefix + reportFields.Cashflow_GraphName,
                "CashFlow");
        // graph
        CashflowTableModel.GrandGroupFooterRow footer = cftm.getGrandTotal();
        if (footer == null) {
            reportFields.setValue(prefix + reportFields.Cashflow_Graph, null);

        } else {
            com.argus.financials.GraphView graph = new com.argus.financials.GraphView();
            graph.setPreferredSize(new java.awt.Dimension(800, 500));
            graph.customizeChart(new double[][] { footer.getGroupTotals() },
                    (String[]) cftm.getColumnNames().toArray(new String[0]),
                    new String[] { footer.toString() },
                    new java.awt.Color[] { java.awt.Color.blue }, true // left
                    );
            graph.setTitleAxisY1("");
            graph
                    .setLabelGeneratorAxisY1(com.argus.beans.format.CurrencyLabelGenerator
                            .getInstance());
            reportFields.setValue(prefix + reportFields.Cashflow_Graph,
                    com.argus.io.ImageUtils.encodeAsJPEG(graph));

        }

    }

}
