package com.argus.financials.myplanner.gwt.main.client.view;

import java.util.Arrays;

import com.argus.financials.myplanner.commons.client.AbstractAsyncCallback;
import com.argus.financials.myplanner.commons.client.BasePair;
import com.argus.financials.myplanner.commons.client.StringPair;
import com.argus.financials.myplanner.gwt.main.client.Main;
import com.argus.financials.myplanner.gwt.main.client.MainServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ClientSearch extends Composite
{

    public static final String TITLE = "Search Client";

    public static final String HISTORY_TOKEN = "clientSearch";

    private CellTable<BasePair> table;

    private ListBox stateComboBox;

    private ListBox countryComboBox;

    private TextBox surnameTextBox;

    private TextBox firstnameTextBox;

    private DateBox dobDateBox;

    private TextBox postcodeTextBox;

    public ClientSearch()
    {
        VerticalPanel verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        verticalPanel.setStyleName("mp-Panel-center");
        verticalPanel.setSize("30em", null);

        FormPanel formPanel = new FormPanel();
        verticalPanel.add(formPanel);
        formPanel.setMethod(FormPanel.METHOD_POST);

        Grid grid = new Grid(3, 5);
        formPanel.setWidget(grid);
        grid.setStyleName("nonDataTable");

        Label label = new Label("Surname");
        grid.setWidget(0, 0, label);

        surnameTextBox = new TextBox();
        surnameTextBox.setFocus(true);
        grid.setWidget(0, 1, surnameTextBox);

        Label label_1 = new Label("DOB");
        grid.setWidget(0, 2, label_1);

        dobDateBox = new DateBox();
        dobDateBox.setFormat(new DefaultFormat(DateTimeFormat
            .getFormat(PredefinedFormat.DATE_SHORT)));
        grid.setWidget(0, 3, dobDateBox);

        Button button = new Button("Search");
        button.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                onSearch();
            }
        });
        grid.setWidget(0, 4, button);

        Label label_2 = new Label("Firstname");
        grid.setWidget(1, 0, label_2);

        firstnameTextBox = new TextBox();
        grid.setWidget(1, 1, firstnameTextBox);

        Label label_3 = new Label("State");
        grid.setWidget(1, 2, label_3);

        stateComboBox = new ListBox();
        grid.setWidget(1, 3, stateComboBox);

        Label label_4 = new Label("Country");
        grid.setWidget(2, 0, label_4);

        countryComboBox = new ListBox();
        grid.setWidget(2, 1, countryComboBox);

        Label label_5 = new Label("Postcode");
        grid.setWidget(2, 2, label_5);

        postcodeTextBox = new TextBox();
        grid.setWidget(2, 3, postcodeTextBox);

        table = new CellTable<BasePair>();
        verticalPanel.add(table);
        table.setWidth("100%");
        table.setStyleName("border");
        table.setPageSize(10);

        Column<BasePair, String> columnId = new Column<BasePair, String>(new TextCell())
        {
            @Override
            public String getValue(BasePair object)
            {
                return object.getFirst().toString();
            }
        };
        table.addColumn(columnId, "Client ID");

        Column<BasePair, String> columnDetails = new Column<BasePair, String>(new TextCell())
        {
            @Override
            public String getValue(BasePair object)
            {
                return object.getSecond().toString();
            }
        };
        table.addColumn(columnDetails, "Client Details");
        table.setSelectionModel(new ClientSelectionModel());
        //table.setRowData(0, Collections.EMPTY_LIST);
        table.setRowCount(0, true);

        Window.setTitle(Main.TITLE + TITLE);
    }

    // Add a selection model to handle user selection.
    private class ClientSelectionModel extends SingleSelectionModel<BasePair>
    {
        public ClientSelectionModel()
        {
            addSelectionChangeHandler(new SelectionChangeEvent.Handler()
            {
                public void onSelectionChange(SelectionChangeEvent event)
                {
                    BasePair selected = ClientSelectionModel.this.getSelectedObject();
                    if (selected != null)
                    {
                        Window.alert("You selected: " + selected.toString());
                    }
                }
            });
        }
    }

    private void onSearch()
    {
        StringPair[] criteria = new StringPair[6];
        MainServiceAsync.Util.getInstance().findClients(criteria, new SearchCallback());
    }

    private class SearchCallback extends AbstractAsyncCallback<BasePair[]>
    {
        public void onSuccess(BasePair[] result)
        {
            // Push the data into the widget.
            table.setRowData(0, Arrays.asList(result));
            // Set the total row count. This isn't strictly necessary, but it affects
            // paging calculations, so its good habit to keep the row count up to date.
            table.setRowCount(result.length, true);
        }
    }

}