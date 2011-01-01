package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.MappedSuperclass;

import com.argus.financials.domain.hibernate.AbstractBase;
import com.argus.financials.domain.refdata.ICode;

/**
 * Base class for all codes.
 * @author vchibaev (Valeri SHIBAEV)
 */
@MappedSuperclass
public abstract class AbstractCode extends AbstractBase<Integer> implements ICode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 8470628483943356675L;

}