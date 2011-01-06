package com.argus.financials.domain.hibernate;

import com.argus.financials.domain.client.IBase;
import com.argus.financials.domain.client.refdata.IAddressCode;
import com.argus.financials.domain.client.refdata.ICountry;
import com.argus.financials.domain.client.refdata.IState;

public interface IAddress extends IBase<Integer>
{
    IAddress getParentId();

    void setParent(IAddress parent);

    IAddressCode getAddressCode();

    void setAddressCode(IAddressCode addressCode);

    String getStreetNumber();

    void setStreetNumber(String streetNumber);

    String getStreetNumber2();

    void setStreetNumber2(String streetNumber2);

    String getSuburb();

    void setSuburb(String suburb);

    Integer getPostcode();

    void setPostcode(Integer postcode);

    IState getState();

    void setState(IState state);

    String getStateCode();

    void setStateCode(String stateCode);

    ICountry getCountry();

    void setCountry(ICountry country);

}