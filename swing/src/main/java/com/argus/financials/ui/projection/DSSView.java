/*
 * DSSView.java
 *
 * Created on 3 December 2001, 15:36
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Cursor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.code.FinancialClassID;
import com.argus.financials.bean.AssetInvestment;
import com.argus.financials.bean.AssetPersonal;
import com.argus.financials.bean.AssetSuperannuation;
import com.argus.financials.bean.Financial;
import com.argus.financials.code.BenefitTypeCode;
import com.argus.financials.code.Code;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.MaritalCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.code.SexCode;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.projection.AllocatedPensionCalc;
import com.argus.financials.projection.DSSCalc2;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.DocumentUtils;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.TaxUtils;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.report.data.DSSData;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.NameInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.AbstractPanel;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.CheckBoxList;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.financials.ui.IMenuCommand;
import com.argus.financials.ui.ListSelection;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.io.IOUtils2;
import com.argus.swing.SwingUtils;
import com.argus.util.DateTimeUtils;

public class DSSView extends AbstractPanel
    implements ActionEventID,
        javax.swing.event.ChangeListener, com.argus.financials.swing.ICloseDialog,
        FinancialClassID {

    private static final int MEANS_TEST_ID = 1;

    private boolean tabFlag;

    private java.awt.event.FocusListener[] listeners;

    public DSSCalc2 dssCalc2;

    private ListSelection apModels;

    private boolean newModel;

    private String title = new String(" Select one Allocated Pension ");

    // avoid duplicated error messages
    private String _oldClientMaritalStatus = ""; // see
                                                    // jComboBoxClientMaritalStatusItemStateChanged(...)

    // avoid error messages when loading a stored model or creating a new one
    private boolean _update_view = false; // see - " - and updateView(...)

    /** Creates new form DSSView */
    public DSSView() {
        dssCalc2 = new DSSCalc2();

        initComponents();
        initComponents2();
    }

    public DSSView(MoneyCalc calc) {
        dssCalc2 = (DSSCalc2) calc;

        initComponents();
        initComponents2();
    }

    public Integer getDefaultType() {
        return ModelTypeID.rcSOCIAL_SECURITY_CALC.getCodeId();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcSOCIAL_SECURITY_CALC.getDescription();
    }

    private Model getModel() {
        Model model = dssCalc2.getModel();
        if (model.getOwner() != null)
            return model;

        PersonService person = clientService;
        if (person != null) {
            try {
                model.setOwner(person.getModels());
            } catch (com.argus.financials.api.ServiceException e) {
                e.printStackTrace(System.err);
            }
        }
        return model;
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void updateTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    private void initComponents2() {

        jTabbedPaneStateChanged(null);

        BenefitTypeCode bf = new BenefitTypeCode();

        jComboBoxClientBenefitType.addItem(bf
                .getCodeDescription(BenefitTypeCode.NONE));
        jComboBoxPartnerBenefitType.addItem(bf
                .getCodeDescription(BenefitTypeCode.NONE));

        jComboBoxClientBenefitType.addItem(bf
                .getCodeDescription(BenefitTypeCode.AGE_PENSION));
        jComboBoxPartnerBenefitType.addItem(bf
                .getCodeDescription(BenefitTypeCode.AGE_PENSION));

        /*
         * jComboBoxClientBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.NEW_START_ALLOWANCE ) );
         * jComboBoxPartnerBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.NEW_START_ALLOWANCE ) );
         * 
         * jComboBoxClientBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.PARTNER_ALLOWANCE ) );
         * jComboBoxPartnerBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.PARTNER_ALLOWANCE ) );
         * 
         * jComboBoxClientBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.MATURE_AGE_ALLOWANCE ) );
         * jComboBoxPartnerBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.MATURE_AGE_ALLOWANCE ) );
         * 
         * jComboBoxClientBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.DISABILITY_SUPPORT_PENSION ) );
         * jComboBoxPartnerBenefitType.addItem( bf.getCodeDescription(
         * BenefitTypeCode.DISABILITY_SUPPORT_PENSION ) );
         */

        jComboBoxClientMaritalStatus.setSelectedItem(null);
        jComboBoxClientBenefitType.setSelectedItem(null);
        jComboBoxPartnerBenefitType.setSelectedItem(null);

        SwingUtils.setDefaultFont(this);
        // ??? SwingUtils.setDefaultKeyAdapter(this);

        jButtonAllocatedPension.setCursor(Cursor
                .getPredefinedCursor(Cursor.HAND_CURSOR));

        _setAccessibleContext();

        if (dssCalc2.getClass().equals(DSSCalc2.class)) {
            dssCalc2.addChangeListener(this);
            DocumentUtils.addListener(this, dssCalc2); // after
                                                        // _setAccessibleContext()
            updateEditable();

        }

        jButtonSave.setEnabled(clientService != null);
        jButtonSaveAs.setEnabled(clientService != null);
        jButtonDelete.setEnabled(jButtonSave.isEnabled());
        setActionMap();

        /*
         * make income fields invisible, because the income is calculated in an
         * other way now, but it's a good idea to keep these fields
         */
        this.jTextFieldSavingsI.setVisible(false);
        this.jTextFieldManagedFundsI.setVisible(false);
        this.jTextFieldSharesI.setVisible(false);
        this.jTextFieldBondsI.setVisible(false);
        this.jTextFieldFixedInterestI.setVisible(false);
        this.jTextFieldGiftsI.setVisible(false);
        this.jTextFieldLoansI.setVisible(false);
        this.jTextFieldSuperannuationI.setVisible(false);
        // this.jTextFieldComplyingPensionA.setVisible(false);
        this.jButtonAllocatedPension.setVisible(false);
        this.jLabel39.setVisible(false);
        this.jTextFieldPensionerRebateC.setVisible(false);

        this.jTextFieldSavingsIPartner.setVisible(false);
        this.jTextFieldManagedFundsIPartner.setVisible(false);
        this.jTextFieldSharesIPartner.setVisible(false);
        this.jTextFieldBondsIPartner.setVisible(false);
        this.jTextFieldFixedInterestIPartner.setVisible(false);
        this.jTextFieldGiftsIPartner.setVisible(false);
        this.jTextFieldLoansIPartner.setVisible(false);
        this.jTextFieldTotalAssetsSubjectToDeemingIPartner.setVisible(false);
        this.jTextFieldSuperannuationIPartner.setVisible(false);
        // this.jTextFieldComplyingPensionAPartner.setVisible(false);
        this.jButtonAllocatedPensionPartner.setVisible(false);
        this.jLabel44.setVisible(false);
        this.jTextFieldPensionerRebateP.setVisible(false);
    }

    private void _setAccessibleContext() {
        jTextFieldClientName.getAccessibleContext().setAccessibleName(
                DocumentNames.CLIENT_NAME);
        jTextFieldClientDOB.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.DOB);

        jRadioButtonClientMale.getAccessibleContext().setAccessibleName(
                DocumentNames.SEX_CODE_MALE);
        jRadioButtonClientFemale.getAccessibleContext().setAccessibleName(
                DocumentNames.SEX_CODE_FEMALE);

        jTextFieldClientCalculationDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.CALCULATION_DATE);
        jComboBoxClientMaritalStatus.getAccessibleContext().setAccessibleName(
                DocumentNames.MARITAL_STATUS);
        jComboBoxClientBenefitType.getAccessibleContext().setAccessibleName(
                DocumentNames.BENEFIT_TYPE);

        jCheckBoxClientHO.getAccessibleContext().setAccessibleName(
                DocumentNames.HOME_OWNER);
        jCheckBoxClientNC.getAccessibleContext().setAccessibleName(
                DocumentNames.NO_CHILDREN);
        jCheckBoxClientSH.getAccessibleContext().setAccessibleName(
                DocumentNames.SHARER);
        jTextFieldRent.getAccessibleContext().setAccessibleName(
                DocumentNames.RENT);
        jTextFieldChildrenAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.CHILDREN_AMOUNT);

        jTextFieldPartnerName.getAccessibleContext().setAccessibleName(
                DocumentNames.NAME_PARTNER);
        jTextFieldPartnerDOB.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.DOB_PARTNER);

        jRadioButtonPartnerMale.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SEX_CODE_MALE);
        jRadioButtonPartnerFemale.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SEX_CODE_FEMALE);

        jTextFieldPartnerCalculationDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.CALCULATION_DATE_PARTNER);
        jComboBoxPartnerBenefitType.getAccessibleContext().setAccessibleName(
                DocumentNames.BENEFIT_TYPE_PARTNER);

        /*
         * client details fields
         * 
         * DSSCalc2 uses these AccessibleNames in the methode:
         * 
         * protected boolean update( Object property, String value )
         * 
         * to identify the input field which has changed and to set the
         * calculator to the new value!!!
         */
        jTextFieldSavingsA.getAccessibleContext().setAccessibleName(
                DocumentNames.SAVINGS_A);
        jTextFieldManagedFundsA.getAccessibleContext().setAccessibleName(
                DocumentNames.MANAGED_FUNDS_A);
        jTextFieldSharesA.getAccessibleContext().setAccessibleName(
                DocumentNames.SHARES_A);
        jTextFieldBondsA.getAccessibleContext().setAccessibleName(
                DocumentNames.BONDS_A);
        jTextFieldFixedInterestA.getAccessibleContext().setAccessibleName(
                DocumentNames.FIXED_INTEREST_A);
        jTextFieldHomeContentsA.getAccessibleContext().setAccessibleName(
                DocumentNames.HOME_CONTENTS_A);
        jTextFieldCarsEtcA.getAccessibleContext().setAccessibleName(
                DocumentNames.CARS_ETC_A);
        jTextFieldPropertyA.getAccessibleContext().setAccessibleName(
                DocumentNames.PROPERTY_A);
        jTextFieldGiftsA.getAccessibleContext().setAccessibleName(
                DocumentNames.GIFTS_A);
        jTextFieldLoans.getAccessibleContext().setAccessibleName(
                DocumentNames.LOANS_A);

        jTextFieldPropertyI.getAccessibleContext().setAccessibleName(
                DocumentNames.PROPERTY_I);
        // jTextFieldGiftsI.getAccessibleContext().setAccessibleName(
        // DocumentNames.GIFTS_I );
        jTextFieldLoansI.getAccessibleContext().setAccessibleName(
                DocumentNames.LOANS_I);
        jTextFieldSuperannuationA.getAccessibleContext().setAccessibleName(
                DocumentNames.SUPERANNUATION_A);
        jTextFieldComplyingPensionA.getAccessibleContext().setAccessibleName(
                DocumentNames.COMPLYING_PENSION_A);
        jTextFieldComplyingPensionI.getAccessibleContext().setAccessibleName(
                DocumentNames.COMPLYING_PENSION_I);
        jTextFieldAllocatedPensionA.getAccessibleContext().setAccessibleName(
                DocumentNames.ALLOCATED_PENSION_A);
        jTextFieldAllocatedPensionI.getAccessibleContext().setAccessibleName(
                DocumentNames.ALLOCATED_PENSION_I);
        jTextFieldClientSalaryI.getAccessibleContext().setAccessibleName(
                DocumentNames.CLIENT_SALARY_I);
        jTextFieldPartnerSalaryI.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SALARY_I);

        /*
         * partner details fields
         * 
         * DSSCalc2 uses these AccessibleNames in the methode:
         * 
         * protected boolean update( Object property, String value )
         * 
         * to identify the input field which has changed and to set the
         * calculator to the new value!!!
         */
        jTextFieldSavingsAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.SAVINGS_A_PARTNER);
        jTextFieldManagedFundsAPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.MANAGED_FUNDS_A_PARTNER);
        jTextFieldSharesAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.SHARES_A_PARTNER);
        jTextFieldBondsAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.BONDS_A_PARTNER);
        jTextFieldFixedInterestAPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.FIXED_INTEREST_A_PARTNER);
        jTextFieldHomeContentsAPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.HOME_CONTENTS_A_PARTNER);
        jTextFieldCarsEtcAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.CARS_ETC_A_PARTNER);
        jTextFieldPropertyAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.PROPERTY_A_PARTNER);
        jTextFieldGiftsAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.GIFTS_A_PARTNER);
        jTextFieldLoansAPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.LOANS_A_PARTNER);

        jTextFieldPropertyIPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.PROPERTY_I_PARTNER);
        // jTextFieldGiftsIPartner.getAccessibleContext().setAccessibleName(
        // DocumentNames.GIFTS_I_PARTNER );
        jTextFieldLoansIPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.LOANS_I_PARTNER);
        jTextFieldSuperannuationAPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.SUPERANNUATION_A_PARTNER);
        jTextFieldComplyingPensionAPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.COMPLYING_PENSION_A_PARTNER);
        jTextFieldComplyingPensionIPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.COMPLYING_PENSION_I_PARTNER);
        jTextFieldAllocatedPensionAPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.ALLOCATED_PENSION_A_PARTNER);
        jTextFieldAllocatedPensionIPartner.getAccessibleContext()
                .setAccessibleName(DocumentNames.ALLOCATED_PENSION_I_PARTNER);

    }

    protected void updateComponents() {
        jButtonSave.setEnabled(clientService != null);
        jButtonSaveAs.setEnabled(clientService != null);
        if (jTabbedPane.getSelectedIndex() == 0)
            jButtonNext.setEnabled(dssCalc2.isReady());
        SwingUtil
                .setEnabled(jCheckBoxClientSH, !jCheckBoxClientHO.isSelected());
        SwingUtil
                .setEnabled(jCheckBoxClientHO, !jCheckBoxClientSH.isSelected());
        SwingUtil.setEnabled(jTextFieldRent, !jCheckBoxClientHO.isSelected());
        SwingUtil.setEnabled(jTextFieldChildrenAmount, !jCheckBoxClientNC
                .isSelected());
        SwingUtil.setEnabled(jButtonReport, dssCalc2.isReady());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        bgClientSex = new javax.swing.ButtonGroup();
        bgPartnerSex = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelBenefitDetails = new javax.swing.JPanel();
        jPanelClient = new javax.swing.JPanel();
        jPanelClientDetails = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldClientName = new javax.swing.JTextField();
        jTextFieldClientDOB = new com.argus.bean.FDateChooser();
        jPanelClientSex = new javax.swing.JPanel();
        jRadioButtonClientMale = new javax.swing.JRadioButton();
        jRadioButtonClientFemale = new javax.swing.JRadioButton();
        jTextFieldClientCalculationDate = new com.argus.bean.FDateChooser();
        jComboBoxClientMaritalStatus = new javax.swing.JComboBox();
        MaritalCode mc = new MaritalCode();
        jComboBoxClientMaritalStatus.addItem(mc
                .getCodeDescription(Code.VALUE_NONE));
        jComboBoxClientMaritalStatus.addItem(mc
                .getCodeDescription(IMaritalCode.PARTNERED));
        jComboBoxClientMaritalStatus.addItem(mc
                .getCodeDescription(IMaritalCode.SINGLE));
        jComboBoxClientMaritalStatus.addItem(mc
                .getCodeDescription(IMaritalCode.SEPARATED_HEALTH));
        jComboBoxClientBenefitType = new javax.swing.JComboBox();
        jCheckBoxClientNC = new javax.swing.JCheckBox();
        jCheckBoxClientHO = new javax.swing.JCheckBox();
        jCheckBoxClientSH = new javax.swing.JCheckBox();
        jLabelRent = new javax.swing.JLabel();
        jTextFieldRent = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldChildrenAmount = new javax.swing.JTextField();
        jScrollPaneClientEC = new javax.swing.JScrollPane();
        jTextAreaClientEC = new javax.swing.JTextArea();
        jPanelPartner = new javax.swing.JPanel();
        jPanelPartnerDetails = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldPartnerName = new javax.swing.JTextField();
        jTextFieldPartnerDOB = new com.argus.bean.FDateChooser();
        jPanelPartnerSex = new javax.swing.JPanel();
        jRadioButtonPartnerMale = new javax.swing.JRadioButton();
        jRadioButtonPartnerFemale = new javax.swing.JRadioButton();
        jTextFieldPartnerCalculationDate = new com.argus.bean.FDateChooser();
        jComboBoxPartnerBenefitType = new javax.swing.JComboBox();
        jScrollPanePartner = new javax.swing.JScrollPane();
        jTextAreaPartnerEC = new javax.swing.JTextArea();
        jPanelMeansTestResults = new javax.swing.JPanel();
        jPanelLayout = new javax.swing.JPanel();
        jPanelNewClientDetails = new javax.swing.JPanel();
        jLabelSavings = new javax.swing.JLabel();
        jLabelManagedFunds = new javax.swing.JLabel();
        jLabelShares = new javax.swing.JLabel();
        jLabelFixedInterest = new javax.swing.JLabel();
        jLabelHomeContents = new javax.swing.JLabel();
        jLabelGifts = new javax.swing.JLabel();
        jLabelProperty = new javax.swing.JLabel();
        jLabelCarsETC = new javax.swing.JLabel();
        jLabelLoans = new javax.swing.JLabel();
        jTextFieldSavingsI = new javax.swing.JTextField();
        jTextFieldManagedFundsI = new javax.swing.JTextField();
        jTextFieldSharesI = new javax.swing.JTextField();
        jTextFieldBondsI = new javax.swing.JTextField();
        jTextFieldGiftsI = new javax.swing.JTextField();
        jTextFieldLoansI = new javax.swing.JTextField();
        jTextFieldSuperannuationI = new javax.swing.JTextField();
        jLabelIncome = new javax.swing.JLabel();
        jLabelSalaryWages = new javax.swing.JLabel();
        jTextFieldSavingsA = new javax.swing.JTextField();
        jTextFieldManagedFundsA = new javax.swing.JTextField();
        jTextFieldSharesA = new javax.swing.JTextField();
        jTextFieldBondsA = new javax.swing.JTextField();
        jTextFieldFixedInterestA = new javax.swing.JTextField();
        jTextFieldHomeContentsA = new javax.swing.JTextField();
        jTextFieldPropertyA = new javax.swing.JTextField();
        jTextFieldCarsEtcA = new javax.swing.JTextField();
        jTextFieldGiftsA = new javax.swing.JTextField();
        jTextFieldLoans = new javax.swing.JTextField();
        jTextFieldSuperannuationA = new javax.swing.JTextField();
        jLabelAssets = new javax.swing.JLabel();
        jTextFieldTotalA = new javax.swing.JTextField();
        jLabelBonds = new javax.swing.JLabel();
        jButtonAllocatedPension = new javax.swing.JButton();
        jTextFieldAllocatedPensionA = new javax.swing.JTextField();
        jTextFieldAllocatedPensionI = new javax.swing.JTextField();
        jTextFieldTotalI = new javax.swing.JTextField();
        jTextFieldPropertyI = new javax.swing.JTextField();
        jTextFieldFixedInterestI = new javax.swing.JTextField();
        jTextFieldClientSalaryI = new javax.swing.JTextField();
        jTextFieldComplyingPensionA = new javax.swing.JTextField();
        jTextFieldComplyingPensionI = new javax.swing.JTextField();
        jLabelAllocatedPension = new javax.swing.JLabel();
        jLabelSuperannuation = new javax.swing.JLabel();
        jLabelComplyingPension = new javax.swing.JLabel();
        jTextFieldClientAssessableIncomeOnProperty = new javax.swing.JTextField();
        jLabelClientAssessableIncomeOnProperty = new javax.swing.JLabel();
        jLabelTotalAssetsSubjectToDeeming = new javax.swing.JLabel();
        jTextFieldTotalAssetsSubjectToDeemingA = new javax.swing.JTextField();
        jTextFieldTotalAssetsSubjectToDeemingIJoint = new javax.swing.JTextField();
        jLabelJointTotalAssetsSubjectToDeemingA = new javax.swing.JLabel();
        jTextFieldJointTotalAssetsSubjectToDeemingA = new javax.swing.JTextField();
        jLabelAPAnnuity = new javax.swing.JLabel();
        jPanelNewPartnerDetails = new javax.swing.JPanel();
        jLabelSavingsPartner = new javax.swing.JLabel();
        jLabelManagedFundsPartner = new javax.swing.JLabel();
        jLabelSharesPartner = new javax.swing.JLabel();
        jLabelFixedInterestPartner = new javax.swing.JLabel();
        jLabelHomeContentsPartner = new javax.swing.JLabel();
        jLabelGiftsPartner = new javax.swing.JLabel();
        jLabelPropertyPartner = new javax.swing.JLabel();
        jLabelCarsETCPartner = new javax.swing.JLabel();
        jLabelLoansPartner = new javax.swing.JLabel();
        jTextFieldSavingsIPartner = new javax.swing.JTextField();
        jTextFieldManagedFundsIPartner = new javax.swing.JTextField();
        jTextFieldSharesIPartner = new javax.swing.JTextField();
        jTextFieldBondsIPartner = new javax.swing.JTextField();
        jTextFieldGiftsIPartner = new javax.swing.JTextField();
        jTextFieldLoansIPartner = new javax.swing.JTextField();
        jTextFieldSuperannuationIPartner = new javax.swing.JTextField();
        jLabelIncomePartner = new javax.swing.JLabel();
        jLabelSalaryWagesPartner = new javax.swing.JLabel();
        jTextFieldSavingsAPartner = new javax.swing.JTextField();
        jTextFieldManagedFundsAPartner = new javax.swing.JTextField();
        jTextFieldSharesAPartner = new javax.swing.JTextField();
        jTextFieldBondsAPartner = new javax.swing.JTextField();
        jTextFieldFixedInterestAPartner = new javax.swing.JTextField();
        jTextFieldHomeContentsAPartner = new javax.swing.JTextField();
        jTextFieldPropertyAPartner = new javax.swing.JTextField();
        jTextFieldCarsEtcAPartner = new javax.swing.JTextField();
        jTextFieldGiftsAPartner = new javax.swing.JTextField();
        jTextFieldLoansAPartner = new javax.swing.JTextField();
        jTextFieldSuperannuationAPartner = new javax.swing.JTextField();
        jLabelAssetsPartner = new javax.swing.JLabel();
        jTextFieldTotalAPartner = new javax.swing.JTextField();
        jLabelBondsPartner = new javax.swing.JLabel();
        jButtonAllocatedPensionPartner = new javax.swing.JButton();
        jTextFieldAllocatedPensionAPartner = new javax.swing.JTextField();
        jTextFieldAllocatedPensionIPartner = new javax.swing.JTextField();
        jTextFieldTotalIPartner = new javax.swing.JTextField();
        jTextFieldPropertyIPartner = new javax.swing.JTextField();
        jTextFieldFixedInterestIPartner = new javax.swing.JTextField();
        jTextFieldPartnerSalaryI = new javax.swing.JTextField();
        jTextFieldComplyingPensionAPartner = new javax.swing.JTextField();
        jTextFieldComplyingPensionIPartner = new javax.swing.JTextField();
        jLabelTotalPartner = new javax.swing.JLabel();
        jLabelSuperannuationPartner = new javax.swing.JLabel();
        jLabelComplyingPensionPartner = new javax.swing.JLabel();
        jLabelTotalAssetsSubjectToDeemingPartner = new javax.swing.JLabel();
        jTextFieldTotalAssetsSubjectToDeemingAPartner = new javax.swing.JTextField();
        jTextFieldTotalAssetsSubjectToDeemingIPartner = new javax.swing.JTextField();
        jLabelClientAssessableIncomeOnPropertyPartner = new javax.swing.JLabel();
        jTextFieldClientAssessableIncomeOnPropertyPartner = new javax.swing.JTextField();
        jLabelDummyPartner = new javax.swing.JLabel();
        jTextFieldSpacer = new javax.swing.JTextField();
        jLabelAPAnnuityPartner = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelMeansTestTotalAssets = new javax.swing.JLabel();
        jTextFieldMeansTestTotalAssets = new javax.swing.JTextField();
        jLabelDummy = new javax.swing.JLabel();
        jLabelMeansTestTotalIncome = new javax.swing.JLabel();
        jTextFieldMeansTestTotalIncome = new javax.swing.JTextField();
        jPanelNewResults = new javax.swing.JPanel();
        jPanelResult = new javax.swing.JPanel();
        jPanelClientResults = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextFieldMaxBenefitC = new javax.swing.JTextField();
        jTextFieldAssetTestC = new javax.swing.JTextField();
        jTextFieldIncomeTestC = new javax.swing.JTextField();
        jTextFieldPensionerRebateC = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextFieldBasicBenefitC = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextFieldPharmAllowanceC = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTextFieldRentAssistanceC = new javax.swing.JTextField();
        jPanelPartnerResults = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextFieldMaxBenefitP = new javax.swing.JTextField();
        jTextFieldAssetTestP = new javax.swing.JTextField();
        jTextFieldIncomeTestP = new javax.swing.JTextField();
        jTextFieldBasicBenefitP = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextFieldPharmAllowanceP = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTextFieldRentAssistanceP = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextFieldPensionerRebateP = new javax.swing.JTextField();
        jPanelPensionPayable = new javax.swing.JPanel();
        jLabelMaxPensionPA = new javax.swing.JLabel();
        jTextFieldMaxPensionPA = new javax.swing.JTextField();
        jLabelActualPensionPA = new javax.swing.JLabel();
        jTextFieldlActualPensionPA = new javax.swing.JTextField();
        jLabelActualPensionPF = new javax.swing.JLabel();
        jTextFieldlActualPensionPF = new javax.swing.JTextField();
        jPanelControls = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(750, 600));
        jTabbedPane.setPreferredSize(new java.awt.Dimension(800, 600));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelBenefitDetails.setLayout(new javax.swing.BoxLayout(
                jPanelBenefitDetails, javax.swing.BoxLayout.Y_AXIS));

        jPanelClient.setLayout(new java.awt.GridLayout(1, 2));

        jPanelClient.setBorder(new javax.swing.border.TitledBorder(
                "ClientView Details"));
        jPanelClient.setPreferredSize(new java.awt.Dimension(700, 200));
        jPanelClient.setMinimumSize(new java.awt.Dimension(510, 200));
        jPanelClientDetails.setLayout(new java.awt.GridBagLayout());

        jPanelClientDetails.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel4.setText("Calculation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Marital Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Benefit Type");
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jLabel6, gridBagConstraints);

        jLabel11.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jLabel11, gridBagConstraints);

        jLabel12.setText("DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jLabel12, gridBagConstraints);

        jLabel13.setText("Sex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jLabel13, gridBagConstraints);

        jTextFieldClientName.setInputVerifier(NameInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelClientDetails.add(jTextFieldClientName, gridBagConstraints);

        jTextFieldClientDOB.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldClientDOB.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jTextFieldClientDOB, gridBagConstraints);

        jPanelClientSex.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT, 0, 5));

        jRadioButtonClientMale.setText("Male");
        bgClientSex.add(jRadioButtonClientMale);
        jRadioButtonClientMale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonClientMaleItemStateChanged(evt);
                    }
                });

        jPanelClientSex.add(jRadioButtonClientMale);

        jRadioButtonClientFemale.setText("Female");
        bgClientSex.add(jRadioButtonClientFemale);
        jRadioButtonClientFemale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonClientMaleItemStateChanged(evt);
                    }
                });

        jPanelClientSex.add(jRadioButtonClientFemale);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jPanelClientSex, gridBagConstraints);

        jTextFieldClientCalculationDate.setInputVerifier(DateInputVerifier
                .getInstance());
        jTextFieldClientCalculationDate
                .setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jTextFieldClientCalculationDate,
                gridBagConstraints);

        jComboBoxClientMaritalStatus.setPreferredSize(new java.awt.Dimension(
                150, 21));
        jComboBoxClientMaritalStatus
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxClientMaritalStatusItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jComboBoxClientMaritalStatus,
                gridBagConstraints);

        jComboBoxClientBenefitType.setPreferredSize(new java.awt.Dimension(220,
                21));
        jComboBoxClientBenefitType
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxClientBenefitTypeItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jComboBoxClientBenefitType, gridBagConstraints);

        jCheckBoxClientNC.setText("No Children");
        jCheckBoxClientNC.setPreferredSize(new java.awt.Dimension(100, 21));
        jCheckBoxClientNC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxClientNCItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jCheckBoxClientNC, gridBagConstraints);

        jCheckBoxClientHO.setText("Home Owner");
        jCheckBoxClientHO.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxClientHOItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientDetails.add(jCheckBoxClientHO, gridBagConstraints);

        jCheckBoxClientSH.setText("Sharer");
        jCheckBoxClientSH.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxClientSHItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jCheckBoxClientSH, gridBagConstraints);

        jLabelRent.setText("rent fortnightly");
        jLabelRent.setPreferredSize(new java.awt.Dimension(90, 16));
        jLabelRent.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jLabelRent, gridBagConstraints);

        jTextFieldRent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRent.setPreferredSize(new java.awt.Dimension(70, 20));
        jTextFieldRent.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientDetails.add(jTextFieldRent, gridBagConstraints);

        jLabel25.setText("No. of Children ");
        jLabel25.setPreferredSize(new java.awt.Dimension(100, 16));
        jLabel25.setAlignmentX(0.5F);
        jLabel25.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientDetails.add(jLabel25, gridBagConstraints);

        jTextFieldChildrenAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldChildrenAmount
                .setPreferredSize(new java.awt.Dimension(70, 20));
        jTextFieldChildrenAmount
                .setInputVerifier(com.argus.financials.swing.IntegerInputVerifier
                        .getInstance());
        jTextFieldChildrenAmount
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldChildrenAmountFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientDetails.add(jTextFieldChildrenAmount, gridBagConstraints);

        jPanelClient.add(jPanelClientDetails);

        jScrollPaneClientEC.setBorder(new javax.swing.border.TitledBorder(
                "Eligibility Conditions"));
        jTextAreaClientEC.setWrapStyleWord(true);
        jTextAreaClientEC.setLineWrap(true);
        jTextAreaClientEC.setEditable(false);
        jScrollPaneClientEC.setViewportView(jTextAreaClientEC);

        jPanelClient.add(jScrollPaneClientEC);

        jPanelBenefitDetails.add(jPanelClient);

        jPanelPartner.setLayout(new java.awt.GridLayout(1, 2));

        jPanelPartner.setBorder(new javax.swing.border.TitledBorder(
                "Partner Details"));
        jPanelPartnerDetails.setLayout(new java.awt.GridBagLayout());

        jPanelPartnerDetails.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel1.setText("Calculation Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerDetails.add(jLabel1, gridBagConstraints);

        jLabel3.setText("Benefit Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerDetails.add(jLabel3, gridBagConstraints);

        jLabel7.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerDetails.add(jLabel7, gridBagConstraints);

        jLabel8.setText("DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerDetails.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Sex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerDetails.add(jLabel9, gridBagConstraints);

        jTextFieldPartnerName.setInputVerifier(NameInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelPartnerDetails.add(jTextFieldPartnerName, gridBagConstraints);

        jTextFieldPartnerDOB.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPartnerDetails.add(jTextFieldPartnerDOB, gridBagConstraints);

        jPanelPartnerSex.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT, 0, 5));

        jRadioButtonPartnerMale.setText("Male");
        bgPartnerSex.add(jRadioButtonPartnerMale);
        jRadioButtonPartnerMale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPartnerMaleItemStateChanged(evt);
                    }
                });

        jPanelPartnerSex.add(jRadioButtonPartnerMale);

        jRadioButtonPartnerFemale.setText("Female");
        bgPartnerSex.add(jRadioButtonPartnerFemale);
        jRadioButtonPartnerFemale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPartnerMaleItemStateChanged(evt);
                    }
                });

        jPanelPartnerSex.add(jRadioButtonPartnerFemale);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPartnerDetails.add(jPanelPartnerSex, gridBagConstraints);

        jTextFieldPartnerCalculationDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPartnerDetails.add(jTextFieldPartnerCalculationDate,
                gridBagConstraints);

        jComboBoxPartnerBenefitType
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxPartnerBenefitTypeItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPartnerDetails.add(jComboBoxPartnerBenefitType,
                gridBagConstraints);

        jPanelPartner.add(jPanelPartnerDetails);

        jScrollPanePartner.setBorder(new javax.swing.border.TitledBorder(
                "Eligibility Conditions"));
        jTextAreaPartnerEC.setWrapStyleWord(true);
        jTextAreaPartnerEC.setLineWrap(true);
        jTextAreaPartnerEC.setEditable(false);
        jTextAreaPartnerEC.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPanePartner.setViewportView(jTextAreaPartnerEC);

        jPanelPartner.add(jScrollPanePartner);

        jPanelBenefitDetails.add(jPanelPartner);

        jTabbedPane.addTab("Benefit Details", null, jPanelBenefitDetails, "");

        jPanelMeansTestResults.setLayout(new java.awt.BorderLayout());

        jPanelLayout.setLayout(new javax.swing.BoxLayout(jPanelLayout,
                javax.swing.BoxLayout.X_AXIS));

        jPanelNewClientDetails.setLayout(new java.awt.GridBagLayout());

        jPanelNewClientDetails.setBorder(new javax.swing.border.TitledBorder(
                "ClientView Test"));
        jLabelSavings.setText("Savings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelSavings, gridBagConstraints);

        jLabelManagedFunds.setText("Managed Funds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelManagedFunds, gridBagConstraints);

        jLabelShares.setText("Shares/Derivatives");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelShares, gridBagConstraints);

        jLabelFixedInterest.setText("Fixed Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelFixedInterest, gridBagConstraints);

        jLabelHomeContents.setText("Home Contents");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelHomeContents, gridBagConstraints);

        jLabelGifts.setText("Gifts Over $10,000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelGifts, gridBagConstraints);

        jLabelProperty.setText("Property");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jLabelProperty, gridBagConstraints);

        jLabelCarsETC.setText("Cars/Caravans/Boats");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelCarsETC, gridBagConstraints);

        jLabelLoans.setText("Loans Owed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelLoans, gridBagConstraints);

        jTextFieldSavingsI.setEditable(false);
        jTextFieldSavingsI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldSavingsI, gridBagConstraints);

        jTextFieldManagedFundsI.setEditable(false);
        jTextFieldManagedFundsI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldManagedFundsI, gridBagConstraints);

        jTextFieldSharesI.setEditable(false);
        jTextFieldSharesI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldSharesI, gridBagConstraints);

        jTextFieldBondsI.setEditable(false);
        jTextFieldBondsI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldBondsI, gridBagConstraints);

        jTextFieldGiftsI.setEditable(false);
        jTextFieldGiftsI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldGiftsI.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldGiftsI, gridBagConstraints);

        jTextFieldLoansI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoansI.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldLoansI, gridBagConstraints);

        jTextFieldSuperannuationI.setEditable(false);
        jTextFieldSuperannuationI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSuperannuationI.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldSuperannuationI,
                gridBagConstraints);

        jLabelIncome.setText("Income");
        jLabelIncome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIncome.setPreferredSize(new java.awt.Dimension(90, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jLabelIncome, gridBagConstraints);

        jLabelSalaryWages.setText("ClientView Salary/Wages");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelSalaryWages, gridBagConstraints);

        jTextFieldSavingsA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSavingsA
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldSavingsA, gridBagConstraints);

        jTextFieldManagedFundsA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldManagedFundsA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldManagedFundsA, gridBagConstraints);

        jTextFieldSharesA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSharesA.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldSharesA, gridBagConstraints);

        jTextFieldBondsA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldBondsA.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldBondsA, gridBagConstraints);

        jTextFieldFixedInterestA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldFixedInterestA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails
                .add(jTextFieldFixedInterestA, gridBagConstraints);

        jTextFieldHomeContentsA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldHomeContentsA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldHomeContentsA, gridBagConstraints);

        jTextFieldPropertyA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPropertyA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldPropertyA, gridBagConstraints);

        jTextFieldCarsEtcA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCarsEtcA
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldCarsEtcA, gridBagConstraints);

        jTextFieldGiftsA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldGiftsA.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldGiftsA, gridBagConstraints);

        jTextFieldLoans.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoans.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldLoans, gridBagConstraints);

        jTextFieldSuperannuationA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSuperannuationA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldSuperannuationA,
                gridBagConstraints);

        jLabelAssets.setText("Assets");
        jLabelAssets.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAssets.setPreferredSize(new java.awt.Dimension(90, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jLabelAssets, gridBagConstraints);

        jTextFieldTotalA.setEditable(false);
        jTextFieldTotalA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldTotalA, gridBagConstraints);

        jLabelBonds.setText("Bonds/Debentures");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelBonds, gridBagConstraints);

        jButtonAllocatedPension.setText("Allocated Pension/Annuity");
        jButtonAllocatedPension
                .setPreferredSize(new java.awt.Dimension(35, 20));
        jButtonAllocatedPension.setActionCommand("Allocated Pension");
        jButtonAllocatedPension
                .setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAllocatedPension.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButtonAllocatedPension
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonAllocatedPensionActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelNewClientDetails.add(jButtonAllocatedPension, gridBagConstraints);

        jTextFieldAllocatedPensionA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAllocatedPensionA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldAllocatedPensionA,
                gridBagConstraints);

        jTextFieldAllocatedPensionI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAllocatedPensionI.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldAllocatedPensionI,
                gridBagConstraints);

        jTextFieldTotalI.setEditable(false);
        jTextFieldTotalI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldTotalI, gridBagConstraints);

        jTextFieldPropertyI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPropertyI.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldPropertyI, gridBagConstraints);

        jTextFieldFixedInterestI.setEditable(false);
        jTextFieldFixedInterestI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails
                .add(jTextFieldFixedInterestI, gridBagConstraints);

        jTextFieldClientSalaryI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientSalaryI.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldClientSalaryI, gridBagConstraints);

        jTextFieldComplyingPensionA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldComplyingPensionA.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldComplyingPensionA,
                gridBagConstraints);

        jTextFieldComplyingPensionI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldComplyingPensionI.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldComplyingPensionI,
                gridBagConstraints);

        jLabelAllocatedPension.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelNewClientDetails.add(jLabelAllocatedPension, gridBagConstraints);

        jLabelSuperannuation.setText("Superannuation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelSuperannuation, gridBagConstraints);

        jLabelComplyingPension.setText("Complying Pension/Annuity");
        jLabelComplyingPension
                .setPreferredSize(new java.awt.Dimension(160, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelComplyingPension, gridBagConstraints);

        jTextFieldClientAssessableIncomeOnProperty.setEditable(false);
        jTextFieldClientAssessableIncomeOnProperty
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientAssessableIncomeOnProperty
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewClientDetails.add(jTextFieldClientAssessableIncomeOnProperty,
                gridBagConstraints);

        jLabelClientAssessableIncomeOnProperty
                .setText("Assessable Income On Property");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelClientAssessableIncomeOnProperty,
                gridBagConstraints);

        jLabelTotalAssetsSubjectToDeeming
                .setText("ClientView Assets Subject to Deeming");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelNewClientDetails.add(jLabelTotalAssetsSubjectToDeeming,
                gridBagConstraints);

        jTextFieldTotalAssetsSubjectToDeemingA.setEditable(false);
        jTextFieldTotalAssetsSubjectToDeemingA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldTotalAssetsSubjectToDeemingA,
                gridBagConstraints);

        jTextFieldTotalAssetsSubjectToDeemingIJoint.setEditable(false);
        jTextFieldTotalAssetsSubjectToDeemingIJoint
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldTotalAssetsSubjectToDeemingIJoint,
                gridBagConstraints);

        jLabelJointTotalAssetsSubjectToDeemingA.setText("Total Joint");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelNewClientDetails.add(jLabelJointTotalAssetsSubjectToDeemingA,
                gridBagConstraints);

        jTextFieldJointTotalAssetsSubjectToDeemingA.setEditable(false);
        jTextFieldJointTotalAssetsSubjectToDeemingA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewClientDetails.add(jTextFieldJointTotalAssetsSubjectToDeemingA,
                gridBagConstraints);

        jLabelAPAnnuity.setText("AP/Annuity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewClientDetails.add(jLabelAPAnnuity, gridBagConstraints);

        jPanelLayout.add(jPanelNewClientDetails);

        jPanelNewPartnerDetails.setLayout(new java.awt.GridBagLayout());

        jPanelNewPartnerDetails.setBorder(new javax.swing.border.TitledBorder(
                "Partner Test"));
        jLabelSavingsPartner.setText("Savings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelSavingsPartner, gridBagConstraints);

        jLabelManagedFundsPartner.setText("Managed Funds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelManagedFundsPartner,
                gridBagConstraints);

        jLabelSharesPartner.setText("Shares/Derivatives");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelSharesPartner, gridBagConstraints);

        jLabelFixedInterestPartner.setText("Fixed Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelFixedInterestPartner,
                gridBagConstraints);

        jLabelHomeContentsPartner.setText("Home Contents");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelHomeContentsPartner,
                gridBagConstraints);

        jLabelGiftsPartner.setText("Gifts Over $10,000");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelGiftsPartner, gridBagConstraints);

        jLabelPropertyPartner.setText("Property");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelPropertyPartner, gridBagConstraints);

        jLabelCarsETCPartner.setText("Cars/Caravans/Boats");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelCarsETCPartner, gridBagConstraints);

        jLabelLoansPartner.setText("Loans Owed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelLoansPartner, gridBagConstraints);

        jTextFieldSavingsIPartner.setEditable(false);
        jTextFieldSavingsIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldSavingsIPartner,
                gridBagConstraints);

        jTextFieldManagedFundsIPartner.setEditable(false);
        jTextFieldManagedFundsIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldManagedFundsIPartner,
                gridBagConstraints);

        jTextFieldSharesIPartner.setEditable(false);
        jTextFieldSharesIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldSharesIPartner,
                gridBagConstraints);

        jTextFieldBondsIPartner.setEditable(false);
        jTextFieldBondsIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails
                .add(jTextFieldBondsIPartner, gridBagConstraints);

        jTextFieldGiftsIPartner.setEditable(false);
        jTextFieldGiftsIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldGiftsIPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails
                .add(jTextFieldGiftsIPartner, gridBagConstraints);

        jTextFieldLoansIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoansIPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails
                .add(jTextFieldLoansIPartner, gridBagConstraints);

        jTextFieldSuperannuationIPartner.setEditable(false);
        jTextFieldSuperannuationIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSuperannuationIPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldSuperannuationIPartner,
                gridBagConstraints);

        jLabelIncomePartner.setText("Income");
        jLabelIncomePartner
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIncomePartner.setPreferredSize(new java.awt.Dimension(90, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jLabelIncomePartner, gridBagConstraints);

        jLabelSalaryWagesPartner.setText("Partner Salary/Wages");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelSalaryWagesPartner,
                gridBagConstraints);

        jTextFieldSavingsAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSavingsAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldSavingsAPartner,
                gridBagConstraints);

        jTextFieldManagedFundsAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldManagedFundsAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldManagedFundsAPartner,
                gridBagConstraints);

        jTextFieldSharesAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSharesAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldSharesAPartner,
                gridBagConstraints);

        jTextFieldBondsAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldBondsAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails
                .add(jTextFieldBondsAPartner, gridBagConstraints);

        jTextFieldFixedInterestAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldFixedInterestAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldFixedInterestAPartner,
                gridBagConstraints);

        jTextFieldHomeContentsAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldHomeContentsAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldHomeContentsAPartner,
                gridBagConstraints);

        jTextFieldPropertyAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPropertyAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldPropertyAPartner,
                gridBagConstraints);

        jTextFieldCarsEtcAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCarsEtcAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldCarsEtcAPartner,
                gridBagConstraints);

        jTextFieldGiftsAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldGiftsAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails
                .add(jTextFieldGiftsAPartner, gridBagConstraints);

        jTextFieldLoansAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldLoansAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails
                .add(jTextFieldLoansAPartner, gridBagConstraints);

        jTextFieldSuperannuationAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSuperannuationAPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldSuperannuationAPartner,
                gridBagConstraints);

        jLabelAssetsPartner.setText("Assets");
        jLabelAssetsPartner
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAssetsPartner.setPreferredSize(new java.awt.Dimension(90, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jLabelAssetsPartner, gridBagConstraints);

        jTextFieldTotalAPartner.setEditable(false);
        jTextFieldTotalAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewPartnerDetails
                .add(jTextFieldTotalAPartner, gridBagConstraints);

        jLabelBondsPartner.setText("Bonds/Debentures");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelBondsPartner, gridBagConstraints);

        jButtonAllocatedPensionPartner.setText("Allocated Pension/Annuity");
        jButtonAllocatedPensionPartner.setPreferredSize(new java.awt.Dimension(
                35, 20));
        jButtonAllocatedPensionPartner.setActionCommand("Allocated Pension");
        jButtonAllocatedPensionPartner
                .setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonAllocatedPensionPartner
                .setMargin(new java.awt.Insets(2, 0, 2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelNewPartnerDetails.add(jButtonAllocatedPensionPartner,
                gridBagConstraints);

        jTextFieldAllocatedPensionAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAllocatedPensionAPartner
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldAllocatedPensionAPartner,
                gridBagConstraints);

        jTextFieldAllocatedPensionIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAllocatedPensionIPartner
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldAllocatedPensionIPartner,
                gridBagConstraints);

        jTextFieldTotalIPartner.setEditable(false);
        jTextFieldTotalIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewPartnerDetails
                .add(jTextFieldTotalIPartner, gridBagConstraints);

        jTextFieldPropertyIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPropertyIPartner.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldPropertyIPartner,
                gridBagConstraints);

        jTextFieldFixedInterestIPartner.setEditable(false);
        jTextFieldFixedInterestIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldFixedInterestIPartner,
                gridBagConstraints);

        jTextFieldPartnerSalaryI
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerSalaryI.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldPartnerSalaryI,
                gridBagConstraints);

        jTextFieldComplyingPensionAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldComplyingPensionAPartner
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldComplyingPensionAPartner,
                gridBagConstraints);

        jTextFieldComplyingPensionIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldComplyingPensionIPartner
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(jTextFieldComplyingPensionIPartner,
                gridBagConstraints);

        jLabelTotalPartner.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelNewPartnerDetails.add(jLabelTotalPartner, gridBagConstraints);

        jLabelSuperannuationPartner.setText("Superannuation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelSuperannuationPartner,
                gridBagConstraints);

        jLabelComplyingPensionPartner.setText("Complying Pension/Annuity");
        jLabelComplyingPensionPartner.setPreferredSize(new java.awt.Dimension(
                160, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelComplyingPensionPartner,
                gridBagConstraints);

        jLabelTotalAssetsSubjectToDeemingPartner
                .setText("Partner Assets Subject to Deeming");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelNewPartnerDetails.add(jLabelTotalAssetsSubjectToDeemingPartner,
                gridBagConstraints);

        jTextFieldTotalAssetsSubjectToDeemingAPartner.setEditable(false);
        jTextFieldTotalAssetsSubjectToDeemingAPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewPartnerDetails.add(
                jTextFieldTotalAssetsSubjectToDeemingAPartner,
                gridBagConstraints);

        jTextFieldTotalAssetsSubjectToDeemingIPartner.setEditable(false);
        jTextFieldTotalAssetsSubjectToDeemingIPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewPartnerDetails.add(
                jTextFieldTotalAssetsSubjectToDeemingIPartner,
                gridBagConstraints);

        jLabelClientAssessableIncomeOnPropertyPartner
                .setText("Assessable Income On Property");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(
                jLabelClientAssessableIncomeOnPropertyPartner,
                gridBagConstraints);

        jTextFieldClientAssessableIncomeOnPropertyPartner.setEditable(false);
        jTextFieldClientAssessableIncomeOnPropertyPartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientAssessableIncomeOnPropertyPartner
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelNewPartnerDetails.add(
                jTextFieldClientAssessableIncomeOnPropertyPartner,
                gridBagConstraints);

        jLabelDummyPartner.setText(" ");
        jLabelDummyPartner.setPreferredSize(new java.awt.Dimension(4, 21));
        jLabelDummyPartner.setMinimumSize(new java.awt.Dimension(4, 21));
        jLabelDummyPartner.setMaximumSize(new java.awt.Dimension(4, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelDummyPartner, gridBagConstraints);

        jTextFieldSpacer.setEditable(false);
        jTextFieldSpacer.setForeground(new java.awt.Color(204, 204, 204));
        jTextFieldSpacer
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldSpacer.setPreferredSize(new java.awt.Dimension(2, 26));
        jTextFieldSpacer.setBorder(new javax.swing.border.LineBorder(
                java.awt.Color.lightGray));
        jTextFieldSpacer.setMinimumSize(new java.awt.Dimension(2, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelNewPartnerDetails.add(jTextFieldSpacer, gridBagConstraints);

        jLabelAPAnnuityPartner.setText("AP/Annuity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelNewPartnerDetails.add(jLabelAPAnnuityPartner, gridBagConstraints);

        jPanelLayout.add(jPanelNewPartnerDetails);

        jPanelMeansTestResults.add(jPanelLayout, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(new javax.swing.border.TitledBorder(
                "Means Test Totals"));
        jLabelMeansTestTotalAssets.setText("Total Assets");
        jPanel2.add(jLabelMeansTestTotalAssets);

        jTextFieldMeansTestTotalAssets.setEditable(false);
        jTextFieldMeansTestTotalAssets
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMeansTestTotalAssets.setPreferredSize(new java.awt.Dimension(
                100, 21));
        jPanel2.add(jTextFieldMeansTestTotalAssets);

        jLabelDummy.setPreferredSize(new java.awt.Dimension(200, 21));
        jLabelDummy.setMinimumSize(new java.awt.Dimension(200, 21));
        jLabelDummy.setMaximumSize(new java.awt.Dimension(200, 21));
        jPanel2.add(jLabelDummy);

        jLabelMeansTestTotalIncome.setText("Total Income");
        jPanel2.add(jLabelMeansTestTotalIncome);

        jTextFieldMeansTestTotalIncome.setEditable(false);
        jTextFieldMeansTestTotalIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMeansTestTotalIncome.setPreferredSize(new java.awt.Dimension(
                100, 21));
        jPanel2.add(jTextFieldMeansTestTotalIncome);

        jPanelMeansTestResults.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("Means Test", null, jPanelMeansTestResults, "");

        jPanelNewResults.setLayout(new java.awt.BorderLayout());

        jPanelResult.setLayout(new javax.swing.BoxLayout(jPanelResult,
                javax.swing.BoxLayout.X_AXIS));

        jPanelClientResults.setLayout(new java.awt.GridBagLayout());

        jPanelClientResults.setBorder(new javax.swing.border.TitledBorder(
                "ClientView Results"));
        jLabel31.setText("Maximum Benefit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel31, gridBagConstraints);

        jLabel32.setText("Asset Test Result");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel32, gridBagConstraints);

        jLabel33.setText("Income Test Result");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel33, gridBagConstraints);

        jLabel34.setText("Basic Benefit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel34, gridBagConstraints);

        jTextFieldMaxBenefitC.setEditable(false);
        jTextFieldMaxBenefitC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMaxBenefitC.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldMaxBenefitC, gridBagConstraints);

        jTextFieldAssetTestC.setEditable(false);
        jTextFieldAssetTestC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldAssetTestC, gridBagConstraints);

        jTextFieldIncomeTestC.setEditable(false);
        jTextFieldIncomeTestC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldIncomeTestC, gridBagConstraints);

        jTextFieldPensionerRebateC.setEditable(false);
        jTextFieldPensionerRebateC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldPensionerRebateC, gridBagConstraints);

        jLabel39.setText("Pensioner Rebate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel39, gridBagConstraints);

        jTextFieldBasicBenefitC.setEditable(false);
        jTextFieldBasicBenefitC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldBasicBenefitC, gridBagConstraints);

        jLabel40.setText("Pharmaceutical Allowance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel40, gridBagConstraints);

        jTextFieldPharmAllowanceC.setEditable(false);
        jTextFieldPharmAllowanceC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldPharmAllowanceC, gridBagConstraints);

        jLabel41.setText("Rent Assistance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelClientResults.add(jLabel41, gridBagConstraints);

        jTextFieldRentAssistanceC.setEditable(false);
        jTextFieldRentAssistanceC
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientResults.add(jTextFieldRentAssistanceC, gridBagConstraints);

        jPanelResult.add(jPanelClientResults);

        jPanelPartnerResults.setLayout(new java.awt.GridBagLayout());

        jPanelPartnerResults.setBorder(new javax.swing.border.TitledBorder(
                "Partner Results"));
        jLabel35.setText("Basic Benefit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel35, gridBagConstraints);

        jLabel36.setText("Income Test Result");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel36, gridBagConstraints);

        jLabel37.setText("Asset Test Result");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel37, gridBagConstraints);

        jLabel38.setText("Maximum Benefit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel38, gridBagConstraints);

        jTextFieldMaxBenefitP.setEditable(false);
        jTextFieldMaxBenefitP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMaxBenefitP.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults.add(jTextFieldMaxBenefitP, gridBagConstraints);

        jTextFieldAssetTestP.setEditable(false);
        jTextFieldAssetTestP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults.add(jTextFieldAssetTestP, gridBagConstraints);

        jTextFieldIncomeTestP.setEditable(false);
        jTextFieldIncomeTestP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults.add(jTextFieldIncomeTestP, gridBagConstraints);

        jTextFieldBasicBenefitP.setEditable(false);
        jTextFieldBasicBenefitP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults.add(jTextFieldBasicBenefitP, gridBagConstraints);

        jLabel42.setText("Pharmaceutical Allowance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel42, gridBagConstraints);

        jTextFieldPharmAllowanceP.setEditable(false);
        jTextFieldPharmAllowanceP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults.add(jTextFieldPharmAllowanceP, gridBagConstraints);

        jLabel43.setText("Rent Assistance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel43, gridBagConstraints);

        jTextFieldRentAssistanceP.setEditable(false);
        jTextFieldRentAssistanceP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults.add(jTextFieldRentAssistanceP, gridBagConstraints);

        jLabel44.setText("Pensioner Rebate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPartnerResults.add(jLabel44, gridBagConstraints);

        jTextFieldPensionerRebateP.setEditable(false);
        jTextFieldPensionerRebateP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartnerResults
                .add(jTextFieldPensionerRebateP, gridBagConstraints);

        jPanelResult.add(jPanelPartnerResults);

        jPanelNewResults.add(jPanelResult, java.awt.BorderLayout.CENTER);

        jPanelPensionPayable.setLayout(new java.awt.GridBagLayout());

        jPanelPensionPayable.setBorder(new javax.swing.border.TitledBorder(
                "Pension Payable"));
        jLabelMaxPensionPA.setText("Maximum Pension Payable - per annum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPensionPayable.add(jLabelMaxPensionPA, gridBagConstraints);

        jTextFieldMaxPensionPA.setEditable(false);
        jTextFieldMaxPensionPA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMaxPensionPA
                .setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldMaxPensionPA.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPensionPayable.add(jTextFieldMaxPensionPA, gridBagConstraints);

        jLabelActualPensionPA.setText("Actual Pension Payable - per annum");
        jLabelActualPensionPA
                .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPensionPayable.add(jLabelActualPensionPA, gridBagConstraints);

        jTextFieldlActualPensionPA.setEditable(false);
        jTextFieldlActualPensionPA
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldlActualPensionPA.setPreferredSize(new java.awt.Dimension(100,
                21));
        jTextFieldlActualPensionPA.setMinimumSize(new java.awt.Dimension(100,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPensionPayable
                .add(jTextFieldlActualPensionPA, gridBagConstraints);

        jLabelActualPensionPF.setText("Actual Pension Payable - fortnightly");
        jLabelActualPensionPF
                .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelPensionPayable.add(jLabelActualPensionPF, gridBagConstraints);

        jTextFieldlActualPensionPF.setEditable(false);
        jTextFieldlActualPensionPF
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldlActualPensionPF.setPreferredSize(new java.awt.Dimension(100,
                21));
        jTextFieldlActualPensionPF.setMinimumSize(new java.awt.Dimension(100,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPensionPayable
                .add(jTextFieldlActualPensionPF, gridBagConstraints);

        jPanelNewResults.add(jPanelPensionPayable, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("Results", null, jPanelNewResults, "");

        add(jTabbedPane);

        jPanelControls.setLayout(new javax.swing.BoxLayout(jPanelControls,
                javax.swing.BoxLayout.X_AXIS));

        jButtonReport.setText("Report");
        jButtonReport.setDefaultCapable(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanel6.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel6.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanel6.add(jButtonSave);

        jButtonSaveAs.setText("Save As");
        jButtonSaveAs.setEnabled(false);
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsActionPerformed(evt);
            }
        });

        jPanel6.add(jButtonSaveAs);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanel6.add(jButtonDelete);

        jPanelControls.add(jPanel6);

        jButtonClear.setText("Clear");
        jButtonClear.setDefaultCapable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonClear);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanel7.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanel7.add(jButtonNext);

        jPanelControls.add(jPanel7);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveAsActionPerformed
        // Add your handling code here:
        newModel = true;
        saveView(newModel);
    }// GEN-LAST:event_jButtonSaveAsActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // clearView();
            doClear(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    private void jTextFieldChildrenAmountFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldChildrenAmountFocusLost
        // Add your handling code here:
        String no_childs = this.jTextFieldChildrenAmount.getText();

        if (no_childs != null && no_childs.length() > 0) {
            SwingUtil.setEnabled(jCheckBoxClientNC, false);
        } else {
            SwingUtil.setEnabled(jCheckBoxClientNC, true);
        }
    }// GEN-LAST:event_jTextFieldChildrenAmountFocusLost

    private void jCheckBoxClientSHItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxClientSHItemStateChanged
        if (evt.getSource() != jCheckBoxClientSH)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
            dssCalc2.setSharer(Boolean.TRUE);
        else
            dssCalc2.setSharer(Boolean.FALSE);
    }// GEN-LAST:event_jCheckBoxClientSHItemStateChanged

    private void jCheckBoxClientNCItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxClientNCItemStateChanged
        if (evt.getSource() != jCheckBoxClientNC)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
            dssCalc2.setNoChildren(Boolean.TRUE);
        else
            dssCalc2.setNoChildren(Boolean.FALSE);
    }// GEN-LAST:event_jCheckBoxClientNCItemStateChanged

    private void jButtonAllocatedPensionActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAllocatedPensionActionPerformed
        displayAP();
    }// GEN-LAST:event_jButtonAllocatedPensionActionPerformed

    private void jRadioButtonPartnerMaleItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonPartnerMaleItemStateChanged
        if (evt.getSource() != jRadioButtonPartnerMale)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        dssCalc2
                .setPartnerSexCodeID(jRadioButtonPartnerMale.isSelected() ? SexCode.MALE
                        : SexCode.FEMALE);
    }// GEN-LAST:event_jRadioButtonPartnerMaleItemStateChanged

    private void jRadioButtonClientMaleItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonClientMaleItemStateChanged
        if (evt.getSource() != jRadioButtonClientMale)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        dssCalc2
                .setSexCodeID(jRadioButtonClientMale.isSelected() ? SexCode.MALE
                        : SexCode.FEMALE);
    }// GEN-LAST:event_jRadioButtonClientMaleItemStateChanged

    private void jCheckBoxClientHOItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxClientHOItemStateChanged
        if (evt.getSource() != jCheckBoxClientHO)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
            dssCalc2.setHomeOwner(true);
        else
            dssCalc2.setHomeOwner(false);

    }// GEN-LAST:event_jCheckBoxClientHOItemStateChanged

    private void jComboBoxPartnerBenefitTypeItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxPartnerBenefitTypeItemStateChanged
        if (evt.getSource() != jComboBoxPartnerBenefitType)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            dssCalc2.setPartnerBenefitType(null);
        else
            dssCalc2.setPartnerBenefitType(new BenefitTypeCode().getCodeID(s));
    }// GEN-LAST:event_jComboBoxPartnerBenefitTypeItemStateChanged

    private void jComboBoxClientBenefitTypeItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxClientBenefitTypeItemStateChanged
        if (evt.getSource() != jComboBoxClientBenefitType)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            dssCalc2.setBenefitType(null);
        else
            dssCalc2.setBenefitType(new BenefitTypeCode().getCodeID(s));
    }// GEN-LAST:event_jComboBoxClientBenefitTypeItemStateChanged

    private void jComboBoxClientMaritalStatusItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxClientMaritalStatusItemStateChanged
        if (evt.getSource() != jComboBoxClientMaritalStatus)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            dssCalc2.setMaritalStatus(null);
        else
            dssCalc2.setMaritalStatus(new MaritalCode().getCodeID(s));

        // update status of partner's inputfields
        if (dssCalc2.getMaritalStatus() != null
                && dssCalc2.getMaritalStatus().equals(IMaritalCode.SINGLE)) {
            this.changeStatusOfPartnerInputFields(false);
        } else {
            this.changeStatusOfPartnerInputFields(true);
        }

        if (s != null && s.length() != 0 && !_update_view
                && !_oldClientMaritalStatus.equals(s)) { // old and new
                                                            // status
            checkRequiredFields(true);
            _oldClientMaritalStatus = s;
        }

    }// GEN-LAST:event_jComboBoxClientMaritalStatusItemStateChanged

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        // Add your handling code here:
        newModel = false;
        saveView(newModel);
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int result = BaseView.closeDialog(this);
        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);

        // refresh chart
        /*
         * if ( index == RESULT_PAGE_ID ) {
         * graphView.jPanelOthers.add(jPanelInvestmentStrategy); updateChart(); }
         * else { jPanelRight.add(jPanelInvestmentStrategy); }
         */
    }// GEN-LAST:event_jTabbedPaneStateChanged

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    private static DSSView view;

    public static DSSView display(String title,
            java.awt.event.FocusListener[] listeners) {

        if (view == null) {
            view = new DSSView();
            // Container container =
            SwingUtil.add2Frame(view, listeners, view.getDefaultTitle(),
                    ViewSettings.getInstance().getViewImage(
                            view.getClass().getName()), true, true, false);
        }

        try {
            view.updateView(title);
            SwingUtil.setVisible(view, true);
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            view = null;
        }

        // BEGIN: BUG FIX 577: for not updating a view which is restored from
        // database
        view.updateNonEditable();
        view.updateComponents();
        // END: BUG FIX 577: for not updating a view which is restored from
        // database

        return view;

    }

    /***************************************************************************
     * BaseView protected methods
     **************************************************************************/
    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(newModel);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    public void doDelete(java.awt.event.ActionEvent evt) {
        fireActionEvent(DATA_REMOVE);
    }

    protected void doClear(java.awt.event.ActionEvent evt) {

        try {
            dssCalc2.disableUpdate();
            dssCalc2.clear();
            updateTitle();
        } finally {
            updateEditable();
            dssCalc2.enableUpdate();
            dssCalc2.doUpdate();
        }

    }

    public boolean isModified() {
        // return snapshotCalc.isModified();
        return (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                .isModified())
                || (jButtonSaveAs.isVisible() && jButtonSaveAs.isEnabled() && getModel()
                        .isModified());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxClientNC;

    private javax.swing.JTextField jTextFieldAllocatedPensionI;

    private javax.swing.JLabel jLabelManagedFunds;

    private javax.swing.JLabel jLabelAssetsPartner;

    private javax.swing.JTextField jTextFieldFixedInterestI;

    private javax.swing.JTextField jTextFieldBondsIPartner;

    private javax.swing.JTextField jTextFieldAllocatedPensionA;

    private javax.swing.JLabel jLabelManagedFundsPartner;

    private javax.swing.JTextField jTextFieldBondsAPartner;

    private javax.swing.JTextField jTextFieldFixedInterestA;

    private javax.swing.JTextArea jTextAreaPartnerEC;

    private javax.swing.JTextField jTextFieldMaxPensionPA;

    private javax.swing.JTextField jTextFieldClientAssessableIncomeOnProperty;

    private javax.swing.JLabel jLabelTotalPartner;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JComboBox jComboBoxClientMaritalStatus;

    private javax.swing.JTextField jTextFieldManagedFundsIPartner;

    private javax.swing.JPanel jPanelPartnerResults;

    private javax.swing.JTextField jTextFieldManagedFundsAPartner;

    private javax.swing.JTextField jTextFieldCarsEtcA;

    private javax.swing.JPanel jPanelPartnerSex;

    private javax.swing.JTextField jTextFieldJointTotalAssetsSubjectToDeemingA;

    private javax.swing.JTextField jTextFieldPharmAllowanceP;

    private javax.swing.JTextField jTextFieldMeansTestTotalIncome;

    private javax.swing.JPanel jPanelResult;

    private javax.swing.JTextArea jTextAreaClientEC;

    private javax.swing.JLabel jLabelSavings;

    private javax.swing.JLabel jLabelBondsPartner;

    private javax.swing.JTextField jTextFieldRent;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JTextField jTextFieldPharmAllowanceC;

    private javax.swing.JLabel jLabelTotalAssetsSubjectToDeemingPartner;

    private javax.swing.JTextField jTextFieldPropertyIPartner;

    private javax.swing.JTextField jTextFieldPropertyAPartner;

    private javax.swing.JTextField jTextFieldClientName;

    private javax.swing.JPanel jPanelLayout;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JTextField jTextFieldSavingsAPartner;

    private javax.swing.JTextField jTextFieldSuperannuationI;

    private javax.swing.JTextField jTextFieldComplyingPensionIPartner;

    private javax.swing.JTextField jTextFieldSuperannuationA;

    private javax.swing.JTextField jTextFieldComplyingPensionAPartner;

    private javax.swing.JLabel jLabelShares;

    private javax.swing.JPanel jPanelPartner;

    private javax.swing.JLabel jLabelMeansTestTotalAssets;

    private javax.swing.JTextField jTextFieldSavingsI;

    private javax.swing.JPanel jPanelNewPartnerDetails;

    private javax.swing.JTextField jTextFieldSharesI;

    private javax.swing.JLabel jLabelSalaryWages;

    private javax.swing.JTextField jTextFieldTotalI;

    private javax.swing.JLabel jLabelTotalAssetsSubjectToDeeming;

    private javax.swing.JLabel jLabelSharesPartner;

    private javax.swing.JTextField jTextFieldSavingsA;

    private javax.swing.JTextField jTextFieldSharesA;

    private javax.swing.JTextField jTextFieldGiftsAPartner;

    private javax.swing.JTextField jTextFieldTotalA;

    private javax.swing.JLabel jLabelAllocatedPension;

    private javax.swing.JTextField jTextFieldTotalIPartner;

    private javax.swing.JTextField jTextFieldTotalAPartner;

    private javax.swing.JPanel jPanelClientSex;

    private javax.swing.JLabel jLabelComplyingPension;

    private javax.swing.JButton jButtonAllocatedPension;

    private javax.swing.JLabel jLabelClientAssessableIncomeOnProperty;

    private javax.swing.JTextField jTextFieldChildrenAmount;

    private javax.swing.JPanel jPanelClientResults;

    private com.argus.bean.FDateChooser jTextFieldPartnerCalculationDate;

    private javax.swing.JScrollPane jScrollPanePartner;

    private javax.swing.JButton jButtonAllocatedPensionPartner;

    private javax.swing.JLabel jLabelCarsETCPartner;

    private javax.swing.JLabel jLabelGiftsPartner;

    private javax.swing.JLabel jLabelCarsETC;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JLabel jLabelSalaryWagesPartner;

    private javax.swing.JTextField jTextFieldPensionerRebateP;

    private javax.swing.JLabel jLabelFixedInterestPartner;

    private javax.swing.JPanel jPanelPensionPayable;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabelDummyPartner;

    private javax.swing.JTextField jTextFieldLoansIPartner;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JTextField jTextFieldLoansAPartner;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPaneClientEC;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextField jTextFieldPensionerRebateC;

    private javax.swing.JPanel jPanelClientDetails;

    private javax.swing.JCheckBox jCheckBoxClientHO;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JTextField jTextFieldPartnerName;

    private javax.swing.JLabel jLabelJointTotalAssetsSubjectToDeemingA;

    private javax.swing.JLabel jLabelSuperannuationPartner;

    private javax.swing.JLabel jLabelMaxPensionPA;

    private javax.swing.JTextField jTextFieldSuperannuationAPartner;

    private javax.swing.JTextField jTextFieldAssetTestP;

    private javax.swing.JTextField jTextFieldPartnerSalaryI;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JTextField jTextFieldHomeContentsA;

    private javax.swing.JTextField jTextFieldlActualPensionPF;

    private javax.swing.JLabel jLabelLoans;

    private javax.swing.JTextField jTextFieldLoans;

    private javax.swing.JLabel jLabelBonds;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField jTextFieldlActualPensionPA;

    private javax.swing.JTextField jTextFieldAssetTestC;

    private javax.swing.JTextField jTextFieldClientAssessableIncomeOnPropertyPartner;

    private javax.swing.JLabel jLabelActualPensionPF;

    private javax.swing.JLabel jLabelProperty;

    private javax.swing.JLabel jLabelActualPensionPA;

    private javax.swing.JPanel jPanelNewResults;

    private javax.swing.JComboBox jComboBoxClientBenefitType;

    private javax.swing.JTextField jTextFieldTotalAssetsSubjectToDeemingAPartner;

    private javax.swing.JPanel jPanelNewClientDetails;

    private javax.swing.JTextField jTextFieldManagedFundsI;

    private javax.swing.JTextField jTextFieldPropertyI;

    private javax.swing.JPanel jPanelClient;

    private javax.swing.JTextField jTextFieldSpacer;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JButton jButtonNext;

    private javax.swing.JTextField jTextFieldManagedFundsA;

    private javax.swing.JTextField jTextFieldPropertyA;

    private javax.swing.ButtonGroup bgClientSex;

    private javax.swing.JTextField jTextFieldRentAssistanceP;

    private javax.swing.JTextField jTextFieldTotalAssetsSubjectToDeemingA;

    private javax.swing.JTextField jTextFieldIncomeTestP;

    private javax.swing.JTextField jTextFieldHomeContentsAPartner;

    private javax.swing.JTextField jTextFieldRentAssistanceC;

    private javax.swing.JTextField jTextFieldAllocatedPensionIPartner;

    private javax.swing.JCheckBox jCheckBoxClientSH;

    private javax.swing.JTextField jTextFieldSharesIPartner;

    private javax.swing.JTextField jTextFieldAllocatedPensionAPartner;

    private javax.swing.JTextField jTextFieldSharesAPartner;

    private javax.swing.JTextField jTextFieldIncomeTestC;

    private javax.swing.JLabel jLabel44;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JLabel jLabel42;

    private javax.swing.JLabel jLabel41;

    private javax.swing.JLabel jLabel40;

    private javax.swing.JTextField jTextFieldSavingsIPartner;

    private com.argus.bean.FDateChooser jTextFieldClientCalculationDate;

    private javax.swing.JLabel jLabelSavingsPartner;

    private javax.swing.JLabel jLabelSuperannuation;

    private javax.swing.JTextField jTextFieldFixedInterestIPartner;

    private javax.swing.JTextField jTextFieldBasicBenefitP;

    private javax.swing.JTextField jTextFieldFixedInterestAPartner;

    private javax.swing.JTextField jTextFieldComplyingPensionI;

    private javax.swing.JLabel jLabel39;

    private javax.swing.JTextField jTextFieldComplyingPensionA;

    private javax.swing.JLabel jLabel38;

    private javax.swing.JTextField jTextFieldBasicBenefitC;

    private javax.swing.JLabel jLabelPropertyPartner;

    private javax.swing.JLabel jLabel37;

    private javax.swing.ButtonGroup bgPartnerSex;

    private javax.swing.JLabel jLabel36;

    private javax.swing.JLabel jLabel35;

    private javax.swing.JLabel jLabel34;

    private javax.swing.JLabel jLabelComplyingPensionPartner;

    private javax.swing.JLabel jLabel33;

    private javax.swing.JLabel jLabelMeansTestTotalIncome;

    private javax.swing.JLabel jLabel32;

    private javax.swing.JLabel jLabelIncomePartner;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JTextField jTextFieldGiftsIPartner;

    private javax.swing.JTextField jTextFieldCarsEtcAPartner;

    private javax.swing.JPanel jPanelMeansTestResults;

    private javax.swing.JLabel jLabelAssets;

    private com.argus.bean.FDateChooser jTextFieldPartnerDOB;

    private com.argus.bean.FDateChooser jTextFieldClientDOB;

    private javax.swing.JComboBox jComboBoxPartnerBenefitType;

    private javax.swing.JTextField jTextFieldClientSalaryI;

    private javax.swing.JTextField jTextFieldMeansTestTotalAssets;

    private javax.swing.JLabel jLabelIncome;

    private javax.swing.JLabel jLabelDummy;

    private javax.swing.JRadioButton jRadioButtonPartnerFemale;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JRadioButton jRadioButtonPartnerMale;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.JPanel jPanelPartnerDetails;

    private javax.swing.JTextField jTextFieldMaxBenefitP;

    private javax.swing.JLabel jLabelAPAnnuityPartner;

    private javax.swing.JTextField jTextFieldGiftsI;

    private javax.swing.JLabel jLabelClientAssessableIncomeOnPropertyPartner;

    private javax.swing.JLabel jLabelLoansPartner;

    private javax.swing.JTextField jTextFieldSuperannuationIPartner;

    private javax.swing.JLabel jLabelHomeContentsPartner;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabelGifts;

    private javax.swing.JTextField jTextFieldGiftsA;

    private javax.swing.JTextField jTextFieldMaxBenefitC;

    private javax.swing.JLabel jLabelHomeContents;

    private javax.swing.JPanel jPanelBenefitDetails;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JLabel jLabelRent;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JTextField jTextFieldTotalAssetsSubjectToDeemingIPartner;

    private javax.swing.JTextField jTextFieldTotalAssetsSubjectToDeemingIJoint;

    private javax.swing.JRadioButton jRadioButtonClientMale;

    private javax.swing.JLabel jLabelAPAnnuity;

    private javax.swing.JRadioButton jRadioButtonClientFemale;

    private javax.swing.JTextField jTextFieldLoansI;

    private javax.swing.JLabel jLabelFixedInterest;

    private javax.swing.JTextField jTextFieldBondsI;

    private javax.swing.JTextField jTextFieldBondsA;

    // End of variables declaration//GEN-END:variables

    public void hideControls() {
        jPanelControls.setVisible(false);
    }

    public void showControls() {
        jPanelControls.setVisible(true);
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        /* BEGIN: NEW - 20/08/2002 */
        // set dssCalc2 values, because when the screen is displayed the first
        // time.
        // these values aren't set
        String s = (String) jComboBoxClientMaritalStatus.getSelectedItem();
        dssCalc2.setMaritalStatus(new MaritalCode().getCodeID(s));

        s = (String) jComboBoxClientBenefitType.getSelectedItem();
        dssCalc2.setBenefitType(new BenefitTypeCode().getCodeID(s));

        s = (String) this.jComboBoxPartnerBenefitType.getSelectedItem();
        dssCalc2.setPartnerBenefitType(new BenefitTypeCode().getCodeID(s));

        // set client's "Date of Birth" in dssCalc2
        if (jTextFieldClientDOB.getText() != null
                && jTextFieldClientDOB.getText().length() > 0) {
            try {
                java.util.Date dob = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy").parse(jTextFieldClientDOB.getText());
                if (dob != null) {
                    dssCalc2.setDateOfBirth(dob);
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace(System.err);
            }
        }
        // set partners's "Date of Birth" in dssCalc2
        if (jTextFieldPartnerDOB.getText() != null
                && jTextFieldPartnerDOB.getText().length() > 0) {
            try {
                java.util.Date dob = new java.text.SimpleDateFormat(
                        "dd/MM/yyyy").parse(jTextFieldPartnerDOB.getText());
                if (dob != null) {
                    dssCalc2.setPartnerDateOfBirth(dob);
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace(System.err);
            }
        }

        dssCalc2
                .setSexCodeID(jRadioButtonClientMale.isSelected() ? SexCode.MALE
                        : SexCode.FEMALE);
        dssCalc2
                .setPartnerSexCodeID(jRadioButtonClientFemale.isSelected() ? SexCode.MALE
                        : SexCode.FEMALE);
        /* END: NEW - 20/08/2002 */

        Object source = changeEvent.getSource();

        if (source.getClass().equals(DSSCalc2.class)) {
            updateNonEditable();
            updateComponents();

            if (dssCalc2.isReady()) {
                if (jTabbedPane.getTabCount() == MEANS_TEST_ID)
                    jTabbedPane.addTab("Means Test & Results",
                            jPanelMeansTestResults);
            } else {
                if (jTabbedPane.getTabCount() > MEANS_TEST_ID) {
                    /* BEGIN: comment out - 20/08/2002 */
                    /* jTabbedPane.removeTabAt( MEANS_TEST_ID ); */
                    /* END: comment out - 20/08/2002 */
                }

            }

        } else if (source.getClass().equals(AllocatedPensionCalc.class)) {
            AllocatedPensionCalc apCalc = (AllocatedPensionCalc) source;
            if (!apCalc.isReady())
                return;
            setAP(apCalc);

        }

    }

    private void checkRequiredFields(boolean showMessage) {
        String msg = "";

        Integer marital_code_id = new MaritalCode()
                .getCodeID((String) this.jComboBoxClientMaritalStatus
                        .getSelectedItem());

        // Single
        if (dssCalc2.getDateOfBirth() == null)
            msg += "DOB is required.\n";
        if (dssCalc2.getSexCodeID() == null)
            msg += "Sex is required.\n";
        if (dssCalc2.getCalculationDate() == null)
            msg += "Calculation Date is required.\n";
        if (dssCalc2.getMaritalStatus() == null)
            msg += "Martital Status is required.\n";
        if (dssCalc2.getBenefitType() == null)
            msg += "Benefit Type is required.\n";

        if (marital_code_id != null
                && !marital_code_id.equals(IMaritalCode.SINGLE)) {
            // Married or anything else
            if (dssCalc2.getPartnerDateOfBirth() == null) {
                msg += "Partner's DOB is required.\n";
            }
            if (dssCalc2.getPartnerSexCodeID() == null)
                msg += "Partner's Sex is required.\n";
            if (dssCalc2.getPartnerCalculationDate() == null) {
                msg += "Partner's Calculation Date is required.\n";
            }
            if (dssCalc2.getPartnerBenefitType() == null)
                msg += "Partner's Benefit Type is required.\n";
        }

        if (msg.length() == 0)
            return;

        msg = "The following input fields are required to use the calculator:\n\n"
                + msg;
        if (showMessage) {
            javax.swing.JOptionPane.showMessageDialog(this, msg, "Message",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayAP() {

        PersonService person = clientService;

        try {
            Vector models = person == null ? null : person
                    .getModels(ModelType.ALLOCATED_PENSION);

            if (models != null && apModels == null) {
                apModels = new ListSelection(new CheckBoxList());
                apModels.setListData(models.toArray());
                // populate list
            }

            if (apModels == null || apModels.isEmpty()) {
                // display AP
                AllocatedPensionView view = AllocatedPensionView.displayAP(
                    IMenuCommand.NEW.getSecond().toString(), listeners);
                MoneyCalc calc = view.getCalculationModel();
                calc.addChangeListener(this);
            } else {
                // modal
                SwingUtil.add2Dialog(null, title, true, apModels);

                Model model = (Model) apModels.getSelectedValue();
                if (model != null) {
                    AllocatedPensionCalc apCalc = new AllocatedPensionCalc();
                    apCalc.setModel(model);

                    //
                    setAP(apCalc);
                    updateEditable();
                }

            }

        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
        }

    }

    private void setAP(AllocatedPensionCalc calc) {
        dssCalc2.setAllocatedPensionA(new BigDecimal(calc.getPurchasePrice()));

        BigDecimal annualPaymentAmount = FrequencyCode.getAnnualAmount(calc
                .getRequiredFrequencyCodeID(), calc.getRequiredPaymentAmount());

        if (annualPaymentAmount == null) {
            dssCalc2.setAllocatedPensionI(null);
            return;
        }

        BigDecimal annualPaymentAssesibleAmount = FrequencyCode
                .getAnnualAmount(FrequencyCode.WEEKLY, annualPaymentAmount
                        .subtract(new BigDecimal(calc
                                .getFirstYearWeeklyPaymentAssesibleAmount())));

        dssCalc2.setAllocatedPensionI(annualPaymentAssesibleAmount);

    }

    /***************************************************************************
     * Viewble interface
     **************************************************************************/
    public void clearView() {
        SwingUtil.clear(this);

        doClear(null);
    }

    public void updateView(String modelTitle) throws java.io.IOException {
        this._oldClientMaritalStatus = "";
        this._update_view = true;

        PersonService person = clientService;

        Model m = person == null ? null : person.getModel(getDefaultType(),
                modelTitle);

        if (m == null) {
            updateView(person);
        } else {
            updateView(m);
        }
        updateTitle();

        this._update_view = false;
    }

    public void updateView(Model m) throws java.io.IOException {

        // saveView();

        // doClear(null);

        // use copy of model
        Integer id = m.getId();
        m = new Model(m);
        m.setId(id);

        try {
            dssCalc2.disableUpdate();
            dssCalc2.setModel(m);
            dssCalc2.setSaved();
        } finally {
            updateEditable();
            dssCalc2.doUpdate();
            dssCalc2.enableUpdate();
        }

    }

    private void saveView(boolean newModel) {

        int result = -1;
        String oldTitle = getTitle();
        try {
            if (!newModel)
                result = SaveProjectionDialog.getSaveProjectionInstance().save(
                        dssCalc2, this);
            else
                result = SaveProjectionDialog.getSaveProjectionInstance()
                        .saveAs(dssCalc2, this);

            if (result == SaveProjectionDialog.CANCEL_OPTION)
                return;
            updateTitle();
        } catch (DuplicateException e) {
            String newTitle = getTitle();

            JOptionPane.showMessageDialog(this,
                    "Title already used by another model",
                    "Failed to save model.", JOptionPane.ERROR_MESSAGE);

            // e.printStackTrace( System.err ); // ( msg + '\n' + e.getMessage()
            // );
            return;
        } catch (ModelTitleRestrictionException me) {
            String msg = "Failed to save model.\n  Please ensure total characters for model title is 3 or more.";
            JOptionPane.showMessageDialog(this, msg, "Invalid title",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            saveView(clientService);
        } catch (Exception e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
            return;
        }

    }

    public void updateView(com.argus.financials.service.PersonService person)
            throws ServiceException {

        dssCalc2.disableUpdate();
        try {

            dssCalc2.clear();
            Model m = new Model();
            m.setTypeID(ModelType.SOCIAL_SECURITY_CALC);
            dssCalc2.setModel(m);

        } finally {

            // set default BenfitType for client and partner
            BenefitTypeCode bf = new BenefitTypeCode();
            dssCalc2.setBenefitType(new BenefitTypeCode().getCodeID(bf
                    .getCodeDescription(BenefitTypeCode.AGE_PENSION)));
            dssCalc2.setPartnerBenefitType(new BenefitTypeCode().getCodeID(bf
                    .getCodeDescription(BenefitTypeCode.AGE_PENSION)));

            // enabled editable fields and set calculationdate as current date
            if (person == null) {
                SwingUtil.setEnabled(jTextFieldClientName, true);
                SwingUtil.setEnabled(jTextFieldClientDOB, true);
                SwingUtil.setEnabled(jRadioButtonClientFemale, true);
                SwingUtil.setEnabled(jRadioButtonClientMale, true);
                SwingUtil.setEnabled(jComboBoxClientMaritalStatus, true);

                dssCalc2.setCalculationDate(new Date());
                return;
            }

            // get the existing data for the person
            IPerson personName = person.getPersonName();
            Integer n = null;
            String s = null, m = null;
            java.math.BigDecimal amount = null;

            java.util.Date dob = personName.getDateOfBirth();
            dssCalc2.setDateOfBirth(dob);

            s = personName.getFullName();
            dssCalc2.setClientName(s);

            n = personName.getSex().getId().intValue();

            if (SexCode.FEMALE.equals(n))
                dssCalc2.setSexCodeID(SexCode.FEMALE);
            if (SexCode.MALE.equals(n))
                dssCalc2.setSexCodeID(SexCode.MALE);

            m = personName.getMarital().getCode();
            setjComboBoxClientMaritalStatus(m);
            // jComboBoxClientMaritalStatus.setSelectedItem( m );

            // disabled the filled fields
            /*
             * SwingUtils.setEnabled( jTextFieldClientDOB, dob == null );
             * SwingUtils.setEnabled( jTextFieldClientName, s == null );
             * SwingUtils.setEnabled( jRadioButtonClientFemale, n == null );
             * SwingUtils.setEnabled( jRadioButtonClientMale, n == null );
             * SwingUtils.setEnabled( jComboBoxClientMaritalStatus, m == null );
             */
            dssCalc2.setCalculationDate(new Date());

            // get partner details
            ClientService clientPerson = clientService;

            PersonService partner = clientPerson.getPartner(false);
            if (partner != null) {
                IPerson partnerName = partner.getPersonName();
                dob = partnerName.getDateOfBirth();
                dssCalc2.setPartnerDateOfBirth(dob);

                s = partnerName.getFullName();
                dssCalc2.setPartnerName(s);

                n = partnerName.getSex().getId().intValue();

                if (SexCode.FEMALE.equals(n))
                    dssCalc2.setPartnerSexCodeID(SexCode.FEMALE);
                if (SexCode.MALE.equals(n))
                    dssCalc2.setPartnerSexCodeID(SexCode.MALE);

                m = partnerName.getMarital().getCode();

                // disabled the filled fields
                /*
                 * SwingUtils.setEnabled( jTextFieldPartnerDOB, dob == null );
                 * SwingUtils.setEnabled( jTextFieldPartnerName, s == null );
                 * SwingUtils.setEnabled( jRadioButtonPartnerFemale, n == null );
                 * SwingUtils.setEnabled( jRadioButtonPartnerMale, n == null );
                 */
                dssCalc2.setPartnerCalculationDate(new Date());
            }

            // The assets should be automatically populated using the
            // information
            // within the assets and liabilities data collection area.

            person.findFinancials();

            // get from Cash Asset
            java.util.Map assetCash = person.getFinancials(ASSET_CASH);

            BigDecimal savingsAmount;
            BigDecimal mortgageAmount;
            BigDecimal fixedInterestAmount;
            BigDecimal debenturesInLiquidation;
            BigDecimal fixedDeposits;
            BigDecimal moneyMarketDeposits;
            BigDecimal lifePolicies;
            // BigDecimal cashAtBank;
            BigDecimal cashAccount;
            // BigDecimal fundPendingAssetAccount;
            // BigDecimal memberPendingAssetAccount;

            if (assetCash != null) {

                savingsAmount = Financial.getTotalAmount(new Integer(1),
                        assetCash.values());
                mortgageAmount = Financial.getTotalAmount(new Integer(2),
                        assetCash.values());
                fixedInterestAmount = Financial.getTotalAmount(new Integer(3),
                        assetCash.values());

                // financial types from SV2
                debenturesInLiquidation = Financial.getTotalAmount(new Integer(
                        1001), assetCash.values());
                fixedDeposits = Financial.getTotalAmount(new Integer(1003),
                        assetCash.values());
                moneyMarketDeposits = Financial.getTotalAmount(
                        new Integer(1004), assetCash.values());
                lifePolicies = Financial.getTotalAmount(new Integer(1011),
                        assetCash.values());
                // cashAtBank = Financial.getTotalAmount( new Integer (1014),
                // assetCash.values() );
                cashAccount = Financial.getTotalAmount(new Integer(1015),
                        assetCash.values());
                // fundPendingAssetAccount = Financial.getTotalAmount( new
                // Integer (1016), assetCash.values() );
                // memberPendingAssetAccount = Financial.getTotalAmount( new
                // Integer (1017), assetCash.values() );

                dssCalc2.setSavingsA(savingsAmount.add(mortgageAmount).add(
                        debenturesInLiquidation).add(moneyMarketDeposits).add(
                        lifePolicies).add(cashAccount));

                /*
                 * old version with "cash at bank", "fund pending asset account"
                 * and "member pending asset account" dssCalc2.setSavingsA(
                 * savingsAmount.add( mortgageAmount ).add(
                 * debenturesInLiquidation ).add( moneyMarketDeposits ).add (
                 * lifePolicies ).add( cashAtBank ).add( cashAccount ).add(
                 * fundPendingAssetAccount ).add( memberPendingAssetAccount) );
                 */
                dssCalc2.setFixedInterestA(fixedInterestAmount
                        .add(fixedDeposits));
            }

            // get from Investment Asset
            java.util.Map assetInvestment = person
                    .getFinancials(ASSET_INVESTMENT);

            BigDecimal listSharesAmount;
            BigDecimal listUnitTrustAmount;
            BigDecimal derivativeAmount;
            BigDecimal lifePolicyAmount;

            BigDecimal debentureAmount;
            BigDecimal austGovernmentBondAmount;

            BigDecimal unlistedExternalUnitTrust;
            BigDecimal unlistedInternalUnitTrust;

            // financial types from SV2
            BigDecimal sharesAustralian;
            BigDecimal sharesImputation;
            BigDecimal sharesGrowth;
            BigDecimal listedUnitTrusts;
            BigDecimal unitTrustsInternal;
            BigDecimal unitTrustsNonPSTWholesale;
            BigDecimal unitTrustsNonPSTRetail;

            if (assetInvestment != null) {

                // get shares
                listSharesAmount = AssetInvestment.getTotalAmount(
                        new Integer(4), assetInvestment.values());
                listUnitTrustAmount = AssetInvestment.getTotalAmount(
                        new Integer(5), assetInvestment.values());
                derivativeAmount = AssetInvestment.getTotalAmount(new Integer(
                        10), assetInvestment.values());
                lifePolicyAmount = AssetInvestment.getTotalAmount(new Integer(
                        11), assetInvestment.values());

                // get bonds
                debentureAmount = AssetInvestment.getTotalAmount(
                        new Integer(8), assetInvestment.values());
                austGovernmentBondAmount = AssetInvestment.getTotalAmount(
                        new Integer(9), assetInvestment.values());

                // get managed funds
                unlistedExternalUnitTrust = AssetInvestment.getTotalAmount(
                        new Integer(6), assetInvestment.values());
                unlistedInternalUnitTrust = AssetInvestment.getTotalAmount(
                        new Integer(7), assetInvestment.values());

                // get values from SV2
                sharesAustralian = AssetInvestment.getTotalAmount(new Integer(
                        1005), assetInvestment.values());
                sharesImputation = AssetInvestment.getTotalAmount(new Integer(
                        1006), assetInvestment.values());
                sharesGrowth = AssetInvestment.getTotalAmount(
                        new Integer(1007), assetInvestment.values());
                listedUnitTrusts = AssetInvestment.getTotalAmount(new Integer(
                        1008), assetInvestment.values());
                unitTrustsInternal = AssetInvestment.getTotalAmount(
                        new Integer(1010), assetInvestment.values());
                unitTrustsNonPSTWholesale = AssetInvestment.getTotalAmount(
                        new Integer(1012), assetInvestment.values());
                unitTrustsNonPSTRetail = AssetInvestment.getTotalAmount(
                        new Integer(1013), assetInvestment.values());

                // set dssCalc2 values
                dssCalc2.setSharesA(listSharesAmount.add(listUnitTrustAmount)
                        .add(derivativeAmount).add(lifePolicyAmount).add(
                                sharesAustralian).add(sharesImputation).add(
                                sharesGrowth));
                dssCalc2.setBondsA(debentureAmount
                        .add(austGovernmentBondAmount));
                dssCalc2.setManagedFundsA(unlistedExternalUnitTrust.add(
                        unlistedInternalUnitTrust).add(listedUnitTrusts).add(
                        unitTrustsInternal).add(unitTrustsNonPSTWholesale).add(
                        unitTrustsNonPSTRetail));
            }

            // get from Personal Asset
            java.util.Map assetPersonal = person.getFinancials(ASSET_PERSONAL);

            BigDecimal investmentProperty;
            BigDecimal automobile;
            BigDecimal motorBike;
            BigDecimal recreationalVehicle;
            BigDecimal sportingEquipment;
            BigDecimal jewelry;
            BigDecimal homeFurnishings;
            BigDecimal toolsAndMachinery;

            if (assetPersonal != null) {

                // get property
                investmentProperty = AssetPersonal.getTotalAmount(new Integer(
                        12), assetPersonal.values());

                // get CarsETC
                automobile = AssetPersonal.getTotalAmount(new Integer(14),
                        assetPersonal.values());
                motorBike = AssetPersonal.getTotalAmount(new Integer(15),
                        assetPersonal.values());
                recreationalVehicle = AssetPersonal.getTotalAmount(new Integer(
                        16), assetPersonal.values());

                // get home contents
                sportingEquipment = AssetPersonal.getTotalAmount(
                        new Integer(17), assetPersonal.values());
                jewelry = AssetPersonal.getTotalAmount(new Integer(18),
                        assetPersonal.values());
                homeFurnishings = AssetPersonal.getTotalAmount(new Integer(19),
                        assetPersonal.values());
                toolsAndMachinery = AssetPersonal.getTotalAmount(
                        new Integer(20), assetPersonal.values());

                // set dssCalc2 values
                dssCalc2.setPropertyA(investmentProperty);
                dssCalc2.setCarsEtcA(automobile.add(motorBike).add(
                        recreationalVehicle));
                dssCalc2.setHomeContentsA(sportingEquipment.add(jewelry).add(
                        homeFurnishings).add(toolsAndMachinery));
            }

            // get from Superannuation Asset
            java.util.Map assetSuperannuation = person
                    .getFinancials(ASSET_SUPERANNUATION);

            BigDecimal superannuationAccount;
            BigDecimal pensionAccount;
            BigDecimal annuityPolicy;

            if (assetSuperannuation != null) {

                // get superannuation
                superannuationAccount = AssetSuperannuation.getTotalAmount(
                        new Integer(21), assetSuperannuation.values());

                // get pension
                pensionAccount = AssetSuperannuation.getTotalAmount(
                        new Integer(22), assetSuperannuation.values());

                // get annuity policy
                annuityPolicy = AssetSuperannuation.getTotalAmount(new Integer(
                        23), assetSuperannuation.values());

                // set dssCalc2 values
                dssCalc2.setSuperannuationA(superannuationAccount);
                dssCalc2.setAllocatedPensionA(pensionAccount);
                dssCalc2.setComplyingPensionA(annuityPolicy);
            }

        }
        dssCalc2.enableUpdate();
        dssCalc2.doUpdate();
        updateEditable();
    }

    public void saveView(PersonService person) throws ServiceException,
            InvalidCodeException {

        java.io.StringWriter w = new java.io.StringWriter();
        java.io.BufferedWriter output = new java.io.BufferedWriter(w);

        try {
            IOUtils2.writeHeader(this, output);
            IOUtils2.write(this, output);

            output.flush();

            getModel().setData(w.toString());

        } catch (java.io.IOException e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (java.io.IOException e) { /* ignore by now */
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (java.io.IOException e) { /* ignore by now */
                }
            }
        }

        person.addModel(getModel());

        person.storeModels();
        // dssCalc2.setSaved();

    }

    /**
     * helper methods
     */
    public void save() {

    }

    public void updateEditable() {

        if (dssCalc2 == null)
            return;

        BigDecimal d;
        String s;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        if (SexCode.MALE.equals(dssCalc2.getSexCodeID()))
            jRadioButtonClientMale.setSelected(true);
        else
            jRadioButtonClientFemale.setSelected(true);

        if (SexCode.MALE.equals(dssCalc2.getPartnerSexCodeID()))
            jRadioButtonPartnerMale.setSelected(true);
        else
            jRadioButtonPartnerFemale.setSelected(true);

        s = dssCalc2.getClientName();
        jTextFieldClientName.setText(s);

        s = DateTimeUtils.asString(dssCalc2.getDateOfBirth());
        jTextFieldClientDOB.setText(s);

        s = DateTimeUtils.asString(dssCalc2.getCalculationDate());
        jTextFieldClientCalculationDate.setText(s);

        s = dssCalc2.getPartnerName();
        jTextFieldPartnerName.setText(s);

        s = DateTimeUtils.asString(dssCalc2.getPartnerDateOfBirth());
        jTextFieldPartnerDOB.setText(s);

        d = dssCalc2.getRent();
        jTextFieldRent.setText(curr.toString(d));

        s = dssCalc2.getChildrenAmount() == null ? null : dssCalc2
                .getChildrenAmount().toString();
        jTextFieldChildrenAmount.setText(s);

        // update "No Children" CheckBox
        if (s != null && s.length() > 0) {
            SwingUtil.setEnabled(jCheckBoxClientNC, false);
        } else {
            SwingUtil.setEnabled(jCheckBoxClientNC, true);
        }

        s = DateTimeUtils.asString(dssCalc2.getPartnerCalculationDate());
        jTextFieldPartnerCalculationDate.setText(s);

        d = dssCalc2.getSavingsA();
        jTextFieldSavingsA.setText(curr.toString(d));

        d = dssCalc2.getManagedFundsA();
        jTextFieldManagedFundsA.setText(curr.toString(d));

        d = dssCalc2.getSharesA();
        jTextFieldSharesA.setText(curr.toString(d));

        d = dssCalc2.getBondsA();
        jTextFieldBondsA.setText(curr.toString(d));

        d = dssCalc2.getFixedInterestA();
        jTextFieldFixedInterestA.setText(curr.toString(d));

        d = dssCalc2.getHomeContentsA();
        jTextFieldHomeContentsA.setText(curr.toString(d));

        d = dssCalc2.getCarsEtcA();
        jTextFieldCarsEtcA.setText(curr.toString(d));

        d = dssCalc2.getPropertyA();
        jTextFieldPropertyA.setText(curr.toString(d));

        d = dssCalc2.getGiftsA();
        jTextFieldGiftsA.setText(curr.toString(d));

        d = dssCalc2.getLoansA();
        jTextFieldLoans.setText(curr.toString(d));

        d = dssCalc2.getPropertyI();
        jTextFieldPropertyI.setText(curr.toString(d));

        d = dssCalc2.getLoansI();
        jTextFieldLoansI.setText(curr.toString(d));

        d = dssCalc2.getSuperannuationA();
        jTextFieldSuperannuationA.setText(curr.toString(d));

        d = dssCalc2.getComplyingPensionA();
        jTextFieldComplyingPensionA.setText(curr.toString(d));

        d = dssCalc2.getComplyingPensionI();
        jTextFieldComplyingPensionI.setText(curr.toString(d));

        d = dssCalc2.getAllocatedPensionA();
        jTextFieldAllocatedPensionA.setText(curr.toString(d));

        d = dssCalc2.getAllocatedPensionI();
        jTextFieldAllocatedPensionI.setText(curr.toString(d));

        d = dssCalc2.getClientSalaryI();
        jTextFieldClientSalaryI.setText(curr.toString(d));

        d = dssCalc2.getSalaryIPartner();
        jTextFieldPartnerSalaryI.setText(curr.toString(d));

        //
        // partner details
        //
        d = dssCalc2.getSavingsAPartner();
        jTextFieldSavingsAPartner.setText(curr.toString(d));

        d = dssCalc2.getManagedFundsAPartner();
        jTextFieldManagedFundsAPartner.setText(curr.toString(d));

        d = dssCalc2.getSharesAPartner();
        jTextFieldSharesAPartner.setText(curr.toString(d));

        d = dssCalc2.getBondsAPartner();
        jTextFieldBondsAPartner.setText(curr.toString(d));

        d = dssCalc2.getFixedInterestAPartner();
        jTextFieldFixedInterestAPartner.setText(curr.toString(d));

        d = dssCalc2.getHomeContentsAPartner();
        jTextFieldHomeContentsAPartner.setText(curr.toString(d));

        d = dssCalc2.getCarsEtcAPartner();
        jTextFieldCarsEtcAPartner.setText(curr.toString(d));

        d = dssCalc2.getPropertyAPartner();
        jTextFieldPropertyAPartner.setText(curr.toString(d));

        d = dssCalc2.getGiftsAPartner();
        jTextFieldGiftsAPartner.setText(curr.toString(d));

        d = dssCalc2.getLoansAPartner();
        jTextFieldLoansAPartner.setText(curr.toString(d));

        d = dssCalc2.getPropertyIPartner();
        jTextFieldPropertyIPartner.setText(curr.toString(d));

        d = dssCalc2.getLoansIPartner();
        jTextFieldLoansIPartner.setText(curr.toString(d));

        d = dssCalc2.getSuperannuationAPartner();
        jTextFieldSuperannuationAPartner.setText(curr.toString(d));

        d = dssCalc2.getComplyingPensionAPartner();
        jTextFieldComplyingPensionAPartner.setText(curr.toString(d));

        d = dssCalc2.getComplyingPensionIPartner();
        jTextFieldComplyingPensionIPartner.setText(curr.toString(d));

        d = dssCalc2.getAllocatedPensionAPartner();
        jTextFieldAllocatedPensionAPartner.setText(curr.toString(d));

        d = dssCalc2.getAllocatedPensionIPartner();
        jTextFieldAllocatedPensionIPartner.setText(curr.toString(d));

        d = dssCalc2.getSalaryIPartner();
        jTextFieldPartnerSalaryI.setText(curr.toString(d));

    }

    public void updateNonEditable() {

        BigDecimal d;
        String s = null;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();
        Integer i;

        if (SexCode.MALE.equals(dssCalc2.getSexCodeID()))
            jRadioButtonClientMale.setSelected(true);
        else
            jRadioButtonClientFemale.setSelected(true);

        jComboBoxClientMaritalStatus.setSelectedItem(dssCalc2
                .getMaritalStatusDesc());

        // update status of partner's inputfields
        if (dssCalc2.getMaritalStatus() != null
                && dssCalc2.getMaritalStatus().equals(IMaritalCode.SINGLE)) {
            this.changeStatusOfPartnerInputFields(false);
        } else {
            this.changeStatusOfPartnerInputFields(true);
        }

        jComboBoxClientBenefitType.setSelectedItem(dssCalc2
                .getBenefitTypeDesc());
        jCheckBoxClientNC.setSelected(Boolean.TRUE.equals(dssCalc2
                .getNoChildren()));
        jCheckBoxClientHO.setSelected(dssCalc2.getHomeOwner());

        jCheckBoxClientSH
                .setSelected(Boolean.TRUE.equals(dssCalc2.getSharer()));

        if (SexCode.MALE.equals(dssCalc2.getPartnerSexCodeID()))
            jRadioButtonPartnerMale.setSelected(true);
        else
            jRadioButtonPartnerFemale.setSelected(true);

        jComboBoxPartnerBenefitType.setSelectedItem(dssCalc2
                .getPartnerBenefitTypeDesc());

        /*
         * boolean en = MaritalCode.MARRIED.equals (dssCalc2.getMaritalStatus()) ||
         * MaritalCode.DEFACTO.equals (dssCalc2.getMaritalStatus());
         */
        // BEGIN: BUG FIX 577 - 15/07/2002
        // by shibaevv
        //        
        // only commented out the following lines of source code:
        // SwingUtils.setEnabled( jTextFieldPartnerName,
        // dssCalc2.getPartnerName() == null );
        // SwingUtils.setEnabled( jTextFieldPartnerDOB,
        // dssCalc2.getPartnerDateOfBirth() == null );
        // SwingUtils.setEnabled( jRadioButtonPartnerMale,
        // dssCalc2.getPartnerSexCodeID() == null );
        // SwingUtils.setEnabled( jRadioButtonPartnerFemale,
        // dssCalc2.getPartnerSexCodeID() == null );
        // SwingUtils.setEnabled( jComboBoxPartnerBenefitType,
        // dssCalc2.getPartnerBenefitType() == null );
        //        
        // SwingUtils.setEnabled( jRadioButtonClientMale,
        // dssCalc2.getSexCodeID() == null );
        // SwingUtils.setEnabled( jRadioButtonClientFemale,
        // dssCalc2.getSexCodeID() == null );
        // SwingUtils.setEnabled( jComboBoxClientMaritalStatus,
        // dssCalc2.getMaritalStatus() == null );
        // SwingUtils.setEnabled( jComboBoxPartnerBenefitType,
        // dssCalc2.getBenefitType() == null );
        //
        // END: BUG FIX 577 - 15/07/2002
        /*
         * OLD-VERSION: SwingUtils.setEnabled( jTextFieldClientName,
         * dssCalc2.getClientName() == null ); SwingUtils.setEnabled(
         * jTextFieldClientDOB, dssCalc2.getDateOfBirth() == null );
         */
        // BEGIN: BUG FIX 577 - 10/07/2002
        // by shibaevv
        //        
        // only commented out two lines of source code:
        // SwingUtils.setEnabled( jTextFieldClientName, dssCalc2.getClientName()
        // == null );
        // SwingUtils.setEnabled( jTextFieldClientDOB, dssCalc2.getDateOfBirth()
        // == null );
        //        
        // END: BUG FIX 577 - 10/07/2002

        /* */
        SwingUtil
                .setEnabled(jCheckBoxClientSH, !jCheckBoxClientHO.isSelected());
        SwingUtil
                .setEnabled(jCheckBoxClientHO, !jCheckBoxClientSH.isSelected());
        SwingUtil.setEnabled(jTextFieldRent, !jCheckBoxClientHO.isSelected());
        SwingUtil.setEnabled(jTextFieldChildrenAmount, !jCheckBoxClientNC
                .isSelected());
        /* */

        i = dssCalc2.getBenefitType();
        if (BenefitTypeCode.NONE.equals(i))
            jTextAreaClientEC.setText(TaxUtils.CLIENT_NONE);
        else if (BenefitTypeCode.AGE_PENSION.equals(i)) {
            if (dssCalc2.entitledForAgePension())
                jTextAreaClientEC
                        .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_AGE_PENSION);
            else
                jTextAreaClientEC
                        .setText("Sorry, you are not entitled for Age Pension.");
        } else if (BenefitTypeCode.NEW_START_ALLOWANCE.equals(i)) {
            // check client's age
            if (dssCalc2.entitledForNewStartAllowance()) {
                jTextAreaClientEC
                        .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_NEWSTART_ALLOWANCE);
            } else {
                jTextAreaClientEC
                        .setText("Sorry, you are not entitled for a Newstart Allowance.");
            }
        } else if (BenefitTypeCode.DISABILITY_SUPPORT_PENSION.equals(i)) {
            jTextAreaClientEC
                    .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_DISABILITY_SUPPORT_PENSION);
        } else if (BenefitTypeCode.MATURE_AGE_ALLOWANCE.equals(i)) {
            jTextAreaClientEC
                    .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_MATURE_AGE_ALLOWANCE);
        } else if (BenefitTypeCode.PARTNER_ALLOWANCE.equals(i)) {
            jTextAreaClientEC
                    .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_PARTNER_ALLOWANCE);
        }

        i = dssCalc2.getPartnerBenefitType();
        if (BenefitTypeCode.NONE.equals(i))
            jTextAreaPartnerEC.setText(TaxUtils.PARTNER_NONE);
        else if (BenefitTypeCode.AGE_PENSION.equals(i)) {
            if (dssCalc2.entitledForAgePensionPartner())
                jTextAreaPartnerEC
                        .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_AGE_PENSION);
            else
                jTextAreaPartnerEC
                        .setText("Sorry, you are not entitled for Age Pension.");
        } else if (BenefitTypeCode.NEW_START_ALLOWANCE.equals(i)) {
            // check client's age
            if (dssCalc2.entitledForNewStartAllowancePartner()) {
                jTextAreaPartnerEC
                        .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_NEWSTART_ALLOWANCE);
            } else {
                jTextAreaPartnerEC
                        .setText("Sorry, you are not entitled for a Newstart Allowance.");
            }
        } else if (BenefitTypeCode.DISABILITY_SUPPORT_PENSION.equals(i)) {
            jTextAreaPartnerEC
                    .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_DISABILITY_SUPPORT_PENSION);
        } else if (BenefitTypeCode.MATURE_AGE_ALLOWANCE.equals(i)) {
            jTextAreaPartnerEC
                    .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_MATURE_AGE_ALLOWANCE);
        } else if (BenefitTypeCode.PARTNER_ALLOWANCE.equals(i)) {
            jTextAreaPartnerEC
                    .setText(TaxUtils.BASIC_CONDITIONS_OF_ELIGIBILITY_PARTNER_ALLOWANCE);
        }

        d = dssCalc2.getTotalAClient();
        jTextFieldTotalA.setText(curr.toString(d));

        d = dssCalc2.getSavingsI();
        jTextFieldSavingsI.setText(curr.toString(d));

        d = dssCalc2.getManagedFundsI();
        jTextFieldManagedFundsI.setText(curr.toString(d));

        d = dssCalc2.getSharesI();
        jTextFieldSharesI.setText(curr.toString(d));

        d = dssCalc2.getBondsI();
        jTextFieldBondsI.setText(curr.toString(d));

        d = dssCalc2.getFixedInterestI();
        jTextFieldFixedInterestI.setText(curr.toString(d));

        d = dssCalc2.getTotalAssetsSubjectToDeemingAClient();
        jTextFieldTotalAssetsSubjectToDeemingA.setText(curr.toString(d));

        d = dssCalc2.getTotalAssetsSubjectToDeemingIClient();
        jTextFieldTotalAssetsSubjectToDeemingIJoint.setText(curr.toString(d));

        d = dssCalc2.getAssessableIncomeOnPropertyClient();
        jTextFieldClientAssessableIncomeOnProperty.setText(curr.toString(d));

        d = dssCalc2.getTotalAssetsSubjectToDeemingAPartner();
        jTextFieldTotalAssetsSubjectToDeemingAPartner.setText(curr.toString(d));

        d = dssCalc2.getAssessableIncomeOnPropertyPartner();
        jTextFieldClientAssessableIncomeOnPropertyPartner.setText(curr
                .toString(d));

        d = dssCalc2.getGiftsI();
        jTextFieldGiftsI.setText(curr.toString(d));

        d = dssCalc2.getSuperannuationI();
        jTextFieldSuperannuationI.setText(curr.toString(d));

        // d = dssCalc2.getTotalI();
        d = dssCalc2.getTotalIClient();
        jTextFieldTotalI.setText(curr.toString(d));

        d = dssCalc2.getMaxBenefitC();
        jTextFieldMaxBenefitC.setText(curr.toString(d));

        d = dssCalc2.getAssetTestC();
        jTextFieldAssetTestC.setText(curr.toString(d));

        d = dssCalc2.getIncomeTestC();
        jTextFieldIncomeTestC.setText(curr.toString(d));

        d = dssCalc2.getBasicBenefitC();
        jTextFieldBasicBenefitC.setText(curr.toString(d));

        d = dssCalc2.getPharmAllowanceC();
        jTextFieldPharmAllowanceC.setText(curr.toString(d));

        d = dssCalc2.getRentAssistanceC();
        jTextFieldRentAssistanceC.setText(curr.toString(d));

        d = dssCalc2.getPensionerRebateC();
        jTextFieldPensionerRebateC.setText(curr.toString(d));

        if (dssCalc2.getMaritalStatus() != null) {
            if (!dssCalc2.getMaritalStatus().equals(IMaritalCode.SINGLE)) {
                d = dssCalc2.getMaxBenefitP();
                jTextFieldMaxBenefitP.setText(curr.toString(d));

                d = dssCalc2.getAssetTestP();
                jTextFieldAssetTestP.setText(curr.toString(d));

                d = dssCalc2.getIncomeTestP();
                jTextFieldIncomeTestP.setText(curr.toString(d));

                d = dssCalc2.getBasicBenefitP();
                jTextFieldBasicBenefitP.setText(curr.toString(d));

                d = dssCalc2.getPharmAllowanceP();
                jTextFieldPharmAllowanceP.setText(curr.toString(d));

                d = dssCalc2.getRentAssistanceP();
                jTextFieldRentAssistanceP.setText(curr.toString(d));

                d = dssCalc2.getPensionerRebateP();
                jTextFieldPensionerRebateP.setText(curr.toString(d));
            } else {
                jTextFieldMaxBenefitP.setText("");
                jTextFieldAssetTestP.setText("");
                jTextFieldIncomeTestP.setText("");
                jTextFieldBasicBenefitP.setText("");
                jTextFieldPharmAllowanceP.setText("");
                jTextFieldRentAssistanceP.setText("");
                jTextFieldPensionerRebateP.setText("");
            }
        }

        // 
        // partner details
        //
        d = dssCalc2.getTotalAPartner();
        jTextFieldTotalAPartner.setText(curr.toString(d));

        d = dssCalc2.getSavingsIPartner();
        jTextFieldSavingsIPartner.setText(curr.toString(d));

        d = dssCalc2.getManagedFundsIPartner();
        jTextFieldManagedFundsIPartner.setText(curr.toString(d));

        d = dssCalc2.getSharesIPartner();
        jTextFieldSharesIPartner.setText(curr.toString(d));

        d = dssCalc2.getBondsIPartner();
        jTextFieldBondsIPartner.setText(curr.toString(d));

        d = dssCalc2.getFixedInterestIPartner();
        jTextFieldFixedInterestIPartner.setText(curr.toString(d));

        d = dssCalc2.getGiftsIPartner();
        jTextFieldGiftsIPartner.setText(curr.toString(d));

        d = dssCalc2.getSuperannuationIPartner();
        jTextFieldSuperannuationIPartner.setText(curr.toString(d));

        d = dssCalc2.getTotalIPartner();
        jTextFieldTotalIPartner.setText(curr.toString(d));

        // Total Joint Assessable Assets
        d = dssCalc2.getTotalAssetsSubjectToDeemingAJoint();
        jTextFieldJointTotalAssetsSubjectToDeemingA.setText(curr.toString(d));

        // Means Test Totals
        d = dssCalc2.getTotalAForAssetsTestJoint();
        jTextFieldMeansTestTotalAssets.setText(curr.toString(d));

        d = dssCalc2.getTotalIJoint();
        jTextFieldMeansTestTotalIncome.setText(curr.toString(d));

        // get pensions payable
        d = dssCalc2.getMaxAgePension();
        jTextFieldMaxPensionPA.setText(curr.toString(d));

        d = dssCalc2.getActualAgePensionPA();
        jTextFieldlActualPensionPA.setText(curr.toString(d));

        d = dssCalc2.getActualAgePensionPF();
        jTextFieldlActualPensionPF.setText(curr.toString(d));

    }

    private void changeStatusOfPartnerInputFields(boolean status) {
        SwingUtil.setEnabled(this.jTextFieldPartnerName, status);
        SwingUtil.setEnabled(this.jTextFieldPartnerDOB, status);
        SwingUtil.setEnabled(this.jRadioButtonPartnerMale, status);
        SwingUtil.setEnabled(this.jRadioButtonPartnerFemale, status);
        SwingUtil.setEnabled(this.jTextFieldPartnerCalculationDate, status);
        SwingUtil.setEnabled(this.jComboBoxPartnerBenefitType, status);
    }

    /**
     * The methode maps all existing marital codes to single, partnered,
     * seperated-health and <EMPTY>. <BR>
     * </BR>
     * 
     * <PRE>
     * 
     * Where client_details.marital_status = Divorced, Single, Widowed
     * SS.martial status = Single Where client_details.marital_status =
     * De-facto, Married SS.martial status = Partnered Where
     * client_details.marital_status = Separated - Health SS.martial status =
     * Separated - Health Where client_details.marital_status = Separated
     * SS.martial status will be blank and administrator will need to set this.
     * Where client_details.marital_status = unknown SS.martial status will be
     * blank and administrator will need to set this.
     * 
     * </PRE>
     * 
     * @param status -
     *            marital status as a String
     */
    private void setjComboBoxClientMaritalStatus(String status) {
        Integer i = new MaritalCode().getCodeID(status);

        if (i == null) {
            // unknown marital status ==> <VALUE_NONE>
            dssCalc2.setMaritalStatus(MaritalCode.VALUE_NONE);
        } else {
            if (i.equals(IMaritalCode.DIVORCED)
             || i.equals(IMaritalCode.SINGLE)
             || i.equals(IMaritalCode.WIDOWED)) {

                dssCalc2.setMaritalStatus(IMaritalCode.SINGLE);
            } else if (i.equals(IMaritalCode.DEFACTO)
                    || i.equals(IMaritalCode.MARRIED)
                    || i.equals(IMaritalCode.PARTNERED)) {

                dssCalc2.setMaritalStatus(IMaritalCode.PARTNERED);
            } else if (i.equals(IMaritalCode.SEPARATED_HEALTH)) {

                dssCalc2.setMaritalStatus(IMaritalCode.SEPARATED_HEALTH);
            } else if (i.equals(IMaritalCode.SEPARATED)) {

                dssCalc2.setMaritalStatus(MaritalCode.VALUE_NONE);
            } else {
                // unknown marital status ==> <VALUE_NONE>
                dssCalc2.setMaritalStatus(MaritalCode.VALUE_NONE);
            }
        }

        jComboBoxClientMaritalStatus.setSelectedItem(dssCalc2
                .getMaritalStatusDesc());

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getDSSReport();
    }

    public ReportFields getReportData(com.argus.financials.service.PersonService person)
            throws java.io.IOException {

        DSSData dssdata = new DSSData();
        dssdata.init(person, dssCalc2);

        ReportFields reportFields = ReportFields.getInstance();
        dssdata.initializeReportData(reportFields);

        return reportFields;

    }

    protected void doReport() {

        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(clientService),
                    getDefaultReport());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private String removeComma(String str) {
        boolean found = false;
        char c;
        StringBuffer help = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c != ',') {
                help.append(c);
            }
        }

        return help.toString();
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
                    person.removeModel(getModel());
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

}
