/*
 * ETPData.java
 *
 * Created on 23 August 2002, 14:52
 */

package com.argus.financials.report.data;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.projection.save.Model;
import com.argus.financials.service.PersonService;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;

public class ETPData extends ClientPersonData {

    /** Creates new ETPData */
    public ETPData() {
    }

    public ETP etp = new ETP();

    public class ETP implements java.io.Serializable {
        public String dob = STRING_EMPTY;

        public String eligibleServiceDate = STRING_EMPTY;

        public String calculationDate = STRING_EMPTY;

        public String thresholdUsed = STRING_ZERO;

        public String thresholdUnused = STRING_ZERO;

        public String taxRate = STRING_ZERO;

        public String totalDays = STRING_ZERO;

        public String preDays = STRING_ZERO;

        public String postDays = STRING_ZERO;

        public String preTotal = STRING_ZERO;

        public String postTaxedTotal = STRING_ZERO;

        public String postUntaxedTotal = STRING_ZERO;

        public String undeductedTotal = STRING_ZERO;

        public String cgtExemptTotal = STRING_ZERO;

        public String excessTotal = STRING_ZERO;

        public String concessionalTotal = STRING_ZERO;

        public String invalidityTotal = STRING_ZERO;

        public String totalAmount = STRING_ZERO;

        public String preRollover = STRING_ZERO;

        public String postTaxedRollover = STRING_ZERO;

        public String postUntaxedRollover = STRING_ZERO;

        public String undeductedRollover = STRING_ZERO;

        public String cgtExemptRollover = STRING_ZERO;

        public String excessRollover = STRING_ZERO;

        public String concessionalRollover = STRING_ZERO;

        public String invalidityRollover = STRING_ZERO;

        public String rolloverAmount = STRING_ZERO;

        public String preWithdraw = STRING_ZERO;

        public String postTaxedWithdraw = STRING_ZERO;

        public String postUntaxedWithdraw = STRING_ZERO;

        public String undeductedWithdraw = STRING_ZERO;

        public String cgtExemptWithdraw = STRING_ZERO;

        public String excessWithdraw = STRING_ZERO;

        public String concessionalWithdraw = STRING_ZERO;

        public String invalidityWithdraw = STRING_ZERO;

        public String withdrawAmount = STRING_ZERO;

        public String taxOnPostUntaxedRollover = STRING_ZERO;

        public String taxOnWithdrawExcess = STRING_ZERO;

        public String taxOnWithdrawConcessional = STRING_ZERO;

        public String taxOnWithdrawPre = STRING_ZERO;

        public String taxOnWithdrawPostTaxed = STRING_ZERO;

        public String taxOnWithdrawPostUntaxed = STRING_ZERO;

        public String taxOnWithdrawTotal = STRING_ZERO;

        public String preNetETP = STRING_ZERO;

        public String postTaxedNetETP = STRING_ZERO;

        public String postUntaxedNetETP = STRING_ZERO;

        public String undeductedNetETP = STRING_ZERO;

        public String cgtExemptNetETP = STRING_ZERO;

        public String excessNetETP = STRING_ZERO;

        public String concessionalNetETP = STRING_ZERO;

        public String invalidityNetETP = STRING_ZERO;

        public String netETPAmount = STRING_ZERO;

    }

    public void init(PersonService person, Model model) throws java.io.IOException {

        if (model == null)
            model = Model.NONE;

        ETPCalcNew calc = new ETPCalcNew();
        calc.setModel(model);
        init(person, calc);

    }

    public void init(PersonService person, ETPCalcNew calc) throws java.io.IOException {

        client = new Client();
        adviser = new Adviser();
        adviser.FullName = userPreferences.getUser().getFullName();

        // etp data
        if (calc.getIsClient() == null || calc.getIsClient().booleanValue()) {
            client.FullName = calc.getClientName();
            etp.dob = calc.getDateOfBirth() == null ? STRING_EMPTY
                    : DateTimeUtils.asString(calc.getDateOfBirth());
        } else {
            client.FullName = calc.getPartnerName();
            etp.dob = calc.getPartnerDOB() == null ? STRING_EMPTY
                    : DateTimeUtils.asString(calc.getPartnerDOB());
        }
        etp.thresholdUnused = currency.toString(calc.getThresholdLeft());
        etp.eligibleServiceDate = calc.getEligibleServiceDate() == null ? STRING_EMPTY
                : DateTimeUtils.asString(calc.getEligibleServiceDate());
        etp.calculationDate = calc.getCalculationDate() == null ? STRING_EMPTY
                : DateTimeUtils.asString(calc.getCalculationDate());

        etp.thresholdUsed = calc.getPost061983ThresholdUsed() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getPost061983ThresholdUsed());

        etp.taxRate = calc.getTaxRate() <= 0 ? STRING_ZERO : Percent
                .getPercentInstance().toString(calc.getTaxRate());

        etp.totalDays = new Integer(calc.getTotalDays()).toString();
        etp.preDays = new Integer(calc.getPre071983Days()).toString();
        etp.postDays = new Integer(calc.getPost061983Days()).toString();

        // etpComponents
        etp.postUntaxedTotal = calc.getPost061983UntaxedTotal() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getPost061983UntaxedTotal());
        etp.undeductedTotal = calc.getUndeductedTotal() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getUndeductedTotal());
        etp.cgtExemptTotal = calc.getCGTExemptTotal() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getCGTExemptTotal());
        etp.excessTotal = calc.getExcessTotal() == null ? STRING_ZERO
                : Currency.getCurrencyInstance()
                        .toString(calc.getExcessTotal());
        etp.concessionalTotal = calc.getConcessionalTotal() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getConcessionalTotal());
        etp.invalidityTotal = calc.getInvalidityTotal() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getInvalidityTotal());
        etp.preTotal = Currency.getCurrencyInstance().toString(
                calc.getPre071983Total());
        etp.postTaxedTotal = Currency.getCurrencyInstance().toString(
                calc.getPost061983TaxedTotal());
        etp.totalAmount = calc.getTotalETPCalculationAmount() == null ? STRING_ZERO
                : Currency.getCurrencyInstance().toString(
                        calc.getTotalETPCalculationAmount());

        etp.postUntaxedRollover = STRING_ZERO;
    }

}
