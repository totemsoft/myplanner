package com.argus.financials.dao;

import java.util.List;

/**
 * @author vchibaev (Valeri SHIBAEV)
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