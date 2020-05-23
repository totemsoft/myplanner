package au.com.totemsoft.myplanner.swing;

import au.com.totemsoft.bean.BasePanel;
import au.com.totemsoft.myplanner.service.ClientService;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class AbstractBasePanel extends BasePanel {

    protected static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        AbstractBasePanel.clientService = clientService;
    }

}