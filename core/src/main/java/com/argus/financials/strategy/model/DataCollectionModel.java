/*
 * DataCollectionModel.java
 *
 * Created on July 3, 2002, 10:53 AM
 */

package com.argus.financials.strategy.model;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import com.argus.financials.bean.Asset;
import com.argus.financials.bean.Assets;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialTotals;
import com.argus.financials.bean.Financials;
import com.argus.financials.bean.ICashFlow;
import com.argus.financials.bean.Liability;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.service.PersonService;
import com.argus.swing.table.TreeTableModel;
import com.argus.util.ReferenceCode;

public class DataCollectionModel extends FinancialDataModel {

    static final long serialVersionUID = 1558898383495637707L;

    public static final ReferenceCode DEFAULT_ROOT = new ReferenceCode(0, "Collection Data");

    private static Node1[] EMPTY_CHILDREN = new Node1[0];

    // tree table columns
    // public static final int TREE_ITEM_NAME = 0;
    public static final int SERVICE = 1;

    public static final int AMOUNT = 2;

    public static final int BALANCE = 3;

    public static final int AMOUNT_2_USE = 4;

    public static final int SELECTED = 5;

    protected int getColumn2UpdateOnSelect() {
        return AMOUNT_2_USE;
    }

    protected static final int AMOUNT_TOTAL = 0;

    protected static final int BALANCE_TOTAL = 1;

    protected static final int AMOUNT_2_USE_TOTAL = 2;

    protected static final int AMOUNT_2_USE_INITIAL = 3;

    protected static final int AMOUNT_2_USE_CONTRIBUTION = 4;

    protected int getCountTotal() {
        return 5;
    }

    // Names of the columns.
    private static String[] cNames = { "Item Name", "Service", "Amount",
            "Unallocated", "Amount to use", "Use" };

    protected String[] getColumnNames() {
        return cNames;
    }

    // Types of the columns.
    private static Class[] colTypes = { TreeTableModel.class,
            java.lang.String.class, java.math.BigDecimal.class,
            java.math.BigDecimal.class, java.math.BigDecimal.class,
            Boolean.class };

    protected Class[] getColumnTypes() {
        return colTypes;
    }

    /** Creates new DataCollectionModel */
    public DataCollectionModel() {
        this(null);
    }

    public DataCollectionModel(Object root) {
        super(root);
    }

    public Assets getAssets() {
        if (assets == null)
            assets = new Assets(getFinancials());
        return assets;
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        super.stateChanged(changeEvent);

    }

    public Object getDefaultRoot() {
        return DEFAULT_ROOT;
    }

    protected BaseNode[] getEmptyChildren() {
        return EMPTY_CHILDREN;
    }

    protected BaseNode createNode(Object obj) {
        return new Node1(obj);
    }

    protected BaseNode createNode(BaseNode parent, Object obj) {
        return new Node1(parent, obj);
    }

    protected BaseNode[] createNodes(int size) {
        return new Node1[size];
    }

    public java.util.Collection getChildren(Boolean selected) {
        return ((Node1) getRoot()).getChildren(selected);
    }

    /***************************************************************************
     * The TreeTableNode interface.
     **************************************************************************/

