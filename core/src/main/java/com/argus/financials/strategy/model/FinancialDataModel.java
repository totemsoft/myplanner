/*
 * FinancialDataModel.java
 *
 * Created on August 8, 2002, 10:36 AM
 */

package com.argus.financials.strategy.model;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import javax.swing.SwingUtilities;

import com.argus.financials.bean.Asset;
import com.argus.financials.bean.Assets;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialTotals;
import com.argus.financials.bean.IRegularType;
import com.argus.financials.bean.Liability;
import com.argus.financials.bean.Regular;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.code.BaseCodeComparator;
import com.argus.financials.code.FinancialClass;
import com.argus.financials.code.FinancialClassID;
import com.argus.financials.config.FPSLocale;
import com.argus.financials.service.PersonService;
import com.argus.financials.swing.table.TreeTableModel;
import com.argus.util.ReferenceCode;

public class FinancialDataModel extends BaseDataModel implements
        FinancialClassID {
    // cd /D D:\projects\Financial Planner\ant\build\classes
    // serialver -classpath . com.argus.strategy.model.FinancialDataModel

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 1589078526771958078L;

    public static final String MODEL_NAME = "Current Quick View (with current financials)";

    private static final ReferenceCode DEFAULT_ROOT = new ReferenceCode(0,
            "Collection Data");

    private static Node[] EMPTY_CHILDREN = new Node[0];

    // tree table columns
    // public static final int TREE_ITEM_NAME = 0;
    public static final int SERVICE = 1;

    public static final int AMOUNT = 2;

    public static final int OWNER = 3;

    private static final int AMOUNT_TOTAL = 0;

    protected int getCountTotal() {
        return 1;
    }

    /**
     * Financial Map data structure Map( ObjectTypeID, Map( ObjectID, Object ) )
     */
    protected java.util.Map details;

    protected Assets assets;

    protected Node nodeAssetsLiabilities;

    protected Node nodeCashflow;

    private int[] rules;

    // financials
    public java.util.Map getDetails() {
        return details;
    }

    public java.util.Map getFinancials() {
        return details;
    }

    // Names of the columns.
    private static String[] cNames = { "Item Name", "Service", "Amount",
            "Owner" };

    protected String[] getColumnNames() {
        return cNames;
    }

    // Types of the columns.
    private static Class[] colTypes = { TreeTableModel.class,
            java.lang.String.class, java.math.BigDecimal.class,
            java.lang.String.class };

    protected Class[] getColumnTypes() {
        return colTypes;
    }

    /** Creates new FinancialDataModel */
    public FinancialDataModel() {
        this(null);
    }

    public FinancialDataModel(Object root) {
        super(root);
        rules = IRegularType.RULES[IRegularType.iFINANCIALS];
    }

    public void setRoot(Object root) {
        super.setRoot(root);

        addDefaultNodes();

    }

    public Assets getAssets() {
        if (assets == null)
            assets = Assets.getCurrentAssets();
        return assets;
    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     **************************************************************************/
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        super.stateChanged(changeEvent);

        Object source = changeEvent.getSource();
        if (source instanceof Financial) {

        }

        updateTotal();

    }

    protected Object getDefaultRoot() {
        return DEFAULT_ROOT;
    }

    protected BaseNode[] getEmptyChildren() {
        return EMPTY_CHILDREN;
    }

    protected BaseNode createNode(Object obj) {
        return new Node(obj);
    }

    protected BaseNode createNode(BaseNode parent, Object obj) {
        return new Node(parent, obj);
    }

    protected BaseNode[] createNodes(int size) {
        return new Node[size];
    }

    public void updateTotal() {
        ((Node) getRoot()).updateTotal_Down();
    }

    public void updatePrice() {

        BaseNode[] groupNodes = nodeAssetsLiabilities.getChildren();
        for (int i = 0; i < groupNodes.length; i++) {

            BaseNode[] nodes = groupNodes[i].getChildren();
            if (nodes == null || nodes.length == 0)
                continue;

            // Assets/Liabilities
            for (int j = 0; j < nodes.length; j++) {
                Object obj = nodes[j].getObject();
                if (obj instanceof Asset)
                    ((Asset) obj).updatePrice();
            }

        }

        updateTotal();

    }

    /***************************************************************************
     * The TreeTableNode interface.
     **************************************************************************/

    /**
     * Returns the value to be displayed for node <code>node</code>, at
     * column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column) {
        Node _node = (Node) node;

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
                // return curr.toString( _node.getFinancialObject().getAmount(
                // true ) );
                return curr.toString(_node.getFinancialObject()
                        .getFinancialYearAmount(true));
            return _node.calcTotals() ? curr.toString(_node.getTotals()
                    .getTotal(AMOUNT_TOTAL)) : null;
        case OWNER:
            if (_node.isFinancial()) {
                String owner = _node.getFinancialObject().getOwner();
                return owner == null ? null : SPACES + owner; // TODO: do cell
                                                                // render later
            }
            return null;
        default:
            ;
        }

        return null;
    }

    public void setValueAt(Object aValue, Object node, int column) {
        super.setValueAt(aValue, node, column); // do nothing

        Node _node = (Node) node;

        switch (column) {
        case TREE_ITEM_NAME:
            return;
        case SERVICE:
            return;
        case AMOUNT:
            if (_node.isFinancial())
                _node.setUsedAmount((java.math.BigDecimal) aValue);
            return;
            // case UNDER_ADVICE:
            // return;
        case OWNER:
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
        Node _node = (Node) node;

        if (_node.isRoot())
            return false;
        if (column == TREE_ITEM_NAME)
            return true;
        return false;
    }

    /**
     * 
     */
    public class Node extends BaseNode {
        // serialver -classpath . com.argus.strategy.model.FinancialDataModel

        // Compatible changes include adding or removing a method or a field.
        // Incompatible changes include changing an object's hierarchy or
        // removing the implementation of the Serializable interface.
        static final long serialVersionUID = -2394538978779543828L;

        //
        private FinancialTotals totals;

        protected Node(Object object) {
            this(null, object);
        }

        protected Node(BaseNode parent, Object object) {
            super(parent, object);

            // getTotals(); // do not do this, it is a virtual method
        }

        protected boolean calcTotals() {
            return RC_ASSETS_LIABILITIES.equals(object)
                    || RC_ASSET_CASH.equals(object)
                    || RC_ASSET_INVESTMENT.equals(object)
                    || RC_ASSET_PERSONAL.equals(object)
                    || RC_ASSET_SUPERANNUATION.equals(object)
                    || RC_INCOME_STREAM.equals(object)
                    || RC_LIABILITY.equals(object);
        }

        protected FinancialTotals getTotals() {

            if (isLeaf())
                return null;

            if (totals != null)
                return totals;

            totals = new FinancialTotals(getCountTotal());
            for (int i = 0; i < getCountTotal(); i++)
                // totals.setTotal( i, Financial.ZERO );
                totals.setTotal(i, null);

            return totals;

        }

        // second level
        public boolean isFinancialGroup() {
            return getParent() == null ? false
                    : (getParent().getParent() == null ? false : getParent()
                            .getParent().isRoot());
        }

        public boolean isFinancial() {
            return getObject() instanceof Financial;
        }

        public Financial getFinancialObject() {
            if (isFinancial())
                return (Financial) getObject();
            return null;
        }

        public java.math.BigDecimal getUsedAmount() {
            return getFinancialObject().getUsedAmount();
        }

        public void setUsedAmount(java.math.BigDecimal value) {
            getFinancialObject().setUsedAmount(value);
        }

        public java.math.BigDecimal getUsedAmount(boolean signed) {
            return getFinancialObject().getUsedAmount(signed);
        }

        /**
         * Returns true if the receiver represents a leaf.
         */
        public boolean isLeaf() {
            return isFinancial();
        }

        /**
         * 
         */
        private void addFinancial(final Financial financial) {

            if (financial instanceof Asset || financial instanceof Liability) {

                SwingUtilities.invokeLater(new Runnable() {

                    private void add(BaseNode parentNode, Financial f) {
                        java.math.BigDecimal amount = f.getAmount();
                        if (amount == null || amount.doubleValue() == 0.)
                            return;

                        BaseNode node = parentNode.find(f);
                        if (node != null)
                            return;

                        parentNode.addChild(f);
                    }

                    public void run() {

                        if (financial instanceof Asset)
                            getAssets().addToAssets((Asset) financial);

                        for (int i = 0; i < rules.length; i++) {
                            Regular r = financial.getRegular(rules[i]);
                            if (r != null) {
                                BaseNode node = nodeCashflow
                                        .find(FinancialClass.getStrategyCode(r
                                                .getObjectTypeID()));
                                r.setOwnerCodeID(financial.getOwnerCodeID());
                                add(node, r);
                            }
                        }

                    } // end run()

                });

            }

            Integer objID = financial.getPrimaryKeyID(); // can be negative
                                                            // (not saved)
            if (objID.intValue() > 0) // restored
                return;

            // add new Financial entry
            if (details == null)
                details = new java.util.TreeMap(new BaseCodeComparator());

            java.util.Map map = (java.util.Map) details.get(financial
                    .getObjectTypeID());
            if (map == null) {
                map = new java.util.TreeMap(new BaseCodeComparator());
                details.put(financial.getObjectTypeID(), map);
            }

            // financial.setOwnerPrimaryKeyID(
            // RmiParams.getInstance().getClientPersonID() );
            financial.setCountryCodeID(FPSLocale.getInstance()
                    .getCountryCodeID());

            if (!map.containsKey(financial.getPrimaryKeyID()))
                map.put(financial.getPrimaryKeyID(), financial);

        }

        public Financial copyFinancial(Financial financial)
                throws java.lang.CloneNotSupportedException {

            if (financial == null)
                return null;

            financial = (Financial) financial.clone();
            financial.setPrimaryKeyID(null);

            // add new Financial entry
            if (details == null)
                details = new java.util.TreeMap(new BaseCodeComparator());

            java.util.Map map = (java.util.Map) details.get(financial
                    .getObjectTypeID());
            if (map == null) {
                map = new java.util.TreeMap(new BaseCodeComparator());
                details.put(financial.getObjectTypeID(), map);
            }

            map.put(financial.getPrimaryKeyID(), financial);
            return financial;

        }

        // update Financial entry
        protected void updateFinancial(final Financial financial) {

            if (financial == null)
                return;

            if (financial instanceof Asset || financial instanceof Liability) {

                SwingUtilities.invokeLater(new Runnable() {

                    private void update(BaseNode parentNode, Financial f) {
                        Node node = (Node) parentNode.find(f);
                        java.math.BigDecimal amount = f.getAmount();
                        if (node == null) {
                            if (amount != null && amount.doubleValue() != 0.)
                                parentNode.addChild(f);

                        } else {
                            if (amount == null || amount.doubleValue() == 0.)
                                node.remove();
                            else
                                node.setObject(f);

                        }

                    }

                    public void run() {

                        for (int i = 0; i < rules.length; i++) {
                            Regular r = financial.getRegular(rules[i]);
                            if (r != null) {
                                BaseNode node = nodeCashflow
                                        .find(FinancialClass.getStrategyCode(r
                                                .getObjectTypeID()));
                                r.setOwnerCodeID(financial.getOwnerCodeID());
                                update(node, r);
                            }
                        }

                    } // end run()

                });

            }

        }

        protected void updateFinancial() {

            final Financial financial = getFinancialObject();
            updateFinancial(financial);

        }

        // remove Financial entry
        private void removeFinancial() {

            final Financial financial = getFinancialObject();
            if (financial == null)
                return;

            if (financial instanceof Asset || financial instanceof Liability) {

                SwingUtilities.invokeLater(new Runnable() {

                    private void remove(BaseNode parentNode, Financial f) {
                        BaseNode node = parentNode.find(f);
                        if (node != null)
                            node.remove();
                    }

                    public void run() {
                        if (financial instanceof Asset)
                            getAssets().removeFromAssets((Asset) financial);

                        for (int i = 0; i < rules.length; i++) {
                            Regular r = financial.getRegular(rules[i]);
                            if (r != null) {
                                BaseNode node = nodeCashflow
                                        .find(FinancialClass.getStrategyCode(r
                                                .getObjectTypeID()));
                                remove(node, r);
                            }
                        }

                    } // end run()

                });

            }

            if (details == null)
                return;

            java.util.Map map = (java.util.Map) details.get(financial
                    .getObjectTypeID());
            if (map == null)
                return;

            Integer objID = financial.getPrimaryKeyID(); // can be negative
                                                            // (not saved)
            if (objID == null)
                return;

            if (objID.intValue() > 0) {
                map.remove(objID);
                map.put(objID, null); // mark for deletion on server
            } else {
                map.remove(objID); // not saved to server yet
            }

        }

        public BaseNode addChild(Object obj) {
            Node newNode = (Node) super.addChild(obj);

            if (obj instanceof Financial) {
                Financial f = (Financial) obj;

                // model to be notified on all financial updates/changes
                f.addChangeListener(FinancialDataModel.this);

                // add new Financial entry into financial container
                addFinancial(f);

            }

            newNode.nodeChanged();

            return newNode;

        }

        public void remove() {
            super.remove();

            removeFinancial();

        }

        /**
         * Can be invoked when a node has changed, will create the appropriate
         * event.
         */
        public void nodeChanged() {
            nodeChanged(true);
        }

        protected void nodeChanged(boolean calcTotal) {
            super.nodeChanged();

            updateFinancial();

            if (calcTotal)
                FinancialDataModel.this.updateTotal();

        }

        protected void nodeInserted() {
            super.nodeInserted();

            FinancialDataModel.this.updateTotal();

        }

        protected void nodeRemoved() {
            super.nodeRemoved();

            FinancialDataModel.this.updateTotal();

        }

        /*
         * 
         */
        public void updateTotal_Down() {

            if (isFinancial())
                return; // break condition

            // update totals
            java.math.BigDecimal underAdviceAmount = Financial.ZERO;
            // used
            java.math.BigDecimal totalInitialAmount = Financial.ZERO;
            java.math.BigDecimal totalContributionAmount = Financial.ZERO;

            java.util.Iterator iter = children.iterator();
            while (iter.hasNext()) {
                Node node = (Node) iter.next();

                java.math.BigDecimal amount = null;

                if (node.isFinancial()) { // isLeaf()

                    Financial f = node.getFinancialObject();
                    // amount = f.getAmount( true );
                    amount = f.getFinancialYearAmount(true);

                    if (f instanceof Asset || f instanceof Liability) {
                        if (amount != null)
                            totalInitialAmount = totalInitialAmount.add(amount);
                    } else if (f instanceof RegularIncome
                            || f instanceof RegularExpense) {
                        totalInitialAmount = null;
                        // if ( amount != null )
                        // totalContributionAmount =
                        // totalContributionAmount.add( amount ); // what year
                        // ???
                    } else {
                        continue;
                    }

                } else {
                    node.updateTotal_Down();

                    FinancialTotals ft = node.getTotals();
                    if (ft == null)
                        continue;

                    amount = ft.getTotal(AMOUNT_TOTAL);

                    if (amount == null)
                        totalInitialAmount = null;
                    else if (totalInitialAmount != null)
                        totalInitialAmount = totalInitialAmount.add(amount);

                }

            }

            // update totals
            FinancialTotals ft = getTotals();
            if (ft == null)
                return;

            if (totalInitialAmount == null || totalContributionAmount == null)
                ft.setTotal(AMOUNT_TOTAL, null);
            else
                ft.setTotal(AMOUNT_TOTAL, totalInitialAmount
                        .add(totalContributionAmount));

        }

        public void updateTotal() {

            if (isFinancial())
                return; // break condition

            // update totals
            java.math.BigDecimal underAdviceAmount = Financial.ZERO;
            // used
            java.math.BigDecimal totalInitialAmount = Financial.ZERO;
            java.math.BigDecimal totalContributionAmount = Financial.ZERO;

            java.util.Iterator iter = children.iterator();
            while (iter.hasNext()) {
                Node node = (Node) iter.next();

                java.math.BigDecimal amount = null;

                if (node.isFinancial()) {

                    Financial f = node.getFinancialObject();
                    // amount = f.getAmount( true );
                    amount = f.getFinancialYearAmount(true);

                    if (f instanceof Asset || f instanceof Liability) {
                        if (amount != null)
                            totalInitialAmount = totalInitialAmount.add(amount);
                    } else if (f instanceof RegularIncome
                            || f instanceof RegularExpense) {
                        totalInitialAmount = null;
                        // if ( amount != null )
                        // totalContributionAmount =
                        // totalContributionAmount.add( amount ); // what year
                        // ???
                    } else {
                        continue;
                    }

                } else {
                    FinancialTotals ft = node.getTotals();
                    if (ft == null)
                        continue;

                    amount = ft.getTotal(AMOUNT_TOTAL);

                    if (amount == null)
                        totalInitialAmount = null;
                    else if (totalInitialAmount != null)
                        totalInitialAmount = totalInitialAmount.add(amount);
                }

            }

            // update totals
            FinancialTotals ft = getTotals();
            if (ft == null)
                return;

            if (totalInitialAmount == null || totalContributionAmount == null)
                ft.setTotal(AMOUNT_TOTAL, null);
            else
                ft.setTotal(AMOUNT_TOTAL, totalInitialAmount
                        .add(totalContributionAmount));

            nodeChanged(false); // to trigger parent.nodeChanged()

        }

    }// end of Class Node

    protected void addDefaultNodes() {

        Node rootNode = (Node) getRoot();
        nodeAssetsLiabilities = (Node) rootNode.addChild(RC_ASSETS_LIABILITIES);
        nodeCashflow = (Node) rootNode.addChild(RC_CASHFLOW);

        // load default empty nodes
        addNodes(nodeAssetsLiabilities, new ReferenceCode[] { RC_ASSET_CASH,
                RC_ASSET_INVESTMENT, RC_ASSET_PERSONAL,
                RC_ASSET_SUPERANNUATION, RC_INCOME_STREAM, RC_LIABILITY, });

        addNodes(nodeCashflow, new ReferenceCode[] { RC_REGULAR_INCOME,
                RC_REGULAR_EXPENSE, RC_TAX_OFFSET, });

    }

    protected void addNodes(Node parentNode, ReferenceCode[] refCodes) {

        if (refCodes == null)
            return;

        for (int i = 0; i < refCodes.length; i++) {
            if (refCodes[i] == ReferenceCode.CODE_NONE)
                continue;

            BaseNode child = parentNode.find(refCodes[i]);
            if (child != null)
                continue;

            BaseNode groupNode = createNode(parentNode, refCodes[i]);
            parentNode.addChild(groupNode);

        }

    }

    protected BaseNode getNode(ReferenceCode refCode) {

        if (refCode == RC_REGULAR_INCOME || refCode == RC_REGULAR_EXPENSE
                || refCode == RC_TAX_OFFSET)
            return nodeCashflow.find(refCode);

        if (refCode == RC_ASSET_CASH || refCode == RC_ASSET_PERSONAL
                || refCode == RC_ASSET_INVESTMENT
                || refCode == RC_ASSET_SUPERANNUATION
                || refCode == RC_INCOME_STREAM || refCode == RC_LIABILITY)
            return nodeAssetsLiabilities.find(refCode);

        return null;

    }

    public java.util.Map reload(PersonService person) throws com.argus.financials.service.ServiceException {

        if (person == null)
            return null;

        // get ALL current financial data (null means ALL object types,
        // financial)
        if (FPSLocale.getInstance().isLocalServer()) // not required for rmi
            person.setFinancials(null, null); // reset all data
        details = person.getFinancials();

        return reload(details);

    }

    public java.util.Map reload(java.util.Map financials) {

        // reset assets
        getAssets().initCodes(null);

        details = financials;

        if (details == null)
            return null;

        FinancialClass strategyType = new FinancialClass();

        /***********************************************************************
         * add other financial data
         **********************************************************************/
        java.util.Iterator iter = details.entrySet().iterator();
        while (iter.hasNext()) {

            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            Integer objectTypeID = (Integer) entry.getKey();
            java.util.Map financialMap = (java.util.Map) entry.getValue();

            ReferenceCode refCode = strategyType.getCode(objectTypeID);
            if (ReferenceCode.CODE_NONE.equals(refCode))
                continue;

            BaseNode parentNode = getNode(refCode);
            if (parentNode == null)
                parentNode = parentNode.addChild(refCode);

            java.util.Iterator iter2 = financialMap.values().iterator();
            while (iter2.hasNext()) {

                Financial financial = (Financial) iter2.next();
                if (financial == null)
                    continue;

                parentNode.addChild(financial);

            }

        }

        return details;

    }

    /**
     * if model deserialized we have to call this method
     */
    public void updateChangeListener(boolean add) {

        java.util.Iterator iter = getFinancials().values().iterator();
        while (iter.hasNext()) {
            java.util.Map financialMap = (java.util.Map) iter.next();
            java.util.Iterator iter2 = financialMap.values().iterator();
            while (iter2.hasNext()) {
                Financial financial = (Financial) iter2.next();
                if (financial == null)
                    continue;

                // model to be notified on all financial updates/changes
                if (add)
                    financial.addChangeListener(this);
                else
                    financial.removeChangeListener(this);
            }

        }

    }

}
