package com.argus.financials.myplanner.gwt.commons.client;

import java.util.Date;

import com.argus.financials.domain.hibernate.Client;
import com.google.gwt.requestfactory.shared.ProxyFor;

@ProxyFor(Client.class)
public interface ClientProxy extends PersonProxy
{

    Date getFeeDate();

    void setFeeDate(Date feeDate);

    Date getReviewDate();

    void setReviewDate(Date reviewDate);

}