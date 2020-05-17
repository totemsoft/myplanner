package com.argus.financials.ui;

import com.argus.financials.api.bean.UserPreferences;
import com.argus.financials.api.service.FinancialService;
import com.argus.financials.api.service.UserService;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.UtilityService;
import com.argus.financials.service.client.EntityService;

/**
 * 
 * @author shibaevv
 */
public abstract class AbstractPanel extends javax.swing.JPanel {

    /** serialVersionUID */
    private static final long serialVersionUID = -199157540327306299L;

    protected static ClientService clientService;
    protected static EntityService entityService;
    protected static FinancialService financialService;
    protected static UserPreferences userPreferences;
    protected static UserService userService;
    protected static UtilityService utilityService;
    public static void setClientService(ClientService clientService) {
        AbstractPanel.clientService = clientService;
    }
    public static void setEntityService(EntityService entityService) {
        AbstractPanel.entityService = entityService;
    }
    public static void setFinancialService(FinancialService financialService) {
        AbstractPanel.financialService = financialService;
    }
    public static void setUserPreferences(UserPreferences userPreferences) {
        AbstractPanel.userPreferences = userPreferences;
    }
    public static void setUserService(UserService userService) {
        AbstractPanel.userService = userService;
    }
    public static void setUtilityService(UtilityService utilityService) {
        AbstractPanel.utilityService = utilityService;
    }

}