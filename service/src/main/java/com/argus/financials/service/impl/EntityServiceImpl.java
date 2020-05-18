package com.argus.financials.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.ISexCode;
import com.argus.financials.api.bean.IState;
import com.argus.financials.api.bean.ITitleCode;
import com.argus.financials.api.dao.EntityDao;
import com.argus.financials.config.FPSLocale;
import com.argus.financials.service.client.EntityService;
import com.argus.financials.util.CountryComparator;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class EntityServiceImpl implements EntityService
{

    @Autowired private EntityDao entityDao;

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findCountries()
     */
    public List<? extends ICountry> findCountries()
    {
        List<? extends ICountry> result = entityDao.findCountries();
        Collections.sort(result, new CountryComparator(FPSLocale.getInstance().getDisplayCountry()));
        return result;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findStates(java.lang.Integer)
     */
    public List<? extends IState> findStates(Integer countryId)
    {
        return entityDao.findStates(countryId);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findMaritalCode(java.lang.Integer)
     */
    public IMaritalCode findMaritalCode(Integer id)
    {
        return entityDao.findMaritalCode(id);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.client.EntityService#findMaritalCode(java.lang.String)
     */
    @Override
    public IMaritalCode findMaritalCode(String codeDesc)
    {
        return entityDao.findMaritalCode(codeDesc);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findMaritalCodes()
     */
    public List<? extends IMaritalCode> findMaritalCodes()
    {
        return entityDao.findMaritalCodes();
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findSexCode(java.lang.Integer)
     */
    public ISexCode findSexCode(Integer id)
    {
        return entityDao.findSexCode(id);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.client.EntityService#findSexCode(java.lang.String)
     */
    @Override
    public ISexCode findSexCode(String codeDesc)
    {
        return entityDao.findSexCode(codeDesc);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findTitleCode(java.lang.Integer)
     */
    public ITitleCode findTitleCode(Integer id)
    {
        return entityDao.findTitleCode(id);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.client.EntityService#findTitleCode(java.lang.String)
     */
    @Override
    public ITitleCode findTitleCode(String codeDesc)
    {
        return entityDao.findTitleCode(codeDesc);
    }

    /* (non-Javadoc)
     * @see com.argus.financials.service.EntityService#findTitleCodes()
     */
    public List<? extends ITitleCode> findTitleCodes()
    {
        return entityDao.findTitleCodes();
    }

}