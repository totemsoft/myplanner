package com.argus.financials.myplanner.gwt.commons.shared;

import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.argus.financials.myplanner.gwt.commons.server.ClientController;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(ClientController.class)
public interface ClientRequest extends RequestContext
{

    //Request<List<ClientProxy>> findClients();

    Request<ClientProxy> findClient(Integer clientId);

    //InstanceRequest<ClientProxy, Void> persist();

    //InstanceRequest<ClientProxy, Void> remove();

}