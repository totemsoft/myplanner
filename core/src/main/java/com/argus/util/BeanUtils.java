/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.util;

/**
 * <p>Title: UtilityService class for JavaBeans manipulation.</p>
 * <p>Description: UtilityService class for JavaBeans manipulation.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1 $
 */

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BeanUtils
{
    /**
     * Get list of public static fields from class.
     * @param clazz Class.
     * @param propertyClass The member class.
     * @return List.
     * @throws SecurityException The Exception.
     * @throws IllegalArgumentException The Exception.
     * @throws IllegalAccessException The Exception.
     */
    public static List getStaticProperties(
        Class clazz, 
        Class propertyClass)
        throws SecurityException, IllegalArgumentException, IllegalAccessException
    {
        List list = new ArrayList();
        Field [] fields = clazz.getFields();
        for (int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];
            Object obj = field.get(null);
            if (propertyClass.isAssignableFrom(obj.getClass()))
            {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * List Classes inside a given package.
     * @param classLoader classLoader.
     * @param packageName String name of a Package, e.g. "java.lang".
     * @param recurse recurse.
     * @param result classes inside the root of the given package.
     * @throws ClassNotFoundException if the Package is invalid.
     */
    public static void getClasses(
        ClassLoader classLoader,
        String packageName,
        final boolean recurse,
        List result)
        throws ClassNotFoundException
    {
        // Get a File object for the package
        File packageDir = new File(
            classLoader.getResource(packageName.replace('.', '/')).getFile());
        if (!packageDir.exists())
        {
            throw new ClassNotFoundException(
                packageName + " does not appear to be a valid package.");
        }

        // Get the list of the files contained in the package
        File [] files = packageDir.listFiles(
            new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    if (recurse && new File(dir, name).isDirectory())
                    {
                        return true;
                    }
                    // we are only interested in .class files
                    return name.endsWith(".class");
                }
            }
        );

        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            String fileName = file.getName();
            if (file.isDirectory())
            {
                getClasses(classLoader, packageName + '.' + fileName, recurse, result);
            }
            else
            {
                // removes the .class extension
                result.add(
                    Class.forName(
                        packageName + '.' + fileName.substring(0, fileName.indexOf(".class"))));
            }
        }
    }

}
