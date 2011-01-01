package com.argus.financials.myplanner.gwt.main.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface RefDataServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.argus.financials.myplanner.gwt.main.client.RefDataService
     */
    void findCountries( AsyncCallback<com.argus.financials.myplanner.gwt.commons.client.BasePair[]> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static RefDataServiceAsync instance;

        public static final RefDataServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (RefDataServiceAsync) GWT.create( RefDataService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "RefDataService" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
