package com.argus.financials.service;

import com.argus.financials.api.bean.UserPreferences;

public class ServiceAware {

    protected transient static ClientService clientService;
    protected transient static UserPreferences userPreferences;
    protected transient static UtilityService utilityService;

    public static void setClientService(ClientService clientService) {
        ServiceAware.clientService = clientService;
    }
    public static void setUserPreferences(UserPreferences userPreferences) {
        ServiceAware.userPreferences = userPreferences;
    }
    public static void setUtilityService(UtilityService utilityService) {
        ServiceAware.utilityService = utilityService;
    }

}