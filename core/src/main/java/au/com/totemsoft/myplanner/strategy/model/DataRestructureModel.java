/*
 * DataRestructureModel.java
 *
 * Created on July 8, 2002, 1:28 PM
 */

package au.com.totemsoft.myplanner.strategy.model;

import au.com.totemsoft.myplanner.bean.Asset;
import au.com.totemsoft.myplanner.bean.Assets;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.FinancialTotals;
import au.com.totemsoft.myplanner.bean.Financials;
import au.com.totemsoft.myplanner.bean.ICashFlow;
import au.com.totemsoft.myplanner.bean.Liability;
import au.com.totemsoft.myplanner.bean.db.ObjectClass;
import au.com.totemsoft.myplanner.code.FinancialClass;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.myplanner.service.PersonService;
import au.com.totemsoft.myplanner.strategy.Recommendation;
import au.com.totemsoft.myplanner.strategy.Strategy;
import au.com.totemsoft.myplanner.strategy.StrategyFinancial;
import au.com.totemsoft.myplanner.strategy.StrategyGroup;
import au.com.totemsoft.myplanner.strategy.StrategyModel;
import au.com.totemsoft.swing.table.TreeTableModel;
import au.com.totemsoft.util.ReferenceCode;

public class DataRestructureModel extends DataCollectionModel {

    static final long serialVersionUID = -730026119010213844L;

    public static final String MODEL_NAME = "Projected Quick View (with restructured financials)";

    public static final String FROM = "FROM: ";

    public static final String SOURCE = "Source";

    // public static final ReferenceCode DEFAULT_ROOT = new ReferenceCode( 0,
    // "Restructured Data" );

    private static Node2[] EMPTY_CHILDREN = new Node2[0];

    // tree table columns
    // public static final int TREE_ITEM_NAME = 0;
    public static final int SERVICE = 1;

    public static final int AMOUNT_2_USE = 2;

    public static final int PROJECTION = 3;

    public static final int SELECTED = 4;

    protected int getColumn2UpdateOnSelect() {
        return -1;
    }

    // Names of the columns.
    private static String[] cNames = { "Portofolio Item Name", "Service",
            "Amount Invested", null// "Applicable Projection"
            , "Remove" };

    protected String[] getColumnNames() {
        return cNames;
    }

    // Types of the columns.
    private static Class[] colTypes = { TreeTableModel.class,
            java.lang.String.class, java.math.BigDecimal.class, Model.class,
            Boolean.class };

    protected Class[] getColumnTypes() {
        return colTypes;
    }

    private static final ReferenceCode RECOMMENDATIONS = new ReferenceCode(0,
            "Recommendations");

    protected Node nodeRecommendations;

    private java.util.Map pairs = new java.util.HashMap();

    /***************************************************************************
     * STRATEGY STATUS
     **************************************************************************/
    private static final int CREATED = 0;

    private static final int TRANSFERED = 1;

    private static final int IMPLEMENTED = 2;

    private static final int ROLLBACKED = 3;

    private int status;

    public String getStatusDesc() {
        switch (status) {
        case CREATED:
            return "Just Created";
        case TRANSFERED:
            return "Transfer Completed";
        case IMPLEMENTED:
            return "Implemented";
        case ROLLBACKED:
            return "Rollbacked";
        default:
            return "Status UNKNOWN";
        }
    }

    public boolean isJustCreated() {
        return status == CREATED;
    }

    public boolean isTransferCompleted() {
        return status != CREATED;
    }

    public boolean setTransferCompleted() {
        if (status == CREATED)
            status = TRANSFERED;
        return isTransferCompleted();
    }

    public boolean isImplemented() {
        return status == IMPLEMENTED;
    }

    public boolean setImplemented() {
        if (status == TRANSFERED || status == ROLLBACKED)
            status = IMPLEMENTED;
        return isImplemented();
    }

    public boolean isRollbacked() {
        return status == ROLLBACKED;
    }

    public boolean setRollbacked() {
        if (status == IMPLEMENTED)
            status = ROLLBACKED;
        return isRollbacked();
    }

