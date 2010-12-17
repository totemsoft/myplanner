/*
 * Mortgage.java
 *
 * Created on 19 December 2001, 16:56
 */

package com.argus.financials.ui.projection;

/**
 * 
 * @author kevinm
 */

import java.awt.Cursor;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.argus.beans.format.CurrencyLabelGenerator;
import com.argus.financials.code.InvalidCodeException;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.etc.DuplicateException;
import com.argus.financials.etc.ModelTitleRestrictionException;
import com.argus.financials.io.IOUtils2;
import com.argus.financials.projection.DocumentNames;
import com.argus.financials.projection.MoneyCalc;
import com.argus.financials.projection.data.MortgageScheduleTableModel;
import com.argus.financials.projection.save.Model;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceException;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.FinancialPlannerApp;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;

public class MortgageView extends javax.swing.JPanel implements ActionEventID,
        javax.swing.event.ChangeListener,
        com.argus.financials.projection.IMortgageCalcResults,
        com.argus.financials.projection.IMortgageCalcParams,
        com.argus.financials.swing.ICloseDialog {

    private static MortgageView view;

    private static final int MORTGAGE_CONDITIONs_TAB = 0;

    private static final int PROJECTION_TAB = 1;

    private static final int SCHEDULE_TAB = 2;

    private static final int FORWARD = 1;

    private static final int BACKWARD = -1;

    private com.argus.financials.GraphView graphView = new com.argus.financials.GraphView();

    private Number2 number = MoneyCalc.getNumberInstance();

    private Percent percent = MoneyCalc.getPercentInstance();

    private Currency curr = MoneyCalc.getCurrencyInstance();

    private boolean readyToSave = false;

    private boolean newModel = false;

    private MortgageCalc calculator;

    public Integer getDefaultType() {
        return ModelTypeID.rcLOAN_MORTGAGE_CALC.getCodeIDInteger();
    }

    public String getDefaultTitle() {
        return ModelTypeID.rcLOAN_MORTGAGE_CALC.getCodeDesc();
    }

    /** Creates new form Mortgage */
    public MortgageView() {
        this(new MortgageCalc());
    }

    public MortgageView(MortgageCalc calc) {
        calculator = calc;

        initComponents();
        initComponents2();

    }

    public static boolean exists() {
        return view != null;
    }

    public static MortgageView getInstance() {
        if (view == null)
            view = new MortgageView();
        return view;

    }

    // Instead of re-using the same vieww, always create a new view for all
    // clients
    public static MortgageView getNewInstance() {
        return new MortgageView();
    }

    private Model getModel() {
        Model model = calculator.getModel();
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
            JOptionPane.showMessageDialog(this,
                    "Title already used by another model.",
                    "Failed to set model title.", JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException("Title already used by another model.");
        } catch (ModelTitleRestrictionException me) {
            String msg = "Failed to set model title.\n  Please ensure total characters for model title is 3 or more!";
            JOptionPane.showMessageDialog(this, msg, "Invalid Title",
                    JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException(msg);

        }
        SwingUtil.setTitle(this, value);
    }

    /**
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        System.out.println("MortgageView::stateChanged:- no action taken.");
        // updateNonEditable();
        // updateComponents();
        // updateChart();
    }

    private void initComponents2() {
        graphView.setPreferredSize(new java.awt.Dimension(700, 500));
        jPanelGraph.add(graphView);

        calculator.setView(this);
        setAccessibleContext();
        enableResultsControls();

        jButtonSave
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonSaveAs
                .setEnabled(ServiceLocator.getInstance().getClientPerson() != null);
        jButtonDelete.setEnabled(jButtonSave.isEnabled());
        setActionMap();
    }

    private void setAccessibleContext() {
        purchasePrice.getAccessibleContext().setAccessibleName(
                DocumentNames.PURCHASE_PRICE);
        depositAmount.getAccessibleContext().setAccessibleName(
                DocumentNames.DEPOSIT);
        amountBorrow.getAccessibleContext().setAccessibleName(
                DocumentNames.AMOUNT_BORROWED);
        loanTerm.getAccessibleContext().setAccessibleName(
                DocumentNames.LOAN_TERM);
        interestRate.getAccessibleContext().setAccessibleName(
                DocumentNames.INTEREST_RATE);
        addYes.getAccessibleContext().setAccessibleName(
                DocumentNames.HAS_ADDITIONAL_PAYMENTS_YES);
        addNo.getAccessibleContext().setAccessibleName(
                DocumentNames.HAS_ADDITIONAL_PAYMENTS_NO);
        monthlyAddition.getAccessibleContext().setAccessibleName(
                DocumentNames.ADDITIONAL_PAYMENT_AMOUNT);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        paymentButtonGroup = new javax.swing.ButtonGroup();
        mortgageTabs = new javax.swing.JTabbedPane();
        mortgageConditionsJPanel = new javax.swing.JPanel();
        MortgageDetails = new javax.swing.JPanel();
        PurchasePriceLabel = new javax.swing.JLabel();
        DepositAmountLabel = new javax.swing.JLabel();
        AmountBorrowLabel = new javax.swing.JLabel();
        InterestRateLabel = new javax.swing.JLabel();
        purchasePrice = new javax.swing.JTextField();
        depositAmount = new javax.swing.JTextField();
        amountBorrow = new javax.swing.JTextField();
        loanTerm = new javax.swing.JTextField();
        LoanTermLabel = new javax.swing.JLabel();
        interestRate = new javax.swing.JTextField();
        AdditionalYesNo = new javax.swing.JPanel();
        addYes = new javax.swing.JRadioButton();
        addNo = new javax.swing.JRadioButton();
        AdditionalLabel = new javax.swing.JLabel();
        MthlyAdditionLabel = new javax.swing.JLabel();
        monthlyAddition = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        StandardRepayments = new javax.swing.JPanel();
        MonthlyPmtLabel = new javax.swing.JLabel();
        stdYearsRepay = new javax.swing.JTextField();
        InterestPaidLabel = new javax.swing.JLabel();
        TotalCapitalLabel = new javax.swing.JLabel();
        NumberPmtsLabel = new javax.swing.JLabel();
        YearsRepayLabel = new javax.swing.JLabel();
        TotalRepayedLabel = new javax.swing.JLabel();
        stdNumberPayments = new javax.swing.JTextField();
        stdMonthlyPayment = new javax.swing.JTextField();
        stdTotalRepaid = new javax.swing.JTextField();
        stdTotalIntPaid = new javax.swing.JTextField();
        stdTotalCapPaid = new javax.swing.JTextField();
        AdditionalRepayments = new javax.swing.JPanel();
        addMonthlyPayment = new javax.swing.JTextField();
        addTotalIntPaid = new javax.swing.JTextField();
        addTotalCapPaid = new javax.swing.JTextField();
        addTotalAmtPaid = new javax.swing.JTextField();
        addTotalYearsRepay = new javax.swing.JTextField();
        addNumberPmts = new javax.swing.JTextField();
        AddMnthPmt = new javax.swing.JLabel();
        AddTotIntPaid = new javax.swing.JLabel();
        AddTotCapPaid = new javax.swing.JLabel();
        AddTotAmtRepaid = new javax.swing.JLabel();
        AddYrsRepay = new javax.swing.JLabel();
        AddNumPmts = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        interestSaved = new javax.swing.JTextField();
        yearsReduced = new javax.swing.JTextField();
        jPanelGraph = new javax.swing.JPanel();
        mortgageScheduleJPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        stdScheduleTable = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        addScheduleTable = new javax.swing.JTable();
        controlsJPanel = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jPanelCloseSave = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(800, 500));
        mortgageTabs.setPreferredSize(new java.awt.Dimension(5, 1000));
        mortgageTabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mortgageTabsStateChanged(evt);
            }
        });

        mortgageConditionsJPanel.setLayout(new javax.swing.BoxLayout(
                mortgageConditionsJPanel, javax.swing.BoxLayout.X_AXIS));

        MortgageDetails.setLayout(new java.awt.GridBagLayout());

        MortgageDetails.setBorder(new javax.swing.border.TitledBorder(
                "Mortgage Details"));
        PurchasePriceLabel.setText("Purchase Price");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(PurchasePriceLabel, gridBagConstraints);

        DepositAmountLabel.setText("Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(DepositAmountLabel, gridBagConstraints);

        AmountBorrowLabel.setText("Amount Borrowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(AmountBorrowLabel, gridBagConstraints);

        InterestRateLabel.setText("Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(InterestRateLabel, gridBagConstraints);

        purchasePrice.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        purchasePrice.setPreferredSize(new java.awt.Dimension(100, 20));
        purchasePrice.setInputVerifier(new PurchasePriceInputVerifier(
                calculator));
        purchasePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFieldsActionPerformed(evt);
            }
        });

        purchasePrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editFieldsFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(purchasePrice, gridBagConstraints);

        depositAmount.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        depositAmount.setPreferredSize(new java.awt.Dimension(100, 20));
        depositAmount
                .setInputVerifier(com.argus.financials.swing.CurrencyInputVerifier
                        .getInstance());
        depositAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFieldsActionPerformed(evt);
            }
        });

        depositAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editFieldsFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(depositAmount, gridBagConstraints);

        amountBorrow.setEditable(false);
        amountBorrow.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        amountBorrow.setPreferredSize(new java.awt.Dimension(100, 20));
        amountBorrow
                .setInputVerifier(com.argus.financials.swing.CurrencyInputVerifier
                        .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(amountBorrow, gridBagConstraints);

        loanTerm.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        loanTerm.setPreferredSize(new java.awt.Dimension(50, 20));
        loanTerm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFieldsActionPerformed(evt);
            }
        });

        loanTerm.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editFieldsFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(loanTerm, gridBagConstraints);

        LoanTermLabel.setText("Loan Term");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(LoanTermLabel, gridBagConstraints);

        interestRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        interestRate.setPreferredSize(new java.awt.Dimension(50, 20));
        interestRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFieldsActionPerformed(evt);
            }
        });

        interestRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editFieldsFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(interestRate, gridBagConstraints);

        AdditionalYesNo.setLayout(new javax.swing.BoxLayout(AdditionalYesNo,
                javax.swing.BoxLayout.X_AXIS));

        AdditionalYesNo.setPreferredSize(new java.awt.Dimension(120, 20));
        addYes.setText("Yes");
        paymentButtonGroup.add(addYes);
        addYes.setPreferredSize(new java.awt.Dimension(130, 20));
        addYes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                addYesItemStateChanged(evt);
            }
        });

        AdditionalYesNo.add(addYes);

        addNo.setSelected(true);
        addNo.setText("No");
        paymentButtonGroup.add(addNo);
        addNo.setPreferredSize(new java.awt.Dimension(130, 20));
        AdditionalYesNo.add(addNo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(AdditionalYesNo, gridBagConstraints);

        AdditionalLabel.setText("Additional Payments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(AdditionalLabel, gridBagConstraints);

        MthlyAdditionLabel.setText("Monthly Addition");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(MthlyAdditionLabel, gridBagConstraints);

        monthlyAddition.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        monthlyAddition.setPreferredSize(new java.awt.Dimension(100, 20));
        monthlyAddition
                .setInputVerifier(com.argus.financials.swing.CurrencyInputVerifier
                        .getInstance());
        monthlyAddition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFieldsActionPerformed(evt);
            }
        });

        monthlyAddition.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                editFieldsFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        MortgageDetails.add(monthlyAddition, gridBagConstraints);

        jLabel4.setText("Year(s)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(jLabel4, gridBagConstraints);

        jLabel5.setText(" %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        MortgageDetails.add(jLabel5, gridBagConstraints);

        mortgageConditionsJPanel.add(MortgageDetails);

        StandardRepayments.setLayout(new java.awt.GridBagLayout());

        StandardRepayments.setBorder(new javax.swing.border.TitledBorder(
                "Standard Repayments"));
        MonthlyPmtLabel.setText("Monthly Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        StandardRepayments.add(MonthlyPmtLabel, gridBagConstraints);

        stdYearsRepay.setEditable(false);
        stdYearsRepay.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stdYearsRepay.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        StandardRepayments.add(stdYearsRepay, gridBagConstraints);

        InterestPaidLabel.setText("Total Interest Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        StandardRepayments.add(InterestPaidLabel, gridBagConstraints);

        TotalCapitalLabel.setText("Total Capital Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        StandardRepayments.add(TotalCapitalLabel, gridBagConstraints);

        NumberPmtsLabel.setText("Number of Payments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        StandardRepayments.add(NumberPmtsLabel, gridBagConstraints);

        YearsRepayLabel.setText("Years to Repay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        StandardRepayments.add(YearsRepayLabel, gridBagConstraints);

        TotalRepayedLabel.setText("Total Amount Repaid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        StandardRepayments.add(TotalRepayedLabel, gridBagConstraints);

        stdNumberPayments.setEditable(false);
        stdNumberPayments
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stdNumberPayments.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        StandardRepayments.add(stdNumberPayments, gridBagConstraints);

        stdMonthlyPayment.setEditable(false);
        stdMonthlyPayment
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stdMonthlyPayment.setPreferredSize(new java.awt.Dimension(100, 20));
        stdMonthlyPayment
                .setInputVerifier(com.argus.financials.swing.CurrencyInputVerifier
                        .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        StandardRepayments.add(stdMonthlyPayment, gridBagConstraints);

        stdTotalRepaid.setEditable(false);
        stdTotalRepaid.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stdTotalRepaid.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        StandardRepayments.add(stdTotalRepaid, gridBagConstraints);

        stdTotalIntPaid.setEditable(false);
        stdTotalIntPaid.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stdTotalIntPaid.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        StandardRepayments.add(stdTotalIntPaid, gridBagConstraints);

        stdTotalCapPaid.setEditable(false);
        stdTotalCapPaid.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        stdTotalCapPaid.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        StandardRepayments.add(stdTotalCapPaid, gridBagConstraints);

        mortgageConditionsJPanel.add(StandardRepayments);

        AdditionalRepayments.setLayout(new java.awt.GridBagLayout());

        AdditionalRepayments.setBorder(new javax.swing.border.TitledBorder(
                "Additional Repayments"));
        addMonthlyPayment.setEditable(false);
        addMonthlyPayment
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        addMonthlyPayment.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(addMonthlyPayment, gridBagConstraints);

        addTotalIntPaid.setEditable(false);
        addTotalIntPaid.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        addTotalIntPaid.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(addTotalIntPaid, gridBagConstraints);

        addTotalCapPaid.setEditable(false);
        addTotalCapPaid.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        addTotalCapPaid.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(addTotalCapPaid, gridBagConstraints);

        addTotalAmtPaid.setEditable(false);
        addTotalAmtPaid.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        addTotalAmtPaid.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(addTotalAmtPaid, gridBagConstraints);

        addTotalYearsRepay.setEditable(false);
        addTotalYearsRepay
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        addTotalYearsRepay.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(addTotalYearsRepay, gridBagConstraints);

        addNumberPmts.setEditable(false);
        addNumberPmts.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        addNumberPmts.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(addNumberPmts, gridBagConstraints);

        AddMnthPmt.setText("Monthly Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        AdditionalRepayments.add(AddMnthPmt, gridBagConstraints);

        AddTotIntPaid.setText("Total Interest Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        AdditionalRepayments.add(AddTotIntPaid, gridBagConstraints);

        AddTotCapPaid.setText("Total Capital Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        AdditionalRepayments.add(AddTotCapPaid, gridBagConstraints);

        AddTotAmtRepaid.setText("Total Amount Repaid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        AdditionalRepayments.add(AddTotAmtRepaid, gridBagConstraints);

        AddYrsRepay.setText("Years to Repay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        AdditionalRepayments.add(AddYrsRepay, gridBagConstraints);

        AddNumPmts.setText("Number of Payments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        AdditionalRepayments.add(AddNumPmts, gridBagConstraints);

        jLabel1.setText("Interest Saved");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(jLabel1, gridBagConstraints);

        jLabel3.setText("Years Reduced");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(jLabel3, gridBagConstraints);

        interestSaved.setEditable(false);
        interestSaved.setText(" ");
        interestSaved.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(interestSaved, gridBagConstraints);

        yearsReduced.setEditable(false);
        yearsReduced.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AdditionalRepayments.add(yearsReduced, gridBagConstraints);

        mortgageConditionsJPanel.add(AdditionalRepayments);

        mortgageTabs.addTab("Mortgage Conditions", null,
                mortgageConditionsJPanel, "");

        jPanelGraph.setLayout(new javax.swing.BoxLayout(jPanelGraph,
                javax.swing.BoxLayout.X_AXIS));

        mortgageTabs.addTab("Projection", null, jPanelGraph, "");

        mortgageScheduleJPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        mortgageScheduleJPanel
                .setBorder(new javax.swing.border.TitledBorder(""));
        jScrollPane9.setBorder(new javax.swing.border.TitledBorder(
                "Standard Repayment Schedule"));
        jScrollPane9
                .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        stdScheduleTable.setAutoCreateColumnsFromModel(false);
        stdScheduleTable.setModel(calculator.getStdScheduleTableModel());
        // Post-Init Code: set default jTable cell renderer for all Double data
        // types to currency format.
        // Manually set the width of the column width.
        stdScheduleTable.setDefaultRenderer(Double.class,
                new com.argus.financials.swing.table.CurrencyRenderer());

        for (int k = 0; k < MortgageScheduleTableModel.M_COLUMNS.length; k++) {
            javax.swing.table.TableColumn column = new javax.swing.table.TableColumn(
                    k, MortgageScheduleTableModel.M_COLUMNS[k].m_width);
            stdScheduleTable.addColumn(column);

        }

        // end of Post-init

        jScrollPane9.setViewportView(stdScheduleTable);

        mortgageScheduleJPanel.add(jScrollPane9);

        jScrollPane8.setBorder(new javax.swing.border.TitledBorder(
                "Additional Repayment Schedule"));
        jScrollPane8
                .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        addScheduleTable.setAutoCreateColumnsFromModel(false);
        addScheduleTable.setModel(calculator.getAddScheduleTableModel());
        // Post-Init Code: set default jTable cell renderer for all Double data
        // types to currency format.
        // Manually set the width of the column width.
        addScheduleTable.setDefaultRenderer(Double.class,
                new com.argus.financials.swing.table.CurrencyRenderer());

        for (int k = 0; k < MortgageScheduleTableModel.M_COLUMNS.length; k++) {
            javax.swing.table.TableColumn column = new javax.swing.table.TableColumn(
                    k, MortgageScheduleTableModel.M_COLUMNS[k].m_width);
            addScheduleTable.addColumn(column);

        }

        // end of Post-Init
        jScrollPane8.setViewportView(addScheduleTable);

        mortgageScheduleJPanel.add(jScrollPane8);

        mortgageTabs.addTab("Schedule", null, mortgageScheduleJPanel, "");

        add(mortgageTabs, java.awt.BorderLayout.CENTER);

        controlsJPanel.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 20, 5));

        jButtonReport.setText("Report");
        jButtonReport.setDefaultCapable(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        controlsJPanel.add(jButtonReport);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelCloseSave.add(jButtonSave);

        jButtonSaveAs.setText("Save As");
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

        controlsJPanel.add(jPanelCloseSave);

        jButtonClear.setText("Clear");
        jButtonClear.setDefaultCapable(false);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        controlsJPanel.add(jButtonClear);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.setEnabled(false);
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanel2.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanel2.add(jButtonNext);

        controlsJPanel.add(jPanel2);

        add(controlsJPanel, java.awt.BorderLayout.SOUTH);

    }// GEN-END:initComponents

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

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonClearActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doClear(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        if (!calculator.hasValidData()) {
            String msg = "Data is not ready!";
            javax.swing.JOptionPane.showMessageDialog(null, msg, "Error!",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                doReport();
            }
        }, "doReport").start();

    }// GEN-LAST:event_jButtonReportActionPerformed

    private void mortgageTabsStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_mortgageTabsStateChanged
        // Add your handling code here:
        int index = mortgageTabs.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < mortgageTabs.getTabCount() - 1);

    }// GEN-LAST:event_mortgageTabsStateChanged

    private void editFieldsFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_editFieldsFocusLost
        // Add your handling code here:
        calculate();
    }// GEN-LAST:event_editFieldsFocusLost

    private void editFieldsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_editFieldsActionPerformed
        // Add your handling code here:
        calculate();
    }// GEN-LAST:event_editFieldsActionPerformed

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
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            newModel = false;
            doSave(evt);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        // Add your handling code here:
        gotoTab(BACKWARD);
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        // Add your handling code here:
        gotoTab(FORWARD);
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void addYesItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_addYesItemStateChanged

        if (!addYes.isSelected()) {
            monthlyAddition.setEnabled(false);
            monthlyAddition.setText(null);
        } else
            monthlyAddition.setEnabled(true);

        // if (addYes.isSelected())
        // monthlyAddition.setBackground(java.awt.Color.white);
        // else
        // monthlyAddition.setBackground(java.awt.Color.lightGray);
        calculate();
    }// GEN-LAST:event_addYesItemStateChanged

    public static MortgageView display(final Model model, FocusListener[] listeners) {

        if (view == null) {
            view = new MortgageView();
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
            calculator.disableUpdate();
            calculator.clear();
            this.clear();
            updateTitle();
        } finally {
            // updateEditable();
            calculator.enableUpdate();
            calculator.doUpdate();
            calculate();
        }

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    protected String getDefaultReport() {
        return WordSettings.getInstance().getMortgageReport();
    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {

        // if ( person == null ) return null;

        ReportFields reportFields = ReportFields.getInstance();
        calculator.initializeReportData(reportFields);

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

    public boolean isModified() {
        return (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                .isModified())
                || (jButtonSave.isVisible() && jButtonSave.isEnabled() && getModel()
                        .isModified());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelCloseSave;

    private javax.swing.ButtonGroup paymentButtonGroup;

    private javax.swing.JButton jButtonPrevious;

    private javax.swing.JLabel MonthlyPmtLabel;

    private javax.swing.JTextField purchasePrice;

    private javax.swing.JTextField stdTotalIntPaid;

    private javax.swing.JTextField amountBorrow;

    private javax.swing.JButton jButtonNext;

    private javax.swing.JLabel AddYrsRepay;

    private javax.swing.JLabel AddTotCapPaid;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JLabel MthlyAdditionLabel;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JScrollPane jScrollPane9;

    private javax.swing.JScrollPane jScrollPane8;

    private javax.swing.JTextField addMonthlyPayment;

    private javax.swing.JLabel AdditionalLabel;

    private javax.swing.JTextField stdTotalRepaid;

    private javax.swing.JPanel controlsJPanel;

    private javax.swing.JPanel AdditionalRepayments;

    private javax.swing.JLabel TotalRepayedLabel;

    private javax.swing.JTextField yearsReduced;

    private javax.swing.JLabel YearsRepayLabel;

    private javax.swing.JLabel NumberPmtsLabel;

    private javax.swing.JLabel AddNumPmts;

    private javax.swing.JTextField stdTotalCapPaid;

    private javax.swing.JLabel LoanTermLabel;

    private javax.swing.JTextField depositAmount;

    private javax.swing.JLabel InterestRateLabel;

    private javax.swing.JTextField addTotalYearsRepay;

    private javax.swing.JTextField stdMonthlyPayment;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JTextField addTotalAmtPaid;

    private javax.swing.JLabel AmountBorrowLabel;

    private javax.swing.JTextField monthlyAddition;

    private javax.swing.JPanel MortgageDetails;

    private javax.swing.JTextField loanTerm;

    private javax.swing.JTextField interestSaved;

    private javax.swing.JTextField stdYearsRepay;

    private javax.swing.JTextField stdNumberPayments;

    private javax.swing.JRadioButton addNo;

    private javax.swing.JButton jButtonReport;

    private javax.swing.JPanel AdditionalYesNo;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTabbedPane mortgageTabs;

    private javax.swing.JLabel DepositAmountLabel;

    private javax.swing.JTextField addTotalIntPaid;

    private javax.swing.JLabel InterestPaidLabel;

    private javax.swing.JLabel TotalCapitalLabel;

    private javax.swing.JLabel AddMnthPmt;

    private javax.swing.JLabel PurchasePriceLabel;

    private javax.swing.JTable stdScheduleTable;

    private javax.swing.JTextField interestRate;

    private javax.swing.JPanel mortgageScheduleJPanel;

    private javax.swing.JLabel AddTotIntPaid;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel AddTotAmtRepaid;

    private javax.swing.JTextField addTotalCapPaid;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JRadioButton addYes;

    private javax.swing.JButton jButtonClose;

    protected javax.swing.JButton jButtonDelete;

    private javax.swing.JTable addScheduleTable;

    private javax.swing.JPanel jPanelGraph;

    private javax.swing.JPanel StandardRepayments;

    private javax.swing.JPanel mortgageConditionsJPanel;

    private javax.swing.JTextField addNumberPmts;

    // End of variables declaration//GEN-END:variables

    public void hideControls() {
        controlsJPanel.setVisible(false);
    }

    public void showControls() {
        controlsJPanel.setVisible(true);
    }

    // stop tab from moving beyond the first or last tabs.
    private void gotoTab(int direction) {
        int tabPosition = mortgageTabs.getSelectedIndex();

        if ((direction == FORWARD && tabPosition < SCHEDULE_TAB)
                || (direction == BACKWARD && tabPosition > MORTGAGE_CONDITIONs_TAB)) {
            mortgageTabs.setSelectedIndex(tabPosition + direction);
        }
    }

    public void calculate() {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // perform mortgage calculation
            calculator.calculateAmountBorrowed(this, this);
            calculator.getInputData(this);
            readyToSave = calculator.calculate(this, this);
            enableResultsControls();
        } finally {
            setCursor(null);
        }

    }

    private void enableResultsControls() {
        jButtonNext.setEnabled(readyToSave);
        // jButtonPrevious.setEnabled(readyToSave);
        mortgageTabs.setEnabledAt(PROJECTION_TAB, readyToSave);
        mortgageTabs.setEnabledAt(SCHEDULE_TAB, readyToSave);
    }

    public void setPurchasePrice(double value) {
        purchasePrice.setText(curr.toString(value));
    }

    public void setDeposit(double value) {
        depositAmount.setText(curr.toString(value));
    }

    public void setAmountBorrowed(double value) {
        amountBorrow.setText(curr.toString(value));
    }

    public void setLoanTerm(double value) {
        loanTerm.setText(String.valueOf(value));
    }

    public void setAnnualInterestRate(double value) {
        interestRate.setText(String.valueOf(value));
    }

    public void setAddtnlPaymentOption(boolean value) {
        if (value)
            addYes.setSelected(true);
        else
            addNo.setSelected(true);
    }

    public void setAddtnlMonthlyPayments(double value) {
        monthlyAddition.setText(curr.toString(value));
    }

    public void setStdTotalCapitalPaid(double value) {
        stdTotalCapPaid.setText(curr.toString(value));
    }

    public void setStdTotalAmountRepaid(double value) {
        stdTotalRepaid.setText(curr.toString(value));
    }

    public void setAddtnlTotalAmountRepaid(double value) {
        addTotalAmtPaid.setText(curr.toString(value));

    }

    public void setAddtnlNumberOfPayments(double value) {
        addNumberPmts.setText(number.toString(value));
    }

    public void setAddtnlTotalInterestPaid(double value) {
        addTotalIntPaid.setText(curr.toString(value));
    }

    public void setStdYearsToRepay(double value) {
        stdYearsRepay.setText(number.toString(value));
    }

    public void setAddtnlTotalCapitalPaid(double value) {
        addTotalCapPaid.setText(curr.toString(value));
    }

    public void setAddtnlYearsToRepay(double value) {
        addTotalYearsRepay.setText(number.toString(value));
    }

    public void setAddtnlMonthlyRepayment(double value) {
        addMonthlyPayment.setText(curr.toString(value));
    }

    public void setStdMonthlyRepayment(double value) {
        stdMonthlyPayment.setText(curr.toString(value));
    }

    public void setStdNumberOfPayments(double value) {
        stdNumberPayments.setText((number.toString(value)));
    }

    public void setSTdTotalInterestPaid(double value) {
        stdTotalIntPaid.setText((curr.toString(value)));
    }

    public double getAnnualInterestRate() {
        return Double.parseDouble(interestRate.getText());
    }

    public double getAdditionalNoRepayment() {
        return Double.parseDouble(addNumberPmts.getText());
    }

    public double getAdditonalMonthlyRepayment() {
        return curr.doubleValue(monthlyAddition.getText());
    }

    public double getPurchasePrice() {
        return curr.doubleValue(purchasePrice.getText());
    }

    public double getDeposit() {
        return curr.doubleValue(depositAmount.getText());
    }

    public boolean hasAdditionalPayment() {
        return addYes.isSelected();
    }

    public double getLoanTerm() {
        return Double.parseDouble(loanTerm.getText());
    }

    public double getAmountBorrowed() {
        return curr.doubleValue(amountBorrow.getText());
    }

    public double getStdMonthlyRepayment() {
        return curr.doubleValue(stdMonthlyPayment.getText());
    }

    public void setInterestSaved(double value) {
        interestSaved.setText(curr.toString(value));
    }

    /*
     * public void setRevisedLoanTerm(double value) { revisedLoanTerm.setText(
     * number.toString(value) ); }
     */
    public void setYearsReduced(double value) {
        yearsReduced.setText(number.toString(value));
    }

    public void setStdSchedule(
            com.argus.financials.projection.data.MortgageScheduleTableModel model) {
        stdScheduleTable.setModel(model);
        // javax.swing.table.TableColumn cm =
        // stdScheduleTable.getColumnModel().getColumn(0);
        // cm.setPreferredWidth(10);

        /*
         * for( int k =0; k < MortgageScheduleTableModel.M_COLUMNS.length; k++) {
         * javax.swing.table.TableColumn column = new
         * javax.swing.table.TableColumn(k,
         * MortgageScheduleTableModel.M_COLUMNS[k].m_width);
         * stdScheduleTable.addColumn(column); }
         */
        model.fireTableDataChanged();
    }

    public void setAddtnlSchedule(
            com.argus.financials.projection.data.MortgageScheduleTableModel model) {
        addScheduleTable.setModel(model);
        model.fireTableDataChanged();
    }

    public void setAnnualSchedules(Vector stdData, Vector addtnlData) {

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {

            double[][] yValues = new double[2][stdData.size()];
            String[] xLabels = new String[stdData.size()];
            double value = 0.0;

            for (int i = 0; i < stdData.size(); i++) {
                yValues[0][i] = ((Double) stdData.elementAt(i)).doubleValue();

                value = ((Double) addtnlData.elementAt(i)).doubleValue();
                if (value > 0.0)
                    yValues[1][i] = value;

                xLabels[i] = String.valueOf(i + 1);
            }

            // update Chart with new data
            graphView.addChartLabels(graphView.customizeChart(yValues, xLabels,
                    new String[] { "Standard Repayments",
                            "Additional Repayments" }, new java.awt.Color[] {
                            java.awt.Color.blue, java.awt.Color.red }, true),
                    Currency.getCurrencyInstance());
            graphView.setTitleAxisX("Year");
            graphView.setTitleAxisY1("");
            graphView.setLabelGeneratorAxisY1(CurrencyLabelGenerator
                    .getInstance());

        } finally {
            setCursor(null);
        }

    }

    /**
     * Implements com.argus.swing.IDefaultButton Return the Default Button that
     * will respond to the Enter key being pressed.
     * 
     * 
     * public javax.swing.JButton getDefaultButton() { return jButtonCalculate; }
     */

    /***************************************************************************
     * Update/Save the data
     **************************************************************************/
    public void updateView(String modelTitle) throws java.io.IOException {

        PersonService person = ServiceLocator.getInstance().getClientPerson();
        Model m = person == null ? null : person.getModel(getDefaultType(),
                modelTitle);

        if (m == null) {
            updateView(person);
        } else {
            updateView(m);
        }
        updateTitle();
        // calculate(); // to trigger TS code update

    }

    public void updateView(Model m) throws java.io.IOException {

        // doClear(null);

        if (m == null) {
            updateView(ServiceLocator.getInstance().getClientPerson());
        } else {
            // use copy of model
            Integer id = m.getPrimaryKeyID();
            m = new Model(m);
            m.setPrimaryKeyID(id);
    
            try {
                calculator.disableUpdate();
                calculator.setModel(m);
                // calculator.setSaved();
            } finally {
                calculate();
                calculator.enableUpdate();
                calculator.doUpdate();
                graphView.setGraphData(calculator.getGraphData());
            }
        }
    }

    public void updateView(PersonService person) throws ServiceException {

        calculator.disableUpdate();
        try {
            this.clear();
            Model m = new Model();
            m.setTypeID(ModelType.LOAN_MORTGAGE_CALC);
            calculator.setModel(m);

        } finally {
            calculator.enableUpdate();
            calculator.doUpdate();
            calculate();
        }

    }

    private void saveView(boolean newModel) {

        int result = -1;
        String oldTitle = getTitle();
        try {
            if (!newModel)
                result = SaveProjectionDialog.getSaveProjectionInstance().save(
                        calculator, this);
            else
                result = SaveProjectionDialog.getSaveProjectionInstance()
                        .saveAs(calculator, this);

            if (result == SaveProjectionDialog.CANCEL_OPTION)
                return;
            updateTitle();
        } catch (DuplicateException e) {
            String newTitle = getTitle();

            String msg = "Failed to save model.\n  Current Title: " + oldTitle
                    + "\n  New Title: " + newTitle;
            JOptionPane.showMessageDialog(this, msg,
                    "Title already used by another model",
                    JOptionPane.ERROR_MESSAGE);

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
            saveView(ServiceLocator.getInstance().getClientPerson());
        } catch (Exception e) {
            e.printStackTrace(System.err); // ( e.getMessage() );
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
        calculator.setSaved();

    }

    /*
     * public void clearView() { SwingUtils.clear(this);
     * 
     * calculator.setModel(null); updateTitle(); }
     */

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

    public void clear() {
        setPurchasePrice(0);
        setDeposit(0);
        setLoanTerm(0);
        setAnnualInterestRate(0);
        setAddtnlMonthlyPayments(0);
    }

    public com.argus.financials.GraphView getGraphView() {
        return this.graphView;
    }

    public void setCalculator(MortgageCalc calculator) {
        this.calculator = calculator;
    }

}
