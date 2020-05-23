package au.com.totemsoft.myplanner.swing;

import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.api.service.FinancialService;
import au.com.totemsoft.myplanner.api.service.UserService;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.UtilityService;
import au.com.totemsoft.myplanner.service.client.EntityService;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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