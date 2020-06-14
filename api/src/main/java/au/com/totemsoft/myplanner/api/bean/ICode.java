package au.com.totemsoft.myplanner.api.bean;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public interface ICode extends IBase<Integer>
{

    /**
     * 
     * @return
     */
    Integer getId();

    /**
     * 
     * @return
     */
    String getCode();

    /**
     * 
     * @return
     */
    String getDescription();

}
