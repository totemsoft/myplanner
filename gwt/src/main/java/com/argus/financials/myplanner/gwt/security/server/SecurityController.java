package com.argus.financials.myplanner.gwt.security.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.argus.financials.myplanner.gwt.AbstractGwtController;
import com.argus.financials.myplanner.gwt.security.client.SecurityService;
import com.argus.financials.myplanner.web.servlet.WebUtils;
import com.argus.financials.service.UserService;

@Controller
public class SecurityController extends AbstractGwtController implements SecurityService
{

    /** serialVersionUID */
    private static final long serialVersionUID = -1034771030159539040L;

    @Autowired
    private UserService userService;

    /**
     * @return the userService
     */
    protected UserService getUserService()
    {
        // FIXME: spring
        if (userService == null)
        {
            LOG.warn("userService=" + userService);
            userService = (UserService) WebUtils.getBean(getServletContext(), "userService");
        }
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.security.client.SecurityService#login(java.lang.String, java.lang.String)
     */
    public String login(String userName, String userPassword)
    {
        try
        {
            UserService user = getUserService().findByLoginNamePassword(userName, userPassword);
            return null;
        }
        catch (Exception e)
        {
            return e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
    
}