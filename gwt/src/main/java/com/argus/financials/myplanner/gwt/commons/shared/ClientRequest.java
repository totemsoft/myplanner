package com.argus.financials.myplanner.gwt.commons.shared;

import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.argus.financials.myplanner.gwt.commons.server.ClientController;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

/**
 * Data-oriented way.
 * @author valeri chibaev
 */
@Service(ClientController.class)
public interface ClientRequest extends RequestContext
{

    Request<ClientProxy> findClient(Long clientId);

    Request<ClientProxy> persist(ClientProxy client);
    //InstanceRequest<ClientProxy, Void> persist(); // implement on Client class

    Request<ClientProxy> remove(ClientProxy client);
    //InstanceRequest<ClientProxy, Void> remove(); // implement on Client class

}