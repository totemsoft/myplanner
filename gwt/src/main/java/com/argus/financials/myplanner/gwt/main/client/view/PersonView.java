package com.argus.financials.myplanner.gwt.main.client.view;

import com.argus.financials.myplanner.gwt.commons.client.PersonProxy;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class PersonView extends Composite implements Editor<PersonProxy>
{
    private ListBox title;
    private TextBox surname;
    private TextBox firstname;
    private TextBox othernames;
    private RadioButton genderMale;
    private RadioButton genderFemale;
    private ListBox maritalStatus;
    private DateBox dateOfBirth;

    public PersonView()
    {
        Grid grid = new Grid(7, 2);
        grid.setStyleName("border");
        initWidget(grid);
        
        Label label = new Label("Title");
        grid.setWidget(0, 0, label);
        
        title = new ListBox();
        grid.setWidget(0, 1, title);

        Label label_1 = new Label("Surname");
        grid.setWidget(1, 0, label_1);
        
        surname = new TextBox();
        grid.setWidget(1, 1, surname);

        Label label_2 = new Label("Firstname");
        grid.setWidget(2, 0, label_2);
        
        firstname = new TextBox();
        grid.setWidget(2, 1, firstname);
        
        Label label_3 = new Label("Other names");
        grid.setWidget(3, 0, label_3);
        
        othernames = new TextBox();
        grid.setWidget(3, 1, othernames);
        
        Label label_4 = new Label("Gender");
        grid.setWidget(4, 0, label_4);
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        grid.setWidget(4, 1, horizontalPanel);

        genderMale = new RadioButton("gender", "Male");
        horizontalPanel.add(genderMale);
        
        genderFemale = new RadioButton("gender", "Female");
        horizontalPanel.add(genderFemale);
        
        Label label_5 = new Label("Marital Status");
        grid.setWidget(5, 0, label_5);
        
        maritalStatus = new ListBox();
        grid.setWidget(5, 1, maritalStatus);

        Label label_6 = new Label("Date of Birth");
        grid.setWidget(6, 0, label_6);
        
        dateOfBirth = new DateBox();
        grid.setWidget(6, 1, dateOfBirth);
    }

    public void onView(PersonProxy person)
    {
        firstname.setText(person.getFirstname());
        surname.setText(person.getSurname());
        othernames.setText(person.getOtherNames());
        dateOfBirth.setValue(person.getDateOfBirth());
    }

    public void onSave(PersonProxy person)
    {
        person.setFirstname(firstname.getText());
        person.setSurname(surname.getText());
        person.setOtherNames(othernames.getText());
        person.setDateOfBirth(dateOfBirth.getValue());
    }

}