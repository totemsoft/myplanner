package au.com.totemsoft.myplanner.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

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

    @Inject private EntityDao entityDao;

    @Override
    public List<ICountry> findCountries()
    {
        List<? extends ICountry> result = entityDao.findCountries();
        Collections.sort(result, new CountryComparator(FPSLocale.getInstance().getDisplayCountry()));
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<IState> findStates(Integer countryId)
    {
        return Collections.unmodifiableList(entityDao.findStates(countryId));
    }

    @Override
    public IMaritalCode findMaritalCode(Integer id)
    {
        return entityDao.findMaritalCode(id);
    }

    @Override
    public IMaritalCode findMaritalCode(String codeDesc)
    {
        return entityDao.findMaritalCode(codeDesc);
    }

    @Override
    public List<IMaritalCode> findMaritalCodes()
    {
        return Collections.unmodifiableList(entityDao.findMaritalCodes());
    }

    @Override
    public ISexCode findSexCode(Integer id)
    {
        return entityDao.findSexCode(id);
    }

    @Override
    public ISexCode findSexCode(String codeDesc)
    {
        return entityDao.findSexCode(codeDesc);
    }

    @Override
    public ITitleCode findTitleCode(Integer id)
    {
        return entityDao.findTitleCode(id);
    }

    @Override
    public ITitleCode findTitleCode(String codeDesc)
    {
        return entityDao.findTitleCode(codeDesc);
    }

    @Override
    public List<ITitleCode> findTitleCodes()
    {
        return Collections.unmodifiableList(entityDao.findTitleCodes());
    }

}
