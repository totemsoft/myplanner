package com.argus.financials.myplanner.gwt.commons.shared;

import com.google.gwt.requestfactory.shared.LoggingRequest;
import com.google.gwt.requestfactory.shared.RequestFactory;

/**
 * Data-oriented way.
 * @author valeri chibaev
 */
public interface GwtRequestFactory extends RequestFactory
{

    ClientRequest clientRequest();

    LoggingRequest loggingRequest();

}