package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.argus.financials.api.bean.ILanguage;
import com.argus.financials.api.bean.hibernate.AbstractCode;

@Entity
@Table(name = "LanguageCode")
@Cache(region = "refdata", usage = CacheConcurrencyStrategy.READ_ONLY)
public class Language extends AbstractCode implements ILanguage
{

    /** serialVersionUID */
    private static final long serialVersionUID = -3638009301024471641L;

    @Id
    @Column(name = "LanguageCodeID", nullable = false)
    @Type(type = "com.argus.financials.domain.hibernate.IntegerType")
    private Integer id;

    @Column(name = "LanguageCode", nullable = false)
    private String code;

    @Column(name = "LanguageCodeDesc", nullable = false)
    private String description;

    public Language() {
        super();
    }

    public Language(Integer id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return code;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.client.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}