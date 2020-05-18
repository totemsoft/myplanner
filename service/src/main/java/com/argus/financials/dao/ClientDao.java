package com.argus.financials.dao;

import java.util.List;
import java.util.Map;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.domain.hibernate.ClientView;

/**
 * @author vchibaev (Valeri SHIBAEV)
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