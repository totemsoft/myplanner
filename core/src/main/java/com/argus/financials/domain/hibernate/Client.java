package com.argus.financials.domain.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.springframework.stereotype.Component;

import com.argus.financials.api.bean.IClient;

@Component
@Entity
@Table(name = "ClientPerson")
@PrimaryKeyJoinColumn(name = "ClientPersonID")
@Where(clause = "Active = 'Y'")
public class Client extends Person implements IClient
{

    /** serialVersionUID */
    private static final long serialVersionUID = 1209327387101767887L;

    @Column(name = "FeeDate")
    private Date feeDate;

    @Column(name = "ReviewDate")
    private Date reviewDate;

//  @Column(name = "Active")
//  @Type(type = "yes_no")
//  private boolean active;

    /**
     * @return the feeDate
     */
    public Date getFeeDate()
    {
        return feeDate;
    }

    /**
     * @param feeDate the feeDate to set
     */
    public void setFeeDate(Date login)
    {
        this.feeDate = login;
    }

    /**
     * @return the reviewDate
     */
    public Date getReviewDate()
    {
        return reviewDate;
    }

    /**
     * @param reviewDate the reviewDate to set
     */
    public void setReviewDate(Date password)
    {
        this.reviewDate = password;
    }

}