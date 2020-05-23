package au.com.totemsoft.myplanner.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import au.com.totemsoft.myplanner.api.bean.IAddressCode;
import au.com.totemsoft.myplanner.api.bean.hibernate.AbstractCode;

@Entity
@Table(name = IAddressCode.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class AddressCode extends AbstractCode implements IAddressCode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 4710325054260470545L;

    @Id
    @Column(name = "AddressCodeID", nullable = false)
    @Type(type = "au.com.totemsoft.myplanner.domain.hibernate.IntegerType")
    private Integer id;

    //@Column(name = "AddressCode", nullable = false)
    //private String code;

    @Column(name = "AddressCodeDesc", nullable = false)
    private String description;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return description;
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}