/*
 * DSSContainer.java
 *
 * Created on April 29, 2003, 9:46 AM
 */

package com.argus.financials.projection.dss;

/**
 * 
 * @author shibaevv
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.argus.financials.code.FundType;
import com.argus.financials.projection.DocumentNames;
import com.argus.util.ReferenceCode;

public class DSSContainer implements DocumentNames {

    public static final int collectionSize = 6;

    public static final int calcSize = 7;

    public static final int ownerSize = 3;

    private boolean calcIsEmpty = true;

    private PersonInfo personInfo;

    private PersonInfo partnerInfo;

    private DSSEligibilityCheck ec;

    private int calcModel = -1;;

    private double deemedIncome;

    private double deemedIncomeC;

    private double deemedIncomeP;

    private double totalActualAsset;

    private double totalActualAssetC;

    private double totalActualAssetP;

    private double totalTestAsset;

    private double totalTestAssetC;

    private double totalActualIncome;

    private double totalActualIncomeC;

    private double totalActualIncomeP;

    private double totalTestIncome;

    private double totalTestIncomeC;

    private double totalTestIncomeP;

    private DSSParams dssParams;

    private Object[][] incomeCollection;

    private Object[][] deemedAssetCollection;

    private Object[][] personalAssetCollection;

    private Object[][] nonDeemedAssetCollection;

    private Object[] calcCollection;

    private Object[] partnerCalcCollection;

    private double maxBenefit;

    private double assetTestResult;

    private double incomeTestResult;

    private double basicBenefit;

    private double phar;

    private double incomeThreshold;

    private double assetThreshold;

    private double maxBenefitPartner;

    private double assetTestResultPartner;

    private double incomeTestResultPartner;

    private double basicBenefitPartner;

    private double pharPartner;

    private double incomeThresholdPartner;

    private double assetThresholdPartner;

    /** Creates a new instance of DSSContainer */
    public DSSContainer() {
        reset();
    }

    public void reset() {

        incomeCollection = new Object[collectionSize][ownerSize];
        deemedAssetCollection = new Object[collectionSize][ownerSize];
        personalAssetCollection = new Object[collectionSize][ownerSize];
        nonDeemedAssetCollection = new Object[collectionSize][ownerSize];

        calcCollection = new Object[calcSize];
        partnerCalcCollection = new Object[calcSize];

    }

    public void set(String streamType, int position, int owner, DSSElement elem) {

        if (INCOME.equals(streamType))
            incomeCollection[position][owner] = elem;
        else if (NON_DEEMED_ASSET.equals(streamType))
            nonDeemedAssetCollection[position][owner] = elem;
        else if (PERSONAL_ASSET.equals(streamType))
            personalAssetCollection[position][owner] = elem;
        else if (DEEMED_ASSET.equals(streamType))
            deemedAssetCollection[position][owner] = elem;

    }

    public void set(String streamType, int position, int owner,
            double actualAsset, double testAsset, double actualIncome,
            double testIncome) {
        set(streamType, position, owner, new DSSElement("",
                ReferenceCode.CODE_NONE, owner, actualAsset, testAsset,
                actualIncome, testIncome));
    }

    public void setPersonInfo(PersonInfo value) {
        personInfo = value;
    }

    public void setPartnerInfo(PersonInfo value) {
        partnerInfo = value;
    }

    public double get(String streamType, int position, int owner, int field) {

        Object[][] itemCollection = new Object[collectionSize][ownerSize];

        if (INCOME.equals(streamType))
            itemCollection = incomeCollection;
        else if (NON_DEEMED_ASSET.equals(streamType))
            itemCollection = nonDeemedAssetCollection;
        else if (PERSONAL_ASSET.equals(streamType))
            itemCollection = personalAssetCollection;
        else if (DEEMED_ASSET.equals(streamType))
            itemCollection = deemedAssetCollection;

        DSSElement elem = (DSSElement) itemCollection[position][owner];
        return elem == null ? 0. : elem.getAmount(field);

    }

    public double getResult(int personType, String field) {

        if (MAX_BENEFIT.equals(field))
            return (CLIENT == personType) ? maxBenefit : maxBenefitPartner;
        if (ASSET_TEST_RESULT.equals(field))
            return (CLIENT == personType) ? assetTestResult
                    : assetTestResultPartner;
        if (INCOME_TEST_RESULT.equals(field))
            return (CLIENT == personType) ? incomeTestResult
                    : incomeTestResultPartner;
        if (BASIC_BENEFIT.equals(field))
            return (CLIENT == personType) ? basicBenefit : basicBenefitPartner;
        if (PHAR.equals(field))
            return (CLIENT == personType) ? phar : pharPartner;
        if (ASSET_THRESHOLD.equals(field))
            return (CLIENT == personType) ? assetThreshold
                    : assetThresholdPartner;
        if (INCOME_THRESHOLD.equals(field))
            return (CLIENT == personType) ? incomeThreshold
                    : incomeThresholdPartner;

        if (MAX_BENEFIT_ANN.equals(field))
            return ((CLIENT == personType) ? maxBenefit : maxBenefitPartner) * 26;
        if (ASSET_TEST_RESULT_ANN.equals(field))
            return ((CLIENT == personType) ? assetTestResult
                    : assetTestResultPartner) * 26;
        if (INCOME_TEST_RESULT_ANN.equals(field))
            return ((CLIENT == personType) ? incomeTestResult
                    : incomeTestResultPartner) * 26;
        if (BASIC_BENEFIT_ANN.equals(field))
            return ((CLIENT == personType) ? basicBenefit : basicBenefitPartner) * 26;
        if (PHAR_ANN.equals(field))
            return ((CLIENT == personType) ? phar : pharPartner) * 26;

        return 0.;

    }

    public void setResult(int personType, String field, double amount) {
        switch (personType) {
        case CLIENT:
            if (MAX_BENEFIT.equals(field))
                maxBenefit = amount;
            else if (ASSET_TEST_RESULT.equals(field))
                assetTestResult = amount;
            else if (INCOME_TEST_RESULT.equals(field))
                incomeTestResult = amount;
            else if (BASIC_BENEFIT.equals(field))
                basicBenefit = amount;
            else if (PHAR.equals(field))
                phar = amount;
            else if (ASSET_THRESHOLD.equals(field))
                assetThreshold = amount;
            else if (INCOME_THRESHOLD.equals(field))
                incomeThreshold = amount;
            break;
        case PARTNER:
            if (MAX_BENEFIT.equals(field))
                maxBenefitPartner = amount;
            else if (ASSET_TEST_RESULT.equals(field))
                assetTestResultPartner = amount;
            else if (INCOME_TEST_RESULT.equals(field))
                incomeTestResultPartner = amount;
            else if (BASIC_BENEFIT.equals(field))
                basicBenefitPartner = amount;
            else if (PHAR.equals(field))
                pharPartner = amount;
            else if (ASSET_THRESHOLD.equals(field))
                assetThresholdPartner = amount;
            else if (INCOME_THRESHOLD.equals(field))
                incomeThresholdPartner = amount;
            break;
        default:
            ;
        }

    }

    public void setNonDeemedAssets(Collection c, int owner) {

        if (c == null)
            return;
        Collection ap = new Vector();
        Collection ann = new Vector();
        Collection cAnn = new Vector();

        Iterator i = c.iterator();
        while (i.hasNext()) {
            DSSElement e = (DSSElement) i.next();
            if (FundType.rcPENSION.equals(e.get(1)))
                ap.add(e);
            else if (FundType.rcANNUITY_LONG.equals(e.get(1)))
                cAnn.add(e);
            else if (FundType.rcANNUITY.equals(e.get(1)))
                ann.add(e);
        }

        nonDeemedAssetCollection[AP][owner] = new DSSElement(NON_DEEMED_ASSET,
                FundType.rcPENSION, owner, sumCollection(ap, 3), sumCollection(
                        ap, 4), sumCollection(ap, 5), sumCollection(ap, 6));
        nonDeemedAssetCollection[ANN][owner] = new DSSElement(NON_DEEMED_ASSET,
                FundType.rcANNUITY, owner, sumCollection(ann, 3),
                sumCollection(ann, 4), sumCollection(ann, 5), sumCollection(
                        ann, 6));
        nonDeemedAssetCollection[COM_ANN][owner] = new DSSElement(
                NON_DEEMED_ASSET, FundType.rcANNUITY_LONG, owner,
                sumCollection(cAnn, 3), sumCollection(cAnn, 4), sumCollection(
                        cAnn, 5), sumCollection(cAnn, 6));

    }

    public void addCalc(String calcName, String value) {

        if (value == null || value.length() == 0)
            return;
        int index = -1;
        DSSCalculator dssCalc = null;
        if (AGE_PENSION.equals(calcName)) {
            index = 0;
            dssCalc = new PensionCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;
        } else if (NEW_START_ALLOWANCE.equals(calcName)) {
            index = 1;
            dssCalc = new AllowanceCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;

        } else if (DISABILITY_SUPPORT_PENSION.equals(calcName)) {
            index = 2;
            dssCalc = new PensionCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;
        } else if (WIDOW_ALLOWANCE.equals(calcName)) {
            index = 3;
            dssCalc = new AllowanceCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;
        } else if (SICKNESS_ALLOWANCE.equals(calcName)) {
            index = 4;
            dssCalc = new AllowanceCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;
        } else if (CARER_ALLOWANCE.equals(calcName)) {
            index = 5;
            dssCalc = new PensionCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;
        } else if (PARTNER_ALLOWANCE.equals(calcName)) {
            index = 6;
            dssCalc = new AllowanceCalculator(CLIENT);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                calcCollection[index] = dssCalc;
            } else
                calcCollection[index] = null;
        }

        if (AGE_PENSION_PARTNER.equals(calcName)) {
            index = 0;
            dssCalc = new PensionCalculator(PARTNER);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                partnerCalcCollection[index] = dssCalc;
            } else
                partnerCalcCollection[index] = null;
        } else if (NEW_START_ALLOWANCE_PARTNER.equals(calcName)) {
            index = 1;
            dssCalc = new AllowanceCalculator(PARTNER);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                partnerCalcCollection[index] = dssCalc;
            } else
                partnerCalcCollection[index] = null;

        } else if (DISABILITY_SUPPORT_PENSION_PARTNER.equals(calcName)) {
            index = 2;
            dssCalc = new PensionCalculator(PARTNER);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                partnerCalcCollection[index] = dssCalc;
            } else
                partnerCalcCollection[index] = null;

        } else if (SICKNESS_ALLOWANCE_PARTNER.equals(calcName)) {
            index = 3;
            dssCalc = new AllowanceCalculator(PARTNER);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                partnerCalcCollection[index] = dssCalc;
            } else
                partnerCalcCollection[index] = null;
        } else if (CARER_ALLOWANCE_PARTNER.equals(calcName)) {
            index = 4;
            dssCalc = new PensionCalculator(PARTNER);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                partnerCalcCollection[index] = dssCalc;
            } else
                partnerCalcCollection[index] = null;
        } else if (PARTNER_ALLOWANCE_PARTNER.equals(calcName)) {
            index = 5;
            dssCalc = new AllowanceCalculator(PARTNER);
            if ("Yes".equals(value)) {
                dssCalc.register(this);
                partnerCalcCollection[index] = dssCalc;
            } else
                partnerCalcCollection[index] = null;
        }

    }

    public void calculate() {
        if (personInfo == null)
            return;

        dssParams = new DSSParams(personInfo.isSingle(), personInfo
                .isHomeOwner(), personInfo.isIllSept(), personInfo.childrenNo);

        deemedIncome = dssParams.getDeemedIncome(sumCollection(DEEMED_ASSET,
                NonDeemedAssetTableModel.ACTUAL_ASSET, COMBINED), false);
        deemedIncomeC = dssParams.getDeemedIncome(sumCollection(DEEMED_ASSET,
                NonDeemedAssetTableModel.ACTUAL_ASSET, CLIENT), personInfo
                .isPartnered());
        deemedIncomeP = dssParams.getDeemedIncome(sumCollection(DEEMED_ASSET,
                NonDeemedAssetTableModel.ACTUAL_ASSET, PARTNER), true);

        totalActualAsset = getTotal(NonDeemedAssetTableModel.ACTUAL_ASSET,
                COMBINED);
        totalActualAssetC = getTotal(NonDeemedAssetTableModel.ACTUAL_ASSET,
                CLIENT);
        totalActualAssetP = getTotal(NonDeemedAssetTableModel.ACTUAL_ASSET,
                PARTNER);

        totalTestAsset = getTotal(NonDeemedAssetTableModel.TEST_ASSET, COMBINED);
        totalTestAssetC = getTotal(NonDeemedAssetTableModel.TEST_ASSET, CLIENT);

        totalActualIncome = getTotal(NonDeemedAssetTableModel.ACTUAL_INCOME,
                COMBINED);
        totalActualIncomeC = getTotal(NonDeemedAssetTableModel.ACTUAL_INCOME,
                CLIENT);
        totalActualIncomeP = getTotal(NonDeemedAssetTableModel.ACTUAL_INCOME,
                PARTNER);

        totalTestIncome = getTotal(NonDeemedAssetTableModel.TEST_INCOME,
                COMBINED)
                + deemedIncome;
        totalTestIncomeC = getTotal(NonDeemedAssetTableModel.TEST_INCOME,
                CLIENT)
                + deemedIncomeC;
        totalTestIncomeP = getTotal(NonDeemedAssetTableModel.TEST_INCOME,
                PARTNER)
                + deemedIncomeP;

        phar = pharPartner = dssParams.getPhar();

        boolean doCalc = false, doCalcPartner = false;
        for (int i = 0; i < calcCollection.length; i++) {
            if (calcCollection[i] == null)
                continue;
            doCalc = true;
            ((DSSCalculator) calcCollection[i]).calc();
        }
        for (int i = 0; i < partnerCalcCollection.length; i++) {
            if (partnerCalcCollection[i] == null)
                continue;
            doCalcPartner = true;
            ((DSSCalculator) partnerCalcCollection[i]).calc();
        }
        if (!doCalc) {
            phar = 0;
            maxBenefit = 0;
            assetTestResult = 0;
            incomeTestResult = 0;
            basicBenefit = 0;
        }
        if (!doCalcPartner) {
            pharPartner = 0;
            maxBenefitPartner = 0;
            assetTestResultPartner = 0;
            incomeTestResultPartner = 0;
            basicBenefitPartner = 0;
        }
    }

    public double getTotal(int field, int owner) {

        return sumCollection(DEEMED_ASSET, field, owner)
                + sumCollection(NON_DEEMED_ASSET, field, owner)
                + sumCollection(PERSONAL_ASSET, field, owner)
                + sumCollection(INCOME, field, owner);
    }

    public double getTotalDeemedIncome(int owner) {
        switch (owner) {
        case COMBINED:
            return deemedIncome;
        case CLIENT:
            return deemedIncomeC;
        case PARTNER:
            return deemedIncomeP;
        default:
            return 0.;
        }

    }

    public double getTotalActualAsset(int owner) {
        switch (owner) {
        case COMBINED:
            return totalActualAsset;
        case CLIENT:
            return totalActualAssetC;
        case PARTNER:
            return totalActualAssetP;
        default:
            return 0.;
        }
    }

    public double getTotalTestAsset(int owner) {
        switch (owner) {
        case COMBINED:
            return totalTestAsset;
        case CLIENT:
            return totalTestAssetC;
        default:
            return 0.;
        }
    }

    public double getTotalActualIncome(int owner) {
        switch (owner) {
        case COMBINED:
            return totalActualIncome;
        case CLIENT:
            return totalActualIncomeC;
        case PARTNER:
            return totalActualIncomeP;
        default:
            return 0.;
        }
    }

    public double getTotalTestIncome(int owner) {
        switch (owner) {
        case COMBINED:
            return totalTestIncome;
        case CLIENT:
            return totalTestIncomeC;
        case PARTNER:
            return totalTestIncomeP;
        default:
            return 0.;
        }
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public PersonInfo getPartnerInfo() {
        return partnerInfo;
    }

    public DSSParams getDSSParams() {
        return dssParams;
    }

    public boolean isEligible(String personType, String calcType)
            throws NotEligibleException {

        ec = new DSSEligibilityCheck(personInfo, partnerInfo);

        return ec.isEligible(personType, calcType);

    }

    public boolean calcIsEmpty() {
        for (int i = 0; i < calcCollection.length; i++) {
            if (calcCollection[i] != null)
                calcIsEmpty = false;
        }
        return calcIsEmpty;
    }

    public double sumCollection(String collectionName, int field, int owner) {

        if (NON_DEEMED_ASSET.equals(collectionName))
            return sumCollection(nonDeemedAssetCollection, field, owner);

        if (INCOME.equals(collectionName))
            return sumCollection(incomeCollection, field, owner);

        if (DEEMED_ASSET.equals(collectionName))
            return sumCollection(deemedAssetCollection, field, owner);

        if (PERSONAL_ASSET.equals(collectionName))
            return sumCollection(personalAssetCollection, field, owner);

        return 0;
    }

    public double sumCollection(Collection itemCollection, int field) {
        double sum = 0;
        java.util.Iterator iter = itemCollection.iterator();
        while (iter.hasNext())
            sum += ((DSSElement) iter.next()).getAmount(field);
        return sum;
    }

    public double sumCollection(Object[][] itemCollection, int field, int owner) {
        double sum = 0;
        for (int i = 0; i < itemCollection.length; i++) {
            sum = sum
                    + (((DSSElement) itemCollection[i][owner]) == null ? 0
                            : ((DSSElement) itemCollection[i][owner])
                                    .getAmount(field));
        }
        return sum;
    }

    public int getCalcModel() {
        return calcModel;
    }

    public void setCalcModel(int value) {
        calcModel = value;
    }

}