    // private String dummy = "Dummy1";
    // public String getDummy() { return "Dummy value - " + dummy; }

    /***************************************************************************
     * Creates new DataRestructureModel
     **************************************************************************/
    public DataRestructureModel() {
        this(null);
    }

    public DataRestructureModel(Object root) {
        super(root);
    }

    public Assets getAssets() {
        if (assets == null)
            assets = new Assets(getFinancials());
        return assets;
    }

    public Object getDefaultRoot() {
        return StrategyGroup.DEFAULT_ROOT;
    }

    protected BaseNode[] getEmptyChildren() {
        return EMPTY_CHILDREN;
    }

    protected BaseNode createNode(Object object) {
        return new Node2(object);
    }

    protected BaseNode createNode(BaseNode parent, Object object) {
        return new Node2(parent, object);
    }

    protected BaseNode[] createNodes(int size) {
        return new Node2[size];
    }

    /**
     * By default, make the column with the Tree in it the only editable one.
     * Making this column editable causes the JTable to forward mouse and
     * keyboard events in the Tree column to the underlying JTree.
     */
    public boolean isCellEditable(Object node, int column) {
        Node2 _node = (Node2) node;

        if (_node.isRoot())
            return false;

        if (column == TREE_ITEM_NAME)
            return true;
        if (column == SELECTED && _node.isFinancial()) {
            if (!isJustCreated())
                return false;

            // if ( _node.parent != null && _node.parent.parent ==
            // nodeRecommendations )
            if (_node.isRecommendationSourceFinancial())
                return true;

            Integer id = _node.getFinancialObject().getId();
            if (id == null || id.intValue() <= 0) // copy of recommendation
                                                    // Destination
                return false;

            return true;
        }
        if ((column == PROJECTION) && _node.isStrategyModel())
            return true;
        return false;
    }

    /**
     * Returns the value to be displayed for node <code>node</code>, at
     * column number <code>column</code>.
     */
    public Object getValueAt(Object node, int column) {
        Node2 _node = (Node2) node;

        switch (column) {
        case TREE_ITEM_NAME:
            return _node;
        case SERVICE:
            if (_node.isStrategyFinancial()) {
                String service = _node.getFinancialObject()
                        .getFinancialServiceCode();
                return service == null ? null : SPACES + service; // TODO: do
                                                                    // cell
                                                                    // render
                                                                    // later
            }
            return null;
        case AMOUNT_2_USE:
            if (_node.isRoot())
                return null;

            if (_node.isStrategyFinancial())
                return curr.toString(_node.getStrategyFinancialObject()
                        .getAmount(true));

            return _node.calcTotals() ? curr.toString(_node.getTotals()
                    .getTotal(AMOUNT_2_USE_TOTAL)) : null;
        case PROJECTION:
            if (_node.isStrategyModel()) {
                StrategyModel sm = _node.getStrategyModelObject();
                return sm == null || sm.getModel() == null ? null : SPACES
                        + sm.getModel();
            }
            return null;
        case SELECTED:
            if (!isJustCreated())
                return null;

            if (_node.isStrategyFinancial()) {
                if (_node.isRecommendationSourceFinancial())
                    return _node.isSelected() ? Boolean.TRUE : Boolean.FALSE;

                // if ( _node.parent != null && _node.parent.parent ==
                // nodeRecommendations ) // recommendation Destination
                if (_node.isRecommendationFinancial())
                    return null;

                Integer id = _node.getFinancialObject().getId();
                if (id == null || id.intValue() <= 0) // copy of
                                                        // recommendation
                                                        // Destination
                    return null;

                return _node.isSelected() ? Boolean.TRUE : Boolean.FALSE;
            }
            return null;
        default:
            return null;
        }

    }

