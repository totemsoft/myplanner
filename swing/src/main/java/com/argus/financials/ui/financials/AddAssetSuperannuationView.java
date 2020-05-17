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
import com.argus.financials.bean.AssetSuperannuation;
import com.argus.financials.bean.db.ApirPicBean;
import com.argus.financials.bean.db.IressAssetNameBean;
import com.argus.financials.code.FinancialServiceCode;
import com.argus.financials.code.FundType;
import com.argus.financials.code.OwnerCode;
import com.argus.financials.swing.CurrencyInputVerifier;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.NumberInputVerifier;
import com.argus.financials.swing.PercentInputVerifier;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

/* BEGIN: BUG-FIX 417 */

public class AddAssetSuperannuationView extends AddAssetView implements
        UpdateUnitSharePriceData {

    private static AddAssetSuperannuationView view;

    private boolean thisCTOR = false;

    /** Creates new form AddAssetSuperannuationView */
    public AddAssetSuperannuationView() {
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

    public static AddAssetSuperannuationView getInstance() {
        if (view == null)
            view = new AddAssetSuperannuationView();
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
        return getCurrencyInstance4().getBigDecimalValue(
                jTextFieldCurrentValue.getText());
    }

    public void setPriceDateValue(java.math.BigDecimal value) {
        jTextFieldCurrentValue.setText(value == null ? null
                : getCurrencyInstance4().toString(value));
        jTextFieldCurrentValue.getInputVerifier()
                .verify(jTextFieldCurrentValue);
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
        jComboBoxFundType = new javax.swing.JComboBox(new Object[] {
                ReferenceCode.CODE_NONE, FundType.rcSUPERANNUATION });
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
        jLabelCurrentValue = new javax.swing.JLabel();
        jTextFieldCurrentValue = new javax.swing.JTextField();
        jLabelPriceDate = new javax.swing.JLabel();
        jTextFieldPriceDate = new com.argus.bean.FDateChooser();
        jLabelPurchaseDate = new javax.swing.JLabel();
        jTextFieldPurchaseDate = new com.argus.bean.FDateChooser();
        jLabelMaturityDate = new javax.swing.JLabel();
        jTextFieldMaturityDate = new com.argus.bean.FDateChooser();
        jPanelPerformance = new javax.swing.JPanel();
        jLabelCapitalGrowth = new javax.swing.JLabel();
        jTextFieldCapitalGrowth = new javax.swing.JTextField();
        jLabelCapitalGrowth1 = new javax.swing.JLabel();
        jLabelCapitalGrowth11 = new javax.swing.JLabel();
        jPanelCashflow = new javax.swing.JPanel();
        jLabelTotalContributions = new javax.swing.JLabel();
        jTextFieldTotalAnnualContributions = new javax.swing.JTextField();
        jLabelPersonalContributions = new javax.swing.JLabel();
        jTextFieldAnnualContributions = new javax.swing.JTextField();
        jLabelIndexation = new javax.swing.JLabel();
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
        jLabelUpfrontFee = new javax.swing.JLabel();
        jTextFieldUpfrontFee = new javax.swing.JTextField();
        jLabelOngoingFee = new javax.swing.JLabel();
        jTextFieldOngoingFee = new javax.swing.JTextField();

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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
        gridBagConstraints.weightx = 1.0;
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
        jPanelValue.add(jTextFieldUnitShares, gridBagConstraints);

        jLabelUnitsSharesPrice.setText("Units/Shares Price");
        jLabelUnitsSharesPrice
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelUnitsSharesPrice.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
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

        jLabelCurrentValue.setText("Current Value *");
        jLabelCurrentValue.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCurrentValue.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
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
        jPanelValue.add(jTextFieldCurrentValue, gridBagConstraints);

        jLabelPriceDate.setText("Price Date");
        jLabelPriceDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelPriceDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelValue.add(jLabelPriceDate, gridBagConstraints);

        jTextFieldPriceDate.setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelValue.add(jTextFieldPurchaseDate, gridBagConstraints);

        jLabelMaturityDate.setText("Maturity Date");
        jLabelMaturityDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelMaturityDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanelValue.add(jLabelMaturityDate, gridBagConstraints);

        jTextFieldMaturityDate
                .setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelValue.add(jTextFieldMaturityDate, gridBagConstraints);

        add(jPanelValue);

        jPanelPerformance.setLayout(new java.awt.GridBagLayout());

        jPanelPerformance.setBorder(new javax.swing.border.TitledBorder(
                "Performance"));
        jLabelCapitalGrowth.setText("Total Return (% p.a.)");
        jLabelCapitalGrowth.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCapitalGrowth.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jTextFieldCapitalGrowth, gridBagConstraints);

        jLabelCapitalGrowth1.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCapitalGrowth1.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelPerformance.add(jLabelCapitalGrowth1, gridBagConstraints);

        jLabelCapitalGrowth11.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelCapitalGrowth11.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerformance.add(jLabelCapitalGrowth11, gridBagConstraints);

        add(jPanelPerformance);

        jPanelCashflow.setLayout(new java.awt.GridBagLayout());

        jPanelCashflow
                .setBorder(new javax.swing.border.TitledBorder("Cashflow"));
        jLabelTotalContributions.setText("Total Contributions");
        jLabelTotalContributions.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelTotalContributions
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelTotalContributions, gridBagConstraints);

        jTextFieldTotalAnnualContributions.setToolTipText("Subject to Tax");
        jTextFieldTotalAnnualContributions
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldTotalAnnualContributions
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldTotalAnnualContributions
                .setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldTotalAnnualContributions
                .setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldTotalAnnualContributions,
                gridBagConstraints);

        jLabelPersonalContributions.setText(", including personal");
        jLabelPersonalContributions.setPreferredSize(new java.awt.Dimension(
                120, 20));
        jLabelPersonalContributions.setMinimumSize(new java.awt.Dimension(120,
                20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelPersonalContributions, gridBagConstraints);

        jTextFieldAnnualContributions
                .setToolTipText("Deductible Amount (Payment)");
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldAnnualContributions, gridBagConstraints);

        jLabelIndexation.setText("Indexation (%)");
        jLabelIndexation.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelIndexation.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelIndexation, gridBagConstraints);

        jTextFieldContributionIndexation
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldContributionIndexation.setInputVerifier(PercentInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
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
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelContributionsEndDate, gridBagConstraints);

        jTextFieldContributionsEndDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCashflow.add(jTextFieldContributionsEndDate, gridBagConstraints);

        jLabelAnnualDrawdowns.setText("Annual Drawdowns");
        jLabelAnnualDrawdowns.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAnnualDrawdowns.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 4;
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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldDrawdownsStartDate, gridBagConstraints);

        jLabelDrawdownsEndDate.setText("End Date");
        jLabelDrawdownsEndDate
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDrawdownsEndDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jLabelDrawdownsEndDate, gridBagConstraints);

        jTextFieldDrawdownsEndDate.setInputVerifier(DateInputVerifier
                .getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCashflow.add(jTextFieldDrawdownsEndDate, gridBagConstraints);

        add(jPanelCashflow);

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

        jTextFieldUpfrontFee.setToolTipText("Subject to Tax");
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

        jTextFieldOngoingFee.setToolTipText("Deductible Amount (Payment)");
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

    }// GEN-END:initComponents

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

    private javax.swing.JLabel jLabelIndexation;

    private javax.swing.JTextField jTextFieldUnitsSharesPrice;

    private javax.swing.JTextField jTextFieldAssetInvestmentName;

    private javax.swing.JLabel jLabelContributionsStartDate;

    private javax.swing.JLabel jLabelCurrentValue;

    private javax.swing.JLabel jLabelAssetInvestmentName;

    private javax.swing.JTextField jTextFieldTotalAnnualContributions;

    private javax.swing.JComboBox jComboBoxFundType;

    private javax.swing.JComboBox jComboBoxService;

    private javax.swing.JTextField jTextFieldAssetInvestmentCode;

    private javax.swing.JLabel jLabelOngoingFee;

    private javax.swing.JPanel jPanelFees;

    private javax.swing.JButton jButtonSearch;

    private javax.swing.JLabel jLabelUpfrontFee;

    private javax.swing.JLabel jLabelAnnualDrawdowns;

    private javax.swing.JLabel jLabelService;

    private javax.swing.JLabel jLabelDesc;

    private javax.swing.JTextField jTextFieldContributionIndexation;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JTextField jTextFieldOngoingFee;

    private javax.swing.JLabel jLabelAssetInvestmentCode;

    private com.argus.bean.FDateChooser jTextFieldContributionsStartDate;

    private javax.swing.JLabel jLabelDrawdownsStartDate;

    private javax.swing.JLabel jLabelPriceDate;

    private javax.swing.JLabel jLabelMaturityDate;

    private javax.swing.JLabel jLabelFundType;

    private javax.swing.JLabel jLabelTotalContributions;

    private javax.swing.JLabel jLabelCapitalGrowth;

    private javax.swing.JLabel jLabelUnitsSharesPrice;

    private javax.swing.JLabel jLabelPersonalContributions;

    private com.argus.bean.FDateChooser jTextFieldDrawdownsEndDate;

    private com.argus.bean.FDateChooser jTextFieldDrawdownsStartDate;

    private javax.swing.JPanel jPanelValue;

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

    private javax.swing.JLabel jLabelCapitalGrowth1;

    private javax.swing.JTextField jTextFieldDrawdownIndexation;

    private javax.swing.JTextField jTextFieldCapitalGrowth;

    private javax.swing.JLabel jLabelContributionsEndDate;

    private javax.swing.JComboBox jComboBoxOwnerCode;

    private com.argus.bean.FDateChooser jTextFieldMaturityDate;

    private javax.swing.JLabel jLabelDrawdownsEndDate;

    private javax.swing.JLabel jLabelPurchaseDate;

    private javax.swing.JLabel jLabelCapitalGrowth11;

    private javax.swing.JLabel jLabelInstitutionName;

    private javax.swing.JPanel jPanelCashflow;

    private javax.swing.JTextField jTextFieldUpfrontFee;

    private javax.swing.JTextField jTextFieldInstitutionName;

    private javax.swing.JTextField jTextFieldAccountNumber;

    // End of variables declaration//GEN-END:variables

    public AssetSuperannuation getAssetSuperannuation() {
        if (getObject() == null)
            setObject(new AssetSuperannuation());
        return (AssetSuperannuation) getObject();
    }

    public Integer getObjectType() {
        return AssetSuperannuation.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_ASSET_SUPERANNUATION.getDescription();
    }

    public boolean updateView() {

        if (!super.updateView())
            return false;

        Currency money = getCurrencyInstance();
        Percent percent = getPercentInstance();
        Number2 number = getNumberInstance();

        AssetSuperannuation assetSuperannuation = getAssetSuperannuation();

        ICode financialType = assetSuperannuation.getFinancialType();
        ICode financialCode = assetSuperannuation.getFinancialCode();
        old_FinancialCode = financialCode;

        jTextFieldAssetInvestmentCode.setText(financialCode == null ? null
                : financialCode.getCode());
        jTextFieldAssetInvestmentName.setText(financialCode == null ? null
                : financialCode.getDescription());

        jTextFieldCapitalGrowth.setText(number.toString(assetSuperannuation
                .getCapitalGrowth()));

        jComboBoxFundType.setSelectedItem(assetSuperannuation.getFundType());

        jTextFieldInstitutionName.setText(assetSuperannuation.getInstitution());

        jComboBoxService.setSelectedItem(assetSuperannuation
                .getFinancialService());

        jTextFieldDesc.setText(assetSuperannuation.getFinancialDesc());

        jComboBoxOwnerCode.setSelectedItem(assetSuperannuation.getOwner());

        jTextFieldAccountNumber.setText(assetSuperannuation.getAccountNumber());

        jTextFieldUnitShares.setText(getNumberInstance4().toString(
                assetSuperannuation.getUnitsShares()));
        jTextFieldUnitShares.getInputVerifier().verify(jTextFieldUnitShares);

        jTextFieldUnitsSharesPrice.setText(getCurrencyInstance4().toString(
                assetSuperannuation.getUnitsSharesPrice()));
        jTextFieldUnitsSharesPrice.getInputVerifier().verify(
                jTextFieldUnitsSharesPrice);

        jTextFieldCurrentValue.setText(money.toString(assetSuperannuation
                .getAmount()));
        java.util.Date date = assetSuperannuation.getPriceDate();
        jTextFieldPriceDate.setText(date == null ? null : DateTimeUtils
                .asString(date));

        date = assetSuperannuation.getStartDate();
        if (date == null)
            jTextFieldPurchaseDate.setText(null);
        else
            jTextFieldPurchaseDate.setText(DateTimeUtils.asString(date, null));

        date = assetSuperannuation.getEndDate();
        if (date == null)
            jTextFieldMaturityDate.setText(null);
        else
            jTextFieldMaturityDate.setText(DateTimeUtils.asString(date, null));

        jTextFieldUpfrontFee.setText(percent.toString(assetSuperannuation
                .getUpfrontFee()));

        jTextFieldOngoingFee.setText(percent.toString(assetSuperannuation
                .getOngoingFee()));

        jTextFieldTotalAnnualContributions.setText(money
                .toString(assetSuperannuation.getAnnualAmount()));

        jTextFieldAnnualContributions.setText(money
                .toString(assetSuperannuation.getContributionAnnualAmount()));
        jTextFieldContributionIndexation.setText(percent
                .toString(assetSuperannuation.getContributionIndexation()));
        jTextFieldContributionsStartDate.setText(DateTimeUtils
                .asString(assetSuperannuation.getContributionStartDate()));
        jTextFieldContributionsEndDate.setText(DateTimeUtils
                .asString(assetSuperannuation.getContributionEndDate()));

        jTextFieldAnnualDrawdowns.setText(money.toString(assetSuperannuation
                .getDrawdownAnnualAmount()));
        jTextFieldDrawdownIndexation.setText(percent
                .toString(assetSuperannuation.getDrawdownIndexation()));
        jTextFieldDrawdownsStartDate.setText(DateTimeUtils
                .asString(assetSuperannuation.getDrawdownStartDate()));
        jTextFieldDrawdownsEndDate.setText(DateTimeUtils
                .asString(assetSuperannuation.getDrawdownEndDate()));

        // update current units/shares price-fields
        // updateCurrentUnitsSharesPrice( financialCode );

        return true;

    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {
        String msg = "";

        Currency money = getCurrencyInstance();

        if (jComboBoxFundType.getSelectedIndex() <= 0) {
            jComboBoxFundType.requestFocus();
            msg += "Fund Type is required.\n";
        }

        String s = jTextFieldAssetInvestmentName.getText();
        if (s == null || s.trim().length() == 0) {
            jTextFieldAssetInvestmentName.requestFocus();
            msg += "Asset Name is required.\n";
        }

        Integer ownerCodeID = new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem());
        if (ownerCodeID == null) {
            jComboBoxOwnerCode.requestFocus();
            msg += "Owner is required.\n";
        }

        java.math.BigDecimal amount = money
                .getBigDecimalValue(jTextFieldCurrentValue.getText());
        if (amount == null) {
            jTextFieldCurrentValue.requestFocus();
            msg += "Current Value is required.\n";
        }

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

        AssetSuperannuation assetSuperannuation = getAssetSuperannuation();

        assetSuperannuation.setCapitalGrowth(number
                .getBigDecimalValue(jTextFieldCapitalGrowth.getText()));

        ReferenceCode refCode = (ReferenceCode) jComboBoxFundType
                .getSelectedItem();
        assetSuperannuation.setFundTypeID(refCode.getCodeId());

        try {
            updateFinancialCode(assetSuperannuation,
                    jTextFieldAssetInvestmentName.getText().trim(),
                    jTextFieldAssetInvestmentCode.getText().trim());

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new InvalidCodeException(e.getMessage());
        }

        assetSuperannuation.setInstitution(jTextFieldInstitutionName.getText());

        assetSuperannuation
                .setFinancialService((ReferenceCode) jComboBoxService
                        .getSelectedItem());

        assetSuperannuation.setFinancialDesc(jTextFieldDesc.getText());

        assetSuperannuation.setOwnerCodeID(new OwnerCode()
                .getCodeID(jComboBoxOwnerCode.getSelectedItem().toString()));

        assetSuperannuation.setAccountNumber(jTextFieldAccountNumber.getText());

        assetSuperannuation.setStartDate(DateTimeUtils
                .getSqlDate(jTextFieldPurchaseDate.getText()));
        assetSuperannuation.setEndDate(DateTimeUtils
                .getSqlDate(jTextFieldMaturityDate.getText()));

        assetSuperannuation.setUnitsShares(getNumberInstance4().getDoubleValue(
                jTextFieldUnitShares.getText()));
        assetSuperannuation.setAmount(money
                .getBigDecimalValue(jTextFieldCurrentValue.getText()));
        assetSuperannuation.setUnitsSharesPrice(getCurrencyInstance4()
                .getBigDecimalValue(jTextFieldUnitsSharesPrice.getText()));
        assetSuperannuation.setPriceDate(DateTimeUtils
                .getSqlDate(jTextFieldPriceDate.getText()));

        assetSuperannuation.setUpfrontFee(percent
                .getBigDecimalValue(jTextFieldUpfrontFee.getText()));
        assetSuperannuation.setOngoingFee(percent
                .getBigDecimalValue(jTextFieldOngoingFee.getText()));

        assetSuperannuation.setAnnualAmount(money
                .getBigDecimalValue(jTextFieldTotalAnnualContributions
                        .getText()));

        assetSuperannuation.setContributionAnnualAmount(money
                .getBigDecimalValue(jTextFieldAnnualContributions.getText()));
        assetSuperannuation
                .setContributionIndexation(percent
                        .getBigDecimalValue(jTextFieldContributionIndexation
                                .getText()));
        assetSuperannuation.setContributionStartDate(DateTimeUtils
                .getSqlDate(jTextFieldContributionsStartDate.getText()));
        assetSuperannuation.setContributionEndDate(DateTimeUtils
                .getSqlDate(jTextFieldContributionsEndDate.getText()));

        assetSuperannuation.setDrawdownAnnualAmount(money
                .getBigDecimalValue(jTextFieldAnnualDrawdowns.getText()));
        assetSuperannuation.setDrawdownIndexation(percent
                .getBigDecimalValue(jTextFieldDrawdownIndexation.getText()));
        assetSuperannuation.setDrawdownStartDate(DateTimeUtils
                .getSqlDate(jTextFieldDrawdownsStartDate.getText()));
        assetSuperannuation.setDrawdownEndDate(DateTimeUtils
                .getSqlDate(jTextFieldDrawdownsEndDate.getText()));

        return true;

    }

}
