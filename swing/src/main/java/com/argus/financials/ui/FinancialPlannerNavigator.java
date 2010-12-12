/*
 * FinancialPlannerMenu2.java
 *
 * Created on 13 June 2006, 18:28
 */

package com.argus.financials.ui;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.argus.financials.code.ModelType;
import com.argus.financials.config.FPSLocale;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.ui.help.HelpBrokerView;
import com.argus.util.Pair;
import com.l2fprod.common.swing.JTaskPaneGroup;

/**
 *
 * @author  Valera
 */
class FinancialPlannerNavigator extends com.l2fprod.common.swing.JTaskPane
    implements IFinancialPlannerNavigator
{
    
    private final FinancialPlannerPermission fpp = FinancialPlannerPermission.getInstance();
    
    /** Creates new form FinancialPlannerMenu2 */
    FinancialPlannerNavigator(ActionMap am) {
        setActionMap(am);
        initComponents();
    }
    
    private void initComponents() {
        com.l2fprod.common.swing.PercentLayout percentLayout = new com.l2fprod.common.swing.PercentLayout();
        percentLayout.setGap(2);
        percentLayout.setOrientation(1);
        setLayout(percentLayout);

        jTaskPaneGroupFile = createTaskGroup(this, "File");
        if (fpp.checkPermissions(IMenuCommand.EXPORT))
            jTaskPaneGroupFile.add(getActionMap().get(IMenuCommand.EXPORT));
        if (fpp.checkPermissions(IMenuCommand.IMPORT))
            jTaskPaneGroupFile.add(getActionMap().get(IMenuCommand.IMPORT));
        jTaskPaneGroupFile.add(getActionMap().get(IMenuCommand.EXIT));

        jTaskPaneGroupClientDetails = createTaskGroup(this, "Client Focus");
        jTaskPaneGroupClientDetails.add(getActionMap().get(IMenuCommand.SEARCH_CLIENT));
        if (fpp.checkPermissions(IMenuCommand.ADD_CLIENT))
            jTaskPaneGroupClientDetails.add(getActionMap().get(IMenuCommand.ADD_CLIENT));
        jTaskPaneGroupClientDetails.add(getActionMap().get(IMenuCommand.CLIENT_DETAILS));
        if (fpp.checkPermissions(IMenuCommand.RISK_EVALUATION_CLIENT))
            jTaskPaneGroupClientDetails.add(getActionMap().get(IMenuCommand.RISK_EVALUATION_CLIENT));
        jTaskPaneGroupClientDetails.add(getActionMap().get(IMenuCommand.FINANCIALS));
        jTaskPaneGroupClientDetails.setExpanded(true);

        jTaskPaneGroupPartnerDetails = createTaskGroup(this, "Partner Focus");
        jTaskPaneGroupPartnerDetails.add(getActionMap().get(IMenuCommand.PARTNER_DETAILS));
        if (fpp.checkPermissions(IMenuCommand.RISK_EVALUATION_PARTNER))
            jTaskPaneGroupPartnerDetails.add(getActionMap().get(IMenuCommand.RISK_EVALUATION_PARTNER));

        if (fpp.checkPermissions(IMenuCommand.STRATEGIC_ADVICE)) {
            jTaskPaneGroupStrategyDevelopment = createTaskGroup(this, (String) IMenuCommand.STRATEGIC_ADVICE.getSecond());
            jTaskPaneGroupStrategyDevelopment.add(getActionMap().get(IMenuCommand.STRATEGY_DESINER));
        }
        
        jTaskPaneGroupCalculators = createTaskGroup(this, "Analysis & Projection Tools");
        updateCalculators();

        if (fpp.checkPermissions(IMenuCommand.PLAN_WIZARD)) {
            officeIntegration = createTaskGroup(this, "Office Integration");
            officeIntegration.add(getActionMap().get(IMenuCommand.PLAN_WIZARD));
            officeIntegration.add(getActionMap().get(IMenuCommand.PLAN_TEMPLATE_WIZARD));
        }
        
        jTaskPaneGroupTools = createTaskGroup(this, "Tools");
        //jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.UPDATE_REF_DATA));
        //jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.MAINTAIN_REF_DATA));
        //jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.SWAP_ASSETS));
        //jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.RECOVER_ASSETS));
        //jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.REMOVE_ASSETS));
        jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.SOFTWARE_UPDATES));
        if (!FPSLocale.isDevelopment()) {
            jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.SYSTEM_OUT));
            jTaskPaneGroupTools.add(getActionMap().get(IMenuCommand.SYSTEM_ERR));
        }
        // look-and-feel
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        JTaskPaneGroup jTaskPaneGroupLookAndFeel = createTaskGroup(jTaskPaneGroupTools, "Look-And-Feels");
        for (int i = 0; i < info.length; i++) {
            String lookAndFeelName = info[i].getName();
            jTaskPaneGroupLookAndFeel.add(getActionMap().get(lookAndFeelName));
        }

        jTaskPaneGroupHelp = createTaskGroup(this, "Help");
        if (fpp.checkPermissions(IMenuCommand.CONTENTS_INDEX)) {
            jTaskPaneGroupHelp.add(getActionMap().get(IMenuCommand.CONTENTS_INDEX));
            HelpBrokerView.initHelpMenu(jTaskPaneGroupHelp);
        }
        jTaskPaneGroupHelp.add(getActionMap().get(IMenuCommand.FEEDBACK));
        jTaskPaneGroupHelp.add(getActionMap().get(IMenuCommand.ABOUT));

    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupCalculators;
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupClientDetails;
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupPartnerDetails;
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupFile;
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupHelp;
    private com.l2fprod.common.swing.JTaskPaneGroup officeIntegration;
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupStrategyDevelopment;
    private com.l2fprod.common.swing.JTaskPaneGroup jTaskPaneGroupTools;
    // End of variables declaration//GEN-END:variables
    
    private JTaskPaneGroup createTaskGroup(JComponent parent, String title) {
        JTaskPaneGroup taskGroup = new JTaskPaneGroup();
        taskGroup.setTitle(title);
        taskGroup.getContentPane().setLayout(getLayout());
        taskGroup.setExpanded(false);
        taskGroup.setSpecial(true);
        parent.add(taskGroup);
        return taskGroup;
    }
    
    private JTaskPaneGroup createTaskGroup4Calculators(
            JComponent parent, Pair command) 
    {
        if (fpp.checkPermissions(command)) {
            JTaskPaneGroup taskGroup = createTaskGroup(parent, (String) command.getSecond());
            taskGroup.setFont(parent.getFont());
            taskGroup.add(getActionMap().get(command));
            return taskGroup;
        }
        return null;
    }

    public void updateCalculators() {
        
        //clearCalculators(jTaskPaneGroupCalculators);
        jTaskPaneGroupCalculators.removeAll();

        JTaskPaneGroup jTaskPaneGroupQuickView = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.CURRENT_POSITION_CALC);
        JTaskPaneGroup jTaskPaneGroupEtpRollover = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.ETP_ROLLOVER_CALC);
        JTaskPaneGroup jTaskPaneGroupAllocatedPension = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.ALOCATED_PENSION_CALC);
        JTaskPaneGroup jTaskPaneGroupInvestmentGearing = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.INVESTMENT_GEARING_CALC);
        JTaskPaneGroup jTaskPaneGroupCentrelink = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.CENTRELINK_CALC);
        JTaskPaneGroup jTaskPaneGroupMortgageLoan = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.LOAN_AND_MORTGAGE_CALC);
        JTaskPaneGroup jTaskPaneGroupPayg = createTaskGroup4Calculators(jTaskPaneGroupCalculators, IMenuCommand.PAYG_CALC);

        // add saved ones
        ClientService client = ServiceLocator.getInstance().getClientPerson();
        if (client == null)
            return;

        try {
            ModelCollection models = client.getModels();
            if (models == null)
                return;

            Iterator iterModels = models.entrySetIterator();
            while (iterModels.hasNext()) {
                Map.Entry entry = (Map.Entry) iterModels.next();
                Integer modelTypeID = (Integer) entry.getKey();

                Iterator iter = ((Vector) entry.getValue()).iterator();
                while (iter.hasNext()) {
                    final Model model = (Model) iter.next();
                    Action action;
                    
                    switch (modelTypeID.intValue()) {
                        case ModelType.iCURRENT_POSITION_CALC:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.CURRENT_POSITION_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupQuickView != null)
                                jTaskPaneGroupQuickView.add(action);
                            break;
                        case ModelType.iINSURANCE_NEEDS:
    
                        case ModelType.iPREMIUM_CALC:
    
                        case ModelType.iINVESTMENT_GEARING:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.INVESTMENT_GEARING_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupInvestmentGearing != null)
                                jTaskPaneGroupInvestmentGearing.add(action);
                            break;
                        case ModelType.iPROJECTED_WEALTH:
    
                        case ModelType.iINVESTMENT_PROPERTIES:
    
                        case ModelType.iLOAN_MORTGAGE_CALC:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.LOAN_AND_MORTGAGE_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupMortgageLoan != null)
                                jTaskPaneGroupMortgageLoan.add(action);
                            break;
                        case ModelType.iALLOCATED_PENSION:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.ALOCATED_PENSION_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupAllocatedPension != null)
                                jTaskPaneGroupAllocatedPension.add(action);
                            break;
                        case ModelType.iETP_ROLLOVER:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.ETP_ROLLOVER_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupEtpRollover != null)
                                jTaskPaneGroupEtpRollover.add(action);
                            break;
                        case ModelType.iSUPERANNUATION_RBL:
    
                        case ModelType.iRETIREMENT_CALC:
    
                        case ModelType.iRETIREMENT_HOME:
    
                        case ModelType.iPAYG_CALC:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.PAYG_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupPayg != null)
                                jTaskPaneGroupPayg.add(action);
                            break;
                        case ModelType.iCGT_CALC:
    
                        case ModelType.iCENTRELINK_CALC:
                            action = new AbstractAction(model.getTitle()) {
                                public void actionPerformed(ActionEvent evt) {
                                    Action a = getActionMap().get(IMenuCommand.CENTRELINK_CALC);
                                    evt.setSource(model);
                                    a.actionPerformed(evt);
                                }
                            };
                            if (jTaskPaneGroupCentrelink != null)
                                jTaskPaneGroupCentrelink.add(action);
                            break;
                        default:
                            continue;
                    }

                }

            }

        } catch (com.argus.financials.service.ServiceException e) {
            e.printStackTrace();
        }

    }
    
}
