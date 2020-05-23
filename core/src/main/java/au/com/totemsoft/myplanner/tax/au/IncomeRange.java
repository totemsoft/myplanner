/*
 * IncomeRange.java
 *
 * Created on 4 September 2002, 16:01
 */

package au.com.totemsoft.myplanner.tax.au;

/**
 * 
 * @version
 */
class IncomeRange {

    private double rangeStart = 0.00;

    private double rangeEnd = 0.00;

    private double taxRate = 0.00;

    private double medicareLevyRate = 0.00;

    private double medicareLevySurchargeRate = 0.00;

    /** Creates new IncomeRange */
    IncomeRange(double rangeStart, double rangeEnd, double taxRate,
            double medicareLevyRate, double medicareLevySurchargeRate) {

        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.taxRate = taxRate;
        this.medicareLevyRate = medicareLevyRate;
        this.medicareLevySurchargeRate = medicareLevySurchargeRate;
    }

    public double getRangeStart() {
        return this.rangeStart;
    }

    public double getRangeEnd() {
        return this.rangeEnd;
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

    public double getRangeScale() {
        return this.rangeEnd - this.rangeStart;
    }

}
