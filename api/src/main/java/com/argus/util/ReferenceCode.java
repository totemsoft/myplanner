package com.argus.util;

import com.argus.financials.api.bean.ICode;

/**
 *
 * @author  valeri chibaev
 * @version 
 *
 *  instances of this class has to be used as Reference Code storage
 *  for ALL com.argus.code.BaseCode derived classes
 *
 */

public class ReferenceCode implements ICode, java.io.Serializable, java.lang.Cloneable {

//serialver -classpath . com.argus.code.ReferenceCode

    // Compatible changes include adding or removing a method or a field.
    // Incompatible changes include changing an object's hierarchy or 
    // removing the implementation of the Serializable interface.
    static final long serialVersionUID = 6022250260979947810L;

    public final static ReferenceCode CODE_NONE = new ReferenceCode( null, "", "" );

    private boolean modified;

    // PrimaryKey >= 0
    private Integer id;
    // ObjectTypeID >= 0
    private int objectId;
    // e.g. M/F
    private String code;
    // e.g. Male/Female
    private String description; // NOT NULL
    // object instantiated using this data
    private Object object;

    // auxilary member > 0 or null
    private Integer codeId;

    /** Creates new ReferenceCode */
    public ReferenceCode( int codeID, int codeObjectID, String code, String codeDesc ) {
        this.id = codeID;
        this.objectId = codeObjectID;
        this.code = code;
        this.description = codeDesc;
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
        this( 0, rc.code, rc.description );
        
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
            ( id > 0 && refCode.id > 0 && id == refCode.id )
            || ( code != null && code.trim().length() > 0 && equals( code, refCode.code ) )
            || ( description != null && description.trim().length() > 0  && equals( description, refCode.description ) )
            ;
    }
    
    
    public String toString() {
        return 
            description
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
    public Integer getId() { return id; }
    public void setId( Integer value ) { id = id <= 0 ? value : id; }
    
    public int getObjectId() { return objectId; }
    public void setObjectId( int value ) { objectId = objectId <= 0 ? value : objectId; }
    
    public String getCode() { return code; }
    public void setCode( String value ) { code = value; }
    
    public String getDescription() { return description; }
    public void setDescription( String value ) { description = value; }
    
    public Integer getCodeId() {
        if ( codeId == null && id > 0 )
            codeId = new Integer(id);
        return codeId; 
    }

    public Object getObject() {
        return object;
    }
    public void setObject( Object value ) {
        //if ( object != null ) return;
        object = value;
    }
    
}
