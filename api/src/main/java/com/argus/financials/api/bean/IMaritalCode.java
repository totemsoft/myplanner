package com.argus.financials.api.bean;

/**
 * 
 * @author valeri chibaev
 */
public interface IMaritalCode extends ICode
{

    String TABLE_NAME = "MaritalCode";

    Integer SINGLE = 1;

    Integer DEFACTO = 2;

    Integer MARRIED = 3;

    Integer SEPARATED = 4;

    Integer DIVORCED = 5;

    Integer WIDOWED = 6;

    Integer SEPARATED_HEALTH = 7;

    Integer PARTNERED = 8;

    static boolean isMarried(Integer maritalCodeId) {
        return MARRIED.equals(maritalCodeId)
            || DEFACTO.equals(maritalCodeId)
            || PARTNERED.equals(maritalCodeId)
            || SEPARATED_HEALTH.equals(maritalCodeId);
    }

    static boolean isSingle(Integer maritalCodeId) {
        return maritalCodeId != null
            && !DEFACTO.equals(maritalCodeId)
            && !MARRIED.equals(maritalCodeId)
            && !PARTNERED.equals(maritalCodeId);
    }

}