package com.argus.financials.api.bean;

public interface IAddress extends IBase<Integer>
{

    String STATE = "StateCodeID";

    String COUNTRY = "CountryCodeID";

    String POSTCODE = "PostCode";

//    IAddress getParent();
//    void setParent(IAddress parent);

//    IAddressCode getAddressCode();
//    void setAddressCode(IAddressCode addressCode);

    String getStreetNumber();
    void setStreetNumber(String streetNumber);

    String getStreetNumber2();
    void setStreetNumber2(String streetNumber2);

    String getSuburb();
    void setSuburb(String suburb);

    Integer getPostcode();
    void setPostcode(Integer postcode);

//    IState getState();
//    void setState(IState state);

    String getStateCode();
    void setStateCode(String stateCode);

//    ICountry getCountry();
//    void setCountry(ICountry country);

}