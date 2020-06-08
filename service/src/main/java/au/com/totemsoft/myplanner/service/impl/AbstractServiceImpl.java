/*
 * EntityBean_impl.java
 *
 * Created on 24 July 2001, 15:48
 */

package au.com.totemsoft.myplanner.service.impl;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import au.com.totemsoft.myplanner.api.RemoveException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.DbConstant;
import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.bean.db.AbstractPersistable;
import au.com.totemsoft.myplanner.etc.FPSObject;

public abstract class AbstractServiceImpl extends AbstractPersistable implements DbConstant {

    @Inject protected UserPreferences userPreferences;

    protected AbstractServiceImpl() {
        super();
    }

    protected AbstractServiceImpl(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public void remove() throws ServiceException, RemoveException {
        // TODO Auto-generated method stub
    }

    public void store() throws ServiceException {
        // TODO Auto-generated method stub
    }

    protected static synchronized void updatePrimaryKey(java.util.Map map) {

        if (map == null)
            return;

        java.util.ArrayList list = new java.util.ArrayList();

        java.util.Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            FPSObject value = (FPSObject) entry.getValue();

            if (value != null && (key == null || key.intValue() <= 0))
                list.add(key);
        }

        iter = list.iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            FPSObject value = (FPSObject) map.remove(key);
            if (value != null)
                map.put(value.getId(), value);
        }
        list = null;
    }

    protected static synchronized void removeNullValues(Map map) {

        if (map == null)
            return;

        ArrayList list = new ArrayList();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (value == null)
                list.add(key);
        }

        iter = list.iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            map.remove(key);
        }
        list = null;
    }

}
