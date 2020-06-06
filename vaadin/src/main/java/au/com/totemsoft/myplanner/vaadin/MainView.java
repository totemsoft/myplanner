package au.com.totemsoft.myplanner.vaadin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import au.com.totemsoft.myplanner.api.bean.IClientView;
import au.com.totemsoft.myplanner.api.service.UserService;
import au.com.totemsoft.myplanner.vaadin.client.ClientDto;
import au.com.totemsoft.myplanner.vaadin.client.ClientSearchForm;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | MyPlanner")
@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = -6231446012104860018L;

    private final UserService userService;

    private final ClientSearchForm form;

    private final Grid<ClientDto> grid;

    public MainView(UserService userService) {
        this.userService = userService;
        //
        addClassName("list-view");
        setSizeFull();
        //
        this.grid = new Grid<>(ClientDto.class, false);
        configureGrid();
        //
        this.form = new ClientSearchForm();
        //
        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        add(getToolBar(), content);
        updateList();
        //closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("id", "name", "dateOfBirth", "country");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.asSingleSelect().addValueChangeListener(evt -> editClient(evt.getValue()));
    }

    private HorizontalLayout getToolBar() {
        TextField filterText = new TextField();
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        //Button addContactButton = new Button("Add contact", click -> addContact());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);//, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        List<IClientView> clients = userService.findClients(getSelectionCriteria(), 0, -1);
        grid.setItems(clients.stream().map(
            c -> ClientDto.builder()
                .id(c.getId())
                .name(c.getFullName())
                .dateOfBirth(c.getDateOfBirth())
                .country("" + c.getDobCountry())
                .build()));
    }

    private Map<String, Object> getSelectionCriteria() {
        Map<String, Object> criteria = new HashMap<String, Object>();
        //if (jCheckBoxDisplayAll.isSelected()) {
            return criteria;
        //}
        // TODO: more criteria @see ClientSearch#getSelectionCriteria
    }

}
