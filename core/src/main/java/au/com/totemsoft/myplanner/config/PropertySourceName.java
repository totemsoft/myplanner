/*
 * PropertySourceName.java
 *
 * Created on January 4, 2002, 9:16 AM
 */

package au.com.totemsoft.myplanner.config;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface PropertySourceName {

    public static final String SOURCE_NAME_PROPERTY = "SOURCE_NAME";

    // user specific properties
    // e.g. last user logged on, etc.
    public static final String PLANNER_LOCALE_PROPERTIES = "PLANNER_LOCALE_PROPERTIES";

    // Word settings
    // e.g. minimum list of documents used in PlanWriter
    public static final String PLANNER_WORD_PROPERTIES = "PLANNER_WORD_PROPERTIES";

    // View settings
    public static final String PLANNER_VIEW_PROPERTIES = "PLANNER_VIEW_PROPERTIES";

}
