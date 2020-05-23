package au.com.totemsoft.myplanner.swing.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.api.service.FinancialService;
import au.com.totemsoft.myplanner.api.service.UserService;
import au.com.totemsoft.myplanner.config.ServiceConfig;
import au.com.totemsoft.myplanner.projection.GeneralTaxCalculatorNew;
import au.com.totemsoft.myplanner.report.IWordReport;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.UtilityService;
import au.com.totemsoft.myplanner.service.client.EntityService;
import au.com.totemsoft.myplanner.swing.AbstractBasePanel;
import au.com.totemsoft.myplanner.swing.AbstractPanel;
import au.com.totemsoft.myplanner.swing.ClientView;
import au.com.totemsoft.myplanner.swing.FinancialPlannerActionMap;
import au.com.totemsoft.myplanner.swing.FinancialPlannerApp;
import au.com.totemsoft.myplanner.swing.FinancialPlannerMenu;
import au.com.totemsoft.myplanner.swing.FinancialPlannerNavigator;
import au.com.totemsoft.myplanner.swing.PartnerView;
import au.com.totemsoft.myplanner.swing.PersonContactsView;
import au.com.totemsoft.myplanner.swing.PersonDependentsView;
import au.com.totemsoft.myplanner.swing.PersonView;
import au.com.totemsoft.myplanner.swing.assetallocation.AssetAllocationTableModel;
import au.com.totemsoft.myplanner.swing.etc.CommentView;
import au.com.totemsoft.myplanner.swing.financials.AddFinancialView;
import au.com.totemsoft.myplanner.swing.plan.PlanWriter;
import au.com.totemsoft.myplanner.swing.projection.PAYGView;
import au.com.totemsoft.myplanner.swing.sql.ExportDataView;
import au.com.totemsoft.myplanner.swing.sql.ExportImportManagerApp;
import au.com.totemsoft.myplanner.swing.sql.UpdateManagerApp;
import au.com.totemsoft.myplanner.swing.strategy.FinancialViewActionMap;

@Configuration
@ComponentScan(
    basePackageClasses = { ServiceConfig.class }
)
public class SwingConfig {

    @Autowired(required = false) private IWordReport report;// = new au.com.totemsoft.activex.wordreport.WordReportJava2COM();

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