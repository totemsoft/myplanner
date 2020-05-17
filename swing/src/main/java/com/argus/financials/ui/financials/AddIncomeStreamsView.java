/*
 * AddIncomeStreamsView.java
 *
 * Created on 13 November 2002, 10:06
 */

package com.argus.financials.ui.financials;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.bean.ICode;
import com.argus.financials.api.code.FinancialTypeEnum;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.assetinvestment.UpdateUnitSharePriceData;
import com.argus.financials.bean.IncomeStream;
import com.argus.financials.bean.db.ApirPicBean;
import com.argus.financials.bean.db.IressAssetNameBean;
import com.argus.financials.code.FinancialServiceCode;
import com.argus.financials.code.FrequencyCode;
import com.argus.financials.code.FundType;
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

public class AddIncomeStreamsView extends AddAssetView // javax.swing.JPanel
        implements UpdateUnitSharePriceData {

    private static AddIncomeStreamsView view;

    private boolean thisCTOR = false;

    /** Creates new form AddIncomeStreamsView */
    public AddIncomeStreamsView() {
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

        SwingUtil.setEnabled(jTextFieldTaxedIncome, false);

        jTextFieldUnitShares.getAccessibleContext().setAccessibleName(UNITS);
        jTextFieldUnitsSharesPrice.getAccessibleContext().setAccessibleName(
                UNIT_PRICE);
        jTextFieldPriceDate.getDateField().getAccessibleContext()
                .setAccessibleName(UNIT_PRICE_DATE);
        jTextFieldCurrentValue.getAccessibleContext().setAccessibleName(
                CURRENT_VALUE);

    }

    protected int getDefaultHeight() {
        return 600;
    }

    public static AddIncomeStreamsView getInstance() {
        if (view == null)
            view = new AddIncomeStreamsView();
        return view;
    }

    public static boolean exists() {
        return view != null;
    }

    public int getDefaultFinancialTypeID(String source) {
        if (source.equals(ApirPicBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.SUPERANNUATION_LISTED_UNIT_TRUST.getId();
        if (source.equals(IressAssetNameBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.INVESTMENT_LISTED_SHARES.getId(); // 4 = Listed Shares
        return FinancialTypeEnum.UNDEFINED.getId();
    }

    public String getDefaultFinancialTypeDesc(String source) {
        if (source.equals(ApirPicBean.DATABASE_TABLE_NAME))
            return FinancialTypeEnum.SUPERANNUATION_LISTED_UNIT_TRUST.getDesc();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelName = new javax.swing.JPanel();
        jLabelFundType = new javax.swing.JLabel();
        jComboBoxFundType = new javax.swing.JComboBox(
                new Object[] { ReferenceCode.CODE_NONE,
                        FundType.rcANNUITY_SHORT, FundType.rcANNUITY,
                        FundType.rcANNUITY_LONG, FundType.rcPENSION });
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
        jLabelPriceDate = new javax.swing.JLabel();
        jTextFieldPriceDate = new com.argus.bean.FDateChooser();
        jLabelPurchaseDate = new javax.swing.JLabel();
        jTextFieldPurchaseDate = new com.argus.bean.FDateChooser();
        jLabelMaturityDate = new javax.swing.JLabel();
        jTextFieldMaturityDate = new com.argus.bean.FDateChooser();
        jPanelPerformance = new javax.swing.JPanel();
        jLabelEarnings = new javax.swing.JLabel();
        jTextFieldEarnings = new javax.swing.JTextField();
        jLabelCapitalGrowth11 = new javax.swing.JLabel();
        jLabelCapitalGrowth1 = new javax.swing.JLabel();
        jPanelFees = new javax.swing.JPanel();
        jLabelUpfrontFee = new javax.swing.JLabel();
        jTextFieldUpfrontFee = new javax.swing.JTextField();
        jLabelOngoingFee = new javax.swing.JLabel();
        jTextFieldOngoingFee = new javax.swing.JTextField();
        jPanelCashflow = new javax.swing.JPanel();
        jLabelAnnualPayment = new javax.swing.JLabel();
        jTextFieldAnnualPayment = new javax.swing.JTextField();
        jLabelFrequency = new javax.swing.JLabel();
        jComboBoxFrequency = new javax.swing.JComboBox(new Object[] {
                ReferenceCode.CODE_NONE, FrequencyCode.rcONLY_ONCE,
                FrequencyCode.rcWEEKLY, FrequencyCode.rcFORTNIGHTLY,
                FrequencyCode.rcTWICE_MONTHLY, FrequencyCode.rcMONTHLY,
                FrequencyCode.rcEVERY_OTHER_MONTH,
                FrequencyCode.rcEVERY_THREE_MONTHS,
                FrequencyCode.rcHALF_YEARLY, FrequencyCode.rcYEARLY, });
        jLabelIndexation = new javax.swing.JLabel();
        jTextFieldIndexation = new javax.swing.JTextField();
        jPanelOther = new javax.swing.JPanel();
        jLabelDeductibleTax = new javax.swing.JLabel();
        jTextFieldDeductibleTax = new javax.swing.JTextField();
        jLabelTaxedIncome = new javax.swing.JLabel();
        jTextFieldTaxedIncome = new javax.swing.JTextField();
        jLabelRebate = new javax.swing.JLabel();
        jTextFieldRebate = new javax.swing.JTextField();
        jLabelComplying4DSS = new javax.swing.JLabel();
        jCheckBoxComplying4DSS = new javax.swing.JCheckBox();
        jLabelDeductibleDSS = new javax.swing.JLabel();
        jTextFieldDeductibleDSS = new javax.swing.JTextField();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new java.awt.GridBagLayout());

        jPanelName.setBorder(new javax.swing.border.TitledBorder("Name"));
        jLabelFundType.setText("Fund Type *");
        jLabelFundType.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelFundType.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelFundType, gridBagConstraints);

        jComboBoxFundType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxFundTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxFundType, gridBagConstraints);

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
        gridBagConstraints.gridx = 4;
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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
        jTextFieldPurchaseCost
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldPurchaseCostFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldPurchaseCost, gridBagConstraints);

        jLabelCurrentValue.setText("Current Value *");
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

        jLabelPurchaseDate.setText("Purchase Date");
        jLabelPurchaseDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelPurchaseDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelValue.add(jLabelPurchaseDate, gridBagConstraints);

        jTextFieldPurchaseDate
                .setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldPurchaseDate
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldPurchaseDate.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelValue.add(jTextFieldPurchaseDate, gridBagConstraints);

        jLabelMaturityDate.setText("Maturity Date");
        jLabelMaturityDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelMaturityDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelValue.add(jLabelMaturityDate, gridBagConstraints);

        jTextFieldMaturityDate
                .setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelValue.add(jTextFieldMaturityDate, gridBagConstraints);

        add(jPanelValue);

        jPanelPerformance.setLayout(new java.awt.GridBagLayout());

        jPanelPerformance.setBorder(new javax.swing.border.TitledBorder(
                "Performance"));
        jLabelEarnings.setText("Earnings (%)");
        jLabelEarnings.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelEarnings.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelEarnings, gridBagConstraints);

        jTextFieldEarnings
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldEarnings.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldEarnings.setInputVerifier(PercentInputVerifier.getInstance());
        jTextFieldEarnings.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jTextFieldEarnings, gridBagConstraints);

        jLabelCapitalGrowth11.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCapitalGrowth11.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelCapitalGrowth11, gridBagConstraints);

        jLabelCapitalGrowth1.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCapitalGrowth1.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelPerformance.add(jLabelCapitalGrowth1, gridBagConstraints);

        add(jPanelPerformance);

        jPanelFees.setLayout(new java.awt.GridBagLayout());

        jPanelFees.setBorder(new javax.swing.border.TitledBorder("Fees"));
        jLabelUpfrontFee.setText("Up front Fee (% p.a.)");
        jLabelUpfrontFee.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelUpfrontFee.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jLabelUpfrontFee, gridBagConstraints);

        jTextFieldUpfrontFee
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldUpfrontFee.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldUpfrontFee.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldUpfrontFee.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jTextFieldUpfrontFee, gridBagConstraints);

        jLabelOngoingFee.setText("On going Fee (% p.a.)");
        jLabelOngoingFee.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOngoingFee.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelFees.add(jLabelOngoingFee, gridBagConstraints);

        jTextFieldOngoingFee
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldOngoingFee.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFees.add(jTextFieldOngoingFee, gridBagConstraints);

        add(jPanelFees);

        jPanelCashflow.setLayout(new java.awt.GridBagLayout());

        jPanelCashflow
                .setBorder(new javax.swing.border.TitledBorder("Cashflow"));
        jLabelAnnualPayment.setText("Annual Payment");
        jLabelAnnualPayment.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAnnualPayment.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelAnnualPayment, gridBagConstraints);

        jTextFieldAnnualPayment
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAnnualPayment
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldAnnualPayment.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldAnnualPayment.setMinimumSize(new java.awt.Dimension(120, 19));
        jTextFieldAnnualPayment
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldPurchaseCostFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldAnnualPayment, gridBagConstraints);

        jLabelFrequency.setText("Frequency");
        jLabelFrequency.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelFrequency.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelFrequency, gridBagConstraints);

        jComboBoxFrequency.setPreferredSize(new java.awt.Dimension(120, 19));
        jComboBoxFrequency.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jComboBoxFrequency, gridBagConstraints);

        jLabelIndexation.setText("Indexation (%)");
        jLabelIndexation.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelIndexation.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelIndexation, gridBagConstraints);

        jTextFieldIndexation
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIndexation.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCashflow.add(jTextFieldIndexation, gridBagConstraints);

        add(jPanelCashflow);

        jPanelOther.setLayout(new java.awt.GridBagLayout());

        jPanelOther.setBorder(new javax.swing.border.TitledBorder("Other"));
        jLabelDeductibleTax.setText("Deductible Tax");
        jLabelDeductibleTax.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDeductibleTax.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOther.add(jLabelDeductibleTax, gridBagConstraints);

        jTextFieldDeductibleTax.setToolTipText("Deductible Amount (Payment)");
        jTextFieldDeductibleTax
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldDeductibleTax
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldDeductibleTax.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        jTextFieldDeductibleTax.setMinimumSize(new java.awt.Dimension(120, 19));
        jTextFieldDeductibleTax
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldPurchaseCostFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOther.add(jTextFieldDeductibleTax, gridBagConstraints);

        jLabelTaxedIncome.setText("Taxed Income");
        jLabelTaxedIncome.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelTaxedIncome.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOther.add(jLabelTaxedIncome, gridBagConstraints);

        jTextFieldTaxedIncome.setToolTipText("Subject to Tax");
        jTextFieldTaxedIncome
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTaxedIncome.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelOther.add(jTextFieldTaxedIncome, gridBagConstraints);

        jLabelRebate.setText("Rebate (max 15 %)");
        jLabelRebate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelRebate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelOther.add(jLabelRebate, gridBagConstraints);

        jTextFieldRebate.setToolTipText("% rebate is to apply ?");
        jTextFieldRebate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldRebate.setInputVerifier(PercentInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelOther.add(jTextFieldRebate, gridBagConstraints);

        jLabelComplying4DSS.setText("Centrelink Complying");
        jLabelComplying4DSS.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelComplying4DSS.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelOther.add(jLabelComplying4DSS, gridBagConstraints);

        jCheckBoxComplying4DSS.setToolTipText("Complying for Social Security");
        jCheckBoxComplying4DSS
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jCheckBoxComplying4DSS.setMinimumSize(new java.awt.Dimension(120, 19));
        jCheckBoxComplying4DSS.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelOther.add(jCheckBoxComplying4DSS, gridBagConstraints);

        jLabelDeductibleDSS.setText("Centrelink Deductible");
        jLabelDeductibleDSS.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDeductibleDSS.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelOther.add(jLabelDeductibleDSS, gridBagConstraints);

        jTextFieldDeductibleDSS
                .setToolTipText("Deductible Amount (Centrelink)");
        jTextFieldDeductibleDSS
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldDeductibleDSS.setInputVerifier(CurrencyInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelOther.add(jTextFieldDeductibleDSS, gridBagConstraints);

        add(jPanelOther);

    }// GEN-END:initComponents

    private void jComboBoxFundTypeItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jComboBoxFundTypeItemStateChanged
        jCheckBoxComplying4DSS.setSelected(FundType.rcANNUITY_LONG
                .equals(jComboBoxFundType.getSelectedItem()));
    }// GEN-LAST:event_jComboBoxFundTypeItemStateChanged

    private void jTextFieldUnitsSharesPriceFocusLost(
            java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldUnitsSharesPriceFocusLost
        unitSharePriceChanged(evt.getComponent().getAccessibleContext()
                .getAccessibleName());
    }// GEN-LAST:event_jTextFieldUnitsSharesPriceFocusLost

    private void jTextFieldPurchaseCostFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldPurchaseCostFocusLost
        updateTaxedIncome();
    }// GEN-LAST:event_jTextFieldPurchaseCostFocusLost

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
    private javax.swing.JLabel jLabelFrequency;

    private com.argus.bean.FDateChooser jTextFieldPurchaseDate;

    private javax.swing.JLabel jLabelIndexation;

    private javax.swing.JTextField jTextFieldUnitsSharesPrice;

    private javax.swing.JTextField jTextFieldRebate;

    private javax.swing.JTextField jTextFieldAssetInvestmentName;

    private javax.swing.JLabel jLabelCurrentValue;

    private javax.swing.JTextField jTextFieldIndexation;

    private javax.swing.JLabel jLabelAssetInvestmentName;

    private javax.swing.JLabel jLabelDeductibleDSS;

    private javax.swing.JComboBox jComboBoxFundType;

    private javax.swing.JComboBox jComboBoxService;

    private javax.swing.JTextField jTextFieldAssetInvestmentCode;

    private javax.swing.JLabel jLabelOngoingFee;

    private javax.swing.JPanel jPanelFees;

    private javax.swing.JButton jButtonSearch;

    private javax.swing.JLabel jLabelUpfrontFee;

    private javax.swing.JLabel jLabelService;

    private javax.swing.JLabel jLabelDesc;

    private javax.swing.JComboBox jComboBoxFrequency;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JTextField jTextFieldOngoingFee;

    private javax.swing.JLabel jLabelDeductibleTax;

    private javax.swing.JLabel jLabelAssetInvestmentCode;

    private javax.swing.JLabel jLabelAnnualPayment;

    private javax.swing.JLabel jLabelPriceDate;

    private javax.swing.JLabel jLabelMaturityDate;

    private javax.swing.JLabel jLabelFundType;

    private javax.swing.JLabel jLabelUnitsSharesPrice;

    private javax.swing.JLabel jLabelComplying4DSS;

    private javax.swing.JLabel jLabelTaxedIncome;

    private javax.swing.JCheckBox jCheckBoxComplying4DSS;

    private javax.swing.JPanel jPanelOther;

    private javax.swing.JPanel jPanelValue;

    private javax.swing.JTextField jTextFieldDeductibleDSS;

    private javax.swing.JLabel jLabelOwnerCode;

    private com.argus.bean.FDateChooser jTextFieldPriceDate;

    private javax.swing.JTextField jTextFieldCurrentValue;

    private javax.swing.JLabel jLabelUnitShares;

    private javax.swing.JTextField jTextFieldDeductibleTax;

    private javax.swing.JLabel jLabelAccountNumber;

    private javax.swing.JTextField jTextFieldUnitShares;

    private javax.swing.JTextField jTextFieldAnnualPayment;

    private javax.swing.JTextField jTextFieldDesc;

    private javax.swing.JPanel jPanelPerformance;

    private javax.swing.JLabel jLabelCapitalGrowth1;

    private javax.swing.JLabel jLabelRebate;

    private javax.swing.JComboBox jComboBoxOwnerCode;

    private javax.swing.JLabel jLabelPurchaseCost;

    private com.argus.bean.FDateChooser jTextFieldMaturityDate;

    private javax.swing.JTextField jTextFieldEarnings;

    private javax.swing.JLabel jLabelPurchaseDate;

    private javax.swing.JLabel jLabelCapitalGrowth11;

    private javax.swing.JLabel jLabelInstitutionName;

    private javax.swing.JPanel jPanelCashflow;

    private javax.swing.JTextField jTextFieldPurchaseCost;

    private javax.swing.JTextField jTextFieldTaxedIncome;

    private javax.swing.JTextField jTextFieldUpfrontFee;

    private javax.swing.JTextField jTextFieldInstitutionName;

    private javax.swing.JTextField jTextFieldAccountNumber;

    private javax.swing.JLabel jLabelEarnings;

    // End of variables declaration//GEN-END:variables

    public IncomeStream getIncomeStream() {
        if (getObject() == null)
            setObject(new IncomeStream());
        return (IncomeStream) getObject();
    }

    public Integer getObjectType() {
        return IncomeStream.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_INCOME_STREAM.getDescription();
    }

    public boolean updateView() {

        if (!super.updateView())
            return false;

        Currency money = getCurrencyInstance();
        Percent percent = getPercentInstance();
        Number2 number = getNumberInstance();

        IncomeStream incomeStream = getIncomeStream();

        // ReferenceCode financialType = incomeStream.getFinancialType();
        ICode financialCode = incomeStream.getFinancialCode();
        old_FinancialCode = financialCode;

        jTextFieldAssetInvestmentCode.setText(financialCode == null ? null
                : financialCode.getCode());
        // jTextFieldAssetInvestmentCode.updateUI();
        jTextFieldAssetInvestmentName.setText(financialCode == null ? null
                : financialCode.getDescription());
        // jTextFieldAssetInvestmentName.updateUI();

        // jTextFieldCapitalGrowth.setText( number.toString(
        // incomeStream.getCapitalGrowth() ) );

        jComboBoxFundType.setSelectedItem(incomeStream.getFundType());

        jTextFieldInstitutionName.setText(incomeStream.getInstitution());

        jComboBoxService.setSelectedItem(incomeStream.getFinancialService());

        jTextFieldDesc.setText(incomeStream.getFinancialDesc());

        jComboBoxOwnerCode.setSelectedItem(incomeStream.getOwner());

        jTextFieldAccountNumber.setText(incomeStream.getAccountNumber());

        jTextFieldMaturityDate.setText(DateTimeUtils.asString(incomeStream
                .getEndDate()));

        jTextFieldUnitShares.setText(getNumberInstance4().toString(
                incomeStream.getUnitsShares()));
        jTextFieldUnitShares.getInputVerifier().verify(jTextFieldUnitShares);

        jTextFieldUnitsSharesPrice.setText(getCurrencyInstance4().toString(
                incomeStream.getUnitsSharesPrice()));
        jTextFieldUnitsSharesPrice.getInputVerifier().verify(
                jTextFieldUnitsSharesPrice);

        jTextFieldCurrentValue
                .setText(money.toString(incomeStream.getAmount()));
        java.util.Date date = incomeStream.getPriceDate();
        jTextFieldPriceDate.setText(date == null ? null : DateTimeUtils
                .asString(date));

        date = incomeStream.getStartDate();
        jTextFieldPurchaseDate.setText(date == null ? null : DateTimeUtils
                .asString(date, null));

        jTextFieldPurchaseCost.setText(money.toString(incomeStream
                .getPurchaseCost()));

        jTextFieldUpfrontFee.setText(percent.toString(incomeStream
                .getUpfrontFee()));
        jTextFieldOngoingFee.setText(percent.toString(incomeStream
                .getOngoingFee()));

        jTextFieldAnnualPayment.setText(money.toString(incomeStream
                .getDrawdownAnnualAmount()));

        jComboBoxFrequency.setSelectedItem(FrequencyCode.getCode(incomeStream
                .getFrequencyCodeID()));

        jTextFieldDeductibleTax.setText(money.toString(incomeStream
                .getDeductible()));

        jTextFieldIndexation.setText(percent.toString(incomeStream
                .getIndexation()));

        jTextFieldEarnings.setText(percent.toString(incomeStream.getIncome()));

        jTextFieldRebate
                .setText(percent.toString(incomeStream.getRebateable()));

        jCheckBoxComplying4DSS.setSelected(incomeStream.isComplyingForDSS());

        jTextFieldDeductibleDSS.setText(money.toString(incomeStream
                .getDeductibleDSS()));

        updateTaxedIncome();

        // update current units/shares price-fields
        // updateCurrentUnitsSharesPrice( financialCode );

        return true;

    }

    private void updateTaxedIncome() {
        Currency curr = getCurrencyInstance();
        com.argus.math.Money money = new com.argus.math.Money();
        java.math.BigDecimal amount = IncomeStream.getTaxedIncome(curr
                .getBigDecimalValue(jTextFieldAnnualPayment.getText()), curr
                .getBigDecimalValue(jTextFieldDeductibleTax.getText()));
        jTextFieldTaxedIncome.setText(money.toString(amount));
    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {

        String msg = "";

        Currency money = getCurrencyInstance();

        if (jComboBoxFundType.getSelectedIndex() <= 0)
            msg += "Fund Type is required.\n";

        String s = jTextFieldAssetInvestmentName.getText();
        if (s == null || s.trim().length() == 0)
            msg += "Asset Name is required.\n";

        Integer ownerCodeID = new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem());
        if (ownerCodeID == null)
            msg += "Owner is required.\n";

        java.math.BigDecimal amount = money
                .getBigDecimalValue(jTextFieldCurrentValue.getText());
        if (amount == null)
            msg += "Current Value is required.\n";

        amount = money.getBigDecimalValue(jTextFieldRebate.getText());
        double rebateAmount = amount == null ? 0. : amount.doubleValue();
        if (rebateAmount > 15.0 || rebateAmount < 0.00)
            msg += "Rebate percent must be positive and can not exceed 15 % .\n";

        msg += checkDateField(jTextFieldPriceDate, "Price Date");
        msg += checkDateField(jTextFieldMaturityDate, "Maturity Date");
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

        IncomeStream incomeStream = getIncomeStream();

        incomeStream.setRebateable(number.getBigDecimalValue(jTextFieldRebate
                .getText()));
        // incomeStream.setCapitalGrowth( number.getBigDecimalValue(
        // jTextFieldCapitalGrowth.getText() ) );

        ReferenceCode refCode = (ReferenceCode) jComboBoxFundType
                .getSelectedItem();
        incomeStream.setFundTypeID(refCode.getCodeId());

        try {
            updateFinancialCode(incomeStream, jTextFieldAssetInvestmentName
                    .getText().trim(), jTextFieldAssetInvestmentCode.getText()
                    .trim());

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new InvalidCodeException(e.getMessage());
        }

        incomeStream.setInstitution(jTextFieldInstitutionName.getText());

        incomeStream.setFinancialService((ReferenceCode) jComboBoxService
                .getSelectedItem());

        incomeStream.setFinancialDesc(jTextFieldDesc.getText());

        incomeStream.setOwnerCodeID(new OwnerCode()
                .getCodeID(jComboBoxOwnerCode.getSelectedItem().toString()));

        incomeStream.setAccountNumber(jTextFieldAccountNumber.getText());

        incomeStream.setEndDate(DateTimeUtils.getSqlDate(jTextFieldMaturityDate
                .getText()));

        incomeStream.setUnitsShares(getNumberInstance4().getDoubleValue(
                jTextFieldUnitShares.getText()));

        incomeStream.setAmount(money.getBigDecimalValue(jTextFieldCurrentValue
                .getText()));
        incomeStream.setUnitsSharesPrice(getCurrencyInstance4()
                .getBigDecimalValue(jTextFieldUnitsSharesPrice.getText()));
        incomeStream.setPriceDate(DateTimeUtils.getSqlDate(jTextFieldPriceDate
                .getText()));

        incomeStream.setUpfrontFee(percent
                .getBigDecimalValue(jTextFieldUpfrontFee.getText()));
        incomeStream.setOngoingFee(percent
                .getBigDecimalValue(jTextFieldOngoingFee.getText()));

        refCode = (ReferenceCode) jComboBoxFrequency.getSelectedItem();
        incomeStream.setFrequencyCodeID(refCode.getCodeId());

        incomeStream.setStartDate(DateTimeUtils
                .getSqlDate(jTextFieldPurchaseDate.getText()));

        incomeStream.setPurchaseCost(money
                .getBigDecimalValue(jTextFieldPurchaseCost.getText()));

        incomeStream.setDrawdownAnnualAmount(money
                .getBigDecimalValue(jTextFieldAnnualPayment.getText()));

        incomeStream.setDeductible(money
                .getBigDecimalValue(jTextFieldDeductibleTax.getText()));

        incomeStream.setIndexation(percent
                .getBigDecimalValue(jTextFieldIndexation.getText()));

        incomeStream.setIncome(percent.getBigDecimalValue(jTextFieldEarnings
                .getText()));

        incomeStream.setRebateable(percent.getBigDecimalValue(jTextFieldRebate
                .getText()));

        incomeStream.setComplyingForDSS(jCheckBoxComplying4DSS.isSelected());

        incomeStream.setDeductibleDSS(money
                .getBigDecimalValue(jTextFieldDeductibleDSS.getText()));

        return true;

    }

}
