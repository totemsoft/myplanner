/*
 * PersonServiceImpl.java
 *
 * Created on 24 July 2001, 12:16
 */

package com.argus.financials.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;

import com.argus.financials.api.ObjectNotFoundException;
import com.argus.financials.api.ServiceException;
import com.argus.financials.api.bean.DbConstant;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.api.bean.ILanguage;
import com.argus.financials.api.bean.IMaritalCode;
import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.api.bean.IPersonTrustDIYStatus;
import com.argus.financials.api.bean.ISexCode;
import com.argus.financials.api.bean.ITitleCode;
import com.argus.financials.api.code.LinkObjectTypeConstant;
import com.argus.financials.api.dao.EntityDao;
import com.argus.financials.api.dao.OccupationDao;
import com.argus.financials.api.dto.PersonDto;
import com.argus.financials.api.service.FinancialService;
import com.argus.financials.bean.Assets;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.bean.db.FinancialBean;
import com.argus.financials.bean.db.FinancialGoalBean;
import com.argus.financials.bean.db.ObjectClass;
import com.argus.financials.code.AddressCode;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.dao.BusinessDao;
import com.argus.financials.domain.hibernate.Occupation;
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.domain.hibernate.refdata.Language;
import com.argus.financials.etc.AddressDto;
import com.argus.financials.etc.Comment;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Dependent;
import com.argus.financials.etc.Survey;
import com.argus.financials.etc.db.AddressBean;
import com.argus.financials.etc.db.CommentBean;
import com.argus.financials.etc.db.ContactBean;
import com.argus.financials.etc.db.ContactMediaBean;
import com.argus.financials.etc.db.DependentBean;
import com.argus.financials.etc.db.PersonHealth;
import com.argus.financials.etc.db.PersonTrustDIYStatus;
import com.argus.financials.etc.db.SurveyBean;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.projection.save.db.ModelBean;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.FinderException;
import com.argus.financials.service.PersonService;
import com.argus.io.IOUtils2;
import com.argus.util.BeanUtils;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;
import com.argus.util.StringUtils;

public class PersonServiceImpl extends AbstractServiceImpl implements PersonService {

    @Autowired protected BusinessDao businessDao;
    @Autowired protected EntityDao entityDao;
    @Autowired protected OccupationDao occupationDao;

    @Autowired protected FinancialService financialService;

    public PersonServiceImpl() {
        super();
    }

    @Deprecated
    private PersonServiceImpl(PersonServiceImpl other) {
        this.businessDao = other.businessDao;
        this.entityDao = other.entityDao;
        this.occupationDao = other.occupationDao;
        this.financialService = other.financialService;
    }


