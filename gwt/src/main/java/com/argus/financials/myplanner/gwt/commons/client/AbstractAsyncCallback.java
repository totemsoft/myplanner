package com.argus.financials.myplanner.gwt.commons.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T>
{
    /** logger */
    private static final Logger LOG = Logger.getLogger(AbstractAsyncCallback.class.getName());

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught)
    {
        String errorMessage = caught.toString();
        if (errorMessage.indexOf("403") != -1)
        {
            // Access denied for this role
            Window.alert("Access denied: " + errorMessage);
            LOG.log(Level.WARNING, "Access denied: " + errorMessage);
        }
        else if (errorMessage.indexOf("Login") != -1)
        {
            // Session expired : display login form
            //LoginDialog.show();
            Window.alert("LoginDialog.show() : " + errorMessage);
            LOG.log(Level.SEVERE, caught.getMessage(), caught);
        }
        else
        {
            // Other error
            Window.alert("Error : " + errorMessage);
            LOG.log(Level.SEVERE, "Error : " + errorMessage);
        }
    }

    /**
     * TODO: move to ClientUtils (commons module)
     * @param listBox
     * @param items
     */
    public static void addItems(ListBox listBox, BasePair[] items)
    {
        listBox.clear();
        listBox.addItem("", (String) null);
        for (BasePair item : items)
        {
            listBox.addItem(item.getSecond(), item.getFirst().toString());
        }
    }

}