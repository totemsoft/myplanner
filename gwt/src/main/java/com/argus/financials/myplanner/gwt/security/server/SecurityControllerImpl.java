package com.argus.financials.myplanner.gwt.security.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.argus.financials.myplanner.gwt.security.client.SecurityController;
import com.argus.financials.myplanner.web.servlet.WebUtils;
import com.argus.financials.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Controller(value = "SecurityController")
public class SecurityControllerImpl extends RemoteServiceServlet /*GWTSpringController*/ implements SecurityController
{

    /** serialVersionUID */
    private static final long serialVersionUID = 8117312195834171482L;

    /** Logger. */
    protected final Logger LOG = Logger.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * @return the userService
     */
    protected UserService getUserService()
    {
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
     * @see com.argus.financials.myplanner.gwt.security.client.SecurityController#login(java.lang.String, java.lang.String)
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