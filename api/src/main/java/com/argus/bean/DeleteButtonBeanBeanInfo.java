package com.argus.bean;

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
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 1.0
 */

public class DeleteButtonBeanBeanInfo extends SimpleBeanInfo {
    Class beanClass = DeleteButtonBean.class;

    String iconColor16x16Filename;

    String iconColor32x32Filename;

    String iconMono16x16Filename;

    String iconMono32x32Filename;

    public DeleteButtonBeanBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _message = new PropertyDescriptor("message",
                    beanClass, "getSample", "setSample");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _message };
            return pds;

        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
        case BeanInfo.ICON_COLOR_16x16:
            return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename)
                    : null;
        case BeanInfo.ICON_COLOR_32x32:
            return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename)
                    : null;
        case BeanInfo.ICON_MONO_16x16:
            return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename)
                    : null;
        case BeanInfo.ICON_MONO_32x32:
            return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename)
                    : null;
        }
        return null;
    }
}