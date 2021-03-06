/*
 * Model.java
 *
 * Created on 12 February 2002, 11:57
 */

package au.com.totemsoft.myplanner.projection.save;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.Vector;

import au.com.totemsoft.myplanner.api.bean.IFPSAssignableObject;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.etc.DuplicateException;
import au.com.totemsoft.myplanner.etc.FPSAssignableObject;
import au.com.totemsoft.myplanner.etc.ModelTitleRestrictionException;

public class Model extends FPSAssignableObject {

    public static final Model NONE = new Model("None");

    private ModelCollection owner;

    private Integer typeID;

    private String title;

    private String desc;

    private String data = ""; // not null

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.MODEL);

    // public static MoneyCalc getCalculator( Integer modelTypeID ) {
    // return modelTypeID == null ? null : getCalculator( modelTypeID.intValue()
    // );
    // }
    // public static MoneyCalc getCalculator( int modelTypeID ) {
    // return MoneyCalc.getNewCalculator( modelTypeID );
    // }

    /** Creates new Model */
    public Model() {
        super();
    }

    public Model(String title) {
        this.title = title;
    }

    public Model(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    public Model(Model m) {
        assign(m);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || !(obj instanceof Model))
            return false;

        return equals(typeID, ((Model) obj).typeID)
                && equals(title, ((Model) obj).title)
                && equals(desc, ((Model) obj).desc)
                && equals(data, ((Model) obj).data);
    }

    public String toString() {
        return getTitle();
    }

    public Object clone() throws CloneNotSupportedException {
        Model m = new Model();
        m.assign(this);
        return m;
    }

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    public ModelCollection getOwner() {
        return owner;
    }

    public void setOwner(ModelCollection value) {
        if (owner != null)
            return;
        owner = value;
    }

    /**
     * Assignable methods
     */
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Model))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Model m = (Model) value;

        owner = value == null ? null : m.owner;
        typeID = value == null ? null : m.typeID;
        title = value == null ? null : m.title;
        desc = value == null ? null : m.desc;
        data = value == null ? null : m.data;

        setModified(true);
    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        // typeID = null;
        // title = null;
        // desc = null;

        data = ""; // only data cleared

    }

    public void checkDuplicates(String newTitle) throws DuplicateException,
            ModelTitleRestrictionException {
        if (owner == null)
            return;

        if (newTitle == null || newTitle.trim().length() < 3)
            throw new ModelTitleRestrictionException(
                    "Invalid title. Please ensure the total "
                            + "characters for title is 3 or more.");

        newTitle = newTitle.trim();
        StringBuffer allTitles = new StringBuffer();

        java.util.Iterator iterModels = owner.valuesIterator();
        while (iterModels.hasNext()) {
            Vector _models = (Vector) iterModels.next();

            java.util.Iterator iter = _models.iterator();
            while (iter.hasNext()) {
                Model model = (Model) iter.next();

                if (model == null || model == this || model.getTitle() == null) // removed
                                                                                // or
                                                                                // same
                    continue;

                String s = model.getTitle().trim();
                allTitles.append(s);

                if (newTitle.equalsIgnoreCase(s))
                    throw new DuplicateException(allTitles.toString());

            }

        }

    }

    /**
     * get/set methods
     */
    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer value) {
        if (equals(typeID, value))
            return;

        typeID = value;
        setModified(true);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) throws DuplicateException,
            ModelTitleRestrictionException {
        if (value != null)
            value = value.trim();

        if (equals(title, value))
            return;

        checkDuplicates(value);

        title = value;

        setModified(true);
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String value) {
        if (value != null)
            value = value.trim();

        if (equals(desc, value))
            return;

        desc = value;
        setModified(true);
    }

    public String getData() {
        return data;
    }

    public void setData(String value) {

        if (value == null)
            value = "";

        if (equals(data, value))
            return;

        data = value;

        setModified(true);

    }

}
