/*
 * TaxAnalysisView.java
 *
 * Created on 14 April 2003, 13:00
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
import javax.swing.table.TableColumn;

import com.argus.financials.bean.Assumptions;
import com.argus.financials.report.ReportFields;
import com.argus.financials.report.data.TaxAnalysisData;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.ISmartTableModel;
import com.argus.financials.swing.table.JSmartTable;

public class TaxAnalysisView extends com.argus.financials.ui.BaseView implements
        javax.swing.event.ChangeListener {
    // private static final int TAXANALYSIS_PAGE_ID = 1;

    private boolean thisCTOR = false;

    private boolean current = true;

    private TaxAnalysisData taxAnalysisData;

    private Assumptions assumptions;

    public String getViewCaption() {
        return "Tax Analysis";
    }

    public TaxAnalysisView(Assumptions assumptions, String prefix) {
        super();

        this.assumptions = assumptions;

        if (ReportFields.PROPOSED_PREFIX.equals(prefix))
            current = false;

        jPanelDetails.setLayout(new javax.swing.BoxLayout(jPanelDetails,
                javax.swing.BoxLayout.X_AXIS));

        thisCTOR = true;
        assumptions.disableNotify();
        try {
            jScrollPaneTable = new javax.swing.JScrollPane();
            jTable = new JSmartTable();

            jScrollPaneTableSummary = new javax.swing.JScrollPane();
            jTableSummary = new JSmartTable();
            TableCellRenderer render = jTableSummary.getGroupFooter2Renderer();
            ((DefaultTableCellRenderer) render)
                    .setHorizontalAlignment(SwingConstants.LEADING);

            setLayout(new javax.swing.BoxLayout(this,
                    javax.swing.BoxLayout.Y_AXIS));

            jScrollPaneTable
                    .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneTable
                    .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            // jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            jScrollPaneTable.setViewportView(jTable);

            jScrollPaneTableSummary
                    .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPaneTableSummary
                    .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            // jTableSummary.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            jScrollPaneTableSummary.setViewportView(jTableSummary);

            taxAnalysisData = new TaxAnalysisData(null, assumptions, prefix);
            taxAnalysisData.addChangeListener(this); // update table/chart

            assumptionView = new AssumptionView(assumptions);
            assumptionView.setTaxAnalysisView();
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

            jTabbedPane.addTab("Summary", jScrollPaneTableSummary);

            jTabbedPane.addTab("Details", jScrollPaneTable);

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

        // taxAnalysisData = new TaxAnalysisData( null, assumptions, prefix );
        // taxAnalysisData.addChangeListener( this ); // update table/chart

        taxAnalysisData.update(person, financials);

    }

    protected void updateComponents() {

        boolean add = assumptions.isReady();
        /*
         * if ( add ) { if ( jTabbedPane.getTabCount() == TAXANALYSIS_PAGE_ID )
         * jTabbedPane.addTab("Tax Analysis Results", jScrollPaneTable); } else {
         * if ( jTabbedPane.getTabCount() > TAXANALYSIS_PAGE_ID )
         * jTabbedPane.removeTabAt( TAXANALYSIS_PAGE_ID ); }
         */
        jButtonReport.setEnabled(add);
        jButtonNext.setEnabled(add); // && jTabbedPane.getSelectedIndex() <
                                        // TAXANALYSIS_PAGE_ID );
        // jButtonSave.setEnabled( RmiParams.getInstance().getClientPerson() !=
        // null );

    }

    // overrite Container method to allow design time UI development
    public java.awt.Component add(java.awt.Component comp) {
        return thisCTOR ? jPanelDetails.add(comp) : super.add(comp);
        // return jPanelDetails.add( comp ); // ??? called from base class ctor
    }

    // Variables declaration - do not modify
    private JSmartTable jTable; // details

    private javax.swing.JScrollPane jScrollPaneTable; // details

    private JSmartTable jTableSummary; // summary

    private javax.swing.JScrollPane jScrollPaneTableSummary; // summary

    // End of variables declaration

    protected static void setColumnWidth(JTable table, int columnID,
            int maxWidth, int minWidth, int preferredWidth) {
        TableColumn column = table.getColumnModel().getColumn(columnID);
        if (column == null)
            return;
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);
    }

    // Variables declaration - do not modify
    private javax.swing.JTabbedPane jTabbedPane;

    private AssumptionView assumptionView;

    // End of variables declaration

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        if (changeEvent.getSource() != taxAnalysisData)
            return; // throw ???

        ISmartTableModel tm = taxAnalysisData.getDetailsTableModel();
        jTable.setModel(tm);
        setColumnWidth(jTable, 0, 1000, 300, 300);
        for (int c = 1; c < tm.getColumnCount(); c++)
            setColumnWidth(jTable, c, 200, 100, 100);
        // jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        // );

        tm = taxAnalysisData.getSummaryTableModel();
        jTableSummary.setModel(tm);
        setColumnWidth(jTableSummary, 0, 1000, 300, 300);
        for (int c = 1; c < tm.getColumnCount(); c++)
            setColumnWidth(jTable, c, 200, 100, 100);
        // jTableSummary.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        // );

        updateComponents();

        // assumptions.setModified( false );

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void doClose(java.awt.event.ActionEvent evt) {
        // taxAnalysisData.removeChangeListener( this );
        // taxAnalysisData.getAssumptions().removeChangeListener( assumptionView
        // );

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
            taxAnalysisData.update();
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void saveView(PersonService person) throws java.io.IOException {
    }

    public void updateView(PersonService person) throws java.io.IOException {
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
                .getTaxAnalysisReport()
                : com.argus.financials.config.WordSettings.getInstance()
                        .getTaxAnalysisProposedReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws java.io.IOException {

        if (person == null)
            return null;

        ReportFields reportFields = ReportFields.getInstance();
        taxAnalysisData.initializeReportData(reportFields, person);

        return reportFields;

    }

}