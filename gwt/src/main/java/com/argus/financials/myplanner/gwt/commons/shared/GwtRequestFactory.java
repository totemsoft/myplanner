package com.argus.financials.myplanner.gwt.commons.shared;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.argus.financials.myplanner.gwt.commons.server.ClientController;
import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.LoggingRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.requestfactory.shared.Service;

/**
 * Data-oriented way.
 * @author valeri chibaev
 */
public interface GwtRequestFactory extends RequestFactory
{
    @Service(Client.class)
    public interface ClientRequest extends RequestContext
    {
        Request<ClientProxy> findClient(Long clientId);
        InstanceRequest<ClientProxy, Void> persist();
        InstanceRequest<ClientProxy, Void> remove();
    }

    @Service(ClientController.class)
    public interface ClientControllerRequest extends RequestContext
    {
        Request<ClientProxy> findClient(Long clientId);
        Request<Long> persist(ClientProxy client);
        Request<Long> remove(ClientProxy client);
    }
    
    ClientRequest clientRequest();

    ClientControllerRequest clientControllerRequest();

    LoggingRequest loggingRequest();

}