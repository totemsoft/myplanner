/*
 * ReportWriter.java
 *
 * Created on 13 March 2002, 16:45
 */

package com.argus.financials.report;

import javax.swing.JFileChooser;

import com.argus.financials.config.WordSettings;
import com.argus.io.IOUtils;
import com.argus.swing.SwingUtils;

public class ReportWriter {

    private static JFileChooser jFileChooser;

    /** Creates new ReportWriter */
    public ReportWriter(Object data) {
    }

    public String getReportTemplate(String defaultReport) {

        getFileChooser().setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.setDialogTitle("Open Report Template ...");
        if (defaultReport == null) {
            jFileChooser.setSelectedFile(null);
        } else {
            java.io.File file = new java.io.File(defaultReport);
            jFileChooser.setCurrentDirectory(file.getParentFile());
            jFileChooser.setSelectedFile(file);
        }

        if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return null;

        try {
            String fileName = jFileChooser.getSelectedFile().getCanonicalPath();
            return fileName;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getFileName2Save() {

        getFileChooser().setDialogType(JFileChooser.SAVE_DIALOG);
        jFileChooser.setDialogTitle("Save Report as ...");

        // user.home User's home directory
        jFileChooser.setCurrentDirectory(new java.io.File(IOUtils.getHomeDirectory()));
        jFileChooser.setSelectedFile(null);

        if (jFileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return null;

        if (jFileChooser.getSelectedFile() == null)
            return null;

        try {
            String fileName = jFileChooser.getSelectedFile().getCanonicalPath();
            return fileName;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected static JFileChooser getFileChooser() {
        if (jFileChooser == null) {
            jFileChooser = new JFileChooser();
            jFileChooser.setCurrentDirectory(WordSettings.getInstance().getPlanTemplate());
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ;
                }
            });
            SwingUtils.setDefaultFont(jFileChooser);
        }
        return jFileChooser;
    }

}
