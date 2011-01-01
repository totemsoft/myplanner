package com.argus.financials.myplanner.gwt.commons.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T>
{

    public void onFailure(Throwable caught)
    {
        String errorMessage = caught.toString();
        if (errorMessage.indexOf("403") != -1)
        {
            // Access denied for this role
            Window.alert("Access denied");
        }
        else if (errorMessage.indexOf("Login") != -1)
        {
            // Session expired : display login form
            //LoginDialog.show();
            Window.alert("LoginDialog.show() : " + errorMessage);
        }
        else
        {
            // Other error
            Window.alert("Error : " + errorMessage);
        }
    }

}