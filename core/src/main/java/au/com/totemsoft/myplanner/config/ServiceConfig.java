package au.com.totemsoft.myplanner.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.api.bean.hibernate.AbstractCode;
import au.com.totemsoft.myplanner.api.dao.EntityDao;
import au.com.totemsoft.myplanner.api.dao.FinancialCodeDao;
import au.com.totemsoft.myplanner.api.dao.LinkObjectDao;
import au.com.totemsoft.myplanner.api.dao.ObjectDao;
import au.com.totemsoft.myplanner.api.dao.OccupationDao;
import au.com.totemsoft.myplanner.api.service.FinancialService;
import au.com.totemsoft.myplanner.assetinvestment.AvailableInvestmentsSearch;
import au.com.totemsoft.myplanner.assetinvestment.AvailableInvestmentsTableRow;
import au.com.totemsoft.myplanner.bean.db.AbstractPersistable;
import au.com.totemsoft.myplanner.bean.db.ApirPicBean;
import au.com.totemsoft.myplanner.bean.db.AssetAllocationBean;
import au.com.totemsoft.myplanner.bean.db.AssetAllocationDataBean;
import au.com.totemsoft.myplanner.bean.db.FinancialCodeBean;
import au.com.totemsoft.myplanner.bean.db.FundTypeBean;
import au.com.totemsoft.myplanner.bean.db.InstitutionBean;
import au.com.totemsoft.myplanner.bean.db.IressAssetNameBean;
import au.com.totemsoft.myplanner.bean.db.ManagerDataBean;
import au.com.totemsoft.myplanner.bean.db.ProductInformationBean;
import au.com.totemsoft.myplanner.bean.db.SharePriceDataBean;
import au.com.totemsoft.myplanner.bean.db.UnitInformationSearchBean;
import au.com.totemsoft.myplanner.bean.db.UnitPriceDataBean;
import au.com.totemsoft.myplanner.etc.db.ContactBean;
import au.com.totemsoft.myplanner.exchange.AssetProcessor;
import au.com.totemsoft.myplanner.exchange.ExportData;
import au.com.totemsoft.myplanner.projection.DSSCalcNew;
import au.com.totemsoft.myplanner.report.IWordReport;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.ServiceAware;
import au.com.totemsoft.myplanner.service.UtilityService;
import au.com.totemsoft.myplanner.strategy.model.FinancialDataModel;
import au.com.totemsoft.myplanner.table.model.CashflowTableModel;

@Configuration
@EnableAutoConfiguration//(exclude = {VelocityAutoConfiguration.class})
@EntityScan(basePackages = {
    "au.com.totemsoft.myplanner.api.bean.hibernate",
    "au.com.totemsoft.myplanner.domain.hibernate",
})
@ImportResource({"applicationContext.xml"})
public class ServiceConfig {

    @Autowired(required = false) private IWordReport report;// = new au.com.totemsoft.activex.wordreport.WordReportJava2COM();

    @Autowired private ClientService clientService;
    @Autowired private FinancialService financialService;
    @Autowired private UserPreferences userPreferences;
    @Autowired private UtilityService utilityService;

    @Autowired private SQLHelper sqlHelper;
    @Autowired private ObjectDao objectDao;
    @Autowired private LinkObjectDao linkObjectDao;
    @Autowired private EntityDao entityDao;
    @Autowired private FinancialCodeDao financialCodeDao;
    @Autowired private OccupationDao occupationDao;

    @PostConstruct
    private void init() {
        ServiceAware.setClientService(clientService);
        ServiceAware.setUserPreferences(userPreferences);
        ServiceAware.setUtilityService(utilityService);

        DSSCalcNew.setClientService(clientService);
        DSSCalcNew.setUserPreferences(userPreferences);

        CashflowTableModel.setClientService(clientService);
        FinancialDataModel.setPersonService(clientService);
        FinancialDataModel.setFinancialService(financialService);

        AbstractPersistable.setSqlHelper(sqlHelper);
        AbstractPersistable.setObjectDao(objectDao);
        AbstractPersistable.setLinkObjectDao(linkObjectDao);
        AbstractPersistable.setFinancialService(financialService);

        AbstractCode.setEntityDao(entityDao);

        ExportData.setSqlHelper(sqlHelper);
        AssetProcessor.setSqlHelper(sqlHelper);
        UnitPriceDataBean.setSqlHelper(sqlHelper);
        UnitInformationSearchBean.setSqlHelper(sqlHelper);
        SharePriceDataBean.setSqlHelper(sqlHelper);
        ProductInformationBean.setSqlHelper(sqlHelper);
        ManagerDataBean.setSqlHelper(sqlHelper);
        IressAssetNameBean.setSqlHelper(sqlHelper);
        InstitutionBean.setSqlHelper(sqlHelper);
        FundTypeBean.setSqlHelper(sqlHelper);
        FinancialCodeBean.setSqlHelper(sqlHelper);
        AssetAllocationDataBean.setSqlHelper(sqlHelper);
        AssetAllocationBean.setSqlHelper(sqlHelper);
        ApirPicBean.setSqlHelper(sqlHelper);
        ContactBean.setOccupationDao(occupationDao);

        ReportFields.setReport(report);

        AvailableInvestmentsSearch.setFinancialCodeDao(financialCodeDao);
        AvailableInvestmentsTableRow.setFinancialService(financialService);
    }

}