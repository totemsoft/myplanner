/*
 * AddRegularExpenseView.java
 *
 * Created on 9 November 2001, 10:06
 */

package com.argus.financials.ui.financials;

import com.argus.financials.api.InvalidCodeException;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.bean.Regular;
import com.argus.financials.bean.RegularExpense;

public class AddRegularExpenseView extends AddRegularView {

    private static AddRegularExpenseView view;

    /** Creates new form AddRegularExpenseView */
    protected AddRegularExpenseView() {
        super();
        initComponents();
    }

    public static AddRegularExpenseView getInstance() {
        if (view == null)
            view = new AddRegularExpenseView();
        return view;
    }

    public static boolean exists() {
        return view != null;
    }

    private void initComponents() {
        jLabelType.setText("Expense Type *");
        jLabelTaxable.setText("Tax Deductible");

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
            msg += "Regular Expense Type is required.\n";

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
            setObject(new RegularExpense());
        return (Regular) getObject();
    }

    public Integer getObjectType() {
        return RegularExpense.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_REGULAR_EXPENSE.getDescription();
    }

}
