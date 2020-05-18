package com.argus.financials.service.client;

import java.util.List;

import org.springframework.stereotype.Service;

import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.ISexCode;
import com.argus.financials.api.bean.IState;
import com.argus.financials.api.bean.ITitleCode;

@Service
public interface EntityService
{

    List<? extends ICountry> findCountries();

    List<? extends IState> findStates(Integer countryId);

    IMaritalCode findMaritalCode(Integer id);
    IMaritalCode findMaritalCode(String codeDesc);
    List<? extends IMaritalCode> findMaritalCodes();

    ISexCode findSexCode(Integer id);
    ISexCode findSexCode(String codeDesc);

    ITitleCode findTitleCode(Integer id);
    ITitleCode findTitleCode(String codeDesc);
    List<? extends ITitleCode> findTitleCodes();

}