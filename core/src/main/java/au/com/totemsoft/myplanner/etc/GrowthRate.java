/*
 * GrowthRate.java
 *
 * Created on 17 September 2001, 14:42
 */

package au.com.totemsoft.myplanner.etc;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class GrowthRate extends Object {

    private double incomeRate;

    private double growthRate;

    private double defensiveRate;

    /** Creates new GrowthRate */
    public GrowthRate(double incomeRate, double growthRate, double defensiveRate) {
        this.incomeRate = incomeRate;
        this.growthRate = growthRate;
        this.defensiveRate = defensiveRate;
    }

    public GrowthRate(GrowthRate value) {
        this.incomeRate = value.incomeRate;
        this.growthRate = value.growthRate;
        this.defensiveRate = value.defensiveRate;
    }

    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (!(obj instanceof GrowthRate))
            throw new RuntimeException("!( obj instanceof GrowthRate )");

        return this.incomeRate == ((GrowthRate) obj).incomeRate
                && this.growthRate == ((GrowthRate) obj).growthRate;
    }

    public double getRate() {
        return incomeRate + growthRate;
    }

    public double getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(double value) {
        incomeRate = value;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double value) {
        growthRate = value;
    }

    public double getDefensiveRate() {
        return defensiveRate;
    }

}
