/*
 * Assumptions.java
 *
 * Created on 5 December 2002, 14:13
 */

package com.argus.financials.bean;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.TableDisplayMode;
import com.argus.financials.etc.GrowthRate;
import com.argus.financials.etc.PersonName;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.utils.RateUtils;
import com.argus.util.DateTimeUtils;

public class Assumptions extends AbstractBase implements java.lang.Cloneable {
    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.financial.Assumptions

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 3685814615883265246L;

    private String clientName;

    private java.util.Date clientDOB;

    private String partnerName;

    private java.util.Date partnerDOB;

    private Integer clientSexID;

    private Integer partnerSexID;

    private double inflation;

    private java.util.Date retirementDate;

    private int years2project = 10;

    private int displayMode = 0;

    private int displayModeRetirement = 0;

    private double retirementIncome;

    private double lumpSumRequired;

    private boolean debtRepayment = true;

    private boolean lifeExpectancy = false;

    private int displayModeTerm = 0;

    private Integer investmentStrategyID;

    private boolean married;

    private boolean clientHospitalCover;

    private String clientMaritalStatus;

    private int clientDependents;

    private double clientIncome;

    private boolean partnerHospitalCover;

    private String partnerMaritalStatus;

    private int partnerDependents;

    private double partnerIncome;

    /** Creates a new instance of Assumptions */
    public Assumptions() {
    }

    public Assumptions(PersonService person) throws com.argus.financials.service.client.ServiceException {
        update(person);
    }

    public Object clone() throws CloneNotSupportedException {
        Assumptions a = (Assumptions) super.clone();
        a.removeChangeListener(null); // remove all Change Listeners
        return a;
    }

    public boolean isReady() {
        return true;
        // inflation >= 0.
        // && retirementDate != null
        // && retirementIncome >= 0.
        // && lumpSumRequired >= 0.
        // && investmentStrategyID != null
        // ;
    }

    /*
     * get/set
     */

    public Integer getClientSex() {
        return clientSexID;
    }

    public Integer getPartnerSex() {
        return partnerSexID;
    }

    public void setClientSex(Integer clientSexID) {
        this.clientSexID = clientSexID;
    }

    public void setPartnerSex(Integer partnerSexID) {
        this.partnerSexID = partnerSexID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String value) {
        if (equals(clientName, value))
            return;
        clientName = value;
        setModified(true);
    }

    public java.util.Date getClientDOB() {
        return clientDOB;
    }

    public void setClientDOB(java.util.Date value) {
        if (equals(clientDOB, value))
            return;
        clientDOB = value;
        setModified(true);
    }

    public double getClientAgeAtRetirement() {
        return DateTimeUtils.getYears(getClientDOB(), getRetirementDate());
    }

    public double getClientAgeAt(java.util.Date value) {
        return DateTimeUtils.getYears(getClientDOB(), value);
    }

    public double getClientAgeAtYearsToProject() {
        int years = getYearsToProject();
        return DateTimeUtils.getYears(getClientDOB(), new java.util.Date())
                + years;
    }

    public boolean getClientHospitalCover() {
        return clientHospitalCover;
    }

    public String getClientMaritalStatus() {
        return clientMaritalStatus;
    }

    public int getClientDependents() {
        return clientDependents;
    }

    public double getClientIncome() {
        return clientIncome;
    }

    public void setClientIncome(double value) {
        if (clientIncome == value)
            return;
        clientIncome = value;
        setModified(true);
    }

    public double getClientAge() {
        return DateTimeUtils.getYears(getClientDOB());
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String value) {
        if (equals(partnerName, value))
            return;
        partnerName = value;
        setModified(true);
    }

    public java.util.Date getPartnerDOB() {
        return partnerDOB;
    }

    public void setPartnerDOB(java.util.Date value) {
        if (equals(partnerDOB, value))
            return;
        partnerDOB = value;
        setModified(true);
    }

    public double getPartnerAgeAtRetirement() {
        return DateTimeUtils.getYears(getPartnerDOB(), getRetirementDate());
    }

    public boolean getPartnerHospitalCover() {
        return partnerHospitalCover;
    }

    public String getPartnerMaritalStatus() {
        return partnerMaritalStatus;
    }

