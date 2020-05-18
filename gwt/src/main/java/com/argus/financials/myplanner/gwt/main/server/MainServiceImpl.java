package com.argus.financials.myplanner.gwt.main.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.domain.hibernate.view.ClientView;
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
    public void openClient(Long clientId)
    {
        Client client = getUserService().findClient(clientId);
        getUserPreferences().setClient(client);
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
        List<ClientView> clients = getUserService().findClients(criteriaMap,
            range.getStart(), range.getLength());
        // convert to dto
        List<BasePair> result = new ArrayList<BasePair>();
        for (ClientView c : clients) {
            result.add(new BasePair(c.getId(), c.getShortName()));
        }
        return (BasePair[]) result.toArray(new BasePair[0]);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.main.client.MainService#logout()
     */
    public void logout()
    {
        // cleanup
        getUserPreferences().clear();
    }

}