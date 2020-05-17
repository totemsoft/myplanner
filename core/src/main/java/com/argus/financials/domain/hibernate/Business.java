package com.argus.financials.domain.hibernate;

import com.argus.financials.api.bean.IBusiness;

//@Entity
//@Table(name = "Business")
//@Inheritance(strategy = InheritanceType.JOINED)
public class Business extends AbstractAuditable<Integer> implements IBusiness {

    /** serialVersionUID */
    private static final long serialVersionUID = -3650472831210406670L;

    private Integer id;

    private Integer parentId;

    private Integer industryCodeID;

    private Integer businessStructureCodeID;

    private String legalName;

    private String tradingName;

    private String businessNumber;

    private String taxFileNumber;

    private String webSiteName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIndustryCodeID() {
        return industryCodeID;
    }

    public void setIndustryCodeID(Integer industryCodeID) {
        this.industryCodeID = industryCodeID;
    }

    public Integer getBusinessStructureCodeID() {
        return businessStructureCodeID;
    }

    public void setBusinessStructureCodeID(Integer businessStructureCodeID) {
        this.businessStructureCodeID = businessStructureCodeID;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getTaxFileNumber() {
        return taxFileNumber;
    }

    public void setTaxFileNumber(String taxFileNumber) {
        this.taxFileNumber = taxFileNumber;
    }

    public String getWebSiteName() {
        return webSiteName;
    }

    public void setWebSiteName(String webSiteName) {
        this.webSiteName = webSiteName;
    }

}