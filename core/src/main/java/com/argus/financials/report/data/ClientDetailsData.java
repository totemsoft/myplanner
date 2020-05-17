/*
 * ClientDetailsData.java
 *
 * Created on 17 September 2002, 17:25
 */

package com.argus.financials.report.data;

/**
 * 
 * @author shibaevv
 * @version
 */

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import com.argus.financials.api.bean.PersonName;
import com.argus.financials.code.RelationshipCode;
import com.argus.financials.etc.Dependent;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;

public class ClientDetailsData extends ClientPersonData {

    public Dependents dependents;

    private Object[] keys;

    /** Creates new ClientDetailsData */
    public ClientDetailsData() {
        dependents = new Dependents();
    }

    public class Dependents implements java.io.Serializable {

        public Item createItem() {
            return new Item();
        }

        public java.util.ArrayList Items = new java.util.ArrayList();

        public class Item extends PersonData {
            public String relatedTo = STRING_EMPTY;

            public String relationship = STRING_EMPTY;

            public String supportToAge = STRING_EMPTY;
        }

        public void init(PersonService person) throws Exception {

            // for client's dependents
            Map details = person.getDependents();
            if (details != null) {
                Object[] dependent = (Object[]) details.values().toArray();
                keys = (Object[]) details.keySet().toArray();

                for (int i = 0; i < dependent.length; i++) {
                    Dependents.Item item = dependents.createItem();
                    PersonName name = ((Dependent) dependent[i]).getName();
                    if (name == null) {
                        name = new PersonName();
                    }
                    item.init(name);

                    Dependent dep = (Dependent) dependent[i];
                    // get relationship
                    Integer rscid = dep.getRelationshipCodeID();
                    if (rscid != null) {
                        item.relationship = new RelationshipCode().getCodeDescription(rscid) == null ? STRING_EMPTY : new RelationshipCode().getCodeDescription(rscid);
                    } else {
                        item.relationship = STRING_EMPTY;
                    }

                    // get related to
                    item.relatedTo = client.FirstName;

                    // get support to age
                    item.supportToAge = (dep.getSupportToAge() == null) ? STRING_EMPTY
                            : dep.getSupportToAge().toString();

                    dependents.Items.add(i, item);

                }

            }

            PersonService clientPartner = ((ClientService) person).getPartner(false);
            if (clientPartner != null) {
                // for partner's dependents
                details = clientPartner.getDependents();
                if (details != null) {
                    Object[] dependent = (Object[]) details.values().toArray();
                    Object[] keys2 = (Object[]) details.keySet().toArray();
                    Vector partnerDependents = new Vector();
                    int flag = 0;

                    for (int i = 0; i < keys2.length; i++) {
                        for (int j = 0; j < keys.length; j++) {
                            if (keys[j] != null && keys2[i] != null
                                    && !keys[j].equals(keys2[i])) {
                                flag++;
                            }
                        }
                        if (flag == keys.length)
                            partnerDependents.addElement(dependent[i]);
                    }
                    Enumeration enu = partnerDependents.elements();
                    int k = 0;
                    while (enu.hasMoreElements()) {
                        Dependents.Item item = dependents.createItem();
                        Dependent dep = (Dependent) enu.nextElement();
                        PersonName name = dep.getName();
                        if (name != null)
                            item.init(name);
                        // get relationship
                        Integer rscid = dep.getRelationshipCodeID();
                        if (rscid != null) {
                            item.relationship = new RelationshipCode().getCodeDescription(rscid) == null ? STRING_EMPTY : new RelationshipCode().getCodeDescription(rscid);
                        } else {
                            item.relationship = STRING_EMPTY;
                        }
                        // get related to
                        item.relatedTo = partner.FirstName;

                        // get support to age
                        item.supportToAge = (dep.getSupportToAge() == null) ? STRING_EMPTY : dep.getSupportToAge().toString();
                        dependents.Items.add(k, item);
                        k++;
                    }

                }

            } // end if

        }

    }

    /**
     * create and initialize data
     */
    public void init(PersonService person) throws Exception {
        super.init(person);

        dependents.init(person);

    }

}
