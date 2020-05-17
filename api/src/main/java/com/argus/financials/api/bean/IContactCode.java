package com.argus.financials.api.bean;

/**
 * 
 * @author valeri chibaev
 */
public interface IContactCode extends ICode
{

    String TABLE_NAME = "ContactMediaCode";

    public enum CODE
    {
        NONE,
        PHONE,
        PHONE_WORK,
        FAX,
        FAX_WORK,
        MOBILE,
        MOBILE_WORK,
        EMAIL,
        EMAIL_WORK
    }

}