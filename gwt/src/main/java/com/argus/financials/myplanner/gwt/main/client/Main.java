package com.argus.financials.myplanner.gwt.main.client;

import com.argus.financials.myplanner.gwt.main.client.view.ClientSearch;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class Main implements EntryPoint, ValueChangeHandler<String>
{

    public static final String HISTORY_TOKEN = "main";

    private SimplePanel centerPanel;

    /* (non-Javadoc)
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public void onModuleLoad()
    {
        RootPanel rootPanel = RootPanel.get();
        rootPanel.setSize("100%", "100%");
        
        DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
        rootPanel.add(dockLayoutPanel, 0, 0);
        dockLayoutPanel.setSize("100%", "100%");
        
        MenuBar menuBar = new MenuBar(false);
        dockLayoutPanel.addNorth(menuBar, 2.0);
        menuBar.setSize("100%", "100%");
        MenuBar menuBar_1 = new MenuBar(true);
        
        MenuItem mntmFileMenu = new MenuItem("File", false, menuBar_1);
        
        MenuItem mntmExport = new MenuItem("Export", false, (Command) null);
        mntmExport.setEnabled(false);
        menuBar_1.addItem(mntmExport);
        
        MenuItem mntmImport = new MenuItem("Import", false, (Command) null);
        mntmImport.setEnabled(false);
        mntmImport.setHTML("Import");
        menuBar_1.addItem(mntmImport);
        
        MenuItemSeparator separator = new MenuItemSeparator();
        menuBar_1.addSeparator(separator);
        
        MenuItem mntmExit = new MenuItem("Logout", false, new Command() {
            public void execute() {
                onLogout();
            }
        });
        menuBar_1.addItem(mntmExit);
        menuBar.addItem(mntmFileMenu);
        MenuBar menuBar_2 = new MenuBar(true);
        
        MenuItem mntmClient = new MenuItem("Client", false, menuBar_2);
        
        MenuItem mntmSearch = new MenuItem("Search", false, new Command() {
            public void execute() {
                onClientSearch();
            }
        });
        menuBar_2.addItem(mntmSearch);
        
        MenuItem mntmAddNew = new MenuItem("Add New", false, (Command) null);
        menuBar_2.addItem(mntmAddNew);
        
        MenuItem mntmClientDetails = new MenuItem("Details", false, (Command) null);
        mntmClientDetails.setEnabled(false);
        menuBar_2.addItem(mntmClientDetails);
        
        MenuItem mntmClientRisk = new MenuItem("Risk", false, (Command) null);
        mntmClientRisk.setEnabled(false);
        menuBar_2.addItem(mntmClientRisk);
        
        MenuItem mntmFinancials = new MenuItem("Financials", false, (Command) null);
        mntmFinancials.setEnabled(false);
        menuBar_2.addItem(mntmFinancials);
        menuBar.addItem(mntmClient);
        MenuBar menuBar_3 = new MenuBar(true);
        
        MenuItem mntmPartner = new MenuItem("Partner", false, menuBar_3);
        
        MenuItem mntmPartnerDetails = new MenuItem("Details", false, (Command) null);
        mntmPartnerDetails.setEnabled(false);
        menuBar_3.addItem(mntmPartnerDetails);
        
        MenuItem mntmPartnerRisk = new MenuItem("Risk", false, (Command) null);
        mntmPartnerRisk.setEnabled(false);
        menuBar_3.addItem(mntmPartnerRisk);
        menuBar.addItem(mntmPartner);
        MenuBar menuBar_4 = new MenuBar(true);
        
        MenuItem mntmStrategy = new MenuItem("Strategy", false, menuBar_4);
        menuBar.addItem(mntmStrategy);
        MenuBar menuBar_5 = new MenuBar(true);
        
        MenuItem mntmAnalysis = new MenuItem("Analysis", false, menuBar_5);
        menuBar.addItem(mntmAnalysis);
        MenuBar menuBar_6 = new MenuBar(true);
        
        MenuItem mntmPlan = new MenuItem("Plan", false, menuBar_6);
        menuBar.addItem(mntmPlan);
        MenuBar menuBar_7 = new MenuBar(true);
        
        MenuItem mntmTools = new MenuItem("Tools", false, menuBar_7);
        menuBar.addItem(mntmTools);
        MenuBar menuBar_8 = new MenuBar(true);
        
        MenuItem mntmHelp = new MenuItem("Help", false, menuBar_8);
        menuBar.addItem(mntmHelp);
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setStyleName("border");
        dockLayoutPanel.addSouth(horizontalPanel, 1.2);
        horizontalPanel.setSize("100%", "100%");
        
        centerPanel = new SimplePanel();
        dockLayoutPanel.add(centerPanel);

        //
        String historyToken = History.getToken();
        if (historyToken == null || historyToken.length() == 0) {
            History.newItem(HISTORY_TOKEN);
            History.addValueChangeHandler(this);
            //Window.alert("History Token added: " + HISTORY_TOKEN);
        }
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
     */
    public void onValueChange(ValueChangeEvent<String> event) {
        String historyToken = event.getValue();
        // parse url fragment
        if (historyToken.endsWith(HISTORY_TOKEN)) {
            
        } else if (historyToken.endsWith(ClientSearch.HISTORY_TOKEN)) {
            onClientSearch();
        }
    }

    private void onClientSearch() {
        centerPanel.add(new ClientSearch());
    }

    private void onLogout() {
        MainServiceAsync.Util.getInstance().logout(new LogoutCallback());
    }

    private static class LogoutCallback implements AsyncCallback<Void> {
        public void onFailure(Throwable t) {
            Window.alert("Failure: " + t.getMessage());
        }
        public void onSuccess(Void result) {
            Window.Location.replace("Login.html");
        }
    }

}