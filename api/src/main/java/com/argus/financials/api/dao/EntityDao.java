package com.argus.financials.api.dao;

import java.util.List;

import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.ISexCode;
import com.argus.financials.api.bean.IState;
import com.argus.financials.api.bean.ITitleCode;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface EntityDao
{

    ICountry findCountry(Integer id);
    List<? extends ICountry> findCountries();

    List<? extends IState> findStates(Integer countryId);

    ISexCode findSexCode(Integer id);
    ISexCode findSexCode(String codeDesc);

    IMaritalCode findMaritalCode(Integer id);
    IMaritalCode findMaritalCode(String codeDesc);
    List<? extends IMaritalCode> findMaritalCodes();

    ITitleCode findTitleCode(Integer id);
    ITitleCode findTitleCode(String codeDesc);
    List<? extends ITitleCode> findTitleCodes();

}