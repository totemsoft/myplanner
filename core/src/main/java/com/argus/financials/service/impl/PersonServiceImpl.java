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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.argus.financials.bean.Assets;
import com.argus.financials.bean.DbConstant;
import com.argus.financials.bean.Financial;
import com.argus.financials.bean.FinancialGoal;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.db.FPSLinkObject;
import com.argus.financials.bean.db.FinancialBean;
import com.argus.financials.bean.db.FinancialGoalBean;
import com.argus.financials.code.AddressCode;
import com.argus.financials.code.BaseCodeComparator;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.etc.Address;
import com.argus.financials.etc.Comment;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.etc.Dependent;
import com.argus.financials.etc.Occupation;
import com.argus.financials.etc.PersonName;
import com.argus.financials.etc.Survey;
import com.argus.financials.etc.db.AddressBean;
import com.argus.financials.etc.db.CommentBean;
import com.argus.financials.etc.db.ContactBean;
import com.argus.financials.etc.db.ContactMediaBean;
import com.argus.financials.etc.db.DependentBean;
import com.argus.financials.etc.db.OccupationBean;
import com.argus.financials.etc.db.PersonHealth;
import com.argus.financials.etc.db.PersonNameAddressBean;
import com.argus.financials.etc.db.PersonTrustDIYStatus;
import com.argus.financials.etc.db.SurveyBean;
import com.argus.financials.io.IOUtils2;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.projection.save.db.ModelBean;
import com.argus.financials.security.UserPreferences;
import com.argus.financials.service.BusinessService;
import com.argus.financials.service.CreateException;
import com.argus.financials.service.FinderException;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.client.ObjectNotFoundException;
import com.argus.financials.service.client.ServiceException;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;
import com.argus.util.StringUtils;

public class PersonServiceImpl extends AbstractServiceImpl implements PersonService {

    @Autowired
    private BeanFactory beanFactory;

    /**
     * @return the userPreferences
     */
    protected UserPreferences getUserPreferences() {
        return (UserPreferences) beanFactory.getBean("userPreferences");
    }

