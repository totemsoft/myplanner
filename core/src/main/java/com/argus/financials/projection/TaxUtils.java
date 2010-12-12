/*
 * TaxFreeThreshold.java
 *
 * Created on 26 September 2001, 14:58
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @author shibaevv
 * @version
 */

import java.math.BigDecimal;

import com.argus.util.DateTimeUtils;

public final class TaxUtils {

    public static final int TAX_EFFECTIVE_AGE = 55;

    public static final java.util.Date TAX_1983_COMMENCE_DATE = DateTimeUtils
            .getDate("1/7/1983");

    public static final java.util.Date TAX_1994_COMMENCE_DATE = DateTimeUtils
            .getDate("1/7/1994");

    public static final double SUPER_TAX_RATE = 15.; // 15%

    public static final double TAX_FREE_THRESHOLD = 112405.; // 02/03

    public static final double ASSESABLE_INCOME_RATE = 0.05; // Pre 1/7/1983

    // Age Pension
    public static String CLIENT_NONE = "The client is not entitled to any social security benefits. \n";

    public static String PARTNER_NONE = "The partner is not entitled to any social security benefits. \n";

    public static String BASIC_CONDITIONS_OF_ELIGIBILITY_AGE_PENSION = " You qualify for Age Pension if you: \n"
            + "      are over a certain age; and \n"
            + "      meet residence requirements. \n"
            + "Qualifying for the Age Pension can be affected if you are receiving other pensions. \n\n"
            + "If you are a man, you qualify for Age Pension at age 65 years or over. If you are a woman, "
            + "it depends on your date of birth. The minimum age for women to get the Age Pension began to "
            + "increase from 1 July 1995 and will continue to increase until it reaches 65 by 2014, making "
            + "it the same for everyone. Until then, your qualifying age depends on your date of birth.\n";

    public static String BASIC_CONDITIONS_OF_ELIGIBILITY_DISABILITY_SUPPORT_PENSION = "To qualify for a Disability Support Pension, a person must: \n\n"
            + "      have a disability, illness or injury which attracts an impairment rating \n"
            + "of at least 20 points on the impairment tables* and be unable to work full-time "
            + "(or be retrained for full-time work) for at least two years because of a "
            + "disability, illness or injury (full-time work means work for at least 30 hours "
            + "a week at award wages); or \n"
            + "       be permanently blind; and \n"
            + "       be aged at least 16, but less than Age Pension age on the day the claim "
            + "is made; and \n"
            + "       meet residence requirements. \n\n"
            + "If you are claiming Disability Support Pension, you will need to provide a "
            + "report from your doctor on your impairment and work capacity. You may "
            + "also be required to attend a medical examination. Your income and assets "
            + "and those of your partner must be below certain limits. Payment of a "
            + "Disability Support Pension may be affected if you get Workers' "
            + "Compensation or Third Party damages. \n"
            + "If you are permanently blind, you may qualify for Disability Support "
            + "Pension, regardless of income or assets. Their partner has to meet income "
            + "and assets test rules to qualify for a partnered-rate pension \n";

    public static String BASIC_CONDITIONS_OF_ELIGIBILITY_MATURE_AGE_ALLOWANCE = "You qualify for Mature Age Allowance if you: \n\n"
            + "       are 60 years of age or over but less than Agen Pension age; and \n"
            + "       have no recent workforce experience (recent workforce experience "
            + "mans work of a t least 20 hours a week for a total of 13 weeks or "
            + "more in the 12 months before claiming); and \n"
            + "       have received an income support payment for a least nine months and \n"
            + "are on a Newstart Allowance when you claim; or \n"
            + "       have received a payment of a social security pension from Centrelink "
            + "or a Veterans' Affairs Service Pension, or a Widow, Partner, Sickness "
            + "or Parenting payment at any time within the 13 weeks immediately "
            + "before claiming; or \n";

    public static String BASIC_CONDITIONS_OF_ELIGIBILITY_PARTNER_ALLOWANCE = "You qualify for Partner Allowance if you: \n\n"
            + "       were born on or before 1 July 1955; \n"
            + "       are the partner of a person aged at least 21 who is getting Newstart "
            + "Allowance, Special Benefit, Rehabilitation Allowance, Age Pension, "
            + "Disability Support Pension, Mature Age Allowance, a Veterans' "
            + "Affairs Service Pension, ABSTUDY Payment, Austudy Payment, or "
            + "a loan under the Student Financial Supplement Scheme, (in some "
            + "circumstances, you can continue to get Partner Allowance if your "
            + "partner stops getting their payment); \n"
            + "       do not qualify for Parenting Payment, (that is, you have no "
            + "dependent children you can claim Parenting Payment for); \n"
            + "       are not engaged in industrial action; \n"
            + "       are not serving a Newstart Allowance, Youth Allowance or Austudy "
            + "payment waiting period; \n"
            + "       are not serving a penalty non-payment period ; \n"
            + "       have no recent workforce experience (employment of at least 20 "
            + "hours per week for a total of 13 weeks or more in the previous 12 "
            + "months); and \n" + "       meet residence requirements. \n";

