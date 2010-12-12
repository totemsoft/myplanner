/*
 * TestAssetAllocationDataBean.java
 *
 * Created on 11 October 2002, 12:19
 */

package com.argus.financials.assetallocation;

import com.argus.financials.bean.db.AssetAllocationDataBean;

/**
 * 
 * @author shibaevv
 */
public class TestAssetAllocationDataBean {

    /** Creates a new instance of TestAssetAllocationDataBean */
    public TestAssetAllocationDataBean() {
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        AssetAllocationDataBean aadb = new AssetAllocationDataBean();

        try {
            /*
             * aadb.setCode ( 1 ); aadb.setInCash ( 1.0 );
             * aadb.setInFixedInterest ( 2.0 ); aadb.setInAustShares ( 3.0 );
             * aadb.setInIntnlShares ( 4.0 ); aadb.setInProperty ( 5.0 );
             * aadb.setInOther ( 6.0 ); aadb.setDataDate ( "01/01/2001" );
             * aadb.setId ( "" );
             * 
             * com.fiducian.license.FiducianHelper.printFieldNames( aadb );
             * 
             * aadb.create();
             */
            aadb.findByCode(1);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

}
