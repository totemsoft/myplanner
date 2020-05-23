/*
 * Liabilities.java
 *
 * Created on August 9, 2002, 1:53 PM
 */

package au.com.totemsoft.myplanner.bean;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class Liabilities extends Financials {

    /** Creates new Liabilities collection */
    public Liabilities() {
        super();
    }

    public Liabilities(java.util.Map details) {
        super(details);
    }

    public void initCodes(java.util.Map details) {

        super.initCodes(details);

        if (details == null)
            return;

        initCodes(details, LIABILITY);
    }

}
