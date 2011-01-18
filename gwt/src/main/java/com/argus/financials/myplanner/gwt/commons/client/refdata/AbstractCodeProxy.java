package com.argus.financials.myplanner.gwt.commons.client.refdata;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.EntityProxyId;

public interface AbstractCodeProxy extends EntityProxy
{

    EntityProxyId<AbstractCodeProxy> stableId();

    String getCode();

    String getDescription();

}