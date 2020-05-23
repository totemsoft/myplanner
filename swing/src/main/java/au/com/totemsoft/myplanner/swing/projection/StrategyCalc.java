/*
 * StrategyGroup.java
 *
 * Created on 17 December 2002, 17:33
 */

package au.com.totemsoft.myplanner.swing.projection;

import au.com.totemsoft.math.Money;
import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.myplanner.bean.Assumptions;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.code.IReportFields;
import au.com.totemsoft.myplanner.code.ModelType;
import au.com.totemsoft.myplanner.projection.MoneyCalc;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.report.Reportable;
import au.com.totemsoft.myplanner.report.data.TaxAnalysisData;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.strategy.Recommendation;
import au.com.totemsoft.myplanner.strategy.StrategyFinancial;
import au.com.totemsoft.myplanner.strategy.model.DataCollectionModel;
import au.com.totemsoft.myplanner.strategy.model.DataRestructureModel;
import au.com.totemsoft.myplanner.strategy.model.StrategyGroupData;
import au.com.totemsoft.myplanner.swing.data.AssetAllocationData;
import au.com.totemsoft.myplanner.swing.data.CashFlowData;
import au.com.totemsoft.myplanner.swing.data.FinancialData;
import au.com.totemsoft.myplanner.swing.data.WealthData;
import au.com.totemsoft.myplanner.table.model.FinancialColumnID;
import au.com.totemsoft.myplanner.table.swing.ISmartTableRow;
import au.com.totemsoft.myplanner.table.swing.ProxyTableModel;
import au.com.totemsoft.myplanner.table.swing.SmartTableModel;

public class StrategyCalc extends MoneyCalc implements Reportable {

    private static final Money _money;
    static {
        _money = new Money();
        _money.setMaximumFractionDigits(0);
        _money.setMinimumFractionDigits(0);
    }

    /** Creates a new instance of StrategyGroup */
    public StrategyCalc() {
    }

    public Integer getDefaultModelType() {
        return ModelType.STRATEGY_CALC;
    }

    /***************************************************************************
     * au.com.totemsoft.activex.Reportable interface
     **************************************************************************/
    public void initializeReportData(ReportFields reportFields)
            throws Exception {
        initializeReportData(reportFields, 
                clientService);
    }

    public void initializeReportData(ReportFields reportFields, PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        StrategyGroupData sgd = (StrategyGroupData) reportFields
                .getValue(IReportFields.Strategy_Group_Data);
        if (sgd == null) {
            sgd = new StrategyGroupData();
            sgd.setCollectionModel(new DataCollectionModel());
            sgd.setRestructureModel(new DataRestructureModel());
        }

        if (sgd.getCashFlowAssumptions() == null)
            sgd.setCashFlowAssumptions(new Assumptions(person));

        if (sgd.getWealthAssumptions() == null)
            sgd.setWealthAssumptions(new Assumptions(person));

        Assumptions cashFlowAssumptions = sgd.getCashFlowAssumptions();
        Assumptions wealthAssumptions = sgd.getWealthAssumptions();
        Assumptions taxAssumptions = new Assumptions(
                person == null ? clientService
                        : person);

        // //////////////////////////////////////////////////////////////////////
        // COLLECTED DATA //
        // //////////////////////////////////////////////////////////////////////
        DataCollectionModel cm = sgd.getCollectionModel();
        java.util.Map financials = cm.getFinancials();
        FinancialData cfd = new FinancialData(financials,
                IReportFields.CURRENT_PREFIX);
        // cfd.setCashFlowAssumptions( cashFlowAssumptions );
        // cfd.setWealthAssumptions( wealthAssumptions );
        // cfd.setTaxAssumptions( taxAssumptions );
        cfd.initializeReportData(reportFields, person);

        new CashFlowData(financials, cashFlowAssumptions,
                IReportFields.CURRENT_PREFIX).initializeReportData(
                reportFields, null);
        new WealthData(financials, wealthAssumptions,
                IReportFields.CURRENT_PREFIX).initializeReportData(
                reportFields, null);
        new TaxAnalysisData(financials, taxAssumptions,
                IReportFields.CURRENT_PREFIX).initializeReportData(
                reportFields, null);

        // //////////////////////////////////////////////////////////////////////
        // RESTRUCTURED DATA //
        // //////////////////////////////////////////////////////////////////////
        DataRestructureModel rm = sgd.getRestructureModel();
        financials = rm.getFinancials();
        FinancialData rfd = new FinancialData(rm.getFinancials(),
                IReportFields.PROPOSED_PREFIX);
        // rfd.setCashFlowAssumptions( cashFlowAssumptions );
        // rfd.setWealthAssumptions( wealthAssumptions );
        // rfd.setTaxAssumptions( taxAssumptions );
        rfd.initializeReportData(reportFields, person);

        new CashFlowData(financials, cashFlowAssumptions,
                IReportFields.PROPOSED_PREFIX).initializeReportData(
                reportFields, null);
        new WealthData(financials, wealthAssumptions,
                IReportFields.PROPOSED_PREFIX).initializeReportData(
                reportFields, null);
        new TaxAnalysisData(financials, taxAssumptions,
                IReportFields.PROPOSED_PREFIX).initializeReportData(
                reportFields, null);

        // //////////////////////////////////////////////////////////////////////
        // ASSET ALLOCATION //
        // //////////////////////////////////////////////////////////////////////
        AssetAllocationData aad = new AssetAllocationData();
        aad.init(clientService, cm
                .getFinancials(), rm.getFinancials());
        aad.initializeReportData(reportFields, null);

        // //////////////////////////////////////////////////////////////////////
        // SUMMARY/RECOMMENDATION //
        // //////////////////////////////////////////////////////////////////////
        reportFields
                .setValue(
                        reportFields.Strategy_Summary,
                        new SummaryTableModel(
                                (ProxyTableModel) reportFields
                                        .getValue(IReportFields.CURRENT_PREFIX
                                                + IReportFields.FinancialAssetLiability),
                                (ProxyTableModel) reportFields
                                        .getValue(IReportFields.PROPOSED_PREFIX
                                                + IReportFields.FinancialAssetLiability)));

        reportFields.setValue(reportFields.Strategy_Recommendations,
                new RecommendationsTableModel(rm.getRecommendations()));

    }

