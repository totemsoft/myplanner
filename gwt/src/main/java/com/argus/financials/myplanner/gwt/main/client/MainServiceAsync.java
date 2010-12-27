package com.argus.financials.myplanner.gwt.main.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface MainServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.argus.financials.myplanner.gwt.main.client.MainService
     */
    void findClients( com.argus.financials.myplanner.commons.client.StringPair[] p0, com.google.gwt.view.client.Range p1, AsyncCallback<com.argus.financials.myplanner.commons.client.BasePair[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.argus.financials.myplanner.gwt.main.client.MainService
     */
    void logout( AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static MainServiceAsync instance;

        public static final MainServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (MainServiceAsync) GWT.create( MainService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "MainService" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
