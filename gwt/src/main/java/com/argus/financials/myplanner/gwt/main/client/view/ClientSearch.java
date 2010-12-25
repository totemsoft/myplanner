package com.argus.financials.myplanner.gwt.main.client.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class ClientSearch extends Composite
{

    public static final String HISTORY_TOKEN = "clientSearch";

    public ClientSearch() {
        
        DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
        initWidget(dockLayoutPanel);

        Grid grid = new Grid(3, 5);
        dockLayoutPanel.addNorth(grid, 7.0);
        grid.setStyleName("nonDataTable");
        grid.setSize("100%", "100%");
        
        Label label = new Label("Surname");
        grid.setWidget(0, 0, label);
        
        TextBox textBox = new TextBox();
        grid.setWidget(0, 1, textBox);
        
        Label label_1 = new Label("DOB");
        grid.setWidget(0, 2, label_1);
                
        DateBox dateBox = new DateBox();
        grid.setWidget(0, 3, dateBox);
        
        Button button = new Button("Search");
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                findClients();
            }
        });
        grid.setWidget(0, 4, button);
        
        Label label_2 = new Label("Firstname");
        grid.setWidget(1, 0, label_2);

        TextBox textBox_1 = new TextBox();
        grid.setWidget(1, 1, textBox_1);

        Label label_3 = new Label("State");
        grid.setWidget(1, 2, label_3);
        
        ListBox comboBox = new ListBox();
        grid.setWidget(1, 3, comboBox);
        
        Label label_4 = new Label("Country");
        grid.setWidget(2, 0, label_4);
        
        ListBox comboBox_1 = new ListBox();
        grid.setWidget(2, 1, comboBox_1);

        Label label_5 = new Label("Postcode");
        grid.setWidget(2, 2, label_5);
        
        TextBox textBox_2 = new TextBox();
        grid.setWidget(2, 3, textBox_2);

        History.newItem(HISTORY_TOKEN);
    }

    private void findClients()
    {
        // TODO Auto-generated method stub
        
    }

}