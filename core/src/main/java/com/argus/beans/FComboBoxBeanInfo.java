package com.argus.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
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
 * @author unascribed
 * @version 1.0
 */

public class FComboBoxBeanInfo extends SimpleBeanInfo {
    Class beanClass = FComboBox.class;

    /** 16x16 color icon. */
    java.awt.Image icon;

    /** 32x32 color icon. */
    java.awt.Image icon32;

    /** 16x16 mono icon. */
    java.awt.Image iconM;

    /** 32x32 mono icon. */
    java.awt.Image icon32M;

    public FComboBoxBeanInfo() {
        try {
            BeanInfo beanInfo = java.beans.Introspector
                    .getBeanInfo(javax.swing.JComboBox.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            icon = beanInfo.getIcon(ICON_COLOR_16x16);
            icon32 = beanInfo.getIcon(ICON_COLOR_32x32);
            iconM = beanInfo.getIcon(ICON_MONO_16x16);
            icon32M = beanInfo.getIcon(ICON_MONO_32x32);
            ;

        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            BeanInfo beanInfo = java.beans.Introspector
                    .getBeanInfo(javax.swing.JComboBox.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            return pds;
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }

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