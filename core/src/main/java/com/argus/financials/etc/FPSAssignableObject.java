package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */
public class FPSAssignableObject extends FPSObject implements Assignable {

    static final long serialVersionUID = -3228137505746936812L;

    protected FPSAssignableObject() {
    }

    protected FPSAssignableObject(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    protected FPSAssignableObject(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable methodes
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        // if ( !( this.getClass().equals( value.getClass() ) ) )
        // throw new ClassCastException( "This is not a " +
        // this.getClass().getName() );

        // setPrimaryKeyID(value == null ? null : value.getPrimaryKeyID());
        setOwnerPrimaryKeyID(value == null ? null : value.getOwnerPrimaryKeyID());

        // has to be last statement in the most derived class assign method (to
        // set modified)
        // modified = value == null ? false : value.modified;
    }

}
