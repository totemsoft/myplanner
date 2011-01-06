package com.argus.financials.myplanner.gwt.commons.shared;

import java.util.List;

import com.argus.financials.domain.hibernate.Client;
import com.argus.financials.myplanner.gwt.commons.client.ClientProxy;
import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@Service(Client.class)
public interface ClientRequest extends RequestContext
{

    Request<List<ClientProxy>> findClients();

    Request<ClientProxy> findClient(Long clientId);

    InstanceRequest<ClientProxy, Void> persist();

    InstanceRequest<ClientProxy, Void> remove();

}