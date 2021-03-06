/*
 * AddIncomeStreamsView.java
 *
 * Created on 13 November 2002, 10:06
 */

package au.com.totemsoft.myplanner.swing.financials;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.Number2;
import au.com.totemsoft.format.Percent;
import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.api.code.FinancialTypeEnum;
import au.com.totemsoft.myplanner.bean.AssetCash;
import au.com.totemsoft.myplanner.bean.NegativeAmountException;
import au.com.totemsoft.myplanner.code.FinancialServiceCode;
import au.com.totemsoft.myplanner.code.Institution;
import au.com.totemsoft.myplanner.code.OwnerCode;
import au.com.totemsoft.swing.CurrencyInputVerifier;
import au.com.totemsoft.swing.DateInputVerifier;
import au.com.totemsoft.swing.PercentInputVerifier;
import au.com.totemsoft.util.DateTimeUtils;
import au.com.totemsoft.util.ReferenceCode;

public class AddAssetCashView extends AddFinancialView {

    private static AddAssetCashView view;

    private boolean thisCTOR = false;

    /** Creates new form AddIncomeStreamsView */
    public AddAssetCashView() {
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

    }

    protected int getDefaultHeight() {
        return 600;
    }

    public static AddAssetCashView getInstance() {
        if (view == null)
            view = new AddAssetCashView();
        return view;
    }

    public static boolean exists() {
        return view != null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelName = new javax.swing.JPanel();
        jLabelAssetType = new javax.swing.JLabel();
        jComboBoxAssetType = new javax.swing.JComboBox(financialService.findFinancialTypes(getObjectType()));
        jLabelInstitution = new javax.swing.JLabel();
        jComboBoxInstitution = new javax.swing.JComboBox(new Institution()
                .getCodeDescriptions());
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
        jLabelAmount = new javax.swing.JLabel();
        jTextFieldAmount = new javax.swing.JTextField();
        jLabelPurchaseDate = new javax.swing.JLabel();
        jTextFieldPurchaseDate = new au.com.totemsoft.bean.FDateChooser();
        jLabelMaturityDate = new javax.swing.JLabel();
        jTextFieldMaturityDate = new au.com.totemsoft.bean.FDateChooser();
        jPanelPerfomance = new javax.swing.JPanel();
        jLabelIncomeRate = new javax.swing.JLabel();
        jTextFieldIncomeRate = new javax.swing.JTextField();
        jLabelReinvest = new javax.swing.JLabel();
        jCheckBoxReinvest = new javax.swing.JCheckBox();
        jPanelCashflow = new javax.swing.JPanel();
        jLabelAnnualContributions = new javax.swing.JLabel();
        jTextFieldAnnualContributions = new javax.swing.JTextField();
        jLabelContributionIndexation = new javax.swing.JLabel();
        jTextFieldContributionIndexation = new javax.swing.JTextField();
        jLabelContributionsStartDate = new javax.swing.JLabel();
        jTextFieldContributionsStartDate = new au.com.totemsoft.bean.FDateChooser();
        jLabelContributionsEndDate = new javax.swing.JLabel();
        jTextFieldContributionsEndDate = new au.com.totemsoft.bean.FDateChooser();
        jLabelAnnualDrawdowns = new javax.swing.JLabel();
        jTextFieldAnnualDrawdowns = new javax.swing.JTextField();
        jLabelDrawdownIndexation = new javax.swing.JLabel();
        jTextFieldDrawdownIndexation = new javax.swing.JTextField();
        jLabelDrawdownsStartDate = new javax.swing.JLabel();
        jTextFieldDrawdownsStartDate = new au.com.totemsoft.bean.FDateChooser();
        jLabelDrawdownsEndDate = new javax.swing.JLabel();
        jTextFieldDrawdownsEndDate = new au.com.totemsoft.bean.FDateChooser();
        jPanelFees = new javax.swing.JPanel();
        jLabelUpfrontFee = new javax.swing.JLabel();
        jTextFieldUpfrontFee = new javax.swing.JTextField();
        jLabelOngoingFee = new javax.swing.JLabel();
        jTextFieldOngoingFee = new javax.swing.JTextField();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new java.awt.GridBagLayout());

