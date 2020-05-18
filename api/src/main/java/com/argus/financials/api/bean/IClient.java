package com.argus.financials.api.bean;

import java.util.Date;

/**
 * @author vchibaev (Valeri SHIBAEV)
 */
public interface IClient extends IPerson
{

    Date getFeeDate();
    void setFeeDate(Date login);

    Date getReviewDate();
    void setReviewDate(Date password);

}