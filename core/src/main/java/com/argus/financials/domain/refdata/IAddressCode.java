package com.argus.financials.domain.refdata;

/**
 * 
 * @author valeri chibaev
 */
public interface IAddressCode extends ICode
{

    String TABLE_NAME = "AddressCode";

    public enum CODE
    {
        NONE,
        RESIDENTIAL,
        POSTAL,
        EMPLOYER,
        WILL_EXECUTOR
    }

}