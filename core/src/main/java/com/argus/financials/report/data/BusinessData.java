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

public class BusinessData extends BaseData {

    public String LegalName = STRING_EMPTY;

    public String TradingName = STRING_EMPTY;

    /** Creates a new instance of BusinessData */
    public BusinessData() {
    }

    protected void clear() {
        LegalName = STRING_EMPTY;
        TradingName = STRING_EMPTY;
    }

    public void init(com.argus.financials.service.PersonService person)
            throws java.io.IOException// , com.argus.financials.service.ServiceException,
    {

        super.init(person);
        if (person == null)
            return;

        com.argus.financials.service.BusinessService b = person.getEmployerBusiness();
        if (b == null) {
            clear();

        } else {
            LegalName = b.getLegalName() == null ? STRING_EMPTY : b
                    .getLegalName();
            TradingName = b.getTradingName() == null ? STRING_EMPTY : b
                    .getTradingName();
        }

    }

}
