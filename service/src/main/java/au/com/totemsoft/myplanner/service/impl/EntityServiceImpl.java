package au.com.totemsoft.myplanner.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.api.bean.IMaritalCode;
import au.com.totemsoft.myplanner.api.bean.ISexCode;
import au.com.totemsoft.myplanner.api.bean.IState;
import au.com.totemsoft.myplanner.api.bean.ITitleCode;
import au.com.totemsoft.myplanner.api.dao.EntityDao;
import au.com.totemsoft.myplanner.config.FPSLocale;
import au.com.totemsoft.myplanner.service.client.EntityService;
import au.com.totemsoft.myplanner.util.CountryComparator;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class EntityServiceImpl implements EntityService
{

    @Autowired private EntityDao entityDao;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findCountries()
     */
    public List<? extends ICountry> findCountries()
    {
        List<? extends ICountry> result = entityDao.findCountries();
        Collections.sort(result, new CountryComparator(FPSLocale.getInstance().getDisplayCountry()));
        return result;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findStates(java.lang.Integer)
     */
    public List<? extends IState> findStates(Integer countryId)
    {
        return entityDao.findStates(countryId);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findMaritalCode(java.lang.Integer)
     */
    public IMaritalCode findMaritalCode(Integer id)
    {
        return entityDao.findMaritalCode(id);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.client.EntityService#findMaritalCode(java.lang.String)
     */
    @Override
    public IMaritalCode findMaritalCode(String codeDesc)
    {
        return entityDao.findMaritalCode(codeDesc);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findMaritalCodes()
     */
    public List<? extends IMaritalCode> findMaritalCodes()
    {
        return entityDao.findMaritalCodes();
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findSexCode(java.lang.Integer)
     */
    public ISexCode findSexCode(Integer id)
    {
        return entityDao.findSexCode(id);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.client.EntityService#findSexCode(java.lang.String)
     */
    @Override
    public ISexCode findSexCode(String codeDesc)
    {
        return entityDao.findSexCode(codeDesc);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findTitleCode(java.lang.Integer)
     */
    public ITitleCode findTitleCode(Integer id)
    {
        return entityDao.findTitleCode(id);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.client.EntityService#findTitleCode(java.lang.String)
     */
    @Override
    public ITitleCode findTitleCode(String codeDesc)
    {
        return entityDao.findTitleCode(codeDesc);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.service.EntityService#findTitleCodes()
     */
    public List<? extends ITitleCode> findTitleCodes()
    {
        return entityDao.findTitleCodes();
    }

}