/*
 * SnapEntry.java
 *
 * Created on 10 September 2001, 11:42
 */

package au.com.totemsoft.myplanner.swing.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.CurrencyLabelGenerator;
import au.com.totemsoft.format.Number2;
import au.com.totemsoft.format.Percent;
import au.com.totemsoft.io.IOUtils2;
import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.IMaritalCode;
import au.com.totemsoft.myplanner.api.bean.IPerson;
import au.com.totemsoft.myplanner.api.code.FinancialClassID;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.FinancialGoal;
import au.com.totemsoft.myplanner.chart.StrategyResultView;
import au.com.totemsoft.myplanner.code.InvestmentStrategyCode;
import au.com.totemsoft.myplanner.code.ModelType;
import au.com.totemsoft.myplanner.code.ModelTypeID;
import au.com.totemsoft.myplanner.code.OwnerCode;
import au.com.totemsoft.myplanner.code.SexCode;
import au.com.totemsoft.myplanner.config.ViewSettings;
import au.com.totemsoft.myplanner.config.WordSettings;
import au.com.totemsoft.myplanner.etc.ActionEventID;
import au.com.totemsoft.myplanner.etc.DuplicateException;
import au.com.totemsoft.myplanner.etc.ModelTitleRestrictionException;
import au.com.totemsoft.myplanner.projection.AssetAllocationView;
import au.com.totemsoft.myplanner.projection.AssetGrowthLinked;
import au.com.totemsoft.myplanner.projection.CurrentPositionCalc;
import au.com.totemsoft.myplanner.projection.CurrentPositionComment;
import au.com.totemsoft.myplanner.projection.DocumentNames;
import au.com.totemsoft.myplanner.projection.DocumentUtils;
import au.com.totemsoft.myplanner.projection.MoneyCalc;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.swing.AbstractPanel;
import au.com.totemsoft.myplanner.swing.BaseView;
import au.com.totemsoft.myplanner.swing.FinancialPlannerApp;
import au.com.totemsoft.swing.CurrencyInputVerifier;
import au.com.totemsoft.swing.DateInputVerifier;
import au.com.totemsoft.swing.PercentInputVerifier;
import au.com.totemsoft.swing.SwingUtil;
import au.com.totemsoft.swing.SwingUtils;
import au.com.totemsoft.util.DateTimeUtils;

