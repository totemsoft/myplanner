package com.argus.financials.myplanner.gwt.security.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface SecurityServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.argus.financials.myplanner.gwt.security.client.SecurityService
     */
    void login( java.lang.String p0, java.lang.String p1, AsyncCallback<java.lang.String> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static SecurityServiceAsync instance;

        public static final SecurityServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (SecurityServiceAsync) GWT.create( SecurityService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "SecurityService" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
