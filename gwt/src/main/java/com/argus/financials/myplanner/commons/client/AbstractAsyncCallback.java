package com.argus.financials.myplanner.commons.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {

    public void onFailure(Throwable t) {
        Window.alert("Failure: " + t.getMessage());
    }

}