        jPanelName.setBorder(new javax.swing.border.TitledBorder("Name"));
        jLabelAssetType.setText("Asset Type *");
        jLabelAssetType.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAssetType.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelAssetType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxAssetType, gridBagConstraints);

        jLabelInstitution.setText("Institution");
        jLabelInstitution.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelInstitution.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelInstitution, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxInstitution, gridBagConstraints);

        jLabelService.setText("Service");
        jLabelService.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelService.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelService, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jComboBoxService, gridBagConstraints);

        jLabelOwnerCode.setText("Owner *");
        jLabelOwnerCode.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOwnerCode.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelOwnerCode, gridBagConstraints);

        jComboBoxOwnerCode.setPreferredSize(new java.awt.Dimension(120, 19));
        jComboBoxOwnerCode.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jComboBoxOwnerCode, gridBagConstraints);

        jLabelDesc.setText("Notes/Comments");
        jLabelDesc.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDesc.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelName.add(jLabelDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelName.add(jLabelAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelName.add(jTextFieldAccountNumber, gridBagConstraints);

        add(jPanelName);

        jPanelValue.setLayout(new java.awt.GridBagLayout());

        jPanelValue.setBorder(new javax.swing.border.TitledBorder("Value"));
        jLabelAmount.setText("Current Value *");
        jLabelAmount.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelAmount.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelAmount, gridBagConstraints);

        jTextFieldAmount
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAmount.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldAmount.setInputVerifier(CurrencyInputVerifier.getInstance());
        jTextFieldAmount.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldAmount, gridBagConstraints);

        jLabelPurchaseDate.setText("Purchase Date");
        jLabelPurchaseDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelPurchaseDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelPurchaseDate, gridBagConstraints);

        jTextFieldPurchaseDate
                .setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldPurchaseDate
                .setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldPurchaseDate.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jTextFieldPurchaseDate, gridBagConstraints);

        jLabelMaturityDate.setText("Maturity Date");
        jLabelMaturityDate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelMaturityDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelValue.add(jLabelMaturityDate, gridBagConstraints);

        jTextFieldMaturityDate
                .setInputVerifier(DateInputVerifier.getInstance());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelValue.add(jTextFieldMaturityDate, gridBagConstraints);

        add(jPanelValue);

        jPanelPerfomance.setLayout(new java.awt.GridBagLayout());

        jPanelPerfomance.setBorder(new javax.swing.border.TitledBorder(
                "Performance"));
        jLabelIncomeRate.setText("Total Return (% p.a.)");
        jLabelIncomeRate.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelIncomeRate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerfomance.add(jLabelIncomeRate, gridBagConstraints);

        jTextFieldIncomeRate
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldIncomeRate.setPreferredSize(new java.awt.Dimension(120, 19));
        jTextFieldIncomeRate.setInputVerifier(PercentInputVerifier
                .getInstance());
        jTextFieldIncomeRate.setMinimumSize(new java.awt.Dimension(120, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerfomance.add(jTextFieldIncomeRate, gridBagConstraints);

        jLabelReinvest.setText("Reinvest");
        jLabelReinvest.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelReinvest.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelPerfomance.add(jLabelReinvest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelPerfomance.add(jCheckBoxReinvest, gridBagConstraints);

        add(jPanelPerfomance);

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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jLabelAnnualContributions, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jTextFieldAnnualContributions, gridBagConstraints);

        jLabelContributionIndexation.setText("Indexation (% p.a.)");
        jLabelContributionIndexation.setPreferredSize(new java.awt.Dimension(
                120, 20));
        jLabelContributionIndexation.setMinimumSize(new java.awt.Dimension(120,
                20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jLabelContributionIndexation, gridBagConstraints);

        jTextFieldContributionIndexation
                .setToolTipText("Indexation of Contributions");
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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jLabelAnnualDrawdowns, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelCashflow.add(jTextFieldAnnualDrawdowns, gridBagConstraints);

        jLabelDrawdownIndexation.setText("Indexation (% p.a.)");
        jLabelDrawdownIndexation.setPreferredSize(new java.awt.Dimension(120,
                20));
        jLabelDrawdownIndexation
                .setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelCashflow.add(jTextFieldDrawdownsStartDate, gridBagConstraints);

        jLabelDrawdownsEndDate.setText("End Date");
        jLabelDrawdownsEndDate
                .setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelDrawdownsEndDate.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
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
        jLabelUpfrontFee.setText("Up front Fee (% p.a.)");
        jLabelUpfrontFee.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelUpfrontFee.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanelFees.add(jTextFieldUpfrontFee, gridBagConstraints);

        jLabelOngoingFee.setText("On going Fee (% p.a.)");
        jLabelOngoingFee.setPreferredSize(new java.awt.Dimension(120, 20));
        jLabelOngoingFee.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
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
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFees.add(jTextFieldOngoingFee, gridBagConstraints);

        add(jPanelFees);

    }// GEN-END:initComponents

    // overrite Container method to allow design time UI development
    public void add(java.awt.Component comp, Object constraints) {
        jPanelDetails.add(comp, constraints);
    }

    public java.awt.Component add(java.awt.Component comp) {
        return thisCTOR ? jPanelDetails.add(comp) : super.add(comp);
        // return jPanelDetails.add( comp ); // ??? called from base class ctor
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private au.com.totemsoft.bean.FDateChooser jTextFieldPurchaseDate;

    private javax.swing.JComboBox jComboBoxAssetType;

    private javax.swing.JLabel jLabelContributionsStartDate;

    private javax.swing.JLabel jLabelAmount;

    private javax.swing.JComboBox jComboBoxService;

    private javax.swing.JCheckBox jCheckBoxReinvest;

    private javax.swing.JLabel jLabelOngoingFee;

    private javax.swing.JPanel jPanelFees;

    private javax.swing.JLabel jLabelUpfrontFee;

    private javax.swing.JLabel jLabelService;

    private javax.swing.JLabel jLabelAnnualDrawdowns;

    private javax.swing.JLabel jLabelDesc;

    private javax.swing.JTextField jTextFieldContributionIndexation;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JLabel jLabelIncomeRate;

    private javax.swing.JTextField jTextFieldOngoingFee;

    private au.com.totemsoft.bean.FDateChooser jTextFieldContributionsStartDate;

    private javax.swing.JLabel jLabelDrawdownsStartDate;

    private javax.swing.JPanel jPanelPerfomance;

    private javax.swing.JLabel jLabelMaturityDate;

    private javax.swing.JLabel jLabelContributionIndexation;

    private au.com.totemsoft.bean.FDateChooser jTextFieldDrawdownsEndDate;

    private au.com.totemsoft.bean.FDateChooser jTextFieldDrawdownsStartDate;

    private javax.swing.JPanel jPanelValue;

    private javax.swing.JLabel jLabelOwnerCode;

    private javax.swing.JLabel jLabelDrawdownIndexation;

    private javax.swing.JTextField jTextFieldAnnualDrawdowns;

    private javax.swing.JLabel jLabelAccountNumber;

    private au.com.totemsoft.bean.FDateChooser jTextFieldContributionsEndDate;

    private javax.swing.JTextField jTextFieldDesc;

    private javax.swing.JComboBox jComboBoxInstitution;

    private javax.swing.JTextField jTextFieldAnnualContributions;

    private javax.swing.JTextField jTextFieldDrawdownIndexation;

    private javax.swing.JTextField jTextFieldAmount;

    private javax.swing.JComboBox jComboBoxOwnerCode;

    private javax.swing.JLabel jLabelContributionsEndDate;

    private au.com.totemsoft.bean.FDateChooser jTextFieldMaturityDate;

    private javax.swing.JLabel jLabelAssetType;

    private javax.swing.JLabel jLabelAnnualContributions;

    private javax.swing.JLabel jLabelDrawdownsEndDate;

    private javax.swing.JLabel jLabelPurchaseDate;

    private javax.swing.JPanel jPanelCashflow;

    private javax.swing.JLabel jLabelInstitution;

    private javax.swing.JTextField jTextFieldUpfrontFee;

    private javax.swing.JTextField jTextFieldIncomeRate;

    private javax.swing.JTextField jTextFieldAccountNumber;

    private javax.swing.JLabel jLabelReinvest;

    // End of variables declaration//GEN-END:variables

    public boolean updateView() {

        if (!super.updateView())
            return false;

        AssetCash assetCash = getAssetCash();
        au.com.totemsoft.math.Money money = new au.com.totemsoft.math.Money();
        au.com.totemsoft.math.Percent percent = new au.com.totemsoft.math.Percent();

        jComboBoxAssetType.setSelectedItem(assetCash.getFinancialType());

        jComboBoxInstitution.setSelectedItem(assetCash.getInstitution());

        jComboBoxService.setSelectedItem(assetCash.getFinancialService());

        jComboBoxOwnerCode.setSelectedItem(assetCash.getOwner());

        jTextFieldAccountNumber.setText(assetCash.getAccountNumber());

        java.util.Date date = assetCash.getStartDate();
        if (date == null)
            jTextFieldPurchaseDate.setText(null);
        else
            jTextFieldPurchaseDate.setText(DateTimeUtils.asString(date, null));

        date = assetCash.getEndDate();
        if (date == null)
            jTextFieldMaturityDate.setText(null);
        else
            jTextFieldMaturityDate.setText(DateTimeUtils.asString(date, null));

        jTextFieldAmount.setText(money.toString(assetCash.getAmount()));

        jTextFieldIncomeRate.setText(percent.toString(assetCash.getIncome()));

        jTextFieldDesc.setText(assetCash.getFinancialDesc());

        jTextFieldUpfrontFee.setText(percent
                .toString(assetCash.getUpfrontFee()));
        jTextFieldOngoingFee.setText(percent
                .toString(assetCash.getOngoingFee()));

        jTextFieldAnnualContributions.setText(money.toString(assetCash
                .getContributionAnnualAmount()));
        jTextFieldContributionIndexation.setText(percent.toString(assetCash
                .getContributionIndexation()));
        jTextFieldContributionsStartDate.setText(DateTimeUtils
                .asString(assetCash.getContributionStartDate()));
        jTextFieldContributionsEndDate.setText(DateTimeUtils.asString(assetCash
                .getContributionEndDate()));

        jTextFieldAnnualDrawdowns.setText(money.toString(assetCash
                .getDrawdownAnnualAmount()));
        jTextFieldDrawdownIndexation.setText(percent.toString(assetCash
                .getDrawdownIndexation()));
        jTextFieldDrawdownsStartDate.setText(DateTimeUtils.asString(assetCash
                .getDrawdownStartDate()));
        jTextFieldDrawdownsEndDate.setText(DateTimeUtils.asString(assetCash
                .getDrawdownEndDate()));

        jCheckBoxReinvest.setSelected(assetCash.isReinvest());

        return true;

    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {
        String msg = "";

        Currency curr = getCurrencyInstance();

        if (jComboBoxAssetType.getSelectedIndex() <= 0)
            msg += "Asset Type is required.\n";
        else {
            ReferenceCode refCode = (ReferenceCode) jComboBoxAssetType
                    .getSelectedItem();
            Integer financialTypeID = refCode.getCodeId();
            if (financialTypeID == FinancialTypeEnum.TERM_DEPOSIT.getId()
                    || financialTypeID == FinancialTypeEnum.FIXED_DEPOSITS.getId())
                if (DateTimeUtils.getSqlDate(jTextFieldMaturityDate.getText()) == null)
                    msg += "Asset Type is entered without a maturity date.\n";
        }

        Integer ownerCodeID = new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem());
        if (ownerCodeID == null)
            msg += "Owner is required.\n";

        java.math.BigDecimal amount = curr.getBigDecimalValue(jTextFieldAmount
                .getText());
        if (amount == null)
            msg += "Amount is required.\n";

        msg += checkDateField(jTextFieldContributionsEndDate,
                "Contributions End Date");
        msg += checkDateField(jTextFieldContributionsStartDate,
                "Contributions Start Date");
        msg += checkDateField(jTextFieldDrawdownsEndDate, "Drawdowns End Date");
        msg += checkDateField(jTextFieldDrawdownsStartDate,
                "Drawdowns Start Date");
        msg += checkDateField(jTextFieldMaturityDate, "Maturity Date");
        msg += checkDateField(jTextFieldPurchaseDate, "Purchase Date");

        if (msg.length() == 0)
            return;

        if (showMessage)
            javax.swing.JOptionPane.showMessageDialog(this, msg, "ERROR",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        throw new InvalidCodeException(msg);

    }

    public boolean saveView() throws InvalidCodeException,
            NegativeAmountException {

        checkRequiredFields(true);

        Currency curr = getCurrencyInstance();
        Number2 number = getNumberInstance();
        Percent percent = getPercentInstance();

        AssetCash assetCash = getAssetCash();

        assetCash.setFinancialType((ReferenceCode) jComboBoxAssetType
                .getSelectedItem());

        assetCash.setInstitution((String) jComboBoxInstitution
                .getSelectedItem());

        assetCash.setFinancialService((ReferenceCode) jComboBoxService
                .getSelectedItem());

        assetCash.setOwnerCodeID(new OwnerCode()
                .getCodeID((String) jComboBoxOwnerCode.getSelectedItem()));

        assetCash.setAccountNumber(jTextFieldAccountNumber.getText());

        assetCash.setStartDate(DateTimeUtils.getSqlDate(jTextFieldPurchaseDate
                .getText()));
        assetCash.setEndDate(DateTimeUtils.getSqlDate(jTextFieldMaturityDate
                .getText()));

        assetCash
                .setAmount(curr.getBigDecimalValue(jTextFieldAmount.getText()));

        assetCash.setIncome(percent.getBigDecimalValue(jTextFieldIncomeRate
                .getText()));

        assetCash.setFinancialDesc(jTextFieldDesc.getText());

        assetCash.setUpfrontFee(percent.getBigDecimalValue(jTextFieldUpfrontFee
                .getText()));
        assetCash.setOngoingFee(percent.getBigDecimalValue(jTextFieldOngoingFee
                .getText()));

        assetCash.setContributionAnnualAmount(curr
                .getBigDecimalValue(jTextFieldAnnualContributions.getText()));
        assetCash
                .setContributionIndexation(percent
                        .getBigDecimalValue(jTextFieldContributionIndexation
                                .getText()));
        assetCash.setContributionStartDate(DateTimeUtils
                .getSqlDate(jTextFieldContributionsStartDate.getText()));
        assetCash.setContributionEndDate(DateTimeUtils
                .getSqlDate(jTextFieldContributionsEndDate.getText()));

        assetCash.setDrawdownAnnualAmount(curr
                .getBigDecimalValue(jTextFieldAnnualDrawdowns.getText()));
        assetCash.setDrawdownIndexation(percent
                .getBigDecimalValue(jTextFieldDrawdownIndexation.getText()));
        assetCash.setDrawdownStartDate(DateTimeUtils
                .getSqlDate(jTextFieldDrawdownsStartDate.getText()));
        assetCash.setDrawdownEndDate(DateTimeUtils
                .getSqlDate(jTextFieldDrawdownsEndDate.getText()));

        assetCash.setReinvest(jCheckBoxReinvest.isSelected());

        return true;

    }

    public AssetCash getAssetCash() {
        if (getObject() == null)
            setObject(new AssetCash());
        return (AssetCash) getObject();
    }

    public Integer getObjectType() {
        return AssetCash.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_ASSET_CASH.getDescription();
    }

}
