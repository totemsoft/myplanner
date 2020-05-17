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
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1.1.1 $
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

public class FileTreeTableModel extends AbstractTreeTableModel {

    public static final int NAME     = 0;
    public static final int SELECTED = 1;
    
    // Names of the columns.
    static private final String[] columnNames = {
        "Name", 
		"Select", 
    };

    // Types of the columns.
    static private final Class[] columnTypes = { 
        TreeTableModel.class, 
		Boolean.class, 
    };

    // Column editable.
    static private final boolean [] columnEditable = { 
        true, 
		true, 
    };

    
	/**
	 * @param root
	 */
	public FileTreeTableModel(String root) {
		this( new File(root) );
	}

	/**
	 * @param root
	 */
	public FileTreeTableModel(File root) {
		super( new FileTreeTableNode(root) );
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
    	return parent == null ? null : ( (FileTreeTableNode) parent ).getChildren(); 
    }

	
	static class FileTreeTableNode implements ITreeTableNode {
		
		// Parent
		private FileTreeTableNode parent;
		// Children
		private List children; 

		//file to manage
		private File file;
		// cache file name
		private String displayName;
		// 
		private boolean selected;
		
		FileTreeTableNode(File file) {
			this(null, file);
		}
		FileTreeTableNode(FileTreeTableNode parent, File file) {
	            
		    this.parent = parent;
		    this.file = file;
/*	            
		    try { 
                displayName = file.getCanonicalPath(); 
            } catch (IOException ex) { 
                ex.printStackTrace();
                displayName = ex.getMessage(); 
            }
 */
            displayName = file.getName();

            createChildren();
            
		}

		public String toString() {
			return displayName;
		}
		
		private void createChildren() {
			children = new ArrayList();

		    try {
		    	String [] files = file.list();
		    	if ( files != null ) {
		    		String path = file.getPath();
		    		for (int i = 0; i < files.length; i++) {
		    			File childFile = new File(path, files[i]); 
		    			children.add( new FileTreeTableNode(this, childFile ) );
		    		}
		    	}
		    } catch (SecurityException se) {
		    	// ignore
		    }
	            
		}

        /************************************************************
         * ITreeTableNode interface
         ***********************************************************/
		public Object getValueAt(int columnIndex) {
		    switch(columnIndex) {
	            case NAME:
	            	// tree node HAS TO BE instance of ITreeTableNode !!!
	                return this;//getName();
	            case SELECTED:
	                return isSelected() ? Boolean.TRUE : Boolean.FALSE;
	            default: return null;
		    }
		}

		public void setValueAt(Object aValue, int columnIndex) {
	        switch(columnIndex) {
	            case NAME:
	                break;
	            case SELECTED:
	                setSelected( ( (Boolean) aValue ).booleanValue() );
	                break;
	            default: ;
	        }
		}

		public boolean isCellEditable(int columnIndex) {
			return columnEditable[columnIndex];
		}

        /************************************************************
         * TreeNode interface
         ***********************************************************/
		public TreeNode getChildAt(int childIndex) {
			return (TreeNode) children.get(childIndex);
		}

        public int getChildCount() { 
            return children.size();
        }

	    public TreeNode getParent() {
		    return parent;
		}

	    public int getIndex(TreeNode node) {
	    	return children.indexOf(node);
	    }

	    public boolean getAllowsChildren() {
	    	return !isLeaf();
	    }

		public boolean isLeaf() {
		    return file.isFile();
		    //return !file.isDirectory();
		}

		public Enumeration children() {
			throw new RuntimeException("consider using ITreeTableNode [] FileTreeTableNode#getChildren().");
		}
		public ITreeTableNode [] getChildren() {
			return (ITreeTableNode []) children.toArray( new ITreeTableNode [0] ); 
		}

        
        /************************************************************
         * FileTreeTableNode properties
         ***********************************************************/
		public String getDisplayName() {
			return displayName;
		}
		
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean value) {
			selected = value;
		}

	}

}
