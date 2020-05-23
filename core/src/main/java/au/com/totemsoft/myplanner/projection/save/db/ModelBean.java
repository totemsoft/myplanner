/*
 * ModelBean.java
 *
 * Created on 12 February 2002, 12:18
 */

package au.com.totemsoft.myplanner.projection.save.db;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import au.com.totemsoft.io.IOUtils2;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.code.ObjectTypeConstant;
import au.com.totemsoft.myplanner.bean.db.AbstractPersistable;
import au.com.totemsoft.myplanner.etc.DuplicateException;
import au.com.totemsoft.myplanner.etc.ModelTitleRestrictionException;
import au.com.totemsoft.myplanner.projection.save.Model;
import au.com.totemsoft.util.StringUtils;

public class ModelBean extends AbstractPersistable {

    private Model model; // aggregation

    // First level of linkage (e.g. objectTypeID1 = PERSON), objectTypeID2 =
    // MODEL
    private int[] linkObjectTypes = new int[1];

    /** Creates new ModelBean */
    public ModelBean() {
    }

    public ModelBean(Model value) {
        this.model = value;
    }

    /**
     * helper methods
     */
    public Class getCommentClass() {
        return Model.class;
    }

    public int getObjectTypeID() {
        return ObjectTypeConstant.MODEL;
    }

    protected int getLinkObjectTypeID(int level) {
        switch (level) {
        case 1:
            return linkObjectTypes[0];
        default:
            throw new RuntimeException(
                    "ModelBean.getLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    public void setLinkObjectTypeID(int level, int value) {
        switch (level) {
        case 1: {
            setModified(linkObjectTypes[0] != value);
            linkObjectTypes[0] = value;
            break;
        }
        default:
            throw new RuntimeException(
                    "ModelBean.setLinkObjectTypeID() Invalid linkage level: "
                            + level);
        }
    }

    /**
     * IPersistable methods
     */
    public String getSelectFieldsList() {
        return "ModelID, ModelTypeID, ModelTitle, ModelDesc, ModelData, ModelData2";
    }

    public void load(Connection con) throws SQLException,
            ObjectNotFoundException {
        load(getId(), con);
    }

    public void load(Integer primaryKeyID, Connection con) throws SQLException,
            ObjectNotFoundException {

        boolean newConnection = (con == null);
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            if (newConnection)
                con = this.getConnection();

            sql = con.prepareStatement("SELECT " + getSelectFieldsList()
                    + " FROM Model" + " WHERE (ModelID = ?)",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, primaryKeyID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Model ID: "
                        + primaryKeyID);

            load(rs);

            // has to be last (to be safe), we are not using primaryKeyID for
            // other queries
            setId(primaryKeyID);

        } finally {
            close(rs, sql);
            if (newConnection && con != null)
                con.close();
        }

    }

    public void load(ResultSet rs) throws SQLException {

        int i = 0;

        Integer modelID = (Integer) rs.getObject(++i);
        if (!equals(getId(), modelID))
            setId(modelID);

        // Model table
        setTypeID((Integer) rs.getObject(++i));

        try {
            setTitle(rs.getString(++i));
        } catch (DuplicateException e) {
        } catch (ModelTitleRestrictionException me) {
        }

        setDescription(rs.getString(++i));

        String mData = rs.getString(++i);
        String mData2 = rs.getString(++i); // new text presentation of ntext
                                            // data
        setData(mData2 == null ? mData : fromText(mData2));

    }

    private String fromText(String mData) {
        try {
            return new String(StringUtils.fromHexString(mData),
                    IOUtils2.ENCODING_2_SERIALIZE);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private String toText(String mData) {
        try {
            return StringUtils.toHexString(mData
                    .getBytes(IOUtils2.ENCODING_2_SERIALIZE));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public int store(Connection con) throws SQLException {

        if (!isModified())
            return 0;

        int primaryKeyID = 0;

        int i = 0;
        PreparedStatement sql = null;

        try {
            if (getId() == null || getId().intValue() < 0) {

                primaryKeyID = getNewObjectID(getObjectTypeID(), con);

                // do insert into Comment table
                sql = con
                        .prepareStatement("INSERT INTO Model"
                                + " (ModelID, ModelTypeID, ModelTitle, ModelDesc, ModelData, ModelData2)"
                                + " VALUES" + " (?,?,?,?,?,?)");

                sql.setInt(++i, primaryKeyID);
                sql.setObject(++i, getTypeID(), java.sql.Types.INTEGER);
                sql.setString(++i, getTitle());
                sql.setString(++i, getDescription());

                String mData = getData();
                sql.setString(++i, mData); // set it to null later ???
                sql.setString(++i, toText(mData));

                sql.executeUpdate();

                setId(new Integer(primaryKeyID));

                int linkID = setLink(getLinkObjectTypeID(1), con);

            } else {

                primaryKeyID = getId().intValue();

                // do update on Comment table
                sql = con
                        .prepareStatement("UPDATE Model SET"
                                + " ModelTypeID=?,ModelTitle=?,ModelDesc=?,ModelData=?,ModelData2=?,DateModified=?"
                                + " WHERE ModelID=?");

                sql.setObject(++i, getTypeID(), java.sql.Types.INTEGER);
                sql.setString(++i, getTitle());
                sql.setString(++i, getDescription());

                String mData = getData();
                sql.setString(++i, mData); // set it to null later ???
                sql.setString(++i, toText(mData));

                sql.setDate(++i, new java.sql.Date(System.currentTimeMillis()));

                sql.setInt(++i, primaryKeyID);

                sql.executeUpdate();

            }
        } finally {
            close(null, sql);
        }

        setModified(false);

        return primaryKeyID;

    }

    // remove/disable link only: DbID.PERSON_2_MODEL
    public void remove(Connection con) throws SQLException {

        // remove/disable link only: DbID.PERSON_2_MODEL
        int linkID = linkObjectDao.unlink(getOwnerId(),
                getId(), getLinkObjectTypeID(1), con);

    }

    public Integer find() throws SQLException {
        return null;
    }

    /**
     * get/set methods
     */
    public Model getModel() {
        if (model == null)
            model = new Model();
        return model;
    }

    public void setModel(Model value) {
        model = value;
        if (model != null)
            setModified(true);
    }

    public boolean isModified() {
        return getModel().isModified();
    }

    public void setModified(boolean value) {
        getModel().setModified(value);
    }

    public Integer getId() {
        return getModel().getId();
    }

    public void setId(Integer value) {
        getModel().setId(value);
    }

    public Integer getOwnerPrimaryKeyID() {
        return getModel().getOwnerId();
    }

    public void setOwnerPrimaryKeyID(Integer value) {
        getModel().setOwnerId(value);
    }

    public Integer getTypeID() {
        return getModel().getTypeID();
    }

    public void setTypeID(Integer value) {
        getModel().setTypeID(value);
    }

    public String getTitle() {
        return getModel().getTitle();
    }

    public void setTitle(String value) throws DuplicateException,
            ModelTitleRestrictionException {
        getModel().setTitle(value);
    }

    public String getDescription() {
        return getModel().getDescription();
    }

    public void setDescription(String value) {
        getModel().setDescription(value);
    }

    public String getData() {
        return getModel().getData();
    }

    public void setData(String value) {
        getModel().setData(value);
    }

}
