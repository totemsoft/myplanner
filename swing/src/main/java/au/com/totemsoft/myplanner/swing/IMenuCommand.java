/*
 * MainMenuActionCommand.java
 *
 * Created on 19 February 2002, 10:15
 */

package au.com.totemsoft.myplanner.swing;

import au.com.totemsoft.util.Pair;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public interface IMenuCommand {
    
    public static final Pair SAVE = new Pair("SAVE", "Save");

    public static final Pair PRINT = new Pair("PRINT", "Print...");

    public static final Pair EXPORT = new Pair("EXPORT", "Export");

    public static final Pair IMPORT = new Pair("IMPORT", "Import");

    public static final Pair EXIT = new Pair("EXIT", "Exit");

    public static final Pair CUT = new Pair("CUT", "Cut");

    public static final Pair COPY = new Pair("COPY", "Copy");

    public static final Pair PASTE = new Pair("PASTE", "Paste");

    public static final Pair ADD = new Pair("ADD", "Add");

    public static final Pair REMOVE = new Pair("REMOVE", "Remove");

    public static final Pair UPDATE = new Pair("UPDATE", "Update");

    public static final Pair STANDARD = new Pair("STANDARD", "Standard");

    public static final Pair CUSTOMISE = new Pair("CUSTOMISE", "Customise...");

    public static final Pair LOOK_AND_FEEL = new Pair("LOOK_AND_FEEL", "LookAndFeel");

    public static final Pair SYSTEM_OUT = new Pair("SYSTEM_OUT", "Sytem Output");

    public static final Pair SYSTEM_ERR = new Pair("SYSTEM_ERR", "Sytem Error");

    public static final Pair SOFTWARE_UPDATES = new Pair("SOFTWARE_UPDATES", "Software Updates");

    public static final Pair DOWNLOAD_UPDATE = new Pair("DOWNLOAD_UPDATE", "Download Update");

    public static final Pair PLAN_TRACKER = new Pair("PLAN_TRACKER", "Plan Tracker");

    public static final Pair CLIENT_DETAILS = new Pair("CLIENT_DETAILS", "ClientView Details");

    public static final Pair PARTNER_DETAILS = new Pair("PARTNER_DETAILS", "Partner Details");

    public static final Pair FINANCIALS = new Pair("FINANCIALS", "Financials");

    public static final Pair RISK_EVALUATION_CLIENT = new Pair("RISK_EVALUATION_CLIENT", "Risk Management");

    public static final Pair RISK_EVALUATION_PARTNER = new Pair("RISK_EVALUATION_PARTNER", "Risk Management");

    public static final Pair STRATEGIC_ADVICE = new Pair("STRATEGIC_ADVICE", "Strategic Advice");

    public static final Pair STRATEGY_DESINER = new Pair("STRATEGY_DESINER", "Strategy Desiner");

    public static final Pair INVESTMENT_GURU = new Pair("INVESTMENT_GURU", "Investment Guru");

    public static final Pair MANUAL_SELECTION = new Pair("MANUAL_SELECTION", "Manual Selection");

    public static final Pair NO_ADVICE = new Pair("NO_ADVICE", "No Advice");

    public static final Pair LIMITED_ADVICE = new Pair("LIMITED_ADVICE", "Limited Advice");

    public static final Pair FULL_ADVICE = new Pair("FULL_ADVICE", "Full Advice");

    public static final Pair PLAN_WIZARD = new Pair("PLAN_WIZARD", "Plan Wizard");

    public static final Pair PLAN_TEMPLATE_WIZARD = new Pair("PLAN_TEMPLATE_WIZARD", "Plan Template Wizard");

    public static final Pair SEARCH_CLIENT = new Pair("SEARCH_CLIENT", "ClientView Search");

    public static final Pair ADD_CLIENT = new Pair("ADD_CLIENT", "Add New ClientView");

    public static final Pair CRM = new Pair("CRM", "ClientView Relationship Mgmt (CRM)");

    public static final Pair NEW = new Pair("NEW", "[New]");

    public static final Pair CURRENT_POSITION_CALC = new Pair("CURRENT_POSITION_CALC", "Current Position");

    // public static final Pair WEALTH_PROTECTION = new Pair("WEALTH_PROTECTION", "Wealth Protection");
    public static final Pair INSURANCE_NEEDS_CALC = new Pair("INSURANCE_NEEDS_CALC", "Insurance Needs");

    public static final Pair PREMIUM_CALC = new Pair("PREMIUM_CALC", "Premium Calculator");

    // public static final Pair WEALTH_CREATION = new Pair("WEALTH_CREATION", "Wealth Creation");
    public static final Pair INVESTMENT_GEARING_CALC = new Pair("INVESTMENT_GEARING_CALC", "Investment Gearing");

    public static final Pair PROJECTED_WEALTH_CALC = new Pair("PROJECTED_WEALTH_CALC", "Projected Wealth");

    public static final Pair INVESTMENT_PROPERTIES_CALC = new Pair("INVESTMENT_PROPERTIES_CALC", "Investment Properties");

    public static final Pair LOAN_AND_MORTGAGE_CALC = new Pair("LOAN_AND_MORTGAGE_CALC", "Loan & Mortgage");

    // public static final Pair RETIREMENT = new Pair("RETIREMENT", "Retirement");
    public static final Pair ALOCATED_PENSION_CALC = new Pair("ALOCATED_PENSION_CALC", "Allocated Pension");

    public static final Pair ETP_ROLLOVER_CALC = new Pair("ETP_ROLLOVER_CALC", "ETP & Rollover");

    public static final Pair SUPERANNUATION_RBL_CALC = new Pair("SUPERANNUATION_RBL_CALC", "Superannuation RBL");

    public static final Pair RETIREMENT_CALC = new Pair("RETIREMENT_CALC", "Retirement Calculator");

    public static final Pair RETIREMENT_HOME_CALC = new Pair("RETIREMENT_HOME_CALC", "Retirement Home");

    // public static final Pair TAXATION = new Pair("TAXATION", "Taxation");
    public static final Pair PAYG_CALC = new Pair("PAYG_CALC", "PAYG Calculator");

    public static final Pair CGT_CALC = new Pair("CGT_CALC", "Capital Gains Tax (CGT)");

    public static final Pair CENTRELINK_CALC = new Pair("CENTRELINK_CALC", "CentreLink Calculator");

    public static final Pair SWAP_ASSETS = new Pair("SWAP_ASSETS", "Swap Assets");

    public static final Pair RECOVER_ASSETS = new Pair("RECOVER_ASSETS", "Recover Assets");

    public static final Pair REMOVE_ASSETS = new Pair("REMOVE_ASSETS", "Remove Assets");

    public static final Pair FEEDBACK = new Pair("FEEDBACK", "Feedback");

    // tools

    // Help
    public static final Pair CONTENTS_INDEX = new Pair("CONTENTS_INDEX", "Help Contents");

    public static final Pair SEARCH_CONTENTS = new Pair("SEARCH_CONTENTS", "Search Contents");

    public static final Pair ABOUT = new Pair("ABOUT", "About Financial Planner");

    //Financial View
    public static final Pair FINANCIAL_ADD = new Pair("FINANCIAL_ADD", "Add Item");
    public static final Pair FINANCIAL_EDIT = new Pair("FINANCIAL_EDIT", "Modify Item");
    public static final Pair FINANCIAL_COPY = new Pair("FINANCIAL_COPY", "Copy Item");
    public static final Pair FINANCIAL_REMOVE = new Pair("FINANCIAL_REMOVE", "Remove Item");
    //View Usage Chart
    public static final Pair WEALTH_ANALYSIS = new Pair("WEALTH_ANALYSIS", "Wealth Analysis");
    public static final Pair TAX_ANALYSIS = new Pair("TAX_ANALYSIS", "Tax Analysis");
    public static final Pair CASHFLOW_ANALYSIS = new Pair("CASHFLOW_ANALYSIS", "Cash Flow");
    public static final Pair ASSET_ALLOCATION = new Pair("ASSET_ALLOCATION", "Asset Allocation");
    public static final Pair UPDATE_PRICE = new Pair("UPDATE_PRICE", "Update Prices");
    
}
