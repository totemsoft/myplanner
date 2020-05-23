/*
 * AssetAllocationData.java
 *
 * Created on 24 September 2002, 11:34
 */

package au.com.totemsoft.myplanner.swing.data;

import au.com.totemsoft.math.FormatedBigDecimal;
import au.com.totemsoft.math.Money;
import au.com.totemsoft.math.Numeric;
import au.com.totemsoft.math.Percent;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.assetallocation.AssetAllocationTableRow;
import au.com.totemsoft.myplanner.assetallocation.IAssetAllocation2;
import au.com.totemsoft.myplanner.code.InvestmentStrategyData;
import au.com.totemsoft.myplanner.etc.Survey;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.swing.assetallocation.AssetAllocationPieChartView;
import au.com.totemsoft.myplanner.swing.assetallocation.CurrentAssetAllocationTableModel;
import au.com.totemsoft.myplanner.swing.assetallocation.NewAssetAllocationTableModel;
import au.com.totemsoft.myplanner.swing.iso.QuickQuest;
import au.com.totemsoft.myplanner.swing.strategy.DataCollectionView;
import au.com.totemsoft.myplanner.table.model.FinancialTableModel;
import au.com.totemsoft.myplanner.table.swing.ISmartTableModel;
import au.com.totemsoft.myplanner.table.swing.ProxyTableModel;
import au.com.totemsoft.util.ReferenceCode;

