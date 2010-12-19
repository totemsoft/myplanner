package com.argus.financials.myplanner.gwt;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Base class for Spring GWT Controller
 */
public abstract class AbstractGwtController extends RemoteServiceServlet
{

    /** serialVersionUID */
    private static final long serialVersionUID = -6954259013777116675L;

    /** Logger. */
    protected final Logger LOG = Logger.getLogger(getClass());

}