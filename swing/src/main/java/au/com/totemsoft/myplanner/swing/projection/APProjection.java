/*
 * APProjection.java
 *
 * Created on 6 September 2002, 20:35
 */

package au.com.totemsoft.myplanner.swing.projection;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import au.com.totemsoft.format.Currency;
import au.com.totemsoft.format.Number2;
import au.com.totemsoft.myplanner.projection.AllocatedPensionCalcNew;
import au.com.totemsoft.swing.SwingUtil;
import au.com.totemsoft.swing.SwingUtils;

public class APProjection extends javax.swing.JPanel {

    private AllocatedPensionCalcNew apCalc;

    /** Creates new form APProjection */
    public APProjection(AllocatedPensionCalcNew calc) {
        apCalc = calc;
        initComponents();
        setDetailedTableModel(jTableData);
        initDetailedTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableData = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(700, 350));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1,
                javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setPreferredSize(new java.awt.Dimension(750, 510));
        jScrollPane1
                .setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1
                .setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jTableData.setBackground(java.awt.Color.lightGray);
        jScrollPane1.setViewportView(jTableData);

        jPanel1.add(jScrollPane1);

        add(jPanel1);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        jPanel2.add(jButtonClose);

        add(jPanel2);

    }// GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCloseActionPerformed
        // Add your handling code here:
        SwingUtil.setVisible(this, false);

    }// GEN-LAST:event_jButtonCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTableData;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JButton jButtonClose;

    // End of variables declaration//GEN-END:variables
    /***************************************************************************
     * 
     **************************************************************************/
    private TableModel detailedTableModel;

    private Object[] detailedColumnNames = new String[] { "Age",
            "Opening Balance", "Minimum", "Maximum", "Other",
            "Selected Pension", "Assessable Income", "Net Tax Payable",
            "Rebate", "Excess Rebate", "Medicare Levy", "Net Pension",
            "End Balance" };

    private static final int DETAILS_COLUMN_COUNT = 13;

    private void setDetailedTableModel(JTable jTable) {

        if (detailedTableModel == null) {

            detailedTableModel = new DefaultTableModel(new Object[][] {},
                    detailedColumnNames) {

                private Class[] types = new Class[] { java.lang.Integer.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class,
                        java.math.BigDecimal.class, java.math.BigDecimal.class };

                public String getColumnName(int columnIndex) {
                    return super.getColumnName(columnIndex);
                }

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }

            };

        }

        if (jTable.getModel() == detailedTableModel)
            return;

        // jTable.setTableHeader(null);
        jTable.setModel(detailedTableModel);

        JTableHeader th = new JTableHeader(jTable.getColumnModel());
        jTable.setTableHeader(th);
        th.setFont(SwingUtils.getDefaultFont());

    }

    private void initDetailedTable() {
        jTableData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < jTableData.getColumnModel().getColumnCount(); i++)
            jTableData.getColumnModel().getColumn(i).setPreferredWidth(100);

        Object[][] rowData = getDetailedRowData();

        TableModel tm = jTableData.getModel();

        if (tm instanceof DefaultTableModel) {
            if (rowData == null)
                ((DefaultTableModel) tm).setRowCount(0);
            else
                ((DefaultTableModel) tm).setDataVector(rowData,
                        detailedColumnNames);

        } else {
            System.err.println("Unhandled TableModel: " + tm);
            return;
        }

    }

    private Object[][] getDetailedRowData() {

        int[] age = apCalc.getAgeArray();
        double[] openingBalance = apCalc.getOpeningBalanceArray();
        double[] minimum = apCalc.getMinimumArray();
        double[] maximum = apCalc.getMaximumArray();
        double[] others = apCalc.getOthersArray();
        double[] selectedPension = apCalc.getSelectedPensionArray();
        double[] assessableIncome = apCalc.getAssessableIncomeArray();
        double[] netTaxPayable = apCalc.getNetTaxPayableArray();
        double[] rebate = apCalc.getRebateArray();
        double[] excessRebate = apCalc.getExcessRebateArray();
        double[] medicareLevy = apCalc.getMedicareLevyArray();
        double[] netPension = apCalc.getNetPensionArray();
        double[] endBalance = apCalc.getEndBalanceArray();

        try {

            Object[][] rowData = new Object[apCalc.getSize()][DETAILS_COLUMN_COUNT];

            for (int i = 0; i < apCalc.getSize(); i++) {
                rowData[i][0] = Number2.getNumberInstance().toString(age[i]);
                rowData[i][1] = Currency.getCurrencyInstance().toString(
                        openingBalance[i]);
                rowData[i][2] = Currency.getCurrencyInstance().toString(
                        minimum[i]);
                rowData[i][3] = Currency.getCurrencyInstance().toString(
                        maximum[i]);
                rowData[i][4] = Currency.getCurrencyInstance().toString(
                        others[i]);
                rowData[i][5] = Currency.getCurrencyInstance().toString(
                        selectedPension[i]);
                rowData[i][6] = Currency.getCurrencyInstance().toString(
                        assessableIncome[i]);
                rowData[i][7] = Currency.getCurrencyInstance().toString(
                        netTaxPayable[i]);
                rowData[i][8] = Currency.getCurrencyInstance().toString(
                        rebate[i]);
                rowData[i][9] = Currency.getCurrencyInstance().toString(
                        excessRebate[i]);
                rowData[i][10] = Currency.getCurrencyInstance().toString(
                        medicareLevy[i]);
                rowData[i][11] = Currency.getCurrencyInstance().toString(
                        netPension[i]);
                rowData[i][12] = Currency.getCurrencyInstance().toString(
                        endBalance[i]);

            }

            return rowData;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

}
