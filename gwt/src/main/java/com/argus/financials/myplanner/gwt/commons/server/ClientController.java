package com.argus.financials.myplanner.gwt.commons.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.myplanner.web.servlet.WebUtils;
import com.argus.financials.service.UserService;

public class ClientController implements Filter
{

    private static UserService userService;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException
    {
        userService = (UserService) WebUtils.getBean(filterConfig.getServletContext(), "userService");
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        userService = null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        try
        {
            //PERSON_SOURCE.set(PersonSource.of(backingStore));
            chain.doFilter(request, response);
        }
        finally
        {
            //PERSON_SOURCE.set(null);
        }
    }

    public static Client findClient(Long clientId)
    {
        return userService.findClient(clientId);
    }

}