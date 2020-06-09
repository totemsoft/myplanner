package au.com.totemsoft.myplanner.dao.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import au.com.totemsoft.myplanner.dao.BaseDAO;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class BaseDAOImpl implements BaseDAO
{

    /** logger. */
    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    /**
     * @return the em
     */
    protected EntityManager getEntityManager()
    {
        return em;
    }

    public <T> T findById(Class<T> clazz, Object id)
    {
        return id == null ? null : getEntityManager().find(clazz, id);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.dao.BaseDAO#findAll(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> clazz)
    {
        return (List<T>) getEntityManager().createQuery("FROM " + clazz.getName()).getResultList();
    }

    /**
     * set query parameters
     * @param qry
     * @param parameters
     */
    protected void updateQuery(Query qry, Map<String, Object> parameters)
    {
        Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();
        while (iter.hasNext())
        {
            Entry<String, Object> entry = iter.next();
            qry.setParameter(entry.getKey(), entry.getValue());
        }
    }

    protected void updateQuery(Query qry, int start, int length)
    {
        if (start >= 0)
        {
            qry.setFirstResult(start);
        }
        if (length > 0)
        {
            qry.setMaxResults(length);
        }
    }

    @Override
    public void persist(Object entity) {
        getEntityManager().persist(entity);
    }

    @Override
    public void merge(Object entity) {
        getEntityManager().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        getEntityManager().remove(entity);
    }

}