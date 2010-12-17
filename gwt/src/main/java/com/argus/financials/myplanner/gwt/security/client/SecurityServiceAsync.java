package com.argus.financials.myplanner.gwt.security.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SecurityServiceAsync
{

    /**
     * 
     * @param userName
     * @param userPassword
     * @param callback
     * @return String error message or null if success
     */
    void login(String userName, String userPassword, AsyncCallback<String> callback);

}