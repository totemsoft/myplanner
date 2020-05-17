package com.argus.financials.ui.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.argus.dao.SQLHelper;
import com.argus.financials.api.bean.UserPreferences;
import com.argus.financials.api.service.FinancialService;
import com.argus.financials.api.service.UserService;
import com.argus.financials.projection.GeneralTaxCalculatorNew;
import com.argus.financials.report.IWordReport;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.UtilityService;
import com.argus.financials.service.client.EntityService;
import com.argus.financials.ui.AbstractBasePanel;
import com.argus.financials.ui.AbstractPanel;
import com.argus.financials.ui.ClientView;
import com.argus.financials.ui.FinancialPlannerActionMap;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.financials.ui.FinancialPlannerMenu;
import com.argus.financials.ui.FinancialPlannerNavigator;
import com.argus.financials.ui.PartnerView;
import com.argus.financials.ui.PersonContactsView;
import com.argus.financials.ui.PersonDependentsView;
import com.argus.financials.ui.PersonView;
import com.argus.financials.ui.assetallocation.AssetAllocationTableModel;
import com.argus.financials.ui.etc.CommentView;
import com.argus.financials.ui.financials.AddFinancialView;
import com.argus.financials.ui.plan.PlanWriter;
import com.argus.financials.ui.projection.PAYGView;
import com.argus.financials.ui.sql.ExportDataView;
import com.argus.financials.ui.sql.ExportImportManagerApp;
import com.argus.financials.ui.sql.UpdateManagerApp;
import com.argus.financials.ui.strategy.FinancialViewActionMap;

@Configuration
@ComponentScan(basePackages = {
    "com.argus.financials.config" // ServiceConfig
})
public class SwingConfig {

    @Autowired private IWordReport report;// = new com.argus.activex.wordreport.WordReportJava2COM();

    @Autowired private ClientService clientService;
    @Autowired private EntityService entityService;
    @Autowired private FinancialService financialService;
    @Autowired private UserPreferences userPreferences;
    @Autowired private UserService userService;
    @Autowired private UtilityService utilityService;
    @Autowired private SQLHelper sqlHelper;

    @PostConstruct
    private void init() {
        FinancialPlannerApp.setClientService(clientService);
        FinancialPlannerApp.setUtilityService(utilityService);
        FinancialPlannerMenu.setClientService(clientService);
        FinancialPlannerNavigator.setClientService(clientService);
        FinancialPlannerActionMap.setClientService(clientService);
        FinancialPlannerActionMap.setUserPreferences(userPreferences);
        FinancialPlannerActionMap.setUserService(userService);
        ClientView.setClientService(clientService);
        ClientView.setUserPreferences(userPreferences);
        PartnerView.setClientService(clientService);
        PersonContactsView.setClientService(clientService);
        PersonView.setUtilityService(utilityService);
        PersonDependentsView.setClientService(clientService);
        AssetAllocationTableModel.setClientService(clientService);
        AddFinancialView.setFinancialService(financialService);
        CommentView.setClientService(clientService);
        ExportImportManagerApp.setClientService(clientService);
        ExportImportManagerApp.setUserPreferences(userPreferences);
        GeneralTaxCalculatorNew.setClientService(clientService);

        AbstractPanel.setClientService(clientService);
        AbstractPanel.setEntityService(entityService);
        AbstractPanel.setFinancialService(financialService);
        AbstractPanel.setUserPreferences(userPreferences);
        AbstractPanel.setUserService(userService);
        AbstractPanel.setUtilityService(utilityService);

        AbstractBasePanel.setClientService(clientService);
        FinancialViewActionMap.setClientService(clientService);
        ExportDataView.setUserService(userService);
        PAYGView.setClientService(clientService);

        PlanWriter.setReport(report);

        UpdateManagerApp.setSqlHelper(sqlHelper);
    }

}