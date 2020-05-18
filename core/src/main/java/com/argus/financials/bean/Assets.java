/*
 * Assets.java
 *
 * Created on August 9, 2002, 1:52 PM
 */

package com.argus.financials.bean;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class Assets extends Financials {

    static final long serialVersionUID = 7814790254001000658L;

    transient private static Assets current = new Assets();

    public static Assets getCurrentAssets() {
        return current;
    }

    /** Creates new Assets collection */
    public Assets() {
        this(null);
    }

    public Assets(java.util.Map financials) {
        super(financials);
    }

    public void initCodes(java.util.Map financials) {

        super.initCodes(financials);

        if (financials == null)
            return;

        for (int i = 0; i < ASSETS.length; i++)
            initCodes(financials, ASSETS[i]);

    }

    public void addToAssets(Asset a) {
        getCodes().add(a);
    }

    public void removeFromAssets(Asset a) {
        getCodes().remove(a);
    }

}
