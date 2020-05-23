/*
 * java
 *
 * Created on 29 August 2002, 16:12
 */
/*
 * ==================================================================================================== 
 * Date:    30/04/2003
 * By:      thomassh 
 * Comment: Changed class to support au.com.totemsoft.activex.Reportable interface!
 *          Changes were a quick "fix". Both tables, AssetsTestTableModel and IncomeTestTableModel,
 *          use the existing "internal" classes. The "new" tables init() methods dumps the values out 
 *          of the "old" classes into the tables!!!
 * ====================================================================================================
 */
package au.com.totemsoft.myplanner.report.data;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 * 
 */

import java.math.BigDecimal;

import au.com.totemsoft.myplanner.api.bean.IMaritalCode;
import au.com.totemsoft.myplanner.projection.DSSCalc2;
import au.com.totemsoft.myplanner.projection.data.AssessableAssetsIncome;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.service.PersonService;

public class DSSData extends au.com.totemsoft.myplanner.bean.AbstractBase implements
        au.com.totemsoft.myplanner.report.Reportable,
        javax.swing.event.ChangeListener {
    protected static final String STRING_EMPTY = "";

    protected static final au.com.totemsoft.math.Percent _percent;
    static {
        _percent = new au.com.totemsoft.math.Percent();
        _percent.setMaximumFractionDigits(2);
        _percent.setMinimumFractionDigits(2);
    }

    protected static final au.com.totemsoft.math.Money _money;
    static {
        _money = new au.com.totemsoft.math.Money();
        _money.setMaximumFractionDigits(2);
        _money.setMinimumFractionDigits(2);
    }

    static final String STRING_ZERO_PERCENT = "0.00%";

    static final String STRING_ZERO_DOLLAR = "0.00";

    protected String maritalStatus = STRING_EMPTY;

    protected String homeOwner = STRING_EMPTY;

    public PensionAssetsTest pat;

    public PensionIncomeTest pit;

    public ClientResults clientResults;

    public PartnerResults partnerResults;

    public JointResults jointResults;

    /** Creates new DSSData */
    public DSSData() {
        pat = new PensionAssetsTest();
        pit = new PensionIncomeTest();
        clientResults = new ClientResults();
        partnerResults = new PartnerResults();
        jointResults = new JointResults();
    }

    /*
     * ============================ BEGIN: ClientView's Results
     * ============================
     */
    public class ClientResults implements java.io.Serializable {
        public String maximumBenefit = STRING_EMPTY;

        public String assetTest = STRING_EMPTY;

        public String incomeTest = STRING_EMPTY;

        public String basicBenefit = STRING_EMPTY;

        public String pharmaceuticalAllowance = STRING_EMPTY;

        public String rentAssistance = STRING_EMPTY;

        public String pensionerRebate = STRING_EMPTY;

        public String maxPensionPA = STRING_EMPTY; // maximum pension payable
                                                    // per annum

        public String actualPensionPA = STRING_EMPTY; // actual pension
                                                        // payable per annum

        public String actualPensionFN = STRING_EMPTY; // actual pension
                                                        // payable fortnightly
    }

    /*
     * ============================ END: ClientView's Results
     * ============================
     */

    /*
     * ============================ BEGIN: Partner's Results
     * ============================
     */
    public class PartnerResults implements java.io.Serializable {
        public String maximumBenefit = STRING_EMPTY;

        public String assetTest = STRING_EMPTY;

        public String incomeTest = STRING_EMPTY;

        public String basicBenefit = STRING_EMPTY;

        public String pharmaceuticalAllowance = STRING_EMPTY;

        public String rentAssistance = STRING_EMPTY;

        public String pensionerRebate = STRING_EMPTY;

        public String maxPensionPA = STRING_EMPTY; // maximum pension payable
                                                    // per annum

        public String actualPensionPA = STRING_EMPTY; // actual pension
                                                        // payable per annum

        public String actualPensionFN = STRING_EMPTY; // actual pension
                                                        // payable fortnightly
    }

    /*
     * ============================ END: Partner's Results
     * ============================
     */

    /*
     * ============================ BEGIN: Joint's Results
     * ============================
     */
    public class JointResults implements java.io.Serializable {
        public String maxPensionPA = STRING_EMPTY; // maximum pension payable
                                                    // per annum

        public String actualPensionPA = STRING_EMPTY; // actual pension
                                                        // payable per annum

        public String actualPensionFN = STRING_EMPTY; // actual pension
                                                        // payable fortnightly
    }

    /*
     * ============================ END: Partner's Results
     * ============================
     */

    /*
     * ============================ BEGIN: PENSION - ASSETS TEST
     * ============================
     */
    public class PensionAssetsTest implements java.io.Serializable {

        public HomeContents homeContents = new HomeContents();

        public CarsETC carsETC = new CarsETC();

        public Property property = new Property();

        public Savings savings = new Savings();

        public ManagedFunds managedFunds = new ManagedFunds();

        public Shares shares = new Shares();

        public Bonds bonds = new Bonds();

        public FixedInterest fixedInterest = new FixedInterest();

        public Gifts gifts = new Gifts();

        public Loans loans = new Loans();

        public Superannuation superannuation = new Superannuation();

        public SuperannuationDeemed superannuationDeemed = new SuperannuationDeemed(); // needed
                                                                                        // for
                                                                                        // the
                                                                                        // "Financial
                                                                                        // subject
                                                                                        // to
                                                                                        // Deeming"
                                                                                        // "Superannuation"
                                                                                        // row
                                                                                        // in
                                                                                        // the
                                                                                        // word
                                                                                        // document

        public AllocatedPension allocatedPension = new AllocatedPension();

        public ComplyingPension complyingPension = new ComplyingPension();

        public Total total = new Total();

        public Centrelink centrelink = new Centrelink();

        public Threshold threshold = new Threshold();

        public ToDeeming toDeeming = new ToDeeming();

        public Result result = new Result();

        // Home Contents (Household Contents)
        public class HomeContents implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Cars/Caravans/Boats (Motor vehicles)
        public class CarsETC implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Property, other than principal home
        public class Property implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Savings
        public class Savings implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Managed Funds
        public class ManagedFunds implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Shares/Derivates
        public class Shares implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Bonds/Debentures
        public class Bonds implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // FixedInterest
        public class FixedInterest implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Gifts
        public class Gifts implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Loans
        public class Loans implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Superannuationt
        public class Superannuation implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // SuperannuationtDeemed
        public class SuperannuationDeemed implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Allocated Pension
        public class AllocatedPension implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Complying Pension
        public class ComplyingPension implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Total
        public class Total implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Centrelink
        public class Centrelink implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Threshold
        public class Threshold implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // financial investment subject to deeming
        public class ToDeeming implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Result
        public class Result implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }
    }

    /*
     * ============================ END: PENSION - ASSETS TEST
     * ============================
     */

    /*
     * ============================ BEGIN: PENSION - INCOME TEST
     * ============================
     */
    public class PensionIncomeTest implements java.io.Serializable {

        public HomeContents homeContents = new HomeContents(); // produce no
                                                                // income:

        public CarsETC carsETC = new CarsETC(); // produce no income:

        public Property property = new Property();

        public Savings savings = new Savings();

        public ManagedFunds managedFunds = new ManagedFunds();

        public Shares shares = new Shares();

        public Bonds bonds = new Bonds();

        public FixedInterest fixedInterest = new FixedInterest();

        public Gifts gifts = new Gifts();

        public Loans loans = new Loans();

        public Superannuation superannuation = new Superannuation();

        public AllocatedPension allocatedPension = new AllocatedPension();

        public ComplyingPension complyingPension = new ComplyingPension();

        public ClientSalary clientSalary = new ClientSalary(); // client's
                                                                // salary/wages

        public PartnerSalary partnerSalary = new PartnerSalary(); // partner's
                                                                    // salary/wages

        public Total total = new Total();

        public NetCentrelink netCentrelink = new NetCentrelink();

        public LessCentrelink lessCentrelink = new LessCentrelink();

        public AllowableIncomeThreshold allowableIncomeThreshold = new AllowableIncomeThreshold();

        public IndexedThreshold indexedThreshold = new IndexedThreshold();// Indexed
                                                                            // Threshold
                                                                            // For
                                                                            // Deemed
                                                                            // Income

        public ToDeeming toDeeming = new ToDeeming(); // Financial Investment
                                                        // Subject To Deeming

        public GrossSalary grossSalary = new GrossSalary();

        public OtherTaxable otherTaxable = new OtherTaxable(); // Other Taxable
                                                                // Income

        public Rental rental = new Rental(); // Rental Income

        public Pensions pensions = new Pensions(); // Complying/Allocated
                                                    // Pensions

        public OtherGross otherGross = new OtherGross(); // Other Gross
                                                            // Income

        public Result result = new Result(); // Income Test Result

        // Home Contents (Household Contents)
        public class HomeContents implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Cars/Caravans/Boats (Motor vehicles)
        public class CarsETC implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Property, other than principal home
        public class Property implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Savings
        public class Savings implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Managed Funds
        public class ManagedFunds implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Shares/Derivates
        public class Shares implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Bonds/Debentures
        public class Bonds implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // FixedInterest
        public class FixedInterest implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Gifts
        public class Gifts implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Loans
        public class Loans implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Superannuationt
        public class Superannuation implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Allocated Pension
        public class AllocatedPension implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Complying Pension
        public class ComplyingPension implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // ClientView Salary/Wages
        public class ClientSalary implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Partner Salary/Wages
        public class PartnerSalary implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Total
        public class Total implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Net Centrelink exempt income
        public class NetCentrelink implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Less Centrelink exempt income
        public class LessCentrelink implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Allowable Income Threshold
        public class AllowableIncomeThreshold implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Indexed Threshold For Deemed Income
        public class IndexedThreshold implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // financial investment subject to deeming
        public class ToDeeming implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Gross Salary income
        public class GrossSalary implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Other Taxable Income
        public class OtherTaxable implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Rental Income
        public class Rental implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Complying/Allocated Pensions
        public class Pensions implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Other Gross Income
        public class OtherGross implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }

        // Result
        public class Result implements java.io.Serializable {
            public String joint = STRING_EMPTY;

            public String client = STRING_EMPTY;

            public String partner = STRING_EMPTY;

            public String assessable = STRING_EMPTY;
        }
    }

    /*
     * ============================ END: PENSION - INCOME TEST
     * ============================
     */

    /***************************************************************************
     * 
     */
    public void init(PersonService person, Model model) throws java.io.IOException {

        if (model == null)
            model = Model.NONE;

        DSSCalc2 calc = new DSSCalc2();
        calc.setModel(model);
        init(person, calc);

    }

    public void init(au.com.totemsoft.myplanner.service.PersonService person,
            DSSCalc2 dssCalc2) throws java.io.IOException {
        /*
         * ??? // personal data client.FullName = dssCalc2.getClientName() ==
         * null? STRING_EMPTY : dssCalc2.getClientName().trim();
         * 
         *  // set martial status if( dssCalc2.getMaritalStatus() != null ) {
         * client.maritalStatus =
         * dssCalc2.getMaritalStatus().equals(au.com.totemsoft.code.MaritalCode.SINGLE) ?
         * "S" : "C"; } else { client.maritalStatus = ""; }
         *  // set home owner client.homeOwner = dssCalc2.getHomeOwner() ? "H" :
         * "N"; // Home Owner (H) / Non-Home Owner (N)
         */

        // set martial status
        if (dssCalc2.getMaritalStatus() != null) {
            maritalStatus = dssCalc2.getMaritalStatus().equals(IMaritalCode.SINGLE) ? "S" : "C";
        } else {
            maritalStatus = "";
        }

        // set home owner
        homeOwner = dssCalc2.getHomeOwner() ? "H" : "N"; // Home Owner (H) /
                                                            // Non-Home Owner
                                                            // (N)

        AssessableAssetsIncome client_ai = dssCalc2
                .getAssessableAssetsIncomeClient();
        AssessableAssetsIncome partner_ai = dssCalc2
                .getAssessableAssetsIncomePartner();
        AssessableAssetsIncome joint_ai = dssCalc2
                .getAssessableAssetsIncomeJoint();
        AssessableAssetsIncome assessable_ai = dssCalc2
                .getAssessableAssetsIncomeAssessable();

        // Assets JOINT column
        pat.savings.joint = joint_ai.savingsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.savingsA);
        pat.managedFunds.joint = joint_ai.managedFundsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.managedFundsA);
        pat.shares.joint = joint_ai.sharesA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.sharesA);
        pat.bonds.joint = joint_ai.bondsA == null ? STRING_ZERO_DOLLAR : _money
                .toString(joint_ai.bondsA);
        pat.fixedInterest.joint = joint_ai.interestA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.interestA);
        pat.homeContents.joint = joint_ai.homeContentsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.homeContentsA);
        pat.carsETC.joint = joint_ai.carsEtcA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.carsEtcA);
        pat.property.joint = joint_ai.propertyA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.propertyA);
        pat.gifts.joint = joint_ai.giftsA == null ? STRING_ZERO_DOLLAR : _money
                .toString(joint_ai.giftsA);
        pat.loans.joint = joint_ai.loansA == null ? STRING_ZERO_DOLLAR : _money
                .toString(joint_ai.loansA);
        pat.superannuation.joint = joint_ai.superannuationA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.superannuationA);
        pat.complyingPension.joint = joint_ai.complyingPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.complyingPensionA);
        pat.allocatedPension.joint = joint_ai.allocatedPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.allocatedPensionA);

        // Assets CLIENT column
        pat.savings.client = client_ai.savingsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.savingsA);
        pat.managedFunds.client = client_ai.managedFundsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.managedFundsA);
        pat.shares.client = client_ai.sharesA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.sharesA);
        pat.bonds.client = client_ai.bondsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.bondsA);
        pat.fixedInterest.client = client_ai.interestA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.interestA);
        pat.homeContents.client = client_ai.homeContentsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.homeContentsA);
        pat.carsETC.client = client_ai.carsEtcA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.carsEtcA);
        pat.property.client = client_ai.propertyA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.propertyA);
        pat.gifts.client = client_ai.giftsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.giftsA);
        pat.loans.client = client_ai.loansA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.loansA);
        pat.superannuation.client = client_ai.superannuationA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.superannuationA);
        pat.complyingPension.client = client_ai.complyingPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.complyingPensionA);
        pat.allocatedPension.client = client_ai.allocatedPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.allocatedPensionA);

        // Assets PARTNER column
        pat.savings.partner = partner_ai.savingsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.savingsA);
        pat.managedFunds.partner = partner_ai.managedFundsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.managedFundsA);
        pat.shares.partner = partner_ai.sharesA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.sharesA);
        pat.bonds.partner = partner_ai.bondsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.bondsA);
        pat.fixedInterest.partner = partner_ai.interestA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.interestA);
        pat.homeContents.partner = partner_ai.homeContentsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.homeContentsA);
        pat.carsETC.partner = partner_ai.carsEtcA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.carsEtcA);
        pat.property.partner = partner_ai.propertyA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.propertyA);
        pat.gifts.partner = partner_ai.giftsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.giftsA);
        pat.loans.partner = partner_ai.loansA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.loansA);
        pat.superannuation.partner = partner_ai.superannuationA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.superannuationA);
        pat.complyingPension.partner = partner_ai.complyingPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.complyingPensionA);
        pat.allocatedPension.partner = partner_ai.allocatedPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.allocatedPensionA);

        // Assets ASSESSABLE column
        pat.savings.assessable = assessable_ai.savingsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.savingsA);
        pat.managedFunds.assessable = assessable_ai.managedFundsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.managedFundsA);
        pat.shares.assessable = assessable_ai.sharesA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.sharesA);
        pat.bonds.assessable = assessable_ai.bondsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.bondsA);
        pat.fixedInterest.assessable = assessable_ai.interestA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.interestA);
        pat.homeContents.assessable = assessable_ai.homeContentsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.homeContentsA);
        pat.carsETC.assessable = assessable_ai.carsEtcA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.carsEtcA);
        pat.property.assessable = assessable_ai.propertyA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.propertyA);
        pat.gifts.assessable = assessable_ai.giftsA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.giftsA);
        pat.loans.assessable = assessable_ai.loansA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.loansA);
        pat.superannuation.assessable = assessable_ai.superannuationA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.superannuationA);
        pat.complyingPension.assessable = assessable_ai.complyingPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.complyingPensionA);
        pat.allocatedPension.assessable = assessable_ai.allocatedPensionA == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.allocatedPensionA);

        // needed for the "Financial subject to Deeming" "Superannuation" row in
        // the word document
        // over age pension age it has to be displayed!!!
        // client over age pension age
        if (dssCalc2.agePension() && dssCalc2.entitledForAgePension()) {
            // yes, then add superannuation
            pat.superannuationDeemed.client = dssCalc2.getSuperannuationA() == null ? STRING_ZERO_DOLLAR
                    : _money.toString(dssCalc2.getSuperannuationA());
        } else {
            // no, ignore superannuation
            pat.superannuationDeemed.client = STRING_ZERO_DOLLAR;
        }

        // needed for the "Financial subject to Deeming" "Superannuation" row in
        // the word document
        // over age pension age it has to be displayed!!!
        // partner over age pension age
        if (dssCalc2.agePensionPartner()
                && dssCalc2.entitledForAgePensionPartner()) {
            // yes, then add superannuation
            pat.superannuationDeemed.partner = dssCalc2
                    .getSuperannuationAPartner() == null ? STRING_ZERO_DOLLAR
                    : _money.toString(dssCalc2.getSuperannuationAPartner());
        } else {
            // no, ignore superannuation
            pat.superannuationDeemed.partner = STRING_ZERO_DOLLAR;
        }

        // needed for the "Financial subject to Deeming" "Superannuation" row in
        // the word document
        // over age pension age it has to be displayed!!!
        // joint
        // calculate new age pension joint = client + partner
        BigDecimal help = new BigDecimal(0.0);
        /*
         * if ( dssCalc2.agePension() && dssCalc2.entitledForAgePension() &&
         * dssCalc2.getSuperannuationA() != null ) { help = help.add(
         * dssCalc2.getSuperannuationA() ); }
         * 
         * if ( dssCalc2.agePensionPartner() &&
         * dssCalc2.entitledForAgePensionPartner() &&
         * dssCalc2.getSuperannuationA() != null ) { help = help.add(
         * dssCalc2.getSuperannuationAPartner() ); }
         */
        if (dssCalc2.ageOver55(dssCalc2.getAge())
                && dssCalc2.getSuperannuationA() != null) {
            help = help.add(dssCalc2.getSuperannuationA());
        }

        if (dssCalc2.ageOver55(dssCalc2.getPartnerAge())
                && dssCalc2.getSuperannuationAPartner() != null) {
            help = help.add(dssCalc2.getSuperannuationAPartner());
        }

        if (help.compareTo(new BigDecimal(0.0)) > 0) {
            // yes, then add superannuation
            pat.superannuationDeemed.joint = _money.toString(help);
        } else {
            // no, ignore superannuation
            pat.superannuationDeemed.joint = STRING_ZERO_DOLLAR;
        }
        // assessable column = joint column
        pat.superannuationDeemed.assessable = pat.superannuationDeemed.joint;

        pat.total.assessable = dssCalc2.getTotalAJoint() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getTotalAJoint());
        pat.centrelink.assessable = dssCalc2.getTotalAForAssetsTestJoint() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getTotalAForAssetsTestJoint());
        pat.threshold.assessable = dssCalc2.getAssetsTestAllowableThreshold() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getAssetsTestAllowableThreshold());
        pat.toDeeming.assessable = dssCalc2
                .getTotalAssetsSubjectToDeemingAJoint() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2
                        .getTotalAssetsSubjectToDeemingAJoint());
        pat.result.assessable = dssCalc2.getAssetTestC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getAssetTestC());

        // Income JOINT column
        pit.savings.joint = joint_ai.savingsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.savingsI);
        pit.managedFunds.joint = joint_ai.managedFundsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.managedFundsI);
        pit.shares.joint = joint_ai.sharesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.sharesI);
        pit.bonds.joint = joint_ai.bondsI == null ? STRING_ZERO_DOLLAR : _money
                .toString(joint_ai.bondsI);
        pit.fixedInterest.joint = joint_ai.interestI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.interestI);
        pit.homeContents.joint = STRING_ZERO_DOLLAR; // doesn't produce
                                                        // income
        pit.carsETC.joint = STRING_ZERO_DOLLAR; // doesn't produce income
        pit.property.joint = joint_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.propertyI);
        pit.gifts.joint = joint_ai.giftsI == null ? STRING_ZERO_DOLLAR : _money
                .toString(joint_ai.giftsI);
        pit.loans.joint = joint_ai.loansI == null ? STRING_ZERO_DOLLAR : _money
                .toString(joint_ai.loansI);

        // Income CLIENT column
        pit.savings.client = client_ai.savingsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.savingsI);
        pit.managedFunds.client = client_ai.managedFundsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.managedFundsI);
        pit.shares.client = client_ai.sharesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.sharesI);
        pit.bonds.client = client_ai.bondsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.bondsI);
        pit.fixedInterest.client = client_ai.interestI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.interestI);
        pit.homeContents.client = STRING_ZERO_DOLLAR; // doesn't produce
                                                        // income
        pit.carsETC.client = STRING_ZERO_DOLLAR; // doesn't produce income
        pit.property.client = client_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.propertyI);
        pit.gifts.client = client_ai.giftsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.giftsI);
        pit.loans.client = client_ai.loansI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.loansI);

        // Income PARTNER column
        pit.savings.partner = partner_ai.savingsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.savingsI);
        pit.managedFunds.partner = partner_ai.managedFundsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.managedFundsI);
        pit.shares.partner = partner_ai.sharesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.sharesI);
        pit.bonds.partner = partner_ai.bondsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.bondsI);
        pit.fixedInterest.partner = partner_ai.interestI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.interestI);
        pit.homeContents.partner = STRING_ZERO_DOLLAR; // doesn't produce
                                                        // income
        pit.carsETC.partner = STRING_ZERO_DOLLAR; // doesn't produce income
        pit.property.partner = partner_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.propertyI);
        pit.gifts.partner = partner_ai.giftsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.giftsI);
        pit.loans.partner = partner_ai.loansI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.loansI);

        // Income ASSESSABLE column
        pit.savings.assessable = assessable_ai.savingsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.savingsI);
        pit.managedFunds.assessable = assessable_ai.managedFundsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.managedFundsI);
        pit.shares.assessable = assessable_ai.sharesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.sharesI);
        pit.bonds.assessable = assessable_ai.bondsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.bondsI);
        pit.fixedInterest.assessable = assessable_ai.interestI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.interestI);
        pit.homeContents.assessable = STRING_ZERO_DOLLAR; // doesn't produce
                                                            // income
        pit.carsETC.assessable = STRING_ZERO_DOLLAR; // doesn't produce
                                                        // income
        pit.property.assessable = assessable_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.propertyI);
        pit.gifts.assessable = assessable_ai.giftsI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.giftsI);
        pit.loans.assessable = assessable_ai.loansI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.loansI);

        pit.complyingPension.joint = joint_ai.complyingPensionI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.complyingPensionI);
        pit.allocatedPension.joint = joint_ai.allocatedPensionI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.allocatedPensionI);

        pit.total.joint = dssCalc2.getTotalIJoint() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getTotalIJoint());

        //
        // preparing data for row "Gross Salary Income"
        //        
        pit.clientSalary.joint = STRING_ZERO_DOLLAR;
        pit.clientSalary.client = client_ai.salaryWagesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.salaryWagesI); // dssCalc2.getClientSalaryI()
                                                            // == null ?
                                                            // STRING_ZERO_DOLLAR
                                                            // :
                                                            // _money.toString(dssCalc2.getClientSalaryI());
        pit.clientSalary.partner = STRING_ZERO_DOLLAR;
        pit.clientSalary.assessable = pit.clientSalary.client;

        pit.partnerSalary.joint = STRING_ZERO_DOLLAR;
        pit.partnerSalary.client = STRING_ZERO_DOLLAR;
        pit.partnerSalary.partner = partner_ai.salaryWagesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.salaryWagesI); // dssCalc2.getSalaryIPartner()
                                                            // == null ?
                                                            // STRING_ZERO_DOLLAR
                                                            // :
                                                            // _money.toString(dssCalc2.getSalaryIPartner());
        pit.partnerSalary.assessable = pit.partnerSalary.partner;

        pit.grossSalary.client = pit.clientSalary.client;
        pit.grossSalary.partner = pit.partnerSalary.partner;
        pit.grossSalary.joint = joint_ai.salaryWagesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.salaryWagesI);
        pit.grossSalary.assessable = assessable_ai.salaryWagesI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.salaryWagesI);

        //
        // preparing data for row "Other Taxable Income"
        //       
        pit.otherTaxable.joint = STRING_ZERO_DOLLAR;
        pit.otherTaxable.client = STRING_ZERO_DOLLAR;
        pit.otherTaxable.partner = STRING_ZERO_DOLLAR;
        pit.otherTaxable.assessable = STRING_ZERO_DOLLAR;

        //
        // preparing data for row "Rental Income"
        //       
        pit.rental.joint = joint_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.propertyI);
        pit.rental.client = client_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.propertyI);
        pit.rental.partner = partner_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.propertyI);
        pit.rental.assessable = assessable_ai.propertyI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.propertyI);

        //
        // preparing data for row "Complying/Allocated Pensions"
        //       
        pit.pensions.joint = joint_ai.pensionI == null ? STRING_ZERO_DOLLAR
                : _money.toString(joint_ai.pensionI);
        pit.pensions.client = client_ai.pensionI == null ? STRING_ZERO_DOLLAR
                : _money.toString(client_ai.pensionI);
        pit.pensions.partner = partner_ai.pensionI == null ? STRING_ZERO_DOLLAR
                : _money.toString(partner_ai.pensionI);
        pit.pensions.assessable = assessable_ai.pensionI == null ? STRING_ZERO_DOLLAR
                : _money.toString(assessable_ai.pensionI);

        pit.toDeeming.assessable = dssCalc2
                .getTotalAssetsSubjectToDeemingIClient() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2
                        .getTotalAssetsSubjectToDeemingIClient());
        pit.indexedThreshold.assessable = dssCalc2
                .getIndexedThresholdForDeemedIncome() == null ? STRING_ZERO_DOLLAR
                : _money
                        .toString(dssCalc2.getIndexedThresholdForDeemedIncome());

        //
        // preparing data for row "Other Gross Income"
        //       
        pit.otherGross.joint = STRING_ZERO_DOLLAR;
        pit.otherGross.client = STRING_ZERO_DOLLAR;
        pit.otherGross.partner = STRING_ZERO_DOLLAR;
        pit.otherGross.assessable = STRING_ZERO_DOLLAR;

        //
        // preparing data for row "Total income"
        //       
        // column JOINT
        double d = 0.0;

        // add Gross Salary Income
        if (pit.grossSalary.joint != null && pit.grossSalary.joint.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.grossSalary.joint));
        }
        // add Other Taxable Income
        if (pit.otherTaxable.joint != null
                && pit.otherTaxable.joint.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherTaxable.joint));
        }
        // add Rental income
        if (pit.rental.joint != null && pit.rental.joint.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.rental.joint));
        }
        // add Complying/Allocated Pensions
        if (pit.pensions.joint != null && pit.pensions.joint.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.pensions.joint));
        }
        // add Other Gross income
        if (pit.otherGross.joint != null && pit.otherGross.joint.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.otherGross.joint));
        }
        // pit.total.joint = new Double(d).toString();
        pit.total.joint = _money.toString(new BigDecimal(d));

        //
        // preparing data for row "Total income"
        //       
        // column CLIENT
        d = 0.0;

        // add Gross Salary Income
        if (pit.grossSalary.client != null
                && pit.grossSalary.client.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.grossSalary.client));
        }
        // add Other Taxable Income
        if (pit.otherTaxable.client != null
                && pit.otherTaxable.client.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherTaxable.client));
        }
        // add Rental income
        if (pit.rental.client != null && pit.rental.client.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.rental.client));
        }
        // add Complying/Allocated Pensions
        if (pit.pensions.client != null && pit.pensions.client.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.pensions.client));
        }
        // add Other Gross income
        if (pit.otherGross.client != null && pit.otherGross.client.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherGross.client));
        }
        // pit.total.client = new Double(d).toString();
        pit.total.client = _money.toString(new BigDecimal(d));

        //
        // preparing data for row "Total income"
        //       
        // column PARTNER
        d = 0.0;

        // add Gross Salary Income
        if (pit.grossSalary.partner != null
                && pit.grossSalary.partner.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.grossSalary.partner));
        }
        // add Other Taxable Income
        if (pit.otherTaxable.partner != null
                && pit.otherTaxable.partner.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherTaxable.partner));
        }
        // add Rental income
        if (pit.rental.partner != null && pit.rental.partner.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.rental.partner));
        }
        // add Complying/Allocated Pensions
        if (pit.pensions.partner != null && pit.pensions.partner.length() > 0) {
            d = d + Double.parseDouble(removeCommaDollar(pit.pensions.partner));
        }
        // add Other Gross income
        if (pit.otherGross.partner != null
                && pit.otherGross.partner.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherGross.partner));
        }
        // pit.total.partner = new Double(d).toString();
        pit.total.partner = _money.toString(new BigDecimal(d));

        //
        // preparing data for row "Total income"
        //       
        // column ASSESSABLE
        d = 0.0;

        // add Deemed Income
        if (pit.toDeeming.assessable != null
                && pit.toDeeming.assessable.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.toDeeming.assessable));
        }
        // add Gross Salary Income
        if (pit.grossSalary.assessable != null
                && pit.grossSalary.assessable.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.grossSalary.assessable));
        }
        // add Other Taxable Income
        if (pit.otherTaxable.assessable != null
                && pit.otherTaxable.assessable.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherTaxable.assessable));
        }
        // add Rental income
        if (pit.rental.assessable != null && pit.rental.assessable.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.rental.assessable));
        }
        // add Complying/Allocated Pensions
        if (pit.pensions.assessable != null
                && pit.pensions.assessable.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.pensions.assessable));
        }
        // add Other Gross income
        if (pit.otherGross.assessable != null
                && pit.otherGross.assessable.length() > 0) {
            d = d
                    + Double
                            .parseDouble(removeCommaDollar(pit.otherGross.assessable));
        }
        // pit.total.assessable = new Double(d).toString();
        pit.total.assessable = _money.toString(new BigDecimal(d));

        //
        // Income Test Result
        //
        pit.result.assessable = dssCalc2.getIncomeTestC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getIncomeTestC());

        //
        // preparing data for row "Less Centrelink exempt income"
        //       
        // all columns are empty

        //
        // preparing data for row "Less Centrelink exempt income"
        //       
        pit.netCentrelink.joint = new String(STRING_EMPTY);
        pit.netCentrelink.client = new String(STRING_EMPTY);
        pit.netCentrelink.partner = new String(STRING_EMPTY);
        pit.netCentrelink.assessable = pit.total.assessable;

        //
        // preparing data for row "Less Centrelink exempt income"
        //       
        // all columns are empty

        //
        // ClientView Results
        //
        clientResults.maximumBenefit = dssCalc2.getMaxBenefitC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getMaxBenefitC());
        clientResults.assetTest = dssCalc2.getAssetTestC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getAssetTestC());
        clientResults.incomeTest = dssCalc2.getIncomeTestC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getIncomeTestC());
        clientResults.basicBenefit = dssCalc2.getBasicBenefitC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getBasicBenefitC());
        clientResults.pharmaceuticalAllowance = dssCalc2.getPharmAllowanceC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getPharmAllowanceC());
        clientResults.rentAssistance = dssCalc2.getRentAssistanceC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getRentAssistanceC());
        clientResults.pensionerRebate = dssCalc2.getPensionerRebateC() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getPensionerRebateC());

        //
        // Partner Results
        //
        partnerResults.maximumBenefit = dssCalc2.getMaxBenefitP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getMaxBenefitP());
        partnerResults.assetTest = dssCalc2.getAssetTestP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getAssetTestP());
        partnerResults.incomeTest = dssCalc2.getIncomeTestP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getIncomeTestP());
        partnerResults.basicBenefit = dssCalc2.getBasicBenefitP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getBasicBenefitP());
        partnerResults.pharmaceuticalAllowance = dssCalc2.getPharmAllowanceP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getPharmAllowanceP());
        partnerResults.rentAssistance = dssCalc2.getRentAssistanceP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getRentAssistanceP());
        partnerResults.pensionerRebate = dssCalc2.getPensionerRebateP() == null ? STRING_ZERO_DOLLAR
                : _money.toString(dssCalc2.getPensionerRebateP());

        //
        // Joint Results
        //
        jointResults.maxPensionPA = _money
                .toString(dssCalc2.getMaxAgePension());

        //
        // calculate actual pension
        //
        jointResults.actualPensionPA = _money.toString(dssCalc2
                .getActualAgePensionPA());
        jointResults.actualPensionFN = _money.toString(dssCalc2
                .getActualAgePensionPF());
    }

    private String removeCommaDollar(String str) {
        boolean found = false;
        char c;
        StringBuffer help = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c != ',' && c != '$') {
                help.append(c);
            }
        }

        return help.toString();
    }

    /*
     * ============================================================================
     * BEGIN: TableModels
     * ============================================================================
     */
    /***************************************************************************
     * Assets Test TableModel
     **************************************************************************/
    public class AssetsTestTableModel extends
            au.com.totemsoft.myplanner.table.swing.SmartTableModel {
        protected java.util.Vector columnNames = new java.util.Vector();

        protected java.util.Vector columnClasses = new java.util.Vector();

        public final int COLUMN_ASSETS = 0;

        public final int COLUMN_JOINT = 1;

        public final int COLUMN_CLIENT = 2;

        public final int COLUMN_PARTNER = 3;

        public final int COLUMN_ASSESSABLE = 4;

        public final int COLUMN_COUNT = 5;

        public AssetsTestTableRow home_contents = new AssetsTestTableRow(
                "Home Contents", AssetsTestTableRow.BODY);

        public AssetsTestTableRow cars_caravans_boats = new AssetsTestTableRow(
                "Cars/Caravans/Boats", AssetsTestTableRow.BODY);

        public AssetsTestTableRow investment_property = new AssetsTestTableRow(
                "Investment Property", AssetsTestTableRow.BODY);

        public AssetsTestTableRow savings = new AssetsTestTableRow("Savings",
                AssetsTestTableRow.BODY);

        public AssetsTestTableRow managed_funds = new AssetsTestTableRow(
                "Managed Funds", AssetsTestTableRow.BODY);

        public AssetsTestTableRow shares_derivatives = new AssetsTestTableRow(
                "Shares/Derivatives", AssetsTestTableRow.BODY);

        public AssetsTestTableRow bonds_debentures = new AssetsTestTableRow(
                "Bonds/Debentures", AssetsTestTableRow.BODY);

        public AssetsTestTableRow fixed_interest = new AssetsTestTableRow(
                "Fixed Interest", AssetsTestTableRow.BODY);

        public AssetsTestTableRow gifts_over_10000 = new AssetsTestTableRow(
                "Gifts Over $10,000", AssetsTestTableRow.BODY);

        public AssetsTestTableRow loans_owed = new AssetsTestTableRow(
                "Loans Owed", AssetsTestTableRow.BODY);

        public AssetsTestTableRow superannuation = new AssetsTestTableRow(
                "Superannuation", AssetsTestTableRow.BODY);

        public AssetsTestTableRow allocated_pension = new AssetsTestTableRow(
                "Allocated Pension", AssetsTestTableRow.BODY);

        public AssetsTestTableRow complying_pension = new AssetsTestTableRow(
                "Complying Pension", AssetsTestTableRow.BODY);

        public AssetsTestTableRow total = new AssetsTestTableRow("Total",
                AssetsTestTableRow.FOOTER1);

        public AssetsTestTableRow assessable_assets = new AssetsTestTableRow(
                "Centrelink assessable assets", AssetsTestTableRow.BODY);

        public AssetsTestTableRow threshold = new AssetsTestTableRow(
                "Assets test allowable threshold", AssetsTestTableRow.BODY);

        public AssetsTestTableRow pension_payable = new AssetsTestTableRow(
                "Pension Payable - Assets Test", AssetsTestTableRow.FOOTER1);

        /** Creates a new instance of AssetAllocationTableModel */
        public AssetsTestTableModel(PersonService person) throws java.io.IOException {
            generalInit();
            populateTable();
        }

        public AssetsTestTableModel() throws java.io.IOException {
            generalInit();
            populateTable();
        }

        /*
         * Initialize the table column names, classes and data vector
         */
        private void generalInit() {
            columnNames.add("Assessable Assets");
            columnClasses.add(java.lang.String.class);
            columnNames.add("Joint ($)");
            columnClasses.add(java.math.BigDecimal.class);
            columnNames.add("ClientView ($)");
            columnClasses.add(java.math.BigDecimal.class);
            columnNames.add("Partner ($)");
            columnClasses.add(java.math.BigDecimal.class);
            columnNames.add("Assessable ($)");
            columnClasses.add(java.math.BigDecimal.class);

            setColumnNames(columnNames);
            setColumnClasses(columnClasses);

            // init tablemodel structure
            java.util.Vector data = new java.util.Vector();
            setData(data);

            data.add(home_contents);
            data.add(cars_caravans_boats);
            data.add(investment_property);
            data.add(savings);
            data.add(managed_funds);
            data.add(shares_derivatives);
            data.add(bonds_debentures);
            data.add(fixed_interest);
            data.add(gifts_over_10000);
            data.add(loans_owed);
            data.add(superannuation);
            data.add(allocated_pension);
            data.add(complying_pension);
            data.add(total);
            data.add(assessable_assets);
            data.add(threshold);
            data.add(pension_payable);
        }

        private void populateTable() {
            home_contents.setValueAt(pat.homeContents.joint, this.COLUMN_JOINT);
            home_contents.setValueAt(pat.homeContents.client,
                    this.COLUMN_CLIENT);
            home_contents.setValueAt(pat.homeContents.partner,
                    this.COLUMN_PARTNER);
            home_contents.setValueAt(pat.homeContents.assessable,
                    this.COLUMN_ASSESSABLE);

            cars_caravans_boats
                    .setValueAt(pat.carsETC.joint, this.COLUMN_JOINT);
            cars_caravans_boats.setValueAt(pat.carsETC.client,
                    this.COLUMN_CLIENT);
            cars_caravans_boats.setValueAt(pat.carsETC.partner,
                    this.COLUMN_PARTNER);
            cars_caravans_boats.setValueAt(pat.carsETC.assessable,
                    this.COLUMN_ASSESSABLE);

            investment_property.setValueAt(pat.property.joint,
                    this.COLUMN_JOINT);
            investment_property.setValueAt(pat.property.client,
                    this.COLUMN_CLIENT);
            investment_property.setValueAt(pat.property.partner,
                    this.COLUMN_PARTNER);
            investment_property.setValueAt(pat.property.assessable,
                    this.COLUMN_ASSESSABLE);

            savings.setValueAt(pat.savings.joint, this.COLUMN_JOINT);
            savings.setValueAt(pat.savings.client, this.COLUMN_CLIENT);
            savings.setValueAt(pat.savings.partner, this.COLUMN_PARTNER);
            savings.setValueAt(pat.savings.assessable, this.COLUMN_ASSESSABLE);

            managed_funds.setValueAt(pat.managedFunds.joint, this.COLUMN_JOINT);
            managed_funds.setValueAt(pat.managedFunds.client,
                    this.COLUMN_CLIENT);
            managed_funds.setValueAt(pat.managedFunds.partner,
                    this.COLUMN_PARTNER);
            managed_funds.setValueAt(pat.managedFunds.assessable,
                    this.COLUMN_ASSESSABLE);

            shares_derivatives.setValueAt(pat.shares.joint, this.COLUMN_JOINT);
            shares_derivatives
                    .setValueAt(pat.shares.client, this.COLUMN_CLIENT);
            shares_derivatives.setValueAt(pat.shares.partner,
                    this.COLUMN_PARTNER);
            shares_derivatives.setValueAt(pat.shares.assessable,
                    this.COLUMN_ASSESSABLE);

            bonds_debentures.setValueAt(pat.bonds.joint, this.COLUMN_JOINT);
            bonds_debentures.setValueAt(pat.bonds.client, this.COLUMN_CLIENT);
            bonds_debentures.setValueAt(pat.bonds.partner, this.COLUMN_PARTNER);
            bonds_debentures.setValueAt(pat.bonds.assessable,
                    this.COLUMN_ASSESSABLE);

            fixed_interest.setValueAt(pat.fixedInterest.joint,
                    this.COLUMN_JOINT);
            fixed_interest.setValueAt(pat.fixedInterest.client,
                    this.COLUMN_CLIENT);
            fixed_interest.setValueAt(pat.fixedInterest.partner,
                    this.COLUMN_PARTNER);
            fixed_interest.setValueAt(pat.fixedInterest.assessable,
                    this.COLUMN_ASSESSABLE);

            gifts_over_10000.setValueAt(pat.gifts.joint, this.COLUMN_JOINT);
            gifts_over_10000.setValueAt(pat.gifts.client, this.COLUMN_CLIENT);
            gifts_over_10000.setValueAt(pat.gifts.partner, this.COLUMN_PARTNER);
            gifts_over_10000.setValueAt(pat.gifts.assessable,
                    this.COLUMN_ASSESSABLE);

            loans_owed.setValueAt(pat.loans.joint, this.COLUMN_JOINT);
            loans_owed.setValueAt(pat.loans.client, this.COLUMN_CLIENT);
            loans_owed.setValueAt(pat.loans.partner, this.COLUMN_PARTNER);
            loans_owed.setValueAt(pat.loans.assessable, this.COLUMN_ASSESSABLE);

            superannuation.setValueAt(pat.superannuation.joint,
                    this.COLUMN_JOINT);
            superannuation.setValueAt(pat.superannuation.client,
                    this.COLUMN_CLIENT);
            superannuation.setValueAt(pat.superannuation.partner,
                    this.COLUMN_PARTNER);
            superannuation.setValueAt(pat.superannuation.assessable,
                    this.COLUMN_ASSESSABLE);

            allocated_pension.setValueAt(pat.allocatedPension.joint,
                    this.COLUMN_JOINT);
            allocated_pension.setValueAt(pat.allocatedPension.client,
                    this.COLUMN_CLIENT);
            allocated_pension.setValueAt(pat.allocatedPension.partner,
                    this.COLUMN_PARTNER);
            allocated_pension.setValueAt(pat.allocatedPension.assessable,
                    this.COLUMN_ASSESSABLE);

            complying_pension.setValueAt(pat.complyingPension.joint,
                    this.COLUMN_JOINT);
            complying_pension.setValueAt(pat.complyingPension.client,
                    this.COLUMN_CLIENT);
            complying_pension.setValueAt(pat.complyingPension.partner,
                    this.COLUMN_PARTNER);
            complying_pension.setValueAt(pat.complyingPension.assessable,
                    this.COLUMN_ASSESSABLE);

            total.setValueAt(pat.total.joint, this.COLUMN_JOINT);
            total.setValueAt(pat.total.client, this.COLUMN_CLIENT);
            total.setValueAt(pat.total.partner, this.COLUMN_PARTNER);
            total.setValueAt(pat.total.assessable, this.COLUMN_ASSESSABLE);

            assessable_assets.setValueAt(pat.centrelink.joint,
                    this.COLUMN_JOINT);
            assessable_assets.setValueAt(pat.centrelink.client,
                    this.COLUMN_CLIENT);
            assessable_assets.setValueAt(pat.centrelink.partner,
                    this.COLUMN_PARTNER);
            assessable_assets.setValueAt(pat.centrelink.assessable,
                    this.COLUMN_ASSESSABLE);

            threshold.setValueAt(pat.threshold.joint, this.COLUMN_JOINT);
            threshold.setValueAt(pat.threshold.client, this.COLUMN_CLIENT);
            threshold.setValueAt(pat.threshold.partner, this.COLUMN_PARTNER);
            threshold.setValueAt(pat.threshold.assessable,
                    this.COLUMN_ASSESSABLE);

            pension_payable.setValueAt(pat.result.joint, this.COLUMN_JOINT);
            pension_payable.setValueAt(pat.result.client, this.COLUMN_CLIENT);
            pension_payable.setValueAt(pat.result.partner, this.COLUMN_PARTNER);
            pension_payable.setValueAt(pat.result.assessable,
                    this.COLUMN_ASSESSABLE);
        }

        public class AssetsTestTableRow extends AbstractSmartTableRow {
            private java.util.Vector rowData; // String,
                                                // Numeric(Percent/Money)

            /** Creates a new instance of AssetAllocationRow */
            public AssetsTestTableRow(String str, int type) {
                super(type);

                rowData = new java.util.Vector(COLUMN_COUNT);
                rowData.add(COLUMN_ASSETS, str);
                for (int c = 1; c < COLUMN_COUNT; c++)
                    rowData.add(c, new String(STRING_EMPTY));
            }

            public String toString() {
                return (String) rowData.elementAt(COLUMN_ASSETS);
            }

            protected java.util.Vector getRowData() {
                return rowData;
            }

            public Object getValueAt(int columnIndex) {
                switch (columnIndex) {
                case COLUMN_ASSETS:
                case COLUMN_JOINT:
                case COLUMN_CLIENT:
                case COLUMN_PARTNER:
                case COLUMN_ASSESSABLE:
                    if (rowData != null
                            && rowData.elementAt(columnIndex) != null) {
                        return rowData.elementAt(columnIndex);
                    } else {
                        return null;
                    }
                default:
                    return null;
                }
            }

            public void setValueAt(Object obj, int columnIndex) {
                rowData.setElementAt(obj, columnIndex);
            }

        }
    }

    /***************************************************************************
     * Income Test TableModel
     **************************************************************************/
    public class IncomeTestTableModel extends
            au.com.totemsoft.myplanner.table.swing.SmartTableModel {
        protected java.util.Vector columnNames = new java.util.Vector();

        protected java.util.Vector columnClasses = new java.util.Vector();

        public final int COLUMN_ASSETS = 0;

        public final int COLUMN_JOINT = 1;

        public final int COLUMN_CLIENT = 2;

        public final int COLUMN_PARTNER = 3;

        public final int COLUMN_ASSESSABLE = 4;

        public final int COLUMN_COUNT = 5;

        public AssetsTestTableRow investment_property = new AssetsTestTableRow(
                "Investment Property", AssetsTestTableRow.BODY);

        public AssetsTestTableRow savings = new AssetsTestTableRow("Savings",
                AssetsTestTableRow.BODY);

        public AssetsTestTableRow managed_funds = new AssetsTestTableRow(
                "Managed Funds", AssetsTestTableRow.BODY);

        public AssetsTestTableRow shares_derivatives = new AssetsTestTableRow(
                "Shares/Derivatives", AssetsTestTableRow.BODY);

        public AssetsTestTableRow bonds_debentures = new AssetsTestTableRow(
                "Bonds/Debentures", AssetsTestTableRow.BODY);

        public AssetsTestTableRow fixed_interest = new AssetsTestTableRow(
                "Fixed Interest", AssetsTestTableRow.BODY);

        public AssetsTestTableRow gifts_over_10000 = new AssetsTestTableRow(
                "Gifts Over $10,000", AssetsTestTableRow.BODY);

        public AssetsTestTableRow loans_owed = new AssetsTestTableRow(
                "Loans Owed", AssetsTestTableRow.BODY);

        public AssetsTestTableRow superannuation = new AssetsTestTableRow(
                "Superannuation", AssetsTestTableRow.BODY);

        public AssetsTestTableRow total = new AssetsTestTableRow("Total",
                AssetsTestTableRow.FOOTER1);

        public AssetsTestTableRow indexed_threshold = new AssetsTestTableRow(
                "Indexed Threshold For Deemed Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow empty_row = new AssetsTestTableRow("",
                AssetsTestTableRow.BODY);

        public AssetsTestTableRow investment_income = new AssetsTestTableRow(
                "Investment Income", AssetsTestTableRow.FOOTER1);

        public AssetsTestTableRow deemed_income = new AssetsTestTableRow(
                "Deemed Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow gross_salary_income = new AssetsTestTableRow(
                "Gross Salary Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow other_taxable_income = new AssetsTestTableRow(
                "Other Taxable Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow rental_income = new AssetsTestTableRow(
                "Rental Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow pensions = new AssetsTestTableRow(
                "Complying and Allocated Pension", AssetsTestTableRow.BODY);

        public AssetsTestTableRow other_gross_income = new AssetsTestTableRow(
                "Other Gross Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow total_income = new AssetsTestTableRow(
                "Total Income", AssetsTestTableRow.FOOTER1);

        public AssetsTestTableRow centrelink_exempt_income = new AssetsTestTableRow(
                "Less Centrelink Exempt Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow centrelink_assessable_income = new AssetsTestTableRow(
                "Net Centrelink Assessable Income", AssetsTestTableRow.BODY);

        public AssetsTestTableRow allowable_income_threshold = new AssetsTestTableRow(
                "Allowable Income Threshold", AssetsTestTableRow.BODY);

        public AssetsTestTableRow pension_payable = new AssetsTestTableRow(
                "Pension Payable - Income Test", AssetsTestTableRow.FOOTER1);

        /** Creates a new instance of AssetAllocationTableModel */
        public IncomeTestTableModel(PersonService person) throws java.io.IOException {
            generalInit();
            populateTable();
        }

        public IncomeTestTableModel() throws java.io.IOException {
            generalInit();
            populateTable();
        }

        /*
         * Initialize the table column names, classes and data vector
         */
        private void generalInit() {
            columnNames.add("Financial Investments Subject To Deeming");
            columnClasses.add(java.lang.String.class);
            columnNames.add("Joint ($)");
            columnClasses.add(java.math.BigDecimal.class);
            columnNames.add("ClientView ($)");
            columnClasses.add(java.math.BigDecimal.class);
            columnNames.add("Partner ($)");
            columnClasses.add(java.math.BigDecimal.class);
            columnNames.add("Assessable ($)");
            columnClasses.add(java.math.BigDecimal.class);

            setColumnNames(columnNames);
            setColumnClasses(columnClasses);

            // init tablemodel structure
            java.util.Vector data = new java.util.Vector();
            setData(data);

            data.add(investment_property);
            data.add(savings);
            data.add(managed_funds);
            data.add(shares_derivatives);
            data.add(bonds_debentures);
            data.add(fixed_interest);
            data.add(gifts_over_10000);
            data.add(loans_owed);
            data.add(superannuation);
            data.add(total);
            data.add(indexed_threshold);
            data.add(empty_row);

            data.add(investment_income);
            data.add(deemed_income);
            data.add(gross_salary_income);
            data.add(other_taxable_income);
            data.add(rental_income);
            data.add(pensions);
            data.add(other_gross_income);
            data.add(total_income);
            data.add(centrelink_exempt_income);
            data.add(centrelink_assessable_income);
            data.add(allowable_income_threshold);
            data.add(pension_payable);
        }

        private void populateTable() {
            investment_property.setValueAt(pat.property.joint,
                    this.COLUMN_JOINT);
            investment_property.setValueAt(pat.property.client,
                    this.COLUMN_CLIENT);
            investment_property.setValueAt(pat.property.partner,
                    this.COLUMN_PARTNER);
            investment_property.setValueAt(pat.property.assessable,
                    this.COLUMN_ASSESSABLE);

            savings.setValueAt(pat.savings.joint, this.COLUMN_JOINT);
            savings.setValueAt(pat.savings.client, this.COLUMN_CLIENT);
            savings.setValueAt(pat.savings.partner, this.COLUMN_PARTNER);
            savings.setValueAt(pat.savings.assessable, this.COLUMN_ASSESSABLE);

            managed_funds.setValueAt(pat.managedFunds.joint, this.COLUMN_JOINT);
            managed_funds.setValueAt(pat.managedFunds.client,
                    this.COLUMN_CLIENT);
            managed_funds.setValueAt(pat.managedFunds.partner,
                    this.COLUMN_PARTNER);
            managed_funds.setValueAt(pat.managedFunds.assessable,
                    this.COLUMN_ASSESSABLE);

            shares_derivatives.setValueAt(pat.shares.joint, this.COLUMN_JOINT);
            shares_derivatives
                    .setValueAt(pat.shares.client, this.COLUMN_CLIENT);
            shares_derivatives.setValueAt(pat.shares.partner,
                    this.COLUMN_PARTNER);
            shares_derivatives.setValueAt(pat.shares.assessable,
                    this.COLUMN_ASSESSABLE);

            bonds_debentures.setValueAt(pat.bonds.joint, this.COLUMN_JOINT);
            bonds_debentures.setValueAt(pat.bonds.client, this.COLUMN_CLIENT);
            bonds_debentures.setValueAt(pat.bonds.partner, this.COLUMN_PARTNER);
            bonds_debentures.setValueAt(pat.bonds.assessable,
                    this.COLUMN_ASSESSABLE);

            fixed_interest.setValueAt(pat.fixedInterest.joint,
                    this.COLUMN_JOINT);
            fixed_interest.setValueAt(pat.fixedInterest.client,
                    this.COLUMN_CLIENT);
            fixed_interest.setValueAt(pat.fixedInterest.partner,
                    this.COLUMN_PARTNER);
            fixed_interest.setValueAt(pat.fixedInterest.assessable,
                    this.COLUMN_ASSESSABLE);

            gifts_over_10000.setValueAt(pat.gifts.joint, this.COLUMN_JOINT);
            gifts_over_10000.setValueAt(pat.gifts.client, this.COLUMN_CLIENT);
            gifts_over_10000.setValueAt(pat.gifts.partner, this.COLUMN_PARTNER);
            gifts_over_10000.setValueAt(pat.gifts.assessable,
                    this.COLUMN_ASSESSABLE);

            loans_owed.setValueAt(pat.loans.joint, this.COLUMN_JOINT);
            loans_owed.setValueAt(pat.loans.client, this.COLUMN_CLIENT);
            loans_owed.setValueAt(pat.loans.partner, this.COLUMN_PARTNER);
            loans_owed.setValueAt(pat.loans.assessable, this.COLUMN_ASSESSABLE);

            superannuation.setValueAt(pat.superannuation.joint,
                    this.COLUMN_JOINT);
            superannuation.setValueAt(pat.superannuation.client,
                    this.COLUMN_CLIENT);
            superannuation.setValueAt(pat.superannuation.partner,
                    this.COLUMN_PARTNER);
            superannuation.setValueAt(pat.superannuation.assessable,
                    this.COLUMN_ASSESSABLE);

            total.setValueAt(pat.total.joint, this.COLUMN_JOINT);
            total.setValueAt(pat.total.client, this.COLUMN_CLIENT);
            total.setValueAt(pat.total.partner, this.COLUMN_PARTNER);
            total.setValueAt(pat.total.assessable, this.COLUMN_ASSESSABLE);

            indexed_threshold.setValueAt(pit.indexedThreshold.joint,
                    this.COLUMN_JOINT);
            indexed_threshold.setValueAt(pit.indexedThreshold.client,
                    this.COLUMN_CLIENT);
            indexed_threshold.setValueAt(pit.indexedThreshold.partner,
                    this.COLUMN_PARTNER);
            indexed_threshold.setValueAt(pit.indexedThreshold.assessable,
                    this.COLUMN_ASSESSABLE);

            empty_row.setValueAt(new String(STRING_EMPTY), this.COLUMN_JOINT);
            empty_row.setValueAt(new String(STRING_EMPTY), this.COLUMN_CLIENT);
            empty_row.setValueAt(new String(STRING_EMPTY), this.COLUMN_PARTNER);
            empty_row.setValueAt(new String(STRING_EMPTY),
                    this.COLUMN_ASSESSABLE);

            investment_income.setValueAt("Joint ($)", this.COLUMN_JOINT);
            investment_income.setValueAt("ClientView ($)", this.COLUMN_CLIENT);
            investment_income.setValueAt("Partner ($)", this.COLUMN_PARTNER);
            investment_income.setValueAt("Assessable ($)",
                    this.COLUMN_ASSESSABLE);

            deemed_income.setValueAt(pit.toDeeming.joint, this.COLUMN_JOINT);
            deemed_income.setValueAt(pit.toDeeming.client, this.COLUMN_CLIENT);
            deemed_income
                    .setValueAt(pit.toDeeming.partner, this.COLUMN_PARTNER);
            deemed_income.setValueAt(pit.toDeeming.assessable,
                    this.COLUMN_ASSESSABLE);

            gross_salary_income.setValueAt(pit.grossSalary.joint,
                    this.COLUMN_JOINT);
            gross_salary_income.setValueAt(pit.grossSalary.client,
                    this.COLUMN_CLIENT);
            gross_salary_income.setValueAt(pit.grossSalary.partner,
                    this.COLUMN_PARTNER);
            gross_salary_income.setValueAt(pit.grossSalary.assessable,
                    this.COLUMN_ASSESSABLE);

            other_taxable_income.setValueAt(pit.otherTaxable.joint,
                    this.COLUMN_JOINT);
            other_taxable_income.setValueAt(pit.otherTaxable.client,
                    this.COLUMN_CLIENT);
            other_taxable_income.setValueAt(pit.otherTaxable.partner,
                    this.COLUMN_PARTNER);
            other_taxable_income.setValueAt(pit.otherTaxable.assessable,
                    this.COLUMN_ASSESSABLE);

            rental_income.setValueAt(pit.rental.joint, this.COLUMN_JOINT);
            rental_income.setValueAt(pit.rental.client, this.COLUMN_CLIENT);
            rental_income.setValueAt(pit.rental.partner, this.COLUMN_PARTNER);
            rental_income.setValueAt(pit.rental.assessable,
                    this.COLUMN_ASSESSABLE);

            pensions.setValueAt(pit.pensions.joint, this.COLUMN_JOINT);
            pensions.setValueAt(pit.pensions.client, this.COLUMN_CLIENT);
            pensions.setValueAt(pit.pensions.partner, this.COLUMN_PARTNER);
            pensions
                    .setValueAt(pit.pensions.assessable, this.COLUMN_ASSESSABLE);

            other_gross_income.setValueAt(pit.otherGross.joint,
                    this.COLUMN_JOINT);
            other_gross_income.setValueAt(pit.otherGross.client,
                    this.COLUMN_CLIENT);
            other_gross_income.setValueAt(pit.otherGross.partner,
                    this.COLUMN_PARTNER);
            other_gross_income.setValueAt(pit.otherGross.assessable,
                    this.COLUMN_ASSESSABLE);

            total_income.setValueAt(pit.total.joint, this.COLUMN_JOINT);
            total_income.setValueAt(pit.total.client, this.COLUMN_CLIENT);
            total_income.setValueAt(pit.total.partner, this.COLUMN_PARTNER);
            total_income.setValueAt(pit.total.assessable,
                    this.COLUMN_ASSESSABLE);

            centrelink_exempt_income.setValueAt(pit.lessCentrelink.joint,
                    this.COLUMN_JOINT);
            centrelink_exempt_income.setValueAt(pit.lessCentrelink.client,
                    this.COLUMN_CLIENT);
            centrelink_exempt_income.setValueAt(pit.lessCentrelink.partner,
                    this.COLUMN_PARTNER);
            centrelink_exempt_income.setValueAt(pit.lessCentrelink.assessable,
                    this.COLUMN_ASSESSABLE);

            centrelink_assessable_income.setValueAt(pit.netCentrelink.joint,
                    this.COLUMN_JOINT);
            centrelink_assessable_income.setValueAt(pit.netCentrelink.client,
                    this.COLUMN_CLIENT);
            centrelink_assessable_income.setValueAt(pit.netCentrelink.partner,
                    this.COLUMN_PARTNER);
            centrelink_assessable_income.setValueAt(
                    pit.netCentrelink.assessable, this.COLUMN_ASSESSABLE);

            allowable_income_threshold.setValueAt(
                    pit.allowableIncomeThreshold.joint, this.COLUMN_JOINT);
            allowable_income_threshold.setValueAt(
                    pit.allowableIncomeThreshold.client, this.COLUMN_CLIENT);
            allowable_income_threshold.setValueAt(
                    pit.allowableIncomeThreshold.partner, this.COLUMN_PARTNER);
            allowable_income_threshold.setValueAt(
                    pit.allowableIncomeThreshold.assessable,
                    this.COLUMN_ASSESSABLE);

            pension_payable.setValueAt(pit.result.joint, this.COLUMN_JOINT);
            pension_payable.setValueAt(pit.result.client, this.COLUMN_CLIENT);
            pension_payable.setValueAt(pit.result.partner, this.COLUMN_PARTNER);
            pension_payable.setValueAt(pit.result.assessable,
                    this.COLUMN_ASSESSABLE);

        }

        public class AssetsTestTableRow extends AbstractSmartTableRow {
            private java.util.Vector rowData; // String,
                                                // Numeric(Percent/Money)

            /** Creates a new instance of AssetAllocationRow */
            public AssetsTestTableRow(String str, int type) {
                super(type);

                rowData = new java.util.Vector(COLUMN_COUNT);
                rowData.add(COLUMN_ASSETS, str);
                for (int c = 1; c < COLUMN_COUNT; c++)
                    rowData.add(c, new String(STRING_EMPTY));
            }

            public String toString() {
                return (String) rowData.elementAt(COLUMN_ASSETS);
            }

            protected java.util.Vector getRowData() {
                return rowData;
            }

            public Object getValueAt(int columnIndex) {
                switch (columnIndex) {
                case COLUMN_ASSETS:
                case COLUMN_JOINT:
                case COLUMN_CLIENT:
                case COLUMN_PARTNER:
                case COLUMN_ASSESSABLE:
                    if (rowData != null
                            && rowData.elementAt(columnIndex) != null) {
                        return rowData.elementAt(columnIndex);
                    } else {
                        return null;
                    }
                default:
                    return null;
                }
            }

            public void setValueAt(Object obj, int columnIndex) {
                rowData.setElementAt(obj, columnIndex);
            }

        }
    }

    /*
     * ============================================================================
     * END: TableModels
     * ============================================================================
     */

    /***************************************************************************
     * au.com.totemsoft.activex.Reportable interface
     **************************************************************************/
    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields, clientService);
    }

    public void initializeReportData(
            au.com.totemsoft.myplanner.report.ReportFields reportFields,
            au.com.totemsoft.myplanner.service.PersonService person)
            throws java.io.IOException {
        if (person != null)
            reportFields.initialize(person);

        AssetsTestTableModel attm = new AssetsTestTableModel();
        reportFields.setValue(reportFields.DSS_AssetsTestTable, attm);

        IncomeTestTableModel ittm = new IncomeTestTableModel();
        reportFields.setValue(reportFields.DSS_IncomeTestTable, ittm);

        reportFields.setValue(reportFields.DSS_MaritalStatus,
                this.maritalStatus);
        reportFields.setValue(reportFields.DSS_HomeOwner, this.homeOwner);

        reportFields.setValue(reportFields.DSS_Pharmaceutical_Allowance_Client,
                this.clientResults.pharmaceuticalAllowance);
        reportFields.setValue(
                reportFields.DSS_Pharmaceutical_Allowance_Partner,
                this.partnerResults.pharmaceuticalAllowance);

        reportFields.setValue(reportFields.DSS_Rent_Assistance_Client,
                this.clientResults.rentAssistance);
        reportFields.setValue(reportFields.DSS_Rent_Assistance_Partner,
                this.partnerResults.rentAssistance);

        reportFields.setValue(reportFields.DSS_Max_Pension_Payable_pa,
                this.jointResults.maxPensionPA);
        reportFields.setValue(reportFields.DSS_Actual_Pension_Payable_pa,
                this.jointResults.actualPensionPA);
        reportFields.setValue(
                reportFields.DSS_Actual_Pension_Payable_fortnightly,
                this.jointResults.actualPensionFN);
    }
}
