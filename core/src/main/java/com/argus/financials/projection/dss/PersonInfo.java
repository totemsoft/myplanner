/*
 * PersonInfo.java
 *
 * Created on May 2, 2003, 9:51 AM
 */

package com.argus.financials.projection.dss;

import java.util.Date;

import com.argus.util.DateTimeUtils;

/**
 * 
 * @author shibaevv
 */
public class PersonInfo {

    public String name;

    public Date dob;

    public String sex;

    public Date calcDate;

    public String marital;

    public String homeOwner;

    public double childrenNo;

    /** Creates new ScheduleData */
    public PersonInfo(String name, Date dob, String sex, Date calcDate,
            String marital, String homeOwner, double childrenNo) {
        this.name = name;
        this.dob = dob;
        this.sex = sex;
        this.calcDate = calcDate;
        this.marital = marital;
        this.homeOwner = homeOwner;
        this.childrenNo = childrenNo;

    }

    public int age() {

        Double d = DateTimeUtils.getAgeDouble(calcDate, dob);
        if (d != null)
            return d.intValue();
        return 0;

    }

    public boolean isSingle() {
        return "Single".equals(marital) || "Divorced".equals(marital)
                || "Separated".equals(marital) || "Widowed".equals(marital);
    }

    public boolean isHomeOwner() {
        return "Yes".equals(homeOwner);
    }

    public boolean isIllSept() {
        return "Separated-Health".equals(marital);
    }

    public boolean isPartnered() {
        return "Separated-Health".equals(marital) || "Married".equals(marital)
                || "De-facto".equals(marital) || "Partnered".equals(marital);
    }

    public String toString() {
        return "{ " + name + ", " + dob + ", " + sex + ", " + calcDate + ", "
                + marital + homeOwner + childrenNo + " }\r\n";
    }

}
