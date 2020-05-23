/*
 * ClientInvRiskData.java
 *
 * Created on 11 February 2003, 14:13
 */

package au.com.totemsoft.myplanner.report.data;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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

    public void init(au.com.totemsoft.myplanner.service.PersonService person)
            throws Exception {
        super.init(person);

        clientData.init(person);

        // is the current person a client or the partner of a client
        try {
            // no exception means the person is a client
            au.com.totemsoft.myplanner.service.PersonService partnerPerson = ((au.com.totemsoft.myplanner.service.ClientService) person)
                    .getPartner(false);
            if (partnerPerson != null)
                partnerData.init(partnerPerson);
        } catch (java.lang.NullPointerException e) {
            // nothing to do here
        } catch (java.lang.ClassCastException e) {
            // nothing to do here
        }
    }

    public void init(au.com.totemsoft.myplanner.etc.Survey survey)
            throws Exception {
        clientData.init(survey);
    }

    public void initPartner(au.com.totemsoft.myplanner.etc.Survey survey)
            throws Exception {
        partnerData.init(survey);
    }

    public void init(Integer investmentStrategyCodeID)
            throws Exception {
        clientData.init(investmentStrategyCodeID);
    }
}