    /**
     * to create a new instance of Person entity bean and therefore insert data
     * into database invoke create(...) method
     */
    public Integer persist(Integer ownerPersonID) throws ServiceException {
        try {
            PersonServiceImpl pb = new PersonServiceImpl(this);
            pb.setOwnerId(ownerPersonID);
            Connection con = sqlHelper.getConnection();
            Integer personID = pb.create(con);
            sqlHelper.close(con);
            return personID;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }


    /**
     * to create a new instance of Person entity bean and therefore insert data
     * into database invoke create(...) method
     */
    protected Integer create(Connection con) throws ServiceException, CreateException {

        PreparedStatement sql = null;
        try {
            // get new ObjectID for person
            Integer personId = getNewObjectID(PERSON, con);
            // add to Person table
            sql = con.prepareStatement("INSERT INTO Person (PersonID) VALUES (?)");
            sql.setInt(1, personId);
            sql.executeUpdate();
            return personId;
        } catch (SQLException e) {
            throw new CreateException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            try {
                close(null, sql);
            } catch (SQLException e) {
                throw new ServiceException(e);
            }
        }
    }

    public PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException,
        FinderException {
        PersonServiceImpl person = new PersonServiceImpl(this);
        person.findByPrimaryKey(personID);
        return person;
    }

    protected int getPersonID() {
        Integer id = getId();
        if (id != null)
            return id;
        return 0;
    }

    /**
     * 
     */
    public void storePerson() throws ServiceException {
        try {
            Connection con = sqlHelper.getConnection();
            storePerson(con);
            if (financialGoal != null)
                storeFinancialGoal(con);
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void storeFinancials(Map financials, FinancialGoal financialGoal) throws ServiceException {
        if (financials == null && financialGoal == null)
            return;
        try {
            Connection con = sqlHelper.getConnection();
            if (financials != null)
                storeFinancials(financials); // first
            if (financialGoal != null)
                storeFinancialGoal(con); // second (will use financial data)
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeComments() throws ServiceException {
        if (comments == null)
            return;
        try {
            Connection con = sqlHelper.getConnection();
            storeComments(con);
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeSurveys() throws ServiceException {
        if (surveys == null)
            return;
        try {
            Connection con = sqlHelper.getConnection();
            storeSurveys(con);
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeModels() throws ServiceException {
        if (models == null)
            return;
        try {
            Connection con = sqlHelper.getConnection();
            storeModels(con);
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeEmployerBusiness() throws ServiceException {
        if (business == null)
            return;
        try {
            Connection con = sqlHelper.getConnection();
            storeEmployerBusiness(con);
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }
    public Object getOwnerPrimaryKey() throws ServiceException {
        return getOwnerId();
    }

    public Integer findByPrimaryKey(Integer personID) throws ServiceException,
            FinderException {

        try {
            // we need client personal information
            this.load(personID);
            return personID;

        } catch (ObjectNotFoundException e) {
            e.printStackTrace(System.err);
            throw new FinderException(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    protected PersonServiceImpl getPerson(Integer personID) throws ServiceException {

        PersonServiceImpl pb = new PersonServiceImpl(this);

        if (personID == null) {
            // create new partner ( will be saved and properly linked in
            // storePerson() )
            pb.setOwnerId(getId());
        } else {
            // load existing partner
            try {
                pb.findByPrimaryKey(personID);
            } catch (FinderException e) {
                throw new ServiceException(e);
            }
        }

        IPerson pn = pb.getPersonName();
        if (pn == null)
            pb.setPersonName(new PersonDto());

        return pb;

    }

    // PERSON SPECIFIC MEMBERS
    private IPerson person = new PersonDto();
    public IPerson getPersonName() {
        return person;
    }
    public void setPersonName(IPerson value) {
        person = value;
    }

    //private Occupation occupation;

    private IBusiness business;

    private AddressDto residentialAddress;

    private AddressDto postalAddress;

    private TreeMap dependents;

    private TreeMap contacts;

    private FinancialGoal financialGoal;

    private HashMap contactMediaDetails;

    private ModelCollection models;

    /**
     * map to store Comment objects till they are saved to db (then clear all
     * for efficiency)
     */
    private HashMap comments;

    /**
     * map to store Survey objects till they are saved to db (then clear all for
     * efficiency)
     */
    private HashMap surveys;

    public boolean isModifiedRemote() throws ServiceException {
        return isModified();
    }

    public boolean isModified() {
        return super.isModified()
                || residentialAddress.isModified()
                || postalAddress.isModified();
    }

    public void setModified(boolean value) {
        super.setModified(value);

        if (residentialAddress != null)
            residentialAddress.setModified(value);
        if (postalAddress != null)
            postalAddress.setModified(value);
    }

    /**
     * 
     */
    public IBusiness getEmployerBusiness() throws ServiceException {
        if (business == null) {
            Connection con = null;
            try {
                con = sqlHelper.getConnection();
                List list = getLinkedObjects(PERSON_2_BUSINESS, 0, PERSON$BUSINESS_2_OCCUPATION, con);
                if (list != null && !list.isEmpty()) {
                    Integer businessId = (Integer) list.get(0);
                    business = businessDao.load(businessId);
                }
            } catch (SQLException e) {
                business = null;
                throw new ServiceException(e);
            } catch (ObjectNotFoundException e) {
                business = null;
                throw e;
            } finally {
                try {
                    sqlHelper.close(con);
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
        return business;
    }

    public void storeEmployerBusiness(Connection con) throws SQLException {
        if (business == null) {
            return;
        }

        Integer businessId = business.getId();
        businessId = businessDao.store(business, con);
        // existing business object
        if (businessId != null) {
            return;
        }

        // new business object just created, now it has to be linked to this person
        linkObjectDao.link(
                linkObjectDao.link(getId(),
                        businessId, PERSON_2_BUSINESS,
                        con), 0, PERSON$BUSINESS_2_OCCUPATION, con);
    }

    /**
     * 
     */
    public AddressDto getResidentialAddress() throws ServiceException {
        if (residentialAddress == null) {
            residentialAddress = new AddressDto(getPersonID());
            residentialAddress.setAddressCodeId(AddressCode.RESIDENTIAL);
        }
        return residentialAddress;
    }

    public void setResidentialAddress(AddressDto value) throws ServiceException {
        if (residentialAddress == null)
            residentialAddress = value;
        else
            residentialAddress.assign(value);
    }

    public AddressDto getPostalAddress() throws ServiceException {
        if (postalAddress == null) {
            postalAddress = new AddressDto(getPersonID());
            postalAddress.setAddressCodeId(AddressCode.POSTAL);
        }
        return postalAddress;
    }

    public void setPostalAddress(AddressDto value) throws ServiceException {
        if (postalAddress == null)
            postalAddress = value;
        else
            postalAddress.assign(value);
    }

    /**
     * 
     */
    private void loadAddresses(Connection con) throws SQLException {

        int personID = getPersonID();

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            sql = con
                    .prepareStatement(
                            "SELECT a.*"
                                    + " FROM Link l, Address a"
                                    + " WHERE ( l.ObjectID1 = ? ) AND ( l.LinkObjectTypeID = ? )"
                                    + " AND ( l.ObjectID2 = a.AddressID )"
                                    + " AND ( LogicallyDeleted IS NULL )"
                                    + " ORDER BY AddressID DESC",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, personID);
            sql.setInt(2, PERSON_2_ADDRESS);
            rs = sql.executeQuery();

            AddressBean ab = new AddressBean(PERSON_2_ADDRESS);
            residentialAddress = null;
            postalAddress = null;

            while (rs.next()) {
                ab.setAddress(new AddressDto(personID));

                ab.load(rs);
                Integer addressCodeID = ab.getAddressCodeID();

                if (AddressCode.RESIDENTIAL.equals(addressCodeID)) {
                    residentialAddress = ab.getAddress();
                } else if (AddressCode.POSTAL.equals(addressCodeID)) {
                    postalAddress = ab.getAddress();
                }

                if (residentialAddress != null && postalAddress != null)
                    break;
            }

        } finally {
            close(rs, sql);
        }

    }

    private void storeAddresses(Connection con) throws SQLException {

        AddressBean ab = new AddressBean(PERSON_2_ADDRESS);

        if (residentialAddress != null && residentialAddress.isModified()) {
            boolean notify = residentialAddress.isNotify();
            if (notify)
                residentialAddress.disableNotify();
            try {
                residentialAddress.setOwnerId(getId());
                ab.setOwnerId(getId());
                ab.setAddress(residentialAddress);
                ab.store(con);
            } finally {
                if (notify)
                    residentialAddress.enableNotify();
            }
        }

        if (postalAddress != null && postalAddress.isModified()) {
            boolean notify = postalAddress.isNotify();
            if (notify)
                postalAddress.disableNotify();
            try {
                postalAddress.setOwnerId(getId());
                ab.setAddress(postalAddress);
                ab.store(con);
            } finally {
                if (notify)
                    postalAddress.enableNotify();
            }
        }

    }

    /**
     * get/set FINANCIAL MAP methodes
     * 
     * Map( ObjectTypeID, Map( ObjectID, Object ) )
     */
    public Map getFinancials(Integer objectTypeID) throws ServiceException {
        return getFinancials(findFinancials(), objectTypeID);
    }

    public Map getFinancials(java.util.Map financialMap, Integer objectTypeID) throws ServiceException {
        if (objectTypeID == null)
            return financialMap;
        return financialMap == null ? null : (Map) financialMap.get(objectTypeID);
    }

    public Financial getFinancial(Integer financialId) throws ServiceException {
        return financialService.findFinancial(getId(), financialId);
    }

    public Financial getFinancial(java.util.Map financialMap,
            Integer financialID) throws ServiceException {

        if (financialID == null)
            return null;
        if (financialMap == null)
            return null;

        Iterator iter = financialMap.keySet().iterator();
        while (iter.hasNext()) {
            Map map = getFinancials(financialMap, (Integer) iter.next());
            if (map == null)
                continue;

            if (map.containsKey(financialID))
                return (Financial) map.get(financialID);

        }

        return null;

    }

    // get current financials, collection data
    public Map<Integer, Map<Integer, Financial>> findFinancials() throws ServiceException {
        return financialService.findFinancials(getId(), null);
    }

    public Map getStrategyGroupFinancials(Integer strategyGroupID, boolean complex) throws ServiceException {

        Connection con = null;
        try {
            con = sqlHelper.getConnection();
            Map result = getStrategyGroupFinancials(con, strategyGroupID, complex);
            sqlHelper.close(con);
            return result;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    public java.util.Map getStrategyGroupFinancials(Connection con, Integer strategyGroupID, boolean complex)
            throws java.sql.SQLException {

        Map<Integer, Map<Integer, Object>> financialMap = financialService.findFinancials(getId(), strategyGroupID);
        if (complex || financialMap == null)
            return financialMap;

        java.util.Map generatedFinancials = new java.util.TreeMap();// new BaseCodeComparator());
        Iterator<Map<Integer, Object>> iter = financialMap.values().iterator();
        while (iter.hasNext()) {
            generatedFinancials.putAll((java.util.Map) iter.next());
        }
        return generatedFinancials;

    }

    protected void storeFinancials(Map financials) throws SQLException {

        if (financials == null)
            return;

        Connection con = sqlHelper.getConnection();

        // iterate through all financial records of this type
        // and close them, call remove(null)
        Iterator iter = financials.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            Integer objectTypeID = (Integer) entry.getKey();
            Map map = (Map) entry.getValue();

            // create dummy FinancialBean object (derived ones - of course)
            // will be used to remove ALL Financial objects of this type
            FinancialBean fb = null;

            Iterator iter2 = map.entrySet().iterator();
            while (iter2.hasNext()) {
                Map.Entry entry2 = (Map.Entry) iter2.next();
                Integer objID = (Integer) entry2.getKey();
                Financial f = (Financial) entry2.getValue();

                if (fb == null)
                    fb = ObjectClass.createNewInstance(objectTypeID);
                // if ( fb == null )
                // continue; // wrong objectTypeID (e.g. SurplusItem)

                if (f == null) {
                    // remove
                    if (objID > 0) { // this removed on client
                        fb.setOwnerId(this.getId());

                        fb.setId(objID);
                        fb.remove(con); // delete object
                    }

                } else {
                    if (!f.isGenerated()) {
                        f.setOwnerId(getId());
                        fb.setFinancial(f);
                        fb.store(con);
                    }

                }

            }

            updatePrimaryKey(map);
            removeNullValues(map);

            if (fb != null)
                fb.setFinancial(null);
        }

        sqlHelper.close(con);
    }

    // delete all person current financials
    protected void deleteFinancials() throws SQLException {
        Connection con = sqlHelper.getConnection();
        try {
            Map financials = findFinancials();
            if (financials == null) {
                return;
            }

            // one by one
            Iterator iterGroup = financials.values().iterator();
            while (iterGroup.hasNext()) {
                Map group = (Map) iterGroup.next();
                Iterator iter = group.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    entry.setValue(null);
                }
            }

            storeFinancials(financials);

            // remove all current assets from collection
            Assets.getCurrentAssets().initCodes(null);

            financials = null;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        } finally {
            sqlHelper.close(con);
        }
    }

    /**
     * get/set professional contacts
     */
    public java.util.TreeMap getContacts() throws ServiceException {

        if (contacts == null) {

            Connection con = null;
            List list = null;

            try {
                con = sqlHelper.getConnection();

                list = getLinkedObjects(PERSON_2_PERSON, CONTACT,
                        PERSON_2_RELATIONSHIP_FINANCE, con);

                contacts = new TreeMap();

                if (list == null || list.size() == 0)
                    return contacts;

                Integer personID = null;
                Contact c = null;
                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    personID = (Integer) iter.next();
                    c = new Contact();
                    c.setOwnerId(getId());
                    c.setId(personID);
                    new ContactBean(c).load(personID, con);
                    contacts.put(personID, c);
                }
                if (con != null)
                    sqlHelper.close(con);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }

        }

        return contacts;

    }

    public void setContacts(java.util.TreeMap value) throws ServiceException {

        contacts = value;// new HashMap(value);
        if (contacts == null)
            return;

        Iterator iter = contacts.values().iterator();
        while (iter.hasNext()) {
            Contact c = (Contact) iter.next();
            if (c != null)
                c.setOwnerId(getId());
        }

    }

    private void storeContacts(Connection con) throws SQLException {

        if (contacts == null)
            return;

        Iterator iter = contacts.keySet().iterator();
        while (iter.hasNext()) {
            Integer id = (Integer) iter.next();
            Contact c = (Contact) contacts.get(id);

            if (c == null) {
                // remove
                if (id > 0) { // this removed on client
                    c = new Contact();
                    ContactBean cb = new ContactBean(c);
                    cb.setObjectTypeID(CONTACT);

                    c.setOwnerId(getId());
                    c.setId(id);
                    cb.remove(con); // unlink permanenty !!!
                } else {
                    ;// throw new SQLException( ".....bug....." );
                }
            } else if (c.isModified()) {
                ContactBean cb = new ContactBean(c);
                cb.setObjectTypeID(CONTACT);

                cb.store(con);
            }

        }

        updatePrimaryKey(contacts);
        removeNullValues(contacts);
    }

    /**
     * get/set dependents
     */
    public java.util.TreeMap getDependents() throws ServiceException {

        if (dependents == null) {

            Connection con = null;
            List list = null;

            try {
                con = sqlHelper.getConnection();

                list = getLinkedObjects(PERSON_2_PERSON, DEPENDENT,
                        PERSON_2_RELATIONSHIP_FINANCE, con);

                dependents = new TreeMap();

                if (list == null || list.size() == 0)
                    return dependents;

                Integer personID = null;
                Dependent d = null;

                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    personID = (Integer) iter.next();

                    d = new Dependent();
                    d.setOwnerId(getId());
                    d.setId(personID);

                    new DependentBean(d).load(personID, con);

                    dependents.put(personID, d);
                }
                sqlHelper.close(con);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }

        }

        return dependents;

    }

    public void setDependents(java.util.TreeMap value)
            throws ServiceException {

        dependents = value;// new HashMap(value);
        if (dependents == null)
            return;

        Iterator iter = dependents.values().iterator();
        while (iter.hasNext()) {
            Dependent d = (Dependent) iter.next();
            /*
             * Object[][] t = new Object[5][]; int m = 0; if ( d == null )
             * ;//rowData[i++] = new Object [] {}; else t[m++] = d.getData();
             * 
             * for ( int n = 0; n < t[m - 1].length; n++ ) System.out.println (
             * "====================" + t[m-1][n] );
             */

            if (d != null) {
                d.setOwnerId(getId());
            }

        }
    }

    private void storeDependents(Connection con) throws SQLException {

        if (dependents == null)
            return;

        Iterator iter = dependents.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer id = (Integer) entry.getKey();
            Dependent d = (Dependent) entry.getValue();

            if (d == null)
                // remove
                if (id > 0) { // this removed on client
                    DependentBean db = new DependentBean(new Dependent());
                    db.setOwnerId(getId());
                    db.setId(id);
                    db.remove(con); // unlink permanenty !!!
                } else {
                    ;// throw new SQLException( ".....bug....." );
                }
            else if (d.isModified()) {
                DependentBean db = new DependentBean(d);
                db.store(con);
            }

        }

        removeNullValues(dependents);
        updatePrimaryKey(dependents);
    }

    /**
     * 
     */
    public FinancialGoal getFinancialGoal() throws ServiceException {

        int personID = this.getPersonID();

        Connection con = null;

        try {
            con = this.sqlHelper.getConnection();

            List list = linkObjectDao.getLinkedObjects(personID,
                    PERSON_2_FINANCIALGOAL, con);

            if (list == null || list.size() == 0) {
                financialGoal = null;
            } else {
                if (financialGoal == null)
                    financialGoal = new FinancialGoal();
                financialGoal.setOwnerId(this.getId());
                financialGoal.setId((Integer) list.get(0));
                new FinancialGoalBean(financialGoal).load(con);
            }
            sqlHelper.close(con);
            return financialGoal;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    public void setFinancialGoal(FinancialGoal value)
            throws ServiceException {
        financialGoal = value;
    }

    private void storeFinancialGoal(Connection con) throws SQLException {

        if (financialGoal == null)
            return;

        FinancialGoalBean fgb = new FinancialGoalBean();

        fgb.setFinancialGoal(financialGoal);
        fgb.setOwnerId(this.getId());

        fgb.setLinkObjectTypeID(1, PERSON_2_FINANCIALGOAL);
        fgb.store(con);

    }

    /**
     * contactMediaID = null, return ALL otherwise, return ONLY this type
     */
    public ContactMedia getContactMedia(Integer contactMediaCodeID)
            throws ServiceException {
        getContactMedia(Boolean.FALSE);
        return (contactMediaDetails == null) ? null
                : (ContactMedia) contactMediaDetails.get(contactMediaCodeID);
    }

    public HashMap getContactMedia(Boolean refresh)
            throws ServiceException {

        if (refresh.booleanValue())
            contactMediaDetails = null;
        else if (contactMediaDetails != null)
            return contactMediaDetails;

        int personID = this.getPersonID();

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            con = this.sqlHelper.getConnection();

            sql = con
                    .prepareStatement(
                            "SELECT cm.*"
                                    + " FROM Link l, ContactMedia cm"
                                    + " WHERE ( l.ObjectID1 = ? ) AND ( l.LinkObjectTypeID = ? )"
                                    + " AND ( l.ObjectID2 = cm.ContactMediaID )"
                                    + " AND ( LogicallyDeleted IS NULL )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, personID);
            sql.setInt(2, PERSON_2_CONTACT_MEDIA);
            rs = sql.executeQuery();

            ContactMediaBean cmb = null;
            while (rs.next()) {
                if (contactMediaDetails == null)
                    contactMediaDetails = new HashMap();
                if (cmb == null)
                    cmb = new ContactMediaBean(null, PERSON_2_CONTACT_MEDIA);
                cmb.setContactMedia(new ContactMedia(personID));
                cmb.load(rs);
                contactMediaDetails.put(cmb.getContactMediaCodeID(), cmb.getContactMedia());
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, sql);
                if (con != null)
                    sqlHelper.close(con);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }
        }

        return contactMediaDetails;

    }

    public void setContactMedia(ContactMedia contactMedia)
            throws ServiceException {
        if (contactMedia == null)
            return;

        getContactMedia(Boolean.FALSE);
        if (contactMediaDetails == null)
            contactMediaDetails = new HashMap();

        contactMediaDetails.put(contactMedia.getContactMediaCodeID(),
                contactMedia);
    }

    public void setContactMedia(HashMap value) throws ServiceException {
        contactMediaDetails = value;// new HashMap(value);
    }

    private void storeContactMedia(Connection con) throws SQLException {

        if (contactMediaDetails == null)
            return;

        ContactMediaBean cmb = new ContactMediaBean(null,
                PERSON_2_CONTACT_MEDIA);

        Iterator iter = contactMediaDetails.keySet().iterator();
        while (iter.hasNext()) {
            Integer id = (Integer) iter.next();
            ContactMedia cm = (ContactMedia) contactMediaDetails.get(id);

            if (cm == null)
                // remove
                if (id > 0) { // this removed on client
                    cmb.setOwnerId(this.getId());
                    cmb.setId(id);
                    cmb.remove(null); // unlink permanenty !!!
                    iter.remove();
                } else {
                    System.err.println(".....bug.....");
                }
            else if (cm.isModified()) {
                cmb.setOwnerId(getId());
                cmb.setContactMedia(cm);
                cmb.store(con);
            }

        }

    }

    /**
     * get comment of link type PERSON$COMMENT_2_objectTypeID2 (most recent one)
     */
    public Comment getComment(Integer linkObjectTypeID)
            throws ServiceException {

        Comment c = null;

        if (comments != null) {
            c = (Comment) comments.get(linkObjectTypeID);
            if (c != null)
                return c;
        }

        int personID = this.getPersonID();

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            con = this.sqlHelper.getConnection();

            sql = con
                    .prepareStatement(
                            "SELECT c.*"
                                    + " FROM Link l, Comment c"
                                    + " WHERE"
                                    + " ( l.ObjectID1 = ? ) AND ( l.ObjectID2 = c.CommentID ) AND ( l.LinkObjectTypeID = ? )"
                                    + // personID, PERSON_2_COMMENT
                                    " AND ( LinkID IN"
                                    + " (SELECT ObjectID1"
                                    + " FROM Link"
                                    + " WHERE ( ObjectID2 IS NULL ) AND ( LinkObjectTypeID = ? )"
                                    + // linkObjectTypeID
                                    " )" + " )"
                                    + " AND ( LogicallyDeleted IS NULL )",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, personID);
            sql.setInt(2, PERSON_2_COMMENT);
            sql.setInt(3, linkObjectTypeID);
            rs = sql.executeQuery();

            CommentBean cb = null;
            if (rs.next()) {
                c = new Comment(this.getId());
                c.setId((Integer) rs.getObject("CommentID"));
                cb = new CommentBean(c);
                cb.load(rs);
                // if ( comments == null )
                // comments = new HashMap();
                // comments.put( linkObjectTypeID, c );
            }
            return c;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, sql);
                if (con != null)
                    sqlHelper.close(con);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }
        }

    }

    public void setComment(Integer linkObjectTypeID, Comment value)
            throws ServiceException {

        if (linkObjectTypeID == null || value == null)
            throw new ServiceException(
                    "linkObjectTypeID == null || value == null");

        if (comments == null)
            comments = new HashMap();

        // add/replace comment
        comments.put(linkObjectTypeID, value);

    }

    private void storeComments(Connection con) throws SQLException {

        if (comments == null || comments.size() == 0)
            return;

        CommentBean cb = new CommentBean();
        cb.setLinkObjectTypeID(1, PERSON_2_COMMENT);

        Iterator iter = comments.entrySet().iterator();
        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();
            Integer linkObjectTypeID = (Integer) entry.getKey();
            Comment c = (Comment) entry.getValue();

            cb.setLinkObjectTypeID(2, linkObjectTypeID);
            cb.setComment(c);
            cb.store(con);

        }

        // clear comments ( can be really big ! )
        // what if not commited ???
        // (client will get an exception and save again !!!)
        comments.clear();

    }

    /**
     * 
     */
    public Survey getSurvey(Integer surveyID) throws ServiceException {

        if (surveyID == null)
            return null;

        SurveyBean sb = new SurveyBean();

        sb.setOwnerId(getId());
        sb.setLinkObjectTypeID(1, PERSON_2_SURVEY);

        try {
            sb.load(surveyID, null);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //throw new ServiceException(e.getMessage());
            return null;
        }

        return sb.getSurvey();

    }

    public Integer getSurveyID(int surveyTypeID)
            throws ServiceException, ObjectNotFoundException {

        Connection con = null;
        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            con = this.sqlHelper.getConnection();

            // search for person specific surveys (already saved)
            sql = con.prepareStatement("SELECT ObjectID1 FROM Link"
                    + " WHERE ( ObjectID1 IN ( SELECT ObjectID2"
                    + " FROM Link"
                    + " WHERE ( ObjectID1=? ) AND ( LinkObjectTypeID=? )"
                    + " AND ( LogicallyDeleted IS NULL ) )"
                    + ") AND ( LinkObjectTypeID=? )"
                    + "  AND ( LogicallyDeleted IS NULL )"
                    + " ORDER BY ObjectID1 DESC");

            int i = 0;
            sql.setInt(++i, getPersonID());
            sql.setInt(++i, PERSON_2_SURVEY); // 1024
            sql.setInt(++i, surveyTypeID); // e.g. SURVEY_2_RISKPROFILE 24027
            rs = sql.executeQuery();

            if (!rs.next()) {
                close(rs, sql);
                rs = null;
                sql = null;

                // search for general/common surveys (not saved yet)
                sql = con.prepareStatement("SELECT ObjectID1" + " FROM Link"
                        + " WHERE ( LinkObjectTypeID=? )"
                        + " AND ( LogicallyDeleted IS NULL )"
                        + " ORDER BY ObjectID1 DESC");

                i = 0;
                sql.setInt(++i, surveyTypeID); // e.g. SURVEY_2_RISKPROFILE
                                                // 24027
                rs = sql.executeQuery();

                if (!rs.next())
                    throw new ObjectNotFoundException(
                            "Can not find survey for surveyTypeID: "
                                    + surveyTypeID);
            }

            // get latest survey (with max id)
            return (Integer) rs.getObject(1);

        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, sql);
                if (con != null)
                    sqlHelper.close(con);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }
        }

    }

    public void setSurvey(Survey survey) throws ServiceException {

        if (survey == null)
            throw new ServiceException("survey == null");

        if (surveys == null)
            surveys = new HashMap();

        // add/replace survey
        survey.setOwnerPrimaryKeyID(getId());
        surveys.put(survey.getId(), survey);

    }

    private void storeSurveys(Connection con) throws SQLException {

        if (surveys == null || surveys.size() == 0)
            return;

        SurveyBean sb = new SurveyBean();
        sb.setLinkObjectTypeID(1, PERSON_2_SURVEY);

        Iterator iter = surveys.entrySet().iterator();
        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();
            Integer surveyID = (Integer) entry.getKey();
            Survey s = (Survey) entry.getValue();

            sb.setSurvey(s);
            sb.store(con);

        }

        // clear surveys ( can be really big ! )
        // what if not commited ???
        // (client will get an exception and save again !!!)
        surveys.clear();

    }

    /***************************************************************************
     * Calculation Models
     */
    public ModelCollection getModels() throws ServiceException {

        // if ( models == null ) {
        models = new ModelCollection();

        Connection con = null;
        try {
            con = sqlHelper.getConnection();
            loadModels(con);
            sqlHelper.close(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
        // }

        return models;

    }

    public Vector getModels(Integer modelTypeID)
            throws ServiceException {
        getModels();
        return models == null ? null : models.getModels(modelTypeID);
    }

    public Model getModel(Integer modelTypeID, String title)
            throws ServiceException {
        if (title == null || title.trim().length() == 0)
            return null;

        Vector _models = getModels(modelTypeID);
        if (_models != null) {
            Iterator iter = _models.iterator();
            while (iter.hasNext()) {
                Model m = (Model) iter.next();
                if (title.equalsIgnoreCase(m.getTitle()))
                    return m;
            }

        }
        return null;
    }

    public Model getModel(Integer modelTypeID, Integer modelID)
            throws ServiceException {
        Vector _models = getModels(modelTypeID);
        if (_models != null) {
            Iterator iter = _models.iterator();
            while (iter.hasNext()) {
                Model m = (Model) iter.next();
                if (m.getId().equals(modelID))
                    return m;
            }

        }
        return null;
    }

    public void addModel(Model value) throws ServiceException {
        if (value == null || value.getTitle() == null)
            return;
        getModels().addModel(value);
    }

    public void removeModel(Model value) throws ServiceException {
        if (value == null)
            return;
        getModels();
        if (models != null)
            models.removeModel(value);
    }

    private void loadModels(Connection con) throws SQLException {
        if (getId() == null) {
            return;
        }
        
        List list = linkObjectDao.getLinkedObjects(getId(), PERSON_2_MODEL, con);

        if (list == null)
            return;

        ModelBean mb = null;

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Model m = new Model(getId());
            m.setId((Integer) iter.next());

            if (mb == null)
                mb = new ModelBean();

            mb.setModel(m);
            try {
                mb.load(con);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace(System.err);
                throw new SQLException(e.getMessage());
            }

            models.addModel(m);

        }

    }

    private void storeModels(Connection con) throws SQLException {
        if (models == null)
            return;

        ModelBean mb = null;

        Iterator iterModels = models.valuesIterator();
        while (iterModels.hasNext()) {
            Vector _models = (Vector) iterModels.next();

            Iterator iter = _models.iterator();
            while (iter.hasNext()) {
                Model model = (Model) iter.next();

                if (!model.isModified() || model.getTitle() == null)
                    continue;

                if (mb == null) {
                    mb = new ModelBean();
                    mb.setLinkObjectTypeID(1, PERSON_2_MODEL);
                }

                mb.setModel(model);
                mb.setOwnerId(getId());
                mb.store(con);
            }

            // com.argus.util.Collection.removeNullValues( _models );

        }

        Iterator iter2remove = models.getModelsToRemove().iterator();
        while (iter2remove.hasNext()) {
            Model model = (Model) iter2remove.next();

            linkObjectDao.unlink(getId(),
                    model.getId(), PERSON_2_MODEL, con);

        }
        models.getModelsToRemove().clear();

    }

    /***************************************************************************
     * 
     **************************************************************************/
    private String fromText(String data) {
        try {
            return new String(StringUtils.fromHexString(data),
                    IOUtils2.ENCODING_2_SERIALIZE);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    private String toText(String data) {
        try {
            return StringUtils.toHexString(data
                    .getBytes(IOUtils2.ENCODING_2_SERIALIZE));
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public Collection getPlans(Integer planTypeID)
            throws ServiceException {

        try {
            Connection con = sqlHelper.getConnection();
            Collection result = getPlans(planTypeID, con);
            sqlHelper.close(con);
            return result;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    private Integer getPlanOwner(Integer planTypeID) {
        if (planTypeID == null)
            return getId();
        if (BeanUtils.equals(TEMPLATE_PLAN, planTypeID)) // global template
            return null;
        if (BeanUtils.equals(TEMPLATE_PLAN_CLIENT, planTypeID)) // client owned template. NB not in use yet !!!
            return getId();
        if (BeanUtils.equals(TEMPLATE_PLAN_USER, planTypeID)) // user owned template. NB not in use yet !!!
            return getOwnerId();
        return null;
    }

    private java.util.Collection getPlans(Integer planTypeID, Connection con)
            throws java.sql.SQLException, ServiceException {
        Integer ownerID = getPlanOwner(planTypeID);
        List list = linkObjectDao.getLinkedObjects(
                ownerID == null ? GLOBAL_PLAN_TEMPLATE : ownerID,
                LinkObjectTypeConstant.PERSON_2_PLAN, con);
        if (list == null || list.size() == 0)
            return null;

        ReferenceCode plan;
        java.util.Collection plans = null;
        java.util.Iterator iter = list.iterator();

        ResultSet rs = null;
        PreparedStatement sql = con.prepareStatement(
                "SELECT PlanDataDesc, PlanDataText, PlanDataText2"
                        + " FROM PlanData"
                        + " WHERE ( PlanDataID=? )"
                        + (planTypeID == null ? " AND PlanTypeID IS NULL"
                                : " AND PlanTypeID=" + planTypeID),
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        try {
            while (iter.hasNext()) {
                Integer planID = (Integer) iter.next();

                try {
                    sql.setInt(1, planID);
                    rs = sql.executeQuery();

                    if (!rs.next())
                        continue;
                    // throw new SQLException( "Can not find Person PlanDataID:
                    // " + planID );

                    int i = 0;
                    if (plans == null)
                        plans = new java.util.ArrayList();

                    plan = new ReferenceCode(planID);
                    plan.setDescription(rs.getString(++i)); // PlanDataDesc

                    String planDataText = rs.getString(++i);
                    String planDataText2 = rs.getString(++i); // new hex
                                                                // presentation
                                                                // of text data
                    if (planDataText2 != null)
                        planDataText = fromText(planDataText2);

                    plan.setObject(IOUtils2.objectToProperties(planDataText)); // PlanDataText

                } finally {
                    close(rs, null);
                }

                plans.add(plan);

            }

        } finally {
            close(null, sql);
        }

        return plans;

    }

    public int storePlan(com.argus.util.ReferenceCode plan, Integer planTypeID)
            throws ServiceException {

        if (plan == null || plan.getDescription() == null)
            return -1;

        String desc = plan.getDescription().trim();
        if (desc.length() > 255) {
            desc = desc.substring(0, 255);
            plan.setDescription(desc);
        }

        try {
            Connection con = sqlHelper.getConnection();
            int result = storePlan(plan, planTypeID, con);
            sqlHelper.close(con);
            return result;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    private int storePlan(com.argus.util.ReferenceCode plan,
            Integer planTypeID, Connection con) throws java.sql.SQLException,
            ServiceException {
        PreparedStatement sql = null;
        int planID = plan.getId();

        Object data = plan.getObject();
        if (data instanceof java.util.Properties)
            data = IOUtils2.propertiesToString((java.util.Properties) data);

        try {
            if (planID <= 0) {

                // insert into Object table new PersonID
                planID = getNewObjectID(DbConstant.PLAN, con);

                // do insert into Person table
                sql = con
                        .prepareStatement("INSERT INTO PlanData"
                                + " (PlanDataID,PlanDataDesc,PlanDataText,PlanDataText2,PlanTypeID)"
                                + " VALUES" + " (?,?,?,?,?)");

                int i = 0;
                sql.setInt(++i, planID);
                sql.setString(++i, plan.getDescription());

                // sql.setString( ++i, "" + data );
                sql.setString(++i, "" + data); // set it to null later ???
                sql.setString(++i, toText("" + data));

                sql.setObject(++i, planTypeID, java.sql.Types.INTEGER);

                sql.executeUpdate();

                Integer ownerID = getPlanOwner(planTypeID);
                linkObjectDao.link(
                        ownerID == null ? GLOBAL_PLAN_TEMPLATE : ownerID
                                , planID, DbConstant.PERSON_2_PLAN,
                        con);

                plan.setId(planID);

            } else {

                // do update on PERSON table
                sql = con
                        .prepareStatement("UPDATE PlanData SET"
                                + " PlanDataDesc=?,PlanDataText=?,PlanDataText2=?,PlanTypeID=?"
                                + " WHERE PlanDataID=?");

                int i = 0;
                sql.setString(++i, plan.getDescription());

                // sql.setString( ++i, "" + data );
                sql.setString(++i, "" + data); // set it to null later ???
                sql.setString(++i, toText("" + data));

                sql.setObject(++i, planTypeID, java.sql.Types.INTEGER);
                sql.setInt(++i, planID);

                sql.executeUpdate();

            }

        } finally {
            close(null, sql);
        }

        return planID;

    }

    public boolean deletePlan(com.argus.util.ReferenceCode plan,
            Integer planTypeID) throws ServiceException {
        int planID = plan.getId();

        if (plan == null || planID <= 0)
            return false;

        try {
            Connection con = sqlHelper.getConnection();
            Integer ownerID = getPlanOwner(planTypeID);
            int linkID = linkObjectDao.unlink(
                    ownerID == null ? GLOBAL_PLAN_TEMPLATE : ownerID,
                    // getPersonID(),
                    planID, LinkObjectTypeConstant.PERSON_2_PLAN, con);
            sqlHelper.close(con);
            return linkID > 0;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    /***************************************************************************
     * 
     */
    public void load(Integer personID) throws SQLException,
            ObjectNotFoundException {

        Connection con = sqlHelper.getConnection();
        load(personID, con);
        sqlHelper.close(con);
    }

    public void load(Integer personID, Connection con) throws SQLException,
            ObjectNotFoundException {

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            String s = "SELECT p.*"
                + ", ph.PersonID AS ph_PersonID, ph.PersonHealthID, ph.IsSmoker, ph.HealthStateCodeID, ph.HospitalCover"
                + ", pts.PersonID AS pts_PersonID, pts.PersonTrustDIYStatusID, pts.TrustStatusCodeID, pts.DIYStatusCodeID, pts.CompanyStatusCodeID, pts.Comment"
                + ", po.PersonID AS po_PersonID, po.PersonOccupationID, po.JobDescription, po.EmploymentStatusCodeID, po.IndustryCodeID, po.OccupationCodeID"
                + " FROM Person p LEFT OUTER JOIN"
                + " PersonHealth ph ON (p.PersonID = ph.PersonID) LEFT OUTER JOIN"
                + " PersonTrustDIYStatus pts ON (p.PersonID = pts.PersonID) LEFT OUTER JOIN"
                + " PersonOccupation po ON (p.PersonID = po.PersonID)"
                + " WHERE (p.PersonID = ?)"
                + " AND (ph.NextID IS NULL)"
                + " AND (pts.NextID IS NULL)"
                + " AND (po.NextID IS NULL)";
                
            sql = con.prepareStatement(
                s,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

            sql.setInt(1, personID);
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Person ID: " + personID);

            setId(personID);

            load(rs);
            close(rs, sql);
            rs = null;
            sql = null;

            loadAddresses(con);

        } finally {
            close(rs, sql);
        }

    }

    public void load(ResultSet rs) throws SQLException {
        int personId = rs.getInt("PersonID");

        load(rs, person);

        Integer dobCountryId = (Integer) rs.getObject("DOBCountryID");
        person.setDobCountry(dobCountryId == null ? null : entityDao.findCountry(dobCountryId));
        person.setTaxFileNumber(rs.getString("TaxFileNumber"));
        String dssRecipient = rs.getString("DSSRecipient");
        person.setDssRecipient(dssRecipient != null && BooleanCode.rcYES.getCode().compareToIgnoreCase(dssRecipient) == 0);
        person.setPreferredLanguage(new Language((Integer) rs.getObject("PreferredLanguageID"), rs.getString("PreferredLanguage"), null));
        person.setResidenceCountry(new Country((Integer) rs.getObject("ResidenceCountryCodeID"), null, null));
        person.setResidenceStatusCodeId((Integer) rs.getObject("ResidenceStatusCodeID"));
        person.setReferalSourceCodeId((Integer) rs.getObject("ReferalSourceCodeID"));
        setDateCreated(rs.getDate("DateCreated"));
        setDateModified(rs.getDate("DateModified"));
        if (rs.getInt("ph_PersonID") > 0) {
            IPersonHealth personHealth = new PersonHealth();
            load(personHealth, rs);
            person.setPersonHealth(personHealth);
        }
        if (rs.getInt("pts_PersonID") > 0) {
            IPersonTrustDIYStatus personTrustDIYStatus = new PersonTrustDIYStatus();
            load(personTrustDIYStatus, rs);
            person.setPersonTrustDIYStatus(personTrustDIYStatus);
        }
        if (rs.getInt("po_PersonID") > 0) {
            IOccupation occupation = person.getOccupation();
            if (occupation == null) {
                occupation = new Occupation(personId);
            }
            occupationDao.load(occupation, rs);
        }
    }

    public void load(ResultSet rs, IPerson personName) throws SQLException {
        Integer sexCodeId = (Integer) rs.getObject("SexCodeID");
        personName.setSex(sexCodeId == null ? null : entityDao.findSexCode(sexCodeId));
        Integer titleCodeId = (Integer) rs.getObject("TitleCodeID");
        personName.setTitle(titleCodeId == null ? null : entityDao.findTitleCode(titleCodeId));
        Integer maritalCodeId = (Integer) rs.getObject("MaritalCodeID");
        personName.setMarital(maritalCodeId == null ? null : entityDao.findMaritalCode(maritalCodeId));
        personName.setSurname(rs.getString("FamilyName"));
        personName.setFirstname(rs.getString("FirstName"));
        personName.setOtherNames(rs.getString("OtherGivenNames"));
        personName.setPreferredName(rs.getString("PreferredName"));
        personName.setDateOfBirth(rs.getDate("DateOfBirth"));
        personName.setModified(false);
    }

    public int storePerson(Connection con) throws SQLException {
        Integer personId = getId();
        if (personId == null) {
            try {
                personId = create(con);
            } catch (Exception e) {
                throw new SQLException(e);
            }
            setId(personId);
        }

        PreparedStatement sql = con.prepareStatement(
            "UPDATE person SET"
                    + " SexCodeID=?, TitleCodeID=?, MaritalCodeID=?"
                    + ", FamilyName=?, FirstName=?, OtherGivenNames=?, PreferredName=?, DateOfBirth=?"
                    + ", DOBCountryID=?, TaxFileNumber=?, DSSRecipient=?, PreferredLanguageID=?, PreferredLanguage=?"
                    + ", ReferalSourceCodeID=?, ResidenceStatusCodeID=?, ResidenceCountryCodeID=?"
                    + ", DateModified=getDate()"
                    + " WHERE personID=?",
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        int i = 0;
        if (person == null) {
            sql.setNull(++i, java.sql.Types.INTEGER);
            sql.setNull(++i, java.sql.Types.INTEGER);
            sql.setNull(++i, java.sql.Types.INTEGER);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
        } else {
            final ISexCode sex = person.getSex();
            sql.setObject(++i, sex == null ? null : sex.getId());
            final ITitleCode title = person.getTitle();
            sql.setObject(++i, title == null ? null : title.getId());
            final IMaritalCode marital = person.getMarital();
            sql.setObject(++i, marital == null ? null : marital.getId());
            sql.setString(++i, person.getSurname());
            sql.setString(++i, person.getFirstname());
            sql.setString(++i, person.getOtherNames());
            sql.setString(++i, person.getPreferredName());

            // javax.ejb.EJBException: [Microsoft][ODBC SQL Server
            // Driver]Optional feature not implemented
            // sql.setDate( ++i, personName.getDateOfBirth() );
            if (person.getDateOfBirth() == null)
                sql.setNull(++i, java.sql.Types.VARCHAR);
            else
                sql.setString(++i, DateTimeUtils.getJdbcDate(person.getDateOfBirth()));
        }

        sql.setObject(++i, person.getDobCountry() == null ? null : person.getDobCountry().getId());
        sql.setString(++i, person.getTaxFileNumber());
        sql.setString(++i, person.isDssRecipient() ? BooleanCode.rcYES.getCode() : BooleanCode.rcNO.getCode());
        ILanguage preferredLanguage = person.getPreferredLanguage();
        sql.setObject(++i, preferredLanguage == null ? null : preferredLanguage.getId());
        sql.setString(++i, preferredLanguage == null ? null : preferredLanguage.getCode());
        sql.setObject(++i, person.getReferalSourceCodeId());
        sql.setObject(++i, person.getResidenceStatusCodeId());
        sql.setObject(++i, person.getResidenceCountry() == null ? null : person.getResidenceCountry().getId());
        sql.setObject(++i, personId, java.sql.Types.INTEGER);
        sql.executeUpdate();
        close(null, sql);

        IPersonHealth personHealth = person.getPersonHealth();
        if (personHealth != null) {
            store(getId(), personHealth, con);
        }
        IPersonTrustDIYStatus personTrustDIYStatus = person.getPersonTrustDIYStatus();
        if (personTrustDIYStatus != null) {
            store(getId(), personTrustDIYStatus, con);
        }
        IOccupation occupation = person.getOccupation();
        if (occupation != null) {
            occupation.setOwnerId(getId());
            occupationDao.store(occupation, con);
        }
        storeAddresses(con);
        storeContactMedia(con);
        storeDependents(con);
        storeContacts(con);
        storeEmployerBusiness(con);
        return personId;
    }

    private void load(IPersonHealth personHealth, ResultSet rs) throws SQLException {
        personHealth.setId((Integer) rs.getObject("PersonHealthID"));
        String s = rs.getString("IsSmoker");
        personHealth.setSmoker(BooleanCode.rcYES.getCode().compareToIgnoreCase(s) == 0);
        personHealth.setHealthStateCodeId((Integer) rs.getObject("HealthStateCodeID"));
        s = rs.getString("HospitalCover");
        personHealth.setHospitalCover(BooleanCode.rcYES.getCode().compareToIgnoreCase(s) == 0);
    }

    private int store(Integer personId, IPersonHealth personHealth, Connection con) throws SQLException {
        /**
         * insert new if data changed (update current) PERSON XXX information
         */
        if (personHealth.getId() != null) {
            return getId();
        }

        PreparedStatement sql = con.prepareStatement(
            "INSERT INTO PersonHealth (PersonID, IsSmoker, HealthStateCodeID, HospitalCover) VALUES (?, ?, ?, ?)");
        sql.setInt(1, personId);
        sql.setString(2, BooleanCode.convert(personHealth.isSmoker()).getCode());
        sql.setObject(3, personHealth.getHealthStateCodeId(), java.sql.Types.INTEGER);
        sql.setString(4, BooleanCode.convert(personHealth.isHospitalCover()).getCode());
        sql.executeUpdate();
        int newID = getIdentityID(con);
        if (personHealth.getId() != null) {
            sql = con.prepareStatement("UPDATE PersonHealth SET NextID=? WHERE PersonHealthID=?");
            sql.setInt(1, newID);
            sql.setInt(2, personHealth.getId());
            sql.executeUpdate();
        }
        personHealth.setId(newID);
        return newID;
    }

    /**
     * 
     */
    public void load(IPersonTrustDIYStatus personTrustDIYStatus, ResultSet rs) throws SQLException {
        personTrustDIYStatus.setId((Integer) rs.getObject("PersonTrustDIYStatusID"));
        personTrustDIYStatus.setTrustStatusCodeId((Integer) rs.getObject("TrustStatusCodeID"));
        personTrustDIYStatus.setDIYStatusCodeId((Integer) rs.getObject("DIYStatusCodeID"));
        personTrustDIYStatus.setCompanyStatusCodeId((Integer) rs.getObject("CompanyStatusCodeID"));
        String comment = rs.getString("Comment");
        if (comment != null) {
            personTrustDIYStatus.setComment(comment.trim());
        }
    }

    public int store(Integer personId, IPersonTrustDIYStatus personTrustDIYStatus, Connection con) throws SQLException {
        /**
         * insert new if data changed (update current) PERSON XXX information
         */
        if (personTrustDIYStatus.getId() != null) {
            return personTrustDIYStatus.getId();
        }

        PreparedStatement sql = con.prepareStatement(
            "INSERT INTO PersonTrustDIYStatus (PersonID, TrustStatusCodeID, DIYStatusCodeID, CompanyStatusCodeID, Comment) VALUES (?, ?, ?, ?, ?)");
        int i = 0;
        sql.setInt(++i, personId);
        sql.setObject(++i, personTrustDIYStatus.getTrustStatusCodeId(), java.sql.Types.INTEGER);
        sql.setObject(++i, personTrustDIYStatus.getDIYStatusCodeId(), java.sql.Types.INTEGER);
        sql.setObject(++i, personTrustDIYStatus.getCompanyStatusCodeId(), java.sql.Types.INTEGER);
        sql.setString(++i, personTrustDIYStatus.getComment());
        sql.executeUpdate();
        int newID = getIdentityID(con);
        if (getId() != null) {
            sql = con.prepareStatement("UPDATE PersonTrustDIYStatus SET NextID=? WHERE PersonTrustDIYStatusID=?");
            sql.setInt(1, newID);
            sql.setInt(2, personTrustDIYStatus.getId());
            sql.executeUpdate();
        }
        personTrustDIYStatus.setId(newID);
        return newID;
    }

}