package com.argus.financials.api.bean;

public interface IPersonHealth extends IFPSObject {

    boolean isSmoker();
    void setSmoker(boolean value);

    boolean isHospitalCover();
    void setHospitalCover(boolean value);

    Integer getHealthStateCodeId();
    void setHealthStateCodeId(Integer value);

}