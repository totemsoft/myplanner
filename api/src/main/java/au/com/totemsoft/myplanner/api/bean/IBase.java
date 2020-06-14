package au.com.totemsoft.myplanner.api.bean;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface IBase<T>
{

    /** ID property name */
    String ID = "id";

    /** IGNORE property(s) */
    String[] IGNORE = new String[] {};

    /**
     * @return
     */
    T getId();

    boolean isModified();
    void setModified(boolean value);

}
