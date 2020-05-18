/*
 * Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd License
 * Version 1.0 (the "License"). You may not use this file except in compliance
 * with the License. A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original Code is
 * Argus Software Pty Ltd, All Rights Reserved.
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

import javax.swing.tree.TreeNode;

public interface ITreeTableNode extends TreeNode {

	/**
	 * Returns the value for the cell at columnIndex. Parameters: columnIndex -
	 * the column whose value is to be queried Returns: the value Object at the
	 * specified cell
	 */
	public Object getValueAt(int columnIndex);

	/**
	 * Sets the value in the cell at columnIndex to aValue. Parameters: aValue -
	 * the new value columnIndex - the column whose value is to be changed
	 */
	public void setValueAt(Object aValue, int columnIndex);

	/**
	 * Returns true if the cell at columnIndex is editable. Otherwise,
	 * setValueAt on the cell will not change the value of that cell.
	 * Parameters: columnIndex - the column whose value to be queried Returns:
	 * true if the cell is editable
	 */
	public boolean isCellEditable(int columnIndex);

}

