package com.argus.financials.domain.refdata;

import com.argus.financials.domain.IBase;

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