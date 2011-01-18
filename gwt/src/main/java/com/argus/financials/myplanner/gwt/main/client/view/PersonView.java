package com.argus.financials.myplanner.gwt.main.client.view;

import com.argus.financials.domain.client.IDateTime;
import com.argus.financials.myplanner.gwt.commons.client.AddListBoxItemsCallback;
import com.argus.financials.myplanner.gwt.commons.client.PersonProxy;
import com.argus.financials.myplanner.gwt.main.client.RefDataServiceAsync;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

public class PersonView extends Composite implements Editor<PersonProxy>
{
    private ListBox title;
    private TextBox surname;
    private TextBox firstname;
    private TextBox othernames;
    private RadioButton genderMale;
    private RadioButton genderFemale;
    private ListBox marital;
    private DateBox dateOfBirth;

    public PersonView(PersonProxy person, ChangeHandler handler)
    {
        Grid grid = new Grid(7, 2);
        grid.setStyleName("border");
        initWidget(grid);
        
        Label label = new Label("Title");
        grid.setWidget(0, 0, label);
        
        title = new ListBox();
        addTitleCodes(title);
        title.addChangeHandler(handler);
        grid.setWidget(0, 1, title);

        Label label_1 = new Label("Surname");
        grid.setWidget(1, 0, label_1);
        
        surname = new TextBox();
        surname.setText(person.getSurname());
        surname.addChangeHandler(handler);
        grid.setWidget(1, 1, surname);

        Label label_2 = new Label("Firstname");
        grid.setWidget(2, 0, label_2);
        
        firstname = new TextBox();
        firstname.setText(person.getFirstname());
        firstname.addChangeHandler(handler);
        grid.setWidget(2, 1, firstname);
        
        Label label_3 = new Label("Other names");
        grid.setWidget(3, 0, label_3);
        
        othernames = new TextBox();
        othernames.setText(person.getOtherNames());
        othernames.addChangeHandler(handler);
        grid.setWidget(3, 1, othernames);
        
        Label label_4 = new Label("Gender");
        grid.setWidget(4, 0, label_4);
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        grid.setWidget(4, 1, horizontalPanel);

        genderMale = new RadioButton("gender", "Male");
        //genderMale.addValueChangeHandler(handler);
        horizontalPanel.add(genderMale);
        
        genderFemale = new RadioButton("gender", "Female");
        //genderFemale.addValueChangeHandler(handler);
        horizontalPanel.add(genderFemale);
        
        Label label_5 = new Label("Marital Status");
        grid.setWidget(5, 0, label_5);
        
        marital = new ListBox();
        addMaritalCodes(marital);
        marital.addChangeHandler(handler);
        grid.setWidget(5, 1, marital);

        Label label_6 = new Label("Date of Birth");
        grid.setWidget(6, 0, label_6);
        
        dateOfBirth = new DateBox();
        dateOfBirth.setFormat(new DefaultFormat(DateTimeFormat.getFormat(IDateTime.DEFAULT_DATE)));
        dateOfBirth.setValue(person.getDateOfBirth());
        //dateOfBirth.addValueChangeHandler(handler);
        grid.setWidget(6, 1, dateOfBirth);
    }

    private void addMaritalCodes(ListBox marital)
    {
        RefDataServiceAsync.Util.getInstance().findMaritalCodes(new AddListBoxItemsCallback(marital));
    }

    private void addTitleCodes(ListBox title)
    {
        RefDataServiceAsync.Util.getInstance().findTitileCodes(new AddListBoxItemsCallback(title));
    }

}