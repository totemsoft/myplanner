package com.argus.financials.api.bean;

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