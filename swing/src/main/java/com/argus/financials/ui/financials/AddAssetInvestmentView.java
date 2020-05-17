/*
 * AddIncomeStreamsView.java
 *
 * Created on 13 November 2002, 10:06
 */

package com.argus.financials.ui.financials;

/**
 * 
 * @author valeri chibaev
 */

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.bean.ICode;
import com.argus.financials.api.code.FinancialTypeEnum;
import com.argus.financials.assetinvestment.AvailableInvestmentsTableRow;
import com.argus.financials.assetinvestment.UpdateUnitSharePriceData;
import com.argus.financials.bean.AssetInvestment;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.db.ApirPicBean;
import com.argus.financials.bean.db.IressAssetNameBean;
import com.argus.financials.code.FinancialServiceCode;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.NumberInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

/* BEGIN: BUG-FIX 417 */

public class AddAssetInvestmentView extends AddAssetView implements
        UpdateUnitSharePriceData {

    public static final String FRANKED = "Franked (%)";

    public static final String TAX_FREE_DEFERRED = "Tax Free Deferred (%)";

    private static AddAssetInvestmentView view;

    private boolean thisCTOR = false;

    /** Creates new form AddIncomeStreamsView */
    public AddAssetInvestmentView() {
        setPreferredSize(new java.awt.Dimension(getDefaultWidth(),
                getDefaultHeight()));
        setMinimumSize(new java.awt.Dimension(getDefaultWidth(),
                getDefaultHeight()));

        thisCTOR = true;
        try {
            initComponents();
            // setLayout(new javax.swing.BoxLayout(this,
            // javax.swing.BoxLayout.Y_AXIS));
        } finally {
            thisCTOR = false;
        }

        // add listener, which updates the field "Current Value"
        // NumberOfUnitsSharesDocumentListener documentListener = new
        // NumberOfUnitsSharesDocumentListener( this );
        // jTextFieldUnitShares.getDocument().addDocumentListener(
        // documentListener );

        jTextFieldUnitShares.getAccessibleContext().setAccessibleName(UNITS);
        jTextFieldUnitsSharesPrice.getAccessibleContext().setAccessibleName(
                UNIT_PRICE);
        jTextFieldPriceDate.getDateField().getAccessibleContext()
                .setAccessibleName(UNIT_PRICE_DATE);
        jTextFieldCurrentValue.getAccessibleContext().setAccessibleName(
                CURRENT_VALUE);

        jTextFieldFranked.getAccessibleContext().setAccessibleName(FRANKED);
        jTextFieldTaxFreeDeferred.getAccessibleContext().setAccessibleName(
                TAX_FREE_DEFERRED);

    }

    protected int getDefaultHeight() {
        return 600;
    }

    public static AddAssetInvestmentView getInstance() {
        if (view == null)
            view = new AddAssetInvestmentView();
        return view;
    }

    public static boolean exists() {
        return view != null;
    }

    public int getDefaultFinancialTypeID(String source) {
        if (source.equals(ApirPicBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getId(); // 5=Listed Unit Trust
        if (source.equals(IressAssetNameBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId(); // 4 = Listed Shares
        return FinancialTypeEnum.UNDEFINED.getId(); // 0
    }

    public String getDefaultFinancialTypeDesc(String source) {
        if (source.equals(ApirPicBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getDesc();
        if (source.equals(IressAssetNameBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getDesc();
        return "";
    }

    public void setAssetInvestmentName(String value) {
        jTextFieldAssetInvestmentName.setText(value);
    }

    public void setAssetInvestmentCode(String value) {
        jTextFieldAssetInvestmentCode.setText(value);
    }

    public void setInsitutionName(String value) {
        jTextFieldInstitutionName.setText(value);
    }

    public java.math.BigDecimal getNumberOfUnitShares() {
        return getNumberInstance4().getBigDecimalValue(
                jTextFieldUnitShares.getText());
    }

    public void setNumberOfUnitShares(java.math.BigDecimal value) {
        jTextFieldUnitShares.setText(value == null ? null
                : getNumberInstance4().toString(value));
        jTextFieldUnitShares.getInputVerifier().verify(jTextFieldUnitShares);
    }

    public java.util.Date getPriceDate() {
        return DateTimeUtils.getDate(jTextFieldPriceDate.getText());
    }

    public void setPriceDate(java.util.Date value) {
        jTextFieldPriceDate.setText(DateTimeUtils.asString(value));
    }

    public java.math.BigDecimal getPriceDateValue() {
        return getCurrencyInstance().getBigDecimalValue(
                jTextFieldCurrentValue.getText());
    }

    public void setPriceDateValue(java.math.BigDecimal value) {
        jTextFieldCurrentValue.setText(value == null ? null
                : getCurrencyInstance().toString(value));
    }

    public java.math.BigDecimal getClosePrice() {
        return getCurrencyInstance4().getBigDecimalValue(
                jTextFieldUnitsSharesPrice.getText());
    }

    public void setClosePrice(java.math.BigDecimal value) {
        jTextFieldUnitsSharesPrice.setText(value == null ? null
                : getCurrencyInstance4().toString(value));
        jTextFieldUnitsSharesPrice.getInputVerifier().verify(
                jTextFieldUnitsSharesPrice);
    }

    private void updateComponents(int financialTypeID) {

        boolean managedFundsShares = 
            financialTypeID == FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getId()
         || financialTypeID == FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId();
        boolean other = financialTypeID == FinancialTypeEnum.INVESTMENT_OTHER.getId();
        boolean investmentProperty = financialTypeID == FinancialTypeEnum.INVESTMENT_PROPERTY.getId();

        // if ( !managedFundsShares )
        // jTextFieldAssetInvestmentCode.setText( null );

        jButtonSearch.setEnabled(managedFundsShares);

        jTextFieldAssetInvestmentName.setEnabled(true);
        jTextFieldInstitutionName.setEnabled(true);
        jTextFieldDesc.setEnabled(true);
        jComboBoxOwnerCode.setEnabled(true);
        jTextFieldAccountNumber.setEnabled(true);

        if (investmentProperty) {
            jTextFieldUnitShares.setText(null);
            jTextFieldUnitsSharesPrice.setText(null);
            jTextFieldUpfrontFee.setText(null);
        }
        SwingUtil.setEnabled(jTextFieldUnitShares, !investmentProperty);
        SwingUtil.setEnabled(jTextFieldUnitsSharesPrice, other);
        SwingUtil.setEnabled(jTextFieldUpfrontFee, !investmentProperty);

        jTextFieldPurchaseCost.setEnabled(true);
        jTextFieldPurchaseDate.setEnabled(true);

        SwingUtil.setEnabled(jTextFieldPriceDate, !managedFundsShares);
        SwingUtil.setEnabled(jTextFieldCurrentValue, !managedFundsShares);

        jTextFieldIncome.setEnabled(true);
        jTextFieldCapitalGrowth.setEnabled(true);
        jTextFieldFranked.setEnabled(true);
        jTextFieldTaxFreeDeferred.setEnabled(true);

        // we have to delete the connected asset allocation, because the asset
        // type has changed!
        deleteAssetAllocation((Financial) getAssetInvestment());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupAssetType = new javax.swing.ButtonGroup();
        jPanelName = new javax.swing.JPanel();
        jPanelAssetType = new javax.swing.JPanel();
        jRadioButtonManagedFunds = new javax.swing.JRadioButton();
        jRadioButtonShares = new javax.swing.JRadioButton();
        jRadioButtonOther = new javax.swing.JRadioButton();
        jRadioButtonInvestmentProperty = new javax.swing.JRadioButton();
        jLabelAssetInvestmentCode = new javax.swing.JLabel();
        jTextFieldAssetInvestmentCode = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jLabelAssetInvestmentName = new javax.swing.JLabel();
        jTextFieldAssetInvestmentName = new javax.swing.JTextField();
        jLabelInstitutionName = new javax.swing.JLabel();
        jTextFieldInstitutionName = new javax.swing.JTextField();
        jLabelService = new javax.swing.JLabel();
        jComboBoxService = new javax.swing.JComboBox(new FinancialServiceCode()
                .getCodes().toArray());
        jLabelOwnerCode = new javax.swing.JLabel();
        jComboBoxOwnerCode = new javax.swing.JComboBox(OWNERS);
        jLabelDesc = new javax.swing.JLabel();
        jTextFieldDesc = new javax.swing.JTextField();
        jLabelAccountNumber = new javax.swing.JLabel();
        jTextFieldAccountNumber = new javax.swing.JTextField();
        jPanelValue = new javax.swing.JPanel();
        jLabelUnitShares = new javax.swing.JLabel();
        jTextFieldUnitShares = new javax.swing.JTextField();
        jLabelUnitsSharesPrice = new javax.swing.JLabel();
        jTextFieldUnitsSharesPrice = new javax.swing.JTextField();
        jLabelPurchaseCost = new javax.swing.JLabel();
        jTextFieldPurchaseCost = new javax.swing.JTextField();
        jLabelCurrentValue = new javax.swing.JLabel();
        jTextFieldCurrentValue = new javax.swing.JTextField();
        jLabelPurchaseDate = new javax.swing.JLabel();
        jTextFieldPurchaseDate = new com.argus.bean.FDateChooser();
        jLabelPriceDate = new javax.swing.JLabel();
        jTextFieldPriceDate = new com.argus.bean.FDateChooser();
        jPanelPerformance = new javax.swing.JPanel();
        jLabelIncome = new javax.swing.JLabel();
        jTextFieldIncome = new javax.swing.JTextField();
        jLabelReinvest = new javax.swing.JLabel();
        jCheckBoxReinvest = new javax.swing.JCheckBox();
        jLabelCapitalGrowth = new javax.swing.JLabel();
        jTextFieldCapitalGrowth = new javax.swing.JTextField();
        jLabelFranked = new javax.swing.JLabel();
        jTextFieldFranked = new javax.swing.JTextField();
        jLabelTaxFreeDeferred = new javax.swing.JLabel();
        jTextFieldTaxFreeDeferred = new javax.swing.JTextField();
        jPanelCashflow = new javax.swing.JPanel();
        jLabelAnnualContributions = new javax.swing.JLabel();
        jTextFieldAnnualContributions = new javax.swing.JTextField();
        jLabelContributionIndexation = new javax.swing.JLabel();
        jTextFieldContributionIndexation = new javax.swing.JTextField();
        jLabelContributionsStartDate = new javax.swing.JLabel();
        jTextFieldContributionsStartDate = new com.argus.bean.FDateChooser();
        jLabelContributionsEndDate = new javax.swing.JLabel();
        jTextFieldContributionsEndDate = new com.argus.bean.FDateChooser();
        jLabelAnnualDrawdowns = new javax.swing.JLabel();
        jTextFieldAnnualDrawdowns = new javax.swing.JTextField();
        jLabelDrawdownIndexation = new javax.swing.JLabel();
        jTextFieldDrawdownIndexation = new javax.swing.JTextField();
        jLabelDrawdownsStartDate = new javax.swing.JLabel();
        jTextFieldDrawdownsStartDate = new com.argus.bean.FDateChooser();
        jLabelDrawdownsEndDate = new javax.swing.JLabel();
        jTextFieldDrawdownsEndDate = new com.argus.bean.FDateChooser();
        jPanelFees = new javax.swing.JPanel();
        jLabelOtherExpenses = new javax.swing.JLabel();
        jTextFieldOtherExpenses = new javax.swing.JTextField();
        jLabelUpfrontFee = new javax.swing.JLabel();
        jTextFieldUpfrontFee = new javax.swing.JTextField();
        jLabelOngoingFee = new javax.swing.JLabel();
        jTextFieldOngoingFee = new javax.swing.JTextField();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new java.awt.GridBagLayout());

        jPanelName.setBorder(new javax.swing.border.TitledBorder("Name"));
        jRadioButtonManagedFunds.setText("Managed Funds");
        buttonGroupAssetType.add(jRadioButtonManagedFunds);
        jPanelAssetType.add(jRadioButtonManagedFunds);

        jRadioButtonShares.setText("Shares");
        buttonGroupAssetType.add(jRadioButtonShares);
        jPanelAssetType.add(jRadioButtonShares);

        jRadioButtonOther.setText("Other");
        buttonGroupAssetType.add(jRadioButtonOther);
        jPanelAssetType.add(jRadioButtonOther);

        jRadioButtonInvestmentProperty.setText("Investment Property");
        buttonGroupAssetType.add(jRadioButtonInvestmentProperty);
        jPanelAssetType.add(jRadioButtonInvestmentProperty);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelName.add(jPanelAssetType, gridBagConstraints);

        jLabelAssetInvestmentCode.setText("Investment Code");
        jLabelAssetInvestmentCode.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelAssetInvestmentCode
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelAssetInvestmentCode, gridBagConstraints);

        jTextFieldAssetInvestmentCode.setPreferredSize(new java.awt.Dimension(
                120, 19));
        jTextFieldAssetInvestmentCode.setMinimumSize(new java.awt.Dimension(
                120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jTextFieldAssetInvestmentCode, gridBagConstraints);

        jButtonSearch.setText("Search");
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelName.add(jButtonSearch, gridBagConstraints);

        jLabelAssetInvestmentName.setText("Asset Name *");
        jLabelAssetInvestmentName.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelAssetInvestmentName
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelAssetInvestmentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jTextFieldAssetInvestmentName, gridBagConstraints);

        jLabelInstitutionName.setText("Institution");
        jLabelInstitutionName.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelInstitutionName.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelInstitutionName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jTextFieldInstitutionName, gridBagConstraints);

        jLabelService.setText("Service");
        jLabelService.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelService.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelService, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxService, gridBagConstraints);

        jLabelOwnerCode.setText("Owner *");
        jLabelOwnerCode.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOwnerCode.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelOwnerCode, gridBagConstraints);

        jComboBoxOwnerCode.setPreferredSize(new java.awt.Dimension(120, 19));
        jComboBoxOwnerCode.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jComboBoxOwnerCode, gridBagConstraints);

        jLabelDesc.setText("Notes/Comments");
        jLabelDesc.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDesc.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelName.add(jLabelDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelName.add(jTextFieldDesc, gridBagConstraints);

        jLabelAccountNumber.setText("Account Number");
        jLabelAccountNumber.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAccountNumber.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jTextFieldAccountNumber, gridBagConstraints);

        add(jPanelName);

        jPanelValue.setLayout(new java.awt.GridBagLayout());

        jPanelValue.setBorder(new javax.swing.border.TitledBorder("Value"));
        jLabelUnitShares.setText("No Units/Shares");
        jLabelUnitShares.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelUnitShares.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelUnitShares, gridBagConstraints);

        jTextFieldUnitShares
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUnitShares.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldUnitShares.setInputVerifier(new NumberInputVerifier(4));
        jTextFieldUnitShares.setMinimumSize(new java.awt.Dimension(120, 19));
        jTextFieldUnitShares
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldUnitsSharesPriceFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldUnitShares, gridBagConstraints);

        jLabelUnitsSharesPrice.setText("Units/Shares Price");
        jLabelUnitsSharesPrice
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelUnitsSharesPrice.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelUnitsSharesPrice, gridBagConstraints);

        jTextFieldUnitsSharesPrice
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUnitsSharesPrice
                .setInputVerifier(new CurrencyInputVerifier(4));
        jTextFieldUnitsSharesPrice
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldUnitsSharesPriceFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldUnitsSharesPrice, gridBagConstraints);

        jLabelPurchaseCost.setText("Purchase Price");
        jLabelPurchaseCost.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelPurchaseCost.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelPurchaseCost, gridBagConstraints);

        jTextFieldPurchaseCost
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldPurchaseCost
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldPurchaseCost.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldPurchaseCost.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldPurchaseCost, gridBagConstraints);

        jLabelCurrentValue.setText("Current Value");
        jLabelCurrentValue.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCurrentValue.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelCurrentValue, gridBagConstraints);

        jTextFieldCurrentValue
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCurrentValue.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldCurrentValue
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldUnitsSharesPriceFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldCurrentValue, gridBagConstraints);

        jLabelPurchaseDate.setText("Purchase Date");
        jLabelPurchaseDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelPurchaseDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelPurchaseDate, gridBagConstraints);

        jTextFieldPurchaseDate
                .setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldPurchaseDate
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldPurchaseDate.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldPurchaseDate, gridBagConstraints);

        jLabelPriceDate.setText("Price Date");
        jLabelPriceDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelPriceDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelPriceDate, gridBagConstraints);

        jTextFieldPriceDate.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldPriceDate, gridBagConstraints);

        add(jPanelValue);

        jPanelPerformance.setLayout(new java.awt.GridBagLayout());

        jPanelPerformance.setBorder(new javax.swing.border.TitledBorder(
                "Performance"));
        jLabelIncome.setText("Income (% p.a.)");
        jLabelIncome.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelIncome.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelIncome, gridBagConstraints);

        jTextFieldIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIncome.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldIncome.setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldIncome.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jTextFieldIncome, gridBagConstraints);

        jLabelReinvest.setText("Reinvest");
        jLabelReinvest.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelReinvest.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelReinvest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelPerformance.add(jCheckBoxReinvest, gridBagConstraints);

        jLabelCapitalGrowth.setText("Capital Growth (% p.a.)");
        jLabelCapitalGrowth.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCapitalGrowth.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelCapitalGrowth, gridBagConstraints);

        jTextFieldCapitalGrowth
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldCapitalGrowth
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldCapitalGrowth.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldCapitalGrowth.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jTextFieldCapitalGrowth, gridBagConstraints);

        jLabelFranked.setText("Franked (%)");
        jLabelFranked.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelFranked.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelFranked, gridBagConstraints);

        jTextFieldFranked
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldFranked.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldFranked.setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldFranked.setMinimumSize(new java.awt.Dimension(120, 19));
        jTextFieldFranked.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldFrankedFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jTextFieldFranked, gridBagConstraints);

        jLabelTaxFreeDeferred.setText("Tax Free Deferred (%)");
        jLabelTaxFreeDeferred.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelTaxFreeDeferred.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelTaxFreeDeferred, gridBagConstraints);

        jTextFieldTaxFreeDeferred.setToolTipText("% rebate is to apply ?");
        jTextFieldTaxFreeDeferred
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTaxFreeDeferred.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldTaxFreeDeferred
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldFrankedFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelPerformance.add(jTextFieldTaxFreeDeferred, gridBagConstraints);

        add(jPanelPerformance);

        jPanelCashflow.setLayout(new java.awt.GridBagLayout());

        jPanelCashflow
                .setBorder(new javax.swing.border.TitledBorder("Cashflow"));
        jLabelAnnualContributions.setText("Annual Contributions");
        jLabelAnnualContributions.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelAnnualContributions
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelAnnualContributions, gridBagConstraints);

        jTextFieldAnnualContributions.setToolTipText("Subject to Tax");
        jTextFieldAnnualContributions
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAnnualContributions.setPreferredSize(new java.awt.Dimension(
                120, 19));
        jTextFieldAnnualContributions.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldAnnualContributions.setMinimumSize(new java.awt.Dimension(
                120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldAnnualContributions, gridBagConstraints);

        jLabelContributionIndexation.setText("Indexation (%)");
        jLabelContributionIndexation.setPreferredSize(new java.awt.Dimension(
                120, 20));
        jLabelContributionIndexation.setMinimumSize(new java.awt.Dimension(120,
                20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelContributionIndexation, gridBagConstraints);

        jTextFieldContributionIndexation
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldContributionIndexation.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCashflow
                .add(jTextFieldContributionIndexation, gridBagConstraints);

        jLabelContributionsStartDate.setText("Start Date");
        jLabelContributionsStartDate.setPreferredSize(new java.awt.Dimension(
                120, 20));
        jLabelContributionsStartDate.setMinimumSize(new java.awt.Dimension(120,
                20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelContributionsStartDate, gridBagConstraints);

        jTextFieldContributionsStartDate.setInputVerifier(DateInputVerifier
                .getInstance());
        jTextFieldContributionsStartDate
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldContributionsStartDate.setMinimumSize(new java.awt.Dimension(
                120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow
                .add(jTextFieldContributionsStartDate, gridBagConstraints);

        jLabelContributionsEndDate.setText("End Date");
        jLabelContributionsEndDate.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelContributionsEndDate.setMinimumSize(new java.awt.Dimension(120,
                20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelContributionsEndDate, gridBagConstraints);

        jTextFieldContributionsEndDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCashflow.add(jTextFieldContributionsEndDate, gridBagConstraints);

        jLabelAnnualDrawdowns.setText("Annual Drawdowns");
        jLabelAnnualDrawdowns.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAnnualDrawdowns.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jLabelAnnualDrawdowns, gridBagConstraints);

        jTextFieldAnnualDrawdowns.setToolTipText("Subject to Tax");
        jTextFieldAnnualDrawdowns
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAnnualDrawdowns.setPreferredSize(new java.awt.Dimension(120,
                19));
        jTextFieldAnnualDrawdowns.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldAnnualDrawdowns
                .setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jTextFieldAnnualDrawdowns, gridBagConstraints);

        jLabelDrawdownIndexation.setText("Indexation (%)");
        jLabelDrawdownIndexation.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelDrawdownIndexation
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jLabelDrawdownIndexation, gridBagConstraints);

        jTextFieldDrawdownIndexation.setToolTipText("Indexation of Drawdowns");
        jTextFieldDrawdownIndexation
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldDrawdownIndexation.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelCashflow.add(jTextFieldDrawdownIndexation, gridBagConstraints);

        jLabelDrawdownsStartDate.setText("Start Date");
        jLabelDrawdownsStartDate.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelDrawdownsStartDate
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelDrawdownsStartDate, gridBagConstraints);

        jTextFieldDrawdownsStartDate.setInputVerifier(DateInputVerifier
                .getInstance());
        jTextFieldDrawdownsStartDate.setPreferredSize(new java.awt.Dimension(
                120, 19));
        jTextFieldDrawdownsStartDate.setMinimumSize(new java.awt.Dimension(120,
                19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldDrawdownsStartDate, gridBagConstraints);

        jLabelDrawdownsEndDate.setText("End Date");
        jLabelDrawdownsEndDate
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDrawdownsEndDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelDrawdownsEndDate, gridBagConstraints);

        jTextFieldDrawdownsEndDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCashflow.add(jTextFieldDrawdownsEndDate, gridBagConstraints);

        add(jPanelCashflow);

        jPanelFees.setLayout(new java.awt.GridBagLayout());

        jPanelFees.setBorder(new javax.swing.border.TitledBorder("Fees"));
        jLabelOtherExpenses.setText("Other Expenses");
        jLabelOtherExpenses.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOtherExpenses.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jLabelOtherExpenses, gridBagConstraints);

        jTextFieldOtherExpenses.setToolTipText("Other Related Expenses");
        jTextFieldOtherExpenses
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOtherExpenses
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldOtherExpenses.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldOtherExpenses.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jTextFieldOtherExpenses, gridBagConstraints);

        jLabelUpfrontFee.setText("Up front Fee (% p.a.)");
        jLabelUpfrontFee.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelUpfrontFee.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jLabelUpfrontFee, gridBagConstraints);

        jTextFieldUpfrontFee.setToolTipText("Subject to Tax");
        jTextFieldUpfrontFee
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUpfrontFee.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldUpfrontFee.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldUpfrontFee.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jTextFieldUpfrontFee, gridBagConstraints);

        jLabelOngoingFee.setText("On going Fee (% p.a.)");
        jLabelOngoingFee.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOngoingFee.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jLabelOngoingFee, gridBagConstraints);

        jTextFieldOngoingFee.setToolTipText("Deductible Amount (Payment)");
        jTextFieldOngoingFee
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOngoingFee.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFees.add(jTextFieldOngoingFee, gridBagConstraints);

        add(jPanelFees);

    }// GEN-END:initComponents

    private void jTextFieldFrankedFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldFrankedFocusLost
        if (evt.getSource() == jTextFieldFranked
                || evt.getSource() == jTextFieldTaxFreeDeferred)
            frankedTaxFreeDeferredChanged(evt.getComponent()
                    .getAccessibleContext().getAccessibleName());
    }// GEN-LAST:event_jTextFieldFrankedFocusLost

    private void jTextFieldUnitsSharesPriceFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldUnitsSharesPriceFocusLost
        unitSharePriceChanged(evt.getComponent().getAccessibleContext()
                .getAccessibleName());
    }// GEN-LAST:event_jTextFieldUnitsSharesPriceFocusLost

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSearchActionPerformed
        doSearch();
    }// GEN-LAST:event_jButtonSearchActionPerformed

    // overrite Container method to allow design time UI development
    public void add(java.awt.Component comp, Object constraints) {
        jPanelDetails.add(comp, constraints);
    }

    public java.awt.Component add(java.awt.Component comp) {
        return thisCTOR ? jPanelDetails.add(comp) : super.add(comp);
        // return jPanelDetails.add( comp ); // ??? called from base class ctor
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.argus.bean.FDateChooser jTextFieldPurchaseDate;

    private javax.swing.JTextField jTextFieldUnitsSharesPrice;

    private javax.swing.JTextField jTextFieldAssetInvestmentName;

    private javax.swing.JLabel jLabelContributionsStartDate;

    private javax.swing.JLabel jLabelCurrentValue;

    private javax.swing.JRadioButton jRadioButtonManagedFunds;

    private javax.swing.JLabel jLabelAssetInvestmentName;

    private javax.swing.JComboBox jComboBoxService;

    private javax.swing.JCheckBox jCheckBoxReinvest;

    private javax.swing.JTextField jTextFieldAssetInvestmentCode;

    private javax.swing.JLabel jLabelOngoingFee;

    private javax.swing.JPanel jPanelFees;

    private javax.swing.JButton jButtonSearch;

    private javax.swing.JLabel jLabelUpfrontFee;

    private javax.swing.JLabel jLabelAnnualDrawdowns;

    private javax.swing.JLabel jLabelService;

    private javax.swing.JLabel jLabelDesc;

    private javax.swing.JTextField jTextFieldOtherExpenses;

    private javax.swing.JTextField jTextFieldContributionIndexation;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JTextField jTextFieldOngoingFee;

    private javax.swing.JLabel jLabelIncome;

    private javax.swing.JLabel jLabelAssetInvestmentCode;

    private javax.swing.JRadioButton jRadioButtonOther;

    private com.argus.bean.FDateChooser jTextFieldContributionsStartDate;

    private javax.swing.JLabel jLabelDrawdownsStartDate;

    private javax.swing.JTextField jTextFieldTaxFreeDeferred;

    private javax.swing.JRadioButton jRadioButtonInvestmentProperty;

    private javax.swing.JRadioButton jRadioButtonShares;

    private javax.swing.JLabel jLabelPriceDate;

    private javax.swing.JLabel jLabelContributionIndexation;

    private javax.swing.JLabel jLabelCapitalGrowth;

    private javax.swing.JLabel jLabelUnitsSharesPrice;

    private com.argus.bean.FDateChooser jTextFieldDrawdownsEndDate;

    private com.argus.bean.FDateChooser jTextFieldDrawdownsStartDate;

    private javax.swing.JPanel jPanelValue;

    private javax.swing.JTextField jTextFieldFranked;

    private javax.swing.ButtonGroup buttonGroupAssetType;

    private javax.swing.JLabel jLabelFranked;

    private javax.swing.JTextField jTextFieldIncome;

    private javax.swing.JLabel jLabelOwnerCode;

    private com.argus.bean.FDateChooser jTextFieldPriceDate;

    private javax.swing.JTextField jTextFieldCurrentValue;

    private javax.swing.JLabel jLabelUnitShares;

    private javax.swing.JLabel jLabelDrawdownIndexation;

    private javax.swing.JTextField jTextFieldAnnualDrawdowns;

    private javax.swing.JLabel jLabelAccountNumber;

    private javax.swing.JTextField jTextFieldUnitShares;

    private com.argus.bean.FDateChooser jTextFieldContributionsEndDate;

    private javax.swing.JTextField jTextFieldDesc;

    private javax.swing.JPanel jPanelPerformance;

    private javax.swing.JTextField jTextFieldAnnualContributions;

    private javax.swing.JTextField jTextFieldDrawdownIndexation;

    private javax.swing.JTextField jTextFieldCapitalGrowth;

    private javax.swing.JLabel jLabelContributionsEndDate;

    private javax.swing.JComboBox jComboBoxOwnerCode;

    private javax.swing.JLabel jLabelPurchaseCost;

    private javax.swing.JLabel jLabelAnnualContributions;

    private javax.swing.JLabel jLabelDrawdownsEndDate;

    private javax.swing.JLabel jLabelPurchaseDate;

    private javax.swing.JLabel jLabelOtherExpenses;

    private javax.swing.JLabel jLabelInstitutionName;

    private javax.swing.JPanel jPanelCashflow;

    private javax.swing.JTextField jTextFieldPurchaseCost;

    private javax.swing.JLabel jLabelTaxFreeDeferred;

    private javax.swing.JPanel jPanelAssetType;

    private javax.swing.JTextField jTextFieldUpfrontFee;

    private javax.swing.JTextField jTextFieldInstitutionName;

    private javax.swing.JTextField jTextFieldAccountNumber;

    private javax.swing.JLabel jLabelReinvest;

    // End of variables declaration//GEN-END:variables

    public AssetInvestment getAssetInvestment() {
        if (getObject() == null)
            setObject(new AssetInvestment());
        return (AssetInvestment) getObject();
    }

    public Integer getObjectType() {
        return AssetInvestment.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_ASSET_INVESTMENT.getDescription();
    }

    public boolean updateView() {

        if (!super.updateView())
            return false;

        Currency money = getCurrencyInstance();
        Percent percent = getPercentInstance();
        Number2 number = getNumberInstance();

        AssetInvestment assetInvestment = getAssetInvestment();

        ICode financialType = assetInvestment.getFinancialType();
        ICode financialCode = assetInvestment.getFinancialCode();
        // set old FinancialCode
        old_FinancialCode = assetInvestment.getFinancialCode();

        // update JRadioButtons for FinancialType ("Managed Funds", "Shares",
        // "Other", ...)
        setSelectedJRadioButtonFinancialType(financialType);

        jTextFieldAssetInvestmentCode.setText(financialCode == null ? null
                : financialCode.getCode());
        jTextFieldAssetInvestmentName.setText(financialCode == null ? null
                : financialCode.getDescription());

        jTextFieldFranked.setText(percent
                .toString(assetInvestment.getFranked()));

        jTextFieldTaxFreeDeferred.setText(percent.toString(assetInvestment
                .getTaxFreeDeferred()));

        jTextFieldCapitalGrowth.setText(percent.toString(assetInvestment
                .getCapitalGrowth()));

        jTextFieldIncome.setText(percent.toString(assetInvestment.getIncome()));

        jTextFieldUpfrontFee.setText(percent.toString(assetInvestment
                .getUpfrontFee()));
        jTextFieldOngoingFee.setText(percent.toString(assetInvestment
                .getOngoingFee()));

        jTextFieldInstitutionName.setText(assetInvestment.getInstitution());

        jComboBoxService.setSelectedItem(assetInvestment.getFinancialService());

        jTextFieldDesc.setText(assetInvestment.getFinancialDesc());

        jComboBoxOwnerCode.setSelectedItem(assetInvestment.getOwner());

        jTextFieldAccountNumber.setText(assetInvestment.getAccountNumber());

        jTextFieldUnitShares.setText(getNumberInstance4().toString(
                assetInvestment.getUnitsShares()));
        jTextFieldUnitShares.getInputVerifier().verify(jTextFieldUnitShares);

        jTextFieldUnitsSharesPrice.setText(getCurrencyInstance4().toString(
                assetInvestment.getUnitsSharesPrice()));
        jTextFieldUnitsSharesPrice.getInputVerifier().verify(
                jTextFieldUnitsSharesPrice);

        jTextFieldCurrentValue.setText(money.toString(assetInvestment
                .getAmount()));
        java.util.Date date = assetInvestment.getPriceDate();
        jTextFieldPriceDate.setText(date == null ? null : DateTimeUtils
                .asString(date));

        date = assetInvestment.getStartDate();
        jTextFieldPurchaseDate.setText(date == null ? null : DateTimeUtils
                .asString(date));

        jTextFieldPurchaseCost.setText(money.toString(assetInvestment
                .getPurchaseCost()));

        jTextFieldAnnualContributions.setText(money.toString(assetInvestment
                .getContributionAnnualAmount()));
        jTextFieldContributionIndexation.setText(percent
                .toString(assetInvestment.getContributionIndexation()));
        jTextFieldContributionsStartDate.setText(DateTimeUtils
                .asString(assetInvestment.getContributionStartDate()));
        jTextFieldContributionsEndDate.setText(DateTimeUtils
                .asString(assetInvestment.getContributionEndDate()));

        jTextFieldAnnualDrawdowns.setText(money.toString(assetInvestment
                .getDrawdownAnnualAmount()));
        jTextFieldDrawdownIndexation.setText(percent.toString(assetInvestment
                .getDrawdownIndexation()));
        jTextFieldDrawdownsStartDate.setText(DateTimeUtils
                .asString(assetInvestment.getDrawdownStartDate()));
        jTextFieldDrawdownsEndDate.setText(DateTimeUtils
                .asString(assetInvestment.getDrawdownEndDate()));

        jTextFieldOtherExpenses.setText(money.toString(assetInvestment
                .getOtherExpenses()));

        jCheckBoxReinvest.setSelected(assetInvestment.isReinvest());

        // update current units/shares price-fields
        // updateCurrentUnitsSharesPrice( financialCode );

        return true;

    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {
        String msg = "";

        String s = jTextFieldAssetInvestmentName.getText();
        if (s == null || s.trim().length() == 0)
            msg += "Asset Name is required.\n";

        Integer ownerCodeID = new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem());
        if (ownerCodeID == null)
            msg += "Owner is required.\n";

        msg += checkDateField(jTextFieldContributionsEndDate,
                "Contributions End Date");
        msg += checkDateField(jTextFieldContributionsStartDate,
                "Contributions Start Date");
        msg += checkDateField(jTextFieldDrawdownsEndDate, "Drawdowns End Date");
        msg += checkDateField(jTextFieldDrawdownsStartDate,
                "Drawdowns Start Date");
        msg += checkDateField(jTextFieldPriceDate, "Price Date");
        msg += checkDateField(jTextFieldPurchaseDate, "Purchase Date");

        if (msg.length() == 0)
            return;

        if (showMessage)
            javax.swing.JOptionPane.showMessageDialog(this, msg, "ERROR",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        throw new InvalidCodeException(msg);

    }

    public boolean saveView() throws InvalidCodeException {

        // unitSharePriceChanged();

        checkRequiredFields(true);

        Currency money = getCurrencyInstance();
        Percent percent = getPercentInstance();
        Number2 number = getNumberInstance();

        AssetInvestment assetInvestment = getAssetInvestment();

        // store input fields: Franked, TaxFreeDeferred, CapitalGrowth, Income
        // and UpfrontFee
        // copyInputFieldsToAssetInvestment();
        assetInvestment.setFranked(percent.getBigDecimalValue(jTextFieldFranked
                .getText()));
        assetInvestment.setTaxFreeDeferred(percent
                .getBigDecimalValue(jTextFieldTaxFreeDeferred.getText()));
        assetInvestment.setCapitalGrowth(percent
                .getBigDecimalValue(jTextFieldCapitalGrowth.getText()));
        assetInvestment.setIncome(percent.getBigDecimalValue(jTextFieldIncome
                .getText()));
        assetInvestment.setUpfrontFee(percent
                .getBigDecimalValue(jTextFieldUpfrontFee.getText()));
        assetInvestment.setOngoingFee(percent
                .getBigDecimalValue(jTextFieldOngoingFee.getText()));

        assetInvestment.setInstitution(jTextFieldInstitutionName.getText());

        assetInvestment.setFinancialService((ReferenceCode) jComboBoxService
                .getSelectedItem());

        // get selected investment type (FinancialType)
        Integer financialTypeId = null;
        if (jRadioButtonManagedFunds.isSelected())
            financialTypeId = FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getId();
        else if (jRadioButtonShares.isSelected())
            financialTypeId = FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId();
        else if (jRadioButtonInvestmentProperty.isSelected())
            financialTypeId = FinancialTypeEnum.INVESTMENT_PROPERTY.getId();
        else if (jRadioButtonOther.isSelected())
            financialTypeId = FinancialTypeEnum.INVESTMENT_OTHER.getId();

        // update FinancialType
        assetInvestment.setFinancialTypeId(financialTypeId);

        try {
            updateFinancialCode(assetInvestment, jTextFieldAssetInvestmentName
                    .getText().trim(), jTextFieldAssetInvestmentCode.getText()
                    .trim());

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new InvalidCodeException(e.getMessage());
        }

        assetInvestment.setFinancialDesc(jTextFieldDesc.getText());

        assetInvestment.setOwnerCodeID(new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem()));

        assetInvestment.setAccountNumber(jTextFieldAccountNumber.getText());

        assetInvestment.setPurchaseCost(money
                .getBigDecimalValue(jTextFieldPurchaseCost.getText()));

        assetInvestment.setUnitsShares(getNumberInstance4().getDoubleValue(
                jTextFieldUnitShares.getText()));
        assetInvestment.setUnitsSharesPrice(getCurrencyInstance4()
                .getBigDecimalValue(jTextFieldUnitsSharesPrice.getText()));
        assetInvestment.setAmount(money
                .getBigDecimalValue(deleteParenthesis(jTextFieldCurrentValue
                        .getText())));
        assetInvestment.setPriceDate(DateTimeUtils
                .getSqlDate(jTextFieldPriceDate.getText()));

        assetInvestment.setStartDate(DateTimeUtils
                .getSqlDate(jTextFieldPurchaseDate.getText()));

        assetInvestment.setContributionAnnualAmount(money
                .getBigDecimalValue(jTextFieldAnnualContributions.getText()));
        assetInvestment
                .setContributionIndexation(percent
                        .getBigDecimalValue(jTextFieldContributionIndexation
                                .getText()));
        assetInvestment.setContributionStartDate(DateTimeUtils
                .getSqlDate(jTextFieldContributionsStartDate.getText()));
        assetInvestment.setContributionEndDate(DateTimeUtils
                .getSqlDate(jTextFieldContributionsEndDate.getText()));

        assetInvestment.setDrawdownAnnualAmount(money
                .getBigDecimalValue(jTextFieldAnnualDrawdowns.getText()));
        assetInvestment.setDrawdownIndexation(percent
                .getBigDecimalValue(jTextFieldDrawdownIndexation.getText()));
        assetInvestment.setDrawdownStartDate(DateTimeUtils
                .getSqlDate(jTextFieldDrawdownsStartDate.getText()));
        assetInvestment.setDrawdownEndDate(DateTimeUtils
                .getSqlDate(jTextFieldDrawdownsEndDate.getText()));

        assetInvestment.setOtherExpenses(money
                .getBigDecimalValue(jTextFieldOtherExpenses.getText()));

        assetInvestment.setReinvest(jCheckBoxReinvest.isSelected());

        return true;

    }

    /**
     * Updates the selected JRadioButtons for FinancialType ("Managed Funds",
     * "Shares", "Other", ...)
     * 
     * @param FinancialType -
     *            financial type of an asset
     */
    private void setSelectedJRadioButtonFinancialType(ICode financialType) {
        // set jRadioButton
        if (financialType != null
                && financialType.getId() == FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId())
            jRadioButtonShares.setSelected(true);
        else if (financialType != null
                && financialType.getId() == FinancialTypeEnum.INVESTMENT_LISTED_UNIT_TRUST.getId())
            jRadioButtonManagedFunds.setSelected(true);
        else if (financialType != null
                && financialType.getId() == FinancialTypeEnum.INVESTMENT_PROPERTY.getId())
            jRadioButtonInvestmentProperty.setSelected(true);
        else
            // if ( financialType != null && financialTypeID.getCodeID() == INVESTMENT_OTHER )
            jRadioButtonOther.setSelected(true);
    }

    // FinancialType has to be updated, if not, AssetAllocation is wrong!!!
    protected void updateInputFields(AvailableInvestmentsTableRow row) {
        super.updateInputFields(row);

        // update JRadioButtons for FinancialType ("Managed Funds", "Shares",
        // "Other", ...)
        setSelectedJRadioButtonFinancialType(getAssetInvestment()
                .getFinancialType());
    }

    protected void frankedTaxFreeDeferredChanged(String source) {

        Percent percent = getPercentInstance();

        double franked = percent.doubleValue(jTextFieldFranked.getText());
        double taxFreeDeferred = percent.doubleValue(jTextFieldTaxFreeDeferred
                .getText());
        if (franked + taxFreeDeferred <= 100.)
            return;

        if (FRANKED.equals(source) && franked > 0.) { // 1st priority
            jTextFieldFranked.setText(percent.toString(0.));
            jTextFieldFranked.requestFocus();
        } else if (TAX_FREE_DEFERRED.equals(source) && taxFreeDeferred > 0.) { // 2nd
                                                                                // priority
            jTextFieldTaxFreeDeferred.setText(percent.toString(0.));
            jTextFieldTaxFreeDeferred.requestFocus();
        }

        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                "Total of '" + FRANKED + "' and '" + TAX_FREE_DEFERRED
                        + "' could not be more than 100.00 %", "ERROR",
                JOptionPane.ERROR_MESSAGE);

    }

}