public class AssetAllocationData extends au.com.totemsoft.myplanner.bean.AbstractBase
        implements au.com.totemsoft.myplanner.report.Reportable,
        javax.swing.event.ChangeListener {
    private static final String STRING_EMPTY = "";

    // columns
    private static final int NAME = 0;

    private static final int RECOMMENDED_PERCENT = 1;

    private static final int RECOMMENDED_DOLLAR = 2;

    private static final int CURRENT_PERCENT = 3;

    private static final int CURRENT_DOLLAR = 4;

    private static final int NEW_PERCENT = 5;

    private static final int NEW_DOLLAR = 6;

    private static final int COLUMNS_COUNT = 7;

    // rows
    private static final int DOMESTIC_HEADER = 1;

    private static final int CASH = 2;

    private static final int FIXED_INTEREST = 3;

    private static final int SHARES = 4;

    private static final int PROPERTY = 5;

    private static final int OTHER = 6;

    private static final int DOMESTIC_FOOTER = 7;

    private static final int INTL_HEADER = 8;

    private static final int INTL_SHARES = 9;

    private static final int INTL_FOOTER = 10;

    private static final int TOTAL_FOOTER = 11;

    private static final int ROWS_COUNT = 12;

    private static final java.util.Vector columnNames = new java.util.Vector();

    private static final java.util.Vector columnClasses = new java.util.Vector();
    static {
        columnNames.add("Name");
        columnNames.add("Recommended (%)");
        columnNames.add("Recommended ($)");
        columnNames.add("Current (%)");
        columnNames.add("Current ($)");
        columnNames.add("Proposed (%)");
        columnNames.add("Proposed ($)");

        columnClasses.add(java.lang.Object.class);
        columnClasses.add(java.lang.Object.class);
        columnClasses.add(java.lang.Object.class);
        columnClasses.add(java.lang.Object.class);
        columnClasses.add(java.lang.Object.class);
        columnClasses.add(java.lang.Object.class);
        columnClasses.add(java.lang.Object.class);

    }

    // column filters
    static final int[] RECOMMENDED = new int[] { NAME, RECOMMENDED_PERCENT,
            RECOMMENDED_DOLLAR };

    static final int[] CURRENT = new int[] { NAME, CURRENT_PERCENT,
            CURRENT_DOLLAR };

    static final int[] PROPOSED = new int[] { NAME, NEW_PERCENT, NEW_DOLLAR };

    static final int[] RECOMMENDED_CURRENT = new int[] { NAME,
            RECOMMENDED_PERCENT, CURRENT_PERCENT, CURRENT_DOLLAR };

    static final int[] RECOMMENDED_PROPOSED = new int[] { NAME,
            RECOMMENDED_PERCENT, NEW_PERCENT, NEW_DOLLAR };

    // static final int [] RECOMMENDED_PROPOSED = new int [] { NAME,
    // RECOMMENDED_PERCENT, NEW_PERCENT, NEW_DOLLAR, CURRENT_PERCENT,
    // CURRENT_DOLLAR };

    // full data set
    private// public
    ISmartTableModel base; // 7 columns

    // view on base
    public ProxyTableModel Recommended;

    public ProxyTableModel Current;

    public ProxyTableModel Proposed;

    public ProxyTableModel RecommendedCurrent;

    public ProxyTableModel RecommendedProposed;

    public String RecommendedGraph;

    public String RecommendedGraphTitle;

    public String CurrentGraph;

    public String CurrentGraphTitle;

    public String ProposedGraph;

    public String ProposedGraphTitle;

    /** Creates new StrategyData */
    public AssetAllocationData() {
        super();
    }

    public void clear() {
        super.clear();

        base = null;
        Recommended = null;
        Current = null;
        Proposed = null;
        RecommendedCurrent = null;
        RecommendedProposed = null;

        RecommendedGraph = STRING_EMPTY;
        CurrentGraph = STRING_EMPTY;
        ProposedGraph = STRING_EMPTY;

        RecommendedGraphTitle = STRING_EMPTY;
        CurrentGraphTitle = STRING_EMPTY;
        ProposedGraphTitle = STRING_EMPTY;
    }

    /**
     * Initialize the Asset Allocation Data, the methode is used by the
     * PlanWriterData!
     * 
     * To get all asset allocation data for the current strategy, we use the
     * CurrentAssetAllocationView and NewAssetAllocationView.
     * 
     * @param person -
     *            a PersonService object
     * @param financials -
     *            a Map object of Financial objects
     * @param get_current_aa -
     *            depending on the value it gets the current asset allocation or
     *            not
     */
    public void init(PersonService person, java.util.Map financials,
            java.util.Map newFinancials) throws Exception {
        base = new AssetAllocationTableModel(person, financials, newFinancials);
        Recommended = new ProxyTableModel(base, RECOMMENDED);
        Current = new ProxyTableModel(base, CURRENT);
        Proposed = new ProxyTableModel(base, PROPOSED);
        RecommendedCurrent = new ProxyTableModel(base, RECOMMENDED_CURRENT);
        RecommendedProposed = new ProxyTableModel(base, RECOMMENDED_PROPOSED);
    }

    /**
     * Initialize the Asset Allocation Data, the methode is used by the
     * PlanWriterData!
     * 
     * To get all asset allocation data for the current strategy, we use the
     * CurrentAssetAllocationView.
     * 
     * @param person -
     *            a PersonService object
     */
    public void init(PersonService person) throws Exception {
        init(person, null, null);
    }

    public void init(PersonService person, java.util.Map financials)
            throws Exception {
        init(person, financials, null);
    }

    public void init(PersonService person,
            au.com.totemsoft.myplanner.strategy.model.DataCollectionModel dcm)
            throws Exception {
        base = new AssetAllocationTableModel(person, dcm);
        Recommended = new ProxyTableModel(base, RECOMMENDED);
        Current = new ProxyTableModel(base, CURRENT);
        Proposed = new ProxyTableModel(base, PROPOSED);
        RecommendedCurrent = new ProxyTableModel(base, RECOMMENDED_CURRENT);
        RecommendedProposed = new ProxyTableModel(base, RECOMMENDED_PROPOSED);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public class AssetAllocationTableModel extends FinancialTableModel {

        private GroupHeaderRow domesticHeader;

        private AssetAllocationRow cash;

        private AssetAllocationRow fixedInterest;

        private AssetAllocationRow shares;

        private AssetAllocationRow property;

        private AssetAllocationRow other;

        private GroupFooterRow domesticFooter;

        private GroupHeaderRow intlHeader;

        private AssetAllocationRow intlShares;

        private GroupFooterRow intlFooter;

        private GroupFooterRow totalFooter;

        public AssetAllocationTableModel(PersonService person,
                au.com.totemsoft.myplanner.strategy.model.DataCollectionModel dcm)
                throws Exception {
            generalInit();

            // init recommended data (from ISO Risk Profile)
            initRecommendedData(person);
            // init current data (from financials)
            initCurrentData(dcm);
            // init new data (from newFinancials)
            // initNewData( null );

        }

        /** Creates a new instance of AssetAllocationTableModel */
        public AssetAllocationTableModel(PersonService person,
                java.util.Map financials, java.util.Map newFinancials)
                throws Exception {
            super();

            generalInit();

            // init recommended data (from ISO Risk Profile)
            initRecommendedData(person);
            // init current data (from financials)
            initCurrentData(financials);
            // init new data (from newFinancials)
            initNewData(newFinancials);
        }

        private void generalInit() {
            setColumnNames(columnNames);
            setColumnClasses(columnClasses);

            // init tablemodel structure
            java.util.Vector data = new java.util.Vector();
            setData(data);

            domesticHeader = new GroupHeaderRow(new ReferenceCode(
                    DOMESTIC_HEADER, "Domestic"));
            data.add(domesticHeader);

            cash = new AssetAllocationRow("Cash");
            data.add(cash);

            fixedInterest = new AssetAllocationRow("Fixed Interest");
            data.add(fixedInterest);

            shares = new AssetAllocationRow("Australian Shares");
            data.add(shares);

            property = new AssetAllocationRow("Property");
            data.add(property);

            other = new AssetAllocationRow("Other");
            data.add(other);

            domesticFooter = new GroupFooterRow(new ReferenceCode(
                    DOMESTIC_FOOTER, "Total"), domesticHeader);
            data.add(domesticFooter);
            // System.out.println( ">>>>>>>>>>> domesticFooter.getRowType()=" +
            // domesticFooter.getRowType() ); // = 2

            intlHeader = new GroupHeaderRow(new ReferenceCode(INTL_HEADER,
                    "International"));
            data.add(intlHeader);

            intlShares = new AssetAllocationRow("International Shares");
            data.add(intlShares);

            intlFooter = new GroupFooterRow(new ReferenceCode(INTL_FOOTER,
                    "Total"), intlHeader);
            data.add(intlFooter);

            totalFooter = new GroupFooterRow(new ReferenceCode(TOTAL_FOOTER,
                    "Total"), null);
            data.add(totalFooter);
        }

        protected Numeric getNumeric(int columnIndex) {

            switch (columnIndex) {
            case (RECOMMENDED_PERCENT):
            case (CURRENT_PERCENT):
            case (NEW_PERCENT):
                return percent;
            case (RECOMMENDED_DOLLAR):
            case (CURRENT_DOLLAR):
            case (NEW_DOLLAR):
                return money;
            default:
                return null;
            }

        }

        public class AssetAllocationRow extends AbstractSmartTableRow {

            private java.util.Vector rowData; // String,
                                                // Numeric(Percent/Money)

            /** Creates a new instance of AssetAllocationRow */
            public AssetAllocationRow(String name) {
                super(BODY);

                rowData = new java.util.Vector(COLUMNS_COUNT);
                rowData.add(NAME, name);
                for (int c = 1; c < COLUMNS_COUNT; c++)
                    rowData.add(null);
            }

            public String toString() {
                return (String) rowData.elementAt(NAME);
            }

            protected java.util.Vector getRowData() {
                return rowData;
            }

            public Object getValueAt(int columnIndex) {
                if (columnIndex == NAME)
                    return toString();
                return getNumeric(columnIndex).setValue(
                        (Numeric) rowData.elementAt(columnIndex));
            }

            public void setValueAt(Object obj, int columnIndex) {
                rowData.setElementAt(obj, columnIndex);
            }

        }

        public abstract class AssetAllocationPercent implements
                IAssetAllocation2 {

            public void setTotalInAustShares(String str) {
            }

            public void setTotalInCash(String str) {
            }

            public void setTotalInFixedInterest(String str) {
            }

            public void setTotalInIntnlShares(String str) {
            }

            public void setTotalInOther(String str) {
            }

            public void setTotalInProperty(String str) {
            }

            public void setTotalTotal(String str) {
            }

        }

        public class CurrentAssetAllocationPercent extends
                AssetAllocationPercent {

            public void setTotalInAustShares(double value) {
                shares.setValueAt(new Money(value), CURRENT_PERCENT);
            }

            public void setTotalInCash(double value) {
                cash.setValueAt(new Money(value), CURRENT_PERCENT);
            }

            public void setTotalInFixedInterest(double value) {
                fixedInterest.setValueAt(new Money(value), CURRENT_PERCENT);
            }

            public void setTotalInIntnlShares(double value) {
                intlShares.setValueAt(new Money(value), CURRENT_PERCENT);
            }

            public void setTotalInOther(double value) {
                other.setValueAt(new Money(value), CURRENT_PERCENT);
            }

            public void setTotalInProperty(double value) {
                property.setValueAt(new Money(value), CURRENT_PERCENT);
            }

            public void setTotalTotal(double value) {
            }

        }

        public class NewAssetAllocationPercent extends AssetAllocationPercent {

            public void setTotalInAustShares(double value) {
                shares.setValueAt(new Money(value), NEW_PERCENT);
            }

            public void setTotalInCash(double value) {
                cash.setValueAt(new Money(value), NEW_PERCENT);
            }

            public void setTotalInFixedInterest(double value) {
                fixedInterest.setValueAt(new Money(value), NEW_PERCENT);
            }

            public void setTotalInIntnlShares(double value) {
                intlShares.setValueAt(new Money(value), NEW_PERCENT);
            }

            public void setTotalInOther(double value) {
                other.setValueAt(new Money(value), NEW_PERCENT);
            }

            public void setTotalInProperty(double value) {
                property.setValueAt(new Money(value), NEW_PERCENT);
            }

            public void setTotalTotal(double value) {
            }

        }

        /**
         * Initialize data for recommended asset allocation (ISO/Risk-Profile).
         * The methods gets the recommended asset allocation, which is the
         * result of the ISO/Risk-Profile screen.
         * 
         * @param person,
         *            a person
         */
        private void initRecommendedData(PersonService person)
                throws Exception {

            Integer surveyID = null;
            try {
                surveyID = person == null ? null
                        : person
                                .getSurveyID(LinkObjectTypeConstant.SURVEY_2_RISKPROFILE);
                // System.out.println( "surveyID: " + surveyID );
            } catch (ObjectNotFoundException e) {
                // ignore that exception, it's raised when we don't have a
                // survey id
            }

            Integer investmentStrategyCodeID = null;
            if (surveyID != null) {
                Survey survey = person.getSurvey(surveyID);
                investmentStrategyCodeID = survey == null ? null : survey.getSelectedInvestmentStrategy();

                // do we have a id?
                if (investmentStrategyCodeID == null) {
                    // no, than try to use QuickQuest to get it
                    QuickQuest qq = new QuickQuest();
                    qq.setObject(person.getSurvey(surveyID));
                    investmentStrategyCodeID = qq.getInvestmentStrategyCodeID();
                }

            }

            double[] data = investmentStrategyCodeID == null ? new double[5]
                    : new InvestmentStrategyData()
                            .getAllocationData(investmentStrategyCodeID);

            cash.setValueAt(new Percent(data[0]), RECOMMENDED_PERCENT);
            cash.setValueAt(new Money(0.), RECOMMENDED_DOLLAR);

            fixedInterest.setValueAt(new Percent(data[1]), RECOMMENDED_PERCENT);
            fixedInterest.setValueAt(new Money(0.), RECOMMENDED_DOLLAR);

            property.setValueAt(new Percent(data[2]), RECOMMENDED_PERCENT);
            property.setValueAt(new Money(0.), RECOMMENDED_DOLLAR);

            shares.setValueAt(new Percent(data[3]), RECOMMENDED_PERCENT);
            shares.setValueAt(new Money(0.), RECOMMENDED_DOLLAR);

            // "Other" is not used in risk profile at the moment
            other.setValueAt(new Percent(0.), RECOMMENDED_PERCENT);
            other.setValueAt(new Money(0.), RECOMMENDED_DOLLAR);

            intlShares.setValueAt(new Percent(data[4]), RECOMMENDED_PERCENT);
            intlShares.setValueAt(new Money(0.), RECOMMENDED_DOLLAR);

            // get total amount in percent to create graph
            AssetAllocationTableRow row = new AssetAllocationTableRow();
            row.percent_in_cash = new FormatedBigDecimal(data[0]);
            row.percent_in_fixed_interest = new FormatedBigDecimal(data[1]);
            row.percent_in_aust_shares = new FormatedBigDecimal(data[3]);
            row.percent_in_intnl_shares = new FormatedBigDecimal(data[4]);
            row.percent_in_property = new FormatedBigDecimal(data[2]);
            row.percent_in_other = new FormatedBigDecimal(0.);
            row.total_in_percent = new FormatedBigDecimal(data[0] + data[1]
                    + data[2] + data[3] + data[4]);

            // create graph
            RecommendedGraphTitle = "Recommended";

            AssetAllocationPieChartView aapcv = new AssetAllocationPieChartView(
                    RecommendedGraphTitle, row);
            aapcv.setPreferredSize(new java.awt.Dimension(250, 250));

            RecommendedGraph = au.com.totemsoft.io.ImageUtils.encodeAsJPEG(aapcv);

        }

        /**
         * Initialize data for the current asset allocation (depending on
         * current financials).
         * 
         * @param financials,
         *            a map of finacials
         */
        private void initCurrentData(java.util.Map financials)
                throws Exception {

            CurrentAssetAllocationTableModel tm = new CurrentAssetAllocationTableModel(
                    new CurrentAssetAllocationPercent(), new java.util.Vector());
            tm.updateModel(financials);

            java.util.Vector rows = tm.getData();

            // get "Total in $" row
            AssetAllocationTableRow row = null;
            if (rows.size() > 0) {
                row = (AssetAllocationTableRow) rows.elementAt(rows.size() - 1);
                cash.setValueAt(new Money(row.percent_in_cash), CURRENT_DOLLAR);
                fixedInterest.setValueAt(new Money(
                        row.percent_in_fixed_interest), CURRENT_DOLLAR);
                property.setValueAt(new Money(row.percent_in_property),
                        CURRENT_DOLLAR);
                shares.setValueAt(new Money(row.percent_in_aust_shares),
                        CURRENT_DOLLAR);
                other.setValueAt(new Money(row.percent_in_other),
                        CURRENT_DOLLAR);
                intlShares.setValueAt(new Money(row.percent_in_intnl_shares),
                        CURRENT_DOLLAR);

            } else {
                System.err
                        .println("AssetAllocationData::initCurrentData() rows.size()=0");
            }

            // get total amount in percent
            cash.setValueAt(new Percent(tm.getTotalInCash()), CURRENT_PERCENT);
            fixedInterest.setValueAt(new Percent(tm.getTotalInFixedInterest()),
                    CURRENT_PERCENT);
            property.setValueAt(new Percent(tm.getTotalInProperty()),
                    CURRENT_PERCENT);
            shares.setValueAt(new Percent(tm.getTotalInAustShares()),
                    CURRENT_PERCENT);
            other
                    .setValueAt(new Percent(tm.getTotalInOther()),
                            CURRENT_PERCENT);
            intlShares.setValueAt(new Percent(tm.getTotalInIntnlShares()),
                    CURRENT_PERCENT);

            // get total amount in percent to create graph
            row = new AssetAllocationTableRow();
            row.percent_in_cash = new FormatedBigDecimal(tm.getTotalInCash());
            row.percent_in_fixed_interest = new FormatedBigDecimal(tm
                    .getTotalInFixedInterest());
            row.percent_in_property = new FormatedBigDecimal(tm
                    .getTotalInProperty());
            row.percent_in_aust_shares = new FormatedBigDecimal(tm
                    .getTotalInAustShares());
            row.percent_in_other = new FormatedBigDecimal(tm.getTotalInOther());
            row.percent_in_intnl_shares = new FormatedBigDecimal(tm
                    .getTotalInIntnlShares());
            row.total_in_percent = new FormatedBigDecimal(tm.getTotalTotal());

            // create graph
            CurrentGraphTitle = "Current";

            AssetAllocationPieChartView aapcv = new AssetAllocationPieChartView(
                    CurrentGraphTitle, row);
            aapcv.setPreferredSize(new java.awt.Dimension(250, 250));

            CurrentGraph = au.com.totemsoft.io.ImageUtils.encodeAsJPEG(aapcv);

        }

        /**
         * Initialize data for the current asset allocation (depending on
         * current financials).
         * 
         * Attention: This methode is required because of the StrategyCreator,
         * the current finacials are stored in a DataCollectionModel not as a
         * java.util.Map!!!
         * 
         * @param dcm,
         *            a DataCollectionModel
         * 
         * @see StrategyCreator
         * @see DataCollectionView.java
         */
        private void initCurrentData(
                au.com.totemsoft.myplanner.strategy.model.DataCollectionModel dcm)
                throws Exception {

            CurrentAssetAllocationTableModel tm = new CurrentAssetAllocationTableModel(
                    new CurrentAssetAllocationPercent(), new java.util.Vector());
            tm.updateModel(dcm);

            java.util.Vector rows = tm.getData();

            // get "Total in $" row
            AssetAllocationTableRow row = null;
            if (rows.size() > 0) {
                row = (AssetAllocationTableRow) rows.elementAt(rows.size() - 1);
                cash.setValueAt(new Money(row.percent_in_cash), CURRENT_DOLLAR);
                fixedInterest.setValueAt(new Money(
                        row.percent_in_fixed_interest), CURRENT_DOLLAR);
                property.setValueAt(new Money(row.percent_in_property),
                        CURRENT_DOLLAR);
                shares.setValueAt(new Money(row.percent_in_aust_shares),
                        CURRENT_DOLLAR);
                other.setValueAt(new Money(row.percent_in_other),
                        CURRENT_DOLLAR);
                intlShares.setValueAt(new Money(row.percent_in_intnl_shares),
                        CURRENT_DOLLAR);

            } else {
                System.err
                        .println("AssetAllocationData::initCurrentData() rows.size()=0");
            }

            // get total in percent
            cash.setValueAt(new Percent(tm.getTotalInCash()), CURRENT_PERCENT);
            fixedInterest.setValueAt(new Percent(tm.getTotalInFixedInterest()),
                    CURRENT_PERCENT);
            property.setValueAt(new Percent(tm.getTotalInProperty()),
                    CURRENT_PERCENT);
            shares.setValueAt(new Percent(tm.getTotalInAustShares()),
                    CURRENT_PERCENT);
            other
                    .setValueAt(new Percent(tm.getTotalInOther()),
                            CURRENT_PERCENT);
            intlShares.setValueAt(new Percent(tm.getTotalInIntnlShares()),
                    CURRENT_PERCENT);

            // get total amount in percent to create graph
            row = new AssetAllocationTableRow();
            row.percent_in_cash = new FormatedBigDecimal(tm.getTotalInCash());
            row.percent_in_fixed_interest = new FormatedBigDecimal(tm
                    .getTotalInFixedInterest());
            row.percent_in_property = new FormatedBigDecimal(tm
                    .getTotalInProperty());
            row.percent_in_aust_shares = new FormatedBigDecimal(tm
                    .getTotalInAustShares());
            row.percent_in_other = new FormatedBigDecimal(tm.getTotalInOther());
            row.percent_in_intnl_shares = new FormatedBigDecimal(tm
                    .getTotalInIntnlShares());
            row.total_in_percent = new FormatedBigDecimal(tm.getTotalTotal());

            // create graph
            CurrentGraphTitle = "Current";

            AssetAllocationPieChartView aapcv = new AssetAllocationPieChartView(
                    CurrentGraphTitle, row);
            aapcv.setPreferredSize(new java.awt.Dimension(250, 250));

            CurrentGraph = au.com.totemsoft.io.ImageUtils.encodeAsJPEG(aapcv);

        }

        /**
         * Initialize data for the new (proposed) asset allocation (depending on
         * "new" financials).
         * 
         * @param financials,
         *            a map of "new" finacials
         */
        private void initNewData(java.util.Map newFinancials)
                throws Exception {

            NewAssetAllocationTableModel tm = new NewAssetAllocationTableModel(
                    new NewAssetAllocationPercent(), new java.util.Vector());
            tm.updateModel(newFinancials);

            java.util.Vector rows = tm.getData();

            // get "Total in $" row
            AssetAllocationTableRow row = null;
            if (rows.size() > 0) {
                row = (AssetAllocationTableRow) rows.elementAt(rows.size() - 1);
                cash.setValueAt(new Money(row.percent_in_cash), NEW_DOLLAR);
                fixedInterest.setValueAt(new Money(
                        row.percent_in_fixed_interest), NEW_DOLLAR);
                property.setValueAt(new Money(row.percent_in_property),
                        NEW_DOLLAR);
                shares.setValueAt(new Money(row.percent_in_aust_shares),
                        NEW_DOLLAR);
                other.setValueAt(new Money(row.percent_in_other), NEW_DOLLAR);
                intlShares.setValueAt(new Money(row.percent_in_intnl_shares),
                        NEW_DOLLAR);

            } else {
                System.err
                        .println("AssetAllocationData::initNewData() rows.size()=0");
            }

            // get total amounts in percent
            cash.setValueAt(new Percent(tm.getTotalInCash()), NEW_PERCENT);
            fixedInterest.setValueAt(new Percent(tm.getTotalInFixedInterest()),
                    NEW_PERCENT);
            property.setValueAt(new Percent(tm.getTotalInProperty()),
                    NEW_PERCENT);
            shares.setValueAt(new Percent(tm.getTotalInAustShares()),
                    NEW_PERCENT);
            other.setValueAt(new Percent(tm.getTotalInOther()), NEW_PERCENT);
            intlShares.setValueAt(new Percent(tm.getTotalInIntnlShares()),
                    NEW_PERCENT);

            // get total amounts in percent to create graph
            row = new AssetAllocationTableRow();
            row.percent_in_cash = new FormatedBigDecimal(tm.getTotalInCash());
            row.percent_in_fixed_interest = new FormatedBigDecimal(tm
                    .getTotalInFixedInterest());
            row.percent_in_property = new FormatedBigDecimal(tm
                    .getTotalInProperty());
            row.percent_in_aust_shares = new FormatedBigDecimal(tm
                    .getTotalInAustShares());
            row.percent_in_other = new FormatedBigDecimal(tm.getTotalInOther());
            row.percent_in_intnl_shares = new FormatedBigDecimal(tm
                    .getTotalInIntnlShares());
            row.total_in_percent = new FormatedBigDecimal(tm.getTotalTotal());

            // create graph
            ProposedGraphTitle = "Proposed";

            AssetAllocationPieChartView aapcv = new AssetAllocationPieChartView(
                    ProposedGraphTitle, row);
            aapcv.setPreferredSize(new java.awt.Dimension(250, 250));

            ProposedGraph = au.com.totemsoft.io.ImageUtils.encodeAsJPEG(aapcv);

        }

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

        reportFields.setValue(reportFields.AA_RecommendedCurrentTable,
                RecommendedCurrent);
        reportFields.setValue(reportFields.AA_RecommendedProposedTable,
                RecommendedProposed);
        reportFields.setValue(reportFields.AA_RecommendedGraph,
                RecommendedGraph);
        reportFields.setValue(reportFields.AA_CurrentGraph, CurrentGraph);
        reportFields.setValue(reportFields.AA_ProposedGraph, ProposedGraph);

    }
}
