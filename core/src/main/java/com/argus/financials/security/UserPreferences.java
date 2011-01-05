package com.argus.financials.security;

import java.io.Serializable;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.domain.hibernate.User;

/**
 * 
 * @author vchibaev (Valeri SHIBAEV)
 */
public class UserPreferences implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -408504227565925088L;

    /** TODO: move to Principal - current logged user */
    private User user;

    private Client client;

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
    public User getUser()
    {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * @return the client
     */
    public Client getClient()
    {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client)
    {
        this.client = client;
    }

}