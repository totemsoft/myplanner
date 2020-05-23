/*
 * MessageSentEvent.java
 *
 * Created on 18 February 2003, 16:46
 */

package au.com.totemsoft.bean;

import java.util.EventObject;

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

public class MessageSentEvent extends EventObject {

    public MessageSentEvent(Object source) {
        super(source);
    }

    public String getMessage() {
        Object obj = this.getSource();
        if (obj instanceof MessageSent) {
            return ((MessageSent) obj).getMessage();
        }
        return "";

    }

    public String getMessageType() {
        Object obj = this.getSource();
        if (obj instanceof MessageSent) {
            return ((MessageSent) obj).getMessageType();
        }
        return "";
    }

    public Object getMessageSender() {
        Object obj = this.getSource();
        if (obj instanceof MessageSent) {
            return ((MessageSent) obj).getMessageSender();
        }
        return null;

    }
}
