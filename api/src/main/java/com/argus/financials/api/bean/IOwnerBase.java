package com.argus.financials.api.bean;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface IOwnerBase<T> extends IBase<T>
{

    /**
     * @return
     */
    T getOwnerId();

}