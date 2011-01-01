package com.argus.financials.myplanner.gwt.main.server;

import java.util.ArrayList;
import java.util.List;

import com.argus.financials.domain.refdata.ICode;
import com.argus.financials.myplanner.gwt.AbstractGwtController;
import com.argus.financials.myplanner.gwt.commons.client.BasePair;
import com.argus.financials.myplanner.gwt.main.client.RefDataService;

public class RefDataServiceImpl extends AbstractGwtController implements RefDataService {

    /** serialVersionUID */
    private static final long serialVersionUID = 7646009319939383411L;

    /* (non-Javadoc)
     * @see com.argus.financials.myplanner.gwt.commons.client.RefDataService#findCountries()
     */
    public BasePair[] findCountries()
    {
        return convert(getEntityService().findCountries());
    }

    /**
     * helper method
     * @param codes
     * @return
     */
    private BasePair[] convert(List<? extends ICode> codes)
    {
        List<BasePair> result = new ArrayList<BasePair>();
        for (ICode c : codes) {
            result.add(new BasePair(c.getId(), c.getDescription()));
        }
        return (BasePair[]) result.toArray(new BasePair[0]);
    }

}