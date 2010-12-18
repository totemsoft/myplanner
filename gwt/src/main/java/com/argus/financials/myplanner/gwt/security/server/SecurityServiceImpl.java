package com.argus.financials.myplanner.gwt.security.server;

import com.argus.crypto.Digest;
import com.argus.financials.myplanner.gwt.security.client.SecurityService;
import com.argus.financials.service.ServiceLocator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SecurityServiceImpl extends RemoteServiceServlet implements SecurityService
{

    /** serialVersionUID */
    private static final long serialVersionUID = 8117312195834171482L;

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.security.client.SecurityService#login(java.lang.String, java.lang.String)
     */
    public String login(String userName, String userPassword)
    {
        try
        {
            //getThreadLocalRequest()
            ServiceLocator.getInstance().login(userName, Digest.digest(userPassword));
            return null;
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
    
}