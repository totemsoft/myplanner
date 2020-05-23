/*
 * DSSElement.java
 *
 * Created on 8 May 2003, 11:28
 */

package au.com.totemsoft.myplanner.projection.dss;

import au.com.totemsoft.util.ReferenceCode;

public class DSSElement extends java.util.Vector {

    static final long serialVersionUID = 8557215418894298824L;

    /** Creates a new instance of DSSElement */
    public DSSElement() {
        this("", ReferenceCode.CODE_NONE, 0, 0., 0., 0., 0.);
    }

    public DSSElement(String name, ReferenceCode type, int owner,
            double actualAsset, double testAsset, double actualIncome,
            double testIncome) {
        super();

        add(name);
        add(type);
        add(new Integer(owner));
        add(new Double(actualAsset));
        add(new Double(testAsset));
        add(new Double(actualIncome));
        add(new Double(testIncome));
    }

    /*
     * public String getType() { return (String) get(
     * NonDeemedAssetTableModel.TYPE ); } public void setType(String value) {
     * setElementAt( value, NonDeemedAssetTableModel.TYPE ); }
     */
    public double getAmount(int index) {
        return ((Double) get(index)).doubleValue();
    }

    public void setAmount(int index, double value) {
        setElementAt(new Double(value), index);
    }

    public void setAmount(int index, Double value) {
        setElementAt(value, index);
    }

}