    /**
     * Returns the value to be displayed for node <code>node</code>, at
     * column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column) {
        Node1 _node = (Node1) node;
        FinancialTotals ft = _node.getTotals();
        boolean calcTotals = _node.calcTotals();

        switch (column) {
        case TREE_ITEM_NAME:
            return _node;
        case SERVICE:
            if (_node.isFinancial()) {
                String service = _node.getFinancialObject()
                        .getFinancialServiceCode();
                return service == null ? null : SPACES + service; // TODO: do
                                                                    // cell
                                                                    // render
                                                                    // later
            }
            return null;
        case AMOUNT:
            if (_node.isRoot())
                return null;
            if (_node.isFinancial())
                return curr
                        .toString(_node.getFinancialObject().getAmount(true));
            return calcTotals ? curr.toString(ft.getTotal(AMOUNT_TOTAL)) : null;
        case BALANCE:
            if (_node.isRoot())
                return null;
            if (_node.isFinancial())
                return curr.toString(_node.getFinancialObject()
                        .getBalanceAmount(true));
            return calcTotals ? curr.toString(ft.getTotal(BALANCE_TOTAL))
                    : null;
        case AMOUNT_2_USE:
            if (_node.isRoot())
                return null;
            if (_node.isFinancial())
                return curr.toString(_node.getUsedAmount(true));
            return calcTotals ? curr.toString(ft.getTotal(AMOUNT_2_USE_TOTAL))
                    : null;
        case SELECTED:
            if (_node.isFinancial())
                return _node.isSelected() ? Boolean.TRUE : Boolean.FALSE;
        default:
            ;
        }

        return null;
    }

    public void setValueAt(Object aValue, Object node, int column) {
        // super.setValueAt( aValue, node, column ); // do nothing

        Node1 _node = (Node1) node;

        switch (column) {
        case TREE_ITEM_NAME:
            return;
        case SERVICE:
            return;
        case AMOUNT:
            return;
        case BALANCE:
            return;
        case AMOUNT_2_USE:
            if (_node.isFinancial()) {
                java.math.BigDecimal usedAmount;

                if (aValue instanceof java.math.BigDecimal)
                    usedAmount = (java.math.BigDecimal) aValue;
                else
                    usedAmount = aValue == null ? null : curr
                            .getBigDecimalValue(aValue.toString());

                _node.setUsedAmount(usedAmount);
            }
            return;
        case SELECTED:
            if (_node.isFinancial())
                _node.setSelected(((Boolean) aValue).booleanValue());
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
        Node1 _node = (Node1) node;

        if (_node.isRoot())
            return false;
        if (column == TREE_ITEM_NAME)
            return true;
        if (_node.isFinancial()
                && (column == SELECTED || column == AMOUNT_2_USE))
            return true;
        return false;
    }

    /**
     * 
     */
    public class Node1 extends Node {

        static final long serialVersionUID = 3630625802921675281L;

        // True if this node is selected
        private boolean selected;

        protected Node1(Object object) {
            this(null, object);
        }

        protected Node1(BaseNode parent, Object object) {
            super(parent, object);
        }

        /**
         * Returns true if node selected.
         */
        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean value) {
            if (selected == value)
                return;

            selected = value;

            if (isFinancial()) {
                java.math.BigDecimal amount2use = null;

                if (selected) {
                    amount2use = this.getUsedAmount();// true );
                    if (amount2use == null)
                        amount2use = getFinancialObject().getBalanceAmount();// true
                                                                                // );
                }
                getFinancialObject().setUsedAmount(amount2use);
                // setValueAt( amount2use, this, getColumn2UpdateOnSelect() );
            }

            nodeChanged();

        }

        public void selectAll(boolean value) {
            if (selected == value)
                return;

            selected = value;
            if (!this.isLeaf()) {
                BaseNode[] nodes = this.getChildren();
                if (nodes == null)
                    return;

                for (int i = 0; i < nodes.length; i++)
                    ((Node1) nodes[i]).selectAll(value);
            }

            nodeChanged();
        }

        /*
         * 
         */
        public java.util.Collection getChildren(Boolean selected) {

            if (children == null || children.size() == 0)
                return null;

            java.util.ArrayList list = new java.util.ArrayList();

            for (int i = 0; i < children.size(); i++) {
                Node1 node = (Node1) children.get(i);

                if (node.isFinancial()
                        && node.getFinancialObject().isGenerated())
                    continue;

                if (node.isLeaf()) {
                    if (selected == null)
                        list.add(node); // ALL
                    else if (selected.booleanValue() && node.isSelected())
                        list.add(node); // ONLY selected
                    else if (!selected.booleanValue() && !node.isSelected())
                        list.add(node); // ONLY not selected

                } else {
                    java.util.Collection c = node.getChildren(selected);
                    if (c != null && c.size() > 0)
                        list.addAll(c);
                }
            }

            return list;

        }

