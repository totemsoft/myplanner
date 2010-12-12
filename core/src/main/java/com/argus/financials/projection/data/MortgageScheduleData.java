/*
 * ScheduleData.java
 *
 * Created on 29 May 2002, 22:06
 */

package com.argus.financials.projection.data;

/**
 * This class is based on the Value Object pattern, it contains the monthly
 * payment schedule of a Mortgage.
 * 
 * @author thomass
 * @version
 */
public class MortgageScheduleData {

    public Integer month;

    public Double openingBalance;

    public Double closingBalance;

    public Double interestPayment;

    public Double capitalPayment;

    /** Creates new ScheduleData */
    public MortgageScheduleData(int month, double openingBalance,
            double closingBalance, double interestPayment, double capitalPayment) {
        this.month = new Integer(month);
        this.openingBalance = new Double(openingBalance);
        this.closingBalance = new Double(closingBalance);
        this.interestPayment = new Double(interestPayment);
        this.capitalPayment = new Double(capitalPayment);

    }

    public String toString() {
        return "{ " + month + ", " + openingBalance + ", " + closingBalance
                + ", " + interestPayment + ", " + capitalPayment + " }\r\n";
    }

}
