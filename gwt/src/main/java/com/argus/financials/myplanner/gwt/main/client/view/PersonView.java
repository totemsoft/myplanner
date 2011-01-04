package com.argus.financials.myplanner.gwt.main.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

public class PersonView extends Composite
{

    public PersonView()
    {
        Grid grid = new Grid(6, 2);
        grid.setStyleName("border");
        initWidget(grid);
        
        Label label = new Label("Title");
        grid.setWidget(0, 0, label);
        
        ListBox title = new ListBox();
        grid.setWidget(0, 1, title);

        Label label_1 = new Label("Surname");
        grid.setWidget(1, 0, label_1);
        
        TextBox surname = new TextBox();
        grid.setWidget(1, 1, surname);

        Label label_2 = new Label("Firstname");
        grid.setWidget(2, 0, label_2);
        
        TextBox firstname = new TextBox();
        grid.setWidget(2, 1, firstname);
        
        Label label_3 = new Label("Other names");
        grid.setWidget(3, 0, label_3);
        
        TextBox othernames = new TextBox();
        grid.setWidget(3, 1, othernames);
        
        Label label_4 = new Label("Gender");
        grid.setWidget(4, 0, label_4);
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        grid.setWidget(4, 1, horizontalPanel);

        RadioButton genderMale = new RadioButton("gender", "Male");
        horizontalPanel.add(genderMale);
        
        RadioButton genderFemale = new RadioButton("gender", "Female");
        horizontalPanel.add(genderFemale);
        
        Label label_5 = new Label("Marital Status");
        grid.setWidget(5, 0, label_5);
        
        ListBox maritalStatus = new ListBox();
        grid.setWidget(5, 1, maritalStatus);
    }

}