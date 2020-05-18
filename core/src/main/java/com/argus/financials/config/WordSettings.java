/*
 * WordSettings.java
 *
 * Created on 28 October 2002, 13:21
 */

package com.argus.financials.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.argus.io.IOUtils;
import com.argus.io.RTFFileFilter;
import com.argus.swing.SwingUtils;

/**
 * 
 * @author valeri chibaev
 */

public class WordSettings extends BasePropertySource {

    public static final String PLAN_TEMPLATE_DIR = "PlanTemplateDirectory";

    public static final String TEMPLATE_DOCUMENT = "TemplateDocument";
    public static final String REPORT_TEMPLATE_DOCUMENT = "ReportTemplateDocument";

    public static final String EXCEL_TEMPLATE_TABLE = "ExcelTemplateTable";
    public static final String WORD_TEMPLATE_TABLE = "WordTemplateTable";

    public static final String ASSET_ALLOCATION_CURRENT_REPORT = "CurrentAssetAllocationReport";
    public static final String ASSET_ALLOCATION_NEW_REPORT = "NewAssetAllocationReport";
    public static final String CASH_FLOW_REPORT = "CashFlowReport";
    public static final String CASH_FLOW_PROPOSED_REPORT = "CashFlowProposedReport";
    public static final String WEALTH_REPORT = "WealthReport";
    public static final String WEALTH_PROPOSED_REPORT = "WealthProposedReport";
    public static final String TAX_ANALYSIS_REPORT = "TaxAnalysisReport";
    public static final String TAX_ANALYSIS_PROPOSED_REPORT = "TaxAnalysisProposedReport";
    public static final String CLIENT_DETAILS_REPORT = "ClientDetailsReport";
    public static final String DSS_REPORT = "DSSReport";
    public static final String CENTRELINK_REPORT = "CentrelinkReport";
    public static final String ALLOCATED_PENSION_REPORT = "AllocatedPensionReport";
    public static final String ETP_REPORT = "ETPReport";
    public static final String FINANCIAL_REPORT = "FinancialReport";
    public static final String GEARING_REPORT = "GearingReport";
    public static final String ISO_REPORT = "ISOReport";
    public static final String MORTGAGE_REPORT = "MortgageReport";
    public static final String CURRENT_POSITION_REPORT = "CurrentPositionReport";
    public static final String STRATEGY_REPORT = "StrategyReport";
    public static final String PAYG_REPORT = "PAYGReport";

    private static WordSettings instance;

    public static synchronized WordSettings getInstance() {
        if (instance == null)
            instance = new WordSettings();
        return instance;
    }

    /** Creates a new instance of WordSettings */
    private WordSettings() {
    }

