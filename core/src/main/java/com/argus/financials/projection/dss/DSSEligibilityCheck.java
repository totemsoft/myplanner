/*
 * DSSEligibilityCheck.java
 *
 * Created on May 2, 2003, 11:11 AM
 */

package com.argus.financials.projection.dss;

import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.projection.DSSCalc;
import com.argus.financials.projection.DocumentNames;

/**
 * 
 * @author shibaevv
 */
public class DSSEligibilityCheck implements DocumentNames {

    private PersonInfo personInfo, partnerInfo;

    /** Creates a new instance of DSSEligibilityCheck */
    public DSSEligibilityCheck(PersonInfo personInfo, PersonInfo partnerInfo) {
        this.personInfo = personInfo;
        this.partnerInfo = partnerInfo;

    }

    public boolean isEligible(String personType, String calcType)
            throws NotEligibleException {

        if (USER_INFO_PAGE.equals(calcType)) {
            if (personInfo.dob == null)
                throw new NotEligibleException(" DOB is required.");

            if (personInfo.sex.length() == 0)
                throw new NotEligibleException(" Sex is required.");

            if (personInfo.calcDate == null)
                throw new NotEligibleException(" Calculation Date is required.");

            if (personInfo.marital.length() == 0)
                throw new NotEligibleException(" Marital Status is required.");

            if (personInfo.childrenNo == -1)
                throw new NotEligibleException(" No. of Children is required.");

            if (partnerInfo != null) {
                if (partnerInfo.dob == null)
                    throw new NotEligibleException(" DOB is required.");

                if (partnerInfo.sex.length() == 0)
                    throw new NotEligibleException(" Sex is required.");

                if (partnerInfo.calcDate == null)
                    throw new NotEligibleException(
                            " Calculation Date is required.");
            }

        }

        if (CLIENT_NAME.equals(personType)) {
            if (AGE_PENSION.equals(calcType)) {
                if (getYearsToAgePension(personInfo) > 0)
                    throw new NotEligibleException(CLIENT_AGE_PENSION_EXCEPTION);
            } else if (NEW_START_ALLOWANCE.equals(calcType)) {
                if (getYearsToAgePension(personInfo) <= 0
                        || personInfo.age() < 21)
                    throw new NotEligibleException(CLIENT_NSA_EXCEPTION);
            } else if (DISABILITY_SUPPORT_PENSION.equals(calcType)) {
                if (getYearsToAgePension(personInfo) <= 0
                        || personInfo.age() < 21)
                    throw new NotEligibleException(CLIENT_DSP_EXCEPTION);
            } else if (WIDOW_ALLOWANCE.equals(calcType)) {
                if (personInfo.age() < 50
                        || getYearsToAgePension(personInfo) <= 0
                        || !new SexCode().getCodeDescription(SexCode.FEMALE)
                                .equals(personInfo.sex)
                        || (!new MaritalCode().getCodeDescription(
                                IMaritalCode.WIDOWED).equals(personInfo.marital)
                                && !new MaritalCode().getCodeDescription(
                                        IMaritalCode.SEPARATED).equals(
                                        personInfo.marital) && !new MaritalCode()
                                .getCodeDescription(IMaritalCode.DIVORCED)
                                .equals(personInfo.marital)))
                    throw new NotEligibleException(CLIENT_WA_EXCEPTION);
            } else if (SICKNESS_ALLOWANCE.equals(calcType)) {
                if (getYearsToAgePension(personInfo) <= 0
                        || personInfo.age() < 21)
                    throw new NotEligibleException(CLIENT_SA_EXCEPTION);
            } else if (PARTNER_ALLOWANCE.equals(calcType)) {
                if (personInfo == null || personInfo.childrenNo > 0
                        || personInfo.isSingle())
                    throw new NotEligibleException(CLIENT_PA_EXCEPTION);

                java.util.Calendar dOfB = java.util.Calendar.getInstance();
                dOfB.setTime(personInfo.dob);
                if (getYearsToAgePension(personInfo) <= 0
                        || dOfB.after(new java.util.GregorianCalendar(1955, 7,
                                1)))
                    throw new NotEligibleException(CLIENT_PA_EXCEPTION);

            }

        }

        if (NAME_PARTNER.equals(personType)) {
            if (AGE_PENSION.equals(calcType)) {
                if (getYearsToAgePension(partnerInfo) > 0)
                    throw new NotEligibleException(
                            PARTNER_AGE_PENSION_EXCEPTION);
            } else if (NEW_START_ALLOWANCE.equals(calcType)) {
                if (partnerInfo == null
                        || getYearsToAgePension(partnerInfo) <= 0
                        || partnerInfo.age() < 21)
                    throw new NotEligibleException(PARTNER_NSA_EXCEPTION);
            } else if (DISABILITY_SUPPORT_PENSION.equals(calcType)) {
                if (partnerInfo == null
                        || getYearsToAgePension(partnerInfo) <= 0
                        || partnerInfo.age() < 21)
                    throw new NotEligibleException(PARTNER_DSP_EXCEPTION);
            } else if (SICKNESS_ALLOWANCE.equals(calcType)) {
                if (partnerInfo == null
                        || getYearsToAgePension(partnerInfo) <= 0
                        || partnerInfo.age() < 21)
                    throw new NotEligibleException(PARTNER_SA_EXCEPTION);
            } else if (PARTNER_ALLOWANCE.equals(calcType)) {
                if (partnerInfo == null || partnerInfo.childrenNo > 0
                        || partnerInfo.isSingle())
                    throw new NotEligibleException(PARTNER_PA_EXCEPTION);

                java.util.Calendar dateOfBirth = java.util.Calendar
                        .getInstance();
                dateOfBirth.setTime(partnerInfo.dob);
                if (getYearsToAgePension(partnerInfo) <= 0
                        || dateOfBirth.after(new java.util.GregorianCalendar(
                                1955, 7, 1)))
                    throw new NotEligibleException(PARTNER_PA_EXCEPTION);
            }

        }

        return true;
    }

    public int getPensionQualifyingYear() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int years = calendar.get(java.util.Calendar.YEAR);

        return (int) Math.ceil(DSSCalc.getPensionQualifyingAge(personInfo.dob,
                ("Female".equals(personInfo.sex) ? SexCode.FEMALE
                        : SexCode.MALE))
                - personInfo.age() + years);
    }

    public double getYearsToAgePension(PersonInfo info) {
        if (info == null || info.dob == null || info.sex == null)
            return 100;
        return (DSSCalc.getPensionQualifyingAge(info.dob, ("Female"
                .equals(info.sex) ? SexCode.FEMALE : SexCode.MALE)) - info
                .age());
    }

}
