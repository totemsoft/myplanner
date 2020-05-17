package com.argus.financials.etc;

import com.argus.financials.api.bean.IFPSAssignableObject;

/**
 * 
 * @author valeri chibaev
 * @version
 */
public class FPSAssignableObject extends FPSObject implements IFPSAssignableObject {

    static final long serialVersionUID = -3228137505746936812L;

    protected FPSAssignableObject() {
        super();
    }

    protected FPSAssignableObject(Integer ownerId) {
        super(ownerId);
    }

    /**
     * Assignable methodes
     */
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        // if ( !( this.getClass().equals( value.getClass() ) ) )
        // throw new ClassCastException( "This is not a " +
        // this.getClass().getName() );

        // setId(value == null ? null : value.getId());
        setOwnerId(value == null ? null : value.getOwnerId());

        // has to be last statement in the most derived class assign method (to
        // set modified)
        // modified = value == null ? false : value.modified;
    }

}
