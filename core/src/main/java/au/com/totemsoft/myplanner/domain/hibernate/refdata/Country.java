package au.com.totemsoft.myplanner.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import au.com.totemsoft.myplanner.api.bean.ICountry;
import au.com.totemsoft.myplanner.api.bean.hibernate.AbstractCode;

@Entity
@Table(name = ICountry.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country extends AbstractCode implements ICountry
{
    /** serialVersionUID */
    private static final long serialVersionUID = -5771582515464015957L;

    @Id
    @Column(name = "CountryCodeID", nullable = false)
    @Type(type = "au.com.totemsoft.myplanner.domain.hibernate.IntegerType")
    private Integer id;

    @Column(name = "CountryCode", nullable = false)
    private String code;

    @Column(name = "CountryCodeDesc", nullable = false)
    private String description;

    public Country() {
        super();
    }

    public Country(Integer id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}