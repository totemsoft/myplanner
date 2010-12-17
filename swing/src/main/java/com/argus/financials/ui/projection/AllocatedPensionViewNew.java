/*
 * AllocatedPensionViewNew.java
 *
 * Created on 28 August 2002, 14:01
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author shibaevv
 */

import java.awt.Cursor;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.argus.beans.format.CurrencyLabelGenerator;
import com.argus.financials.IGraphView;
import com.argus.financials.code.APRelationshipCode;
import com.argus.financials.code.APRelationshipCodeID;
import com.argus.financials.code.DeathBenefitCode;
import com.argus.financials.code.DeathBenefitCodeID;
import com.argus.financials.code.GeneralTaxExemptionCode;
import com.argus.financials.code.GeneralTaxExemptionCodeID;
import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.code.InvestmentStrategyCode;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.code.SelectedAnnualPensionCode;
import com.argus.financials.code.SexCode;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.etc.PersonName;
import com.argus.financials.io.IOUtils2;
import com.argus.financials.projection.APGraphViewNew;
import com.argus.financials.projection.AllocatedPensionCalcNew;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.data.ETPConstants;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceException;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.ProjectToAgeInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.financials.ui.ListenerUtils;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.swing.SwingUtils;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public class AllocatedPensionViewNew extends javax.swing.JPanel implements
        ActionEventID, javax.swing.event.ChangeListener,
        com.argus.financials.swing.ICloseDialog {

    private static final int RESULT_PAGE_ID = 1;

    private AllocatedPensionCalcNew apCalc = new AllocatedPensionCalcNew();

    private APGraphViewNew graphView;

    private boolean newModel;

    private static com.argus.math.Money money;
    static {
        money = new com.argus.math.Money();
        money.setMaximumFractionDigits(0);
        money.setMinimumFractionDigits(0);
    }

    private java.awt.event.FocusListener[] listeners;

    /** Creates new form AllocatedPensionViewNew */
    public AllocatedPensionViewNew() {
        this(new AllocatedPensionCalcNew());
    }

    public AllocatedPensionViewNew(MoneyCalc calc) {
        apCalc = (AllocatedPensionCalcNew) calc;
        initComponents();
        // for saving dialog window

        initComponents2();
    }

    public static AllocatedPensionViewNew getInstance() {
        if (view == null)
            view = new AllocatedPensionViewNew();
        return view;
    }

    public Integer getDefaultType() {
        return ModelTypeID.rcALLOCATED_PENSION.getCodeIDInteger();

    }

    public String getDefaultTitle() {
        return ModelType.rcALLOCATED_PENSION.getCodeDesc();
    }

    protected Model getModel() {
        Model model = apCalc.getModel();
        if (model.getOwner() != null)
            return model;

        PersonService person = ServiceLocator.getInstance().getClientPerson();
        if (person != null) {
            try {
                model.setOwner(person.getModels());
            } catch (com.argus.financials.service.ServiceException e) {
                e.printStackTrace(System.err);
            }
        }
        return model;
    }

    public AllocatedPensionCalcNew getCalculationModel() {
        return apCalc;
    }

    public void setCalculationModel(AllocatedPensionCalcNew calculator) {
        apCalc = calculator;
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void updateTitle() {
        SwingUtil.setTitle(this, getTitle());
    }

    public void setTitle(String value) {
        try {
            getModel().setTitle(value);
        } catch (DuplicateException e) {
            String msg = "Failed to set model title.";
            JOptionPane.showMessageDialog(this, msg,
                    "Title already used by another model",
                    JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException(msg);
        } catch (ModelTitleRestrictionException me) {
            String msg = "Failed to set model title.\n  Please ensure total characters for model title is 3 or more.";
            JOptionPane.showMessageDialog(this, msg, "Invalid title",
                    JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException(msg);

        }
        SwingUtil.setTitle(this, value);
    }

    public String getViewCaption() {
        return "Allocated Pension";
    }

    private void initComponents2() {

        graphView = (APGraphViewNew) jPanelGraph;
        SwingUtils.setDefaultFont(graphView);

        // invisible by default
        jTabbedPane1.removeTabAt(RESULT_PAGE_ID);

        jPanel5.setVisible(false);
        jPanel12.setVisible(false);
        /*
         * FrequencyCode fc = new FrequencyCode();
         * jComboBoxPensionFrequency.addItem( fc.getCodeDescription(
         * FrequencyCode.VALUE_NONE ) ); jComboBoxPensionFrequency.addItem(
         * fc.getCodeDescription( FrequencyCode.MONTHLY ) );
         * jComboBoxPensionFrequency.addItem( fc.getCodeDescription(
         * FrequencyCode.EVERY_THREE_MONTHS ) );
         * jComboBoxPensionFrequency.addItem( fc.getCodeDescription(
         * FrequencyCode.HALF_YEARLY ) ); jComboBoxPensionFrequency.addItem(
         * fc.getCodeDescription( FrequencyCode.YEARLY ) );
         */
        _setAccessibleContext();

        apCalc.addChangeListener(this);
        ListenerUtils.addListener(this, apCalc); // after
                                                    // _setAccessibleContext()
        updateEditable();
        updateComponents();

        jButtonSave
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonSaveAs
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonDelete.setEnabled(jButtonSave.isEnabled());
        setActionMap();

        setDetailedTableModel(jTableData1);
        initDetailedTable();

    }

    private void _setAccessibleContext() {
        jTextFieldClientName.getAccessibleContext().setAccessibleName(
                DocumentNames.CLIENT_NAME);
        jTextFieldDOB.getDateField().getAccessibleContext().setAccessibleName(
                DocumentNames.DOB);
        jTextFieldEligibleServiceDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.ELIGIBLE_SERVICE_DATE);
        jTextFieldTotalETP.getAccessibleContext().setAccessibleName(
                DocumentNames.TOTAL_ETP_AMOUNT);

        jTextFieldPensionStartDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.PENSION_START_DATE);
        jTextFieldFirstPaymentDate.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.FIRST_PAYMENT_DATE);

        jComboBoxSex.getAccessibleContext().setAccessibleName(
                DocumentNames.SEX_CODE);

        jRadioButtonNo.getAccessibleContext().setAccessibleName(
                DocumentNames.RO_NO);
        jRadioButtonYes.getAccessibleContext().setAccessibleName(
                DocumentNames.RO_YES);

        jRadioButtonIsClient.getAccessibleContext().setAccessibleName(
                DocumentNames.IS_CLIENT);
        jRadioButtonIsPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.IS_PARTNER);

        jComboBoxGeneralTaxExemption.getAccessibleContext().setAccessibleName(
                DocumentNames.GENERAL_TAX_EXEMPTION);
        jComboBoxDeathBenefit.getAccessibleContext().setAccessibleName(
                DocumentNames.DEATH_BENEFIT);
        // jComboBoxPensionFrequency.getAccessibleContext().setAccessibleName(
        // DocumentNames.PENSION_FREQUENCY );

        jTextFieldEntryFee.getAccessibleContext().setAccessibleName(
                DocumentNames.ENTRY_FEE);
        jTextFieldOngoingFee.getAccessibleContext().setAccessibleName(
                DocumentNames.ONGOING_FEE);
        jTextFieldProjectToAge.getAccessibleContext().setAccessibleName(
                DocumentNames.PROJECT_TO_AGE);

        jRadioButtonNetEarningRateFlat.getAccessibleContext()
                .setAccessibleName(DocumentNames.FLAT_CAPITAL_GROWTH_RATES);
        jRadioButtonNetEarningRateVariable.getAccessibleContext()
                .setAccessibleName(DocumentNames.VARIABLE_CAPITAL_GROWTH_RATES);
        jComboBoxInvestmentStrategy.getAccessibleContext().setAccessibleName(
                DocumentNames.INV_STRATEGY);
        jTextFieldNetEarningRate.getAccessibleContext().setAccessibleName(
                DocumentNames.NET_EARNING_RATE);

        jComboBoxSelectedAnnualPension.getAccessibleContext()
                .setAccessibleName(DocumentNames.SELECTED_ANNUAL_PENSION);
        jTextFieldOther.getAccessibleContext().setAccessibleName(
                DocumentNames.OTHER_VALUE);

        jTextFieldAnnualIncreasePayments.getAccessibleContext()
                .setAccessibleName(DocumentNames.ANNUAL_INCREASE_PAYMENTS);

        // For saving dialog window
        jTextFieldPre071983.getAccessibleContext().setAccessibleName(
                DocumentNames.PRE);
        jTextFieldPost061983Taxed.getAccessibleContext().setAccessibleName(
                DocumentNames.POST_TAXED);
        jTextFieldPost061983Untaxed.getAccessibleContext().setAccessibleName(
                DocumentNames.POST_JUNE_1983_UNTAXED);
        jTextFieldUndeducted.getAccessibleContext().setAccessibleName(
                DocumentNames.UNDEDUCTED);
        jTextFieldCGTExempt.getAccessibleContext().setAccessibleName(
                DocumentNames.CGT_EXEMPT);
        jTextFieldExcess.getAccessibleContext().setAccessibleName(
                DocumentNames.EXCESS);
        jTextFieldConcessional.getAccessibleContext().setAccessibleName(
                DocumentNames.CONCESSIONAL);
        jTextFieldInvalidity.getAccessibleContext().setAccessibleName(
                DocumentNames.POST_JUNE_94_INVALIDITY);

        jComboBoxRelationship.getAccessibleContext().setAccessibleName(
                DocumentNames.RELATIONSHIP);
        jComboBoxSexPartner.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SEX_CODE);
        jTextFieldNamePartner.getAccessibleContext().setAccessibleName(
                DocumentNames.NAME_PARTNER);
        jTextFieldDOBPartner.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.DOB_PARTNER);
    }

    public void updateComponents() {
        boolean add = apCalc != null && apCalc.isReady();

        if (add) {
            if (jTabbedPane1.getTabCount() == RESULT_PAGE_ID)
                jTabbedPane1.addTab("Projections", jPanelResults);
        } else {
            if (jTabbedPane1.getTabCount() > RESULT_PAGE_ID)
                jTabbedPane1.removeTabAt(RESULT_PAGE_ID);
        }

        // enable/disable save option
        if (apCalc != null) {
            /*
             * if ( apCalc.getSelectedAnnualPensionType() != null &&
             * APConstants.OTHER.equals( apCalc.getSelectedAnnualPensionType() ) ) {
             * SwingUtils.setEnabled(jTextFieldOther, true );
             * SwingUtils.setEnabled( jTextFieldAnnualIncreasePayments, true ); }
             * else { jTextFieldOther.setText( null );
             * jTextFieldAnnualIncreasePayments.setText( null );
             * SwingUtils.setEnabled(jTextFieldOther, false );
             * SwingUtils.setEnabled( jTextFieldAnnualIncreasePayments, false ); }
             */
            jButtonReversionaryDetails
                    .setEnabled(apCalc.getDeathBenefitID() != null
                            && (DeathBenefitCodeID.REVERSIONARY.equals(apCalc
                                    .getDeathBenefitID()) || DeathBenefitCodeID.SURVIVING_DEPENDENT
                                    .equals(apCalc.getDeathBenefitID())));
        }
        jButtonReport.setEnabled(add);
        jButtonNext.setEnabled(add && jTabbedPane1.getSelectedIndex() == 0);
        jButtonSave
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonSaveAs
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        SwingUtil.setEnabled(jTextFieldNetEarningRate,
                jRadioButtonNetEarningRateFlat.isSelected());
    }

    private void updateChart() {

        if (apCalc == null || !apCalc.isReady())
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // assets
            int[] age = apCalc.getAgeArray();
            double[] assets = apCalc.getEndBalanceArray();

            // nothing changed
            if (assets == null && age == null)
                return;

            // do chart
            graphView.removeChartLabels();

            graphView
                    .addChartLabels(
                            graphView
                                    .customizeChart(
                                            new double[][] { assets },
                                            apCalc.getLabels(),
                                            new String[] { "<html>Assets <i>(end-balance)</i></html>" },
                                            new java.awt.Color[] { java.awt.Color.blue },
                                            true), Currency
                                    .getCurrencyInstance());
            // let user to go to chart
            graphView.setTitleAxisY1("$");
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                    .getInstance());

            updateComponents();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {

        updateNonEditable();
        updateComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroupClientSex = new javax.swing.ButtonGroup();
        buttonGroupRO = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelDetails1 = new javax.swing.JPanel();
        jPanelClientDetails = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jRadioButtonIsClient = new javax.swing.JRadioButton();
        jRadioButtonIsPartner = new javax.swing.JRadioButton();
        jPanel191 = new javax.swing.JPanel();
        jPanel1911 = new javax.swing.JPanel();
        jPanel1912 = new javax.swing.JPanel();
        jPanel1913 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldClientName = new javax.swing.JTextField();
        jTextFieldDOB = new com.argus.beans.FDateChooser();
        jTextFieldPensionStartDate = new com.argus.beans.FDateChooser();
        jTextFieldFirstPaymentDate = new com.argus.beans.FDateChooser();
        jTextFieldNamePartner = new javax.swing.JTextField();
        jTextFieldDOBPartner = new com.argus.beans.FDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabelSex = new javax.swing.JLabel();
        jLabelAge = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldAge = new javax.swing.JTextField();
        jTextFieldEligibleServiceDate = new com.argus.beans.FDateChooser();
        jPanel13 = new javax.swing.JPanel();
        jRadioButtonYes = new javax.swing.JRadioButton();
        jRadioButtonNo = new javax.swing.JRadioButton();
        jComboBoxSex = new JComboBox(new SexCode().getCodeDescriptions());
        jComboBoxSexPartner = new JComboBox(new SexCode().getCodeDescriptions());
        jPanelPensionOptions = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxGeneralTaxExemption = new javax.swing.JComboBox(
                new GeneralTaxExemptionCode().getCodes().toArray());
        jComboBoxDeathBenefit = new javax.swing.JComboBox(
                new DeathBenefitCode().getCodes().toArray());
        jButtonReversionaryDetails = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldEntryFee = new javax.swing.JTextField();
        jTextFieldOngoingFee = new javax.swing.JTextField();
        jTextFieldProjectToAge = new javax.swing.JTextField();
        jPanelTotal = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldTotalETP = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jButtonAdjustETP = new javax.swing.JButton();
        jPanelPensionProjection = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldNetEarningRate = new javax.swing.JTextField();
        jTextFieldUPP = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jRadioButtonNetEarningRateFlat = new javax.swing.JRadioButton();
        jRadioButtonNetEarningRateVariable = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxInvestmentStrategy = new javax.swing.JComboBox(
                new InvestmentStrategyCode().getCodeDescriptions());
        jLabel22 = new javax.swing.JLabel();
        jTextFieldDeductibleAmount = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextFieldMinimum = new javax.swing.JTextField();
        jTextFieldMaximum = new javax.swing.JTextField();
        jTextFieldOther = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jComboBoxSelectedAnnualPension = new javax.swing.JComboBox(
                new SelectedAnnualPensionCode().getCodes().toArray());
        jLabel27 = new javax.swing.JLabel();
        jTextFieldAnnualIncreasePayments = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jComboBoxRelationship = new javax.swing.JComboBox(
                new APRelationshipCode().getCodes().toArray());
        jPanel5 = new javax.swing.JPanel();
        jTextFieldPre071983 = new javax.swing.JTextField();
        jTextFieldPost061983Taxed = new javax.swing.JTextField();
        jTextFieldPost061983Untaxed = new javax.swing.JTextField();
        jTextFieldExcess = new javax.swing.JTextField();
        jTextFieldConcessional = new javax.swing.JTextField();
        jTextFieldInvalidity = new javax.swing.JTextField();
        jTextFieldUndeducted = new javax.swing.JTextField();
        jTextFieldCGTExempt = new javax.swing.JTextField();
        jPanelResults = new javax.swing.JPanel();
        jPanelGraph = new APGraphViewNew();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableData1 = new javax.swing.JTable();
        jPanelControl = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jButtonClear = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanelDetails1.setLayout(new javax.swing.BoxLayout(jPanelDetails1,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelClientDetails.setLayout(new javax.swing.BoxLayout(
                jPanelClientDetails, javax.swing.BoxLayout.Y_AXIS));

        jPanelClientDetails.setBorder(new javax.swing.border.TitledBorder(
                "Client Details"));
        jPanel41.setLayout(new javax.swing.BoxLayout(jPanel41,
                javax.swing.BoxLayout.X_AXIS));

        jPanel19.setLayout(new java.awt.GridBagLayout());

        jRadioButtonIsClient.setText("For Client");
        buttonGroup2.add(jRadioButtonIsClient);
        jRadioButtonIsClient.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonIsClientItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel19.add(jRadioButtonIsClient, gridBagConstraints);

        jRadioButtonIsPartner.setText("For Partner");
        buttonGroup2.add(jRadioButtonIsPartner);
        jRadioButtonIsPartner
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonIsClientItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel19.add(jRadioButtonIsPartner, gridBagConstraints);

        jPanel41.add(jPanel19);

        jPanel191.setLayout(new java.awt.GridBagLayout());

        jPanel41.add(jPanel191);

        jPanel1911.setLayout(new java.awt.GridBagLayout());

        jPanel41.add(jPanel1911);

        jPanel1912.setLayout(new java.awt.GridBagLayout());

        jPanel41.add(jPanel1912);

        jPanel1913.setLayout(new java.awt.GridBagLayout());

        jPanel41.add(jPanel1913);

        jPanelClientDetails.add(jPanel41);

        jPanel18.setLayout(new javax.swing.BoxLayout(jPanel18,
                javax.swing.BoxLayout.X_AXIS));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel1.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Pension Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText("First Payment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel4, gridBagConstraints);

        jTextFieldClientName
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientName.setNextFocusableComponent(jComboBoxSex);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(jTextFieldClientName, gridBagConstraints);

        jTextFieldDOB.setInputVerifier(new DateInputVerifier());
        jTextFieldDOB.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldDOB.setNextFocusableComponent(jTextFieldPensionStartDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(jTextFieldDOB, gridBagConstraints);

        jTextFieldPensionStartDate.setInputVerifier(new DateInputVerifier());
        jTextFieldPensionStartDate
                .setNextFocusableComponent(jTextFieldEligibleServiceDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(jTextFieldPensionStartDate, gridBagConstraints);

        jTextFieldFirstPaymentDate.setInputVerifier(new DateInputVerifier());
        jTextFieldFirstPaymentDate.setNextFocusableComponent(jRadioButtonYes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(jTextFieldFirstPaymentDate, gridBagConstraints);

        jTextFieldNamePartner
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldNamePartner.setNextFocusableComponent(jComboBoxSex);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(jTextFieldNamePartner, gridBagConstraints);

        jTextFieldDOBPartner.setInputVerifier(new DateInputVerifier());
        jTextFieldDOBPartner.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldDOBPartner
                .setNextFocusableComponent(jTextFieldPensionStartDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(jTextFieldDOBPartner, gridBagConstraints);

        jPanel18.add(jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabelSex.setText("Sex");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabelSex, gridBagConstraints);

        jLabelAge.setText("Age At Pension Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabelAge, gridBagConstraints);

        jLabel7.setText("Eligible Service Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel7, gridBagConstraints);

        jLabel8.setText("R/O from Pre 1994 Income Stream");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel8, gridBagConstraints);

        jTextFieldAge.setEditable(false);
        jTextFieldAge.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel2.add(jTextFieldAge, gridBagConstraints);

        jTextFieldEligibleServiceDate.setInputVerifier(new DateInputVerifier());
        jTextFieldEligibleServiceDate
                .setNextFocusableComponent(jTextFieldFirstPaymentDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel2.add(jTextFieldEligibleServiceDate, gridBagConstraints);

        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        jRadioButtonYes.setText("Yes");
        buttonGroupRO.add(jRadioButtonYes);
        jPanel13.add(jRadioButtonYes);

        jRadioButtonNo.setSelected(true);
        jRadioButtonNo.setText("No");
        buttonGroupRO.add(jRadioButtonNo);
        jPanel13.add(jRadioButtonNo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel2.add(jPanel13, gridBagConstraints);

        jComboBoxSex.setNextFocusableComponent(jTextFieldDOB);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel2.add(jComboBoxSex, gridBagConstraints);

        jComboBoxSexPartner.setNextFocusableComponent(jTextFieldDOB);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel2.add(jComboBoxSexPartner, gridBagConstraints);

        jPanel18.add(jPanel2);

        jPanelClientDetails.add(jPanel18);

        jPanelDetails1.add(jPanelClientDetails);

        jPanelPensionOptions.setLayout(new java.awt.GridLayout(1, 2));

        jPanelPensionOptions.setBorder(new javax.swing.border.TitledBorder(
                "Pension Options"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel10.setText("General Tax Exemption Claimed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Death Benefit Options");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel11, gridBagConstraints);

        jComboBoxGeneralTaxExemption
                .setSelectedItem(GeneralTaxExemptionCodeID.rcGENERAL_EXEMPTION);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel3.add(jComboBoxGeneralTaxExemption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel3.add(jComboBoxDeathBenefit, gridBagConstraints);

        jButtonReversionaryDetails.setText("Reversionary Details");
        jButtonReversionaryDetails
                .addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jButtonReversionaryDetailsMouseClicked(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel3.add(jButtonReversionaryDetails, gridBagConstraints);

        jPanelPensionOptions.add(jPanel3);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel13.setText("Entry Fee(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Ongoing Portfolio Review Fee(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel14, gridBagConstraints);

        jLabel15.setText("Project to Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel15, gridBagConstraints);

        jTextFieldEntryFee
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEntryFee.setInputVerifier(PercentInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel4.add(jTextFieldEntryFee, gridBagConstraints);

        jTextFieldOngoingFee
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOngoingFee.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel4.add(jTextFieldOngoingFee, gridBagConstraints);

        jTextFieldProjectToAge
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldProjectToAge.setInputVerifier(ProjectToAgeInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel4.add(jTextFieldProjectToAge, gridBagConstraints);

        jPanelPensionOptions.add(jPanel4);

        jPanelDetails1.add(jPanelPensionOptions);

        jPanelTotal.setLayout(new java.awt.GridLayout(1, 2));

        jPanelTotal.setBorder(new javax.swing.border.TitledBorder("Total"));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel8.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel18.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel18, gridBagConstraints);

        jTextFieldTotalETP
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalETP
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldTotalETP
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jTextFieldTotalETPActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel8.add(jTextFieldTotalETP, gridBagConstraints);

        jPanelTotal.add(jPanel8);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jPanel9.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel5.setText("Button to change if desired");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel9.add(jLabel5, gridBagConstraints);

        jButtonAdjustETP.setText("Adjust ETP");
        jButtonAdjustETP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAdjustETPMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel9.add(jButtonAdjustETP, gridBagConstraints);

        jPanelTotal.add(jPanel9);

        jPanelDetails1.add(jPanelTotal);

        jPanelPensionProjection.setLayout(new java.awt.GridLayout(1, 0));

        jPanelPensionProjection.setBorder(new javax.swing.border.TitledBorder(
                "Pension Projection & Payment Levels"));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel10.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel20.setText("Net Earning Rate(%)");
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel20, gridBagConstraints);

        jLabel21.setText("UPP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel21, gridBagConstraints);

        jTextFieldNetEarningRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldNetEarningRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel10.add(jTextFieldNetEarningRate, gridBagConstraints);

        jTextFieldUPP.setEditable(false);
        jTextFieldUPP.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUPP.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel10.add(jTextFieldUPP, gridBagConstraints);

        jPanel14.setLayout(new java.awt.GridLayout(1, 0));

        jRadioButtonNetEarningRateFlat.setSelected(true);
        jRadioButtonNetEarningRateFlat.setText("Flat");
        buttonGroup1.add(jRadioButtonNetEarningRateFlat);
        jPanel14.add(jRadioButtonNetEarningRateFlat);

        jRadioButtonNetEarningRateVariable.setText("Variable");
        buttonGroup1.add(jRadioButtonNetEarningRateVariable);
        jPanel14.add(jRadioButtonNetEarningRateVariable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel10.add(jPanel14, gridBagConstraints);

        jLabel9.setText("Investment Strategy");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel10.add(jComboBoxInvestmentStrategy, gridBagConstraints);

        jLabel22.setText("Deductible amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel22, gridBagConstraints);

        jTextFieldDeductibleAmount.setEditable(false);
        jTextFieldDeductibleAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldDeductibleAmount.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel10.add(jTextFieldDeductibleAmount, gridBagConstraints);

        jLabel6.setText("Net Earning Rate Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(jLabel6, gridBagConstraints);

        jPanelPensionProjection.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jPanel11.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel24.setText("Minimum Annual Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel11.add(jLabel24, gridBagConstraints);

        jLabel25.setText("Maximum Annual Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel11.add(jLabel25, gridBagConstraints);

        jLabel26.setText("Selected Annual Pension");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel11.add(jLabel26, gridBagConstraints);

        jTextFieldMinimum.setEditable(false);
        jTextFieldMinimum
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMinimum.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel11.add(jTextFieldMinimum, gridBagConstraints);

        jTextFieldMaximum.setEditable(false);
        jTextFieldMaximum
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldMaximum.setInputVerifier(CurrencyInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel11.add(jTextFieldMaximum, gridBagConstraints);

        jTextFieldOther.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOther.setInputVerifier(new OtherAnnualPensionInputVerifier(
                apCalc));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel11.add(jTextFieldOther, gridBagConstraints);

        jLabel30.setText("Nominated");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel11.add(jLabel30, gridBagConstraints);

        jComboBoxSelectedAnnualPension
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxSelectedAnnualPensionItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel11.add(jComboBoxSelectedAnnualPension, gridBagConstraints);

        jLabel27.setText("Annual Increase Payments(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel11.add(jLabel27, gridBagConstraints);

        jTextFieldAnnualIncreasePayments
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAnnualIncreasePayments.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel11.add(jTextFieldAnnualIncreasePayments, gridBagConstraints);

        jPanelPensionProjection.add(jPanel11);

        jPanelDetails1.add(jPanelPensionProjection);

        jPanel12.setEnabled(false);
        jPanel12.add(jComboBoxRelationship);

        jPanelDetails1.add(jPanel12);

        jPanel5.setEnabled(false);
        jTextFieldPre071983.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldPre071983);

        jTextFieldPost061983Taxed.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldPost061983Taxed);

        jTextFieldPost061983Untaxed.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldPost061983Untaxed);

        jTextFieldExcess.setInputVerifier(CurrencyInputVerifier.getInstance());
        jPanel5.add(jTextFieldExcess);

        jTextFieldConcessional.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldConcessional);

        jTextFieldInvalidity.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldInvalidity);

        jTextFieldUndeducted.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldUndeducted);

        jTextFieldCGTExempt.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jPanel5.add(jTextFieldCGTExempt);

        jPanelDetails1.add(jPanel5);

        jTabbedPane1.addTab("Details", jPanelDetails1);

        jPanelResults.setLayout(new javax.swing.BoxLayout(jPanelResults,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelGraph.setLayout(new javax.swing.BoxLayout(jPanelGraph,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelGraph.setBorder(new javax.swing.border.TitledBorder("Graph"));
        jPanelResults.add(jPanelGraph);

        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16,
                javax.swing.BoxLayout.X_AXIS));

        jScrollPane2.setViewportView(jTableData1);

        jPanel16.add(jScrollPane2);

        jPanelResults.add(jPanel16);

        jTabbedPane1.addTab("Results", jPanelResults);

        add(jTabbedPane1);

        jPanelControl.setLayout(new javax.swing.BoxLayout(jPanelControl,
                javax.swing.BoxLayout.X_AXIS));

        jButtonReport.setText("Report");
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanel6.add(jButtonReport);

        jPanelControl.add(jPanel6);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel7.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanel7.add(jButtonSave);

        jButtonSaveAs.setText("Save As");
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsActionPerformed(evt);
            }
        });

        jPanel7.add(jButtonSaveAs);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanel7.add(jButtonDelete);

        jPanelControl.add(jPanel7);

        jButtonClear.setText("Clear");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPanel15.add(jButtonClear);

        jPanelControl.add(jPanel15);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.setActionCommand("Next");
        jButtonPrevious.setDefaultCapable(false);
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanel17.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.setActionCommand("Next");
        jButtonNext.setDefaultCapable(false);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanel17.add(jButtonNext);

        jPanelControl.add(jPanel17);

        add(jPanelControl);

    }// GEN-END:initComponents

    private void jRadioButtonIsClientItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonIsClientItemStateChanged
        // Add your handling code here:
        if (evt.getSource() != jRadioButtonIsClient)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        if (jRadioButtonIsClient.isSelected()) {
            apCalc.setIsClient(Boolean.TRUE);
            jTextFieldNamePartner.setVisible(false);
            jTextFieldDOBPartner.setVisible(false);
            jComboBoxSexPartner.setVisible(false);
            jTextFieldClientName.setVisible(true);
            jTextFieldDOB.setVisible(true);
            jComboBoxSex.setVisible(true);
            updateNonEditable();
        } else {
            apCalc.setIsClient(Boolean.FALSE);
            jTextFieldNamePartner.setVisible(true);
            jTextFieldDOBPartner.setVisible(true);
            jComboBoxSexPartner.setVisible(true);
            jTextFieldClientName.setVisible(false);
            jTextFieldDOB.setVisible(false);
            jComboBoxSex.setVisible(false);
            updateNonEditable();
        }

    }// GEN-LAST:event_jRadioButtonIsClientItemStateChanged

    private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveAsActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            newModel = true;
            doSave(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonSaveAsActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void jComboBoxSelectedAnnualPensionItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxSelectedAnnualPensionItemStateChanged
        // Add your handling code here:
        ReferenceCode rc = (ReferenceCode) jComboBoxSelectedAnnualPension
                .getSelectedItem();
        if (rc == null)
            return;
        if (rc.equals(SelectedAnnualPensionCode.rcOTHER)) {
            jTextFieldOther.setVisible(true);
            jTextFieldAnnualIncreasePayments.setVisible(true);
            jLabel30.setVisible(true);
            jLabel27.setVisible(true);

        } else {
            jTextFieldOther.setVisible(false);
            jTextFieldAnnualIncreasePayments.setVisible(false);
            jLabel30.setVisible(false);
            jLabel27.setVisible(false);

        }

    }// GEN-LAST:event_jComboBoxSelectedAnnualPensionItemStateChanged

    private void jTextFieldTotalETPActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextFieldTotalETPActionPerformed
        // Add your handling code here:
    }// GEN-LAST:event_jTextFieldTotalETPActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPane1StateChanged
        // Add your handling code here:
        int index = jTabbedPane1.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane1.getTabCount() - 1);
    }// GEN-LAST:event_jTabbedPane1StateChanged

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doNext(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doPrevious(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }// GEN-LAST:event_jButtonPreviousActionPerformed

    private void jButtonReversionaryDetailsMouseClicked(
            java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButtonReversionaryDetailsMouseClicked
        // Add your handling code here:
        ReversionaryOptionView oView = new ReversionaryOptionView(apCalc);
        SwingUtil.add2Dialog(null, "Reversionary Option/Surviving Dependent",
                true, oView, true, true);

        if (oView.getResult() == ETPConstants.OK_OPTION)
            updateReversionaryOptionViewDataForSave();

    }// GEN-LAST:event_jButtonReversionaryDetailsMouseClicked

    private void jButtonAdjustETPMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButtonAdjustETPMouseClicked
        // Add your handling code here:
        if (apCalc.getEligibleServiceDate() == null)
            javax.swing.JOptionPane
                    .showMessageDialog(
                            null,
                            "Please input Eligible Service Date before adjusting ETP Components!",
                            "Error Message",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
        else if (apCalc.getPensionStartDate() == null)
            javax.swing.JOptionPane
                    .showMessageDialog(
                            null,
                            "Please input Pension Start Date before adjusting ETP Components!",
                            "Error Message",
                            javax.swing.JOptionPane.ERROR_MESSAGE);

        else {
            ETPDetailsView eView = new ETPDetailsView(apCalc);
            SwingUtil.add2Dialog(null, "ETP Details", true, eView, true, true);

            if (eView.getResult() == ETPConstants.OK_OPTION) {
                updateETPDetailsViewDataForSave();
            }
        }
    }// GEN-LAST:event_jButtonAdjustETPMouseClicked

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int result = BaseView.closeDialog(this);
        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doClear(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }// GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        // Add your handling code here:
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            newModel = false;
            doSave(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }// GEN-LAST:event_jButtonSaveActionPerformed

    protected void doNext(java.awt.event.ActionEvent evt) {
        if (jButtonNext.isEnabled())
            jTabbedPane1.setSelectedIndex(jTabbedPane1.getSelectedIndex() + 1);
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        if (jButtonPrevious.isEnabled())
            jTabbedPane1.setSelectedIndex(jTabbedPane1.getSelectedIndex() - 1);
    }

    /***************************************************************************
     * BaseView protected methods
     **************************************************************************/
    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(newModel);
            // update menu
            FinancialPlannerApp.getInstance().updateModels();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    public void doDelete(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this,
                "Do You want to delete this projection?",
                "Delete Projection Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        fireActionEvent(DATA_REMOVE);
    }

    protected void doClear(java.awt.event.ActionEvent evt) {

        try {
            apCalc.disableUpdate();
            apCalc.clear();
            updateTitle();
        } finally {
            // updateKnownComponents();
            updateEditable();
            // updateComponents();
            apCalc.enableUpdate();
            apCalc.doUpdate();
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return com.argus.financials.config.WordSettings.getInstance()
                .getAllocatedPensionReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {

        // if ( person == null ) return null;

        ReportFields reportFields = ReportFields.getInstance();
        apCalc.initializeReportData(reportFields);

        return reportFields;

    }

    protected void doReport() {

        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(ServiceLocator.getInstance().getClientPerson()),
                    getDefaultReport());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private static AllocatedPensionViewNew view;

    public static AllocatedPensionViewNew display(final Model model, FocusListener[] listeners) {

        if (view == null) {
            view = new AllocatedPensionViewNew();
            SwingUtil.add2Frame(view, listeners,
                model == null ? view.getDefaultTitle() : model.getTitle(),
                ViewSettings.getInstance().getViewImage(
                view.getClass().getName()), true, true, false);
        }

        int age = view.getCalculationModel().getAge();
        if (age != view.getCalculationModel().UNKNOWN_VALUE
                && age < ETPConstants.TAX_EFFECTIVE_AGE
                && JOptionPane.showConfirmDialog(
                        null,
                        "Client's age is less than " + ETPConstants.TAX_EFFECTIVE_AGE + ", would you like to proceed?",
                        "Warning Message",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
        {
            return null;
        }

        try {
            view.updateView(model);

            String title = model == null ? view.getDefaultTitle() : model.getTitle();
            SwingUtil.setTitle(view, title);
            SwingUtil.setVisible(view, true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            view = null;
        }

        return view;

    }

    public void setClientNameFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTextFieldClientName.requestFocus();
            }
        });
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private TableModel detailedTableModel;

    private Object[] detailedColumnNames = new String[] { "Age",
            "Opening Balance", "Selected Pension", "Assessable Income",
            "Rebate", "Net Pension", "Earnings", "End Balance", "Minimum",
            "Maximum", "Other", "Net Tax Payable", "Excess Rebate",
            "Medicare Levy" };

    private static final int DETAILS_COLUMN_COUNT = 14;

    private void setDetailedTableModel(JTable jTable) {

        if (detailedTableModel == null) {

            detailedTableModel = new DefaultTableModel(new Object[][] {},
                    detailedColumnNames) {

                private Class[] types = new Class[] { java.lang.Integer.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class };

                public String getColumnName(int columnIndex) {
                    return super.getColumnName(columnIndex);
                }

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }

                public Object getValueAt(int row, int column) {
                    Object obj = super.getValueAt(row, column);

                    if (obj instanceof Number)
                        return money.roundToTen((Number) obj);
                    return obj;
                }

            };

        }

        if (jTable.getModel() == detailedTableModel)
            return;

        // jTable.setTableHeader(null);
        jTable.setModel(detailedTableModel);

        JTableHeader th = new JTableHeader(jTable.getColumnModel());
        jTable.setTableHeader(th);
        th.setFont(SwingUtils.getDefaultFont());

    }

    private void initDetailedTable() {
        jTableData1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < jTableData1.getColumnModel().getColumnCount(); i++)
            jTableData1.getColumnModel().getColumn(i).setPreferredWidth(100);

        Object[][] rowData = getDetailedRowData();

        TableModel tm = jTableData1.getModel();

        if (tm instanceof DefaultTableModel) {
            if (rowData == null)
                ((DefaultTableModel) tm).setRowCount(0);
            else
                ((DefaultTableModel) tm).setDataVector(rowData,
                        detailedColumnNames);

        } else {
            System.err.println("Unhandled TableModel: " + tm);
            return;
        }

    }

    private Object[][] getDetailedRowData() {

        int[] age = apCalc.getAgeArray();
        if (age == null)
            return null;

        double[] openingBalance = apCalc.getOpeningBalanceArray();
        double[] minimum = apCalc.getMinimumArray();
        double[] maximum = apCalc.getMaximumArray();
        double[] others = apCalc.getOthersArray();
        double[] selectedPension = apCalc.getSelectedPensionArray();
        double[] assessableIncome = apCalc.getAssessableIncomeArray();
        double[] netTaxPayable = apCalc.getNetTaxPayableArray();
        double[] rebate = apCalc.getRebateArray();
        double[] excessRebate = apCalc.getExcessRebateArray();
        double[] medicareLevy = apCalc.getMedicareLevyArray();
        double[] netPension = apCalc.getNetPensionArray();
        double[] endBalance = apCalc.getEndBalanceArray();
        double[] netEarnings = apCalc.getNetEarningsArray();

        try {

            Object[][] rowData = new Object[age.length][DETAILS_COLUMN_COUNT];

            for (int i = 0; i < age.length; i++) {
                rowData[i][0] = "" + age[i];
                rowData[i][1] = new java.math.BigDecimal(openingBalance[i]);
                rowData[i][2] = new java.math.BigDecimal(selectedPension[i]);
                rowData[i][3] = new java.math.BigDecimal(assessableIncome[i]);
                rowData[i][4] = new java.math.BigDecimal(rebate[i]);
                rowData[i][5] = new java.math.BigDecimal(netPension[i]);
                rowData[i][6] = new java.math.BigDecimal(netEarnings[i]);
                rowData[i][7] = new java.math.BigDecimal(endBalance[i]);
                rowData[i][8] = new java.math.BigDecimal(minimum[i]);
                rowData[i][9] = new java.math.BigDecimal(maximum[i]);
                rowData[i][10] = new java.math.BigDecimal(others[i]);
                rowData[i][11] = new java.math.BigDecimal(netTaxPayable[i]);
                rowData[i][12] = new java.math.BigDecimal(excessRebate[i]);
                rowData[i][13] = new java.math.BigDecimal(medicareLevy[i]);

            }

            return rowData;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    public boolean isModified() {
        return (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                .isModified())
                || (jButtonSaveAs.isVisible() && jButtonSaveAs.isEnabled() && getModel()
                        .isModified());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxInvestmentStrategy;

    private javax.swing.JTextField jTextFieldDeductibleAmount;

    private javax.swing.JTable jTableData1;

    private javax.swing.JButton jButtonAdjustETP;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JPanel jPanel19;

    private javax.swing.JPanel jPanel18;

    private javax.swing.JPanel jPanel17;

    private javax.swing.JLabel jLabel30;

    private javax.swing.JPanel jPanel16;

    private javax.swing.JPanel jPanel15;

    private javax.swing.JPanel jPanel14;

    private javax.swing.JPanel jPanel13;

    private javax.swing.JPanel jPanel12;

    private javax.swing.JPanel jPanel11;

    private javax.swing.JPanel jPanel10;

    private javax.swing.ButtonGroup buttonGroupRO;

    private javax.swing.ButtonGroup buttonGroupClientSex;

    private com.argus.beans.FDateChooser jTextFieldEligibleServiceDate;

    private javax.swing.JRadioButton jRadioButtonIsClient;

    private javax.swing.JLabel jLabel27;

    protected javax.swing.JButton jButtonNext;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JTextField jTextFieldNetEarningRate;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextField jTextFieldMaximum;

    private javax.swing.JRadioButton jRadioButtonIsPartner;

    private javax.swing.JComboBox jComboBoxSex;

    private javax.swing.JTextField jTextFieldPre071983;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JRadioButton jRadioButtonNetEarningRateVariable;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JButton jButtonReversionaryDetails;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JComboBox jComboBoxSexPartner;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanelResults;

    private javax.swing.JTextField jTextFieldProjectToAge;

    private javax.swing.JTextField jTextFieldConcessional;

    private javax.swing.JPanel jPanelTotal;

    protected javax.swing.JButton jButtonPrevious;

    private javax.swing.JComboBox jComboBoxSelectedAnnualPension;

    private javax.swing.JTextField jTextFieldExcess;

    private javax.swing.JTextField jTextFieldOther;

    private javax.swing.JPanel jPanel191;

    private javax.swing.JPanel jPanel1913;

    private javax.swing.JPanel jPanel1912;

    private javax.swing.JPanel jPanel1911;

    private com.argus.beans.FDateChooser jTextFieldDOBPartner;

    private javax.swing.JTextField jTextFieldTotalETP;

    private javax.swing.JTextField jTextFieldPost061983Untaxed;

    private javax.swing.JTextField jTextFieldMinimum;

    private javax.swing.JButton jButtonSaveAs;

    private com.argus.beans.FDateChooser jTextFieldDOB;

    private javax.swing.JTextField jTextFieldNamePartner;

    private javax.swing.JPanel jPanelGraph;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanelPensionProjection;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JTextField jTextFieldUndeducted;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JRadioButton jRadioButtonYes;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JTextField jTextFieldAnnualIncreasePayments;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanelDetails1;

    private javax.swing.JLabel jLabelSex;

    private javax.swing.JLabel jLabelAge;

    private javax.swing.JRadioButton jRadioButtonNo;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JTextField jTextFieldCGTExempt;

    private javax.swing.JPanel jPanel41;

    private javax.swing.ButtonGroup buttonGroup2;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JComboBox jComboBoxDeathBenefit;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JPanel jPanelControl;

    private javax.swing.JTextField jTextFieldEntryFee;

    private javax.swing.JComboBox jComboBoxRelationship;

    private com.argus.beans.FDateChooser jTextFieldFirstPaymentDate;

    private javax.swing.JTextField jTextFieldUPP;

    private javax.swing.JTextField jTextFieldOngoingFee;

    private javax.swing.JComboBox jComboBoxGeneralTaxExemption;

    private javax.swing.JPanel jPanelPensionOptions;

    private javax.swing.JRadioButton jRadioButtonNetEarningRateFlat;

    private javax.swing.JTextField jTextFieldAge;

    private javax.swing.JTextField jTextFieldInvalidity;

    private javax.swing.JTextField jTextFieldPost061983Taxed;

    private javax.swing.JTextField jTextFieldClientName;

    private javax.swing.JPanel jPanelClientDetails;

    private com.argus.beans.FDateChooser jTextFieldPensionStartDate;

    // End of variables declaration//GEN-END:variables

    public void hideControls() {
        jPanelControl.setVisible(false);
    }

    public void showControls() {
        jPanelControl.setVisible(true);
    }

    public void updateView(String modelTitle) throws Exception {
        jTextFieldClientName.requestFocus();
        PersonService person = ServiceLocator.getInstance().getClientPerson();
        Model m = person == null ? null : person.getModel(getDefaultType(),
                modelTitle);

        if (m == null) {
            updateView(person);
        } else {
            updateView(m);
        }

        updateTitle();

    }

    public void updateView(Model m) throws Exception {

        // doClear(null);

        if (m == null) {
            updateView(ServiceLocator.getInstance().getClientPerson());
        } else {
            // use copy of model
            Integer id = m == null ? null : m.getPrimaryKeyID();
            m = new Model(m);
            m.setPrimaryKeyID(id);
    
            try {
                apCalc.disableUpdate();
    
                apCalc.setModel(m);
                // apCalc.setSaved();
            } finally {
                // apCalc.doUpdate();
                updateKnownComponents();
                updateEditable();
                updateComponents();
                graphView.setGraphData(apCalc.getGraphData());
                apCalc.enableUpdate();
            }
        }
    }

    public void updateView(PersonService person)
            throws ServiceException {

        apCalc.disableUpdate();
        try {
            apCalc.clear();
            Model m = new Model();
            m.setTypeID(ModelType.ALLOCATED_PENSION);
            apCalc.setModel(m);

        } finally {
            updateKnownComponents();
            updateEditable();
            apCalc.enableUpdate();
            apCalc.doUpdate();
            // updateComponents();
        }
    }

    private void saveView(boolean newModel) {
        int result = -1;
        String oldTitle = getTitle();
        try {
            if (!newModel)
                result = SaveProjectionDialog.getSaveProjectionInstance().save(
                        apCalc, this);
            else
                result = SaveProjectionDialog.getSaveProjectionInstance()
                        .saveAs(apCalc, this);

            if (result == SaveProjectionDialog.CANCEL_OPTION)
                return;

            updateTitle();
        } catch (DuplicateException e) {
            String newTitle = getTitle();

            String msg = "Failed to save model.";
            JOptionPane.showMessageDialog(this, msg,
                    "Title already used by another model",
                    JOptionPane.ERROR_MESSAGE);

            return;
        } catch (ModelTitleRestrictionException me) {

            String msg = "Failed to save model.\n  Please ensure total characters for model title is 3 or more.";
            JOptionPane.showMessageDialog(this, msg, "Invalid Title",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            saveView(ServiceLocator.getInstance().getClientPerson());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
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

        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (Exception e) { /* ignore by now */
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) { /* ignore by now */
                }
            }
        }

        person.addModel(getModel());
        person.storeModels();
        apCalc.setSaved();
    }

    /**
     * helper methods
     */
    public void save() {

    }

    public void updateEditable() {
        updateOthers();
        if (apCalc.getIsClient() == null || apCalc.getIsClient().booleanValue()) {
            jRadioButtonIsClient.setSelected(true);
            jTextFieldNamePartner.setVisible(false);
            jTextFieldDOBPartner.setVisible(false);
            jComboBoxSexPartner.setVisible(false);
            jTextFieldClientName.setVisible(true);
            jTextFieldDOB.setVisible(true);
            jComboBoxSex.setVisible(true);
        } else {
            jRadioButtonIsPartner.setSelected(true);
            jTextFieldNamePartner.setVisible(true);
            jTextFieldDOBPartner.setVisible(true);
            jComboBoxSexPartner.setVisible(true);
            jTextFieldClientName.setVisible(false);
            jTextFieldDOB.setVisible(false);
            jComboBoxSex.setVisible(false);

        }

        apCalc.disableUpdate();
        try {
            jTextFieldTotalETP.setText(Currency.getCurrencyInstance().toString(
                    apCalc.getTotalETPAmount()));
            jTextFieldEligibleServiceDate.setText(DateTimeUtils.asString(apCalc
                    .getEligibleServiceDate()));
            jTextFieldEntryFee.setText(Percent.getPercentInstance().toString(
                    apCalc.getEntryFee()));
            // jTextFieldNetEarningRate.setText(
            // Percent.getPercentInstance().toString( apCalc.getNetEarningRate()
            // ) );
            jTextFieldAnnualIncreasePayments.setText(Percent
                    .getPercentInstance().toString(
                            apCalc.getAnnualIncreasePayments()));
            jTextFieldOngoingFee.setText(Percent.getPercentInstance().toString(
                    apCalc.getOngoingFee()));
            jTextFieldProjectToAge
                    .setText((apCalc.getProjectToAge() == null || apCalc.ZERO
                            .equals(apCalc.getProjectToAge())) ? new String(
                            "90") : apCalc.getProjectToAge().toString());
            jTextFieldOther.setText(Currency.getCurrencyInstance().toString(
                    apCalc.getOtherValue()));

            // for saving dialog window
            jComboBoxRelationship.setSelectedItem(new APRelationshipCode()
                    .getCode(apCalc.getRelationshipID()));
            jTextFieldNamePartner
                    .setText(apCalc.getPartnerName() == null ? null : apCalc
                            .getPartnerName().trim());
            jTextFieldDOBPartner.setText(DateTimeUtils.asString(apCalc
                    .getPartnerDOB()));

            jComboBoxSexPartner.setSelectedItem(new SexCode()
                    .getCodeDescription(apCalc.getPartnerSexCodeID()));

            jTextFieldPre071983.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getPre()));
            jTextFieldPost061983Taxed.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getPostTaxed()));
            jTextFieldPost061983Untaxed.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getPost061983UntaxedTotal()));
            jTextFieldUndeducted.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getUndeductedTotal()));
            jTextFieldCGTExempt.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getCGTExemptTotal()));
            jTextFieldExcess.setText(Currency.getCurrencyInstance().toString(
                    apCalc.getExcessTotal()));
            jTextFieldConcessional.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getConcessionalTotal()));
            jTextFieldInvalidity.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getInvalidityTotal()));

            String s = Percent.getPercentInstance().toString(
                    apCalc.getNetEarningRate());
            jTextFieldNetEarningRate.setText(s);
            jTextFieldClientName.setText(apCalc.getClientName() == null ? null
                    : apCalc.getClientName().trim());
            jTextFieldDOB.setText(DateTimeUtils.asString(apCalc
                    .getDateOfBirth()));
            jTextFieldNamePartner
                    .setText(apCalc.getPartnerName() == null ? null : apCalc
                            .getPartnerName().trim());
            jTextFieldDOBPartner.setText(DateTimeUtils.asString(apCalc
                    .getPartnerDOB()));
            jTextFieldPensionStartDate.setText(DateTimeUtils.asString(apCalc
                    .getPensionStartDate()));
            jTextFieldFirstPaymentDate.setText(DateTimeUtils.asString(apCalc
                    .getFirstPaymentDate()));

        } finally {
            apCalc.enableUpdate();

        }
    }

    private void updateNonEditable() {
        updateOthers();
        if (apCalc.getIsClient() == null || apCalc.getIsClient().booleanValue())
            jTextFieldAge
                    .setText(""
                            + (apCalc.getAge(apCalc.getDateOfBirth()) == apCalc.UNKNOWN_VALUE ? 0
                                    : apCalc.getAge(apCalc.getDateOfBirth())));
        else
            jTextFieldAge
                    .setText(""
                            + (apCalc.getAge(apCalc.getPartnerDOB()) == apCalc.UNKNOWN_VALUE ? 0
                                    : apCalc.getAge(apCalc.getPartnerDOB())));

        jTextFieldMinimum.setText(money.roundToTen(
                apCalc.getAnnualMinimumAmount()).toString());
        jTextFieldMaximum.setText(money.roundToTen(
                apCalc.getAnnualMaximumAmount()).toString());
        jTextFieldUPP.setText(Currency.getCurrencyInstance().toString(
                apCalc.getATOUPPAmount()));

        jTextFieldDeductibleAmount.setText(Currency.getCurrencyInstance()
                .toString(apCalc.getATOAnnualDeductibleAmount()));

        if (apCalc != null && apCalc.isReady()) {
            apCalc.calculate();
            updateChart();
            initDetailedTable();
            updateComponents();
        }

    }

    private void updateOthers() {
        jComboBoxSex.setSelectedItem(new SexCode().getCodeDescription(apCalc
                .getSexCodeID()));
        jComboBoxSexPartner.setSelectedItem(new SexCode()
                .getCodeDescription(apCalc.getPartnerSexCodeID()));

        if (apCalc.getRO() != null && apCalc.getRO().booleanValue())
            jRadioButtonYes.setSelected(true);
        else
            jRadioButtonNo.setSelected(true);

        if (apCalc.getFlatRate() != null && apCalc.getFlatRate().booleanValue())
            jRadioButtonNetEarningRateFlat.setSelected(true);
        else
            jRadioButtonNetEarningRateVariable.setSelected(true);

        if (apCalc.getGeneralTaxExemptionOptionID() == null)
            apCalc
                    .setGeneralTaxExemptionOptionID(GeneralTaxExemptionCodeID.GENERAL_EXEMPTION);

        jComboBoxGeneralTaxExemption
                .setSelectedItem(new GeneralTaxExemptionCode().getCode(apCalc
                        .getGeneralTaxExemptionOptionID()));
        jComboBoxDeathBenefit.setSelectedItem(new DeathBenefitCode()
                .getCode(apCalc.getDeathBenefitID()));
        /*
         * jComboBoxPensionFrequency.setSelectedItem( new
         * FrequencyCode().getCodeDescription(
         * apCalc.getPensionFrequencyCodeID() ));
         */jComboBoxSelectedAnnualPension
                .setSelectedItem(new SelectedAnnualPensionCode().getCode(apCalc
                        .getSelectedAnnualPensionType()));
        jComboBoxInvestmentStrategy
                .setSelectedItem(new InvestmentStrategyCode()
                        .getCodeDescription(apCalc.getInvStrategyCodeID()));

        jTextFieldNetEarningRate.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldClientName.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldNamePartner.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);

        try {
            String s = Percent.getPercentInstance().toString(
                    apCalc.getNetEarningRate());
            jTextFieldNetEarningRate.setText(s);
            jTextFieldClientName.setText(apCalc.getClientName() == null ? null
                    : apCalc.getClientName().trim());
            jTextFieldNamePartner
                    .setText(apCalc.getPartnerName() == null ? null : apCalc
                            .getPartnerName().trim());
        } finally {
            jTextFieldNetEarningRate.getDocument().putProperty(
                    DocumentNames.READY, Boolean.TRUE);
            jTextFieldClientName.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldNamePartner.getDocument().putProperty(
                    DocumentNames.READY, Boolean.TRUE);
        }

    }

    private void updateKnownComponents() {

        PersonService person = ServiceLocator.getInstance().getClientPerson();

        if (person == null)
            return;
        try {
            PersonName personName = person.getPersonName();

            if (apCalc.getClientName() == null)
                apCalc.setClientName(personName == null ? null : personName
                        .getFullName());
            if (apCalc.getDateOfBirth() == null)
                apCalc.setDateOfBirth(personName == null ? null : personName
                        .getDateOfBirth());

            // update partner details
            PersonService partner = ((ClientService) person).getPartner(false);
            if (partner != null
                    && new Integer(0).equals(apCalc.getRelationshipID())) {
                apCalc.setRelationshipID(APRelationshipCodeID.SPOUSE);
                PersonName name = partner.getPersonName();
                if (personName != null) {
                    apCalc.setPartnerName(name.getFullName());
                    apCalc.setPartnerDOB(name.getDateOfBirth());
                    apCalc.setPartnerSexCodeID(name.getSexCodeID());
                }
            }
        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    private void updateETPDetailsViewDataForSave() {
        jTextFieldPost061983Untaxed.getDocument().putProperty(
                DocumentNames.READY, Boolean.FALSE);
        jTextFieldUndeducted.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldCGTExempt.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldExcess.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldConcessional.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        jTextFieldInvalidity.getDocument().putProperty(DocumentNames.READY,
                Boolean.FALSE);
        try {
            jTextFieldPost061983Untaxed.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getPost061983UntaxedTotal()));

            jTextFieldUndeducted.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getUndeductedTotal()));

            jTextFieldCGTExempt.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getCGTExemptTotal()));

            jTextFieldExcess.setText(Currency.getCurrencyInstance().toString(
                    apCalc.getExcessTotal()));

            jTextFieldConcessional.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getConcessionalTotal()));

            jTextFieldInvalidity.setText(Currency.getCurrencyInstance()
                    .toString(apCalc.getInvalidityTotal()));
        } finally {
            jTextFieldPost061983Untaxed.getDocument().putProperty(
                    DocumentNames.READY, Boolean.TRUE);
            jTextFieldUndeducted.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldCGTExempt.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldExcess.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
            jTextFieldConcessional.getDocument().putProperty(
                    DocumentNames.READY, Boolean.TRUE);
            jTextFieldInvalidity.getDocument().putProperty(DocumentNames.READY,
                    Boolean.TRUE);
        }
    }

    private void updateReversionaryOptionViewDataForSave() {
        jComboBoxRelationship.setSelectedItem(new APRelationshipCode()
                .getCode(apCalc.getRelationshipID()));

        if (apCalc.getIsClient() == null || apCalc.getIsClient().booleanValue()) {
            jTextFieldNamePartner.getDocument().putProperty(
                    DocumentNames.READY, Boolean.FALSE);
            jTextFieldDOBPartner.getDocument().putProperty(DocumentNames.READY,
                    Boolean.FALSE);
            try {
                jTextFieldNamePartner.setText(apCalc.getPartnerName());
                jTextFieldDOBPartner.setText(DateTimeUtils.asString(apCalc
                        .getPartnerDOB()));
                jComboBoxSexPartner.setSelectedItem(new SexCode()
                        .getCodeDescription(apCalc.getPartnerSexCodeID()));
            } finally {
                jTextFieldNamePartner.getDocument().putProperty(
                        DocumentNames.READY, Boolean.TRUE);
                jTextFieldDOBPartner.getDocument().putProperty(
                        DocumentNames.READY, Boolean.TRUE);
            }
        } else {
            jTextFieldClientName.getDocument().putProperty(DocumentNames.READY,
                    Boolean.FALSE);
            jTextFieldDOB.getDocument().putProperty(DocumentNames.READY,
                    Boolean.FALSE);
            try {
                jTextFieldClientName.setText(apCalc.getClientName());
                jTextFieldDOB.setText(DateTimeUtils.asString(apCalc
                        .getDateOfBirth()));
                jComboBoxSex.setSelectedItem(new SexCode()
                        .getCodeDescription(apCalc.getSexCodeID()));
            } finally {
                jTextFieldClientName.getDocument().putProperty(
                        DocumentNames.READY, Boolean.TRUE);
                jTextFieldDOB.getDocument().putProperty(DocumentNames.READY,
                        Boolean.TRUE);
            }
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

                PersonService person = ServiceLocator.getInstance().getClientPerson();
                if (person == null)
                    return;

                try {
                    person.removeModel(getModel());
                    person.storeModels();
                } catch (com.argus.financials.service.ServiceException e) {
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

    public IGraphView getGraphView() {
        return graphView;
    }
}