    // //////////////////////////////////////////////////////////////////////////
    // SummaryTableModel //
    // //////////////////////////////////////////////////////////////////////////
    public static final int NAME = 0;

    public static final int CURRENT = 1;

    public static final int PROPOSED = 2;

    private static java.util.Vector summaryColumnNames;

    private static java.util.Vector summaryColumnClasses;
    static {
        summaryColumnNames = new java.util.Vector();
        summaryColumnNames.add(NAME, "Assets");
        summaryColumnNames.add(CURRENT, "Current Assets");
        summaryColumnNames.add(PROPOSED, "Recommended Assets");

        summaryColumnClasses = new java.util.Vector();
        summaryColumnClasses.add(NAME, java.lang.String.class);
        summaryColumnClasses.add(CURRENT, java.math.BigDecimal.class);
        summaryColumnClasses.add(PROPOSED, java.math.BigDecimal.class);

    }

    class SummaryTableModel extends SmartTableModel {

        private java.util.Vector _data;

        SummaryTableModel(ProxyTableModel current, ProxyTableModel proposed) {
            super(null, summaryColumnNames, summaryColumnClasses);

            initData(current, proposed);
            setData(_data);
        }

        private void initData(ProxyTableModel current, ProxyTableModel proposed) {

            _data = new java.util.Vector();

            if (current == null || proposed == null)
                return;

            ProxyTableModel currentAssetLiability = new ProxyTableModel(
                    current, null, new int[] { ISmartTableRow.FOOTER1 });

            ProxyTableModel proposedAssetLiability = new ProxyTableModel(
                    proposed, null, new int[] { ISmartTableRow.FOOTER1 });

            final double[] cashAssets = new double[2];
            final double[] personalAssets = new double[2];
            final double[] investmentAssets = new double[2];
            final double[] superAssets = new double[2];
            final double[] incomeStreamAssets = new double[2];
            final double[] liabilities = new double[2];
            final double[] residualAmount = new double[2];
            final double[] totalAssets = new double[2];

            for (int i = 0; i < currentAssetLiability.getRowCount(); i++) {
                ISmartTableRow row = currentAssetLiability.getRowAt(i);

                String name = row.getValueAt(FinancialColumnID.NAME).toString();
                java.math.BigDecimal _amount = (java.math.BigDecimal) row
                        .getValueAt(FinancialColumnID.AMOUNT);
                double amount = _amount == null ? 0. : _amount.doubleValue();
                // System.out.println( "currentAssetLiability=" + name + ",
                // amount=" + amount );
                if (amount == 0.)
                    continue;

                if (FinancialClassID.RC_ASSET_CASH.toString().equalsIgnoreCase(
                        name))
                    cashAssets[0] += amount;
                else if (FinancialClassID.RC_ASSET_PERSONAL.toString()
                        .equalsIgnoreCase(name))
                    personalAssets[0] += amount;
                else if (FinancialClassID.RC_ASSET_INVESTMENT.toString()
                        .equalsIgnoreCase(name))
                    investmentAssets[0] += amount;
                else if (FinancialClassID.RC_ASSET_SUPERANNUATION.toString()
                        .equalsIgnoreCase(name))
                    superAssets[0] += amount;
                else if (FinancialClassID.RC_INCOME_STREAM.toString()
                        .equalsIgnoreCase(name))
                    incomeStreamAssets[0] += amount;
                else if (FinancialClassID.RC_LIABILITY.toString()
                        .equalsIgnoreCase(name))
                    liabilities[0] += amount;
                else
                    continue;

                totalAssets[0] += amount;
            }

            for (int i = 0; i < proposedAssetLiability.getRowCount(); i++) {
                ISmartTableRow row = proposedAssetLiability.getRowAt(i);

                String name = row.getValueAt(FinancialColumnID.NAME).toString();
                java.math.BigDecimal _amount = (java.math.BigDecimal) row
                        .getValueAt(FinancialColumnID.AMOUNT);
                double amount = _amount == null ? 0. : _amount.doubleValue();
                // System.out.println( "proposedAssetLiability=" + name + ",
                // amount=" + amount );
                if (amount == 0.)
                    continue;

                if (FinancialClassID.RC_ASSET_CASH.toString().equalsIgnoreCase(
                        name))
                    cashAssets[1] += amount;
                else if (FinancialClassID.RC_ASSET_PERSONAL.toString()
                        .equalsIgnoreCase(name))
                    personalAssets[1] += amount;
                else if (FinancialClassID.RC_ASSET_INVESTMENT.toString()
                        .equalsIgnoreCase(name))
                    investmentAssets[1] += amount;
                else if (FinancialClassID.RC_ASSET_SUPERANNUATION.toString()
                        .equalsIgnoreCase(name))
                    superAssets[1] += amount;
                else if (FinancialClassID.RC_INCOME_STREAM.toString()
                        .equalsIgnoreCase(name))
                    incomeStreamAssets[1] += amount;
                else if (FinancialClassID.RC_LIABILITY.toString()
                        .equalsIgnoreCase(name))
                    liabilities[1] += amount;
                else
                    continue;

                totalAssets[1] += amount;
            }

            residualAmount[1] = totalAssets[0] - totalAssets[1];
            totalAssets[1] = totalAssets[0];

            ISmartTableRow row = new AbstractSmartTableRow(
                    ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return FinancialClassID.RC_ASSET_CASH.toString();
                    case CURRENT:
                        return _money.toString(cashAssets[0]);
                    case PROPOSED:
                        return _money.toString(cashAssets[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return FinancialClassID.RC_ASSET_PERSONAL.toString();
                    case CURRENT:
                        return _money.toString(personalAssets[0]);
                    case PROPOSED:
                        return _money.toString(personalAssets[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return FinancialClassID.RC_ASSET_INVESTMENT.toString();
                    case CURRENT:
                        return _money.toString(investmentAssets[0]);
                    case PROPOSED:
                        return _money.toString(investmentAssets[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return FinancialClassID.RC_ASSET_SUPERANNUATION
                                .toString();
                    case CURRENT:
                        return _money.toString(superAssets[0]);
                    case PROPOSED:
                        return _money.toString(superAssets[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return FinancialClassID.RC_INCOME_STREAM.toString();
                    case CURRENT:
                        return _money.toString(incomeStreamAssets[0]);
                    case PROPOSED:
                        return _money.toString(incomeStreamAssets[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return FinancialClassID.RC_LIABILITY.toString();
                    case CURRENT:
                        return _money.toString(liabilities[0]);
                    case PROPOSED:
                        return _money.toString(liabilities[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return "Residual Amount";
                    case CURRENT:
                        return _money.toString(residualAmount[0]);
                    case PROPOSED:
                        return _money.toString(residualAmount[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

            row = new AbstractSmartTableRow(ISmartTableRow.HEADER1) {
                public Object getValueAt(int columnIndex) {
                    switch (columnIndex) {
                    case NAME:
                        return "Total Assets";
                    case CURRENT:
                        return _money.toString(totalAssets[0]);
                    case PROPOSED:
                        return _money.toString(totalAssets[1]);
                    default:
                        return null;
                    }
                }
            };
            _data.add(row);

        }

    }

    // //////////////////////////////////////////////////////////////////////////
    // RecommendationsTableModel //
    // //////////////////////////////////////////////////////////////////////////
    // public static final int NAME = 0;
    public static final int TOTAL = 1;

    private static java.util.Vector recommendationsColumnNames;

    private static java.util.Vector recommendationsColumnClasses;
    static {
        recommendationsColumnNames = new java.util.Vector();
        recommendationsColumnNames.add(NAME, "Summary of Asset Changes");
        recommendationsColumnNames.add(TOTAL, "Amount");

        recommendationsColumnClasses = new java.util.Vector();
        recommendationsColumnClasses.add(NAME, java.lang.String.class);
        recommendationsColumnClasses.add(TOTAL, java.math.BigDecimal.class);

    }

    class RecommendationsTableModel extends SmartTableModel {

        private java.util.Vector _data;

        RecommendationsTableModel(java.util.Collection recommendations) {
            super(null, recommendationsColumnNames,
                    recommendationsColumnClasses);

            initData(recommendations);
            setData(_data);
        }

        private void initData(java.util.Collection recommendations) {

            _data = new java.util.Vector();

            if (recommendations == null)
                return;

            java.util.Iterator iter = recommendations.iterator();
            double totalSourceAmount = 0.;
            double totalDestinationAmount = 0.;

            while (iter.hasNext()) {
                final Recommendation recommendation = (Recommendation) iter
                        .next();
                java.util.Iterator iterSource = recommendation.getSource()
                        .iterator();
                java.util.Iterator iterDestination = recommendation
                        .getDestination().iterator();

                ISmartTableRow row = new AbstractSmartTableRow(
                        ISmartTableRow.HEADER1) {
                    public Object getValueAt(int columnIndex) {
                        switch (columnIndex) {
                        case NAME:
                            return recommendation.getName();
                        case TOTAL:
                        default:
                            return null;
                        }
                    }
                };
                _data.add(row);

                // //////////////////////////////////////////////////////////////
                // Source
                // //////////////////////////////////////////////////////////////
                row = new AbstractSmartTableRow(ISmartTableRow.HEADER2) {
                    public Object getValueAt(int columnIndex) {
                        switch (columnIndex) {
                        case NAME:
                            return "Assets used";
                        case TOTAL:
                        default:
                            return null;
                        }
                    }
                };
                _data.add(row);

                while (iterSource.hasNext()) {
                    final StrategyFinancial sf = (StrategyFinancial) iterSource
                            .next();
                    totalSourceAmount += sf.getAmount(true).doubleValue();

                    row = new AbstractSmartTableRow(ISmartTableRow.BODY) {
                        public Object getValueAt(int columnIndex) {
                            switch (columnIndex) {
                            case NAME:
                                return sf.toString();
                            case TOTAL:
                                return _money.toString(sf.getAmount(true));
                            default:
                                return null;
                            }
                        }
                    };
                    _data.add(row);

                }

                final double totalSource = totalSourceAmount;
                row = new AbstractSmartTableRow(ISmartTableRow.FOOTER2) {
                    public Object getValueAt(int columnIndex) {
                        switch (columnIndex) {
                        case NAME:
                            return "Total Source";
                        case TOTAL:
                            return _money.toString(totalSource);
                        default:
                            return null;
                        }
                    }
                };
                _data.add(row);

                // //////////////////////////////////////////////////////////////
                // Destination
                // //////////////////////////////////////////////////////////////
                row = new AbstractSmartTableRow(ISmartTableRow.HEADER2) {
                    public Object getValueAt(int columnIndex) {
                        switch (columnIndex) {
                        case NAME:
                            return "Assets created/changed";
                        case TOTAL:
                        default:
                            return null;
                        }
                    }
                };
                _data.add(row);

                while (iterDestination.hasNext()) {
                    final Financial f = (Financial) iterDestination.next();
                    totalDestinationAmount += f.getAmount(true).doubleValue();

                    row = new AbstractSmartTableRow(ISmartTableRow.BODY) {
                        public Object getValueAt(int columnIndex) {
                            switch (columnIndex) {
                            case NAME:
                                return f.toString();
                            case TOTAL:
                                return _money.toString(f.getAmount(true));
                            default:
                                return null;
                            }
                        }
                    };
                    _data.add(row);

                }

                final double totalDestination = totalDestinationAmount;
                row = new AbstractSmartTableRow(ISmartTableRow.FOOTER2) {
                    public Object getValueAt(int columnIndex) {
                        switch (columnIndex) {
                        case NAME:
                            return "Total Destination";
                        case TOTAL:
                            return _money.toString(totalDestination);
                        default:
                            return null;
                        }
                    }
                };
                _data.add(row);

                final double totalResidual = totalSource - totalDestination;
                row = new AbstractSmartTableRow(ISmartTableRow.FOOTER1) {
                    public Object getValueAt(int columnIndex) {
                        switch (columnIndex) {
                        case NAME:
                            return "Residual Amount";
                        case TOTAL:
                            return _money.toString(totalResidual);
                        default:
                            return null;
                        }
                    }
                };
                _data.add(row);

            }
            /*
             * final double grandTotalResidual = sum of totalResidual;
             * ISmartTableRow row = new AbstractSmartTableRow(
             * ISmartTableRow.FOOTER1 ) { public Object getValueAt(int
             * columnIndex) { switch (columnIndex) { case NAME : return "Total
             * Residual Amount"; case TOTAL : return _money.toString(
             * grandTotalResidual ); default : return null; } } }; _data.add(
             * row );
             */
        }

    }

}
