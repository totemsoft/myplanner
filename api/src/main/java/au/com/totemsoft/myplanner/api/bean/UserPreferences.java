package au.com.totemsoft.myplanner.api.bean;

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

    private Long clientId;

    /**
     * 
     */
    public void clear()
    {
        this.clientId = null;
        this.user = null;
    }

    /**
     * @return the user
     */
    public IUser user()
    {
        return user;
    }

    public void user(IUser user)
    {
        this.user = user;
    }

    /**
     * @return the clientId
     */
    public Long clientId()
    {
        return clientId;
    }

    public void clientId(Long clientId)
    {
        this.clientId = clientId;
    }

}