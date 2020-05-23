package au.com.totemsoft.myplanner.dao;

import java.util.List;
import java.util.Map;

import au.com.totemsoft.myplanner.domain.hibernate.Client;
import au.com.totemsoft.myplanner.domain.hibernate.ClientView;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface ClientDao extends BaseDAO
{

    /**
     * 
     * @param clientId
     * @return
     */
    Client findById(Long clientId);

    /**
     * 
     * @param criteria
     * @param start
     * @param length
     * @return
     */
    List<ClientView> findClients(Map<String, Object> criteria, int start, int length);

}