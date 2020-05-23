/*
 * AggregationFunction.java
 *
 * Created on 28 March 2003, 12:39
 */

package au.com.totemsoft.math;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public abstract class AggregationFunction extends Function {
    
    public static final int NONE    = 0;
    public static final int SUM     = 1;
    public static final int AVG     = 2;
    public static final int STDDEV  = 3;
        private static final int LAST  = 3;
    
    
    private int type = NONE;
    protected java.lang.Number [] data;
    
    
    /** Creates a new instance of AggregationFunction */
    protected AggregationFunction() {
    }
    protected AggregationFunction( int type ) {
        setType( type );
    }
    protected AggregationFunction( java.lang.Number [] data ) {
        setData( data );
    }
    
    
    public abstract double calculate();
    

    public int getType() {
        return type;
    }
    public void setType( int type ) {
        if ( type < NONE || type > LAST )
            throw new java.lang.IllegalArgumentException( "Illegal Function Type: " + type );//+ setTypeUsage() );
        this.type = type;
    }
    public String getTypeDesc() {
        switch (type) {
            case NONE   : return "";
            case SUM    : return "Total (Sum)";
            case AVG    : return "Average";
            case STDDEV : return "Std. Deviation";
            default     : return "";
        }
        
    }
    
    public java.lang.Number [] getData() { return data; }
    public void setData( java.lang.Number [] data ) { this.data = data; }
    
}
