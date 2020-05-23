/*
 * SaveProjectionDialog.java
 *
 * Created on July 1, 2002, 3:47 PM
 */

package au.com.totemsoft.myplanner.swing.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.Component;

import javax.swing.JOptionPane;

import au.com.totemsoft.myplanner.etc.DuplicateException;
import au.com.totemsoft.myplanner.etc.ModelTitleRestrictionException;
import au.com.totemsoft.myplanner.projection.MoneyCalc;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.swing.IMenuCommand;

public class SaveProjectionDialog extends au.com.totemsoft.myplanner.swing.InputDialog {

    private static SaveProjectionDialog dialog;

    /** Creates new form InputDialog */
    protected SaveProjectionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

    }

    public static SaveProjectionDialog getSaveProjectionInstance() {
        if (dialog == null)
            dialog = new SaveProjectionDialog(null, true);
        return dialog;
    }

    /***************************************************************************
     * parentComponent - can be null RETURN: OK = OK_OPTION Cancel =
     * CANCEL_OPTION
     **************************************************************************/
    public static int save(MoneyCalc calc,
            Component parentComponent)
            throws au.com.totemsoft.myplanner.etc.DuplicateException,
            ModelTitleRestrictionException {
        if (!calc.getSaved())
            return CANCEL_OPTION;

        String oldTitle = calc.getModel().getTitle();
        if (oldTitle != null && oldTitle.trim().length() > 0)
            return OK_OPTION; // already saved, has valid name

        // just created
        SaveProjectionDialog dlg = SaveProjectionDialog
                .getSaveProjectionInstance();

        dlg.setTitle("Save " + calc.getDefaultTitle() + " as ..");
        dlg.setSaveTitle(calc.getModel().getTitle());
        dlg.show();

        if (dlg.getResult() == CANCEL_OPTION)
            return CANCEL_OPTION;

        String newTitle = dlg.getSaveTitle();
        if (newTitle == null || newTitle.trim().length() == 0
                || newTitle.trim().equalsIgnoreCase(calc.getDefaultTitle())
                || newTitle.trim().equalsIgnoreCase(oldTitle)
                || newTitle.trim().equalsIgnoreCase(IMenuCommand.NEW.getSecond().toString())) {
            JOptionPane.showMessageDialog(parentComponent, "Invalid title",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return CANCEL_OPTION; // has to be new/not empty title
        }

        boolean saveAsNew = !newTitle.equalsIgnoreCase(oldTitle);
        if (saveAsNew) {
            Model m = calc.getModel();
            m.checkDuplicates(newTitle); // can throw DuplicateException

            if (m.getTitle() != null) // old, saved model (lets create new copy)
                m = new Model(calc.getModel());
            m.setTitle(newTitle);

            calc.setModel(m);

        }

        return OK_OPTION;

    }

    public static int saveAs(MoneyCalc calc,
            Component parentComponent)
            throws DuplicateException,
            ModelTitleRestrictionException {
        // if ( !calc.getSaved() )
        // return CANCEL_OPTION;
        String oldTitle = calc.getModel().getTitle();

        // just created
        SaveProjectionDialog dlg = SaveProjectionDialog
                .getSaveProjectionInstance();

        dlg.setTitle("Save " + calc.getDefaultTitle() + " as ..");
        dlg.setSaveTitle("");
        dlg.show();

        if (dlg.getResult() == CANCEL_OPTION)
            return CANCEL_OPTION;

        String newTitle = dlg.getSaveTitle();
        if (newTitle == null || newTitle.trim().length() == 0
                || newTitle.trim().equalsIgnoreCase(calc.getDefaultTitle())
                || newTitle.trim().equalsIgnoreCase(oldTitle)
                || newTitle.trim().equalsIgnoreCase(IMenuCommand.NEW.getSecond().toString())) {
            JOptionPane.showMessageDialog(parentComponent, "Invalid title",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return CANCEL_OPTION; // has to be new/not empty title
        }

        Model m = calc.getModel();
        m.checkDuplicates(newTitle); // can throw DuplicateException

        m = new Model();
        m.setTypeID(calc.getModel().getTypeID());
        m.setTitle(newTitle);

        calc.setModel(m);

        return OK_OPTION;

    }

}
