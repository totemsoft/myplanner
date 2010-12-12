/*
 * AssetGrowth2.java
 *
 * Created on 8 February 2002, 13:34
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.projection.save.Model;

public class AssetGrowthLinked extends AssetGrowth {

    private AssetGrowthLinked nextAsset;

    /**
     * Creates new AssetGrowth2
     */
    public AssetGrowthLinked() {
        this(null);
    }

    public AssetGrowthLinked(Integer assetCodeID) {
        super(assetCodeID);
        _clear();
    }

    /**
     * 
     */
    public void assign(AssetGrowth ag) {
        super.assign(ag);

        if (ag instanceof AssetGrowthLinked) {
            AssetGrowthLinked next = ((AssetGrowthLinked) ag).nextAsset;

            while (next != null) {
                setNextAsset(next);
                next = next.getNextAsset();
            }
        }

        // do not copy data if not the same class
        if (!this.getClass().equals(ag.getClass()))
            return;

        modified = true;// ag.modified;
    }

    public void clear() {
        super.clear();
        _clear();
    }

    private void _clear() {
        if (nextAsset != null)
            nextAsset.clear();
    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void addChangeListener(javax.swing.event.ChangeListener listener) {

        super.addChangeListener(listener);

        if (nextAsset != null)
            nextAsset.addChangeListener(listener);

    }

    /***************************************************************************
     * 
     */
    public void enableUpdate() {
        super.enableUpdate();
        if (nextAsset != null)
            nextAsset.enableUpdate();
    }

    public void disableUpdate() {
        super.disableUpdate();
        if (nextAsset != null)
            nextAsset.disableUpdate();
    }

    public void doUpdate() {
        super.doUpdate();
        if (nextAsset != null)
            nextAsset.doUpdate();
    }

    public void setModel(Model value) {
        super.setModel(value);
        if (nextAsset != null)
            nextAsset.setModel(value);
    }

    public void setTargetAge(int value) {
        super.setTargetAge(value);
        if (nextAsset != null)
            nextAsset.setTargetAge(value);
    }

    /***************************************************************************
     * javax.swing.text.Document
     */
    public boolean update(javax.swing.text.Document doc) {

        if (!check(doc))
            return false;

        Object group = doc.getProperty(GROUP_NAME);
        if (group == null)
            return super.update(doc);

        if (group.equals(getAssetCodeDesc())) {

            Object property = doc.getProperty(NAME);
            if (property == null)
                return false;

            String value = null;
            try {
                value = getValue(doc);
                return super.update(property, value);
            } catch (javax.swing.text.BadLocationException e) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                e.printStackTrace(System.err);
                return false;
            }

        }

        return nextAsset == null ? false : nextAsset.update(doc);

    }

    protected boolean update(Object property, String value) {

        int index = ((String) property).indexOf(DELIM);
        if (index > 0) {

            String group = ((String) property).substring(0, index);
            if (group == null)
                return super.update(property, value);

            if (group.equals(getAssetCodeDesc())) {

                property = ((String) property).substring(index + 1);
                if (property == null)
                    return false;

                return super.update(property, value);

            }

            return nextAsset == null ? false : nextAsset
                    .update(property, value);

        } else {
            return super.update(property, value);
        }

    }

    /**
     * 
     */
    public boolean isModified() {
        boolean b = super.isModified();
        if (nextAsset != null)
            b = b || nextAsset.isModified();
        return b;
    }

    public boolean isReady() {
        boolean b = super.isReady();
        if (nextAsset != null)
            b = b || nextAsset.isReady();
        return b;
    }

    public boolean isModifiedReady() {
        boolean b = super.isModified() && super.isReady();
        // if at least one asset is modified and ready
        if (nextAsset != null)
            b = b || (nextAsset.isModified() && nextAsset.isReady());
        return b;
    }

    public boolean getSaved() {
        return super.getSaved() || nextAsset == null ? false : nextAsset
                .getSaved();
    }

    public void setSaved() {
        super.setSaved();
        if (nextAsset != null)
            nextAsset.setSaved();
    }

    /**
     * 
     */
    public AssetGrowthLinked getNextAsset() {
        return nextAsset;
    }

    public void setNextAsset(AssetGrowthLinked ag) {
        if (nextAsset == null)
            nextAsset = ag;
        else
            nextAsset.setNextAsset(ag);
    }

    public AssetGrowthLinked getAsset(Integer assetCode) {
        if (assetCode.equals(getAssetCode()))
            return this;
        return nextAsset == null ? null : nextAsset.getAsset(assetCode);
    }

    /**
     * 
     */
    public void setAssetCode(Integer value) {
        super.setAssetCode(value);
    }

    public int getYearsInt() {
        int years = super.getYearsInt();
        return years >= 0 ? years : 0;
    }

    public void setYears(double value) {
        super.setYears(value);
        if (nextAsset != null)
            nextAsset.setYears(value);
    }

    public void setDateOfBirth(java.util.Date value) {
        super.setDateOfBirth(value);
        if (nextAsset != null)
            nextAsset.setDateOfBirth(value);
    }

    public boolean getSurcharge() {
        if (ASSET_SUPERANNUATION.equals(getAssetCode()))
            return super.getSurcharge();
        return nextAsset == null ? false : nextAsset.getSurcharge();
    }

    public void setSurcharge(boolean value) {
        super.setSurcharge(value);
        if (nextAsset != null)
            nextAsset.setSurcharge(value);
    }

    public void setIndexed(boolean value) {
        super.setIndexed(value);
        if (nextAsset != null)
            nextAsset.setIndexed(value);
    }

    public void setIndexRate(double value) {
        super.setIndexRate(value);
        if (nextAsset != null)
            nextAsset.setIndexRate(value);
    }

    public void setInvestmentStrategyCodeID(Integer value) {
        super.setInvestmentStrategyCodeID(value);
        if (nextAsset != null)
            nextAsset.setInvestmentStrategyCodeID(value);
    }

    public void setTaxRate(double value) {
        super.setTaxRate(value);
        if (nextAsset != null)
            nextAsset.setTaxRate(value);
    }

    public void setEntryFeeRate(double value) {
        super.setEntryFeeRate(value);
        if (nextAsset != null)
            nextAsset.setEntryFeeRate(value);
    }

    public void setRevisionFeeRate(double value) {
        super.setRevisionFeeRate(value);
        if (nextAsset != null)
            nextAsset.setRevisionFeeRate(value);
    }

    /**
     * 
     */
    public double getTargetValue(Integer assetCodeID) {
        AssetGrowthLinked ag = getAsset(assetCodeID);
        return ag == null ? 0 : ag.getTargetValue();
    }

    public double[] getTargetValues(Integer assetCodeID) {
        AssetGrowthLinked ag = getAsset(assetCodeID);
        return ag == null ? null : ag.getTargetValues();
    }

    public double getTotalTargetValue() {
        double value = 0;
        double[] values = getTotalTargetValues();

        // lets get last positive value
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0 && values[i] != MoneyCalc.HOLE) {
                value = values[i];
                continue;
            } else
                break;
        }

        return value;
    }

    public double[] getTotalTargetValues() {

        int years = getYearsInt(); // can be 0

        double[] values = new double[years];

        double value = 0;
        AssetGrowthLinked ag = this;
        while (ag != null) {
            value += ag.getTargetValue();
            sum(ag, values);
            ag = ag.getNextAsset();
        }

        if (values == null || values.length == 0)
            values = new double[] { value };

        return values;

    }

}
