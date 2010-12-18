/*
 * java
 *
 * Created on 11 August 2002, 13:21
 */

package com.argus.financials.report.data;

/**
 * 
 * @author shibaevv
 * @version
 */

import com.argus.financials.code.DeathBenefitCode;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.SelectedAnnualPensionCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.projection.AllocatedPensionCalcNew;
import com.argus.financials.projection.data.APConstants;
import com.argus.financials.projection.save.Model;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;

public class AllocatedPensionData extends ClientPersonData {

    /** Creates new AllocatedPensionData */
    public AllocatedPensionData() {
    }

    public AllocatedPension allocatedPension = new AllocatedPension();

    public class AllocatedPension implements java.io.Serializable {

        public Name name = new Name();

        public ETPData etpData = new ETPData();

        public APData apData = new APData();

        public String Graph = STRING_EMPTY;

        public String GraphName = STRING_EMPTY;

        public class Name implements java.io.Serializable {
            public String name = STRING_EMPTY;
        }

        public class ETPData implements java.io.Serializable {
            // ETP details
            public String eligibleServiceDate = STRING_EMPTY;

            public String preJuly = STRING_EMPTY;

            public String postJuneTaxed = STRING_EMPTY;

            public String postJuneUntaxed = STRING_EMPTY;

            public String undeducted = STRING_EMPTY;

            public String cgtExempt = STRING_EMPTY;

            public String excess = STRING_EMPTY;

            public String concessional = STRING_EMPTY;

            public String invalidity = STRING_EMPTY;

            public String total = STRING_EMPTY;
        }

        public class APData implements java.io.Serializable {
            // Pension Payment Levels
            public String pensionStartDate = STRING_EMPTY;

            public String selectedAnnualPensionType = STRING_EMPTY;

            public String selectedAnnualPensionAmount = STRING_EMPTY;

            public String deductibleAmount = STRING_EMPTY;

            public String frequency = STRING_EMPTY;

            public String firstPaymentDate = STRING_EMPTY;

            public String netEarningRate = STRING_EMPTY;

            public String netEarningRateType = STRING_EMPTY;

            public String DeathBenefitOption = STRING_EMPTY;

            public String maxAmount = STRING_EMPTY;

            public String minAmount = STRING_EMPTY;

        }
    }

    public AllocatedPensionProjection allocatedPensionProjection = new AllocatedPensionProjection();

    public class AllocatedPensionProjection implements java.io.Serializable {

        public java.util.ArrayList Items = new java.util.ArrayList();

        public Item createItem() {
            return new Item();
        }

        public class Item implements java.io.Serializable {
            public String age = STRING_EMPTY;

            public String openingBalanceAmount = STRING_EMPTY;

            public String selectedPensionAmount = STRING_EMPTY;

            public String assessableIncomeAmount = STRING_EMPTY;

            public String rebateAmount = STRING_EMPTY;

            public String netPensionAmount = STRING_EMPTY;

            public String endBalanceAmount = STRING_EMPTY;

            public String earningsAmount = STRING_EMPTY;
            // public String maxAmount = STRING_EMPTY;
            // public String minAmount = STRING_EMPTY;
        }

    }

    protected void clear() {
        allocatedPension = new AllocatedPension();
        allocatedPensionProjection = new AllocatedPensionProjection();
    }

    public void init(PersonService person, Model model) throws java.io.IOException {

        if (model == null)
            model = Model.NONE;

        AllocatedPensionCalcNew calc = new AllocatedPensionCalcNew();
        calc.setModel(model);
        init(person, calc);

    }

