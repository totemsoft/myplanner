/*
 * Assets.java
 *
 * Created on August 9, 2002, 1:52 PM
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class Assets extends Financials {

    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.financial.Assets

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
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
