package com.argus.financials.api.bean;

/**
 *
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IBase<T>
{

    /** ID property name */
    String ID = "id";

    /** IGNORE property(s) */
    String[] IGNORE = new String[] {};

    /**
     * @return
     */
    T getId();

    boolean isModified();
    void setModified(boolean value);

}