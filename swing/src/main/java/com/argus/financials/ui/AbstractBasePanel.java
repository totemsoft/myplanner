package com.argus.financials.ui;

import com.argus.bean.BasePanel;
import com.argus.financials.service.ClientService;

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