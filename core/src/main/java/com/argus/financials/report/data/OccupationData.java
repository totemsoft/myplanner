/*
 * BusinessData.java
 *
 * Created on 17 October 2002, 10:59
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 */

public class OccupationData extends BaseData {

    public String EmploymentStatus = STRING_EMPTY;

    public String OccupationName = STRING_EMPTY;

    /** Creates a new instance of OccupationData */
    public OccupationData() {
    }

    protected void clear() {
        EmploymentStatus = STRING_EMPTY;
        OccupationName = STRING_EMPTY;
    }

    public void init(com.argus.financials.service.PersonService person)
            throws Exception
    {

        super.init(person);
        if (person == null)
            return;

        com.argus.financials.etc.Occupation o = person.getOccupation();
        if (o == null) {
            clear();

        } else {
            EmploymentStatus = o.getEmploymentStatus();
            OccupationName = o.getOccupation() == null ? STRING_EMPTY : o
                    .getOccupation();
        }

    }

}
