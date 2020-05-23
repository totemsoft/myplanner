/*
 * PlanData.java
 *
 * Created on March 5, 2002, 8:43 AM
 */

package au.com.totemsoft.myplanner.etc;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class PlanData implements java.io.Serializable {

    public _Adviser Adviser;

    public _Client Client;

    public _Partner Partner;

    public PlanData() {
        Adviser = new _Adviser();
        Client = new _Client();
        // Partner = new _Partner(); // client can has no partner
    }

    /**
     * 
     */
    public class _Person implements java.io.Serializable {
        public String Sex;

        public String Title;

        public String Marital;

        public String FamilyName;

        public String FirstName;

        public String OtherGivenNames;

        public String PreferredName;

        public String FullName;
    }

    public class _Address implements java.io.Serializable {
        public String StreetNumber;

        public String StreetNumber2;

        public String Suburb;

        public String Postcode;

        public String State;

        public String Country;
    }

    public class _Adviser implements java.io.Serializable {
        public _Person Person = new _Person();

    }

    public class _Client implements java.io.Serializable {
        public _Person Person = new _Person();

        public _Address Address = new _Address();

    }

    public class _Partner implements java.io.Serializable {
        public _Person Person = new _Person();

    }

}
