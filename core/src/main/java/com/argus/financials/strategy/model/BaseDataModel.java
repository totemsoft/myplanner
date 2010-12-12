/*
 * BaseDataModel.java
 *
 * Created on July 3, 2002, 10:53 AM
 */

package com.argus.financials.strategy.model;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.io.Serializable;
import java.util.logging.Logger;

import com.argus.financials.bean.AbstractBase;
import com.argus.financials.swing.table.AbstractTreeTableModel;
import com.argus.financials.swing.table.MergeSort;
import com.argus.financials.swing.table.TreeTableModel;
import com.argus.format.Currency;
import com.argus.format.Percent;
import com.argus.util.ReferenceCode;

public abstract class BaseDataModel extends AbstractTreeTableModel implements
        javax.swing.event.ChangeListener {
    // serialver -classpath . com.argus.strategy.model.BaseDataModel

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 6891372027270185711L;

    private static final Logger LOG = Logger.getLogger(BaseDataModel.class
            .getName());

    // tree table columns
    public static final int TREE_ITEM_NAME = 0;

    protected static final String SPACES = "     "; // 5 spaces

    protected static Currency curr;

    protected static Percent percent;

    static {
        curr = Currency.createCurrencyInstance();
        curr.setMinimumFractionDigits(0);
        curr.setMaximumFractionDigits(0);

        percent = Percent.createPercentInstance();
    }

    // Names of the columns.
    private static String[] cNames = { "Item Name" };

    protected String[] getColumnNames() {
        return cNames;
    }

    // Types of the columns.
    private static Class[] colTypes = { TreeTableModel.class };

    protected Class[] getColumnTypes() {
        return colTypes;
    }

    /** Creates new BaseDataModel */
    public BaseDataModel() {
        this(null);
    }

    public BaseDataModel(Object root) {
        super(root);
    }

    public void setRoot(Object root) {
        this.root = createNode(root == null ? getDefaultRoot() : root);
        ((BaseNode) getRoot()).nodeChanged();
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        // *
        Object source = changeEvent.getSource();
        if (source instanceof AbstractBase)
            if (!((AbstractBase) source).isModified())
                return;
        // */

        BaseNode[] nodes = getChildren((BaseNode) getRoot());
        for (int i = 0; i < nodes.length; i++)
            nodes[i].nodeChanged();
    }

    public Currency getCurrency() {
        return curr;
    }

    public ReferenceCode getRootObject() {
        return (ReferenceCode) ((BaseNode) getRoot()).getObject();
    }

    protected abstract Object getDefaultRoot();

    protected abstract BaseNode[] getEmptyChildren();

    protected abstract BaseNode createNode(Object obj);

    protected abstract BaseNode createNode(BaseNode parent, Object obj);

    protected BaseNode[] createNodes(int size) {
        return new BaseNode[size];
    }

    protected abstract void addDefaultNodes();

    /***************************************************************************
     * The TreeModel interface
     **************************************************************************/

    /**
     * Returns the number of children of <code>node</code>.
     */
    public int getChildCount(Object node) {
        Object[] children = getChildren(node);
        return children == null ? 0 : children.length;
    }

    /**
     * Returns the child of <code>node</code> at index <code>i</code>.
     */
    public Object getChild(Object node, int i) {
        return getChildren(node)[i];
    }

    /**
     * Returns true if the passed in object represents a leaf, false otherwise.
     */
    public boolean isLeaf(Object node) {
        if (node instanceof BaseNode)
            return ((BaseNode) node).isLeaf();
        return false;
    }

    /***************************************************************************
     * The TreeTableNode interface.
     **************************************************************************/

    /**
     * Returns the number ofs availible column.
     */
    public int getColumnCount() {
        return getColumnNames().length;
    }

    /**
     * Returns the name for column number <code>column</code>.
     */
    public String getColumnName(int column) {
        return getColumnNames()[column];
    }

    /**
     * Returns the class for the particular column.
     */
    public Class getColumnClass(int column) {
        return getColumnTypes()[column];
    }

    /**
     * Returns the value to be displayed for node <code>node</code>, at
     * column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column) {
        BaseNode _node = (BaseNode) node;

        switch (column) {
        case TREE_ITEM_NAME:
            return _node;
        default:
            ;
        }

        return null;
    }

    public void setValueAt(Object aValue, Object node, int column) {
        super.setValueAt(aValue, node, column); // do nothing

        BaseNode _node = (BaseNode) node;

        switch (column) {
        case TREE_ITEM_NAME:
            return;
        default:
            ;
        }

    }

    /**
     * By default, make the column with the Tree in it the only editable one.
     * Making this column editable causes the JTable to forward mouse and
     * keyboard events in the Tree column to the underlying JTree.
     */
    public boolean isCellEditable(Object node, int column) {
        BaseNode _node = (BaseNode) node;

        if (_node.isRoot())
            return false;
        if (column == TREE_ITEM_NAME)
            return true;
        return false;
    }

    protected BaseNode[] getChildren(Object node) {
        return node == null ? null : ((BaseNode) node).getChildren();
    }

    /**
     * 
     */
    private static int refCount = 0;

    public abstract class BaseNode implements java.util.Comparator,
            Serializable {
        // serialver -classpath .
        // com.argus.strategy.model.BaseDataModel$BaseNode

        // Compatible changes include adding or removing a method or a field.
        // Incompatible changes include changing an object's hierarchy or
        // removing the implementation of the Serializable interface.
        static final long serialVersionUID = 9059901894089630991L;

        // Parent BaseNode of the receiver.
        protected BaseNode parent;

        // Object the receiver represents.
        protected Object object;

        // Children of the receiver.
        protected java.util.ArrayList children; // BaseNode

        private int id = 0;

        protected BaseNode(Object object) {
            this(null, object);
        }

        protected BaseNode(BaseNode parent, Object object) {
            this.parent = parent;
            this.object = object;

            this.children = new java.util.ArrayList();

        }

        protected void finalize() throws Throwable {

            // if (DEBUG) System.out.println( getClass().getName() + "
            // finalized, id=" + id );

            super.finalize();

        }

        public String toString() {
            return getObject() == null ? null : getObject().toString();
        }

        public boolean isRoot() {
            return parent == null;
        }

        /**
         * Returns the parent of the receiver.
         */
        public BaseNode getParent() {
            return parent;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object value) {
            if (object == value)
                return;

            object = value;
            this.nodeChanged();
        }

        /**
         * Gets the path from the root to the receiver.
         */
        public BaseNode[] getPath() {
            return getPathToRoot(this, 0);
        }

        protected BaseNode[] getPathToRoot(BaseNode aNode, int depth) {
            BaseNode[] retNodes;

            if (aNode == null) {
                if (depth == 0)
                    return null;
                retNodes = createNodes(depth);

            } else {
                depth++;
                retNodes = getPathToRoot(aNode.getParent(), depth);
                retNodes[retNodes.length - depth] = aNode;
            }

            return retNodes;
        }

        /**
         * Returns true if the receiver represents a leaf.
         */
        public abstract boolean isLeaf();

        /**
         * Returns true if the receiver can be updated.
         */
        public boolean isEditable() {
            return false;
        }

        /**
         * Returns true if we can add new nodes to the receiver.
         */
        public boolean isAddable() {
            return !isLeaf();
        }

        /**
         * Returns true if the receiver can be removed.
         */
        public boolean isRemovable() {
            return false;
        }

        /**
         * Loads the children, caching the results in the children instance
         * variable.
         */
        public BaseNode[] getChildren() {
            if (children == null || children.size() == 0)
                return null;
            // return getEmptyChildren();

            /*
             * BaseNode [] nodes = createNodes( children.size() ); for ( int i =
             * 0; i < nodes.length; i++ ) { BaseNode node = (BaseNode)
             * children.get(i); nodes[i] = node; } return nodes;
             */

            return (BaseNode[]) children.toArray(getEmptyChildren());
        }

        /**
         * Sets the children of the receiver, updates the total size, and if
         * generateEvent is true a tree structure changed event is created.
         */
        protected void setChildren(BaseNode[] newChildren) {

            if (children != null)
                children.clear();

            for (int i = 0; i < newChildren.length; i++)
                children.add(newChildren[i]);

            fireTreeStructureChanged(BaseDataModel.this, getPath(), null, null);

        }

        /**
         * Returns the child of <code>node</code> at index <code>i</code>.
         */
        public BaseNode getChild(int index) {
            return children == null ? null : (BaseNode) children.get(index);
        }

        /**
         * Returns the number of children of <code>node</code>.
         */
        public int getChildCount() {
            return children == null ? 0 : children.size();
        }

        protected void sortChildren() {
            if (children == null || children.size() <= 1)
                return;

            BaseNode[] nodes = (BaseNode[]) children.toArray(new BaseNode[0]);

            getSizeSorter().sort(nodes);

            setChildren(nodes);

        }

        /**
         * 
         */
        private BaseNode addChild(Object obj, boolean fireEvent) {
            if (isLeaf())
                throw new IllegalArgumentException();

            // remove old node, if any
            BaseNode node = this.find(obj);
            if (node == null) {
                // add new node
                node = obj instanceof BaseNode ? (BaseNode) obj
                        : createNode(obj);
                node.parent = this;

                if (children == null)
                    children = new java.util.ArrayList();
                children.add(node);

                if (fireEvent)
                    node.nodeInserted();

            } else {
                node.object = obj;

                if (fireEvent)
                    node.nodeChanged();

            }

            return node;

        }

        public BaseNode addChild(Object obj) {
            return addChild(obj, true);
        }

        public void remove(boolean fireEvent) {
            if (parent == null)
                throw new IllegalArgumentException();

            /*
             * do not need this (removed anyway) if ( children != null ) {
             * java.util.Iterator iter = children.iterator(); while (
             * iter.hasNext() ) ( (BaseNode) iter.next() ).remove( fireEvent ); }
             */

            if (fireEvent)
                nodeRemoved(); // VSH: it has to be here (before remove(this))!
                                // ( nodeRemoved()::int [] index = {
                                // getIndexOfChild(parent, this) }; )
            // parent.children.remove( this ); // lets do it in nodeRemoved()

        }

        public void remove() {
            remove(true);
        }

        // try to compare String representation of this node and passed obj
        public BaseNode find(Object obj) {
            // if (DEBUG) System.out.println( obj.toString() );
            if (children == null || obj == null || obj.toString() == null)
                return null;

            java.util.Iterator iter = children.iterator();
            while (iter.hasNext()) {
                BaseNode node = (BaseNode) iter.next();
                // if (DEBUG) System.out.println( "\t" + node.toString() );
                // if ( node != null && obj.toString().equals( node.toString() )
                // )
                if (compare(obj, node.getObject()) == 0)
                    return node;
            }

            return null;

        }

        public int compare(Object o1, Object o2) {

            if (o1 == null && o2 == null)
                return 0;
            if (o1 == null)
                return -1;
            if (o2 == null)
                return 1;

            if (o1 instanceof AbstractBase && o2 instanceof AbstractBase)
                return _compare((AbstractBase) o1, (AbstractBase) o2);

            if (o1 instanceof ReferenceCode && o2 instanceof ReferenceCode)
                return _compare((ReferenceCode) o1, (ReferenceCode) o2);

            return _compare(o1.toString(), o2.toString());

        }

        protected int _compare(AbstractBase bo1, AbstractBase bo2) {
            if (bo1 == null && bo2 == null)
                return 0;
            if (bo1 == null)
                return -1;
            if (bo2 == null)
                return 1;

            return bo1.equals(bo2) ? 0 : -1;
        }

        protected int _compare(ReferenceCode rc1, ReferenceCode rc2) {
            if (rc1 == null && rc2 == null)
                return 0;
            if (rc1 == null)
                return -1;
            if (rc2 == null)
                return 1;

            return rc1.equals(rc2) ? 0 : -1;
        }

        protected int _compare(Number n1, Number n2) {
            if (n1 == null && n2 == null)
                return 0;
            if (n1 == null)
                return -1;
            if (n2 == null)
                return 1;

            double result = n1.doubleValue() - n2.doubleValue();
            if (result < 0)
                return -1;
            if (result > 0)
                return 1;
            return 0;
        }

        protected int _compare(String s1, String s2) {
            if (s1 == null && s2 == null)
                return 0;
            if (s1 == null)
                return -1;
            if (s2 == null)
                return 1;

            int result = s1.compareToIgnoreCase(s2);
            if (result < 0)
                return -1;
            if (result > 0)
                return 1;
            return 0;
        }

        /**
         * Can be invoked when a node has changed, will create the appropriate
         * event.
         */
        public void nodeChanged() {
            if (parent == null)
                return;

            // javax.swing.SwingUtilities.invokeLater( new Runnable() { public
            // void run() {
            if (isLeaf())
                parent.sortChildren();
            // } } );

            BaseNode[] path = parent.getPath();
            int[] index = { getIndexOfChild(parent, this) };
            // if ( index[0] < 0 ) return;
            Object[] children = { this };

            fireTreeNodesChanged(BaseDataModel.this, path, index, children);

        }

        protected void nodeInserted() {
            if (parent == null)
                return;

            // javax.swing.SwingUtilities.invokeLater( new Runnable() { public
            // void run() {
            if (isLeaf())
                parent.sortChildren();
            // } } );

            BaseNode[] path = parent.getPath();
            int[] index = { getIndexOfChild(parent, this) };
            // if ( index[0] < 0 ) return;
            Object[] children = { this };

            fireTreeNodesInserted(BaseDataModel.this, path, index, children);

        }

        protected void nodeRemoved() {
            if (parent == null)
                return;

            // javax.swing.SwingUtilities.invokeLater( new Runnable() { public
            // void run() {
            // if ( isLeaf() )
            // parent.sortChildren();
            // } } );

            BaseNode[] path = parent.getPath();
            int[] index = { getIndexOfChild(parent, this) };
            // if ( index[0] < 0 ) return;
            Object[] children = { this };

            parent.children.remove(this);

            fireTreeNodesRemoved(BaseDataModel.this, path, index, children);

        }

        // public void nodeStructureChanged() {
        // fireTreeStructureChanged( BaseDataModel.this, getPath(), null, null);
        // }

    }

    /**
     * Sorts the contents, which must be instances of FileNode based on
     * alfabetical order.
     */
    protected static MergeSort getSizeSorter() {
        return new AlfaSorter();
    }

    static class AlfaSorter extends MergeSort {

        public int compareElementsAt(int beginLoc, int endLoc) {
            BaseNode node1 = (BaseNode) toSort[beginLoc];
            BaseNode node2 = (BaseNode) toSort[endLoc];

            // non-leaf nodes has to be first
            if (!node1.isLeaf()) {
                if (!node2.isLeaf())
                    return compare(node1.toString(), node2.toString());
                return -1;
            }

            if (!node2.isLeaf()) {
                if (!node1.isLeaf())
                    return compare(node2.toString(), node1.toString());
                return 1;
            }

            return compare(node1.toString(), node2.toString());
        }

        private int compare(String s1, String s2) {
            if (s1 == null && s2 == null)
                return 0;
            if (s1 == null)
                return -1;
            if (s2 == null)
                return 1;

            int result = s1.compareToIgnoreCase(s2);
            if (result < 0)
                return -1;
            if (result > 0)
                return 1;
            return 0;
        }

    }

    /***************************************************************************
     * javax.swing.event.TreeExpansionListener interface
     * 
     * public class TreeExpansionListener_impl implements
     * javax.swing.event.TreeExpansionListener {
     * 
     * public void treeExpanded(javax.swing.event.TreeExpansionEvent
     * treeExpansionEvent) { System.out.println( "Tree-expanded event detected." ); }
     * 
     * public void treeCollapsed(javax.swing.event.TreeExpansionEvent
     * treeExpansionEvent) { System.out.println( "Tree-collapsed event
     * detected." ); }
     *  }
     */

    /***************************************************************************
     * javax.swing.event.TreeWillExpandListener interface
     * 
     * public class TreeWillExpandListener_impl implements
     * javax.swing.event.TreeWillExpandListener {
     *  // Invoked whenever a node in the tree is about to be expanded. public
     * void treeWillExpand(javax.swing.event.TreeExpansionEvent
     * treeExpansionEvent) throws javax.swing.tree.ExpandVetoException {
     * System.out.println( "Tree-will-expand event detected" );
     * 
     * TreePath tp = treeExpansionEvent.getPath(); BaseNode fn = (BaseNode)
     * tp.getPathComponent( tp.getPathCount() - 1 ); // if ( !fn.isSelected() ) {
     * //User said cancel expansion. //System.out.println( "Tree expansion
     * cancelled" ); //throw new javax.swing.tree.ExpandVetoException(
     * treeExpansionEvent ); //}
     *  }
     *  // Invoked whenever a node in the tree is about to be collapsed. public
     * void treeWillCollapse(javax.swing.event.TreeExpansionEvent
     * treeExpansionEvent) throws javax.swing.tree.ExpandVetoException {
     * System.out.println( "Tree-will-collapse event detected." ); }
     *  }
     */

}
