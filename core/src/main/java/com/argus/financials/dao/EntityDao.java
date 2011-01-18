package com.argus.financials.dao;

import java.util.List;

import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.MaritalCode;
import com.argus.financials.domain.hibernate.refdata.State;
import com.argus.financials.domain.hibernate.refdata.TitleCode;

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
     * 
     * @return
     */
    List<MaritalCode> findMaritalCodes();

    /**
     * @param countryId
     * @return
     */
    List<State> findStates(Integer countryId);

    /**
     * 
     * @return
     */
    List<TitleCode> findTitleCodes();

}