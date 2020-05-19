package au.com.totemsoft.myplanner;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "login")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class LoginView extends VerticalLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = -1170060631447434100L;

    public LoginView() {
        final TextField textFieldUserID = new TextField("User ID");
        final PasswordField passwordField = new PasswordField("Password");
        final Button buttonOK = new Button("OK");
        buttonOK.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonOK.addClickShortcut(Key.ENTER);
        buttonOK.addAttachListener(
            e -> login(textFieldUserID.getValue(), passwordField.getValue()));
        addClassName("centered-content");
        add(textFieldUserID, passwordField, buttonOK);
    }

    private void login(String username, String password) {
        Notification.show(username);
    }

}
