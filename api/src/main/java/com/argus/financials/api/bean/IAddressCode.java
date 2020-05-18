package com.argus.financials.api.bean;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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