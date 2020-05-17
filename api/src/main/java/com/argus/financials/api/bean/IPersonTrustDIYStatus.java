package com.argus.financials.api.bean;

public interface IPersonTrustDIYStatus extends IFPSObject {

    Integer getTrustStatusCodeId();
    void setTrustStatusCodeId(Integer value);

    Integer getDIYStatusCodeId();
    void setDIYStatusCodeId(Integer value);

    Integer getCompanyStatusCodeId();
    void setCompanyStatusCodeId(Integer value);

    String getComment();
    void setComment(String value);

}