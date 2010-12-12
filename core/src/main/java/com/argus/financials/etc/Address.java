/*
 * Address.java
 *
 * Created on 20 August 2001, 12:41
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.code.CountryCode;
import com.argus.financials.code.StateCode;

public class Address extends FPSAssignableObject {

    public static final String STATE = "StateCodeID";

    public static final String COUNTRY = "CountryCodeID";

    public static final String POSTCODE = "PostCode";

    private Integer addressCodeID;

    private Integer parentAddressID;

    private String streetNumber;

    private String streetNumber2;

    private String suburb;

    private Integer postCodeID;

    private String state;

    // private Integer stateCodeID;
    private Integer countryCodeID;

    public Address() {
    }

    public Address(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public String toString() {
        return (streetNumber == null ? "" : streetNumber + "\n")
                + (streetNumber2 == null ? "" : streetNumber2 + "\n") + suburb
                + "  " + getState() + "  " + postCodeID;
    }

    public String getFullAddress() {

        return (streetNumber == null ? "" : streetNumber) + " "
                + (streetNumber2 == null ? "" : streetNumber2) + " "
                + (suburb == null ? "" : suburb) + " "
                + (state == null ? "" : state) + " "
                + (postCodeID == null ? "" : "" + postCodeID);
    }

    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Address))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        addressCodeID = ((Address) value).addressCodeID;
        streetNumber = ((Address) value).streetNumber;
        streetNumber2 = ((Address) value).streetNumber2;
        suburb = ((Address) value).suburb;
        postCodeID = ((Address) value).postCodeID;
        state = ((Address) value).state;
        // stateCodeID = ( (Address) value ).stateCodeID;
        countryCodeID = ((Address) value).countryCodeID;
        parentAddressID = ((Address) value).parentAddressID;

        // has to be last (to set modified)
        setModified(true);
    }

    /**
     * helper methods
     */
    protected void clear() {
        super.clear();

        addressCodeID = null;
        streetNumber = null;
        streetNumber2 = null;
        suburb = null;
        postCodeID = null;
        state = null;
        // stateCodeID = null;
        countryCodeID = null;
        parentAddressID = null;
    }

    public boolean isClear() {
        if (countryCodeID == null && streetNumber == null
                && streetNumber2 == null && suburb == null
                && postCodeID == null && state == null
                // && stateCodeID == null
                && parentAddressID == null)
            return true;
        return false;
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        if (changeEvent.getSource() instanceof Address) {
            Address parentAddress = (Address) changeEvent.getSource();

            if (!equals(getParentAddressID(), parentAddress.getPrimaryKeyID()))
                return;

            // do assignement
            try {
                Integer oldAddressCodeID = getAddressCodeID();

                assign(parentAddress);
                setAddressCodeID(oldAddressCodeID);
                setParentAddressID(parentAddress.getPrimaryKeyID());

            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

        }

    }

    /**
     * get/set methods
     */
    public Integer getAddressCodeID() {
        return addressCodeID;
    }

    public void setAddressCodeID(Integer value) {
        if (equals(addressCodeID, value))
            return;

        addressCodeID = value;
        setModified(true);
    }

    public Integer getParentAddressID() {
        return parentAddressID;
    }

    public void setParentAddressID(Integer value) {
        if (equals(parentAddressID, value))
            return;

        parentAddressID = value;
        setModified(true);
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String value) {
        if (equals(streetNumber, value))
            return;

        streetNumber = value == null ? value : value.trim();
        setModified(true);
    }

    public String getStreetNumber2() {
        return streetNumber2;
    }

    public void setStreetNumber2(String value) {
        if (equals(streetNumber2, value))
            return;

        streetNumber2 = value == null ? value : value.trim();
        setModified(true);
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String value) {
        if (equals(suburb, value))
            return;

        suburb = value == null ? value : value.trim();
        setModified(true);
    }

    public Integer getPostCodeID() {
        return postCodeID;
    }

    public void setPostCodeID(Integer value) {
        if (equals(postCodeID, value))
            return;

        postCodeID = value;
        setModified(true);
    }

    public String getPostCode() {
        return postCodeID == null ? null : postCodeID.toString();
    }

    public Integer getStateCodeID() {
        return new StateCode(countryCodeID).getCodeID(state);
    }

    public void setStateCodeID(Integer value) {
        if (state != null && value == null)
            return;

        setState(new StateCode(countryCodeID).getCodeDescription(value));
    }

    public String getState() {
        return state;
    }

    public void setState(String value) {
        if (equals(state, value))
            return;

        state = value;
        setModified(true);
    }

    public Integer getCountryCodeID() {
        return countryCodeID;
    }

    public void setCountryCodeID(Integer value) {
        if (equals(countryCodeID, value))
            return;

        countryCodeID = value;
        setModified(true);
    }

    public String getCountryCode() {
        return countryCodeID == null ? null : new CountryCode()
                .getCodeDescription(countryCodeID);
    }

}
