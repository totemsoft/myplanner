/*
 * SumFunction.java
 *
 * Created on 28 March 2003, 12:49
 */

package au.com.totemsoft.math;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public class SumFunction extends AggregationFunction {
    
    /** Creates a new instance of SumFunction */
    public SumFunction() {
        super( SUM );
    }
    public SumFunction( java.lang.Number [] data ) {
        super( data );
    }
    
    
    public double calculate() {
        
        double result = 0.;
        
        if ( data == null || data.length == 0 )
            return result;
        
        for ( int i = 0; i < data.length; i++ )
            if ( data[i] != null )
                result += data[i].doubleValue();
        
        return result;
        
    }
    
}