    public void init(PersonService person, AllocatedPensionCalcNew calc)
            throws java.io.IOException {

        client = new Client();
        adviser = new Adviser();
        partner = new Partner();
        adviser.FullName = ServiceLocator.getInstance().getUserService()
                .getPersonName().getFullName();

        if (calc.getIsClient() == null || calc.getIsClient().booleanValue()) {
            client.FullName = calc.getClientName();
            client.DOB = DateTimeUtils.asString(calc.getDateOfBirth());
            client.Sex = new SexCode().getCodeDescription(calc.getSexCodeID());

            if (calc.getPartnerName() != null) {
                client.MaritalCode = "Married";
                partner.FullName = calc.getPartnerName();
                partner.DOB = DateTimeUtils.asString(calc.getPartnerDOB());
            }
        } else {
            client.FullName = calc.getPartnerName();
            client.DOB = DateTimeUtils.asString(calc.getPartnerDOB());
            client.Sex = new SexCode().getCodeDescription(calc
                    .getPartnerSexCodeID());

            if (calc.getClientName() != null) {
                client.MaritalCode = "Married";
                partner.FullName = calc.getClientName();
                partner.DOB = DateTimeUtils.asString(calc.getDateOfBirth());
            }
        }

        // Allocated Pension data
        if (!calc.isReady()) {
            clear();
            return;
        }

        calc.calculate();

        allocatedPension.name.name = calc.getModel().getTitle() == null ? STRING_EMPTY
                : calc.getModel().getTitle();
        allocatedPension.apData.pensionStartDate = DateTimeUtils.asString(calc
                .getPensionStartDate()) == null ? STRING_EMPTY : DateTimeUtils
                .asString(calc.getPensionStartDate());
        allocatedPension.etpData.eligibleServiceDate = DateTimeUtils
                .asString(calc.getEligibleServiceDate()) == null ? STRING_EMPTY
                : DateTimeUtils.asString(calc.getEligibleServiceDate());
        allocatedPension.etpData.preJuly = currency.toString(calc
                .getPre071983Total()) == null ? STRING_EMPTY : currency
                .toString(calc.getPre071983Total());
        allocatedPension.etpData.postJuneTaxed = currency.toString(calc
                .getPost061983TaxedTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getPost061983TaxedTotal());
        allocatedPension.etpData.postJuneUntaxed = currency.toString(calc
                .getPost061983UntaxedTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getPost061983UntaxedTotal());
        allocatedPension.etpData.undeducted = currency.toString(calc
                .getUndeductedTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getUndeductedTotal());
        allocatedPension.etpData.cgtExempt = currency.toString(calc
                .getCGTExemptTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getCGTExemptTotal());
        allocatedPension.etpData.excess = currency.toString(calc
                .getExcessTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getExcessTotal());
        allocatedPension.etpData.concessional = currency.toString(calc
                .getConcessionalTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getConcessionalTotal());
        allocatedPension.etpData.invalidity = currency.toString(calc
                .getInvalidityTotal()) == null ? STRING_EMPTY : currency
                .toString(calc.getInvalidityTotal());
        allocatedPension.etpData.total = currency.toString(calc
                .getTotalETPAmount()) == null ? STRING_EMPTY : currency
                .toString(calc.getTotalETPAmount());
        allocatedPension.apData.deductibleAmount = money.roundToTen(calc
                .getATOAnnualDeductibleAmount()) == null ? STRING_EMPTY
                : currency.toString(calc.getATOAnnualDeductibleAmount());
        allocatedPension.apData.frequency = new FrequencyCode()
                .getCodeDescription(calc.getPensionFrequencyCodeID());
        allocatedPension.apData.firstPaymentDate = DateTimeUtils.asString(calc
                .getFirstPaymentDate()) == null ? STRING_EMPTY : DateTimeUtils
                .asString(calc.getFirstPaymentDate());
        allocatedPension.apData.netEarningRate = Percent.getPercentInstance()
                .toString(calc.getNetEarningRate());
        allocatedPension.apData.selectedAnnualPensionType = new SelectedAnnualPensionCode()
                .getCode(calc.getSelectedAnnualPensionType()).getCodeDesc();
        if (APConstants.OTHER.equals(calc.getSelectedAnnualPensionType()))
            allocatedPension.apData.selectedAnnualPensionAmount = money
                    .roundToTen(calc.getOtherValue()).toString();
        else if (APConstants.MAX.equals(calc.getSelectedAnnualPensionType()))
            allocatedPension.apData.selectedAnnualPensionAmount = money
                    .roundToTen(calc.getAnnualMaximumAmount()).toString();
        else if (APConstants.MIN.equals(calc.getSelectedAnnualPensionType()))
            allocatedPension.apData.selectedAnnualPensionAmount = money
                    .roundToTen(calc.getAnnualMinimumAmount()).toString();
        allocatedPension.apData.maxAmount = money.roundToTen(
                calc.getAnnualMaximumAmount()).toString();
        allocatedPension.apData.minAmount = money.roundToTen(
                calc.getAnnualMinimumAmount()).toString();
        allocatedPension.apData.DeathBenefitOption = new DeathBenefitCode()
                .getCode(calc.getDeathBenefitID()).getCodeDesc();
        allocatedPension.apData.netEarningRateType = (calc.getFlatRate() != null && calc
                .getFlatRate().booleanValue()) ? "Flat" : "Variable";

        int[] age = calc.getAgeArray();
        double[] openingBalance = calc.getOpeningBalanceArray();
        double[] selectedPension = calc.getSelectedPensionArray();
        double[] assessableIncome = calc.getAssessableIncomeArray();
        double[] rebate = calc.getRebateArray();
        double[] netPension = calc.getNetPensionArray();
        double[] endBalance = calc.getEndBalanceArray();
        double[] earnings = calc.getNetEarningsArray();
        double[] max = calc.getMaximumArray();
        double[] min = calc.getMinimumArray();

        if (age != null) {

            AllocatedPensionProjection.Item item;
            int j = 0;
            // for ( int i = age.length -1; i >=0; i-- ) {
            for (int i = 0; i < age.length; i++) {
                item = allocatedPensionProjection.createItem();

                item.age = new Integer(age[i]).toString();

                item.openingBalanceAmount = money.roundToTen(openingBalance[i])
                        .toString();
                item.selectedPensionAmount = money.roundToTen(
                        selectedPension[i]).toString();
                item.assessableIncomeAmount = money.roundToTen(
                        assessableIncome[i]).toString();
                item.rebateAmount = money.roundToTen(rebate[i]).toString();
                item.netPensionAmount = money.roundToTen(netPension[i])
                        .toString();
                item.endBalanceAmount = money.roundToTen(endBalance[i])
                        .toString();
                item.earningsAmount = money.roundToTen(earnings[i]).toString();
                // item.maxAmount = money.roundToTen( max[i] ).toString();
                // item.minAmount = money.roundToTen( min[i] ).toString();
                allocatedPensionProjection.Items.add(j, item);
                j++;
            }

        }

    }

}
