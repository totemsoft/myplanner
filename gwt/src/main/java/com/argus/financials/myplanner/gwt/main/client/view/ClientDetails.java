package com.argus.financials.myplanner.gwt.main.client.view;

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

    public ClientDetails()
    {
        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(1.5, Unit.EM);
        tabLayoutPanel.setHeight("40.5em");
        initWidget(tabLayoutPanel);
        
        HorizontalPanel details = new HorizontalPanel();
        tabLayoutPanel.add(details, "Client Details", false);
        details.setSize("100%", "100%");
        
        VerticalPanel leftPanel = new VerticalPanel();
        details.add(leftPanel);
        
        PersonView personView = new PersonView();
        leftPanel.add(personView);
        
        AddressView addressView = new AddressView();
        leftPanel.add(addressView);
        
        CheckBox sameAsAbove = new CheckBox("Same As Above");
        leftPanel.add(sameAsAbove);
        
        AddressView postalAddressView = new AddressView();
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
        contacts.setSize("100%", "100%");


        Window.setTitle(Main.TITLE + TITLE);
    }

}