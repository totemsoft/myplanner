/*
 * UserManagmentDataResolver.java
 *
 * Created on 11 March 2003, 17:51
 */

package com.argus.financials.db;

import java.util.ArrayList;

import com.argus.crypto.Digest;
import com.argus.financials.bean.LinkObjectTypeConstant;
import com.argus.financials.bean.ObjectTypeConstant;
import com.argus.financials.swing.table.UpdateableTableModel;
import com.argus.financials.swing.table.UpdateableTableRow;
import com.argus.util.KeyValue;

/**
 * 
 * @author shibaevv
 * @version
 */
public class UserManagmentDataResolver implements DataResolver {

    private String sqlInsertUserPerson = "INSERT  INTO User (UserPersonID,AdviserTypeCodeID,LoginName,LoginPassword) VALUES ";

    private String sqlInsertPerson = "INSERT  INTO Person     (PersonID,TitleCodeID,FamilyName,FirstName, OtherGivenNames, PreferredName) VALUES ";

    private String sqlInsertObjectUserPesron = "INSERT INTO Object (ObjectTypeID) VALUES ("
            + ObjectTypeConstant.USER_PERSON + ")";

    private String sqlInsertIdentity = "SELECT @@IDENTITY AS NEW_IDENTITY";

    private String sqlInsertObjectAddress = "INSERT INTO Object (ObjectTypeID) VALUES ("
            + ObjectTypeConstant.ADDRESS + ")";

    private String sqlInsertObjectContactMedia = "INSERT INTO Object (ObjectTypeID) VALUES ("
            + ObjectTypeConstant.CONTACT_MEDIA + ")";

    private String sqlInsertObjectLinkAddress = "INSERT INTO Object (ObjectTypeID) VALUES ("
            + LinkObjectTypeConstant.PERSON_2_ADDRESS + ")";

    private String sqlInsertObjectLinkContact = "INSERT INTO Object (ObjectTypeID) VALUES ("
            + LinkObjectTypeConstant.PERSON_2_CONTACT_MEDIA + ")";

    private String sqlInsertAddress = "INSERT  INTO  Address     (AddressID, AddressCodeID,StreetNumber,StreetNumber2,Suburb,Postcode,StateCodeID,CountryCodeID,DateCreated,DateModified,ParentAddressID,State) VALUES ";

    private String sqlInsertContactMedia = "INSERT  INTO  ContactMedia     (ContactMediaID,ContactMediaCodeID,Value1,Value2,ContactMediaDesc,DateCreated,DateModified) VALUES ";

    private String sqlInsertLinkAddress = "INSERT  INTO  Link     (LinkID,ObjectID1,ObjectID2,LinkObjectTypeID) VALUES (?,?,?,1005)";

    // "INSERT INTO Link
    // (LinkID,ObjectID1,ObjectID2,LinkObjectTypeID,LogicallyDeleted) VALUES
    // (?,?,?,1005,null)" ;

    private String sqlInsertLinkContact = "INSERT  INTO  Link     (LinkID,ObjectID1,ObjectID2,LinkObjectTypeID,LogicallyDeleted) VALUES (?,?,?,1006,null)";

    private String sqlDeleteUserPerson = "UPDATE  User     SET ActiveUser = 'N' WHERE  UserPersonID = ";

    private String sqlUpdateUserPersonSet = "UPDATE  User     SET ";

    private String sqlUpdateUserPersonWhere = " WHERE  UserPersonID = ";

    private String sqlUpdatePersonSet = "UPDATE  Person     SET  ";

    private String sqlUpdatePersonWhere = " WHERE  PersonID = ";

    private String sqlUpdateAddressSet = "UPDATE  Address     SET  ";

    private String sqlUpdateAddressWhere = " WHERE  AddressID = ";

    private String sqlUpdateContactMediaSet = "UPDATE  ContactMedia     SET ";

    private String sqlUpdateContactMediaWhere = " WHERE  ContactMediaID = ";

    // UserPersonID,AdviserTypeCodeID,LoginName,LoginPassword

    // PersonID,TitleCodeID,FamilyName,FirstName, OtherGivenNames, PreferredName

    UpdateableTableModel tm = null;

    /** Creates new UserManagmentDataResolver */
    public UserManagmentDataResolver(UpdateableTableModel tm) {
        this.tm = tm;
    }

