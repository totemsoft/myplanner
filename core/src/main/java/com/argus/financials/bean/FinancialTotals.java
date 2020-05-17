/*
 * StrategyTotal.java
 *
 * Created on 22 April 2002, 16:47
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class FinancialTotals implements java.io.Serializable {

    static final long serialVersionUID = -7982249500608673193L;

    private java.math.BigDecimal[] totals;

    /** Creates new FinancialTotals */
    public FinancialTotals(int numberOfTotals) {
        totals = new java.math.BigDecimal[numberOfTotals];
    }

    public String toString() {
        return "Total";// totals.toString();
    }

    public String toHTMLString() {
        return "<html>" + this.toString() + "</html>";
    }

    public String dump() {
        String buf = "";
        for (int i = 0; i < totals.length; i++)
            buf += "[" + i + "]=" + totals[i] + ";";
        return buf;
    }

    public java.math.BigDecimal getTotal(int index) {
        return totals[index];
    }

    public void setTotal(int index, java.math.BigDecimal value) {
        totals[index] = value;
    }

}
