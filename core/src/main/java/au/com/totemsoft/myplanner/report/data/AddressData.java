/*
 * AddressData.java
 *
 * Created on 16 October 2002, 11:20
 */

package au.com.totemsoft.myplanner.report.data;

import au.com.totemsoft.myplanner.etc.AddressDto;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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

    public AddressData(AddressDto address) {
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

    public void init(AddressDto address) {

        if (address == null) {
            clear();
            return;
        }

        StreetNumber = address.getStreetNumber() == null ? STRING_EMPTY : address.getStreetNumber();
        StreetNumber2 = address.getStreetNumber2() == null ? STRING_EMPTY : address.getStreetNumber2();
        Suburb = address.getSuburb() == null ? STRING_EMPTY : address.getSuburb();
        Postcode = address.getPostcode() == null ? STRING_EMPTY : address.getPostcode().toString();
        State = address.getStateCode() == null ? STRING_EMPTY : address.getStateCode();
        Country = address.getCountryCode() == null ? STRING_EMPTY : address.getCountryCode();

    }

}
