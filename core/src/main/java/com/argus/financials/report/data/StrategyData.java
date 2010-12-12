/*
 * StrategyData.java
 *
 * Created on July 23, 2002, 8:56 AM
 */

package com.argus.financials.report.data;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class StrategyData {

    /** Creates new StrategyData */
    private StrategyData() {
    }

    /*
     * 
     * public CashFlowAssumptions cashFlowAssumptions = new
     * CashFlowAssumptions(); public class CashFlowAssumptions extends
     * AssumptionData {}
     * 
     * public WealthAssumptions wealthAssumptions = new WealthAssumptions();
     * public class WealthAssumptions extends AssumptionData {}
     * 
     *  // because of a bug in BaseDocument::getFields() we have to put String
     * members into inner class public Data strategy = new Data(); public class
     * Data implements java.io.Serializable {
     * 
     * public String Name = ""; public String UnallocatedAmount = "";
     *  }
     * 
     * public Restructured restructured = new Restructured(); public class
     * Restructured extends FinancialData {}
     * 
     * public Source source = new Source(); public class Source implements
     * java.io.Serializable { public String TotalAmount = ""; public
     * java.util.ArrayList Items = new java.util.ArrayList();
     * 
     * public class Items implements java.io.Serializable { public String
     * ItemName = ""; // Strategy public String Projection = ""; //
     * StrategyModel public String TotalAmount = "";
     * 
     * public String Financials = ""; // list of all financials,
     * FinancialDesc+Amount }
     * 
     * public Items createNewItem() { return new Items(); }
     * 
     * public void clear() { TotalAmount = ""; Items.clear(); }
     *  }
     * 
     * public Destination destination = new Destination(); public class
     * Destination implements java.io.Serializable { public String TotalAmount =
     * ""; public java.util.ArrayList Items = new java.util.ArrayList();
     * 
     * public class Items implements java.io.Serializable { public String
     * ItemName = ""; // Strategy public String Projection = ""; //
     * StrategyModel public String TotalAmount = "";
     * 
     * public String Financials = ""; // list of all financials,
     * FinancialDesc+Amount }
     * 
     * public Items createNewItem() { return new Items(); }
     * 
     * public void clear() { TotalAmount = ""; Items.clear(); }
     *  }
     * 
     * 
     * public void clear() { super.clear();
     * 
     * restructured.clear();
     * 
     * source.clear(); destination.clear();
     *  }
     * 
     * public void init( com.argus.server.Person person,
     * com.argus.strategy.StrategyGroup sg ) throws java.io.IOException//,
     * com.argus.financials.service.ServiceException { // initPersonal( person );
     * 
     * 
     * if ( sg == null ) return; java.math.BigDecimal totalRestrAmount = ZERO;
     * java.math.BigDecimal totalAssetAmount = ZERO;
     * 
     * StrategyGroupData data = sg.getStrategyGroupData(); if ( data == null )
     * return;
     * 
     * DataCollectionModel dcm = data.getCollectionModel(); DataRestructureModel
     * drm = data.getRestructureModel();
     * 
     * strategy.Name = sg.toString();
     * 
     * Assumptions wa = data.getWealthAssumptions(); if ( wa == null ) { wa =
     * new Assumptions(); wa.update(person); } wealthAssumptions.init( wa );
     * 
     * Assumptions ca = data.getCashFlowAssumptions(); if ( ca == null ) { ca =
     * new Assumptions(); ca.update(person); } cashFlowAssumptions.init( ca );
     * 
     * //if ( dcm != null ) { java.util.Map financials = dcm.getFinancials(); //
     * super.init( person, financials, wa, ca); //}
     * 
     * //if ( drm != null ) { java.util.Map newFinancials = drm.getFinancials(); //
     * restructured.init( person, newFinancials, wa, ca );
     * 
     * //restructured.assetAllocation = null; // we'll use super.assetAllocation
     * instead //System.out.println( "\n!!!!!!!! assetAllocation.init( person,
     * financials, newFinancials )" ); //assetAllocation.init( person,
     * financials, newFinancials ); //System.out.println( "!!!!!!!!!
     * assetAllocation.init( person, financials, newFinancials )\n" );
     * 
     * java.util.Collection r = drm.getRecommendations(); java.util.ArrayList
     * items = source.Items; java.util.ArrayList items2 = destination.Items;
     * 
     * if ( r != null ) {
     * 
     * java.util.Iterator it = r.iterator(); java.math.BigDecimal
     * totalRecomSourceAmount = ZERO; java.math.BigDecimal totalRecomDestAmount =
     * ZERO;
     * 
     * while (it.hasNext()) { com.argus.strategy.Recommendation reco =
     * (com.argus.strategy.Recommendation)it.next(); java.util.Iterator it1 =
     * reco.getSource().iterator(); java.util.Iterator it2 =
     * reco.getDestination().iterator();
     * 
     * while ( it1.hasNext() ){ // Financial f = (Financial)it1.next();
     * StrategyFinancial f = (StrategyFinancial)it1.next();
     * 
     * java.math.BigDecimal totalAmount = ZERO;
     * 
     * if ( f.getAmount(true) != null ) totalAmount = totalAmount.add(
     * f.getAmount(true) );
     * 
     * Source.Items itemRecomSource = source.createNewItem();
     * itemRecomSource.ItemName = (f.getTypeDesc() == null) ? "" :
     * f.getTypeDesc();
     *  // itemCash.Projection = (financial.getModelTitle() == null) ? "" :
     * strategyModel.getModelTitle(); itemRecomSource.TotalAmount =
     * (currency.toString( totalAmount ) == null) ? "" : currency.toString(
     * totalAmount ); totalRecomSourceAmount = totalRecomSourceAmount.add(
     * totalAmount ); itemRecomSource.Financials = (f.toString() == null) ? "" :
     * f.toString(); items.add( itemRecomSource );
     *  }
     * 
     * while ( it2.hasNext() ){ Financial f = (Financial)it2.next();
     * java.math.BigDecimal totalAmount = ZERO;
     * 
     * if ( f.getAmount() != null ) totalAmount = totalAmount.add( f.getAmount() );
     * 
     * Destination.Items itemRecomDest = destination.createNewItem();
     * itemRecomDest.ItemName = (f.getTypeDesc() == null) ? "" :
     * f.getTypeDesc();
     *  // itemCash.Projection = (financial.getModelTitle() == null) ? "" :
     * strategyModel.getModelTitle(); itemRecomDest.TotalAmount =
     * (currency.toString( totalAmount ) == null) ? "" : currency.toString(
     * totalAmount ); totalRecomDestAmount = totalRecomDestAmount.add(
     * totalAmount ); itemRecomDest.Financials = (f.toString() == null) ? "" :
     * f.toString(); items2.add( itemRecomDest );
     *  }
     *  }
     * 
     * source.TotalAmount = currency.toString(totalRecomSourceAmount);
     * destination.TotalAmount = currency.toString(totalRecomDestAmount);
     * strategy.UnallocatedAmount = currency.toString(
     * totalRecomSourceAmount.subtract( totalRecomDestAmount ) );
     *  }
     * 
     * //}
     *  }
     */
}
