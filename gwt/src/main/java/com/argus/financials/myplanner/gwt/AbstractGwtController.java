package com.argus.financials.myplanner.gwt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.argus.financials.myplanner.web.servlet.WebUtils;
import com.argus.financials.security.UserPreferences;
import com.argus.financials.service.EntityService;
import com.argus.financials.service.client.UserService;
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
    private EntityService entityService;

    @Autowired
    private UserService userService;

    /**
     * @return the entityService
     */
    protected EntityService getEntityService()
    {
        // FIXME: spring
        if (entityService == null)
        {
            LOG.warn("entityService=" + entityService);
            entityService = (EntityService) WebUtils.getBean(getServletContext(), "entityService");
        }
        return entityService;
    }

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

    protected UserPreferences getUserPreferences()
    {
        return (UserPreferences) WebUtils.getBean(getServletContext(), "userPreferences");
    }

}