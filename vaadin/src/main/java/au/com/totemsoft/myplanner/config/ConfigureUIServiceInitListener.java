package au.com.totemsoft.myplanner.config;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import au.com.totemsoft.myplanner.vaadin.LoginView;

@Component 
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    /** serialVersionUID */
    private static final long serialVersionUID = -6980961678904500544L;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> { 
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
            && !SecurityUtils.isUserLoggedIn()) { 
            event.rerouteTo(LoginView.class);
        }
    }

}
