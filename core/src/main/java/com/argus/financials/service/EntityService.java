package com.argus.financials.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.State;

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
     * @param countryId
     * @return
     */
    List<State> findStates(Long countryId);

}