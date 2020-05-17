package com.argus.financials.api.bean;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IUser extends IPerson
{

    Integer getTypeId();
    void setTypeId(Integer typeId);

    String getLogin();
    void setLogin(String login);

    String getPassword();
    void setPassword(String password);

}