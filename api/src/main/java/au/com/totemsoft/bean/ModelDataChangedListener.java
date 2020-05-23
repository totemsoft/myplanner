package au.com.totemsoft.bean;

import java.util.EventListener;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 1.0
 */

public interface ModelDataChangedListener extends EventListener {

    public void modelDataChanged(ModelDataChangedEvent e);
}