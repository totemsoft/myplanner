/*
 * ClientPersonData.java
 *
 * Created on 10 January 2003, 17:14
 */

package com.argus.financials.report.data;

import com.argus.financials.api.bean.PersonName;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.code.Advisers;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Dependent;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;

public class ClientPersonData extends BaseData {

    private java.util.Collection duplicates;

    /** Creates a new instance of ClientPersonData */
    public ClientPersonData() {
    }

    public Adviser adviser;

    public Adviser getAdviser() {
        return adviser = (adviser == null ? new Adviser() : adviser);
    }

    public class Adviser extends PersonData {
    }

    public Partner partner;

    public Partner getPartner() {
        return partner = (partner == null ? new Partner() : partner);
    }

    public class Partner extends PersonData {

        public Occupation occupation = new Occupation();

        public class Occupation extends OccupationData {
        }

        public Employer employer = new Employer();

        public class Employer extends BusinessData {
        }

        public String TargetAge = STRING_EMPTY;

        public String TargetDate = STRING_EMPTY;

        public String YearsToProject = STRING_EMPTY;

        public FinancialGoal financialGoal = new FinancialGoal();

        public class FinancialGoal extends FinancialGoalData {
        }

        public Dependents dependents = new Dependents();

        public class Dependents implements java.io.Serializable {

            public java.util.ArrayList Items = new java.util.ArrayList();

            public class Items extends PersonData {
            }

            public Items createItem() {
                return new Items();
            }

        }

        /*
         * has to be moved to derived class, do not keep it here public InvRisk
         * invRisk = new InvRisk(); public class InvRisk extends
         * ClientInvRiskData {}
         */

        public Partner() {
        }

        public void init(PersonService person)
                throws Exception {

            super.init(person);
            if (person == null)
                return;

            occupation.init(person);
            employer.init(person);
            financialGoal.init(person);

            java.util.Map details = person.getDependents();
            if (details != null) {
                if (duplicates == null) // client's dependants
                    duplicates = details.keySet();

                java.util.Iterator iter = details.entrySet().iterator();
                while (iter.hasNext()) {
                    java.util.Map.Entry entry = (java.util.Map.Entry) iter
                            .next();
                    Object key = entry.getKey();
                    if (duplicates != null && duplicates.contains(key))
                        continue;

                    Dependent d = (Dependent) entry.getValue();
                    if (d == null)
                        continue;

                    PersonName name = d.getName();
                    if (name == null)
                        continue;

                    Dependents.Items item = dependents.createItem();
                    item.init(name);
                    dependents.Items.add(item);

                }

            }
            /*
             * // client and partner's risk and iso try { invRisk = null;
             * 
             * Integer surveyID = person.getSurveyID(
             * com.argus.server.LinkObjectTypeID.SURVEY_2_RISKPROFILE); if (
             * surveyID != null && surveyID.intValue() > 0 ) {
             * com.argus.etc.Survey survey = person.getSurvey(surveyID);
             * 
             * if ( survey != null ) { invRisk = new InvRisk();
             * 
             * invRisk.init( person ); invRisk.init( survey );
             * //invRisk.initPartner( survey );
             *  }
             *  }
             *  } catch ( javax.ejb.ObjectNotFoundException e ) { throw new
             * Exception( e.getMessage() ); }
             */
        }

    }

    public Client client;

    public Client getClient() {
        return client = (client == null ? new Client() : client);
    }

    public class Client extends Partner {

        public String maritalStatus;

        public String homeOwner; // Home Owner (H) / Non-Home Owner (N)

        public String phoneWork = STRING_EMPTY;

        public String phoneHome = STRING_EMPTY;

        public String mobile = STRING_EMPTY;

        public String fax = STRING_EMPTY;

        public String email = STRING_EMPTY;

        public String address = STRING_EMPTY;

        public String postalAddress = STRING_EMPTY;

        public ResidentialAddress residential = new ResidentialAddress();

        public class ResidentialAddress extends AddressData {
        }

        public PostalAddress postal = new PostalAddress();

        public class PostalAddress extends AddressData {
        }

        public void init(PersonService person)
                throws Exception {

            super.init(person);

            residential.init(person.getResidentialAddress());
            address = residential.getFullAddress() == null ? STRING_EMPTY
                    : residential.getFullAddress();

            postal.init(person.getPostalAddress());
            postalAddress = postal.getFullAddress() == null ? STRING_EMPTY
                    : postal.getFullAddress();

            java.util.Map map = person.getContactMedia(Boolean.TRUE);
            if (map != null) {
                ContactMedia cm = (ContactMedia) map
                        .get(ContactMediaCode.PHONE);
                phoneHome = "" + (cm == null ? STRING_EMPTY : cm.toString());

                cm = (ContactMedia) map.get(ContactMediaCode.PHONE_WORK);
                phoneWork = "" + (cm == null ? STRING_EMPTY : cm.toString());

                cm = (ContactMedia) map.get(ContactMediaCode.MOBILE);
                mobile = "" + (cm == null ? STRING_EMPTY : cm.toString());

                cm = (ContactMedia) map.get(ContactMediaCode.FAX);
                fax = "" + (cm == null ? STRING_EMPTY : cm.toString());

                cm = (ContactMedia) map.get(ContactMediaCode.EMAIL);
                email = "" + (cm == null ? STRING_EMPTY : cm.toString());

            }

        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void init(PersonService person)
            throws Exception {

        if (person == null)
            return;
        super.init(person);

        /***********************************************************************
         * client's personal data
         **********************************************************************/
        duplicates = null; // reset dependants pkid's
        getClient().init(person);

        /***********************************************************************
         * partner's personal data
         **********************************************************************/
        PersonService partnerPerson = person instanceof ClientService ? // person
                                                                                                                        // can
                                                                                                                        // be
                                                                                                                        // partner
        ((ClientService) person).getPartner(false)
                : null;

        if (partnerPerson == null) {
            partner = null;

        } else {
            getPartner().init(partnerPerson);

        }

        /***********************************************************************
         * adviser's personal data
         **********************************************************************/
        Object ownerPrimaryKeyID;
        if (person instanceof ClientService)
            // we have a client person
            ownerPrimaryKeyID = person.getOwnerPrimaryKey();
        else
            // we have a partner of a client, so we have to find the client's
            // adviser
            ownerPrimaryKeyID = clientService.getOwnerPrimaryKey();

        Object owner = new Advisers().findByPrimaryKey(ownerPrimaryKeyID);
        getAdviser().init(
                owner == null ? new PersonName()
                        : ((Contact) owner).getName());

    }

}
