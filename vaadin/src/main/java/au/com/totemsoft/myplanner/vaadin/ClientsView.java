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
import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.api.service.UserService;
import au.com.totemsoft.myplanner.domain.dto.BuilderClientDto;
import au.com.totemsoft.myplanner.domain.dto.ClientDto;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.client.EntityService;
import au.com.totemsoft.myplanner.vaadin.client.ClientForm;

@PageTitle("Clients | MyPlanner")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
public class ClientsView extends VerticalLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = -6231446012104860018L;

    private final ClientService clientService;
    private final UserService userService;
    private final UserPreferences userPreferences;

    private final ClientForm form;

    private final Grid<ClientDto> grid;

    public ClientsView(
            EntityService entityService,
            ClientService clientService,
            UserService userService,
            UserPreferences userPreferences) {
        this.clientService = clientService;
        this.userService = userService;
        this.userPreferences = userPreferences;
        //
        addClassName("clients-view");
        setSizeFull();
        //
        this.form = new ClientForm(
            entityService.findTitleCodes(),
            entityService.findCountries());
        this.form.addListener(ClientForm.SaveEvent.class, this::saveClient);
        this.form.addListener(ClientForm.DeleteEvent.class, this::removeClient);
        this.form.addListener(ClientForm.CloseEvent.class, e -> closeEditor());
        //
        this.grid = new Grid<>(ClientDto.class, false);
        configureGrid();
        //
        Div content = new Div(form, grid);
        content.addClassName("content");
        content.setSizeFull();
        //
        add(getToolBar(), content);
        updateList();
        //
        Long clientId = userPreferences.clientId();
        if (clientId == null) {
            closeEditor();
        } else {
            editClient(BuilderClientDto.client(clientService.findClientById(clientId)));
        }
    }

    private void removeClient(ClientForm.DeleteEvent evt) {
        ClientDto client = evt.getClient();
        clientService.removeClient(client);
        updateList();
        closeEditor();
    }

    private void saveClient(ClientForm.SaveEvent evt) {
        ClientDto client = evt.getClient();
        clientService.saveClient(client);
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("clients-grid");
        grid.setSizeFull();
        // ClientDto properties
        grid.addColumn(ClientDto::getId).setHeader("Id");
        grid.addColumn(c -> c.getTitle() == null ? null : c.getTitle().getDescription()).setHeader("Title");
        grid.addColumn(ClientDto::getFirstname).setHeader("First Name");
        grid.addColumn(ClientDto::getSurname).setHeader("Surname");
        grid.addColumn(ClientDto::getDateOfBirth).setHeader("Date Of Birth");
        grid.addColumn(c -> c.getDobCountry() == null ? null : c.getDobCountry().getDescription()).setHeader("Country");
        //
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

        Button addClientButton = new Button("Add Client", e -> addClient());
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
            final Long clientId;
            if (client.getId() == null) {
                clientId = clientService.createClient();
                client.setId(clientId.intValue());
                updateList();
            } else {
                clientId = client.getId().longValue();
            }
            form.setClient(client);
            form.setVisible(true);
            addClassName("editing");
            // store in user session
            userPreferences.clientId(clientId.longValue());
        }
    }

    private void closeEditor() {
        // remove from user session
        userPreferences.clientId(null);
        //
        form.setClient(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        List<IClientView> clients = userService.findClients(getSelectionCriteria(), 0, 0);
        grid.setItems(clients.stream().map(
            c -> BuilderClientDto.clientView(c)
        ));
    }

    private Map<String, Object> getSelectionCriteria() {
        Map<String, Object> criteria = new HashMap<>();
        //if (checkBoxDisplayAll.isSelected()) {
            return criteria;
        //}
        // TODO: more criteria @see ClientSearch#getSelectionCriteria
    }

}
