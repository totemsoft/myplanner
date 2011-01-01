package com.argus.financials.myplanner.gwt.main.client;

import com.argus.financials.myplanner.gwt.commons.client.BasePair;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("RefDataService")
public interface RefDataService extends RemoteService {

    /**
     * 
     * @return
     */
    BasePair[] findCountries();

}