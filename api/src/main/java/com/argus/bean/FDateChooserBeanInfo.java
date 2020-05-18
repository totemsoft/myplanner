package com.argus.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 1.0
 */

public class FDateChooserBeanInfo extends SimpleBeanInfo {
    Class beanClass = FDateChooser.class;

    /** 16x16 color icon. */
    java.awt.Image icon;

    /** 32x32 color icon. */
    java.awt.Image icon32;

    /** 16x16 mono icon. */
    java.awt.Image iconM;

    /** 32x32 mono icon. */
    java.awt.Image icon32M;

    public FDateChooserBeanInfo() {
        super();
        icon = loadImage("com/toedter/calendar/images/JCalendarColor16.gif");
        iconM = loadImage("com/toedter/calendar/images/JCalendarMono16.gif");
        icon32 = loadImage("com/toedter/calendar/images/JCalendarColor32.gif");
        icon32M = loadImage("com/toedter/calendar/images/JCalendarMono32.gif");

    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        /*
         * try { if (PropertyEditorManager.findEditor(FDateChooser.class) ==
         * null) { BeanInfo beanInfo =
         * Introspector.getBeanInfo(javax.swing.JTextField.class);
         * PropertyDescriptor[] p = beanInfo.getPropertyDescriptors();
         * 
         * int length = p.length; PropertyDescriptor[] propertyDescriptors = new
         * PropertyDescriptor[length + 1]; for (int i = 0; i < length; i++)
         * propertyDescriptors[i + 1] = p[i];
         * 
         * propertyDescriptors [0] = new PropertyDescriptor("FDateChooser",
         * FDateChooser.class); propertyDescriptors [0].setBound(true);
         * propertyDescriptors [0].setConstrained(false); propertyDescriptors
         * [0].setPropertyEditorClass( javax.swing.JTextField.class); return
         * propertyDescriptors; } } catch (IntrospectionException e) {
         * e.printStackTrace(); } return null;
         * 
         * try { BeanInfo beanInfo =
         * java.beans.Introspector.getBeanInfo(javax.swing.JTextField.class);
         * 
         * PropertyDescriptor[] _JTextField1 =
         * beanInfo.getPropertyDescriptors();
         * 
         * BeanInfo beanInfo0 =
         * java.beans.Introspector.getBeanInfo(javax.swing.JButton.class);
         * 
         * PropertyDescriptor[] _JButton1 = beanInfo0.getPropertyDescriptors();
         * 
         * PropertyDescriptor[] pds = new
         * PropertyDescriptor[_JTextField1.length+_JButton1.length] ;
         * 
         * for (int i = 0 ; i < _JTextField1.length;i++){ pds[i] =
         * _JTextField1[i] ; }
         * 
         * for (int i = _JTextField1.length ; i < _JButton1.length;i++){ pds[i] =
         * _JButton1[i] ; }
         * 
         * return pds; } catch (IntrospectionException e) { e.printStackTrace(); }
         * return null;
         */

        try {
            PropertyDescriptor _JButton1 = new PropertyDescriptor(
                    "selectButton", beanClass, "getSelectButton",
                    "setSelectButton");
            PropertyDescriptor _JTextField1 = new PropertyDescriptor(
                    "dateField", beanClass, "getDateField", "setDateField");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _JButton1,
                    _JTextField1 };
            return pds;

        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = beanClass.getSuperclass();
        try {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns an image object that can be used to represent the
     * bean in toolboxes, toolbars, etc.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
        case ICON_COLOR_16x16:
            return icon;
        case ICON_COLOR_32x32:
            return icon32;
        case ICON_MONO_16x16:
            return iconM;
        case ICON_MONO_32x32:
            return icon32M;
        }
        return null;
    }

}