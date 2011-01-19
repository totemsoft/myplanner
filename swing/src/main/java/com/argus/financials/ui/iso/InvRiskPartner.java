/*
 * InvRiskPartner.java
 *
 * Created on 10 October 2001, 11:31
 */

package com.argus.financials.ui.iso;

/**
 * 
 * @author valeri chibaev
 */

import com.argus.financials.config.ViewSettings;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.service.client.ServiceException;
import com.argus.financials.swing.SwingUtil;

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
            com.argus.financials.swing.SwingUtil.setVisible(view, false);
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
            ClientService client = ServiceLocator.getInstance()
                    .getClientPerson();
            PersonService partner = client.getPartner(true);

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
