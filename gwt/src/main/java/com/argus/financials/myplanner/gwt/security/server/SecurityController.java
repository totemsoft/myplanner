package com.argus.financials.myplanner.gwt.security.server;

import org.springframework.stereotype.Controller;

import com.argus.financials.domain.hibernate.User;
import com.argus.financials.myplanner.gwt.AbstractGwtController;
import com.argus.financials.myplanner.gwt.security.client.SecurityService;

@Controller
public class SecurityController extends AbstractGwtController implements SecurityService
{

    /** serialVersionUID */
    private static final long serialVersionUID = -1034771030159539040L;

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.security.client.SecurityService#login(java.lang.String, java.lang.String)
     */
    public String login(String login, String password)
    {
        try
        {
            User user = getUserService().login(login, password);
            return null;
        }
        catch (Exception e)
        {
            return e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
    
}