/*
 * ModelType.java
 *
 * Created on 12 February 2002, 14:04
 */

package com.argus.financials.code;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.Collection;
import java.util.TreeSet;

public class ModelType extends BaseCode implements ModelTypeID {

    private static Collection codes;

    /** Creates new ModelType */
    public ModelType() {
        codes = new TreeSet(new CodeComparator());
        initCodes();
    }

    public Collection getCodes() {
        return codes;
    }

    private void initCodes() {

        codes.add(CODE_NONE);

        codes.add(rcCURRENT_POSITION_CALC);
        // codes.add( rcINSURANCE_NEEDS) );
        // codes.add( rcPREMIUM_CALC );
        codes.add(rcINVESTMENT_GEARING);
        // codes.add( rcPROJECTED_WEALTH );
        // codes.add( rcINVESTMENT_PROPERTIES );
        codes.add(rcLOAN_MORTGAGE_CALC);
        codes.add(rcALLOCATED_PENSION);
        codes.add(rcETP_ROLLOVER);
        // codes.add( rcSUPERANNUATION_RBL );
        // codes.add( rcRETIREMENT_CALC) );
        // codes.add( rcRETIREMENT_HOME );
        // codes.add( rcPAYG_CALC );
        // codes.add( rcCGT_CALC );
        codes.add(rcSOCIAL_SECURITY_CALC);
        codes.add(rcCENTRELINK_CALC);
    }

}
