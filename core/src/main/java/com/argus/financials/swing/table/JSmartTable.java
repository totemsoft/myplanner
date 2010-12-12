/*
 * JSmartTable.java
 *
 * Created on 16 April 2003, 09:57
 */

package com.argus.financials.swing.table;

/**
 * 
 * @author valeri chibaev
 * 
 * HEADER1 (Bold,LEADING) HEADER2 (Bold,LEADING) HEADER3 (Bold,CENTER) HEADER4
 * (Bold,TRAILING) HEADER5 (Bold,TRAILING)
 * 
 * BODY
 * 
 * FOOTER5 (Bold,TRAILING) FOOTER4 (Bold,TRAILING) FOOTER3 (Bold,CENTER) FOOTER2
 * (Bold,LEADING) FOOTER1 (Bold,TRAILING)
 * 
 */

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class JSmartTable extends javax.swing.JTable {

    /** Creates a new instance of JSmartTable */
    public JSmartTable() {
    }

    public void setModel(TableModel dataModel) {
        if (dataModel instanceof ISmartTableModel)
            super.setModel(dataModel);
        else
            super.setModel(dataModel);
        /*
         * else throw new IllegalArgumentException( "JSmartTable::setModel( " +
         * dataModel.getClass() + " )\n" + "Instance of
         * com.argus.swing.table.ISmartTableModel expected." );
         */
    }

    private static final TableCellRenderer groupHeader1Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.LEADING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupHeader1Renderer() {
        return groupHeader1Renderer;
    }

    private static final TableCellRenderer groupHeader2Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.LEADING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupHeader2Renderer() {
        return groupHeader2Renderer;
    }

    private static final TableCellRenderer groupHeader3Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.LEADING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupHeader3Renderer() {
        return groupHeader3Renderer;
    }

    private static final TableCellRenderer groupHeader4Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.LEADING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupHeader4Renderer() {
        return groupHeader4Renderer;
    }

    private static final TableCellRenderer groupHeader5Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.LEADING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupHeader5Renderer() {
        return groupHeader5Renderer;
    }

    private static final TableCellRenderer groupFooter1Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.TRAILING); // GRAND
                                                                        // TOTAL
                                                                        // for
                                                                        // this
                                                                        // table
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupFooter1Renderer() {
        return groupFooter1Renderer;
    }

    private static final TableCellRenderer groupFooter2Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.TRAILING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupFooter2Renderer() {
        return groupFooter2Renderer;
    }

    private static final TableCellRenderer groupFooter3Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.TRAILING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupFooter3Renderer() {
        return groupFooter3Renderer;
    }

    private static final TableCellRenderer groupFooter4Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.TRAILING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupFooter4Renderer() {
        return groupFooter4Renderer;
    }

    private static final TableCellRenderer groupFooter5Renderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.TRAILING);
            }
            return font;
        }
    };

    public static final TableCellRenderer getGroupFooter5Renderer() {
        return groupFooter5Renderer;
    }

    private static final TableCellRenderer numericBoldCellRenderer = new DefaultTableCellRenderer() {
        private java.awt.Font font;

        public java.awt.Font getFont() {
            if (font == null) {
                java.awt.Font f = super.getFont();
                if (f == null)
                    return null;
                font = new java.awt.Font(f.getName(), java.awt.Font.BOLD, f
                        .getSize());
                this.setHorizontalAlignment(SwingConstants.TRAILING);
            }
            return font;
        }
    };

    public TableCellRenderer getCellRenderer(int row, int column) {
        ISmartTableModel tm = (ISmartTableModel) this.getModel();
        int rowType = tm.getRowAt(row).getRowType();

        Class columnClass = tm.getColumnClass(column);
        if (java.lang.Number.class.isAssignableFrom(columnClass)
                || com.argus.math.Numeric.class.isAssignableFrom(columnClass))
            return rowType == ISmartTableRow.BODY ? super.getCellRenderer(row,
                    column) : numericBoldCellRenderer;

        if (rowType == ISmartTableRow.FOOTER1)
            return groupFooter1Renderer;
        if (rowType == ISmartTableRow.FOOTER2)
            return groupFooter2Renderer;
        if (rowType == ISmartTableRow.FOOTER3)
            return groupFooter3Renderer;
        if (rowType == ISmartTableRow.FOOTER4)
            return groupFooter4Renderer;
        if (rowType == ISmartTableRow.FOOTER5)
            return groupFooter5Renderer;
        if (rowType == ISmartTableRow.HEADER1)
            return groupHeader1Renderer;
        if (rowType == ISmartTableRow.HEADER2)
            return groupHeader2Renderer;
        if (rowType == ISmartTableRow.HEADER3)
            return groupHeader3Renderer;
        if (rowType == ISmartTableRow.HEADER4)
            return groupHeader4Renderer;
        if (rowType == ISmartTableRow.HEADER5)
            return groupHeader5Renderer;
        return super.getCellRenderer(row, column);
    }

}
