package au.com.totemsoft.myplanner.vaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.Exclude;
import com.vaadin.flow.templatemodel.TemplateModel;

import au.com.totemsoft.myplanner.api.bean.IClient;
import au.com.totemsoft.myplanner.api.bean.ITitleCode;
import au.com.totemsoft.myplanner.domain.dto.BuilderClientDto;
import au.com.totemsoft.myplanner.domain.dto.ClientDto;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.client.EntityService;

/**
 * A Designer generated component for the client-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@PageTitle("Client | MyPlanner")
@Route(value = "client", layout = MainLayout.class)
@Tag("client-view")
@JsModule("./src/views/client-view.js")
@CssImport("./styles/shared-styles.css")
public class ClientView extends PolymerTemplate<ClientView.ClientViewModel> {

    /** serialVersionUID */
    private static final long serialVersionUID = 5177717078125099292L;

    @Id("title")
    private ComboBox<ITitleCode> title;
    @Id("firstname")
    private TextField firstname;
    @Id("surname")
    private TextField surname;
    @Id("otherGivenNames")
    private TextField otherGivenNames;
    @Id("sex")
    private RadioButtonGroup<String> sex;
    @Id("marital")
    private ComboBox<String> marital;

    private ClientService clientService;

    /**
     * Creates a new ClientView.
     */
    public ClientView(EntityService entityService, ClientService clientService) {
        this.clientService = clientService;
        // You can initialise any data required for the connected UI components here.
        title.setItems(entityService.findTitleCodes());
        title.setItemLabelGenerator(ITitleCode::getDescription);
        //
        Integer clientId = clientService.getId();
        if (clientId != null && clientId > 0) {
            IClient client = clientService.findClientById(clientId.longValue());
            getModel().setClient(BuilderClientDto.client(client));
        }
    }

    @EventHandler
    public void submit() {
        ClientDto client = getModel().getClient();
        // TODO: save
    }

    /**
     * This model binds properties between ClientView and client-view
     */
    public interface ClientViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
        @Exclude({"id", "dateOfBirth"})
        ClientDto getClient();
        void setClient(ClientDto client);
    }

}
