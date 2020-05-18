/*
 * PlanWriterTemplateModel.java
 *
 * Created on 18 February 2002, 17:34
 */

package com.argus.financials.ui.plan;

/*
 * 
 * @author valeri chibaev
 * 
 * @version
 * 
 * parameter format <NameSpaceURL:value-of select=Node/>
 * 
 */

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Stack;

import javax.swing.tree.TreePath;

import com.argus.financials.config.WordSettings;
import com.argus.io.IOUtils;
import com.argus.io.IOUtils2;
import com.argus.io.RTFFileFilter;
import com.argus.swing.table.AbstractTreeTableModel;
import com.argus.swing.table.MergeSort;
import com.argus.swing.table.TreeTableModel;
import com.argus.util.ReferenceCode;

public class PlanWriterTemplateModel extends AbstractTreeTableModel {

    public static final String NONE = "None";

    public static final ReferenceCode NONE_TEMPLATE_PLAN;

    static {
        NONE_TEMPLATE_PLAN = new ReferenceCode(0, NONE);
        NONE_TEMPLATE_PLAN.setObject(new java.util.Properties());
    }

    public static final String FILE_NOT_FOUND = "File Not Found: ";

    private String defaultFilter = RTFFileFilter.RTF;

    public static final int NAME = 0;

    public static final int SELECTED = 1;

    // public static final int SIZE = 2;
    // public static final int TYPE = 3;
    // public static final int MODIFIED = 4;

    // Names of the columns.
    static protected String[] cNames = { "Name", "Select",
    // "Size", "Type", "Modified"
    };

    // Types of the columns.
    static protected Class[] cTypes = { TreeTableModel.class, Boolean.class,
    // Integer.class, String.class, Date.class
    };

    // Column editable.
    static protected boolean[] editable = { true, true,
    // false, false, false
    };

    // The the returned file length for directories.
    public static final Integer ZERO = new Integer(0);

    /** An array of MergeSorter sorters, that will sort based on size. */
    static Stack sorters = new Stack();

    /**
     * True if the receiver is valid, once set to false all Threads loading
     * files will stop.
     */
    protected boolean isValid;

    /**
     * Node currently being reloaded, this becomes somewhat muddy if reloading
     * is happening in multiple threads.
     */
    protected FileNode reloadNode;

    /** > 0 indicates reloading some nodes. */
    int reloadCount;

    /** Returns true if links are to be descended. */
    protected boolean descendLinks;

    /**
     * Returns a MergeSort that can sort on the totalSize of a FileNode.
     */
    protected static MergeSort getSizeSorter() {
        synchronized (sorters) {
            if (sorters.size() == 0) {
                // return new SizeSorter();
                return new AlfaSorter();
            }
            return (MergeSort) sorters.pop();
        }
    }

