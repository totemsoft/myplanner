/*
 * StrategyGroup.java
 *
 * Created on 17 December 2002, 17:33
 */

package com.argus.financials.strategy;

import java.util.Map;

import com.argus.financials.api.bean.IStrategyGroup;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import com.argus.financials.bean.Financials;
import com.argus.financials.etc.FPSAssignableObject;
import com.argus.financials.strategy.model.StrategyGroupData;
import com.argus.util.DateTimeUtils;

public class StrategyGroup extends FPSAssignableObject implements IStrategyGroup {

    static final long serialVersionUID = 1397703350987660141L;

    public static final StrategyGroup DEFAULT_ROOT = new StrategyGroup();

    public static final StrategyGroup NONE = new StrategyGroup();
    static {
        DEFAULT_ROOT.setStrategyGroupDesc("--- New Strategy ---");
        NONE.setStrategyGroupDesc("None");
    }

    private String desc;

    private StrategyGroupData data;

    private byte[] sData;

    /** Creates a new instance of StrategyGroup */
    public StrategyGroup() {
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !(obj instanceof StrategyGroup))
            return false;

        return equals(desc, ((StrategyGroup) obj).desc);
    }

    public String toString() {
        return getStrategyGroupDesc()
                + (getStatusDesc() == null ? "" : " (" + getStatusDesc() + ")")
                + (getDateCreated() == null ? "" : ", created "
                        + DateTimeUtils.asString(getDateCreated()))
                + (getDateModified() == null ? "" : ", modified "
                        + DateTimeUtils.asString(getDateModified()));
    }

    public String getStatusDesc() {
        try {
            getStrategyGroupData();
        } catch (java.io.IOException e) {
            return null;
        }

        return data == null ? null : data.getStatusDesc();
    }

    public String getStrategyGroupDesc() {
        return desc;
    }

    public void setStrategyGroupDesc(String value) {
        desc = value;
    }

    public StrategyGroupData getStrategyGroupData() throws java.io.IOException {

        if (data == null) {

            if (sData == null)
                return null;

            java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream(
                    sData);
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(is);

            try {
                data = (StrategyGroupData) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace(System.err);
                data = null;

                throw new java.io.IOException(e.getMessage());

            } catch (java.io.InvalidClassException e) {
                // some of the serialized classes changed
                // put to error log
                System.err
                        .println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.err.println("!!!\t" + e.getMessage());
                System.err
                        .println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                // and create new Strategy
                data = null;

                throw new java.io.IOException(e.getMessage());

            } finally {
                in.close();
                is.close();
            }

        }

        return data;

    }

    public void setStrategyGroupData(StrategyGroupData value) {
        data = value;
        sData = null;
    }

    public byte[] getSerializedStrategyGroupData() throws java.io.IOException {

        if (sData == null) {

            if (data == null)
                return null;

            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(os);

            try {
                out.writeObject(data);
                sData = os.toByteArray();
            } finally {
                out.close();
                os.close();
            }

        }

        return sData;

    }

    public void setSerializedStrategyGroupData(byte[] value) {
        sData = value;
        data = null;
    }

    public Map getCollectionFinancials(boolean deepCopy) {

        try {
            Map financials = getStrategyGroupData()
                    .getCollectionModel().getFinancials();
            if (deepCopy)
                financials = Financials.deepCopy(financials, true);

            return financials;

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    public Map getRestructureFinancials(boolean deepCopy) {

        try {
            Map financials = getStrategyGroupData().getRestructureModel().getFinancials();
            if (deepCopy)
                financials = Financials.deepCopy(financials, true);
            // System.out.println(financials);

            return financials;

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

}
