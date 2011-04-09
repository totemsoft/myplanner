package com.argus.financials.myplanner.web.servlet;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.domain.hibernate.refdata.AbstractCode;
import com.argus.financials.service.EntityService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.service.UtilityService;
import com.argus.financials.service.client.UserService;

/**
 * Helper class for setting up the web application.
 * It must be configured as a listener on in the web application <code>web.xml</code>.
 * @author Valeri Shibaev
 */
public class SetupContextListener extends ContextLoaderListener
{
    /** Used for logging. */
    private static final Logger LOG = Logger.getLogger(SetupContextListener.class);
    static
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            for (Map.Entry<String, String> entry : System.getenv().entrySet())
            {
                sb.append(entry.getKey() + "=" + entry.getValue()).append('\n');
            }
            LOG.info("System environment variables:\n" + sb.toString());
        }
        catch (Exception e)
        {
            LOG.warn(e.getMessage());
        }
        sb.setLength(0);
        try
        {
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet())
            {
                sb.append(entry.getKey() + "=" + entry.getValue()).append('\n');
            }
            LOG.info("System Properties:\n" + sb.toString());
        }
        catch (RuntimeException e)
        {
            LOG.warn(e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent event)
    {
        super.contextInitialized(event);
        LOG.info("Application Context has been initialised.");
    }

    /* (non-Javadoc)
     * @see org.springframework.web.context.ContextLoader#initWebApplicationContext(javax.servlet.ServletContext)
     */
    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
    {
        WebApplicationContext ctx = super.initWebApplicationContext(servletContext);
        ServiceLocator.getInstance().setApplicationContext(ctx);
        try
        {
            UtilityService utilityService = (UtilityService) WebUtils.getBean(servletContext, "utilityService");
            utilityService.syncDBSchema();
        }
        catch (Exception e)
        {
            LOG.error("Failed to syncDBSchema", e);
            //throw e;
        }
        //
        UserService userService = (UserService) WebUtils.getBean(servletContext, "userService");
        Client.setUserService(userService);
        //
        EntityService entityService = (EntityService) WebUtils.getBean(servletContext, "entityService");
        AbstractCode.setEntityService(entityService);
        //
        return ctx;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
        super.contextDestroyed(event);
        LOG.info("Application Context has been destroyed.");
    }

}