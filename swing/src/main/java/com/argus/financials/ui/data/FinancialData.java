/*
 * FinancialData.java
 *
 * Created on 17 September 2002, 18:26
 */

package com.argus.financials.ui.data;

import com.argus.financials.api.code.FinancialClassID;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.bean.Financial;
import com.argus.financials.bean.IRegularType;
import com.argus.financials.code.FinancialClass;
import com.argus.financials.report.ReportFields;
import com.argus.financials.swing.table.ISmartTableModel;
import com.argus.financials.swing.table.JointTableModel;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.financials.table.FinancialColumnID;
import com.argus.financials.table.FinancialTableModel2;
import com.argus.financials.tree.FinancialTreeFilter;
import com.argus.financials.tree.FinancialTreeFilters;
import com.argus.financials.tree.FinancialTreeModel;
import com.argus.financials.tree.FinancialTreeStructure;

public class FinancialData extends com.argus.financials.bean.AbstractBase
        implements com.argus.financials.report.Reportable,
        javax.swing.event.ChangeListener {

    private static final int[] financialRules = IRegularType.RULES[IRegularType.iFINANCIALS];

    private static final int[] cashflowRules = IRegularType.RULES[IRegularType.iCASHFLOW];

    private static final int[] taxRules = IRegularType.RULES[IRegularType.iTAXANALYSIS];

    private static final int[] wealthRules = IRegularType.RULES[IRegularType.iWEALTH];

    private static final int[] reportRules = IRegularType.RULES[IRegularType.iREPORTS];

    private String prefix;

    public String getReportFieldsPrefix() {
        return prefix;
    }

    private java.util.Map financials;

    // private Assumptions cashFlowAssumptions;
    // private Assumptions wealthAssumptions;
    // private Assumptions taxAssumptions;

    private ProxyTableModel assetLiability;

    private ProxyTableModel assetLiabilityByService;

    private ProxyTableModel earningsEstimate;

    private ProxyTableModel cashFlow;

    private final FinancialTreeModel financialTreeModel = new FinancialTreeModel();

    public synchronized void setFinancials(FinancialTreeModel value) {
        financialTreeModel.setFinancials(value);
        financialTreeModel.treeChanged();
    }

    public synchronized void setFinancials(java.util.Map value) {
        financialTreeModel.setFinancials(value);
        financialTreeModel.treeChanged();
    }

    // Creates new FinancialData
    public FinancialData(java.util.Map financials, String prefix) {

        this.financials = financials;
        this.prefix = prefix == null ? ReportFields.CURRENT_PREFIX : prefix;

        // //////////////////////////////////////////////////////////////////////
        // FINANCIALS //
        // //////////////////////////////////////////////////////////////////////
        FinancialTreeStructure structure = new FinancialTreeStructure();
        structure.add(FinancialTreeStructure.FINANCIAL_CLASS);
        // structure.add( FinancialTreeStructure.FINANCIAL_TYPE );
        // structure.add( FinancialTreeStructure.OWNER_CODE );
        // structure.add( FinancialTreeStructure.COUNTRY_CODE );

        financialTreeModel.setStructure(structure);

        // set common rules
        financialTreeModel.setRules(cashflowRules);

        // set empty filters
        FinancialTreeFilters filters = new FinancialTreeFilters();
        financialTreeModel.setFilters(filters);

        FinancialTreeFilter financialClassFilter = new FinancialTreeFilter(
                FinancialTreeStructure.FINANCIAL_CLASS);
        filters.add(financialClassFilter);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_ASSET_CASH);
        FinancialTreeModel tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 cashTM = new FinancialTableModel2(tree);
        tree.addTreeModelListener(cashTM);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_ASSET_PERSONAL);
        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 personalTM = new FinancialTableModel2(tree);
        tree.addTreeModelListener(personalTM);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_ASSET_SUPERANNUATION);
        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 superTM = new FinancialTableModel2(tree) {

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == FinancialColumnID.INCOME
                        || columnIndex == FinancialColumnID.CAPITAL_GROWTH)
                    return percent.toString(Financial.ZERO);
                return super.getValueAt(rowIndex, columnIndex);
            }

        };
        tree.addTreeModelListener(superTM);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_INCOME_STREAM);
        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 incomeStreamTM = new FinancialTableModel2(tree) {

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == FinancialColumnID.INCOME
                        || columnIndex == FinancialColumnID.CAPITAL_GROWTH)
                    return percent.toString(Financial.ZERO);
                return super.getValueAt(rowIndex, columnIndex);
            }

        };
        tree.addTreeModelListener(incomeStreamTM);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_LIABILITY);
        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 liabilityTM = new FinancialTableModel2(tree);
        tree.addTreeModelListener(liabilityTM);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_ASSET_INVESTMENT);
        FinancialTreeFilter financialTypeFilter = new FinancialTreeFilter(FinancialTreeStructure.FINANCIAL_TYPE);
        filters.add(financialTypeFilter);
        structure.add(FinancialTreeStructure.FINANCIAL_TYPE);
        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 investmentTM = new FinancialTableModel2(tree);
        tree.addTreeModelListener(investmentTM);

        assetLiability = new ProxyTableModel(new JointTableModel(
                new ISmartTableModel[] { cashTM, personalTM, investmentTM,
                        superTM, incomeStreamTM, liabilityTM }, false, true),
                new int[] { FinancialColumnID.NAME,
                        FinancialColumnID.FINANCIAL_SERVICE,
                        FinancialColumnID.OWNER,
                        FinancialColumnID.UNITS_SHARES,
                        FinancialColumnID.UNITS_SHARES_PRICE,
                        FinancialColumnID.AMOUNT, }/*
                                                     * , new int [] {
                                                     * ISmartTableRow.HEADER1,
                                                     * ISmartTableRow.HEADER2,
                                                     * ISmartTableRow.BODY }
                                                     */
        );

        earningsEstimate = new ProxyTableModel(new JointTableModel(
                new ISmartTableModel[] { cashTM, investmentTM, superTM,
                        incomeStreamTM }, false, true), new int[] {
                FinancialColumnID.NAME, FinancialColumnID.OWNER,
                FinancialColumnID.AMOUNT, FinancialColumnID.INCOME,
                FinancialColumnID.CAPITAL_GROWTH,
                FinancialColumnID.TOTAL_RETURN, }/*
                                                     * , new int [] {
                                                     * ISmartTableRow.HEADER1,
                                                     * ISmartTableRow.HEADER2,
                                                     * ISmartTableRow.BODY }
                                                     */
        );

        financialClassFilter = new FinancialTreeFilter(new FinancialClass());
        financialClassFilter.add(FinancialClassID.RC_REGULAR_INCOME);
        financialClassFilter.add(FinancialClassID.RC_REGULAR_EXPENSE);
        financialClassFilter.add(FinancialClassID.RC_TAX_OFFSET);

        filters = new FinancialTreeFilters();
        filters.add(financialClassFilter);

        financialTreeModel.setFilters(filters);

        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 cashFlowTM = new FinancialTableModel2(tree);
        tree.addTreeModelListener(cashFlowTM);

        cashFlow = new ProxyTableModel(cashFlowTM, new int[] {
                FinancialColumnID.NAME, FinancialColumnID.FINANCIAL_TYPE,
                FinancialColumnID.OWNER, FinancialColumnID.AMOUNT, }/*
                                                                     * , new int [] {
                                                                     * ISmartTableRow.HEADER1,
                                                                     * ISmartTableRow.BODY }
                                                                     */
        );

        //
        structure = new FinancialTreeStructure();
        structure.add(FinancialTreeStructure.SERVICE_CODE);
        structure.add(FinancialTreeStructure.FINANCIAL_CLASS);
        // structure.add( FinancialTreeStructure.FINANCIAL_TYPE );
        // structure.add( FinancialTreeStructure.OWNER_CODE );
        // structure.add( FinancialTreeStructure.COUNTRY_CODE );

        financialTreeModel.setStructure(structure);

        // set common rules
        financialTreeModel.setRules(cashflowRules);

        // set empty filters
        filters = financialTreeModel.getFilters();
        filters.clear();

        financialClassFilter = new FinancialTreeFilter(new FinancialTreeFilter(
                FinancialTreeStructure.SERVICE_CODE),
                FinancialTreeStructure.FINANCIAL_CLASS);
        filters.add(financialClassFilter);

        financialClassFilter.clear();
        financialClassFilter.add(FinancialClassID.RC_ASSET_CASH);
        financialClassFilter.add(FinancialClassID.RC_ASSET_SUPERANNUATION);
        financialClassFilter.add(FinancialClassID.RC_INCOME_STREAM);
        financialClassFilter.add(FinancialClassID.RC_ASSET_INVESTMENT);
        tree = financialTreeModel.buildCopy();
        financialTreeModel.addTreeModelListener(tree);
        FinancialTableModel2 tm = new FinancialTableModel2(tree);
        tree.addTreeModelListener(tm);

        assetLiabilityByService = new ProxyTableModel(tm,
                new int[] { FinancialColumnID.NAME, FinancialColumnID.OWNER,
                        FinancialColumnID.UNITS_SHARES,
                        FinancialColumnID.UNITS_SHARES_PRICE,
                        FinancialColumnID.AMOUNT, }/*
                                                     * , new int [] {
                                                     * ISmartTableRow.HEADER1,
                                                     * ISmartTableRow.HEADER2,
                                                     * ISmartTableRow.BODY }
                                                     */
        );

        setFinancials(this.financials);

    }

    public void clear() {
        super.clear();

    }

    /*
     * public Assumptions getCashFlowAssumptions() { return cashFlowAssumptions; }
     * public void setCashFlowAssumptions( Assumptions value ) {
     * cashFlowAssumptions = value; }
     * 
     * public Assumptions getWealthAssumptions() { return wealthAssumptions; }
     * public void setWealthAssumptions( Assumptions value ) { wealthAssumptions =
     * value; }
     * 
     * public Assumptions getTaxAssumptions() { return taxAssumptions; } public
     * void setTaxAssumptions( Assumptions value ) { taxAssumptions = value; }
     */

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        // if ( changeEvent.getSource() != assumptions )
        // ; // throw ???

        // if ( !assumptions.isModified() ) return;

        // updateTreeModel();

        notifyChangeListeners();

    }

    /***************************************************************************
     * com.argus.activex.Reportable interface
     **************************************************************************/
    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields)
            throws Exception {
        initializeReportData(reportFields, clientService);
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields,
            com.argus.financials.service.PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        reportFields.setValue(prefix + ReportFields.FinancialAssetLiability,
                assetLiability);
        reportFields.setValue(prefix
                + ReportFields.FinancialAssetLiabilityByService,
                assetLiabilityByService);
        reportFields.setValue(prefix + ReportFields.FinancialEarningsEstimate,
                earningsEstimate);
        reportFields
                .setValue(prefix + ReportFields.FinancialCashFlow, cashFlow);

        /*
         * if ( cashFlowAssumptions == null ) cashFlowAssumptions = new
         * Assumptions( person ); CashFlowData cashFlowData = new CashFlowData(
         * financials, cashFlowAssumptions, prefix );
         * cashFlowData.initializeReportData( reportFields, null );
         * 
         * 
         * if ( wealthAssumptions == null ) wealthAssumptions = new Assumptions(
         * person ); WealthData wealthData = new WealthData( financials,
         * wealthAssumptions, prefix ); wealthData.initializeReportData(
         * reportFields, null );
         * 
         * 
         * if ( taxAssumptions == null ) taxAssumptions = new Assumptions(
         * person ); TaxAnalysisData taxAnalysisData = new TaxAnalysisData(
         * financials, taxAssumptions, prefix );
         * taxAnalysisData.initializeReportData( reportFields, null );
         */

        AssetAllocationData assetAllocationData = new AssetAllocationData();
        if (reportFields.CURRENT_PREFIX.equals(prefix))
            assetAllocationData.init(person, financials, null);
        else
            assetAllocationData.init(person, null, financials);
        assetAllocationData.initializeReportData(reportFields, null);

    }

}
