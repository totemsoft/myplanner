/*
 * PersonNameAddress.java
 *
 * Created on 24 October 2001, 18:23
 */

package com.argus.financials.etc;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class PersonNameAddress extends FPSAssignableObject {

    private PersonName ownerName;

    protected PersonName name = new PersonName();

    protected Address address = new Address();

    /** Creates new PersonNameAddress */
    public PersonNameAddress() {
    }

    public PersonNameAddress(int ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public PersonNameAddress(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * override Object methodes
     */
    public String toString() {
        return name.toString();
    }

    /**
     * Assignable methods
     */
    public void assign(FPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof PersonNameAddress))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        PersonNameAddress pna = (PersonNameAddress) value;

        setName(pna.getName());
        setAddress(pna.getAddress());

        // has to be last (to set modified)
        setModified(value.isModified());
    }

    /**
     * helper methods
     */
    protected void clear() {

        super.clear();

        name.clear();
        address.clear();

    }

    public Object[] getData() {

        return new Object[] { name.getFullName(), address.toString() };

    }

    /**
     * get/set methods
     */
    public boolean isModified() {
        if (super.isModified() || name.isModified())
            return true;
        if (address.isModified())
            return true;
        return false;
    }

    public void setModified(boolean value) {
        super.setModified(value);
        name.setModified(value);
        address.setModified(value);
    }

    public void setPrimaryKeyID(Integer value) {
        super.setPrimaryKeyID(value);

        address.setOwnerPrimaryKeyID(value);
    }

    public PersonName getName() {
        if (name == null)
            name = new PersonName();
        return name;
    }

    public void setName(PersonName value) {
        name = value;
    }

    public Address getAddress() {
        if (address == null)
            address = new Address();
        return address;
    }

    public void setAddress(Address value) {
        address = value;
    }

    public PersonName getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(PersonName value) {
        ownerName = value;
    }

}
