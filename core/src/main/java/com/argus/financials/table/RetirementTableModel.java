/*
 * RetirementTableModel.java
 *
 * Created on 5 December 2002, 12:52
 */

package com.argus.financials.table;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.util.Vector;

import com.argus.financials.bean.Assumptions;
import com.argus.financials.swing.table.ProxyTableModel;
import com.argus.math.Numeric;
import com.argus.util.DateTimeUtils;

public class RetirementTableModel extends FinancialTableModel {

    private Assumptions assumptions;

    private double[] retirement;

    private double[] lifeExpectancy;

    private ProxyTableModel proxy;

    public ProxyTableModel getProxy() {
        if (proxy == null)
            proxy = new ProxyTableModel(this, getDisplayColumns());
        return proxy;
    }

    /** Creates a new instance of RetirementTableModel */
    public RetirementTableModel(double total, Assumptions assumptions) {
        super();

        this.assumptions = assumptions;

        int length = assumptions.getYearsMapRetirement().length;

        setColumnNames(getColumnNames(length));
        setColumnClasses(getColumnClasses(length));
        setData(initData(total, length)); // last !!!
    }

    public int[] getDisplayColumns() {
        int[] displayColumns = assumptions.getYearsMapRetirement(assumptions
                .getRetirementDate(), assumptions.getDisplayModeRetirement());
        int[] displayColumns2 = new int[displayColumns.length + 1];

        displayColumns2[0] = 0; // first column = name, others = years
        for (int i = 0; i < displayColumns.length; i++) {
            displayColumns2[i + 1] = displayColumns[i] + 1;
        }

        return displayColumns2;
    }

    private Vector getColumnNames(int length) {

        Vector names = new Vector();

        java.util.Date date = assumptions.getRetirementDate();
        int retirementYear = date == null || length == 0 ? 0 : DateTimeUtils
                .getFinancialYearEnd(date);

        int mode = assumptions.getShowTermMode();

        names.add("Title");
        for (int i = 0; i < length; i++) {
            /**
             * Changes made for tasks id 10004 & 10007
             * 
             */
            long age = java.lang.Math.round(assumptions
                    .getClientAgeAt(DateTimeUtils.getDate("30/6/"
                            + (retirementYear))));
            int year = retirementYear++;
            String title;
            if (mode == 0) {
                title = (age >= 1 ? "" + age : "");
            } else {
                title = "June " + year;
            }

            names.add(title);
        }
        return names;

    }

    private Vector getColumnClasses(int length) {

        Vector classes = new Vector();

        classes.add(java.lang.String.class);
        for (int i = 0; i < length; i++)
            classes.add(java.math.BigDecimal.class);

        return classes;

    }

    // total already includes inflation/indexations
    private Vector initData(double total, int length) {

        Vector data = new Vector();
        if (length <= 0)
            length = 0;

        double inflation = getInflation();
        if (inflation < 0.)
            inflation = 0.;

        double[] drawdown = new double[length];
        retirement = new double[length];
        lifeExpectancy = new double[length];

        double factor = (100. + assumptions.getTotalRate() - inflation) / 100.;
        double valueAtRetirement = total
                - assumptions.getLumpSumRequiredAtRetirement();
        double incomeRequired = assumptions.getRetirementIncomeAtRetirement();

        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < length; i++) {
            // Inflated Income
            drawdown[i] = incomeRequired;
            // Value available at retirment
            retirement[i] = valueAtRetirement;

            maxValue = java.lang.Math.max(maxValue, valueAtRetirement);
            lifeExpectancy[i] = 0.;
            valueAtRetirement = (valueAtRetirement - incomeRequired) * factor;
            incomeRequired *= (100. + inflation) / 100.;
            if (valueAtRetirement <= 0.) {
                valueAtRetirement = 0.;
                incomeRequired = 0.;
            }

        }

        /**
         * Changes for task id = 10004
         * 
         */
        double ageAtRetiremet = assumptions.getClientAgeAtRetirement();
        double ageAtTheEnd = assumptions.getClientAgeAtYearsToProject();
        double ageToday = assumptions.getClientAgeAt(new java.util.Date());
        // int age = (int) java.lang.Math.round(ageToday);

        int age = (int) java.lang.Math.round(ageAtRetiremet);

        double ageAsExcpected = com.argus.financials.projection.data.LifeExpectancy
                .getValue(age, assumptions.getClientSex());
        // Calculate data for Life Expectancy line

        java.util.Date date = assumptions.getRetirementDate();
        int retirementYear = date == null || length == 0 ? 0 : DateTimeUtils
                .getFinancialYearEnd(date);
        age = (int) java.lang.Math.floor(assumptions
                .getClientAgeAt(DateTimeUtils.getDate("30/6/"
                        + (retirementYear))));

        // if (ageToday + ageAsExcpected > ageAtRetiremet && ageToday +
        // ageAsExcpected <= ageAtTheEnd ) {
        if (ageAtRetiremet + ageAsExcpected <= ageAtTheEnd && age > 0) {
            // Calculate index in life expectancy array

            for (int i = 0; i < length; i++) {
                // if (age > ageToday + ageAsExcpected ){
                if (age > ageAtRetiremet + ageAsExcpected) {
                    if (i == 0)
                        lifeExpectancy[i] = maxValue - maxValue / 10.;
                    else
                        lifeExpectancy[i - 1] = maxValue - maxValue / 10.;

                    break;
                }
                age = age + 1;
            }
            // System.out.println("You will be alive !!!!!!") ;

        }

        data.add(new RetirementRow("Retirement Income", drawdown));
        data.add(new RetirementRow("Capital Value", retirement));

        return data;

    }

    protected Numeric getNumeric(int columnIndex) {
        return money;
    }

    public Vector getColumnNames() {
        Vector names = new Vector(super.getColumnNames());
        names.remove(0); // no title
        return names;
    }

    public double[] getRetirementValues() {
        return retirement == null ? new double[0] : retirement;
    }

    public double[] getLifeExpectancyValues() {
        return lifeExpectancy == null ? new double[0] : lifeExpectancy;
    }

    public double getInflation() {
        return assumptions.getInflation();
    }

    public class RetirementRow extends AbstractSmartTableRow {

        private String title;

        private double[] data;

        /** Creates a new instance of FinancialRow */
        public RetirementRow(String title, double[] data) {
            super(BODY);

            this.title = title;
            this.data = data;
        }

        public String toString() {
            return title;
        }

        public Object getValueAt(int columnIndex) {
            if (columnIndex == getPrimaryColumnIndex())
                return title;
            return getNumeric(columnIndex).setValue(data[columnIndex - 1]);

        }

    }

}
