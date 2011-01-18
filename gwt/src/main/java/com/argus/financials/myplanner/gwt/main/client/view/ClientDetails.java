package com.argus.financials.myplanner.gwt.main.client.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.argus.financials.myplanner.gwt.commons.shared.ClientRequest;
import com.argus.financials.myplanner.gwt.commons.shared.GwtRequestFactory;
import com.argus.financials.myplanner.gwt.main.client.Main;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientDetails extends Composite implements ChangeHandler
{

    private static final Logger LOG = Logger.getLogger(ClientDetails.class.getName());

    public static final String TITLE = "Client Details";

    public static final String HISTORY_TOKEN = "clientDetails";

    private final ClientProxy client;

    private final EventBus eventBus;

    private final GwtRequestFactory requestFactory;

    private final PersonView personView;

    private final AddressView addressView;

    private final CheckBox sameAsAbove;

    private final AddressView postalAddressView;

    private final Button buttonSave;

    public ClientDetails(ClientProxy client, EventBus eventBus, GwtRequestFactory requestFactory)
    {
        // Any objects not returned from RequestContext.create(), such as those received from the server,
        // must be enabled for changes by calling the RequestFactory's edit() method.
        // Any EntityProxies returned from the getters of an editable proxy are also editable.
        ClientRequest request = requestFactory.clientRequest();
        if (client == null)
        {
            this.client = request.create(ClientProxy.class);
        }
        else
        {
            this.client = client;//request.edit(client);
        }
        this.eventBus = eventBus;
        this.requestFactory = requestFactory;

        VerticalPanel verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        verticalPanel.setStyleName("mp-Panel-center");
        verticalPanel.setSize("30em", null);

        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(1.5, Unit.EM);
        verticalPanel.add(tabLayoutPanel);
        tabLayoutPanel.setHeight("43em");
        
        HorizontalPanel details = new HorizontalPanel();
        tabLayoutPanel.add(details, "Client Details", false);
        
        VerticalPanel leftPanel = new VerticalPanel();
        details.add(leftPanel);
        
        personView = new PersonView(client, this);
        leftPanel.add(personView);
        
        addressView = new AddressView(null);
        leftPanel.add(addressView);
        
        sameAsAbove = new CheckBox("Same As Above");
        leftPanel.add(sameAsAbove);
        
        postalAddressView = new AddressView(null);
        leftPanel.add(postalAddressView);
        
        VerticalPanel rightPanel = new VerticalPanel();
        details.add(rightPanel);
        
        Grid grid_3 = new Grid(4, 2);
        rightPanel.add(grid_3);
        
        Grid grid_4 = new Grid(4, 2);
        rightPanel.add(grid_4);
        
        Grid grid_5 = new Grid(3, 2);
        rightPanel.add(grid_5);

        HorizontalPanel contacts = new HorizontalPanel();
        tabLayoutPanel.add(contacts, "Contact Details", false);
        
        FlowPanel flowPanel = new FlowPanel();
        verticalPanel.add(flowPanel);
        
        buttonSave = new Button("Save");
        buttonSave.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event) {
                save();
            }
        });
        buttonSave.setEnabled(false);
        flowPanel.add(buttonSave);
        
        Window.setTitle(Main.TITLE + TITLE);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
     */
    public void onChange(ChangeEvent event)
    {
        LOG.log(Level.INFO, "onChange: " + event.toDebugString());
        buttonSave.setEnabled(true);//requestFactory.clientRequest().isChanged());
    }

    private void save()
    {
        LOG.log(Level.INFO, "save");
        ClientRequest request = requestFactory.clientRequest();
        request.persist(client).fire();
        //request.persist().using(client).fire();
    }

}