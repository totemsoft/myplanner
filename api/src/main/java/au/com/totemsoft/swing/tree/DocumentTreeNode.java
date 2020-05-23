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

package au.com.totemsoft.swing.tree;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocumentTreeNode 
	implements TreeNode 
{
	private Node node;
	private ArrayList children;
	private TreeNode parent;
	
	/**
	 * 
	 */
	public DocumentTreeNode(Node node) {
		this( node, null );
	}

	public DocumentTreeNode(Node node, TreeNode parent) {
		this.node = node;
		this.parent = parent;
		
		NamedNodeMap attributes = node.getAttributes();
		NodeList nodeList = node.getChildNodes();
		
		int size = attributes == null ? 0 : attributes.getLength();
		size += nodeList == null ? 0 : nodeList.getLength();
		this.children = new ArrayList(size);

		for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
			Node n = attributes.item(i);
			children.add( createDocumentTreeNode(n) );
		}
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			//TODO: ???
			// do not add empty text node
			if (n.getNodeType() == Node.TEXT_NODE
				&& n.getNodeValue().trim().length() == 0) continue;
			children.add( createDocumentTreeNode(n) );
		}
		
	}

	public String toString() {
		if (node.getNodeType() == Node.TEXT_NODE)
			return node.getNodeValue();
		if (node.getNodeType() == Node.ATTRIBUTE_NODE)
			return node.getNodeName() + "=\"" + node.getNodeValue() + "\"";
		return node.getNodeName();
	}
	
	public int hashCode() {
		return node.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof DocumentTreeNode)
			return node.equals( ((DocumentTreeNode)obj).node );
		if (obj instanceof Node)
			return node.equals( (Node)obj );
		return super.equals(obj);
	}
	
	public Node getNode() {
		return node;
	}
	
	protected DocumentTreeNode createDocumentTreeNode(Node node) {
		return new DocumentTreeNode(node, this);
	}
	
	//////////////////////////////////////////////////////////////////////
	//				Implementation of TreeNode interface				//
	//////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	public int getChildCount() {
		return children.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		short nodeType = node.getNodeType();
		return 
			//nodeType == Node.ENTITY_NODE ||
			//nodeType == Node.ENTITY_REFERENCE_NODE ||
			nodeType == Node.DOCUMENT_FRAGMENT_NODE ||
			nodeType == Node.DOCUMENT_NODE ||
			nodeType == Node.ELEMENT_NODE
		;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return !getAllowsChildren();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	public Enumeration children() {
		return Collections.enumeration(children);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getParent()
	 */
	public TreeNode getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	public TreeNode getChildAt(int childIndex) {
		return (TreeNode) children.get(childIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}

	/**
	 * 
	 * @return
	 */
	public DocumentTreeNode [] getChildren() {
		return (DocumentTreeNode []) children.toArray( new DocumentTreeNode [0] ); 
	}

}
