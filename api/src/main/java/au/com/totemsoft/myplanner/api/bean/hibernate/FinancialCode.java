package au.com.totemsoft.myplanner.api.bean.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import au.com.totemsoft.myplanner.api.bean.ICode;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Entity
@Table(name = "FinancialCode")
@Cache(region = "FinancialCode", usage = CacheConcurrencyStrategy.READ_ONLY)
public class FinancialCode extends AbstractCode {

    public static final String TABLE_NAME  = "FinancialCode";
    public static final String COLUMN_ID = "FinancialCodeID";
    public static final String COLUMN_CODE = "FinancialCode";
    public static final String COLUMN_DESC = "FinancialCodeDesc";

    /** serialVersionUID */
    private static final long serialVersionUID = 1989378592754186527L;

    @Id
    @Column(name = "FinancialCodeID", nullable = false)
    private Integer id;

    @Column(name = "FinancialCode", nullable = true)
    private String code;

    @Column(name = "FinancialCodeDesc", nullable = false)
    private String description;

    @Column(name = "FinancialTypeID")
    private Integer financialTypeId;

    @ManyToOne // (fetch = FetchType.LAZY)
    @JoinColumn(name = "FinancialTypeID", insertable = false, updatable = false)
    private FinancialType financialType;

    public FinancialCode() {
        super();
    }

    public FinancialCode(Integer id) {
        this.id = id;
    }

    public FinancialCode(Integer id, String code, String description, Integer financialTypeId) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.financialTypeId = financialTypeId;
    }

    public FinancialCode(ICode code, Integer financialTypeId) {
        this(code.getId(), code.getCode(), code.getDescription(), financialTypeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.com.totemsoft.myplanner.domain.IBase#getId()
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFinancialTypeId() {
        return financialTypeId;
    }

    public void setFinancialTypeId(Integer financialTypeId) {
        this.financialTypeId = financialTypeId;
    }

    public FinancialType getFinancialType() {
        return financialType;
    }

    public void setFinancialType(FinancialType financialType) {
        this.financialType = financialType;
    }

}