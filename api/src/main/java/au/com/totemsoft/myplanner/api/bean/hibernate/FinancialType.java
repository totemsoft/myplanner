package au.com.totemsoft.myplanner.api.bean.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import au.com.totemsoft.myplanner.api.code.FinancialTypeID;

/**
 * @see FinancialTypeID
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Entity
@Table(name = "FinancialType")
@Cache(region = "FinancialType", usage = CacheConcurrencyStrategy.READ_ONLY)
@NamedQueries({
    @NamedQuery(name = "financialType.findByObjectType", query = "FROM FinancialType WHERE objectTypeId = :objectTypeId")
})
public class FinancialType extends AbstractCode {

    /** serialVersionUID */
    private static final long serialVersionUID = -1428172935291758279L;

    public static final FinancialType[] EMPTY = new FinancialType[0];

    @Id
    @Column(name = "FinancialTypeID", nullable = false)
    private Integer id;

    @Transient
    //@Column(name = "FinancialType", nullable = false)
    private String code;

    @Column(name = "FinancialTypeDesc", nullable = false)
    private String description;

    @Column(name = "ObjectTypeID", nullable = false)
    private Integer objectTypeId;

    public FinancialType() {
        super();
    }

    public FinancialType(Integer id) {
        super();
        this.id = id;
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

    /**
     * @return the code
     */
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

    public Integer getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

}