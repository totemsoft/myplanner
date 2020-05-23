/*
 * UserManagmentDataResolver.java
 *
 * Created on 11 March 2003, 17:51
 */

package au.com.totemsoft.myplanner.db;

/**
 * 
 * @version
 */
public class UserManagmentDataProvider implements DataProvider {

    private static final String sqlSelect = "SELECT * FROM vUserManagmentDataProvider";

    /*
     * "SELECT " + " a.UserPersonID UserPersonID, " + " a.AdviserTypeCodeID
     * AdviserTypeCodeID, " + " a.LoginName LoginName, " + " a.LoginPassword
     * LoginPassword, " + " b.PersonID PersonID, " + " b.TitleCodeID
     * TitleCodeID, " + " b.FamilyName FamilyName, " + " b.FirstName FirstName, " + "
     * b.FamilyName+' '+ b.FirstName FullName, " + " b.OtherGivenNames
     * OtherGivenNames, " + " b.PreferredName PreferredName, " + " d.AddressID
     * AddressID, " + " 2 as AddressCodeID, " + " d.StreetNumber StreetNumber, " + "
     * d.StreetNumber2 StreetNumber2, " + " d.Suburb Suburb, " + " d.Postcode
     * Postcode, " + " d.StateCodeID StateCodeID, " + " d.CountryCodeID
     * CountryCodeID, " + " d.State State, " + " d.DateCreated DateCreated, " + "
     * GETDATE() DateModified, " + " e.ContactMediaID PhoneContactMediaID, " + "
     * 2 as PhoneContactMediaCodeID," + " e.Value1 PhoneValue1 , " + " e.Value2
     * PhoneValue2 , " + " e.ContactMediaDesc PhoneContactMediaDesc, " + "
     * e.DateCreated PhoneDateCreated , " + " g.ContactMediaID
     * MobileContactMediaID, " + " 6 as MobileContactMediaCodeID," + " g.Value1
     * MobileValue1 , " + " g.Value2 MobileValue2 , " + " g.ContactMediaDesc
     * MobileContactMediaDesc, " + " g.DateCreated MobileDateCreated, " + "
     * l.ContactMediaID EmailContactMediaID, " + " 8 as
     * EmailContactMediaCodeID," + " l.Value1 EmailValue1 , " + " l.Value2
     * EmailValue2 , " + " l.ContactMediaDesc EmailContactMediaDesc, " + "
     * l.DateCreated EmailDateCreated " + " FROM User a , " + " Person b
     * LEFT OUTER JOIN " + " ( SELECT " + " AddressID , " + " AddressCodeID , " + "
     * StreetNumber , " + " StreetNumber2 , " + " Suburb , " + " Postcode , " + "
     * StateCodeID , " + " CountryCodeID , " + " DateCreated , " + "
     * DateModified, " + " State, " + " ObjectID1 PersonID " + " FROM Address
     * ad, Link ln " + " WHERE ad.AddressID = ln.ObjectID2 AND " + "
     * ln.LinkObjectTypeID = 1005 AND " + " ad.AddressCodeID = 2 " + " AND
     * ln.LogicallyDeleted IS NULL " + " ) as d ON b.PersonID = d.PersonID " + " , " + "
     * Person c LEFT OUTER JOIN " + " ( " + //Phone " SELECT " + "
     * ContactMediaID , " + " ContactMediaCodeID, " + " Value1 , " + " Value2 , " + "
     * ContactMediaDesc , " + " DateCreated , " + " DateModified, " + "
     * ObjectID1 PersonID " + " FROM ContactMedia cm, Link ln " + " WHERE
     * cm.ContactMediaID = ln.ObjectID2 AND " + " ln.LinkObjectTypeID = 1006 AND " + "
     * cm.ContactMediaCodeID = 2 " + " AND ln.LogicallyDeleted IS NULL " + " " + " )
     * as e ON c.PersonID = e.PersonID " + " " + " , " + " Person f LEFT OUTER
     * JOIN " + " ( " + //Mobile " SELECT " + " ContactMediaID , " + "
     * ContactMediaCodeID, " + " Value1 , " + " Value2 , " + " ContactMediaDesc , " + "
     * DateCreated , " + " DateModified, " + " ObjectID1 PersonID " + " FROM
     * ContactMedia cm, Link ln " + " WHERE cm.ContactMediaID = ln.ObjectID2 AND " + "
     * ln.LinkObjectTypeID = 1006 AND " + " cm.ContactMediaCodeID = 6 " + " AND
     * ln.LogicallyDeleted IS NULL " + " " + " ) as g ON f.PersonID = g.PersonID " + " , " + "
     * Person k LEFT OUTER JOIN " + " ( " + //E-mail " SELECT " + "
     * ContactMediaID , " + " ContactMediaCodeID, " + " Value1 , " + " Value2 , " + "
     * ContactMediaDesc , " + " DateCreated , " + " DateModified, " + "
     * ObjectID1 PersonID " + " FROM ContactMedia cm, Link ln " + " WHERE
     * cm.ContactMediaID = ln.ObjectID2 AND " + " ln.LinkObjectTypeID = 1006 AND " + "
     * cm.ContactMediaCodeID = 8 " + " AND ln.LogicallyDeleted IS NULL " + " " + " )
     * as l ON k.PersonID = l.PersonID " + " WHERE ( a.ActiveUser <> 'N' OR
     * a.ActiveUser IS NULL ) AND " + " a.UserPersonID = b.PersonID AND " + "
     * a.UserPersonID = c.PersonID AND " + " a.UserPersonID = f.PersonID AND " + "
     * a.UserPersonID = k.PersonID " + " ORDER BY FamilyName " ;
     */

    /** Creates new UserManagmentDataProvider */
    public UserManagmentDataProvider() {

    }

    public String getSelect() {
        return sqlSelect;
    }

}