    public void setValueAt(Object aValue, Object node, int column) {

        Node2 _node = (Node2) node;

        switch (column) {
        case TREE_ITEM_NAME:
            if (_node.isRecommendation()) {
                // does not work ???
                // _node.getStrategyObject().getReferenceCode().setCodeDesc(
                // (String) aValue );
            }

            return;

        case SERVICE:
            return;

        case PROJECTION:
            // do it on button event in view
            return;

        case SELECTED:
            if (_node.isFinancial())
                _node.setSelected(((Boolean) aValue).booleanValue());
            return;

        default:
            ;
        }

    }

    public BaseNode getStrategyNode(Integer objectTypeID) {
        ReferenceCode strategy = FinancialClass.getStrategyCode(objectTypeID);
        if (strategy == null)
            return null;
        /*
         * BaseNode node = nodeAssetsLiabilities.find( strategy ); if ( node ==
         * null ) node = nodeCashflow.find( strategy ); return node;
         */

        java.util.Iterator iter = nodeAssetsLiabilities.children.iterator();
        while (iter.hasNext()) {
            Node2 node = (Node2) iter.next();
            Strategy s = node.getStrategyObject();
            if (s != null && strategy.equals(s.getReferenceCode()))
                return node;
        }

        iter = nodeCashflow.children.iterator();
        while (iter.hasNext()) {
            Node2 node = (Node2) iter.next();
            Strategy s = node.getStrategyObject();
            if (s != null && strategy.equals(s.getReferenceCode()))
                return node;
        }

        return null;

    }

    protected void addDefaultNodes() {

        Node rootNode = (Node) getRoot();
        nodeRecommendations = (Node) rootNode.addChild(RECOMMENDATIONS);

        super.addDefaultNodes();

    }

    // ==========================================================================
    private Node2 getNode(int objectTypeID) {

        java.util.Iterator iter = nodeCashflow.children.iterator();
        while (iter.hasNext()) {
            Node2 n = (Node2) iter.next();
            if (n.isStrategy()
                    && n.getStrategyObject().getReferenceCode().getId() == objectTypeID)
                return n;
        }

        iter = nodeAssetsLiabilities.children.iterator();
        while (iter.hasNext()) {
            Node2 n = (Node2) iter.next();
            if (n.isStrategy()
                    && n.getStrategyObject().getReferenceCode().getId() == objectTypeID)
                return n;
        }

        return null;

    }

    public Node2 addItem(Node2 node, ReferenceCode item) {

        if (item == null) {
            if (node.isStrategy()) // e.g. CashFlow item
                item = node.getStrategyObject().getReferenceCode();
            else
                return null; // ???
        }

        int objectTypeID = item.getId();

        try {
            // new recommendation (with empty source node)
            if (objectTypeID == 0) {
                BaseDataModel.BaseNode n = node.addChild(new Strategy(item));
                return (Node2) n.addChild(new StrategyModel(new ReferenceCode(
                        0, SOURCE)));
            }

            // new strategy financial
            Financial f = ObjectClass.createNewInstance(-1 * objectTypeID);
            f.setFinancialDesc(item.getDescription());
            return (Node2) node.addChild(f); // add to selected node

        } finally {
            node.sortChildren();

        }

    }

    public void removeItem(Node2 node) {
        if (node == null || !node.isRemovable())
            return;

        // remove all node children (financials)
        removeFinancials(node.getChildren(null));

        // finally remove this node
        node.remove();

    }