    /**
     * Should be invoked when a MergeSort is no longer needed.
     */
    protected static void recycleSorter(MergeSort sorter) {
        synchronized (sorters) {
            sorters.push(sorter);
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private ReferenceCode plan;

    // selected files from plan
    private java.util.Vector selectedFiles;

    public static boolean isValidFileName(String fileName) {
        return fileName.trim().indexOf(FILE_NOT_FOUND) != 0;
    }

    public java.util.Properties getPlanFiles() {
        java.util.Properties props = IOUtils2.objectToProperties(getPlan()
                .getObject());
        getPlan().setObject(props);
        return (java.util.Properties) getPlan().getObject();
    }

    public void setModified() {
        if (getPlan() != null)
            getPlan().setModified();
    }

    public ReferenceCode getPlan() {
        if (plan == null) {
            plan = new ReferenceCode();
            plan.setObject(new java.util.Properties());
        }
        return plan;
    }

    public void setPlan(ReferenceCode value) {

        plan = value;
        if (root == null)
            return;

        FileNode rootNode = (FileNode) this.getRoot();
        rootNode.selectAll(false);

        selectedFiles = new java.util.Vector();

        // load plan files
        java.util.Properties planFiles = getPlanFiles();
        if (planFiles == null)
            return;

        int size = planFiles.size();
        for (int i = 1; i <= size; i++) {
            String key = "" + i;
            String fileName = planFiles.getProperty(key, null);
            if (fileName == null)
                continue;

            fileName = fileName.trim();
            System.out.println(fileName);

            // clear file name from FILE_NOT_FOUND prefix
            // "File Not Found: e. Closing Pages\Fees & Charges v2.rtf"
            if (!isValidFileName(fileName))
                fileName = fileName.substring(FILE_NOT_FOUND.length()).trim();

            FileNode node = rootNode.find(fileName);
            if (node == null) {
                fileName = FILE_NOT_FOUND + fileName;
                planFiles.setProperty(key, fileName);
                System.err.println(fileName);
                continue;
            }
            planFiles.setProperty(key, node.getRelativePath());

            node.setSelected(true);

            if (node.isLeaf())
                selectedFiles.add(node);

            while ((node = node.getParent()) != null)
                node.setSelected(true);

        }

    }

    public java.util.Vector getSelectedFiles() {
        return selectedFiles;
    }

    public void selectAll(boolean value) {
        FileNode rootNode = (FileNode) getRoot();
        if (rootNode != null)
            rootNode.selectAll(value);
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public PlanWriterTemplateModel() {
        super(null);
    }

    /**
     * Set a PlanWriterTemplateModel with the root being <code>rootPath</code>.
     * This does not load it, you should invoke <code>reloadChildren</code>
     * with the root to start loading.
     */
    public void setRootPath(String rootPath) {
        setPlan(null);

        if (rootPath == null) {
            root = null;
            // selectAll( false );
            return; // rootPath = File.separator;
        }

        isValid = true;
        root = new FileNode(new File(rootPath));
    }

    /***************************************************************************
     * The TreeModel interface
     **************************************************************************/

    /**
     * Returns the number of children of <code>node</code>.
     */
    public int getSelectedChildrenCount() {
        java.util.Collection nodes = getSelectedChildren();
        return nodes == null ? 0 : nodes.size();
    }

    public java.util.Collection getSelectedChildren() {
        return getRoot() == null ? null : ((FileNode) getRoot())
                .getSelectedChildren();
    }

    public int getChildCount(Object node) {
        Object[] children = getChildren(node);
        return (children == null) ? 0 : children.length;
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
        if (node instanceof FileNode)
            return ((FileNode) node).isLeaf();
        return false;
    }

    /***************************************************************************
     * The TreeTableNode interface.
     **************************************************************************/

    /**
     * Returns the number of columns.
     */
    public int getColumnCount() {
        return cNames.length;
    }

    /**
     * Returns the name for a particular column.
     */
    public String getColumnName(int column) {
        return cNames[column];
    }

    /**
     * Returns the class for the particular column.
     */
    public Class getColumnClass(int column) {
        return cTypes[column];
    }

    /**
     * Returns the value of the particular column.
     */
    public Object getValueAt(Object node, int column) {
        FileNode fn = (FileNode) node;

        try {
            switch (column) {
            case NAME:
                return fn;
            case SELECTED:
                return fn.isSelected() ? Boolean.TRUE : Boolean.FALSE;
                /*
                 * case SIZE: if (fn.isTotalSizeValid()) { return new
                 * Integer((int)((FileNode)node).totalSize()); } return null;
                 * case TYPE: return fn.isLeaf() ? "File" : "Directory"; case
                 * MODIFIED: return fn.lastModified();
                 */
            default:
                ;
            }
        } catch (SecurityException se) {
        }

        return null;
    }

    public void setValueAt(Object aValue, Object node, int column) {
        super.setValueAt(aValue, node, column); // do nothing

        FileNode fn = (FileNode) node;
        switch (column) {
        case NAME:
            break;
        case SELECTED:
            fn.setSelected(((Boolean) aValue).booleanValue());
            break;
        /*
         * case SIZE: break; case TYPE: break; case MODIFIED: break;
         */
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

        FileNode _node = (FileNode) node;
        // if ( _node.isRoot() ) return false;

        if (column == SELECTED && !_node.isLeaf())
            return false;
        return editable[column] || super.isCellEditable(_node, column);
    }

    //
    // Some convenience methods.
    //

    /**
     * Reloads the children of the specified node.
     */
    public void reloadChildren(Object node) {
        FileNode fn = (FileNode) node;

        synchronized (this) {
            reloadCount++;
        }
        fn.resetSize();

        Runnable r = new FileNodeLoader((FileNode) node);
        // new Thread( r ).start();
        r.run();

    }

    /**
     * Stops and waits for all threads to finish loading.
     */
    public void stopLoading() {
        isValid = false;
        synchronized (this) {
            while (reloadCount > 0) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                }
            }
        }
        isValid = true;
    }

    /**
     * If <code>newValue</code> is true, links are descended. Odd results may
     * happen if you set this while other threads are loading.
     */
    public void setDescendsLinks(boolean newValue) {
        descendLinks = newValue;
    }

    /**
     * Returns true if links are to be automatically descended.
     */
    public boolean getDescendsLinks() {
        return descendLinks;
    }

    /**
     * Returns the path <code>node</code> represents.
     */
    public String getPath(Object node) {
        return ((FileNode) node).getFile().getPath();
    }

    /**
     * Returns the total size of the receiver.
     */
    public long getTotalSize(Object node) {
        return ((FileNode) node).totalSize();
    }

    /**
     * Returns true if the receiver is loading any children.
     */
    public boolean isReloading() {
        return reloadCount > 0;
    }

    /**
     * Returns the path to the node that is being loaded.
     */
    public TreePath getPathLoading() {
        FileNode rn = reloadNode;
        if (rn == null)
            return null;
        return new TreePath(rn.getPath());
    }

    /**
     * Returns the node being loaded.
     */
    public Object getNodeLoading() {
        return reloadNode;
    }

    protected File getFile(Object node) {
        FileNode fileNode = ((FileNode) node);
        return fileNode.getFile();
    }

    protected Object[] getChildren(Object node) {
        return node == null ? null : ((FileNode) node).getChildren();
    }

    protected static FileNode[] EMPTY_CHILDREN = new FileNode[0];

    // Used to sort the file names.
    static private MergeSort fileMS = new MergeSort() {
        public int compareElementsAt(int beginLoc, int endLoc) {
            return ((String) toSort[beginLoc])
                    .compareToIgnoreCase((String) toSort[endLoc]);
        }
    };

    /**
     * A FileNode is a derivative of the File class - though we delegate to the
     * File object rather than subclassing it. It is used to maintain a cache of
     * a directory's children and therefore avoid repeated access to the
     * underlying file system during rendering.
     */
    class FileNode {
        // java.io.File the receiver represents.
        protected File file;

        // Parent FileNode of the receiver.
        private FileNode parent;

        // Children of the receiver.
        protected FileNode[] children;

        // Size of the receiver and all its children.
        protected long totalSize;

        // Valid if the totalSize has finished being calced.
        protected boolean totalSizeValid;

        // Path of the receiver.
        protected String canonicalPath;

        protected String relativePath;

        // Path of the saved version of this template.
        protected String savedFileName;

        // True if the canonicalPath of this instance does not start with
        // the canonical path of the parent.
        protected boolean isLink;

        // Date last modified.
        protected Date lastModified;

        // True if this node is selected to be used in plan writer
        protected boolean selected;

        protected FileNode(File file) {
            this(null, file);
        }

        protected FileNode(FileNode parent, File file) {

            this.parent = parent;
            this.file = file;

            String planTemplateDirectory = WordSettings.getInstance().getPlanTemplateDirectory();
            try {
                canonicalPath = file.getCanonicalPath();
                if (planTemplateDirectory.length() >= canonicalPath.length())
                    relativePath = "";
                else
                    relativePath = canonicalPath
                            .substring(planTemplateDirectory.length());
            } catch (IOException e) {
                e.printStackTrace(System.err);
                canonicalPath = "";
                relativePath = "";
            }

            // for root
            // selected = parent == null;
            isLink = parent == null || selected ? false : !canonicalPath
                    .startsWith(parent.getCanonicalPath());

            if (isLeaf()) {
                totalSize = file.length();
                totalSizeValid = true;
            }

        }

        /**
         * Returns the date the receiver was last modified.
         */
        public Date lastModified() {
            if (lastModified == null && file != null)
                lastModified = new Date(file.lastModified());
            return lastModified;
        }

        /**
         * Returns the the string to be used to display this leaf in the JTree.
         */
        public String toString() {
            return file.getName();
            // return file.getFile().getName();
        }

        /**
         * Returns the java.io.File the receiver represents.
         */
        public File getFile() {
            return file;
        }

        /**
         * Returns size of the receiver and all its children.
         */
        public long totalSize() {
            return totalSize;
        }

        /**
         * Returns the parent of the receiver.
         */
        public FileNode getParent() {
            return parent;
        }

        /**
         * Returns true if the receiver represents a leaf, that is it is isn't a
         * directory.
         */
        public boolean isLeaf() {
            return file.isFile();
        }

        public boolean isRoot() {
            return parent == null;
        }

        // e.g. D:\\projects\\Financial Planner
        // Documents\\production\\templates\\clientplantext\\general\\ClientReport.rtf
        public FileNode find(String fileName) {
            if (fileName == null)
                return null;

            // e.g. \\general\\ClientReport.rtf
            String path = getRelativePath();

            if (fileName.indexOf(path) >= 0
                    && fileName.indexOf(path) + path.length() == fileName
                            .length())
                return this;

            if (children == null)
                return null;

            for (int i = 0; i < children.length; i++) {
                FileNode node = children[i].find(fileName);
                if (node != null)
                    return node;
            }

            return null;

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
            if (!selected && !this.isLeaf()) {
                // directory is deselected, we have to deselect all children
                // (file/dir)
                FileNode[] nodes = this.getChildren();
                if (nodes == null)
                    return;

                for (int i = 0; i < nodes.length; i++)
                    nodes[i].setSelected(value);
            }

            if (selected && parent != null && !parent.isSelected())
                // child selected, but parent is not. Let us fix this problem
                parent.setSelected(true);

            nodeChanged();
        }

        public void selectAll(boolean value) {
            // selected = this == root ? true : value;
            selected = value;

            if (!this.isLeaf()) {
                FileNode[] nodes = this.getChildren();
                if (nodes == null)
                    return;

                for (int i = 0; i < nodes.length; i++)
                    nodes[i].selectAll(value);
            }

            nodeChanged();
        }

        /**
         * Returns true if the total size is valid.
         */
        public boolean isTotalSizeValid() {
            return totalSizeValid;
        }

        /**
         * Clears the date.
         */
        protected void resetLastModified() {
            lastModified = null;
        }

        /**
         * Sets the size of the receiver to be 0.
         */
        protected void resetSize() {
            alterTotalSize(-totalSize);
        }

        /**
         * Loads the children, caching the results in the children instance
         * variable.
         */
        protected FileNode[] getChildren() {
            return children;
        }

        public int getChildCount() {
            return (children == null) ? 0 : children.length;
        }

        protected java.util.Collection getSelectedChildren() {

            if (children == null || children.length == 0)
                return null;

            java.util.ArrayList list = new java.util.ArrayList();

            for (int i = 0; i < children.length; i++) {
                if (!children[i].isSelected())
                    continue;

                if (children[i].isLeaf()) {
                    list.add(children[i]);
                } else {
                    java.util.Collection c = children[i].getSelectedChildren();
                    if (c != null && c.size() > 0)
                        list.addAll(c);
                }
            }

            return list;

        }

        /*
         * public boolean isSelectedLeaf() { return this.isLeaf() &&
         * this.isSelected(); }
         */

        /**
         * Recursively loads all the children of the receiver.
         */
        protected void loadChildren(MergeSort sorter) {
            totalSize = file.length();
            children = createChildren(null);
            for (int counter = children.length - 1; counter >= 0; counter--) {
                Thread.yield(); // Give the GUI CPU time to draw itself.
                if (!children[counter].isLeaf()
                        && (descendLinks || !children[counter].isLink())) {
                    children[counter].loadChildren(sorter);
                }
                totalSize += children[counter].totalSize();
                if (!isValid) {
                    counter = 0;
                }
            }
            if (isValid) {
                if (sorter != null) {
                    sorter.sort(children);
                }
                totalSizeValid = true;
            }
        }

        /**
         * Loads the children of of the receiver.
         */
        protected FileNode[] createChildren(MergeSort sorter) {
            FileNode[] retArray = null;

            try {
                String[] files = file.list(new java.io.FilenameFilter() {
                    public boolean accept(java.io.File dir, String name) {
                        if (dir.isDirectory()
                                && new java.io.File(dir.getPath()
                                        + java.io.File.separator + name)
                                        .isDirectory())
                            return true;

                        String extension = IOUtils.getExtension(name);
                        return extension != null
                                && extension.equals(defaultFilter);
                    }
                });

                if (files != null) {
                    if (sorter != null) {
                        sorter.sort(files);
                    }
                    retArray = new FileNode[files.length];
                    String path = file.getPath();
                    for (int i = 0; i < files.length; i++) {
                        File childFile = new File(path, files[i]);
                        retArray[i] = new FileNode(this, childFile);
                    }
                }
            } catch (SecurityException se) {
            }

            return retArray == null ? EMPTY_CHILDREN : retArray;
        }

        /**
         * Returns true if the children have been loaded.
         */
        protected boolean loadedChildren() {
            return (file.isFile() || (children != null));
        }

        /**
         * Gets the path from the root to the receiver.
         */
        public FileNode[] getPath() {
            return getPathToRoot(this, 0);
        }

        /**
         * Returns the canonical path for the receiver.
         */
        public String getCanonicalPath() {
            return canonicalPath;
        }

        public String getRelativePath() {
            return relativePath;
        }

        /**
         * Returns/Set the saved file name.
         */
        public String getSavedFileName() {
            return savedFileName;
        }

        public void setSavedFileName(String value) {
            savedFileName = value;
        }

        /**
         * Returns true if the receiver's path does not begin with the parent's
         * canonical path.
         */
        public boolean isLink() {
            return isLink;
        }

        protected FileNode[] getPathToRoot(FileNode aNode, int depth) {
            FileNode[] retNodes;

            if (aNode == null) {
                if (depth == 0)
                    return null;
                else
                    retNodes = new FileNode[depth];
            } else {
                depth++;
                retNodes = getPathToRoot(aNode.getParent(), depth);
                retNodes[retNodes.length - depth] = aNode;
            }
            return retNodes;
        }

        /**
         * Sets the children of the receiver, updates the total size, and if
         * generateEvent is true a tree structure changed event is created.
         */
        protected void setChildren(FileNode[] newChildren, boolean generateEvent) {
            long oldSize = totalSize;

            totalSize = file.length();
            children = newChildren;
            for (int counter = children.length - 1; counter >= 0; counter--) {
                if (children[counter] != null)
                    totalSize += children[counter].totalSize();
            }

            if (generateEvent) {
                FileNode[] path = getPath();

                fireTreeStructureChanged(PlanWriterTemplateModel.this, path,
                        null, null);

                FileNode parent = getParent();

                if (parent != null) {
                    parent.alterTotalSize(totalSize - oldSize);
                }
            }
        }

        protected synchronized void alterTotalSize(long sizeDelta) {
            if (sizeDelta != 0 && (parent = getParent()) != null) {
                totalSize += sizeDelta;
                nodeChanged();
                parent.alterTotalSize(sizeDelta);
            } else {
                // Need a way to specify the root.
                totalSize += sizeDelta;
            }
        }

        /**
         * This should only be invoked on the event dispatching thread.
         */
        protected synchronized void setTotalSizeValid(boolean newValue) {
            if (totalSizeValid != newValue) {
                nodeChanged();
                totalSizeValid = newValue;

                FileNode parent = getParent();

                if (parent != null) {
                    parent.childTotalSizeChanged(this);
                }
            }
        }

        /**
         * Marks the receivers total size as valid, but does not invoke node
         * changed, nor message the parent.
         */
        protected synchronized void forceTotalSizeValid() {
            totalSizeValid = true;
        }

        /**
         * Invoked when a childs total size has changed.
         */
        protected synchronized void childTotalSizeChanged(FileNode child) {
            if (totalSizeValid != child.isTotalSizeValid()) {
                if (totalSizeValid) {
                    setTotalSizeValid(false);
                } else {
                    FileNode[] children = getChildren();

                    for (int counter = children.length - 1; counter >= 0; counter--) {
                        if (!children[counter].isTotalSizeValid()) {
                            return;
                        }
                    }
                    setTotalSizeValid(true);
                }
            }

        }

        /**
         * Can be invoked when a node has changed, will create the appropriate
         * event.
         */
        protected void nodeChanged() {
            // FileNode parent = getParent();

            if (parent != null) {
                FileNode[] path = parent.getPath();
                int[] index = { getIndexOfChild(parent, this) };
                Object[] children = { this };

                fireTreeNodesChanged(PlanWriterTemplateModel.this, path, index,
                        children);
            }
        }
    }

    /**
     * FileNodeLoader can be used to reload all the children of a particular
     * node. It first resets the children of the FileNode it is created with,
     * and in its run method will reload all of that nodes children.
     * FileNodeLoader may not be running in the event dispatching thread. As
     * swing is not thread safe it is important that we don't generate events in
     * this thread. SwingUtilities.invokeLater is used so that events are
     * generated in the event dispatching thread.
     */
    class FileNodeLoader implements Runnable {
        /** Node creating children for. */
        FileNode node;

        /** Sorter. */
        MergeSort sizeMS;

        FileNodeLoader(FileNode node) {
            this.node = node;
            node.resetLastModified();
            node.setChildren(node.createChildren(fileMS), true);
            node.setTotalSizeValid(false);
        }

        public void run() {
            FileNode[] children = node.getChildren();

            sizeMS = getSizeSorter();
            for (int counter = children.length - 1; counter >= 0; counter--) {
                if (!children[counter].isLeaf()) {
                    reloadNode = children[counter];
                    loadChildren(children[counter]);
                    reloadNode = null;
                }
                if (!isValid) {
                    counter = 0;
                }
            }
            recycleSorter(sizeMS);
            if (isValid) {
                // SwingUtilities.invokeLater(new Runnable() { public void run()
                // {
                MergeSort sorter = getSizeSorter();

                sorter.sort(node.getChildren());
                recycleSorter(sorter);
                node.setChildren(node.getChildren(), true);
                synchronized (PlanWriterTemplateModel.this) {
                    reloadCount--;
                    PlanWriterTemplateModel.this.notifyAll();
                }
                // }});

            } else {
                synchronized (PlanWriterTemplateModel.this) {
                    reloadCount--;
                    PlanWriterTemplateModel.this.notifyAll();
                }
            }
        }

        protected void loadChildren(FileNode node) {
            if (!node.isLeaf() && (descendLinks || !node.isLink())) {
                final FileNode[] children = node.createChildren(null);

                for (int counter = children.length - 1; counter >= 0; counter--) {
                    if (!children[counter].isLeaf()) {
                        if (descendLinks || !children[counter].isLink()) {
                            children[counter].loadChildren(sizeMS);
                        } else {
                            children[counter].forceTotalSizeValid();
                        }
                    }
                    if (!isValid) {
                        counter = 0;
                    }
                }
                if (isValid) {
                    final FileNode fn = node;

                    // Reset the children
                    // SwingUtilities.invokeLater(new Runnable() { public void
                    // run() {
                    MergeSort sorter = getSizeSorter();

                    sorter.sort(children);
                    recycleSorter(sorter);
                    fn.setChildren(children, true);
                    fn.setTotalSizeValid(true);
                    fn.nodeChanged();
                    // }});
                }
            } else {
                node.forceTotalSizeValid();
            }
        }
    }

    /**
     * Sorts the contents, which must be instances of FileNode based on
     * totalSize.
     */
    static class SizeSorter extends MergeSort {

        public int compareElementsAt(int beginLoc, int endLoc) {
            long firstSize = ((FileNode) toSort[beginLoc]).totalSize();
            long secondSize = ((FileNode) toSort[endLoc]).totalSize();

            if (firstSize != secondSize) {
                return (int) (secondSize - firstSize);
            }
            return ((FileNode) toSort[beginLoc])
                    .toString()
                    .compareToIgnoreCase(((FileNode) toSort[endLoc]).toString());
        }

    }

    /**
     * Sorts the contents, which must be instances of FileNode based on
     * alfabetical order.
     */
    static class AlfaSorter extends MergeSort {

        public int compareElementsAt(int beginLoc, int endLoc) {
            String s1 = ((FileNode) toSort[beginLoc]).getRelativePath();
            String s2 = ((FileNode) toSort[endLoc]).getRelativePath();

            return compare(s1, s2);
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

    /**
     * javax.swing.event.TreeExpansionListener server
     */
    class TreeExpansionListener_impl implements
            javax.swing.event.TreeExpansionListener {

        public void treeExpanded(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent) {
            // System.out.println( "Tree-expanded event detected." );
        }

        public void treeCollapsed(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent) {
            // System.out.println( "Tree-collapsed event detected." );
        }

    }

    /**
     * javax.swing.event.TreeWillExpandListener interface
     */
    class TreeWillExpandListener_impl implements
            javax.swing.event.TreeWillExpandListener {

        // Invoked whenever a node in the tree is about to be expanded.
        public void treeWillExpand(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent)
                throws javax.swing.tree.ExpandVetoException {
            // System.out.println( "Tree-will-expand event detected" );

            TreePath tp = treeExpansionEvent.getPath();
            FileNode fn = (FileNode) tp.getPathComponent(tp.getPathCount() - 1);
            if (!fn.isSelected()) {
                // User said cancel expansion.
                // System.out.println( "Tree expansion cancelled" );
                throw new javax.swing.tree.ExpandVetoException(
                        treeExpansionEvent);
            }

        }

        // Invoked whenever a node in the tree is about to be collapsed.
        public void treeWillCollapse(
                javax.swing.event.TreeExpansionEvent treeExpansionEvent)
                throws javax.swing.tree.ExpandVetoException {
            // System.out.println( "Tree-will-collapse event detected." );
        }

    }

}