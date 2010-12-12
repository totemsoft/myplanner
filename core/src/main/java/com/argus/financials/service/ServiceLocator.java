/*
 * RmiParams.java
 *
 * Created on 8 August 2001, 09:56
 */

package com.argus.financials.service;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import javax.sql.DataSource;

import org.springframework.context.support.AbstractApplicationContext;

import com.argus.financials.code.ReferenceDataLoader;
import com.argus.financials.config.FPSLocale;

public final class ServiceLocator {

    public static final String APP_VERSION = "1.02.b01";

//    public static final String REQUIRED_DBVERSION = "FPS.00.00";
    public static final String REQUIRED_DBVERSION = "FPS.02.00"; //MsSql

    private static ServiceLocator instance = new ServiceLocator();

    private AbstractApplicationContext applicationContext;

    protected String lastError;

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        return instance;
    }

    public String getLastError() {
        return lastError;
    }

    /**
     * @return the applicationContext
     */
    public AbstractApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param applicationContext the applicationContext to set
     */
    public void setApplicationContext(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * do login on rmi server
     */
    public boolean login(String serverURL, String userName, String userPassword) {

        FPSLocale.getInstance().setServerURL(serverURL);

        // do it just in case
        // logout();

        // pre-load ALL referewnce codes
        Thread t = new Thread(new ReferenceDataLoader(), "ReferenceDataLoader");

        try {
            UserService userPerson = getUserPerson().findByLoginNamePassword(userName, userPassword);
            Integer userPersonID = (Integer) userPerson.getPrimaryKey();
        } catch (Exception e) {
            lastError = e.getMessage();
            e.printStackTrace();
            return false;
        }

        t.start();

        return true;

    }

    public void logout() {

    }

    public String getDBVersion() {
        try {
            return getUtility().getDBVersion();
        } catch (ServiceException e) {
            return null;
        }
    }

    public String getDBServerVersion() {
        try {
            return getUtility().getDBServerVersion();
        } catch (ServiceException e) {
            return "___???___";
        }
    }

    public DataSource getDataSource() {
        return (DataSource) applicationContext.getBean("dataSource");
    }

    public UtilityService getUtility() {
        return (UtilityService) applicationContext.getBean("utility");
    }

    /**
     * get/set methodes
     */
    public String getServerURL() {
        return FPSLocale.getInstance().getServerURL();
    }

    public UserService getUserPerson() {
        UserService person = (UserService) applicationContext.getBean("userPerson");
        return person;
    }

    public ClientService getClientPerson() {
        ClientService client = (ClientService) applicationContext.getBean("clientPerson");
        return client == null || client.getPrimaryKey() == null ? null : client;
    }

    public Integer getUserPersonID() {
        return (Integer) getUserPerson().getPrimaryKey();
    }

    public void setClientPersonID(Integer value) throws Exception {
        ClientService client = (ClientService) applicationContext.getBean("clientPerson");
        client.findByPrimaryKey(value);
    }

    public Integer getClientPersonID() {
        ClientService client = getClientPerson();
        return client == null ? null : (Integer) client.getPrimaryKey();
    }

    public Integer createClientPerson() throws Exception {
        if (getUserPersonID() == null)
            return null;
        return (Integer) getClientPerson().create(getUserPersonID(), true)
                .getPrimaryKey();
    }

}
