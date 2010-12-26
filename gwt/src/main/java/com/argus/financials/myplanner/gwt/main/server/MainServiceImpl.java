package com.argus.financials.myplanner.gwt.main.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.xalan.xsltc.runtime.Hashtable;

import com.argus.financials.etc.Contact;
import com.argus.financials.myplanner.commons.client.BasePair;
import com.argus.financials.myplanner.commons.client.StringPair;
import com.argus.financials.myplanner.gwt.AbstractGwtController;
import com.argus.financials.myplanner.gwt.main.client.MainService;

public class MainServiceImpl extends AbstractGwtController implements MainService {

    /** serialVersionUID */
    private static final long serialVersionUID = 955889773407212757L;

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.main.client.MainService#getClients()
     */
    public BasePair[] findClients(StringPair[] criteria)
    {
        List<BasePair> result = new ArrayList<BasePair>();

        Map<String, Object> criteriaMap = new TreeMap<String, Object>();
        for (StringPair p : criteria) {
            if (p.getFirst() != null && p.getSecond() != null) {
                criteriaMap.put(p.getFirst(), p.getSecond());
            }
        }
        List<Contact> clients = getUserService().findClients(criteriaMap);
        for (Contact c : clients) {
            result.add(new BasePair(c.getOwnerPrimaryKeyID(), c.getName().getFullName()));
        }

        int i = 1;
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        result.add(new BasePair(i++, "Valera"));
        result.add(new BasePair(i++, "Ilia"));
        
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