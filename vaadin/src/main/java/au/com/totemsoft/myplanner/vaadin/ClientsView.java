package au.com.totemsoft.myplanner.vaadin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
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
import au.com.totemsoft.myplanner.vaadin.client.ClientForm;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Clients | MyPlanner")
@CssImport("./styles/shared-styles.css")
public class ClientsView extends VerticalLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = -6231446012104860018L;

    private final UserService userService;

    private final ClientForm form;

    private final Grid<ClientDto> grid;

    public ClientsView(UserService userService) {
        this.userService = userService;
        //
        addClassName("clients-view");
        setSizeFull();
        //
        this.grid = new Grid<>(ClientDto.class, false);
        configureGrid();
        //
        this.form = new ClientForm();
        this.form.addListener(ClientForm.SaveEvent.class, this::saveClient);
        this.form.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
        this.form.addListener(ClientForm.CloseEvent.class, e -> closeEditor());
        //
        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();
        //
        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteClient(ClientForm.DeleteEvent evt) {
        //userService.delete(evt.getClient());
        updateList();
        closeEditor();
    }

    private void saveClient(ClientForm.SaveEvent evt) {
        //userService.save(evt.getClient());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("clients-grid");
        grid.setSizeFull();
        grid.setColumns("id", "name", "dateOfBirth", "country");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editClient(e.getValue()));
    }

    private HorizontalLayout getToolBar() {
        // TODO: use getSelectionCriteria
        TextField filterText = new TextField();
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addClientButton = new Button("Add Client", click -> addClient());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addClientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addClient() {
        grid.asSingleSelect().clear();
        editClient(new ClientDto());
    }

    private void editClient(ClientDto client) {
        if (client == null) {
            closeEditor();
        } else {
            form.setClient(client);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setClient(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        List<IClientView> clients = userService.findClients(getSelectionCriteria(), 0, 0);
        grid.setItems(clients.stream().map(
            c -> ClientDto.builder()
                .id(c.getId())
                .name(c.getFullName())
                .dateOfBirth(c.getDateOfBirth())
                .country(c.getDobCountry() == null ? null : c.getDobCountry().getDescription())
                .build()));
    }

    private Map<String, Object> getSelectionCriteria() {
        Map<String, Object> criteria = new HashMap<>();
        //if (checkBoxDisplayAll.isSelected()) {
            return criteria;
        //}
        // TODO: more criteria @see ClientSearch#getSelectionCriteria
    }

}
