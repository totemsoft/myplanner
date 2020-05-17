/*
 * DSSCalcNew.java
 *
 * Created on April 28, 2003, 6:59 PM
 */

package com.argus.financials.projection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.argus.bean.AbstractComponentModel;
import com.argus.bean.MessageSent;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.UserPreferences;
import com.argus.financials.code.FundType;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.projection.dss.DSSContainer;
import com.argus.financials.projection.dss.NonDeemedAssetTableModel;
import com.argus.financials.projection.dss.NotEligibleException;
import com.argus.financials.projection.dss.PersonInfo;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.UpdateableTableModel;
import com.argus.financials.swing.table.UpdateableTableRow;
import com.argus.financials.table.FinancialColumnID;
import com.argus.util.DateTimeUtils;

public class DSSCalcNew extends AbstractComponentModel implements
        DocumentNames, com.argus.financials.report.Reportable {

    private DSSContainer dssc;

    private boolean pension;

    private boolean noClientPhar;

    private boolean noPartnerPhar;

    protected transient static ClientService clientService;
    protected transient static UserPreferences userPreferences;
    public static void setClientService(ClientService clientService) {
        DSSCalcNew.clientService = clientService;
    }
    public static void setUserPreferences(UserPreferences userPreferences) {
        DSSCalcNew.userPreferences = userPreferences;
    }

    /** Creates a new instance of DSSCalcNew */
    public DSSCalcNew() {
        dssc = new DSSContainer();
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private static final String[] componentNames = { CASH_ASSET_AA,
            MAN_FUNDS_AA, SHARES_AA, OTHER_INV_AA,
            // INV_PRO_AA,
            STA_AA, HC_AA, VBC_AA, OTHER_PER_AA, SAL_AI, OTHER_AI, INV_PRO_AI, };

    private static final String[] componentNamesC = { CASH_ASSET_AA_C,
            MAN_FUNDS_AA_C, SHARES_AA_C, OTHER_INV_AA_C,
            // INV_PRO_AA_C,
            STA_AA_C, HC_AA_C, VBC_AA_C, OTHER_PER_AA_C, SAL_AI_C, OTHER_AI_C,
            INV_PRO_AI_C, };

    private static final String[] componentNamesP = { CASH_ASSET_AA_P,
            MAN_FUNDS_AA_P, SHARES_AA_P, OTHER_INV_AA_P,
            // INV_PRO_AA_P,
            STA_AA_P, HC_AA_P, VBC_AA_P, OTHER_PER_AA_P, SAL_AI_P, OTHER_AI_P,
            INV_PRO_AI_P, };

    public void setValues(Map financials) {

        DSSUtils dssu = new DSSUtils(financials);

        for (int i = 0; i < componentNames.length; i++) {
            String componentName = componentNames[i];
            int columnIndex = FinancialColumnID.REAL_AMOUNT_CLIENT;
            setDouble(componentNamesC[i], dssu.doubleValue(componentName,
                    columnIndex), 2);
            columnIndex = FinancialColumnID.REAL_AMOUNT_PARTNER;
            setDouble(componentNamesP[i], dssu.doubleValue(componentName,
                    columnIndex), 2);

        }

        double invProC = dssu.doubleValue(INV_PRO_AA,
                FinancialColumnID.REAL_AMOUNT_CLIENT);
        double invProP = dssu.doubleValue(INV_PRO_AA,
                FinancialColumnID.REAL_AMOUNT_PARTNER);
        double hhC = dssu.doubleValue(HH_AA,
                FinancialColumnID.REAL_AMOUNT_CLIENT);
        double hhP = dssu.doubleValue(HH_AA,
                FinancialColumnID.REAL_AMOUNT_PARTNER);

        setDouble(INV_PRO_AA_C, invProC + hhC, 2);
        setDouble(INV_PRO_AA_P, invProP + hhP, 2);

        double client = dssu.doubleValue(DEEMED_SUP_AA,
                FinancialColumnID.REAL_AMOUNT_CLIENT);
        double partner = dssu.doubleValue(DEEMED_SUP_AA,
                FinancialColumnID.REAL_AMOUNT_PARTNER);
        double total = 0;
        validate("");
        try {
            dssc.isEligible(CLIENT_NAME, AGE_PENSION);
            setDouble(DEEMED_SUP_AA_C, client, 2);
            total += client;
        } catch (NotEligibleException e) {
        }

        try {
            dssc.isEligible(NAME_PARTNER, AGE_PENSION);
            setDouble(DEEMED_SUP_AA_P, partner, 2);
            total += partner;
        } catch (NotEligibleException e) {
        }

        if (dssu.doubleValue(HOME_OWNER, FinancialColumnID.REAL_AMOUNT) > 0)
            putValue(HOME_OWNER, "Yes");
        else
            putValue(HOME_OWNER, "No");

        setDouble(CASH_ASSET_AI_C, dssu.doubleValue(CASH_ASSET_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(CASH_ASSET_AI_P, dssu.doubleValue(CASH_ASSET_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(MAN_FUNDS_AI_C, dssu.doubleValue(MAN_FUNDS_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(MAN_FUNDS_AI_P, dssu.doubleValue(MAN_FUNDS_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(SHARES_AI_C, dssu.doubleValue(SHARES_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(SHARES_AI_P, dssu.doubleValue(SHARES_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(OTHER_INV_AI_C, dssu.doubleValue(OTHER_INV_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(OTHER_INV_AI_P, dssu.doubleValue(OTHER_INV_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(DEEMED_SUP_AI, 0, 2);
        setDouble(DEEMED_SUP_AI_C, 0, 2);
        setDouble(DEEMED_SUP_AI_P, 0, 2);

        setDouble(STA_AI_C, dssu.doubleValue(STA_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(STA_AI_P, dssu.doubleValue(STA_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(SAL_AI_C, dssu.doubleValue(SAL_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(SAL_AI_P, dssu.doubleValue(SAL_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(OTHER_AI_C, dssu.doubleValue(OTHER_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(OTHER_AI_P, dssu.doubleValue(OTHER_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        setDouble(INV_PRO_AI_C, dssu.doubleValue(INV_PRO_AI,
                FinancialColumnID.REAL_AMOUNT_CLIENT), 2);
        setDouble(INV_PRO_AI_P, dssu.doubleValue(INV_PRO_AI,
                FinancialColumnID.REAL_AMOUNT_PARTNER), 2);

        Vector v = new Vector();
        v.addAll(dssu.getDSSElements(FundType.rcPENSION, OwnerCode.CLIENT));
        v.addAll(dssu.getDSSElements(FundType.rcANNUITY, OwnerCode.CLIENT));
        v
                .addAll(dssu.getDSSElements(FundType.rcANNUITY_LONG,
                        OwnerCode.CLIENT));

        putValue(NON_DEEMED_ASSETS_C, v);

        v = new Vector();
        v.addAll(dssu.getDSSElements(FundType.rcPENSION, OwnerCode.PARTNER));
        v.addAll(dssu.getDSSElements(FundType.rcANNUITY, OwnerCode.PARTNER));
        v.addAll(dssu
                .getDSSElements(FundType.rcANNUITY_LONG, OwnerCode.PARTNER));

        putValue(NON_DEEMED_ASSETS_P, v);

    }

    public void setValues(ClientService client) throws ServiceException {

        IPerson pn = client == null ? null : client.getPersonName();
        putValue(CLIENT_NAME, pn == null ? null : pn.getFullName());

        setTrueDate(DOB, pn == null ? null : pn.getDateOfBirth());
        if (pn != null && pn.getDateOfBirth() != null)
            setInt(AGE, pn.getAge().doubleValue());
        setDate(CALCULATION_DATE, new Date());

        putValue(SEX_CODE, pn == null ? null : pn.getSex().getCode());
        putValue(MARITAL_STATUS, pn == null ? null : pn.getMarital().getCode());
        TreeMap dependents = client == null ? null : client.getDependents();
        setInt(CHILDREN_AMOUNT, dependents == null ? 0 : dependents.size());

        PersonService partner = client == null || !pn.isMarried() ? null : client.getPartner(false);
        pn = partner == null ? null : partner.getPersonName();
        putValue(NAME_PARTNER, pn == null ? null : pn.getFullName());

        setTrueDate(DOB_PARTNER, pn == null ? null : pn.getDateOfBirth());
        if (pn != null && pn.getDateOfBirth() != null)
            setInt(AGE_PARTNER, pn.getAge().doubleValue());
        setDate(CALCULATION_DATE_PARTNER, new Date());
        putValue(PARTNER_SEX_CODE, pn == null ? null : pn.getSex().getCode());

    }

    public boolean validate(String whoIsChanged) {

        String name = getValue(CLIENT_NAME);
        String sex = getValue(SEX_CODE);
        Date dob = getTrueDate(DOB);
        Date calcDate = getTrueDate(CALCULATION_DATE);

        String namePartner = getValue(NAME_PARTNER);
        String sexPartner = getValue(PARTNER_SEX_CODE);
        Date dobPartner = getTrueDate(DOB_PARTNER);
        Date calcDatePartner = getTrueDate(CALCULATION_DATE_PARTNER);

        String maritalStatus = getValue(MARITAL_STATUS);
        String isHomeOwner = getValue(HOME_OWNER);
        Object value = getValueAsObject(CHILDREN_AMOUNT);
        double children = -1;
        if (value != null)
            children = getDouble(CHILDREN_AMOUNT);

        dssc.setPersonInfo(new PersonInfo(name, dob, sex, calcDate,
                maritalStatus, isHomeOwner, children));
        if (dssc.getPersonInfo().isPartnered())
            dssc.setPartnerInfo(new PersonInfo(namePartner, dobPartner,
                    sexPartner, calcDatePartner, maritalStatus, isHomeOwner,
                    children));
        else
            dssc.setPartnerInfo(null);

        // Validating user input
        try {
            dssc.isEligible("", USER_INFO_PAGE);
            putValue("wizard", "2");
            boolean goForward = false;
            boolean tempPension = false;

            try {
                dssc.isEligible(CLIENT_NAME, AGE_PENSION);
                goForward = true;
                tempPension = true;
                sendMessage(this, MessageSent.INFO, CLIENT_AGE_PENSION_VALID);
            } catch (NotEligibleException e) {
                putValue(AGE_PENSION, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());

            }

            try {
                dssc.isEligible(NAME_PARTNER, AGE_PENSION);
                goForward = true;
                tempPension = true;
                sendMessage(this, MessageSent.INFO, PARTNER_AGE_PENSION_VALID);
            } catch (NotEligibleException e) {
                putValue(AGE_PENSION_PARTNER, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            try {
                dssc.isEligible(CLIENT_NAME, NEW_START_ALLOWANCE);
                goForward = true;
                sendMessage(this, MessageSent.INFO, CLIENT_NSA_VALID);
            } catch (NotEligibleException e) {
                putValue(NEW_START_ALLOWANCE, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }
            try {
                dssc.isEligible(NAME_PARTNER, NEW_START_ALLOWANCE);
                goForward = true;
                sendMessage(this, MessageSent.INFO, PARTNER_NSA_VALID);
            } catch (NotEligibleException e) {
                putValue(NEW_START_ALLOWANCE_PARTNER, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            try {
                dssc.isEligible(CLIENT_NAME, DISABILITY_SUPPORT_PENSION);
                goForward = true;
                sendMessage(this, MessageSent.INFO, CLIENT_DSP_VALID);
            } catch (NotEligibleException e) {
                putValue(DISABILITY_SUPPORT_PENSION, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            try {
                dssc.isEligible(CLIENT_NAME, WIDOW_ALLOWANCE);
                goForward = true;
                sendMessage(this, MessageSent.INFO, CLIENT_WA_VALID);
            } catch (NotEligibleException e) {
                putValue(WIDOW_ALLOWANCE, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            try {
                dssc.isEligible(NAME_PARTNER, DISABILITY_SUPPORT_PENSION);
                goForward = true;
                sendMessage(this, MessageSent.INFO, PARTNER_DSP_VALID);
            } catch (NotEligibleException e) {
                putValue(DISABILITY_SUPPORT_PENSION_PARTNER, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            try {
                dssc.isEligible(CLIENT_NAME, SICKNESS_ALLOWANCE);
                goForward = true;
                sendMessage(this, MessageSent.INFO, CLIENT_SA_VALID);
            } catch (NotEligibleException e) {
                putValue(SICKNESS_ALLOWANCE, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }
            try {
                dssc.isEligible(NAME_PARTNER, SICKNESS_ALLOWANCE);
                goForward = true;
                sendMessage(this, MessageSent.INFO, PARTNER_SA_VALID);
            } catch (NotEligibleException e) {
                putValue(SICKNESS_ALLOWANCE_PARTNER, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            String isAgePensionClient = getValue(AGE_PENSION);
            String isAgePensionPartner = getValue(AGE_PENSION_PARTNER);

            String isNSAClient = getValue(NEW_START_ALLOWANCE);
            String isNSAPartner = getValue(NEW_START_ALLOWANCE_PARTNER);

            String isDSPClient = getValue(DISABILITY_SUPPORT_PENSION);
            String isDSPPartner = getValue(DISABILITY_SUPPORT_PENSION_PARTNER);

            String isWAClient = getValue(WIDOW_ALLOWANCE);

            String isSAClient = getValue(SICKNESS_ALLOWANCE);
            String isSAPartner = getValue(SICKNESS_ALLOWANCE_PARTNER);

            String isCarerPayment = getValue(CARER_ALLOWANCE);
            String isCarerPaymentPartner = getValue(CARER_ALLOWANCE_PARTNER);

            dssc.addCalc(AGE_PENSION, isAgePensionClient);
            dssc.addCalc(AGE_PENSION_PARTNER, isAgePensionPartner);

            dssc.addCalc(NEW_START_ALLOWANCE, isNSAClient);
            dssc.addCalc(NEW_START_ALLOWANCE_PARTNER, isNSAPartner);

            dssc.addCalc(DISABILITY_SUPPORT_PENSION, isDSPClient);
            dssc.addCalc(DISABILITY_SUPPORT_PENSION_PARTNER, isDSPPartner);

            dssc.addCalc(WIDOW_ALLOWANCE, isWAClient);

            dssc.addCalc(SICKNESS_ALLOWANCE, isSAClient);
            dssc.addCalc(SICKNESS_ALLOWANCE_PARTNER, isSAPartner);

            dssc.addCalc(CARER_ALLOWANCE, isCarerPayment);
            dssc.addCalc(CARER_ALLOWANCE_PARTNER, isCarerPaymentPartner);

            if (goForward == true
                    && (YES.equals(isAgePensionClient) || YES
                            .equals(isAgePensionPartner))
                    || YES.equals(isNSAClient) || YES.equals(isNSAPartner)
                    || YES.equals(isDSPClient) || YES.equals(isDSPPartner)
                    || YES.equals(isWAClient) || YES.equals(isSAClient)
                    || YES.equals(isSAPartner) || YES.equals(isCarerPayment)
                    || YES.equals(isCarerPaymentPartner))
                putValue("wizard", "5");
            else
                putValue("wizard", "2");

            try {
                dssc.isEligible(NAME_PARTNER, PARTNER_ALLOWANCE);
                sendMessage(this, MessageSent.INFO, PARTNER_PA_VALID);
            } catch (NotEligibleException e) {
                putValue(PARTNER_ALLOWANCE_PARTNER, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            try {
                dssc.isEligible(CLIENT_NAME, PARTNER_ALLOWANCE);
                sendMessage(this, MessageSent.INFO, CLIENT_PA_VALID);
            } catch (NotEligibleException e) {
                putValue(PARTNER_ALLOWANCE, NO);
                sendMessage(this, MessageSent.WARNING, e.getMessage());
            }

            String isPartnerAllowance = getValue(PARTNER_ALLOWANCE);
            String isPartnerAllowancePartner = getValue(PARTNER_ALLOWANCE_PARTNER);
            dssc.addCalc(PARTNER_ALLOWANCE, isPartnerAllowance);
            dssc.addCalc(PARTNER_ALLOWANCE_PARTNER, isPartnerAllowancePartner);

            pension = tempPension;

        } catch (NotEligibleException e) {
            putValue("wizard", "1");

        }

        return true;
    }

    public void calculate(String whoIsChanged) {
        double cashAssetsC = getDouble(CASH_ASSET_AA_C);
        double cashAssetsP = getDouble(CASH_ASSET_AA_P);

        double manFundsC = getDouble(MAN_FUNDS_AA_C);
        double manFundsP = getDouble(MAN_FUNDS_AA_P);

        double sharesC = getDouble(SHARES_AA_C);
        double sharesP = getDouble(SHARES_AA_P);

        double otherInvC = getDouble(OTHER_INV_AA_C);
        double otherInvP = getDouble(OTHER_INV_AA_P);

        double deemedSuperC = getDouble(DEEMED_SUP_AA_C);
        double deemedSuperP = getDouble(DEEMED_SUP_AA_P);

        double staC = getDouble(STA_AA_C);
        double staP = getDouble(STA_AA_P);

        double invProC = getDouble(INV_PRO_AA_C);
        double invProP = getDouble(INV_PRO_AA_P);

        double homeContentsC = getDouble(HC_AA_C);
        double homeContentsP = getDouble(HC_AA_P);

        double vbcC = getDouble(VBC_AA_C);
        double vbcP = getDouble(VBC_AA_P);

        double otherPersonalC = getDouble(OTHER_PER_AA_C);
        double otherPersonalP = getDouble(OTHER_PER_AA_P);

        double giftAmountC = getDouble(GA_AA_C);
        double giftAmountP = getDouble(GA_AA_P);

        double loansC = getDouble(LOANS_AA_C);
        double loansP = getDouble(LOANS_AA_P);

        double salaryC = getDouble(SAL_AI_C);
        double salaryP = getDouble(SAL_AI_P);

        double otherIncomeC = getDouble(OTHER_AI_C);
        double otherIncomeP = getDouble(OTHER_AI_P);

        double invProIC = getDouble(INV_PRO_AI_C);
        double invProIP = getDouble(INV_PRO_AI_P);

        Collection v = (Collection) getValueAsObject(NON_DEEMED_ASSETS_C);
        Collection vP = (Collection) getValueAsObject(NON_DEEMED_ASSETS_P);
        double cashAssetsAIC = getDouble(CASH_ASSET_AI_C);
        double cashAssetsAIP = getDouble(CASH_ASSET_AI_P);

        double manFundsAIC = getDouble(MAN_FUNDS_AI_C);
        double manFundsAIP = getDouble(MAN_FUNDS_AI_P);

        double sharesAIC = getDouble(SHARES_AI_C);
        double sharesAIP = getDouble(SHARES_AI_P);

        double otherInvAIC = getDouble(OTHER_INV_AI_C);
        double otherInvAIP = getDouble(OTHER_INV_AI_P);

        double staAIC = getDouble(STA_AI_C);
        double staAIP = getDouble(STA_AI_P);

        setDouble(CASH_ASSET_AA, cashAssetsC + cashAssetsP, 2);
        setDouble(MAN_FUNDS_AA, manFundsC + manFundsP, 2);
        setDouble(SHARES_AA, sharesC + sharesP, 2);
        setDouble(OTHER_INV_AA, otherInvC + otherInvP, 2);
        setDouble(DEEMED_SUP_AA, deemedSuperC + deemedSuperP, 2);
        setDouble(STA_AA, staC + staP, 2);
        setDouble(INV_PRO_AA, invProC + invProP, 2);
        setDouble(HC_AA, homeContentsC + homeContentsP, 2);
        setDouble(VBC_AA, vbcC + vbcP, 2);
        setDouble(OTHER_PER_AA, otherPersonalC + otherPersonalP, 2);
        setDouble(SAL_AI, salaryC + salaryP, 2);
        setDouble(OTHER_AI, otherIncomeC + otherIncomeP, 2);
        setDouble(INV_PRO_AI, invProIC + invProIP, 2);
        setDouble(CASH_ASSET_AI, cashAssetsAIC + cashAssetsAIP, 2);
        setDouble(MAN_FUNDS_AI, manFundsAIC + manFundsAIP, 2);
        setDouble(SHARES_AI, sharesAIC + sharesAIP, 2);
        setDouble(OTHER_INV_AI, otherInvAIC + otherInvAIP, 2);
        setDouble(STA_AI, staAIC + staAIP, 2);

        double cashAssets = getDouble(CASH_ASSET_AA);
        double manFunds = getDouble(MAN_FUNDS_AA);
        double shares = getDouble(SHARES_AA);
        double otherInv = getDouble(OTHER_INV_AA);
        double deemedSuper = getDouble(DEEMED_SUP_AA);
        double sta = getDouble(STA_AA);
        double invPro = getDouble(INV_PRO_AA);
        double homeContents = getDouble(HC_AA);
        double vbc = getDouble(VBC_AA);
        double otherPersonal = getDouble(OTHER_PER_AA);
        double giftAmount = getDouble(GA_AA);
        double loans = getDouble(LOANS_AA);
        double salary = getDouble(SAL_AI);
        double otherIncome = getDouble(OTHER_AI);
        double invProI = getDouble(INV_PRO_AI);
        double cashAssetsAI = getDouble(CASH_ASSET_AI);
        double manFundsAI = getDouble(MAN_FUNDS_AI);
        double sharesAI = getDouble(SHARES_AI);
        double otherInvAI = getDouble(OTHER_INV_AI);
        double staAI = getDouble(STA_AI);

        dssc.set(DEEMED_ASSET, CASH_ASSET, COMBINED, cashAssets, cashAssets,
                cashAssetsAI, 0);
        dssc.set(DEEMED_ASSET, CASH_ASSET, CLIENT, cashAssetsC, cashAssetsC,
                cashAssetsAIC, 0);
        dssc.set(DEEMED_ASSET, CASH_ASSET, PARTNER, cashAssetsP, cashAssetsP,
                cashAssetsAIP, 0);

        dssc.set(DEEMED_ASSET, MAN_FUNDS, COMBINED, manFunds, manFunds,
                manFundsAI, 0);
        dssc.set(DEEMED_ASSET, MAN_FUNDS, CLIENT, manFundsC, manFundsC,
                manFundsAIC, 0);
        dssc.set(DEEMED_ASSET, MAN_FUNDS, PARTNER, manFundsP, manFundsP,
                manFundsAIP, 0);

        dssc.set(DEEMED_ASSET, SHARES, COMBINED, shares, shares, sharesAI, 0);
        dssc.set(DEEMED_ASSET, SHARES, CLIENT, sharesC, sharesC, sharesAIC, 0);
        dssc.set(DEEMED_ASSET, SHARES, PARTNER, sharesP, sharesP, sharesAIP, 0);

        dssc.set(DEEMED_ASSET, OTHER_INV, COMBINED, otherInv, otherInv,
                otherInvAI, 0);
        dssc.set(DEEMED_ASSET, OTHER_INV, CLIENT, otherInvC, otherInvC,
                otherInvAIC, 0);
        dssc.set(DEEMED_ASSET, OTHER_INV, PARTNER, otherInvP, otherInvP,
                otherInvAIP, 0);

        dssc.set(DEEMED_ASSET, DEEMED_SUP, COMBINED, deemedSuper, deemedSuper,
                0, 0);
        dssc.set(DEEMED_ASSET, DEEMED_SUP, CLIENT, deemedSuperC, deemedSuperC,
                0, 0);
        dssc.set(DEEMED_ASSET, DEEMED_SUP, PARTNER, deemedSuperP, deemedSuperP,
                0, 0);

        dssc.set(DEEMED_ASSET, STA, COMBINED, sta, sta, staAI, 0);
        dssc.set(DEEMED_ASSET, STA, CLIENT, staC, staC, staAIC, 0);
        dssc.set(DEEMED_ASSET, STA, PARTNER, staP, staP, staAIP, 0);

        dssc.set(NON_DEEMED_ASSET, INV_PRO, COMBINED, invPro, invPro, invProI,
                invProI * .75);
        dssc.set(NON_DEEMED_ASSET, INV_PRO, CLIENT, invProC, invProC, invProIC,
                invProIC * .75);
        dssc.set(NON_DEEMED_ASSET, INV_PRO, PARTNER, invProP, invProP,
                invProIP, invProIP * .75);

        dssc
                .set(PERSONAL_ASSET, HC, COMBINED, homeContents, homeContents,
                        0, 0);
        dssc
                .set(PERSONAL_ASSET, HC, CLIENT, homeContentsC, homeContentsC,
                        0, 0);
        dssc.set(PERSONAL_ASSET, HC, PARTNER, homeContentsP, homeContentsP, 0,
                0);

        dssc.set(PERSONAL_ASSET, VBC, COMBINED, vbc, vbc, 0, 0);
        dssc.set(PERSONAL_ASSET, VBC, CLIENT, vbcC, vbcC, 0, 0);
        dssc.set(PERSONAL_ASSET, VBC, PARTNER, vbcP, vbcP, 0, 0);

        dssc.set(PERSONAL_ASSET, OTHER_PER, COMBINED, otherPersonal,
                otherPersonal, 0, 0);
        dssc.set(PERSONAL_ASSET, OTHER_PER, CLIENT, otherPersonalC,
                otherPersonalC, 0, 0);
        dssc.set(PERSONAL_ASSET, OTHER_PER, PARTNER, otherPersonalP,
                otherPersonalP, 0, 0);

        dssc.set(PERSONAL_ASSET, GA, COMBINED, giftAmount, giftAmount, 0, 0);
        dssc.set(PERSONAL_ASSET, GA, CLIENT, giftAmountC, giftAmountC, 0, 0);
        dssc.set(PERSONAL_ASSET, GA, PARTNER, giftAmountP, giftAmountP, 0, 0);

        dssc.set(PERSONAL_ASSET, LOANS, COMBINED, loans, loans, 0, 0);
        dssc.set(PERSONAL_ASSET, LOANS, CLIENT, loansC, loansC, 0, 0);
        dssc.set(PERSONAL_ASSET, LOANS, PARTNER, loansP, loansP, 0, 0);

        dssc.set(INCOME, SAL, COMBINED, 0, 0, salary, salary);
        dssc.set(INCOME, SAL, CLIENT, 0, 0, salaryC, salaryC);
        dssc.set(INCOME, SAL, PARTNER, 0, 0, salaryP, salaryP);

        dssc.set(INCOME, OTHER, COMBINED, 0, 0, otherIncome, otherIncome);
        dssc.set(INCOME, OTHER, CLIENT, 0, 0, otherIncomeC, otherIncomeC);
        dssc.set(INCOME, OTHER, PARTNER, 0, 0, otherIncomeP, otherIncomeP);

        dssc.setNonDeemedAssets(v, CLIENT);
        dssc.setNonDeemedAssets(vP, PARTNER);
        Vector vCombined = new Vector();
        if (v != null)
            vCombined.addAll(v);
        if (vP != null)
            vCombined.addAll(vP);
        dssc.setNonDeemedAssets(vCombined, COMBINED);

        dssc.calculate();

        if (dssc.getPersonInfo() != null) {
            int age = dssc.getPersonInfo().age();
            if (age > 0)
                // putValue( AGE, new Integer(age) );
                setInt(AGE, age);
        }

        if (dssc.getPartnerInfo() != null) {
            int age = dssc.getPartnerInfo().age();
            if (age > 0)
                setInt(AGE_PARTNER, age);
        }
        setDouble(CASH_ASSET_TA, cashAssets, 2);
        setDouble(MAN_FUNDS_TA, manFunds, 2);
        setDouble(SHARES_TA, shares, 2);
        setDouble(OTHER_INV_TA, otherInv, 2);
        setDouble(DEEMED_SUP_TA, deemedSuper, 2);
        setDouble(STA_TA, sta, 2);
        setDouble(INV_PRO_TA, invPro, 2);
        setDouble(HC_TA, homeContents, 2);
        setDouble(VBC_TA, vbc, 2);
        setDouble(OTHER_PER_TA, otherPersonal, 2);
        setDouble(GA_TA, giftAmount, 2);
        setDouble(LOANS_TA, loans, 2);

        setDouble(CASH_ASSET_TA_C, cashAssetsC, 2);
        setDouble(MAN_FUNDS_TA_C, manFundsC, 2);
        setDouble(SHARES_TA_C, sharesC, 2);
        setDouble(OTHER_INV_TA_C, otherInvC, 2);
        setDouble(DEEMED_SUP_TA_C, deemedSuperC, 2);
        setDouble(STA_TA_C, staC, 2);
        setDouble(INV_PRO_TA_C, invProC, 2);
        setDouble(HC_TA_C, homeContentsC, 2);
        setDouble(VBC_TA_C, vbcC, 2);
        setDouble(OTHER_PER_TA_C, otherPersonalC, 2);
        setDouble(GA_TA_C, giftAmountC, 2);
        setDouble(LOANS_TA_C, loansC, 2);

        setDouble(SAL_TI, salary, 2);
        setDouble(SAL_TI_C, salaryC, 2);
        setDouble(SAL_TI_P, salaryP, 2);

        setDouble(OTHER_TI, otherIncome, 2);
        setDouble(OTHER_TI_C, otherIncomeC, 2);
        setDouble(OTHER_TI_P, otherIncomeP, 2);

        setDouble(INV_PRO_TI, invProI * .75, 2);
        setDouble(INV_PRO_TI_C, invProIC * .75, 2);
        setDouble(INV_PRO_TI_P, invProIP * .75, 2);

        setDouble(AP_AA_C, dssc.get(NON_DEEMED_ASSET, AP, CLIENT,
                NonDeemedAssetTableModel.ACTUAL_ASSET), 2);
        setDouble(AP_AA_P, dssc.get(NON_DEEMED_ASSET, AP, PARTNER,
                NonDeemedAssetTableModel.ACTUAL_ASSET), 2);
        setDouble(AP_AA, getDouble(AP_AA_C) + getDouble(AP_AA_P), 2);
        setDouble(AP_TA_C, getDouble(AP_AA_C), 2);
        setDouble(AP_TA, getDouble(AP_AA), 2);

        setDouble(AP_AI_C, dssc.get(NON_DEEMED_ASSET, AP, CLIENT,
                NonDeemedAssetTableModel.ACTUAL_INCOME), 2);
        setDouble(AP_AI_P, dssc.get(NON_DEEMED_ASSET, AP, PARTNER,
                NonDeemedAssetTableModel.ACTUAL_INCOME), 2);
        setDouble(AP_AI, getDouble(AP_AI_C) + getDouble(AP_AI_P), 2);

        setDouble(AP_TI_C, dssc.get(NON_DEEMED_ASSET, AP, CLIENT,
                NonDeemedAssetTableModel.TEST_INCOME), 2);
        setDouble(AP_TI_P, dssc.get(NON_DEEMED_ASSET, AP, PARTNER,
                NonDeemedAssetTableModel.TEST_INCOME), 2);
        setDouble(AP_TI, getDouble(AP_TI_C) + getDouble(AP_TI_P), 2);

        setDouble(ANN_AA_C, dssc.get(NON_DEEMED_ASSET, ANN, CLIENT,
                NonDeemedAssetTableModel.ACTUAL_ASSET), 2);
        setDouble(ANN_AA_P, dssc.get(NON_DEEMED_ASSET, ANN, PARTNER,
                NonDeemedAssetTableModel.ACTUAL_ASSET), 2);
        setDouble(ANN_AA, getDouble(ANN_AA_C) + getDouble(ANN_AA_P), 2);
        setDouble(ANN_TA, getDouble(ANN_AA), 2);
        setDouble(ANN_TA_C, getDouble(ANN_AA_C), 2);

        setDouble(ANN_AI_C, dssc.get(NON_DEEMED_ASSET, ANN, CLIENT,
                NonDeemedAssetTableModel.ACTUAL_INCOME), 2);
        setDouble(ANN_AI_P, dssc.get(NON_DEEMED_ASSET, ANN, PARTNER,
                NonDeemedAssetTableModel.ACTUAL_INCOME), 2);
        setDouble(ANN_AI, getDouble(ANN_AI_C) + getDouble(ANN_AI_P), 2);

        setDouble(ANN_TI_C, dssc.get(NON_DEEMED_ASSET, ANN, CLIENT,
                NonDeemedAssetTableModel.TEST_INCOME), 2);
        setDouble(ANN_TI_P, dssc.get(NON_DEEMED_ASSET, ANN, PARTNER,
                NonDeemedAssetTableModel.TEST_INCOME), 2);
        setDouble(ANN_TI, getDouble(ANN_TI_C) + getDouble(ANN_TI_P), 2);

        setDouble(COM_ANN_AA_C, dssc.get(NON_DEEMED_ASSET, COM_ANN, CLIENT,
                NonDeemedAssetTableModel.ACTUAL_ASSET), 2);
        setDouble(COM_ANN_AA_P, dssc.get(NON_DEEMED_ASSET, COM_ANN, PARTNER,
                NonDeemedAssetTableModel.ACTUAL_ASSET), 2);
        setDouble(COM_ANN_AA,
                getDouble(COM_ANN_AA_C) + getDouble(COM_ANN_AA_P), 2);
        setDouble(COM_ANN_TA, 0., 2);
        setDouble(COM_ANN_TA_C, 0., 2);

        setDouble(COM_ANN_AI_C, dssc.get(NON_DEEMED_ASSET, COM_ANN, CLIENT,
                NonDeemedAssetTableModel.ACTUAL_INCOME), 2);
        setDouble(COM_ANN_AI_P, dssc.get(NON_DEEMED_ASSET, COM_ANN, PARTNER,
                NonDeemedAssetTableModel.ACTUAL_INCOME), 2);
        setDouble(COM_ANN_AI,
                getDouble(COM_ANN_AI_C) + getDouble(COM_ANN_AI_P), 2);

        setDouble(COM_ANN_TI_C, dssc.get(NON_DEEMED_ASSET, COM_ANN, CLIENT,
                NonDeemedAssetTableModel.TEST_INCOME), 2);
        setDouble(COM_ANN_TI_P, dssc.get(NON_DEEMED_ASSET, COM_ANN, PARTNER,
                NonDeemedAssetTableModel.TEST_INCOME), 2);
        setDouble(COM_ANN_TI,
                getDouble(COM_ANN_TI_C) + getDouble(COM_ANN_TI_P), 2);

        setDouble(TDI, dssc.getTotalDeemedIncome(COMBINED), 2);
        setDouble(TDI_C, dssc.getTotalDeemedIncome(CLIENT), 2);
        setDouble(TDI_P, dssc.getTotalDeemedIncome(PARTNER), 2);

        setDouble(TOTAL_AA, dssc.getTotalActualAsset(COMBINED), 2);
        setDouble(TOTAL_AA_C, dssc.getTotalActualAsset(CLIENT), 2);
        setDouble(TOTAL_AA_P, dssc.getTotalActualAsset(PARTNER), 2);

        setDouble(TOTAL_TA, dssc.getTotalTestAsset(COMBINED), 2);
        setDouble(TOTAL_TA_C, dssc.getTotalTestAsset(CLIENT), 2);

        setDouble(TOTAL_AI, dssc.getTotalActualIncome(COMBINED), 2);
        setDouble(TOTAL_AI_C, dssc.getTotalActualIncome(CLIENT), 2);
        setDouble(TOTAL_AI_P, dssc.getTotalActualIncome(PARTNER), 2);

        setDouble(TOTAL_TI, dssc.getTotalTestIncome(COMBINED), 2);
        setDouble(TOTAL_TI_C, dssc.getTotalTestIncome(CLIENT), 2);
        setDouble(TOTAL_TI_P, dssc.getTotalTestIncome(PARTNER), 2);

        setDouble(MAX_BENEFIT, dssc.getResult(CLIENT, MAX_BENEFIT), 2);
        setDouble(ASSET_TEST_RESULT, dssc.getResult(CLIENT, ASSET_TEST_RESULT),
                2);
        setDouble(INCOME_TEST_RESULT, dssc
                .getResult(CLIENT, INCOME_TEST_RESULT), 2);
        setDouble(BASIC_BENEFIT, dssc.getResult(CLIENT, BASIC_BENEFIT), 2);
        setDouble(PHAR, dssc.getResult(CLIENT, PHAR), 2);

        setDouble(MAX_BENEFIT_P, dssc.getResult(PARTNER, MAX_BENEFIT), 2);
        setDouble(ASSET_TEST_RESULT_P, dssc.getResult(PARTNER,
                ASSET_TEST_RESULT), 2);
        setDouble(INCOME_TEST_RESULT_P, dssc.getResult(PARTNER,
                INCOME_TEST_RESULT), 2);
        setDouble(BASIC_BENEFIT_P, dssc.getResult(PARTNER, BASIC_BENEFIT), 2);
        setDouble(PHAR_P, dssc.getResult(PARTNER, PHAR), 2);

        setDouble(MAX_BENEFIT_ANN, dssc.getResult(CLIENT, MAX_BENEFIT_ANN), 2);
        setDouble(ASSET_TEST_RESULT_ANN, dssc.getResult(CLIENT,
                ASSET_TEST_RESULT_ANN), 2);
        setDouble(INCOME_TEST_RESULT_ANN, dssc.getResult(CLIENT,
                INCOME_TEST_RESULT_ANN), 2);
        setDouble(BASIC_BENEFIT_ANN, dssc.getResult(CLIENT, BASIC_BENEFIT_ANN),
                2);
        setDouble(PHAR_ANN, dssc.getResult(CLIENT, PHAR_ANN), 2);

        setDouble(MAX_BENEFIT_P_ANN, dssc.getResult(PARTNER, MAX_BENEFIT_ANN),
                2);
        setDouble(ASSET_TEST_RESULT_P_ANN, dssc.getResult(PARTNER,
                ASSET_TEST_RESULT_ANN), 2);
        setDouble(INCOME_TEST_RESULT_P_ANN, dssc.getResult(PARTNER,
                INCOME_TEST_RESULT_ANN), 2);
        setDouble(BASIC_BENEFIT_P_ANN, dssc.getResult(PARTNER,
                BASIC_BENEFIT_ANN), 2);
        setDouble(PHAR_P_ANN, dssc.getResult(PARTNER, PHAR_ANN), 2);

    }

    public DSSContainer getDSSContainer() {
        return dssc;
    }

    public boolean pensionAvailable() {
        return pension;
    }

    public javax.swing.table.TableModel getCentrelinkTableModel() {

        boolean addPartner = false;

        UpdateableTableModel utm = new UpdateableTableModel(new String[] {
                "Description", "", "", "" });

        com.argus.format.Currency currency = com.argus.format.Currency
                .getCurrencyInstance();
        if (dssc.getPersonInfo() != null && dssc.getPersonInfo().isPartnered())
            addPartner = true;

        UpdateableTableRow row;
        ArrayList values = new ArrayList();
        ArrayList columClasses = new ArrayList();
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");
        columClasses.add("String");

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Asset Test");
        values.add("");
        values.add("Actual Value");
        values.add("Test Value");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Deemed Assets");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Cash Assets");
        values.add("");
        values.add(currency.toString(getDouble(CASH_ASSET_AA), true));
        values.add(currency.toString(getDouble(CASH_ASSET_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Managed Funds");
        values.add("");
        values.add(currency.toString(getDouble(MAN_FUNDS_AA), true));
        values.add(currency.toString(getDouble(MAN_FUNDS_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Shares");
        values.add("");
        values.add(currency.toString(getDouble(SHARES_AA), true));
        values.add(currency.toString(getDouble(SHARES_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Other Investments");
        values.add("");
        values.add(currency.toString(getDouble(OTHER_INV_AA), true));
        values.add(currency.toString(getDouble(OTHER_INV_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Deemed Superannuation");
        values.add("");
        values.add(currency.toString(getDouble(DEEMED_SUP_AA), true));
        values.add(currency.toString(getDouble(DEEMED_SUP_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Short Term Annuities");
        values.add("");
        values.add(currency.toString(getDouble(STA_AA), true));
        values.add(currency.toString(getDouble(STA_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Non Deemed Assets");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Allocated Pensions");
        values.add("");
        values.add(currency.toString(getDouble(AP_AA), true));
        values.add(currency.toString(getDouble(AP_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Annuities");
        values.add("");
        values.add(currency.toString(getDouble(ANN_AA), true));
        values.add(currency.toString(getDouble(ANN_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Annuity (Long Term)");
        values.add("");
        values.add(currency.toString(getDouble(ANN_AA), true));
        values.add(currency.toString(getDouble(ANN_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Investment Property");
        values.add("");
        values.add(currency.toString(getDouble(INV_PRO_AA), true));
        values.add(currency.toString(getDouble(INV_PRO_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Personal Assets");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Home Contents");
        values.add("");
        values.add(currency.toString(getDouble(HC_AA), true));
        values.add(currency.toString(getDouble(HC_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Vehicles/Boats/Caravans");
        values.add("");
        values.add(currency.toString(getDouble(VBC_AA), true));
        values.add(currency.toString(getDouble(VBC_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Other");
        values.add("");
        values.add(currency.toString(getDouble(OTHER_PER_AA), true));
        values.add(currency.toString(getDouble(OTHER_PER_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Gifted Amounts");
        values.add("");
        values.add(currency.toString(getDouble(GA_AA), true));
        values.add(currency.toString(getDouble(GA_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Loans owed to Clients");
        values.add("");
        values.add(currency.toString(getDouble(LOANS_AA), true));
        values.add(currency.toString(getDouble(LOANS_TA), true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(TOTAL_TA), false));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("Allowable Asset Threshold");
        values.add("");
        values.add("");
        values.add(currency.toString(Math.max(dssc.getResult(CLIENT,
                ASSET_THRESHOLD), dssc.getResult(PARTNER, ASSET_THRESHOLD)),
                true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Benefit Payable - Assets Test");
        values.add("");
        values.add("ClientView");
        if (dssc.getPartnerInfo() == null)
            values.add("");
        else
            values.add("Partner");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("");
        values.add("");
        values.add(currency.toString(getDouble(ASSET_TEST_RESULT), false));
        if (dssc.getPartnerInfo() == null)
            values.add("");
        else
            values
                    .add(currency.toString(getDouble(ASSET_TEST_RESULT_P),
                            false));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Income Test");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add("Combined Test");
        } else {
            values.add("ClientView Test");
            if (dssc.getPartnerInfo() == null)
                values.add("");
            else
                values.add("Partner Test");
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Deemed Assets");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Cash Assets");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(CASH_ASSET_AI), true));
        } else {
            values.add(currency.toString(getDouble(CASH_ASSET_AI_C), true));
            values.add(currency.toString(getDouble(CASH_ASSET_AI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Managed Funds");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(MAN_FUNDS_AI), true));
        } else {
            values.add(currency.toString(getDouble(MAN_FUNDS_AI_C), true));
            values.add(currency.toString(getDouble(MAN_FUNDS_AI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Shares");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(SHARES_AI), true));
        } else {
            values.add(currency.toString(getDouble(SHARES_AI_C), true));
            values.add(currency.toString(getDouble(SHARES_AI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Other Investments");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(OTHER_INV_AI), true));
        } else {
            values.add(currency.toString(getDouble(OTHER_INV_AI_C), true));
            values.add(currency.toString(getDouble(OTHER_INV_AI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Deemed Superannuation");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(DEEMED_SUP_AI), true));
        } else {
            values.add(currency.toString(getDouble(DEEMED_SUP_AI_C), true));
            values.add(currency.toString(getDouble(DEEMED_SUP_AI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Short Term Annuities");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(STA_AI), true));
        } else {
            values.add(currency.toString(getDouble(STA_AI_C), true));
            values.add(currency.toString(getDouble(STA_AI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total Deemed Income");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(TDI), true));
        } else {
            values.add(currency.toString(getDouble(TDI_C), true));
            values.add(currency.toString(getDouble(TDI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Non Deemed Assets");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Allocated Pensions");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(AP_TI), true));
        } else {
            values.add(currency.toString(getDouble(AP_TI_C), true));
            values.add(currency.toString(getDouble(AP_TI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Annuities");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(ANN_TI), true));
        } else {
            values.add(currency.toString(getDouble(ANN_TI_C), true));
            values.add(currency.toString(getDouble(ANN_TI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Annuity (Long Term)");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(COM_ANN_TI), true));
        } else {
            values.add(currency.toString(getDouble(COM_ANN_TI_C), true));
            values.add(currency.toString(getDouble(COM_ANN_TI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Investment Property");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(INV_PRO_TI), true));
        } else {
            values.add(currency.toString(getDouble(INV_PRO_TI_C), true));
            values.add(currency.toString(getDouble(INV_PRO_TI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Income");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Salary");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(SAL_TI), true));
        } else {
            values.add(currency.toString(getDouble(SAL_TI_C), true));
            values.add(currency.toString(getDouble(SAL_TI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Other Income");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(OTHER_TI), true));
        } else {
            values.add(currency.toString(getDouble(OTHER_TI_C), true));
            values.add(currency.toString(getDouble(OTHER_TI_P), true));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Total");
        values.add("");
        if (dssc.getCalcModel() == A_P) {
            values.add("");
            values.add(currency.toString(getDouble(TOTAL_TI), true));
        } else {
            values.add(currency.toString(getDouble(TOTAL_TI_C), false));
            if (dssc.getPartnerInfo() == null)
                values.add("");
            else
                values.add(currency.toString(getDouble(TOTAL_TI_P), false));
        }
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("Full Payment Income Threshold");
        values.add("");
        values.add(currency.toString(
                dssc.getResult(CLIENT, INCOME_THRESHOLD) * 26, true));
        values.add(currency.toString(
                dssc.getResult(PARTNER, INCOME_THRESHOLD) * 26, true));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Benefit Payable - Income Test");
        values.add("");
        values.add(currency.toString(getDouble(INCOME_TEST_RESULT), false));
        if (dssc.getPartnerInfo() == null)
            values.add("");
        else
            values.add(currency
                    .toString(getDouble(INCOME_TEST_RESULT_P), false));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.FOOTER1);

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Results");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("");
        values.add("");
        values.add("ClientView");
        if (dssc.getPartnerInfo() == null)
            values.add("");
        else
            values.add("Partner");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER1);

        values.add("Fortnight Payment");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Actual Payment");
        values.add("");
        values.add(currency.toString(getDouble(BASIC_BENEFIT), false));
        if (dssc.getPartnerInfo() == null)
            values.add("");
        else
            values.add(currency.toString(getDouble(BASIC_BENEFIT_P), false));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        if (!noPartnerPhar || !noClientPhar) {
            values.add("Pharmaceutical Allowance");
            values.add("");
            if (!noClientPhar)
                values.add(currency.toString(getDouble(PHAR), false));
            else
                values.add("");

            if (!noPartnerPhar)
                values.add(currency.toString(getDouble(PHAR_P), false));
            else
                values.add("");
            row = new UpdateableTableRow(values, columClasses);
            utm.addRow(row);
            values.clear();
        }

        values.add("");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        values.add("Annual Payment");
        values.add("");
        values.add("");
        values.add("");
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();
        row.setRowType(row.HEADER2);

        values.add("Actual Payment");
        values.add("");
        values.add(currency.toString(getDouble(BASIC_BENEFIT_ANN), false));
        if (dssc.getPartnerInfo() == null)
            values.add("");
        else
            values
                    .add(currency.toString(getDouble(BASIC_BENEFIT_P_ANN),
                            false));
        row = new UpdateableTableRow(values, columClasses);
        utm.addRow(row);
        values.clear();

        if (!noPartnerPhar || !noClientPhar) {
            values.add("Pharmaceutical Allowance");
            values.add("");
            if (!noClientPhar)
                values.add(currency.toString(getDouble(PHAR_ANN), false));
            else
                values.add("");

            if (!noPartnerPhar)
                values.add(currency.toString(getDouble(PHAR_P_ANN), false));
            else
                values.add("");
            row = new UpdateableTableRow(values, columClasses);
            utm.addRow(row);
            values.clear();
        }

        return utm;
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields)
            throws java.io.IOException {
        initializeReportData(reportFields, clientService);
    }

    public void initializeReportData(
            com.argus.financials.report.ReportFields reportFields,
            PersonService person)
            throws java.io.IOException {

        if (person != null)
            reportFields.initialize(person);
        else {
            reportFields.setValue(reportFields.Adviser_FullName,
                userPreferences.getUser().getFullName());
            reportFields.setValue(reportFields.Client_FullName,
                getValue(CLIENT_NAME));
            Date now = new Date();
            reportFields.setValue(reportFields.DateMedium,
                DateTimeUtils.formatAsMEDIUM(now));
        }

        reportFields.setValue(reportFields.CentrelinkReport_Table,
                getCentrelinkTableModel());

    }

    public void setNoClientPhar(boolean value) {
        noClientPhar = value;
    }

    public void setNoPartnerPhar(boolean value) {
        noPartnerPhar = value;
    }

}
