/*
 * PlanData.java
 *
 * Created on March 5, 2002, 8:43 AM
 */

package com.argus.financials.ui.data;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import java.util.Iterator;
import java.util.Properties;

import com.argus.financials.code.ModelTypeID;
import com.argus.financials.projection.AllocatedPensionCalcNew;
import com.argus.financials.projection.DSSCalcNew;
import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.projection.GearingCalc2;
import com.argus.financials.projection.GeneralTaxCalculatorNew;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.strategy.StrategyGroup;
import com.argus.financials.ui.plan.PlanWriterModel;
import com.argus.financials.ui.projection.MortgageCalc;
import com.argus.financials.ui.projection.StrategyCalc;
import com.argus.util.ReferenceCode;

public class PlanWriterData
// extends com.argus.util.BaseObject
        implements com.argus.financials.report.Reportable {

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
            ReportFields reportFields,
            PersonService person)
            throws java.io.IOException {

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
        com.argus.financials.report.data.InvRiskData invriskdata = new com.argus.financials.report.data.InvRiskData();
        invriskdata.init(person, false); // false = don't create
                                            // question/answer table (faster)
        invriskdata.initializeReportData(reportFields, person);

    }

}