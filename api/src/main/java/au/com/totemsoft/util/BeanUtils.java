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

package au.com.totemsoft.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;

public class BeanUtils
{

    /** hide ctor. */
    private BeanUtils()
    {
    }

    /**
     * 
     * @param source
     * @param target
     * @param ignoreProperties
     * @throws DomainException
     */
    public static void copyProperties(Object source, Object target, String[] ignoreProperties)
        throws BeansException
    {
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> boolean equals(T1 o1, T2 o2)
    {
        return compareTo(o1, o2, true, true) == 0;
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> int compareTo(T1 o1, T2 o2)
    {
        return compareTo(o1, o2, true, true);
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> int compareTo(T1 o1, T2 o2, boolean asc)
    {
        return compareTo(o1, o2, asc, true);
    }

    @SuppressWarnings("unchecked")
    private static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> int compareTo(T1 o1, T2 o2, boolean asc, boolean ignoreCase)
    {
        if ((o1 == null || StringUtils.trimToNull(o1.toString()) == null) &&
            (o2 == null || StringUtils.trimToNull(o2.toString()) == null))
        {
            return 0;
        }
        int result;
        if (o1 == null)
        {
            result = 1;
        }
        else if (o2 == null)
        {
            result = -1;
        }
        else
        {
            if (o1 instanceof String && o2 instanceof String)
            {
                if (ignoreCase) {
                    result = ((String) o1).compareToIgnoreCase((String) o2);
                }
                else {
                    result = ((String) o1).compareTo((String) o2);
                }
            }
            else if (o1 instanceof Number && o2 instanceof String)
            {
                result = o1.toString().compareTo((String) o2);
            }
            else if (o1 instanceof String && o2 instanceof Number)
            {
                result = ((String) o1).compareTo(o2.toString());
            }
            else// if (o1 instanceof o2.getClass())
            {
                result = o1.compareTo((T1) o2);
            }
        }
        return asc ? result : -result;
    }

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