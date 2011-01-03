package com.argus.financials.myplanner.gwt.main.client;

import com.argus.financials.myplanner.gwt.commons.client.AbstractAsyncCallback;
import com.argus.financials.myplanner.gwt.main.client.view.ClientDetails;
import com.argus.financials.myplanner.gwt.main.client.view.ClientSearch;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Main implements EntryPoint, ValueChangeHandler<String> {

    public static final String TITLE = "MyPlanner :: ";

    private ScrollPanel centerPanel;

    /* (non-Javadoc)
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public void onModuleLoad() {
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
                showView(ClientSearch.HISTORY_TOKEN);
            }
        });
        menuBar_2.addItem(mntmSearch);
        
        MenuItem mntmAddNew = new MenuItem("Add New", false, (Command) null);
        menuBar_2.addItem(mntmAddNew);
        
        MenuItem mntmClientDetails = new MenuItem("Details", false, new Command() {
            public void execute() {
                showView(ClientDetails.HISTORY_TOKEN);
            }
        });
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
        
        centerPanel = new ScrollPanel();
        dockLayoutPanel.add(centerPanel);
        
        History.addValueChangeHandler(this);

        showView(ClientSearch.HISTORY_TOKEN); // default
    }

    private void showView(String historyToken) {
        if (History.getToken().equals(historyToken)) {
            History.fireCurrentHistoryState();
        } else {
            History.newItem(historyToken);
        }
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
     */
    public void onValueChange(ValueChangeEvent<String> event) {
        String historyToken = event.getValue();
        if (historyToken.length() == 0) {
            Window.alert("Empty History Token");
            return;
        }

        // for DEBUG only
        //Window.alert("History Token changed: " + historyToken);
        // parse url fragment
        if (historyToken.equals(ClientSearch.HISTORY_TOKEN)) {
            centerPanel.setWidget(new ClientSearch());
        } else if (historyToken.equals(ClientDetails.HISTORY_TOKEN)) {
            centerPanel.setWidget(new ClientDetails());
        } else {
            Window.alert("Unhandled History Token: " + historyToken);
        }
    }

    private void onLogout() {
        MainServiceAsync.Util.getInstance().logout(new LogoutCallback());
    }

    private class LogoutCallback extends AbstractAsyncCallback<Void> {
        public void onSuccess(Void result) {
            Window.Location.replace("Login.html");
        }
    }

}