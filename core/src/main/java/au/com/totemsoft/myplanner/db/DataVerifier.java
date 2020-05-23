/*
 * DataVerifier.java
 *
 * Created on 14 April 2003, 12:57
 */

package au.com.totemsoft.myplanner.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public interface DataVerifier {
    /**
     * Public inteface designed to provide SQL query to test data
     * avalability/correctness
     * 
     */
    public String getVerificationRequest(int reguestID, Object[] criteria);
    /**
     * The only method returns SQL request as String. Parameters: requestID -
     * int request naumber. (e.g. implementing class will contain public static
     * field CLIENT_NAME_EXISTS = 1). Inside the method there will be a control
     * structure to check if request ID is CLIENT_NAME_EXISTS and String
     * variable 'SELECT ClientName FROM Clients WHERE ClientName=' criteria -
     * array of values to be used to customize request (e.g. for the example
     * above criteria[0]="David Maier")
     */

}
