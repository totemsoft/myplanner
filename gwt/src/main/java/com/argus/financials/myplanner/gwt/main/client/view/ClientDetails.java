package com.argus.financials.myplanner.gwt.main.client.view;

import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.argus.financials.myplanner.gwt.main.client.Main;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientDetails extends Composite
{

    public static final String TITLE = "Client Details";

    public static final String HISTORY_TOKEN = "clientDetails";

    private final PersonView personView;

    private final AddressView addressView;

    private final CheckBox sameAsAbove;

    private final AddressView postalAddressView;
    
    public ClientDetails()
    {
        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(1.5, Unit.EM);
        initWidget(tabLayoutPanel);
        tabLayoutPanel.setHeight("42em");
        
        HorizontalPanel details = new HorizontalPanel();
        tabLayoutPanel.add(details, "Client Details", false);
        
        VerticalPanel leftPanel = new VerticalPanel();
        details.add(leftPanel);
        
        personView = new PersonView();
        leftPanel.add(personView);
        
        addressView = new AddressView();
        leftPanel.add(addressView);
        
        sameAsAbove = new CheckBox("Same As Above");
        leftPanel.add(sameAsAbove);
        
        postalAddressView = new AddressView();
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
        
        Window.setTitle(Main.TITLE + TITLE);
    }

    public void setClient(ClientProxy client)
    {

    }

}