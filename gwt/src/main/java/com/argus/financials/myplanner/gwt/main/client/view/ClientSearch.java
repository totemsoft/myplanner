package com.argus.financials.myplanner.gwt.main.client.view;

import java.util.Arrays;

import com.argus.financials.myplanner.commons.client.AbstractAsyncCallback;
import com.argus.financials.myplanner.commons.client.BasePair;
import com.argus.financials.myplanner.gwt.main.client.Main;
import com.argus.financials.myplanner.gwt.main.client.MainServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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

    public ClientSearch()
    {
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setStyleName("mp-Panel-center");
        initWidget(verticalPanel);
        verticalPanel.setSize("30em", null);

        FormPanel formPanel = new FormPanel();
        verticalPanel.add(formPanel);
        formPanel.setMethod(FormPanel.METHOD_POST);

        Grid grid = new Grid(3, 5);
        formPanel.setWidget(grid);
        grid.setStyleName("nonDataTable");

        Label label = new Label("Surname");
        grid.setWidget(0, 0, label);

        TextBox textBox = new TextBox();
        textBox.setFocus(true);
        grid.setWidget(0, 1, textBox);

        Label label_1 = new Label("DOB");
        grid.setWidget(0, 2, label_1);

        DateBox dateBox = new DateBox();
        dateBox.setFormat(new DefaultFormat(DateTimeFormat.getShortDateFormat()));
        grid.setWidget(0, 3, dateBox);

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

        table = new CellTable<BasePair>();
        verticalPanel.add(table);
        table.setStyleName("border");
        table.setSize("100%", "100%");
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
        MainServiceAsync.Util.getInstance().findClients(new SearchCallback());
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