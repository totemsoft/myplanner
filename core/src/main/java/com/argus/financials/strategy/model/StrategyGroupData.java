/*
 * StrategyGroupData.java
 *
 * Created on 17 December 2002, 16:20
 */

package com.argus.financials.strategy.model;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.bean.Assumptions;

public class StrategyGroupData implements java.io.Serializable {

    static final long serialVersionUID = 439878443864103696L;

    private DataCollectionModel collectionModel;

    private DataRestructureModel restructureModel;

    private Assumptions cashFlow;

    private Assumptions wealth;

    /** Creates a new instance of StrategyGroupData */
    public StrategyGroupData() {
    }

    public String getStatusDesc() {
        return getRestructureModel().getStatusDesc();
    }

    public DataCollectionModel getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(DataCollectionModel model) {
        collectionModel = model;
    }

    public DataRestructureModel getRestructureModel() {
        return restructureModel;
    }

    public void setRestructureModel(DataRestructureModel model) {
        restructureModel = model;
    }

    public Assumptions getCashFlowAssumptions() {
        return cashFlow;
    }

    public void setCashFlowAssumptions(Assumptions value) {
        cashFlow = value;
    }

    public Assumptions getWealthAssumptions() {
        return wealth;
    }

    public void setWealthAssumptions(Assumptions value) {
        wealth = value;
    }

}
