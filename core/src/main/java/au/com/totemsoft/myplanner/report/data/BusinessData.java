/*
 * BusinessData.java
 *
 * Created on 17 October 2002, 10:59
 */

package au.com.totemsoft.myplanner.report.data;

import au.com.totemsoft.myplanner.api.bean.IBusiness;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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

    public void init(au.com.totemsoft.myplanner.service.PersonService person)
            throws Exception
    {

        super.init(person);
        if (person == null)
            return;

        IBusiness b = person.getEmployerBusiness();
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
