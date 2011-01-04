package com.argus.financials.myplanner.gwt.main.client.view;

import com.argus.financials.myplanner.gwt.commons.client.AddListBoxItemsCallback;
import com.argus.financials.myplanner.gwt.main.client.RefDataServiceAsync;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class AddressView extends Composite
{

    private TextBox streetNumber;
    
    private TextBox streetNumber2;

    private ListBox state;

    private ListBox country;

    private TextBox suburb;
    
    private TextBox postcode;

    public AddressView()
    {
        Grid grid = new Grid(6, 2);
        grid.setStyleName("border");
        initWidget(grid);

        Label label = new Label("Address");
        grid.setWidget(0, 0, label);
        
        streetNumber = new TextBox();
        grid.setWidget(0, 1, streetNumber);

        streetNumber2 = new TextBox();
        grid.setWidget(1, 1, streetNumber2);

        Label label_2 = new Label("Country");
        grid.setWidget(2, 0, label_2);
        
        country = new ListBox();
        country.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                addStates(country.getValue(country.getSelectedIndex()));
            }
        });
        grid.setWidget(2, 1, country);
        addCountries(country);
        
        Label label_3 = new Label("State");
        grid.setWidget(3, 0, label_3);
        
        state = new ListBox();
        grid.setWidget(3, 1, state);
        
        Label label_4 = new Label("Suburb");
        grid.setWidget(4, 0, label_4);
        
        suburb = new TextBox();
        grid.setWidget(4, 1, suburb);

        Label label_5 = new Label("Postcode");
        grid.setWidget(5, 0, label_5);
        
        postcode = new TextBox();
        grid.setWidget(5, 1, postcode);
    }

    private void addCountries(ListBox country)
    {
        RefDataServiceAsync.Util.getInstance().findCountries(new AddListBoxItemsCallback(country));
    }

    private void addStates(String countryId)
    {
        RefDataServiceAsync.Util.getInstance().findStates(countryId, new AddListBoxItemsCallback(state));
    }

}