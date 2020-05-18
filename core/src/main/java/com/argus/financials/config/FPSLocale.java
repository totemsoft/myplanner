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

import com.argus.config.ResourceLoader;
import com.argus.financials.code.CountryCode;
import com.argus.io.IOUtils;

public class FPSLocale extends BasePropertySource {

    private static FPSLocale instance;

    private static java.util.Locale locale;

    private FPSLocale() {
    }

    public static synchronized FPSLocale getInstance() {
        if (instance == null)
            instance = new FPSLocale();
        return instance;
    }

    public static java.util.Locale getLocale() {
        if (locale == null) {
            getInstance();
            String language = instance.getLanguage() == null ? "" : instance.getLanguage().trim();
            String country = instance.getCountry() == null ? "" : instance.getCountry().trim();
            if (language.length() > 0 && country.length() > 0)
                locale = new java.util.Locale(language, country);
            else
                locale = java.util.Locale.getDefault();
        }
        return locale;
    }

    public static java.util.Locale getLocale(String language, String country) {
        if (locale != null && locale.getLanguage().equals(language)
                && locale.getCountry().equals(country))
            return locale;
        locale = new java.util.Locale(language, country);
        return locale;
    }

    // default values
    protected void initProperties(java.util.Properties value) {
        if (!value.containsKey("HelpsetName"))
            value.setProperty("HelpsetName", "help/Financial Planner.hs");
        if (!value.containsKey("LicenseKeyFilename"))
            value.setProperty("LicenseKeyFilename", "license.txt");
        if (!value.containsKey("ProxyHost"))
            value.setProperty("ProxyHost", "localhost");
        if (!value.containsKey("ProxyPort"))
            value.setProperty("ProxyPort", "8080");
        if (!value.containsKey("DownloadServerURL"))
            value.setProperty("DownloadServerURL", "http://localhost:8080/myplanner/wrap?dispatch=requestprocessor");
        if (!value.containsKey("UpdateDownloadURL"))
            value.setProperty("UpdateDownloadURL", "http://localhost:8080/myplanner/Financial Planner_Download.html");
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
            ResourceLoader.saveProperties((String) getPropertySource(), getProperties());
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

    public String getHelpsetName() {
        return getProperties().getProperty("HelpsetName");
    }

    public void setHelpsetName(String value) {
        getProperties().setProperty("HelpsetName", value);
    }
    
    public String getLogDir() {
        String logDir = getProperties().getProperty("LogDir");
        if (logDir == null || !new File(logDir).exists())
            logDir = IOUtils.getUserDirectory();
        return logDir;
    }

}