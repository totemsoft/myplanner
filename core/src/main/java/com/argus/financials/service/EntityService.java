package com.argus.financials.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.domain.hibernate.refdata.Country;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface EntityService
{

    /**
     * @return
     */
    List<Country> findCountries();

}