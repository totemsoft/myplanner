/*
 * BusinessData.java
 *
 * Created on 17 October 2002, 10:59
 */

package com.argus.financials.report.data;

import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.bean.IPerson;

/**
 * 
 * @author valeri chibaev
 */

public class OccupationData extends BaseData {

    public String EmploymentStatus = STRING_EMPTY;

    public String OccupationName = STRING_EMPTY;

    protected void clear() {
        EmploymentStatus = STRING_EMPTY;
        OccupationName = STRING_EMPTY;
    }

    public void init(com.argus.financials.service.PersonService person) throws Exception
    {
        super.init(person);
        if (person == null)
            return;

        IPerson personName = person == null ? null : person.getPersonName();
        IOccupation o = personName == null ? null : personName.getOccupation();
        if (o == null) {
            clear();
        } else {
            EmploymentStatus = o.getEmploymentStatus();
            OccupationName = o.getOccupation() == null ? STRING_EMPTY : o.getOccupation();
        }

    }

}