public class SnapEntryView
    extends AbstractPanel
    implements ActionEventID,
        javax.swing.event.ChangeListener, au.com.totemsoft.swing.ICloseDialog,
        FinancialClassID {

    public static final double HOLE = MoneyCalc.HOLE;

    private static final int RESULT_PAGE_ID = 1;

    // at least one series (PRE_RETIREMENT)
    private double[][] dataAggregator = new double[1][];

    private boolean newModel;

    private Number2 number = MoneyCalc.getNumberInstance();

    private Percent percent = MoneyCalc.getPercentInstance();

    private Currency curr = MoneyCalc.getCurrencyInstance();

    public Integer getDefaultType() {
        return ModelTypeID.rcCURRENT_POSITION_CALC.getCodeId();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcCURRENT_POSITION_CALC.getDescription();
    }

    //
    // Super, Savings, Investments, Property, BusinessService, Other
    private java.util.Map assetViews;

    // to calculate total/spend values
    private CurrentPositionCalc snapshotCalc;

    private StrategyResultView graphView;

    /** Creates new form SnapEntry */
    public SnapEntryView() {
        this(new CurrentPositionCalc());
    }

    public SnapEntryView(MoneyCalc calc) {
        snapshotCalc = (CurrentPositionCalc) calc;

        initComponents();
        initComponents2();
    }

    private Model getModel() {
        Model model = snapshotCalc.getModel();
        if (model.getOwner() != null)
            return model;

        PersonService person = clientService;
        if (person != null) {
            try {
                model.setOwner(person.getModels());
            } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
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

    private AssetView addAsset(Integer assetCodeID, int index) {

        AssetGrowthLinked ag;

        if (snapshotCalc.getAssetCode() == null) {
            ag = snapshotCalc;
            snapshotCalc.setAssetCode(assetCodeID);
        } else {
            ag = new AssetGrowthLinked(assetCodeID);
            snapshotCalc.setNextAsset(ag);
        }

        AssetView av = new AssetView(assetCodeID, jPanelAssets, index);

        // set model for this view
        av.setAssetGrowth(ag);

        // add views for this model
        ag.addChangeListener(av);
        ag.addChangeListener(this);

        // create number of asset views
        if (assetViews == null)
            assetViews = new java.util.HashMap();
        assetViews.put(assetCodeID, av);

        return av;

    }

    private void initComponents2() {

        // do not need this
        // jPanelDSS.setVisible( false );
        // jCheckBoxApplySurcharge.setVisible( false );
        jLabelPartnerTaxRate.setVisible(false);
        jTextFieldPartnerTaxRate.setVisible(false);
        jLabelRequiredIncomePartial.setVisible(false);
        jTextFieldRequiredIncomePartial.setVisible(false);

        graphView = new StrategyResultView();
        SwingUtils.setDefaultFont(graphView);

        jLabelISBefore
                .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelISAfter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        jComboBoxInvestmentStrategyAfter.setSelectedItem(null);
        jComboBoxInvestmentStrategyBefore.setSelectedItem(null);

        int i = 0;
        addAsset(ASSET_CASH, i++);
        addAsset(ASSET_SUPERANNUATION, i++);
        AssetView av = addAsset(ASSET_INVESTMENT, i++);

        // av.addTotalField( jTextFieldTotalAssets );

        _setAccessibleContext();
        _addUndoableEditListener();

        snapshotCalc.addChangeListener(this);
        DocumentUtils.addListener(this, snapshotCalc); // after
                                                        // _setAccessibleContext()
        updateEditable();

        jButtonSave
                .setEnabled(clientService != null);
        jButtonSaveAs
                .setEnabled(clientService != null);
        jButtonDelete.setEnabled(jButtonSave.isEnabled());
        setActionMap();

    }

    private void _addUndoableEditListener() {
        jTextFieldInflation.getDocument().addUndoableEditListener(
                MoneyCalc.getUndoManager());

    }

    private void _setAccessibleContext() {

        jTextFieldClientDOB.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.DOB);
        jRadioButtonClientMale.getAccessibleContext().setAccessibleName(
                DocumentNames.SEX_CODE_MALE);
        jRadioButtonClientFemale.getAccessibleContext().setAccessibleName(
                DocumentNames.SEX_CODE_FEMALE);
        jTextFieldClientTargetAge.getAccessibleContext().setAccessibleName(
                DocumentNames.TARGET_AGE);

        jTextFieldPartnerDOB.getDateField().getAccessibleContext()
                .setAccessibleName(DocumentNames.DOB_PARTNER);
        jRadioButtonPartnerMale.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SEX_CODE_MALE);
        jRadioButtonPartnerFemale.getAccessibleContext().setAccessibleName(
                DocumentNames.PARTNER_SEX_CODE_FEMALE);
        jTextFieldPartnerTargetAge.getAccessibleContext().setAccessibleName(
                DocumentNames.TARGET_AGE_PARTNER);

        jRadioButtonFinancialIndependence.getAccessibleContext()
                .setAccessibleName(DocumentNames.GOAL_FIN_INDEPENDENCE);
        jRadioButtonRetirement.getAccessibleContext().setAccessibleName(
                DocumentNames.GOAL_RETIREMENT);

        jTextFieldYearsRequiredIncome.getAccessibleContext().setAccessibleName(
                DocumentNames.YEARS_PROJECT_RETIRE_INCOME);

        jCheckBoxIndexContributions.getAccessibleContext().setAccessibleName(
                DocumentNames.INDEXED);
        jTextFieldInflation.getAccessibleContext().setAccessibleName(
                DocumentNames.INDEX_RATE);
        jTextFieldEntryFees.getAccessibleContext().setAccessibleName(
                DocumentNames.ENTRY_FEES);
        jTextFieldReviewFees.getAccessibleContext().setAccessibleName(
                DocumentNames.REVIEW_FEES);
        jTextFieldClientTaxRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_RATE);
        jTextFieldPartnerTaxRate.getAccessibleContext().setAccessibleName(
                DocumentNames.TAX_RATE_PARTNER);

        jComboBoxInvestmentStrategyBefore.getAccessibleContext()
                .setAccessibleName(DocumentNames.INV_STRATEGY_AG);
        jCheckBoxApplySurcharge.getAccessibleContext().setAccessibleName(
                DocumentNames.SUR_CHARGE);

        jComboBoxInvestmentStrategyAfter.getAccessibleContext()
                .setAccessibleName(DocumentNames.INV_STRATEGY_AS);
        jTextFieldRequiredIncomePartial.getAccessibleContext()
                .setAccessibleName(DocumentNames.REQUIRED_INCOME_PART);
        jTextFieldRequiredIncomeFull.getAccessibleContext().setAccessibleName(
                DocumentNames.REQUIRED_INCOME_FULL);

        jCheckBoxIncludeDSS.getAccessibleContext().setAccessibleName(
                DocumentNames.INCLUDE_DSS);
        jCheckBoxHomeOwner.getAccessibleContext().setAccessibleName(
                DocumentNames.HOME_OWNER);

        jTextFieldUPPato.getAccessibleContext().setAccessibleName(
                DocumentNames.ATO_UPP);
        jTextFieldUPPdss.getAccessibleContext().setAccessibleName(
                DocumentNames.DSS_UPP);
        jTextFieldRebate.getAccessibleContext().setAccessibleName(
                DocumentNames.REBATE);
        jTextFieldResidualRequired.getAccessibleContext().setAccessibleName(
                DocumentNames.REQUIRED_RESIDUAL);

    }

    public void setActionListener(java.awt.event.ActionListener al) {
        // to notify main form/applet
        jButtonClose.addActionListener(al);
        // jButtonSave.addActionListener( al );
    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        updateNonEditable();
        updateComponents();
        updateChart();
    }

    private void updateChart() {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            dataAggregator = snapshotCalc.getChartData();
            if (dataAggregator == null)
                return;

            // do chart
            graphView.removeChartLabels();

            graphView
                    .addChartLabels(graphView.customizeChart(dataAggregator,
                            snapshotCalc.getLabels(), snapshotCalc
                                    .getLegends(dataAggregator.length),
                            snapshotCalc.getColors(), true), Currency
                            .getCurrencyInstance());
            graphView.setTitleAxisY1("");
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                    .getInstance());

            // let user to go to chart
            updateComponents();

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupSingleJoint = new javax.swing.ButtonGroup();
        buttonGroupGoal = new javax.swing.ButtonGroup();
        buttonGroupClientSex = new javax.swing.ButtonGroup();
        buttonGroupPartnerSex = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelDetails = new javax.swing.JPanel();
        jPanelAge = new javax.swing.JPanel();
        jPanelClientDetails = new javax.swing.JPanel();
        jPanelSingle = new javax.swing.JPanel();
        jRadioButtonSingle = new javax.swing.JRadioButton();
        jRadioButtonJoint = new javax.swing.JRadioButton();
        jPanelClient = new javax.swing.JPanel();
        jLabelClientDOB = new javax.swing.JLabel();
        jTextFieldClientDOB = new au.com.totemsoft.bean.FDateChooser();
        jLabelClientAge = new javax.swing.JLabel();
        jTextFieldClientAge = new javax.swing.JTextField();
        jRadioButtonClientMale = new javax.swing.JRadioButton();
        jRadioButtonClientFemale = new javax.swing.JRadioButton();
        jPanelPartner = new javax.swing.JPanel();
        jLabelPartnerDOB = new javax.swing.JLabel();
        jTextFieldPartnerDOB = new au.com.totemsoft.bean.FDateChooser();
        jLabelPartnerAge = new javax.swing.JLabel();
        jTextFieldPartnerAge = new javax.swing.JTextField();
        jRadioButtonPartnerMale = new javax.swing.JRadioButton();
        jRadioButtonPartnerFemale = new javax.swing.JRadioButton();
        jLabelYearsRequiredIncome1 = new javax.swing.JLabel();
        jPanelClientGoal = new javax.swing.JPanel();
        jPanelGoal = new javax.swing.JPanel();
        jRadioButtonFinancialIndependence = new javax.swing.JRadioButton();
        jRadioButtonRetirement = new javax.swing.JRadioButton();
        jLabelYearsRequiredIncome = new javax.swing.JLabel();
        jTextFieldYearsRequiredIncome = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldClientTargetAge = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldClientTargetDate = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldPartnerTargetAge = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldPartnerTargetDate = new javax.swing.JTextField();
        jLabelLifeExpectancy = new javax.swing.JLabel();
        jPanelFeesTax = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldInflation = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldEntryFees = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldReviewFees = new javax.swing.JTextField();
        jLabelClientTaxRate = new javax.swing.JLabel();
        jTextFieldClientTaxRate = new javax.swing.JTextField();
        jLabelPartnerTaxRate = new javax.swing.JLabel();
        jTextFieldPartnerTaxRate = new javax.swing.JTextField();
        jCheckBoxIndexContributions = new javax.swing.JCheckBox();
        jPanelAssetsTotal = new javax.swing.JPanel();
        jPanelInvStrategy = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jComboBoxInvestmentStrategyBefore = new javax.swing.JComboBox(
                new InvestmentStrategyCode().getCodeDescriptions());
        jLabelISBefore = new javax.swing.JLabel();
        jCheckBoxApplySurcharge = new javax.swing.JCheckBox();
        jPanelAssets = new javax.swing.JPanel();
        jPanelTotal = new javax.swing.JPanel();
        jTextFieldTotalAssets = new javax.swing.JTextField();
        jPanelAssumptions = new javax.swing.JPanel();
        jPanelAssumptions1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxInvestmentStrategyAfter = new javax.swing.JComboBox(
                new InvestmentStrategyCode().getCodeDescriptions());
        jTextFieldRequiredIncomePartial = new javax.swing.JTextField();
        jTextFieldRequiredIncomeFull = new javax.swing.JTextField();
        jLabelRequiredIncomeFull = new javax.swing.JLabel();
        jLabelRequiredIncomePartial = new javax.swing.JLabel();
        jPanelDSS = new javax.swing.JPanel();
        jCheckBoxIncludeDSS = new javax.swing.JCheckBox();
        jCheckBoxHomeOwner = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldResidualRequired = new javax.swing.JTextField();
        jLabelISAfter = new javax.swing.JLabel();
        jPanelAssumptions2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldUPPato = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldUPPdss = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldRebate = new javax.swing.JTextField();
        jPanelControls = new javax.swing.JPanel();
        jPanelReportCloseSave = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPanelPrevNext = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(700, 600));
        setMinimumSize(new java.awt.Dimension(700, 600));
        jTabbedPane.setPreferredSize(new java.awt.Dimension(600, 2000));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelDetails.setLayout(new javax.swing.BoxLayout(jPanelDetails,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelDetails.setPreferredSize(new java.awt.Dimension(670, 300));
        jPanelDetails.setFont(new java.awt.Font("Arial", 0, 11));
        jPanelAge.setLayout(new java.awt.GridLayout(1, 2));

        jPanelClientDetails.setLayout(new java.awt.GridBagLayout());

        jPanelClientDetails.setBorder(new javax.swing.border.TitledBorder(null,
                "ClientView Details",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Arial", 0, 11)));
        jPanelSingle
                .setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanelSingle.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 5, 1, 1)));
        jRadioButtonSingle.setText("Single");
        buttonGroupSingleJoint.add(jRadioButtonSingle);
        jRadioButtonSingle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonSingleItemStateChanged(evt);
            }
        });

        jPanelSingle.add(jRadioButtonSingle);

        jRadioButtonJoint.setText("Joint");
        buttonGroupSingleJoint.add(jRadioButtonJoint);
        jRadioButtonJoint.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonSingleItemStateChanged(evt);
            }
        });

        jPanelSingle.add(jRadioButtonJoint);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelClientDetails.add(jPanelSingle, gridBagConstraints);

        jPanelClient.setLayout(new java.awt.GridBagLayout());

        jPanelClient.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jPanelClient.setToolTipText("ClientView's");
        jLabelClientDOB.setText("DOB");
        jLabelClientDOB.setToolTipText("ClientView's DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClient.add(jLabelClientDOB, gridBagConstraints);

        jTextFieldClientDOB.setToolTipText("ClientView's DOB");
        jTextFieldClientDOB.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldClientDOB.setPreferredSize(new java.awt.Dimension(70, 21));
        jTextFieldClientDOB.setMinimumSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelClient.add(jTextFieldClientDOB, gridBagConstraints);

        jLabelClientAge.setText("Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelClient.add(jLabelClientAge, gridBagConstraints);

        jTextFieldClientAge.setEditable(false);
        jTextFieldClientAge
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientAge.setPreferredSize(new java.awt.Dimension(30, 21));
        jTextFieldClientAge.setMinimumSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelClient.add(jTextFieldClientAge, gridBagConstraints);

        jRadioButtonClientMale.setText("Male");
        buttonGroupClientSex.add(jRadioButtonClientMale);
        jRadioButtonClientMale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonClientMaleItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelClient.add(jRadioButtonClientMale, gridBagConstraints);

        jRadioButtonClientFemale.setText("Female");
        buttonGroupClientSex.add(jRadioButtonClientFemale);
        jRadioButtonClientFemale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonClientMaleItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanelClient.add(jRadioButtonClientFemale, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelClientDetails.add(jPanelClient, gridBagConstraints);

        jPanelPartner.setLayout(new java.awt.GridBagLayout());

        jPanelPartner.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jPanelPartner.setToolTipText("Partner's");
        jLabelPartnerDOB.setText("DOB");
        jLabelPartnerDOB.setToolTipText("Partner's DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelPartner.add(jLabelPartnerDOB, gridBagConstraints);

        jTextFieldPartnerDOB.setToolTipText("Partner's DOB");
        jTextFieldPartnerDOB.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldPartnerDOB.setPreferredSize(new java.awt.Dimension(70, 21));
        jTextFieldPartnerDOB.setMinimumSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPartner.add(jTextFieldPartnerDOB, gridBagConstraints);

        jLabelPartnerAge.setText("Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPartner.add(jLabelPartnerAge, gridBagConstraints);

        jTextFieldPartnerAge.setEditable(false);
        jTextFieldPartnerAge
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerAge.setPreferredSize(new java.awt.Dimension(30, 21));
        jTextFieldPartnerAge.setMinimumSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPartner.add(jTextFieldPartnerAge, gridBagConstraints);

        jRadioButtonPartnerMale.setText("Male");
        buttonGroupPartnerSex.add(jRadioButtonPartnerMale);
        jRadioButtonPartnerMale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPartnerMaleItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelPartner.add(jRadioButtonPartnerMale, gridBagConstraints);

        jRadioButtonPartnerFemale.setText("Female");
        buttonGroupPartnerSex.add(jRadioButtonPartnerFemale);
        jRadioButtonPartnerFemale
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPartnerMaleItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanelPartner.add(jRadioButtonPartnerFemale, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelClientDetails.add(jPanelPartner, gridBagConstraints);

        jLabelYearsRequiredIncome1.setText("   ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelClientDetails.add(jLabelYearsRequiredIncome1, gridBagConstraints);

        jPanelAge.add(jPanelClientDetails);

        jPanelClientGoal.setLayout(new java.awt.GridBagLayout());

        jPanelClientGoal.setBorder(new javax.swing.border.TitledBorder("Goal"));
        jPanelGoal.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jRadioButtonFinancialIndependence
                .setText("Wealth Accumulation Projection");
        buttonGroupGoal.add(jRadioButtonFinancialIndependence);
        jRadioButtonFinancialIndependence
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonFinancialIndependenceItemStateChanged(evt);
                    }
                });

        jPanelGoal.add(jRadioButtonFinancialIndependence);

        jRadioButtonRetirement.setSelected(true);
        jRadioButtonRetirement.setText("Retirement");
        buttonGroupGoal.add(jRadioButtonRetirement);
        jRadioButtonRetirement
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonFinancialIndependenceItemStateChanged(evt);
                    }
                });

        jPanelGoal.add(jRadioButtonRetirement);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelClientGoal.add(jPanelGoal, gridBagConstraints);

        jLabelYearsRequiredIncome.setText("Years Project Retire Inc.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelClientGoal.add(jLabelYearsRequiredIncome, gridBagConstraints);

        jTextFieldYearsRequiredIncome
                .setToolTipText("Enter the number of years you wish to calculate the retirement income for.");
        jTextFieldYearsRequiredIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldYearsRequiredIncome.setPreferredSize(new java.awt.Dimension(
                30, 21));
        jTextFieldYearsRequiredIncome.setMinimumSize(new java.awt.Dimension(30,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelClientGoal.add(jTextFieldYearsRequiredIncome, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel5.setText("Target Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel5, gridBagConstraints);

        jTextFieldClientTargetAge
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientTargetAge.setPreferredSize(new java.awt.Dimension(30,
                21));
        jTextFieldClientTargetAge
                .setMinimumSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(jTextFieldClientTargetAge, gridBagConstraints);

        jLabel7.setText("Goal Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(jLabel7, gridBagConstraints);

        jTextFieldClientTargetDate.setEditable(false);
        jTextFieldClientTargetDate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientTargetDate.setPreferredSize(new java.awt.Dimension(70,
                21));
        jTextFieldClientTargetDate
                .setMinimumSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel1.add(jTextFieldClientTargetDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelClientGoal.add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 10, 1, 10)));
        jLabel6.setText("Target Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel6, gridBagConstraints);

        jTextFieldPartnerTargetAge
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerTargetAge.setPreferredSize(new java.awt.Dimension(30,
                21));
        jTextFieldPartnerTargetAge
                .setMinimumSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(jTextFieldPartnerTargetAge, gridBagConstraints);

        jLabel8.setText("Goal Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel2.add(jLabel8, gridBagConstraints);

        jTextFieldPartnerTargetDate.setEditable(false);
        jTextFieldPartnerTargetDate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerTargetDate.setPreferredSize(new java.awt.Dimension(70,
                21));
        jTextFieldPartnerTargetDate.setMinimumSize(new java.awt.Dimension(70,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanel2.add(jTextFieldPartnerTargetDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanelClientGoal.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelClientGoal.add(jLabelLifeExpectancy, gridBagConstraints);

        jPanelAge.add(jPanelClientGoal);

        jPanelDetails.add(jPanelAge);

        jPanelFeesTax.setLayout(new java.awt.GridBagLayout());

        jPanelFeesTax.setBorder(new javax.swing.border.TitledBorder(
                "Earnings Assumptions"));
        jLabel12.setText("Indexation Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelFeesTax.add(jLabel12, gridBagConstraints);

        jTextFieldInflation
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldInflation.setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldInflation
                .setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldInflation.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelFeesTax.add(jTextFieldInflation, gridBagConstraints);

        jLabel15.setText("Entry Fees on Contributions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelFeesTax.add(jLabel15, gridBagConstraints);

        jTextFieldEntryFees
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEntryFees.setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldEntryFees
                .setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldEntryFees.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelFeesTax.add(jTextFieldEntryFees, gridBagConstraints);

        jLabel16.setText("Additional Ongoing Fees");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelFeesTax.add(jLabel16, gridBagConstraints);

        jTextFieldReviewFees
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldReviewFees.setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldReviewFees.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldReviewFees.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelFeesTax.add(jTextFieldReviewFees, gridBagConstraints);

        jLabelClientTaxRate.setText("Tax Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelFeesTax.add(jLabelClientTaxRate, gridBagConstraints);

        jTextFieldClientTaxRate
                .setToolTipText("Rate of tax applied to investment and cash income");
        jTextFieldClientTaxRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldClientTaxRate
                .setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldClientTaxRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldClientTaxRate.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelFeesTax.add(jTextFieldClientTaxRate, gridBagConstraints);

        jLabelPartnerTaxRate.setText("Partner Tax Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        jPanelFeesTax.add(jLabelPartnerTaxRate, gridBagConstraints);

        jTextFieldPartnerTaxRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPartnerTaxRate
                .setPreferredSize(new java.awt.Dimension(50, 21));
        jTextFieldPartnerTaxRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldPartnerTaxRate.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelFeesTax.add(jTextFieldPartnerTaxRate, gridBagConstraints);

        jCheckBoxIndexContributions.setText("Index Contributions");
        jCheckBoxIndexContributions
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBoxIndexContributionsItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFeesTax.add(jCheckBoxIndexContributions, gridBagConstraints);

        jPanelDetails.add(jPanelFeesTax);

        jPanelAssetsTotal.setLayout(new javax.swing.BoxLayout(
                jPanelAssetsTotal, javax.swing.BoxLayout.Y_AXIS));

        jPanelAssetsTotal.setBorder(new javax.swing.border.TitledBorder(
                "Current Investment Assets"));
        jPanelInvStrategy.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT, 10, 5));

        jLabel24.setText("Investment Strategies before target age");
        jLabel24.setPreferredSize(new java.awt.Dimension(230, 15));
        jLabel24.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanelInvStrategy.add(jLabel24);

        jComboBoxInvestmentStrategyBefore
                .setPreferredSize(new java.awt.Dimension(150, 21));
        jComboBoxInvestmentStrategyBefore
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxInvestmentStrategyBeforeItemStateChanged(evt);
                    }
                });

        jPanelInvStrategy.add(jComboBoxInvestmentStrategyBefore);

        jLabelISBefore.setText("Show");
        jLabelISBefore
                .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelISBefore.setPreferredSize(new java.awt.Dimension(48, 17));
        jLabelISBefore.setBorder(new javax.swing.border.EtchedBorder());
        jLabelISBefore.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelISBeforeMouseClicked(evt);
            }
        });

        jPanelInvStrategy.add(jLabelISBefore);

        jCheckBoxApplySurcharge.setText("Apply Surcharge");
        jCheckBoxApplySurcharge
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBoxApplySurchargeItemStateChanged(evt);
                    }
                });

        jPanelInvStrategy.add(jCheckBoxApplySurcharge);

        jPanelAssetsTotal.add(jPanelInvStrategy);

        jPanelAssets.setLayout(new java.awt.GridBagLayout());

        jPanelAssetsTotal.add(jPanelAssets);

        jPanelTotal.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.RIGHT, 20, 5));

        jTextFieldTotalAssets.setEditable(false);
        jTextFieldTotalAssets
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalAssets.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldTotalAssets.setMinimumSize(new java.awt.Dimension(100, 21));
        jPanelTotal.add(jTextFieldTotalAssets);

        jPanelAssetsTotal.add(jPanelTotal);

        jPanelDetails.add(jPanelAssetsTotal);

        jPanelAssumptions.setLayout(new javax.swing.BoxLayout(
                jPanelAssumptions, javax.swing.BoxLayout.X_AXIS));

        jPanelAssumptions1.setLayout(new java.awt.GridBagLayout());

        jPanelAssumptions1.setBorder(new javax.swing.border.TitledBorder(
                "Retirement Assumptions"));
        jLabel9.setText("Investment Strategies after target age");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanelAssumptions1.add(jLabel9, gridBagConstraints);

        jComboBoxInvestmentStrategyAfter
                .setPreferredSize(new java.awt.Dimension(170, 21));
        jComboBoxInvestmentStrategyAfter
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBoxInvestmentStrategyAfterItemStateChanged(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelAssumptions1.add(jComboBoxInvestmentStrategyAfter,
                gridBagConstraints);

        jTextFieldRequiredIncomePartial.setToolTipText("Today's Value");
        jTextFieldRequiredIncomePartial
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRequiredIncomePartial
                .setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldRequiredIncomePartial.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldRequiredIncomePartial.setMinimumSize(new java.awt.Dimension(
                90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelAssumptions1.add(jTextFieldRequiredIncomePartial,
                gridBagConstraints);

        jTextFieldRequiredIncomeFull.setToolTipText("Future Value");
        jTextFieldRequiredIncomeFull
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRequiredIncomeFull.setPreferredSize(new java.awt.Dimension(
                90, 21));
        jTextFieldRequiredIncomeFull.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldRequiredIncomeFull.setMinimumSize(new java.awt.Dimension(90,
                21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelAssumptions1
                .add(jTextFieldRequiredIncomeFull, gridBagConstraints);

        jLabelRequiredIncomeFull.setText("Retirement Income");
        jLabelRequiredIncomeFull
                .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelAssumptions1.add(jLabelRequiredIncomeFull, gridBagConstraints);

        jLabelRequiredIncomePartial.setText("partial");
        jLabelRequiredIncomePartial
                .setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelAssumptions1.add(jLabelRequiredIncomePartial, gridBagConstraints);

        jPanelDSS.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jCheckBoxIncludeDSS.setText("Include DSS");
        jCheckBoxIncludeDSS.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxIncludeDSSItemStateChanged(evt);
            }
        });

        jPanelDSS.add(jCheckBoxIncludeDSS);

        jCheckBoxHomeOwner.setText("Home Owner");
        jCheckBoxHomeOwner.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxHomeOwnerItemStateChanged(evt);
            }
        });

        jPanelDSS.add(jCheckBoxHomeOwner);

        jLabel23.setText("Residual Required");
        jPanelDSS.add(jLabel23);

        jTextFieldResidualRequired.setToolTipText(" Residual Required");
        jTextFieldResidualRequired
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldResidualRequired.setPreferredSize(new java.awt.Dimension(90,
                21));
        jTextFieldResidualRequired.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldResidualRequired
                .setMinimumSize(new java.awt.Dimension(90, 21));
        jPanelDSS.add(jTextFieldResidualRequired);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanelAssumptions1.add(jPanelDSS, gridBagConstraints);

        jLabelISAfter.setText("Show");
        jLabelISAfter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelISAfter.setPreferredSize(new java.awt.Dimension(48, 17));
        jLabelISAfter.setBorder(new javax.swing.border.EtchedBorder());
        jLabelISAfter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelISAfterMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanelAssumptions1.add(jLabelISAfter, gridBagConstraints);

        jPanelAssumptions.add(jPanelAssumptions1);

        jPanelAssumptions2.setLayout(new java.awt.GridBagLayout());

        jPanelAssumptions2.setBorder(new javax.swing.border.TitledBorder(
                "Disbursement Assumptions"));
        jLabel14.setText("Pension Rebate (%)");
        jLabel14.setToolTipText("A fully rebatable pension is 100%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        jPanelAssumptions2.add(jLabel14, gridBagConstraints);

        jTextFieldUPPato.setToolTipText("Usually undeducted contribution");
        jTextFieldUPPato
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUPPato.setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldUPPato.setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldUPPato.setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelAssumptions2.add(jTextFieldUPPato, gridBagConstraints);

        jLabel18.setText("ATO UPP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAssumptions2.add(jLabel18, gridBagConstraints);

        jTextFieldUPPdss.setToolTipText("Usually purchase price of pension");
        jTextFieldUPPdss
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUPPdss.setPreferredSize(new java.awt.Dimension(90, 21));
        jTextFieldUPPdss.setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldUPPdss.setMinimumSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelAssumptions2.add(jTextFieldUPPdss, gridBagConstraints);

        jLabel22.setText("DSS UPP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAssumptions2.add(jLabel22, gridBagConstraints);

        jTextFieldRebate.setToolTipText("A fully rebatable pension is 100%");
        jTextFieldRebate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRebate.setPreferredSize(new java.awt.Dimension(60, 21));
        jTextFieldRebate.setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldRebate.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        jPanelAssumptions2.add(jTextFieldRebate, gridBagConstraints);

        jPanelAssumptions.add(jPanelAssumptions2);

        jPanelDetails.add(jPanelAssumptions);

        jTabbedPane.addTab("Details", null, jPanelDetails, "");

        add(jTabbedPane);

        jPanelControls.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 40, 5));

        jButtonReport.setText("Report");
        jButtonReport.setDefaultCapable(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanelReportCloseSave.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelReportCloseSave.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setDefaultCapable(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelReportCloseSave.add(jButtonSave);

        jButtonSaveAs.setText("Save As");
        jButtonSaveAs.setDefaultCapable(false);
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsActionPerformed(evt);
            }
        });

        jPanelReportCloseSave.add(jButtonSaveAs);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanelReportCloseSave.add(jButtonDelete);

        jPanelControls.add(jPanelReportCloseSave);

        jButtonClear.setText("Clear");
        jButtonClear.setDefaultCapable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jPanelControls.add(jButtonClear);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.setActionCommand("Next");
        jButtonPrevious.setDefaultCapable(false);
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanelPrevNext.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.setActionCommand("Next");
        jButtonNext.setDefaultCapable(false);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanelPrevNext.add(jButtonNext);

        jPanelControls.add(jPanelPrevNext);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveAsActionPerformed
        // Add your handling code here:
        newModel = true;
        doSave(evt);
    }// GEN-LAST:event_jButtonSaveAsActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
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

    private void jRadioButtonFinancialIndependenceItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonFinancialIndependenceItemStateChanged
        if (evt.getSource() != jRadioButtonFinancialIndependence)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        snapshotCalc.setRetirement(jRadioButtonRetirement.isSelected());
        // setRetirementView( jRadioButtonRetirement.isSelected() );
        // updateComponents();
    }// GEN-LAST:event_jRadioButtonFinancialIndependenceItemStateChanged

    private void jRadioButtonSingleItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonSingleItemStateChanged
        if (evt.getSource() != jRadioButtonSingle)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        snapshotCalc.setSingle(jRadioButtonSingle.isSelected());
    }// GEN-LAST:event_jRadioButtonSingleItemStateChanged

    private void jRadioButtonPartnerMaleItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonPartnerMaleItemStateChanged
        if (evt.getSource() != jRadioButtonPartnerMale)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        snapshotCalc
                .setPartnerSexCodeID(jRadioButtonClientMale.isSelected() ? SexCode.MALE
                        : SexCode.FEMALE);
    }// GEN-LAST:event_jRadioButtonPartnerMaleItemStateChanged

    private void jRadioButtonClientMaleItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonClientMaleItemStateChanged
        if (evt.getSource() != jRadioButtonClientMale)
            return;
        JRadioButton rb = (JRadioButton) evt.getSource();

        snapshotCalc
                .setSexCodeID(jRadioButtonClientMale.isSelected() ? SexCode.MALE
                        : SexCode.FEMALE);
    }// GEN-LAST:event_jRadioButtonClientMaleItemStateChanged

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int result = BaseView.closeDialog(this);
        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        newModel = false;
        doSave(evt);
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jLabelISAfterMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabelISAfterMouseClicked
        AssetAllocationView view = getStrategyAfterRetirement(); // refresh
        if (view != null)
            SwingUtil.add2Dialog(null, view.getViewCaption(), true, view, true,
                    true);
    }// GEN-LAST:event_jLabelISAfterMouseClicked

    private void jLabelISBeforeMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabelISBeforeMouseClicked
        AssetAllocationView view = getStrategyBeforeRetirement(); // refresh
        if (view != null)
            SwingUtil.add2Dialog(null, view.getViewCaption(), true, view, true,
                    true);
    }// GEN-LAST:event_jLabelISBeforeMouseClicked

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        int index = jTabbedPane.getSelectedIndex() - 1;
        if (index >= 0)
            jTabbedPane.setSelectedIndex(index);
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    private void jCheckBoxIndexContributionsItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxIndexContributionsItemStateChanged
        if (evt.getSource() != jCheckBoxIndexContributions)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        snapshotCalc.setIndexed(cb.isSelected());
    }// GEN-LAST:event_jCheckBoxIndexContributionsItemStateChanged

    private void jCheckBoxHomeOwnerItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxHomeOwnerItemStateChanged
        if (evt.getSource() != jCheckBoxHomeOwner)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        snapshotCalc.setHomeOwner(cb.isSelected());
    }// GEN-LAST:event_jCheckBoxHomeOwnerItemStateChanged

    private void jCheckBoxIncludeDSSItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxIncludeDSSItemStateChanged
        if (evt.getSource() != jCheckBoxIncludeDSS)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        snapshotCalc.setIncludeDSS(cb.isSelected());
    }// GEN-LAST:event_jCheckBoxIncludeDSSItemStateChanged

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        int index = jTabbedPane.getSelectedIndex() + 1;
        if (jTabbedPane.getTabCount() > index)
            jTabbedPane.setSelectedIndex(index);
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jCheckBoxApplySurchargeItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxApplySurchargeItemStateChanged
        if (evt.getSource() != jCheckBoxApplySurcharge)
            return;
        JCheckBox cb = (JCheckBox) evt.getSource();

        snapshotCalc.setSurcharge(cb.isSelected());
    }// GEN-LAST:event_jCheckBoxApplySurchargeItemStateChanged

    private void jComboBoxInvestmentStrategyAfterItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxInvestmentStrategyAfterItemStateChanged
        if (evt.getSource() != jComboBoxInvestmentStrategyAfter)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            snapshotCalc.setInvestmentStrategyCodeIDSpend(null);
        else
            snapshotCalc
                    .setInvestmentStrategyCodeIDSpend(new InvestmentStrategyCode()
                            .getCodeID(s));
    }// GEN-LAST:event_jComboBoxInvestmentStrategyAfterItemStateChanged

    private void jComboBoxInvestmentStrategyBeforeItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxInvestmentStrategyBeforeItemStateChanged
        if (evt.getSource() != jComboBoxInvestmentStrategyBefore)
            return;
        JComboBox cb = (JComboBox) evt.getSource();

        String s = (String) cb.getSelectedItem();
        if (s == null || s.length() == 0)
            snapshotCalc.setInvestmentStrategyCodeID(null);
        else
            snapshotCalc
                    .setInvestmentStrategyCodeID(new InvestmentStrategyCode()
                            .getCodeID(s));
    }// GEN-LAST:event_jComboBoxInvestmentStrategyBeforeItemStateChanged

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);

        if (index == RESULT_PAGE_ID) {
            // update actual income
            graphView.setActualIncome(snapshotCalc.getActualIncome(),
                    snapshotCalc.getIndexRate(), snapshotCalc.getYearsInt());
            graphView.setRequiredIncome(snapshotCalc.getSpendValue(),
                    snapshotCalc.getIndexRate(), snapshotCalc.getYearsInt());
            graphView.setComment(CurrentPositionComment.getComment(snapshotCalc));
        }

    }// GEN-LAST:event_jTabbedPaneStateChanged

    private static SnapEntryView view;
    public static SnapEntryView display(final Model model, FocusListener[] listeners) {

       if (view == null) {
            view = new SnapEntryView();
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
        } catch (Exception e) {
            e.printStackTrace(System.err);
            view = null;
        }

        return view;

    }

    public AssetAllocationView getStrategyBeforeRetirement() {
        Integer id = new InvestmentStrategyCode()
                .getCodeID((String) (jComboBoxInvestmentStrategyBefore
                        .getSelectedItem()));
        if (id == null)
            return null;

        return new AssetAllocationView(id);
    }

    public AssetAllocationView getStrategyAfterRetirement() {
        Integer id = new InvestmentStrategyCode()
                .getCodeID((String) (jComboBoxInvestmentStrategyAfter
                        .getSelectedItem()));
        if (id == null)
            return null;

        return new AssetAllocationView(id);
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
            snapshotCalc.disableUpdate();
            snapshotCalc.clear();
            updateTitle();
        } finally {
            updateEditable();
            snapshotCalc.enableUpdate();
            snapshotCalc.doUpdate();
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
    private javax.swing.JPanel jPanelPartner;

    private javax.swing.JRadioButton jRadioButtonClientFemale;

    private javax.swing.JTextField jTextFieldRebate;

    private javax.swing.JButton jButtonReport;

    private javax.swing.ButtonGroup buttonGroupClientSex;

    private javax.swing.JTextField jTextFieldPartnerAge;

    private javax.swing.JCheckBox jCheckBoxApplySurcharge;

    private javax.swing.ButtonGroup buttonGroupPartnerSex;

    private javax.swing.JButton jButtonNext;

    private javax.swing.JPanel jPanelInvStrategy;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabelLifeExpectancy;

    private javax.swing.JLabel jLabelYearsRequiredIncome1;

    private javax.swing.JPanel jPanelClient;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JTextField jTextFieldRequiredIncomePartial;

    private javax.swing.JTextField jTextFieldUPPdss;

    private javax.swing.JLabel jLabelClientDOB;

    private javax.swing.JLabel jLabelISBefore;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JTextField jTextFieldUPPato;

    private javax.swing.JPanel jPanelAssetsTotal;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JTextField jTextFieldClientTargetAge;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JPanel jPanelReportCloseSave;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JPanel jPanelDSS;

    private javax.swing.JTextField jTextFieldInflation;

    private javax.swing.JRadioButton jRadioButtonPartnerMale;

    private javax.swing.JPanel jPanelFeesTax;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabelYearsRequiredIncome;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField jTextFieldReviewFees;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanelAssumptions;

    private javax.swing.JPanel jPanelTotal;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.ButtonGroup buttonGroupGoal;

    private javax.swing.JPanel jPanelAge;

    private javax.swing.JComboBox jComboBoxInvestmentStrategyAfter;

    private javax.swing.JRadioButton jRadioButtonClientMale;

    private javax.swing.JPanel jPanelPrevNext;

    private javax.swing.ButtonGroup buttonGroupSingleJoint;

    private javax.swing.JTextField jTextFieldRequiredIncomeFull;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JRadioButton jRadioButtonSingle;

    private au.com.totemsoft.bean.FDateChooser jTextFieldClientDOB;

    private javax.swing.JLabel jLabelRequiredIncomePartial;

    private javax.swing.JComboBox jComboBoxInvestmentStrategyBefore;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabelPartnerDOB;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JTextField jTextFieldClientTargetDate;

    private javax.swing.JPanel jPanelGoal;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JLabel jLabelClientAge;

    private javax.swing.JPanel jPanelClientGoal;

    private javax.swing.JLabel jLabelRequiredIncomeFull;

    private javax.swing.JPanel jPanelSingle;

    private javax.swing.JRadioButton jRadioButtonRetirement;

    private javax.swing.JLabel jLabelPartnerTaxRate;

    private javax.swing.JTextField jTextFieldPartnerTaxRate;

    private javax.swing.JCheckBox jCheckBoxIncludeDSS;

    private javax.swing.JTextField jTextFieldPartnerTargetAge;

    private javax.swing.JCheckBox jCheckBoxHomeOwner;

    private javax.swing.JRadioButton jRadioButtonJoint;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JTextField jTextFieldPartnerTargetDate;

    private javax.swing.JLabel jLabelClientTaxRate;

    private javax.swing.JTextField jTextFieldTotalAssets;

    private javax.swing.JTextField jTextFieldResidualRequired;

    private au.com.totemsoft.bean.FDateChooser jTextFieldPartnerDOB;

    private javax.swing.JTextField jTextFieldClientTaxRate;

    private javax.swing.JPanel jPanelAssets;

    private javax.swing.JLabel jLabelISAfter;

    private javax.swing.JPanel jPanelDetails;

    private javax.swing.JCheckBox jCheckBoxIndexContributions;

    private javax.swing.JPanel jPanelAssumptions2;

    private javax.swing.JTextField jTextFieldEntryFees;

    private javax.swing.JPanel jPanelAssumptions1;

    private javax.swing.JRadioButton jRadioButtonFinancialIndependence;

    private javax.swing.JTextField jTextFieldYearsRequiredIncome;

    private javax.swing.JRadioButton jRadioButtonPartnerFemale;

    private javax.swing.JTextField jTextFieldClientAge;

    private javax.swing.JPanel jPanelClientDetails;

    private javax.swing.JLabel jLabelPartnerAge;

    // End of variables declaration//GEN-END:variables

    public void hideControls() {
        jPanelControls.setVisible(false);
    }

    public void showControls() {
        jPanelControls.setVisible(true);
    }

    /**
     * 
     */
    public void updateView(String modelTitle) throws Exception {

        PersonService person = clientService;
        Model m = person == null ? null : person.getModel(getDefaultType(), modelTitle);

        updateView(m);

        updateTitle();

    }

    public void updateView(Model m) throws Exception {

        // saveView();

        // doClear(null);

        if (m == null) {
            updateView(clientService);
        } else {
            // use copy of model
            Integer id = m.getId();
            m = new Model(m);
            m.setId(id);

            try {
                snapshotCalc.disableUpdate();

                snapshotCalc.setModel(m);
                snapshotCalc.setSaved();
            } finally {
                updateEditable();
                snapshotCalc.enableUpdate();
                snapshotCalc.doUpdate();

                graphView.setGraphData(snapshotCalc.getGraphData());
            }
        }

    }

    private void saveView(boolean newModel) {

        int result = -1;
        String oldTitle = getTitle();

        try {

            if (!newModel)
                result = SaveProjectionDialog.getSaveProjectionInstance().save(
                        snapshotCalc, this);
            else
                result = SaveProjectionDialog.getSaveProjectionInstance()
                        .saveAs(snapshotCalc, this);

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
            JOptionPane.showMessageDialog(this, msg, "Invalid title",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            saveView(clientService);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    // public void saveView() throws ServiceException,
    // InvalidCodeException {
    // saveView( RmiParams.getInstance().getClientPerson() );
    // }

    /**
     * 
     */
    public void updateView(PersonService person) throws ServiceException {

        snapshotCalc.disableUpdate();
        try {
            snapshotCalc.clear();
            Model m = new Model();
            m.setTypeID(ModelType.CURRENT_POSITION_CALC);
            snapshotCalc.setModel(m);
        } finally {
            snapshotCalc.enableUpdate();
            snapshotCalc.doUpdate();
            updateEditable();
            if (person != null) {
                IPerson personName = person.getPersonName();
                snapshotCalc.setDateOfBirth(personName.getDateOfBirth());
                snapshotCalc.setSexCodeID(personName.getSex() == null ? null : personName.getSex().getId());
                // FinancialGoal
                FinancialGoal fg = person.getFinancialGoal();
                if (fg != null) {
                    Integer n = fg.getTargetAge();
                    snapshotCalc.setClientTargetAge(n == null ? (int) MoneyCalc.UNKNOWN_VALUE : n.intValue());

                    java.math.BigDecimal amount = fg.getTargetIncome();
                    snapshotCalc.setSpendValue(amount == null ? 0. : amount.doubleValue());

                    n = fg.getYearsIncomeReq();
                    snapshotCalc.setYearsSpend(n == null ? 0. : n.doubleValue());

                    amount = fg.getResidualReq();
                    snapshotCalc.setResidualValue(amount == null ? 0. : amount.doubleValue());
                } else {
                    snapshotCalc.setClientTargetAge((int) MoneyCalc.UNKNOWN_VALUE);
                    snapshotCalc.setSpendValue(0.);
                    snapshotCalc.setYearsSpend(0.);
                    snapshotCalc.setResidualValue(0.);
                }

                if (IMaritalCode.isSingle(personName.getMarital().getId())) {
                    snapshotCalc.setSingle(true);
                } else {
                    // client
                    ClientService client = (ClientService) person;

                    // partner
                    PersonService partner = client.getPartner(false);
                    snapshotCalc.setSingle(partner == null);
                    if (partner != null) {
                        IPerson partnerName = partner.getPersonName();
                        snapshotCalc.setPartnerDateOfBirth(partnerName.getDateOfBirth());
                        snapshotCalc.setPartnerSexCodeID(partnerName.getSex() == null ? null : partnerName.getSex().getId());
                    }
                    // FinancialGoal
                    fg = partner == null ? null : partner.getFinancialGoal();
                    if (fg != null) {
                        Integer n = fg.getTargetAge();
                        snapshotCalc.setPartnerTargetAge(n == null ? (int) MoneyCalc.UNKNOWN_VALUE : n.intValue());

                        // amount = fg.getTargetIncome();
                        // snapshotCalc.setSpendValue( amount == null ? 0. :
                        // amount.doubleValue() );

                        // n = fg.getYearsIncomeReq();
                        // snapshotCalc.setYearsSpend( n == null ?
                        // MoneyCalc.UNKNOWN_VALUE : n.doubleValue() );

                        // amount = fg.getResidualReq();
                        // snapshotCalc.setResidualValue( amount == null ? 0. :
                        // amount.doubleValue() );
                    } else {
                        snapshotCalc.setPartnerTargetAge((int) MoneyCalc.UNKNOWN_VALUE);
                    }
                }
                updateFinancials(person.findFinancials());
            }
        }
        // updateFinancials( person.getFinancials() );
    }

    public void updateFinancials(java.util.Map financials) {

        try {

            java.util.Map map = financials == null ? null
                    : (java.util.Map) financials.get(ASSET_CASH);
            java.util.Collection values = map == null ? null : map.values();
            java.math.BigDecimal amount = Financial.getTotalAmount(values);
            AssetGrowthLinked agl = snapshotCalc.getAsset(ASSET_CASH);
            agl.setInitialValue(amount == null ? 0 : amount.doubleValue());
            agl.setOwnerCodeID(OwnerCode.CLIENT);

            map = financials == null ? null : (java.util.Map) financials
                    .get(ASSET_SUPERANNUATION);
            values = map == null ? null : map.values();
            amount = Financial.getTotalAmount(values);
            agl = snapshotCalc.getAsset(ASSET_SUPERANNUATION);
            agl.setInitialValue(amount == null ? 0 : amount.doubleValue());
            agl.setOwnerCodeID(OwnerCode.CLIENT);

            map = financials == null ? null : (java.util.Map) financials
                    .get(ASSET_INVESTMENT);
            values = map == null ? null : map.values();
            amount = Financial.getTotalAmount(values);
            agl = snapshotCalc.getAsset(ASSET_INVESTMENT);
            agl.setInitialValue(amount == null ? 0 : amount.doubleValue());
            agl.setOwnerCodeID(OwnerCode.CLIENT);

        } finally {
            updateEditable();
            snapshotCalc.enableUpdate();
            snapshotCalc.doUpdate();
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
        snapshotCalc.setSaved();

    }

    /***************************************************************************
     * 
     */
    private void updateEditable() {

        String s = null;
        int n;
        double d;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        updateOthers();

        // client
        jTextFieldClientDOB.setText(DateTimeUtils.asString(snapshotCalc
                .getDateOfBirth()));

        n = snapshotCalc.getClientTargetAge();
        jTextFieldClientTargetAge.setText(n < 0 ? null : "" + n);

        // partner
        jTextFieldPartnerDOB.setText(DateTimeUtils.asString(snapshotCalc
                .getPartnerDateOfBirth()));

        n = snapshotCalc.getPartnerTargetAge();
        jTextFieldPartnerTargetAge.setText(n < 0 ? null : "" + n);

        n = snapshotCalc.getYearsIntSpend();
        jTextFieldYearsRequiredIncome.setText(n <= 0 ? null : "" + n);

        d = snapshotCalc.getIndexRate();
        jTextFieldInflation.setText(d < 0. ? null : percent.toString(d));

        d = snapshotCalc.getEntryFeeRate();
        jTextFieldEntryFees.setText(d < 0. ? null : percent.toString(d));

        d = snapshotCalc.getRevisionFeeRate();
        jTextFieldReviewFees.setText(d < 0. ? null : percent.toString(d));

        d = snapshotCalc.getTaxRate();
        jTextFieldClientTaxRate.setText(d < 0. ? null : percent.toString(d));

        d = snapshotCalc.getPartnerTaxRate();
        jTextFieldPartnerTaxRate.setText(d < 0. ? null : percent.toString(d));

        d = snapshotCalc.getPartSpendValue();
        jTextFieldRequiredIncomePartial.setText(d < 0. ? null : curr
                .toString(d));

        d = snapshotCalc.getSpendValue();
        jTextFieldRequiredIncomeFull.setText(d < 0. ? null : curr.toString(d));

        d = snapshotCalc.getUndeductedPurchasePriceATO();
        jTextFieldUPPato.setText(d < 0. ? null : curr.toString(d));

        d = snapshotCalc.getUndeductedPurchasePriceDSS();
        jTextFieldUPPdss.setText(d < 0. ? null : curr.toString(d));

        d = snapshotCalc.getRebateRate();
        jTextFieldRebate.setText(d < 0. ? null : percent.toString(d));

        d = snapshotCalc.getResidualValue();
        jTextFieldResidualRequired.setText(d < 0. ? null : curr.toString(d));

        // update asset views as well
        java.util.Iterator iter = assetViews.values().iterator();
        while (iter.hasNext()) {
            AssetView av = (AssetView) iter.next();
            av.updateEditable();
            // av.updateNonEditable();
        }

    }

    private void updateOthers() {

        setSingleView(snapshotCalc.getSingle());
        setRetirementView(snapshotCalc.getRetirement());
        updateComponents();

        if (SexCode.MALE.equals(snapshotCalc.getSexCodeID()))
            jRadioButtonClientMale.setSelected(true);
        else
            jRadioButtonClientFemale.setSelected(true);

        if (SexCode.MALE.equals(snapshotCalc.getPartnerSexCodeID()))
            jRadioButtonPartnerMale.setSelected(true);
        else
            jRadioButtonPartnerFemale.setSelected(true);

        jCheckBoxIndexContributions.setSelected(snapshotCalc.isIndexed());
        SwingUtil.setEnabled(jTextFieldInflation, snapshotCalc.isIndexed());

        jCheckBoxApplySurcharge.setSelected(snapshotCalc.getSurcharge());

        String s = snapshotCalc.getInvestmentStrategyCodeDesc();
        jComboBoxInvestmentStrategyBefore.setSelectedItem(s == null
                || s.length() == 0 ? null : s);

        s = snapshotCalc.getInvestmentStrategyCodeSpendDesc();
        jComboBoxInvestmentStrategyAfter.setSelectedItem(s == null
                || s.length() == 0 ? null : s);

        jCheckBoxIncludeDSS.setSelected(snapshotCalc.getIncludeDSS());

        jCheckBoxHomeOwner.setSelected(snapshotCalc.getHomeOwner());
        jCheckBoxHomeOwner.setEnabled(jCheckBoxIncludeDSS.isSelected());

    }

    private void updateNonEditable() {

        String s = null;
        int n;
        double d;
        Currency curr = Currency.getCurrencyInstance();
        Percent percent = Percent.getPercentInstance();

        updateOthers();

        // client
        n = snapshotCalc.getClientAge();
        jTextFieldClientAge.setText(n < 0 ? null : "" + n);

        s = DateTimeUtils.asString(snapshotCalc.getClientTargetDate());
        jTextFieldClientTargetDate.setText(s);

        // partner
        n = snapshotCalc.getPartnerAge();
        jTextFieldPartnerAge.setText(n < 0 ? null : "" + n);

        s = DateTimeUtils.asString(snapshotCalc.getPartnerTargetDate());
        jTextFieldPartnerTargetDate.setText(s);

        double le = snapshotCalc.getLifeExpectancyAfterRetire();
        s = le < 0 ? null : new java.text.MessageFormat(
                "Life Expectancy {0} year(s)")
                .format(new Object[] { new Double(le) });
        jLabelLifeExpectancy.setText(s);

        s = curr.toString(snapshotCalc.getTotalTargetValue());
        jTextFieldTotalAssets.setText(s);

    }

    // public void run() {
    // updateEditable();
    // }

    /**
     * helper method
     */
    private void updateComponents() {

        boolean ready = (!isRetirementView() && snapshotCalc.isReady())
                || (isRetirementView() && snapshotCalc.isReady() && snapshotCalc
                        .isReadySpend());

        if (jTabbedPane.getSelectedIndex() == 0)
            jButtonNext.setEnabled(ready);

        if (ready) {
            if (jTabbedPane.getTabCount() == RESULT_PAGE_ID)
                jTabbedPane.addTab("Results", graphView);
        } else {
            if (jTabbedPane.getTabCount() > RESULT_PAGE_ID)
                jTabbedPane.removeTabAt(RESULT_PAGE_ID);
        }

        // enable/disable save option
        jButtonSave
                .setEnabled(clientService != null);
        jButtonSaveAs
                .setEnabled(clientService != null);
        jButtonReport.setEnabled(ready);

    }

    // private boolean isSingleView() { return jTextFieldPartnerDOB.isEnabled();
    // }
    private void setSingleView(boolean value) {
        // if ( isSingleView() == value ) return;

        if (value)
            jRadioButtonSingle.setSelected(true);
        else
            jRadioButtonJoint.setSelected(true);

        if (value) {
            // single, clear partner fields
            // BEGIN: BUG FIX 614 - 11/07/2002
            // by shibaevv
            // Don't delete partner values.
            /*
             * jTextFieldPartnerDOB.setText(null);
             * jTextFieldPartnerAge.setText(null);
             * jTextFieldPartnerTargetAge.setText(null);
             * jTextFieldPartnerTargetDate.setText(null);
             * jTextFieldPartnerTaxRate.setText(null);
             * jRadioButtonPartnerMale.setSelected(false);
             * jRadioButtonPartnerFemale.setSelected(false);
             */
            // END: BUG FIX 614 - 11/07/2002
        }

        // enable/disable partner fields
        SwingUtil.setEnabled(jTextFieldPartnerDOB, !value);
        SwingUtil.setEnabled(jTextFieldPartnerTargetAge, !value);
        SwingUtil.setEnabled(jRadioButtonPartnerMale, !value);
        SwingUtil.setEnabled(jRadioButtonPartnerFemale, !value);
        SwingUtil.setEnabled(jTextFieldPartnerTaxRate, !value);

    }

    public void setRetirementView() {
        setRetirementView(true);
    }

    public void setWealthAccumulationProjectionView() {
        setRetirementView(false);
    }

    private boolean isRetirementView() {
        return jTextFieldYearsRequiredIncome.isVisible();
    }

    private void setRetirementView(boolean value) {
        if (isRetirementView() == value)
            return;

        if (value)
            jRadioButtonRetirement.setSelected(true);
        else
            jRadioButtonFinancialIndependence.setSelected(true);

        jTextFieldYearsRequiredIncome.setVisible(value);
        jLabelYearsRequiredIncome.setVisible(value);
        jPanelAssumptions.setVisible(value);

        graphView.setRetirementView(value);

        jLabelLifeExpectancy.setVisible(value);

        if (value) {

        } else {
            if (snapshotCalc != null && snapshotCalc.isReady()
                    && dataAggregator != null
                    && dataAggregator[CurrentPositionCalc.PRE_RETIREMENT] != null) {
                // save old data
                double[] preRetirement = new double[snapshotCalc.getYearsInt()];
                System.arraycopy(dataAggregator[CurrentPositionCalc.PRE_RETIREMENT],
                        0, preRetirement, 0, preRetirement.length);

                dataAggregator = new double[1][];
                dataAggregator[CurrentPositionCalc.PRE_RETIREMENT] = preRetirement;
            }

            // to disable retirement calcs
            snapshotCalc.setYearsSpend((int) MoneyCalc.UNKNOWN_VALUE);
            jTextFieldYearsRequiredIncome.setText(null);

        }

        // snapshotCalc.getAssetSpend().setModified();

        // remove post-retirement/dss series
        updateChart();

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getCurrentPositionReport();
    }

    public ReportFields getReportData(PersonService person) throws Exception {

        if (!snapshotCalc.isReady())
            return null;

        ReportFields reportFields = ReportFields.getInstance();
        snapshotCalc.initializeReportData(reportFields);

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
                } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
                    e.printStackTrace();
                    return;
                }

                // update menu
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

        ActionMap actionMap = getActionMap();
        if (actionMap == null)
            return;

        Action action = (Action) actionMap.get(actionID);
        if (action == null)
            return;

        action.actionPerformed(
            new ActionEvent(this, ++eventID, getClass().getName()));

    }

}
