/*
 * DSSParams.java
 *
 * Created on April 29, 2003, 11:30 AM
 */

package com.argus.financials.projection.dss;

import com.argus.financials.projection.DocumentNames;

/**
 * 
 * @author shibaevv
 */
public class DSSParams implements DSSConstants, DocumentNames {

    private boolean single;

    private boolean homeOwner;

    private boolean illSept;

    private double children;

    private double deemingThreshold;

    private double deemingRate;

    private double assetThreshold;

    private double assetRate;

    private double agePension;

    private double newStartAllowance;

    private double pensionIncomeThreshold;

    private double pensionIncomeRate;

    private double phar;

    /** Creates a new instance of DSSParams */
    public DSSParams(boolean single, boolean homeOwner, boolean illSept,
            double children) {
        this.single = single;
        this.homeOwner = homeOwner;
        this.illSept = illSept;
        this.children = children;
        setParams();
    }

    public void setParams() {
        assetRate = ASSET_RATE;
        pensionIncomeRate = PENSION_INCOME_RATE;
        phar = PHAR_AMOUNT;

        if (single) {
            deemingThreshold = DEEMING_SINGLE;

            agePension = AGE_PENSION_SINGLE;
            if (homeOwner)
                assetThreshold = SINGLE_HOME_ASSET;
            else
                assetThreshold = SINGLE_NON_HOME_ASSET;

            pensionIncomeThreshold = PENSION_INCOME_SINGLE
                    + PENSION_INCOME_CHILD * children;

            if (children > 0)
                newStartAllowance = NSA_SINGLE_CHILDREN;
            else
                newStartAllowance = NSA_SINGLE;

        } else {
            deemingThreshold = DEEMING_COUPLE;
            if (homeOwner)
                assetThreshold = COUPLE_HOME_ASSET;
            else
                assetThreshold = COUPLE_NON_HOME_ASSET;

            if (illSept)
                agePension = AGE_PENSION_SINGLE;
            else
                agePension = AGE_PENSION_COUPLE;

            pensionIncomeThreshold = PENSION_INCOME_COUPLE
                    + PENSION_INCOME_CHILD * children;

            newStartAllowance = NSA_COUPLE;
        }

    }

    public double getAssetThreshold() {
        return assetThreshold;
    }

    public double getAssetRate() {
        return assetRate;
    }

    public double maxBenefit(String type) {
        if (AGE_PENSION.equals(type))
            return agePension;
        if (NEW_START_ALLOWANCE.equals(type))
            return newStartAllowance;

        return 0.;
    }

    public double getPensionIncomeThreshold() {
        return pensionIncomeThreshold;
    }

    public double getPensionIncomeRate() {
        return pensionIncomeRate;
    }

    public double getPhar() {
        return phar;
    }

    // =IF(TotalAssetsSubjectToDeeming<=LowerDeemingRatesCouple,C37*J32,((C37-I32)*J33)+K33)

    public double getDeemedIncome(double totalDeemedAsset, boolean half) {
        double threshold = half ? deemingThreshold / 2 : deemingThreshold;

        if (totalDeemedAsset <= threshold)
            return totalDeemedAsset * DEEMING_LOWER_RATE;

        return (totalDeemedAsset - threshold) * DEEMING_HIGHER_RATE + threshold
                * DEEMING_LOWER_RATE;

    }
}
