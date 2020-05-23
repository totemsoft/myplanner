package au.com.totemsoft.myplanner.api.dao;

import java.util.List;

import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.api.bean.IMaritalCode;
import au.com.totemsoft.myplanner.api.bean.ISexCode;
import au.com.totemsoft.myplanner.api.bean.IState;
import au.com.totemsoft.myplanner.api.bean.ITitleCode;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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