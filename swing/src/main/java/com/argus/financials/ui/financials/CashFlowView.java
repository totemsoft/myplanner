/*
 * CashFlowView.java
 *
 * Created on 5 April 2002, 14:28
 */

package com.argus.financials.ui.financials;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.argus.financials.bean.Assumptions;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.ISmartTableModel;
import com.argus.financials.swing.table.ISmartTableRow;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.financials.table.CashflowTableModel;
import com.argus.financials.ui.data.CashFlowData;
import com.argus.format.Currency;
import com.argus.format.CurrencyLabelGenerator;

public class CashFlowView extends com.argus.financials.ui.GraphTableView
        implements javax.swing.event.ChangeListener {
    private static final int CASHFLOW_PAGE_ID = 1;

    private boolean thisCTOR = false;

    private boolean current = true;

    private CashFlowData cashFlowData;

    private Assumptions assumptions;

    public String getViewCaption() {
        return "Cash Flow";
    }

    public CashFlowView(Assumptions assumptions, String prefix) {
        super();

        this.assumptions = assumptions;

        if (ReportFields.PROPOSED_PREFIX.equals(prefix))
            current = false;

        thisCTOR = true;
        assumptions.disableNotify();
        try {
            cashFlowData = new CashFlowData(null, assumptions, prefix);
            cashFlowData.addChangeListener(this); // update table/chart

            assumptionView = new AssumptionView(assumptions);
            assumptionView.changeViewForCashFlow();
            assumptions.addChangeListener(assumptionView); // TODO implement
                                                            // properly

            jTabbedPane = new javax.swing.JTabbedPane();

            jTabbedPane
                    .addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(
                                javax.swing.event.ChangeEvent evt) {
                            jTabbedPaneStateChanged(evt);
                        }
                    });

            jTabbedPane.addTab("Assumptions", assumptionView);
            jTabbedPane.addTab("Cash Flow", jSplitPane);
            jSplitPane.setDividerLocation(200);

            updateComponents();

            add(jTabbedPane);

        } finally {
            thisCTOR = false;
            assumptions.enableNotify();
        }

        setPreferredSize(new java.awt.Dimension(700, 500));

        jButtonSave.setVisible(false);
        jButtonNext.setEnabled(true);
        jButtonDelete.setVisible(false);
        jButtonRefresh.setVisible(true);

    }

    private void init(PersonService person, java.util.Map financials, String prefix)
            throws com.argus.financials.api.ServiceException {

        // cashFlowData = new CashFlowData( null, assumptions, prefix );
        // cashFlowData.addChangeListener( this ); // update table/chart

        cashFlowData.update(person, financials);

    }

    protected void updateComponents() {
        boolean add = assumptions.isReady();

        if (add) {
            if (jTabbedPane.getTabCount() == CASHFLOW_PAGE_ID)
                jTabbedPane.addTab("Cash Flow Results", jSplitPane);
        } else {
            if (jTabbedPane.getTabCount() > CASHFLOW_PAGE_ID)
                jTabbedPane.removeTabAt(CASHFLOW_PAGE_ID);
        }

        jButtonReport.setEnabled(add);
        jButtonNext.setEnabled(add
                && jTabbedPane.getSelectedIndex() < CASHFLOW_PAGE_ID);
        // jButtonSave.setEnabled( RmiParams.getInstance().getClientPerson() !=
        // null );

    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected javax.swing.JTable getJTable() {
        if (jTable == null) {
            jTable = new JTable() {

                private TableCellRenderer groupHeaderRenderer = new DefaultTableCellRenderer() {
                    private java.awt.Font font;

                    public java.awt.Font getFont() {
                        if (font == null) {
                            java.awt.Font f = super.getFont();
                            if (f == null)
                                return null;
                            font = new java.awt.Font(f.getName(),
                                    java.awt.Font.BOLD, f.getSize());
                            this.setHorizontalAlignment(SwingConstants.CENTER);
                        }
                        return font;
                    }
                };

                private TableCellRenderer groupFooterRenderer = new DefaultTableCellRenderer() {
                    private java.awt.Font font;

                    public java.awt.Font getFont() {
                        if (font == null) {
                            java.awt.Font f = super.getFont();
                            if (f == null)
                                return null;
                            font = new java.awt.Font(f.getName(),
                                    java.awt.Font.BOLD, f.getSize());
                            this
                                    .setHorizontalAlignment(SwingConstants.TRAILING);
                        }
                        return font;
                    }
                };

                public TableCellRenderer getCellRenderer(int row, int column) {
                    ISmartTableModel tm = (ISmartTableModel) jTable.getModel();
                    ISmartTableRow r = tm.getRowAt(row);
                    if (r.getRowType() == r.FOOTER1
                            || r.getRowType() == r.FOOTER2
                            || r.getRowType() == r.FOOTER3
                            || r.getRowType() == r.FOOTER4
                            || r.getRowType() == r.FOOTER5) // derived
                        return groupFooterRenderer;
                    if (r.getRowType() == r.HEADER1
                            || r.getRowType() == r.HEADER2
                            || r.getRowType() == r.HEADER3
                            || r.getRowType() == r.HEADER4
                            || r.getRowType() == r.HEADER5) // base
                        return groupHeaderRenderer;
                    return super.getCellRenderer(row, column);
                }

            };

        }

        return jTable;

    }

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);
    }

    // overrite Container method to allow design time UI development
    public java.awt.Component add(java.awt.Component comp) {
        return thisCTOR ? jPanelDetails.add(comp) : super.add(comp);
        // return jPanelDetails.add( comp ); // ??? called from base class ctor
    }

    // Variables declaration - do not modify
    private javax.swing.JTabbedPane jTabbedPane;

    private AssumptionView assumptionView;

    // End of variables declaration

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        if (changeEvent.getSource() != cashFlowData)
            ; // throw ???

        ProxyTableModel proxy = cashFlowData.getProxyTableModel();
        if (getJTable().getModel() != proxy) {
            getJTable().setModel(proxy);
            setColumnWidth(jTable, 0, 1000, 100, 200);
            for (int c = 1; c < proxy.getColumnCount(); c++)
                setColumnWidth(jTable, c, 100, 70, 70);
            // jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
            // );
        }

        // update chart
        CashflowTableModel.GrandGroupFooterRow footer = ((CashflowTableModel) proxy
                .getFullTableModel()).getGrandTotal();
        if (footer == null)
            return;
        double[] grandTotals = footer.getGroupTotals();

        graphView.removeChartLabels();
        graphView.addChartLabels(graphView.customizeChart(
                new double[][] { grandTotals },
                (String[]) ((CashflowTableModel) proxy.getFullTableModel())
                        .getColumnNames().toArray(new String[0]),
                new String[] { footer.toString() },
                new java.awt.Color[] { java.awt.Color.blue }, true // left
                ), Currency.getCurrencyInstance());
        graphView.setTitleAxisY1("");
        graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator.getInstance());

        updateComponents();

        // assumptions.setModified( false );

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void doClose(java.awt.event.ActionEvent evt) {
        // cashFlowData.removeChangeListener( this );
        // cashFlowData.getAssumptions().removeChangeListener( assumptionView );

        super.doClose(evt);
    }

    protected void doNext(java.awt.event.ActionEvent evt) {
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }

    protected void doRefresh(java.awt.event.ActionEvent evt) {
        try {
            cashFlowData.update();
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void saveView(PersonService person) throws com.argus.financials.api.ServiceException {
    }

    public void updateView(PersonService person) throws com.argus.financials.api.ServiceException {
        updateView(person, person.findFinancials(), ReportFields.CURRENT_PREFIX);
    }

    public void updateView(PersonService person, java.util.Map financials,
            String prefix) throws com.argus.financials.api.ServiceException {
        init(person, financials, prefix);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected String getDefaultReport() {
        return current ? com.argus.financials.config.WordSettings.getInstance()
                .getCashFlowReport() : com.argus.financials.config.WordSettings
                .getInstance().getCashFlowProposedReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {
        if (person == null)
            return null;

        // new reporting ( using pre-defined map(field,value) )
        ReportFields reportFields = ReportFields.getInstance();
        cashFlowData.initializeReportData(reportFields, person);

        return reportFields;

    }

    public com.argus.io.ImageEncoder getCashFlowGraph() {
        return graphView;
    }

}