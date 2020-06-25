package au.com.totemsoft.myplanner.vaadin.client;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.api.bean.ITitleCode;
import au.com.totemsoft.myplanner.domain.dto.ClientDto;

public class ClientForm extends FormLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = 2237074066676533140L;

    private ComboBox<ITitleCode> title;
    private TextField firstname;
    private TextField surname;
    private ComboBox<ICountry> dobCountry;

    private Binder<ClientDto> binder = new BeanValidationBinder<>(ClientDto.class);

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    public ClientForm(List<ITitleCode> titles, List<ICountry> countries) {
        addClassName("client-search-form");
        //
        title = new ComboBox<>("Title", titles);
        title.setItemLabelGenerator(ITitleCode::getDescription);
        //
        firstname = new TextField("First Name");
        //
        surname = new TextField("Surname");
        //
        dobCountry = new ComboBox<>("Country", countries);
        dobCountry.setItemLabelGenerator(ICountry::getDescription);
        //
        add(
            title,
            firstname,
            surname,
            dobCountry,
            createButtonsLayout()
        );
        // last
        binder.bindInstanceFields(this);
    }

    public void setClient(ClientDto client) {
        binder.setBean(client);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private static final long serialVersionUID = -3920431684869159899L;
        private ClientDto client;
        protected ClientFormEvent(ClientForm source, ClientDto client) {
            super(source, false);
            this.client = client;
        }
        public ClientDto getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ClientFormEvent {
        private static final long serialVersionUID = -4722894530658496231L;
        SaveEvent(ClientForm source, ClientDto client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ClientFormEvent {
        private static final long serialVersionUID = -8403116928337464095L;
        DeleteEvent(ClientForm source, ClientDto client) {
            super(source, client);
        }
    }

    public static class CloseEvent extends ClientFormEvent {
        private static final long serialVersionUID = 6700889385688574215L;
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(
        Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
