package com.argus.util;

/**
 *
 * @author  valeri chibaev
 * @version 
 *
 *  instances of this class has to be used as Reference Code storage
 *  for ALL com.argus.code.BaseCode derived classes
 *
 */

public class ReferenceCode implements java.io.Serializable, java.lang.Cloneable {

//serialver -classpath . com.argus.code.ReferenceCode

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or 
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 6022250260979947810L;

    
    public final static ReferenceCode CODE_NONE = new ReferenceCode( null, "", "" );
    
    
    private boolean modified;
    
    // PrimaryKey >= 0
    private int codeID;
    // ObjectTypeID >= 0
    private int codeObjectID;
    // e.g. M/F
    private String code;
    // e.g. Male/Female
    private String codeDesc; // NOT NULL
    // object instantiated using this data
    private Object object;
    
    // auxilary member > 0 or null
    private Integer codeIDInteger;
    
    
    /** Creates new ReferenceCode */
    public ReferenceCode( int codeID, int codeObjectID, String code, String codeDesc ) {
        this.codeID = codeID;
        this.codeObjectID = codeObjectID;
        this.code = code;
        this.codeDesc = codeDesc;
    }
    public ReferenceCode( int codeID, String code, String codeDesc ) {
        this( codeID, 0, code, codeDesc );
    }
    public ReferenceCode() {
        this( 0, 0, null, null );
    }
    public ReferenceCode( int codeID ) {
        this( codeID, 0, null, null );
    }
    public ReferenceCode( int codeID, String codeDesc ) {
        this( codeID, 0, null, codeDesc );
    }
    public ReferenceCode( Integer codeID ) {
        this( codeID, null, null, null );
    }
    public ReferenceCode( Integer codeID, String codeDesc ) {
        this( codeID, null, null, codeDesc );
    }
    public ReferenceCode( Integer codeID, String code, String codeDesc ) {
        this( codeID == null || codeID.intValue() <= 0 ? 0 : codeID.intValue(), 0, code, codeDesc );
    }
    public ReferenceCode( Integer codeID, Integer codeObjectID, String code, String codeDesc ) {
        this( 
            codeID == null || codeID.intValue() <= 0 ? 0 : codeID.intValue(), 
            codeObjectID == null || codeObjectID.intValue() <= 0 ? 0 : codeObjectID.intValue(), 
            code, codeDesc 
        );
    }
    public ReferenceCode( ReferenceCode rc ) {
        this( 0, rc.code, rc.codeDesc );
        
        if ( rc.object == null ) 
            return;
        
        if ( rc.object instanceof String ) {
            this.object = new String( rc.object.toString() );
            
        } else {
            try {
                Class objClass = rc.object.getClass();
                java.lang.reflect.Method m = objClass.getMethod( "clone" , null );
                this.object = m.invoke( rc.object, new Object [0] );
                //this.object = rc.object.clone(); // protected
            } catch ( Exception e ) {
                e.printStackTrace( System.err );
                //this.object = rc.object;
            }
            
        }
        
    }
    
    
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }
    
    
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        
        if ( obj == null || !( obj instanceof ReferenceCode ) )
            return false;
        
        ReferenceCode refCode = ( ReferenceCode ) obj;
        return
            ( codeID > 0 && refCode.codeID > 0 && codeID == refCode.codeID )
            || ( code != null && code.trim().length() > 0 && equals( code, refCode.code ) )
            || ( codeDesc != null && codeDesc.trim().length() > 0  && equals( codeDesc, refCode.codeDesc ) )
            ;
    }
    
    
    public String toString() {
        return 
            codeDesc
            //+ ( code == null || code.trim().length() == 0 ? "" : " (" + code + ")" )
            ;
    }
    
    
    public boolean isModified() { 
        return modified; 
    }
    public void setModified( boolean value ) {
        if ( modified == value ) return;
        modified = value;
    }
    public void setModified() {
        setModified( true );
    }
    
    
    protected boolean equals( Object value1, Object value2 ) {
        return 
            ( value1 == null && value2 == null ) ||
            ( value1 != null && value1.equals( value2 ) ) ||
            ( value2 != null && value2.equals( value1 ) );
    }            

    
    /*
     *  get/set methods
     */
    public int getCodeID() { return codeID; }
    public void setCodeID( int value ) { codeID = codeID <= 0 ? value : codeID; }
    
    public int getCodeObjectID() { return codeObjectID; }
    public void setCodeObjectID( int value ) { codeObjectID = codeObjectID <= 0 ? value : codeObjectID; }
    
    public String getCode() { return code; }
    public void setCode( String value ) { code = value; }
    
    public String getCodeDesc() { return codeDesc; }
    public void setCodeDesc( String value ) { codeDesc = value; }
    
    public Integer getCodeIDInteger() {
        if ( codeIDInteger == null && codeID > 0 )
            codeIDInteger = new Integer(codeID);
        return codeIDInteger; 
    }

    public Object getObject() {
        return object;
    }
    public void setObject( Object value ) {
        //if ( object != null ) return;
        object = value;
    }
    
}
