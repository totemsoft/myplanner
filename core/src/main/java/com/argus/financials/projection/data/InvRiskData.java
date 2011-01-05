/*
 * InvRiskData.java
 *
 * Created on 18 September 2002, 09:30
 */

package com.argus.financials.projection.data;

/**
 * 
 * @author shibaevv
 * @version
 */
public class InvRiskData {
    public static final String STRING_ZERO = new String("0.00");

    public static final String STRING_EMPTY = new String("");

    public Client client;

    public Question question;

    public Answer answer;

    public RiskRating rr;

    public RecommendedAssetAllocation raa;

    /** Creates new DSSData */
    public InvRiskData() {
        client = new Client();
        question = new Question();
        answer = new Answer();
        rr = new RiskRating();
        raa = new RecommendedAssetAllocation();
    }

    /*
     * ============================ BEGIN: ClientView data
     * ============================
     */
    public class Client implements java.io.Serializable {

        public Person person = new Person();

        public class Person implements java.io.Serializable {
            public String fullName; // ClientView.Person.FullName
        }
    }

    /*
     * ============================ END: ClientView data
     * ============================
     */

    /*
     * ============================ BEGIN: Question data
     * ============================
     */
    public class Question implements java.io.Serializable {
        public String no_0;

        public String no_1;

        public String no_2;

        public String no_3;

        public String no_4;

        public String no_5;

        public String no_6;

        public String no_7;

        public String no_8;

        public String no_9;

        public String no_10;

        public String no_11;

        public String no_12;

        public String no_13;

        public String no_14;

        public String no_15;

        public String no_16;

        public String no_17;

        public String no_18;

        public String no_19;

        public String no_20;
    }

    /*
     * ============================ END: Question data
     * ============================
     */

    /*
     * ============================ BEGIN: Question data
     * ============================
     */
    public class Answer implements java.io.Serializable {
        public String no_0;

        public String no_1;

        public String no_2;

        public String no_3;

        public String no_4;

        public String no_5;

        public String no_6;

        public String no_7;

        public String no_8;

        public String no_9;

        public String no_10;

        public String no_11;

        public String no_12;

        public String no_13;

        public String no_14;

        public String no_15;

        public String no_16;

        public String no_17;

        public String no_18;

        public String no_19;
    }

    /*
     * ============================ END: Question data
     * ============================
     */

    /*
     * ============================ BEGIN: RiskRating data
     * ============================
     */
    public class RiskRating implements java.io.Serializable {
        public String riskProfileRating;

        public String incomeReturn;

        public String capitalGrowthReturn;

        public String totalReturn;

        public String defensiveAsset;

        public String growthAsset;

        public String rangeOfOutcomes_1yr;

        public String rangeOfOutcomes_in_2_out_of_3_Years;
    }

    /*
     * ============================ END: RiskRating data
     * ============================
     */

    /*
     * ============================ BEGIN: RecommendedAssetAllocation data
     * ============================
     */
    public class RecommendedAssetAllocation implements java.io.Serializable {
        public String cash;

        public String fixedInterest;

        public String listedProperty;

        public String australianShares;

        public String internationalShares;

        public String total;
    }
    /*
     * ============================ END: RecommendedAssetAllocation data
     * ============================
     */

}