        public BaseNode addChild(Object obj) {

            return super.addChild(obj);

        }

        /*
         * 
         */
        public void updateTotal_Down() {

            if (isFinancial())
                return; // break condition

            // update totals
            java.math.BigDecimal totalAmount = Financial.ZERO;
            java.math.BigDecimal totalBalanceAmount = Financial.ZERO;
            // used
            java.math.BigDecimal totalInitialAmount = Financial.ZERO;
            java.math.BigDecimal totalContributionAmount = Financial.ZERO;

            java.util.Iterator iter = children.iterator();
            while (iter.hasNext()) {
                Node node = (Node) iter.next();

                java.math.BigDecimal amount = null;
                java.math.BigDecimal balanceAmount = null;
                java.math.BigDecimal amount2use = null;

                if (node.isFinancial()) { // isLeaf()

                    Financial f = node.getFinancialObject();
                    amount = f.getAmount(true);
                    balanceAmount = f.getBalanceAmount(true);
                    amount2use = node.getUsedAmount(true);

                    if (f instanceof Asset || f instanceof Liability) {
                        if (amount2use != null)
                            totalInitialAmount = totalInitialAmount
                                    .add(amount2use);
                    } else if (f instanceof RegularIncome
                            || f instanceof RegularExpense) {
                        totalAmount = null;
                        totalBalanceAmount = null;
                        totalInitialAmount = null;
                        totalContributionAmount = null;
                        continue;
                    } else {
                        continue;
                    }

                } else {
                    node.updateTotal_Down();

                    FinancialTotals ft = node.getTotals();
                    if (ft == null)
                        continue;

                    amount = ft.getTotal(AMOUNT_TOTAL);
                    balanceAmount = ft.getTotal(BALANCE_TOTAL);
                    amount2use = ft.getTotal(AMOUNT_2_USE_TOTAL);

                    if (amount2use == null)
                        totalInitialAmount = null;
                    else if (totalInitialAmount != null)
                        totalInitialAmount = totalInitialAmount.add(amount2use);

                }

                if (totalAmount != null && amount != null)
                    totalAmount = totalAmount.add(amount);
                if (totalBalanceAmount != null && balanceAmount != null)
                    totalBalanceAmount = totalBalanceAmount.add(balanceAmount);

            }

            // update totals
            FinancialTotals ft = getTotals();
            if (ft == null)
                return;

            ft.setTotal(AMOUNT_TOTAL, totalAmount); // under advice
            ft.setTotal(BALANCE_TOTAL, totalBalanceAmount); // balance

            if (totalInitialAmount == null || totalContributionAmount == null)
                ft.setTotal(AMOUNT_2_USE_TOTAL, null);
            else
                ft.setTotal(AMOUNT_2_USE_TOTAL, totalInitialAmount
                        .add(totalContributionAmount)); // amount

            ft.setTotal(AMOUNT_2_USE_INITIAL, totalInitialAmount);
            ft.setTotal(AMOUNT_2_USE_CONTRIBUTION, totalContributionAmount);

            // nodeChanged( false ); // to trigger parent.nodeChanged()

        }

