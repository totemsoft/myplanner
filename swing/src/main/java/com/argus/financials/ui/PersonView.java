/*
 * PersonView.java
 *
 * Created on 18 July 2001, 17:50
 */

package com.argus.financials.ui;

import java.util.Date;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.argus.crypto.Digest;
import com.argus.financials.api.InvalidCodeException;
import com.argus.financials.api.bean.IBusiness;
import com.argus.financials.api.bean.ICountry;
import com.argus.financials.api.bean.IOccupation;
import com.argus.financials.api.bean.IPerson;
import com.argus.financials.api.bean.IPersonHealth;
import com.argus.financials.api.bean.IPersonTrustDIYStatus;
import com.argus.financials.api.code.ObjectTypeConstant;
import com.argus.financials.code.AddressCode;
import com.argus.financials.code.Advisers;
import com.argus.financials.code.BooleanCode;
import com.argus.financials.code.ContactMediaCode;
import com.argus.financials.code.CountryCode;
import com.argus.financials.code.EmploymentStatusCode;
import com.argus.financials.code.HealthStateCode;
import com.argus.financials.code.OccupationCode;
import com.argus.financials.code.ResidenceStatusCode;
import com.argus.financials.code.StatusCode;
import com.argus.financials.domain.hibernate.Occupation;
import com.argus.financials.domain.hibernate.refdata.Country;
import com.argus.financials.etc.AddressDto;
import com.argus.financials.etc.Contact;
import com.argus.financials.etc.ContactMedia;
import com.argus.financials.service.ClientService;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.UtilityService;
import com.argus.financials.swing.DateInputVerifier;
import com.argus.financials.swing.IntegerInputVerifier;
import com.argus.financials.swing.SwingUtil;
import com.argus.format.LimitedPlainDocument;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public abstract class PersonView extends BaseView {

    public static final int PERSONAL_DETAILS = 0;

    public static final int CONTACT_DETAILS = 1;

    public static final int EMPLOYMENT = 2;

    protected static int LAST = EMPLOYMENT;

    private boolean thisCTOR = false;

    private Object primaryKey;

    private PersonService personForPassword;

    private NameView personNameView;

    private AddressView residentialAddressView;

    private AddressView postalAddressView;

    private ContactMediaView phoneHome;

    private ContactMediaView phoneWork;

    private ContactMediaView phoneMobile;

    private ContactMediaView faxHome;

    private ContactMediaView faxWork;

    private ContactMediaView emailHome;

    private ContactMediaView emailWork;

    private static UtilityService utilityService;
    public static void setUtilityService(UtilityService utilityService) {
        PersonView.utilityService = utilityService;
    }

    /** Creates new form PersonView */
    public PersonView() {
        super();

        thisCTOR = true;
        initComponents();
        jPanelDetails.setLayout(new javax.swing.BoxLayout(jPanelDetails,
                javax.swing.BoxLayout.Y_AXIS));
        initComponents2();
        thisCTOR = false;

    }

    public abstract String getViewCaption();

    public Object getPrimaryKey() {
        return primaryKey;
    }

    private void initComponents2() {

        jButtonDelete.setVisible(false);

        jComboBoxSmoker.setModel(new javax.swing.DefaultComboBoxModel(
                (com.argus.util.ReferenceCode[]) new BooleanCode().getCodes()
                        .toArray(new ReferenceCode[0])));

        Contact[] advisers = (Contact[]) new Advisers().getCodes().toArray(
                new Contact[0]);
        if (advisers != null && advisers.length > 0) {
            Contact[] advisers2 = new Contact[advisers.length - 1];
            System.arraycopy(advisers, 1, advisers2, 0, advisers2.length);
            jComboBoxAdviser.setModel(new javax.swing.DefaultComboBoxModel(
                    advisers2));
        }

        jPanelAdvisor.setVisible(false); // only client view can see/modify
                                            // it

        residentialAddressView = new AddressView(AddressCode.RESIDENTIAL);
        residentialAddressView.setBorder(new javax.swing.border.TitledBorder(
                "Residential Address"));
        jPanelAddresses.add(residentialAddressView);

        postalAddressView = new AddressView(AddressCode.POSTAL);
        postalAddressView.setBorder(new javax.swing.border.TitledBorder(
                "Postal Address"));
        jPanelAddresses.add(postalAddressView);

        phoneHome = new ContactMediaView(ContactMediaCode.PHONE, jPanelHome, 0);
        phoneHome.setDescription("Phone");
        faxHome = new ContactMediaView(ContactMediaCode.FAX, jPanelHome, 1);
        faxHome.setDescription("Fax");
        emailHome = new ContactMediaView(ContactMediaCode.EMAIL, jPanelHome, 2);
        emailHome.setDescription("E-Mail");
        phoneMobile = new ContactMediaView(ContactMediaCode.MOBILE, jPanelHome,
                3);
        phoneMobile.setDescription("Mobile");

        phoneWork = new ContactMediaView(ContactMediaCode.PHONE_WORK,
                jPanelWork, 0);
        phoneWork.setDescription("Phone");
        faxWork = new ContactMediaView(ContactMediaCode.FAX_WORK, jPanelWork, 1);
        faxWork.setDescription("Fax");
        emailWork = new ContactMediaView(ContactMediaCode.EMAIL_WORK,
                jPanelWork, 2);
        emailWork.setDescription("E-Mail");

        jTextFieldTaxFileNumber1.setDocument(new LimitedPlainDocument(3));
        jTextFieldTaxFileNumber2.setDocument(new LimitedPlainDocument(3));
        jTextFieldTaxFileNumber3.setDocument(new LimitedPlainDocument(3));
        jTextFieldEmpName.setDocument(new LimitedPlainDocument(150));

        updateComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelClientDetails = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanelName = new javax.swing.JPanel();
        jPanelClientHistory = new javax.swing.JPanel();
        jLabelDateOfBirth = new javax.swing.JLabel();
        jLabelResidenceStatus = new javax.swing.JLabel();
        Age = new javax.swing.JLabel();
        jLabelTaxFileNumber = new javax.swing.JLabel();
        jTextFieldDateOfBirth = new com.argus.bean.FDateChooser();
        jTextFieldAge = new javax.swing.JTextField();
        jComboBoxResidenceStatus = new javax.swing.JComboBox(
                new ResidenceStatusCode().getCodeDescriptions());
        jPanelTFN = new javax.swing.JPanel();
        jTextFieldTaxFileNumber1 = new javax.swing.JTextField();
        jTextFieldTaxFileNumber2 = new javax.swing.JTextField();
        jTextFieldTaxFileNumber3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jPasswordField3 = new javax.swing.JPasswordField();
        jButtonPassword = new javax.swing.JButton();
        jLabelResidenceCountry = new javax.swing.JLabel();
        jComboBoxResidenceCountry = new javax.swing.JComboBox(new CountryCode()
                .getCodeDescriptions());
        jPanelOccupation = new javax.swing.JPanel();
        jLabelOccupation = new javax.swing.JLabel();
        jLabelEmpStatus = new javax.swing.JLabel();
        jLabelEmpName = new javax.swing.JLabel();
        jComboBoxOccupation = new javax.swing.JComboBox(new OccupationCode()
                .getCodeDescriptions());
        jTextFieldEmpName = new javax.swing.JTextField();
        jComboBoxEmpStatus = new javax.swing.JComboBox(
                new EmploymentStatusCode().getCodeDescriptions());
        jLabelDSSRecipient = new javax.swing.JLabel();
        jCheckBoxDSSRecipient = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jPanelAdvisor = new javax.swing.JPanel();
        jLabelClientActive = new javax.swing.JLabel();
        jCheckBoxClientActive = new javax.swing.JCheckBox();
        jLabelAdviser = new javax.swing.JLabel();
        jComboBoxAdviser = new javax.swing.JComboBox();
        jLabelFeeDate = new javax.swing.JLabel();
        jLabelReviewDate = new javax.swing.JLabel();
        jTextFieldFeeDate = new com.argus.bean.FDateChooser();
        jTextFieldReviewDate = new com.argus.bean.FDateChooser();
        jPanelCurrentHealth = new javax.swing.JPanel();
        jLabelAgeNextBirthday = new javax.swing.JLabel();
        jTextFieldAgeNextBirthday = new javax.swing.JTextField();
        jLabelHospitalCover = new javax.swing.JLabel();
        jCheckBoxHospitalCover = new javax.swing.JCheckBox();
        jLabelSmoker = new javax.swing.JLabel();
        jComboBoxSmoker = new javax.swing.JComboBox();
        jLabelStateOfHealth = new javax.swing.JLabel();
        jComboBoxStateOfHealth = new javax.swing.JComboBox(
                new HealthStateCode().getCodeDescriptions());
        jPanelTrustDIY = new javax.swing.JPanel();
        jLabelTrustStatus = new javax.swing.JLabel();
        jLabelDIYStatus = new javax.swing.JLabel();
        jLabelCompanyStatus = new javax.swing.JLabel();
        jComboBoxTrustStatus = new javax.swing.JComboBox(new StatusCode()
                .getCodeDescriptions());
        jComboBoxCompanyStatus = new javax.swing.JComboBox(new StatusCode()
                .getCodeDescriptions());
        jComboBoxDIYStatus = new javax.swing.JComboBox(new StatusCode()
                .getCodeDescriptions());
        jPanelContactDetails = new javax.swing.JPanel();
        jPanelAddresses = new javax.swing.JPanel();
        jPanelContact = new javax.swing.JPanel();
        jPanelHome = new javax.swing.JPanel();
        jPanelWork = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setPreferredSize(new java.awt.Dimension(650, 450));
        jTabbedPane.setPreferredSize(new java.awt.Dimension(5, 1000));
        jTabbedPane.setMinimumSize(new java.awt.Dimension(629, 405));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelClientDetails.setLayout(new javax.swing.BoxLayout(
                jPanelClientDetails, javax.swing.BoxLayout.X_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelName.setLayout(new javax.swing.BoxLayout(jPanelName,
                javax.swing.BoxLayout.X_AXIS));

        jPanelName.setBorder(new javax.swing.border.TitledBorder("Name"));
        personNameView = new NameView(true);
        jPanelName.add(personNameView);
        jPanel4.add(jPanelName);

        jPanelClientHistory.setLayout(new java.awt.GridBagLayout());

        jPanelClientHistory.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder("ClientView History"),
                new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 10,
                        1, 10))));
        jLabelDateOfBirth.setText("DOB ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelClientHistory.add(jLabelDateOfBirth, gridBagConstraints);

        jLabelResidenceStatus.setText("Residence Status");
        jLabelResidenceStatus.setPreferredSize(new java.awt.Dimension(110, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelClientHistory.add(jLabelResidenceStatus, gridBagConstraints);

        Age.setText("Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelClientHistory.add(Age, gridBagConstraints);

        jLabelTaxFileNumber.setText("Tax File Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelClientHistory.add(jLabelTaxFileNumber, gridBagConstraints);

        jTextFieldDateOfBirth.setInputVerifier(new DateInputVerifier());
        jTextFieldDateOfBirth
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        jTextFieldDateOfBirthFocusLost(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelClientHistory.add(jTextFieldDateOfBirth, gridBagConstraints);

        jTextFieldAge.setEditable(false);
        jTextFieldAge.setFont(new java.awt.Font("SansSerif", 1, 11));
        jTextFieldAge.setBackground(java.awt.Color.lightGray);
        jTextFieldAge.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAge.setPreferredSize(new java.awt.Dimension(20, 20));
        jTextFieldAge.setMinimumSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelClientHistory.add(jTextFieldAge, gridBagConstraints);

        jComboBoxResidenceStatus.setPreferredSize(new java.awt.Dimension(170,
                20));
        jComboBoxResidenceStatus
                .setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxResidenceStatus
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBoxResidenceStatusActionPerformed(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelClientHistory.add(jComboBoxResidenceStatus, gridBagConstraints);

        jPanelTFN.setLayout(new java.awt.GridBagLayout());

        jTextFieldTaxFileNumber1
                .setPreferredSize(new java.awt.Dimension(30, 20));
        jTextFieldTaxFileNumber1.setInputVerifier(IntegerInputVerifier
                .getInstance());
        jTextFieldTaxFileNumber1.setMinimumSize(new java.awt.Dimension(30, 20));
        jTextFieldTaxFileNumber1
                .addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        jTextFieldTaxFileNumber1KeyTyped(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelTFN.add(jTextFieldTaxFileNumber1, gridBagConstraints);

        jTextFieldTaxFileNumber2
                .setPreferredSize(new java.awt.Dimension(30, 20));
        jTextFieldTaxFileNumber2.setInputVerifier(IntegerInputVerifier
                .getInstance());
        jTextFieldTaxFileNumber2.setMinimumSize(new java.awt.Dimension(30, 20));
        jTextFieldTaxFileNumber2
                .addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        jTextFieldTaxFileNumber2KeyTyped(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTFN.add(jTextFieldTaxFileNumber2, gridBagConstraints);

        jTextFieldTaxFileNumber3
                .setPreferredSize(new java.awt.Dimension(30, 20));
        jTextFieldTaxFileNumber3.setInputVerifier(IntegerInputVerifier
                .getInstance());
        jTextFieldTaxFileNumber3.setMinimumSize(new java.awt.Dimension(30, 20));
        jTextFieldTaxFileNumber3
                .addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        jTextFieldTaxFileNumber3KeyTyped(evt);
                    }
                });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelTFN.add(jTextFieldTaxFileNumber3, gridBagConstraints);

        jLabel1.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanelTFN.add(jLabel1, gridBagConstraints);

        jLabel5.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanelTFN.add(jLabel5, gridBagConstraints);

        jPasswordField1.setEditable(false);
        jPasswordField1.setBackground(java.awt.Color.lightGray);
        jPasswordField1.setPreferredSize(new java.awt.Dimension(30, 20));
        jPasswordField1.setMinimumSize(new java.awt.Dimension(30, 20));
        jPasswordField1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelTFN.add(jPasswordField1, gridBagConstraints);

        jPasswordField2.setEditable(false);
        jPasswordField2.setBackground(java.awt.Color.lightGray);
        jPasswordField2.setPreferredSize(new java.awt.Dimension(30, 20));
        jPasswordField2.setMinimumSize(new java.awt.Dimension(30, 20));
        jPasswordField2.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanelTFN.add(jPasswordField2, gridBagConstraints);

        jPasswordField3.setEditable(false);
        jPasswordField3.setBackground(java.awt.Color.lightGray);
        jPasswordField3.setPreferredSize(new java.awt.Dimension(30, 20));
        jPasswordField3.setMinimumSize(new java.awt.Dimension(30, 20));
        jPasswordField3.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanelTFN.add(jPasswordField3, gridBagConstraints);

        jButtonPassword.setText("Password");
        jButtonPassword.setPreferredSize(new java.awt.Dimension(81, 20));
        jButtonPassword.setNextFocusableComponent(jTextFieldTaxFileNumber1);
        jButtonPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonPasswordMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanelTFN.add(jButtonPassword, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelClientHistory.add(jPanelTFN, gridBagConstraints);

        jLabelResidenceCountry.setText("Residence Country");
        jLabelResidenceCountry
                .setPreferredSize(new java.awt.Dimension(110, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelClientHistory.add(jLabelResidenceCountry, gridBagConstraints);

        jComboBoxResidenceCountry.setPreferredSize(new java.awt.Dimension(170,
                20));
        jComboBoxResidenceCountry
                .setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelClientHistory.add(jComboBoxResidenceCountry, gridBagConstraints);

        jPanel4.add(jPanelClientHistory);

        jPanelOccupation.setLayout(new java.awt.GridBagLayout());

        jPanelOccupation.setBorder(new javax.swing.border.TitledBorder(
                "Occupation Details"));
        jLabelOccupation.setText("Occupation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelOccupation.add(jLabelOccupation, gridBagConstraints);

        jLabelEmpStatus.setText("Employment Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelOccupation.add(jLabelEmpStatus, gridBagConstraints);

        jLabelEmpName.setText("Employer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelOccupation.add(jLabelEmpName, gridBagConstraints);

        jComboBoxOccupation.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelOccupation.add(jComboBoxOccupation, gridBagConstraints);

        jTextFieldEmpName.setMaximumSize(new java.awt.Dimension(170, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelOccupation.add(jTextFieldEmpName, gridBagConstraints);

        jComboBoxEmpStatus.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelOccupation.add(jComboBoxEmpStatus, gridBagConstraints);

        jLabelDSSRecipient.setText("Centrelink Recipient");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelOccupation.add(jLabelDSSRecipient, gridBagConstraints);

        jCheckBoxDSSRecipient
                .setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelOccupation.add(jCheckBoxDSSRecipient, gridBagConstraints);

        jPanel4.add(jPanelOccupation);

        jPanelClientDetails.add(jPanel4);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelAdvisor.setLayout(new java.awt.GridBagLayout());

        jPanelAdvisor.setBorder(new javax.swing.border.TitledBorder(
                "Adviser/Services"));
        jLabelClientActive.setText("ClientView Active");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAdvisor.add(jLabelClientActive, gridBagConstraints);

        jCheckBoxClientActive
                .setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelAdvisor.add(jCheckBoxClientActive, gridBagConstraints);

        jLabelAdviser.setText("Adviser");
        jLabelAdviser.setPreferredSize(new java.awt.Dimension(110, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAdvisor.add(jLabelAdviser, gridBagConstraints);

        jComboBoxAdviser.setPreferredSize(new java.awt.Dimension(170, 20));
        jComboBoxAdviser.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelAdvisor.add(jComboBoxAdviser, gridBagConstraints);

        jLabelFeeDate.setText("Date next Fee is due");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAdvisor.add(jLabelFeeDate, gridBagConstraints);

        jLabelReviewDate.setText("Date for Next Review");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelAdvisor.add(jLabelReviewDate, gridBagConstraints);

        jTextFieldFeeDate.setInputVerifier(DateInputVerifier.getInstance());
        jTextFieldFeeDate.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldFeeDate.setMinimumSize(new java.awt.Dimension(100, 20));
        jTextFieldFeeDate.setNextFocusableComponent(jTextFieldReviewDate);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelAdvisor.add(jTextFieldFeeDate, gridBagConstraints);

        jTextFieldReviewDate.setPreferredSize(new java.awt.Dimension(100, 21));
        jTextFieldReviewDate.setMinimumSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelAdvisor.add(jTextFieldReviewDate, gridBagConstraints);

        jPanel3.add(jPanelAdvisor);

        jPanelCurrentHealth.setLayout(new java.awt.GridBagLayout());

        jPanelCurrentHealth.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Current Health"));
        jLabelAgeNextBirthday.setText("Age Next Birthday");
        jLabelAgeNextBirthday.setPreferredSize(new java.awt.Dimension(110, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelCurrentHealth.add(jLabelAgeNextBirthday, gridBagConstraints);

        jTextFieldAgeNextBirthday.setEditable(false);
        jTextFieldAgeNextBirthday
                .setFont(new java.awt.Font("SansSerif", 1, 11));
        jTextFieldAgeNextBirthday.setBackground(java.awt.Color.lightGray);
        jTextFieldAgeNextBirthday
                .setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextFieldAgeNextBirthday.setPreferredSize(new java.awt.Dimension(20,
                20));
        jTextFieldAgeNextBirthday
                .setMinimumSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelCurrentHealth.add(jTextFieldAgeNextBirthday, gridBagConstraints);

        jLabelHospitalCover.setText("Hospital Cover");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelCurrentHealth.add(jLabelHospitalCover, gridBagConstraints);

        jCheckBoxHospitalCover
                .setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelCurrentHealth.add(jCheckBoxHospitalCover, gridBagConstraints);

        jLabelSmoker.setText("Smoker");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelCurrentHealth.add(jLabelSmoker, gridBagConstraints);

        jComboBoxSmoker.setPreferredSize(new java.awt.Dimension(50, 20));
        jComboBoxSmoker.setMinimumSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelCurrentHealth.add(jComboBoxSmoker, gridBagConstraints);

        jLabelStateOfHealth.setText("State of Health");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelCurrentHealth.add(jLabelStateOfHealth, gridBagConstraints);

        jComboBoxStateOfHealth
                .setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelCurrentHealth.add(jComboBoxStateOfHealth, gridBagConstraints);

        jPanel3.add(jPanelCurrentHealth);

        jPanelTrustDIY.setLayout(new java.awt.GridBagLayout());

        jPanelTrustDIY.setBorder(new javax.swing.border.TitledBorder(
                "Trust, Company, DIY"));
        jLabelTrustStatus.setText("Trust Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTrustDIY.add(jLabelTrustStatus, gridBagConstraints);

        jLabelDIYStatus.setText("SMSF Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTrustDIY.add(jLabelDIYStatus, gridBagConstraints);

        jLabelCompanyStatus.setText("Company Status");
        jLabelCompanyStatus.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        jPanelTrustDIY.add(jLabelCompanyStatus, gridBagConstraints);

        jComboBoxTrustStatus.setPreferredSize(new java.awt.Dimension(150, 21));
        jComboBoxTrustStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelTrustDIY.add(jComboBoxTrustStatus, gridBagConstraints);

        jComboBoxCompanyStatus
                .setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxCompanyStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelTrustDIY.add(jComboBoxCompanyStatus, gridBagConstraints);

        jComboBoxDIYStatus.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxDIYStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanelTrustDIY.add(jComboBoxDIYStatus, gridBagConstraints);

        jPanel3.add(jPanelTrustDIY);

        jPanelClientDetails.add(jPanel3);

        jTabbedPane.addTab("ClientView Details", null, jPanelClientDetails, "");

        jPanelContactDetails.setLayout(new javax.swing.BoxLayout(
                jPanelContactDetails, javax.swing.BoxLayout.X_AXIS));

        jPanelAddresses.setLayout(new javax.swing.BoxLayout(jPanelAddresses,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelContactDetails.add(jPanelAddresses);

        jPanelContact.setLayout(new javax.swing.BoxLayout(jPanelContact,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelHome.setLayout(new javax.swing.BoxLayout(jPanelHome,
                javax.swing.BoxLayout.X_AXIS));

        jPanelHome.setBorder(new javax.swing.border.TitledBorder("Home"));
        jPanelContact.add(jPanelHome);

        jPanelWork.setLayout(new javax.swing.BoxLayout(jPanelWork,
                javax.swing.BoxLayout.Y_AXIS));

        jPanelWork.setBorder(new javax.swing.border.TitledBorder("Work"));
        jPanelContact.add(jPanelWork);

        jPanelContactDetails.add(jPanelContact);

        jTabbedPane.addTab("Contact Details", null, jPanelContactDetails, "");

        add(jTabbedPane);

    }// GEN-END:initComponents

    private void jComboBoxResidenceStatusActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBoxResidenceStatusActionPerformed
        if (evt.getSource() != jComboBoxResidenceStatus)
            return;

        Integer residenceStatusCode = new ResidenceStatusCode().getCodeID(""
                + jComboBoxResidenceStatus.getSelectedItem());

        CountryCode cc = new CountryCode();
        jComboBoxResidenceCountry.setSelectedItem(ResidenceStatusCode.RESIDENT
                .equals(residenceStatusCode) ? cc
                .getCodeDescription(ICountry.AUSTRALIA_ID) : CountryCode.NONE);

    }// GEN-LAST:event_jComboBoxResidenceStatusActionPerformed

    private void jTextFieldTaxFileNumber3KeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextFieldTaxFileNumber3KeyTyped
        // Add your handling code here:
        char code = evt.getKeyChar();
        if (!Character.isDigit(code))
            return;

        String input = jTextFieldTaxFileNumber3.getText();
        if (input != null && input.length() == 2)
            jCheckBoxHospitalCover.requestFocus();

    }// GEN-LAST:event_jTextFieldTaxFileNumber3KeyTyped

    private void jTextFieldTaxFileNumber2KeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextFieldTaxFileNumber2KeyTyped
        // Add your handling code here:
        char code = evt.getKeyChar();
        if (!Character.isDigit(code))
            return;

        String input = jTextFieldTaxFileNumber2.getText();
        if (input != null && input.length() == 2)
            jTextFieldTaxFileNumber3.requestFocus();

    }// GEN-LAST:event_jTextFieldTaxFileNumber2KeyTyped

    private void jTextFieldTaxFileNumber1KeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextFieldTaxFileNumber1KeyTyped
        // Add your handling code here:
        char code = evt.getKeyChar();
        if (!Character.isDigit(code))
            return;

        String input = jTextFieldTaxFileNumber1.getText();
        if (input != null && input.length() == 2)
            jTextFieldTaxFileNumber2.requestFocus();

    }// GEN-LAST:event_jTextFieldTaxFileNumber1KeyTyped

    private void jButtonPasswordMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButtonPasswordMouseClicked
        // Add your handling code here:
        try {
            String sPassword = inputPassword();
            if ((personForPassword != null)
                    && ((ClientService) personForPassword)
                            .validatePassword(sPassword)) {
                setJPasswordFieldVisible(false);
                setJTextFieldTaxFileNumberVisible(true);
                this.repaint();

            } else {
                setJTextFieldTaxFileNumberVisible(false);
                setJPasswordFieldVisible(true);

            }
        } catch (com.argus.financials.api.ServiceException e) {
            e.printStackTrace(System.err);
        }
    }// GEN-LAST:event_jButtonPasswordMouseClicked

    private void jButtonAddOccupationActionPerformed(
            java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAddOccupationActionPerformed
        addNewOccupation(); // modal dialog
    }// GEN-LAST:event_jButtonAddOccupationActionPerformed

    private void jTextFieldDateOfBirthFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldDateOfBirthFocusLost
        setAge(jTextFieldDateOfBirth.getText());
        updateDOB();
    }// GEN-LAST:event_jTextFieldDateOfBirthFocusLost

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jTabbedPaneStateChanged
        updateComponents();
    }// GEN-LAST:event_jTabbedPaneStateChanged

    // overrite Container method to allow design time UI development
    public java.awt.Component add(java.awt.Component comp) {
        return thisCTOR ? jPanelDetails.add(comp) : super.add(comp);
        // return jPanelDetails.add( comp ); // ??? called from base class ctor
    }

    // public void add( java.awt.Component comp, Object constraints ) {
    // jPanelDetails.add( comp, constraints );
    // }

    // has to be overriden in derived classes
    public boolean isModified() {
        return super.isModified();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jTextFieldTaxFileNumber1;

    protected javax.swing.JCheckBox jCheckBoxClientActive;

    private javax.swing.JComboBox jComboBoxOccupation;

    private javax.swing.JLabel jLabelAgeNextBirthday;

    private javax.swing.JButton jButtonPassword;

    private javax.swing.JLabel jLabelTrustStatus;

    protected javax.swing.JCheckBox jCheckBoxDSSRecipient;

    private javax.swing.JTextField jTextFieldEmpName;

    private javax.swing.JComboBox jComboBoxDIYStatus;

    private javax.swing.JPanel jPanelTrustDIY;

    private javax.swing.JLabel jLabelFeeDate;

    private javax.swing.JComboBox jComboBoxResidenceStatus;

    private javax.swing.JLabel jLabelTaxFileNumber;

    private javax.swing.JPanel jPanelName;

    private javax.swing.JLabel jLabelDateOfBirth;

    private javax.swing.JLabel jLabelCompanyStatus;

    private javax.swing.JLabel jLabelOccupation;

    protected javax.swing.JLabel jLabelClientActive;

    private javax.swing.JLabel jLabelStateOfHealth;

    private javax.swing.JPanel jPanelClientHistory;

    private javax.swing.JPanel jPanelContactDetails;

    private javax.swing.JLabel jLabelSmoker;

    private javax.swing.JLabel jLabelEmpStatus;

    private javax.swing.JTextField jTextFieldAgeNextBirthday;

    private com.argus.bean.FDateChooser jTextFieldFeeDate;

    private javax.swing.JLabel jLabelDSSRecipient;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JComboBox jComboBoxResidenceCountry;

    private javax.swing.JPasswordField jPasswordField3;

    private javax.swing.JPasswordField jPasswordField2;

    private javax.swing.JPasswordField jPasswordField1;

    protected javax.swing.JCheckBox jCheckBoxHospitalCover;

    private javax.swing.JPanel jPanelClientDetails;

    private javax.swing.JPanel jPanelTFN;

    private javax.swing.JTextField jTextFieldAge;

    private javax.swing.JTabbedPane jTabbedPane;

    protected com.argus.bean.FDateChooser jTextFieldDateOfBirth;

    private javax.swing.JComboBox jComboBoxEmpStatus;

    protected javax.swing.JPanel jPanelAdvisor;

    private javax.swing.JComboBox jComboBoxSmoker;

    private javax.swing.JPanel jPanelCurrentHealth;

    protected javax.swing.JComboBox jComboBoxAdviser;

    private javax.swing.JPanel jPanelOccupation;

    private javax.swing.JPanel jPanelWork;

    private javax.swing.JPanel jPanelHome;

    private javax.swing.JLabel jLabelAdviser;

    private javax.swing.JLabel jLabelReviewDate;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabelResidenceStatus;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JComboBox jComboBoxCompanyStatus;

    private javax.swing.JPanel jPanelAddresses;

    private com.argus.bean.FDateChooser jTextFieldReviewDate;

    private javax.swing.JLabel jLabelEmpName;

    private javax.swing.JLabel Age;

    private javax.swing.JComboBox jComboBoxStateOfHealth;

    private javax.swing.JPanel jPanelContact;

    protected javax.swing.JLabel jLabelHospitalCover;

    private javax.swing.JComboBox jComboBoxTrustStatus;

    private javax.swing.JLabel jLabelDIYStatus;

    private javax.swing.JLabel jLabelResidenceCountry;

    private javax.swing.JTextField jTextFieldTaxFileNumber3;

    private javax.swing.JTextField jTextFieldTaxFileNumber2;

    // End of variables declaration//GEN-END:variables

    public void doSave(java.awt.event.ActionEvent evt) {
        try {
            saveView();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    protected void doNext(java.awt.event.ActionEvent evt) {
        if (jButtonNext.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() + 1);
    }

    protected void doPrevious(java.awt.event.ActionEvent evt) {
        if (jButtonPrevious.isEnabled())
            jTabbedPane.setSelectedIndex(jTabbedPane.getSelectedIndex() - 1);
    }

    /**
     * helper method
     */
    private void setAge(java.util.Date dateOfBirth) {
        Double age = DateTimeUtils.getAgeDouble(dateOfBirth);

        if (dateOfBirth == null || age == null) {
            jTextFieldDateOfBirth.setText(null);
            jTextFieldAge.setText(null);
            jTextFieldAgeNextBirthday.setText(null);
        } else {
            jTextFieldDateOfBirth.setText(DateTimeUtils.asString(dateOfBirth,
                    null));
            jTextFieldAge.setText("" + age.intValue());
            jTextFieldAgeNextBirthday.setText("" + (age.intValue() + 1));
        }
    }

    private void setAge(String dateOfBirth) {
        setAge(DateTimeUtils.getDate(dateOfBirth));
    }

    private void updateComponents() {
        jButtonPrevious.setEnabled(jTabbedPane.getSelectedIndex() > 0);
        jButtonNext.setEnabled(jTabbedPane.getSelectedIndex() < jTabbedPane
                .getTabCount() - 1);
    }

    protected javax.swing.JTabbedPane getJTabbedPane() {
        ;
        return jTabbedPane;
    }

    public void setPage(int index) {
        if (index < 0)
            return;
        if (index >= jTabbedPane.getTabCount())
            return;

        jTabbedPane.setSelectedIndex(index);
    }

    /**
     * 
     */
    public void updateView() throws com.argus.financials.api.ServiceException,
            InvalidCodeException {
    }

    public void saveView() throws com.argus.financials.api.ServiceException,
            InvalidCodeException {
    }

    public void updateView(PersonService person) throws com.argus.financials.api.ServiceException {

        clearView();

        if (person instanceof ClientService) {
            jTextFieldFeeDate.setText(DateTimeUtils
                    .asString(((ClientService) person).getFeeDate()));
            jTextFieldReviewDate.setText(DateTimeUtils
                    .asString(((ClientService) person).getReviewDate()));
        }
        personForPassword = person;
        Integer id = null;
        IPerson personName = person.getPersonName();
        IPersonHealth personHealth = personName == null ? null : personName.getPersonHealth();
        IPersonTrustDIYStatus personTrustDIYStatus = personName == null ? null : personName.getPersonTrustDIYStatus();

        // name
        personNameView.setObject(personName);
        personNameView.updateView(person);

        // addresses
        AddressDto residentialAddress = person.getResidentialAddress();
        residentialAddressView.setObject(residentialAddress);
        residentialAddress.addChangeListener(residentialAddressView);
        residentialAddressView
                .setSameAsVisible(!(person instanceof ClientService));
        residentialAddressView.updateView();

        AddressDto postalAddress = person.getPostalAddress();
        postalAddressView.setObject(postalAddress);
        postalAddress.addChangeListener(postalAddressView);
        residentialAddress.addChangeListener(postalAddress);
        postalAddressView.updateView();

        // client history
        setAge(personName.getDateOfBirth());

        // TFN
        String value = personName.getTaxFileNumber();
        if (value == null || value.equals("--")) {
            jTextFieldTaxFileNumber1.setText(null);
            jTextFieldTaxFileNumber2.setText(null);
            jTextFieldTaxFileNumber3.setText(null);
        } else {
            String first = null;
            String second = null;
            String third = null;

            String tmp = "";
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (!Character.isDigit(c))
                    continue;
                // if ( "0123456789".indexOf(c) < 0 ) continue;
                tmp += c;

                if (tmp.length() == 3) {
                    if (first == null)
                        first = tmp;
                    else if (second == null)
                        second = tmp;
                    else if (third == null)
                        third = tmp;
                    tmp = "";
                }

            }

            if (first != null && second != null && third != null) {
                jTextFieldTaxFileNumber1.setText(first);
                jTextFieldTaxFileNumber2.setText(second);
                jTextFieldTaxFileNumber3.setText(third);

                jPasswordField1.setText(first);
                jPasswordField2.setText(second);
                jPasswordField3.setText(third);

                setJTextFieldTaxFileNumberVisible(false);
                setJPasswordFieldVisible(true);
            } else {
                setJTextFieldTaxFileNumberVisible(true);
                setJPasswordFieldVisible(false);
            }

        }

        id = personName.getResidenceStatusCodeId();
        jComboBoxResidenceStatus.setSelectedItem(new ResidenceStatusCode().getCodeDescription(id));

        ICountry residenceCountry = personName.getResidenceCountry();
        jComboBoxResidenceCountry.setSelectedItem(residenceCountry == null ? null : residenceCountry.getCode());

        // PERSON HEALTH
        boolean smoker = personHealth == null ? false : personHealth.isSmoker();
        jComboBoxSmoker.setSelectedItem(smoker ? BooleanCode.rcYES : BooleanCode.rcNO);

        jCheckBoxHospitalCover.setSelected(personHealth == null ? false : personHealth.isHospitalCover());
        jCheckBoxDSSRecipient.setSelected(personName.isDssRecipient());

        if (personHealth != null) {
            id = personHealth.getHealthStateCodeId();
            jComboBoxStateOfHealth.setSelectedItem(new HealthStateCode().getCodeDescription(id));
        }

        // PERSON TRUST DIY COMPANY STATUS
        if (personTrustDIYStatus != null) {
            id = personTrustDIYStatus.getTrustStatusCodeId();
            jComboBoxTrustStatus.setSelectedItem(new StatusCode().getCodeDescription(id));
            id = personTrustDIYStatus.getDIYStatusCodeId();
            jComboBoxDIYStatus.setSelectedItem(new StatusCode().getCodeDescription(id));
            id = personTrustDIYStatus.getCompanyStatusCodeId();
            jComboBoxCompanyStatus.setSelectedItem(new StatusCode().getCodeDescription(id));
        }

        // occupation
        IOccupation occupation = personName.getOccupation();
        if (occupation == null) {
            SwingUtil.clear(jPanelOccupation);
        } else {
            id = occupation.getOccupationCodeId();
            jComboBoxOccupation.setSelectedItem(new OccupationCode().getCodeDescription(id));
            id = occupation.getEmploymentStatusCodeId();
            jComboBoxEmpStatus.setSelectedItem(new EmploymentStatusCode().getCodeDescription(id));
        }

        IBusiness business = person.getEmployerBusiness();
        jTextFieldEmpName.setText(business == null ? null : business.getLegalName());

        // do full refresh first
        Map map = person.getContactMedia(Boolean.TRUE);

        phoneHome.setObject(map == null ? null : (ContactMedia) map
                .get(phoneHome.getContactMediaCodeID()));
        phoneHome.updateView(person);

        phoneWork.setObject(map == null ? null : (ContactMedia) map
                .get(phoneWork.getContactMediaCodeID()));
        phoneWork.updateView(person);

        phoneMobile.setObject(map == null ? null : (ContactMedia) map
                .get(phoneMobile.getContactMediaCodeID()));
        phoneMobile.updateView(person);

        faxHome.setObject(map == null ? null : (ContactMedia) map.get(faxHome
                .getContactMediaCodeID()));
        faxHome.updateView(person);

        faxWork.setObject(map == null ? null : (ContactMedia) map.get(faxWork
                .getContactMediaCodeID()));
        faxWork.updateView(person);

        emailHome.setObject(map == null ? null : (ContactMedia) map
                .get(emailHome.getContactMediaCodeID()));
        emailHome.updateView(person);

        emailWork.setObject(map == null ? null : (ContactMedia) map
                .get(emailWork.getContactMediaCodeID()));
        emailWork.updateView(person);

        // and finally
        primaryKey = person.getId();

        jTextFieldEmpName
                .setToolTipText(person.getEmployerBusiness() == null ? ""
                        : person.getEmployerBusiness().getLegalName());

        updateDOB();

    }

    public void saveView(PersonService person) throws com.argus.financials.api.ServiceException {

        String s = null;
        Integer n = null;

        if (person instanceof ClientService) {
            ClientService clientService = (ClientService) person;
            clientService.setFeeDate(DateTimeUtils.getDate(jTextFieldFeeDate.getText()));
            clientService.setReviewDate(DateTimeUtils.getDate(jTextFieldReviewDate.getText()));
        }
        // name
        personNameView.saveView(person);
        IPerson personName = (IPerson) personNameView.getObject();
        person.setPersonName(personName);

        IPersonHealth personHealth = personName == null ? null : personName.getPersonHealth();
        IPersonTrustDIYStatus personTrustDIYStatus = personName == null ? null : personName.getPersonTrustDIYStatus();

        // addresses
        residentialAddressView.saveView();
        person.setResidentialAddress((AddressDto) residentialAddressView.getObject());

        postalAddressView.saveView();
        person.setPostalAddress((AddressDto) postalAddressView.getObject());

        // client history
        Date dob = DateTimeUtils.getSqlDate(jTextFieldDateOfBirth.getText());
        personName.setDateOfBirth(dob);

        String delim = "-";
        String first = jTextFieldTaxFileNumber1.getText();
        String second = jTextFieldTaxFileNumber2.getText();
        String third = jTextFieldTaxFileNumber3.getText();
        personName.setTaxFileNumber(
        // first == null || second == null || third == null ? null :
                first + delim + second + delim + third);

        s = (String) jComboBoxResidenceStatus.getSelectedItem();
        personName.setResidenceStatusCodeId(new ResidenceStatusCode().getCodeID(s));

        s = (String) jComboBoxResidenceCountry.getSelectedItem();
        Country residenceCountry = new Country();
        residenceCountry.setCode(s);
        personName.setResidenceCountry(residenceCountry);

        // PERSON HEALTH
        if (personHealth != null) {
            ReferenceCode refCode = (ReferenceCode) jComboBoxSmoker.getSelectedItem();
            personHealth.setSmoker(refCode.equals(BooleanCode.CODE_NONE) ? null
                    : (refCode.equals(BooleanCode.rcYES) ? Boolean.TRUE : Boolean.FALSE));
            personHealth.setHospitalCover(jCheckBoxHospitalCover.isSelected());
            s = (String) jComboBoxStateOfHealth.getSelectedItem();
            personHealth.setHealthStateCodeId(new HealthStateCode().getCodeID(s));
        }
        personName.setDssRecipient(jCheckBoxDSSRecipient.isSelected());

        // PERSON TRUST DIY COMPANY STATUS
        if (personTrustDIYStatus != null) {
            s = (String) jComboBoxTrustStatus.getSelectedItem();
            personTrustDIYStatus.setTrustStatusCodeId(new StatusCode().getCodeID(s));
            s = (String) jComboBoxDIYStatus.getSelectedItem();
            personTrustDIYStatus.setDIYStatusCodeId(new StatusCode().getCodeID(s));
            s = (String) jComboBoxCompanyStatus.getSelectedItem();
            personTrustDIYStatus.setCompanyStatusCodeId(new StatusCode().getCodeID(s));
        }

        // occupation
        IOccupation occupation = personName.getOccupation();
        if (occupation == null) {
            occupation = new Occupation();
            personName.setOccupation(occupation);
        }

        s = (String) jComboBoxOccupation.getSelectedItem();
        occupation.setOccupationCodeId(new OccupationCode().getCodeID(s));

        s = (String) jComboBoxEmpStatus.getSelectedItem();
        occupation.setEmploymentStatusCodeId(new EmploymentStatusCode().getCodeID(s));

        IBusiness business = person.getEmployerBusiness();
        if (business != null) {
            business.setLegalName(jTextFieldEmpName.getText());
            business.setTradingName(jTextFieldEmpName.getText());
        }

        // do not do full refresh
        HashMap map = person.getContactMedia(Boolean.FALSE);
        if (map == null)
            map = new HashMap();

        phoneHome.saveView(person);
        map.put(phoneHome.getContactMediaCodeID(), phoneHome.getObject());

        phoneWork.saveView(person);
        map.put(phoneWork.getContactMediaCodeID(), phoneWork.getObject());

        phoneMobile.saveView(person);
        map.put(phoneMobile.getContactMediaCodeID(), phoneMobile.getObject());

        faxHome.saveView(person);
        map.put(faxHome.getContactMediaCodeID(), faxHome.getObject());

        faxWork.saveView(person);
        map.put(faxWork.getContactMediaCodeID(), faxWork.getObject());

        emailHome.saveView(person);
        map.put(emailHome.getContactMediaCodeID(), emailHome.getObject());

        emailWork.saveView(person);
        map.put(emailWork.getContactMediaCodeID(), emailWork.getObject());

        person.setContactMedia(map);

    }

    public void clearView() {

    }

    public Integer getObjectType() {
        return new Integer(ObjectTypeConstant.PERSON);
    }

    // modal dialog to get new occupation
    private void addNewOccupation() {

        String occupation = JOptionPane.showInputDialog(this,
                "Please input a value", "Add new occupation",
                JOptionPane.QUESTION_MESSAGE);

        if (occupation == null || occupation.length() == 0)
            return;

        try {
            utilityService.addCode(OccupationCode.OCCUPATION_TABLE, occupation);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    // modal dialog to get new occupation
    private String inputPassword() {

        String sPasswordMessage = "";
        JOptionPane iFeelYourPane = new JOptionPane();
        Object[] oMessageAndInput = new Object[2];
        oMessageAndInput[0] = sPasswordMessage;
        oMessageAndInput[1] = new JPasswordField();

        iFeelYourPane.showOptionDialog(this, oMessageAndInput,
                "Please Input Password", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);

        return Digest.digest(String
                .copyValueOf(((JPasswordField) oMessageAndInput[1])
                        .getPassword()));

    }

    private void setJTextFieldTaxFileNumberVisible(boolean value) {
        jTextFieldTaxFileNumber1.setVisible(value);
        jTextFieldTaxFileNumber2.setVisible(value);
        jTextFieldTaxFileNumber3.setVisible(value);
    }

    private void setJPasswordFieldVisible(boolean value) {
        jPasswordField1.setVisible(value);
        jPasswordField2.setVisible(value);
        jPasswordField3.setVisible(value);
    }

    protected void updateDOB() {
    }

}
