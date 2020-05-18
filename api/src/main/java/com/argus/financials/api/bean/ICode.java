package com.argus.financials.api.bean;

/**
 *
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface ICode extends IBase<Integer>
{

    /**
     * 
     * @return
     */
    Integer getId();

    /**
     * 
     * @return
     */
    String getCode();

    /**
     * 
     * @return
     */
    String getDescription();

}