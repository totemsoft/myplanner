/*
 * DSSViewNew.java
 *
 * Created on April 28, 2003, 4:31 PM
 */

package com.argus.financials.ui.projection;

import java.awt.Cursor;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.argus.bean.FComboBox;
import com.argus.bean.MessageSentEvent;
import com.argus.bean.WizardContentHandler;
import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.code.SexCode;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.projection.DSSCalcNew;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.IntegerInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.AbstractBasePanel;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.financials.ui.IMenuCommand;
import com.argus.util.KeyValue;

/**
 * 
 * @author shibaevv
 */
public class DSSViewNew extends AbstractBasePanel implements DocumentNames,
        com.argus.financials.swing.ICloseDialog, ActionEventID {

    private String modelTitle = "New Centrelink Calculator";

    private DSSCalcNew calc;

    private Model model;

    private boolean newModel;

    private NonDeemedAssetsDetailsView dView;

    private NonDeemedAssetsDetailsViewP dViewP;

    WizardContentHandler wch;

    private boolean noClientPension;

    private boolean noPartnerPension;

    /** Creates new form DSSViewNew */
    public DSSViewNew() {
        calc = new DSSCalcNew();

        addFormDataModel(calc);
        calc.addModelDataChangedListener(this);
        calc.addMessageSentListener(this);

        initComponents();
        initComponents2();

        setActionMap();

    }

    private void initComponents2() {

        jPanel12.setVisible(false);

        fTextFieldName.setFieldType(fTextFieldName.ANY);
        fTextFieldNameP.setFieldType(fTextFieldName.ANY);

        jPanelPartner.setVisible(false);
        jPanelPartner1.setVisible(false);
        jPanelPartner2.setVisible(false);

        // Add wizard content handler

        wch = new WizardContentHandler(jPanelCards);

        jPanelCards.setLayout(wch);

        wch.addItem(new KeyValue("ClientView Info", "card1"));
        wch.addItem(new KeyValue("Benefit Options", "card2"));
        wch.addItem(new KeyValue("ClientView's Asset & Income", "card3"));
        wch.addItem(new KeyValue("Test Result", "card4"));

        wch.addLayoutComponent(jPanel3, "card1");
        wch.addLayoutComponent(jPanel11, "card2");
        wch.addLayoutComponent(jPanel6, "card3");
        wch.addLayoutComponent(jPanel8, "card4");

        wch.setButtonNext(jButtonNext);
        wch.setButtonPrevious(jButtonBack);
        wch.addFormDataChangedListener(this);
        wch.setLabel(jLabel88);
        wch.registerComponent(WIZARD, this);
        jPanelSteps.add(wch.getPanel());
        wch.show(jPanelCards, "card1");
        addChangedListener();
        registerComponents();

        dView = new NonDeemedAssetsDetailsView(calc, this);
        dViewP = new NonDeemedAssetsDetailsViewP(calc, this);

    }

    public Integer getDefaultType() {
        return ModelTypeID.rcCENTRELINK_CALC.getCodeId();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcCENTRELINK_CALC.getDescription();
    }

    public String getTitle() {
        return this.modelTitle;
    }

    public void setTitle(String title) {
        this.modelTitle = title;
    }

    public void updateTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    private void addChangedListener() {
        fTextFieldName.addFormDataChangedListener(this);
        fTextFieldNameP.addFormDataChangedListener(this);
        fTextFieldChildren.addFormDataChangedListener(this);
        fCheckBox1.addFormDataChangedListener(this);
        fComboBox1.addFormDataChangedListener(this);
        fComboBox3.addFormDataChangedListener(this);
        fComboBoxMarital.addFormDataChangedListener(this);
        fDateChooser1.addFormDataChangedListener(this);
        fDateChooser2.addFormDataChangedListener(this);
        fDateChooser3.addFormDataChangedListener(this);
        fDateChooser4.addFormDataChangedListener(this);

        fCheckBox2.addFormDataChangedListener(this);
        fCheckBox3.addFormDataChangedListener(this);
        fCheckBox4.addFormDataChangedListener(this);
        fCheckBox5.addFormDataChangedListener(this);
        fCheckBox6.addFormDataChangedListener(this);
        fCheckBox7.addFormDataChangedListener(this);
        fCheckBox8.addFormDataChangedListener(this);
        fCheckBox10.addFormDataChangedListener(this);

        fCheckBox21.addFormDataChangedListener(this);
        fCheckBox31.addFormDataChangedListener(this);
        fCheckBox41.addFormDataChangedListener(this);
        fCheckBox61.addFormDataChangedListener(this);
        fCheckBox71.addFormDataChangedListener(this);
        fCheckBox9.addFormDataChangedListener(this);
        fCheckBox11.addFormDataChangedListener(this);

        fTextField231.addFormDataChangedListener(this);
        fTextField241.addFormDataChangedListener(this);
        fTextField251.addFormDataChangedListener(this);
        fTextField261.addFormDataChangedListener(this);
        fTextField281.addFormDataChangedListener(this);
        fTextField141.addFormDataChangedListener(this);
        fTextField151.addFormDataChangedListener(this);
        fTextField161.addFormDataChangedListener(this);

        fTextField2311.addFormDataChangedListener(this);
        fTextField2411.addFormDataChangedListener(this);
        fTextField2511.addFormDataChangedListener(this);
        fTextField2611.addFormDataChangedListener(this);
        fTextField2811.addFormDataChangedListener(this);
        fTextField1411.addFormDataChangedListener(this);
        fTextField1511.addFormDataChangedListener(this);
        fTextField1611.addFormDataChangedListener(this);

        fTextField110.addFormDataChangedListener(this);
        fTextField210.addFormDataChangedListener(this);
        fTextField310.addFormDataChangedListener(this);
        fTextField410.addFormDataChangedListener(this);
        fTextField510.addFormDataChangedListener(this);
        fTextField65.addFormDataChangedListener(this);
        fTextField72.addFormDataChangedListener(this);
        fTextField81.addFormDataChangedListener(this);
        fTextField91.addFormDataChangedListener(this);
        fTextField112.addFormDataChangedListener(this);
        fTextField121.addFormDataChangedListener(this);
        fTextField131.addFormDataChangedListener(this);
        fTextField111.addFormDataChangedListener(this);
        fTextField211.addFormDataChangedListener(this);
        fTextField312.addFormDataChangedListener(this);
        fTextField412.addFormDataChangedListener(this);
        fTextField512.addFormDataChangedListener(this);
        fTextField66.addFormDataChangedListener(this);
        fTextField73.addFormDataChangedListener(this);
        fTextField441.addFormDataChangedListener(this);
        fTextField451.addFormDataChangedListener(this);
        fTextField521.addFormDataChangedListener(this);
        fTextField501.addFormDataChangedListener(this);
        fTextField541.addFormDataChangedListener(this);

    }

    private void registerComponents() {
        fTextFieldChildren.registerComponent(CHILDREN_AMOUNT, this);
        fTextFieldName.registerComponent(CLIENT_NAME, this);
        fTextFieldNameP.registerComponent(NAME_PARTNER, this);
        fComboBox1.registerComponent(SEX_CODE, this);
        fComboBox3.registerComponent(PARTNER_SEX_CODE, this);
        fComboBoxMarital.registerComponent(MARITAL_STATUS, this);
        fDateChooser1.registerComponent(DOB, this);
        fDateChooser3.registerComponent(DOB_PARTNER, this);
        fDateChooser2.registerComponent(CALCULATION_DATE, this);
        fDateChooser4.registerComponent(CALCULATION_DATE_PARTNER, this);
        fCheckBox1.registerComponent(HOME_OWNER, this);
        fTextField64.registerComponent(AGE, this);
        fTextField641.registerComponent(AGE_PARTNER, this);

        fCheckBox2.registerComponent(AGE_PENSION, this);
        fCheckBox3.registerComponent(NEW_START_ALLOWANCE, this);
        fCheckBox4.registerComponent(DISABILITY_SUPPORT_PENSION, this);
        fCheckBox5.registerComponent(WIDOW_ALLOWANCE, this);
        fCheckBox6.registerComponent(SICKNESS_ALLOWANCE, this);
        fCheckBox7.registerComponent(CARER_ALLOWANCE, this);
        fCheckBox8.registerComponent(NO_OPTION, this);
        fCheckBox10.registerComponent(PARTNER_ALLOWANCE, this);

        fCheckBox21.registerComponent(AGE_PENSION_PARTNER, this);
        fCheckBox31.registerComponent(NEW_START_ALLOWANCE_PARTNER, this);
        fCheckBox41.registerComponent(DISABILITY_SUPPORT_PENSION_PARTNER, this);
        fCheckBox61.registerComponent(SICKNESS_ALLOWANCE_PARTNER, this);
        fCheckBox71.registerComponent(CARER_ALLOWANCE_PARTNER, this);
        fCheckBox9.registerComponent(NO_OPTION_P, this);
        fCheckBox11.registerComponent(PARTNER_ALLOWANCE_PARTNER, this);

        fTextField1.registerComponent(CASH_ASSET_AA, this);
        fTextField2.registerComponent(MAN_FUNDS_AA, this);
        fTextField3.registerComponent(SHARES_AA, this);
        fTextField4.registerComponent(OTHER_INV_AA, this);
        fTextField5.registerComponent(DEEMED_SUP_AA, this);
        fTextField6.registerComponent(STA_AA, this);
        fTextField7.registerComponent(INV_PRO_AA, this);
        fTextField8.registerComponent(HC_AA, this);
        fTextField9.registerComponent(VBC_AA, this);
        fTextField11.registerComponent(OTHER_PER_AA, this);
        fTextField12.registerComponent(GA_AA, this);
        fTextField13.registerComponent(LOANS_AA, this);
        fTextField14.registerComponent(INV_PRO_AI, this);
        fTextField15.registerComponent(SAL_AI, this);
        fTextField16.registerComponent(OTHER_AI, this);

        fTextField17.registerComponent(CASH_ASSET_TA, this);
        fTextField18.registerComponent(MAN_FUNDS_TA, this);
        fTextField19.registerComponent(SHARES_TA, this);
        fTextField20.registerComponent(OTHER_INV_TA, this);
        fTextField21.registerComponent(DEEMED_SUP_TA, this);
        fTextField22.registerComponent(STA_TA, this);
        fTextField23.registerComponent(CASH_ASSET_AI, this);
        fTextField24.registerComponent(MAN_FUNDS_AI, this);
        fTextField25.registerComponent(SHARES_AI, this);
        fTextField26.registerComponent(OTHER_INV_AI, this);
        fTextField27.registerComponent(DEEMED_SUP_AI, this);
        fTextField28.registerComponent(STA_AI, this);
        fTextField38.registerComponent(TDI, this);
        fTextField35.registerComponent(AP_AA, this);
        fTextField36.registerComponent(ANN_AA, this);
        fTextField37.registerComponent(COM_ANN_AA, this);
        fTextField32.registerComponent(AP_TA, this);
        fTextField33.registerComponent(ANN_TA, this);
        fTextField34.registerComponent(COM_ANN_TA, this);
        fTextField43.registerComponent(INV_PRO_TA, this);
        fTextField29.registerComponent(AP_AI, this);
        fTextField30.registerComponent(ANN_AI, this);
        fTextField31.registerComponent(COM_ANN_AI, this);
        fTextField41.registerComponent(AP_TI, this);
        fTextField40.registerComponent(ANN_TI, this);
        fTextField39.registerComponent(COM_ANN_TI, this);
        fTextField42.registerComponent(INV_PRO_TI, this);
        fTextField44.registerComponent(HC_TA, this);
        fTextField45.registerComponent(VBC_TA, this);
        fTextField52.registerComponent(OTHER_PER_TA, this);
        fTextField50.registerComponent(GA_TA, this);
        fTextField47.registerComponent(LOANS_TA, this);
        fTextField51.registerComponent(SAL_TI, this);
        fTextField49.registerComponent(OTHER_TI, this);
        fTextField54.registerComponent(TOTAL_AA, this);
        fTextField53.registerComponent(TOTAL_TA, this);
        fTextField55.registerComponent(TOTAL_AI, this);
        fTextField48.registerComponent(TOTAL_TI, this);
        fTextField70.registerComponent(MAX_BENEFIT, this);
        fTextField56.registerComponent(ASSET_TEST_RESULT, this);
        fTextField58.registerComponent(INCOME_TEST_RESULT, this);
        fTextField60.registerComponent(BASIC_BENEFIT, this);
        fTextField62.registerComponent(PHAR, this);
        fTextField71.registerComponent(MAX_BENEFIT_P, this);
        fTextField57.registerComponent(ASSET_TEST_RESULT_P, this);
        fTextField59.registerComponent(INCOME_TEST_RESULT_P, this);
        fTextField61.registerComponent(BASIC_BENEFIT_P, this);
        fTextField63.registerComponent(PHAR_P, this);

        fTextField701.registerComponent(MAX_BENEFIT_ANN, this);
        fTextField561.registerComponent(ASSET_TEST_RESULT_ANN, this);
        fTextField581.registerComponent(INCOME_TEST_RESULT_ANN, this);
        fTextField601.registerComponent(BASIC_BENEFIT_ANN, this);
        fTextField621.registerComponent(PHAR_ANN, this);
        fTextField711.registerComponent(MAX_BENEFIT_P_ANN, this);
        fTextField571.registerComponent(ASSET_TEST_RESULT_P_ANN, this);
        fTextField591.registerComponent(INCOME_TEST_RESULT_P_ANN, this);
        fTextField611.registerComponent(BASIC_BENEFIT_P_ANN, this);
        fTextField631.registerComponent(PHAR_P_ANN, this);

        fTextField231.registerComponent(CASH_ASSET_AI_C, this);
        fTextField2311.registerComponent(CASH_ASSET_AI_P, this);

        fTextField241.registerComponent(MAN_FUNDS_AI_C, this);
        fTextField2411.registerComponent(MAN_FUNDS_AI_P, this);

        fTextField251.registerComponent(SHARES_AI_C, this);
        fTextField2511.registerComponent(SHARES_AI_P, this);

        fTextField261.registerComponent(OTHER_INV_AI_C, this);
        fTextField2611.registerComponent(OTHER_INV_AI_P, this);

        fTextField271.registerComponent(DEEMED_SUP_AI_C, this);
        fTextField2711.registerComponent(DEEMED_SUP_AI_P, this);

        fTextField281.registerComponent(STA_AI_C, this);
        fTextField2811.registerComponent(STA_AI_P, this);

        fTextField381.registerComponent(TDI_C, this);
        fTextField3811.registerComponent(TDI_P, this);

        fTextField141.registerComponent(INV_PRO_AI_C, this);
        fTextField421.registerComponent(INV_PRO_TI_C, this);
        fTextField1411.registerComponent(INV_PRO_AI_P, this);
        fTextField4211.registerComponent(INV_PRO_TI_P, this);

        fTextField151.registerComponent(SAL_AI_C, this);
        fTextField511.registerComponent(SAL_TI_C, this);
        fTextField1511.registerComponent(SAL_AI_P, this);
        fTextField5111.registerComponent(SAL_TI_P, this);

        fTextField161.registerComponent(OTHER_AI_C, this);
        fTextField491.registerComponent(OTHER_TI_C, this);
        fTextField1611.registerComponent(OTHER_AI_P, this);
        fTextField4911.registerComponent(OTHER_TI_P, this);

        fTextField551.registerComponent(TOTAL_AI_C, this);
        fTextField481.registerComponent(TOTAL_TI_C, this);
        fTextField5511.registerComponent(TOTAL_AI_P, this);
        fTextField4811.registerComponent(TOTAL_TI_P, this);

        fTextField110.registerComponent(CASH_ASSET_AA_C, this);
        fTextField111.registerComponent(CASH_ASSET_AA_P, this);

        fTextField210.registerComponent(MAN_FUNDS_AA_C, this);
        fTextField211.registerComponent(MAN_FUNDS_AA_P, this);

        fTextField310.registerComponent(SHARES_AA_C, this);
        fTextField312.registerComponent(SHARES_AA_P, this);

        fTextField410.registerComponent(OTHER_INV_AA_C, this);
        fTextField412.registerComponent(OTHER_INV_AA_P, this);

        fTextField510.registerComponent(DEEMED_SUP_AA_C, this);
        fTextField512.registerComponent(DEEMED_SUP_AA_P, this);

        fTextField65.registerComponent(STA_AA_C, this);
        fTextField66.registerComponent(STA_AA_P, this);

        fTextField351.registerComponent(AP_AA_C, this);
        fTextField352.registerComponent(AP_AA_P, this);

        fTextField361.registerComponent(ANN_AA_C, this);
        fTextField362.registerComponent(ANN_AA_P, this);

        fTextField371.registerComponent(COM_ANN_AA_C, this);
        fTextField372.registerComponent(COM_ANN_AA_P, this);

        fTextField72.registerComponent(INV_PRO_AA_C, this);
        fTextField73.registerComponent(INV_PRO_AA_P, this);

        fTextField81.registerComponent(HC_AA_C, this);
        fTextField441.registerComponent(HC_AA_P, this);

        fTextField91.registerComponent(VBC_AA_C, this);
        fTextField451.registerComponent(VBC_AA_P, this);

        fTextField112.registerComponent(OTHER_PER_AA_C, this);
        fTextField521.registerComponent(OTHER_PER_AA_P, this);

        fTextField121.registerComponent(GA_AA_C, this);
        fTextField501.registerComponent(GA_AA_P, this);

        fTextField131.registerComponent(LOANS_AA_C, this);
        fTextField541.registerComponent(LOANS_AA_P, this);

        fTextField471.registerComponent(TOTAL_AA_C, this);
        fTextField542.registerComponent(TOTAL_AA_P, this);

        fTextField291.registerComponent(AP_AI_C, this);
        fTextField2911.registerComponent(AP_AI_P, this);

        fTextField411.registerComponent(AP_TI_C, this);
        fTextField4111.registerComponent(AP_TI_P, this);

        fTextField301.registerComponent(ANN_AI_C, this);
        fTextField3011.registerComponent(ANN_AI_P, this);

        fTextField401.registerComponent(ANN_TI_C, this);
        fTextField4011.registerComponent(ANN_TI_P, this);

        fTextField311.registerComponent(COM_ANN_AI_C, this);
        fTextField3111.registerComponent(COM_ANN_AI_P, this);

        fTextField391.registerComponent(COM_ANN_TI_C, this);
        fTextField3911.registerComponent(COM_ANN_TI_P, this);

        fTextField171.registerComponent(CASH_ASSET_TA_C, this);
        fTextField181.registerComponent(MAN_FUNDS_TA_C, this);
        fTextField191.registerComponent(SHARES_TA_C, this);
        fTextField201.registerComponent(OTHER_INV_TA_C, this);
        fTextField212.registerComponent(DEEMED_SUP_TA_C, this);
        fTextField221.registerComponent(STA_TA_C, this);
        fTextField321.registerComponent(AP_TA_C, this);
        fTextField331.registerComponent(ANN_TA_C, this);
        fTextField341.registerComponent(COM_ANN_TA_C, this);
        fTextField431.registerComponent(INV_PRO_TA_C, this);
        fTextField442.registerComponent(HC_TA_C, this);
        fTextField452.registerComponent(VBC_TA_C, this);
        fTextField522.registerComponent(OTHER_PER_TA_C, this);
        fTextField502.registerComponent(GA_TA_C, this);
        fTextField472.registerComponent(LOANS_TA_C, this);
        fTextField531.registerComponent(TOTAL_TA_C, this);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup11 = new javax.swing.ButtonGroup();
        jPanel14 = new javax.swing.JPanel();
        jPanelCards = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanelClient = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fTextFieldName = new com.argus.bean.FTextField();
        fDateChooser1 = new com.argus.bean.FDateChooser();
        fComboBox1 = new FComboBox(new SexCode().getCodeDescriptions());
        fDateChooser2 = new com.argus.bean.FDateChooser();
        fComboBoxMarital = new FComboBox(new MaritalCode()
                .getCodeDescriptions());
        fTextField64 = new com.argus.bean.FTextField();
        fCheckBox1 = new com.argus.bean.FCheckBox();
        fTextFieldChildren = new com.argus.bean.FTextField();
        jPanelPartner = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        fTextFieldNameP = new com.argus.bean.FTextField();
        fDateChooser3 = new com.argus.bean.FDateChooser();
        fComboBox3 = new FComboBox(new SexCode().getCodeDescriptions());
        fDateChooser4 = new com.argus.bean.FDateChooser();
        jLabel391 = new javax.swing.JLabel();
        fTextField641 = new com.argus.bean.FTextField();
        jPanel27 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        fCheckBox2 = new com.argus.bean.FCheckBox();
        fCheckBox3 = new com.argus.bean.FCheckBox();
        fCheckBox4 = new com.argus.bean.FCheckBox();
        fCheckBox5 = new com.argus.bean.FCheckBox();
        fCheckBox6 = new com.argus.bean.FCheckBox();
        fCheckBox7 = new com.argus.bean.FCheckBox();
        fCheckBox10 = new com.argus.bean.FCheckBox();
        fCheckBox8 = new com.argus.bean.FCheckBox();
        jPanelPartner2 = new javax.swing.JPanel();
        fCheckBox21 = new com.argus.bean.FCheckBox();
        fCheckBox31 = new com.argus.bean.FCheckBox();
        fCheckBox41 = new com.argus.bean.FCheckBox();
        fCheckBox61 = new com.argus.bean.FCheckBox();
        fCheckBox71 = new com.argus.bean.FCheckBox();
        fCheckBox11 = new com.argus.bean.FCheckBox();
        fCheckBox9 = new com.argus.bean.FCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel413 = new javax.swing.JLabel();
        jLabel412 = new javax.swing.JLabel();
        jLabel512 = new javax.swing.JLabel();
        jLabel612 = new javax.swing.JLabel();
        jLabel712 = new javax.swing.JLabel();
        jLabel812 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel411 = new javax.swing.JLabel();
        jLabel511 = new javax.swing.JLabel();
        jLabel611 = new javax.swing.JLabel();
        jLabel711 = new javax.swing.JLabel();
        jLabel811 = new javax.swing.JLabel();
        jPanelClient1 = new javax.swing.JPanel();
        jLabel110 = new javax.swing.JLabel();
        jLabel381 = new javax.swing.JLabel();
        fTextField70 = new com.argus.bean.FTextField();
        fTextField56 = new com.argus.bean.FTextField();
        fTextField58 = new com.argus.bean.FTextField();
        fTextField60 = new com.argus.bean.FTextField();
        fTextField62 = new com.argus.bean.FTextField();
        jLabel382 = new javax.swing.JLabel();
        fTextField701 = new com.argus.bean.FTextField();
        fTextField561 = new com.argus.bean.FTextField();
        fTextField581 = new com.argus.bean.FTextField();
        fTextField601 = new com.argus.bean.FTextField();
        fTextField621 = new com.argus.bean.FTextField();
        jPanelPartner1 = new javax.swing.JPanel();
        jLabel210 = new javax.swing.JLabel();
        jLabel3811 = new javax.swing.JLabel();
        fTextField71 = new com.argus.bean.FTextField();
        fTextField57 = new com.argus.bean.FTextField();
        fTextField59 = new com.argus.bean.FTextField();
        fTextField61 = new com.argus.bean.FTextField();
        fTextField63 = new com.argus.bean.FTextField();
        jLabel3821 = new javax.swing.JLabel();
        fTextField711 = new com.argus.bean.FTextField();
        fTextField571 = new com.argus.bean.FTextField();
        fTextField591 = new com.argus.bean.FTextField();
        fTextField611 = new com.argus.bean.FTextField();
        fTextField631 = new com.argus.bean.FTextField();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel29 = new javax.swing.JPanel();
        jPanelLabels = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel271 = new javax.swing.JLabel();
        jPanelClientAsset = new javax.swing.JPanel();
        jLabel121 = new javax.swing.JLabel();
        fTextField110 = new com.argus.bean.FTextField();
        fTextField210 = new com.argus.bean.FTextField();
        fTextField310 = new com.argus.bean.FTextField();
        fTextField410 = new com.argus.bean.FTextField();
        fTextField510 = new com.argus.bean.FTextField();
        fTextField65 = new com.argus.bean.FTextField();
        jButton1 = new javax.swing.JButton();
        fTextField351 = new com.argus.bean.FTextField();
        fTextField361 = new com.argus.bean.FTextField();
        fTextField371 = new com.argus.bean.FTextField();
        fTextField72 = new com.argus.bean.FTextField();
        jLabel442 = new javax.swing.JLabel();
        fTextField81 = new com.argus.bean.FTextField();
        fTextField91 = new com.argus.bean.FTextField();
        fTextField112 = new com.argus.bean.FTextField();
        fTextField121 = new com.argus.bean.FTextField();
        fTextField131 = new com.argus.bean.FTextField();
        fTextField471 = new com.argus.bean.FTextField();
        jPanelClientAssetTest = new javax.swing.JPanel();
        jLabel132 = new javax.swing.JLabel();
        fTextField171 = new com.argus.bean.FTextField();
        fTextField181 = new com.argus.bean.FTextField();
        fTextField191 = new com.argus.bean.FTextField();
        fTextField201 = new com.argus.bean.FTextField();
        fTextField212 = new com.argus.bean.FTextField();
        fTextField221 = new com.argus.bean.FTextField();
        jLabel681 = new javax.swing.JLabel();
        fTextField321 = new com.argus.bean.FTextField();
        fTextField331 = new com.argus.bean.FTextField();
        fTextField341 = new com.argus.bean.FTextField();
        fTextField431 = new com.argus.bean.FTextField();
        jLabel691 = new javax.swing.JLabel();
        fTextField442 = new com.argus.bean.FTextField();
        fTextField452 = new com.argus.bean.FTextField();
        fTextField522 = new com.argus.bean.FTextField();
        fTextField502 = new com.argus.bean.FTextField();
        fTextField472 = new com.argus.bean.FTextField();
        fTextField531 = new com.argus.bean.FTextField();
        jPanelPartnerAsset = new javax.swing.JPanel();
        jLabel131 = new javax.swing.JLabel();
        fTextField111 = new com.argus.bean.FTextField();
        fTextField211 = new com.argus.bean.FTextField();
        fTextField312 = new com.argus.bean.FTextField();
        fTextField412 = new com.argus.bean.FTextField();
        fTextField512 = new com.argus.bean.FTextField();
        fTextField66 = new com.argus.bean.FTextField();
        jButton11 = new javax.swing.JButton();
        fTextField352 = new com.argus.bean.FTextField();
        fTextField362 = new com.argus.bean.FTextField();
        fTextField372 = new com.argus.bean.FTextField();
        fTextField73 = new com.argus.bean.FTextField();
        jLabel446 = new javax.swing.JLabel();
        fTextField441 = new com.argus.bean.FTextField();
        fTextField451 = new com.argus.bean.FTextField();
        fTextField521 = new com.argus.bean.FTextField();
        fTextField501 = new com.argus.bean.FTextField();
        fTextField541 = new com.argus.bean.FTextField();
        fTextField542 = new com.argus.bean.FTextField();
        jPanelCombinedAsset = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        fTextField1 = new com.argus.bean.FTextField();
        fTextField2 = new com.argus.bean.FTextField();
        fTextField3 = new com.argus.bean.FTextField();
        fTextField4 = new com.argus.bean.FTextField();
        fTextField5 = new com.argus.bean.FTextField();
        fTextField6 = new com.argus.bean.FTextField();
        jLabel434 = new javax.swing.JLabel();
        fTextField35 = new com.argus.bean.FTextField();
        fTextField36 = new com.argus.bean.FTextField();
        fTextField37 = new com.argus.bean.FTextField();
        fTextField7 = new com.argus.bean.FTextField();
        jLabel44 = new javax.swing.JLabel();
        fTextField8 = new com.argus.bean.FTextField();
        fTextField9 = new com.argus.bean.FTextField();
        fTextField11 = new com.argus.bean.FTextField();
        fTextField12 = new com.argus.bean.FTextField();
        fTextField13 = new com.argus.bean.FTextField();
        fTextField54 = new com.argus.bean.FTextField();
        jPanelCombinedAssetTest = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        fTextField17 = new com.argus.bean.FTextField();
        fTextField18 = new com.argus.bean.FTextField();
        fTextField19 = new com.argus.bean.FTextField();
        fTextField20 = new com.argus.bean.FTextField();
        fTextField21 = new com.argus.bean.FTextField();
        fTextField22 = new com.argus.bean.FTextField();
        jLabel68 = new javax.swing.JLabel();
        fTextField32 = new com.argus.bean.FTextField();
        fTextField33 = new com.argus.bean.FTextField();
        fTextField34 = new com.argus.bean.FTextField();
        fTextField43 = new com.argus.bean.FTextField();
        jLabel69 = new javax.swing.JLabel();
        fTextField44 = new com.argus.bean.FTextField();
        fTextField45 = new com.argus.bean.FTextField();
        fTextField52 = new com.argus.bean.FTextField();
        fTextField50 = new com.argus.bean.FTextField();
        fTextField47 = new com.argus.bean.FTextField();
        fTextField53 = new com.argus.bean.FTextField();
        jPanel30 = new javax.swing.JPanel();
        jPanelLabels1 = new javax.swing.JPanel();
        jLabel401 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel171 = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        jLabel191 = new javax.swing.JLabel();
        jLabel201 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jLabel211 = new javax.swing.JLabel();
        jLabel35111 = new javax.swing.JLabel();
        jLabel231 = new javax.swing.JLabel();
        jLabel241 = new javax.swing.JLabel();
        jLabel251 = new javax.swing.JLabel();
        jLabel261 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel351 = new javax.swing.JLabel();
        jLabel361 = new javax.swing.JLabel();
        jLabel341 = new javax.swing.JLabel();
        jLabel3511 = new javax.swing.JLabel();
        jPanelClientIncome = new javax.swing.JPanel();
        jLabel461 = new javax.swing.JLabel();
        jLabel471 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        fTextField231 = new com.argus.bean.FTextField();
        jLabel481 = new javax.swing.JLabel();
        fTextField241 = new com.argus.bean.FTextField();
        jLabel491 = new javax.swing.JLabel();
        fTextField251 = new com.argus.bean.FTextField();
        jLabel501 = new javax.swing.JLabel();
        fTextField261 = new com.argus.bean.FTextField();
        jLabel521 = new javax.swing.JLabel();
        fTextField271 = new com.argus.bean.FTextField();
        jLabel531 = new javax.swing.JLabel();
        fTextField281 = new com.argus.bean.FTextField();
        jLabel541 = new javax.swing.JLabel();
        jLabel5811 = new javax.swing.JLabel();
        fTextField381 = new com.argus.bean.FTextField();
        jLabel561 = new javax.swing.JLabel();
        jLabel571 = new javax.swing.JLabel();
        fTextField291 = new com.argus.bean.FTextField();
        fTextField411 = new com.argus.bean.FTextField();
        fTextField301 = new com.argus.bean.FTextField();
        fTextField401 = new com.argus.bean.FTextField();
        fTextField311 = new com.argus.bean.FTextField();
        fTextField391 = new com.argus.bean.FTextField();
        fTextField141 = new com.argus.bean.FTextField();
        fTextField421 = new com.argus.bean.FTextField();
        jLabel634 = new javax.swing.JLabel();
        jLabel754 = new javax.swing.JLabel();
        fTextField151 = new com.argus.bean.FTextField();
        fTextField511 = new com.argus.bean.FTextField();
        fTextField161 = new com.argus.bean.FTextField();
        fTextField491 = new com.argus.bean.FTextField();
        fTextField551 = new com.argus.bean.FTextField();
        fTextField481 = new com.argus.bean.FTextField();
        jPanelPartnerIncome = new javax.swing.JPanel();
        jLabel4611 = new javax.swing.JLabel();
        jLabel4711 = new javax.swing.JLabel();
        jLabel1411 = new javax.swing.JLabel();
        jLabel1511 = new javax.swing.JLabel();
        fTextField2311 = new com.argus.bean.FTextField();
        jLabel4811 = new javax.swing.JLabel();
        fTextField2411 = new com.argus.bean.FTextField();
        jLabel4911 = new javax.swing.JLabel();
        fTextField2511 = new com.argus.bean.FTextField();
        jLabel5011 = new javax.swing.JLabel();
        fTextField2611 = new com.argus.bean.FTextField();
        jLabel5211 = new javax.swing.JLabel();
        fTextField2711 = new com.argus.bean.FTextField();
        jLabel5311 = new javax.swing.JLabel();
        fTextField2811 = new com.argus.bean.FTextField();
        jLabel5411 = new javax.swing.JLabel();
        jLabel5812 = new javax.swing.JLabel();
        fTextField3811 = new com.argus.bean.FTextField();
        jLabel5711 = new javax.swing.JLabel();
        jLabel5511 = new javax.swing.JLabel();
        fTextField2911 = new com.argus.bean.FTextField();
        fTextField4111 = new com.argus.bean.FTextField();
        fTextField3011 = new com.argus.bean.FTextField();
        fTextField4011 = new com.argus.bean.FTextField();
        fTextField3111 = new com.argus.bean.FTextField();
        fTextField3911 = new com.argus.bean.FTextField();
        fTextField1411 = new com.argus.bean.FTextField();
        fTextField4211 = new com.argus.bean.FTextField();
        jLabel6341 = new javax.swing.JLabel();
        jLabel7541 = new javax.swing.JLabel();
        fTextField1511 = new com.argus.bean.FTextField();
        fTextField5111 = new com.argus.bean.FTextField();
        fTextField1611 = new com.argus.bean.FTextField();
        fTextField4911 = new com.argus.bean.FTextField();
        fTextField5511 = new com.argus.bean.FTextField();
        fTextField4811 = new com.argus.bean.FTextField();
        jPanelCombinedIncome = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        fTextField23 = new com.argus.bean.FTextField();
        jLabel48 = new javax.swing.JLabel();
        fTextField24 = new com.argus.bean.FTextField();
        jLabel49 = new javax.swing.JLabel();
        fTextField25 = new com.argus.bean.FTextField();
        jLabel50 = new javax.swing.JLabel();
        fTextField26 = new com.argus.bean.FTextField();
        jLabel52 = new javax.swing.JLabel();
        fTextField27 = new com.argus.bean.FTextField();
        jLabel53 = new javax.swing.JLabel();
        fTextField28 = new com.argus.bean.FTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel581 = new javax.swing.JLabel();
        fTextField38 = new com.argus.bean.FTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        fTextField29 = new com.argus.bean.FTextField();
        fTextField41 = new com.argus.bean.FTextField();
        fTextField30 = new com.argus.bean.FTextField();
        fTextField40 = new com.argus.bean.FTextField();
        fTextField31 = new com.argus.bean.FTextField();
        fTextField39 = new com.argus.bean.FTextField();
        fTextField14 = new com.argus.bean.FTextField();
        fTextField42 = new com.argus.bean.FTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        fTextField15 = new com.argus.bean.FTextField();
        fTextField51 = new com.argus.bean.FTextField();
        fTextField16 = new com.argus.bean.FTextField();
        fTextField49 = new com.argus.bean.FTextField();
        fTextField55 = new com.argus.bean.FTextField();
        fTextField48 = new com.argus.bean.FTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jButtonBack = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanelNavigation = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanelImage = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jPanelSteps = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(10,
                10, 10, 10)));
        setPreferredSize(new java.awt.Dimension(732, 510));
        setMaximumSize(new java.awt.Dimension(800, 600));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelCards.setLayout(new java.awt.CardLayout());

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel20.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 15, 1, 15)));
        jPanel20.setPreferredSize(new java.awt.Dimension(0, 150));
        jPanel20.setMinimumSize(new java.awt.Dimension(0, 50));
        jLabel90
                .setText("<HTML>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\n<P>\nYou may use this calculator for an existing client / partner or as a standalone tool.\n</P>\n<p>\n<P>\nThe figures used in the Centrelink Calculator are based on rates and threshold announced by Centrelink and do change regularly. We will update the underlying assumptions when these thresholds change. Please ensure you have the latest available version before proceeding.\n</P>\n<p>\n<P>\n<b>Remember</b> - Centrelink calculations are estimates only and should therefore only be used as a guide. Centrelink will make the final assessment as to the level of Centrelink payments a recipient receives. \n</P>\n<P>\n<P>\nPlease check that all fields below are completed before proceeding.\n</P>\n</FONT>\n</HTML>");
        jPanel20.add(jLabel90, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel20);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2,
                javax.swing.BoxLayout.X_AXIS));

        jPanel2.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 200));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 210));
        jPanel4.setLayout(new java.awt.GridLayout(9, 1, 0, 5));

        jPanel4.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 0, 10)));
        jPanel4.setPreferredSize(new java.awt.Dimension(180, 100));
        jPanel4.setMinimumSize(new java.awt.Dimension(80, 100));
        jPanel4.setMaximumSize(new java.awt.Dimension(180, 32767));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel3);

        jLabel4.setText("Name");
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel4);

        jLabel5.setText("DOB");
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel5);

        jLabel6.setText("Sex");
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel6);

        jLabel7.setText("Calculation Date");
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel7);

        jLabel8.setText("Marital Status");
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel8);

        jLabel39.setText("Age");
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel39);

        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel87);

        jLabel9.setText("No. of Children");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel4.add(jLabel9);

        jPanel2.add(jPanel4);

        jPanelClient.setLayout(new java.awt.GridLayout(9, 1));

        jPanelClient.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 0, 0, 0)));
        jPanelClient.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanelClient.setMinimumSize(new java.awt.Dimension(130, 100));
        jPanelClient.setMaximumSize(new java.awt.Dimension(200, 32767));
        jLabel1.setText("ClientView");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelClient.add(jLabel1);

        fTextFieldName.setLayout(new java.awt.FlowLayout());

        fTextFieldName.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextFieldName.setMaximumSize(new java.awt.Dimension(2147483647, 21));
        fTextFieldName.setNextFocusableComponent(fDateChooser1);
        jPanelClient.add(fTextFieldName);

        fDateChooser1.setNextFocusableComponent(fComboBox1);
        jPanelClient.add(fDateChooser1);

        fComboBox1.setNextFocusableComponent(fDateChooser2);
        jPanelClient.add(fComboBox1);

        fDateChooser2.setNextFocusableComponent(fComboBoxMarital);
        jPanelClient.add(fDateChooser2);

        fComboBoxMarital.setNextFocusableComponent(fCheckBox1);
        fComboBoxMarital.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fComboBoxMaritalItemStateChanged(evt);
            }
        });

        jPanelClient.add(fComboBoxMarital);

        fTextField64.setEditable(false);
        fTextField64.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField64.setPreferredSize(new java.awt.Dimension(40, 21));
        jPanelClient.add(fTextField64);

        fCheckBox1.setText("Home Owner");
        fCheckBox1.setNextFocusableComponent(fTextFieldChildren);
        jPanelClient.add(fCheckBox1);

        fTextFieldChildren
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextFieldChildren.setPreferredSize(new java.awt.Dimension(50, 21));
        fTextFieldChildren.setInputVerifier(IntegerInputVerifier.getInstance());
        fTextFieldChildren.setNextFocusableComponent(jTextField1);
        jPanelClient.add(fTextFieldChildren);

        jPanel2.add(jPanelClient);

        jPanelPartner.setLayout(new java.awt.GridLayout(9, 1));

        jPanelPartner.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 10, 0, 0)));
        jPanelPartner.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanelPartner.setMinimumSize(new java.awt.Dimension(136, 100));
        jLabel2.setText("Partner");
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelPartner.add(jLabel2);

        fTextFieldNameP.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jPanelPartner.add(fTextFieldNameP);

        jPanelPartner.add(fDateChooser3);

        jPanelPartner.add(fComboBox3);

        fDateChooser4.setNextFocusableComponent(jTextField1);
        jPanelPartner.add(fDateChooser4);

        jPanelPartner.add(jLabel391);

        fTextField641.setEditable(false);
        fTextField641.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField641.setPreferredSize(new java.awt.Dimension(40, 21));
        jPanelPartner.add(fTextField641);

        jPanel2.add(jPanelPartner);

        jPanel27.setLayout(new java.awt.BorderLayout());

        jPanel27.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 15, 1, 15)));
        jPanel27.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel27.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanel27.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jPanel2.add(jPanel27);

        jPanel3.add(jPanel2);

        jPanel22.setLayout(new java.awt.BorderLayout());

        jPanel22.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 15, 1, 15)));
        jPanel22.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel22.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanel22.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jPanel3.add(jPanel22);

        jPanelCards.add(jPanel3, "card1");

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel16.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 15, 1, 15)));
        jPanel16.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel16.setMinimumSize(new java.awt.Dimension(0, 50));
        jLabel91
                .setText("<HTML>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\n<P>\nThis screen includes a list of the current benefit assessment options available within the Centrelink Calculator.\nOnly those Centrelink benefits that your client or partner initially qualify for are displayed on the list of available benefits. If they do not qualify (based on criteria such as age), then the benefit will not be selectable from the list. \n</P>\n\n<P>\n</P>\n<P>\nPlease select the Centrelink benefit that you wish to calculate from the list below. \n</P>\n</FONT>\n</HTML>");
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel16.add(jLabel91, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel16);

        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel26.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel26.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanel26.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel11.add(jPanel26);

        jPanel17.setMinimumSize(new java.awt.Dimension(10, 200));
        jPanel10.setLayout(new java.awt.GridLayout(8, 1));

        jPanel10.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10,
                        10, 10)), "ClientView "));
        fCheckBox2.setText("Age Pension");
        buttonGroup1.add(fCheckBox2);
        jPanel10.add(fCheckBox2);

        fCheckBox3.setText("Newstart Allowance");
        buttonGroup1.add(fCheckBox3);
        jPanel10.add(fCheckBox3);

        fCheckBox4.setText("Disability Support Pension");
        buttonGroup1.add(fCheckBox4);
        jPanel10.add(fCheckBox4);

        fCheckBox5.setText("Widow Allowance");
        buttonGroup1.add(fCheckBox5);
        jPanel10.add(fCheckBox5);

        fCheckBox6.setText("Sickness Allowance");
        buttonGroup1.add(fCheckBox6);
        jPanel10.add(fCheckBox6);

        fCheckBox7.setText("Carer Payment");
        buttonGroup1.add(fCheckBox7);
        jPanel10.add(fCheckBox7);

        fCheckBox10.setText("Partner Allowance");
        buttonGroup1.add(fCheckBox10);
        jPanel10.add(fCheckBox10);

        fCheckBox8.setText("None");
        buttonGroup1.add(fCheckBox8);
        jPanel10.add(fCheckBox8);

        jPanel17.add(jPanel10);

        jPanelPartner2.setLayout(new java.awt.GridLayout(8, 1));

        jPanelPartner2.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(10, 10,
                        10, 10)), "Partner"));
        fCheckBox21.setText("Age Pension");
        buttonGroup11.add(fCheckBox21);
        jPanelPartner2.add(fCheckBox21);

        fCheckBox31.setText("Newstart Allowance");
        buttonGroup11.add(fCheckBox31);
        jPanelPartner2.add(fCheckBox31);

        fCheckBox41.setText("Disability Support Pension");
        buttonGroup11.add(fCheckBox41);
        jPanelPartner2.add(fCheckBox41);

        fCheckBox61.setText("Sickness Allowance");
        buttonGroup11.add(fCheckBox61);
        jPanelPartner2.add(fCheckBox61);

        fCheckBox71.setText("Carer Payment ");
        buttonGroup11.add(fCheckBox71);
        jPanelPartner2.add(fCheckBox71);

        fCheckBox11.setText("Partner Allowance");
        buttonGroup11.add(fCheckBox11);
        jPanelPartner2.add(fCheckBox11);

        fCheckBox9.setText("None");
        buttonGroup11.add(fCheckBox9);
        jPanelPartner2.add(fCheckBox9);

        jPanel17.add(jPanelPartner2);

        jPanel11.add(jPanel17);

        jPanelCards.add(jPanel11, "card2");

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel25.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 15, 1, 15)));
        jPanel25.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel25.setMinimumSize(new java.awt.Dimension(0, 50));
        jLabel89
                .setText("<HTML>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\n<P>\nResults are based on the preceding means test screen. Please ensure that the details input on the means test screen match the client and partners financial situation.\n</P>\n<P>\n<P>\nCentrelink payment results are estimated below. They are displayed fortnightly and annually. \n</P>\n</FONT>\n</HTML>");
        jPanel25.add(jLabel89, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel25);

        jPanel21.setLayout(new java.awt.GridBagLayout());

        jPanel61.setLayout(new java.awt.GridLayout(1, 3));

        jPanel61.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        jPanel41.setLayout(new java.awt.GridLayout(13, 1, 0, 1));

        jPanel41.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 0, 10)));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel41.add(jLabel38);

        jLabel413
                .setText("<HTML>\n<I>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nFortnight Payment\n</FONT>\n</B>\n</I>\n</HTML>");
        jPanel41.add(jLabel413);

        jLabel412.setText("Max Benefit");
        jPanel41.add(jLabel412);

        jLabel512.setText("Asset Test Result");
        jPanel41.add(jLabel512);

        jLabel612.setText("Income Test Result");
        jPanel41.add(jLabel612);

        jLabel712
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nBasic Benefit\n</FONT>\n</B>\n</HTML>");
        jPanel41.add(jLabel712);

        jLabel812.setText("Pharmaceutical Allowance");
        jPanel41.add(jLabel812);

        jLabel41
                .setText("<HTML>\n<B>\n<I>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nAnnual Payment\n</FONT>\n</I>\n</B>\n</HTML>");
        jPanel41.add(jLabel41);

        jLabel411.setText("Max Benefit");
        jPanel41.add(jLabel411);

        jLabel511.setText("Asset Test Result");
        jPanel41.add(jLabel511);

        jLabel611.setText("Income Test Result");
        jPanel41.add(jLabel611);

        jLabel711
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nBasic Benefit\n</FONT>\n</B>\n</HTML>");
        jPanel41.add(jLabel711);

        jLabel811.setText("Pharmaceutical Allowance");
        jPanel41.add(jLabel811);

        jPanel61.add(jPanel41);

        jPanelClient1.setLayout(new java.awt.GridLayout(13, 1));

        jPanelClient1.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 0, 0, 0)));
        jLabel110.setText("ClientView");
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelClient1.add(jLabel110);

        jLabel381.setForeground(new java.awt.Color(102, 102, 153));
        jLabel381.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelClient1.add(jLabel381);

        fTextField70.setLayout(new java.awt.FlowLayout());

        fTextField70.setEditable(false);
        fTextField70.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField70.setDisabledTextColor(java.awt.Color.black);
        fTextField70.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField70.setEnabled(false);
        jPanelClient1.add(fTextField70);

        fTextField56.setEditable(false);
        fTextField56.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField56.setDisabledTextColor(java.awt.Color.black);
        fTextField56.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField56.setEnabled(false);
        jPanelClient1.add(fTextField56);

        fTextField58.setEditable(false);
        fTextField58.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField58.setDisabledTextColor(java.awt.Color.black);
        fTextField58.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField58.setEnabled(false);
        jPanelClient1.add(fTextField58);

        fTextField60.setEditable(false);
        fTextField60.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField60.setDisabledTextColor(java.awt.Color.black);
        fTextField60.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField60.setEnabled(false);
        jPanelClient1.add(fTextField60);

        fTextField62.setEditable(false);
        fTextField62.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField62.setDisabledTextColor(java.awt.Color.black);
        fTextField62.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField62.setEnabled(false);
        jPanelClient1.add(fTextField62);

        jLabel382.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelClient1.add(jLabel382);

        fTextField701.setLayout(new java.awt.FlowLayout());

        fTextField701.setEditable(false);
        fTextField701.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField701.setDisabledTextColor(java.awt.Color.black);
        fTextField701.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField701.setEnabled(false);
        jPanelClient1.add(fTextField701);

        fTextField561.setEditable(false);
        fTextField561.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField561.setDisabledTextColor(java.awt.Color.black);
        fTextField561.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField561.setEnabled(false);
        jPanelClient1.add(fTextField561);

        fTextField581.setEditable(false);
        fTextField581.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField581.setDisabledTextColor(java.awt.Color.black);
        fTextField581.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField581.setEnabled(false);
        jPanelClient1.add(fTextField581);

        fTextField601.setEditable(false);
        fTextField601.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField601.setDisabledTextColor(java.awt.Color.black);
        fTextField601.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField601.setEnabled(false);
        jPanelClient1.add(fTextField601);

        fTextField621.setEditable(false);
        fTextField621.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField621.setDisabledTextColor(java.awt.Color.black);
        fTextField621.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField621.setEnabled(false);
        jPanelClient1.add(fTextField621);

        jPanel61.add(jPanelClient1);

        jPanelPartner1.setLayout(new java.awt.GridLayout(13, 1));

        jPanelPartner1.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 10, 0, 0)));
        jLabel210.setText("Partner");
        jLabel210.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelPartner1.add(jLabel210);

        jLabel3811.setForeground(new java.awt.Color(102, 102, 153));
        jLabel3811.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelPartner1.add(jLabel3811);

        fTextField71.setEditable(false);
        fTextField71.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField71.setDisabledTextColor(java.awt.Color.black);
        fTextField71.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField71.setEnabled(false);
        jPanelPartner1.add(fTextField71);

        fTextField57.setEditable(false);
        fTextField57.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField57.setDisabledTextColor(java.awt.Color.black);
        fTextField57.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField57.setEnabled(false);
        jPanelPartner1.add(fTextField57);

        fTextField59.setEditable(false);
        fTextField59.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField59.setDisabledTextColor(java.awt.Color.black);
        fTextField59.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField59.setEnabled(false);
        jPanelPartner1.add(fTextField59);

        fTextField61.setEditable(false);
        fTextField61.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField61.setDisabledTextColor(java.awt.Color.black);
        fTextField61.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField61.setEnabled(false);
        jPanelPartner1.add(fTextField61);

        fTextField63.setEditable(false);
        fTextField63.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField63.setDisabledTextColor(java.awt.Color.black);
        fTextField63.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField63.setEnabled(false);
        jPanelPartner1.add(fTextField63);

        jLabel3821.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanelPartner1.add(jLabel3821);

        fTextField711.setEditable(false);
        fTextField711.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField711.setDisabledTextColor(java.awt.Color.black);
        fTextField711.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField711.setEnabled(false);
        jPanelPartner1.add(fTextField711);

        fTextField571.setEditable(false);
        fTextField571.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField571.setDisabledTextColor(java.awt.Color.black);
        fTextField571.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField571.setEnabled(false);
        jPanelPartner1.add(fTextField571);

        fTextField591.setEditable(false);
        fTextField591.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField591.setDisabledTextColor(java.awt.Color.black);
        fTextField591.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField591.setEnabled(false);
        jPanelPartner1.add(fTextField591);

        fTextField611.setEditable(false);
        fTextField611.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField611.setDisabledTextColor(java.awt.Color.black);
        fTextField611.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField611.setEnabled(false);
        jPanelPartner1.add(fTextField611);

        fTextField631.setEditable(false);
        fTextField631.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField631.setDisabledTextColor(java.awt.Color.black);
        fTextField631.setPreferredSize(new java.awt.Dimension(100, 21));
        fTextField631.setEnabled(false);
        jPanelPartner1.add(fTextField631);

        jPanel61.add(jPanelPartner1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel21.add(jPanel61, gridBagConstraints);

        jPanel8.add(jPanel21);

        jPanelCards.add(jPanel8, "card4");

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(5, 250));
        jPanel29.setLayout(new javax.swing.BoxLayout(jPanel29,
                javax.swing.BoxLayout.X_AXIS));

        jPanel29.setPreferredSize(new java.awt.Dimension(10, 300));
        jPanelLabels.setLayout(new java.awt.GridLayout(19, 0));

        jPanelLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 5, 0, 5)));
        jPanelLabels.setPreferredSize(new java.awt.Dimension(150, 300));
        jLabel10
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nDeemed Assets\n</FONT>\n</B>\n</HTML>");
        jLabel10.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel10);

        jLabel17.setText("Cash Assets");
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel17);

        jLabel18.setText("Managed Funds");
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel18);

        jLabel19.setText("Shares");
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel19);

        jLabel20.setText("Other Investments");
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel20);

        jLabel16.setText("Deemed Superannuation");
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel16);

        jLabel21.setText("Short Term Annuities");
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel21);

        jLabel23
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nNon-Deemed Assets\n</FONT>\n</B>\n</HTML>");
        jLabel23.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel23);

        jLabel24.setText("Allocated Pensions");
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel24);

        jLabel25.setText("Annuities");
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel25);

        jLabel26.setText("Annuity(Long Term)");
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel26);

        jLabel11.setText("Investment Property");
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel11);

        jLabel27
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nPersonal Assets\n</FONT>\n</B>\n</HTML>");
        jLabel27.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel27);

        jLabel28.setText("Home Contents");
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel28);

        jLabel30.setText("Vehicles/Boats/Caravans");
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel30);

        jLabel31.setText("Other");
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel31);

        jLabel32.setText("Gifted Amounts");
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel32);

        jLabel33.setText("Loans Owed to Clients");
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel33);

        jLabel271
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nTotal\n</FONT>\n</B>\n</HTML>");
        jLabel271.setPreferredSize(new java.awt.Dimension(85, 17));
        jPanelLabels.add(jLabel271);

        jPanel29.add(jPanelLabels);

        jPanelClientAsset.setLayout(new java.awt.GridLayout(19, 0));

        jPanelClientAsset.setPreferredSize(new java.awt.Dimension(70, 300));
        jLabel121.setText("ClientView Actual");
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelClientAsset.add(jLabel121);

        fTextField110.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField110.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField110.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField110.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField110.setNextFocusableComponent(fTextField210);
        jPanelClientAsset.add(fTextField110);

        fTextField210.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField210.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField210.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField210.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField210.setNextFocusableComponent(fTextField310);
        jPanelClientAsset.add(fTextField210);

        fTextField310.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField310.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField310.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField310.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField310.setNextFocusableComponent(fTextField410);
        jPanelClientAsset.add(fTextField310);

        fTextField410.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField410.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField410.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField410.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField410.setNextFocusableComponent(fTextField510);
        jPanelClientAsset.add(fTextField410);

        fTextField510.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField510.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField510.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField510.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField510.setNextFocusableComponent(fTextField65);
        jPanelClientAsset.add(fTextField510);

        fTextField65.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField65.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField65.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField65.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField65.setNextFocusableComponent(fTextField72);
        jPanelClientAsset.add(fTextField65);

        jButton1.setText("Details");
        jButton1.setPreferredSize(new java.awt.Dimension(70, 17));
        jButton1.setMinimumSize(new java.awt.Dimension(132, 17));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanelClientAsset.add(jButton1);

        fTextField351.setEditable(false);
        fTextField351.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField351.setDisabledTextColor(java.awt.Color.black);
        fTextField351.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField351.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField351.setEnabled(false);
        jPanelClientAsset.add(fTextField351);

        fTextField361.setEditable(false);
        fTextField361.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField361.setDisabledTextColor(java.awt.Color.black);
        fTextField361.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField361.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField361.setEnabled(false);
        jPanelClientAsset.add(fTextField361);

        fTextField371.setEditable(false);
        fTextField371.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField371.setDisabledTextColor(java.awt.Color.black);
        fTextField371.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField371.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField371.setEnabled(false);
        jPanelClientAsset.add(fTextField371);

        fTextField72.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField72.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField72.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField72.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField72.setNextFocusableComponent(fTextField81);
        jPanelClientAsset.add(fTextField72);

        jLabel442.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel442.setMinimumSize(new java.awt.Dimension(107, 17));
        jPanelClientAsset.add(jLabel442);

        fTextField81.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField81.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField81.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField81.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField81.setNextFocusableComponent(fTextField91);
        jPanelClientAsset.add(fTextField81);

        fTextField91.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField91.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField91.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField91.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField91.setNextFocusableComponent(fTextField112);
        jPanelClientAsset.add(fTextField91);

        fTextField112.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField112.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField112.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField112.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField112.setNextFocusableComponent(fTextField121);
        jPanelClientAsset.add(fTextField112);

        fTextField121.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField121.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField121.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField121.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField121.setNextFocusableComponent(fTextField131);
        jPanelClientAsset.add(fTextField121);

        fTextField131.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField131.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField131.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField131.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField131.setNextFocusableComponent(fTextField111);
        jPanelClientAsset.add(fTextField131);

        fTextField471.setEditable(false);
        fTextField471.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField471.setDisabledTextColor(java.awt.Color.black);
        fTextField471.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField471.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField471.setEnabled(false);
        jPanelClientAsset.add(fTextField471);

        jPanel29.add(jPanelClientAsset);

        jPanelClientAssetTest.setLayout(new java.awt.GridLayout(19, 0));

        jPanelClientAssetTest.setPreferredSize(new java.awt.Dimension(70, 340));
        jLabel132.setText("ClientView Test");
        jLabel132.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel132.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelClientAssetTest.add(jLabel132);

        fTextField171.setEditable(false);
        fTextField171.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField171.setDisabledTextColor(java.awt.Color.black);
        fTextField171.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField171.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField171.setEnabled(false);
        jPanelClientAssetTest.add(fTextField171);

        fTextField181.setEditable(false);
        fTextField181.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField181.setDisabledTextColor(java.awt.Color.black);
        fTextField181.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField181.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField181.setEnabled(false);
        jPanelClientAssetTest.add(fTextField181);

        fTextField191.setEditable(false);
        fTextField191.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField191.setDisabledTextColor(java.awt.Color.black);
        fTextField191.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField191.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField191.setEnabled(false);
        jPanelClientAssetTest.add(fTextField191);

        fTextField201.setEditable(false);
        fTextField201.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField201.setDisabledTextColor(java.awt.Color.black);
        fTextField201.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField201.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField201.setEnabled(false);
        jPanelClientAssetTest.add(fTextField201);

        fTextField212.setEditable(false);
        fTextField212.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField212.setDisabledTextColor(java.awt.Color.black);
        fTextField212.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField212.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField212.setEnabled(false);
        jPanelClientAssetTest.add(fTextField212);

        fTextField221.setEditable(false);
        fTextField221.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField221.setDisabledTextColor(java.awt.Color.black);
        fTextField221.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField221.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField221.setEnabled(false);
        jPanelClientAssetTest.add(fTextField221);

        jLabel681.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelClientAssetTest.add(jLabel681);

        fTextField321.setEditable(false);
        fTextField321.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField321.setDisabledTextColor(java.awt.Color.black);
        fTextField321.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField321.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField321.setEnabled(false);
        jPanelClientAssetTest.add(fTextField321);

        fTextField331.setEditable(false);
        fTextField331.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField331.setDisabledTextColor(java.awt.Color.black);
        fTextField331.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField331.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField331.setEnabled(false);
        jPanelClientAssetTest.add(fTextField331);

        fTextField341.setEditable(false);
        fTextField341.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField341.setDisabledTextColor(java.awt.Color.black);
        fTextField341.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField341.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField341.setEnabled(false);
        jPanelClientAssetTest.add(fTextField341);

        fTextField431.setEditable(false);
        fTextField431.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField431.setDisabledTextColor(java.awt.Color.black);
        fTextField431.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField431.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField431.setEnabled(false);
        jPanelClientAssetTest.add(fTextField431);

        jLabel691.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelClientAssetTest.add(jLabel691);

        fTextField442.setEditable(false);
        fTextField442.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField442.setDisabledTextColor(java.awt.Color.black);
        fTextField442.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField442.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField442.setEnabled(false);
        jPanelClientAssetTest.add(fTextField442);

        fTextField452.setEditable(false);
        fTextField452.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField452.setDisabledTextColor(java.awt.Color.black);
        fTextField452.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField452.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField452.setEnabled(false);
        jPanelClientAssetTest.add(fTextField452);

        fTextField522.setEditable(false);
        fTextField522.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField522.setDisabledTextColor(java.awt.Color.black);
        fTextField522.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField522.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField522.setEnabled(false);
        jPanelClientAssetTest.add(fTextField522);

        fTextField502.setEditable(false);
        fTextField502.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField502.setDisabledTextColor(java.awt.Color.black);
        fTextField502.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField502.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField502.setEnabled(false);
        jPanelClientAssetTest.add(fTextField502);

        fTextField472.setEditable(false);
        fTextField472.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField472.setDisabledTextColor(java.awt.Color.black);
        fTextField472.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField472.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField472.setEnabled(false);
        jPanelClientAssetTest.add(fTextField472);

        fTextField531.setEditable(false);
        fTextField531.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField531.setDisabledTextColor(java.awt.Color.black);
        fTextField531.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField531.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField531.setEnabled(false);
        jPanelClientAssetTest.add(fTextField531);

        jPanel29.add(jPanelClientAssetTest);

        jPanelPartnerAsset.setLayout(new java.awt.GridLayout(19, 0));

        jPanelPartnerAsset.setPreferredSize(new java.awt.Dimension(70, 340));
        jLabel131.setText("Partner Actual");
        jLabel131.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel131.setMinimumSize(new java.awt.Dimension(107, 17));
        jPanelPartnerAsset.add(jLabel131);

        fTextField111.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField111.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField111.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField111.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField111.setNextFocusableComponent(fTextField211);
        jPanelPartnerAsset.add(fTextField111);

        fTextField211.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField211.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField211.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField211.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField211.setNextFocusableComponent(fTextField312);
        jPanelPartnerAsset.add(fTextField211);

        fTextField312.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField312.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField312.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField312.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField312.setNextFocusableComponent(fTextField412);
        jPanelPartnerAsset.add(fTextField312);

        fTextField412.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField412.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField412.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField412.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField412.setNextFocusableComponent(fTextField512);
        jPanelPartnerAsset.add(fTextField412);

        fTextField512.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField512.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField512.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField512.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField512.setNextFocusableComponent(fTextField66);
        jPanelPartnerAsset.add(fTextField512);

        fTextField66.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField66.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField66.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField66.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField66.setNextFocusableComponent(fTextField73);
        jPanelPartnerAsset.add(fTextField66);

        jButton11.setText("Details");
        jButton11.setPreferredSize(new java.awt.Dimension(70, 17));
        jButton11.setMinimumSize(new java.awt.Dimension(132, 17));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jPanelPartnerAsset.add(jButton11);

        fTextField352.setEditable(false);
        fTextField352.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField352.setDisabledTextColor(java.awt.Color.black);
        fTextField352.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField352.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField352.setEnabled(false);
        jPanelPartnerAsset.add(fTextField352);

        fTextField362.setEditable(false);
        fTextField362.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField362.setDisabledTextColor(java.awt.Color.black);
        fTextField362.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField362.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField362.setEnabled(false);
        jPanelPartnerAsset.add(fTextField362);

        fTextField372.setEditable(false);
        fTextField372.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField372.setDisabledTextColor(java.awt.Color.black);
        fTextField372.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField372.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField372.setEnabled(false);
        jPanelPartnerAsset.add(fTextField372);

        fTextField73.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField73.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField73.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField73.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField73.setNextFocusableComponent(fTextField441);
        jPanelPartnerAsset.add(fTextField73);

        jLabel446.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel446.setMinimumSize(new java.awt.Dimension(107, 17));
        jPanelPartnerAsset.add(jLabel446);

        fTextField441.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField441.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField441.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField441.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField441.setNextFocusableComponent(fTextField451);
        jPanelPartnerAsset.add(fTextField441);

        fTextField451.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField451.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField451.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField451.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField451.setNextFocusableComponent(fTextField521);
        jPanelPartnerAsset.add(fTextField451);

        fTextField521.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField521.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField521.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField521.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField521.setNextFocusableComponent(fTextField501);
        jPanelPartnerAsset.add(fTextField521);

        fTextField501.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField501.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField501.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField501.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField501.setNextFocusableComponent(fTextField541);
        jPanelPartnerAsset.add(fTextField501);

        fTextField541.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField541.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField541.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField541.setMinimumSize(new java.awt.Dimension(107, 17));
        jPanelPartnerAsset.add(fTextField541);

        fTextField542.setEditable(false);
        fTextField542.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField542.setDisabledTextColor(java.awt.Color.black);
        fTextField542.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField542.setMinimumSize(new java.awt.Dimension(107, 17));
        fTextField542.setEnabled(false);
        jPanelPartnerAsset.add(fTextField542);

        jPanel29.add(jPanelPartnerAsset);

        jPanelCombinedAsset.setLayout(new java.awt.GridLayout(19, 0));

        jPanelCombinedAsset.setPreferredSize(new java.awt.Dimension(70, 300));
        jLabel12.setText("Combined Actual");
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelCombinedAsset.add(jLabel12);

        fTextField1.setEditable(false);
        fTextField1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField1.setDisabledTextColor(java.awt.Color.black);
        fTextField1.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField1.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField1.setEnabled(false);
        fTextField1.setNextFocusableComponent(fTextField2);
        jPanelCombinedAsset.add(fTextField1);

        fTextField2.setEditable(false);
        fTextField2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2.setDisabledTextColor(java.awt.Color.black);
        fTextField2.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField2.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField2.setEnabled(false);
        fTextField2.setNextFocusableComponent(fTextField3);
        jPanelCombinedAsset.add(fTextField2);

        fTextField3.setEditable(false);
        fTextField3.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField3.setDisabledTextColor(java.awt.Color.black);
        fTextField3.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField3.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField3.setEnabled(false);
        fTextField3.setNextFocusableComponent(fTextField4);
        jPanelCombinedAsset.add(fTextField3);

        fTextField4.setEditable(false);
        fTextField4.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField4.setDisabledTextColor(java.awt.Color.black);
        fTextField4.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField4.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField4.setEnabled(false);
        fTextField4.setNextFocusableComponent(fTextField5);
        jPanelCombinedAsset.add(fTextField4);

        fTextField5.setEditable(false);
        fTextField5.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField5.setDisabledTextColor(java.awt.Color.black);
        fTextField5.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField5.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField5.setEnabled(false);
        fTextField5.setNextFocusableComponent(fTextField6);
        jPanelCombinedAsset.add(fTextField5);

        fTextField6.setEditable(false);
        fTextField6.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField6.setDisabledTextColor(java.awt.Color.black);
        fTextField6.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField6.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField6.setEnabled(false);
        fTextField6.setNextFocusableComponent(fTextField7);
        jPanelCombinedAsset.add(fTextField6);

        jLabel434.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel434.setMinimumSize(new java.awt.Dimension(107, 17));
        jPanelCombinedAsset.add(jLabel434);

        fTextField35.setEditable(false);
        fTextField35.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField35.setDisabledTextColor(java.awt.Color.black);
        fTextField35.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField35.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField35.setEnabled(false);
        jPanelCombinedAsset.add(fTextField35);

        fTextField36.setEditable(false);
        fTextField36.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField36.setDisabledTextColor(java.awt.Color.black);
        fTextField36.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField36.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField36.setEnabled(false);
        jPanelCombinedAsset.add(fTextField36);

        fTextField37.setEditable(false);
        fTextField37.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField37.setDisabledTextColor(java.awt.Color.black);
        fTextField37.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField37.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField37.setEnabled(false);
        jPanelCombinedAsset.add(fTextField37);

        fTextField7.setEditable(false);
        fTextField7.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField7.setDisabledTextColor(java.awt.Color.black);
        fTextField7.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField7.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField7.setEnabled(false);
        fTextField7.setNextFocusableComponent(fTextField8);
        jPanelCombinedAsset.add(fTextField7);

        jLabel44.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel44.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelCombinedAsset.add(jLabel44);

        fTextField8.setEditable(false);
        fTextField8.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField8.setDisabledTextColor(java.awt.Color.black);
        fTextField8.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField8.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField8.setEnabled(false);
        fTextField8.setNextFocusableComponent(fTextField9);
        jPanelCombinedAsset.add(fTextField8);

        fTextField9.setEditable(false);
        fTextField9.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField9.setDisabledTextColor(java.awt.Color.black);
        fTextField9.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField9.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField9.setEnabled(false);
        jPanelCombinedAsset.add(fTextField9);

        fTextField11.setEditable(false);
        fTextField11.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField11.setDisabledTextColor(java.awt.Color.black);
        fTextField11.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField11.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField11.setEnabled(false);
        fTextField11.setNextFocusableComponent(fTextField12);
        jPanelCombinedAsset.add(fTextField11);

        fTextField12.setEditable(false);
        fTextField12.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField12.setDisabledTextColor(java.awt.Color.black);
        fTextField12.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField12.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField12.setEnabled(false);
        fTextField12.setNextFocusableComponent(fTextField13);
        jPanelCombinedAsset.add(fTextField12);

        fTextField13.setEditable(false);
        fTextField13.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField13.setDisabledTextColor(java.awt.Color.black);
        fTextField13.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField13.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField13.setEnabled(false);
        fTextField13.setNextFocusableComponent(fTextField110);
        jPanelCombinedAsset.add(fTextField13);

        fTextField54.setEditable(false);
        fTextField54.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField54.setDisabledTextColor(java.awt.Color.black);
        fTextField54.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField54.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField54.setEnabled(false);
        jPanelCombinedAsset.add(fTextField54);

        jPanel29.add(jPanelCombinedAsset);

        jPanelCombinedAssetTest.setLayout(new java.awt.GridLayout(19, 0));

        jPanelCombinedAssetTest
                .setPreferredSize(new java.awt.Dimension(70, 340));
        jLabel13.setText("Combined Test");
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelCombinedAssetTest.add(jLabel13);

        fTextField17.setEditable(false);
        fTextField17.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField17.setDisabledTextColor(java.awt.Color.black);
        fTextField17.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField17.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField17.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField17);

        fTextField18.setEditable(false);
        fTextField18.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField18.setDisabledTextColor(java.awt.Color.black);
        fTextField18.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField18.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField18.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField18);

        fTextField19.setEditable(false);
        fTextField19.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField19.setDisabledTextColor(java.awt.Color.black);
        fTextField19.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField19.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField19.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField19);

        fTextField20.setEditable(false);
        fTextField20.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField20.setDisabledTextColor(java.awt.Color.black);
        fTextField20.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField20.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField20.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField20);

        fTextField21.setEditable(false);
        fTextField21.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField21.setDisabledTextColor(java.awt.Color.black);
        fTextField21.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField21.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField21.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField21);

        fTextField22.setEditable(false);
        fTextField22.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField22.setDisabledTextColor(java.awt.Color.black);
        fTextField22.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField22.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField22.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField22);

        jLabel68.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelCombinedAssetTest.add(jLabel68);

        fTextField32.setEditable(false);
        fTextField32.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField32.setDisabledTextColor(java.awt.Color.black);
        fTextField32.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField32.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField32.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField32);

        fTextField33.setEditable(false);
        fTextField33.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField33.setDisabledTextColor(java.awt.Color.black);
        fTextField33.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField33.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField33.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField33);

        fTextField34.setEditable(false);
        fTextField34.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField34.setDisabledTextColor(java.awt.Color.black);
        fTextField34.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField34.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField34.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField34);

        fTextField43.setEditable(false);
        fTextField43.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField43.setDisabledTextColor(java.awt.Color.black);
        fTextField43.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField43.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField43.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField43);

        jLabel69.setMinimumSize(new java.awt.Dimension(132, 17));
        jPanelCombinedAssetTest.add(jLabel69);

        fTextField44.setEditable(false);
        fTextField44.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField44.setDisabledTextColor(java.awt.Color.black);
        fTextField44.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField44.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField44.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField44);

        fTextField45.setEditable(false);
        fTextField45.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField45.setDisabledTextColor(java.awt.Color.black);
        fTextField45.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField45.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField45.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField45);

        fTextField52.setEditable(false);
        fTextField52.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField52.setDisabledTextColor(java.awt.Color.black);
        fTextField52.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField52.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField52.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField52);

        fTextField50.setEditable(false);
        fTextField50.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField50.setDisabledTextColor(java.awt.Color.black);
        fTextField50.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField50.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField50.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField50);

        fTextField47.setEditable(false);
        fTextField47.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField47.setDisabledTextColor(java.awt.Color.black);
        fTextField47.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField47.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField47.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField47);

        fTextField53.setEditable(false);
        fTextField53.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField53.setDisabledTextColor(java.awt.Color.black);
        fTextField53.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField53.setMinimumSize(new java.awt.Dimension(132, 17));
        fTextField53.setEnabled(false);
        jPanelCombinedAssetTest.add(fTextField53);

        jPanel29.add(jPanelCombinedAssetTest);

        jTabbedPane1.addTab("Asset", jPanel29);

        jPanel30.setLayout(new javax.swing.BoxLayout(jPanel30,
                javax.swing.BoxLayout.X_AXIS));

        jPanelLabels1.setLayout(new java.awt.GridLayout(18, 0));

        jPanelLabels1.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 5, 0, 5)));
        jPanelLabels1.setPreferredSize(new java.awt.Dimension(110, 300));
        jLabel401.setPreferredSize(new java.awt.Dimension(0, 17));
        jLabel401.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel401);

        jLabel101
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nDeemed Assets\n</FONT>\n</B>\n</HTML>");
        jLabel101.setPreferredSize(new java.awt.Dimension(85, 17));
        jLabel101.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel101);

        jLabel171.setText("Cash Assets");
        jLabel171.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel171.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel171);

        jLabel181.setText("Managed Funds");
        jLabel181.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel181.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel181);

        jLabel191.setText("Shares");
        jLabel191.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel191.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel191);

        jLabel201.setText("Other Investments");
        jLabel201.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel201.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel201);

        jLabel161.setText("Deemed Superannuation");
        jLabel161.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel161.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel161);

        jLabel211.setText("Short Term Annuities");
        jLabel211.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel211.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel211);

        jLabel35111
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nTotal Deemed Income\n</FONT>\n</B>\n</HTML>");
        jLabel35111.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel35111);

        jLabel231
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nNon-Deemed Assets\n</FONT>\n</B>\n</HTML>");
        jLabel231.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel231);

        jLabel241.setText("Allocated Pensions");
        jLabel241.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel241.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel241);

        jLabel251.setText("Annuities");
        jLabel251.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel251.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel251);

        jLabel261.setText("Annuity(Long Term)");
        jLabel261.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel261.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel261);

        jLabel111.setText("Investment Property");
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel111.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel111);

        jLabel351
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nIncome\n</FONT>\n</B>\n</HTML>");
        jLabel351.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel351);

        jLabel361.setText("Salary");
        jLabel361.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel361.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel361);

        jLabel341.setText("Other");
        jLabel341.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel341.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel341);

        jLabel3511
                .setText("<HTML>\n<B>\n<FONT face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">\nTotal\n</FONT>\n</B>\n</HTML>");
        jLabel3511.setMinimumSize(new java.awt.Dimension(0, 17));
        jPanelLabels1.add(jLabel3511);

        jPanel30.add(jPanelLabels1);

        jPanelClientIncome.setLayout(new java.awt.GridLayout(18, 0));

        jPanelClientIncome.setPreferredSize(new java.awt.Dimension(100, 300));
        jPanelClientIncome.setMinimumSize(new java.awt.Dimension(73, 300));
        jLabel461.setText("ClientView");
        jLabel461.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jPanelClientIncome.add(jLabel461);

        jLabel471.setText(" Income");
        jPanelClientIncome.add(jLabel471);

        jLabel141.setText("Actual");
        jLabel141.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel141.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel141);

        jLabel151.setText("Test");
        jLabel151.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel151.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel151);

        fTextField231.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField231.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField231.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField231.setMinimumSize(new java.awt.Dimension(4, 17));
        fTextField231.setNextFocusableComponent(fTextField241);
        jPanelClientIncome.add(fTextField231);

        jLabel481.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel481);

        fTextField241.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField241.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField241.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField241.setNextFocusableComponent(fTextField251);
        jPanelClientIncome.add(fTextField241);

        jLabel491.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel491);

        fTextField251.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField251.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField251.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField251.setNextFocusableComponent(fTextField261);
        jPanelClientIncome.add(fTextField251);

        jLabel501.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel501);

        fTextField261.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField261.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField261.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField261.setNextFocusableComponent(fTextField281);
        jPanelClientIncome.add(fTextField261);

        jLabel521.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel521);

        fTextField271.setEditable(false);
        fTextField271.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField271.setDisabledTextColor(java.awt.Color.black);
        fTextField271.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField271.setEnabled(false);
        jPanelClientIncome.add(fTextField271);

        jLabel531.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel531);

        fTextField281.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField281.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField281.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField281.setNextFocusableComponent(fTextField141);
        jPanelClientIncome.add(fTextField281);

        jLabel541.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel541);

        jLabel5811.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel5811);

        fTextField381.setEditable(false);
        fTextField381.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField381.setDisabledTextColor(java.awt.Color.black);
        fTextField381.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField381.setEnabled(false);
        jPanelClientIncome.add(fTextField381);

        jLabel561.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel561);

        jLabel571.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel571);

        fTextField291.setEditable(false);
        fTextField291.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField291.setDisabledTextColor(java.awt.Color.black);
        fTextField291.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField291.setEnabled(false);
        jPanelClientIncome.add(fTextField291);

        fTextField411.setEditable(false);
        fTextField411.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField411.setDisabledTextColor(java.awt.Color.black);
        fTextField411.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField411.setEnabled(false);
        jPanelClientIncome.add(fTextField411);

        fTextField301.setEditable(false);
        fTextField301.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField301.setDisabledTextColor(java.awt.Color.black);
        fTextField301.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField301.setEnabled(false);
        jPanelClientIncome.add(fTextField301);

        fTextField401.setEditable(false);
        fTextField401.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField401.setDisabledTextColor(java.awt.Color.black);
        fTextField401.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField401.setEnabled(false);
        jPanelClientIncome.add(fTextField401);

        fTextField311.setEditable(false);
        fTextField311.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField311.setDisabledTextColor(java.awt.Color.black);
        fTextField311.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField311.setEnabled(false);
        jPanelClientIncome.add(fTextField311);

        fTextField391.setEditable(false);
        fTextField391.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField391.setDisabledTextColor(java.awt.Color.black);
        fTextField391.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField391.setEnabled(false);
        jPanelClientIncome.add(fTextField391);

        fTextField141.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField141.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField141.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField141.setNextFocusableComponent(fTextField151);
        jPanelClientIncome.add(fTextField141);

        fTextField421.setEditable(false);
        fTextField421.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField421.setDisabledTextColor(java.awt.Color.black);
        fTextField421.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField421.setEnabled(false);
        jPanelClientIncome.add(fTextField421);

        jLabel634.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel634);

        jLabel754.setPreferredSize(new java.awt.Dimension(32, 17));
        jPanelClientIncome.add(jLabel754);

        fTextField151.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField151.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField151.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField151.setNextFocusableComponent(fTextField161);
        jPanelClientIncome.add(fTextField151);

        fTextField511.setEditable(false);
        fTextField511.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField511.setDisabledTextColor(java.awt.Color.black);
        fTextField511.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField511.setEnabled(false);
        jPanelClientIncome.add(fTextField511);

        fTextField161.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField161.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField161.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField161.setNextFocusableComponent(fTextField2311);
        jPanelClientIncome.add(fTextField161);

        fTextField491.setEditable(false);
        fTextField491.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField491.setDisabledTextColor(java.awt.Color.black);
        fTextField491.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField491.setEnabled(false);
        jPanelClientIncome.add(fTextField491);

        fTextField551.setEditable(false);
        fTextField551.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField551.setDisabledTextColor(java.awt.Color.black);
        fTextField551.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField551.setEnabled(false);
        jPanelClientIncome.add(fTextField551);

        fTextField481.setEditable(false);
        fTextField481.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField481.setDisabledTextColor(java.awt.Color.black);
        fTextField481.setPreferredSize(new java.awt.Dimension(32, 17));
        fTextField481.setEnabled(false);
        jPanelClientIncome.add(fTextField481);

        jPanel30.add(jPanelClientIncome);

        jPanelPartnerIncome.setLayout(new java.awt.GridLayout(18, 0));

        jPanelPartnerIncome.setPreferredSize(new java.awt.Dimension(100, 300));
        jPanelPartnerIncome.setMinimumSize(new java.awt.Dimension(73, 300));
        jLabel4611.setText("Partner");
        jLabel4611.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jPanelPartnerIncome.add(jLabel4611);

        jLabel4711.setText(" Income");
        jPanelPartnerIncome.add(jLabel4711);

        jLabel1411.setText("Actual");
        jLabel1411.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1411.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel1411);

        jLabel1511.setText("Test");
        jLabel1511.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1511.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel1511);

        fTextField2311.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2311.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2311.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField2311.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2311.setNextFocusableComponent(fTextField2411);
        jPanelPartnerIncome.add(fTextField2311);

        jLabel4811.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel4811);

        fTextField2411.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2411.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2411.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField2411.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2411.setNextFocusableComponent(fTextField2511);
        jPanelPartnerIncome.add(fTextField2411);

        jLabel4911.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel4911);

        fTextField2511.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2511.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2511.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField2511.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2511.setNextFocusableComponent(fTextField2611);
        jPanelPartnerIncome.add(fTextField2511);

        jLabel5011.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5011);

        fTextField2611.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2611.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2611.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField2611.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2611.setNextFocusableComponent(fTextField2811);
        jPanelPartnerIncome.add(fTextField2611);

        jLabel5211.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5211);

        fTextField2711.setEditable(false);
        fTextField2711.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2711.setDisabledTextColor(java.awt.Color.black);
        fTextField2711.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2711.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2711.setEnabled(false);
        jPanelPartnerIncome.add(fTextField2711);

        jLabel5311.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5311);

        fTextField2811.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2811.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2811.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField2811.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2811.setNextFocusableComponent(fTextField1411);
        jPanelPartnerIncome.add(fTextField2811);

        jLabel5411.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5411);

        jLabel5812.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel5812.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5812);

        fTextField3811.setEditable(false);
        fTextField3811.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField3811.setDisabledTextColor(java.awt.Color.black);
        fTextField3811.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField3811.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField3811.setEnabled(false);
        jPanelPartnerIncome.add(fTextField3811);

        jLabel5711.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5711);

        jLabel5511.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel5511);

        fTextField2911.setEditable(false);
        fTextField2911.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField2911.setDisabledTextColor(java.awt.Color.black);
        fTextField2911.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField2911.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField2911.setEnabled(false);
        jPanelPartnerIncome.add(fTextField2911);

        fTextField4111.setEditable(false);
        fTextField4111.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField4111.setDisabledTextColor(java.awt.Color.black);
        fTextField4111.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField4111.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField4111.setEnabled(false);
        jPanelPartnerIncome.add(fTextField4111);

        fTextField3011.setEditable(false);
        fTextField3011.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField3011.setDisabledTextColor(java.awt.Color.black);
        fTextField3011.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField3011.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField3011.setEnabled(false);
        jPanelPartnerIncome.add(fTextField3011);

        fTextField4011.setEditable(false);
        fTextField4011.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField4011.setDisabledTextColor(java.awt.Color.black);
        fTextField4011.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField4011.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField4011.setEnabled(false);
        jPanelPartnerIncome.add(fTextField4011);

        fTextField3111.setEditable(false);
        fTextField3111.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField3111.setDisabledTextColor(java.awt.Color.black);
        fTextField3111.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField3111.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField3111.setEnabled(false);
        jPanelPartnerIncome.add(fTextField3111);

        fTextField3911.setEditable(false);
        fTextField3911.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField3911.setDisabledTextColor(java.awt.Color.black);
        fTextField3911.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField3911.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField3911.setEnabled(false);
        jPanelPartnerIncome.add(fTextField3911);

        fTextField1411.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField1411.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField1411.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField1411.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField1411.setNextFocusableComponent(fTextField1511);
        jPanelPartnerIncome.add(fTextField1411);

        fTextField4211.setEditable(false);
        fTextField4211.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField4211.setDisabledTextColor(java.awt.Color.black);
        fTextField4211.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField4211.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField4211.setEnabled(false);
        jPanelPartnerIncome.add(fTextField4211);

        jLabel6341.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel6341);

        jLabel7541.setMinimumSize(new java.awt.Dimension(43, 17));
        jPanelPartnerIncome.add(jLabel7541);

        fTextField1511.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField1511.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField1511.setInputVerifier(CurrencyInputVerifier.getInstance());
        fTextField1511.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField1511.setNextFocusableComponent(fTextField1611);
        jPanelPartnerIncome.add(fTextField1511);

        fTextField5111.setEditable(false);
        fTextField5111.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField5111.setDisabledTextColor(java.awt.Color.black);
        fTextField5111.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField5111.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField5111.setEnabled(false);
        jPanelPartnerIncome.add(fTextField5111);

        fTextField1611.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField1611.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField1611.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField1611.setNextFocusableComponent(jTextField1);
        jPanelPartnerIncome.add(fTextField1611);

        fTextField4911.setEditable(false);
        fTextField4911.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField4911.setDisabledTextColor(java.awt.Color.black);
        fTextField4911.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField4911.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField4911.setEnabled(false);
        jPanelPartnerIncome.add(fTextField4911);

        fTextField5511.setEditable(false);
        fTextField5511.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField5511.setDisabledTextColor(java.awt.Color.black);
        fTextField5511.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField5511.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField5511.setEnabled(false);
        jPanelPartnerIncome.add(fTextField5511);

        fTextField4811.setEditable(false);
        fTextField4811.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField4811.setDisabledTextColor(java.awt.Color.black);
        fTextField4811.setPreferredSize(new java.awt.Dimension(100, 17));
        fTextField4811.setMinimumSize(new java.awt.Dimension(43, 17));
        fTextField4811.setEnabled(false);
        jPanelPartnerIncome.add(fTextField4811);

        jPanel30.add(jPanelPartnerIncome);

        jPanelCombinedIncome.setLayout(new java.awt.GridLayout(18, 0));

        jPanelCombinedIncome.setPreferredSize(new java.awt.Dimension(100, 300));
        jPanelCombinedIncome.setMinimumSize(new java.awt.Dimension(70, 300));
        jLabel46.setText("Combined");
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel46.setPreferredSize(new java.awt.Dimension(70, 17));
        jPanelCombinedIncome.add(jLabel46);

        jLabel47.setText(" Income");
        jLabel47.setPreferredSize(new java.awt.Dimension(70, 17));
        jPanelCombinedIncome.add(jLabel47);

        jLabel14.setText("Actual");
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel14.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel14);

        jLabel15.setText("Test");
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel15);

        fTextField23.setEditable(false);
        fTextField23.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField23.setDisabledTextColor(java.awt.Color.black);
        fTextField23.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField23.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField23.setEnabled(false);
        jPanelCombinedIncome.add(fTextField23);

        jLabel48.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel48.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel48);

        fTextField24.setEditable(false);
        fTextField24.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField24.setDisabledTextColor(java.awt.Color.black);
        fTextField24.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField24.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField24.setEnabled(false);
        jPanelCombinedIncome.add(fTextField24);

        jLabel49.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel49.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel49);

        fTextField25.setEditable(false);
        fTextField25.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField25.setDisabledTextColor(java.awt.Color.black);
        fTextField25.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField25.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField25.setEnabled(false);
        jPanelCombinedIncome.add(fTextField25);

        jLabel50.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel50.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel50);

        fTextField26.setEditable(false);
        fTextField26.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField26.setDisabledTextColor(java.awt.Color.black);
        fTextField26.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField26.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField26.setEnabled(false);
        fTextField26.setNextFocusableComponent(fTextField28);
        jPanelCombinedIncome.add(fTextField26);

        jLabel52.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel52.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel52);

        fTextField27.setEditable(false);
        fTextField27.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField27.setDisabledTextColor(java.awt.Color.black);
        fTextField27.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField27.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField27.setEnabled(false);
        jPanelCombinedIncome.add(fTextField27);

        jLabel53.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel53.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel53);

        fTextField28.setEditable(false);
        fTextField28.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField28.setDisabledTextColor(java.awt.Color.black);
        fTextField28.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField28.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField28.setEnabled(false);
        fTextField28.setNextFocusableComponent(fTextField14);
        jPanelCombinedIncome.add(fTextField28);

        jLabel54.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel54.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel54);

        jLabel581.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel581.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel581);

        fTextField38.setEditable(false);
        fTextField38.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField38.setDisabledTextColor(java.awt.Color.black);
        fTextField38.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField38.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField38.setEnabled(false);
        jPanelCombinedIncome.add(fTextField38);

        jLabel56.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel56.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel56);

        jLabel57.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel57.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel57);

        fTextField29.setEditable(false);
        fTextField29.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField29.setDisabledTextColor(java.awt.Color.black);
        fTextField29.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField29.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField29.setEnabled(false);
        jPanelCombinedIncome.add(fTextField29);

        fTextField41.setEditable(false);
        fTextField41.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField41.setDisabledTextColor(java.awt.Color.black);
        fTextField41.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField41.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField41.setEnabled(false);
        jPanelCombinedIncome.add(fTextField41);

        fTextField30.setEditable(false);
        fTextField30.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField30.setDisabledTextColor(java.awt.Color.black);
        fTextField30.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField30.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField30.setEnabled(false);
        jPanelCombinedIncome.add(fTextField30);

        fTextField40.setEditable(false);
        fTextField40.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField40.setDisabledTextColor(java.awt.Color.black);
        fTextField40.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField40.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField40.setEnabled(false);
        jPanelCombinedIncome.add(fTextField40);

        fTextField31.setEditable(false);
        fTextField31.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField31.setDisabledTextColor(java.awt.Color.black);
        fTextField31.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField31.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField31.setEnabled(false);
        jPanelCombinedIncome.add(fTextField31);

        fTextField39.setEditable(false);
        fTextField39.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField39.setDisabledTextColor(java.awt.Color.black);
        fTextField39.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField39.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField39.setEnabled(false);
        jPanelCombinedIncome.add(fTextField39);

        fTextField14.setEditable(false);
        fTextField14.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField14.setDisabledTextColor(java.awt.Color.black);
        fTextField14.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField14.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField14.setEnabled(false);
        fTextField14.setNextFocusableComponent(fTextField15);
        jPanelCombinedIncome.add(fTextField14);

        fTextField42.setEditable(false);
        fTextField42.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField42.setDisabledTextColor(java.awt.Color.black);
        fTextField42.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField42.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField42.setEnabled(false);
        jPanelCombinedIncome.add(fTextField42);

        jLabel58.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel58.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel58);

        jLabel70.setPreferredSize(new java.awt.Dimension(70, 17));
        jLabel70.setMinimumSize(new java.awt.Dimension(57, 17));
        jPanelCombinedIncome.add(jLabel70);

        fTextField15.setEditable(false);
        fTextField15.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField15.setDisabledTextColor(java.awt.Color.black);
        fTextField15.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField15.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField15.setEnabled(false);
        fTextField15.setNextFocusableComponent(fTextField16);
        jPanelCombinedIncome.add(fTextField15);

        fTextField51.setEditable(false);
        fTextField51.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField51.setDisabledTextColor(java.awt.Color.black);
        fTextField51.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField51.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField51.setEnabled(false);
        jPanelCombinedIncome.add(fTextField51);

        fTextField16.setEditable(false);
        fTextField16.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField16.setDisabledTextColor(java.awt.Color.black);
        fTextField16.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField16.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField16.setEnabled(false);
        fTextField16.setNextFocusableComponent(fTextField231);
        jPanelCombinedIncome.add(fTextField16);

        fTextField49.setEditable(false);
        fTextField49.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField49.setDisabledTextColor(java.awt.Color.black);
        fTextField49.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField49.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField49.setEnabled(false);
        jPanelCombinedIncome.add(fTextField49);

        fTextField55.setEditable(false);
        fTextField55.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField55.setDisabledTextColor(java.awt.Color.black);
        fTextField55.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField55.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField55.setEnabled(false);
        jPanelCombinedIncome.add(fTextField55);

        fTextField48.setEditable(false);
        fTextField48.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fTextField48.setDisabledTextColor(java.awt.Color.black);
        fTextField48.setPreferredSize(new java.awt.Dimension(70, 17));
        fTextField48.setMinimumSize(new java.awt.Dimension(57, 17));
        fTextField48.setEnabled(false);
        jPanelCombinedIncome.add(fTextField48);

        jPanel30.add(jPanelCombinedIncome);

        jTabbedPane1.addTab("Income", jPanel30);

        jPanel6.add(jTabbedPane1);

        jPanelCards.add(jPanel6, "card3");

        jPanel14.add(jPanelCards);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(
                java.awt.Color.black, null));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 1));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 1));
        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 1));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel18.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel18.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanel1.add(jPanel18, java.awt.BorderLayout.SOUTH);

        jPanel14.add(jPanel1);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1,
                5));

        jPanel9.setPreferredSize(new java.awt.Dimension(578, 41));
        jPanel9.setMinimumSize(new java.awt.Dimension(540, 41));
        jButton4.setText("Report");
        jButton4.setPreferredSize(new java.awt.Dimension(67, 21));
        jButton4.setDefaultCapable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed1(evt);
            }
        });

        jPanel9.add(jButton4);

        jButtonClose.setText("Close");
        jButtonClose.setPreferredSize(new java.awt.Dimension(67, 21));
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel9.add(jButtonClose);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
                1, 5));

        jButton2.setText("Save");
        jButton2.setPreferredSize(new java.awt.Dimension(63, 21));
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel13.add(jButton2);

        jButton3.setText("Save As");
        jButton3.setPreferredSize(new java.awt.Dimension(81, 21));
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel13.add(jButton3);

        jPanel9.add(jPanel13);

        jButtonDelete.setText("Delete");
        jButtonDelete.setPreferredSize(new java.awt.Dimension(65, 21));
        jButtonDelete.setMaximumSize(new java.awt.Dimension(65, 27));
        jButtonDelete.setMinimumSize(new java.awt.Dimension(65, 27));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanel9.add(jButtonDelete);

        jButtonClear.setText("Clear");
        jButtonClear.setPreferredSize(new java.awt.Dimension(65, 21));
        jButtonClear.setDefaultCapable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPanel9.add(jButtonClear);

        jButtonBack.setText("Back");
        jButtonBack.setPreferredSize(new java.awt.Dimension(63, 21));
        jButtonBack.setEnabled(false);
        jPanel9.add(jButtonBack);

        jButtonNext.setText("Next");
        jButtonNext.setPreferredSize(new java.awt.Dimension(61, 21));
        jButtonNext.setEnabled(false);
        jPanel9.add(jButtonNext);

        jPanel12.add(jTextField1);

        jPanel9.add(jPanel12);

        jPanel14.add(jPanel9);

        add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanelNavigation.setLayout(new javax.swing.BoxLayout(jPanelNavigation,
                javax.swing.BoxLayout.X_AXIS));

        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel24.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 5, 1, 1)));
        jPanelImage.setLayout(new java.awt.BorderLayout());

        jLabel86.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/SocialSecurity.jpg")));
        jPanelImage.add(jLabel86, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanelImage, java.awt.BorderLayout.CENTER);

        jPanelSteps.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.BorderLayout());

        jPanel19.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel19.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanelSteps.add(jPanel19, java.awt.BorderLayout.SOUTH);

        jPanel24.add(jPanelSteps, java.awt.BorderLayout.SOUTH);

        jPanelNavigation.add(jPanel24);

        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15,
                javax.swing.BoxLayout.X_AXIS));

        jPanel15.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(0, 5, 1, 1)));
        jPanel15.setPreferredSize(new java.awt.Dimension(10, 1));
        jPanel15.setMinimumSize(new java.awt.Dimension(5, 1));
        jPanel15.setMaximumSize(new java.awt.Dimension(15, 32767));
        jPanel23.setLayout(new java.awt.BorderLayout());

        jPanel23.setBorder(new javax.swing.border.EtchedBorder(
                java.awt.Color.black, null));
        jPanel23.setPreferredSize(new java.awt.Dimension(1, 30000));
        jPanel23.setMinimumSize(new java.awt.Dimension(1, 30000));
        jPanel23.setMaximumSize(new java.awt.Dimension(1, 2147483647));
        jPanel15.add(jPanel23);

        jPanelNavigation.add(jPanel15);

        add(jPanelNavigation, java.awt.BorderLayout.WEST);

        jLabel88.setText("Test Results");
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel88.setFont(new java.awt.Font("Arial", 1, 20));
        add(jLabel88, java.awt.BorderLayout.NORTH);

    }// GEN-END:initComponents

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton11ActionPerformed
        // Add your handling code here:
        SwingUtil.add2Dialog(SwingUtil.getJFrame(this),
                "Non-Deemed Assets Details View Partner", true, dViewP, true,
                true);
        calc.sendNotification(new Object());
    }// GEN-LAST:event_jButton11ActionPerformed

    private void jButton4ActionPerformed1(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed1
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();

    }// GEN-LAST:event_jButton4ActionPerformed1

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        // Add your handling code here:
        doClear(evt);
    }// GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        // Add your handling code here:
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int result = BaseView.closeDialog(this);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // Add your handling code here:
        newModel = true;
        doSave(evt);
    }// GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        // Add your handling code here:
        if (calc.isModified()) {
            newModel = false;
            doSave(evt);
        }

    }// GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        SwingUtil.add2Dialog(SwingUtil.getJFrame(this),
                "Non-Deemed Assets Details View", true, dView, true, true);
        calc.sendNotification(new Object());
    }// GEN-LAST:event_jButton1ActionPerformed

    private void fComboBoxMaritalItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_fComboBoxMaritalItemStateChanged
        // Add your handling code here:
        if (evt.SELECTED != evt.getStateChange())
            return;

        if (fComboBoxMarital.getSelectedIndex() == 2
                || fComboBoxMarital.getSelectedIndex() == 5
                || fComboBoxMarital.getSelectedIndex() == 7
                || fComboBoxMarital.getSelectedIndex() == 8) {
            jPanelPartner.setVisible(false);
            jPanelPartner1.setVisible(false);
            jPanelPartner2.setVisible(false);
            jPanelPartnerAsset.setVisible(false);
            jPanelPartnerIncome.setVisible(false);
            jPanelCombinedAsset.setVisible(false);
            jPanelCombinedAssetTest.setVisible(false);
            jPanelCombinedIncome.setVisible(false);
            jPanelClientAssetTest.setVisible(true);
            fTextField381.setVisible(true);
            fTextField411.setVisible(true);
            fTextField401.setVisible(true);
            fTextField391.setVisible(true);
            fTextField421.setVisible(true);
            fTextField511.setVisible(true);
            fTextField491.setVisible(true);
            fTextField481.setVisible(true);
        } else {
            jPanelPartner.setVisible(true);
            jPanelPartner1.setVisible(true);
            jPanelPartner2.setVisible(true);
            jPanelPartnerAsset.setVisible(true);
            jPanelPartnerIncome.setVisible(true);
            jPanelCombinedAsset.setVisible(true);
            jPanelCombinedAssetTest.setVisible(true);
            jPanelCombinedIncome.setVisible(true);
            jPanelClientAssetTest.setVisible(false);
        }

    }// GEN-LAST:event_fComboBoxMaritalItemStateChanged

    public void messageSent(MessageSentEvent e) {
        if (CLIENT_AGE_PENSION_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox2.isSelected())
                buttonGroup1.setSelected(null, true);

            fCheckBox2.setEnabled(false);

        } else if (PARTNER_AGE_PENSION_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox21.isSelected())
                buttonGroup11.setSelected(null, true);

            fCheckBox21.setEnabled(false);

        } else if (CLIENT_AGE_PENSION_VALID.equals(e.getMessage())) {
            fCheckBox2.setEnabled(true);

        } else if (PARTNER_AGE_PENSION_VALID.equals(e.getMessage())) {
            fCheckBox21.setEnabled(true);

        } else if (CLIENT_NSA_VALID.equals(e.getMessage())) {
            fCheckBox3.setEnabled(true);

        } else if (PARTNER_NSA_VALID.equals(e.getMessage())) {
            fCheckBox31.setEnabled(true);

        } else if (CLIENT_NSA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox3.isSelected())
                buttonGroup1.setSelected(null, true);

            fCheckBox3.setEnabled(false);
        } else if (PARTNER_NSA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox31.isSelected())
                buttonGroup11.setSelected(null, true);

            fCheckBox31.setEnabled(false);
        } else if (CLIENT_DSP_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox4.isSelected())
                buttonGroup1.setSelected(null, true);

            fCheckBox4.setEnabled(false);

        } else if (PARTNER_DSP_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox41.isSelected())
                buttonGroup11.setSelected(null, true);

            fCheckBox41.setEnabled(false);

        } else if (CLIENT_WA_VALID.equals(e.getMessage())) {
            fCheckBox5.setEnabled(true);

        } else if (CLIENT_WA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox5.isSelected())
                buttonGroup1.setSelected(null, true);

            fCheckBox5.setEnabled(false);

        } else if (CLIENT_SA_VALID.equals(e.getMessage())) {
            fCheckBox6.setEnabled(true);

        } else if (CLIENT_SA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox6.isSelected())
                buttonGroup1.setSelected(null, true);

            fCheckBox6.setEnabled(false);

        } else if (PARTNER_SA_VALID.equals(e.getMessage())) {
            fCheckBox61.setEnabled(true);

        } else if (PARTNER_SA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox61.isSelected())
                buttonGroup11.setSelected(null, true);

            fCheckBox61.setEnabled(false);

        } else if (CLIENT_DSP_VALID.equals(e.getMessage())) {
            fCheckBox4.setEnabled(true);

        } else if (PARTNER_DSP_VALID.equals(e.getMessage())) {
            fCheckBox41.setEnabled(true);

        } else if (CLIENT_PA_VALID.equals(e.getMessage())) {
            if (fCheckBox21.isSelected() || fCheckBox31.isSelected()
                    || fCheckBox41.isSelected() || fCheckBox61.isSelected()
                    || fCheckBox71.isSelected())
                fCheckBox10.setEnabled(true);
            else {
                if (fCheckBox10.isSelected())
                    buttonGroup1.setSelected(null, true);
                fCheckBox10.setEnabled(false);

            }

        } else if (PARTNER_PA_VALID.equals(e.getMessage())) {
            if (fCheckBox2.isSelected() || fCheckBox3.isSelected()
                    || fCheckBox4.isSelected() || fCheckBox5.isSelected()
                    || fCheckBox6.isSelected() || fCheckBox7.isSelected())
                fCheckBox11.setEnabled(true);
            else {
                if (fCheckBox11.isSelected())
                    buttonGroup11.setSelected(null, true);
                fCheckBox11.setEnabled(false);
            }

        } else if (CLIENT_PA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox10.isSelected())
                buttonGroup1.setSelected(null, true);

            fCheckBox10.setEnabled(false);
        } else if (PARTNER_PA_EXCEPTION.equals(e.getMessage())) {
            if (fCheckBox11.isSelected())
                buttonGroup11.setSelected(null, true);

            fCheckBox11.setEnabled(false);

        } else {
            if (!INVALID.equals(e.getMessage())) {
                String message = e.getMessage();
                String messageType = e.getMessageType();
                java.awt.Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, message, messageType,
                        JOptionPane.CANCEL_OPTION);
            }
        }

        updatePharC();
        updatePharP();
        calc.setNoClientPhar(noClientPension);
        calc.setNoPartnerPhar(noPartnerPension);

        if (noClientPension && noPartnerPension)
            setPharLableVisible(false);
        else
            setPharLableVisible(true);

        updateDeemedIncomeComponent();
    }

    /***************************************************************************
     * BaseView protected methods
     **************************************************************************/
    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(newModel);
            calc.setModified(false);
            // update menu
            FinancialPlannerApp.getInstance().updateModels();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.argus.bean.FTextField fTextField291;

    private javax.swing.JLabel jLabel3511;

    private javax.swing.JLabel jLabel261;

    private com.argus.bean.FTextField fTextField341;

    private javax.swing.JPanel jPanel41;

    private com.argus.bean.FTextField fTextField281;

    private javax.swing.JButton jButtonClear;

    private com.argus.bean.FTextField fTextField4111;

    private javax.swing.JLabel jLabel251;

    private com.argus.bean.FTextField fTextField331;

    private javax.swing.JPanel jPanel30;

    private javax.swing.JLabel jLabel5411;

    private javax.swing.JLabel jLabel191;

    private com.argus.bean.FTextField fTextField2711;

    private com.argus.bean.FTextField fTextField271;

    private javax.swing.JPanel jPanelPartner2;

    private javax.swing.JPanel jPanelPartner1;

    private javax.swing.JPanel jPanel29;

    private javax.swing.JLabel jLabel241;

    private com.argus.bean.FTextField fTextField321;

    private javax.swing.JPanel jPanel27;

    private javax.swing.JLabel jLabel4711;

    private javax.swing.JPanel jPanel26;

    private javax.swing.JPanel jPanel25;

    private javax.swing.JPanel jPanel24;

    private javax.swing.JPanel jPanel23;

    private javax.swing.JPanel jPanel22;

    private javax.swing.JPanel jPanel21;

    private javax.swing.JPanel jPanel20;

    private javax.swing.JLabel jLabel1511;

    private javax.swing.JLabel jLabel181;

    private com.argus.bean.FTextField fTextField261;

    private javax.swing.JPanel jPanelClient;

    private javax.swing.JPanel jPanel19;

    private com.argus.bean.FCheckBox fCheckBox9;

    private com.argus.bean.FTextField fTextField312;

    private javax.swing.JLabel jLabel231;

    private javax.swing.JLabel jLabel812;

    private javax.swing.JPanel jPanel18;

    private com.argus.bean.FCheckBox fCheckBox8;

    private com.argus.bean.FTextField fTextField311;

    private javax.swing.JLabel jLabel811;

    private com.argus.bean.FCheckBox fCheckBox7;

    private com.argus.bean.FTextField fTextField310;

    private javax.swing.JPanel jPanel17;

    private com.argus.bean.FCheckBox fCheckBox6;

    private javax.swing.JPanel jPanel16;

    private javax.swing.JPanel jPanel15;

    private com.argus.bean.FCheckBox fCheckBox5;

    private com.argus.bean.FCheckBox fCheckBox4;

    private javax.swing.JPanel jPanel14;

    private javax.swing.JPanel jPanel13;

    private com.argus.bean.FCheckBox fCheckBox3;

    private javax.swing.JPanel jPanel12;

    private com.argus.bean.FCheckBox fCheckBox2;

    private com.argus.bean.FCheckBox fCheckBox1;

    private javax.swing.JPanel jPanel11;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JLabel jLabel754;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel171;

    private javax.swing.JLabel jLabel7;

    private com.argus.bean.FTextField fTextField251;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanelPartner;

    private com.argus.bean.FTextField fTextField301;

    private com.argus.bean.FTextField fTextField3911;

    private com.argus.bean.FTextField fTextField1411;

    private com.argus.bean.FTextField fTextField191;

    private javax.swing.JLabel jLabel691;

    private com.argus.bean.FTextField fTextFieldChildren;

    private javax.swing.JLabel jLabel161;

    private com.argus.bean.FTextField fTextField241;

    private javax.swing.JLabel jLabel211;

    private javax.swing.JLabel jLabel210;

    private com.argus.bean.FCheckBox fCheckBox71;

    private com.argus.bean.FTextField fTextField181;

    private javax.swing.JLabel jLabel681;

    private com.argus.bean.FTextField fTextField4011;

    private javax.swing.JLabel jLabel151;

    private com.argus.bean.FTextField fTextField231;

    private javax.swing.JPanel jPanelClientIncome;

    private javax.swing.JPanel jPanelImage;

    private javax.swing.JLabel jLabel201;

    private com.argus.bean.FCheckBox fCheckBox61;

    private javax.swing.JLabel jLabel5311;

    private com.argus.bean.FTextField fTextField2611;

    private com.argus.bean.FTextField fTextField171;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JLabel jLabel141;

    private com.argus.bean.FTextField fTextField221;

    private javax.swing.JLabel jLabel4611;

    private javax.swing.JPanel jPanelClientAssetTest;

    private javax.swing.JLabel jLabel1411;

    private com.argus.bean.FTextField fTextField161;

    private javax.swing.JLabel jLabel132;

    private javax.swing.JPanel jPanelPartnerIncome;

    private com.argus.bean.FTextField fTextField212;

    private javax.swing.JLabel jLabel131;

    private javax.swing.JLabel jLabel712;

    private com.argus.bean.FTextField fTextField211;

    private javax.swing.JLabel jLabel711;

    private com.argus.bean.FTextField fTextField210;

    private com.argus.bean.FDateChooser fDateChooser4;

    private com.argus.bean.FDateChooser fDateChooser3;

    private com.argus.bean.FDateChooser fDateChooser2;

    private com.argus.bean.FDateChooser fDateChooser1;

    private com.argus.bean.FCheckBox fCheckBox41;

    private com.argus.bean.FTextField fTextField151;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JLabel jLabel121;

    private com.argus.bean.FTextField fTextField201;

    private javax.swing.JPanel jPanel6;

    private com.argus.bean.FTextField fTextField3811;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel jLabel5812;

    private javax.swing.JLabel jLabel5811;

    private com.argus.bean.FCheckBox fCheckBox31;

    private com.argus.bean.FTextField fTextField141;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JLabel jLabel111;

    private javax.swing.JLabel jLabel110;

    private javax.swing.JButton jButtonNext;

    private com.argus.bean.FCheckBox fCheckBox21;

    private javax.swing.JLabel jLabel581;

    private javax.swing.JLabel jLabel634;

    private com.argus.bean.FTextField fTextField131;

    private com.argus.bean.FTextField fTextField711;

    private javax.swing.JLabel jLabel101;

    private javax.swing.JLabel jLabel3821;

    private javax.swing.JLabel jLabel5211;

    private com.argus.bean.FCheckBox fCheckBox11;

    private com.argus.bean.FCheckBox fCheckBox10;

    private javax.swing.JLabel jLabel571;

    private com.argus.bean.FTextField fTextField2511;

    private com.argus.bean.FTextField fTextField121;

    private com.argus.bean.FTextField fTextField701;

    private javax.swing.JLabel jLabel91;

    private javax.swing.JLabel jLabel90;

    private com.argus.bean.FTextField fTextField591;

    private javax.swing.JLabel jLabel3811;

    private javax.swing.JLabel jLabel561;

    private com.argus.bean.FTextField fTextField641;

    private javax.swing.JLabel jLabel89;

    private javax.swing.JLabel jLabel88;

    private javax.swing.JLabel jLabel87;

    private javax.swing.JLabel jLabel86;

    private com.argus.bean.FTextField fTextField112;

    private javax.swing.JLabel jLabel612;

    private com.argus.bean.FTextField fTextField111;

    private javax.swing.JLabel jLabel611;

    private com.argus.bean.FTextField fTextField110;

    private com.argus.bean.FTextField fTextField5111;

    private com.argus.bean.FTextField fTextField91;

    private com.argus.bean.FTextField fTextField581;

    private javax.swing.JPanel jPanelClient1;

    private com.argus.bean.FTextField fTextField631;

    private javax.swing.JLabel jLabel70;

    private com.argus.bean.FTextField fTextField81;

    private javax.swing.JLabel jLabel5711;

    private javax.swing.JLabel jLabel491;

    private com.argus.bean.FTextField fTextField571;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JLabel jLabel541;

    private com.argus.bean.FTextField fTextField621;

    private javax.swing.ButtonGroup buttonGroup11;

    private javax.swing.JLabel jLabel69;

    private javax.swing.JButton jButtonBack;

    private javax.swing.JLabel jLabel68;

    private com.argus.bean.FTextField fTextFieldNameP;

    private com.argus.bean.FTextField fTextField73;

    private com.argus.bean.FTextField fTextField72;

    private com.argus.bean.FTextField fTextField71;

    private com.argus.bean.FTextField fTextField70;

    private javax.swing.JLabel jLabel481;

    private com.argus.bean.FTextField fTextField561;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JLabel jLabel6341;

    private javax.swing.JLabel jLabel531;

    private com.argus.bean.FTextField fTextField611;

    private javax.swing.JLabel jLabel58;

    private javax.swing.JLabel jLabel35111;

    private javax.swing.JLabel jLabel57;

    private javax.swing.JLabel jLabel56;

    private javax.swing.JPanel jPanelNavigation;

    private javax.swing.JLabel jLabel54;

    private com.argus.bean.FTextField fTextField3111;

    private javax.swing.JLabel jLabel53;

    private javax.swing.JLabel jLabel52;

    private com.argus.bean.FTextField fTextField66;

    private com.argus.bean.FTextField fTextField65;

    private javax.swing.JLabel jLabel50;

    private com.argus.bean.FTextField fTextField64;

    private com.argus.bean.FTextField fTextField63;

    private com.argus.bean.FTextField fTextField62;

    private com.argus.bean.FComboBox fComboBoxMarital;

    private com.argus.bean.FTextField fTextField61;

    private javax.swing.JPanel jPanelLabels1;

    private com.argus.bean.FTextField fTextField60;

    private com.argus.bean.FTextField fTextField4911;

    private com.argus.bean.FTextField fTextField2411;

    private javax.swing.JLabel jLabel471;

    private com.argus.bean.FTextField fTextField551;

    private javax.swing.JLabel jLabel521;

    private com.argus.bean.FTextField fTextField601;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel49;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel48;

    private javax.swing.JLabel jLabel47;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel46;

    private javax.swing.JPanel jPanelLabels;

    private com.argus.bean.FTextField fTextField59;

    private com.argus.bean.FTextField fTextField58;

    private javax.swing.JLabel jLabel44;

    private com.argus.bean.FTextField fTextField57;

    private com.argus.bean.FTextField fTextField56;

    private com.argus.bean.FTextField fTextField55;

    private javax.swing.JLabel jLabel41;

    private com.argus.bean.FTextField fTextField54;

    private com.argus.bean.FTextField fTextField53;

    private com.argus.bean.FTextField fTextField491;

    private com.argus.bean.FTextField fTextField52;

    private com.argus.bean.FTextField fTextField51;

    private com.argus.bean.FTextField fTextField50;

    private com.argus.bean.FTextField fTextField542;

    private javax.swing.JLabel jLabel461;

    private com.argus.bean.FTextField fTextField541;

    private com.argus.bean.FTextField fTextField9;

    private com.argus.bean.FTextField fTextField8;

    private com.argus.bean.FTextField fTextField7;

    private com.argus.bean.FTextField fTextField6;

    private javax.swing.JLabel jLabel512;

    private com.argus.bean.FTextField fTextField5;

    private javax.swing.JLabel jLabel511;

    private com.argus.bean.FTextField fTextField4;

    private com.argus.bean.FTextField fTextField3;

    private com.argus.bean.FTextField fTextField2;

    private javax.swing.JLabel jLabel39;

    private com.argus.bean.FTextField fTextField1;

    private javax.swing.JLabel jLabel38;

    private com.argus.bean.FTextField fTextField49;

    private javax.swing.JLabel jLabel7541;

    private com.argus.bean.FTextField fTextField48;

    private javax.swing.JLabel jLabel33;

    private com.argus.bean.FTextField fTextField47;

    private javax.swing.JLabel jLabel32;

    private javax.swing.JLabel jLabel31;

    private com.argus.bean.FTextField fTextField45;

    private javax.swing.JLabel jLabel30;

    private com.argus.bean.FTextField fTextField44;

    private com.argus.bean.FTextField fTextField481;

    private com.argus.bean.FTextField fTextField43;

    private com.argus.bean.FTextField fTextField42;

    private com.argus.bean.FTextField fTextField41;

    private com.argus.bean.FTextField fTextField40;

    private com.argus.bean.FTextField fTextField531;

    private javax.swing.JPanel jPanelClientAsset;

    private javax.swing.JLabel jLabel501;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JLabel jLabel26;

    private com.argus.bean.FTextField fTextField39;

    private javax.swing.JLabel jLabel25;

    private com.argus.bean.FTextField fTextField38;

    private javax.swing.JLabel jLabel24;

    private com.argus.bean.FTextField fTextFieldName;

    private javax.swing.JLabel jLabel23;

    private com.argus.bean.FTextField fTextField37;

    private com.argus.bean.FTextField fTextField36;

    private javax.swing.JLabel jLabel21;

    private com.argus.bean.FTextField fTextField35;

    private javax.swing.JLabel jLabel20;

    private com.argus.bean.FTextField fTextField472;

    private javax.swing.JLabel jLabel391;

    private com.argus.bean.FTextField fTextField34;

    private com.argus.bean.FTextField fTextField2911;

    private com.argus.bean.FTextField fTextField471;

    private com.argus.bean.FTextField fTextField33;

    private com.argus.bean.FTextField fTextField32;

    private com.argus.bean.FTextField fTextField31;

    private com.argus.bean.FTextField fTextField30;

    private javax.swing.JLabel jLabel446;

    private javax.swing.JLabel jLabel442;

    private com.argus.bean.FTextField fTextField522;

    private com.argus.bean.FTextField fTextField521;

    private javax.swing.JLabel jLabel4911;

    private javax.swing.JPanel jPanelSteps;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel16;

    private com.argus.bean.FTextField fTextField29;

    private javax.swing.JLabel jLabel15;

    private com.argus.bean.FTextField fTextField28;

    private javax.swing.JLabel jLabel14;

    private com.argus.bean.FTextField fTextField27;

    private javax.swing.JLabel jLabel13;

    private com.argus.bean.FTextField fTextField26;

    private javax.swing.JLabel jLabel12;

    private com.argus.bean.FTextField fTextField25;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel382;

    private com.argus.bean.FTextField fTextField24;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel381;

    private com.argus.bean.FTextField fTextField23;

    private com.argus.bean.FTextField fTextField22;

    private com.argus.bean.FTextField fTextField21;

    private com.argus.bean.FTextField fTextField20;

    private javax.swing.JLabel jLabel434;

    private com.argus.bean.FTextField fTextField512;

    private com.argus.bean.FTextField fTextField511;

    private com.argus.bean.FTextField fTextField510;

    private com.argus.bean.FTextField fTextField5511;

    private com.argus.bean.FTextField fTextField3011;

    private com.argus.bean.FTextField fTextField19;

    private com.argus.bean.FTextField fTextField18;

    private javax.swing.JLabel jLabel5011;

    private com.argus.bean.FTextField fTextField17;

    private com.argus.bean.FTextField fTextField16;

    private com.argus.bean.FTextField fTextField4811;

    private com.argus.bean.FTextField fTextField15;

    private com.argus.bean.FTextField fTextField14;

    private com.argus.bean.FTextField fTextField2311;

    private com.argus.bean.FTextField fTextField452;

    private com.argus.bean.FTextField fTextField451;

    private com.argus.bean.FTextField fTextField13;

    private javax.swing.JTextField jTextField1;

    private com.argus.bean.FTextField fTextField12;

    private com.argus.bean.FTextField fTextField11;

    private com.argus.bean.FTextField fTextField502;

    private com.argus.bean.FTextField fTextField501;

    private com.argus.bean.FTextField fTextField1611;

    private javax.swing.JPanel jPanelCombinedAsset;

    private com.argus.bean.FTextField fTextField391;

    private com.argus.bean.FTextField fTextField442;

    private javax.swing.JPanel jPanelPartnerAsset;

    private javax.swing.JLabel jLabel361;

    private com.argus.bean.FTextField fTextField441;

    private javax.swing.JLabel jLabel413;

    private javax.swing.JLabel jLabel412;

    private javax.swing.JLabel jLabel411;

    private javax.swing.JPanel jPanelCombinedIncome;

    private javax.swing.JPanel jPanelCards;

    private com.argus.bean.FTextField fTextField381;

    private com.argus.bean.FTextField fTextField4211;

    private javax.swing.JLabel jLabel351;

    private com.argus.bean.FTextField fTextField431;

    private javax.swing.JLabel jLabel401;

    private javax.swing.JLabel jLabel5511;

    private com.argus.bean.FTextField fTextField372;

    private com.argus.bean.FTextField fTextField371;

    private com.argus.bean.FTextField fTextField2811;

    private javax.swing.JPanel jPanelCombinedAssetTest;

    private javax.swing.JLabel jLabel341;

    private com.argus.bean.FTextField fTextField421;

    private javax.swing.JLabel jLabel4811;

    private javax.swing.JButton jButton11;

    private com.argus.bean.FTextField fTextField362;

    private com.argus.bean.FTextField fTextField361;

    private javax.swing.JPanel jPanel61;

    private com.argus.bean.FTextField fTextField412;

    private com.argus.bean.FTextField fTextField411;

    private com.argus.bean.FTextField fTextField410;

    private com.argus.bean.FComboBox fComboBox3;

    private com.argus.bean.FComboBox fComboBox1;

    private com.argus.bean.FTextField fTextField352;

    private javax.swing.JLabel jLabel271;

    private com.argus.bean.FTextField fTextField351;

    private com.argus.bean.FTextField fTextField401;

    private com.argus.bean.FTextField fTextField1511;

    // End of variables declaration//GEN-END:variables
    private static DSSViewNew view;

    public static DSSViewNew display(final Model model, FocusListener[] listeners) {

        if (view == null) {
            view = new DSSViewNew();
            SwingUtil.add2Frame(view, listeners,
                    model == null ? view.getDefaultTitle() : model.getTitle(),
                    ViewSettings.getInstance().getViewImage(
                    view.getClass().getName()), true, true, false);

        }

        try {
            view.updateView(model);

            String title = model == null ? view.getDefaultTitle() : model.getTitle();
            SwingUtil.setTitle(view, title);
            SwingUtil.setVisible(view, true);
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            view = null;
        }

        return view;

    }

    public void updateView(Model model) throws java.io.IOException {

        ClientService client = clientService;
        jButton2.setEnabled(client != null);
        jButton3.setEnabled(client != null);
        jButtonDelete.setEnabled(client != null);

        // calc.reset();
        // ((DSSCalcNew)calc).getDSSContainer().reset();
        doClear(null);

        if (model == null) {
            calc.setValues(client);
            if (client != null) {
                calc.setValues(client.findFinancials());
            }

        } else {
            calc.setValuesFromObject(model.getData());

        }

        calc.sendNotification(new Object());
        calc.validate("");
        calc.setModified(false);
        wch.show(jPanelCards, "card1");

        String title = model == null ? view.getDefaultTitle() : model.getTitle();
        view.setTitle(title);
        SwingUtil.setTitle(view, title);

    }

    private void saveView(boolean newModel) {

        ClientService person = clientService;

        ModelCollection mc = null;
        try {
            mc = person.getModels();
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
            return;
        }

        Model m = null;

        String oldTitle = getTitle();
        try {

            if (newModel || oldTitle == null || oldTitle.trim().length() <= 0
                    || oldTitle.trim().equals(getDefaultTitle())) {
                SaveProjectionDialog dlg = SaveProjectionDialog
                        .getSaveProjectionInstance();

                dlg.setTitle("Save " + oldTitle + " as ..");
                dlg.setSaveTitle("");
                dlg.show();

                if (dlg.getResult() == com.argus.financials.ui.InputDialog.CANCEL_OPTION)
                    return;
                String newTitle = dlg.getSaveTitle().trim();

                // validate new title
                if (newTitle == null || newTitle.length() < 3)
                    throw new ModelTitleRestrictionException(
                            "Failed to save model.\n  Please ensure total characters for model title is 3 or more.");

                if (newTitle.equalsIgnoreCase(getDefaultTitle())
                        || newTitle.equalsIgnoreCase(oldTitle)
                        || newTitle.equalsIgnoreCase(IMenuCommand.NEW.getSecond().toString()))
                    throw new ModelTitleRestrictionException(
                            "Title is not allowed"); // has to be new/not
                                                        // empty title

                if (mc.findByName(ModelType.CENTRELINK_CALC, newTitle) != null)
                    throw new DuplicateException(
                            "Title already used by another model.");

                m = new Model();
                m.setTypeID(ModelType.CENTRELINK_CALC);
                m.setTitle(newTitle);
                m.setDescription("");

                setTitle(newTitle);
                SwingUtil.setTitle(this, getTitle());

                mc.addModel(m);

            } else {
                m = mc.findByName(ModelType.CENTRELINK_CALC, oldTitle);
            }

            m.setData(calc.getValuesAsObject());

        } catch (DuplicateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Duplicate Title Error", JOptionPane.ERROR_MESSAGE);
            return;

        } catch (ModelTitleRestrictionException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Invalid Title Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        try {
            person.storeModels();
            // saveView( person );
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    public void saveView(PersonService person) throws ServiceException,
            InvalidCodeException {

        person.storeModels();
        // dm.setSaved();

    }

    protected void doClear(java.awt.event.ActionEvent evt) {
        buttonGroup1.setSelected(null, true);
        buttonGroup11.setSelected(null, true);
        calc.reset();
        ((DSSCalcNew) calc).getDSSContainer().reset();
        calc.validate("");
        calc.sendNotification(new Object());
        wch.show(jPanelCards, "card1");

    }

    public boolean isModified() {
        return (jButton2.isVisible() && jButton2.isEnabled() && calc
                .isModified())
                || (jButton3.isVisible() && jButton3.isEnabled() && calc
                        .isModified());
    }

    public void doDelete(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this,
                "Do You want to delete this projection?",
                "Delete Projection Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        fireActionEvent(DATA_REMOVE);
    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return com.argus.financials.config.WordSettings.getInstance()
                .getCentrelinkReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws java.io.IOException {

        ReportFields reportFields = ReportFields.getInstance();
        calc.initializeReportData(reportFields);

        return reportFields;

    }

    protected void doReport() {
        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(clientService),
                    getDefaultReport());

        } catch (java.io.IOException e) {

            e.printStackTrace(System.err);
        }
    }

    /**
     * 
     */
    protected void setActionMap() {

        ActionMap am = getActionMap();
        if (am == null) {
            am = new ActionMap();
            this.setActionMap(am);
        }

        am.put(DATA_ADD, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveView(false);
                doClear(null);
            }
        });
        am.put(DATA_REMOVE, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                PersonService person = clientService;
                if (person == null)
                    return;

                try {
                    person.removeModel(model);
                    person.storeModels();
                } catch (com.argus.financials.api.ServiceException e) {
                    e.printStackTrace();
                    return;
                }

                // remove from menu
                FinancialPlannerApp.getInstance().updateModels();

                doClose(null);
                // doClear(null);

            }
        });
        /*
         * am.put( DATA_UPDATE, new AbstractAction() { public void
         * actionPerformed(java.awt.event.ActionEvent evt) {
         *  } } );
         */
    }

    private static int eventID;

    private void fireActionEvent(Integer actionID) {

        javax.swing.ActionMap actionMap = getActionMap();
        if (actionMap == null)
            return;

        javax.swing.Action action = (javax.swing.Action) (actionMap
                .get(actionID));
        if (action == null)
            return;

        action.actionPerformed(new java.awt.event.ActionEvent(this, ++eventID,
                this.getClass().getName()));

    }

    private void setClientPharVisible(boolean value) {
        fTextField62.setVisible(value);
        fTextField621.setVisible(value);
    }

    private void setPartnerPharVisible(boolean value) {
        fTextField63.setVisible(value);
        fTextField631.setVisible(value);
    }

    private void setPharLableVisible(boolean value) {
        jLabel812.setVisible(value);
        jLabel811.setVisible(value);
    }

    private void updatePharP() {
        if (!fCheckBox21.isSelected() && !fCheckBox41.isSelected()
                && !fCheckBox61.isSelected()) {
            setPartnerPharVisible(false);
            noPartnerPension = true;
        } else {
            setPartnerPharVisible(true);
            noPartnerPension = false;
        }
    }

    private void updatePharC() {

        if (!fCheckBox4.isSelected() && !fCheckBox2.isSelected()
                && !fCheckBox6.isSelected()) {
            setClientPharVisible(false);
            noClientPension = true;
        } else {
            setClientPharVisible(true);
            noClientPension = false;
        }
    }

    private void updateDeemedIncomeComponent() {
        if (fCheckBox31.isSelected() || fCheckBox61.isSelected()) {
            if (fCheckBox2.isSelected() || fCheckBox4.isSelected()
                    || fCheckBox7.isSelected()) {
                calc.getDSSContainer().setCalcModel(A_P);
                jPanelCombinedIncome.setVisible(true);
                fTextField381.setVisible(false);
                fTextField411.setVisible(false);
                fTextField401.setVisible(false);
                fTextField391.setVisible(false);
                fTextField421.setVisible(false);
                fTextField511.setVisible(false);
                fTextField491.setVisible(false);
                fTextField481.setVisible(false);

                fTextField3811.setVisible(false);
                fTextField4111.setVisible(false);
                fTextField4011.setVisible(false);
                fTextField3911.setVisible(false);
                fTextField4211.setVisible(false);
                fTextField5111.setVisible(false);
                fTextField4911.setVisible(false);
                fTextField4811.setVisible(false);
            } else if (fCheckBox3.isSelected() || fCheckBox5.isSelected()
                    || fCheckBox6.isSelected()) {
                calc.getDSSContainer().setCalcModel(A_A);
                jPanelCombinedIncome.setVisible(false);
                fTextField381.setVisible(true);
                fTextField411.setVisible(true);
                fTextField401.setVisible(true);
                fTextField391.setVisible(true);
                fTextField421.setVisible(true);
                fTextField511.setVisible(true);
                fTextField491.setVisible(true);
                fTextField481.setVisible(true);

                fTextField3811.setVisible(true);
                fTextField4111.setVisible(true);
                fTextField4011.setVisible(true);
                fTextField3911.setVisible(true);
                fTextField4211.setVisible(true);
                fTextField5111.setVisible(true);
                fTextField4911.setVisible(true);
                fTextField4811.setVisible(true);

            } else {
                calc.getDSSContainer().setCalcModel(A_N);
                jPanelCombinedIncome.setVisible(false);
                fTextField381.setVisible(true);
                fTextField411.setVisible(true);
                fTextField401.setVisible(true);
                fTextField391.setVisible(true);
                fTextField421.setVisible(true);
                fTextField511.setVisible(true);
                fTextField491.setVisible(true);
                fTextField481.setVisible(true);

                fTextField3811.setVisible(true);
                fTextField4111.setVisible(true);
                fTextField4011.setVisible(true);
                fTextField3911.setVisible(true);
                fTextField4211.setVisible(true);
                fTextField5111.setVisible(true);
                fTextField4911.setVisible(true);
                fTextField4811.setVisible(true);
            }

        } else if (fCheckBox3.isSelected() || fCheckBox5.isSelected()
                || fCheckBox6.isSelected()) {
            if (fCheckBox21.isSelected() || fCheckBox41.isSelected()
                    || fCheckBox71.isSelected()) {
                calc.getDSSContainer().setCalcModel(A_P);
                jPanelCombinedIncome.setVisible(true);
                fTextField381.setVisible(false);
                fTextField411.setVisible(false);
                fTextField401.setVisible(false);
                fTextField391.setVisible(false);
                fTextField421.setVisible(false);
                fTextField511.setVisible(false);
                fTextField491.setVisible(false);
                fTextField481.setVisible(false);

                fTextField3811.setVisible(false);
                fTextField4111.setVisible(false);
                fTextField4011.setVisible(false);
                fTextField3911.setVisible(false);
                fTextField4211.setVisible(false);
                fTextField5111.setVisible(false);
                fTextField4911.setVisible(false);
                fTextField4811.setVisible(false);

            } else if (fCheckBox31.isSelected() || fCheckBox61.isSelected()) {
                calc.getDSSContainer().setCalcModel(A_A);
                jPanelCombinedIncome.setVisible(false);
                fTextField381.setVisible(true);
                fTextField411.setVisible(true);
                fTextField401.setVisible(true);
                fTextField391.setVisible(true);
                fTextField421.setVisible(true);
                fTextField511.setVisible(true);
                fTextField491.setVisible(true);
                fTextField481.setVisible(true);

                fTextField3811.setVisible(true);
                fTextField4111.setVisible(true);
                fTextField4011.setVisible(true);
                fTextField3911.setVisible(true);
                fTextField4211.setVisible(true);
                fTextField5111.setVisible(true);
                fTextField4911.setVisible(true);
                fTextField4811.setVisible(true);

            } else {
                calc.getDSSContainer().setCalcModel(A_N);
                jPanelCombinedIncome.setVisible(false);
                fTextField381.setVisible(true);
                fTextField411.setVisible(true);
                fTextField401.setVisible(true);
                fTextField391.setVisible(true);
                fTextField421.setVisible(true);
                fTextField511.setVisible(true);
                fTextField491.setVisible(true);
                fTextField481.setVisible(true);

                fTextField3811.setVisible(true);
                fTextField4111.setVisible(true);
                fTextField4011.setVisible(true);
                fTextField3911.setVisible(true);
                fTextField4211.setVisible(true);
                fTextField5111.setVisible(true);
                fTextField4911.setVisible(true);
                fTextField4811.setVisible(true);

            }
        } else if (calc.getDSSContainer().getPersonInfo().isSingle()) {
            jPanelCombinedIncome.setVisible(false);
            fTextField381.setVisible(true);
            fTextField411.setVisible(true);
            fTextField401.setVisible(true);
            fTextField391.setVisible(true);
            fTextField421.setVisible(true);
            fTextField511.setVisible(true);
            fTextField491.setVisible(true);
            fTextField481.setVisible(true);
        } else {
            jPanelCombinedIncome.setVisible(true);
            fTextField381.setVisible(false);
            fTextField411.setVisible(false);
            fTextField401.setVisible(false);
            fTextField391.setVisible(false);
            fTextField421.setVisible(false);
            fTextField511.setVisible(false);
            fTextField491.setVisible(false);
            fTextField481.setVisible(false);

            fTextField3811.setVisible(false);
            fTextField4111.setVisible(false);
            fTextField4011.setVisible(false);
            fTextField3911.setVisible(false);
            fTextField4211.setVisible(false);
            fTextField5111.setVisible(false);
            fTextField4911.setVisible(false);
            fTextField4811.setVisible(false);

        }
    }
}
