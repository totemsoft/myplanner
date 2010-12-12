/*
 * AssetAllocation.java
 *
 * Created on 26 September 2002, 10:44
 */

package com.argus.financials.assetallocation;

/**
 * 
 * @author shibaevv
 * @version
 */
public class AssetAllocation implements java.io.Serializable,
        java.lang.Cloneable {
    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath .
    // com.argus.financial.assetallocation.AssetAllocation

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = -4334040362429877270L; // local class

    // static final long serialVersionUID = 4492000584253160306L; // stream
    // classdesc

    public Integer assetAllocationID; // = new Integer( -1 );

    public Double amount; // = new Double( 0.0 );

    public Double inCash; // = new Double( 0.0 );

    public Double inFixedInterest; // = new Double( 0.0 );

    public Double inAustShares; // = new Double( 0.0 );

    public Double inIntnlShares; // = new Double( 0.0 );

    public Double inProperty; // = new Double( 0.0 );

    public Double inOther; // = new Double( 0.0 );

    public Boolean include; // = new Boolean( true );

    /** Creates new AssetAllocation */
    public AssetAllocation() {
    }

    // this method performs a "shallow copy" of this object, not a "deep copy"
    // operation.
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Integer getPrimaryKeyID() {
        return this.assetAllocationID;
    }

    public Integer getAssetAllocationID() {
        return this.assetAllocationID;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Double getInCash() {
        return this.inCash;
    }

    public Double getInFixedInterest() {
        return this.inFixedInterest;
    }

    public Double getInAustShares() {
        return this.inAustShares;
    }

    public Double getInIntnlShares() {
        return this.inIntnlShares;
    }

    public Double getInProperty() {
        return this.inProperty;
    }

    public Double getInOther() {
        return this.inOther;
    }

    public Boolean getInclude() {
        return this.include;
    }

    public void setPrimaryKeyID(Integer value) {
        this.assetAllocationID = value;
    }

    public void setAssetAllocationID(Integer value) {
        this.assetAllocationID = value;
    }

    public void setAmount(Double value) {
        this.amount = value;
    }

    public void setInCash(Double value) {
        this.inCash = value;
    }

    public void setInFixedInterest(Double value) {
        this.inFixedInterest = value;
    }

    public void setInAustShares(Double value) {
        this.inAustShares = value;
    }

    public void setInIntnlShares(Double value) {
        this.inIntnlShares = value;
    }

    public void setInProperty(Double value) {
        this.inProperty = value;
    }

    public void setInOther(Double value) {
        this.inOther = value;
    }

    public void setInclude(Boolean value) {
        this.include = value;
    }

}
