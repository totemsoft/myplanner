package com.argus.financials.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.FocusManager;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.argus.financials.config.FPSLocale;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.UtilityService;
import com.argus.financials.swing.FinancialPlannerFocusManager;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.config.AppConfig;
import com.argus.financials.ui.login.RegistrationView;
import com.argus.financials.ui.login.UserLogin;
import com.argus.financials.ui.sql.UpdateManagerApp;
import com.argus.swing.SplashWindow;
import com.argus.swing.StatusBarPanel;
import com.argus.swing.SwingUtils;
import com.argus.swing.plaf.MetalTheme;


/**
 *
 * @author  Valera
 */
public class FinancialPlannerApp
    extends JFrame
    implements FocusListener
{

    /** serialVersionUID */
    private static final long serialVersionUID = 7773047659819372527L;

    /** logger. */
    private static final Logger LOG = Logger.getLogger(FinancialPlannerApp.class);

    public static final String APP_VERSION = "1.02.b01";

    private static final int USER0_IDX = 0;
    private static final int USER_IDX = 1;
    private static final int CLIENT0_IDX = 2;
    private static final int CLIENT_IDX = 3;

    private static FinancialPlannerApp instance;

    private FinancialPlannerActionMap actionMap;
    private IFinancialPlannerNavigator navigator;
    
    private StatusBarPanel statusBarPanel;
    private SplashWindow splashWindow;// = new SplashWindow("/image/logo/splash.gif", this, 10000);

    private static ClientService clientService;
    private static UtilityService utilityService;
    public static void setClientService(ClientService clientService) {
        FinancialPlannerApp.clientService = clientService;
    }
    public static void setUtilityService(UtilityService utilityService) {
        FinancialPlannerApp.utilityService = utilityService;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //final boolean useTaskBar = false;
        final boolean useTaskBar = true;
        ApplicationContext applicationContext = SpringApplication.run(AppConfig.class);
        java.awt.EventQueue.invokeLater(new Runnable() {public void run() {
            try {
                instance = new FinancialPlannerApp();
                instance.initComponents(useTaskBar);
                instance.syncDBSchema();
                // modal dialog
                boolean displayOnDesktop = Boolean.valueOf(System.getProperty("DisplayOnDesktop"));
                instance.displayLogin(!useTaskBar, displayOnDesktop);
                //instance.setVisible(true);
            } catch (Exception e) {
                LOG.fatal(e.getMessage(), e);
                JOptionPane.showMessageDialog(
                    instance,
                    e.getMessage(),
                    "Application failed to start",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }});
    }

    public static String getViewCaption()
    {
        return "Financial Planner";
    }

    
    private void initComponents(boolean useTaskBar) 
        throws IOException
    {
        final String config = "financialPlanner.properties";
        actionMap = new FinancialPlannerActionMap(this, this, config);

        // LookAndFeel class that implements the native systems look and feel if there is one,
        // otherwise the name of the default cross platform LookAndFeel class.
        try {
            MetalLookAndFeel.setCurrentTheme(new MetalTheme());
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        FocusManager.setCurrentManager(new FinancialPlannerFocusManager());

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // !!! SWITCH !!!
        navigator = useTaskBar ?
                (IFinancialPlannerNavigator) new FinancialPlannerNavigator(actionMap) :
                (IFinancialPlannerNavigator) new FinancialPlannerMenu(actionMap);
        if (navigator instanceof JMenuBar){
            this.setJMenuBar((JMenuBar) navigator);
        } else {
            JScrollPane jScrollPane = new JScrollPane();
            this.getContentPane().add(jScrollPane, java.awt.BorderLayout.CENTER);
            jScrollPane.setViewportView((Component) navigator);
        }

        statusBarPanel = new StatusBarPanel();
        JPanel jp = new JPanel(); jp.add(new JLabel("User: "));
        statusBarPanel.addJComponent(jp, USER0_IDX);
        statusBarPanel.addJComponent(new JLabel(), USER_IDX);
        jp = new JPanel(); jp.add(new JLabel("ClientView: "));
        statusBarPanel.addJComponent(jp, CLIENT0_IDX);
        statusBarPanel.addJComponent(new JLabel(), CLIENT_IDX);
        this.getContentPane().add(statusBarPanel, java.awt.BorderLayout.SOUTH);

        this.setTitle(getViewCaption());
        SwingUtil.setIconImage(this, null);

        SwingUtils.setDefaultFont(this);

        this.pack();
    }

    public static FinancialPlannerApp getInstance() {
        return instance;
    }
    
    public void focusGained(FocusEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void focusLost(FocusEvent e) {
        // TODO Auto-generated method stub
        
    }


    private void displayLogin(boolean allowDisplayOnDesktop, boolean displayOnDesktop) {
        // create UserLogin
        boolean exists = UserLogin.exists();
        final UserLogin loginView = UserLogin.newInstance(allowDisplayOnDesktop, displayOnDesktop);
        loginView.setUserName(FPSLocale.getInstance().getLastUserName());

        if (!exists) {

            loginView.setActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {

                    loginView.setVisible(false);

                    if (evt.getActionCommand().equalsIgnoreCase("OK")) {

                        if (loginView.login()) {
                            //EventQueue.invokeLater(new Runnable(){public void run() {
                                updateUser(loginView.getUserName());
                            //}});

                            if (SwingUtil.isDisplayOnDesktop()) {
                                // maximize
                                setSize(SwingUtils.DIM_SCREEN);
                                setContentPane(SwingUtil.getDesktop());
                                //setResizable(displayOnDesktop);
                            }

                            setVisible(true);
                            pack();

                            if (splashWindow != null)
                                splashWindow.close();

                        } else {
                            JOptionPane.showMessageDialog(null,
                                "Failed to login.\n" + loginView.getLastError(),
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                            logout(1);
                        }

                    } else if (evt.getActionCommand().equalsIgnoreCase(
                            "Register")) {
                        register(); // modal dialog

                    } else { // Cancel
                        logout(0);

                    }

                }
            });

            JDialog dialog = (JDialog) SwingUtil.add2Dialog(this,
                    FinancialPlannerApp.getViewCaption(), true, loginView, false,
                    false);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent) {
                    // System.exit(0);
                }
            });

        }

        loginView.setVisible(true);

    }

    private void logout(int status) {
        UserLogin login = UserLogin.getInstance();
        if (login != null) login.logout();
        System.exit(status);
    }

    private void register() {
        RegistrationView view = RegistrationView.getInstance();
        JDialog dlg = SwingUtil.add2Dialog(this, view.getViewCaption(), true, view);
        System.exit(0);
    }

    private void syncDBSchema() {
        try {
            utilityService.syncDBSchema();
        } catch (Exception e) {
            LOG.fatal(e.getMessage(), e);
            e.printStackTrace(System.err);

            if (JOptionPane.showConfirmDialog(SwingUtil.getSharedOwnerFrame(),
                    "Failed to update database.\n" + e.getMessage()
                            + "\nDo you want to login as administrator"
                            + "\nand try to fix this SQL update problem ?",
                    "SQL Update Error", JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                String cmd = "start \"SQL Manager\" /B \"java\" "
                        + UpdateManagerApp.class.getName()
                        + " myplanner.properties";
                try {
                    Process process = com.argus.io.DOSRunner.getInstance().run(
                            cmd, "/c");
                } catch (java.io.IOException e2) {
                    System.err.println(e2.getMessage());
                }

            }

            logout(1);
        }
        
    }

    // HACK code for update client
    public void updateClient() {
        try {
            ClientService client = clientService;
            String clientName = client == null ? "" : client.getPersonName().getFullName();

            statusBarPanel.setText(CLIENT_IDX, clientName);

            // re-init client saved models
            actionMap.updateAccessability();
            
            //pack();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    // HACK code for update user
    public void updateUser(String loginID) {

        try {
            ClientSearch.getInstance().updateView();

            actionMap.updateAccessability();

            ClientService clientPerson = clientService;
            String clientName = clientPerson == null ? "" : clientPerson.getPersonName().getFullName();
            statusBarPanel.setText(CLIENT_IDX, clientName);

            statusBarPanel.setText(USER_IDX, loginID);
            FPSLocale.getInstance().setLastUserName(loginID);
            
            //pack();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public void updateModels() {
        navigator.updateCalculators();
        //pack();
    }
    
}
