package com.argus.financials.myplanner.gwt.security.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface SecurityControllerAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.argus.financials.myplanner.gwt.security.client.SecurityController
     */
    void login( java.lang.String userName, java.lang.String userPassword, AsyncCallback<java.lang.String> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static SecurityControllerAsync instance;

        public static final SecurityControllerAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (SecurityControllerAsync) GWT.create( SecurityController.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "SecurityController" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
