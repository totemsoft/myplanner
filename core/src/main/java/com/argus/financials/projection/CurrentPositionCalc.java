/*
 * Assetjava
 *
 * Created on 17 September 2001, 11:17
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.beans.format.CurrencyLabelGenerator;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.IReportFields;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.SexCode;
import com.argus.financials.projection.data.LifeExpectancy;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.report.Reportable;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.io.ImageUtils;
import com.argus.util.DateTimeUtils;

public final class CurrentPositionCalc extends AssetGrowthLinked implements Reportable {

    public static final int PRE_RETIREMENT = 0; // asset

    public static final int POST_RETIREMENT = 1; // asets

    public static final int POST_RETIREMENT2 = 2; // income

    public static final int DSS_CALCULATION = 3; // dss

    private boolean retirement;

    private AssetSpend assetSpend;

    /**
     * Creates new SnapshotCalc2
     */
    public CurrentPositionCalc() {
        this(null);
    }

    public CurrentPositionCalc(Integer assetCodeID) {
        super(assetCodeID);
        _clear();
    }

    /**
     * 
     */
    public void assign(CurrentPositionCalc obj) {
        super.assign(obj);

        assetSpend = obj.assetSpend;

        // do not copy data if not the same class
        if (!this.getClass().equals(obj.getClass()))
            return;

        modified = true;
    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        retirement = true;

        if (assetSpend == null)
            assetSpend = new AssetSpend();
        else
            assetSpend.clear();
    }

    public Integer getDefaultModelType() {
        return ModelType.CURRENT_POSITION_CALC;
    }

    public String getDefaultTitle() {
        return "Quick View";
    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void addChangeListener(javax.swing.event.ChangeListener listener) {

        super.addChangeListener(listener);

        assetSpend.addChangeListener(listener);

    }

    /***************************************************************************
     * javax.swing.text.Document
     */
    protected boolean update(Object property, String value) {

        boolean b1 = super.update(property, value);
        boolean b2 = assetSpend.update(property, value);

        if (b1 || b2) {

        } else if (property.equals(GOAL_FIN_INDEPENDENCE)) {
            setRetirement(!Boolean.TRUE.toString().equalsIgnoreCase(value));

        } else if (property.equals(GOAL_RETIREMENT)) {
            setRetirement(Boolean.TRUE.toString().equalsIgnoreCase(value));

        } else
            return false;

        return true;

    }

    /***************************************************************************
     * 
     */
    public boolean isModifiedSpend() {
        return assetSpend.isModified();
    }

    public boolean isReadySpend() {
        return assetSpend.isReady();
    }

    public boolean isModifiedReadySpend() {
        return assetSpend.isModified() && assetSpend.isReady();
    }

    /***************************************************************************
     * 
     */
    public void enableUpdate() {
        super.enableUpdate();
        assetSpend.enableUpdate();
    }

    public void disableUpdate() {
        super.disableUpdate();
        assetSpend.disableUpdate();
    }

    public void doUpdate() {
        super.doUpdate();
        assetSpend.doUpdate();
    }

    public void setModel(Model value) {
        super.setModel(value);
        assetSpend.setModel(value);
    }

    /***************************************************************************
     * Mark as saved
     */
    public boolean getSaved() {
        return super.getSaved() || assetSpend.getSaved();
    }

    public void setSaved() {
        super.setSaved();
        assetSpend.setSaved();
    }

    /**
     * 
     */
    public AssetSpend getAssetSpend() {
        return assetSpend;
    }

    public void setIndexed(boolean value) {
        super.setIndexed(value);
        assetSpend.setIndexed(value);
    }

    public void setIndexRate(double value) {
        super.setIndexRate(value);
        assetSpend.setIndexRate(value);
    }

    public boolean getIncludeDSS() {
        return assetSpend.getIncludeDSS();
    }

    public void setIncludeDSS(boolean value) {
        assetSpend.setIncludeDSS(value);
    }

    public boolean getSingle() {
        return assetSpend.getSingle();
    }

    public void setSingle(boolean value) {
        assetSpend.setSingle(value);
    }

    public boolean getRetirement() {
        return retirement;
    }

    public void setRetirement(boolean value) {
        if (retirement == value)
            return;

        retirement = value;
        setModified();
    }

    public boolean getHomeOwner() {
        return assetSpend.getHomeOwner();
    }

    public void setHomeOwner(boolean value) {
        assetSpend.setHomeOwner(value);
    }

    public Integer getSexCodeID() {
        return assetSpend.getSexCodeID();
    }

    public void setSexCodeID(Integer value) {
        super.setSexCodeID(value);
        assetSpend.setSexCodeID(value);
    }

    public String getSexCode() {
        return new SexCode().getCodeDescription(getSexCodeID());
    }

    public Integer getPartnerSexCodeID() {
        return assetSpend.getPartnerSexCodeID();
    }

    public void setPartnerSexCodeID(Integer value) {
        assetSpend.setPartnerSexCodeID(value);
    }

    public String getPartnerSexCode() {
        return new SexCode().getCodeDescription(getPartnerSexCodeID());
    }

    public double getInitialValueSpend() {
        return assetSpend.getInitialValue();
    }

    public void setInitialValueSpend(double initialValue) {
        assetSpend.setInitialValue(initialValue);
    }

    public double getResidualValue() {
        return assetSpend.getResidualValue();
    }

    public void setResidualValue(double value) {
        assetSpend.setResidualValue(value);
    }

    public double getPartSpendValue() {
        return assetSpend.getPartSpendValue();
    }

    public void setPartSpendValue(double value) {
        assetSpend.setPartSpendValue(value);
    }

    public double getSpendValue() {
        return assetSpend.getSpendValue();
    }

    public void setSpendValue(double value) {
        assetSpend.setSpendValue(value);
    }

    public Integer getInvestmentStrategyCodeIDSpend() {
        return assetSpend.getInvestmentStrategyCodeID();
    }

    public void setInvestmentStrategyCodeIDSpend(Integer value) {
        assetSpend.setInvestmentStrategyCodeID(value);
    }

    public String getInvestmentStrategyCodeSpendDesc() {
        return new InvestmentStrategyCode().getCodeDescription(assetSpend
                .getInvestmentStrategyCodeID());
    }

    public double getIncomeRateSpend() {
        return getInvestmentStrategyCodeIDSpend() == null ? 0.
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeIDSpend()).getIncomeRate();
    }

    public double getGrowthRateSpend() {
        return getInvestmentStrategyCodeIDSpend() == null ? 0.
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeIDSpend()).getGrowthRate();
    }

    public double getTotalReturnRateSpend() {
        return getInvestmentStrategyCodeIDSpend() == null ? 0.
                : InvestmentStrategyCode.getGrowthRate(
                        getInvestmentStrategyCodeIDSpend()).getRate();
    }

    public void setRevisionFeeRate(double value) {
        super.setRevisionFeeRate(value);
        assetSpend.setRevisionFeeRate(value);
    }

    public void setDateOfBirth(java.util.Date value) {
        super.setDateOfBirth(value);
        assetSpend.setDateOfBirth(value);
    }

    public java.util.Date getPartnerDateOfBirth() {
        return assetSpend.getPartnerDateOfBirth();
    }

    public void setPartnerDateOfBirth(java.util.Date value) {
        assetSpend.setPartnerDateOfBirth(value);
    }

    public int getClientAge() {
        return assetSpend.getAge();
    }

    public int getPartnerAge() {
        return assetSpend.getPartnerAge();
    }

    public int getClientTargetAge() {
        return assetSpend.getTargetAge();
    }

    public void setClientTargetAge(int value) {
        super.setTargetAge(value);
        assetSpend.setTargetAge(value);
    }

    public java.util.Date getClientTargetDate() {
        return assetSpend.getTargetDate();
    }

    public int getPartnerTargetAge() {
        return assetSpend.getPartnerTargetAge();
    }

    public void setPartnerTargetAge(int value) {
        // super.setPartnerTargetAge(value);
        assetSpend.setPartnerTargetAge(value);
    }

    public java.util.Date getPartnerTargetDate() {
        return assetSpend.getPartnerTargetDate();
    }

    public double getPartnerTaxRate() {
        return assetSpend.getPartnerTaxRate();
    }

    public void setPartnerTaxRate(double value) {
        assetSpend.setPartnerTaxRate(value);
    }

    public int getYearsIntSpend() {
        int years = assetSpend.getYearsInt();
        return years >= 0 ? years : 0;
    }

    public void setYearsSpend(double yearsValue) {
        assetSpend.setYears(yearsValue);
    }

    public double getPensionQualifyingAge() {
        return assetSpend.getPensionQualifyingAge();
    }

    public double getLifeExpectancy() {
        return assetSpend.getLifeExpectancy();
    }

    public double getLifeExpectancyAfterRetire() {
        int targetAge = getClientTargetAge();
        return targetAge < 0 ? 0 : // UNKNOWN_VALUE :
                LifeExpectancy.getValue(targetAge, getSexCodeID());
        // getLifeExpectancy() - ( (double) targetAge - getClientAge() );
    }

    public double getUndeductedPurchasePriceATO() {
        return assetSpend.getUndeductedPurchasePriceATO();
    }

    public void setUndeductedPurchasePriceATO(double value) {
        assetSpend.setUndeductedPurchasePriceATO(value);
    }

    public double getUndeductedPurchasePriceDSS() {
        return assetSpend.getUndeductedPurchasePriceDSS();
    }

    public void setUndeductedPurchasePriceDSS(double value) {
        assetSpend.setUndeductedPurchasePriceDSS(value);
    }

    public double getRebateRate() {
        return assetSpend.getRebateRate();
    }

    public void setRebateRate(double value) {
        assetSpend.setRebateRate(value);
    }

    /**
     * 
     */
    public double[] getTotalTargetValues() {
        double[] values = super.getTotalTargetValues();
        setInitialValueSpend(values[values.length - 1]);
        return arrayCopy(values, getYearsIntSpend());
    }

    public double getTargetSpendValue() {
        return assetSpend.getTargetValue();
    }

    public double[] getTargetSpendValues() {
        return arrayCopyOffset(assetSpend.getTargetValues(), getYearsInt());
    }

    public double[] getRequiredIncomeValues() {
        return arrayCopyOffset(assetSpend.getRequiredIncomeValues(),
                getYearsInt());
    }

    public double getActualIncome() {
        return assetSpend.getActualIncome(true);
    }

    public double[] getDSSTargetValues() {
        return arrayCopyOffset(assetSpend.getDSSTargetValues(), getYearsInt());
    }

    /**
     * HELPER METHODS
     */
    private String[] labels;

    private String[] legends1 = new String[] { "<html>Assets <i>(pre-retirement)</i></html>", };

    private String[] legends3 = new String[] { legends1[0],
            "<html>Assets <i>(post-retirement)</i></html>",
            "<html>Income</html>", };

    private String[] legends4 = new String[] { legends1[0], legends3[1],
            legends3[2], "<html>DSS Entitlement</html>", };

    public String[] getLabels() {
        // int offset = getAge();
        int offset = getClientAge();
        int years = getYearsInt() + getYearsIntSpend();

        // if ( labels != null && labels.length == years )
        // return labels;

        labels = new String[years];
        for (int i = 0; i < years; i++)
            labels[i] = String.valueOf(i + offset);

        return labels;
    }

    public String[] getLegends(int seriesCount) {
        switch (seriesCount) {
        case 1:
            return legends1;
        case 3:
            return legends3;
        case 4:
            return legends4;
        default:
            return null;
        }
    }

    private java.awt.Color[] colors = new java.awt.Color[] {
            java.awt.Color.green, java.awt.Color.red, java.awt.Color.blue,
            java.awt.Color.pink };

    public java.awt.Color[] getColors() {
        return colors;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Integer getRequiredFrequencyCodeID() {
        return FrequencyCode.YEARLY;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        java.util.Collection financials = super
                .getGeneratedFinancialItems(desc);

        java.util.Collection c = getGeneratedIncomes(desc);
        if (c != null)
            financials.addAll(c);

        c = getGeneratedExpenses(desc);
        if (c != null)
            financials.addAll(c);

        c = getGeneratedLiabilities(desc);
        if (c != null)
            financials.addAll(c);

        return financials;

    }

    private java.util.Collection getGeneratedIncomes(String desc) {
        return null;
    }

    private java.util.Collection getGeneratedExpenses(String desc) {
        return null;
    }

    private java.util.Collection getGeneratedLiabilities(String desc) {
        return null;
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    public double[][] getChartData() {

        if (getRetirement()) {
            if (!isReadySpend())
                return null;
        } else {
            if (!isReady())
                return null;
        }

        double[][] dataAggregator = new double[1][];

        double[] preRetirement = null;
        double[] postRetirement = null;
        double[] postRetirement2 = null;
        double[] dssCalculation = null;

        // before retirement
        preRetirement = getTotalTargetValues();

        if (getRetirement()) {
            // if ( isReady() && isModifiedReadySpend() ) {
            if (isReadySpend()) {

                // update totals before retirement ( increase array length HOLE
                // )
                // preRetirement = getTotalTargetValues();

                // assets post-retirement
                postRetirement = getTargetSpendValues();

                // update required income (after retirement)
                postRetirement2 = getRequiredIncomeValues();

                // 
                if (getIncludeDSS())
                    dssCalculation = getDSSTargetValues();

            }

        } else if (preRetirement == null) {
            preRetirement = dataAggregator[PRE_RETIREMENT];
        }

        // nothing changed
        if (preRetirement == null && postRetirement == null
                && postRetirement2 == null && dssCalculation == null)
            return null;

        if (dssCalculation != null) {
            if (dataAggregator.length < 4) {
                // save this ref
                if (preRetirement == null)
                    preRetirement = dataAggregator[PRE_RETIREMENT];

                dataAggregator = new double[4][];
            }

            dataAggregator[DSS_CALCULATION] = dssCalculation;
        }

        // and finally update whole chart
        if (postRetirement != null && postRetirement2 != null) {
            if (dataAggregator.length < 3 || dssCalculation == null) {
                // save this ref (can be saved already)
                if (preRetirement == null)
                    preRetirement = dataAggregator[PRE_RETIREMENT];

                dataAggregator = new double[3][];
            }

            dataAggregator[POST_RETIREMENT] = postRetirement;
            dataAggregator[POST_RETIREMENT2] = postRetirement2;
        }

        if (preRetirement != null)
            dataAggregator[PRE_RETIREMENT] = preRetirement;

        return dataAggregator;

    }

    /***************************************************************************
     * com.argus.activex.Reportable interface *
     **************************************************************************/
    public void initializeReportData(ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields, ServiceLocator.getInstance()
                .getClientPerson());
    }

    public void initializeReportData(ReportFields reportFields, PersonService person)
            throws java.io.IOException {

        if (person != null)
            reportFields.initialize(person);

        Currency curr = getCurrencyInstance();
        Percent percent = getPercentInstance();

        // General
        reportFields.setValue(IReportFields.CurrenPosition_Name,
                getModel() == null ? "QUICKView" : getModel().toString());
        reportFields.setValue(IReportFields.CurrenPosition_GoalRetirement,
                getRetirement() ? "Yes" : "No");

        boolean single = getSingle();
        reportFields.setValue(IReportFields.CurrenPosition_MaritalCode,
                single ? "Single" : "Joint");
        reportFields.setValue(IReportFields.CurrenPosition_YearsToProject, ""
                + getYearsIntSpend());
        reportFields.setValue(IReportFields.CurrenPosition_LifeExpectancy, ""
                + getLifeExpectancyAfterRetire());

        reportFields.setValue(IReportFields.CurrenPosition_ClientSex, getSexCode());
        reportFields.setValue(IReportFields.CurrenPosition_ClientDOB, DateTimeUtils
                .formatAsMEDIUM(getDateOfBirth()));
        reportFields.setValue(IReportFields.CurrenPosition_ClientAge, "" + getAge());
        reportFields.setValue(IReportFields.CurrenPosition_ClientTargetAge, ""
                + getTargetAge());
        reportFields.setValue(IReportFields.CurrenPosition_ClientTargetDate,
                DateTimeUtils.formatAsMEDIUM(getTargetDate()));
        reportFields.setValue(IReportFields.CurrenPosition_ClientTaxRate, percent
                .toString(getTaxRate()));

        reportFields.setValue(IReportFields.CurrenPosition_PartnerSex,
                getPartnerSexCode());
        reportFields.setValue(IReportFields.CurrenPosition_PartnerDOB, single ? null
                : DateTimeUtils.formatAsMEDIUM(getPartnerDateOfBirth()));
        reportFields.setValue(IReportFields.CurrenPosition_PartnerAge, single ? null
                : "" + getPartnerAge());
        reportFields.setValue(IReportFields.CurrenPosition_PartnerTargetAge,
                single ? null : "" + getPartnerTargetAge());
        reportFields.setValue(IReportFields.CurrenPosition_PartnerTargetDate,
                single ? null : DateTimeUtils
                        .formatAsMEDIUM(getPartnerTargetDate()));
        reportFields.setValue(IReportFields.CurrenPosition_PartnerTaxRate,
                single ? null : percent.toString(getPartnerTaxRate()));

        // FeesAndTaxes
        reportFields.setValue(IReportFields.CurrenPosition_Indexation, percent
                .toString(getIndexRate()));
        // reportFields.setValue(IReportFields.CurrenPosition_Inflation,
        // percent.toString( getIndexRate() ) );
        reportFields.setValue(IReportFields.CurrenPosition_EntryFees, percent
                .toString(getEntryFeeRate()));
        reportFields.setValue(IReportFields.CurrenPosition_ReviewFees, percent
                .toString(getRevisionFeeRate()));
        reportFields.setValue(IReportFields.CurrenPosition_RequiredIncome, curr
                .toString(getSpendValue()));
        reportFields.setValue(IReportFields.CurrenPosition_ResidualRequired, curr
                .toString(getResidualValue()));
        reportFields.setValue(IReportFields.CurrenPosition_ATOUPP, curr
                .toString(getUndeductedPurchasePriceATO()));
        reportFields.setValue(IReportFields.CurrenPosition_DSSUPP, curr
                .toString(getUndeductedPurchasePriceDSS()));
        reportFields.setValue(IReportFields.CurrenPosition_PensionRebate, percent
                .toString(getRebateRate()));

        // ??? TableModel ???
        AssetGrowthLinked ag = getAsset(ASSET_CASH);
        reportFields.setValue(IReportFields.CurrenPosition_ContributionCash,
                ag == null ? null : curr.toString(ag.getAddValue()));
        double amount = ag == null ? 0. : ag.getTargetValue();
        double totalAmount = amount;
        reportFields.setValue(IReportFields.CurrenPosition_ProjectedCash, curr
                .toString(amount));

        ag = getAsset(ASSET_SUPERANNUATION);
        reportFields.setValue(IReportFields.CurrenPosition_ContributionSuper,
                ag == null ? null : curr.toString(ag.getAddValue()));
        amount = ag == null ? 0. : ag.getTargetValue();
        totalAmount += amount;
        reportFields.setValue(IReportFields.CurrenPosition_ProjectedSuper, curr
                .toString(amount));

        ag = getAsset(ASSET_INVESTMENT);
        reportFields.setValue(IReportFields.CurrenPosition_ContributionSavings,
                ag == null ? null : curr.toString(ag.getAddValue()));
        amount = ag == null ? 0. : ag.getTargetValue();
        totalAmount += amount;
        reportFields.setValue(IReportFields.CurrenPosition_ProjectedSavings, curr
                .toString(amount));

        reportFields.setValue(IReportFields.CurrenPosition_ProjectedTotal, curr
                .toString(totalAmount));

        // Result
        StrategyResultView graphView = new StrategyResultView();
        graphView.setPreferredSize(new java.awt.Dimension(800, 500));

        double[][] dataAggregator = getChartData();
        if (dataAggregator != null) {
            graphView.customizeChart(dataAggregator, getLabels(),
                    getLegends(dataAggregator.length), getColors(), true);
            graphView.setTitleAxisY1("");
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                    .getInstance());
        }
        reportFields.setValue(IReportFields.CurrenPosition_Graph,
                ImageUtils.encodeAsJPEG(graphView));
        reportFields.setValue(IReportFields.CurrenPosition_GraphName,
                "QUICKView Graph");
        reportFields.setValue(IReportFields.CurrenPosition_Comment, CurrentPositionComment
                .getComment(this));
        reportFields.setValue(IReportFields.CurrenPosition_IncomePayable, curr
                .toString(getActualIncome()));

        // BeforeRetirement
        reportFields.setValue(IReportFields.CurrenPosition_BeforeRetirement_Title,
                getInvestmentStrategyCodeDesc());
        reportFields.setValue(IReportFields.CurrenPosition_BeforeRetirement_IncomeRate,
                getIncomeRate());
        reportFields.setValue(IReportFields.CurrenPosition_BeforeRetirement_GrowthRate,
                getGrowthRate());
        reportFields.setValue(IReportFields.CurrenPosition_BeforeRetirement_TotalReturnRate,
                getTotalReturnRate());
        AssetAllocationView view = new AssetAllocationView(
                getInvestmentStrategyCodeID());
        view.setPreferredSize(new java.awt.Dimension(800, 500));
        reportFields.setValue(IReportFields.CurrenPosition_BeforeRetirement_Graph,
                ImageUtils.encodeAsJPEG(view));
        reportFields.setValue(IReportFields.CurrenPosition_BeforeRetirement_GraphName,
                "Investment Strategy Before Retirement");

        // AfterRetirement
        if (getRetirement()) {
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_Title,
                    getInvestmentStrategyCodeSpendDesc());
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_IncomeRate,
                    getIncomeRateSpend());
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_GrowthRate,
                    getGrowthRateSpend());
            reportFields.setValue(
                    IReportFields.CurrenPosition_AfterRetirement_TotalReturnRate,
                    getTotalReturnRateSpend());
            view = new AssetAllocationView(getInvestmentStrategyCodeIDSpend());
            view.setPreferredSize(new java.awt.Dimension(800, 500));
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_Graph, ImageUtils.encodeAsJPEG(view));
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_GraphName, "Investment Strategy After Retirement");
        } else {
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_Title, null);
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_IncomeRate, null);
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_GrowthRate, null);
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_TotalReturnRate, null);
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_Graph, null);
            reportFields.setValue(IReportFields.CurrenPosition_AfterRetirement_GraphName, null);
        }

    }

}
