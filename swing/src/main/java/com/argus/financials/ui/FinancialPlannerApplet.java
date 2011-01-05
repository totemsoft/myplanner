/*
 * MainApplet.java
 *
 * Created on 23 November 2001, 09:38
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.FocusManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.argus.financials.config.FPSLocale;
import com.argus.financials.config.PropertySourceManager;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.projection.ETPCalc;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.FinancialPlannerFocusManager;
import com.argus.financials.ui.iso.InvRisk;
import com.argus.financials.ui.login.UserLogin;
import com.argus.financials.ui.projection.AllocatedPensionView;
import com.argus.financials.ui.projection.ETPRolloverView;
import com.argus.financials.ui.projection.GearingView;
import com.argus.financials.ui.projection.SnapEntryView;

public class FinancialPlannerApplet extends javax.swing.JApplet implements
        ActionEventID {

    private static boolean DEBUG = false;

    private static FinancialPlannerApplet mainApplet;

    private java.awt.CardLayout cardLayout;

    private UserLogin userLogin;

    public static boolean exists() {
        return mainApplet != null;
    }

    public static String getViewCaption() {
        return "Fiducian Financial Planning";
    }

    public static FinancialPlannerApplet getMainApplet() {
        if (mainApplet == null)
            mainApplet = new FinancialPlannerApplet();
        return mainApplet;
    }

    /** Creates new form MainApplet */
    public FinancialPlannerApplet() {
        if (mainApplet == null)
            mainApplet = this;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        FocusManager.setCurrentManager(new FinancialPlannerFocusManager());

        initComponents();

    }

    public void init() {
        super.init();

        try {
            String source = getParameter("properties");
            if (source == null)
                source = "force2.properties"; // default for applet

            source = getCodeBase().toString() + source;
            System.out.println("source: " + source);

            PropertySourceManager.getInstance().load(source);
            FPSLocale r = FPSLocale.getInstance();
            DEBUG = Boolean.valueOf(System.getProperty("DEBUG")).booleanValue();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        initComponents2();
    }

    public void destroy() {
        UserLogin.getInstance().logout();

        // unload/store local configuration
        PropertySourceManager.getInstance().unload();

        super.destroy();
    }

    private void initComponents2() {
        cardLayout = (java.awt.CardLayout) jPanelCenter.getLayout();

        setJMenuBar(null);
        updateComponents();

        displayLogin();

    }

    private void displayLogin() {

        if (userLogin == null)
            userLogin = UserLogin.newInstance(true);

        jPanelCenter.add(userLogin, UserLogin.class.getName());
        // cardLayout.show( jPanelCenter, UserLogin.class.getName() );

        userLogin.setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equalsIgnoreCase("OK")) {

                    if (userLogin.login()) {

                        // remove login screen
                        jPanelCenter.remove(userLogin);

                        // setJMenuBar(mainMenu);
                        updateComponents();

                        jLabelUser.setText(userLogin.getUserName());

                        resize(getWidth() + 1, getHeight() + 1);

                        return;

                    }

                } else if (e.getActionCommand().equalsIgnoreCase("Cancel")) {

                }

                javax.swing.JLabel jLabelInfo = new javax.swing.JLabel();
                jLabelInfo
                        .setText("<html><body><h1>Failed to Login</h1>"
                                + "\u00a9Fiducian Financial Services"
                                + "<br><br><i><b>email:</b> valerichibaev@fiducian.com.au<i>"
                                + "</body></html>");
                jLabelInfo
                        .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jPanelCenter.add(jLabelInfo, "jLabelInfo");
                cardLayout.show(jPanelCenter, "jLabelInfo");

                stop();

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jPanelCenter = new javax.swing.JPanel();
        jPanelNorth = new javax.swing.JPanel();
        jPanelInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelServer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelClient = new javax.swing.JLabel();

        jPanelCenter.setLayout(new java.awt.CardLayout());

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelNorth.setPreferredSize(new java.awt.Dimension(10, 20));
        getContentPane().add(jPanelNorth, java.awt.BorderLayout.NORTH);

        jPanelInfo.setLayout(new java.awt.GridLayout(1, 6));

        jPanelInfo.setBorder(new javax.swing.border.EtchedBorder());
        jPanelInfo.setPreferredSize(new java.awt.Dimension(4, 20));
        jPanelInfo.setMinimumSize(new java.awt.Dimension(4, 12));
        jPanelInfo.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jLabel1.setText("Server");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelInfo.add(jLabel1);

        jLabelServer.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.LOWERED));
        jPanelInfo.add(jLabelServer);

        jLabel3.setText("User");
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelInfo.add(jLabel3);

        jLabelUser.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.LOWERED));
        jPanelInfo.add(jLabelUser);

        jLabel5.setText("ClientView");
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanelInfo.add(jLabel5);

        jLabelClient.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.LOWERED));
        jPanelInfo.add(jLabelClient);

        getContentPane().add(jPanelInfo, java.awt.BorderLayout.SOUTH);

    }// GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelCenter;

    private javax.swing.JPanel jPanelNorth;

    private javax.swing.JPanel jPanelInfo;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabelServer;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabelUser;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabelClient;

    // End of variables declaration//GEN-END:variables

    /**
     * events
     */
    private static int eventID;

    private void fireActionEvent(Integer actionID) {

        javax.swing.ActionMap actionMap = getActionMap();
        if (actionMap == null)
            return;

        javax.swing.Action action = (javax.swing.Action) (actionMap
                .get(actionID));
        if (action == null)
            return;

        action.actionPerformed(new java.awt.event.ActionEvent(this, ++eventID,
                this.getClass().getName()));

    }

    public javax.swing.ActionMap getActionMap() {
        return PlanTracker.getInstance().getActionMap();
    }

    /**
     * helper method
     */
    private void updateComponents() {

    }

    private void LookAndFeelActionPerformed(java.awt.event.ActionEvent evt) {
        Component c = (Component) evt.getSource();
        String s = c.getName();

        try {
            Class.forName(s);
            UIManager.setLookAndFeel(s);

            SwingUtilities.updateComponentTreeUI(this);

            // for ( int i = 0; i < this.getComponentCount(); i++ )
            // SwingUtilities.updateComponentTreeUI( this.getComponent(i) );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * user and client methods
     */
    private Integer getUserPersonID() {
        return ServiceLocator.getInstance().getUserPersonID();
    }

    private Integer getClientPersonID() {
        return ServiceLocator.getInstance().getClientPersonID();
    }

    /**
     * 
     */
    private void displayClientList() {
        /*
         * ClientSearch view = null;
         * 
         * if ( !ClientSearch.exists() ) { view = ClientSearch.getInstance();
         * 
         * view.setActionListener( new ActionListener() { public void
         * actionPerformed( ActionEvent e) {
         * 
         * ClientSearch view = ClientSearch.getInstance();
         * 
         * if ( e.getActionCommand().equalsIgnoreCase( "OK" ) ) { Integer id =
         * view.getSelectedPersonID(); if (id == null) return;
         * 
         * try { RmiParams.getInstance().setClientPersonID( id ); } catch
         * (Exception ex) { return; }
         * 
         * jLabelClient.setText( view.getClientName() ); updateComponents();
         *  }
         * 
         * ClientSearch.getInstance().setVisible( false );
         *  } } );
         * 
         * jPanelCenter.add( view, ClientSearch.class.getName() ); resize(
         * getWidth() + 1, getHeight() + 1 );
         *  }
         * 
         * cardLayout.show( jPanelCenter, ClientSearch.class.getName() );
         */
    }

    void displayClient() {

        if (getClientPersonID() == null)
            return;

        ClientView view = ClientView.getClientView();

        if (!getClientPersonID().equals(view.getPrimaryKey())) {
            jPanelCenter.add(view, ClientView.class.getName());
            resize(getWidth() + 1, getHeight() + 1);

            try {
                view.updateView(null);
            } catch (com.argus.financials.service.ServiceException re) {
                throw new RuntimeException(re.getMessage());
            }
        }

        cardLayout.show(jPanelCenter, ClientView.class.getName());

    }

    private void createNewClient() {
        displayClient();
    }

    private void displayClientPartner() {

        if (getClientPersonID() == null)
            return;

        PartnerView view = PartnerView.getPartnerView();

        if (!getClientPersonID().equals(view.getPrimaryKey())) {
            jPanelCenter.add(view, PartnerView.class.getName());
            resize(getWidth() + 1, getHeight() + 1);

            try {
                view.updateView(ServiceLocator.getInstance().getClientPerson()
                        .getPartner(true));
            } catch (com.argus.financials.service.ServiceException re) {
                throw new RuntimeException(re.getMessage());
            }
        }

        cardLayout.show(jPanelCenter, PartnerView.class.getName());

    }

    private void displayRiskEvaluation() {

        InvRisk view = InvRisk.getInstance();

        // if ( !getClientPersonID().equals( view.getPrimaryKey() ) ) {
        jPanelCenter.add(view, InvRisk.class.getName());
        resize(getWidth() + 1, getHeight() + 1);

        view.updateView(ServiceLocator.getInstance().getClientPerson());

        cardLayout.show(jPanelCenter, InvRisk.class.getName());

    }

    /**
     * Models and Projections
     */
    private void displaySnapshot(int quickViewID) {

        final SnapEntryView view = null;

        // if ( !SnapEntryView.exists() ) {
        // view = SnapEntryView.getInstance();
        // view.setObject( new smoothlogic.math.SnapshotCalc( view ) );

        view.setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equalsIgnoreCase("Close")) {
                    view.setVisible(false);
                } else if (e.getActionCommand().equalsIgnoreCase("Save")) {

                }
            }
        });

        jPanelCenter.add(view, SnapEntryView.class.getName());
        resize(getWidth() + 1, getHeight() + 1);

        // try { view.updateView( RmiParams.getClientPerson() ); }
        // catch (com.argus.financials.service.ServiceException re) { throw new RuntimeException(
        // re.getMessage() ); }
        // }

        cardLayout.show(jPanelCenter, SnapEntryView.class.getName());

    }

    private void displayETPRollover() {

        cardLayout.show(jPanelCenter, ETPRolloverView.class.getName());

    }

    private void displayAllocatedPension(ETPCalc etp) {

        cardLayout.show(jPanelCenter, AllocatedPensionView.class.getName());

    }

    private void displayInvestmentGearing() {

        cardLayout.show(jPanelCenter, GearingView.class.getName());

    }

    private void displayLoans() {

    }

}
