/*
 * FPSLocale.java
 *
 * Created on 19 October 2001, 09:59
 */

package com.argus.financials.config;

/**
 * 
 * @author valeri chibaev
 * @version
 * 
 * static class
 * 
 */

import java.io.File;
import java.util.Locale;

import com.argus.config.ResourceLoader;
import com.argus.financials.code.CountryCode;

public class FPSLocale extends BasePropertySource {

    private static FPSLocale locale;

    private static java.util.Locale l;

    private Integer countryCodeID;

    private FPSLocale() {
    }

    public static synchronized FPSLocale getInstance() {
        if (locale == null)
            locale = new FPSLocale();
        return locale;
    }

    public static java.util.Locale getLocale() {
        if (l == null) {
            getInstance();

            String language = locale.getLanguage() == null ? "" : locale
                    .getLanguage().trim();
            String country = locale.getCountry() == null ? "" : locale
                    .getCountry().trim();

            // in jdk 1.4 length has to be only 2 char - country (AU), language
            // (en)
            if (language.length() > 0 && country.length() > 0)
                l = new java.util.Locale(language, country);
            else
                l = java.util.Locale.getDefault();

            // if ( getInstance().isDebug() )
            // System.out.println( "\nLocale used is: " + l.getDisplayName() +
            // '\n' );
        }

        return l;
    }

    public static java.util.Locale getLocale(String language, String country) {
        if (l != null && l.getLanguage().equals(language)
                && l.getCountry().equals(country))
            return l;

        l = new java.util.Locale(language, country);
        // if ( getInstance().isDebug() )
        // System.out.println( "\nLocale used is: " + l.getDisplayName() + '\n'
        // );
        return l;
    }

    // default values
    protected void initProperties(java.util.Properties value) {
        if (!value.containsKey("DEBUG"))
            value.setProperty("DEBUG", Boolean.FALSE.toString());
        if (!value.containsKey("HelpsetName"))
            value.setProperty("HelpsetName", "help/Financial Planner.hs");
        if (!value.containsKey("LicenseKeyFilename"))
            value.setProperty("LicenseKeyFilename", "license.txt");
        if (!value.containsKey("ProxyHost"))
            value.setProperty("ProxyHost", "10.101.2.10");
        if (!value.containsKey("ProxyPort"))
            value.setProperty("ProxyPort", "8080");
        if (!value.containsKey("DownloadServerURL"))
            value
                    .setProperty("DownloadServerURL",
                            "http://192.168.1.3:8100/Financial Planner/wrap?cmd=requestprocessor");
        if (!value.containsKey("UpdateDownloadURL"))
            value
                    .setProperty("UpdateDownloadURL",
                            "http://192.168.1.3/Financial Planner/Financial Planner_Download.html");
        if (!value.containsKey("ServerURL"))
            value.setProperty("ServerURL", "");
        if (!value.containsKey("LastUserName"))
            value.setProperty("LastUserName", "administrator");
        if (!value.containsKey("Language"))
            value.setProperty("Language", "en");
        if (!value.containsKey("Country"))
            value.setProperty("Country", "AU");
        if (!value.containsKey("DisplayOnDesktop"))
            value.setProperty("DisplayOnDesktop", Boolean.FALSE.toString());

    }

    /**
     * TODO: load/save these values from/to property file
     */
    public void store() {
        try {
            ResourceLoader.saveProperties((String) getPropertySource(),
                    getProperties());
        } catch (java.io.IOException e) {
            System.err.println("-----> " + e.getMessage() + " <-----");
        }
    }

    /**
     * get/set
     */
    public void setPropertySource(Object value) throws java.io.IOException {
        if (getPropertySource() != null)
            return;

        if (value == null)
            return;

        // set only once - on application startup
        // load getProperties() with pairs from propertySource
        super.setPropertySource(value);

    }

    // rmi server URL (can be localhost or null or www.feducin.com.au)
    public String getServerURL() {
        return getProperties().getProperty("ServerURL");
    }

    public void setServerURL(String value) {
        getProperties().setProperty("ServerURL", value);
    }

    public boolean isLocalServer() {
        String server = getServerURL();
        return server == null || server.trim().length() == 0;
    }

    public boolean useRMIServer() {
        String server = getServerURL();
        return server != null && server.trim().length() > 0;
    }

