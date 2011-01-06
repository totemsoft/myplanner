package com.argus.financials.myplanner.gwt.commons.client;

import com.argus.financials.domain.hibernate.Address;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.ProxyFor;

@ProxyFor(Address.class)
public interface AddressProxy extends EntityProxy
{

    EntityProxyId<AddressProxy> stableId();

    AddressProxy getParentId();

    void setParent(AddressProxy parent);

//    AddressCodeProxy getAddressCode();
//
//    void setAddressCode(AddressCodeProxy addressCode);

    String getStreetNumber();

    void setStreetNumber(String streetNumber);

    String getStreetNumber2();

    void setStreetNumber2(String streetNumber2);

    String getSuburb();

    void setSuburb(String suburb);

    Integer getPostcode();

    void setPostcode(Integer postcode);

//    StateProxy getState();
//
//    void setState(StateProxy state);

    String getStateCode();

    void setStateCode(String stateCode);

//    CountryProxy getCountry();
//
//    void setCountry(CountryProxy country);

}