    public ArrayList getUpdates() {
        String[] updates;
        if (tm == null || tm.getRowCount() == 0)
            return null;

        ArrayList sb = new ArrayList();
        for (int i = 0; i < tm.getRowCount(); i++) {

            UpdateableTableRow row = (UpdateableTableRow) tm.getRowAt(i);

            if (row.getRowStatus() == row.STATUS_MODIFIED) {

                String person = sqlUpdatePersonSet;
                String userPerson = sqlUpdateUserPersonSet;
                String address = sqlUpdateAddressSet;
                String contactMedia = sqlUpdateContactMediaSet;

                Object UserPersonID = row.getValueAt(tm
                        .getColumnIndex("UserPersonID"));

                Object AdviserTypeCodeID = row.getValueAt(tm
                        .getColumnIndex("AdviserTypeCodeID"));
                Object LoginName = row.getValueAt(tm
                        .getColumnIndex("LoginName"));
                Object LoginPassword = row.getValueAt(tm
                        .getColumnIndex("LoginPassword"));

                if (row.getColumnStatus(tm.getColumnIndex("LoginPassword")) == row.STATUS_MODIFIED)
                    LoginPassword = Digest.digest((String) LoginPassword);

                Object TitleCodeID = row.getValueAt(tm
                        .getColumnIndex("TitleCodeID"));
                Object FamilyName = row.getValueAt(tm
                        .getColumnIndex("FamilyName"));
                Object FirstName = row.getValueAt(tm
                        .getColumnIndex("FirstName"));
                Object OtherGivenNames = row.getValueAt(tm
                        .getColumnIndex("OtherGivenNames"));
                Object PreferredName = row.getValueAt(tm
                        .getColumnIndex("PreferredName"));

                Object AddressID = row.getValueAt(tm
                        .getColumnIndex("AddressID"));
                Object AddressCodeID = row.getValueAt(tm
                        .getColumnIndex("AddressCodeID"));
                Object StreetNumber = row.getValueAt(tm
                        .getColumnIndex("StreetNumber"));
                Object StreetNumber2 = row.getValueAt(tm
                        .getColumnIndex("StreetNumber2"));
                Object Suburb = row.getValueAt(tm.getColumnIndex("Suburb"));
                Object Postcode = row.getValueAt(tm.getColumnIndex("Postcode"));
                Object StateCodeID = row.getValueAt(tm
                        .getColumnIndex("StateCodeID"));
                Object State = row.getValueAt(tm.getColumnIndex("State"));

                Object CountryCodeID = row.getValueAt(tm
                        .getColumnIndex("CountryCodeID"));

                Object PhoneValue2 = row.getValueAt(tm
                        .getColumnIndex("PhoneValue2"));
                Object MobileValue2 = row.getValueAt(tm
                        .getColumnIndex("MobileValue2"));
                Object EmailValue2 = row.getValueAt(tm
                        .getColumnIndex("EmailValue2"));

                Object PhoneContactMediaCodeID = row.getValueAt(tm
                        .getColumnIndex("PhoneContactMediaCodeID"));
                Object MobileContactMediaCodeID = row.getValueAt(tm
                        .getColumnIndex("MobileContactMediaCodeID"));
                Object EmailContactMediaCodeID = row.getValueAt(tm
                        .getColumnIndex("EmailContactMediaCodeID"));

                Object PhoneContactMediaID = row.getValueAt(tm
                        .getColumnIndex("PhoneContactMediaID"));
                Object MobileContactMediaID = row.getValueAt(tm
                        .getColumnIndex("MobileContactMediaID"));
                Object EmailContactMediaID = row.getValueAt(tm
                        .getColumnIndex("EmailContactMediaID"));

                java.sql.Date DateModified = new java.sql.Date(
                        (new java.util.Date()).getTime());

                userPerson = userPerson + "AdviserTypeCodeID="
                        + AdviserTypeCodeID + ",LoginName='" + LoginName
                        + "',LoginPassword='" + LoginPassword + "' "
                        + sqlUpdateUserPersonWhere + UserPersonID;
                person = person + " TitleCodeID =" + TitleCodeID
                        + ",FamilyName='" + FamilyName + "',FirstName='"
                        + FirstName + "',OtherGivenNames='" + OtherGivenNames
                        + "',PreferredName='" + PreferredName + "'"
                        + sqlUpdatePersonWhere + UserPersonID;

                address = address + "AddressCodeID=" + AddressCodeID
                        + ",StreetNumber='" + StreetNumber
                        + "',StreetNumber2='" + StreetNumber2 + "',Suburb='"
                        + Suburb + "',Postcode=" + Postcode + ",StateCodeID="
                        + StateCodeID + ",CountryCodeID=" + CountryCodeID
                        + ",DateModified='" + DateModified + "', State='"
                        + State + "' " + sqlUpdateAddressWhere + AddressID;

                String contactMedia1 = sqlUpdateContactMediaSet
                        + "ContactMediaCodeID=" + PhoneContactMediaCodeID
                        + ",Value2='" + PhoneValue2
                        + "',ContactMediaDesc='Phone'"
                        + sqlUpdateContactMediaWhere + PhoneContactMediaID;

                String contactMedia2 = sqlUpdateContactMediaSet
                        + "ContactMediaCodeID=" + MobileContactMediaCodeID
                        + ",Value2='" + MobileValue2
                        + "',ContactMediaDesc='Mobile'"
                        + sqlUpdateContactMediaWhere + MobileContactMediaID;

                String contactMedia3 = sqlUpdateContactMediaSet
                        + "ContactMediaCodeID=" + EmailContactMediaCodeID
                        + ",Value2='" + EmailValue2
                        + "',ContactMediaDesc='E-mail'"
                        + sqlUpdateContactMediaWhere + EmailContactMediaID;

                /* Insert user person */
                sb.add(new KeyValue(PLAIN, userPerson));
                sb.add(new KeyValue(PLAIN, person));
                sb.add(new KeyValue(PLAIN, address));

                sb.add(new KeyValue(PLAIN, contactMedia1));
                sb.add(new KeyValue(PLAIN, contactMedia2));
                sb.add(new KeyValue(PLAIN, contactMedia3));

                row.setRowStatus(row.STATUS_ORIGINAL);
            }
        }

        return sb;
    }

