package com.argus.financials.myplanner.gwt.commons.shared;

import com.google.gwt.requestfactory.shared.LoggingRequest;
import com.google.gwt.requestfactory.shared.RequestFactory;

public interface ClientRequestFactory extends RequestFactory
{

    LoggingRequest loggingRequest();

    ClientRequest clientRequest();

}