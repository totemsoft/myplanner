/*
 * PersonData.java
 *
 * Created on 15 October 2002, 14:39
 */

package com.argus.financials.report.data;

import java.io.IOException;

import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.api.bean.PersonName;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import com.argus.financials.code.HealthStateCode;
import com.argus.financials.code.ResidenceStatusCode;
import com.argus.financials.projection.DSSCalc2;
import com.argus.financials.projection.data.LifeExpectancy;
import com.argus.financials.service.PersonService;

// we have to extend this class to use,
// e.g. public class ClientView extends PersonData {}
public abstract class PersonData extends BaseData {

    public String Sex = STRING_EMPTY;

    public String Title = STRING_EMPTY;

    public String Marital = STRING_EMPTY;

    public String FamilyName = STRING_EMPTY;

    public String FirstName = STRING_EMPTY;

    public String OtherGivenNames = STRING_EMPTY;

    public String FullName = STRING_EMPTY;

    public String DOB = STRING_EMPTY;

    public String age = STRING_EMPTY;

    public String lifeExpectancy = STRING_EMPTY;

    public String MaritalCode = STRING_EMPTY;

    public String stateOfHealth = STRING_EMPTY;

    public String resident = STRING_EMPTY;

    public String agePensionQualifyingYear = STRING_EMPTY;

    public String agePensionQualifyingAge = STRING_EMPTY;

    private boolean married;

    /** Creates a new instance of PersonData */
    public PersonData() {
    }

    public boolean isMarried() {
        return married;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void init(PersonName personName) throws IOException {
        Sex = personName.getSex() == null ? STRING_EMPTY : personName.getSex().getCode();
        Title = personName.getTitle() == null ? STRING_EMPTY : personName.getTitle().getCode();
        MaritalCode = personName.getMarital() == null ? STRING_EMPTY : personName.getMarital().getCode();
        FamilyName = personName.getSurname() == null ? STRING_EMPTY : personName.getSurname();
        FirstName = personName.getFirstname() == null ? STRING_EMPTY : personName.getFirstname();
        OtherGivenNames = personName.getOtherNames() == null ? STRING_EMPTY : personName.getOtherNames();
        FullName = personName.getFullName() == null ? STRING_EMPTY : personName.getFullName();
        if (personName.getDateOfBirth() != null && personName.getSex() != null) {
            agePensionQualifyingYear = new DSSCalc2().getPensionQualifyingYear(personName.getDateOfBirth(), personName.getSex().getId()) + "";
            agePensionQualifyingAge = (int) Math.ceil(DSSCalc2.getPensionQualifyingAge(personName.getDateOfBirth(), personName.getSex().getId())) + "";
        }
        if (personName.getDateOfBirth() == null)
            DOB = "";
        else
            DOB = com.argus.util.DateTimeUtils.formatAsMEDIUM(personName.getDateOfBirth());
        Marital = MaritalCode;
        married = personName.isMarried();

        int n = personName.getAge() == null ? 0 : personName.getAge().intValue();
        age = n == 0 ? "" : "" + n;

        double le = LifeExpectancy.getValue(n, personName.getSex().getId());
        lifeExpectancy = le < 0 ? STRING_EMPTY : number.toString(le);
    }

    public void init(PersonService person) throws Exception {
        super.init(person);
        if (person == null)
            return;

        IPerson personName = person.getPersonName();
        IPersonHealth personHealth = personName == null ? null : personName.getPersonHealth();
        stateOfHealth = new HealthStateCode().getCodeDescription(personHealth == null ? null : personHealth.getHealthStateCodeId());
        resident = new ResidenceStatusCode().getCodeDescription(personName.getResidenceStatusCodeId());
        if (personName instanceof PersonName) {
            init((PersonName) personName);
        }
    }

}