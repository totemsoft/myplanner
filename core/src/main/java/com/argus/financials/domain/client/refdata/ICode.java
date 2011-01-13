package com.argus.financials.domain.client.refdata;

import com.argus.financials.domain.client.IBase;

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
    String getCode();

    /**
     * 
     * @return
     */
    String getDescription();

}