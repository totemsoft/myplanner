package com.argus.financials.api.bean;

import java.io.Serializable;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class UserPreferences implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -408504227565925088L;

    /** TODO: move to Principal - current logged user */
    private IUser user;

    private IClient client;

    /**
     * 
     */
    public void clear()
    {
        this.client = null;
        this.user = null;
    }

    /**
     * @return the user
     */
    public IUser getUser()
    {
        return user;
    }

    public void setUser(IUser user)
    {
        this.user = user;
    }

    /**
     * @return the client
     */
    public IClient getClient()
    {
        return client;
    }

    public void setClient(IClient client)
    {
        this.client = client;
    }

}