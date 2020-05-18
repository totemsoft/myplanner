package com.argus.financials.myplanner.gwt.security.client;

import com.argus.financials.myplanner.gwt.commons.client.AbstractAsyncCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Login implements EntryPoint, ClickHandler {

    private TextBox login;

    private PasswordTextBox password;

	/* (non-Javadoc)
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    public void onModuleLoad() {
        RootPanel rootPanel = RootPanel.get();
        rootPanel.setSize("100%", "100%");
		
        DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.EM);
        dockLayoutPanel.setStyleName("mp-Panel-center");
		rootPanel.add(dockLayoutPanel);
		dockLayoutPanel.setSize("230px", "160px");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("mp-Panel-center");
		dockLayoutPanel.add(verticalPanel);
		
		SimplePanel infoPanel = new SimplePanel();
		verticalPanel.add(infoPanel);
		
		Label infoLabel = new Label("MyPlanner Login");
		infoLabel.setStyleName("mp-h1");
		infoLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		infoPanel.setWidget(infoLabel);
		
		FormPanel form = new FormPanel();
		//form.setAction("/j_spring_security_check");
		form.setMethod(FormPanel.METHOD_POST);
		verticalPanel.add(form);
		
		Grid grid = new Grid(3, 2);
		form.setWidget(grid);
		grid.setSize("100%", "100%");
		
		Label loginLabel = new Label("User ID:");
		grid.setWidget(0, 0, loginLabel);
		
		login = new TextBox();
		login.setName("j_username");
		login.setText("trial");
		grid.setWidget(0, 1, login);
		
		Label passwordLabel = new Label("Password:");
		grid.setWidget(1, 0, passwordLabel);
		
		password = new PasswordTextBox();
		password.setName("j_password");
		grid.setWidget(1, 1, password);
		
		Button loginButton = new Button("Login");
		//loginButton.getElement().setPropertyString("type", "submit");
		loginButton.addClickHandler(this);
		grid.setWidget(2, 1, loginButton);
	}

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    public void onClick(ClickEvent event) {
        SecurityServiceAsync.Util.getInstance().login(login.getText(), password.getText(), new LoginCallback());
    }

    private static class LoginCallback extends AbstractAsyncCallback<String> {
        public void onSuccess(String result) {
            if (result == null) {
                Window.Location.replace("Main.html");
            } else {
                Window.alert("User login failed:\n" + result); 
            }
        }
    }

}