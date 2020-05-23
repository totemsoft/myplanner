package au.com.totemsoft.myplanner.api.bean;

import java.util.Date;

public interface IFPSObject extends IBase<Integer> {

    @Override
    Integer getId();
    void setId(Integer value);

    Integer getOwnerId();
    void setOwnerId(Integer value);

    Date getDateCreated();
    void setDateCreated(Date value);

    Date getDateModified();
    void setDateModified(Date value);

}