    // remove node(s) from model
    private void removeFinancials(java.util.Collection nodes) {
        if (nodes == null)
            return;

        java.util.Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            Node2 node = (Node2) iter.next();
            Financial f = node.getFinancialObject();
            StrategyFinancial sf = node.getStrategyFinancialObject();

            if (sf != null)
                // put it back
                sf.setAmount((java.math.BigDecimal) null);
            // f.updatePoolBalanceAmount( sf.getAmount( true ) );

            node.remove();
            node.setSelected(false);

        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public class Node2 extends Node1 {

        static final long serialVersionUID = 4498783635361069976L;

        protected Node2(Object object) {
            this(null, object);
        }

        protected Node2(BaseNode parent, Object object) {
            super(parent, object);
        }

        public String toString() {
            Financial f = getFinancialObject();
            if ((f instanceof Asset || f instanceof Liability)
                    && ((Node2) parent).isStrategyModel())
                return FROM + f.toString();

            return super.toString();
        }

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
            return !isLeaf() && (this == nodeRecommendations || // Recommendations
                    this.parent == nodeRecommendations || // Source,
                                                            // Destination
                    this.parent == nodeCashflow);
        }

        /**
         * Returns true if the receiver can be removed.
         */
        public boolean isRemovable() {
            return parent == nodeRecommendations
                    || // any recommendation
                    (parent != null && parent.parent == nodeRecommendations && isStrategyFinancial())
                    || // Destination ONLY
                    (parent != null && parent.parent == nodeCashflow && isStrategyFinancial()) // cashflow
                                                                                                // nodes
                                                                                                // ONLY
            ;
        }

        protected boolean calcTotals() {

            if (isRecommendations() || isRecommendation()
                    || isRecommendationSource())
                return true;

            if (isStrategy()) {
                Object object = getStrategyObject().getReferenceCode();
                return RC_ASSET_CASH.equals(object)
                        || RC_ASSET_INVESTMENT.equals(object)
                        || RC_ASSET_PERSONAL.equals(object)
                        || RC_ASSET_SUPERANNUATION.equals(object)
                        || RC_INCOME_STREAM.equals(object)
                        || RC_LIABILITY.equals(object);
            }

            return super.calcTotals();

        }

        /**
         * Returns true if the receiver represents a leaf.
         */
        public boolean isLeaf() {
            return super.isLeaf() || isStrategyFinancial();
        }

        /**
         * Recommendations ---> Recommendation 1 ---> Source --->
         * StrategyFinancial_FromFinancial_1 --->
         * StrategyFinancial_FromFinancial_N ---> NewStrategyFinancial_1 --->
         * NewStrategyFinancial_N ---> Recommendation 2 ...
         */
        public boolean isRecommendations() {
            return this == nodeRecommendations;
        }

        public boolean isRecommendation() {
            return !isLeaf() && parent == nodeRecommendations;
        }

        public boolean isRecommendationSource() {
            return !isLeaf() && parent != null
                    && ((Node2) parent).isRecommendation();
        }

        public boolean isRecommendationSourceFinancial() {
            return parent != null && ((Node2) parent).isRecommendationSource();
        }

        public boolean isRecommendationFinancial() {
            return isLeaf() && parent != null
                    && ((Node2) parent).isRecommendation();
        }

        public boolean isStrategy() {
            return object instanceof Strategy;
        }

        public boolean isStrategyModel() {
            return object instanceof StrategyModel;
        }

        public boolean isFinancial() {
            return isStrategyFinancial();
        }

        public boolean isStrategyFinancial() {
            return object instanceof StrategyFinancial;
        }

        public Object getObject() {
            if (object instanceof StrategyModel)
                return getStrategyModelObject();
            return super.getObject();
        }

        public void setObject(Object value) {
            if (value instanceof Financial)
                value = new StrategyFinancial((Financial) value);
            super.setObject(value);
        }

        // create new financial items for this strategy
        public java.util.Map createStrategyFinancials() {
            if (!isStrategy())
                return null;

            java.util.Map map = new java.util.HashMap();
            for (int i = 0; i < getChildCount(); i++) {
                Node2 node = (Node2) getChild(i);
                Financial f = node.getFinancialObject();
                // Financial f = node.createStrategyModelFinancial();
                if (f == null)
                    continue;
                // System.out.println(f);
                // AssetAllocation aa = f.getAssetAllocation();
                // System.out.println( "\taa.getInCash()=" + aa.getInCash() + ",
                // " + aa.getAssetAllocationID() );

                map.put(f.getId(), f);
            }

            return map;

        }

        // create new financial item of parent's object type (amount=sum(all
        // financials of this node))
        public Financial createStrategyModelFinancial() {
            int objectTypeID = (((Strategy) parent.object).getReferenceCode())
                    .getId();
            Financial f = ObjectClass.createNewInstance(-1 * objectTypeID);

            if (isStrategyModel()) {

                FinancialTotals ft = getTotals();
                if (ft == null)
                    return null; // for cashflow items we do not keep totals

                f.setAmount(ft.getTotal(AMOUNT_2_USE_TOTAL));

                StrategyModel sm = getStrategyModelObject();
                f.setFinancialDesc(sm == null ? null : sm.toString());

            } else if (isStrategyFinancial()) {

                StrategyFinancial sf = getStrategyFinancialObject();
                f.assign(sf.getFinancial());
                f.setAmount(sf.getAmount());

            }

            return f;

        }

        public Financial getFinancialObject() {
            if (isStrategyFinancial())
                return getStrategyFinancialObject().getFinancial();
            return super.getFinancialObject();
        }

        public StrategyFinancial getStrategyFinancialObject() {
            if (isStrategyFinancial())
                return (StrategyFinancial) object;
            return null;
        }

        public StrategyModel getStrategyModelObject() {
            if (isStrategyModel()) {
                StrategyModel sm = (StrategyModel) object;
                if (sm != null) {
                    FinancialTotals ft = getTotals();
                    sm.setTotalInitialAmount(ft == null ? null : ft
                            .getTotal(AMOUNT_2_USE_INITIAL));
                    sm.setTotalContributionAmount(ft == null ? null : ft
                            .getTotal(AMOUNT_2_USE_CONTRIBUTION));
                }
                return sm;
            }
            return null;
        }

        public Strategy getStrategyObject() {
            if (isStrategy())
                return (Strategy) object;
            return null;
        }

        public java.math.BigDecimal getUsedAmount() {
            return getUsedAmount(false);
        }

        public void setUsedAmount(java.math.BigDecimal value) {
            if (!isStrategyFinancial())
                return;
            ((StrategyFinancial) object).setAmount(value);
        }

        public java.math.BigDecimal getUsedAmount(boolean signed) {
            if (!isStrategyFinancial())
                return null;
            return ((StrategyFinancial) object).getAmount(signed);
        }

        protected void updateFinancial() {

            if (isRecommendationSourceFinancial())
                ; // do nothing
            else
                super.updateFinancial();

        }

        public BaseNode addChild(Object obj) {
            // try to find Financial
            Node2 node = (Node2) find(obj);

            if (node == null) {
                // add new entry
                if (obj instanceof Financial) {
                    // java.math.BigDecimal amount = ( (Financial) obj
                    // ).getAmount();
                    obj = new StrategyFinancial((Financial) obj);
                    // ( (StrategyFinancial) obj ).setAmount( amount );

                }

                Node2 childNode = (Node2) super.addChild(obj);

                if (childNode.isStrategyFinancial()) {
                    final Financial f = childNode.getFinancialObject();
                    if (f instanceof Asset)
                        getAssets().addToAssets((Asset) f);

                    // recommendation destination node
                    if (childNode.isRecommendationFinancial()) {

                        int objectTypeID = f.getObjectTypeID().intValue();

                        Node2 add2Node = getNode(objectTypeID);
                        if (add2Node != null) {
                            BaseNode childNode2 = add2Node.addChild(obj); // add
                                                                            // to
                                                                            // nodeAssetsLiabilities
                                                                            // as
                                                                            // well
                                                                            // ???
                            pairs.put(childNode, childNode2); // to keep
                                                                // duplicated
                                                                // nodes ( for
                                                                // remove() )
                        }

                        // AssetsLiablities/CashFlow nodes
                    } else if (childNode.parent.parent == nodeAssetsLiabilities) {

                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                updateFinancial(f); // add/recalc generated
                                                    // items
                            }
                        });

                    }

                }

