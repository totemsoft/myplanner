/*
 * PersonData.java
 *
 * Created on 15 October 2002, 14:39
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.code.HealthStateCode;
import com.argus.financials.code.ResidenceStatusCode;
import com.argus.financials.projection.DSSCalc2;

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
    public void init(com.argus.financials.etc.PersonName name)
            throws java.io.IOException {
        Sex = name.getSexCode() == null ? STRING_EMPTY : name.getSexCode();
        Title = name.getTitleCode() == null ? STRING_EMPTY : name
                .getTitleCode();
        MaritalCode = name.getMaritalCode() == null ? STRING_EMPTY : name
                .getMaritalCode();
        FamilyName = name.getSurname() == null ? STRING_EMPTY : name
                .getSurname();
        FirstName = name.getFirstName() == null ? STRING_EMPTY : name
                .getFirstName();
        OtherGivenNames = name.getOtherGivenNames() == null ? STRING_EMPTY
                : name.getOtherGivenNames();
        FullName = name.getFullName() == null ? STRING_EMPTY : name
                .getFullName();
        if (name.getDateOfBirth() != null && name.getSexCodeID() != null) {
            agePensionQualifyingYear = new DSSCalc2().getPensionQualifyingYear(
                    name.getDateOfBirth(), name.getSexCodeID())
                    + "";
            agePensionQualifyingAge = (int) Math.ceil(DSSCalc2
                    .getPensionQualifyingAge(name.getDateOfBirth(), name
                            .getSexCodeID()))
                    + "";

        }
        if (name.getDateOfBirth() == null)
            DOB = "";
        else
            DOB = com.argus.util.DateTimeUtils.formatAsMEDIUM(name
                    .getDateOfBirth());
        Marital = MaritalCode;
        married = name.isMarried();

        int n = name.getAge() == null ? 0 : name.getAge().intValue();
        age = n == 0 ? "" : "" + n;

        double le = com.argus.financials.projection.data.LifeExpectancy
                .getValue(n, name.getSexCodeID());
        lifeExpectancy = le < 0 ? STRING_EMPTY : number.toString(le);

    }

    public void init(com.argus.financials.service.PersonService person)
            throws Exception {

        super.init(person);
        if (person == null)
            return;

        com.argus.financials.etc.PersonName name = person.getPersonName();
        stateOfHealth = new HealthStateCode().getCodeDescription(person
                .getHealthStateCodeID());
        resident = new ResidenceStatusCode().getCodeDescription(person
                .getResidenceStatusCodeID());
        init(name);

    }

}
