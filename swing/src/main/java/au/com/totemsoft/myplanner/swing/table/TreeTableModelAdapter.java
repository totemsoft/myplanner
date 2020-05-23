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

package au.com.totemsoft.myplanner.swing.table;

/**
 * <p>Title: </p>
 * <p>Description:
 * This is a wrapper class takes a TreeTableModel and implements the table model
 * interface. The implementation is trivial, with all of the event dispatching
 * support provided by the superclass: the AbstractTableModel.
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

class TreeTableModelAdapter extends AbstractTableModel {
	
	private JTree tree;

	private TreeTableModel treeTableModel;

	TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
		
		this.tree = tree;
		this.treeTableModel = treeTableModel;

		this.tree.addTreeExpansionListener(new TreeExpansionListener() {
			// Don't use fireTableRowsInserted() here; the selection model
			// would get updated twice.
			public void treeExpanded(TreeExpansionEvent event) {
				fireTableDataChanged();
			}

			public void treeCollapsed(TreeExpansionEvent event) {
				fireTableDataChanged();
			}
		});

		// Install a TreeModelListener that can update the table when
		// tree changes. We use delayedFireTableDataChanged as we can
		// not be guaranteed the tree will have finished processing
		// the event before us.
		this.treeTableModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireTableDataChanged();
					}
				});
			}

			public void treeNodesInserted(TreeModelEvent e) {
				int[] indexes = e.getChildIndices();
				final int firstRow = e.getChildIndices()[0];
				final int lastRow = e.getChildIndices()[indexes.length - 1];

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireTableRowsInserted(firstRow, lastRow);
					}
				});
			}

			public void treeNodesRemoved(TreeModelEvent e) {
				int[] indexes = e.getChildIndices();
				final int firstRow = e.getChildIndices()[0];
				final int lastRow = e.getChildIndices()[indexes.length - 1];

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireTableRowsDeleted(firstRow, lastRow);
					}
				});
			}

			public void treeStructureChanged(TreeModelEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						//fireTableDataChanged();
						// do complete re-initialization (we do not want to)
						fireTableStructureChanged(); 
					}
				});
			}
		});
	}

	// Wrappers, implementing TableModel interface.

	public int getColumnCount() {
		return treeTableModel.getColumnCount();
	}

	public String getColumnName(int column) {
		return treeTableModel.getColumnName(column);
	}

	public Class getColumnClass(int column) {
		return treeTableModel.getColumnClass(column);
	}

	public int getRowCount() {
		return tree.getRowCount();
	}

	protected ITreeTableNode nodeForRow(int row) {
		TreePath treePath = tree.getPathForRow(row);
		//System.out.println(treePath.getLastPathComponent().getClass().getName());
		return (ITreeTableNode) treePath.getLastPathComponent();
	}

	public Object getValueAt(int row, int column) {
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}

	public boolean isCellEditable(int row, int column) {
		return treeTableModel.isCellEditable(nodeForRow(row), column);
	}

	public void setValueAt(Object value, int row, int column) {
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}

}