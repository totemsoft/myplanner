package com.argus.financials.myplanner.gwt.main.server;

import java.util.ArrayList;
import java.util.List;

import com.argus.financials.myplanner.commons.client.BasePair;
import com.argus.financials.myplanner.gwt.AbstractGwtController;
import com.argus.financials.myplanner.gwt.main.client.MainService;

public class MainServiceImpl extends AbstractGwtController implements MainService {

    /** serialVersionUID */
    private static final long serialVersionUID = 955889773407212757L;

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.main.client.MainService#getClients()
     */
    public BasePair[] findClients()
    {
        List<BasePair> result = new ArrayList<BasePair>();
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