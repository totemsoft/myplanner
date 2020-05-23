/*
 * PlanData.java
 *
 * Created on March 5, 2002, 8:43 AM
 */

package au.com.totemsoft.myplanner.swing.data;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Iterator;
import java.util.Properties;

import au.com.totemsoft.myplanner.code.ModelTypeID;
import au.com.totemsoft.myplanner.projection.AllocatedPensionCalcNew;
import au.com.totemsoft.myplanner.projection.DSSCalcNew;
import au.com.totemsoft.myplanner.projection.ETPCalcNew;
import au.com.totemsoft.myplanner.projection.GearingCalc2;
import au.com.totemsoft.myplanner.projection.GeneralTaxCalculatorNew;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.projection.save.ModelCollection;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.service.ServiceAware;
import au.com.totemsoft.myplanner.strategy.StrategyGroup;
import au.com.totemsoft.myplanner.swing.plan.PlanWriterModel;
import au.com.totemsoft.myplanner.swing.projection.MortgageCalc;
import au.com.totemsoft.myplanner.swing.projection.StrategyCalc;
import au.com.totemsoft.util.ReferenceCode;

public class PlanWriterData
    extends ServiceAware
    implements au.com.totemsoft.myplanner.report.Reportable {

    public PlanWriterData() {
    }

    private ReferenceCode plan;

    public ReferenceCode getPlan() {
        return plan;
    }

    public void setPlan(ReferenceCode value) {
        plan = value;
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
            ReportFields reportFields,
            PersonService person)
            throws Exception {

        if (person != null)
            reportFields.initialize(person);

        ClientService client = null;
        if (person instanceof ClientService)
            client = (ClientService) person;
        else
            return;

        if (plan == null)
            return;

        // string values
        Properties props = (Properties) plan.getObject();

        // 2. selected strategy (current/proposed financials)
        String strategyName = props.getProperty(PlanWriterModel.STRATEGY);
        System.out.println("***** Search for strategy '" + strategyName
                + "' ....");

        boolean found = false;
        java.util.Collection strategies = client.getStrategies();
        if (strategies != null) {
            Iterator iter = strategies.iterator();
            while (iter.hasNext()) {
                Object item = iter.next();
                String itemValue = item.toString();
                found = itemValue != null && strategyName != null
                        && (strategyName.equals(itemValue) || itemValue.indexOf(strategyName) == 0);
                if (found) {
                    StrategyGroup sg = (StrategyGroup) item;

                    StrategyCalc calc = new StrategyCalc();
                    // Model m = mc.findByName( ModelTypeID.STRATEGY_CALC,
                    // strategyName );
                    // calc.setModel( m );
                    calc.initializeReportData(reportFields, null);
                    break;
                }

            }
        }

        if (!found)
            System.err.println("***** Search for strategy '" + strategyName
                    + "' failed.");

        // 3. selected models
        ModelCollection mc = client.getModels();
        if (mc == null)
            return;

        String apName = props.getProperty(PlanWriterModel.AP);
        AllocatedPensionCalcNew apCalc = new AllocatedPensionCalcNew();
        Model m = mc.findByName(ModelTypeID.ALLOCATED_PENSION, apName);
        apCalc.setModel(m);
        apCalc.initializeReportData(reportFields, null);

        String dssName = props.getProperty(PlanWriterModel.DSS);
        DSSCalcNew dssCalc = new DSSCalcNew();
        m = mc.findByName(ModelTypeID.SOCIAL_SECURITY_CALC, dssName);
        dssCalc.setValuesFromObject(m == null ? null : m.getData());
        dssCalc.initializeReportData(reportFields, null);

        String etpName = props.getProperty(PlanWriterModel.ETP);
        ETPCalcNew etpCalc = new ETPCalcNew();
        m = mc.findByName(ModelTypeID.ETP_ROLLOVER, etpName);
        etpCalc.setModel(m);
        etpCalc.initializeReportData(reportFields, null);

        String gearingName = props.getProperty(PlanWriterModel.GEARING);
        GearingCalc2 gearingCalc = new GearingCalc2();
        m = mc.findByName(ModelTypeID.INVESTMENT_GEARING, gearingName);
        gearingCalc.setModel(m);
        gearingCalc.initializeReportData(reportFields, null);

        String mortgageName = props.getProperty(PlanWriterModel.MORTGAGE);
        MortgageCalc mortgageCalc = new MortgageCalc();
        m = mc.findByName(ModelTypeID.LOAN_MORTGAGE_CALC, mortgageName);
        mortgageCalc.setModel(m);
        mortgageCalc.initializeReportData(reportFields, null);

        String paygName = props.getProperty(PlanWriterModel.PAYG);
        GeneralTaxCalculatorNew paygCalc = new GeneralTaxCalculatorNew();
        m = mc.findByName(ModelTypeID.PAYG_CALC, paygName);
        paygCalc.setValuesFromObject(m == null ? null : m.getData());
        paygCalc.initializeReportData(reportFields, null);

        // 4. InvRiskData (ISO - questions, answers, risk profile)
        au.com.totemsoft.myplanner.report.data.InvRiskData invriskdata = new au.com.totemsoft.myplanner.report.data.InvRiskData();
        invriskdata.init(person, false); // false = don't create
                                            // question/answer table (faster)
        invriskdata.initializeReportData(reportFields, person);

    }

}