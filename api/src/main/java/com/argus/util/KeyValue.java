/*
 * KeyValue.java
 *
 * Created on 14 March 2003, 17:16
 */

package com.argus.util;

/**
 *
 * @author
 * @version 
 */
public class KeyValue {
    Object key   = null;
    Object value = null;
    Object [] supplement = null;
    /** Creates new KeyValue */
    public KeyValue(Object key, Object value) {
        this.key = key;
        this.value=value;
    }
    
    public KeyValue(Object key, Object value,  Object [] supplement) {
        this.key = key;
        this.value=value;
        this.supplement = supplement;
    }
    
    public Object getValue() {return this.value ;}
    public Object getKey() {return this.key;}    
    public Object[] getSupplement() {return this.supplement;}   
    public String toString() {
        return key.toString() ;
    }
}