    public int getPartnerDependents() {
        return partnerDependents;
    }

    public double getPartnerIncome() {
        return partnerIncome;
    }

    public void setPartnerIncome(double value) {
        if (partnerIncome == value)
            return;
        partnerIncome = value;
        setModified(true);
    }

    public double getPartnerAge() {
        return DateTimeUtils.getYears(getPartnerDOB());
    }

    public int getYearsToProject() {
        return years2project;
    }

    public void setYearsToProject(int value) {
        if (years2project == value)
            return;
        years2project = value;
        setModified(true);
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int value) {
        if (displayMode == value)
            return;
        displayMode = value;
        setModified(true);
    }

    public int[] getYearsMap() {
        return getYearsMap(getRetirementDate(), TableDisplayMode.EVERY_YEAR);
    }

    public int[] getYearsMap(java.util.Date before) {
        return getYearsMap(before, TableDisplayMode.EVERY_YEAR);
    }

    public int[] getYearsMap(java.util.Date before, int displayMode) {

        int yearEnd = getYearsToProject() - 1;
        if (before != null)
            yearEnd = Math.min(yearEnd, DateTimeUtils
                    .getFinancialYearEnd(before)
                    - DateTimeUtils.getFinancialYearEnd());

        return TableDisplayMode.getDisplayFrequency(displayMode, 0, yearEnd);

    }

    public int[] getYearsMap(int length, int displayMode) {
        return TableDisplayMode.getDisplayFrequency(displayMode, 0, length - 1);

    }

    public int getDisplayModeRetirement() {
        return displayModeRetirement;
    }

    public void setDisplayModeRetirement(int value) {
        if (displayModeRetirement == value)
            return;
        displayModeRetirement = value;
        setModified(true);
    }

    public int[] getYearsMapRetirement() {
        return getYearsMapRetirement(getRetirementDate(),
                TableDisplayMode.EVERY_YEAR);
    }

    public int[] getYearsMapRetirement(java.util.Date before) {
        return getYearsMapRetirement(before, TableDisplayMode.EVERY_YEAR);
    }

    public int[] getYearsMapRetirement(java.util.Date before, int displayMode) {

        int yearStart = getYearsToProject() - 1;
        int yearEnd = getYearsToProject() - 1;
        if (before != null)
            yearStart = java.lang.Math.min(getYearsToProject(), DateTimeUtils
                    .getFinancialYearEnd(before)
                    - DateTimeUtils.getFinancialYearEnd());

        return TableDisplayMode.getDisplayFrequency(displayMode, 0, yearEnd
                - yearStart);

    }

    public double getInflation() {
        return inflation;
    }

    public void setInflation(double value) {
        if (inflation == value)
            return;
        inflation = value;
        setModified(true);
    }

    public java.util.Date getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(java.util.Date value) {
        if (equals(retirementDate, value))
            return;
        retirementDate = value;
        setModified(true);
    }

    public double getYearsToRetirement() {
        return DateTimeUtils
                .getYears(new java.util.Date(), getRetirementDate());
    }

    public double getRetirementIncome() {
        return retirementIncome;
    }

    public void setRetirementIncome(double value) {
        if (retirementIncome == value)
            return;
        retirementIncome = value;
        setModified(true);
    }

    public void setRetirementIncome(java.lang.Number value) {
        setRetirementIncome(value == null ? 0. : value.doubleValue());
    }

    public double getRetirementIncomeAtRetirement() {
        // return MoneyCalc.getIndexedValue( retirementIncome, inflation, (int)
        // getYearsToRetirement() );
        return RateUtils.getCompoundedAmount(retirementIncome,
                inflation / 100., (int) getYearsToRetirement());
    }

    public double getLumpSumRequired() {
        return lumpSumRequired;
    }

    public void setLumpSumRequired(double value) {
        if (lumpSumRequired == value)
            return;
        lumpSumRequired = value;
        setModified(true);
    }

    public void setLumpSumRequired(java.lang.Number value) {
        setLumpSumRequired(value == null ? 0. : value.doubleValue());
    }