    /**
     * to create a new instance of Person entity bean and therefore insert data
     * into database invoke create(...) method
     */
    public Integer persist(Integer ownerPersonID)
            throws ServiceException {
        try {
            PersonServiceImpl pb = new PersonServiceImpl();
            pb.setOwnerPrimaryKeyID(ownerPersonID);
            Connection con = getConnection();
            Integer personID = pb.create(con);
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
    protected Integer create(Connection con) throws ServiceException,
            CreateException {

        PreparedStatement sql = null;

        try {
            // get new ObjectID for person
            Integer personID = new Integer(getNewObjectID(PERSON, con));

            // add to Person table
            sql = con
                    .prepareStatement("INSERT INTO Person (PersonID) VALUES (?)");
            sql.setInt(1, personID.intValue());
            sql.executeUpdate();

            return personID;

        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new CreateException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(null, sql);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }
        }

    }

    public PersonService findByPrimaryKey(Integer personID, boolean store) throws ServiceException,
        FinderException {
        PersonServiceImpl person = new PersonServiceImpl();
        person.findByPrimaryKey(personID);
        return person;
    }

    protected int getPersonID() {
        Integer id = (Integer) getPrimaryKey();
        if (id != null)
            return id.intValue();
        return 0;
    }

    /**
     * 
     */
    public void storePerson() throws ServiceException {
        try {
            Connection con = getConnection();
            storePerson(con);
            if (financialGoal != null)
                storeFinancialGoal(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void storeFinancials() throws ServiceException {
        if (financials == null && financialGoal == null)
            return;
        try {
            Connection con = getConnection();
            if (financials != null)
                storeFinancials(con); // first
            if (financialGoal != null)
                storeFinancialGoal(con); // second (will use financial data)
            setFinancialSaved();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeComments() throws ServiceException {
        if (comments == null)
            return;
        try {
            Connection con = getConnection();
            storeComments(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeSurveys() throws ServiceException {
        if (surveys == null)
            return;
        try {
            Connection con = getConnection();
            storeSurveys(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeModels() throws ServiceException {
        if (models == null)
            return;
        try {
            Connection con = getConnection();
            storeModels(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }

    public void storeEmployerBusiness() throws ServiceException {
        if (employerBusiness == null)
            return;
        try {
            Connection con = getConnection();
            storeEmployerBusiness(con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }
    }
    public Object getOwnerPrimaryKey() throws ServiceException {
        return getOwnerPrimaryKeyID();
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

        PersonServiceImpl pb = new PersonServiceImpl();

        if (personID == null) {
            // create new partner ( will be saved and properly linked in
            // storePerson() )
            pb.setOwnerPrimaryKeyID(getPrimaryKeyID());
        } else {
            // load existing partner
            try {
                pb.findByPrimaryKey(personID);
            } catch (FinderException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }
        }

        PersonName pn = pb.getPersonName();
        if (pn == null)
            pb.setPersonName(new PersonName());

        return pb;

    }

    // PERSON SPECIFIC MEMBERS
    private PersonName ownerName;

    protected PersonName personName = new PersonName();

    protected Integer dobCountryID;

    protected String taxFileNumber;

    protected boolean isDSSRecipient;

    protected Integer preferredLanguageID;

    protected String preferredLanguage;

    protected Integer referalSourceCodeID;

    protected Integer residenceStatusCodeID;

    protected Integer residenceCountryCodeID;

    private PersonHealth personHealth;

    private PersonTrustDIYStatus personTrustDIYStatus;

    private Occupation occupation;

    private BusinessServiceImpl employerBusiness;

    private Address residentialAddress;

    private Address postalAddress;

    private TreeMap dependents;

    private TreeMap contacts;

    /**
     * Map( ObjectTypeID, Map( ObjectID, Object ) )
     */
    private Map financials;

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
        return super.isModified() || personHealth.isModified()
                || personTrustDIYStatus.isModified() || occupation.isModified()
                || employerBusiness.isModified()
                || residentialAddress.isModified()
                || postalAddress.isModified();
    }

    public void setModified(boolean value) {
        super.setModified(value);

        if (personHealth != null)
            personHealth.setModified(value);
        if (personTrustDIYStatus != null)
            personTrustDIYStatus.setModified(value);
        if (occupation != null)
            occupation.setModified(value);
        if (employerBusiness != null)
            employerBusiness.setModified(value);
        if (residentialAddress != null)
            residentialAddress.setModified(value);
        if (postalAddress != null)
            postalAddress.setModified(value);
    }

    public PersonName getOwnerName() {
        if (ownerName == null) {
            try {
                ownerName = new PersonName();
                PersonNameAddressBean pnab = new PersonNameAddressBean();
                pnab.setPersonName(ownerName);
                pnab.setPrimaryKeyID(getOwnerPrimaryKeyID());

                Connection con = getConnection();
                pnab.load(getOwnerPrimaryKeyID(), con);
            } catch (Exception e) {
                ownerName = null;
                e.printStackTrace(System.err);
                // throw new ServiceException( e.getMessage() );
            }

        }

        return ownerName;

    }

    public PersonName getPersonName() throws ServiceException {
        return personName;
    }

    public void setPersonName(PersonName value) throws ServiceException {
        if (equals(personName, value))
            return;

        personName = value;
        super.setModified(true);
    }

    public Integer getDobCountryID() throws ServiceException {
        return dobCountryID;
    }

    public void setDobCountryID(Integer value) throws ServiceException {
        if (equals(dobCountryID, value))
            return;

        dobCountryID = value;
        super.setModified(true);
    }

    public String getTaxFileNumber() throws ServiceException {
        return taxFileNumber;
    }

    public void setTaxFileNumber(String value) throws ServiceException {
        if (equals(taxFileNumber, value))
            return;

        taxFileNumber = value;
        super.setModified(true);
    }

    public boolean isDSSRecipient() throws ServiceException {
        return isDSSRecipient;

    }

    public void setDSSRecipient(boolean value) throws ServiceException {
        if (value == isDSSRecipient)
            return;

        isDSSRecipient = value;
        super.setModified(true);
    }

    public Integer getPreferredLanguageID() throws ServiceException {
        return preferredLanguageID;
    }

    public void setPreferredLanguageID(Integer value)
            throws ServiceException {
        if (equals(preferredLanguageID, value))
            return;

        preferredLanguageID = value;
        super.setModified(true);
    }

    public String getPreferredLanguage() throws ServiceException {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String value)
            throws ServiceException {
        if (equals(preferredLanguage, value))
            return;

        preferredLanguage = value;
        super.setModified(true);
    }

    public Integer getReferalSourceCodeID() throws ServiceException {
        return referalSourceCodeID;
    }

    public void setReferalSourceCodeID(Integer value)
            throws ServiceException {
        if (equals(referalSourceCodeID, value))
            return;

        referalSourceCodeID = value;
        super.setModified(true);
    }

    public Integer getResidenceStatusCodeID() throws ServiceException {
        return residenceStatusCodeID;
    }

    public void setResidenceStatusCodeID(Integer value)
            throws ServiceException {
        if (equals(residenceStatusCodeID, value))
            return;

        residenceStatusCodeID = value;
        super.setModified(true);
    }

    public Integer getResidenceCountryCodeID() throws ServiceException {
        return residenceCountryCodeID;
    }

    public void setResidenceCountryCodeID(Integer value)
            throws ServiceException {
        if (equals(residenceCountryCodeID, value))
            return;

        residenceCountryCodeID = value;
        super.setModified(true);
    }

    public java.util.Date getDateOfBirth() throws ServiceException {
        return getPersonName().getDateOfBirth();
    }

    public void setDateOfBirth(java.util.Date value)
            throws ServiceException {
        getPersonName().setDateOfBirth(value);
    }

    /**
     * 
     */
    public PersonHealth getPersonHealth() {
        if (personHealth == null)
            personHealth = new PersonHealth();
        return personHealth;
    }

    public Boolean getIsSmoker() throws ServiceException {
        return personHealth == null ? null : getPersonHealth().getIsSmoker();
    }

    public void setIsSmoker(Boolean value) throws ServiceException {
        getPersonHealth().setIsSmoker(value);
    }

    public Integer getHealthStateCodeID() throws ServiceException {
        return personHealth == null ? null : getPersonHealth()
                .getHealthStateCodeID();
    }

    public void setHealthStateCodeID(Integer value)
            throws ServiceException {
        getPersonHealth().setHealthStateCodeID(value);
    }

    public boolean hasHospitalCover() throws ServiceException {
        return getPersonHealth().hasHospitalCover();
    }

    public void hasHospitalCover(boolean value) throws ServiceException {
        getPersonHealth().hasHospitalCover(value);
    }

    /**
     * 
     */
    public PersonTrustDIYStatus getTrustStatus() {
        if (personTrustDIYStatus == null)
            personTrustDIYStatus = new PersonTrustDIYStatus();
        return personTrustDIYStatus;
    }

    public Integer getTrustStatusCodeID() throws ServiceException {
        if (personTrustDIYStatus == null)
            return null;
        return getTrustStatus().getTrustStatusCodeID();
    }

    public void setTrustStatusCodeID(Integer value)
            throws ServiceException {
        getTrustStatus().setTrustStatusCodeID(value);
    }

    public Integer getDIYStatusCodeID() throws ServiceException {
        return personTrustDIYStatus == null ? null : getTrustStatus()
                .getDIYStatusCodeID();
    }

    public void setDIYStatusCodeID(Integer value)
            throws ServiceException {
        getTrustStatus().setDIYStatusCodeID(value);
    }

    public Integer getCompanyStatusCodeID() throws ServiceException {
        return personTrustDIYStatus == null ? null : getTrustStatus()
                .getCompanyStatusCodeID();
    }

    public void setCompanyStatusCodeID(Integer value)
            throws ServiceException {
        getTrustStatus().setCompanyStatusCodeID(value);
    }

    public String getTrustDIYStatusComment() throws ServiceException {
        return personTrustDIYStatus == null ? null : getTrustStatus()
                .getComment();
    }

    public void setTrustDIYStatusComment(String value)
            throws ServiceException {
        getTrustStatus().setComment(value);
    }

    /**
     * 
     */
    public Occupation getOccupation() throws ServiceException {
        return occupation;
    }

    public void setOccupation(Occupation value) throws ServiceException {
        if (equals(occupation, value))
            return;

        if (occupation == null)
            occupation = value;
        else
            occupation.assign(value);
    }

    /**
     * 
     */
    public BusinessService getEmployerBusiness() throws ServiceException {

        if (employerBusiness == null) {
            Connection con = null;

            try {
                con = getConnection();

                List list = getLinkedObjects(PERSON_2_BUSINESS, 0,
                        PERSON$BUSINESS_2_OCCUPATION, con);

                employerBusiness = new BusinessServiceImpl();
                if (list != null)
                    employerBusiness.load((Integer) list.get(0), con);

            } catch (SQLException e) {
                employerBusiness = null;
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());

            } catch (ObjectNotFoundException e) {
                employerBusiness = null;
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }

        }

        return employerBusiness;

    }

    public void storeEmployerBusiness(Connection con) throws SQLException {

        if (employerBusiness == null || !employerBusiness.isModified())
            return;

        Integer businessID = employerBusiness.getPrimaryKeyID();
        employerBusiness.store(con);

        // existing business object
        if (businessID != null)
            return;

        // new business object just created, now it has to be linked to this
        // person
        FPSLinkObject.getInstance().link(
                FPSLinkObject.getInstance().link(getPrimaryKeyID(),
                        employerBusiness.getPrimaryKeyID(), PERSON_2_BUSINESS,
                        con), 0, PERSON$BUSINESS_2_OCCUPATION, con);

    }

    /**
     * 
     */
    public Address getResidentialAddress() throws ServiceException {
        if (residentialAddress == null) {
            residentialAddress = new Address(getPersonID());
            residentialAddress.setAddressCodeID(AddressCode.RESIDENTIAL);
        }
        return residentialAddress;
    }

    public void setResidentialAddress(Address value)
            throws ServiceException {
        if (equals(residentialAddress, value))
            return;

        if (residentialAddress == null)
            residentialAddress = value;
        else
            residentialAddress.assign(value);
    }

    public Address getPostalAddress() throws ServiceException {
        if (postalAddress == null) {
            postalAddress = new Address(getPersonID());
            postalAddress.setAddressCodeID(AddressCode.POSTAL);
        }
        return postalAddress;
    }

    public void setPostalAddress(Address value) throws ServiceException {
        if (equals(postalAddress, value))
            return;

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
                ab.setAddress(new Address(personID));

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
                residentialAddress.setOwnerPrimaryKeyID(getPrimaryKeyID());
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
                postalAddress.setOwnerPrimaryKeyID(getPrimaryKeyID());
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
    public Map getFinancials(Integer objectTypeID)
            throws ServiceException {
        if (financials == null)
            financials = getFinancials();
        return getFinancials(financials, objectTypeID);
    }

    public Map getFinancials(java.util.Map financialMap, Integer objectTypeID)
            throws ServiceException {
        if (objectTypeID == null)
            return financialMap;
        return financialMap == null ? null : (Map) financialMap
                .get(objectTypeID);
    }

    public Financial getFinancial(Integer financialID)
            throws ServiceException {
        if (financials == null)
            financials = getFinancials();
        return getFinancial(financials, financialID);
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
    public java.util.Map getFinancials() throws ServiceException {

        if (financials == null) {

            Connection con = null;
            try {
                con = this.getConnection();
                financials = getFinancials(con, null);

            } catch (SQLException e) {
                e.printStackTrace(System.err);
                throw new ServiceException(e.getMessage());
            }

        }

        return financials;
    }

    public java.util.Map getStrategyGroupFinancials(Integer strategyGroupID,
            boolean complex) throws ServiceException {

        Connection con = null;
        try {
            con = getConnection();
            return getStrategyGroupFinancials(con, strategyGroupID, complex);

        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    public java.util.Map getStrategyGroupFinancials(Connection con,
            Integer strategyGroupID, boolean complex)
            throws java.sql.SQLException {

        java.util.Map financialMap = getFinancials(con, strategyGroupID);

        if (complex || financialMap == null)
            return financialMap;

        java.util.Map generatedFinancials = new java.util.TreeMap();// new
                                                                    // BaseCodeComparator()
                                                                    // );
        java.util.Iterator iter = financialMap.values().iterator();
        while (iter.hasNext())
            generatedFinancials.putAll((java.util.Map) iter.next());

        return generatedFinancials;

    }

    public java.util.Map getFinancials(Connection con, Integer strategyGroupID)
            throws java.sql.SQLException {

        if (financials != null && strategyGroupID == null)
            return financials;

        java.util.Map financialMap = null;

        Integer personID = getPrimaryKeyID();

        PreparedStatement sql = null;
        ResultSet rs = null;

        try {
            // get object id's for ALL person financial
            try {
                int i = 0;
                sql = con
                        .prepareStatement(
                                "SELECT FinancialID, ObjectID, ObjectTypeID"
                                        + " FROM Financial, Object"
                                        + " WHERE ( FinancialID IN"
                                        + " (SELECT ObjectID2"
                                        + " FROM Link"
                                        + " WHERE ( ObjectID1 = ? ) AND ( LinkObjectTypeID = ? )"
                                        + " AND ( LogicallyDeleted IS NULL )"
                                        + " )"
                                        + " ) AND (NextID IS NULL)"
                                        + (strategyGroupID == null ? " AND (StrategyGroupID IS NULL)"
                                                : " AND (StrategyGroupID = ?)")
                                        + " AND (FinancialID = ObjectID)",
                                ResultSet.TYPE_FORWARD_ONLY,
                                ResultSet.CONCUR_READ_ONLY);

                sql.setInt(++i, personID.intValue());
                sql.setInt(++i, PERSON_2_FINANCIAL); // 1004
                if (strategyGroupID != null)
                    sql.setInt(++i, strategyGroupID.intValue());

                rs = sql.executeQuery();

                // save id, typeId ONLY
                while (rs.next()) {
                    if (financialMap == null)
                        financialMap = new TreeMap(new BaseCodeComparator());

                    Integer id = new Integer(rs.getInt("FinancialID"));
                    Integer typeId = new Integer(rs.getInt("ObjectTypeID"));

                    // get/create map for this object type
                    Map map = (Map) financialMap.get(typeId);
                    if (map == null) {
                        // map = new TreeMap();
                        map = new TreeMap(new BaseCodeComparator());
                        financialMap.put(typeId, map);
                    }
                    map.put(id, null); // load Financial object later (on
                                        // request)

                }

                if (financialMap == null)
                    return null;

            } finally {
                close(rs, sql);
                rs = null;
                sql = null;
            }

            // create ALL objects for ALL object types
            // init null Financial objects (if any) for this object type
            Iterator iter = financialMap.keySet().iterator();
            while (iter.hasNext())
                initFinancials(financialMap, (Integer) iter.next(), con);

        } finally {
            close(rs, sql);
        }

        // Map( ObjectTypeID, Map( ObjectID, Object ) )
        return financialMap;

    }

    private void initFinancials(java.util.Map financialMap,
            Integer objectTypeID, Connection con) throws SQLException {

        if (financialMap == null)
            return;

        // create ONLY this object id
        Map map = (Map) financialMap.get(objectTypeID);
        if (map == null)
            return;

        // create dummy FinancialBean object (derived ones - of course)
        // will be used to initialize ALL Financial objects of this type
        FinancialBean fb = null;
        Integer personID = getPrimaryKeyID();

        // iterate through all objects of this type
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            Integer id = (Integer) entry.getKey();
            Financial f = (Financial) entry.getValue();

            if (f != null)
                continue; // already initialized (id < 0 means not saved to db
                            // yet)

            if (fb == null)
                fb = (FinancialBean) createNewInstance(objectTypeID);

            // create Financial object (owner - personID)
            f = fb.getNewFinancial();
            f.setOwnerPrimaryKeyID(personID);

            // set and initialize Financial objects using FinancialBean object
            fb.setFinancial(f);
            try {
                fb.load(id, con);
            } // id will be > 0 allways
            catch (ObjectNotFoundException e) {
                e.printStackTrace(System.err);
                throw new SQLException(e.getMessage());
            }

            entry.setValue(f);

        }

        if (fb != null)
            fb.setFinancial(null);

    }

    // Map( ObjectTypeID, Map( ObjectID, Object ) )
    // do not save, to save call store()
    protected void setFinancials(Map value) {
        financials = value;
    }

    public void setFinancials(Integer objectTypeID, Map value)
            throws ServiceException {
        if (objectTypeID == null)
            financials = value;
        else
            financials.put(objectTypeID, value);

    }

    protected void storeFinancials(Connection con) throws SQLException {

        if (financials == null)
            return;

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
                    fb = (FinancialBean) createNewInstance(objectTypeID);
                // if ( fb == null )
                // continue; // wrong objectTypeID (e.g. SurplusItem)

                if (f == null) {
                    // remove
                    if (objID.intValue() > 0) { // this removed on client
                        fb.setOwnerPrimaryKeyID(this.getPrimaryKeyID());

                        fb.setPrimaryKeyID(objID);
                        fb.remove(con); // delete object
                    } else {
                        ;// System.err.println(
                            // "PersonServiceImpl::storeFinancials(con) .....bug....."
                            // );
                    }

                } else {
                    if (!f.isGenerated()) {
                        f.setOwnerPrimaryKeyID(getPrimaryKeyID());
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

    }

    // mark all financials as saved
    private void setFinancialSaved() {

        if (financials == null)
            return;

        // iterate through all financial records of this type
        // and close them, call remove(null)
        Iterator iter = financials.values().iterator();
        while (iter.hasNext()) {
            Map map = (Map) iter.next();

            Iterator iter2 = map.values().iterator();
            while (iter2.hasNext()) {
                Financial f = (Financial) iter2.next();
                if (f != null)
                    f.setModified(false);
            }

        }

    }

    // delete all person current financials
    protected void deleteFinancials(Connection con) throws SQLException {

        if (financials == null)
            return;

        // *
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

        storeFinancials(con);
        // */

        /*
         * // all together in batch (better) CallableStatement cs =
         * con.prepareCall( "{call sp_delete_financials(?)}" ); try { cs.setInt(
         * 1, getPrimaryKeyID().intValue() ); cs.execute();
         * 
         * //return cs.getInt(1) == 0; // 0 (success) or 1 (failure)
         *  } finally { cs.close(); }
         */
        // remove all current assets from collection
        Assets.getCurrentAssets().initCodes(null);

        financials = null;

    }

    /**
     * get/set professional contacts
     */
    public java.util.TreeMap getContacts() throws ServiceException {

        if (contacts == null) {

            Connection con = null;
            List list = null;

            try {
                con = getConnection();

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
                    c.setOwnerPrimaryKeyID(getPrimaryKeyID());
                    c.setPrimaryKeyID(personID);

                    new ContactBean(c).load(personID, con);

                    contacts.put(personID, c);
                }

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
                c.setOwnerPrimaryKeyID(getPrimaryKeyID());
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
                if (id.intValue() > 0) { // this removed on client
                    c = new Contact();
                    ContactBean cb = new ContactBean(c);
                    cb.setObjectTypeID(CONTACT);

                    c.setOwnerPrimaryKeyID(getPrimaryKeyID());
                    c.setPrimaryKeyID(id);
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
                con = getConnection();

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
                    d.setOwnerPrimaryKeyID(getPrimaryKeyID());
                    d.setPrimaryKeyID(personID);

                    new DependentBean(d).load(personID, con);

                    dependents.put(personID, d);
                }

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
                d.setOwnerPrimaryKeyID(getPrimaryKeyID());
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
                if (id.intValue() > 0) { // this removed on client
                    DependentBean db = new DependentBean(new Dependent());
                    db.setOwnerPrimaryKeyID(getPrimaryKeyID());
                    db.setPrimaryKeyID(id);
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
            con = this.getConnection();

            List list = FPSLinkObject.getInstance().getLinkedObjects(personID,
                    PERSON_2_FINANCIALGOAL, con);

            if (list == null || list.size() == 0) {
                financialGoal = null;

            } else {

                if (financialGoal == null)
                    financialGoal = new FinancialGoal();

                financialGoal.setOwnerPrimaryKeyID(this.getPrimaryKeyID());
                financialGoal.setPrimaryKeyID((Integer) list.get(0));

                new FinancialGoalBean(financialGoal).load(con);

            }

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
        fgb.setOwnerPrimaryKeyID(this.getPrimaryKeyID());

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
            con = this.getConnection();

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

                contactMediaDetails.put(cmb.getContactMediaCodeID(), cmb
                        .getContactMedia());

            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        } finally {
            try {
                close(rs, sql);
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
                if (id.intValue() > 0) { // this removed on client
                    cmb.setOwnerPrimaryKeyID(this.getPrimaryKeyID());
                    cmb.setPrimaryKeyID(id);
                    cmb.remove(null); // unlink permanenty !!!
                    iter.remove();
                } else {
                    System.out.println(".....bug.....");
                }
            else if (cm.isModified()) {
                cm.setOwnerPrimaryKeyID(getPrimaryKeyID());
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
            con = this.getConnection();

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
            sql.setInt(3, linkObjectTypeID.intValue());
            rs = sql.executeQuery();

            CommentBean cb = null;

            if (rs.next()) {
                c = new Comment(this.getPrimaryKeyID());
                c.setPrimaryKeyID((Integer) rs.getObject("CommentID"));

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

            cb.setLinkObjectTypeID(2, linkObjectTypeID.intValue());
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

        sb.setOwnerPrimaryKeyID(getPrimaryKeyID());
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
            con = this.getConnection();

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
        survey.setOwnerPrimaryKeyID(getPrimaryKeyID());
        surveys.put(survey.getPrimaryKeyID(), survey);

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
            con = getConnection();
            loadModels(con);
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
                if (m.getPrimaryKeyID().equals(modelID))
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
        if (getPrimaryKeyID() == null) {
            return;
        }
        
        List list = FPSLinkObject.getInstance().getLinkedObjects(getPrimaryKeyID(), PERSON_2_MODEL, con);

        if (list == null)
            return;

        ModelBean mb = null;

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Model m = new Model(getPrimaryKeyID());
            m.setPrimaryKeyID((Integer) iter.next());

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
                mb.setOwnerPrimaryKeyID(getPrimaryKeyID());
                mb.store(con);
            }

            // com.argus.util.Collection.removeNullValues( _models );

        }

        Iterator iter2remove = models.getModelsToRemove().iterator();
        while (iter2remove.hasNext()) {
            Model model = (Model) iter2remove.next();

            FPSLinkObject.getInstance().unlink(getPrimaryKeyID(),
                    model.getPrimaryKeyID(), PERSON_2_MODEL, con);

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

    public java.util.Collection getPlans(Integer planTypeID)
            throws ServiceException {

        try {
            Connection con = getConnection();
            return getPlans(planTypeID, con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    private Integer getPlanOwner(Integer planTypeID) {
        if (planTypeID == null)
            return getPrimaryKeyID();
        if (equals(TEMPLATE_PLAN, planTypeID)) // global template
            return null;
        if (equals(TEMPLATE_PLAN_CLIENT, planTypeID)) // client owned
                                                        // template. NB not in
                                                        // use yet !!!
            return getPrimaryKeyID();
        if (equals(TEMPLATE_PLAN_USER, planTypeID)) // user owned template. NB
                                                    // not in use yet !!!
            return getOwnerPrimaryKeyID();
        return null;
    }

    private java.util.Collection getPlans(Integer planTypeID, Connection con)
            throws java.sql.SQLException, ServiceException {
        Integer ownerID = getPlanOwner(planTypeID);
        List list = FPSLinkObject.getInstance().getLinkedObjects(
                ownerID == null ? GLOBAL_PLAN_TEMPLATE : ownerID.intValue(),
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
                                : " AND PlanTypeID=" + planTypeID.intValue()),
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        try {
            while (iter.hasNext()) {
                Integer planID = (Integer) iter.next();

                try {
                    sql.setInt(1, planID.intValue());
                    rs = sql.executeQuery();

                    if (!rs.next())
                        continue;
                    // throw new SQLException( "Can not find Person PlanDataID:
                    // " + planID );

                    int i = 0;
                    if (plans == null)
                        plans = new java.util.ArrayList();

                    plan = new ReferenceCode(planID);
                    plan.setCodeDesc(rs.getString(++i)); // PlanDataDesc

                    String planDataText = rs.getString(++i);
                    String planDataText2 = rs.getString(++i); // new hex
                                                                // presentation
                                                                // of text data
                    if (planDataText2 != null)
                        planDataText = fromText(planDataText2);

                    plan.setObject(IOUtils2.objectToProperties(planDataText)); // PlanDataText

                    // if (DEBUG) System.out.println( "read planTypeID=" +
                    // planTypeID + " plan.getObject(): " + plan.getObject() );

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

        if (plan == null || plan.getCodeDesc() == null)
            return -1;

        String desc = plan.getCodeDesc().trim();
        if (desc.length() > 255) {
            desc = desc.substring(0, 255);
            plan.setCodeDesc(desc);
        }

        try {
            Connection con = getConnection();
            return storePlan(plan, planTypeID, con);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new ServiceException(e.getMessage());
        }

    }

    private int storePlan(com.argus.util.ReferenceCode plan,
            Integer planTypeID, Connection con) throws java.sql.SQLException,
            ServiceException {
        PreparedStatement sql = null;
        int planID = plan.getCodeID();

        Object data = plan.getObject();
        if (data instanceof java.util.Properties)
            data = IOUtils2.propertiesToString((java.util.Properties) data);
        // if (DEBUG) System.out.println( "write plan.getObject(): " + obj );

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
                sql.setString(++i, plan.getCodeDesc());

                // sql.setString( ++i, "" + data );
                sql.setString(++i, "" + data); // set it to null later ???
                sql.setString(++i, toText("" + data));

                sql.setObject(++i, planTypeID, java.sql.Types.INTEGER);

                sql.executeUpdate();

                Integer ownerID = getPlanOwner(planTypeID);
                FPSLinkObject.getInstance().link(
                        ownerID == null ? GLOBAL_PLAN_TEMPLATE : ownerID
                                .intValue(), planID, DbConstant.PERSON_2_PLAN,
                        con);

                plan.setCodeID(planID);

            } else {

                // do update on PERSON table
                sql = con
                        .prepareStatement("UPDATE PlanData SET"
                                + " PlanDataDesc=?,PlanDataText=?,PlanDataText2=?,PlanTypeID=?"
                                + " WHERE PlanDataID=?");

                int i = 0;
                sql.setString(++i, plan.getCodeDesc());

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
        int planID = plan.getCodeID();

        // if (DEBUG) System.out.println( "Plan deleting logically:\n" + plan +
        // ", " + planID );

        if (plan == null || planID <= 0)
            return false;

        try {
            Connection con = getConnection();
            Integer ownerID = getPlanOwner(planTypeID);
            int linkID = FPSLinkObject.getInstance().unlink(
                    ownerID == null ? GLOBAL_PLAN_TEMPLATE : ownerID
                            .intValue(),
                    // getPersonID(),
                    planID, LinkObjectTypeConstant.PERSON_2_PLAN, con);
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

        Connection con = getConnection();
        load(personID, con);
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

            sql.setInt(1, personID.intValue());
            rs = sql.executeQuery();

            if (!rs.next())
                throw new ObjectNotFoundException("Can not find Person ID: "
                        + personID);

            setPrimaryKeyID(personID);

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

        super.load(rs);

        int personID = rs.getInt("PersonID");

        // PERSON
        PersonNameAddressBean pnab = new PersonNameAddressBean();
        pnab.setPersonName(personName);
        pnab.load(rs);

        dobCountryID = (Integer) rs.getObject("DOBCountryID");
        taxFileNumber = rs.getString("TaxFileNumber");
        String s = rs.getString("DSSRecipient");
        if (s == null)
            isDSSRecipient = false;
        else if (BooleanCode.rcNO.getCode().compareToIgnoreCase(s) == 0)
            isDSSRecipient = false;
        else if (BooleanCode.rcYES.getCode().compareToIgnoreCase(s) == 0)
            isDSSRecipient = true;

        preferredLanguageID = (Integer) rs.getObject("PreferredLanguageID");
        preferredLanguage = rs.getString("PreferredLanguage");
        residenceCountryCodeID = (Integer) rs
                .getObject("ResidenceCountryCodeID");
        residenceStatusCodeID = (Integer) rs.getObject("ResidenceStatusCodeID");
        referalSourceCodeID = (Integer) rs.getObject("ReferalSourceCodeID");

        setDateCreated(rs.getDate("DateCreated"));
        setDateModified(rs.getDate("DateModified"));

        if (rs.getInt("ph_PersonID") > 0)
            getPersonHealth().load(rs);

        if (rs.getInt("pts_PersonID") > 0)
            getTrustStatus().load(rs);

        if (rs.getInt("po_PersonID") > 0) {
            if (occupation == null)
                occupation = new Occupation(personID);
            new OccupationBean(occupation).load(rs);
        }

    }

    public int storePerson(Connection con) throws SQLException {

        /**
         * update PERSON information
         */
        Integer personID = getPrimaryKeyID();

        if (personID == null) {
            try {
                personID = create(con);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                throw new SQLException(e.getMessage());
            }

            // set
            setPrimaryKeyID(personID);
        }

        PreparedStatement sql = con
                .prepareStatement(
                        "UPDATE person SET"
                                + " SexCodeID=?, TitleCodeID=?, MaritalCodeID=?"
                                + ", FamilyName=?, FirstName=?, OtherGivenNames=?, PreferredName=?, DateOfBirth=?"
                                + ", DOBCountryID=?, TaxFileNumber=?, DSSRecipient=?, PreferredLanguageID=?, PreferredLanguage=?"
                                + ", ReferalSourceCodeID=?, ResidenceStatusCodeID=?, ResidenceCountryCodeID=?"
                                + ", DateModified=getDate()"
                                + " WHERE personID=?",
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        int i = 0;
        if (personName == null) {
            sql.setNull(++i, java.sql.Types.INTEGER);
            sql.setNull(++i, java.sql.Types.INTEGER);
            sql.setNull(++i, java.sql.Types.INTEGER);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
            sql.setNull(++i, java.sql.Types.VARCHAR);
        } else {
            sql.setObject(++i, personName.getSexCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, personName.getTitleCodeID(),
                    java.sql.Types.INTEGER);
            sql.setObject(++i, personName.getMaritalCodeID(),
                    java.sql.Types.INTEGER);
            sql.setString(++i, personName.getSurname());
            sql.setString(++i, personName.getFirstName());
            sql.setString(++i, personName.getOtherGivenNames());
            sql.setString(++i, personName.getPreferredName());

            // javax.ejb.EJBException: [Microsoft][ODBC SQL Server
            // Driver]Optional feature not implemented
            // sql.setDate( ++i, personName.getDateOfBirth() );
            if (personName.getDateOfBirth() == null)
                sql.setNull(++i, java.sql.Types.VARCHAR);
            else
                sql.setString(++i, DateTimeUtils.getJdbcDate(personName
                        .getDateOfBirth()));
        }

        sql.setObject(++i, dobCountryID, java.sql.Types.INTEGER);
        sql.setString(++i, taxFileNumber);
        String s;
        if (isDSSRecipient)
            s = BooleanCode.rcYES.getCode();
        else
            s = BooleanCode.rcNO.getCode();
        sql.setString(++i, s);
        sql.setObject(++i, preferredLanguageID, java.sql.Types.INTEGER);
        sql.setString(++i, preferredLanguage);
        sql.setObject(++i, referalSourceCodeID, java.sql.Types.INTEGER);
        sql.setObject(++i, residenceStatusCodeID, java.sql.Types.INTEGER);
        sql.setObject(++i, residenceCountryCodeID, java.sql.Types.INTEGER);

        sql.setObject(++i, personID, java.sql.Types.INTEGER);

        sql.executeUpdate();
        close(null, sql);

        if (personHealth != null) {
            personHealth.setOwnerPrimaryKeyID(getPrimaryKeyID());
            personHealth.store(con);
        }

        if (personTrustDIYStatus != null) {
            personTrustDIYStatus.setOwnerPrimaryKeyID(getPrimaryKeyID());
            personTrustDIYStatus.store(con);
        }

        if (occupation != null) {
            occupation.setOwnerPrimaryKeyID(getPrimaryKeyID());
            new OccupationBean(occupation).store(con);
        }

        storeAddresses(con);
        storeContactMedia(con);

        storeDependents(con);
        storeContacts(con);

        storeEmployerBusiness(con);

        return personID.intValue();

    }

}
