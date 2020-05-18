/*
 * Assumptions.java
 *
 * Created on 5 December 2002, 14:13
 */

package com.argus.financials.bean;

import java.util.Date;
import java.util.TreeMap;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.TableDisplayMode;
import com.argus.financials.etc.GrowthRate;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.util.DateTimeUtils;
import com.argus.util.RateUtils;

public class Assumptions extends AbstractBase implements java.lang.Cloneable {

    static final long serialVersionUID = 3685814615883265246L;

    private String clientName;

    private Date clientDOB;

    private String partnerName;

    private java.util.Date partnerDOB;

    private Integer clientSexID;

    private Integer partnerSexID;

    private double inflation;

    private Date retirementDate;

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

    public Assumptions(PersonService person) throws ServiceException {
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

    public Date getClientDOB() {
        return clientDOB;
    }

    public void setClientDOB(Date value) {
        if (equals(clientDOB, value))
            return;
        clientDOB = value;
        setModified(true);
    }

    public double getClientAgeAtRetirement() {
        return DateTimeUtils.getYears(getClientDOB(), getRetirementDate());
    }

    public double getClientAgeAt(Date value) {
        return DateTimeUtils.getYears(getClientDOB(), value);
    }

    public double getClientAgeAtYearsToProject() {
        int years = getYearsToProject();
        return DateTimeUtils.getYears(getClientDOB(), new Date())
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

    public Date getPartnerDOB() {
        return partnerDOB;
    }

    public void setPartnerDOB(Date value) {
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

    public int[] getYearsMap(Date before) {
        return getYearsMap(before, TableDisplayMode.EVERY_YEAR);
    }

    public int[] getYearsMap(Date before, int displayMode) {

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

    public int[] getYearsMapRetirement(Date before) {
        return getYearsMapRetirement(before, TableDisplayMode.EVERY_YEAR);
    }

    public int[] getYearsMapRetirement(Date before, int displayMode) {

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

    public Date getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(Date value) {
        if (equals(retirementDate, value))
            return;
        retirementDate = value;
        setModified(true);
    }

    public double getYearsToRetirement() {
        return DateTimeUtils
                .getYears(new Date(), getRetirementDate());
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

    public void update(PersonService person) throws ServiceException {

        if (person == null)
            person = clientService;

        IPerson clientName = person == null ? null : person.getPersonName();
        IPersonHealth clientHealth = clientName == null ? null : clientName.getPersonHealth();
        TreeMap dependants = person == null ? null : person .getDependents();
        married = clientName == null ? false : !IMaritalCode.isSingle(clientName.getMarital().getId());

        setClientName(clientName == null ? null : clientName.getFullName());
        setClientDOB(clientName == null ? null : clientName.getDateOfBirth());
        setClientSex(clientName == null ? null : clientName.getSex().getId());
        clientHospitalCover = clientHealth == null ? false : clientHealth.isHospitalCover();
        clientMaritalStatus = clientName == null ? null : clientName.getMarital().getCode();
        clientDependents = dependants == null ? 0 : dependants.size();

        PersonService partner = person instanceof ClientService ? ((ClientService) person).getPartner(false) : null;
        IPerson partnerName = partner == null ? null : partner.getPersonName();
        IPersonHealth partnerHealth = partnerName == null ? null : partnerName.getPersonHealth();
        dependants = partner == null ? null : partner.getDependents();

        setPartnerName(partnerName == null ? null : partnerName.getFullName());
        setPartnerDOB(partnerName == null ? null : partnerName.getDateOfBirth());
        setPartnerSex(partnerName == null ? null : partnerName.getSex().getId());
        partnerHospitalCover = partnerHealth == null ? false : partnerHealth.isHospitalCover();
        partnerMaritalStatus = clientName == null ? null : clientName.getMarital().getCode();
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
