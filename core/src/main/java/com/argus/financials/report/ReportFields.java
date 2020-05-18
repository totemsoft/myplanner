/*
 * ReportFields_java
 *
 * Created on 3 April 2003, 09:47
 */

package com.argus.financials.report;

/**
 * @author valeri chibaev
 */

import java.io.File;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.api.bean.PersonName;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.code.Advisers;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.code.HealthStateCode;
import com.argus.financials.code.IReportFields;
import com.argus.financials.code.ResidenceStatusCode;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.AddressDto;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.DependentTableModel;
import com.argus.financials.projection.AllocatedPensionCalcNew;
import com.argus.financials.projection.CurrentPositionCalc;
import com.argus.financials.projection.DSSCalc2;
import com.argus.financials.projection.DSSCalcNew;
import com.argus.financials.projection.ETPCalcNew;
import com.argus.financials.projection.GearingCalc2;
import com.argus.financials.projection.GeneralTaxCalculatorNew;
import com.argus.financials.projection.data.LifeExpectancy;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.swing.SplashWindow;
import com.argus.util.DateTimeUtils;

public final class ReportFields implements IReportFields {

    private static IWordReport report;
    public static void setReport(IWordReport report) {
        ReportFields.report = report;
    }

    private static com.argus.format.Currency currency;

    private static com.argus.format.Percent percent;

    private static com.argus.format.Number2 number;

    private static com.argus.math.Money money;

    static {
        java.text.DecimalFormat df;

        // require special formatting
        currency = com.argus.format.Currency.createCurrencyInstance();
        df = (java.text.DecimalFormat) currency.getNumberFormatter();

        df.setPositivePrefix("");
        // df.setPositivePrefix( com.argus.format.Percent.DEFAULT_PREFIX );
        // df.setNegativePrefix( "" );
        df.setNegativePrefix(com.argus.format.Percent.DEFAULT_PREFIX_NEG);
        df.setPositiveSuffix("");
        // df.setPositiveSuffix( com.argus.format.Percent.DEFAULT_SUFFIX );
        df.setNegativeSuffix(""); // 
        // df.setNegativeSuffix( com.argus.format.Percent.DEFAULT_SUFFIX_NEG );

        percent = com.argus.format.Percent.createPercentInstance();
        df = (java.text.DecimalFormat) percent.getNumberFormatter();

        // restore defaults
        df.setPositivePrefix(com.argus.format.Percent.DEFAULT_PREFIX);
        df.setNegativePrefix(com.argus.format.Percent.DEFAULT_PREFIX_NEG);
        df.setPositiveSuffix(com.argus.format.Percent.DEFAULT_SUFFIX);
        df.setNegativeSuffix(com.argus.format.Percent.DEFAULT_SUFFIX_NEG);

        number = com.argus.format.Number2.createInstance();
        df = (java.text.DecimalFormat) number.getNumberFormatter();

        money = new com.argus.math.Money();
        // money.setMaximumFractionDigits(2);
        // money.setMinimumFractionDigits(2);

    }

    private final static Object lockObject = new Object();

    public static Object getLockObject() {
        return lockObject;
    }

    // to provide access to singleton instance
    private static ReportFields reportFields;

    public static ReportFields getInstance() {
        if (reportFields == null)
            reportFields = new ReportFields();
        return reportFields;
    }

    public static ReportFields createInstance() {
        return new ReportFields();
    }

    /** Creates a new instance of ReportFields */
    private ReportFields() {
    }

    // //////////////////////////////////////////////////////////////////////////
    // FIELDS //
    // //////////////////////////////////////////////////////////////////////////
    private final java.util.Map fields = new java.util.TreeMap();

    public java.util.Map getValues() {
        return fields;
    }

    public Object getValue(String fieldName) {
        Object value = fields.get(fieldName);
        return value;
    }

