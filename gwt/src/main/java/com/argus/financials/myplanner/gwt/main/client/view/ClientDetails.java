package com.argus.financials.myplanner.gwt.main.client.view;

import com.argus.financials.myplanner.gwt.main.client.Main;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
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
        tabLayoutPanel.setHeight("50em");
        initWidget(tabLayoutPanel);
        
        HorizontalPanel details = new HorizontalPanel();
        tabLayoutPanel.add(details, "Client Details", false);
        details.setSize("100%", "100%");
        
        VerticalPanel leftPanel = new VerticalPanel();
        details.add(leftPanel);
        leftPanel.setSize("100%", "100%");
        
        PersonView personView = new PersonView();
        leftPanel.add(personView);
        
        Grid grid_1 = new Grid(5, 2);
        leftPanel.add(grid_1);
        
        Grid grid_2 = new Grid(4, 2);
        leftPanel.add(grid_2);
        
        VerticalPanel rightPanel = new VerticalPanel();
        details.add(rightPanel);
        rightPanel.setSize("100%", "100%");
        
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