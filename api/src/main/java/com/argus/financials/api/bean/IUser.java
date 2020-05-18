package com.argus.financials.api.bean;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
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