    public static BigDecimal GIFTS_THRESHOLD = new BigDecimal(10000.);

    public static BigDecimal DEEMING_RATES_LOW = new BigDecimal(.025); // 5.
                                                                        // December
                                                                        // 2002
                                                                        // -
                                                                        // http://www.centrelink.gov.au/internet/internet.nsf/factors/income_deeming.htm

    public static BigDecimal DEEMING_RATES_HIGH = new BigDecimal(.04); // 5.
                                                                        // December
                                                                        // 2002
                                                                        // -
                                                                        // http://www.centrelink.gov.au/internet/internet.nsf/factors/income_deeming.htm

    public static BigDecimal DEEMING_RATES_SINGLE_THRESHOLD = new BigDecimal(
            34400.); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/factors/income_deeming.htm

    public static BigDecimal DEEMING_RATES_COUPLE_THRESHOLD = new BigDecimal(
            57400.); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/factors/income_deeming.htm

    public static BigDecimal MAX_AGE_PENSION_SINGLE = new BigDecimal(429.40); // 5.
                                                                                // December
                                                                                // 2002
                                                                                // -
                                                                                // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_agepens.htm

    public static BigDecimal MAX_AGE_PENSION_COUPLE = new BigDecimal(358.40); // 5.
                                                                                // December
                                                                                // 2002
                                                                                // -
                                                                                // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_agepens.htm

    public static BigDecimal PHARMACUETICAL_ALLOWANCE_SINGLE = new BigDecimal(
            5.80); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_agepens.htm

    public static BigDecimal PHARMACUETICAL_ALLOWANCE_COUPLE = new BigDecimal(
            2.90); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_agepens.htm

