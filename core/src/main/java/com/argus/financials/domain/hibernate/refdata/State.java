package com.argus.financials.domain.hibernate.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.argus.financials.domain.refdata.IState;

@Entity
@Table(name = IState.TABLE_NAME)
public class State extends AbstractCode implements IState
{

    /** serialVersionUID */
    private static final long serialVersionUID = 211575394547398989L;

    @Id
    @Column(name = "StateCodeID", nullable = false)
    private Integer id;

    @Column(name = "StateCode", nullable = false)
    private String code;

    @Column(name = "StateCodeDesc", nullable = false)
    private String description;

    /* (non-Javadoc)
     * @see com.argus.financials.domain.IBase#getId()
     */
    public Integer getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.refdata.ICode#getCode()
     */
    public String getCode()
    {
        return code;
    }

    /* (non-Javadoc)
     * @see com.argus.financials.domain.refdata.ICode#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

}