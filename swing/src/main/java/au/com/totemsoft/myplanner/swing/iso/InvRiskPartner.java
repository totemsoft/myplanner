/*
 * InvRiskPartner.java
 *
 * Created on 10 October 2001, 11:31
 */

package au.com.totemsoft.myplanner.swing.iso;

import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.config.ViewSettings;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.swing.SwingUtil;

public class InvRiskPartner extends InvRisk {
    private static InvRiskPartner view;

    /** Creates new form InvRiskPartner */
    private InvRiskPartner() {
        super();
    }

    public static InvRiskPartner getPartnerInstance() {
        /*
         * if ( view == null ) view = new InvRiskPartner(); return view;
         */
        if (view != null) {
            au.com.totemsoft.swing.SwingUtil.setVisible(view, false);
        }

        view = new InvRiskPartner();

        return view;
    }

    public String getViewCaption() {
        return "Partner: Risk Evaluation/ISO";
    }

    /**
     * helper methods
     */
    public static void displayPartner(java.awt.event.FocusListener[] listeners) {

        InvRiskPartner view = InvRiskPartner.getPartnerInstance();

        SwingUtil.add2Frame(view, listeners, view.getViewCaption(),
                ViewSettings.getInstance().getViewImage(
                        view.getClass().getName()), true, true, true);

        try {
            PersonService partner = clientService.getPartner(true);
            if (partner != null) {
                view.updateView(partner);
            } else {
                // ??? display error message
            }
        } catch (ServiceException e) {
            e.printStackTrace(System.err);
        }

    }

}
