/*
 * MessageSent.java
 *
 * Created on 18 February 2003, 16:48
 */

package com.argus.bean;

/**
 * 
 * @version
 */

public class MessageSent {

    private String message = "";

    private String messageType = "";

    private Object messageSender = null;

    /** Creates new DataChanged */
    public MessageSent(Object messageSender, String messageType, String message) {

        this.message = message;
        this.messageType = messageType;
        this.messageSender = messageSender;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public Object getMessageSender() {
        return this.messageSender;
    }

    public static final String WARNING = "Warning";

    public static final String ERROR = "Error";

    public static final String INFO = "Info";

}
