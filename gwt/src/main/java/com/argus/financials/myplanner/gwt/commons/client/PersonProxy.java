package com.argus.financials.myplanner.gwt.commons.client;

import java.util.Date;

import com.argus.financials.domain.hibernate.Person;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.ProxyFor;

@ProxyFor(Person.class)
public interface PersonProxy extends EntityProxy
{

    EntityProxyId<PersonProxy> stableId();

    String getFirstname();

    void setFirstname(String firstname);

    String getSurname();

    void setSurname(String surname);

    String getOtherNames();

    void setOtherNames(String otherNames);

    Date getDateOfBirth();

    void setDateOfBirth(Date dateOfBirth);

}