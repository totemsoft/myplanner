package com.argus.financials.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.MaritalCode;
import com.argus.financials.domain.hibernate.refdata.SexCode;
import com.argus.financials.domain.hibernate.refdata.State;
import com.argus.financials.domain.hibernate.refdata.TitleCode;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface EntityService
{

    /**
     * @return
     */
    List<Country> findCountries();

    /**
     * 
     * @param id
     * @return
     */
    MaritalCode findMaritalCode(Long id);

    /**
     * 
     * @return
     */
    List<MaritalCode> findMaritalCodes();

    /**
     * 
     * @param id
     * @return
     */
    SexCode findSexCode(Long id);

    /**
     * 
     * @param countryId
     * @return
     */
    List<State> findStates(Integer countryId);

    /**
     * 
     * @param id
     * @return
     */
    TitleCode findTitleCode(Long id);

    /**
     * 
     * @return
     */
    List<TitleCode> findTitleCodes();

}