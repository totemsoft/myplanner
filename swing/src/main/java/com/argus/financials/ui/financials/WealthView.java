/*
 * CashFlowView.java
 *
 * Created on 5 April 2002, 14:28
 */

package com.argus.financials.ui.financials;

/**
 * 
 * @author valeri chibaev
 */

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.argus.financials.api.code.FinancialClassID;
import com.argus.financials.bean.Assumptions;
import com.argus.financials.chart.GraphView;
import com.argus.financials.chart.IGraphView;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.ISmartTableModel;
import com.argus.financials.swing.table.ISmartTableRow;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.financials.ui.data.WealthData;

public class WealthView extends com.argus.financials.ui.GraphTableView
        implements javax.swing.event.ChangeListener, FinancialClassID {

    private static final int RETIREMENT_PAGE_ID = 2;

    private static WealthView view;

    private boolean thisCTOR = false;

    private boolean current = true;

    private WealthData wealthData;

    private Assumptions assumptions;

    public String getViewCaption() {
        return "Wealth Analysis";
    }

    public WealthView(Assumptions assumptions, String prefix) {
        super();

        this.assumptions = assumptions;

        if (ReportFields.PROPOSED_PREFIX.equals(prefix))
            current = false;

        thisCTOR = true;
        assumptions.disableNotify();
        try {
            wealthData = new WealthData(null, assumptions, prefix);
            wealthData.addChangeListener(this); // update table/chart

            assumptionView = new AssumptionView(assumptions);
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

            jSplitPane.setLeftComponent(getGraphView());

            jSplitPaneRetirement = new javax.swing.JSplitPane();
            jPanelGraphRetirement = wealthData.getGraphViewRetirement();
            jScrollPaneTableRetirement = new javax.swing.JScrollPane();
            jTableRetirement = getJTableRetirement();

            jTabbedPane.addTab("Assumptions", assumptionView);

            jTabbedPane.addTab("Wealth", jSplitPane);
            jSplitPane.setDividerLocation(200);

            jSplitPaneRetirement.setDividerLocation(350);
            jSplitPaneRetirement
                    .setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            jSplitPaneRetirement.setLeftComponent(jPanelGraphRetirement);

            jScrollPaneTableRetirement
                    .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneTableRetirement
                    .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jTableRetirement
                    .setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            jScrollPaneTableRetirement.setViewportView(jTableRetirement);

            jSplitPaneRetirement.setRightComponent(jScrollPaneTableRetirement);

            // jTabbedPane.addTab("Retirement", jSplitPaneRetirement);
            updateComponents();

            add(jTabbedPane);

        } finally {
            thisCTOR = false;
            assumptions.enableNotify();
        }

        setPreferredSize(new java.awt.Dimension(600, 500));

        jButtonSave.setVisible(false);
        jButtonNext.setEnabled(true);
        jButtonDelete.setVisible(false);
        jButtonRefresh.setVisible(true);

        getGraphView().setChartControlsVisible(false);

    }

    private void init(PersonService person, java.util.Map financials, String prefix)
            throws com.argus.financials.api.ServiceException {

        // wealthData = new WealthData( null, assumptions, prefix );
        // wealthData.addChangeListener( this ); // update table/chart

        wealthData.update(person, financials);

    }

    protected void updateComponents() {

        boolean add = assumptions.isReady();
        if (add) {
            if (jTabbedPane.getTabCount() == RETIREMENT_PAGE_ID)
                jTabbedPane.addTab("Retirement", jSplitPaneRetirement);
        } else {
            if (jTabbedPane.getTabCount() > RETIREMENT_PAGE_ID)
                jTabbedPane.removeTabAt(RETIREMENT_PAGE_ID);
        }

        // jButtonReport.setEnabled( add );
        jButtonNext.setEnabled(add
                && jTabbedPane.getSelectedIndex() < RETIREMENT_PAGE_ID);
        // jButtonSave.setEnabled( RmiParams.getInstance().getClientPerson() !=
        // null );

        // refresh graphs
        IGraphView g1 = getGraphView();
        IGraphView g2 = getGraphViewRetirement();
        double max = Math.max(g1.getMaxAxisY1(), g2.getMaxAxisY1());
        max = Math.max(g1.getMaxAxisY2(), max);

        g1.setMaxAxisY1(max);
        g1.setMaxAxisY2(max);
        g2.setMaxAxisY1(max);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        // if ( changeEvent.getSource() != wealthData ) return;

        // //////////////////////////////////////////////////////////////////////
        // WEALTH
        // //////////////////////////////////////////////////////////////////////
        // WealthTableModel wtm = wealthData.getWealthTableModel();
        ProxyTableModel wproxy = wealthData.getWealthProxyTableModel();

        jTable.setModel(wproxy);
        setColumnWidth(jTable, 0, 1000, 100, 200);
        for (int c = 1; c < wproxy.getColumnCount(); c++)
            setColumnWidth(jTable, c, 100, 70, 70);

        // //////////////////////////////////////////////////////////////////////
        // RETIREMENT
        // //////////////////////////////////////////////////////////////////////
        // RetirementTableModel rtm = wealthData.getRetirementTableModel();
        ProxyTableModel rproxy = wealthData.getRetirementProxyTableModel();

        jTableRetirement.setModel(rproxy);
        setColumnWidth(jTableRetirement, 0, 1000, 100, 200);
        for (int c = 1; c < rproxy.getColumnCount(); c++)
            setColumnWidth(jTableRetirement, c, 100, 70, 70);

        updateComponents();

    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected GraphView getGraphView() {
        return wealthData == null ? null : wealthData.getGraphView();
    }

    protected IGraphView getGraphViewRetirement() {
        return wealthData == null ? null : wealthData.getGraphViewRetirement();
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

    protected javax.swing.JTable getJTableRetirement() {
        if (jTableRetirement == null) {
            jTableRetirement = new JTable();
        }

        return jTableRetirement;

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

    private javax.swing.JSplitPane jSplitPaneRetirement;

    private javax.swing.JPanel jPanelGraphRetirement;

    private javax.swing.JTable jTableRetirement;

    private javax.swing.JScrollPane jScrollPaneTableRetirement;

    // End of variables declaration

    /***************************************************************************
     * 
     **************************************************************************/
    public void doClose(java.awt.event.ActionEvent evt) {
        // wealthData.removeChangeListener( this );
        // wealthData.getAssumptions().removeChangeListener( assumptionView );

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
            wealthData.update();
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void saveView(PersonService person) throws Exception {
    }

    public void updateView(PersonService person) throws Exception {
        updateView(person, person.findFinancials(), ReportFields.CURRENT_PREFIX);
    }

    public void updateView(PersonService person, java.util.Map financials,
            String prefix) throws com.argus.financials.api.ServiceException {
        init(person, financials, prefix);
    }

    /***************************************************************************
     * pageSetup
     **************************************************************************/
    protected String getDefaultReport() {
        return current ? com.argus.financials.config.WordSettings.getInstance()
                .getWealthReport() : com.argus.financials.config.WordSettings
                .getInstance().getWealthProposedReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {

        if (person == null)
            return null;

        ReportFields reportFields = ReportFields.getInstance();
        wealthData.initializeReportData(reportFields, person);

        return reportFields;

    }

}
