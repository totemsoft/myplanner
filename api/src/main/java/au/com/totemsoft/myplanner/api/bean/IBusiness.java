/*
 * BusinessService.java
 *
 * Created on 22 January 2002, 11:59
 */

package au.com.totemsoft.myplanner.api.bean;

public interface IBusiness extends IBase<Integer> {

    void setId(Integer value);

    Integer getParentId();
    void setParentId(Integer value);

    Integer getIndustryCodeID();
    void setIndustryCodeID(Integer value);

    Integer getBusinessStructureCodeID();
    void setBusinessStructureCodeID(Integer value);

    String getLegalName();
    void setLegalName(String value);

    String getTradingName();
    void setTradingName(String value);

    String getBusinessNumber();
    void setBusinessNumber(String value);

    String getTaxFileNumber();
    void setTaxFileNumber(String value);

    String getWebSiteName();
    void setWebSiteName(String value);

}