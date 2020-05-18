/*
 * GetStaticClass.java
 *
 * Created on 1 August 2001, 12:51
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.lang.reflect.Method;
import java.util.ResourceBundle;

class GetClass4Static {
    public static Class callingClass() {
        try {
            Method m = ResourceBundle.class.getDeclaredMethod(
                    "getClassContext", new Class[0]);
            m.setAccessible(true);
            Class[] classStack = (Class[]) m.invoke(null, new Object[0]);
            // 1 is a magic number here
            return classStack[1];
        } catch (Exception e) {
            return null;
        }
    }
}
