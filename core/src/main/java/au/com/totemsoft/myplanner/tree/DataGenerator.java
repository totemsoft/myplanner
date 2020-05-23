/*
 * DataGenerator.java
 *
 * Created on 11 June 2003, 09:25
 */

package au.com.totemsoft.myplanner.tree;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public interface DataGenerator {

    public void addGeneratedData(FinancialTreeModel model,
            java.util.Map source, final int[] rules, int year);

    public void addGeneratedData(FinancialTreeModel model,
            java.util.Collection source, final int[] rules, int year);

}