    public ArrayList getDeletes() {
        String[] deletes;
        if (tm == null || tm.getDeletedRowCount() == 0)
            return null;

        ArrayList sb = new ArrayList();
        for (int i = 0; i < tm.getDeletedRowCount(); i++) {
            String userPerson = sqlDeleteUserPerson;

            UpdateableTableRow row = (UpdateableTableRow) tm.getDeletedRowAt(i);
            if (row != null) {

                Object UserPersonID = row.getValueAt(tm
                        .getColumnIndex("UserPersonID"));

                if (UserPersonID != null) {

                    userPerson = userPerson + UserPersonID;
                    sb.add(new KeyValue(PLAIN, userPerson));

                }

            }
        }
        /* Reset deleted flag */
        tm.removeDeleted();

        return sb;

    }

    public ArrayList getInserts() {
        String[] inserts;
        if (tm == null || tm.getRowCount() == 0)
            return null;

        ArrayList sb = new ArrayList();
        for (int i = 0; i < tm.getRowCount(); i++) {
            String person = sqlInsertPerson;
            String userPerson = sqlInsertUserPerson;
            String address = sqlInsertAddress;
            String contactMedia = sqlInsertContactMedia;

            UpdateableTableRow row = (UpdateableTableRow) tm.getRowAt(i);
            if (row.getRowStatus() == row.STATUS_NEW) {

                Object AdviserTypeCodeID = row.getValueAt(tm
                        .getColumnIndex("AdviserTypeCodeID"));
                Object LoginName = row.getValueAt(tm
                        .getColumnIndex("LoginName"));
                Object LoginPassword = row.getValueAt(tm
                        .getColumnIndex("LoginPassword"));
                Object TitleCodeID = row.getValueAt(tm
                        .getColumnIndex("TitleCodeID"));
                Object FamilyName = row.getValueAt(tm
                        .getColumnIndex("FamilyName"));
                Object FirstName = row.getValueAt(tm
                        .getColumnIndex("FirstName"));
                Object OtherGivenNames = row.getValueAt(tm
                        .getColumnIndex("OtherGivenNames"));
                Object PreferredName = row.getValueAt(tm
                        .getColumnIndex("PreferredName"));

                Object AddressCodeID = row.getValueAt(tm
                        .getColumnIndex("AddressCodeID"));
                Object StreetNumber = row.getValueAt(tm
                        .getColumnIndex("StreetNumber"));
                Object StreetNumber2 = row.getValueAt(tm
                        .getColumnIndex("StreetNumber2"));
                Object Suburb = row.getValueAt(tm.getColumnIndex("Suburb"));
                Object Postcode = row.getValueAt(tm.getColumnIndex("Postcode"));
                Object StateCodeID = row.getValueAt(tm
                        .getColumnIndex("StateCodeID"));
                Object State = row.getValueAt(tm.getColumnIndex("State"));

                Object CountryCodeID = row.getValueAt(tm
                        .getColumnIndex("CountryCodeID"));

                Object PhoneValue2 = row.getValueAt(tm
                        .getColumnIndex("PhoneValue2"));
                Object MobileValue2 = row.getValueAt(tm
                        .getColumnIndex("MobileValue2"));
                Object EmailValue2 = row.getValueAt(tm
                        .getColumnIndex("EmailValue2"));

                Object PhoneContactMediaCodeID = row.getValueAt(tm
                        .getColumnIndex("PhoneContactMediaCodeID"));
                Object MobileContactMediaCodeID = row.getValueAt(tm
                        .getColumnIndex("MobileContactMediaCodeID"));
                Object EmailContactMediaCodeID = row.getValueAt(tm
                        .getColumnIndex("EmailContactMediaCodeID"));

                LoginPassword = Digest.digest((String) LoginPassword);

                if (StateCodeID.equals(new Integer(0)))
                    StateCodeID = null;
                if (CountryCodeID.equals(new Integer(0)))
                    CountryCodeID = null;

                if (TitleCodeID == null || TitleCodeID.equals(new Integer(0)))
                    TitleCodeID = new Integer(1);

                java.sql.Date DateModified = new java.sql.Date(
                        (new java.util.Date()).getTime());

                userPerson = userPerson + "(?," + AdviserTypeCodeID + ",'"
                        + LoginName + "','" + LoginPassword + "')";
                person = person + "(?," + TitleCodeID + ",'" + FamilyName
                        + "','" + FirstName + "','" + OtherGivenNames + "','"
                        + PreferredName + "')";

                address = address + "(?," + "2" + ",'" + StreetNumber + "','"
                        + StreetNumber2 + "','" + Suburb + "'," + Postcode
                        + "," + StateCodeID + "," + CountryCodeID + ",'"
                        + DateModified + "','" + DateModified + "',"
                        + java.sql.Types.NULL + ",'" + State + "')";

                String contactMedia1 = contactMedia + "(?," + "2" + ",'','"
                        + PhoneValue2 + "','Phone','" + DateModified + "','"
                        + DateModified + "')";

                String contactMedia2 = contactMedia + "(?," + "6" + ",'','"
                        + MobileValue2 + "','Mobile','" + DateModified + "','"
                        + DateModified + "')";

                String contactMedia3 = contactMedia + "(?," + "8" + ",'','"
                        + EmailValue2 + "','E-mail','" + DateModified + "','"
                        + DateModified + "')";

                /* Insert user person */
                sb.add(new KeyValue(PLAIN, sqlInsertObjectUserPesron));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "PersonID" }));