    public void setValue(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    public void setValue(String fieldName, double value) {
        fields.put(fieldName, new Double(value));
    }

    public void setValue(String fieldName, int value) {
        fields.put(fieldName, new Integer(value));
    }

    public void setValue(String fieldName, java.util.Date value) {
        fields.put(fieldName, value == null ? null : DateTimeUtils
                .formatAsMEDIUM(value));
    }

    public void print(java.io.PrintStream ps) {

        java.util.Iterator iter = fields.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            ps.println("\t" + entry.getKey() + "=" + entry.getValue());
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    // initialization (clear all fields) //
    // //////////////////////////////////////////////////////////////////////////
    public synchronized void initialize() throws Exception {

        // 1. personal (compulsary data)
        initialize((PersonService) null);

        // 2. current financials
        // new FinancialData( null, CURRENT_PREFIX ).initializeReportData( this,
        // null );

        // 3. Strategy current/restructured financials
        //new StrategyCalc()
        ((Reportable) Class.forName("com.argus.financials.ui.projection.StrategyCalc").newInstance())
        .initializeReportData(this, null);

        // 4. Allocated Pension data
        new AllocatedPensionCalcNew().initializeReportData(this, null);

        // 5. QUICKView data
        new CurrentPositionCalc().initializeReportData(this, null);

        // 6. Gearing data
        new GearingCalc2().initializeReportData(this, null);

        // 7. InvRisk data (ISO - Risk Profile)
        new com.argus.financials.report.data.InvRiskData().initializeReportData(this, null);

        /*
         * // 8. DSSData (social security) new
         * com.argus.activex.data.DSSData().initializeReportData( this, null );
         */
        // 8. DSSData (Centrelink)
        new DSSCalcNew().initializeReportData(this, null);

        // 9. Gearing data
        //new MortgageCalc()
        ((Reportable) Class.forName("com.argus.financials.ui.projection.MortgageCalc").newInstance())
        .initializeReportData(this, null);

        // 10. PAYG calc
        new GeneralTaxCalculatorNew().initializeReportData(this, null);

        // 11. ETP calc
        new ETPCalcNew().initializeReportData(this, null);

    }

    // //////////////////////////////////////////////////////////////////////////
    // PERSONAL //
    // //////////////////////////////////////////////////////////////////////////
    public synchronized void initialize(PersonService person)
            throws ServiceException {

        if (person == null) {
            // java.awt.Toolkit.getDefaultToolkit().beep();
            System.out.println("\t***** ReportFields::initialize( null ) *****");
        }

        // general
        java.util.Date now = new java.util.Date();
        fields.put(DateShort, DateTimeUtils.formatAsSHORT(now));
        fields.put(DateMedium, DateTimeUtils.formatAsMEDIUM(now));
        fields.put(DateLong, DateTimeUtils.formatAsLONG(now));

        // adviser person name
        Object owner = person == null ? null : new Advisers()
                .findByPrimaryKey(person.getOwnerPrimaryKey());
        initAdviser(owner == null ? null : ((Contact) owner).getName());

        if (person == null || person instanceof ClientService) {
            // client person name
            initClient((ClientService) person);

            // partner person name
            PersonService partner = person == null ? null : ((ClientService) person)
                    .getPartner(false);
            initPartner(partner);

        } else {
            // partner person name
            initPartner(person);

        }

    }

    private void initAdviser(PersonName pn) throws ServiceException {

        fields.put(Adviser_Title, pn == null ? null : pn.getTitleCode());
        fields.put(Adviser_FamilyName, pn == null ? null : pn.getSurname());
        fields.put(Adviser_FirstName, pn == null ? null : pn.getFirstname());
        fields.put(Adviser_OtherGivenNames, pn == null ? null : pn
                .getOtherNames());
        fields.put(Adviser_FullName, pn == null ? null : pn.getFullName());

    }

    private void initClient(ClientService person) throws ServiceException {

        IPerson personName = person == null ? null : person.getPersonName();
        fields.put(Client_Sex, personName == null ? null : personName.getSex().getCode());
        fields.put(Client_Title, personName == null ? null : personName.getTitle().getCode());
        fields.put(Client_IsMarried, personName == null ? null
                : (personName.isMarried() ? "Yes" : "No"));
        fields.put(Client_MaritalCode, personName == null ? null : personName.getMarital().getCode());
        fields.put(Client_FamilyName, personName == null ? null : personName.getSurname());
        fields.put(Client_FirstName, personName == null ? null : personName.getFirstname());
        fields.put(Client_OtherGivenNames, personName == null ? null : personName.getOtherNames());
        fields.put(Client_FullName, personName == null ? null : personName.getFullName());

        java.util.Date dob = personName == null ? null : personName.getDateOfBirth();
        setValue(Client_DOB, dob);

        int n = personName == null ? 0 : personName.getAge() == null ? 0 : personName.getAge().intValue();
        fields.put(Client_Age, n == 0 ? null : new Integer(n));

        double le = personName == null ? -1 : LifeExpectancy.getValue(n, personName.getSex().getId());
        fields.put(Client_LifeExpectancy, le < 0 ? null : number.toString(le));

        IPersonHealth personHealth = personName == null ? null : personName.getPersonHealth();
        fields.put(Client_HealthCover, personHealth == null ? null : personHealth.isHospitalCover() ? BooleanCode.rcYES.getDescription() : BooleanCode.rcNO.getDescription());
        fields.put(Client_StateOfHealth, personHealth == null ? null
                : new HealthStateCode().getCodeDescription(personHealth.getHealthStateCodeId()));
        fields.put(Client_Resident, person == null ? null
                : new ResidenceStatusCode().getCodeDescription(personName.getResidenceStatusCodeId()));

        fields.put(Client_AgePensionQualifyingYear, personName == null ? null
                : new DSSCalc2().getPensionQualifyingYear(dob, personName.getSex().getId()));
        fields.put(Client_AgePensionQualifyingAge, personName == null ? null
                : (int) Math.ceil(DSSCalc2.getPensionQualifyingAge(dob, personName.getSex().getId())));

        java.util.Map dependents = person == null ? null : person.getDependents();
        DependentTableModel dtm = new DependentTableModel(dependents);
        fields.put(Client_DependentsNameAgeSentence, (personName == null ? null : personName
                .getFirstname())
                + " " + dtm.getDependentsNameAge());
        fields.put(Client_Dependents, dtm);
        fields.put(Client_NumberOfDependents, new Integer(
                dependents == null ? 0 : dependents.size()));

        java.util.Map map = person == null ? null : person
                .getContactMedia(Boolean.TRUE);
        ContactMedia cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.PHONE);
        fields.put(Client_PhoneHome, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.PHONE_WORK);
        fields.put(Client_PhoneWork, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.MOBILE);
        fields.put(Client_Mobile, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map.get(ContactMediaCode.FAX);
        fields.put(Client_Fax, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.EMAIL);
        fields.put(Client_Email, cm == null ? null : cm.toString());

        AddressDto a = person == null ? null : person.getResidentialAddress();
        fields.put(Client_ResidentialAddress, a == null ? null : a
                .getFullAddress());
        fields.put(Client_ResidentialAddress_StreetNumber, a == null ? null : a
                .getStreetNumber());
        fields.put(Client_ResidentialAddress_StreetNumber2, a == null ? null
                : a.getStreetNumber2());
        fields.put(Client_ResidentialAddress_Suburb, a == null ? null : a
                .getSuburb());
        fields.put(Client_ResidentialAddress_Postcode, a == null ? null : a
                .getPostcode());
        fields.put(Client_ResidentialAddress_State, a == null ? null : a
                .getStateCode());
        fields.put(Client_ResidentialAddress_Country, a == null ? null : a
                .getCountryCode());

        a = person == null ? null : person.getPostalAddress();
        fields.put(Client_PostalAddress, a == null ? null : a.getFullAddress());
        fields.put(Client_PostalAddress_StreetNumber, a == null ? null : a
                .getStreetNumber());
        fields.put(Client_PostalAddress_StreetNumber2, a == null ? null : a
                .getStreetNumber2());
        fields.put(Client_PostalAddress_Suburb, a == null ? null : a
                .getSuburb());
        fields.put(Client_PostalAddress_Postcode, a == null ? null : a
                .getPostcode());
        fields.put(Client_PostalAddress_State, a == null ? null : a.getStateCode());
        fields.put(Client_PostalAddress_Country, a == null ? null : a
                .getCountryCode());

        FinancialGoal fg = person == null ? null : person.getFinancialGoal();
        fields.put(Client_TargetAge, fg == null ? null : fg.getTargetAge());
        setValue(Client_TargetDate, fg == null ? null : fg.getTargetDate(dob));
        fields.put(Client_YearsToTargetAge, fg == null ? null : fg
                .getYearsToTargetAge(dob));
        fields.put(Client_TargetIncome, fg == null ? null : currency
                .toString(fg.getTargetIncome()));
        fields.put(Client_LumpSumRequired, fg == null ? null : currency
                .toString(fg.getLumpSumRequired()));
        fields.put(Client_Notes, fg == null ? null : fg.getNotes());

        IBusiness b = person == null ? null : person.getEmployerBusiness();
        fields.put(Client_Employer_LegalName, b == null ? null : b.getLegalName());
        fields.put(Client_Employer_TradingName, b == null ? null : b.getTradingName());

        IOccupation o = personName == null ? null : personName.getOccupation();
        fields.put(Client_Occupation_EmploymentStatus, o == null ? null : o.getEmploymentStatus());
        fields.put(Client_Occupation_OccupationName, o == null ? null : o.getOccupation());
        fields.put(Client_EmploymentStatus, o == null ? null : o.getEmploymentStatus());

        fields.put(Client_FeeDate, person == null ? null : person.getFeeDate());
        fields.put(Client_ReviewDate, person == null ? null : person.getReviewDate());
    }

    private void initPartner(PersonService person) throws ServiceException {

        IPerson personName = person == null ? null : person.getPersonName();
        fields.put(Partner_Sex, personName == null ? null : personName.getSex().getCode());
        fields.put(Partner_Title, personName == null ? null : personName.getTitle().getCode());
        fields.put(Partner_MaritalCode, personName == null ? null : personName.getMarital().getCode());
        fields.put(Partner_FamilyName, personName == null ? null : personName.getSurname());
        fields.put(Partner_FirstName, personName == null ? null : personName.getFirstname());
        fields.put(Partner_OtherGivenNames, personName == null ? null : personName.getOtherNames());
        fields.put(Partner_FullName, personName == null ? null : personName.getFullName());

        java.util.Date dob = personName == null ? null : personName.getDateOfBirth();
        fields.put(Partner_DOB, dob == null ? null : DateTimeUtils.formatAsMEDIUM(dob));

        int n = personName == null ? 0 : personName.getAge() == null ? 0 : personName.getAge().intValue();
        fields.put(Partner_Age, n == 0 ? null : new Integer(n));

        double le = personName == null ? -1 : LifeExpectancy.getValue(n, personName.getSex().getId());
        fields.put(Partner_LifeExpectancy, le < 0 ? null : number.toString(le));

        IPersonHealth personHealth = personName == null ? null : personName.getPersonHealth();
        fields.put(Partner_HealthCover, personHealth == null ? null : personHealth.isHospitalCover() ? BooleanCode.rcYES.getDescription() : BooleanCode.rcNO.getDescription());
        fields.put(Partner_StateOfHealth, personHealth == null ? null
                : new HealthStateCode().getCodeDescription(personHealth.getHealthStateCodeId()));
        fields.put(Partner_Resident, person == null ? null
                : new ResidenceStatusCode().getCodeDescription(personName.getResidenceStatusCodeId()));

        fields.put(Partner_AgePensionQualifyingYear, personName == null ? null
                : new DSSCalc2().getPensionQualifyingYear(dob, personName.getSex().getId()));
        fields.put(Partner_AgePensionQualifyingAge, personName == null ? null
                : (int) Math.ceil(DSSCalc2.getPensionQualifyingAge(dob, personName.getSex().getId())));

        java.util.Map dependents = person == null ? null : person
                .getDependents();
        DependentTableModel dtm = new DependentTableModel(dependents);
        fields.put(Partner_DependentsNameAgeSentence, (personName == null ? null : personName
                .getFirstname())
                + " " + dtm.getDependentsNameAge());
        fields.put(Partner_Dependents, dtm);
        fields.put(Partner_NumberOfDependents, new Integer(
                dependents == null ? 0 : dependents.size()));

        java.util.Map map = person == null ? null : person
                .getContactMedia(Boolean.TRUE);
        ContactMedia cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.PHONE);
        fields.put(Partner_PhoneHome, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.PHONE_WORK);
        fields.put(Partner_PhoneWork, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.MOBILE);
        fields.put(Partner_Mobile, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map.get(ContactMediaCode.FAX);
        fields.put(Partner_Fax, cm == null ? null : cm.toString());

        cm = map == null ? null : (ContactMedia) map
                .get(ContactMediaCode.EMAIL);
        fields.put(Partner_Email, cm == null ? null : cm.toString());

        AddressDto a = person == null ? null : person.getResidentialAddress();
        fields.put(Partner_ResidentialAddress, a == null ? null : a
                .getFullAddress());
        fields.put(Partner_ResidentialAddress_StreetNumber, a == null ? null
                : a.getStreetNumber());
        fields.put(Partner_ResidentialAddress_StreetNumber2, a == null ? null
                : a.getStreetNumber2());
        fields.put(Partner_ResidentialAddress_Suburb, a == null ? null : a
                .getSuburb());
        fields.put(Partner_ResidentialAddress_Postcode, a == null ? null : a
                .getPostcode());
        fields.put(Partner_ResidentialAddress_State, a == null ? null : a
                .getStateCode());
        fields.put(Partner_ResidentialAddress_Country, a == null ? null : a
                .getCountryCode());

        a = person == null ? null : person.getPostalAddress();
        fields
                .put(Partner_PostalAddress, a == null ? null : a
                        .getFullAddress());
        fields.put(Partner_PostalAddress_StreetNumber, a == null ? null : a
                .getStreetNumber());
        fields.put(Partner_PostalAddress_StreetNumber2, a == null ? null : a
                .getStreetNumber2());
        fields.put(Partner_PostalAddress_Suburb, a == null ? null : a
                .getSuburb());
        fields.put(Partner_PostalAddress_Postcode, a == null ? null : a
                .getPostcode());
        fields
                .put(Partner_PostalAddress_State, a == null ? null : a
                        .getStateCode());
        fields.put(Partner_PostalAddress_Country, a == null ? null : a
                .getCountryCode());

        FinancialGoal fg = person == null ? null : person.getFinancialGoal();
        fields.put(Partner_TargetAge, fg == null ? null : fg.getTargetAge());
        fields.put(Partner_TargetDate, fg == null ? null : DateTimeUtils
                .formatAsSHORT(fg.getTargetDate(dob)));
        fields.put(Partner_YearsToTargetAge, fg == null ? null : fg
                .getYearsToTargetAge(dob));
        fields.put(Partner_TargetIncome, fg == null ? null : currency
                .toString(fg.getTargetIncome()));
        fields.put(Partner_LumpSumRequired, fg == null ? null : currency
                .toString(fg.getLumpSumRequired()));
        fields.put(Partner_Notes, fg == null ? null : fg.getNotes());

        IBusiness b = person == null ? null : person.getEmployerBusiness();
        fields.put(Partner_Employer_LegalName, b == null ? null : b.getLegalName());
        fields.put(Partner_Employer_TradingName, b == null ? null : b.getTradingName());

        IOccupation o = personName == null ? null : personName.getOccupation();
        fields.put(Partner_Occupation_EmploymentStatus, o == null ? null : o.getEmploymentStatus());
        fields.put(Partner_Occupation_OccupationName, o == null ? null : o.getOccupation());
        fields.put(Partner_EmploymentStatus, o == null ? null : o.getEmploymentStatus());
    }

    public static void generateReport(final java.awt.Window w,
            final ReportFields rf, final String reportName)
    {
        // System.out.println( "ReportFields::generateReport()" );
        final SplashWindow splash = new SplashWindow(null,
                w instanceof java.awt.Frame ? (java.awt.Frame) w : null);
        splash.setStringPainted("Waiting for report engine to start ...");
        splash.setVisible(true);

        new Thread(new Runnable() {
            public void run() {
                // System.out.println( "ReportFields::generateReport()\tBefore
                // synchronized section ..." );
                synchronized (lockObject) {
                    new Thread(splash, "SplashWindow").start();
                    splash.setStringPainted("Generating Report...   Please Wait...");

                    try {
                        // System.out.println( "ReportFields::generateReport()\t...
                        // In synchronized section" );

                        String fileName = reportName;
                        if (fileName == null || !new File(fileName).exists()) {
                            fileName = new ReportWriter(null).getReportTemplate(reportName);
                            if (fileName == null)
                                return;
                        }

                        splash.setStringPainted("Initialising Word ... ");
                        report.setTemplate(WordSettings.getInstance().getReportTemplateDocument());

                        splash.setStringPainted("Adding Document ... ");
                        report.setReport(fileName);

                        splash.setStringPainted("Updating Document ... ");
                        report.setData(rf.getValues());

                        report.run();
                            
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    } finally {
                        splash.close();
                        lockObject.notify();
                    }

                } // end of synchronized
            }
        }, "ReportWriter").start();

    }

}
