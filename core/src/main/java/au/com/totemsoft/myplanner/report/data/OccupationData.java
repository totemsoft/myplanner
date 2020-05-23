/*
 * BusinessData.java
 *
 * Created on 17 October 2002, 10:59
 */

package au.com.totemsoft.myplanner.report.data;

import au.com.totemsoft.myplanner.api.bean.IOccupation;
import au.com.totemsoft.myplanner.api.bean.IPerson;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class OccupationData extends BaseData {

    public String EmploymentStatus = STRING_EMPTY;

    public String OccupationName = STRING_EMPTY;

    protected void clear() {
        EmploymentStatus = STRING_EMPTY;
        OccupationName = STRING_EMPTY;
    }

    public void init(au.com.totemsoft.myplanner.service.PersonService person) throws Exception
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