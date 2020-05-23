package au.com.totemsoft.myplanner.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import au.com.totemsoft.myplanner.api.bean.ITitleCode;
import au.com.totemsoft.myplanner.api.bean.hibernate.AbstractCode;

@Entity
@Table(name = ITitleCode.TABLE_NAME)
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class TitleCode extends AbstractCode implements ITitleCode
{

    /** serialVersionUID */
    private static final long serialVersionUID = 6087201382516353070L;

    @Id
    @Column(name = "TitleCodeID", nullable = false)
    @Type(type = "au.com.totemsoft.myplanner.domain.hibernate.IntegerType")
    private Integer id;

    //@Column(name = "TitleCode", nullable = false)
    //private String code;

    @Column(name = "TitleCodeDesc", nullable = false)
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
        return getDescription();
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    public static ITitleCode findTitleCode(Integer id)
    {
        return entityDao.findTitleCode(id);
    }

}