                sb.add(new KeyValue(SET_IDENTITY, person,
                        new String[] { "PersonID" }));
                sb.add(new KeyValue(SET_IDENTITY, userPerson,
                        new String[] { "PersonID" }));
                /* Insert address */
                sb.add(new KeyValue(PLAIN, sqlInsertObjectAddress));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "AddressID" }));

                sb.add(new KeyValue(PLAIN, sqlInsertObjectLinkAddress));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "LinkID" }));

                sb.add(new KeyValue(SET_IDENTITY, sqlInsertLinkAddress,
                        new String[] { "LinkID", "PersonID", "AddressID" }));

                sb.add(new KeyValue(SET_IDENTITY, address,
                        new String[] { "AddressID" }));
                /* Inserting contacts */
                sb.add(new KeyValue(PLAIN, sqlInsertObjectContactMedia));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "Contact1" }));

                sb.add(new KeyValue(PLAIN, sqlInsertObjectContactMedia));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "Contact2" }));

                sb.add(new KeyValue(PLAIN, sqlInsertObjectContactMedia));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "Contact3" }));
                // Insert Phone contact
                sb.add(new KeyValue(PLAIN, sqlInsertObjectLinkContact));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "LinkID" }));

                sb.add(new KeyValue(SET_IDENTITY, sqlInsertLinkContact,
                        new String[] { "LinkID", "PersonID", "Contact1" }));
                sb.add(new KeyValue(SET_IDENTITY, contactMedia1,
                        new String[] { "Contact1" }));
                // Insert Mobile contact
                sb.add(new KeyValue(PLAIN, sqlInsertObjectLinkContact));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "LinkID" }));

                sb.add(new KeyValue(SET_IDENTITY, sqlInsertLinkContact,
                        new String[] { "LinkID", "PersonID", "Contact2" }));
                sb.add(new KeyValue(SET_IDENTITY, contactMedia2,
                        new String[] { "Contact2" }));
                // Insert Email contact
                sb.add(new KeyValue(PLAIN, sqlInsertObjectLinkContact));
                sb.add(new KeyValue(GET_IDENTITY, sqlInsertIdentity,
                        new String[] { "LinkID" }));

                sb.add(new KeyValue(SET_IDENTITY, sqlInsertLinkContact,
                        new String[] { "LinkID", "PersonID", "Contact3" }));
                sb.add(new KeyValue(SET_IDENTITY, contactMedia3,
                        new String[] { "Contact3" }));

                /* Reset table model flag to not modified */
                row.setRowStatus(row.STATUS_ORIGINAL);
            }
        }
        return sb;
    }

}
