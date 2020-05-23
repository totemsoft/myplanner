/*
 * CapitalGainsTax.java
 *
 * Created on 19 December 2001, 18:28
 */

package au.com.totemsoft.myplanner.swing.projection;

/**
 * 
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import au.com.totemsoft.bean.BasePanel;
import au.com.totemsoft.bean.MessageSentEvent;
import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.code.ModelType;
import au.com.totemsoft.myplanner.config.ViewSettings;
import au.com.totemsoft.myplanner.config.WordSettings;
import au.com.totemsoft.myplanner.etc.ActionEventID;
import au.com.totemsoft.myplanner.etc.DuplicateException;
import au.com.totemsoft.myplanner.etc.ModelTitleRestrictionException;
import au.com.totemsoft.myplanner.projection.GeneralTaxCalculatorNew;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.report.ReportFields;
import au.com.totemsoft.myplanner.service.ClientService;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.swing.BaseView;
import au.com.totemsoft.myplanner.swing.FinancialPlannerApp;
import au.com.totemsoft.myplanner.swing.IMenuCommand;
import au.com.totemsoft.myplanner.tax.au.ITaxConstants;
import au.com.totemsoft.swing.SwingUtil;

public class PAYGView
    extends BasePanel
    implements ActionEventID, au.com.totemsoft.swing.ICloseDialog
{

    private static PAYGView view;

    private String modelTitle = "PAYG Calculator";

    private String defaultTitle = "PAYG Calculator";

    private boolean newModel;

    private static ClientService clientService;
    public static void setClientService(ClientService clientService) {
        PAYGView.clientService = clientService;
    }

    public Integer getDefaultType() {
        return ModelType.PAYG_CALC;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    private GeneralTaxCalculatorNew dm;

    private Model model;

    public Model getModel() {
        return model;
    }

    /** Creates new form CapitalGainsTax */
    public PAYGView() {

        /**
         * Create data model an add this form as data change listener
         */
        dm = new GeneralTaxCalculatorNew();

        addFormDataModel(dm);

        dm.addModelDataChangedListener(this);

        dm.addMessageSentListener(this);

        initComponents();

        /** Register form fields with underlying data model */

        fTextField1.addFormDataChangedListener(this);
        fTextField2.addFormDataChangedListener(this);
        fTextField3.addFormDataChangedListener(this);
        fTextField4.addFormDataChangedListener(this);
        fTextField5.addFormDataChangedListener(this);
        fTextField6.addFormDataChangedListener(this);
        fTextField7.addFormDataChangedListener(this);
        fTextField9.addFormDataChangedListener(this);
        fTextField10.addFormDataChangedListener(this);
        fTextField13.addFormDataChangedListener(this);
        fTextField14.addFormDataChangedListener(this);
        fTextField15.addFormDataChangedListener(this);
        fTextField16.addFormDataChangedListener(this);
        fTextField17.addFormDataChangedListener(this);
        fTextField18.addFormDataChangedListener(this);
        fTextField19.addFormDataChangedListener(this);
        fTextField20.addFormDataChangedListener(this);
        fTextField21.addFormDataChangedListener(this);
        fTextField22.addFormDataChangedListener(this);
        fTextField23.addFormDataChangedListener(this);
        fTextField24.addFormDataChangedListener(this);
        fTextField8.addFormDataChangedListener(this);
        fTextField26.addFormDataChangedListener(this);
        fTextField27.addFormDataChangedListener(this);
        fTextField28.addFormDataChangedListener(this);
        fTextField25.addFormDataChangedListener(this);
        fTextField29.addFormDataChangedListener(this);
        fTextField12.addFormDataChangedListener(this);
        fTextField30.addFormDataChangedListener(this);
        fTextField31.addFormDataChangedListener(this);
        fTextField32.addFormDataChangedListener(this);
        fTextField33.addFormDataChangedListener(this);
        fTextField34.addFormDataChangedListener(this);
        fTextField35.addFormDataChangedListener(this);
        fTextField36.addFormDataChangedListener(this);
        fTextField37.addFormDataChangedListener(this);
        fTextField38.addFormDataChangedListener(this);
        fComboBox1.addFormDataChangedListener(this);
        fComboBox2.addFormDataChangedListener(this);

        //

        fTextField1.registerComponent(dm.T_SALARY, this);
        fTextField2.registerComponent(dm.T_LUMP_SUM_PART_A, this);
        fTextField3.registerComponent(dm.T_LUMP_SUM_PART_B, this);
        fTextField4.registerComponent(dm.T_ETP, this);
        fTextField5.registerComponent(dm.T_PENSIONS_ALLOWANCE, this);
        fTextField6.registerComponent(dm.T_OTHER_PENSIONS, this);
        fTextField7.registerComponent(dm.T_GROSS_INTEREST, this);
        fTextField9.registerComponent(dm.T_DIVIDENDS, this);
        fTextField10.registerComponent(dm.T_TOTAL_TAX_WITHHELD, this);
        fTextField13.registerComponent(dm.I_SALARY, this);
        fTextField14.registerComponent(dm.I_LUMP_SUM_PART_A, this);
        fTextField15.registerComponent(dm.I_LUMP_SUM_PART_B, this);
        fTextField38.registerComponent(dm.I_LUMP_SUM_PART_C, this);
        fTextField16.registerComponent(dm.I_ETP_NON_EXCESSIVE, this);
        fTextField17.registerComponent(dm.I_ETP_EXCESSIVE, this);
        fTextField18.registerComponent(dm.I_PENSIONS_ALLOWANCE, this);
        fTextField19.registerComponent(dm.I_OTHER_PENSIONS, this);
        fTextField20.registerComponent(dm.I_RFB, this);
        fTextField21.registerComponent(dm.I_GROSS_INTEREST, this);
        fTextField22.registerComponent(dm.I_DIVIDENDS_FRANKED, this);
        fTextField23.registerComponent(dm.I_DIVIDENDS_UNFRANKED, this);
        fTextField24.registerComponent(dm.I_DIVIDENDS_IMPUTATION_CREDIT, this);
        fTextField8.registerComponent(dm.I_TOTAL_INCOME, this);
        fTextField26.registerComponent(dm.D_GENERAL, this);
        fTextField27.registerComponent(dm.D_INTEREST_DIVIDEND, this);
        fTextField28.registerComponent(dm.D_UNDEDUCTED_PIRCHASE_PRICE, this);
        fTextField25.registerComponent(dm.O_TAX_WITHHELD, this);
        fTextField29.registerComponent(dm.O_IMPUTATION_CREDIT, this);
        fTextField12.registerComponent(dm.O_LUMP_SUM_ETP, this);
        fTextField30.registerComponent(dm.TAXABLE_INCOME, this);
        fTextField31.registerComponent(dm.TAX_ON_TAXABLE_INCOME, this);
        fTextField32.registerComponent(dm.ML, this);
        fTextField33.registerComponent(dm.MLS, this);
        fTextField34.registerComponent(dm.P_DEPENDANTS, this);
        fTextField35.registerComponent(dm.P_SPOUSE_INCOME, this);
        fTextField36.registerComponent(dm.P_AGE, this);
        fTextField11.registerComponent(dm.TAX_PAYABLE, this);

        fComboBox1.registerComponent(dm.P_HOSPITAL_COVER, this);
        fComboBox2.registerComponent(dm.P_MARITAL_STATUS, this);

        fTextField13.requestFocus();

        fTextField13.setNextFocusableComponent(fTextField1);
        fTextField3.setNextFocusableComponent(fTextField16);
        fTextField17.setNextFocusableComponent(fTextField4);
        fTextField4.setNextFocusableComponent(fTextField18);
        fTextField18.setNextFocusableComponent(fTextField5);
        fTextField5.setNextFocusableComponent(fTextField19);
        fTextField19.setNextFocusableComponent(fTextField6);
        fTextField6.setNextFocusableComponent(fTextField20);
        fTextField21.setNextFocusableComponent(fTextField7);
        fTextField7.setNextFocusableComponent(fTextField22);
        fTextField24.setNextFocusableComponent(fTextField9);
        fTextField9.setNextFocusableComponent(fComboBox1);
        fTextField28.setNextFocusableComponent(fTextField13);
        fTextField37.registerComponent(dm.O_LOW_INCOME, this);
        fTextField34.setColumns(2);
        fTextField36.setColumns(2);

        jLabel30.setVisible(false);
        fTextField9.setVisible(false);

        setActionMap();

    }

    public static boolean exists() {
        return view != null;
    }

    public static PAYGView getInstance() {
        if (view == null)
            view = new PAYGView();
        view.checkAvailabilty();
        return view;
    }

    private void checkAvailabilty() {
        jButtonSave
                .setEnabled(clientService != null);
        jButtonSaveAs
                .setEnabled(clientService != null);
        jButtonDelete.setEnabled(jButtonSave.isEnabled());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        bgCalculateTax = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        LeftPanel = new javax.swing.JPanel();
        Income = new javax.swing.JPanel();
        IncomeLabels = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        IncomeReceived = new javax.swing.JPanel();
        fTextField13 = new au.com.totemsoft.bean.FTextField();
        fTextField14 = new au.com.totemsoft.bean.FTextField();
        fTextField15 = new au.com.totemsoft.bean.FTextField();
        fTextField38 = new au.com.totemsoft.bean.FTextField();
        fTextField16 = new au.com.totemsoft.bean.FTextField();
        fTextField17 = new au.com.totemsoft.bean.FTextField();
        jLabel25 = new javax.swing.JLabel();
        fTextField18 = new au.com.totemsoft.bean.FTextField();
        fTextField19 = new au.com.totemsoft.bean.FTextField();
        fTextField20 = new au.com.totemsoft.bean.FTextField();
        fTextField21 = new au.com.totemsoft.bean.FTextField();
        fTextField22 = new au.com.totemsoft.bean.FTextField();
        fTextField23 = new au.com.totemsoft.bean.FTextField();
        fTextField24 = new au.com.totemsoft.bean.FTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        fTextField8 = new au.com.totemsoft.bean.FTextField();
        IncomeTaxWithHeld = new javax.swing.JPanel();
        fTextField1 = new au.com.totemsoft.bean.FTextField();
        fTextField2 = new au.com.totemsoft.bean.FTextField();
        fTextField3 = new au.com.totemsoft.bean.FTextField();
        jLabel46 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        fTextField4 = new au.com.totemsoft.bean.FTextField();
        fTextField5 = new au.com.totemsoft.bean.FTextField();
        fTextField6 = new au.com.totemsoft.bean.FTextField();
        jLabel22 = new javax.swing.JLabel();
        fTextField7 = new au.com.totemsoft.bean.FTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        fTextField9 = new au.com.totemsoft.bean.FTextField();
        jLabel34 = new javax.swing.JLabel();
        fTextField10 = new au.com.totemsoft.bean.FTextField();
        DummyLeft = new javax.swing.JPanel();
        RightPanel = new javax.swing.JPanel();
        Personal = new javax.swing.JPanel();
        PersonalLabels = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        PersonalValues = new javax.swing.JPanel();
        fComboBox1 = new au.com.totemsoft.bean.FComboBox();
        fComboBox2 = new au.com.totemsoft.bean.FComboBox();
        fTextField34 = new au.com.totemsoft.bean.FTextField();
        fTextField35 = new au.com.totemsoft.bean.FTextField();
        fTextField36 = new au.com.totemsoft.bean.FTextField();
        Deductions = new javax.swing.JPanel();
        DeductionsLabel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        DeductionsAmount = new javax.swing.JPanel();
        fTextField26 = new au.com.totemsoft.bean.FTextField();
        fTextField27 = new au.com.totemsoft.bean.FTextField();
        fTextField28 = new au.com.totemsoft.bean.FTextField();
        Rebates = new javax.swing.JPanel();
        RebatesLabel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        RebatesAmount = new javax.swing.JPanel();
        fTextField25 = new au.com.totemsoft.bean.FTextField();
        fTextField29 = new au.com.totemsoft.bean.FTextField();
        fTextField12 = new au.com.totemsoft.bean.FTextField();
        fTextField37 = new au.com.totemsoft.bean.FTextField();
        Tax = new javax.swing.JPanel();
        TaxLabel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        TaxAmount = new javax.swing.JPanel();
        fTextField30 = new au.com.totemsoft.bean.FTextField();
        fTextField31 = new au.com.totemsoft.bean.FTextField();
        fTextField32 = new au.com.totemsoft.bean.FTextField();
        fTextField33 = new au.com.totemsoft.bean.FTextField();
        jLabel42 = new javax.swing.JLabel();
        fTextField11 = new au.com.totemsoft.bean.FTextField();
        DummyRight = new javax.swing.JPanel();
        jPanelControls = new javax.swing.JPanel();
        jPanelCloseSave = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jClearAll = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(700, 500));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1,
                javax.swing.BoxLayout.X_AXIS));

        LeftPanel.setLayout(new javax.swing.BoxLayout(LeftPanel,
                javax.swing.BoxLayout.Y_AXIS));

        LeftPanel.setPreferredSize(new java.awt.Dimension(100, 890));
        LeftPanel.setMinimumSize(new java.awt.Dimension(10, 890));
        Income.setLayout(new javax.swing.BoxLayout(Income,
                javax.swing.BoxLayout.X_AXIS));

        Income.setBorder(new javax.swing.border.TitledBorder("Income"));
        Income.setPreferredSize(new java.awt.Dimension(500, 428));
        Income.setMinimumSize(new java.awt.Dimension(300, 428));
        Income.setMaximumSize(new java.awt.Dimension(500, 428));
        IncomeLabels.setLayout(new java.awt.GridLayout(17, 1, 3, 0));

        IncomeLabels.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1,
                        1)), " "));
        IncomeLabels.setPreferredSize(new java.awt.Dimension(200, 865));
        IncomeLabels.setMinimumSize(new java.awt.Dimension(180, 865));
        IncomeLabels.setMaximumSize(new java.awt.Dimension(400, 32767));
        jLabel1.setText("Salary or wages");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel1);

        jLabel2.setText("Leave Lump Sum (taxed 30% ) ");
        jLabel2
                .setToolTipText("Annual leave and long service leave max 30 % tax");
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel2);

        jLabel3.setText("Leave Lump Sum  5 % (taxed at MRT)");
        jLabel3
                .setToolTipText("Long service leave 5 % of pre 78 or annual leave post 93");
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel3);

        jLabel45.setText("Leave Lump Sum (taxed at MRT)");
        jLabel45
                .setToolTipText("Long service leave 5 % of pre 78 or annual leave post 93");
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel45);

        jLabel4.setText("ETP taxable amount (not excessive)");
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel4);

        jLabel5.setText("ETP excessive component");
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel5);

        jLabel24.setText("ETP Tax");
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel24);

        jLabel6.setText("Pensions & Allowances");
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel6);

        jLabel7.setText("Other Pensions");
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel7);

        jLabel8.setText("Total RFB");
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel8);

        jLabel9.setText("Gross Interest");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel9);

        jLabel11.setText("Dividends - Franked Amount");
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel11);

        jLabel10.setText("Dividends - Unfranked Amount");
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel10);

        jLabel12.setText("Dividends - Imputation Credit");
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel12);

        jLabel30.setText("Dividends - Tax");
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel30);

        jLabel31.setText(" ");
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel31);

        jLabel32.setText("Total");
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IncomeLabels.add(jLabel32);

        Income.add(IncomeLabels);

        IncomeReceived.setLayout(new java.awt.GridLayout(17, 0, 3, 3));

        IncomeReceived.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1,
                        1)), "Income", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BELOW_TOP));
        IncomeReceived.setPreferredSize(new java.awt.Dimension(90, 865));
        IncomeReceived.setMinimumSize(new java.awt.Dimension(10, 865));
        IncomeReceived.setMaximumSize(new java.awt.Dimension(90, 32767));
        fTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField13.setNextFocusableComponent(fTextField1
                .getNextFocusableComponent());
        IncomeReceived.add(fTextField13);

        fTextField14.setEditable(false);
        fTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField14.setDisabledTextColor(java.awt.Color.black);
        fTextField14.setEnabled(false);
        IncomeReceived.add(fTextField14);

        fTextField15.setEditable(false);
        fTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField15.setDisabledTextColor(java.awt.Color.black);
        fTextField15.setEnabled(false);
        IncomeReceived.add(fTextField15);

        fTextField38.setEditable(false);
        fTextField38.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField38.setDisabledTextColor(java.awt.Color.black);
        fTextField38.setEnabled(false);
        IncomeReceived.add(fTextField38);

        fTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField16);

        fTextField17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField17);

        jLabel25.setText(" ");
        IncomeReceived.add(jLabel25);

        fTextField18.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField18);

        fTextField19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField19);

        fTextField20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField20);

        fTextField21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField21);

        fTextField22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField22);

        fTextField23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField23);

        fTextField24.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeReceived.add(fTextField24);

        jLabel33.setText(" ");
        IncomeReceived.add(jLabel33);

        jLabel35.setText(" ");
        IncomeReceived.add(jLabel35);

        fTextField8.setEditable(false);
        fTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField8.setDisabledTextColor(java.awt.Color.black);
        fTextField8.setEnabled(false);
        IncomeReceived.add(fTextField8);

        Income.add(IncomeReceived);

        IncomeTaxWithHeld.setLayout(new java.awt.GridLayout(17, 0, 3, 3));

        IncomeTaxWithHeld.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1,
                        1)), "Tax Withheld/Paid",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BELOW_TOP));
        IncomeTaxWithHeld.setPreferredSize(new java.awt.Dimension(90, 865));
        IncomeTaxWithHeld.setMinimumSize(new java.awt.Dimension(10, 865));
        IncomeTaxWithHeld.setMaximumSize(new java.awt.Dimension(90, 32767));
        fTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField1.setNextFocusableComponent(fTextField13
                .getNextFocusableComponent());
        IncomeTaxWithHeld.add(fTextField1);

        fTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField2);

        fTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField3);

        jLabel46.setText(" ");
        IncomeTaxWithHeld.add(jLabel46);

        jButton1.setFont(new java.awt.Font("Dialog", 1, 10));
        jButton1.setText("ETP & Leave");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setDefaultCapable(false);
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setAutoscrolls(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonETPActionPerformed(evt);
            }
        });

        IncomeTaxWithHeld.add(jButton1);

        jLabel23.setText(" ");
        IncomeTaxWithHeld.add(jLabel23);

        fTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField4);

        fTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField5);

        fTextField6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField6);

        jLabel22.setText(" ");
        IncomeTaxWithHeld.add(jLabel22);

        fTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField7);

        jLabel27.setText(" ");
        IncomeTaxWithHeld.add(jLabel27);

        jLabel28.setText(" ");
        IncomeTaxWithHeld.add(jLabel28);

        jLabel29.setText(" ");
        IncomeTaxWithHeld.add(jLabel29);

        fTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        IncomeTaxWithHeld.add(fTextField9);

        jLabel34.setText(" ");
        IncomeTaxWithHeld.add(jLabel34);

        fTextField10.setEditable(false);
        fTextField10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField10.setDisabledTextColor(java.awt.Color.black);
        fTextField10.setEnabled(false);
        IncomeTaxWithHeld.add(fTextField10);

        Income.add(IncomeTaxWithHeld);

        LeftPanel.add(Income);

        LeftPanel.add(DummyLeft);

        jPanel1.add(LeftPanel);

        RightPanel.setLayout(new javax.swing.BoxLayout(RightPanel,
                javax.swing.BoxLayout.Y_AXIS));

        RightPanel.setPreferredSize(new java.awt.Dimension(10, 334));
        RightPanel.setMinimumSize(new java.awt.Dimension(10, 334));
        Personal.setLayout(new javax.swing.BoxLayout(Personal,
                javax.swing.BoxLayout.X_AXIS));

        Personal.setBorder(new javax.swing.border.TitledBorder("Parameters"));
        Personal.setPreferredSize(new java.awt.Dimension(300, 125));
        Personal.setMinimumSize(new java.awt.Dimension(0, 105));
        Personal.setMaximumSize(new java.awt.Dimension(300, 134));
        PersonalLabels.setLayout(new java.awt.GridLayout(5, 0));

        PersonalLabels.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 10)));
        PersonalLabels.setPreferredSize(new java.awt.Dimension(200, 93));
        PersonalLabels.setMinimumSize(new java.awt.Dimension(180, 78));
        PersonalLabels.setMaximumSize(new java.awt.Dimension(200, 32767));
        jLabel36.setText("Hospital Cover Held");
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PersonalLabels.add(jLabel36);

        jLabel37.setText("Marital Status");
        jLabel37.setToolTipText("");
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PersonalLabels.add(jLabel37);

        jLabel38.setText("Number of Dependents");
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PersonalLabels.add(jLabel38);

        jLabel39.setText("Spouse's Income");
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PersonalLabels.add(jLabel39);

        jLabel44.setText("Age");
        jLabel44.setToolTipText("");
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PersonalLabels.add(jLabel44);

        Personal.add(PersonalLabels);

        PersonalValues.setLayout(new java.awt.GridLayout(5, 0));

        PersonalValues.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 1)));
        PersonalValues.setPreferredSize(new java.awt.Dimension(90, 80));
        PersonalValues.setMinimumSize(new java.awt.Dimension(10, 80));
        PersonalValues.setMaximumSize(new java.awt.Dimension(90, 32767));
        fComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
                "No", "Yes" }));
        PersonalValues.add(fComboBox1);

        fComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
                "Single", "Married", "De-facto", "Partnered" }));
        fComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fComboBox2ItemStateChanged(evt);
            }
        });

        PersonalValues.add(fComboBox2);

        fTextField34.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PersonalValues.add(fTextField34);

        fTextField35.setEditable(false);
        fTextField35.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField35.setEnabled(false);
        PersonalValues.add(fTextField35);

        fTextField36.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        PersonalValues.add(fTextField36);

        Personal.add(PersonalValues);

        RightPanel.add(Personal);

        Deductions.setLayout(new javax.swing.BoxLayout(Deductions,
                javax.swing.BoxLayout.X_AXIS));

        Deductions.setBorder(new javax.swing.border.TitledBorder("Deductions"));
        Deductions.setPreferredSize(new java.awt.Dimension(300, 86));
        Deductions.setMinimumSize(new java.awt.Dimension(100, 86));
        Deductions.setMaximumSize(new java.awt.Dimension(300, 86));
        DeductionsLabel.setLayout(new java.awt.GridLayout(3, 0));

        DeductionsLabel.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 10)));
        DeductionsLabel.setPreferredSize(new java.awt.Dimension(200, 218));
        DeductionsLabel.setMinimumSize(new java.awt.Dimension(180, 378));
        DeductionsLabel.setMaximumSize(new java.awt.Dimension(200, 218));
        jLabel13.setText("General deductions");
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DeductionsLabel.add(jLabel13);

        jLabel14.setText("Interest and dividend deductions");
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DeductionsLabel.add(jLabel14);

        jLabel15.setText("Deductible amount of UPP");
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DeductionsLabel.add(jLabel15);

        Deductions.add(DeductionsLabel);

        DeductionsAmount.setLayout(new java.awt.GridLayout(3, 0));

        DeductionsAmount.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 1)));
        DeductionsAmount.setPreferredSize(new java.awt.Dimension(90, 235));
        DeductionsAmount.setMinimumSize(new java.awt.Dimension(10, 235));
        DeductionsAmount.setMaximumSize(new java.awt.Dimension(90, 32767));
        fTextField26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        DeductionsAmount.add(fTextField26);

        fTextField27.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        DeductionsAmount.add(fTextField27);

        fTextField28.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        DeductionsAmount.add(fTextField28);

        Deductions.add(DeductionsAmount);

        RightPanel.add(Deductions);

        Rebates.setLayout(new javax.swing.BoxLayout(Rebates,
                javax.swing.BoxLayout.X_AXIS));

        Rebates.setBorder(new javax.swing.border.TitledBorder("Tax Offsets"));
        Rebates.setPreferredSize(new java.awt.Dimension(300, 105));
        Rebates.setMinimumSize(new java.awt.Dimension(100, 86));
        Rebates.setMaximumSize(new java.awt.Dimension(300, 110));
        RebatesLabel.setLayout(new java.awt.GridLayout(4, 0));

        RebatesLabel.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 10)));
        RebatesLabel.setPreferredSize(new java.awt.Dimension(200, 378));
        RebatesLabel.setMinimumSize(new java.awt.Dimension(180, 378));
        RebatesLabel.setMaximumSize(new java.awt.Dimension(200, 32767));
        jLabel16.setText("Tax Withheld");
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        RebatesLabel.add(jLabel16);

        jLabel17.setText("Imputation Credit");
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        RebatesLabel.add(jLabel17);

        jLabel43.setText("Lump Sum and ETP");
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        RebatesLabel.add(jLabel43);

        jLabel26.setText("Low Income Rebate");
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        RebatesLabel.add(jLabel26);

        Rebates.add(RebatesLabel);

        RebatesAmount.setLayout(new java.awt.GridLayout(4, 0));

        RebatesAmount.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 1)));
        RebatesAmount.setPreferredSize(new java.awt.Dimension(90, 235));
        RebatesAmount.setMinimumSize(new java.awt.Dimension(10, 235));
        RebatesAmount.setMaximumSize(new java.awt.Dimension(90, 32767));
        fTextField25.setEditable(false);
        fTextField25.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField25.setDisabledTextColor(java.awt.Color.black);
        fTextField25.setEnabled(false);
        RebatesAmount.add(fTextField25);

        fTextField29.setEditable(false);
        fTextField29.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField29.setDisabledTextColor(java.awt.Color.black);
        fTextField29.setEnabled(false);
        RebatesAmount.add(fTextField29);

        fTextField12.setEditable(false);
        fTextField12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField12.setDisabledTextColor(java.awt.Color.black);
        fTextField12.setEnabled(false);
        RebatesAmount.add(fTextField12);

        fTextField37.setEditable(false);
        fTextField37.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField37.setDisabledTextColor(java.awt.Color.black);
        fTextField37.setEnabled(false);
        RebatesAmount.add(fTextField37);

        Rebates.add(RebatesAmount);

        RightPanel.add(Rebates);

        Tax.setLayout(new javax.swing.BoxLayout(Tax,
                javax.swing.BoxLayout.X_AXIS));

        Tax.setBorder(new javax.swing.border.TitledBorder("Tax"));
        Tax.setPreferredSize(new java.awt.Dimension(300, 150));
        Tax.setMinimumSize(new java.awt.Dimension(100, 150));
        Tax.setMaximumSize(new java.awt.Dimension(300, 160));
        TaxLabel.setLayout(new java.awt.GridLayout(6, 0));

        TaxLabel.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 10)));
        TaxLabel.setPreferredSize(new java.awt.Dimension(200, 93));
        TaxLabel.setMinimumSize(new java.awt.Dimension(200, 447));
        TaxLabel.setMaximumSize(new java.awt.Dimension(200, 32767));
        jLabel21.setText("Taxable Income");
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxLabel.add(jLabel21);

        jLabel18.setText("Gross Tax On Taxable Income");
        jLabel18.setToolTipText("");
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxLabel.add(jLabel18);

        jLabel19.setText("Medicare Levy");
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxLabel.add(jLabel19);

        jLabel20.setText("Medicare Levy Surcharge");
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxLabel.add(jLabel20);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxLabel.add(jLabel40);

        jLabel41.setText("Tax Payable/Refund");
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxLabel.add(jLabel41);

        Tax.add(TaxLabel);

        TaxAmount.setLayout(new java.awt.GridLayout(6, 0));

        TaxAmount.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(1, 1, 1, 1)));
        TaxAmount.setPreferredSize(new java.awt.Dimension(90, 100));
        TaxAmount.setMinimumSize(new java.awt.Dimension(10, 100));
        TaxAmount.setMaximumSize(new java.awt.Dimension(90, 32767));
        fTextField30.setEditable(false);
        fTextField30.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField30.setDisabledTextColor(java.awt.Color.black);
        fTextField30.setEnabled(false);
        TaxAmount.add(fTextField30);

        fTextField31.setEditable(false);
        fTextField31.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField31.setDisabledTextColor(java.awt.Color.black);
        fTextField31.setEnabled(false);
        TaxAmount.add(fTextField31);

        fTextField32.setEditable(false);
        fTextField32.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField32.setDisabledTextColor(java.awt.Color.black);
        fTextField32.setEnabled(false);
        TaxAmount.add(fTextField32);

        fTextField33.setEditable(false);
        fTextField33.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField33.setDisabledTextColor(java.awt.Color.black);
        fTextField33.setEnabled(false);
        TaxAmount.add(fTextField33);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TaxAmount.add(jLabel42);

        fTextField11.setEditable(false);
        fTextField11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fTextField11.setDisabledTextColor(java.awt.Color.black);
        fTextField11.setEnabled(false);
        TaxAmount.add(fTextField11);

        Tax.add(TaxAmount);

        RightPanel.add(Tax);

        DummyRight.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 0, 0));

        RightPanel.add(DummyRight);

        jPanel1.add(RightPanel);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanelControls.setLayout(new javax.swing.BoxLayout(jPanelControls,
                javax.swing.BoxLayout.X_AXIS));

        jPanelCloseSave.setBorder(new javax.swing.border.EtchedBorder());
        jButtonReport.setText("Report");
        jButtonReport.setDefaultCapable(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setDefaultCapable(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSave);

        jButtonSaveAs.setText("Save As");
        jButtonSaveAs.setDefaultCapable(false);
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSaveAs);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonDelete);

        jClearAll.setText("Clear All");
        jClearAll.setDefaultCapable(false);
        jClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearAllActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jClearAll);

        jPanelControls.add(jPanelCloseSave);

        add(jPanelControls, java.awt.BorderLayout.SOUTH);

    }// GEN-END:initComponents

    private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveAsActionPerformed
        // Add your handling code here:
        newModel = true;
        doSave(evt);

    }// GEN-LAST:event_jButtonSaveAsActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        doDelete(evt);
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void fComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_fComboBox2ItemStateChanged
        // Add your handling code here:
        // Add your handling code here:

        if (evt.SELECTED != evt.getStateChange())
            return;

        if (fComboBox2.getSelectedIndex() == 0) {
            fTextField35.setEditable(false);
            fTextField35.setEnabled(false);
            fTextField35.setBackground(Color.lightGray);
            fTextField35.setText("");

        } else {
            fTextField35.setEditable(true);
            fTextField35.setEnabled(true);

            fTextField35.setBackground(Color.white);

        }

    }// GEN-LAST:event_fComboBox2ItemStateChanged

    private void jButtonClearAllActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearAllActionPerformed
        // Add your handling code here:
        dm.reset();

        dm.putValue(au.com.totemsoft.myplanner.tax.au.ITaxConstants.LSL_REDUNDANCY,
                "Resignation/Retirement");
        dm.putValue(dm.P_HOSPITAL_COVER, "Yes");
        dm.putValue(dm.P_MARITAL_STATUS, "Single");

        dm.sendNotification(this);
    }// GEN-LAST:event_jButtonClearAllActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    private void jButtonETPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonETPActionPerformed
        // Add your handling code here:
        JFrame parentFrame = null;
        Container parent;
        parent = this.getTopLevelAncestor();

        while (parent != null) {
            if (parent instanceof JFrame) {
                parentFrame = (JFrame) parent;
                break;
            }
            parent = parent.getParent();
        }

        JDialog etpView = SwingUtil.add2Dialog(parentFrame,
                "ETP & Lump Sum Components", true, new PAYGEtpViewNew(dm),
                false, true);

    }// GEN-LAST:event_jButtonETPActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int result = BaseView.closeDialog(this);
        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        // Add your handling code here:
        if (dm.isModified()) {
            newModel = false;
            doSave(evt);
        }
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void saveView(boolean newModel) {

        String oldTitle = getTitle();
        try {
            String newTitle = "";
            if (newModel || oldTitle == null || oldTitle.trim().length() <= 0) {
                SaveProjectionDialog dlg = SaveProjectionDialog
                        .getSaveProjectionInstance();

                dlg.setTitle("Save " + oldTitle + " as ..");
                dlg.setSaveTitle("");
                dlg.show();

                if (dlg.getResult() == au.com.totemsoft.myplanner.swing.InputDialog.CANCEL_OPTION)
                    return;
                newTitle = dlg.getSaveTitle();
                // validate new title
                if (newTitle == null || newTitle.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Empty title!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    return; // has to be new/not empty title
                }

                newTitle = newTitle.trim();
                if (newTitle.equalsIgnoreCase(getDefaultTitle())
                        || newTitle.equalsIgnoreCase(oldTitle)
                        || newTitle.equalsIgnoreCase(IMenuCommand.NEW.getSecond().toString())) {
                    JOptionPane.showMessageDialog(this, "Duplicated Title!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    return; // has to be new/not empty title

                }

                if (newTitle.length() < 3) {
                    String msg = "Failed to save model.\n  Please ensure total characters for model title is 3 or more.";
                    JOptionPane.showMessageDialog(this, msg, "Invalid title",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ModelCollection models = null;
                java.util.Vector models = null;
                try {
                    models = clientService
                            .getModels(ModelType.PAYG_CALC);
                } catch (au.com.totemsoft.myplanner.api.ServiceException e) {
                    e.printStackTrace(System.err);
                    return;
                }

                if (models != null) {
                    // java.util.Iterator iterModels = models.valuesIterator();
                    java.util.Iterator iterModels = models.iterator();
                    while (iterModels.hasNext()) {
                        // java.util.Vector _models = (java.util.Vector)
                        // iterModels.next();

                        // java.util.Iterator iter = _models.iterator();
                        // while ( iter.hasNext() ) {
                        Model model = (Model) iterModels.next();

                        if (model == null || model.getTitle() == null) // removed
                                                                        // or
                                                                        // same
                            continue;

                        String s = model.getTitle().trim();
                        if (newTitle.equalsIgnoreCase(s)) {
                            JOptionPane.showMessageDialog(this,
                                    "Title already used by another model",
                                    "Failed to save model.",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // }

                    }
                }

                model = new Model();
                model.setTypeID(getDefaultType());
                model.setDescription("");

                setTitle(newTitle);
                model.setTitle(getTitle());
                SwingUtil.setTitle(this, getTitle());
                
                //addModel
                FinancialPlannerApp.getInstance().updateModels();

            }
            model.setData(dm.getValuesAsObject());

        } catch (DuplicateException e) {
            String newTitle = getTitle();

            String msg = "Failed to save model.";
            JOptionPane.showMessageDialog(this, msg,
                    "Title was already used by another model",
                    JOptionPane.ERROR_MESSAGE);

            return;

        } catch (ModelTitleRestrictionException e) {
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

    public void saveView(PersonService person) throws ServiceException,
            InvalidCodeException {

        person.addModel(getModel());

        person.storeModels();
        // dm.setSaved();

    }

    public String getTitle() {
        return this.modelTitle;
    }

    public void setTitle(String title) {
        this.modelTitle = title;
    }

    public static PAYGView display(final Model model, FocusListener[] listeners) {

        boolean exists = PAYGView.exists();

        PAYGView view = PAYGView.getInstance();
        PersonService person = clientService;
        view.model = model;

        // Initialize parameters for new model

        view.dm.reset();
        view.dm.putValue(ITaxConstants.LSL_REDUNDANCY, "Resignation/Retirement");
        view.dm.putValue(ITaxConstants.P_HOSPITAL_COVER, "Yes");
        view.dm.putValue(ITaxConstants.P_MARITAL_STATUS, "Single");

        if (model != null)// {
            view.dm.setValuesFromObject(model.getData());
        // isNew = false ;
        // } else {
        // isNew = true ;
        // }
        view.dm.sendNotification(new Object());
        view.dm.setModified(false);

        if (!exists)
            SwingUtil.add2Frame(view, listeners,
                model == null ? view.getDefaultTitle() : model.getTitle(),
                ViewSettings.getInstance().getViewImage(
                view.getClass().getName()), true, true, false);

        String title = model == null ? view.getDefaultTitle() : model.getTitle();
        view.setTitle(title);
        SwingUtil.setTitle(view, title);
        SwingUtil.setVisible(view, true);

        return view;

    }

    public void messageSent(MessageSentEvent e) {

        String message = e.getMessage();
        String messageType = e.getMessageType();
        java.awt.Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(this, message, messageType,
                JOptionPane.CANCEL_OPTION);

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getPAYGReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws java.io.IOException {
        // if ( person == null ) return null;

        ReportFields reportFields = ReportFields.getInstance();
        dm.initializeReportData(reportFields);

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

    /***************************************************************************
     * BaseView protected methods
     **************************************************************************/
    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView(newModel);
            dm.setModified(false);
            // update menu
            FinancialPlannerApp.getInstance().updateModels();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void doDelete(java.awt.event.ActionEvent evt) {
        fireActionEvent(DATA_REMOVE);
    }

    protected void doClear(java.awt.event.ActionEvent evt) {
        /*
         * try { dm.disableUpdate(); dm.clear(); updateTitle(); } finally {
         * updateEditable(); dm.enableUpdate(); dm.doUpdate(); }
         */
    }

    public boolean isModified() {
        return (jButtonSave.isVisible() && jButtonSave.isEnabled() && dm
                .isModified())
                || (jButtonSaveAs.isVisible() && jButtonSaveAs.isEnabled() && dm
                        .isModified());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private au.com.totemsoft.bean.FTextField fTextField38;

    private au.com.totemsoft.bean.FTextField fTextField37;

    private au.com.totemsoft.bean.FTextField fTextField36;

    private javax.swing.JPanel PersonalValues;

    private au.com.totemsoft.bean.FTextField fTextField35;

    private javax.swing.JPanel Personal;

    private au.com.totemsoft.bean.FTextField fTextField34;

    private au.com.totemsoft.bean.FTextField fTextField33;

    private au.com.totemsoft.bean.FTextField fTextField32;

    private au.com.totemsoft.bean.FTextField fTextField31;

    private au.com.totemsoft.bean.FTextField fTextField30;

    private javax.swing.JLabel jLabel39;

    private javax.swing.JLabel jLabel38;

    private javax.swing.JLabel jLabel37;

    private javax.swing.JLabel jLabel36;

    private javax.swing.JLabel jLabel35;

    private javax.swing.JLabel jLabel34;

    private javax.swing.JLabel jLabel33;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JLabel jLabel32;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JLabel jLabel30;

    private au.com.totemsoft.bean.FTextField fTextField29;

    private au.com.totemsoft.bean.FTextField fTextField28;

    private au.com.totemsoft.bean.FTextField fTextField27;

    private au.com.totemsoft.bean.FTextField fTextField26;

    private au.com.totemsoft.bean.FTextField fTextField25;

    private au.com.totemsoft.bean.FTextField fTextField24;

    private au.com.totemsoft.bean.FTextField fTextField23;

    private au.com.totemsoft.bean.FTextField fTextField22;

    private au.com.totemsoft.bean.FTextField fTextField21;

    private au.com.totemsoft.bean.FTextField fTextField20;

    private javax.swing.JPanel Rebates;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JPanel IncomeReceived;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JPanel PersonalLabels;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JPanel jPanelCloseSave;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JPanel DeductionsLabel;

    private javax.swing.JPanel IncomeTaxWithHeld;

    private javax.swing.JPanel TaxLabel;

    private javax.swing.JPanel jPanelControls;

    private javax.swing.JPanel TaxAmount;

    private au.com.totemsoft.bean.FTextField fTextField19;

    private au.com.totemsoft.bean.FTextField fTextField18;

    private javax.swing.ButtonGroup bgCalculateTax;

    private au.com.totemsoft.bean.FTextField fTextField17;

    private au.com.totemsoft.bean.FTextField fTextField16;

    private au.com.totemsoft.bean.FTextField fTextField15;

    private au.com.totemsoft.bean.FTextField fTextField14;

    private au.com.totemsoft.bean.FTextField fTextField13;

    private au.com.totemsoft.bean.FTextField fTextField12;

    private au.com.totemsoft.bean.FTextField fTextField11;

    private au.com.totemsoft.bean.FTextField fTextField10;

    private javax.swing.JButton jClearAll;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel18;

    private au.com.totemsoft.bean.FTextField fTextField9;

    private javax.swing.JLabel jLabel17;

    private au.com.totemsoft.bean.FTextField fTextField8;

    private javax.swing.JLabel jLabel16;

    private au.com.totemsoft.bean.FTextField fTextField7;

    private javax.swing.JLabel jLabel15;

    private au.com.totemsoft.bean.FTextField fTextField6;

    private javax.swing.JLabel jLabel14;

    private au.com.totemsoft.bean.FTextField fTextField5;

    private javax.swing.JLabel jLabel13;

    private au.com.totemsoft.bean.FTextField fTextField4;

    private au.com.totemsoft.bean.FTextField fTextField3;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JPanel RebatesLabel;

    private au.com.totemsoft.bean.FTextField fTextField2;

    private javax.swing.JLabel jLabel11;

    private au.com.totemsoft.bean.FTextField fTextField1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton jButton1;

    private javax.swing.JPanel DummyLeft;

    private au.com.totemsoft.bean.FComboBox fComboBox2;

    private javax.swing.JPanel DeductionsAmount;

    private au.com.totemsoft.bean.FComboBox fComboBox1;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel Deductions;

    private javax.swing.JPanel RebatesAmount;

    private javax.swing.JPanel RightPanel;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JPanel Income;

    private javax.swing.JLabel jLabel46;

    private javax.swing.JPanel Tax;

    private javax.swing.JLabel jLabel45;

    private javax.swing.JLabel jLabel44;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JPanel IncomeLabels;

    private javax.swing.JLabel jLabel42;

    private javax.swing.JLabel jLabel41;

    private javax.swing.JLabel jLabel40;

    private javax.swing.JPanel DummyRight;

    private javax.swing.JPanel LeftPanel;

    // End of variables declaration//GEN-END:variables

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
