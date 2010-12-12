/*
 * CheckBoxList.java
 *
 * Created on 18 June 2002, 17:32
 */

package com.argus.financials.ui;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class CheckBoxList extends JList implements DropTargetListener,
        DragSourceListener, DragGestureListener {

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private Color emptyColor;

    private DropTarget dropTarget;

    private DragSource dragSource;

    public CheckBoxList() {
        this(new DefaultListModel());
    }

    public CheckBoxList(DefaultListModel dlm) {
        super(dlm);

        emptyColor = UIManager.getLookAndFeel().getName().indexOf("Metal") >= 0 ? javax.swing.plaf.metal.MetalLookAndFeel
                .getMenuBackground()
                : Color.lightGray;
        setBackground(emptyColor);

        getModel().addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
                contentsChanged();
            }

            public void intervalRemoved(ListDataEvent e) {
                contentsChanged();
            }

            public void contentsChanged(ListDataEvent e) {
            }

            private void contentsChanged() {
                setBackground(getModel().getSize() > 0 ? Color.white
                        : emptyColor);
            }
        });

        setCellRenderer(new CellRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // default
        addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                // public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() != 1)
                    return;

                java.awt.Point point = e.getPoint();
                int index = locationToIndex(point);
                if (index < 0)
                    return;

                JCheckBox cb = (JCheckBox) getModel().getElementAt(index);
                /**
                 * this condition was removed because even though checkbox is
                 * not checked after the click, action still performed on the
                 * item which was clicked
                 */
                // if ( e.getModifiers() == e.BUTTON1_MASK && point.getX() <= 16
                // ) // left button, within the checkbox
                cb.setSelected(!cb.isSelected());

                setToolTipText(cb.getToolTipText());

                repaint();

            }

        });

    }

    protected class CellRenderer
    // extends JCheckBox
            implements ListCellRenderer {
        private final javax.swing.Icon ICON = new javax.swing.ImageIcon(
                getClass().getResource("/image/Play16.gif"));

        private final javax.swing.Icon DICON = new javax.swing.ImageIcon(
                getClass().getResource("/image/Stop16.gif"));

        public CellRenderer() {
        }

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (!(value instanceof JCheckBox))
                return null;

            JCheckBox cb = (JCheckBox) value;

            // cb.setIcon( ICON );
            // cb.setDisabledIcon( DICON );

            // cb.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
            cb.setBackground(isSelected ? getSelectionBackground()
                    : getBackground());
            cb.setForeground(isSelected ? getSelectionForeground()
                    : getForeground());

            // cb.setEnabled( isEnabled() );

            cb.setFont(getFont());
            cb.setFocusPainted(false);
            cb.setBorderPainted(true);
            cb
                    .setBorder(isSelected ? UIManager
                            .getBorder("List.focusCellHighlightBorder")
                            : noFocusBorder);

            return cb;

        }

    }

    public void setListData(Object[] listData) {

        if (listData == null)
            listData = new String[] { "" };

        Vector v = new Vector();
        for (int i = 0; i < listData.length; i++)
            v.add(createJCheckBox(listData[i].toString(), true));

        setListData(v);
    }

    public List getListData() {

        List listData = new ArrayList();

        ListModel model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            String s = ((JCheckBox) model.getElementAt(i)).getToolTipText();
            // if (DEBUG) System.out.println( "getListData(): " + s );
            listData.add(s);
        }

        return listData;
    }

    public Object[] getSelectedValues() {

        Object[] selection = super.getSelectedValues();
        if (selection == null || selection.length == 0)
            return null;

        String[] array = new String[selection.length];
        for (int i = 0; i < selection.length; i++) {
            String s = ((JCheckBox) selection[i]).getToolTipText();
            // if (DEBUG) System.out.println( "getSelectedValues(): " + s );
            array[i] = s;
        }

        return array;
    }

    public int getSelectedCount() {

        int count = 0;
        ListModel model = getModel();
        for (int i = 0; i < model.getSize(); i++)
            if (((JCheckBox) model.getElementAt(i)).isSelected())
                count++;

        return count;

    }

    public List getCheckedValues() {

        List list = new ArrayList();
        ListModel model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            JCheckBox cb = (JCheckBox) model.getElementAt(i);
            if (cb.isSelected())
                list.add(cb.getToolTipText());
        }

        return list;

    }

    public void selectAll(boolean value) {

        ListModel model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            ((JCheckBox) model.getElementAt(i)).setSelected(value);
            repaint();
        }

    }

    // compare tooltips
    public boolean contains(Object obj) {

        DefaultListModel model = (DefaultListModel) getModel();

        // if (DEBUG) System.out.println( "contains( '" + obj + "' )" );
        for (int i = 0; i < model.getSize(); i++) {
            String s = ((JCheckBox) model.getElementAt(i)).getToolTipText();
            // if (DEBUG) System.out.println( "\t" + s );
            if (s.equals(obj.toString()))
                return true;
        }

        return false;

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static JCheckBox createJCheckBox(Object obj, boolean valid) {

        JCheckBox cb = null;
        if (obj instanceof JCheckBox) { // for drag-and-drop
            cb = (JCheckBox) obj;
        } else {
            cb = new JCheckBox(obj == null ? "???" : obj.toString());
            setToolTipText(cb);
        }

        cb.setEnabled(valid);

        return cb;

    }

    private static void setToolTipText(JCheckBox cb) {

        String s = cb.getText();
        java.io.File file = new java.io.File(s);
        if (file.exists()) {
            cb.setText(file.getName());
            s = file.getPath();
        }

        cb.setToolTipText(s);

    }

    public void addItem(Object obj) {
        addItem(obj, true);
    }

    public void addItem(Object obj, boolean valid) {
        if (obj == null)
            return;

        DefaultListModel model = (DefaultListModel) getModel();
        model.addElement(createJCheckBox(obj, valid));
    }

    public void insertItemAt(Object obj, int index) {
        boolean valid = obj instanceof java.awt.Component ? ((java.awt.Component) obj)
                .isEnabled()
                : true;
        insertItemAt(obj, valid, index);
    }

    public void insertItemAt(Object obj, boolean valid, int index) {
        if (obj == null)
            return;
        if (index < 0) // || index > getModel().getSize() )
            return;

        DefaultListModel model = (DefaultListModel) getModel();
        model.insertElementAt(createJCheckBox(obj, valid), index);
    }

    public void addItems(Object items) {

        removeAllElements();
        if (items == null)
            return;

        java.util.Properties files = new java.util.Properties();
        try {
            files.load(new java.io.ByteArrayInputStream(items.toString()
                    .getBytes()));
        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            return;
        }

        for (int i = 1; i <= files.size(); i++)
            addItem(files.getProperty("" + i, null));

    }

    public void removeItem(Object obj) {

        DefaultListModel model = (DefaultListModel) getModel();

        for (int i = 0; i < model.getSize(); i++) {
            // JCheckBox cb = (JCheckBox) model.getElementAt(i);
            if (model.getElementAt(i) == obj) // ||
                                                // cb.getToolTipText().equals(
                                                // obj.toString() ) )
                model.remove(i);
        }

    }

    public void removeCheckedItems() {

        DefaultListModel model = (DefaultListModel) getModel();

        for (int i = model.getSize() - 1; i >= 0; i--) {
            JCheckBox cb = (JCheckBox) model.getElementAt(i);
            if (cb.isSelected())
                model.remove(i);
        }

    }

    public void removeAllElements() {
        DefaultListModel model = (DefaultListModel) getModel();
        model.removeAllElements();
    }

    public boolean moveItemUp(Object obj) {
        if (obj == null)
            obj = getSelectedValue();
        if (obj == null)
            return false;

        DefaultListModel model = (DefaultListModel) getModel();

        int index = model.indexOf(obj);
        if (index == 0)
            return false;

        model.remove(index);
        model.insertElementAt(obj, --index);
        setSelectedIndex(index);

        return true;

    }

    public void moveItemUp(Object obj, int count) {
        while (count-- > 0)
            if (!moveItemUp(obj))
                break;
    }

    public boolean moveItemDown(Object obj) {
        if (obj == null)
            obj = getSelectedValue();
        if (obj == null)
            return false;

        DefaultListModel model = (DefaultListModel) getModel();

        int index = model.indexOf(obj);
        if (index == model.getSize() - 1)
            return false;

        model.remove(index);
        model.insertElementAt(obj, ++index);
        setSelectedIndex(index);

        return true;

    }

    public void moveItemDown(Object obj, int count) {
        while (count-- > 0)
            if (!moveItemDown(obj))
                break;
    }

    public void moveItem(Object obj, int count) {
        if (count > 0)
            moveItemUp(obj, count);
        else if (count < 0)
            moveItemDown(obj, -count);
    }

    public void addMouseListener(final JPopupMenu popupMenu) {
        super.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /***************************************************************************
     * Drag-And-Drop code
     **************************************************************************/
    public boolean isDragAndDropEnabled() {
        return dropTarget != null && dragSource != null;
    }

    public void setDragAndDropEnabled(boolean value) {
        if (isDragAndDropEnabled() == value)
            return;

        if (value) {
            dropTarget = new DropTarget(this, this);
            dragSource = new DragSource();
            dragSource.createDefaultDragGestureRecognizer(this,
                    DnDConstants.ACTION_MOVE, this);
        } else {
            dropTarget = null;
            dragSource = null;
        }

    }

    /**
     * DragGestureListener
     */
    public void dragGestureRecognized(java.awt.dnd.DragGestureEvent event) {
        if (!isDragAndDropEnabled())
            return;

        JCheckBox cb = (JCheckBox) getSelectedValue();

        if (cb != null) {
            Transferable t = new CheckBoxTransferable(cb);
            dragSource.startDrag(event, DragSource.DefaultMoveDrop, t, this);
        } else {
            // System.out.println( "dragGestureRecognized: nothing was
            // selected");
        }

    }

    /**
     * DropTargetListener
     */
    public void dragEnter(java.awt.dnd.DropTargetDragEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DropTarget dragEnter");
        event.acceptDrag(DnDConstants.ACTION_MOVE);
    }

    public void dragExit(java.awt.dnd.DropTargetEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DropTarget dragExit");
    }

    public void dragOver(java.awt.dnd.DropTargetDragEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DropTarget dragOver");
    }

    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent event) {
        if (!isDragAndDropEnabled())
            return;

    }

    public void drop(java.awt.dnd.DropTargetDropEvent event) {
        if (!isDragAndDropEnabled())
            return;

        try {
            Transferable transferable = event.getTransferable();

            // we accept only JCheckBox
            if (transferable.isDataFlavorSupported(flavors[CHECKBOX])) {
                event.acceptDrop(DnDConstants.ACTION_MOVE);
                Object obj = transferable.getTransferData(flavors[CHECKBOX]);

                int index = locationToIndex(event.getLocation());
                // System.out.println( ((JCheckBox)obj).getToolTipText() +
                // "\n\tdrop at " + index );

                if (index < 0) {
                    index = getModel().getSize();
                    addItem(obj); // add to the end
                } else
                    insertItemAt(obj, index);

                setSelectedIndex(index);

                event.getDropTargetContext().dropComplete(true);
            } else {
                event.rejectDrop();
            }

        } catch (java.io.IOException e) {
            e.printStackTrace(System.err);
            event.rejectDrop();

        } catch (UnsupportedFlavorException e) {
            e.printStackTrace(System.err);
            event.rejectDrop();
        }

    }

    /**
     * DragSourceListener
     */
    public void dragDropEnd(java.awt.dnd.DragSourceDropEvent event) {
        if (!isDragAndDropEnabled())
            return;

        if (!event.getDropSuccess())
            return;

        try {
            Object obj = event.getDragSourceContext().getTransferable()
                    .getTransferData(null);
            removeItem(obj);
            // System.out.println( "DragSource dragDropEnd");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public void dragEnter(java.awt.dnd.DragSourceDragEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DragSource dragEnter");
    }

    public void dragExit(java.awt.dnd.DragSourceEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DragSource dragExit");
    }

    public void dragOver(java.awt.dnd.DragSourceDragEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DragSource dragOver");
    }

    public void dropActionChanged(java.awt.dnd.DragSourceDragEvent event) {
        if (!isDragAndDropEnabled())
            return;

        // System.out.println( "DragSource dropActionChanged");
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final int CHECKBOX = 0;

    private static final DataFlavor[] flavors = { new DataFlavor(
            JCheckBox.class, "JCheckBox"), };

    private class CheckBoxTransferable implements
            java.awt.datatransfer.Transferable {

        private JCheckBox sourceData;

        private JCheckBox transferData;

        private CheckBoxTransferable(JCheckBox data) {
            sourceData = data;
            transferData = new JCheckBox(data.getText(), data.getIcon(), data
                    .isSelected());
            transferData.setEnabled(data.isEnabled());
            transferData.setToolTipText(data.getToolTipText());
        }

        public java.lang.Object getTransferData(
                java.awt.datatransfer.DataFlavor dataFlavor)
                throws java.awt.datatransfer.UnsupportedFlavorException,
                java.io.IOException {
            if (dataFlavor == null)
                return sourceData;
            if (dataFlavor.equals(flavors[CHECKBOX]))
                return transferData;
            throw new UnsupportedFlavorException(dataFlavor);
        }

        public boolean isDataFlavorSupported(
                java.awt.datatransfer.DataFlavor dataFlavor) {
            for (int i = 0; i < flavors.length; i++)
                if (dataFlavor.equals(flavors[i]))
                    return true;
            return false;
        }

        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
            return (DataFlavor[]) flavors.clone();
        }

    }

}
