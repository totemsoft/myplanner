/*
 * ClientInvRiskData.java
 *
 * Created on 11 February 2003, 14:13
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 */

public class ClientInvRiskData extends ClientPersonData {

    public ClientData clientData = new ClientData();

    public class ClientData extends InvRiskData {
    }

    public PartnerData partnerData = new PartnerData();

    public class PartnerData extends InvRiskData {
    }

    /** Creates a new instance of ClientInvRiskData */
    public ClientInvRiskData() {
    }

    public void init(com.argus.financials.service.PersonService person)
            throws java.io.IOException {
        super.init(person);

        clientData.init(person);

        // is the current person a client or the partner of a client
        try {
            // no exception means the person is a client
            com.argus.financials.service.PersonService partnerPerson = ((com.argus.financials.service.ClientService) person)
                    .getPartner(false);
            if (partnerPerson != null)
                partnerData.init(partnerPerson);
        } catch (java.lang.NullPointerException e) {
            // nothing to do here
        } catch (java.lang.ClassCastException e) {
            // nothing to do here
        }
    }

    public void init(com.argus.financials.etc.Survey survey)
            throws java.io.IOException {
        clientData.init(survey);
    }

    public void initPartner(com.argus.financials.etc.Survey survey)
            throws java.io.IOException {
        partnerData.init(survey);
    }

    public void init(Integer investmentStrategyCodeID)
            throws java.io.IOException {
        clientData.init(investmentStrategyCodeID);
    }
}
