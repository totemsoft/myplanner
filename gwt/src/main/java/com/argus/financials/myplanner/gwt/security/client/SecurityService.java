package com.argus.financials.myplanner.gwt.security.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SecurityService")
public interface SecurityService extends RemoteService
{

    /**
     * 
     * @param userName
     * @param userPassword
     * @return error message or null if success
     */
    String login(String userName, String userPassword);

}