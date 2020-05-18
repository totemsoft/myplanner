package com.argus.financials.api.bean;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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