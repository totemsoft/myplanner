/*
 * DataResolver.java
 *
 * Created on 11 March 2003, 17:50
 */

package au.com.totemsoft.myplanner.db;

import java.util.ArrayList;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public interface DataResolver {

    public static final String GET_IDENTITY = "0";

    public static final String SET_IDENTITY = "1";

    public static final String PLAIN = "2";

    public static final String COMMIT = "10";

    public ArrayList getUpdates();

    public ArrayList getInserts();

    public ArrayList getDeletes();
}
