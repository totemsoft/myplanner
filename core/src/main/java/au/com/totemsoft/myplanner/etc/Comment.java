/*
 * Comment.java
 *
 * Created on 14 November 2001, 16:36
 */

package au.com.totemsoft.myplanner.etc;

import au.com.totemsoft.myplanner.api.bean.IFPSAssignableObject;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;

public class Comment extends FPSAssignableObject {

    private String commentText;

    public static final Integer OBJECT_TYPE_ID = new Integer(
            ObjectTypeConstant.COMMENT);

    public Integer getObjectTypeID() {
        return OBJECT_TYPE_ID;
    }

    /** Creates new Comment */
    public Comment() {
    }

    public Comment(Integer ownerPrimaryKeyID) {
        super(ownerPrimaryKeyID);
    }

    /**
     * Assignable methods
     */
    @Override
    public void assign(IFPSAssignableObject value) throws ClassCastException {

        super.assign(value);

        if (!(this instanceof Comment))
            throw new ClassCastException("This is not a "
                    + this.getClass().getName());

        Comment c = (Comment) value;

        setCommentText(c.commentText);

    }

    /**
     * helper methods
     */
    public void clear() {
        super.clear();

        commentText = null;

    }

    /**
     * get/set methods
     */
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String value) {
        if (value != null)
            value = value.trim();
        if (value != null && value.length() == 0)
            value = null; // too smart ???

        if (equals(commentText, value))
            return;

        commentText = value;
        setModified(true);
    }

}
