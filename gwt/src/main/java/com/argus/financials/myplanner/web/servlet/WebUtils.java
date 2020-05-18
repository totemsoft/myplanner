package com.argus.financials.myplanner.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/*
 * @author Valera Shibaev
 */
public final class WebUtils
{
    /** hide ctor */
    private WebUtils()
    {
    }

    /**
     * 
     * @param servletContext
     * @return
     */
    public static WebApplicationContext getApplicationContext(ServletContext servletContext)
    {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    /**
     *
     * @param servletContext
     * @param name Spring bean name.
     * @return Spring bean instance.
     */
    public static Object getBean(ServletContext servletContext, String name)
    {
        return getApplicationContext(servletContext).getBean(name);
    }

    /**
     *
     * @param request
     * @return
     */
    public static Integer getPropogationBehavior(HttpServletRequest request)
    {
        return getId(request, SessionConstants.PROPOGATION_BEHAVIOR);
    }

    /**
     * Default readOnly = false
     * TODO: Make default readOnly = true
     * @param request
     * @return
     */
    public static boolean isReadOnly(HttpServletRequest request)
    {
        String value = request.getParameter(SessionConstants.READ_ONLY);
        return StringUtils.isNotBlank(value) && Boolean.valueOf(value).booleanValue();
    }

    /**
     *
     * @param request
     * @return
     */
    private static Integer getInteger(String value)
    {
        try
        {
            // for IE6,7 pass part of query string, eg fileId=7#editClientDetails
            int idx = value.indexOf('#');
            if (idx > 0)
            {
                value = value.substring(0, idx);
            }
            Integer id = Integer.valueOf(value);
            return id == null || id == 0 ? null : id;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     *
     * @param request
     * @return
     */
    private static Integer getId(HttpServletRequest request, String name)
    {
        return getInteger(request.getParameter(name));
    }

    /**
     *
     * @param request
     * @return
     */
    public static Integer getId(HttpServletRequest request)
    {
        return getId(request, SessionConstants.ID);
    }

    /**
     *
     * @param request
     * @param value
     */
    public static void setId(HttpServletRequest request, Integer value)
    {
        request.setAttribute(SessionConstants.ID, value);
    }

    /**
     * 
     * @param request
     * @return
     */
    public static int getIndex(HttpServletRequest request)
    {
        return Integer.valueOf(request.getParameter(SessionConstants.INDEX)).intValue();
    }

}