    public String getLanguage() {
        return getProperties().getProperty("Language");
    }

    public String getDisplayLanguage() {
        String language = getLanguage();

        if (language == null || language.length() == 0)
            return java.util.Locale.getDefault().getDisplayLanguage();
        return getLocale(language, getCountry()).getDisplayLanguage();
    }

    public String getCountry() {
        return getProperties().getProperty("Country");
    }

    public String getDisplayCountry() {
        String country = getCountry();

        if (country == null || country.length() == 0)
            return java.util.Locale.getDefault().getDisplayCountry();
        return getLocale(getLanguage(), country).getDisplayCountry();
    }

    public Integer getCountryCodeID() {
        return new CountryCode().getCodeID(getDisplayCountry());
    }

    public String getLastUserName() {
        return getProperties().getProperty("LastUserName");
    }

    public void setLastUserName(String value) {
        getProperties().setProperty("LastUserName", value);
    }

    public String getDownloadServerURL() {
        return getProperties().getProperty("DownloadServerURL");
    }

    public String getUpdateDownloadURL() {
        return getProperties().getProperty("UpdateDownloadURL");
    }

    public String getProxyHost() {
        return getProperties().getProperty("ProxyHost");
    }

    public String getProxyPort() {
        return getProperties().getProperty("ProxyPort");
    }

    public String getLicenseKeyFilename() {
        return getProperties().getProperty("LicenseKeyFilename");
    }

    public String getHelpsetName() {
        return getProperties().getProperty("HelpsetName");
    }

    public void setHelpsetName(String value) {
        getProperties().setProperty("HelpsetName", value);
    }
    
    public String getLogDir() {
        String logDir = getProperties().getProperty("LogDir");
        if (logDir == null || !new File(logDir).exists())
            logDir = getUserDirectory();
        return logDir;
    }

    public static String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.net.UnknownHostException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    /***************************************************************************
     * System getProperties()
     **************************************************************************/
    public boolean isDisplayOnDesktop() {
        return Boolean.valueOf(System.getProperty("DisplayOnDesktop"))
                .booleanValue();
    }

    // java.rmi.server.codebase
    public static String getRMIServerCodebase() {
        return System.getProperty("java.rmi.server.codebase");
    }

    // user.name User's account name
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    // user.dir User's current working directory
    public static String getUserDirectory() {
        return System.getProperty("user.dir");
    }

    // user.home User's home directory
    public static String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    // java.io.tmpdir
    public static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    private static final String DEV_DIR = "\\dev\\jworkspace";

    public static boolean isDevelopment() {
        // System.out.println( "Working Directory: " + getWorkingDirectory() );
        return getUserDirectory().indexOf(DEV_DIR) >= 0;
    }

    // java.applet.codebase
    public static java.net.URL getAppletCodebase()
            throws java.net.MalformedURLException {
        String s = System.getProperty("java.applet.codebase");
        return s == null ? null : new java.net.URL(s);
    }

    /**
     * @param args
     *            the command line arguments
     */
    // *
    public static void main(String args[]) {
        // java.util.Locale locale = Locale.getDefault();

        java.util.Locale locale = new Locale(Locale.ENGLISH.getLanguage(), "AU"); // jdk
                                                                                    // 1.3
        System.out.println(locale.getLanguage() + ", "
                + Locale.ENGLISH.getLanguage());
        System.out.println(locale.getCountry());

        // java.util.Locale locale = new Locale( "en", "AU" ); // jdk 1.4
        // System.out.println( locale.getDisplayLanguage() );
        // System.out.println( locale.getDisplayCountry() );

        System.out.println(locale.getDisplayName());
        java.text.NumberFormat currencyNumberFormatter = java.text.NumberFormat
                .getCurrencyInstance(locale);
        System.out.println(currencyNumberFormatter.format(19999999.345678));
        System.out.println(currencyNumberFormatter.format(-19999999.345678));

        java.util.Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            System.out.println(locales[i].getLanguage());
            System.out.println(locales[i].getCountry());
            System.out.println(locales[i].getDisplayName());
            currencyNumberFormatter = java.text.NumberFormat
                    .getCurrencyInstance(locales[i]);
            System.out.println(currencyNumberFormatter.format(19999999.345678));
            System.out
                    .println(currencyNumberFormatter.format(-19999999.345678));
        }

        System.exit(0);
    }
    // */

}
