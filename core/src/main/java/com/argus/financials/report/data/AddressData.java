/*
 * AddressData.java
 *
 * Created on 16 October 2002, 11:20
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 */

public class AddressData extends BaseData {

    public String StreetNumber = STRING_EMPTY;

    public String StreetNumber2 = STRING_EMPTY;

    public String Suburb = STRING_EMPTY;

    public String Postcode = STRING_EMPTY;

    public String State = STRING_EMPTY;

    public String Country = STRING_EMPTY;

    /** Creates a new instance of AddressData */
    public AddressData() {
        clear();
    }

    public AddressData(com.argus.financials.etc.Address address) {
        this();
        init(address);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void clear() {
        StreetNumber = STRING_EMPTY;
        StreetNumber2 = STRING_EMPTY; // optional
        Suburb = STRING_EMPTY;
        Postcode = STRING_EMPTY;
        State = STRING_EMPTY;
        Country = STRING_EMPTY;
    }

    public String getFullAddress() {

        return (StreetNumber == null ? STRING_EMPTY : StreetNumber) + " "
                + (StreetNumber2 == null ? STRING_EMPTY : StreetNumber2) + " "
                + (Suburb == null ? STRING_EMPTY : Suburb) + " "
                + (State == null ? STRING_EMPTY : State) + " "
                + (Postcode == null ? STRING_EMPTY : Postcode);
    }

    public void init(com.argus.financials.etc.Address address) {

        if (address == null) {
            clear();
            return;
        }

        StreetNumber = address.getStreetNumber() == null ? STRING_EMPTY
                : address.getStreetNumber();
        StreetNumber2 = address.getStreetNumber2() == null ? STRING_EMPTY
                : address.getStreetNumber2();
        Suburb = address.getSuburb() == null ? STRING_EMPTY : address
                .getSuburb();
        Postcode = address.getPostCode() == null ? STRING_EMPTY : address
                .getPostCode();
        State = address.getState() == null ? STRING_EMPTY : address.getState();
        Country = address.getCountryCode() == null ? STRING_EMPTY : address
                .getCountryCode();

    }

}
