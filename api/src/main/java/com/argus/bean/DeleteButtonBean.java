package com.argus.bean;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class DeleteButtonBean extends JButton implements ActionListener {
    BorderLayout borderLayout1 = new BorderLayout();

    private String message = "Are you sure you want to delete this record ?";

    private transient Vector deleteDataListeners;

    public DeleteButtonBean() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);

        this.addActionListener(this);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public void actionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(null, message, "",
                JOptionPane.YES_NO_OPTION) == 0) {
            System.out.println("User selected Yes");
            fireConfirmDataDeletion(new DeleteDataEvent(this, 0, this
                    .getActionCommand()));
        } else {
            System.out.println("User selected No");
        }

    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentAdded(ContainerEvent e) {
    }

    public void componentRemoved(ContainerEvent e) {
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
    }

    public synchronized void removedeleteDataListener(DeleteDataListener l) {
        if (deleteDataListeners != null && deleteDataListeners.contains(l)) {
            Vector v = (Vector) deleteDataListeners.clone();
            v.removeElement(l);
            deleteDataListeners = v;
        }
    }

    public synchronized void adddeleteDataListener(DeleteDataListener l) {
        Vector v = deleteDataListeners == null ? new Vector(2)
                : (Vector) deleteDataListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            deleteDataListeners = v;
        }
    }

    protected void fireConfirmDataDeletion(DeleteDataEvent e) {
        if (deleteDataListeners != null) {
            Vector listeners = deleteDataListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((DeleteDataListener) listeners.elementAt(i))
                        .confirmDataDeletion(e);
            }
        }
    }

}