    /**
     * load/save these values from/to property file
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

    // default values
    protected void initProperties(java.util.Properties value) {

        if (!value.containsKey(PLAN_TEMPLATE_DIR))
            value.setProperty(PLAN_TEMPLATE_DIR, SwingUtils.getWorkingDirectory());
        if (!value.containsKey(TEMPLATE_DOCUMENT))
            value.setProperty(TEMPLATE_DOCUMENT, "Normal.dot");
        if (!value.containsKey(REPORT_TEMPLATE_DOCUMENT))
            value.setProperty(REPORT_TEMPLATE_DOCUMENT, "Normal.dot");
        if (!value.containsKey(EXCEL_TEMPLATE_TABLE))
            value.setProperty(EXCEL_TEMPLATE_TABLE, "Table.xlt");
        if (!value.containsKey(WORD_TEMPLATE_TABLE))
            value.setProperty(WORD_TEMPLATE_TABLE, "TableTemplates.doc");

        if (!value.containsKey(ALLOCATED_PENSION_REPORT))
            value.setProperty(ALLOCATED_PENSION_REPORT,
                    ALLOCATED_PENSION_REPORT + "." + RTFFileFilter.RTF);
        if (!value.containsKey(ASSET_ALLOCATION_CURRENT_REPORT))
            value.setProperty(ASSET_ALLOCATION_CURRENT_REPORT,
                    ASSET_ALLOCATION_CURRENT_REPORT + "." + RTFFileFilter.RTF);
        if (!value.containsKey(ASSET_ALLOCATION_NEW_REPORT))
            value.setProperty(ASSET_ALLOCATION_NEW_REPORT,
                    ASSET_ALLOCATION_NEW_REPORT + "." + RTFFileFilter.RTF);

        if (!value.containsKey(CASH_FLOW_REPORT))
            value.setProperty(CASH_FLOW_REPORT, CASH_FLOW_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(CASH_FLOW_PROPOSED_REPORT))
            value.setProperty(CASH_FLOW_PROPOSED_REPORT,
                    CASH_FLOW_PROPOSED_REPORT + "." + RTFFileFilter.RTF);

        if (!value.containsKey(WEALTH_REPORT))
            value.setProperty(WEALTH_REPORT, WEALTH_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(WEALTH_PROPOSED_REPORT))
            value.setProperty(WEALTH_PROPOSED_REPORT, WEALTH_PROPOSED_REPORT
                    + "." + RTFFileFilter.RTF);

        if (!value.containsKey(TAX_ANALYSIS_REPORT))
            value.setProperty(TAX_ANALYSIS_REPORT, TAX_ANALYSIS_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(TAX_ANALYSIS_PROPOSED_REPORT))
            value.setProperty(TAX_ANALYSIS_PROPOSED_REPORT,
                    TAX_ANALYSIS_PROPOSED_REPORT + "." + RTFFileFilter.RTF);

        if (!value.containsKey(CLIENT_DETAILS_REPORT))
            value.setProperty(CLIENT_DETAILS_REPORT, CLIENT_DETAILS_REPORT
                    + "." + RTFFileFilter.RTF);
        if (!value.containsKey(DSS_REPORT))
            value.setProperty(DSS_REPORT, DSS_REPORT + "." + RTFFileFilter.RTF);
        if (!value.containsKey(CENTRELINK_REPORT))
            value.setProperty(CENTRELINK_REPORT, CENTRELINK_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(ETP_REPORT))
            value.setProperty(ETP_REPORT, ETP_REPORT + "." + RTFFileFilter.RTF);
        if (!value.containsKey(FINANCIAL_REPORT))
            value.setProperty(FINANCIAL_REPORT, FINANCIAL_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(GEARING_REPORT))
            value.setProperty(GEARING_REPORT, GEARING_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(ISO_REPORT))
            value.setProperty(ISO_REPORT, ISO_REPORT + "." + RTFFileFilter.RTF);
        if (!value.containsKey(MORTGAGE_REPORT))
            value.setProperty(MORTGAGE_REPORT, MORTGAGE_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(CURRENT_POSITION_REPORT))
            value.setProperty(CURRENT_POSITION_REPORT, CURRENT_POSITION_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(STRATEGY_REPORT))
            value.setProperty(STRATEGY_REPORT, STRATEGY_REPORT + "."
                    + RTFFileFilter.RTF);
        if (!value.containsKey(PAYG_REPORT))
            value.setProperty(PAYG_REPORT, PAYG_REPORT + "."
                    + RTFFileFilter.RTF);
    }

    public void store() {
        /*
         * try { ResourceLoader.saveProperties( (String) getPropertySource(),
         * getProperties() ); } catch ( java.io.IOException e ) {
         * System.err.println( "-----> " + e.getMessage() + " <-----" ); }
         */
    }

    private String createNewFile(String filePath) {
        try {
            // class path (could be inside zip/jar)
            File file = new File(SwingUtils.getTemporaryDirectory(), filePath);
            if (file.exists() && !file.delete())
                System.err.println("Failed to delete file: " + file);
            else if (!IOUtils.createNewFile(file))
                throw new IOException("Failed to create file: " + file);
            
            IOUtils.write(
                    IOUtils.getResource(filePath).openStream(),
                    new FileOutputStream(file));
            
            return file.getCanonicalPath();
            
        } catch (IOException e) {
            e.printStackTrace();
            return filePath; // give up
            //throw new RuntimeException(e);
        }
    }

    /**
     * get/set
     */
    public String getPlanTemplateDirectory() {
        String planTemplateDirectory = getProperties().getProperty(PLAN_TEMPLATE_DIR);
        return planTemplateDirectory;
    }

    public File getPlanTemplate() {
        String planTemplateDirectory = getPlanTemplateDirectory();
        File file = new File(planTemplateDirectory);
        if (!file.exists()) {
            file = new File(IOUtils.getUserDirectory(), planTemplateDirectory);
        }
        return file;
    }

    public String getTemplateDocument() {
        return createNewFile(getProperties().getProperty(TEMPLATE_DOCUMENT));
    }

    public String getReportTemplateDocument() {
        return createNewFile(getProperties().getProperty(REPORT_TEMPLATE_DOCUMENT));
    }

    public String getExcelTemplateTable() {
        return createNewFile(getProperties().getProperty(EXCEL_TEMPLATE_TABLE));
    }

    public String getWordTemplateTable() {
        return createNewFile(getProperties().getProperty(WORD_TEMPLATE_TABLE));
    }

    // //////////////////////////////////////////////////////////////////////////
    // REPORTS //
    // //////////////////////////////////////////////////////////////////////////
    public String getAllocatedPensionReport() {
        return createNewFile(getProperties().getProperty(ALLOCATED_PENSION_REPORT));
    }

    public String getAssetAllocationCurrentReport() {
        return createNewFile(getProperties().getProperty(ASSET_ALLOCATION_CURRENT_REPORT));
    }

    public String getAssetAllocationNewReport() {
        return createNewFile(getProperties().getProperty(ASSET_ALLOCATION_NEW_REPORT));
    }

    public String getCashFlowReport() {
        return createNewFile(getProperties().getProperty(CASH_FLOW_REPORT));
    }

    public String getCashFlowProposedReport() {
        return createNewFile(getProperties().getProperty(CASH_FLOW_PROPOSED_REPORT));
    }

    public String getWealthReport() {
        return createNewFile(getProperties().getProperty(WEALTH_REPORT));
    }

    public String getWealthProposedReport() {
        return createNewFile(getProperties().getProperty(WEALTH_PROPOSED_REPORT));
    }

    public String getTaxAnalysisReport() {
        return createNewFile(getProperties().getProperty(TAX_ANALYSIS_REPORT));
    }

    public String getTaxAnalysisProposedReport() {
        return createNewFile(getProperties().getProperty(TAX_ANALYSIS_PROPOSED_REPORT));
    }

    public String getClientDetailsReport() {
        return createNewFile(getProperties().getProperty(CLIENT_DETAILS_REPORT));
    }

    public String getDSSReport() {
        return createNewFile(getProperties().getProperty(DSS_REPORT));
    }

    public String getCentrelinkReport() {
        return createNewFile(getProperties().getProperty(CENTRELINK_REPORT));
    }

    public String getETPReport() {
        return createNewFile(getProperties().getProperty(ETP_REPORT));
    }

    public String getFinancialReport() {
        return createNewFile(getProperties().getProperty(FINANCIAL_REPORT));
    }

    public String getGearingReport() {
        return createNewFile(getProperties().getProperty(GEARING_REPORT));
    }

    public String getISOReport() {
        return createNewFile(getProperties().getProperty(ISO_REPORT));
    }

    public String getMortgageReport() {
        return createNewFile(getProperties().getProperty(MORTGAGE_REPORT));
    }

    public String getCurrentPositionReport() {
        return createNewFile(getProperties().getProperty(CURRENT_POSITION_REPORT));
    }

    public String getStrategyReport() {
        return createNewFile(getProperties().getProperty(STRATEGY_REPORT));
    }

    public String getPAYGReport() {
        return createNewFile(getProperties().getProperty(PAYG_REPORT));
    }

}
