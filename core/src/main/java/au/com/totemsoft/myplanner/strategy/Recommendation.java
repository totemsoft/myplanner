/*
 * Recommendation.java
 *
 * Created on 20 December 2002, 17:10
 */

package au.com.totemsoft.myplanner.strategy;

import au.com.totemsoft.myplanner.bean.AbstractBase;

public class Recommendation extends AbstractBase {

    static final long serialVersionUID = 6151414596952719756L;

    private String name;

    private java.util.Collection source;

    private java.util.Collection destination;

    /** Creates a new instance of Recommendation */
    public Recommendation(String name) {
        this.name = name;
        source = new java.util.ArrayList();
        destination = new java.util.ArrayList();
    }

    public String getName() {
        return name;
    }

    public java.util.Collection getSource() {
        return source;
    }

    public java.util.Collection getDestination() {
        return destination;
    }

    public void addToSource(Object obj) {
        if (!source.contains(obj))
            source.add(obj);
    }

    public void removeFromSource(Object obj) {
        if (source.contains(obj))
            source.remove(obj);
    }

    public void addToDestination(Object obj) {
        if (!destination.contains(obj))
            destination.add(obj);
    }

    public void removeFromDestination(Object obj) {
        if (destination.contains(obj))
            destination.remove(obj);
    }

}
