/*
 * AddTaxOffsetView.java
 *
 * Created on 9 November 2001, 10:06
 */

package com.argus.financials.ui.financials;

import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.code.FinancialTypeEnum;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.bean.Regular;
import com.argus.financials.bean.TaxOffset;
import com.argus.util.ReferenceCode;

public class AddTaxOffsetView extends AddRegularView {

    private static AddTaxOffsetView view;

    /** Creates new form AddTaxOffsetView */
    protected AddTaxOffsetView() {
        super();
        initComponents();
    }

    public static AddTaxOffsetView getInstance() {
        if (view == null)
            view = new AddTaxOffsetView();
        return view;
    }

    public static boolean exists() {
        return view != null;
    }

    private void initComponents() {
        jLabelType.setText("Tax Offset Type *");

        jLabelIndexation.setVisible(false);
        jTextFieldIndexation.setVisible(false);

        jLabelAssociatedAsset.setVisible(false);
        jComboBoxAssociatedAsset.setVisible(false);

        // jLabelTaxable.setText( "Tax Deductible" );
        jLabelTaxable.setVisible(false);
        jCheckBoxTaxable.setVisible(false);

        // Variables initialization
        // End of variables initialization
    }

    // Variables declaration - do not modify
    // End of variables declaration

    public boolean updateView() {

        if (!super.updateView())
            return false;

        return true;

    }

    protected void checkRequiredFields(boolean showMessage)
            throws InvalidCodeException {

        super.checkRequiredFields(showMessage);

        String msg = "";

        if (jComboBoxType.getSelectedIndex() <= 0)
            msg += "Tax Offset Type is required.\n";
        else {
            ReferenceCode refCode = (ReferenceCode) jComboBoxType.getSelectedItem();
            Integer financialTypeID = refCode.getCodeId();
            if (financialTypeID != null && financialTypeID == FinancialTypeEnum.TAXOFFSET_LOW_INCOME.getId())
                msg += "Tip!\n\n"
                    + "The dollar amount of the Low Income Tax Offset is automatically\n"
                    + "calculated in the Tax Analysis Module for qualifying Individuals.\n"
                    + "You are not required to enter any details here.\n"
                    + "Please select another Tax Offset or press Cancel.\n"
                 // + "\n\tAssistant Manager Technical Services\n"
                ;
        }

        if (msg.length() == 0)
            return;

        if (showMessage)
            javax.swing.JOptionPane.showMessageDialog(this, msg, "ERROR",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

        throw new InvalidCodeException(msg);

    }

    public boolean saveView() throws InvalidCodeException {

        checkRequiredFields(true);

        if (!super.saveView())
            return false;

        return true;

    }

    public Regular getRegular() {
        if (getObject() == null)
            setObject(new TaxOffset());
        return (Regular) getObject();
    }

    public Integer getObjectType() {
        return TaxOffset.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_TAX_OFFSET.getDescription();
    }

}
