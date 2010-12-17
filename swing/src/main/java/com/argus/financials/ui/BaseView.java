/*
 * BaseView.java
 *
 * Created on July 23, 2002, 9:10 AM
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 */

import java.awt.Cursor;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.argus.financials.config.FPSLocale;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.ui.plan.PlanWriterException;

public abstract class BaseView extends javax.swing.JPanel implements
        com.argus.financials.swing.ICloseDialog {

    protected static boolean DEBUG = false;

    public static final int CANCEL_OPTION = javax.swing.JOptionPane.CANCEL_OPTION;

    public static final int OK_OPTION = javax.swing.JOptionPane.OK_OPTION;

    private int result; // CANCEL_OPTION, OK_OPTION

    /** Creates new form BaseView */
    public BaseView() {
        FPSLocale r = FPSLocale.getInstance();
        DEBUG = Boolean.valueOf(System.getProperty("DEBUG")).booleanValue();

        initComponents();

        jButtonRefresh.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jPanelDetails = new javax.swing.JPanel();
        jPanelControls = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jButtonReport = new javax.swing.JButton();
        jPanelCenter = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jPanelPrevNext = new javax.swing.JPanel();
        jButtonPrevious = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        add(jPanelDetails);

        jPanelControls.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER, 20, 5));

        jPanelLeft
                .setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonReport.setText("Report");
        jButtonReport.setDefaultCapable(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jPanelLeft.add(jButtonReport);

        jPanelControls.add(jPanelLeft);

        jPanelCenter
                .setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButtonClose.setText("Close");
        jButtonClose.setDefaultCapable(false);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanelCenter.add(jButtonClose);

        jButtonSave.setText("Save");
        jButtonSave.setDefaultCapable(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanelCenter.add(jButtonSave);

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jPanelCenter.add(jButtonDelete);

        jButtonRefresh.setText("Refresh");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jPanelCenter.add(jButtonRefresh);

        jPanelControls.add(jPanelCenter);

        jButtonPrevious.setText("< Previous");
        jButtonPrevious.setActionCommand("Next");
        jButtonPrevious.setDefaultCapable(false);
        jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviousActionPerformed(evt);
            }
        });

        jPanelPrevNext.add(jButtonPrevious);

        jButtonNext.setText("Next >");
        jButtonNext.setActionCommand("Next");
        jButtonNext.setDefaultCapable(false);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jPanelPrevNext.add(jButtonNext);

        jPanelControls.add(jPanelPrevNext);

        add(jPanelControls);

    }// GEN-END:initComponents

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRefreshActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doRefresh(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonDeleteActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doDelete(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonDeleteActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentShown
        result = CANCEL_OPTION;
    }// GEN-LAST:event_formComponentShown

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNextActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doNext(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPreviousActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPreviousActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doPrevious(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonPreviousActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            doSave(evt);
        } finally {
            setCursor(null);
        }
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            result = closeDialog(this);
        } finally {
            setCursor(null);
        }

    }// GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonReportActionPerformed
        new Thread(new Runnable() {
            public void run() {
                try {
                    doReport();
                } catch (PlanWriterException e) {
                    System.err.println(e.getMessage());
                }
            }
        }, "doReport").start();
    }// GEN-LAST:event_jButtonReportActionPerformed

    public boolean isModified() {
        return jButtonSave.isVisible() && jButtonSave.isEnabled();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JPanel jPanelControls;

    protected javax.swing.JButton jButtonDelete;

    protected javax.swing.JButton jButtonClose;

    protected javax.swing.JButton jButtonRefresh;

    protected javax.swing.JPanel jPanelPrevNext;

    protected javax.swing.JPanel jPanelDetails;

    protected javax.swing.JButton jButtonReport;

    protected javax.swing.JButton jButtonNext;

    protected javax.swing.JButton jButtonPrevious;

    protected javax.swing.JPanel jPanelCenter;

    private javax.swing.JPanel jPanelLeft;

    protected javax.swing.JButton jButtonSave;

    // End of variables declaration//GEN-END:variables

    public int getResult() {
        return result;
    }

    protected void doNext(java.awt.event.ActionEvent evt) {
        // do nothing
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        // do nothing
    }

    public void doSave(java.awt.event.ActionEvent evt) {
        // do nothing
    }

    protected void doDelete(java.awt.event.ActionEvent evt) {
        // do nothing
    }

    protected void doRefresh(java.awt.event.ActionEvent evt) {
        // do nothing
    }

    public void doClose(java.awt.event.ActionEvent evt) {
        SwingUtil.setVisible(this, false);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected String getDefaultReport() {
        return null;
    } // user will be propted for report name

    protected abstract ReportFields getReportData(PersonService person)
            throws Exception;

    protected PersonService getPerson() throws com.argus.financials.service.ServiceException {
        return ServiceLocator.getInstance().getClientPerson();
    }

    protected void doReport()
        throws PlanWriterException
    {
        try {
            ReportFields.generateReport(
                    SwingUtilities.windowForComponent(this),
                    getReportData(getPerson()),
                    getDefaultReport());

        } catch (Exception e) {
            throw new PlanWriterException(e);
        }

    }

    /**
     * @throws Exception *************************************************************************
     * 
     **************************************************************************/
    public abstract void updateView(PersonService person) throws Exception;

    public abstract void saveView(PersonService person) throws Exception;

    public static int closeDialog(com.argus.financials.swing.ICloseDialog view) {

        int result;
        if (view.isModified()) {
            result = JOptionPane
                    .showConfirmDialog(
                            view instanceof java.awt.Component ? (java.awt.Component) view
                                    : null,
                            "Do you want to save data before closing?",
                            "Close dialog", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                view.doSave(null);
                result = OK_OPTION;
                view.doClose(null);

            } else if (result == JOptionPane.NO_OPTION) {
                result = OK_OPTION;
                view.doClose(null);

            } else {
                result = CANCEL_OPTION;
                // keep open
            }

        } else {
            result = OK_OPTION;
            view.doClose(null);
        }

        return result;

    }

}
