package com.argus.financials.myplanner.gwt.commons.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T>
{

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
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

    /**
     * 
     * @param listBox
     * @param items
     */
    protected void addItems(ListBox listBox, BasePair[] items)
    {
        listBox.clear();
        listBox.addItem("", null);
        for (BasePair item : items)
        {
            listBox.addItem(item.getSecond(), item.getFirst().toString());
        }
    }

}