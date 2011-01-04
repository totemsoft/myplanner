package com.argus.financials.myplanner.gwt.main.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.domain.hibernate.view.Client;
import com.argus.financials.myplanner.gwt.AbstractGwtController;
import com.argus.financials.myplanner.gwt.commons.client.BasePair;
import com.argus.financials.myplanner.gwt.commons.client.StringPair;
import com.argus.financials.myplanner.gwt.main.client.MainService;
import com.google.gwt.view.client.Range;

public class MainServiceImpl extends AbstractGwtController implements MainService {

    /** serialVersionUID */
    private static final long serialVersionUID = 955889773407212757L;

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.main.client.MainService#setClient(com.argus.financials.myplanner.gwt.commons.client.BasePair)
     */
    public void setClient(BasePair selected)
    {
        getUserPreferences().setClient(getUserService().findClient(selected.getFirst()));
    }

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.main.client.MainService#getClients()
     */
    public BasePair[] findClients(StringPair[] criteria, Range range)
    {
        Map<String, Object> criteriaMap = new TreeMap<String, Object>();
        for (StringPair p : criteria) {
            if (p != null && p.getFirst() != null && p.getSecond() != null) {
                criteriaMap.put(p.getFirst(), p.getSecond());
            }
        }
        List<Client> clients = getUserService().findClients(criteriaMap, range == null ? null :
            new com.argus.util.Range(range.getStart(), range.getLength()));
        // convert to dto
        List<BasePair> result = new ArrayList<BasePair>();
        for (Client c : clients) {
            result.add(new BasePair(c.getId(), c.getShortName()));
        }
        return (BasePair[]) result.toArray(new BasePair[0]);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.main.client.MainService#logout()
     */
    public void logout()
    {
        // TODO Auto-generated method stub
        
    }

}