                return childNode;

            }

            // update existing StrategyFinancial entry
            if (obj instanceof StrategyFinancial) {
                final StrategyFinancial sf = node.getStrategyFinancialObject();
                sf.setAmount((StrategyFinancial) obj);

                // javax.swing.SwingUtilities.invokeLater( new Runnable() {
                // public void run() {
                // updateFinancial( sf.getFinancial() ); // add/recalc generated
                // items
                // } } );

            }

            return node;

        }

        public void remove() {
            super.remove();

            Node2 node2 = (Node2) pairs.get(this);
            if (node2 != null) {
                pairs.remove(this);
                node2.remove();
            }

            if (getFinancialObject() instanceof Asset)
                getAssets().removeFromAssets((Asset) getFinancialObject());
        }

        public void updateTotal_Down() {

            // super.updateTotal_Down();

            java.math.BigDecimal amount = Financial.ZERO;
            if (isRecommendations()) {
                java.util.Iterator iter = children.iterator();
                while (iter.hasNext()) {
                    Node2 n = (Node2) iter.next();
                    n.updateTotal_Down();

                    FinancialTotals ft = n.getTotals();
                    if (ft != null) {
                        java.math.BigDecimal a = ft
                                .getTotal(AMOUNT_2_USE_TOTAL);
                        if (a != null)
                            amount = amount.add(a);
                    }
                }

            } else if (isRecommendation()) { // show diff = source -
                                                // application(s)
                java.util.Iterator iter = children.iterator();
                while (iter.hasNext()) {
                    Node2 n = (Node2) iter.next();

                    if (n.isRecommendationSource()) { // Source
                        n.updateTotal_Down();

                        java.math.BigDecimal a = n.getTotals().getTotal(
                                AMOUNT_2_USE_TOTAL);
                        if (a != null)
                            amount = amount.add(a);
                    } else if (n.isRecommendationFinancial()) {
                        java.math.BigDecimal a = n.getStrategyFinancialObject()
                                .getAmount(true);
                        if (a != null)
                            amount = amount.subtract(a);
                    }
                }

            } else if (isRecommendationSource()) {
                java.util.Iterator iter = children.iterator();
                while (iter.hasNext()) {
                    Node2 n = (Node2) iter.next();

                    if (n.isStrategyFinancial()) {
                        java.math.BigDecimal a = n.getStrategyFinancialObject()
                                .getAmount(true);
                        if (a != null)
                            amount = amount.add(a);
                    }
                }

            } else {
                java.util.Iterator iter = children.iterator();
                while (iter.hasNext()) {
                    Node2 n = (Node2) iter.next();

                    if (n.isStrategyFinancial()) {
                        java.math.BigDecimal a = n.getStrategyFinancialObject()
                                .getAmount(true);
                        if (a != null)
                            amount = amount.add(a);
                    } else {
                        n.updateTotal_Down();

                        FinancialTotals ft = n.getTotals();
                        if (ft != null) {
                            java.math.BigDecimal a = ft
                                    .getTotal(AMOUNT_2_USE_TOTAL);
                            if (a != null)
                                amount = amount.add(a);
                        }

                    }

                }

            }

            FinancialTotals ft = getTotals();
            if (ft != null)
                ft.setTotal(AMOUNT_2_USE_TOTAL, amount);

        }
        /*
         * public void updateTotal() {
         * 
         * super.updateTotal();
         *  }
         */

    } // end Node2 class

    protected void addNodes(Node parentNode, ReferenceCode[] refCodes) {

        if (refCodes == null)
            return;

        for (int i = 0; i < refCodes.length; i++) {
            if (refCodes[i] == ReferenceCode.CODE_NONE)
                continue;

            BaseNode strategyNode = parentNode.find(refCodes[i]);
            if (strategyNode != null)
                continue;

            BaseNode groupNode = createNode(parentNode, new Strategy(
                    refCodes[i]));
            parentNode.addChild(groupNode);

        }

    }

    public java.util.Map reload(PersonService person) throws au.com.totemsoft.myplanner.api.ServiceException {
        return null; // do nothing, restore as serialized object
    }

    // Collection data (deep copy)
    public java.util.Map getFinancials() {
        java.util.Map financials = getAssetLiabilityItems();
        financials.putAll(getCashflowItems());

        return financials;
    }

    // Restructured Recommendation Data
    public java.util.Collection getRecommendations() {

        // TODO: extract data... Recommendation
        if (nodeRecommendations == null)
            return null;
        java.util.ArrayList children = nodeRecommendations.children;
        if (children == null || children.size() == 0)
            return null;

        java.util.Collection recommendations = new java.util.ArrayList(children
                .size());
        java.util.Iterator iter = children.iterator();
        while (iter.hasNext()) {
            Node2 node = (Node2) iter.next();
            if (!node.isStrategy())
                continue; // will never happened

            java.util.ArrayList children2 = node.children;
            if (children2 == null || children2.size() == 0)
                continue; // will never happened

            Recommendation r = new Recommendation(node.toString());
            recommendations.add(r);

            java.util.Iterator iter2 = children2.iterator();
            while (iter2.hasNext()) {
                Node2 node2 = (Node2) iter2.next();
                if (node2.isStrategyModel()) { // Source node
                    java.util.ArrayList children3 = node2.children;
                    if (children3 == null || children3.size() == 0)
                        continue;

                    java.util.Iterator iter3 = children3.iterator();
                    while (iter3.hasNext()) {
                        Node2 node3 = (Node2) iter3.next();

                        if (node3.isStrategyFinancial()) { // always
                            StrategyFinancial sf = node3
                                    .getStrategyFinancialObject();
                            r.addToSource(sf);
                        }
                    }

                } else if (node2.isStrategyFinancial()) {
                    r.addToDestination(node2.getStrategyFinancialObject()
                            .getFinancial());

                }

            }

        }

        return recommendations;
    }

    public java.util.Map getAssetLiabilityItems() {

        java.util.Map map = new java.util.HashMap();

        java.util.Map _cash = new java.util.HashMap();
        map.put(ASSET_CASH, _cash);

        java.util.Map _super = new java.util.HashMap();
        map.put(ASSET_SUPERANNUATION, _super);

        java.util.Map _investment = new java.util.HashMap();
        map.put(ASSET_INVESTMENT, _investment);

        java.util.Map _personal = new java.util.HashMap();
        map.put(ASSET_PERSONAL, _personal);

        java.util.Map _incomeStream = new java.util.HashMap();
        map.put(ASSET_INCOME_STREAM, _incomeStream);

        java.util.Map _liability = new java.util.HashMap();
        map.put(LIABILITY, _liability);

        if (nodeAssetsLiabilities != null)
            for (int i = 0; i < nodeAssetsLiabilities.getChildCount(); i++) {
                Node2 node = (Node2) nodeAssetsLiabilities.getChild(i);
                java.util.Map m = node.createStrategyFinancials();
                if (m == null || m.size() == 0)
                    continue;

                ReferenceCode refCode = ((Strategy) node.object)
                        .getReferenceCode();
                if (ASSET_CASH.equals(refCode.getCodeId()))
                    _cash.putAll(m);
                else if (ASSET_SUPERANNUATION
                        .equals(refCode.getCodeId()))
                    _super.putAll(m);
                else if (ASSET_INVESTMENT.equals(refCode.getCodeId()))
                    _investment.putAll(m);
                else if (ASSET_PERSONAL.equals(refCode.getCodeId()))
                    _personal.putAll(m);
                else if (ASSET_INCOME_STREAM.equals(refCode.getCodeId()))
                    _incomeStream.putAll(m);
                else if (LIABILITY.equals(refCode.getCodeId()))
                    _liability.putAll(m);
                else {
                    System.err
                            .println("===========================================");
                    System.err.println("??? Invalid node: " + node);
                    System.err
                            .println("===========================================");
                }

            }
        // else
        // System.out.println( "===========================================
        // nodeAssetsLiabilities == null" );

        return map;

    }

    public java.util.Map getCashflowItems() {

        java.util.Map map = new java.util.HashMap();

        java.util.Map incomes = new java.util.HashMap();
        map.put(ICashFlow.OBJECT_TYPE_INCOME, incomes);

        java.util.Map expenses = new java.util.HashMap();
        map.put(ICashFlow.OBJECT_TYPE_EXPENSE, expenses);

        java.util.Map offsets = new java.util.HashMap();
        map.put(ICashFlow.OBJECT_TAX_OFFSET, offsets);

        if (nodeCashflow != null)
            for (int i = 0; i < nodeCashflow.getChildCount(); i++) {
                Node2 node = (Node2) nodeCashflow.getChild(i);
                java.util.Map m = node.createStrategyFinancials();
                if (m == null || m.size() == 0)
                    continue;

                ReferenceCode refCode = ((Strategy) node.object)
                        .getReferenceCode();
                if (ICashFlow.OBJECT_TYPE_INCOME.equals(refCode
                        .getCodeId()))
                    incomes.putAll(m);
                else if (ICashFlow.OBJECT_TYPE_EXPENSE.equals(refCode
                        .getCodeId()))
                    expenses.putAll(m);
                else if (ICashFlow.OBJECT_TAX_OFFSET.equals(refCode
                        .getCodeId()))
                    offsets.putAll(m);
                else {
                    System.err
                            .println("===========================================");
                    System.err.println("??? Invalid node: " + node);
                    System.err
                            .println("===========================================");
                }

            }
        // if ( nodeCashflow != null )
        // for ( int i = 0; i < nodeCashflow.getChildCount(); i++ )
        // addFinancials( (Node2) nodeCashflow.getChild(i), incomes, expenses );

        return map;

    }

    public void addFinancials(java.util.Map financials) {

        if (financials == null)
            return;

        // will remove null entries
        java.util.Map financials2 = Financials.deepCopy(financials, false);
        if (financials2 == null)
            return;

        java.util.Iterator iter = financials2.values().iterator();
        while (iter.hasNext()) {
            java.util.Map map = (java.util.Map) iter.next();
            if (map == null)
                continue;

            java.util.Iterator iter2 = map.values().iterator();
            while (iter2.hasNext()) {
                final Financial f2 = (Financial) iter2.next();
                if (f2 == null || f2.isGenerated())
                    continue;

                // cache PK
                Integer id = f2.getId();
                // and reset PK
                f2.setId(null);

                Integer objectTypeID = f2.getObjectTypeID();

                // get original financial
                final Financial f = (Financial) ((java.util.Map) financials
                        .get(objectTypeID)).get(id);

                // should never happen if Financials.deepCopy( financials, false
                // ) works correctly
                // if ( f == null ) continue;

                java.math.BigDecimal balance = f2.getBalanceAmount();
                if (balance == null || balance.doubleValue() == 0.)
                    continue;

                BaseNode dest = getStrategyNode(objectTypeID);
                if (dest == null)
                    continue;

                f2.setAmount(f.getBalanceAmount());
                f2.setUsedAmount(Financial.ZERO);

                if (f2 instanceof Asset) {
                    Asset a = (Asset) f2;
                    a.setUnitsShares(null);
                }

                StrategyFinancial sf = new StrategyFinancial(f2);
                sf.setAmount(f2.getAmount());

                dest.addChild(sf);

                // close source financial
                f.updateBalanceAmount(balance.negate());

            }

        }

        updateTotal();

    }

    private void addFinancials(Node2 node, java.util.Map incomes,
            java.util.Map expenses) {

        if (node.isStrategyFinancial()) {
            Financial f = node.getFinancialObject();

            if (ICashFlow.OBJECT_TYPE_INCOME.equals(f.getObjectTypeID()))
                incomes.put(f.getId(), f);
            else if (ICashFlow.OBJECT_TYPE_EXPENSE.equals(f.getObjectTypeID()))
                expenses.put(f.getId(), f);
            // else if ( ICashFlow.OBJECT_TAX_OFFSET.equals(
            // refCode.getCodeIDInteger() ) )
            // offsets.putAll( m );
            else {
                System.err
                        .println("===========================================");
                System.err.println("??? Invalid node: " + node);
                System.err
                        .println("===========================================");
            }

        } else {
            for (int i = 0; i < node.getChildCount(); i++)
                addFinancials((Node2) node.getChild(i), incomes, expenses);

        }

    }

}