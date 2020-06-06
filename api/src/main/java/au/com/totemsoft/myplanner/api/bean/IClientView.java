package au.com.totemsoft.myplanner.api.bean;

public interface IClientView extends IPerson, IOwnerBase<Long> {

    String getOwnerFirstname();

    String getOwnerSurname();

    String getOwnerShortName();

    //Address getAddress();

    String getEMail();

    String getEmailWork();

    String getFax();

    String getFaxWork();

    String getPhone();

    String getPhoneWork();

    String getDetails();

}
