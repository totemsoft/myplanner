/*
 * TaxableIncome.java
 *
 * Created on 5 February 2002, 00:00
 */

package au.com.totemsoft.myplanner.tax.au;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
class TaxGross extends TaxRule {

    double taxableIncome;

    double otherIncome;

    double hospital;

    double age;

    double dependants;

    double maritalStatus;

    double spouseIncome;

    /** Creates new TaxableIncome */
    TaxGross() {
    }

    void apply() {

        // Get precalculated list of taxable items

        java.util.ArrayList taxableItems = tc.getTaxableCollection();

        // Initialize target variables
        double grossTax = 0;
        double grossTaxOnNonInvestment = 0;
        double medicareLevy = 0;
        double medicareLevySurchagre = 0;

        double medicareLevyNonIvestment = 0;
        double medicareLevySurchagreNonIvestment = 0;

        /**
         * There is a difference in medicare Levy Surchagre calculation which
         * comes from the client's personal details: - Surcharge is not
         * applicable if client holds Private Health Insurance; - Surcharge is
         * not applicable if client's income less than < nnnnnn; - Surcharge
         * gets applied to higher income range if client has dependants;
         */

        for (int i = 0; i < taxableItems.size(); i++) {
            TaxElement te = (TaxElement) taxableItems.get(i);
            grossTax = grossTax + te.getAmount() * te.getTaxRate() / 100.00;
            if (te.getType().equals(ITaxConstants.NON_INVESTMENT_INCOME)) {
                grossTaxOnNonInvestment = grossTaxOnNonInvestment
                        + te.getAmount() * te.getTaxRate() / 100.00;
            }
        }

        grossTax = Math.round(grossTax);
        grossTaxOnNonInvestment = Math.round(grossTaxOnNonInvestment);

        // Work out if client have to pay Medicare Levy Surcharge

        taxableIncome = tc.sumCollection(tc.TAX,
                new String[] { ITaxConstants.TAXABLE_INCOME });

        otherIncome = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_OTHER });

        hospital = tc.sumCollection(tc.PERSONAL,
                new String[] { ITaxConstants.P_HOSPITAL_COVER });

        age = tc
                .sumCollection(tc.PERSONAL, new String[] { ITaxConstants.P_AGE });

        dependants = tc.sumCollection(tc.PERSONAL,
                new String[] { ITaxConstants.P_DEPENDANTS });

        maritalStatus = tc.sumCollection(tc.PERSONAL,
                new String[] { ITaxConstants.P_MARITAL_STATUS });
        /* if person had the dependands he is considerd as a member of family */
        if (dependants > 0.00)
            maritalStatus = 1.00;

        spouseIncome = tc.sumCollection(tc.PERSONAL,
                new String[] { ITaxConstants.P_SPOUSE_INCOME });

        // Taxable Income for medicare levy purposes needs to be reduced by Post
        // June83 taxed component
        //
        double postJune83Taxed = tc.sumCollection(tc.INCOME,
                new String[] { ITaxConstants.I_POST_JUNE_1983_TAXED });

        if (age >= 55 && postJune83Taxed <= ITaxConstants.POST_JUNE_83_THRESHOLD)
            taxableIncome = taxableIncome - postJune83Taxed;
        else if (age >= 55
                && postJune83Taxed > ITaxConstants.POST_JUNE_83_THRESHOLD)
            taxableIncome = taxableIncome - ITaxConstants.POST_JUNE_83_THRESHOLD;

        medicareLevy = getMedicareLevy(taxableIncome, spouseIncome);
        medicareLevySurchagre = getMedicareLevySurcharge(taxableIncome);

        medicareLevyNonIvestment = getMedicareLevy(otherIncome, spouseIncome);
        medicareLevySurchagreNonIvestment = getMedicareLevySurcharge(otherIncome);

        // Create taxable income entry in taxCollection
        tc.add(tc.TAX, ITaxConstants.TAX_ON_TAXABLE_INCOME, grossTax);
        tc.add(tc.TAX, ITaxConstants.ML, medicareLevy);
        tc.add(tc.TAX, ITaxConstants.MLS, medicareLevySurchagre);

        tc.add(tc.TAX, ITaxConstants.TAX_ON_NON_INVESTMENT_INCOME,
                grossTaxOnNonInvestment);
        tc
                .add(tc.TAX, ITaxConstants.ML_NON_INVESTMENT,
                        medicareLevyNonIvestment);
        tc.add(tc.TAX, ITaxConstants.MLS_NON_INVESTMENT,
                medicareLevySurchagreNonIvestment);

    }

    private double getMedicareLevy(double taxableIncome, double spouseIncome) {

        // No levy payable
        if (taxableIncome == 0.0)
            return 0.0;
        if (maritalStatus == 0.0
                && taxableIncome < ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL)
            return 0.0;
        if (maritalStatus == 1.0
                && taxableIncome + spouseIncome < ITaxConstants.ML_THRESHOLD_MIN_FAMILY
                        + ITaxConstants.ML_THRESHOLD_MIN_STEP * (dependants))
            return 0.0;
        // reduced levy payable
        if (maritalStatus == 0.0
                && taxableIncome <= ITaxConstants.ML_THRESHOLD_MAX_INDIVIDUAL)

            return Math
                    .round((taxableIncome - ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL)
                            * ITaxConstants.MEDICARE_LEVY_LOW_INCOME / 100.);

        // apply reduction amount
        if (maritalStatus == 1.0
                && (taxableIncome < ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL || spouseIncome < ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL)
                && taxableIncome + spouseIncome < ITaxConstants.ML_THRESHOLD_MAX_FAMILY
                        + ITaxConstants.ML_THRESHOLD_MAX_STEP * (dependants)) {
            if (taxableIncome < ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL)
                return 0.0;
            // get the ordinary levy
            double ordinaryRate = taxableIncome * ITaxConstants.MEDICARE_LEVY
                    / 100.00;
            double familyRhreshold = (ITaxConstants.ML_THRESHOLD_MIN_FAMILY + ITaxConstants.ML_THRESHOLD_MIN_STEP
                    * (dependants));
            return Math
                    .round(ordinaryRate
                            - (1.5 / 100. * familyRhreshold - 18.5 / 100. * (taxableIncome
                                    + spouseIncome - familyRhreshold)));
        }

        // apply reduction amount apportioned between client and partner

        if (maritalStatus == 1.0
                && (taxableIncome < ITaxConstants.ML_THRESHOLD_MAX_INDIVIDUAL || spouseIncome < ITaxConstants.ML_THRESHOLD_MAX_INDIVIDUAL)
                && taxableIncome + spouseIncome < ITaxConstants.ML_THRESHOLD_MAX_FAMILY
                        + ITaxConstants.ML_THRESHOLD_MAX_STEP * (dependants)) {
            double familyRhreshold = (ITaxConstants.ML_THRESHOLD_MIN_FAMILY + ITaxConstants.ML_THRESHOLD_MIN_STEP
                    * (dependants));
            // get the reduction amount
            double reduction = 1.5 / 100. * familyRhreshold - 18.5 / 100.
                    * (taxableIncome + spouseIncome - familyRhreshold);
            double clientReduction = taxableIncome
                    / (taxableIncome + spouseIncome) * reduction;
            double spouseReduction = spouseIncome
                    / (taxableIncome + spouseIncome) * reduction;

            double excess = 0.00;

            if (taxableIncome < ITaxConstants.ML_THRESHOLD_MAX_INDIVIDUAL) {
                excess = (taxableIncome - ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL)
                        * ITaxConstants.MEDICARE_LEVY_LOW_INCOME
                        / 100.
                        - clientReduction;
                // No levy payable
                if (excess <= 0.00)
                    return 0.00;
            } else if (spouseIncome < ITaxConstants.ML_THRESHOLD_MAX_INDIVIDUAL) {
                excess = (spouseIncome - ITaxConstants.ML_THRESHOLD_MIN_INDIVIDUAL)
                        * ITaxConstants.MEDICARE_LEVY_LOW_INCOME
                        / 100.
                        - spouseReduction;
                // No levy payable excess goes to client
                if (excess > 0.00)
                    excess = 0.00;

                double clientRate = ITaxConstants.MEDICARE_LEVY * taxableIncome
                        / 100. - clientReduction + excess;
                if (clientRate <= 0.00)
                    return 0.00;
                return Math.round(clientRate);
            }

            return 0.00;
        }

        if (maritalStatus == 1.0
                && taxableIncome + spouseIncome < ITaxConstants.ML_THRESHOLD_MAX_FAMILY
                        + ITaxConstants.ML_THRESHOLD_MAX_STEP * (dependants))
            return Math
                    .round(taxableIncome
                            / (taxableIncome + spouseIncome)
                            * (taxableIncome + spouseIncome - (ITaxConstants.ML_THRESHOLD_MIN_FAMILY + ITaxConstants.ML_THRESHOLD_MIN_STEP
                                    * (dependants)))
                            * ITaxConstants.MEDICARE_LEVY_LOW_INCOME / 100.);

        return Math
                .round((taxableIncome) * ITaxConstants.MEDICARE_LEVY / 100.00);

    }

    private double getMedicareLevySurcharge(double taxableIncome) {

        if (hospital == 1.00)
            return 0.0;
        if (taxableIncome + spouseIncome <= ITaxConstants.SINGLE_INCOME_THRESHOLD
                + ITaxConstants.SINGLE_INCOME_THRESHOLD
                * maritalStatus
                + ITaxConstants.DEPENDANT_THRESHOLD * (dependants))
            return 0.00;

        return Math.round((taxableIncome)
                * ITaxConstants.MEDICARE_LEVY_SURCHARGE / 100.00);

    }

}
