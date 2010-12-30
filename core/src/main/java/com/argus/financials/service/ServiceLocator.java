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

import org.springframework.context.ApplicationContext;

import com.argus.financials.domain.hibernate.User;
import com.argus.financials.security.UserPreferences;

public final class ServiceLocator {

    public static final String APP_VERSION = "1.02.b01";

//    public static final String REQUIRED_DBVERSION = "FPS.00.00";
    public static final String REQUIRED_DBVERSION = "FPS.02.00"; //MsSql

    private static ServiceLocator instance = new ServiceLocator();

    private ApplicationContext applicationContext;

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
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param applicationContext the applicationContext to set
     * @throws Exception 
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public UserPreferences getUserPreferences()
    {
        return (UserPreferences) applicationContext.getBean("userPreferences");
    }

    /**
     * do login on rmi server
     */
    public void login(String login, String password) throws Exception {
        // do it just in case
        // logout();
        try {
            User user = getUserService().login(login, password);
        } catch (Exception e) {
            lastError = e.getMessage();
            throw e;
        }
    }

    public void logout() {
        getUserService().logout();
    }

    public String getDBVersion() {
        try {
            return getUtilityService().getDBVersion();
        } catch (ServiceException e) {
            return null;
        }
    }

    public String getDBServerVersion() {
        try {
            return getUtilityService().getDBServerVersion();
        } catch (ServiceException e) {
            return "___???___";
        }
    }

    public DataSource getDataSource() {
        return (DataSource) applicationContext.getBean("dataSource");
    }

    public UtilityService getUtilityService() {
        return (UtilityService) applicationContext.getBean("utilityService");
    }

    public UserService getUserService() {
        UserService person = (UserService) applicationContext.getBean("userService");
        return person;
    }

    public ClientService getClientPerson() {
        ClientService client = (ClientService) applicationContext.getBean("clientService");
        return client == null || client.getPrimaryKey() == null ? null : client;
    }

    public Integer getUserPersonID() {
        return (Integer) getUserService().getPrimaryKey();
    }

    public void setClientPersonID(Integer value) throws Exception {
        ClientService client = (ClientService) applicationContext.getBean("clientService");
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