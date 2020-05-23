/*
 * PersonNameAddress.java
 *
 * Created on 24 October 2001, 18:23
 */

package au.com.totemsoft.myplanner.etc;

import au.com.totemsoft.myplanner.api.bean.IFPSAssignableObject;
import au.com.totemsoft.myplanner.api.bean.PersonName;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

public class PersonNameAddress extends FPSAssignableObject {

    private PersonName ownerName;

    protected PersonName name = new PersonName();

    protected AddressDto address = new AddressDto();

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
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof PersonNameAddress))
            throw new ClassCastException("This is not a " + this.getClass().getName());

        PersonNameAddress pna = (PersonNameAddress) value;

        setName(pna.getName());
        setAddress(pna.getAddress());

        // has to be last (to set modified)
        setModified(value.isModified());
    }

    /**
     * helper methods
     */
    public void clear() {

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

    public void setId(Integer value) {
        super.setId(value);

        address.setOwnerId(value);
    }

    public PersonName getName() {
        if (name == null)
            name = new PersonName();
        return name;
    }

    public void setName(PersonName value) {
        name = value;
    }

    public AddressDto getAddress() {
        if (address == null)
            address = new AddressDto();
        return address;
    }

    public void setAddress(AddressDto value) {
        address = value;
    }

    public PersonName getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(PersonName value) {
        ownerName = value;
    }

}
