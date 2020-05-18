/*
 * TaxableElement.java
 *
 * Created on 4 September 2002, 17:00
 */

package com.argus.financials.tax.au;

/**
 * 
 * @version
 */
class TaxElement {

    private String type = "";

    private double amount = 0;

    private double taxWithHeld = 0;

    private double taxRate = 0.00;

    private double medicareLevyRate = 0.00;

    private double medicareLevySurchargeRate = 0.00;

    /** Creates new TaxableElement */
    TaxElement() {
    }

    public String getType() {
        return this.type;
    }

    public double getTaxWithHeld() {
        return this.taxWithHeld;
    }

    public double getAmount() {
        return this.amount;
    }

    public double getTaxRate() {
        return this.taxRate;
    }

    public double getMedicareLevyRate() {
        return this.medicareLevyRate;
    }

    public double getMedicareLevySurchargeRate() {
        return this.medicareLevySurchargeRate;
    }

    public void setType(String value) {
        this.type = value;
    }

    public void setTaxWithHeld(double value) {
        this.taxWithHeld = value;
    }

    public void setAmount(double value) {
        this.amount = value;
    }

    public void setTaxRate(double value) {
        this.taxRate = value;
    }

    public void setMedicareLevyRate(double value) {
        this.medicareLevyRate = value;
    }

    public void setMedicareLevySurchargeRate(double value) {
        this.medicareLevySurchargeRate = value;
    }

    public String toString() {
        return getType() + " : { " + "Amount=" + getAmount() + ", "
                + "TaxRate=" + getTaxRate() + ", " + "TaxWithHeld="
                + getTaxWithHeld() + ", " + "MedicareLevyRate="
                + getMedicareLevyRate() + ", " + "MedicareLevySurchargeRate="
                + getMedicareLevySurchargeRate() + "} ";

    }
}
