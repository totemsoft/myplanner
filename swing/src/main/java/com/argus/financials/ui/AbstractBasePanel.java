package com.argus.financials.ui;

import com.argus.bean.BasePanel;
import com.argus.financials.service.ClientService;

/**
 * 
 * @author shibaevv
 */
public class AbstractBasePanel extends BasePanel {

    protected static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        AbstractBasePanel.clientService = clientService;
    }

}