    public static BigDecimal MAXIMUM_RENT_ASSIATANCE = new BigDecimal(92.00); // 5.
                                                                                // December
                                                                                // 2002
                                                                                // -
                                                                                // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD = new BigDecimal(
            81.60); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD = new BigDecimal(
            204.27); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal RENT_ASSISTANCE_FACTOR = new BigDecimal(.75); // 5.
                                                                            // December
                                                                            // 2002
                                                                            // -
                                                                            // http://www.centrelink.gov.au/internet/internet.nsf/ea3b9a1335df87bcca2569890008040e/96ec70ab35b682dfca25699200115000!OpenDocument

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_SHARER = new BigDecimal(
            61.33); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER = new BigDecimal(
            81.60); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SHARER = new BigDecimal(
            163.37); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_COUPLE = new BigDecimal(
            86.80); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE = new BigDecimal(
            133.00); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE = new BigDecimal(
            248.73); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_SEPARATED = new BigDecimal(
            92.00); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED = new BigDecimal(
            81.60); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED = new BigDecimal(
            204.27); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    // single with 1 or 2 children
    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_SINGLE_1_OR_2 = new BigDecimal(
            107.94); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_1_OR_2 = new BigDecimal(
            107.52); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_1_OR_2 = new BigDecimal(
            251.44); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    // single with 3 or more children
    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_SINGLE_3_OR_MORE = new BigDecimal(
            122.08); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_3_OR_MORE = new BigDecimal(
            107.52); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SINGLE_3_OR_MORE = new BigDecimal(
            270.29); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    // couple with 1 or 2 children
    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_COUPLE_1_OR_2 = new BigDecimal(
            107.94); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_1_OR_2 = new BigDecimal(
            159.18); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_1_OR_2 = new BigDecimal(
            303.10); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    // couple with 3 or more children
    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_COUPLE_3_OR_MORE = new BigDecimal(
            122.08); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_3_OR_MORE = new BigDecimal(
            159.18); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_COUPLE_3_OR_MORE = new BigDecimal(
            321.95); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    // not calculated yet
    public static BigDecimal MAXIMUM_RENT_ASSIATANCE_SEPARATED_TEMPORARILY = new BigDecimal(
            86.80); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MINIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED_TEMPORARILY = new BigDecimal(
            81.60); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    public static BigDecimal MAXIMUM_RENT_ASSISTANCE_THRESHOLD_SEPARATED_TEMPORARILY = new BigDecimal(
            197.33); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/pay_how_ra.htm

    // Assets Test for Homeowners
    public static BigDecimal MEANS_LIMITS_ASSETS_HO_SINGLE = new BigDecimal(
            145250.); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartab.htm

    public static BigDecimal MEANS_LIMITS_ASSETS_HO_COUPLE = new BigDecimal(
            206500.); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartab.htm

    // Assets Test for Non-Homeowners
    public static BigDecimal MEANS_LIMITS_ASSETS_NH_SINGLE = new BigDecimal(
            249750.); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartab.htm

    public static BigDecimal MEANS_LIMITS_ASSETS_NH_COUPLE = new BigDecimal(
            311000.); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartab.htm

    // Factor
    public static BigDecimal MEANS_LIMITS_ASSETS_FACTOR = new BigDecimal(3.); // 5.
                                                                                // December
                                                                                // 2002
                                                                                // -
                                                                                // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartab.htm

    // Income Test for Pensions
    // For full payment (per fortnight)
    // Single
    public static BigDecimal MEANS_LIMITS_INCOME_SINGLE = new BigDecimal(116.); // 5.
                                                                                // December
                                                                                // 2002
                                                                                // -
                                                                                // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    // Couple (combined)
    public static BigDecimal MEANS_LIMITS_INCOME_COUPLE = new BigDecimal(204.); // 5.
                                                                                // December
                                                                                // 2002
                                                                                // -
                                                                                // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    // illness separated couple
    public static BigDecimal MEANS_LIMITS_INCOME_ILLNESS_SEPARATED_COUPLE = new BigDecimal(
            204.); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    // Single + 1 child
    public static BigDecimal MEANS_LIMITS_INCOME_SINGLE_ONE_CHILD = new BigDecimal(
            140.60); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    // Additional Children
    public static BigDecimal MEANS_LIMITS_INCOME_CHILDREN = new BigDecimal(
            24.60); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    // Factors
    public static BigDecimal MEANS_LIMITS_INCOME_SINGLE_FACTOR = new BigDecimal(
            .40); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    public static BigDecimal MEANS_LIMITS_INCOME_COUPLE_FACTOR = new BigDecimal(
            .20); // 5. December 2002 -
                    // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    // For part payment (per fortnight)
    public static BigDecimal MEANS_LIMITS_INCOME_PART_SINGLE = new BigDecimal(
            1204.00); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    public static BigDecimal MEANS_LIMITS_INCOME_PART_COUPLE = new BigDecimal(
            2010.50); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    public static BigDecimal MEANS_LIMITS_INCOME_PART_SINGLE_ONE_CHILD = new BigDecimal(
            1228.60); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    public static BigDecimal MEANS_LIMITS_INCOME_PART_CHILDREN = new BigDecimal(
            2281.50);

    public static BigDecimal MEANS_LIMITS_INCOME_PART_ILLNESS_SEPARATED_COUPLE = new BigDecimal(
            2380.00); // 5. December 2002 -
                        // http://www.centrelink.gov.au/internet/internet.nsf/payments/chartc.htm

    public static BigDecimal PENSIONER_REBATE_SINGLE = new BigDecimal(1358.);

    public static BigDecimal PENSIONER_REBATE_COUPLE = new BigDecimal(980.);

    public static BigDecimal PENSIONER_REBATE_SEPARATED_HEALTH = new BigDecimal(
            1296.);

    public static BigDecimal PENSIONER_REBATE_SINGLE_THRESHOLD = new BigDecimal(
            12190.);

    public static BigDecimal PENSIONER_REBATE_COUPLE_THRESHOLD = new BigDecimal(
            10300.);

    public static BigDecimal PENSIONER_REBATE_SEPARATED_HEALTH_THRESHOLD = new BigDecimal(
            11880.);

    public static BigDecimal PENSIONER_REBATE_REDUCTION_FACTOR = new BigDecimal(
            .125);

    // Newstart Allowance
    public static String BASIC_CONDITIONS_OF_ELIGIBILITY_NEWSTART_ALLOWANCE = "  Must be unemployed, capable of undertaking, available \n"
            + " for and actively seeking work or temporarily \n"
            + " incapacitated for work. \n"
            + "  Aged 21 or over but under Age Pension age and \n"
            + " registered as unemployed. \n"
            + "  May do training and voluntary work with approval. \n"
            + "  Willing to enter into a Preparing for Work Agreement \n"
            + " if required, allowing participation in a broad range of \n"
            + " activities. \n"
            + "  NSA recipients incapacitated for work remain on NSA, \n"
            + " subject to medical certificates. \n";

    public static BigDecimal MAX_SINGLE_NO_CHILDREN_NSA = new BigDecimal(364.60);

    public static BigDecimal MAX_SINGLE_CHILDREN_NSA = new BigDecimal(394.30);

    public static BigDecimal MAX_SINGLE_SIXTY_OR_OVER_NSA = new BigDecimal(
            394.30);

    public static BigDecimal MAX_PARTNERED_NSA = new BigDecimal(328.90);

    public static BigDecimal MEANS_LIMITS_INCOME_SINGLE_TWENTYFIRST_OR_OVER_NSA = new BigDecimal(
            62.);

    public static BigDecimal MEANS_LIMITS_INCOME_SINGLE_SIXTY_OR_OVER_NSA = new BigDecimal(
            62.);

    public static BigDecimal MEANS_LIMITS_INCOME_EIGHTEEN_MORE_CHILDREN_NSA = new BigDecimal(
            62.);

    public static BigDecimal MEANS_LIMITS_INCOME_PARTNER_NSA = new BigDecimal(
            62.);

    public static BigDecimal MEANS_LIMITS_INCOME_PART_SINGLE_TWENTYFIRST_OR_OVER_NSA = new BigDecimal(
            605.71);

    public static BigDecimal MEANS_LIMITS_INCOME_PART_SINGLE_SIXTY_OR_OVER_NSA = new BigDecimal(
            648.14);

    public static BigDecimal MEANS_LIMITS_INCOME_PART_EIGHTEEN_MORE_CHILDREN_NSA = new BigDecimal(
            648.14);

    public static BigDecimal MEANS_LIMITS_INCOME_PART_PARTNER_NSA = new BigDecimal(
            554.71);

    public static BigDecimal MEANS_LIMITS_INCOME_FIRST_FACTOR_NSA = new BigDecimal(
            .50);

    public static BigDecimal MEANS_LIMITS_INCOME_SECOND_FACTOR_NSA = new BigDecimal(
            .70);

    public static BigDecimal MEANS_LIMITS_INCOME_LOW_NSA = new BigDecimal(62.);

    public static BigDecimal MEANS_LIMITS_INCOME_HIGH_NSA = new BigDecimal(142.);

    public static BigDecimal MEANS_LIMITS_TWENTY_ONE_NSA = new BigDecimal(21);

    public static BigDecimal MEANS_LIMITS_SIXTY_NSA = new BigDecimal(60);

    public static BigDecimal MEANS_LIMITS_EIGHTEEN_NSA = new BigDecimal(18);

    // per week (default)
    static boolean NO_PAYG = true;

    private TaxUtils() {}
    
    public static double getPAYG(double taxableIncome) {
        // if ( NO_PAYG ) return 0;

        if (taxableIncome < 110)
            return 0;
        else if (taxableIncome < 261)
            return taxableIncome * 0.1728 - 19.04;
        else if (taxableIncome < 282)
            return taxableIncome * 0.376 - 72.1194;
        else if (taxableIncome < 378)
            return taxableIncome * 0.188 - 19.0311;
        else if (taxableIncome < 966)
            return taxableIncome * 0.315 - 67.0957;
        else if (taxableIncome < 1147)
            return taxableIncome * 0.435 - 181.7419;
        else
            return taxableIncome * 0.485 - 239.1265;
    }

    public static double getPAYG(java.math.BigDecimal taxableIncome) {
        return taxableIncome == null ? Double.NaN : getPAYG(taxableIncome
                .doubleValue());
    }

    /**
     * Tax Rates Post June 1983 Under Thresh 0.00% Post June 1983 Over Thresh
     * 16.50%
     */
    public static double getTaxRatePost061983UnderThreshold(int age) {
        return age < TAX_EFFECTIVE_AGE ? .215 : 0;
    }

    public static double getTaxRatePost061983OverThreshold(int age) {
        return age < TAX_EFFECTIVE_AGE ? .215 : .165;
    }

    /*
     * // If the client is < 55 or > 55 over the threshold the
     * Post061983AmountUntaxed component should be taxed at 31.5%. public static
     * double getTaxRatePost061983OverThreshold( int age ) { return age <
     * TAX_EFFECTIVE_AGE ? .215 : .165; }
     *  // If the client is > 55 and within the threshold the
     * Post061983AmountUntaxed component should be taxed at 16.5%. public static
     * double getTaxRatePost061983UnderThreshold( int age ) { return age <
     * TAX_EFFECTIVE_AGE ? .215 : 0; }
     */

}
