package au.com.totemsoft.myplanner.api.bean;

public interface IOccupation extends IFPSObject {

    String getJobDescription();
    void setJobDescription(String value);

    Integer getEmploymentStatusCodeId();
    void setEmploymentStatusCodeId(Integer value);
    String getEmploymentStatus();

    Integer getIndustryCodeId();
    void setIndustryCodeId(Integer value);
    String getIndustry();

    Integer getOccupationCodeId();
    void setOccupationCodeId(Integer value);
    String getOccupation();

}