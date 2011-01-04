package com.argus.financials.myplanner.gwt.commons.client;

import com.google.gwt.user.client.ui.ListBox;

public class AddListBoxItemsCallback extends AbstractAsyncCallback<BasePair[]>
{

    private final ListBox listBox;

    public AddListBoxItemsCallback(ListBox listBox)
    {
        this.listBox = listBox;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(BasePair[] result)
    {
        addItems(listBox, result);
    }

}