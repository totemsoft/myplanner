package com.argus.financials.myplanner.gwt.main.client;

import com.argus.financials.myplanner.gwt.commons.client.BasePair;
import com.argus.financials.myplanner.gwt.commons.client.StringPair;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.view.client.Range;

@RemoteServiceRelativePath("MainService")
public interface MainService extends RemoteService {

    /**
     * 
     * @param selected
     */
    void setClient(Long clientId);

    BasePair[] findClients(StringPair[] criteria, Range range);

    void logout();

}