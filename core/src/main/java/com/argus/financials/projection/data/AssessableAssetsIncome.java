package com.argus.financials.projection.data;

import java.math.BigDecimal;

/**
 * Stores assessable assets and their income.
 */
public class AssessableAssetsIncome {
    // assets
    public BigDecimal homeContentsA = new BigDecimal(0.0); // Home Contents

    public BigDecimal carsEtcA = new BigDecimal(0.0); // Cars/Caravans/Boats

    public BigDecimal propertyA = new BigDecimal(0.0); // Investment Property

    public BigDecimal savingsA = new BigDecimal(0.0); // Savings

    public BigDecimal managedFundsA = new BigDecimal(0.0); // Managed Funds

    public BigDecimal sharesA = new BigDecimal(0.0); // Shares/Derivates

    public BigDecimal bondsA = new BigDecimal(0.0); // Bonds/Debentures

    public BigDecimal interestA = new BigDecimal(0.0); // Fixed Interest

    public BigDecimal giftsA = new BigDecimal(0.0); // Gifts Over $10,000

    public BigDecimal loansA = new BigDecimal(0.0); // Loans Owed

    public BigDecimal superannuationA = new BigDecimal(0.0); // Superannuation

    public BigDecimal allocatedPensionA = new BigDecimal(0.0); // Allocated
                                                                // Pension

    public BigDecimal complyingPensionA = new BigDecimal(0.0); // Complying
                                                                // Pension

    // income
    // public BigDecimal homeContentsI = new BigDecimal(0.0); // Home Contents
    // Income
    // public BigDecimal carsEtcI = new BigDecimal(0.0); // Cars/Caravans/Boats
    // Income
    public BigDecimal propertyI = new BigDecimal(0.0); // Investment Property
                                                        // Income

    public BigDecimal savingsI = new BigDecimal(0.0); // Savings Income

    public BigDecimal managedFundsI = new BigDecimal(0.0); // Managed Funds
                                                            // Income

    public BigDecimal sharesI = new BigDecimal(0.0); // Shares/Derivates
                                                        // Income

    public BigDecimal bondsI = new BigDecimal(0.0); // Bonds/Debentures Income

    public BigDecimal interestI = new BigDecimal(0.0); // Fixed Interest Income

    public BigDecimal giftsI = new BigDecimal(0.0); // Gifts Over $10,000 Income

    public BigDecimal loansI = new BigDecimal(0.0); // Loans Owed Income

    public BigDecimal superannuationI = new BigDecimal(0.0); // Superannuation
                                                                // Income

    public BigDecimal allocatedPensionI = new BigDecimal(0.0); // Allocated
                                                                // Pension
                                                                // Income

    public BigDecimal complyingPensionI = new BigDecimal(0.0); // Complying
                                                                // Pension
                                                                // Income

    public BigDecimal pensionI = new BigDecimal(0.0); // Allocated + Complying
                                                        // Pension Income

    public BigDecimal salaryWagesI = new BigDecimal(0.0); // Salary/Wages
                                                            // Income

    public AssessableAssetsIncome() {
    }

    public AssessableAssetsIncome(AssessableAssetsIncome assi) {
        // assets
        this.homeContentsA = assi.homeContentsA; // Home Contents
        this.carsEtcA = assi.carsEtcA; // Cars/Caravans/Boats
        this.propertyA = assi.propertyA; // Investment Property
        this.savingsA = assi.savingsA; // Savings
        this.managedFundsA = assi.managedFundsA; // Managed Funds
        this.sharesA = assi.sharesA; // Shares/Derivates
        this.bondsA = assi.bondsA; // Bonds/Debentures
        this.interestA = assi.interestA; // Fixed Interest
        this.giftsA = assi.giftsA; // Gifts Over $10,000
        this.loansA = assi.loansA; // Loans Owed
        this.superannuationA = assi.superannuationA; // Superannuation
        this.allocatedPensionA = assi.allocatedPensionA; // Allocated Pension
        this.complyingPensionA = assi.complyingPensionA; // Complying Pension
        // income
        // this.homeContentsI = assi.homeContentsI; // Home Contents Income
        // this.carsEtcI = assi.carsEtcI; // Cars/Caravans/Boats Income
        this.propertyI = assi.propertyI; // Investment Property Income
        this.savingsI = assi.savingsI; // Savings Income
        this.managedFundsI = assi.managedFundsI; // Managed Funds Income
        this.sharesI = assi.sharesI; // Shares/Derivates Income
        this.bondsI = assi.bondsI; // Bonds/Debentures Income
        this.interestI = assi.interestI; // Fixed Interest Income
        this.giftsI = assi.giftsI; // Gifts Over $10,000 Income
        this.loansI = assi.loansI; // Loans Owed Income
        this.superannuationI = assi.superannuationI; // Superannuation Income
        this.allocatedPensionI = assi.allocatedPensionI; // Allocated Pension
                                                            // Income
        this.complyingPensionI = assi.complyingPensionI; // Complying Pension
                                                            // Income
        this.pensionI = assi.pensionI; // Allocated + Complying Pension Income
        this.salaryWagesI = assi.salaryWagesI; // Salary/Wages Income
    }

}
