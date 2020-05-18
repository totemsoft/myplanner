/*
 * SwingUtilities.java
 *
 * Created on 5 October 2001, 10:16
 */

package com.argus.financials.swing;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;

import com.argus.config.ResourceLoader;
import com.argus.financials.api.ServiceException;
import com.argus.swing.ComboBoxKeySelectionManager;
import com.argus.swing.SwingUtils;

public class SwingUtil {

    private static Color EDITABLE = Color.white;
    private static Color NON_EDITABLE = Color.lightGray;

    // default icon settings
    private final static String imageName = "/image/logo/financial-planner.gif";

    private static JDesktopPane desktop;

    public static JDesktopPane getDesktop() {
        if (desktop == null) {
            desktop = new JDesktopPane();
            // Make dragging faster:
            // desktop.putClientProperty("JDesktopPane.dragMode", "outline");
            // //pre-1.3 code
            desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE); // 1.3 code
        }
        return desktop;
    }

    public static boolean isDisplayOnDesktop() {
        // return FPSLocale.getInstance().isDisplayOnDesktop();
        return Boolean.valueOf(System.getProperty("DisplayOnDesktop"))
                .booleanValue();
    }

    public static void showError(ServiceException e) {
        // TODO: pop-up dialog
        System.err.println(e.getMessage());
    }

    /**
     * 
     */
    public static ImageIcon getIconImage(String name) 
    {
        if (name == null || name.equals(""))
            name = imageName;
        return new ImageIcon(ResourceLoader.getResource(name));
    }

    public static void setIconImage(Frame frame, String name)
    {
        frame.setIconImage(getIconImage(name).getImage());
    }

    public static void setVisible(JComponent component, boolean value) {
        if (component.getTopLevelAncestor() instanceof JApplet)
            component.setVisible(value);
        else {
            Component c = getJDialog(component);
            if (c == null)
                c = getJInternalFrame(component);
            if (c == null)
                c = getJFrame(component);

            c.setVisible(value); // can throw NullPointerException - means development bug

            if (value && c instanceof java.awt.Frame)
                ((java.awt.Frame) c).setState(java.awt.Frame.NORMAL);
        }

    }

    public static void pack(Component comp) {
        Component c = getJFrame(comp);
        if (c != null) {
            ((JFrame) c).pack();
            return;
        }

        c = getJInternalFrame(comp);
        if (c != null) {
            ((JInternalFrame) c).pack();
            return;
        }

        c = getJDialog(comp);
        if (c != null) {
            ((JDialog) c).pack(); // can throw NullPointerException - means
                                    // development bug
            return;
        }

        // JApplet
    }

    public static void setTitle(Component comp, String value) {
        Component c = getJFrame(comp);
        if (c != null) {
            ((JFrame) c).setTitle(value);
            return;
        }

        c = getJInternalFrame(comp);
        if (c != null) {
            ((JInternalFrame) c).setTitle(value);
            return;
        }

        c = getJDialog(comp);
        if (c != null) {
            ((JDialog) c).setTitle(value); // can throw NullPointerException -
                                            // means development bug
            return;
        }

        // JApplet
    }

    public static ActionMap getActionMap(Component comp) {
        Component c = getJFrame(comp);
        if (c != null)
            return ((JFrame) c).getRootPane().getActionMap();

        c = getJInternalFrame(comp);
        if (c != null)
            return ((JInternalFrame) c).getRootPane().getActionMap();

        c = getJDialog(comp);
        if (c != null)
            return ((JDialog) c).getRootPane().getActionMap();

        // JApplet
        return null;
    }

    public static void setActionMap(Component comp, ActionMap value) {
        Component c = getJFrame(comp);
        if (c != null) {
            ((JFrame) c).getRootPane().setActionMap(value);
            return;
        }

        c = getJInternalFrame(comp);
        if (c != null) {
            ((JInternalFrame) c).getRootPane().setActionMap(value);
            return;
        }

        c = getJDialog(comp);
        if (c != null) {
            ((JDialog) c).getRootPane().setActionMap(value);
            return;
        }

        // JApplet

    }

    /***************************************************************************
     * 
     */
    public static JFrame getJFrame(Component comp) {

        // return (JFrame) windows.get( comp );

        if (comp == null)
            return null;

        if (comp instanceof JFrame)
            return (JFrame) comp;

        if (comp instanceof JComponent) {
            Container con = ((JComponent) comp).getTopLevelAncestor();
            if (con instanceof JFrame)
                return (JFrame) con;
            return null;
        }

        return null;

    }

    // ContentPane = desktop
    public static JInternalFrame getJInternalFrame(Component comp) {

        // return (JInternalFrame) windows.get( comp );

        if (comp == null)
            return null;

        if (comp instanceof JInternalFrame)
            return (JInternalFrame) comp;

        comp = comp.getParent();
        return getJInternalFrame(comp);

    }

    public static JDialog getJDialog(Component comp) {

        // return (JDialog) windows.get( comp );

        if (comp == null)
            return null;

        if (comp instanceof JDialog)
            return (JDialog) comp;

        if (comp instanceof JComponent) {
            Container con = ((JComponent) comp).getTopLevelAncestor();
            if (con instanceof JDialog)
                return (JDialog) con;
            return null;
        }

        return null;

    }

    /***************************************************************************
     * 
     */
    // maintain list of all windows created via add2Frame(), add2Dialog()
    // methods
    private static java.util.Map windows = new java.util.HashMap();

    public static int closeAll() {

        java.util.Iterator iter = windows.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            Component key = (Component) entry.getKey();
            Component value = (Component) entry.getValue();

            if (value instanceof Window) {
                Window w = (Window) value;

                // to get all of the WindowListener(s) for the given Window w
                WindowListener[] wls = (WindowListener[]) w
                        .getListeners(WindowListener.class);
                if (wls != null && wls.length > 0) {
                    WindowEvent evt = new WindowEvent(w,
                            WindowEvent.WINDOW_CLOSING);
                    for (int i = 0; i < wls.length; i++)
                        wls[i].windowClosing(evt);
                }

            }

            // System.out.println( key.getClass().getName() );
            if (key instanceof IReset) {
                // System.out.println( "IReset: " + key.getClass().getName() );
                ((IReset) key).reset();
            }

            value.setVisible(false); // Frame or Dialog

        }

        return windows.size();

    }

    public static void setLookAndFeel(String className) {

        try {
            Class.forName(className);
            UIManager.setLookAndFeel(className);

            java.util.Iterator iter = windows.values().iterator();
            while (iter.hasNext())
                SwingUtilities.updateComponentTreeUI((Component) iter.next());

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    public static java.awt.Container add2Frame(Component comp,
            final FocusListener[] listeners, String title, String iconImage,
            boolean center, boolean resizable, boolean visible) {

        if (isDisplayOnDesktop())
            return add2JInternalFrame(comp, listeners, getDesktop(), title,
                    iconImage, center, resizable, visible);
        else
            return add2JFrame(comp, listeners, title, iconImage, center,
                    resizable, visible);

    }

    public static java.awt.Container add2Frame(Component comp,
            final FocusListener l, String title, String iconImage,
            boolean center, boolean resizable, boolean visible) {
        return add2Frame(comp, new FocusListener[] { l }, title, iconImage,
                center, resizable, true);
    }

    public static java.awt.Container add2Frame(Component comp,
            final java.awt.event.FocusListener l, String title,
            String iconImage, boolean center, boolean resizable) {
        return add2Frame(comp, new FocusListener[] { l }, title, iconImage,
                center, resizable, true);
    }

    /**
     * 
     */
    private static void setSizes(Component owner, Component comp) {

        if (comp instanceof JComponent) {
            int h = (int) comp.getPreferredSize().getHeight();
            int w = (int) comp.getPreferredSize().getWidth();
            if (h > SwingUtils.SCREEN_HEIGHT)
                h = SwingUtils.SCREEN_HEIGHT;
            if (w > SwingUtils.SCREEN_WIDTH)
                w = SwingUtils.SCREEN_WIDTH;

            ((JComponent) comp).setPreferredSize(new java.awt.Dimension(w, h));
        }

        if (comp instanceof JComponent) {
            int h = (int) comp.getMinimumSize().getHeight();
            int w = (int) comp.getMinimumSize().getWidth();
            if (h > SwingUtils.SCREEN_HEIGHT)
                h = SwingUtils.SCREEN_HEIGHT;
            if (w > SwingUtils.SCREEN_WIDTH)
                w = SwingUtils.SCREEN_WIDTH;

            ((JComponent) comp).setMinimumSize(new java.awt.Dimension(w, h));
        }

        if (owner instanceof JComponent) {
            ((JComponent) owner).setMaximumSize(comp.getMaximumSize());
            ((JComponent) owner).setMinimumSize(comp.getMinimumSize());
            ((JComponent) owner).setPreferredSize(comp.getPreferredSize());
        }

    }

    public static JInternalFrame add2JInternalFrame(Component comp,
            java.awt.event.FocusListener[] listeners, JDesktopPane desktop,
            String title, String iconImage, boolean center, boolean resizable,
            boolean visible) {

        JInternalFrame frame = getJInternalFrame(comp);

        if (frame == null) {
            SwingUtils.setDefaultFont(comp);
            setDefaultColor(comp);
            setDefaultKeySelectionManager(comp);

            setSizes(frame, comp);

            // JInternalFrame(String title, boolean resizable,
            // boolean closable, boolean maximizable, boolean iconifiable)
            final JInternalFrame frameNew = new JInternalFrame(title,
                    resizable, true, resizable, true);
            frameNew.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frameNew.getContentPane().add(comp);
            // setDefaultFont(frame); // for title

            if (listeners != null)
                for (int i = 0; i < listeners.length; i++)
                    frameNew.addFocusListener(listeners[i]);

            // InternalFrameListener, InternalFrameAdapter
            frameNew.addInternalFrameListener(new InternalFrameAdapter() {
                public void internalFrameOpened(InternalFrameEvent e) {
                    setMenuBar();
                    e.getInternalFrame().setCursor(
                            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                public void internalFrameActivated(InternalFrameEvent e) {
                    setMenuBar();
                }

                public void internalFrameDeiconified(InternalFrameEvent e) {
                    setMenuBar();
                }

                public void internalFrameClosing(InternalFrameEvent e) {
                    resetMenuBar();
                }

                public void internalFrameClosed(InternalFrameEvent e) {
                    resetMenuBar();
                }

                public void internalFrameDeactivated(InternalFrameEvent e) {
                    resetMenuBar();
                }

                public void internalFrameIconified(InternalFrameEvent e) {
                    resetMenuBar();
                }

                private void setMenuBar() {
                    JMenuBar menuBar = frameNew.getJMenuBar();
                    if (menuBar == null)
                        return;

                    JFrame f = (JFrame) getJFrame(frameNew);
                    if (f != null)
                        f.setJMenuBar(menuBar); // overriden, restore default
                                                // one
                    f.validate();
                }

                private void resetMenuBar() {
                    JMenuBar menuBar = frameNew.getJMenuBar();
                    if (menuBar == null)
                        return;

                    JFrame f = (JFrame) getJFrame(frameNew);
                    if (f != null)
                        f.setJMenuBar(null); // overriden, restore default
                                                // one
                }
            });

            // desktop = MainFrame ContentPane
            desktop.add(frameNew);

            frameNew.setFrameIcon(getIconImage(iconImage));

            frameNew.setResizable(resizable);
            frameNew.pack();

            // position in center
            if (center)
                frameNew.setLocation((SwingUtils.SCREEN_WIDTH - frameNew.getWidth()) / 2,
                        (SwingUtils.SCREEN_HEIGHT - frameNew.getHeight()) / 2);

            frame = frameNew;
            windows.put(comp, frame);

            if (comp instanceof IDefaultButton)
                frame.getRootPane().setDefaultButton(
                        ((IDefaultButton) comp).getDefaultButton());

        }

        try {
            if (frame.isIcon())
                ;// frame.setMaximum(true); // normal
            if (!frame.isSelected())
                frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }

        frame.setTitle(title);

        // if ( visible )
        // frame.setState( java.awt.Frame.NORMAL );

        if (!frame.isVisible())
            frame.setVisible(visible);

        return frame;

    }

    /**
     * 
     */
    public static JFrame add2JFrame(Component comp,
            java.awt.event.FocusListener[] listeners, String title,
            String iconImage, boolean center, boolean resizable, boolean visible) {

        JFrame frame = getJFrame(comp);

        if (frame == null) {
            SwingUtils.setDefaultFont(comp);
            setDefaultColor(comp);
            setDefaultKeySelectionManager(comp);

            setSizes(frame, comp);

            frame = new JFrame(title);
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.getContentPane().add(comp);

            if (listeners != null) {
                // final Container container = ( comp instanceof Container ) ?
                // (Container) comp : null;

                // translate FocusListener to WindowFocusListener
                for (int i = 0; i < listeners.length; i++) {
                    final java.awt.event.FocusListener l = listeners[i];

                    if (l != null) {
                        // frame.addFocusListener( l ); // does not work with
                        // frrames, we have to use addWindowListener()
                        frame.addWindowListener( // java.awt.Window
                                new java.awt.event.WindowAdapter() {
                                    public void windowActivated(
                                            WindowEvent windowEvent) {
                                        l.focusGained(new java.awt.event.FocusEvent(
                                                        windowEvent.getWindow(),
                                                        windowEvent.getID()));
                                    }

                                    public void windowDeactivated(
                                            WindowEvent windowEvent) {
                                        l.focusLost(new java.awt.event.FocusEvent(
                                                        windowEvent.getWindow(),
                                                        windowEvent.getID()));
                                    }

                                    public void windowOpened(
                                            WindowEvent windowEvent) {
                                        windowEvent
                                                .getWindow()
                                                .setCursor(
                                                        Cursor
                                                                .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                    }
                                });
                    }
                }
            }

            if (iconImage == null) {
                iconImage = imageName;// getIconImage(comp);
            }
            setIconImage(frame, iconImage);
            
            frame.setResizable(resizable);
            frame.pack();

            // position in center
            if (center)
                frame.setLocation((SwingUtils.SCREEN_WIDTH - frame.getWidth()) / 2,
                        (SwingUtils.SCREEN_HEIGHT - frame.getHeight()) / 2);

            // frame.invalidate();
            windows.put(comp, frame);

            if (comp instanceof IDefaultButton)
                frame.getRootPane().setDefaultButton(
                        ((IDefaultButton) comp).getDefaultButton());

        }

        frame.setTitle(title);

        if (visible)
            frame.setState(java.awt.Frame.NORMAL);
        frame.setVisible(visible);

        return frame;

    }

    /**
     * java.awt.event.FocusListener [] listeners
     */
    public static JDialog add2Dialog(Frame owner, String title, boolean modal,
            Component comp, boolean resizable, boolean visible, boolean center) {

        JDialog dialog = getJDialog(comp);

        if (owner == null)
            owner = getSharedOwnerFrame();

        // new dialog or new owner for existing dialog
        if (dialog == null || dialog.getOwner() != owner) {

            if (dialog == null) {
                SwingUtils.setDefaultFont(comp);
                setDefaultColor(comp);
                setDefaultKeySelectionManager(comp);

            } else {
                windows.remove(comp);
                dialog.dispose();

            }

            // setSizes( dialog, comp );

            dialog = new JDialog(owner, title, modal);

            setSizes(dialog, comp);

            // do not do anything - require the program to handle the operation
            // in the windowClosing method of a registered WindowListener
            // object.
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(WindowEvent windowEvent) {
                    windowEvent.getWindow().setVisible(false);
                }

                public void windowOpened(WindowEvent windowEvent) {
                    windowEvent.getWindow().setCursor(
                            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });

            dialog.getContentPane().add(comp);

            dialog.pack();

            // position in center
            if (center)
                dialog.setLocation((SwingUtils.SCREEN_WIDTH - dialog.getWidth()) / 2,
                        (SwingUtils.SCREEN_HEIGHT - dialog.getHeight()) / 2);

            dialog.setResizable(resizable);
            windows.put(comp, dialog);

            if (comp instanceof IDefaultButton)
                dialog.getRootPane().setDefaultButton(
                        ((IDefaultButton) comp).getDefaultButton());

        } else {
            dialog.setTitle(title);
            dialog.setModal(modal);

        }

        // if ( visible )
        // dialog.setState( java.awt.Frame.NORMAL );

        dialog.setVisible(visible);

        return dialog;

    }

    /**
     * Returns a toolkit-private, shared, invisible Frame to be the owner for
     * JDialogs and JWindows created with null owners.
     */
    private static Frame sharedOwnerFrame;

    public static Frame getSharedOwnerFrame() {

        if (sharedOwnerFrame == null) {
            sharedOwnerFrame = new Frame() {
                public void show() {
                    // This frame can never be shown
                }

                public synchronized void dispose() {
                    try {
                        getToolkit().getSystemEventQueue();
                        super.dispose();
                    } catch (Exception e) {
                        // untrusted code not allowed to dispose
                    }
                }
            };
            setIconImage(sharedOwnerFrame, null);
        }

        return sharedOwnerFrame;

    }

    public static JDialog add2Dialog(Frame owner, String title, boolean modal,
            Component comp, boolean resizable, boolean visible) {
        return add2Dialog(owner, title, modal, comp, resizable, visible, true);
    }

    public static JDialog add2Dialog(Frame owner, String title, boolean modal,
            Component comp, boolean visible) {
        return add2Dialog(owner, title, modal, comp, false, visible);
    }

    public static JDialog add2Dialog(Frame owner, String title, boolean modal,
            Component comp) {
        return add2Dialog(owner, title, modal, comp, true);
    }

    /**
     * 
     */
    public static void clear(Component component) {

        if (!(component instanceof Container))
            return;

        Container container = (Container) component;

        if (container.getComponentCount() == 0) {

            if (component instanceof JLabel)
                return;
            if (component.hasFocus())
                return;

            if (component instanceof JTextComponent) {
                if (component instanceof JTextField)
                    ((JTextField) component).setText(null);
                else if (component instanceof JTextArea)
                    ((JTextArea) component).setText(null);
                else if (component instanceof JEditorPane) {
                    if (component instanceof JTextPane)
                        ((JTextPane) component).setText(""); // ???
                    else
                        ((JEditorPane) component).setText(null);
                }
            } else if (component instanceof AbstractButton) {
                ((AbstractButton) component).setSelected(false);
            }

        } else if (component instanceof JComboBox) { // consists of many
                                                        // other UI, Render, ...
            ((JComboBox) component).setSelectedItem(null);
        } else {
            for (int i = 0; i < container.getComponentCount(); i++)
                clear(container.getComponent(i));
        }

    }

    public static void setEnabled(Component component, boolean value) {

        if (!(component instanceof Container))
            return;

        Container container = (Container) component;

        if (container.getComponentCount() == 0) {
            if (component instanceof JLabel)
                return;

            if (component instanceof JTextComponent) {
                ((JTextComponent) component).setEditable(value);
            } else
                component.setEnabled(value);

            setDefaultColor(component);
        } else if (component instanceof JComboBox) {
            component.setEnabled(value);
        } else {
            for (int i = 0; i < container.getComponentCount(); i++)
                setEnabled(container.getComponent(i), value);
        }

    }

    public static void setDefaultColor(Component component) {

        if (component instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) component;
            if (tc.isEditable())
                tc.setBackground(EDITABLE);
            else
                // if ( !EDITABLE.equals( tc.getBackground() ) )
                tc.setBackground(NON_EDITABLE);

        } else if (component instanceof JComboBox) {
            // do nothing

        } else if (component instanceof Container) {
            Container container = (Container) component;

            for (int i = 0; i < container.getComponentCount(); i++)
                setDefaultColor(container.getComponent(i));

        }

    }

    public static void setDefaultKeySelectionManager(Component component) {

        if (component instanceof JComboBox) {
            ((JComboBox) component)
                    .setKeySelectionManager(ComboBoxKeySelectionManager
                            .getInstance());

        } else if (component instanceof Container) {
            Container container = (Container) component;

            for (int i = 0; i < container.getComponentCount(); i++)
                setDefaultKeySelectionManager(container.getComponent(i));

        }

    }

    // /**
    // * Font change
    // */
    // private static Font defaultFont = new Font( "Arial", Font.PLAIN, 11 );
    //    
    // public static Font getDefaultFont() {
    // return defaultFont;
    // }
    // public static void setDefaultFont( Font value ) {
    // defaultFont = value;
    // }
    //    
    //    
    // public static void setDefaultFont( Component comp ) {
    //        
    // comp.setFont( defaultFont );
    //        
    // if ( comp instanceof JComponent ) {
    //            
    // //System.out.println(comp);
    //            
    // if ( comp instanceof JMenu ) {
    // MenuElement[] me = ( (JMenu) comp ).getSubElements();
    // if ( me != null )
    // for ( int j = 0; j < me.length; j++ )
    // setDefaultFont( (Component) me[j] );
    //                
    // } else if ( comp instanceof JTable ) {
    // ( (JTable) comp ).getTableHeader().setFont( defaultFont );
    //                
    // } else if ( comp instanceof JTextField ) {
    // /*
    // final JTextField jTextField = ( JTextField ) comp;
    //
    // if ( jTextField.getInputVerifier() instanceof DateInputVerifier )
    // jTextField.addFocusListener(new java.awt.event.FocusAdapter() {
    // public void focusGained(java.awt.event.FocusEvent evt) {
    // if ( jTextField.getText().trim().length() == 0 )
    // jTextField.setText( com.argus.format.DateTime.DEFAULT_INPUT );
    // }
    // });
    // */
    // }
    //            
    // // set panel border title font
    // Border border = ( (JComponent) comp ).getBorder();
    // if ( border instanceof TitledBorder )
    // ( (TitledBorder) border ).setTitleFont( defaultFont );
    //            
    // }
    //        
    // if ( comp instanceof Container ) {
    // Container container = (Container) comp;
    //            
    // for ( int i = 0; i < container.getComponentCount(); i++ )
    // setDefaultFont( container.getComponent(i) );
    //            
    // }
    //        
    // }
    //    
    //    
    // /**
    // * This methode set a default font for the application
    // *
    // * @param oFont - the new default font
    // */
    // public static void setUIDefaultFont()
    // {
    // java.util.Hashtable oUIDefault = UIManager.getDefaults();
    // java.util.Enumeration oKey = oUIDefault.keys();
    // String oStringKey = null;
    //
    // while (oKey.hasMoreElements()){
    // oStringKey = oKey.nextElement().toString();
    // if (oStringKey.endsWith("font") ||
    // oStringKey.endsWith("acceleratorFont")){
    // UIManager.put(oStringKey, defaultFont);
    // }
    // }
    // }
    //    
}
