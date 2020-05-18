/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.financials.ui.table;

/**
 * <p>Title: </p>
 * <p>Description:
 * An abstract implementation of the TreeTableModel interface, handling the list 
 * of listeners. 
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.2 $
 */

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
 
public abstract class AbstractTreeTableModel implements TreeTableModel {
    
    private ITreeTableNode root;     
    private EventListenerList listenerList = new EventListenerList();
  
    public AbstractTreeTableModel() {
        this(null); 
    }
    public AbstractTreeTableModel(ITreeTableNode root) {
        this.root = root; 
    }

    public void setRoot(ITreeTableNode root) {
        this.root = root;
        //this.root.nodeChanged();
    }

    /*****************************************************************
     * Default implmentations for methods in the TreeModel interface. 
     ****************************************************************/
    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0; 
    }

    public void valueForPathChanged(TreePath path, Object newValue) {}

    // This is not called in the JTree's default mode: use a naive implementation. 
    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
		    if (getChild(parent, i).equals(child)) { 
		        return i; 
		    }
        }
        return -1; 
    }

    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    
    /**
     * 
     * @param node
     */
    protected void fireTreeNodesChanged(Object node) {
    	Object source = this;
    	Object[] path = {node};
    	int[] childIndices = {getIndexOfChild(getRoot(), node)};
		Object[] children = {node};
    	fireTreeNodesChanged(source, path, childIndices, children);
    }
    
//    protected void fireTreeNodesChanged(Object [] nodes) {
//    	Object source = this;
//    	Object[] path = nodes;
//    	int[] childIndices = new int [nodes.length];
//    	for (int i = 0; i < nodes.length; i++)
//    		childIndices[i] = getIndexOfChild(getRoot(), nodes[i]);
//		Object[] children = nodes;
//    	fireTreeNodesChanged(source, path, childIndices, children);
//    }
    
    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                
                ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
                
            }          
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
            }          
        }
        
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
            }          
        }
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
            }          
        }
    }

    /***********************************************************************
     * Default impelmentations for methods in the TreeTableModel interface. 
     **********************************************************************/
    public Class getColumnClass(int column) { 
    	return Object.class; 
    }

    /**
     * By default, make the column with the Tree in it the only editable one. 
     * Making this column editable causes the JTable to forward mouse 
     * and keyboard events in the Tree column to the underlying JTree. 
     * 
     */
    public boolean isCellEditable(ITreeTableNode node, int column) { 
         return getColumnClass(column) == TreeTableModel.class; 
    }

    public void setValueAt(Object aValue, ITreeTableNode node, int column) {}
    //public Object getValueAt(ITreeTableNode node, int column) {return null;}


    // Left to be implemented in the subclass:

    /* 
     *   public Object getChild(Object parent, int index)
     *   public int getChildCount(Object parent) 
     *   public int getColumnCount() 
     *   public String getColumnName(Object node, int column)  
     *   public Object getValueAt(Object node, int column) 
     */

}
