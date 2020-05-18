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
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.1 $
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TypeSafeCollection implements Collection
{
    /** Logger. */
    //private static final Logger LOG = Logger.getLogger(TypeSafeCollection.class);

    /** ItemClass. */
    private final Class mItemClass;

    /** Container. */
    private final Collection mContainer;

    /**
     * CTOR.
     * @param itemClass itemClass
     */
    public TypeSafeCollection(Class itemClass)
    {
        this(itemClass, new ArrayList());
    }

    /**
     * CTOR.
     * @param itemClass itemClass.
     * @param container container.
     */
    protected TypeSafeCollection(Class itemClass, Collection container)
    {
        mItemClass = itemClass;
        mContainer = container;
    }

    /**
     * ItemClass.
     * @return ItemClass.
     */
    public Class getItemClass()
    {
        return mItemClass;
    }

    /** {@inheritDoc} */
    public int size()
    {
        return mContainer.size();
    }

    /** {@inheritDoc} */
    public boolean isEmpty()
    {
        return mContainer.isEmpty();
    }

    /** {@inheritDoc} */
    public boolean contains(Object o)
    {
        return mContainer.contains(o);
    }

    /** {@inheritDoc} */
    public Iterator iterator()
    {
        return mContainer.iterator();
    }

    /** {@inheritDoc} */
    public Object[] toArray()
    {
        return mContainer.toArray();
    }

    /** {@inheritDoc} */
    public Object[] toArray(Object [] a)
    {
        return mContainer.toArray(a);
    }

    /** {@inheritDoc} */
    public boolean add(Object o)
    {
        checkType(o);
        return mContainer.add(o);
    }

    /** {@inheritDoc} */
    public boolean remove(Object o)
    {
        return mContainer.remove(o);
    }

    /** {@inheritDoc} */
    public boolean containsAll(Collection c)
    {
        Iterator iter = c.iterator();
        while (iter.hasNext())
        {
            checkType(iter.next());
        }
        return mContainer.containsAll(c);
    }

    /** {@inheritDoc} */
    public boolean addAll(Collection c)
    {
        Iterator iter = c.iterator();
        while (iter.hasNext())
        {
            checkType(iter.next());
        }
        return mContainer.addAll(c);
    }

    /** {@inheritDoc} */
    public boolean removeAll(Collection c)
    {
        return mContainer.removeAll(c);
    }

    /** {@inheritDoc} */
    public boolean retainAll(Collection c)
    {
        Iterator iter = c.iterator();
        while (iter.hasNext())
        {
            checkType(iter.next());
        }
        return mContainer.retainAll(c);
    }

    /** {@inheritDoc} */
    public void clear()
    {
        mContainer.clear();
    }

    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        return mContainer.equals(o);
    }

    /** {@inheritDoc} */
    public int hashCode()
    {
        return mContainer.hashCode();
    }

    /**
     * Check Object Type.
     * @param o Object.
     * @throws SystemException SystemException.
     */
    private void checkType(Object o)
        throws RuntimeException
    {
        if (!mItemClass.isAssignableFrom(o.getClass()))
        {
            throw new RuntimeException(
                o.getClass().getName() + " is not an instance of " + mItemClass.getName());
        }
    }

}
