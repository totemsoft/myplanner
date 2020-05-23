package au.com.totemsoft.myplanner.vaadin;

import javax.inject.Inject;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import au.com.totemsoft.myplanner.api.bean.IUser;
import au.com.totemsoft.myplanner.api.service.UserService;

@Route(value = "login")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class LoginView extends VerticalLayout {

    /** serialVersionUID */
    private static final long serialVersionUID = -1170060631447434100L;

    @Inject private UserService userService;

    public LoginView() {
        final TextField textFieldLogin = new TextField("Login");
        final PasswordField passwordField = new PasswordField("Password");
        final Button buttonOK = new Button("OK",
            e -> login(textFieldLogin.getValue(), passwordField.getValue()));
        buttonOK.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonOK.addClickShortcut(Key.ENTER);
        addClassName("centered-content");
        add(textFieldLogin, passwordField, buttonOK);
    }

    private void login(String username, String password) {
        IUser user = userService.login(username, password);
        //log.info(user);
    }

}
