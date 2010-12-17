/*
 * CurrentAssetAllocationView.java
 *
 * Created on 20 September 2002, 11:29
 */

package com.argus.financials.ui.assetallocation;

import java.awt.Cursor;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.argus.financials.assetallocation.AssetAllocationTableModel;
import com.argus.financials.assetallocation.AssetAllocationTableRow;
import com.argus.financials.assetallocation.CurrentAssetAllocationTableModel;
import com.argus.financials.assetallocation.IAssetAllocation;
import com.argus.financials.assetallocation.NewAssetAllocationTableModel;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.code.InvestmentStrategyData;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.GrowthRate;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.ObjectNotFoundException;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.data.AssetAllocationData;
import com.argus.format.Percent;
import com.argus.math.FormatedBigDecimal;

/**
 * Displays the new asset allocation and allows to change it.
 * 
 * @author shibaevv
 */
public class NewAssetAllocationView extends AssetAllocationView implements
        IAssetAllocation {

    private static NewAssetAllocationView view = null;

    // map of Financial objects to be displayed
    // asset allocation pie charts
    private AssetAllocationPieChartView _aapcv_recommended;

    private AssetAllocationPieChartView _aapcv_current;

    private AssetAllocationPieChartView _aapcv_new;

    // total asset allocation JPanels
    protected TotalAssetAllocationView _taav_recommended;

    protected TotalAssetAllocationView _taav_current;

    protected TotalAssetAllocationView _taav_new;

    // for report
    protected AssetAllocationData _data = new AssetAllocationData();

    /** Creates new form InterfaceAssetAllocationView */
    public NewAssetAllocationView() {
        initComponents();
        initComponents2();
    }

    private void initComponents2() {

        createTotalViews();
        initTable(this.jTableAssetAllocation);
        createCharts();

        try {
            displayRiskProfileAssetAllocation(ServiceLocator.getInstance()
                    .getClientPerson());
            updateRecommendedChart();
            displayCurrentAssetAllocation(null);
            updateCurrentChart();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public static NewAssetAllocationView getInstance() {
        if (view == null)
            view = new NewAssetAllocationView();
        return view;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jScrollPaneAssetAllocationTable = new javax.swing.JScrollPane();
        jTableAssetAllocation = new javax.swing.JTable();
        jPanelSummary = new javax.swing.JPanel();
        jPanelCharts = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jPanelButtonsLeft = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(700, 500));
        jTableAssetAllocation.setModel(new NewAssetAllocationTableModel(this,
                new Vector()));
        jScrollPaneAssetAllocationTable.setViewportView(jTableAssetAllocation);

        add(jScrollPaneAssetAllocationTable);

        jPanelSummary.setLayout(new java.awt.GridLayout(1, 2));

        add(jPanelSummary);

        jPanelCharts.setLayout(new javax.swing.BoxLayout(jPanelCharts,
                javax.swing.BoxLayout.X_AXIS));

        add(jPanelCharts);

        jPanelButtons.setLayout(new java.awt.GridLayout(1, 0));

        jButtonReport.setText("Report");
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanelButtonsLeft.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelButtonsLeft.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelButtonsLeft.add(jButtonSave);

        jPanelButtons.add(jPanelButtonsLeft);

        add(jPanelButtons);

    }// GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
    // Add your handling code here:
        doSave(evt);
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed
    // Add your handling code here:
        // check if the data has changed
        if (((NewAssetAllocationTableModel) this.jTableAssetAllocation
                .getModel()).isModified()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                if (JOptionPane.showConfirmDialog(this,
                        "Do you want to save data before closing?",
                        "Close dialog", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                    doSave(evt);
                doClose(evt);
            } finally {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        } else {
            doClose(evt);
        }
    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneAssetAllocationTable;

    private javax.swing.JPanel jPanelCharts;

    private javax.swing.JPanel jPanelButtons;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JTable jTableAssetAllocation;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JPanel jPanelSummary;

    private javax.swing.JPanel jPanelButtonsLeft;

    private javax.swing.JButton jButtonSave;

    // End of variables declaration//GEN-END:variables

    /**
     * Updates the view with the assets of a person. It uses all assets of the
     * following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param person -
     *            a person object
     */
    public void updateView(com.argus.financials.service.PersonService person)
            throws com.argus.financials.service.ServiceException {
        // update pie charts
        displayRiskProfileAssetAllocation(ServiceLocator.getInstance()
                .getClientPerson());
        updateRecommendedChart();

        displayCurrentAssetAllocation(null);
        updateCurrentChart();

        // NewAssetAllocationTableModel caatm = new
        // NewAssetAllocationTableModel( this, new Vector() );
        // caatm.updateModel(person);
        ((NewAssetAllocationTableModel) jTableAssetAllocation.getModel())
                .updateModel(person);
        jTableAssetAllocation.updateUI();
    }

    /**
     * Gets the client's current risk profile rating and recommended asset
     * allocation. The recommended asset alloction is displayed on screen.
     * 
     * @param person -
     *            a client
     */
    private void displayRiskProfileAssetAllocation(
            com.argus.financials.service.PersonService person)
            throws com.argus.financials.service.ServiceException {
        Integer surveyID = null;
        Integer investmentStrategyCodeID = null;
        String investmentStrategyName = "";

        try {
            surveyID = person
                    .getSurveyID(LinkObjectTypeConstant.SURVEY_2_RISKPROFILE);
            // System.out.println( "surveyID: " + surveyID );
        } catch (ObjectNotFoundException e) {
            // ignore that exception, it's raised when we don't have a survey id
            // e.printStackTrace(System.err);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
        }

        com.argus.financials.ui.iso.QuickQuest qq = new com.argus.financials.ui.iso.QuickQuest();

        if (surveyID != null) {
            // qq.setObject( person.getSurvey( surveyID ) );
            // investmentStrategyCodeID = qq.getInvestmentStrategyCodeID();
            investmentStrategyCodeID = person.getSurvey(surveyID)
                    .getSelectedInvestmentStrategy();

            // do we have a id?
            if (investmentStrategyCodeID == null) {
                // no, than try to use QuickQuest to get it
                qq.setObject(person.getSurvey(surveyID));
                investmentStrategyCodeID = qq.getInvestmentStrategyCodeID();
            }
        }

        if (investmentStrategyCodeID != null) {
            InvestmentStrategyData isd = new InvestmentStrategyData();

            investmentStrategyName = isd
                    .getCodeDescription(investmentStrategyCodeID);

            if (investmentStrategyName != null
                    || investmentStrategyName.length() > 0) {
                this._taav_recommended.setHeader(investmentStrategyName);
            } else {
                this._taav_recommended.setHeader("");
            }

            double[] qq_data = null;
            double[][] qq_data2 = null;
            Percent percent = Percent.getPercentInstance();

            GrowthRate gr = isd.getGrowthRate(investmentStrategyCodeID);
            isd.getAllocationData2(investmentStrategyCodeID);

            // data.rr.defensiveAsset = (percent.toString( gr.getDefensiveRate()
            // ) == null ) ? percent.toString( 0.0 ) : percent.toString(
            // gr.getDefensiveRate() );
            // data.rr.growthAsset = (percent.toString( 100 -
            // gr.getDefensiveRate() ) == null ) ? percent.toString( 0.0 ) :
            // percent.toString( 100 - gr.getDefensiveRate() );

            qq_data = isd.getAllocationData(investmentStrategyCodeID);

            String str = (percent.toString(qq_data[0]) == null) ? percent
                    .toString(0.0) : percent.toString(qq_data[0]);
            this._taav_recommended.setTotalInCash(str);

            str = (percent.toString(qq_data[1]) == null) ? percent
                    .toString(0.0) : percent.toString(qq_data[1]);
            this._taav_recommended.setTotalInFixedInterest(str);

            str = (percent.toString(qq_data[2]) == null) ? percent
                    .toString(0.0) : percent.toString(qq_data[2]);
            this._taav_recommended.setTotalInProperty(str);

            str = (percent.toString(qq_data[3]) == null) ? percent
                    .toString(0.0) : percent.toString(qq_data[3]);
            this._taav_recommended.setTotalInAustShares(str);

            str = (percent.toString(qq_data[4]) == null) ? percent
                    .toString(0.0) : percent.toString(qq_data[4]);
            this._taav_recommended.setTotalInIntnlShares(str);

            // "Other" is not used in risk profile at the moment
            this._taav_recommended.setTotalInOther(percent.toString(0.0));

            // calculate total in perecent
            // total = Cash + FixedInterest + Property + Australian Shares +
            // International Shares
            // !!! data array isn't initialized to 0.0, fields can contain
            // 1.7976931348623157E308 = infinte !!!
            double total = 0.0;
            // add only if we have a percent value
            for (int i = 0; i < qq_data.length; i++) {
                if (qq_data[i] >= 0.0 && qq_data[i] <= 100.0) {
                    total += qq_data[i];
                }
            }
            str = (percent.toString(total) == null) ? percent.toString(0.0)
                    : percent.toString(total);
            this._taav_recommended.setTotalTotal(str);

        } else {
            // not all questions are answere, so we haven't any risk profile
            // rating
            this._taav_recommended.setTotalInCash("");
            this._taav_recommended.setTotalInFixedInterest("");
            this._taav_recommended.setTotalInProperty("");
            this._taav_recommended.setTotalInOther("");
            this._taav_recommended.setTotalInAustShares("");
            this._taav_recommended.setTotalInIntnlShares("");
            this._taav_recommended.setTotalTotal("");
        }

    }

    /**
     * Updates the view with the assets out of a StrategyCollection object. It
     * uses all assets of the following types:
     *  - FinancialTypeID.ASSET_CASH - FinancialTypeID.ASSET_INVESTMENT: -
     * FinancialTypeID.ASSET_SUPERANNUATION:
     * 
     * @param financials -
     *            a Map object of all Financial objects
     */
    private java.util.Map oldFinancials;

    private java.util.Map newFinancials;

    public void updateView(java.util.Map oldFinancials,
            java.util.Map newFinancials) throws com.argus.financials.service.ServiceException {

        // update pie charts
        displayRiskProfileAssetAllocation(ServiceLocator.getInstance()
                .getClientPerson());
        updateRecommendedChart();

        this.oldFinancials = oldFinancials;
        displayCurrentAssetAllocation(oldFinancials);
        updateCurrentChart();

        this.newFinancials = newFinancials;
        ((NewAssetAllocationTableModel) jTableAssetAllocation.getModel())
                .updateModel(newFinancials);
        jTableAssetAllocation.updateUI();

    }

    private void doSave(java.awt.event.ActionEvent evt) {
        saveView();
    }

    protected void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    private void saveView() {
        // saveModel() sets only the modified flag, data is not stored!!!
        try {
            ((NewAssetAllocationTableModel) jTableAssetAllocation.getModel())
                    .saveModel(newFinancials);
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }

    public String getTitle() {
        return ((NewAssetAllocationTableModel) this.jTableAssetAllocation
                .getModel()).getModelName();
    }

    public String getDefaultTitle() {
        return "New-" + AssetAllocationTableModel.DEFAULT_MODELN_NAME;
    }

    public void setTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    /**
     * Get the current asset allocation for this client and display it on
     * screen.
     */
    private void displayCurrentAssetAllocation(java.util.Map financials)
            throws com.argus.financials.service.ServiceException {
        CurrentAssetAllocationTableModel caatm = new CurrentAssetAllocationTableModel(
                this, null);

        if (financials == null)
            caatm.updateModel(ServiceLocator.getInstance().getClientPerson());
        else
            caatm.updateModel(financials);

        // calculate the totals
        caatm.updateAssetAllocationView();
        // update current totals on screen
        Percent percent = Percent.getPercentInstance();

        this._taav_current.setTotalInCash(percent.toString(caatm
                .getTotalInCash()));
        this._taav_current.setTotalInFixedInterest(percent.toString(caatm
                .getTotalInFixedInterest()));
        this._taav_current.setTotalInAustShares(percent.toString(caatm
                .getTotalInAustShares()));
        this._taav_current.setTotalInIntnlShares(percent.toString(caatm
                .getTotalInIntnlShares()));
        this._taav_current.setTotalInProperty(percent.toString(caatm
                .getTotalInProperty()));
        this._taav_current.setTotalInOther(percent.toString(caatm
                .getTotalInOther()));
        this._taav_current.setTotalTotal(percent
                .toString(caatm.getTotalTotal()));
    }

    private void doHideCharts() {
        this.jPanelCharts.setVisible(false);
    }

    private void doShowCharts() {
        this.jPanelCharts.setVisible(true);
    }

    /**
     * Create the asset allocation pie chart graphs, one for the recommended,
     * current and new asset allocation. out of JTextfields, which contains the
     * recommended asset allocation.
     */
    private void createCharts() {
        // get recommended asset allocation from textfields
        AssetAllocationTableRow recommended_aatr = new AssetAllocationTableRow();

        recommended_aatr.percent_in_cash = (this._taav_recommended
                .getTotalInCash() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_recommended
                                .getTotalInCash()));
        recommended_aatr.percent_in_fixed_interest = (this._taav_recommended
                .getTotalInFixedInterest() == null) ? new FormatedBigDecimal(
                0.0) : new FormatedBigDecimal(
                deletePercentAndCommaSign(this._taav_recommended
                        .getTotalInFixedInterest()));
        recommended_aatr.percent_in_aust_shares = (this._taav_recommended
                .getTotalInAustShares() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_recommended
                                .getTotalInAustShares()));
        recommended_aatr.percent_in_intnl_shares = (this._taav_recommended
                .getTotalInIntnlShares() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_recommended
                                .getTotalInIntnlShares()));
        recommended_aatr.percent_in_property = (this._taav_recommended
                .getTotalInProperty() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_recommended
                                .getTotalInProperty()));
        recommended_aatr.percent_in_other = (this._taav_recommended
                .getTotalInOther() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_recommended
                                .getTotalInOther()));
        recommended_aatr.total_in_percent = (this._taav_recommended
                .getTotalTotal() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_recommended
                                .getTotalTotal()));

        // get current asset allocation from textfields
        AssetAllocationTableRow current_aatr = new AssetAllocationTableRow();

        current_aatr.percent_in_cash = (this._taav_current.getTotalInCash() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_current
                                .getTotalInCash()));
        current_aatr.percent_in_fixed_interest = (this._taav_current
                .getTotalInFixedInterest() == null) ? new FormatedBigDecimal(
                0.0) : new FormatedBigDecimal(
                deletePercentAndCommaSign(this._taav_current
                        .getTotalInFixedInterest()));
        current_aatr.percent_in_aust_shares = (this._taav_current
                .getTotalInAustShares() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_current
                                .getTotalInAustShares()));
        current_aatr.percent_in_intnl_shares = (this._taav_current
                .getTotalInIntnlShares() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_current
                                .getTotalInIntnlShares()));
        current_aatr.percent_in_property = (this._taav_current
                .getTotalInProperty() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_current
                                .getTotalInProperty()));
        current_aatr.percent_in_other = (this._taav_current.getTotalInOther() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_current
                                .getTotalInOther()));
        current_aatr.total_in_percent = (this._taav_current.getTotalTotal() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_current
                                .getTotalTotal()));

        // get new asset allocation from textfields
        AssetAllocationTableRow new_aatr = new AssetAllocationTableRow();

        new_aatr.percent_in_cash = (this._taav_new.getTotalInCash() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_new
                                .getTotalInCash()));
        new_aatr.percent_in_fixed_interest = (this._taav_new
                .getTotalInFixedInterest() == null) ? new FormatedBigDecimal(
                0.0) : new FormatedBigDecimal(
                deletePercentAndCommaSign(this._taav_new
                        .getTotalInFixedInterest()));
        new_aatr.percent_in_aust_shares = (this._taav_new
                .getTotalInAustShares() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_new
                                .getTotalInAustShares()));
        new_aatr.percent_in_intnl_shares = (this._taav_new
                .getTotalInIntnlShares() == null) ? new FormatedBigDecimal(0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_new
                                .getTotalInIntnlShares()));
        new_aatr.percent_in_property = (this._taav_new.getTotalInProperty() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_new
                                .getTotalInProperty()));
        new_aatr.percent_in_other = (this._taav_new.getTotalInOther() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_new
                                .getTotalInOther()));
        new_aatr.total_in_percent = (this._taav_new.getTotalTotal() == null) ? new FormatedBigDecimal(
                0.0)
                : new FormatedBigDecimal(
                        deletePercentAndCommaSign(this._taav_new
                                .getTotalTotal()));

        _aapcv_recommended = new AssetAllocationPieChartView("Recommended",
                recommended_aatr);
        _aapcv_current = new AssetAllocationPieChartView("Current",
                current_aatr);
        _aapcv_new = new AssetAllocationPieChartView("New", new_aatr);

        this.jPanelCharts.add(_aapcv_recommended);
        this.jPanelCharts.add(_aapcv_current);
        this.jPanelCharts.add(_aapcv_new);
    }

    /**
     * Update the new asset allocation pie chart graph with the values out of
     * JTextFields, which contains the new asset allocation.
     */
    private void updateNewChart() {
        if (_aapcv_new != null) {
            // get new asset allocation from textfields
            AssetAllocationTableRow new_aatr = new AssetAllocationTableRow();

            new_aatr.percent_in_cash = (this._taav_new.getTotalInCash() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_new
                                    .getTotalInCash()));
            new_aatr.percent_in_fixed_interest = (this._taav_new
                    .getTotalInFixedInterest() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_new
                                    .getTotalInFixedInterest()));
            new_aatr.percent_in_aust_shares = (this._taav_new
                    .getTotalInAustShares() == null) ? new FormatedBigDecimal(
                    0.0) : new FormatedBigDecimal(
                    deletePercentAndCommaSign(this._taav_new
                            .getTotalInAustShares()));
            new_aatr.percent_in_intnl_shares = (this._taav_new
                    .getTotalInIntnlShares() == null) ? new FormatedBigDecimal(
                    0.0) : new FormatedBigDecimal(
                    deletePercentAndCommaSign(this._taav_new
                            .getTotalInIntnlShares()));
            new_aatr.percent_in_property = (this._taav_new.getTotalInProperty() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_new
                                    .getTotalInProperty()));
            new_aatr.percent_in_other = (this._taav_new.getTotalInOther() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_new
                                    .getTotalInOther()));
            new_aatr.total_in_percent = (this._taav_new.getTotalTotal() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_new
                                    .getTotalTotal()));

            _aapcv_new.updateView(new_aatr);
        }
    }

    /**
     * Update the current asset allocation pie chart graph with the values out
     * of JTextFields, which contains the current asset allocation.
     */
    private void updateCurrentChart() {
        if (_aapcv_current != null) {
            // get current asset allocation from textfields
            AssetAllocationTableRow current_aatr = new AssetAllocationTableRow();

            current_aatr.percent_in_cash = (this._taav_current.getTotalInCash() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_current
                                    .getTotalInCash()));
            current_aatr.percent_in_fixed_interest = (this._taav_current
                    .getTotalInFixedInterest() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_current
                                    .getTotalInFixedInterest()));
            current_aatr.percent_in_aust_shares = (this._taav_current
                    .getTotalInAustShares() == null) ? new FormatedBigDecimal(
                    0.0) : new FormatedBigDecimal(
                    deletePercentAndCommaSign(this._taav_current
                            .getTotalInAustShares()));
            current_aatr.percent_in_intnl_shares = (this._taav_current
                    .getTotalInIntnlShares() == null) ? new FormatedBigDecimal(
                    0.0) : new FormatedBigDecimal(
                    deletePercentAndCommaSign(this._taav_current
                            .getTotalInIntnlShares()));
            current_aatr.percent_in_property = (this._taav_current
                    .getTotalInProperty() == null) ? new FormatedBigDecimal(0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_current
                                    .getTotalInProperty()));
            current_aatr.percent_in_other = (this._taav_current
                    .getTotalInOther() == null) ? new FormatedBigDecimal(0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_current
                                    .getTotalInOther()));
            current_aatr.total_in_percent = (this._taav_current.getTotalTotal() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_current
                                    .getTotalTotal()));

            _aapcv_current.updateView(current_aatr);
        }
    }

    /**
     * Update the recommanded asset allocation pie chart graph with the values
     * out of JTextFields, which contains the recommended asset allocation.
     */
    private void updateRecommendedChart() {
        if (_aapcv_recommended != null) {
            // get recommended asset allocation from textfields
            AssetAllocationTableRow recommended_aatr = new AssetAllocationTableRow();

            recommended_aatr.percent_in_cash = (this._taav_recommended
                    .getTotalInCash() == null) ? new FormatedBigDecimal(0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_recommended
                                    .getTotalInCash()));
            recommended_aatr.percent_in_fixed_interest = (this._taav_recommended
                    .getTotalInFixedInterest() == null) ? new FormatedBigDecimal(
                    0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_recommended
                                    .getTotalInFixedInterest()));
            recommended_aatr.percent_in_aust_shares = (this._taav_recommended
                    .getTotalInAustShares() == null) ? new FormatedBigDecimal(
                    0.0) : new FormatedBigDecimal(
                    deletePercentAndCommaSign(this._taav_recommended
                            .getTotalInAustShares()));
            recommended_aatr.percent_in_intnl_shares = (this._taav_recommended
                    .getTotalInIntnlShares() == null) ? new FormatedBigDecimal(
                    0.0) : new FormatedBigDecimal(
                    deletePercentAndCommaSign(this._taav_recommended
                            .getTotalInIntnlShares()));
            recommended_aatr.percent_in_property = (this._taav_recommended
                    .getTotalInProperty() == null) ? new FormatedBigDecimal(0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_recommended
                                    .getTotalInProperty()));
            recommended_aatr.percent_in_other = (this._taav_recommended
                    .getTotalInOther() == null) ? new FormatedBigDecimal(0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_recommended
                                    .getTotalInOther()));
            recommended_aatr.total_in_percent = (this._taav_recommended
                    .getTotalTotal() == null) ? new FormatedBigDecimal(0.0)
                    : new FormatedBigDecimal(
                            deletePercentAndCommaSign(this._taav_recommended
                                    .getTotalTotal()));

            _aapcv_recommended.updateView(recommended_aatr);
        }
    }

    /**
     * Create the total asset allocation view, one for the recommended, current
     * and new asset allocation. Each view displays the percentage in cash,
     * fixed interest, australian shares, international shares, property, others
     * and the total sum.
     */
    private void createTotalViews() {
        this._taav_recommended = new TotalAssetAllocationView(
                "Result of Client ISO Profile (%)");
        // this._taav_recommended = new TotalAssetAllocationView( "Recommended
        // Asset Allocation" );
        this._taav_current = new TotalAssetAllocationView(
                "Current Asset Allocation (%)");
        this._taav_new = new TotalAssetAllocationView(
                "New Asset Allocation (%)");

        this.jPanelSummary.add(this._taav_recommended);
        this.jPanelSummary.add(this._taav_current);
        this.jPanelSummary.add(this._taav_new);
    }

    // begin: implement interface InterfaceAssetAllocationView
    public void setTotalInCash(String value) {
        this._taav_new.setTotalInCash(value);
    }

    public void setTotalInFixedInterest(String value) {
        this._taav_new.setTotalInFixedInterest(value);
    }

    public void setTotalInAustShares(String value) {
        this._taav_new.setTotalInAustShares(value);
    }

    public void setTotalInIntnlShares(String value) {
        this._taav_new.setTotalInIntnlShares(value);
    }

    public void setTotalInProperty(String value) {
        this._taav_new.setTotalInProperty(value);
    }

    public void setTotalInOther(String value) {
        this._taav_new.setTotalInOther(value);
    }

    public void setTotalTotal(String value) {
        this._taav_new.setTotalTotal(value);
        updateNewChart();
    }

    // end: implement interface InterfaceAssetAllocationView

    // get methods for recommended asset allocation values
    public String getRiskProfileTotalInCash() {
        return this._taav_recommended.getTotalInCash();
    }

    public String getRiskProfileTotalInFixedInterest() {
        return this._taav_recommended.getTotalInFixedInterest();
    }

    public String getRiskProfileTotalInAustShares() {
        return this._taav_recommended.getTotalInAustShares();
    }

    public String getRiskProfileTotalInIntnlShares() {
        return this._taav_recommended.getTotalInIntnlShares();
    }

    public String getRiskProfileTotalInProperty() {
        return this._taav_recommended.getTotalInProperty();
    }

    public String getRiskProfileTotalInOther() {
        return this._taav_recommended.getTotalInOther();
    }

    public String getRiskProfileTotalTotal() {
        return this._taav_recommended.getTotalTotal();
    }

    // get methods for current asset allocation values
    public String getCurrentTotalInCash() {
        return this._taav_current.getTotalInCash();
    }

    public String getCurrentTotalInFixedInterest() {
        return this._taav_current.getTotalInFixedInterest();
    }

    public String getCurrentTotalInAustShares() {
        return this._taav_current.getTotalInAustShares();
    }

    public String getCurrentTotalInIntnlShares() {
        return this._taav_current.getTotalInIntnlShares();
    }

    public String getCurrentTotalInProperty() {
        return this._taav_current.getTotalInProperty();
    }

    public String getCurrentTotalInOther() {
        return this._taav_current.getTotalInOther();
    }

    public String getCurrentTotalTotal() {
        return this._taav_current.getTotalTotal();
    }

    // get methods for new asset allocation values
    public String getNewTotalInCash() {
        return this._taav_new.getTotalInCash();
    }

    public String getNewTotalInFixedInterest() {
        return this._taav_new.getTotalInFixedInterest();
    }

    public String getNewTotalInAustShares() {
        return this._taav_new.getTotalInAustShares();
    }

    public String getNewTotalInIntnlShares() {
        return this._taav_new.getTotalInIntnlShares();
    }

    public String getNewTotalInProperty() {
        return this._taav_new.getTotalInProperty();
    }

    public String getNewTotalInOther() {
        return this._taav_new.getTotalInOther();
    }

    public String getNewTotalTotal() {
        return this._taav_new.getTotalTotal();
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getAssetAllocationNewReport();
    }

    public ReportFields getReportData(PersonService person) throws Exception {

        ReportFields reportFields = ReportFields.getInstance();
        _data.initializeReportData(reportFields, person);

        return reportFields;
    }

    protected void doReport() {

        try {
            // save the view, because in any other case we get the old asset
            // allocation!?!???
            // reason: we still use a different tables to display the asset
            // allocation and generate a report
            saveView();

            ClientService cp = ServiceLocator.getInstance().getClientPerson();
            _data = new AssetAllocationData();
            _data.init(cp, oldFinancials, newFinancials);

            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this), 
                    getReportData(cp),
                    getDefaultReport());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}
