package au.com.totemsoft.myplanner.vaadin.client;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class ClientSearchForm extends FormLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = 2237074066676533140L;

    private TextField name = new TextField("Name");

    private ComboBox<String> country = new ComboBox<>("Country");

    private Binder<ClientDto> binder = new BeanValidationBinder<>(ClientDto.class);

    //private ComboBox<ClientDto> client = new ComboBox<>("Client");

    public ClientSearchForm() {
        addClassName("clientSearch-form");
        //
        binder.bindInstanceFields(this);
        //client.setItems(clients);
        //
        add(
            name,
            country
        );
    }

}
