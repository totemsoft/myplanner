/*
 * VectorTemplate.java
 *
 * Created on 7 January 2003, 16:18
 */

package com.argus.util;

/**
 *
 * @author  valeri chibaev
 *
 *  safe vector implementation, accepts ONLY Model
 */

public class VectorTemplate extends java.util.Vector {
    
    private java.lang.Class classTemplate;
    
    
    /** Creates a new instance of VectorTemplate */
    public VectorTemplate( java.lang.Class classTemplate ) {
        super();
        this.classTemplate = classTemplate;
    }
    public VectorTemplate( java.lang.Class classTemplate, int initialCapacity ) {
        super( initialCapacity );
        this.classTemplate = classTemplate;
    }
    public VectorTemplate( java.lang.Class classTemplate, int initialCapacity, int capacityIncrement ) {
        super( initialCapacity, capacityIncrement );
        this.classTemplate = classTemplate;
    }
    
              
    public java.lang.Class getClassTemplate() {
        return classTemplate;
    }
    
    protected boolean check( Object obj ) {
        return java.beans.Beans.isInstanceOf( obj, classTemplate );
    }
    
    
    public boolean add(Object obj) {
        if ( check( obj ) )
            return super.add(obj);
        throw new ClassCastException( "VectorTemplate::add(Object obj)\tobj has to be instance of " + classTemplate );
    }
    public void add(int index, Object obj) {
        if ( check( obj ) )
            super.add(index, obj);
        throw new ClassCastException( "VectorTemplate::add(int index, Object obj)\tobj has to be instance of " + classTemplate );
    }
    public void addElement(Object obj) {
        if ( check( obj ) )
            super.addElement(obj);
        throw new ClassCastException( "VectorTemplate::addElement(Object obj)\tobj has to be instance of " + classTemplate );
    }
    /*
    public boolean addAll(Collection c) {
        return super.addAll(c);
    }
    public boolean addAll(int index, Collection c) {
        return super.addAll(index, c);
    }
     */
    public Object set(int index, Object obj) {
        if ( check( obj ) )
            return super.set(index, obj);
        throw new ClassCastException( "VectorTemplate::set(int index, Object obj)\tobj has to be instance of " + classTemplate );
    }
    public void setElementAt(Object obj, int index) {
        if ( check( obj ) )
            super.setElementAt(obj, index);
        throw new ClassCastException( "VectorTemplate::setElementAt(Object obj, int index)\tobj has to be instance of " + classTemplate );
    }

}
