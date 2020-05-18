package com.argus.financials.myplanner.gwt.main.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.argus.financials.myplanner.gwt.commons.client.AbstractAsyncCallback;
import com.argus.financials.myplanner.gwt.commons.client.AbstractReceiver;
import com.argus.financials.myplanner.gwt.commons.client.BasePair;
import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.argus.financials.myplanner.gwt.commons.shared.GwtRequestFactory;
import com.argus.financials.myplanner.gwt.main.client.view.ClientDetails;
import com.argus.financials.myplanner.gwt.main.client.view.ClientSearch;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Main implements EntryPoint, ValueChangeHandler<String> {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static final String TITLE = "MyPlanner :: ";

    private final EventBus eventBus = new SimpleEventBus();

    private final GwtRequestFactory requestFactory = GWT.create(GwtRequestFactory.class);
    
    private ScrollPanel centerPanel;

    private HorizontalPanel footerPanel;

    private MenuItem mntmExport;
    private MenuItem mntmImport;
    private MenuItem mntmClientDetails;
    private MenuItem mntmClientRisk;
    private MenuItem mntmFinancials;
    private MenuItem mntmPartnerDetails;
    private MenuItem mntmPartnerRisk;
    private MenuItem mntmStrategy;
    private MenuItem mntmAnalysis;
    private MenuItem mntmPlan;
    
    private ClientSearch clientSearch;

    /* (non-Javadoc)
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler()
        {
            public void onUncaughtException(Throwable e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        });

        requestFactory.initialize(eventBus);

        RootPanel rootPanel = RootPanel.get();
        rootPanel.setSize("100%", "100%");
        
        DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
        rootPanel.add(dockLayoutPanel, 0, 0);
        dockLayoutPanel.setSize("100%", "100%");
        
        MenuBar menuBar = new MenuBar(false);
        dockLayoutPanel.addNorth(menuBar, 2.0);
        menuBar.setSize("100%", "100%");
        MenuBar menuBarFile = new MenuBar(true);
        
        MenuItem mntmFileMenu = new MenuItem("File", false, menuBarFile);
        
        mntmExport = new MenuItem("Export", false, (Command) null);
        mntmExport.setEnabled(false);
        menuBarFile.addItem(mntmExport);
        
        mntmImport = new MenuItem("Import", false, (Command) null);
        mntmImport.setEnabled(false);
        mntmImport.setHTML("Import");
        menuBarFile.addItem(mntmImport);
        
        MenuItemSeparator separator = new MenuItemSeparator();
        menuBarFile.addSeparator(separator);
        
        MenuItem mntmExit = new MenuItem("Logout", false, logoutCommand);
        menuBarFile.addItem(mntmExit);
        menuBar.addItem(mntmFileMenu);
        MenuBar menuBarClient = new MenuBar(true);
        
        MenuItem mntmClient = new MenuItem("Client", false, menuBarClient);
        
        MenuItem mntmSearch = new MenuItem("Search", false, searchClient);
        menuBarClient.addItem(mntmSearch);
        
        MenuItem mntmAddNewClient = new MenuItem("Add New", false, addNewClientCommand);
        menuBarClient.addItem(mntmAddNewClient);
        
        mntmClientDetails = new MenuItem("Details", false, clientDetailsCommand);
        mntmClientDetails.setEnabled(false);
        menuBarClient.addItem(mntmClientDetails);
        
        mntmClientRisk = new MenuItem("Risk", false, (Command) null);
        mntmClientRisk.setEnabled(false);
        menuBarClient.addItem(mntmClientRisk);
        
        mntmFinancials = new MenuItem("Financials", false, (Command) null);
        mntmFinancials.setEnabled(false);
        menuBarClient.addItem(mntmFinancials);
        menuBar.addItem(mntmClient);
        MenuBar menuBarPartner = new MenuBar(true);
        
        MenuItem mntmPartner = new MenuItem("Partner", false, menuBarPartner);
        
        mntmPartnerDetails = new MenuItem("Details", false, (Command) null);
        mntmPartnerDetails.setEnabled(false);
        menuBarPartner.addItem(mntmPartnerDetails);
        
        mntmPartnerRisk = new MenuItem("Risk", false, (Command) null);
        mntmPartnerRisk.setEnabled(false);
        menuBarPartner.addItem(mntmPartnerRisk);
        menuBar.addItem(mntmPartner);
        MenuBar menuBar_4 = new MenuBar(true);
        
        mntmStrategy = new MenuItem("Strategy", false, menuBar_4);
        mntmStrategy.setEnabled(false);
        menuBar.addItem(mntmStrategy);
        MenuBar menuBar_5 = new MenuBar(true);
        
        mntmAnalysis = new MenuItem("Analysis", false, menuBar_5);
        mntmAnalysis.setEnabled(false);
        menuBar.addItem(mntmAnalysis);
        MenuBar menuBar_6 = new MenuBar(true);
        
        mntmPlan = new MenuItem("Plan", false, menuBar_6);
        menuBar.addItem(mntmPlan);
        MenuBar menuBar_7 = new MenuBar(true);
        
        MenuItem mntmTools = new MenuItem("Tools", false, menuBar_7);
        menuBar.addItem(mntmTools);
        MenuBar menuBar_8 = new MenuBar(true);
        
        MenuItem mntmHelp = new MenuItem("Help", false, menuBar_8);
        menuBar.addItem(mntmHelp);
        
        footerPanel = new HorizontalPanel();
        footerPanel.setStyleName("border");
        dockLayoutPanel.addSouth(footerPanel, 1.2);
        footerPanel.setSize("100%", "100%");
        
        centerPanel = new ScrollPanel();
        dockLayoutPanel.add(centerPanel);
        
        History.addValueChangeHandler(this);

        showView(ClientSearch.HISTORY_TOKEN); // default
        
        LOG.log(Level.INFO, "[Main] view succesfully loaded.");
    }

    public void showView(String historyToken) {
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
            LOG.log(Level.WARNING, "Empty History Token");
            return;
        }

        LOG.log(Level.INFO, "History Token changed: " + historyToken);
        // parse url fragment
        if (historyToken.equals(ClientSearch.HISTORY_TOKEN)) {
            if (clientSearch == null) {
                clientSearch = new ClientSearch(this);
            }
            centerPanel.setWidget(clientSearch);
        } else if (historyToken.equals(ClientDetails.HISTORY_TOKEN)) {
            BasePair client = clientSearch.getSelectedClient();
            Long clientId = client == null ? null : client.getFirst().longValue();
            openClient(clientId);
        } else {
            LOG.log(Level.WARNING, "Unhandled History Token: " + historyToken);
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

    private void openClient(Long clientId)
    {
        // When querying the server, RequestFactory does not automatically populate relations in the object graph.
        // To do this, use the with() method on a request and specify the related property name as a String
        Request<ClientProxy> request = requestFactory.clientRequest().findClient(clientId);//.with("address");
        request.fire(new AbstractReceiver<ClientProxy>()
        {
            @Override
            public void onSuccess(ClientProxy client)
            {
                centerPanel.setWidget(new ClientDetails(client, eventBus, requestFactory));
                updateMenu(client);
            }
        });
    }

    private void updateMenu(ClientProxy client)
    {
        // menu
        mntmExport.setEnabled(true);
        mntmImport.setEnabled(true);
        mntmClientDetails.setEnabled(true);
        mntmClientRisk.setEnabled(true);
        mntmFinancials.setEnabled(true);
        boolean isSingle = true; // MaritalCode.isSingle(client.getPersonName().getMaritalCodeID())
        mntmPartnerDetails.setEnabled(!isSingle);
        mntmPartnerRisk.setEnabled(!isSingle);
        mntmStrategy.setEnabled(true);
        mntmAnalysis.setEnabled(true);
        mntmPlan.setEnabled(true);
        // footer
        footerPanel.clear();
        footerPanel.add(new Label("Client: " + client.getSurname()));
    }

    private Command searchClient = new Command()
    {
        public void execute()
        {
            showView(ClientSearch.HISTORY_TOKEN);
        }
    };

    private Command clientDetailsCommand = new Command()
    {
        public void execute()
        {
            showView(ClientDetails.HISTORY_TOKEN);
        }
    };

    private Command addNewClientCommand = new Command()
    {
        public void execute()
        {
            Request<Long> request = requestFactory.clientControllerRequest().persist(null);
            request.fire(new AbstractReceiver<Long>()
            {
                @Override
                public void onSuccess(final Long clientId)
                {
                    MainServiceAsync.Util.getInstance().openClient(clientId, new AbstractAsyncCallback<Void>()
                    {
                        public void onSuccess(Void result)
                        {
                            openClient(clientId);
                        }
                    });
                }
            });
        }
    };

    private Command logoutCommand = new Command()
    {
        public void execute()
        {
            onLogout();
        }
    };

}