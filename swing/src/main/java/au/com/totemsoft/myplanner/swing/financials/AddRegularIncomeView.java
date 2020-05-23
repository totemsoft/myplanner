/*
 * AddRegularIncomeView.java
 *
 * Created on 9 November 2001, 10:21
 */

package au.com.totemsoft.myplanner.swing.financials;

import au.com.totemsoft.myplanner.api.InvalidCodeException;
import au.com.totemsoft.myplanner.bean.Regular;
import au.com.totemsoft.myplanner.bean.RegularIncome;

public class AddRegularIncomeView extends AddRegularView {

    private static AddRegularIncomeView view;

    /** Creates new form AddRegularIncomeView */
    protected AddRegularIncomeView() {
        super();
        initComponents();
        initComponents2();
    }

    public static AddRegularIncomeView getInstance() {
        if (view == null)
            view = new AddRegularIncomeView();
        else
            view.initComponents2();
        return view;
    }

    public static boolean exists() {
        return view != null;
    }

    private void initComponents2() {

    }

    private void initComponents() {
        jLabelType.setText("Income Type *");

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
            msg += "Regular Income Type is required.\n";

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
            setObject(new RegularIncome());
        return (Regular) getObject();
    }

    public Integer getObjectType() {
        return RegularIncome.OBJECT_TYPE_ID;
    }

    public String getTitle() {
        return RC_REGULAR_INCOME.getDescription();
    }

}
