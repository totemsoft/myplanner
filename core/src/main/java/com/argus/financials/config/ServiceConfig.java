package com.argus.financials.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.argus.activex.wordreport.IWordReport;
import com.argus.dao.SQLHelper;
import com.argus.financials.api.bean.UserPreferences;
import com.argus.financials.api.bean.hibernate.AbstractCode;
import com.argus.financials.api.dao.EntityDao;
import com.argus.financials.api.dao.FinancialCodeDao;
import com.argus.financials.api.dao.LinkObjectDao;
import com.argus.financials.api.dao.ObjectDao;
import com.argus.financials.api.dao.OccupationDao;
import com.argus.financials.api.service.FinancialService;
import com.argus.financials.assetinvestment.AvailableInvestmentsSearch;
import com.argus.financials.assetinvestment.AvailableInvestmentsTableRow;
import com.argus.financials.bean.db.AbstractPersistable;
import com.argus.financials.bean.db.ApirPicBean;
import com.argus.financials.bean.db.AssetAllocationBean;
import com.argus.financials.bean.db.AssetAllocationDataBean;
import com.argus.financials.bean.db.FinancialCodeBean;
import com.argus.financials.bean.db.FundTypeBean;
import com.argus.financials.bean.db.InstitutionBean;
import com.argus.financials.bean.db.IressAssetNameBean;
import com.argus.financials.bean.db.ManagerDataBean;
import com.argus.financials.bean.db.ProductInformationBean;
import com.argus.financials.bean.db.SharePriceDataBean;
import com.argus.financials.bean.db.UnitInformationSearchBean;
import com.argus.financials.bean.db.UnitPriceDataBean;
import com.argus.financials.etc.db.ContactBean;
import com.argus.financials.exchange.AssetProcessor;
import com.argus.financials.exchange.ExportData;
import com.argus.financials.projection.DSSCalcNew;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.ServiceAware;
import com.argus.financials.service.UtilityService;
import com.argus.financials.strategy.model.FinancialDataModel;
import com.argus.financials.table.CashflowTableModel;

@Configuration
@EnableAutoConfiguration//(exclude = {VelocityAutoConfiguration.class})
@EntityScan(basePackages = {
    "com.argus.financials.api.bean.hibernate",
    "com.argus.financials.domain.hibernate",
})
@ImportResource({"applicationContext.xml"})
public class ServiceConfig {

    @Autowired private IWordReport report;// = new com.argus.activex.wordreport.WordReportJava2COM();

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