/*
 * FinancialTreeModel.java
 *
 * Created on 21 March 2003, 13:50
 */

package com.argus.financials.tree;

/**
 * 
 * @author valeri chibaev
 */

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import com.argus.financials.bean.Financial;
import com.argus.financials.bean.Financials;
import com.argus.financials.swing.table.AlfaSorter;

public class FinancialTreeModel
        // extends javax.swing.tree.DefaultTreeModel
        implements javax.swing.tree.TreeModel,
        javax.swing.event.TreeModelListener, java.lang.Cloneable,
        java.io.Serializable {
    private static final com.argus.math.Money MONEY = new com.argus.math.Money();

    private EventListenerList listenerList = new EventListenerList();

    private GroupNode root;

    private final java.util.Collection financials = new java.util.ArrayList();

    private int[] rules;

    private FinancialTreeStructure structure;

    private FinancialTreeFilters filters;

    /** Creates a new instance of FinancialTreeModel */
    public FinancialTreeModel() {
        root = new GroupNode(null, new FinancialTreeFilter());
    }

    public FinancialTreeModel(java.util.Map financials) {
        this(financials, null);
    }

    public FinancialTreeModel(java.util.Map financials,
            DataGenerator dataGenerator) {
        root = new GroupNode(null, new FinancialTreeFilter());
        this.dataGenerator = dataGenerator;
        setFinancials(financials);
    }

    protected FinancialTreeModel(FinancialTreeModel ftm) {

        // !!! has to be a new root
        root = new GroupNode(null, new FinancialTreeFilter());

        if (ftm.rules != null) {
            rules = new int[ftm.rules.length];
            System.arraycopy(ftm.rules, 0, rules, 0, ftm.rules.length);
            // System.out.println( "\t*****\trules=" + rules + ", length=" +
            // rules.length );
        }

        structure = new FinancialTreeStructure(ftm.structure);

        filters = new FinancialTreeFilters(ftm.filters);

        dataGenerator = ftm.dataGenerator;

        setFinancials(ftm);

    }

    // //////////////////////////////////////////////////////////////////////////
    //
    // //////////////////////////////////////////////////////////////////////////
    private DataGenerator dataGenerator = new DataGenerator() {

        public void addGeneratedData(FinancialTreeModel model,
                java.util.Map source, final int[] rules, int year) {
            Financials.addGeneratedData(source, rules, year);
        }

        public void addGeneratedData(FinancialTreeModel model,
                java.util.Collection source, final int[] rules, int year) {
            Financials.addGeneratedData(source, rules, year);
        }

    };

    public DataGenerator getDataGenerator() {
        return dataGenerator;
    }

    public void setDataGenerator(DataGenerator value) {
        dataGenerator = value;
    }

    /***************************************************************************
     * override methods
     **************************************************************************/

    /***************************************************************************
     * get/set
     **************************************************************************/
    public int[] getRules() {
        return rules;
    }

    public void setRules(int[] value) {
        rules = value;
    }

    public FinancialTreeStructure getStructure() {
        return structure;
    }

    public void setStructure(FinancialTreeStructure value) {
        structure = value;
    }

    public FinancialTreeFilters getFilters() {
        if (filters == null)
            filters = new FinancialTreeFilters();
        return filters;
    }

    public void setFilters(FinancialTreeFilters value) {
        filters = value;
    }

    public java.util.Collection getFinancials() {
        return financials;
    }

    public void setFinancials(FinancialTreeModel value) {
        this.financials.clear();
        if (value != null)
            this.financials.addAll(value.financials);
    }

    public void setFinancials(java.util.Map value) {

        this.financials.clear();
        if (value != null) {

            if (getDataGenerator() != null)
                getDataGenerator().addGeneratedData(this, value, rules, 0);

            // convert map to list of Financial items
            java.util.Iterator iter = value.values().iterator();
            while (iter.hasNext()) {
                java.util.Map map = (java.util.Map) iter.next();
                if (map == null)
                    continue;

                java.util.Iterator iter2 = map.values().iterator();
                while (iter2.hasNext()) {
                    Financial f = (Financial) iter2.next();
                    if (f != null)
                        financials.add(f);

                }

            }

            // System.out.println( "BEFORE SORT: " + financials );

            // sort financials
            sort(financials);

            // System.out.println( "AFTER SORT: " + financials );

        }

    }

    /***************************************************************************
     * Sorts the contents, based on alfabetical order.
     **************************************************************************/
    protected void sort(java.util.Collection c) {
        new AlfaSorter().sort(c);
    }

    /***************************************************************************
     * operations
     **************************************************************************/
    public void pack() {
        root.pack();
    }

    public void build() {
        build(0); // current year
    }

    public void build(int year) {

        if (getDataGenerator() != null)
            getDataGenerator().addGeneratedData(this, financials, rules, year);

        // remove generated financials, where amount = 0
        java.util.Iterator iter = financials.iterator();
        while (iter.hasNext()) {
            Financial f = (Financial) iter.next();
            if (f == null)
                continue;

            java.math.BigDecimal amount = f.getAmount();
            if (amount == null || amount.doubleValue() == 0.)
                iter.remove();
        }

        applyStructure();
        applyFilter();
        treeChanged();

    }

    public FinancialTreeModel buildCopy() {
        return buildCopy(0); // current year
    }

    public FinancialTreeModel buildCopy(int year) {
        FinancialTreeModel model = new FinancialTreeModel(this);
        model.build(year);
        return model;
    }

    private void applyStructure() {

        // clear previous structure
        root.children.clear();

        // create groups
        addFinancials();

        // remove groups with no children ( empty nodes )
        pack();

    }

    private void applyFilter() {

        // apply filter (if any)
        if (filters == null || filters.size() == 0)
            return;

        int count = getRowCount();
        for (int i = count - 1; i >= 0; i--) {
            INode n = getRowAt(i);
            if (!n.isLeaf())
                continue;

            java.util.Iterator iter = filters.iterator();
            while (iter.hasNext()) {
                FinancialTreeFilter filter = (FinancialTreeFilter) iter.next();
                Financial f = (Financial) ((Node) n).getFinancial();
                if (!filter.inGroup(f))
                    n.remove();

            }

        }

        // remove groups with no children ( empty nodes )
        pack();

    }

    public void addFinancial(Financial f) {
        financials.add(f);
        // build();
    }

    private void addFinancials() {
        if (structure == null || structure.size() == 0)
            root.addFinancials();
        else
            addFinancials(root, 0);
    }

    private void addFinancials(GroupNode parent, int level) {

        if (structure.size() == level)
            return;

        Object group = structure.toArray()[level];
        // System.out.println( "\ngroup=" + group );
        level++;

        Object[] groupItems = FinancialTreeFilter.getGroupItems(group);
        for (int j = 0; j < groupItems.length; j++) {
            Object groupItem = groupItems[j];
            if ("".equals(groupItem))
                continue;

            GroupNode groupNode = new GroupNode(parent,
                    new FinancialTreeFilter(parent.getFinancialTreeFilter(),
                            group, groupItem));

            if (structure.size() > level)
                addFinancials(groupNode, level);
            else
                groupNode.addFinancials();

        }

    }

    /***************************************************************************
     * methods used in TableModel/ISmartTableModel interface
     **************************************************************************/
    public int getRowCount() {
        return getChildCount(root);
    }

    private int getChildCount(INode node) {

        if (node.isLeaf())
            return 0;

        int count = node.getChildCount();

        INode[] nodes = node.getChildren();
        for (int i = 0; i < nodes.length; i++)
            count += getChildCount(nodes[i]);

        return count;

    }

    public INode getRowAt(int rowIndex) {
        return getRowAt(rowIndex, root);
    }

    private INode getRowAt(int rowIndex, INode node) {

        INode[] nodes = node.getChildren();
        if (nodes == null) // a leaf or footer (no children)
            return null;

        for (int i = 0; i < nodes.length; i++) {

            if (rowIndex-- == 0)
                return nodes[i];

            node = getRowAt(rowIndex, nodes[i]);
            if (node != null)
                return node;

            rowIndex -= getChildCount(nodes[i]);

        }

        return null;

    }

    /***************************************************************************
     * javax.swing.tree.TreeModel interface
     **************************************************************************/
    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        return ((INode) node).isLeaf();
    }

    public int getIndexOfChild(Object parent, Object child) {

        for (int i = 0; i < getChildCount(parent); i++)
            if (getChild(parent, i).equals(child))
                return i;
        return -1;

    }

    public int getChildCount(Object node) {
        Object[] children = getChildren((INode) node);
        return (children == null) ? 0 : children.length;
    }

    public Object getChild(Object node, int i) {
        return getChildren((INode) node)[i];
    }

    public void addTreeModelListener(TreeModelListener treeModelListener) {
        listenerList.add(TreeModelListener.class, treeModelListener);
    }

    public void removeTreeModelListener(TreeModelListener treeModelListener) {
        listenerList.remove(TreeModelListener.class, treeModelListener);
    }

    public void valueForPathChanged(TreePath treePath, Object obj) {
        // do nothing
    }

    /*
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        fireTreeNodesChanged(new TreeModelEvent(source, path, childIndices,
                children));
    }

    protected void fireTreeNodesChanged(TreeModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == TreeModelListener.class)
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
    }

    protected void fireTreeNodesInserted(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        fireTreeNodesInserted(new TreeModelEvent(source, path, childIndices,
                children));
    }

    protected void fireTreeNodesInserted(TreeModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == TreeModelListener.class)
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        fireTreeNodesRemoved(new TreeModelEvent(source, path, childIndices,
                children));
    }

    protected void fireTreeNodesRemoved(TreeModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == TreeModelListener.class)
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
    }

    public void treeChanged() {
        fireTreeStructureChanged(this, root.getPath(), null, null);
    }

    protected void fireTreeStructureChanged(Object source, Object[] path,
            int[] childIndices, Object[] children) {
        fireTreeStructureChanged(new TreeModelEvent(source, path, childIndices,
                children));
    }

    protected void fireTreeStructureChanged(TreeModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == TreeModelListener.class)
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
    }

    protected INode[] getChildren(INode node) {
        return node == null ? null : node.getChildren();
    }

    // */

    /***************************************************************************
     * javax.swing.event.TreeModelListener interface
     **************************************************************************/
    public void treeNodesChanged(javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTreeModel::treeNodesChanged( " +
        // treeModelEvent + " )" );
        setFinancials((FinancialTreeModel) treeModelEvent.getSource());
        build();
        fireTreeNodesChanged(treeModelEvent);
    }

    public void treeNodesInserted(
            javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTreeModel::treeNodesInserted( "
        // + treeModelEvent + " )" );
        setFinancials((FinancialTreeModel) treeModelEvent.getSource());
        build();
        fireTreeNodesInserted(treeModelEvent);
    }

    public void treeNodesRemoved(javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTreeModel::treeNodesRemoved( " +
        // treeModelEvent + " )" );
        setFinancials((FinancialTreeModel) treeModelEvent.getSource());
        build();
        fireTreeNodesRemoved(treeModelEvent);
    }

    public void treeStructureChanged(
            javax.swing.event.TreeModelEvent treeModelEvent) {
        // System.out.println( "*****\tFinancialTreeModel::treeStructureChanged(
        // " + treeModelEvent + " )" );
        setFinancials((FinancialTreeModel) treeModelEvent.getSource());
        build();
        fireTreeStructureChanged(treeModelEvent);
        // System.out.println( "******\t4. RowCount=" + getRowCount() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public interface INode { // extends TreeNode {

        public boolean isLeaf();

        public INode getParent();

        public INode[] getChildren();

        public int getChildCount();

        public int getLevel();

        public void remove();

        public void pack();

    }

    abstract class AbstractNode implements INode {

        // Parent Node of the receiver.
        protected GroupNode parent;

        protected AbstractNode(GroupNode parent) {

            this.parent = parent;

            if (this.parent != null)
                this.parent.children.add(this);

        }

        /**
         * Returns the parent of the receiver.
         */
        public INode getParent() {
            return parent;
        }

        public boolean isRoot() {
            return parent == null;
        }

        public int getLevel() {
            int level = 0;

            INode p = parent;
            while ((p = p.getParent()) != null)
                level++;

            return level;
        }

        public void remove() {
            if (parent != null)
                parent.children.remove(this);
        }

        public INode[] getChildren() {
            return null;
        }

        public int getChildCount() {
            return 0;
        }

        public void pack() {
        }

        /**
         * Gets the path from the root to the receiver.
         */
        public INode[] getPath() {
            return getPathToRoot(this, 0);
        }

        protected INode[] getPathToRoot(INode aNode, int depth) {
            INode[] retNodes;

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

    }

    protected INode[] createNodes(int size) {
        return new INode[size];
    }

    /***************************************************************************
     * A GroupNode is a derivative of the ReferenceCode class - though we
     * delegate to the FinancialGroup object rather than subclassing it. It is
     * used to maintain a cache of a directory's children and therefore avoid
     * repeated access to the underlying file system during rendering.
     **************************************************************************/
    public class GroupNode extends AbstractNode {

        // the receiver represents.
        protected FinancialTreeFilter groupFilter;

        // Children of the receiver.
        private java.util.Collection children;

        protected GroupNode(GroupNode parent, FinancialTreeFilter groupFilter) {

            super(parent);

            this.groupFilter = groupFilter;
            children = new java.util.Vector();

        }

        public String toString() {
            return groupFilter == null ? "<GROUP>" : groupFilter.toString();
        }

        /**
         * Returns the Financial the receiver represents.
         */
        public FinancialTreeFilter getFinancialTreeFilter() {
            return groupFilter;
        }

        public boolean isLeaf() {
            return false;
        }

        public INode[] getChildren() {
            return (INode[]) children.toArray(new INode[0]);
        }

        public int getChildCount() {
            return children.size();
        }

        public void pack() {

            Object[] nodes = children.toArray();
            for (int i = 0; i < nodes.length; i++) {
                INode node = (INode) nodes[i];

                if (!node.isLeaf()) {
                    node.pack();

                    if (node.getChildCount() == 0)
                        node.remove();
                }

            }

        }

        private void addFinancials() {

            if (financials == null)
                return;

            // java.math.BigDecimal totalAmount = null;

            java.util.Iterator iter = financials.iterator();
            while (iter.hasNext()) {
                Financial f = (Financial) iter.next();
                if (isRoot() || getFinancialTreeFilter().inGroup(f)) {
                    new Node(this, f);

                    // if ( totalAmount == null )
                    // totalAmount = new java.math.BigDecimal(0.);
                    // totalAmount = totalAmount.add( f.getAmount() );
                }

            }

            // if ( totalAmount != null )
            // new GroupFooterNode( this, this.groupFilter, totalAmount );

        }

    }

    /*
     * public class GroupFooterNode extends AbstractNode {
     * 
     * private java.math.BigDecimal totalAmount; // the receiver represents.
     * protected FinancialTreeFilter groupFilter;
     * 
     * protected GroupFooterNode( GroupNode parent, FinancialTreeFilter
     * groupFilter, java.math.BigDecimal totalAmount ) {
     * 
     * super( parent );
     * 
     * this.groupFilter = groupFilter; this.totalAmount = totalAmount;
     *  }
     * 
     * public String toString() { return totalAmount == null ? "<Total>" :
     * "Total=" + MONEY.toString( totalAmount ); }
     * 
     * public boolean isLeaf() { return false; }
     * 
     * public void remove() {}
     *  }
     */

    /***************************************************************************
     * A Node is a derivative of the Financial class - though we delegate to the
     * Financial object rather than subclassing it. It is used to maintain a
     * cache of a directory's children and therefore avoid repeated access to
     * the underlying file system during rendering.
     **************************************************************************/
    public class Node extends AbstractNode {

        // java.io.File the receiver represents.
        protected Financial f;

        protected Node(GroupNode parent, Financial f) {

            super(parent);

            this.f = f;

        }

        public String toString() {
            return f == null ? "<FINANCIAL>" : f.toString();
        }

        /**
         * Returns the Financial the receiver represents.
         */
        public Financial getFinancial() {
            return f;
        }

        public boolean isLeaf() {
            return true;
        }

    }

}
