package com.argus.financials.api.bean;

import java.util.Date;

/**
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface IClient extends IPerson
{

    Date getFeeDate();
    void setFeeDate(Date login);

    Date getReviewDate();
    void setReviewDate(Date password);

}