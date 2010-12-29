package com.argus.financials.dao;

import java.util.List;
import java.util.Map;

import com.argus.financials.domain.hibernate.view.Client;
import com.argus.util.Range;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface ClientDao
{
    /**
     * 
     * @param criteria
     * @param range
     * @return
     */
    List<Client> findClients(Map<String, Object> criteria, Range range);

}