package au.com.totemsoft.myplanner.dao;

import java.util.List;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface BaseDAO
{

    /**
     * 
     * @param <T>
     * @param clazz
     * @param id
     * @return
     */
    <T> T findById(Class<T> clazz, Object id);

    /**
     * Finder:
     * @param <T>
     * @param clazz
     * @return
     */
    <T> List<T> findAll(Class<T> clazz);

    void save(Object entity);

    void delete(Object entity);

}