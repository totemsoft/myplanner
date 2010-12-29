package com.argus.financials.domain;

/**
 *
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IOwnerBase<T> extends IBase<T>
{

    /**
     * @return
     */
    T getOwnerId();

}