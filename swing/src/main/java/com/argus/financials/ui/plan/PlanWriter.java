/*
 * PlanWriter.java
 *
 * Created on 20 February 2002, 11:31
 */

package com.argus.financials.ui.plan;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.DbConstant;
import com.argus.financials.code.ModelTypeID;
import com.argus.financials.config.ViewSettings;
import com.argus.financials.config.WordSettings;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.report.IWordReport;
import com.argus.financials.report.ReportException;
import com.argus.financials.report.ReportFields;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.strategy.StrategyGroup;
import com.argus.financials.swing.SwingUtil;
import com.argus.financials.swing.table.JTreeTable;
import com.argus.financials.ui.BaseView;
import com.argus.financials.ui.CheckBoxList;
import com.argus.financials.ui.data.PlanWriterData;
import com.argus.io.IOUtils;
import com.argus.swing.SplashWindow;
import com.argus.swing.SwingUtils;
import com.argus.swing.table.TreeTableModel;
import com.argus.util.ReferenceCode;

public class PlanWriter extends BaseView
    implements TreeSelectionListener, ListSelectionListener
{

    private static IWordReport report;
    public static void setReport(IWordReport report) {
        PlanWriter.report = report;
    }

    private static PlanWriter view;

    private boolean thisCTOR = false;

    private java.util.Vector clientPlans;

    private java.util.Vector templatePlans;

    // Model for the JTreeTable.
    protected PlanWriterModel model;

    // Used to represent the model.
    protected JTreeTable treeTable;

    private CheckBoxList destList;

    private DestinationListModel destListModel;

    private javax.swing.JFileChooser jFileChooser;

    // Row the is being reloaded.
    protected int reloadRow = -1;

    // TreePath being reloaded.
    protected TreePath reloadPath;

    // A counter increment as the Timer fies and the same path is being
    // reloaded.
    protected int reloadCounter;

    // Timer used to update reload state.
    protected Timer timer;

    /**
     * 
     */
    private boolean added2frame = false;

    /**
     * 
     */
    public static boolean exists() {
        return view != null;
    }

    public String getDefaultTitle() {
        return "Plan Writer";
    }

    /** Creates new form PlanWriter */
    private PlanWriter() {
        thisCTOR = true;
        initComponents();
        jPanelDetails.setLayout(new javax.swing.BoxLayout(jPanelDetails,
                javax.swing.BoxLayout.Y_AXIS));
        initComponents2();
        thisCTOR = false;

        SwingUtils.setDefaultFont(this);

        jToolBarFile.setVisible(false);
        jButtonGenerateTOC.setVisible(false);
        jButtonSaveAs.setVisible(false);

        jMenuFile.setVisible(false);

    }

    private java.util.Vector getPlans() {
        return getPlans(null);
    }

    private java.util.Vector getPlans(PersonService person) {

        if (person != null) {
            java.util.Collection plans = null;
            try {
                plans = person.getPlans(null);
            } catch (com.argus.financials.api.ServiceException e) {
                e.printStackTrace(System.err);
            }
            clientPlans = plans == null || plans.size() == 0 ? new java.util.Vector()
                    : new java.util.Vector(plans);
        }

        if (clientPlans == null)
            clientPlans = new java.util.Vector();

        return clientPlans;

    }

    private java.util.Vector getTemplatePlans() {
        return getTemplatePlans(null);
    }

    private java.util.Vector getTemplatePlans(PersonService person) {
        if (templatePlans == null && person != null) {
            java.util.Collection plans = null;
            try {
                plans = person.getPlans(DbConstant.TEMPLATE_PLAN);
            } catch (com.argus.financials.api.ServiceException e) {
                e.printStackTrace(System.err);
            }
            templatePlans = plans == null || plans.size() == 0 ? new java.util.Vector()
                    : new java.util.Vector(plans);
            templatePlans.add(0, PlanWriterTemplateModel.NONE_TEMPLATE_PLAN);
        }
        return templatePlans;
    }

    /***************************************************************************
     * these two methods has to work together (as ctor/dtor)
     */
    private void initialize() throws ReportException, Exception {

        clientPlans = null;

        setModified(false);
        updateView(clientService);

        SwingUtil.setVisible(this, true);

    }

    private void uninitialize() throws ReportException {

        // hide plan writer
        SwingUtil.setVisible(this, false);

        // destList.removeAllElements();
        setRootPath(null);
        setModified(false);

    }

    protected ReportFields getReportData(PersonService person)
            throws Exception {

        if (person == null)
            return null;

        // new reporting ( using pre-defined map(field,value) )
        ReportFields reportFields = ReportFields.getInstance();
        PlanWriterData planWriterData = new PlanWriterData();
        planWriterData.setPlan(model.getPlan());
        planWriterData.initializeReportData(reportFields, person);

        return reportFields;

    }

    /**
     * Set a PlanWriterModel with the root being <code>rootPath</code>. This
     * does not load it, you should invoke <code>reloadChildren</code> with
     * the root to start loading.
     */
    public void setRootPath(String rootPath) {

        jScrollPaneTree.setViewportView(null);
        model.setRootPath(null);

        if (rootPath == null) {
            setModified(false);

        } else {
            jScrollPaneTree.setViewportView(null);
            try {
                model.setRootPath(rootPath);
                setTreeTableModel(model);

                reload(model.getRoot());
                setModified(true);

            } finally {
                jScrollPaneTree.setViewportView(treeTable);
            }

        }

    }

    /**
     * Creates and returns the instanceof JTreeTable that will be used. This
     * also creates, but does not start, the Timer that is used to update the
     * display as files are loaded.
     */
    private void setTreeTableModel(TreeTableModel treeTableModel) {

        treeTable.setModel(treeTableModel);
        /*
         * if ( treeTable.getColumnCount() >= PlanWriterModel.SELECTED ) {
         * treeTable.getColumnModel().getColumn( PlanWriterModel.SELECTED
         * ).setCellRenderer( new IndicatorRenderer() ); }
         */
        if (treeTable.getColumnCount() >= PlanWriterModel.SELECTED) {

            javax.swing.JCheckBox cb = new javax.swing.JCheckBox();
            cb.setHorizontalAlignment(SwingConstants.CENTER);
            javax.swing.DefaultCellEditor ce = new javax.swing.DefaultCellEditor(
                    cb);
            treeTable.getColumnModel().getColumn(PlanWriterModel.SELECTED)
                    .setCellEditor(ce);

        }

        //
        setColumnWidth(PlanWriterModel.NAME, 1000, 100, 300);
        setColumnWidth(PlanWriterModel.SELECTED, 50, 10, 50);
        // setColumnWidth( PlanWriterModel.SIZE, 0, 0, 0 );
        // setColumnWidth( PlanWriterModel.TYPE, 0, 0, 0 );
        // setColumnWidth( PlanWriterModel.MODIFIED, 0, 0, 0 );

    }

    public void setColumnWidth(int columnID, int maxWidth, int minWidth,
            int preferredWidth) {
        TableColumn column = treeTable.getColumnModel().getColumn(columnID);
        if (maxWidth >= 0)
            column.setMaxWidth(maxWidth);
        if (minWidth >= 0)
            column.setMinWidth(minWidth);
        if (preferredWidth >= 0)
            column.setPreferredWidth(preferredWidth);
    }

    private void initComponents2() {

        model = new PlanWriterModel();

        jPanelControls.add(jPanelNewPlan, 0); // to insert the component at
                                                // the beggining.

        jButtonReport.setText("Generate Plan");
        jPanelControls.add(jButtonReport, -1); // to insert the component at
                                                // the end.

        jListPlans.addListSelectionListener(model);

        /***********************************************************************
         * 
         **********************************************************************/
        destListModel = new DestinationListModel();

        destList = new CheckBoxList(destListModel);
        destList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // .MULTIPLE_INTERVAL_SELECTION
                                                                        // );
        // destList.setDragAndDropEnabled( true );
        jScrollPaneDestination.setViewportView(destList);

        // destList.addListSelectionListener( this );
        // destList.addMouseListener( this );
        // destList.addMouseListener( jPopupMenuUpDown );
        jListPlans.addListSelectionListener(destListModel); // for selected plan
        destList.addListSelectionListener(destListModel); // for selected plan
                                                            // file

        CustomizedActionListener cal = new CustomizedActionListener(
                jComboBoxPlanTemplate, jLabelPlanTemplate);
        jListPlans.addListSelectionListener(cal);
        //
        jButtonAddItems.addActionListener(cal);
        jButtonRemoveItems.addActionListener(cal);
        jButtonUp.addActionListener(cal);
        jButtonDown.addActionListener(cal);

        jMenuItemUp.setName(DestinationListModel.UP);
        jMenuItemUp.addActionListener(destListModel);

        jMenuItemDown.setName(DestinationListModel.DOWN);
        jMenuItemDown.addActionListener(destListModel);

        jButtonUp.setName(DestinationListModel.UP);
        jButtonUp.addActionListener(destListModel);

        jButtonDown.setName(DestinationListModel.DOWN);
        jButtonDown.addActionListener(destListModel);

        jButtonAddItems.setName(DestinationListModel.ADD);
        jButtonAddItems.addActionListener(destListModel);

        jButtonRemoveItems.setName(DestinationListModel.REMOVE);
        jButtonRemoveItems.addActionListener(destListModel);

        jScrollPaneTree.setPreferredSize(getPreferredSize());

        treeTable = new JTreeTable();
        treeTable.getTree().addTreeSelectionListener(this);

        jFileChooser = new javax.swing.JFileChooser();
        File dir = WordSettings.getInstance().getPlanTemplate();
        jFileChooser.setCurrentDirectory(dir.getParentFile());
        jFileChooser.setSelectedFile(dir);

        // jFileChooser.setFileFilter( new com.argus.io.RTFFileFilter() );
        jFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ; // do what ???
            }
        });
        SwingUtils.setDefaultFont(jFileChooser);

        // treeTable.getTree().addTreeExpansionListener( model.new
        // TreeExpansionListener_impl() );
        // treeTable.getTree().addTreeWillExpandListener( model.new
        // TreeWillExpandListener_impl() );

        Reloader rl = new Reloader();
        timer = new Timer(700, rl);
        timer.setRepeats(true);
        treeTable.getTree().addTreeExpansionListener(rl);

        // set selection mode
        treeTable.getTree().getSelectionModel().setSelectionMode(
                javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);

        jTabbedPaneStateChanged(null);

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        PlanWriterTemplateModel.FileNode node = getSelectedNode();
        jCheckBoxSelectAllSource.setSelected(node != null && node.isSelected());
        jCheckBoxSelectAllSource.setEnabled(node != null
                && node.getChildCount() > 0);
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting())
            return;

        int destSize = destList.getModel().getSize();

        // jCheckBoxSelectAllDestination.setSelected( );
        jCheckBoxSelectAllDestination.setEnabled(destSize > 0);
        jButtonUp.setEnabled(destSize > 0);
        jButtonDown.setEnabled(destSize > 0);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    class PWComboBoxModel extends javax.swing.DefaultComboBoxModel implements
            javax.swing.event.ListSelectionListener,
            java.awt.event.ItemListener {
        protected java.util.Properties props;

        protected String name;

        PWComboBoxModel(String name, java.util.Vector items) {
            super(items);
            this.name = name;
        }

        protected void init() {

            String value = props == null ? null : props.getProperty(name, "");

            if (value != null && !value.equals("")) { // not customized

                for (int i = 0; i < getSize(); i++) {

                    Object item = getElementAt(i);
                    String itemValue = item.toString();
                    if (itemValue.equals(value)
                            || itemValue.indexOf(value) == 0) {
                        setSelectedItem(item);
                        return;
                    }

                }

            }

            setSelectedItem(null);

        }

        /*
         * protected void setSelectedItem2( Object obj ) {
         * 
         * if ( obj == null ) { if ( getSize() >= 0 ) setSelectedItem(
         * getElementAt( 0 ) );
         *  } else { setSelectedItem( obj );
         *  }
         *  }
         */
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

            if (evt.getValueIsAdjusting())
                return;

            ReferenceCode newPlan = (ReferenceCode) ((javax.swing.JList) evt
                    .getSource()).getSelectedValue();

            props = newPlan == null ? null : (java.util.Properties) newPlan
                    .getObject();

            init();

        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {

            if (evt.getStateChange() != evt.SELECTED)
                return; // adjusting

            if (props != null)
                props.setProperty(name, evt.getItem() == null ? "" : evt
                        .getItem().toString());

        }

    }

    class PWTemplateComboBoxModel extends PWComboBoxModel {

        PWTemplateComboBoxModel(String name, java.util.Vector items) {
            super(name, items);
        }

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

            super.valueChanged(evt);

            if (evt.getValueIsAdjusting())
                return;
        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {

            if (evt.getStateChange() != evt.SELECTED)
                return; // adjusting

            if (props == null)
                return;
            // 1. set new template name
            super.itemStateChanged(evt);

            setPlanTemplateFiles((ReferenceCode) evt.getItem());

        }

        private void setPlanTemplateFiles(ReferenceCode template) {

            // 2. remove all entries N=XXX
            int size = props.size();
            for (int i = 1; i <= size; i++) {
                String key = "" + i;
                String file = props.getProperty(key, null);
                if (file != null)
                    props.remove(key);
            }

            // 3. set template files
            if (template != null && template.getObject() != null)
                props.putAll((java.util.Properties) template.getObject());

        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    class DestinationListModel extends javax.swing.DefaultListModel
            // javax.swing.AbstractListModel
            implements javax.swing.event.ListSelectionListener,
            java.awt.event.ItemListener, java.awt.event.ActionListener {
        static final String UP = "UP";

        static final String DOWN = "DOWN";

        static final String ADD = "ADD";

        static final String REMOVE = "REMOVE";

        //
        private javax.swing.JList listUI;

        private int selectedIndex;

        private java.util.Properties props;

        private String selectedFile;

        private java.util.Vector cbs;

        DestinationListModel() {
        }

        private void init() {

            cbs = new java.util.Vector();

            if (props == null)
                return;

            String dir = WordSettings.getInstance().getPlanTemplateDirectory();

            int size = props.size();
            for (int i = 1; i <= size; i++) {
                String fileName = props.getProperty("" + i, null);
                if (fileName == null)
                    continue;

                // check for missing files ( FILE_NOT_FOUND + fileName )
                boolean valid = PlanWriterTemplateModel
                        .isValidFileName(fileName);
                if (valid && fileName.indexOf(dir) != 0)
                    fileName = dir + fileName;

                cbs.add(CheckBoxList.createJCheckBox(fileName, valid));

            }

            if (listUI != null && selectedIndex < cbs.size()
                    && selectedIndex >= 0)
                listUI.setSelectedIndex(selectedIndex); // -1 no selection

        }

        public Object getElementAt(int param) {
            return cbs == null ? null : cbs.elementAt(param);
        }

        public int getSize() {
            return cbs == null ? 0 : cbs.size();
        }

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

            if (evt.getValueIsAdjusting())
                return;

            Object selectedValue = ((javax.swing.JList) evt.getSource())
                    .getSelectedValue();
            selectedFile = null;

            if (selectedValue instanceof ReferenceCode) { // plan list
                ReferenceCode newPlan = (ReferenceCode) selectedValue;
                props = newPlan == null ? null : (java.util.Properties) newPlan
                        .getObject();

                init();
                fireContentsChanged(this, 0, getSize());

            } else if (selectedValue instanceof JCheckBox) { // destination
                                                                // list
                listUI = (javax.swing.JList) evt.getSource();
                selectedIndex = listUI.getSelectedIndex();

                selectedFile = ((JCheckBox) selectedValue).getToolTipText();// .getText();
                System.out.println("DestinationListModel::valueChanged: "
                        + selectedFile);

            }

        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {

            if (evt.getStateChange() != evt.SELECTED)
                return;
            final Object item = evt.getItem();

            // notify that template cb changed (update dest list)
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    init();
                    fireContentsChanged(this, 0, getSize());
                }
            });

        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {

            String name = ((java.awt.Component) evt.getSource()).getName();

            if (UP.equals(name))
                up(true);
            else if (DOWN.equals(name))
                up(false);
            else if (ADD.equals(name))
                add(true);
            else if (REMOVE.equals(name))
                add(false);
            else
                ;

            init();
            fireContentsChanged(this, 0, getSize());

        }

        private void up(boolean up) {

            if (cbs == null || cbs.size() <= 1)
                return;

            // change file order in props
            if (selectedFile == null)
                return;
            // System.out.println( "up=" + up + ", selectedFile=" + selectedFile
            // );

            if (up) {
                selectedIndex--;
                if (selectedIndex < 0) {
                    selectedIndex = 0;
                    return;
                }

            } else { // down
                selectedIndex++;
                if (selectedIndex >= cbs.size()) {
                    selectedIndex = cbs.size() - 1;
                    return;
                }

            }

            String keyPrev = null;
            String valuePrev = null;

            String key = null;
            String value = null;

            int size = props.size();
            for (int i = 1; i <= size; i++) {
                key = "" + i;
                value = props.getProperty(key, null);
                if (value == null)
                    continue;

                // System.out.println( "\tPrev: " + keyPrev + "=" + valuePrev );
                // System.out.println( "\tCurr: " + key + "=" + value );

                if (valuePrev != null && selectedFile.indexOf(valuePrev) >= 0) {
                    // System.out.println( "\tselectedFile.equals( valuePrev )"
                    // );
                    if (!up) {
                        props.setProperty(keyPrev, value);
                        props.setProperty(key, valuePrev);
                        break;
                    }
                }

                if (selectedFile.indexOf(value) >= 0) {
                    // System.out.println( "\tselectedFile.equals( value )" );
                    if (up && keyPrev != null) {
                        props.setProperty(keyPrev, value);
                        props.setProperty(key, valuePrev);
                        break;
                    }
                }

                keyPrev = key;
                valuePrev = value;
                // System.out.println();

            }

        }

        private void add(boolean add) {

            selectedIndex = -1;
            if (props == null || cbs == null)
                return;

            if (add) {

                java.util.Collection nodes = model.getSelectedChildren();
                if (nodes == null)
                    return;

                int size = cbs.size();
                java.util.Iterator iter = nodes.iterator();
                while (iter.hasNext()) {
                    PlanWriterTemplateModel.FileNode node = (PlanWriterTemplateModel.FileNode) iter
                            .next();

                    String value = node.getCanonicalPath();

                    boolean found = false;
                    java.util.Iterator iter2 = cbs.iterator();
                    while (iter2.hasNext()) {
                        String cbvalue = ((JComponent) iter2.next())
                                .getToolTipText().trim();
                        if (cbvalue.equalsIgnoreCase(value)) {
                            found = true;
                            break;
                        }

                    }

                    if (!found) {
                        int key = ++size;
                        props.setProperty("" + key, node.getRelativePath());
                    }

                }

            } else {

                for (int i = 0; i < getSize(); i++)
                    if (((JCheckBox) getElementAt(i)).isSelected())
                        props.remove("" + (i + 1));

                // re-number props
                int count = 0;
                for (int i = 1; i <= props.size(); i++) {
                    String key = "" + i;
                    String value = props.getProperty(key, null);
                    if (value == null)
                        continue; // not a file

                    count++;
                    if (key.equals("" + count))
                        continue; // check order

                    props.remove(key);
                    props.setProperty("" + count, value);
                }

            }

        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    class CustomizedActionListener implements java.awt.event.ActionListener,
            javax.swing.event.ListSelectionListener {
        private java.util.Properties props;

        private java.awt.Component compNonCustomized;

        private java.awt.Component compCustomized;

        CustomizedActionListener(java.awt.Component compNonCustomized,
                java.awt.Component compCustomized) {
            this.compNonCustomized = compNonCustomized;
            this.compCustomized = compCustomized;
        }

        private void init() {

            String template = props == null ? null : props.getProperty(
                    PlanWriterModel.TEMPLATE, null);

            boolean customized = template != null && "".equals(template);
            compNonCustomized.setVisible(!customized);
            compCustomized.setVisible(customized);

        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {

            if (props == null)
                return;
            // System.out.println( "CustomizedActionListener::actionPerformed: "
            // + evt.getSource() );

            props.setProperty(PlanWriterModel.TEMPLATE, "");

            init();
        }

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

            if (evt.getValueIsAdjusting())
                return;

            ReferenceCode newPlan = (ReferenceCode) ((javax.swing.JList) evt
                    .getSource()).getSelectedValue();
            props = newPlan == null ? null : (java.util.Properties) newPlan
                    .getObject();
            System.out.println("CustomizedActionListener::valueChanged: "
                    + evt.getSource() + "\n" + props);

            init();

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpenTemplateDirectory = new javax.swing.JMenuItem();
        jPopupMenuUpDown = new javax.swing.JPopupMenu();
        jMenuItemUp = new javax.swing.JMenuItem();
        jMenuItemDown = new javax.swing.JMenuItem();
        jPanelToolBar = new javax.swing.JPanel();
        jToolBarFile = new javax.swing.JToolBar();
        jButtonOpenTemplateDirectory = new javax.swing.JButton();
        jButtonGeneratePlan = new javax.swing.JButton();
        jButtonGenerateTOC = new javax.swing.JButton();
        jButtonSaveAs = new javax.swing.JButton();
        jPanelNewPlan = new javax.swing.JPanel();
        jButtonNewPlan = new javax.swing.JButton();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelAssumptions = new javax.swing.JPanel();
        jScrollPanePlans = new javax.swing.JScrollPane();
        jListPlans = new javax.swing.JList();
        jPanelOthers = new javax.swing.JPanel();
        jPanelStrategy = new javax.swing.JPanel();
        jComboBoxStrategy = new javax.swing.JComboBox();
        jPanelModels = new javax.swing.JPanel();
        jLabelGearing = new javax.swing.JLabel();
        jComboBoxGearing = new javax.swing.JComboBox();
        jLabelETP = new javax.swing.JLabel();
        jComboBoxETP = new javax.swing.JComboBox();
        jLabelAP = new javax.swing.JLabel();
        jComboBoxAP = new javax.swing.JComboBox();
        jLabelPAYG = new javax.swing.JLabel();
        jComboBoxPAYG = new javax.swing.JComboBox();
        jLabelMortgage = new javax.swing.JLabel();
        jComboBoxMortgage = new javax.swing.JComboBox();
        jLabelDSS = new javax.swing.JLabel();
        jComboBoxDSS = new javax.swing.JComboBox();
        jPanelTemplate = new javax.swing.JPanel();
        jComboBoxPlanTemplate = new javax.swing.JComboBox();
        jLabelPlanTemplate = new javax.swing.JLabel();
        jSplitPane = new javax.swing.JSplitPane();
        jPanelLeft = new javax.swing.JPanel();
        jScrollPaneTree = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxSelectAllSource = new javax.swing.JCheckBox();
        jPanelRight = new javax.swing.JPanel();
        jPanelAddRemove = new javax.swing.JPanel();
        jLabelTransfer = new javax.swing.JLabel();
        jButtonAddItems = new javax.swing.JButton();
        jButtonRemoveItems = new javax.swing.JButton();
        jPanelDestination = new javax.swing.JPanel();
        jScrollPaneDestination = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jCheckBoxSelectAllDestination = new javax.swing.JCheckBox();
        jButtonUp = new javax.swing.JButton();
        jButtonDown = new javax.swing.JButton();

        jMenuFile.setText("File");
        jMenuItemOpenTemplateDirectory.setText("Open Template Directory");
        jMenuItemOpenTemplateDirectory
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemOpenTemplateDirectoryActionPerformed(evt);
                    }
                });

        jMenuFile.add(jMenuItemOpenTemplateDirectory);
        jMenuBar.add(jMenuFile);

        jMenuItemUp.setToolTipText("Move Up");
        jMenuItemUp.setText("Move Up");
        jMenuItemUp.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Up16.gif")));
        jPopupMenuUpDown.add(jMenuItemUp);
        jMenuItemDown.setToolTipText("Move Down");
        jMenuItemDown.setText("Move Down");
        jMenuItemDown.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Down16.gif")));
        jPopupMenuUpDown.add(jMenuItemDown);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(750, 460));
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jPanelToolBar.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT));

        jPanelToolBar.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.RAISED));
        jButtonOpenTemplateDirectory.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/image/Home24.gif")));
        jButtonOpenTemplateDirectory.setToolTipText("Open Template Directory");
        jButtonOpenTemplateDirectory
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonOpenTemplateDirectoryActionPerformed(evt);
                    }
                });

        jToolBarFile.add(jButtonOpenTemplateDirectory);

        jButtonGeneratePlan.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/Play24.gif")));
        jButtonGeneratePlan.setToolTipText("Generate Plan");
        jButtonGeneratePlan
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonGeneratePlanActionPerformed(evt);
                    }
                });

        jToolBarFile.add(jButtonGeneratePlan);

        jButtonGenerateTOC.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/History24.gif")));
        jButtonGenerateTOC.setToolTipText("Generate Table of Contents");
        jButtonGenerateTOC
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonGenerateTOCActionPerformed(evt);
                    }
                });

        jToolBarFile.add(jButtonGenerateTOC);

        jButtonSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/SaveAs24.gif")));
        jButtonSaveAs.setToolTipText("Save As..");
        jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsActionPerformed(evt);
            }
        });

        jToolBarFile.add(jButtonSaveAs);

        jPanelToolBar.add(jToolBarFile);

        jButtonNewPlan.setToolTipText("Create New Plan");
        jButtonNewPlan.setText("New");
        jButtonNewPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewPlanActionPerformed(evt);
            }
        });

        jPanelNewPlan.add(jButtonNewPlan);

        jPanelToolBar.add(jPanelNewPlan);

        add(jPanelToolBar);

        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelAssumptions.setLayout(new java.awt.GridLayout(1, 2));

        jListPlans
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListPlans
                .addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        jListPlansValueChanged(evt);
                    }
                });

        jScrollPanePlans.setViewportView(jListPlans);

        jPanelAssumptions.add(jScrollPanePlans);

        jPanelOthers.setLayout(new javax.swing.BoxLayout(jPanelOthers,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelStrategy.setLayout(new java.awt.GridBagLayout());

        jPanelStrategy.setBorder(new javax.swing.border.TitledBorder(
                "Selected Strategy for Plan"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelStrategy.add(jComboBoxStrategy, gridBagConstraints);

        jPanelOthers.add(jPanelStrategy);

        jPanelModels.setLayout(new java.awt.GridBagLayout());

        jPanelModels.setBorder(new javax.swing.border.TitledBorder(
                "Models to Include in Plan"));
        jLabelGearing.setText("Gearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelModels.add(jLabelGearing, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelModels.add(jComboBoxGearing, gridBagConstraints);

        jLabelETP.setText("ETP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelModels.add(jLabelETP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelModels.add(jComboBoxETP, gridBagConstraints);

        jLabelAP.setText("AP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelModels.add(jLabelAP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelModels.add(jComboBoxAP, gridBagConstraints);

        jLabelPAYG.setText("PAYG");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelModels.add(jLabelPAYG, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelModels.add(jComboBoxPAYG, gridBagConstraints);

        jLabelMortgage.setText("Mortgage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelModels.add(jLabelMortgage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelModels.add(jComboBoxMortgage, gridBagConstraints);

        jLabelDSS.setText("DSS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelModels.add(jLabelDSS, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelModels.add(jComboBoxDSS, gridBagConstraints);

        jPanelOthers.add(jPanelModels);

        jPanelTemplate.setLayout(new java.awt.GridBagLayout());

        jPanelTemplate.setBorder(new javax.swing.border.TitledBorder(
                "Select Plan Template"));
        jPanelTemplate.setPreferredSize(new java.awt.Dimension(160, 50));
        jPanelTemplate.setMinimumSize(new java.awt.Dimension(156, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTemplate.add(jComboBoxPlanTemplate, gridBagConstraints);

        jLabelPlanTemplate.setText("Customized Plan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTemplate.add(jLabelPlanTemplate, gridBagConstraints);

        jPanelOthers.add(jPanelTemplate);

        jPanelAssumptions.add(jPanelOthers);

        jTabbedPane.addTab("Plans", jPanelAssumptions);

        jSplitPane.setDividerLocation(300);
        jSplitPane.setAlignmentX(0.5F);
        jPanelLeft.setLayout(new javax.swing.BoxLayout(jPanelLeft,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelLeft.setBorder(new javax.swing.border.TitledBorder("Source"));
        jScrollPaneTree.setPreferredSize(new java.awt.Dimension(10, 1000));
        jPanelLeft.add(jScrollPaneTree);

        jCheckBoxSelectAllSource.setToolTipText("Select all under this node");
        jCheckBoxSelectAllSource.setText("Select All");
        jCheckBoxSelectAllSource.setAlignmentX(0.5F);
        jCheckBoxSelectAllSource
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBoxSelectAllSourceActionPerformed(evt);
                    }
                });

        jPanel2.add(jCheckBoxSelectAllSource);

        jPanelLeft.add(jPanel2);

        jSplitPane.setLeftComponent(jPanelLeft);

        jPanelRight.setLayout(new javax.swing.BoxLayout(jPanelRight,
                javax.swing.BoxLayout.X_AXIS));

        jPanelAddRemove.setLayout(new javax.swing.BoxLayout(jPanelAddRemove,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelAddRemove.setBorder(new javax.swing.border.EmptyBorder(
                new java.awt.Insets(10, 10, 10, 10)));
        jLabelTransfer.setText("Transfer");
        jPanelAddRemove.add(jLabelTransfer);

        jButtonAddItems.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/ForwardThick16.gif")));
        jButtonAddItems.setToolTipText("Add Document(s)");
        jButtonAddItems.setDefaultCapable(false);
        jPanelAddRemove.add(jButtonAddItems);

        jButtonRemoveItems.setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/image/BackThick16.gif")));
        jButtonRemoveItems.setToolTipText("Remove Document(s)");
        jButtonRemoveItems.setDefaultCapable(false);
        jPanelAddRemove.add(jButtonRemoveItems);

        jPanelRight.add(jPanelAddRemove);

        jPanelDestination.setLayout(new javax.swing.BoxLayout(
                jPanelDestination, javax.swing.BoxLayout.Y_AXIS));

        jPanelDestination.setBorder(new javax.swing.border.TitledBorder(
                "Financial Plan Document"));
        jScrollPaneDestination
                .setPreferredSize(new java.awt.Dimension(10, 1000));
        jPanelDestination.add(jScrollPaneDestination);

        jCheckBoxSelectAllDestination.setText("Select All");
        jCheckBoxSelectAllDestination.setAlignmentX(0.5F);
        jCheckBoxSelectAllDestination
                .addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBoxSelectAllDestinationItemStateChanged(evt);
                    }
                });

        jPanel1.add(jCheckBoxSelectAllDestination);

        jButtonUp.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Up16.gif")));
        jButtonUp.setToolTipText("Move Up");
        jPanel1.add(jButtonUp);

        jButtonDown.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/image/Down16.gif")));
        jButtonDown.setToolTipText("Move Down");
        jPanel1.add(jButtonDown);

        jPanelDestination.add(jPanel1);

        jPanelRight.add(jPanelDestination);

        jSplitPane.setRightComponent(jPanelRight);

        jTabbedPane.addTab("Text", jSplitPane);

        add(jTabbedPane);

    }// GEN-END:initComponents

    private void jListPlansValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jListPlansValueChanged
        if (evt.getValueIsAdjusting())
            return;

        Object selectedValue = ((javax.swing.JList) evt.getSource())
                .getSelectedValue();

        if (selectedValue instanceof ReferenceCode) { // plan list
            ReferenceCode newPlan = (ReferenceCode) selectedValue;
            SwingUtil.setTitle(this, newPlan.getDescription());
        }

    }// GEN-LAST:event_jListPlansValueChanged

    private void jButtonNewPlanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonNewPlanActionPerformed
        doNew(evt);
    }// GEN-LAST:event_jButtonNewPlanActionPerformed

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        int index = jTabbedPane.getSelectedIndex();

        jButtonPrevious.setEnabled(index > 0);
        jButtonNext.setEnabled(index < jTabbedPane.getTabCount() - 1);
    }// GEN-LAST:event_jTabbedPaneStateChanged

    private void jCheckBoxSelectAllSourceActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBoxSelectAllSourceActionPerformed
        PlanWriterTemplateModel.FileNode node = getSelectedNode();
        if (node == null)
            return;

        node.selectAll(jCheckBoxSelectAllSource.isSelected());
    }// GEN-LAST:event_jCheckBoxSelectAllSourceActionPerformed

    private void jCheckBoxSelectAllDestinationItemStateChanged(
            java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jCheckBoxSelectAllDestinationItemStateChanged
        destList.selectAll(evt.getStateChange() == evt.SELECTED);
        // updateControls();
    }// GEN-LAST:event_jCheckBoxSelectAllDestinationItemStateChanged

    private void jButtonGenerateTOCActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonGenerateTOCActionPerformed
    }// GEN-LAST:event_jButtonGenerateTOCActionPerformed

    private void jButtonOpenTemplateDirectoryActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOpenTemplateDirectoryActionPerformed
        openPlan();
    }// GEN-LAST:event_jButtonOpenTemplateDirectoryActionPerformed

    private void jButtonGeneratePlanActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonGeneratePlanActionPerformed
        // hidden toolbar button !!!
        new Thread(new Runnable() {
            public void run() {
                try {
                    doReport();
                } catch (PlanWriterException e) {
                    System.err.println(e.getMessage());
                }
            }
        }, "generatePlan").start();
    }// GEN-LAST:event_jButtonGeneratePlanActionPerformed

    private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveAsActionPerformed
    }// GEN-LAST:event_jButtonSaveAsActionPerformed

    private void jMenuItemOpenTemplateDirectoryActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItemOpenTemplateDirectoryActionPerformed
        openPlan();
    }// GEN-LAST:event_jMenuItemOpenTemplateDirectoryActionPerformed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_formAncestorAdded
        if (added2frame)
            return;

        JInternalFrame iFrame = SwingUtil.getJInternalFrame(this);
        if (iFrame == null) {
            JFrame frame = SwingUtil.getJFrame(this);
            frame.setJMenuBar(jMenuBar);

            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    // public void windowClosed(java.awt.event.WindowEvent evt)
                    // {
                    try {
                        uninitialize();
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        } else {
            iFrame.setJMenuBar(jMenuBar);

            iFrame.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameClosing(InternalFrameEvent evt) {
                    // public void internalFrameClosed(InternalFrameEvent evt) {
                    try {
                        uninitialize();
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }

        added2frame = true;
    }// GEN-LAST:event_formAncestorAdded

    private void jRadioButtonPlainItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_jRadioButtonPlainItemStateChanged

    }// GEN-LAST:event_jRadioButtonPlainItemStateChanged

    public static void display(java.awt.event.FocusListener[] listeners) {
        try {
            if (!exists()) {
                view = new PlanWriter();
                SwingUtil.add2Frame(view, listeners,
                    view.getDefaultTitle(),
                    ViewSettings.getInstance().getViewImage(view.getClass().getName()),
                    true, true, false);
            }
            view.openPlan(WordSettings.getInstance().getPlanTemplateDirectory());
            SwingUtil.setVisible(view, true);
        } catch (ServiceException e) {
            view = null;
            SwingUtil.showError(e);
        }
    }

    // overrite Container method to allow design time UI development
    public java.awt.Component add(java.awt.Component comp) {
        if (thisCTOR)
            return jPanelDetails.add(comp);
        return super.add(comp);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelGearing;

    private javax.swing.JComboBox jComboBoxPlanTemplate;

    private javax.swing.JComboBox jComboBoxDSS;

    private javax.swing.JComboBox jComboBoxStrategy;

    private javax.swing.JLabel jLabelAP;

    private javax.swing.JButton jButtonGenerateTOC;

    private javax.swing.JButton jButtonGeneratePlan;

    private javax.swing.JPanel jPanelLeft;

    private javax.swing.JMenuItem jMenuItemOpenTemplateDirectory;

    private javax.swing.JPanel jPanelToolBar;

    private javax.swing.JButton jButtonSaveAs;

    private javax.swing.JCheckBox jCheckBoxSelectAllDestination;

    private javax.swing.JScrollPane jScrollPaneDestination;

    private javax.swing.JMenuItem jMenuItemUp;

    private javax.swing.JButton jButtonUp;

    private javax.swing.JComboBox jComboBoxETP;

    private javax.swing.JToolBar jToolBarFile;

    private javax.swing.JLabel jLabelDSS;

    private javax.swing.JScrollPane jScrollPanePlans;

    private javax.swing.JPanel jPanelRight;

    private javax.swing.JMenuBar jMenuBar;

    private javax.swing.JPanel jPanelOthers;

    private javax.swing.JButton jButtonNewPlan;

    private javax.swing.JPanel jPanelAssumptions;

    private javax.swing.JLabel jLabelTransfer;

    private javax.swing.JCheckBox jCheckBoxSelectAllSource;

    private javax.swing.JLabel jLabelPlanTemplate;

    private javax.swing.JPopupMenu jPopupMenuUpDown;

    private javax.swing.JComboBox jComboBoxPAYG;

    private javax.swing.JPanel jPanelDestination;

    private javax.swing.JLabel jLabelPAYG;

    private javax.swing.JLabel jLabelETP;

    private javax.swing.JButton jButtonOpenTemplateDirectory;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSplitPane jSplitPane;

    private javax.swing.JScrollPane jScrollPaneTree;

    private javax.swing.JButton jButtonRemoveItems;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JPanel jPanelAddRemove;

    private javax.swing.JPanel jPanelModels;

    private javax.swing.JMenu jMenuFile;

    private javax.swing.JMenuItem jMenuItemDown;

    private javax.swing.JComboBox jComboBoxMortgage;

    private javax.swing.JList jListPlans;

    private javax.swing.JPanel jPanelTemplate;

    private javax.swing.JComboBox jComboBoxAP;

    private javax.swing.JButton jButtonDown;

    private javax.swing.JPanel jPanelNewPlan;

    private javax.swing.JComboBox jComboBoxGearing;

    private javax.swing.JLabel jLabelMortgage;

    private javax.swing.JPanel jPanelStrategy;

    private javax.swing.JButton jButtonAddItems;

    // End of variables declaration//GEN-END:variables

    /**
     * 
     */
    public boolean isModified() {
        return model.getPlan().isModified();
    }

    protected void setModified(boolean value) {
        // if ( isModified() == value ) return;

        if (model.getPlan() == null)
            return;

        model.getPlan().setModified(value);
        // updateControls();
    }

    protected void setModified() {
        setModified(true);
    }

    /**
     * 
     */
    private void openPlan() {

        jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // jFileChooser.setFileFilter( null );
        jFileChooser
                .setDialogTitle("Select Plan Writer template directory ...");

        if (jFileChooser.showOpenDialog(SwingUtil.getJFrame(this)) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            openPlan(jFileChooser.getSelectedFile().getPath());
        } catch (ServiceException e) {
            SwingUtil.showError(e);
        }
    }

    private void openPlan(String dir) throws ServiceException {
        File d = new File(dir);
        if (!d.exists()) {
            throw new ServiceException("Directory does not exists: " + IOUtils.getCanonicalPath(d));
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // initialize tree/document
            uninitialize();
            setRootPath(dir);
            initialize();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            setCursor(null);
        }
    }

    private void canGenerate(boolean checked) 
        throws PlanWriterException
    {
        if (jListPlans.getSelectedIndex() < 0) {
            throw new PlanWriterException( "No Plan selected." );
        }

        if (jComboBoxStrategy.getSelectedIndex() < 1) {
            throw new PlanWriterException( "No Strategy selected." );
        }

        Collection list = checked ? destList.getCheckedValues() : destList.getListData();
        if (list.size() == 0) {
            throw new PlanWriterException( "No Document(s) selected." );
        }

    }

    protected void doReport()
        throws PlanWriterException
    {
        //super.doReport(); // do reports only

        // override BaseView virtual method
        generatePlan(false); // all dest list documents
    }

    private void generatePlan(final boolean checked)
        throws PlanWriterException
    {
        canGenerate(checked);

        PlanWriterTemplateModel.FileNode root = (PlanWriterTemplateModel.FileNode) model.getRoot();
        if (root == null)
            return;

        final List list = checked ? destList.getCheckedValues() : destList.getListData();
        if (list == null || list.size() == 0)
            return;

        Window w = SwingUtilities.windowForComponent(PlanWriter.this);

        final SplashWindow splash = new SplashWindow(null,
                w instanceof Frame ? (Frame) w : null);
        splash.setStringPainted("Waiting for plan writer engine to start ...");
        splash.setVisible(true);

        final Object lockObject = ReportFields.getLockObject();

        new Thread(new Runnable() {
            public void run() {
                synchronized (lockObject) {
                    ReportFields rf = ReportFields.getInstance();

                    new Thread(splash, "SplashWindow").start();
                    splash.setStringPainted("Generating Plan...   Please Wait...");

                    try {
                        splash.setStringPainted("Initialising Word ... ");
                        report.setTemplate(WordSettings.getInstance().getTemplateDocument());

                        try {
                            splash.setStringPainted("Adding Documents...   Please Wait...");
//                          has to be less than 255 characters (word limitation!!!)
                            String [] fileNames = new String [list.size()];
                            for (int i = 0; i < list.size(); i++) {
                                Object obj = list.get(i);
                                String fileName;
                                if (obj instanceof PlanWriterTemplateModel.FileNode) {
                                    PlanWriterTemplateModel.FileNode node = (PlanWriterTemplateModel.FileNode) obj;
                                    fileName = node.getSavedFileName();
                                    if (fileName == null)
                                        fileName = node.getCanonicalPath();
                                } else {
                                    fileName = obj.toString();

                                    File file = new File(fileName);
                                    if (!file.exists()) {
                                        System.err.println("PlanWriter::generatePlan() can not find file: " + fileName);
                                        continue;
                                    }

                                }
                                fileNames[i] = fileName;
                                splash.setStringPainted(fileName);

                            }

                            report.setSubReport(fileNames);

                            splash.setStringPainted("Initializing Data..  Please Wait..");

                            PlanWriterData data = new PlanWriterData();
                            data.setPlan(model.getPlan());
                            data.initializeReportData(rf, clientService);

                            report.setData(rf.getValues());

                            splash.setStringPainted("Running report..  Please Wait..");

                            report.run();
                            
                        } finally {
                            splash.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    } finally {
                        lockObject.notify();
                    }

                } // end of synchronized
            }
        }, "GeneratePlan").start();

    }

    /**
     * Invoked to reload the children of a particular node. This will also
     * restart the timer.
     */
    protected void reload(Object node) {
        model.reloadChildren(node);
        if (!timer.isRunning())
            timer.start();
    }

    /**
     * Reloader is the ActionListener used in the Timer. In response to the
     * timer updating it will reset the reloadRow/reloadPath and generate the
     * necessary event so that the display will update. It also implements the
     * TreeExpansionListener so that if the tree is altered while loading the
     * reloadRow is updated accordingly.
     */
    class Reloader implements ActionListener, TreeExpansionListener {

        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            if (!model.isReloading()) {
                // No longer loading.
                timer.stop();
                if (reloadRow != -1) {
                    generateChangeEvent(reloadRow);
                }
                reloadRow = -1;
                reloadPath = null;

            } else {
                // Still loading, see if paths changed.
                TreePath newPath = model.getPathLoading();

                if (newPath == null) {
                    // Hmm... Will usually indicate the reload thread
                    // completed between time we asked if reloading.
                    if (reloadRow != -1) {
                        generateChangeEvent(reloadRow);
                    }
                    reloadRow = -1;
                    reloadPath = null;

                } else {
                    // Ok, valid path, see if matches last path.
                    int newRow = treeTable.getTree().getRowForPath(newPath);

                    if (newPath.equals(reloadPath)) {
                        reloadCounter = (reloadCounter + 1) % 8;
                        if (newRow != reloadRow) {
                            int lastRow = reloadRow;

                            reloadRow = newRow;
                            generateChangeEvent(lastRow);
                        }
                        generateChangeEvent(reloadRow);
                    } else {
                        int lastRow = reloadRow;

                        reloadCounter = 0;
                        reloadRow = newRow;
                        reloadPath = newPath;
                        if (lastRow != reloadRow) {
                            generateChangeEvent(lastRow);
                        }
                        generateChangeEvent(reloadRow);
                    }
                }
            }
        }

        /**
         * Generates and update event for the specified row. FileSystemModel2
         * could do this, but it would not know when the row has changed as a
         * result of expanding/collapsing nodes in the tree.
         */
        protected void generateChangeEvent(int row) {
            if (row == -1)
                return;

            AbstractTableModel tModel = (AbstractTableModel) treeTable
                    .getModel();
            tModel.fireTableChanged(new TableModelEvent(tModel, row, row, 1));
        }

        //
        // TreeExpansionListener
        //

        /**
         * Invoked when the tree has expanded.
         */
        public void treeExpanded(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent) {
            updateRow();
        }

        /**
         * Invoked when the tree has collapsed.
         */
        public void treeCollapsed(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent) {
            updateRow();
        }

        /**
         * Updates the reloadRow and path, this does not genernate a change
         * event.
         */
        protected void updateRow() {
            reloadPath = model.getPathLoading();
            if (reloadPath != null)
                reloadRow = treeTable.getTree().getRowForPath(reloadPath);
        }

    }

    /**
     * A renderer that will give an indicator when a cell is being reloaded.
     */
    class IndicatorRenderer extends DefaultTableCellRenderer {
        /**
         * Makes sure the number of displayed in an internationalized manner.
         */
        protected NumberFormat formatter;

        /** Row that is currently being painted. */
        protected int lastRow;

        IndicatorRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
            formatter = NumberFormat.getInstance();
        }

        /**
         * Invoked as part of DefaultTableCellRenderers implemention. Sets the
         * text of the label.
         */
        public void setValue(Object value) {
            setText(value == null ? "---" : formatter.format(value));
        }

        /**
         * Returns this.
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            lastRow = row;
            return this;
        }

        /**
         * If the row being painted is also being reloaded this will draw a
         * little indicator.
         */
        public void paint(Graphics g) {
            if (lastRow == reloadRow) {
                int width = getWidth();
                int height = getHeight();

                g.setColor(getBackground());
                g.fillRect(0, 0, width, height);
                g.setColor(getForeground());

                int diameter = Math.min(width, height);

                if (reloadCounter < 5) {
                    g.fillArc((width - diameter) / 2, (height - diameter) / 2,
                            diameter, diameter, 90, -(reloadCounter * 90));
                } else {
                    g.fillArc((width - diameter) / 2, (height - diameter) / 2,
                            diameter, diameter, 90,
                            (4 - reloadCounter % 4) * 90);
                }
            } else {
                super.paint(g);
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void expandPath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().expandPath(selPath);
    }

    public void expandPath() {
        expandPath(treeTable.getTree().getSelectionPath());
    }

    public void collapsePath(TreePath selPath) {
        if (selPath == null)
            return;

        treeTable.getTree().collapsePath(selPath);
    }

    public void collapsePath() {
        collapsePath(treeTable.getTree().getSelectionPath());
    }

    /***************************************************************************
     * 
     **************************************************************************/
    //
    public PlanWriterTemplateModel.FileNode getCurrentCollectionNode() {
        return getSelectedNode();
    }

    private PlanWriterTemplateModel.FileNode getSelectedNode() {
        // Node currently being selected
        return (PlanWriterTemplateModel.FileNode) treeTable.getTree()
                .getLastSelectedPathComponent();
    }

    // ==========================================================================
    //
    // ==========================================================================
    protected void doNext(java.awt.event.ActionEvent evt) {
        int index = jTabbedPane.getSelectedIndex() + 1;
        if (jTabbedPane.getTabCount() > index)
            jTabbedPane.setSelectedIndex(index);
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        int index = jTabbedPane.getSelectedIndex() - 1;
        if (index >= 0)
            jTabbedPane.setSelectedIndex(index);
    }

    public void doClose(java.awt.event.ActionEvent evt) {

        try {
            uninitialize();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    protected void doNew(java.awt.event.ActionEvent evt) {

        String planDesc = JOptionPane.showInputDialog(this, "Title Dialog",
                "Please Input New Plan Title: ", JOptionPane.QUESTION_MESSAGE);

        if (planDesc == null || planDesc.trim().length() == 0)
            return;

        // create new
        ReferenceCode plan = new ReferenceCode(PlanWriterModel.NONE_PLAN);
        plan.setDescription(planDesc);

        getPlans().add(plan);
        jListPlans.setListData(getPlans());

        jListPlans.setSelectedIndex(jListPlans.getModel().getSize() - 1);

        setModified(true);

    }

    public void doSave(java.awt.event.ActionEvent evt) {

        ReferenceCode plan = model.getPlan();
        /*
         * java.util.Properties otherValues = model.getOtherValues();
         * 
         * java.util.Properties planFiles = model.getPlanFiles(); if ( planFiles ==
         * null ) planFiles = new java.util.Properties(); else
         * planFiles.clear();
         * 
         * planFiles.putAll( getSelectedFilesDest( false ) ); planFiles.putAll(
         * otherValues ); plan.setObject( planFiles );
         */
        // updateCustomized();
        // PersonService person = RmiParams.getInstance().getClientPerson();
        if (person == null)
            return;

        try {
            person.storePlan(plan, null);

            SwingUtil.setTitle(this, plan.getDescription());

            setModified(false);

        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
            return;
        }

    }

    private java.util.Properties getSelectedFilesSource(boolean fullPath) {
        java.util.Collection nodes = model.getSelectedChildren();
        if (nodes == null || nodes.size() == 0)
            return new java.util.Properties();

        java.util.Properties selectedFiles = new java.util.Properties();
        int dirLength = WordSettings.getInstance().getPlanTemplateDirectory().length();

        int count = 0;
        java.util.Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            PlanWriterTemplateModel.FileNode node = (PlanWriterTemplateModel.FileNode) iter
                    .next();
            java.io.File file = node.getFile();

            String name = "" + ++count;
            String value = file.getPath();
            if (!fullPath)
                value = value.substring(dirLength);
            selectedFiles.setProperty(name, value);
        }

        return selectedFiles;

    }

    private java.util.Properties getSelectedFilesDest(boolean fullPath) {

        java.util.Collection listData = null;// destList.getListData();
        if (listData == null || listData.size() == 0)
            return new java.util.Properties();

        java.util.Properties selectedFiles = new java.util.Properties();
        String dir = WordSettings.getInstance().getPlanTemplateDirectory();
        int dirLength = dir.length();

        int count = 0;
        java.util.Iterator iter = listData.iterator();
        while (iter.hasNext()) {
            String name = "" + ++count;
            String value = iter.next().toString();
            if (!fullPath) {
                if (value.indexOf(dir) == 0)
                    value = value.substring(dirLength);
            }
            selectedFiles.setProperty(name, value);
        }

        return selectedFiles;

    }

    protected void doDelete(java.awt.event.ActionEvent evt) {

        ReferenceCode plan = model.getPlan();
        if (JOptionPane.showConfirmDialog(this,
                "Do You want to delete this plan?\n" + plan.getDescription(),
                "Delete Plan Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;

        try {
            if (plan.getId() > 0)
                person.deletePlan(plan, null);
            // UnInitialize();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        getPlans().remove(plan);
        jListPlans.setListData(getPlans());

        // doPlanChanged( null );

        // updateCustomized();

    }

    /***************************************************************************
     * 
     **************************************************************************/
    transient private PersonService person;

    public void saveView(PersonService person) throws Exception {

    }

    public void updateView(PersonService person) throws Exception {

        this.person = person;

        //
        jListPlans.setListData(getPlans(person));

        //
        java.util.Vector items = new java.util.Vector();
        items.add(0, StrategyGroup.NONE);
        java.util.Collection set = ((ClientService) person).getStrategies();
        if (set != null && set.size() > 0)
            items.addAll(set);

        jComboBoxStrategy.setName(model.STRATEGY);
        PWComboBoxModel pwcbm = new PWComboBoxModel(model.STRATEGY, items);
        jComboBoxStrategy.setModel(pwcbm);
        jComboBoxStrategy.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        //
        ModelCollection mc = ((ClientService) person).getModels();

        jComboBoxGearing.setName(model.GEARING);
        pwcbm = new PWComboBoxModel(model.GEARING, getComboBoxItems(mc,
                ModelTypeID.INVESTMENT_GEARING));
        jComboBoxGearing.setModel(pwcbm);
        jComboBoxGearing.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        jComboBoxETP.setName(model.ETP);
        pwcbm = new PWComboBoxModel(model.ETP, getComboBoxItems(mc,
                ModelTypeID.ETP_ROLLOVER));
        jComboBoxETP.setModel(pwcbm);
        jComboBoxETP.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        jComboBoxAP.setName(model.AP);
        pwcbm = new PWComboBoxModel(model.AP, getComboBoxItems(mc,
                ModelTypeID.ALLOCATED_PENSION));
        jComboBoxAP.setModel(pwcbm);
        jComboBoxAP.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        jComboBoxPAYG.setName(model.PAYG);
        pwcbm = new PWComboBoxModel(model.PAYG, getComboBoxItems(mc,
                ModelTypeID.PAYG_CALC));
        jComboBoxPAYG.setModel(pwcbm);
        jComboBoxPAYG.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        jComboBoxMortgage.setName(model.MORTGAGE);
        pwcbm = new PWComboBoxModel(model.MORTGAGE, getComboBoxItems(mc,
                ModelTypeID.LOAN_MORTGAGE_CALC));
        jComboBoxMortgage.setModel(pwcbm);
        jComboBoxMortgage.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        jComboBoxDSS.setName(model.DSS);
        pwcbm = new PWComboBoxModel(model.DSS, getComboBoxItems(mc,
                ModelTypeID.SOCIAL_SECURITY_CALC));
        jComboBoxDSS.setModel(pwcbm);
        jComboBoxDSS.addItemListener(pwcbm);
        jListPlans.addListSelectionListener(pwcbm);

        //
        jComboBoxPlanTemplate.setName(model.TEMPLATE);
        pwcbm = new PWTemplateComboBoxModel(model.TEMPLATE,
                getTemplatePlans(person));
        jComboBoxPlanTemplate.setModel(pwcbm);
        jListPlans.addListSelectionListener(pwcbm); // update cb when plan
                                                    // changed
        jComboBoxPlanTemplate.addItemListener(pwcbm); // model listen to
                                                        // update planFiles
        jComboBoxPlanTemplate.addItemListener(destListModel); // destination
                                                                // will listen
                                                                // on plan
                                                                // template
                                                                // changes
        jComboBoxPlanTemplate.addItemListener(model); // source will listen on
                                                        // plan template changes

    }

    private java.util.Vector getComboBoxItems(ModelCollection mc,
            Integer modelTypeID) {
        java.util.Vector items = mc.getModels(modelTypeID);
        if (items == null)
            items = new java.util.Vector();
        else
            items = new java.util.Vector(items);
        items.add(0, Model.NONE);
        return items;
    }

    private Object getSelection(JComboBox cb, String value) {

        if (value != null) {

            for (int i = 0; i < cb.getItemCount(); i++) {
                Object item = cb.getItemAt(i);
                String itemValue = item.toString();
                if (itemValue.equals(value) || itemValue.indexOf(value) == 0)
                    return item;

            }

        }

        return null;

    }

    private void setSelection(JComboBox cb, String value) {

        if (value != null) {

            for (int i = 0; i < cb.getItemCount(); i++) {

                Object item = cb.getItemAt(i);
                String itemValue = item.toString();
                if (itemValue.equals(value) || itemValue.indexOf(value) == 0) {
                    cb.setSelectedItem(item);
                    return;
                }

            }

        }

        cb.setSelectedIndex(cb.getItemCount() == 0 ? -1 : 0);

    }

}