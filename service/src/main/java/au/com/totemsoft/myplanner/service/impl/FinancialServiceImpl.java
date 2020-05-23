package au.com.totemsoft.myplanner.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.totemsoft.dao.SQLHelper;
import au.com.totemsoft.myplanner.api.ObjectNotFoundException;
import au.com.totemsoft.myplanner.api.ServiceException;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode;
import au.com.totemsoft.myplanner.api.bean.hibernate.FinancialType;
import au.com.totemsoft.myplanner.api.code.CodeComparator;
import au.com.totemsoft.myplanner.api.code.LinkObjectTypeConstant;
import au.com.totemsoft.myplanner.api.dao.FinancialCodeDao;
import au.com.totemsoft.myplanner.api.service.FinancialService;
import au.com.totemsoft.myplanner.bean.Financial;
import au.com.totemsoft.myplanner.bean.db.FinancialBean;
import au.com.totemsoft.myplanner.bean.db.ObjectClass;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FinancialServiceImpl implements FinancialService {

    //private static final Logger LOG = Logger.getLogger(FinancialServiceImpl.class);

    @Autowired private SQLHelper sqlHelper;

    @Autowired private FinancialCodeDao financialCodeDao;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findFinancialTypes(java.lang.Integer)
     */
    @Override
    public FinancialType[] findFinancialTypes(Integer objectTypeId) {
        try {
            List<FinancialType> result = financialCodeDao.findFinancialTypes(objectTypeId);
            return result.toArray(FinancialType.EMPTY);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findFinancialType(java.lang.Integer)
     */
    @Override
    public FinancialType findFinancialType(Integer financialTypeId) {
        try {
            return financialCodeDao.findFinancialType(financialTypeId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findFinancialCode(java.lang.Integer)
     */
    @Override
    public FinancialCode findFinancialCode(Integer financialCodeId) {
        try {
            return financialCodeDao.findFinancialCode(financialCodeId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findByFinancialCode(java.lang.String)
     */
    @Override
    public FinancialCode findByFinancialCode(String code) {
        try {
            return financialCodeDao.findByColumnName(FinancialCode.COLUMN_CODE, code);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findByFinancialCodeDesc(java.lang.String)
     */
    @Override
    public FinancialCode findByFinancialCodeDesc(String desc) {
        try {
            return financialCodeDao.findByColumnName(FinancialCode.COLUMN_DESC, desc);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#save(au.com.totemsoft.myplanner.api.bean.hibernate.FinancialCode)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void save(FinancialCode entity) {
        try {
            if (entity.getId() != null && entity.getId() > 0) {
                financialCodeDao.store(entity);
            }
            else {
                financialCodeDao.create(entity);
            }
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findFinancial(java.lang.Integer)
     */
    @Override
    public Financial findFinancial(Integer personId, Integer financialId) {
        Connection con = null;
        try {
            con = sqlHelper.getConnection();
            PreparedStatement sql = null;
            ResultSet rs = null;
            Integer objectTypeId = null;
            try {
                sql = con.prepareStatement(
                    "SELECT ObjectTypeID FROM Object WHERE ObjectID = ?",
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
    
                int i = 0;
                sql.setInt(++i, financialId);
                rs = sql.executeQuery();
    
                // save id, typeId ONLY
                if (!rs.next()) {
                    return null;
                }
                objectTypeId = rs.getInt("ObjectTypeID");
            } finally {
                sqlHelper.close(rs, sql);
            }

            FinancialBean fb = ObjectClass.createNewInstance(objectTypeId);
            return initFinancial(financialId, personId, fb, con);
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                sqlHelper.close(con);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.api.service.FinancialService#findFinancials(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<Integer, Map<Integer, Financial>> findFinancials(Integer personId, Integer strategyGroupId) {
        Connection con = null;
        try {
            con = sqlHelper.getConnection();

            PreparedStatement sql = null;
            ResultSet rs = null;
            Map<Integer, Map<Integer, Financial>> result = null;
            // get object id's for ALL person financial
            try {
                sql = con.prepareStatement(
                    "SELECT FinancialID, ObjectID, ObjectTypeID"
                    + " FROM Financial, Object"
                    + " WHERE"
                    + " (FinancialID IN"
                    + " (SELECT ObjectID2 FROM Link WHERE ObjectID1 = ? AND LinkObjectTypeID = ? AND LogicallyDeleted IS NULL)"
                    + " ) AND (NextID IS NULL)"
                    + (strategyGroupId == null ? " AND (StrategyGroupID IS NULL)" : " AND (StrategyGroupID = ?)")
                    + " AND (FinancialID = ObjectID)",
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

                int i = 0;
                sql.setInt(++i, personId.intValue());
                sql.setInt(++i, LinkObjectTypeConstant.PERSON_2_FINANCIAL); // 1004
                if (strategyGroupId != null) {
                    sql.setInt(++i, strategyGroupId.intValue());
                }
                rs = sql.executeQuery();

                // save id, typeId ONLY
                while (rs.next()) {
                    if (result == null) {
                        result = new TreeMap<Integer, Map<Integer, Financial>>(new CodeComparator<Integer>());
                    }
                    Integer id = rs.getInt("FinancialID");
                    Integer objectTypeId = rs.getInt("ObjectTypeID");
                    // get/create map for this object type
                    Map<Integer, Financial> map = result.get(objectTypeId);
                    if (map == null) {
                        // map = new TreeMap();
                        map = new TreeMap<Integer, Financial>(new CodeComparator<Integer>());
                        result.put(objectTypeId, map);
                    }
                    map.put(id, null); // load Financial object later (on request)
                }
                if (result == null) {
                    return null;
                }
            } finally {
                sqlHelper.close(rs, sql);
            }

            // create ALL objects for ALL object types
            // init null Financial objects (if any) for this object type
            Iterator<Integer> iter = result.keySet().iterator();
            while (iter.hasNext()) {
                initFinancials(personId, result, iter.next(), con);
            }
            // Map( ObjectTypeID, Map( ObjectID, Object ) )
            return result;
        } catch (SQLException e) {
            sqlHelper.printSQLException(e);
            //con.rollback();
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                sqlHelper.close(con);
            } catch (SQLException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    // create Financial object (owner - personID)
    private Financial initFinancial(Integer financialId, Integer personId, FinancialBean fb, Connection con) throws SQLException {
        Financial f = fb.getNewFinancial();
        f.setOwnerId(personId);
        // set and initialize Financial objects using FinancialBean object
        fb.setFinancial(f);
        try {
            fb.load(financialId, con);
            return f;
        } // id will be > 0 allways
        catch (ObjectNotFoundException e) {
            e.printStackTrace(System.err);
            throw new SQLException(e.getMessage());
        }
    }

    private void initFinancials(Integer personId, Map<Integer, Map<Integer, Financial>> result, Integer objectTypeId, Connection con) throws SQLException {
        if (result == null) {
            return;
        }
        // create ONLY this object id
        Map<Integer, Financial> map = result.get(objectTypeId);
        if (map == null) {
            return;
        }
        // create dummy FinancialBean object (derived ones - of course)
        // will be used to initialize ALL Financial objects of this type
        FinancialBean fb = null;
        // iterate through all objects of this type
        Iterator<Entry<Integer, Financial>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, Financial> entry = iter.next();
            Integer financialId = entry.getKey();
            Financial f = entry.getValue();
            if (f != null) {
                continue; // already initialized (id < 0 means not saved to db yet)
            }
            if (fb == null) {
                fb = ObjectClass.createNewInstance(objectTypeId);
            }
            f = initFinancial(financialId, personId, fb, con);
            entry.setValue(f);
        }
    }

}