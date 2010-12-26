package com.argus.financials.myplanner.gwt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.argus.financials.myplanner.web.servlet.WebUtils;
import com.argus.financials.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Base class for Spring GWT Controller
 */
public abstract class AbstractGwtController extends RemoteServiceServlet
{

    /** serialVersionUID */
    private static final long serialVersionUID = -6954259013777116675L;

    /** Logger. */
    protected final Logger LOG = Logger.getLogger(getClass());

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

}