        public void updateTotal() {

            if (isFinancial())
                return; // break condition

            // update totals
            java.math.BigDecimal totalAmount = Financial.ZERO;
            java.math.BigDecimal totalBalanceAmount = Financial.ZERO;
            // used
            java.math.BigDecimal totalInitialAmount = Financial.ZERO;
            java.math.BigDecimal totalContributionAmount = Financial.ZERO;

            java.util.Iterator iter = children.iterator();
            while (iter.hasNext()) {
                Node1 node = (Node1) iter.next();

                java.math.BigDecimal amount = null;
                java.math.BigDecimal balanceAmount = null;
                java.math.BigDecimal amount2use = null;

                if (node.isFinancial()) {

                    Financial f = node.getFinancialObject();
                    amount = f.getAmount(true);
                    balanceAmount = f.getBalanceAmount(true);
                    amount2use = node.getUsedAmount(true);

                    if (f instanceof Asset || f instanceof Liability) {
                        if (amount2use != null)
                            totalInitialAmount = totalInitialAmount
                                    .add(amount2use);
                    } else if (f instanceof RegularIncome
                            || f instanceof RegularExpense) {
                        totalAmount = null;
                        totalBalanceAmount = null;
                        totalInitialAmount = null;
                        totalContributionAmount = null;
                        // if ( amount2use != null )
                        // totalContributionAmount =
                        // totalContributionAmount.add( amount2use );
                    } else {
                        continue;
                    }

                } else {
                    FinancialTotals ft = node.getTotals();
                    if (ft == null)
                        continue;

                    amount = ft.getTotal(AMOUNT_TOTAL);
                    balanceAmount = ft.getTotal(BALANCE_TOTAL);
                    amount2use = ft.getTotal(AMOUNT_2_USE_TOTAL);

                    if (amount2use == null)
                        totalInitialAmount = null;
                    else if (totalInitialAmount != null)
                        totalInitialAmount = totalInitialAmount.add(amount2use);
                }

                if (totalAmount != null && amount != null)
                    totalAmount = totalAmount.add(amount);
                if (totalBalanceAmount != null && balanceAmount != null)
                    totalBalanceAmount = totalBalanceAmount.add(balanceAmount);

            }

            // update totals
            FinancialTotals ft = getTotals();
            if (ft == null)
                return;

            ft.setTotal(AMOUNT_TOTAL, totalAmount);
            ft.setTotal(BALANCE_TOTAL, totalBalanceAmount); // balance

            if (totalInitialAmount == null || totalContributionAmount == null)
                ft.setTotal(AMOUNT_2_USE_TOTAL, null);
            else
                ft.setTotal(AMOUNT_2_USE_TOTAL, totalInitialAmount
                        .add(totalContributionAmount)); // amount

            ft.setTotal(AMOUNT_2_USE_INITIAL, totalInitialAmount);
            ft.setTotal(AMOUNT_2_USE_CONTRIBUTION, totalContributionAmount);

            nodeChanged(false); // to trigger parent.nodeChanged()

        }

    }// end of Class Node1

    // load COPY of financial data
    public java.util.Map reload(PersonService person) throws com.argus.financials.api.ServiceException {

        if (person == null)
            return null;

        // get ALL current financial data (null means ALL object types, financial)
        // reload COPY financials for Strategy Collection
        return reload(Financials.deepCopy(person.findFinancials(), true));

    }

    public java.util.Map getFinancials(Boolean generated) {

        java.util.Map financials = new java.util.HashMap(getFinancials());
        if (generated == null)
            return financials;

        java.util.Iterator iter = financials.values().iterator();
        while (iter.hasNext()) {
            java.util.Map map = (java.util.Map) iter.next();
            java.util.Iterator iter2 = map.entrySet().iterator();
            while (iter2.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) iter2.next();
                Financial f = (Financial) entry.getValue();
                if ((generated.booleanValue() && !f.isGenerated())
                        || (!generated.booleanValue() && f.isGenerated()))
                    entry.setValue(null);
            }

        }

        return financials;

    }

    public java.util.Map getCashflowItems() {

        java.util.Map map = new java.util.HashMap();

        java.util.Map source = getFinancials();
        if (source == null)
            return map;

        java.util.Map incomes = (java.util.Map) source
                .get(ICashFlow.OBJECT_TYPE_INCOME);
        map.put(ICashFlow.OBJECT_TYPE_INCOME, incomes);

        java.util.Map expenses = (java.util.Map) source
                .get(ICashFlow.OBJECT_TYPE_EXPENSE);
        map.put(ICashFlow.OBJECT_TYPE_EXPENSE, expenses);

        java.util.Map offsets = (java.util.Map) source
                .get(ICashFlow.OBJECT_TAX_OFFSET);
        map.put(ICashFlow.OBJECT_TAX_OFFSET, offsets);

        return map;

    }

}