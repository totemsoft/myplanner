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
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */


import org.w3c.dom.Node;

import com.argus.util.Enumeration;

public class DocumentTreeTableModel extends AbstractTreeTableModel {

    public static final int NODE  = 0;
    public static final int VALUE = 1;
    
    // Names of the columns.
    static final String[] columnNames = {
        "Node", 
		"Value", 
    };

    // Types of the columns.
    static final Class[] columnTypes = { 
        TreeTableModel.class, 
		String.class, 
    };

    // Column editable.
    static final boolean [] columnEditable = { 
        true, 
		true, 
    };

    // enumeration for value column
	private final Object [] values;

	
	/**
	 * @param node
	 */
	public DocumentTreeTableModel(Node node) {
		super( new DocumentTreeTableNode(node) );
		this.values = null;
	}
	
	public DocumentTreeTableModel(Node node, final Object [] values) {
		super( new DocumentTreeTableNode(node) );

		this.values = values;
	}

	/**
	 * @return Returns the values.
	 */
	public Object[] getValues() {
		return values;
	}

	/* (non-Javadoc)
	 * @see com.argus.table.TreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/* (non-Javadoc)
	 * @see com.argus.table.TreeTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/* (non-Javadoc)
	 * @see com.argus.table.TreeTableModel#getColumnClass(int)
	 */
    public Class getColumnClass(int column) {
		if (column == VALUE)
			return values == null ? columnTypes[column] : Enumeration.class;
    	return columnTypes[column];
    }

    /**
     * By default, make the column with the Tree in it the only editable one. 
     * Making this column editable causes the JTable to forward mouse 
     * and keyboard events in the Tree column to the underlying JTree. 
     * 
	 * (non-Javadoc)
	 * @see com.com.argus.swing.table.TreeTableModel#isCellEditable(ITreeTableNode, int)
     */
    public boolean isCellEditable(ITreeTableNode node, int column) { 
    	if (super.isCellEditable(node, column)) 
        	return true;
        return node.isCellEditable(column);
    }

    /* (non-Javadoc)
	 * @see com.argus.table.TreeTableModel#getValueAt(ITreeTableNode, int)
	 */
	public Object getValueAt(ITreeTableNode node, int column) {
		return node.getValueAt(column);
    }

	/* (non-Javadoc)
	 * @see com.argus.table.TreeTableModel#setValueAt(Object, ITreeTableNode, int)
	 */
    public void setValueAt(Object aValue, ITreeTableNode node, int column) {
    	node.setValueAt(aValue, column);
    	fireTreeNodesChanged(node);
    }

    /**
     * Returns true if the passed in object represents a leaf, false
     * otherwise.
     */
    public boolean isLeaf(Object node) {
        if ( node instanceof ITreeTableNode )
            return ( (ITreeTableNode) node ).isLeaf();
        return false;
    }

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		Object [] children = getChildren(parent); 
		return (children == null) ? 0 : children.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
    	return getChildren( parent )[index]; 
	}
	
    private Object[] getChildren(Object parent) {
    	if (parent instanceof DocumentTreeTableNode)
    		return ( (DocumentTreeTableNode) parent ).getChildren();
    	return null;
    }

}
