/*
 * ModelCollection.java
 *
 * Created on 12 February 2002, 13:37
 */

package com.argus.financials.projection.save;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.argus.util.VectorTemplate;

public class ModelCollection implements java.io.Serializable {

    /**
     * STRUCTURE: Map( ModelTypeID, Vector( Model ) )
     */
    private Map models = new HashMap();

    private VectorTemplate models2remove = new VectorTemplate(Model.class);

    public java.util.Collection getModelsToRemove() {
        return models2remove;
    }

    /** Creates new ModelCollection */
    public ModelCollection() {
    }

    /**
     * 
     */
    public int size() {
        return models.size();
    }

    public Vector getModels(Integer modelTypeID) {
        return (Vector) models.get(modelTypeID);
    }

    public void addModel(Model value) {
        if (value == null)
            return;

        Vector _models = getModels(value.getTypeID());
        if (_models == null) {
            _models = new VectorTemplate(Model.class);
            models.put(value.getTypeID(), _models);
        }

        removeModel(_models, value);

        value.setOwner(this);
        _models.add(value); // replace anyway

    }

    private boolean contains(Vector source, Model model) {

        String title = model.getTitle();
        if (title == null)
            return true; // skip null values

        Iterator iter = source.iterator();
        while (iter.hasNext())
            if (title.equals(((Model) iter.next()).getTitle()))
                return true;

        return false;

    }

    private Model removeModel(Vector source, Model model) {

        String title = model.getTitle();
        if (title == null)
            return null; // skip null values

        Iterator iter = source.iterator();
        while (iter.hasNext()) {
            Model m = (Model) iter.next();
            if (title.equals(m.getTitle())) {
                if (source.remove(m))
                    return m;
                return null;
            }
        }

        return null;

    }

    public void removeModel(Model value) {
        if (value == null)
            return;

        Vector _models = getModels(value.getTypeID());
        if (_models == null)
            return;

        _models.remove(value);
        models2remove.add(value); // for removal
    }

    public Iterator entrySetIterator() {
        return models.entrySet().iterator();
    }

    public Iterator keySetIterator() {
        return models.keySet().iterator();
    }

    public Iterator valuesIterator() {
        return models.values().iterator();
    }

    public Model findByName(Integer modelTypeID, String name) {

        if (name == null || name.trim().length() == 0 || modelTypeID == null)
            return null;

        Vector _models = getModels(modelTypeID);
        if (_models == null)
            return null;

        Iterator iter = _models.iterator();
        while (iter.hasNext()) {
            Model m = (Model) iter.next();
            if (name.equals(m.toString()))
                return m;
        }
        return null;

    }

}