package au.com.totemsoft.myplanner.vaadin;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login")
@PageTitle("Login | MyPlanner")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    /** serialVersionUID */
    private static final long serialVersionUID = -1170060631447434100L;

    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");
        add(new H1("MyPlanner"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // inform the user about an authentication error
        List<String> errors = event.getLocation().getQueryParameters().getParameters().get("error");
        if (errors != null && !errors.isEmpty() && StringUtils.isNotBlank(errors.get(0))) {
            login.setError(true);
        }
    }

}
