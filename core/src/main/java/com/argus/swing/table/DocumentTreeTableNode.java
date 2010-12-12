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

package com.argus.swing.table;

import org.w3c.dom.Node;

import com.argus.swing.tree.DocumentTreeNode;

public class DocumentTreeTableNode
	extends DocumentTreeNode
	implements ITreeTableNode
{

    public DocumentTreeTableNode(Node node) {
		this(node, null);
	}
    public DocumentTreeTableNode(Node node, DocumentTreeTableNode parent) {
	    super(node, parent);
	}

	public String toString() {
		return getNode().getNodeName();
	}
	
	protected DocumentTreeNode createDocumentTreeNode(Node node) {
		return new DocumentTreeTableNode(node, this);
	}
	
    /************************************************************
     * ITreeTableNode interface
     ***********************************************************/
	public Object getValueAt(int columnIndex) {
	    switch(columnIndex) {
            case DocumentTreeTableModel.NODE:
            	// tree node HAS TO BE instance of ITreeTableNode (for JTreeTable, etc) !!!
                return this;//getNode().getNodeName();
            case DocumentTreeTableModel.VALUE:
                return getNode().getNodeValue();
            default: return null;
	    }
	}

	public void setValueAt(Object aValue, int columnIndex) {
        switch(columnIndex) {
            case DocumentTreeTableModel.NODE:
                break;
            case DocumentTreeTableModel.VALUE:
            	getNode().setNodeValue( (String) aValue );
                break;
            default: ;
        }
        
	}

	public boolean isCellEditable(int columnIndex) {
		short nodeType = getNode().getNodeType();
		if (nodeType != Node.TEXT_NODE && nodeType != Node.ATTRIBUTE_NODE)
			return false;
		return DocumentTreeTableModel.columnEditable[columnIndex];
	}

}