    public double getLumpSumRequiredAtRetirement() {
        // return MoneyCalc.getIndexedValue( lumpSumRequired, inflation, (int)
        // getYearsToRetirement() );
        return RateUtils.getCompoundedAmount(lumpSumRequired, inflation / 100.,
                (int) getYearsToRetirement());
    }

    public Integer getInvestmentStrategyID() {
        return investmentStrategyID;
    }

    public void setInvestmentStrategyID(Integer value) {
        if (equals(investmentStrategyID, value))
            return;
        investmentStrategyID = value;
        setModified(true);
    }

    public String getInvestmentStrategyDesc() {
        return new InvestmentStrategyCode()
                .getCodeDescription(investmentStrategyID);
    }

    public double getGrowthRate() {
        if (investmentStrategyID == null)
            return 0.;

        GrowthRate gr = new InvestmentStrategyCode()
                .getGrowthRate(investmentStrategyID);
        if (gr == null)
            return 0.;
        return gr.getGrowthRate();
    }

    public double getIncomeRate() {
        if (investmentStrategyID == null)
            return 0.;

        GrowthRate gr = new InvestmentStrategyCode()
                .getGrowthRate(investmentStrategyID);
        if (gr == null)
            return 0.;
        return gr.getIncomeRate();
    }

    public double getTotalRate() {
        if (investmentStrategyID == null)
            return 0.;

        GrowthRate gr = new InvestmentStrategyCode()
                .getGrowthRate(investmentStrategyID);
        if (gr == null)
            return 0.;
        return gr.getRate();
    }

    public boolean getDebtRepayment() {
        return debtRepayment;
    }

    public void setDebtRepayment(boolean value) {
        if (debtRepayment == value)
            return;
        debtRepayment = value;
        setModified(true);
    }

    public boolean getLifeExpectancy() {
        return lifeExpectancy;
    }

    public void setLifeExpectancy(boolean value) {
        lifeExpectancy = value;
        setModified(true);
    }

    public void setShowTermMode(int value) {
        displayModeTerm = value;
        setModified(true);
    }

    public int getShowTermMode() {
        return displayModeTerm;
    }

    public void update(PersonService person) throws com.argus.financials.service.client.ServiceException {

        if (person == null)
            person = ServiceLocator.getInstance().getClientPerson();

        PersonName clientName = person == null ? null : person.getPersonName();
        java.util.TreeMap dependants = person == null ? null : person
                .getDependents();
        married = clientName == null ? false : !MaritalCode.isSingle(clientName
                .getMaritalCodeID());

        setClientName(clientName == null ? null : clientName.getFullName());
        setClientDOB(clientName == null ? null : clientName.getDateOfBirth());
        setClientSex(clientName == null ? null : clientName.getSexCodeID());
        clientHospitalCover = person == null ? false : person
                .hasHospitalCover();
        clientMaritalStatus = clientName == null ? null : clientName
                .getMaritalCode();
        clientDependents = dependants == null ? 0 : dependants.size();

        PersonService partner = person instanceof ClientService ? ((ClientService) person)
                .getPartner(false)
                : null;
        PersonName partnerName = partner == null ? null : partner
                .getPersonName();
        dependants = partner == null ? null : partner.getDependents();

        setPartnerName(partnerName == null ? null : partnerName.getFullName());
        setPartnerDOB(partnerName == null ? null : partnerName.getDateOfBirth());
        setPartnerSex(partnerName == null ? null : partnerName.getSexCodeID());
        partnerHospitalCover = partner == null ? false : partner
                .hasHospitalCover();
        partnerMaritalStatus = clientName == null ? null : clientName
                .getMaritalCode();
        partnerDependents = dependants == null ? 0 : dependants.size();

        FinancialGoal fg = person == null ? null : person.getFinancialGoal();

        java.math.BigDecimal d = fg == null ? null : fg.getInflation();
        setInflation(d == null ? 0. : d.doubleValue());

        setRetirementDate(fg == null ? null : fg.getTargetDate(clientName
                .getDateOfBirth()));

        setRetirementIncome(fg == null ? null : fg.getTargetIncome());

        d = fg == null ? null : fg.getLumpSumRequired();
        setLumpSumRequired(d == null ? 0. : d.doubleValue());

        setInvestmentStrategyID(fg == null ? null : fg.getTargetStrategyID());

    }

}
