package com.argus.financials.myplanner.gwt.security.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

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

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util
    {
        private static SecurityServiceAsync instance;

        public static SecurityServiceAsync getInstance()
        {
            if (instance == null)
            {
                instance = GWT.create(SecurityService.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "/SecurityService");
            }
            return instance;
        }
    }

}