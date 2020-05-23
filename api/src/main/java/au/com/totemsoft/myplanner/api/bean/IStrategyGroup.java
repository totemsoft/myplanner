package au.com.totemsoft.myplanner.api.bean;

import java.util.Map;

public interface IStrategyGroup extends IFPSAssignableObject {

    String getStrategyGroupDesc();
    void setStrategyGroupDesc(String value);

    byte[] getSerializedStrategyGroupData() throws java.io.IOException;
    void setSerializedStrategyGroupData(byte[] value);

    Map getCollectionFinancials(boolean deepCopy);
    Map getRestructureFinancials(boolean deepCopy);

}