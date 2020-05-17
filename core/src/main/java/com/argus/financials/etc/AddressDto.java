/*
 * Address.java
 *
 * Created on 20 August 2001, 12:41
 */

package com.argus.financials.etc;

import com.argus.financials.api.bean.IAddress;
import com.argus.financials.api.bean.IFPSAssignableObject;
import com.argus.financials.code.CountryCode;
import com.argus.financials.code.StateCode;

public class AddressDto extends FPSAssignableObject implements IAddress
{

    private Integer addressCodeId;

    private Integer parentAddressId;

    private String streetNumber;

    private String streetNumber2;

    private String suburb;

    private Integer postcode;

    private String stateCode;

    private Integer countryCodeId;

    public AddressDto() {
        super();
    }

    public AddressDto(int ownerId) {
        super(ownerId);
    }

    public String toString() {
        return getFullAddress();
    }

    public String getFullAddress() {
        return (streetNumber == null ? "" : streetNumber) + " "
                + (streetNumber2 == null ? "" : streetNumber2) + " "
                + (suburb == null ? "" : suburb) + " "
                + (stateCode == null ? "" : stateCode) + " "
                + (postcode == null ? "" : "" + postcode);
    }

    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {
        super.assign(value);
        if (!(this instanceof AddressDto))
            throw new ClassCastException("This is not a " + this.getClass().getName());
        addressCodeId = ((AddressDto) value).addressCodeId;
        streetNumber = ((AddressDto) value).streetNumber;
        streetNumber2 = ((AddressDto) value).streetNumber2;
        suburb = ((AddressDto) value).suburb;
        postcode = ((AddressDto) value).postcode;
        stateCode = ((AddressDto) value).stateCode;
        // stateCode = ( (Address) value ).stateCode;
        countryCodeId = ((AddressDto) value).countryCodeId;
        parentAddressId = ((AddressDto) value).parentAddressId;
        // has to be last (to set modified)
        setModified(true);
    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();
        addressCodeId = null;
        streetNumber = null;
        streetNumber2 = null;
        suburb = null;
        postcode = null;
        stateCode = null;
        // stateCode = null;
        countryCodeId = null;
        parentAddressId = null;
    }

    public boolean isClear() {
        if (countryCodeId == null && streetNumber == null
                && streetNumber2 == null && suburb == null
                && postcode == null && stateCode == null
                // && stateCodeID == null
                && parentAddressId == null)
            return true;
        return false;
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        if (changeEvent.getSource() instanceof AddressDto) {
            AddressDto parentAddress = (AddressDto) changeEvent.getSource();
            if (!equals(getParentAddressId(), parentAddress.getId()))
                return;
            // do assignement
            try {
                Integer oldAddressCodeID = getAddressCodeId();
                assign(parentAddress);
                setAddressCodeId(oldAddressCodeID);
                setParentAddressId(parentAddress.getId());
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }
        }
    }

    /**
     * get/set methods
     */
    public Integer getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(Integer value) {
        if (equals(addressCodeId, value))
            return;
        addressCodeId = value;
        setModified(true);
    }

    public Integer getParentAddressId() {
        return parentAddressId;
    }

    public void setParentAddressId(Integer value) {
        if (equals(parentAddressId, value))
            return;
        parentAddressId = value;
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

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer value) {
        if (equals(postcode, value))
            return;
        postcode = value;
        setModified(true);
    }

    public Integer getStateCodeId() {
        return new StateCode(countryCodeId).getCodeID(stateCode);
    }

    public void setStateCodeId(Integer value) {
        if (stateCode != null && value == null)
            return;
        setStateCode(new StateCode(countryCodeId).getCodeDescription(value));
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String value) {
        if (equals(stateCode, value))
            return;
        stateCode = value;
        setModified(true);
    }

    public Integer getCountryCodeId() {
        return countryCodeId;
    }

    public void setCountryCodeId(Integer value) {
        if (equals(countryCodeId, value))
            return;
        countryCodeId = value;
        setModified(true);
    }

    public String getCountryCode() {
        return countryCodeId == null ? null : new CountryCode().getCodeDescription(countryCodeId);
    }

}