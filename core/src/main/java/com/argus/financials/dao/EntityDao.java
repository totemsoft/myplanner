package com.argus.financials.dao;

import java.util.List;

import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.State;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface EntityDao extends BaseDAO
{

    /**
     * @return
     */
    List<Country> findCountries();

    /**
     * @param countryId
     * @return
     */
    List<State> findStates(Integer countryId);

}