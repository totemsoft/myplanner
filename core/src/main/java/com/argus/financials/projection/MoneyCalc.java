/*
 * MoneyCalc.java
 *
 * Created on 17 October 2001, 11:31
 */

package com.argus.financials.projection;

/**
 * 
 * @author valeri chibaev
 * @version
 */

import java.math.BigDecimal;

import com.argus.financials.bean.Financial;
import com.argus.financials.code.ModelType;
import com.argus.financials.code.SexCode;
import com.argus.financials.etc.ActionEventID;
import com.argus.financials.projection.data.ETPConstants;
import com.argus.financials.projection.save.Model;
import com.argus.financials.projection.save.ModelCollection;
import com.argus.financials.service.PersonService;
import com.argus.financials.service.ServiceLocator;
import com.argus.financials.utils.RateUtils;
import com.argus.format.Currency;
import com.argus.format.Number2;
import com.argus.format.Percent;
import com.argus.util.DateTimeUtils;
import com.argus.util.ReferenceCode;

public abstract class MoneyCalc implements ActionEventID,
        javax.swing.event.ChangeListener, javax.swing.event.DocumentListener,
        DocumentNames, java.awt.event.ItemListener, ETPConstants {

    /**
     * CONST
     */
    public static final int MONTHS_PER_YEAR = DateTimeUtils.MONTHS_PER_YEAR;

    public static final int SECONDS_PER_DAY = DateTimeUtils.SECONDS_PER_DAY;

    public static final int MILLIS_PER_DAY = DateTimeUtils.MILLIS_PER_DAY;

    public static final double MILLIS_PER_YEAR = DateTimeUtils.MILLIS_PER_YEAR;

    public static final int WEEKS_PER_YEAR = DateTimeUtils.WEEKS_PER_YEAR;

    public static final int FORTNIGHTS_PER_YEAR = DateTimeUtils.FORTNIGHTS_PER_YEAR;

    public static final BigDecimal ZERO = new BigDecimal(0);

    private Object lastUpdateObject;

    /**
     * input params
     */
    private Integer sexCodeID;

    private double initial; // current value ($)

    private double regularContribution; // regularContribution ($)

    private boolean indexed;

    private double indexRate; // indexation 0.025 (2.5% p.a.)

    private double taxRate;

    private double years; // do calc for N years

    private java.util.Date dateOfBirth;

    // used in calculations
    protected double targetValue; // last not 0.0

    protected double[] targetValues; // target value after X years ($)

    // calc storage
    // this value has to be automaticly reset to false in getTargetValues()
    protected boolean modified = false;

    private boolean toBeSaved = false;

    private boolean enableUpdate = true;

    private Model model;

    private GraphData graphData = new GraphData();

    private static Number2 number = Number2.getNumberInstance();

    private static Percent percent = Percent.getPercentInstance();

    private static Currency currency = Currency.getCurrencyInstance();

    public static Number2 getNumberInstance() {
        return number;
    }

    public static Percent getPercentInstance() {
        return percent;
    }

    public static Currency getCurrencyInstance() {
        return currency;
    }

    /***************************************************************************
     * Creates new MoneyCalc
     **************************************************************************/
    protected MoneyCalc() {
        super();
        _clear();
    }

    private static javax.swing.undo.UndoManager undoManager = new javax.swing.undo.UndoManager();

    public static javax.swing.undo.UndoManager getUndoManager() {
        return undoManager;
    }

    protected static void undo() {
        try {
            if (undoManager.canUndo())
                undoManager.undo();
        } catch (javax.swing.undo.CannotUndoException e) {
            e.printStackTrace(System.err);
        } catch (java.lang.IllegalStateException e) {
            e.printStackTrace(System.err);
        }
    }

    public Model getModel() {
        if (model == null) {
            model = new Model();

            model.setTypeID(getDefaultModelType());

            PersonService person = ServiceLocator.getInstance().getClientPerson();
            if (person != null) {
                try {
                    ModelCollection models = person.getModels();
                    models.addModel(model);
                } catch (com.argus.financials.service.client.ServiceException e) {
                    e.printStackTrace(System.err);
                }
            }

        }

        return model;
    }

    public void setModel(Model value) {
        if (value == null) {
            this.model = null;
            // this.clear();
            // this.assign( this.clone() );
            return;
        }
        if (value.getData() == null)
            return;

        java.io.StringReader r = new java.io.StringReader(value.getData());
        java.io.BufferedReader input = new java.io.BufferedReader(r);

        java.util.Properties properties = new java.util.Properties();

        try {
            String name;
            while ((name = input.readLine()) != null) {
                if (name.startsWith("<") || name.equals("false")) {
                    name = input.readLine();
                    continue;
                }

                String nameValue = input.readLine();
                properties.put(name, nameValue == null ? "" : nameValue);
            }

        } catch (java.io.IOException e) {
            System.err.println("MoneyCalc::setModel(...) " + e.getMessage());
        }

        java.util.Iterator iter = properties.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            String property = (String) entry.getKey();
            String v = (String) entry.getValue();

            if (!update(property, v))
                graphData.update(property, v);
        }

        this.model = value;
        this.setSaved();

    }

    public abstract Integer getDefaultModelType();

    public String getDefaultTitle() {
        return "New projection";
    }

    /***************************************************************************
     * implementation of java.awt.event.ItemListener interface
     */
    public void itemStateChanged(java.awt.event.ItemEvent evt) {
        update(evt.getItemSelectable(), evt.getItem());
    }

    public boolean update(java.awt.ItemSelectable itemSelectable, Object item) {

        Object property = ((javax.accessibility.Accessible) itemSelectable)
                .getAccessibleContext().getAccessibleName();
        if (property == null)
            return false;

        lastUpdateObject = property;

        Object[] selectedObjects = itemSelectable.getSelectedObjects();
        if (selectedObjects == null || selectedObjects.length == 0)
            return false;

        Object value = selectedObjects[0];
        if (value instanceof ReferenceCode)
            value = "" + ((ReferenceCode) value).getCodeID();
        else if (value instanceof javax.swing.AbstractButton)
            value = "" + ((javax.swing.AbstractButton) value).isSelected();
        else if (item instanceof javax.swing.AbstractButton)
            value = "" + ((javax.swing.AbstractButton) item).isSelected();

        return update(property, value.toString());

    }

    /***************************************************************************
     * implementation of javax.swing.event.DocumentListener interface
     */
    public void removeUpdate(javax.swing.event.DocumentEvent documentEvent) {
        // if (DEBUG) System.out.println( "removeUpdate: " );
        update(documentEvent.getDocument());
    }

    public void insertUpdate(javax.swing.event.DocumentEvent documentEvent) {
        // if (DEBUG) System.out.println( "insertUpdate: " );
        update(documentEvent.getDocument());
    }

    public void changedUpdate(javax.swing.event.DocumentEvent documentEvent) {
        // if (DEBUG) System.out.println( "changedUpdate: " );
        // we won't ever get this with a PlainDocument
        // Plain text components don't fire this event
    }

    public boolean update(javax.swing.text.Document doc) {

        if (!check(doc))
            return false;

        Object property = doc.getProperty(NAME);
        if (property == null)
            return false;

        lastUpdateObject = property;

        String value = null;
        try {
            value = getValue(doc);
        } catch (javax.swing.text.BadLocationException e) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            e.printStackTrace();
            return false;
        }

        return update(property, value);

    }

    public Object getLastUpdateObject() {
        return lastUpdateObject;

    }

    public boolean check(javax.swing.text.Document doc) {
        return Boolean.TRUE.equals(doc.getProperty(READY));
    }

    protected String getValue(javax.swing.text.Document doc)
            throws javax.swing.text.BadLocationException {
        return doc.getText(doc.getStartPosition().getOffset(), doc
                .getEndPosition().getOffset());
    }

    public void enableUpdate() {
        enableUpdate = true;
    }

    public void disableUpdate() {
        enableUpdate = false;
    }

    public void doUpdate() {
        setModified();
    } // force update

    protected boolean update(Object property, String value) {

        double d = 0.;// UNKNOWN_VALUE;

        if (property.equals(DOB)) {
            setDateOfBirth(DateTimeUtils.getDate(value));

        } else if (property.equals(SEX_CODE)) {
            if (number.isValid(value))
                setSexCodeID(new Integer(value));
            else
                setSexCodeID(new SexCode().getCodeID(value));

        } else if (property.equals(SEX_CODE_MALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setSexCodeID(SexCode.MALE);

        } else if (property.equals(SEX_CODE_FEMALE)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setSexCodeID(SexCode.FEMALE);

        } else if (property.equals(TAX_RATE)) {
            if (value != null && value.length() > 0)
                d = percent.doubleValue(value);
            setTaxRate(d);

        } else if (property.equals(INDEX_RATE)) {
            if (value != null && value.length() > 0)
                d = percent.doubleValue(value);
            setIndexRate(d);

        } else if (property.equals(INDEXED)) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(value))
                setIndexed(true);
            else
                setIndexed(false);

        } else
            return false;

        return true;

    }

    public String toString() {
        return DOB + '=' + DateTimeUtils.getTimeInMillis(getDateOfBirth())
                + '\n' + SEX_CODE + '=' + getSexCodeID() + '\n' + INDEXED + '='
                + isIndexed() + '\n' + INDEX_RATE + '=' + getIndexRate() + '\n'
                + TAX_RATE + '=' + getIndexRate();
    }

    /***************************************************************************
     * helper methods
     */
    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    public double getCompoundedAmount(double valueNow, double yearsValue) {
        return getCompoundedAmount(valueNow, getIndexRate(), yearsValue);
    }

    public static double getCompoundedAmount(double valueNow, double index,
            double yearsValue) {
        return RateUtils
                .getCompoundedAmount(valueNow, index / 100., yearsValue);
    }

    public static BigDecimal getCompoundedAmount(BigDecimal valueNow,
            BigDecimal index, double yearsValue) {
        if (valueNow == null || valueNow.doubleValue() == 0.)
            return ZERO;
        if (index == null || index.doubleValue() == 0.)
            return valueNow;
        return new BigDecimal(getCompoundedAmount(valueNow.doubleValue(), index
                .doubleValue(), yearsValue));
    }

    public static double vLookUp(ETPCalcNew etpCalc, boolean postTaxed,
            int optionID, double value, boolean notMatch) {
        if (postTaxed) {
            switch (optionID) {
            case 1:
                break;
            case 2:
                if (notMatch) {
                    if (etpCalc.getAge() < TAX_EFFECTIVE_AGE) {
                        if (value <= -10000000)
                            return 0;
                        else if (value <= -0.00001)
                            return 0;
                        else if (value <= etpCalc.getThresholdLeft()
                                .doubleValue())
                            return POST_TAXED_UNDER_55_TAX_RATE;
                        else if (value <= 10000000)
                            return POST_TAXED_UNDER_55_TAX_RATE;
                        else if (value > 10000000)
                            return POST_TAXED_UNDER_55_TAX_RATE;
                    } else {
                        if (value <= -10000000)
                            return 0;
                        else if (value <= -0.00001)
                            return 0;
                        else if (value <= etpCalc.getThresholdLeft()
                                .doubleValue())
                            return 0;
                        else if (value <= 10000000)
                            return THRESHOLD_TAX_RATE2;
                        else if (value > 10000000)
                            return THRESHOLD_TAX_RATE2;
                    }

                } else {
                    if (etpCalc.getAge() < TAX_EFFECTIVE_AGE)
                        return POST_TAXED_UNDER_55_TAX_RATE;
                    else
                        return THRESHOLD_TAX_RATE2;
                }
                break;
            case 3:
                if (notMatch) {
                    if (etpCalc.getAge() < TAX_EFFECTIVE_AGE) {
                        if (value <= -10000000)
                            return 0;
                        else if (value <= -0.00001)
                            return 0;
                        // else if ( value <=
                        // etpCalc.getThresholdLeft().doubleValue() ) return
                        // POST_TAXED_UNDER_55_TAX_RATE;
                        else if (value <= 9999999)
                            return POST_TAXED_UNDER_55_TAX_RATE;
                        else if (value > 9999999)
                            return POST_TAXED_UNDER_55_TAX_RATE;
                    } else {
                        if (value <= -10000000)
                            return 0;
                        else if (value <= -0.00001)
                            return 0;
                        // else if ( value <=
                        // etpCalc.getThresholdLeft().doubleValue() ) return 0;
                        else if (value <= 9999999)
                            return THRESHOLD_TAX_RATE2;
                        else if (value > 9999999)
                            return THRESHOLD_TAX_RATE2;
                    }

                } else {
                    if (etpCalc.getAge() < TAX_EFFECTIVE_AGE)
                        return POST_TAXED_UNDER_55_TAX_RATE;
                    else
                        return THRESHOLD_TAX_RATE2;
                }
                break;
            case 4:
                if (notMatch) {
                    if (value <= -10000000)
                        return 0;
                    else if (value <= -0.00001)
                        return 0;
                    else if (value <= etpCalc.getThresholdLeft().doubleValue())
                        return 0;
                    else if (value <= 10000000)
                        return THRESHOLD_TAX_RATE2;
                    else if (value > 10000000)
                        return THRESHOLD_TAX_RATE2;
                } else
                    return THRESHOLD_TAX_RATE2;
            default:
                return 0;

            }
        } else {
            switch (optionID) {
            case 1:
                break;
            case 2:
                if (notMatch) {
                    if (etpCalc.getAge() < TAX_EFFECTIVE_AGE) {
                        if (value <= -10000000)
                            return 0;
                        else if (value <= -0.00001)
                            return 0;
                        else if (value <= etpCalc
                                .getThresholdLeftAfterPost061983TaxedDeducted()
                                .doubleValue())
                            return THRESHOLD_TAX_RATE1;
                        else if (value <= 10000000)
                            return THRESHOLD_TAX_RATE1;
                        else if (value > 10000000)
                            return THRESHOLD_TAX_RATE1;
                    } else {
                        if (value <= -10000000)
                            return 0;
                        else if (value <= -0.00001)
                            return 0;
                        else if (value <= etpCalc
                                .getThresholdLeftAfterPost061983TaxedDeducted()
                                .doubleValue())
                            return 0;
                        else if (value <= 10000000)
                            return THRESHOLD_TAX_RATE2;
                        else if (value > 10000000)
                            return THRESHOLD_TAX_RATE1;
                    }

                } else {
                    if (etpCalc.getAge() < TAX_EFFECTIVE_AGE)
                        return THRESHOLD_TAX_RATE1;
                    else
                        return THRESHOLD_TAX_RATE2;
                }
                break;
            case 3:
                break;
            case 4:
            default:
                return 0;

            }

        }
        return 0;

    }

    /***************************************************************************
     * javax.swing.event.ChangeListener interface
     */
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        // default = DO NOTHING
        // derived classes, who is interested in receiving this event
        // has to override this method
    }

    transient private java.util.ArrayList changeListeners;

    // transient private javax.swing.event.EventListenerList changeListeners;

    public void addChangeListener(javax.swing.event.ChangeListener listener) {
        if (changeListeners == null)
            changeListeners = new java.util.ArrayList();
        // changeListeners = new javax.swing.event.EventListenerList();

        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
            // to update this listener state
            if (isReady())
                listener.stateChanged(new javax.swing.event.ChangeEvent(this));
        }
    }

    public void removeChangeListener(javax.swing.event.ChangeListener listener) {
        if (changeListeners == null || !changeListeners.contains(listener))
            return;

        changeListeners.remove(listener);
    }

    protected void notifyChangeListeners(Integer dataChangeFlag) {
        if (changeListeners == null)
            return;
        if (dataChangeFlag.equals(DATA_READY) && !isReady())
            return;

        java.util.Iterator iter = changeListeners.iterator();
        while (iter.hasNext()) {
            // to avoid java.lang.IllegalStateException: Attempt to mutate in
            // notification
            final javax.swing.event.ChangeListener listener = (javax.swing.event.ChangeListener) iter
                    .next();
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.stateChanged(new javax.swing.event.ChangeEvent(
                            MoneyCalc.this));
                }
            });

        }

    }

    /***************************************************************************
     * 
     */
    public void assign(MoneyCalc mc) {

        sexCodeID = mc.sexCodeID;
        initial = mc.initial;
        indexed = mc.indexed;
        indexRate = mc.indexRate;
        taxRate = mc.taxRate;
        years = mc.years;
        regularContribution = mc.regularContribution;

        dateOfBirth = mc.dateOfBirth;

        if (mc.targetValues != null) {
            targetValues = new double[mc.targetValues.length];
            System.arraycopy(mc.targetValues, 0, targetValues, 0,
                    mc.targetValues.length);
        }

    }

    public void clear() {
        _clear();
    }

    private void _clear() {

        sexCodeID = SexCode.MALE;
        initial = 0.;
        indexed = false;
        indexRate = 0.;
        taxRate = 0.;
        years = UNKNOWN_VALUE;
        regularContribution = 0.;

        dateOfBirth = null;

        targetValues = null;

        if (model != null)
            model.clear();

        modified = false;
    }

    protected int mod(int num1, int num2) {
        return com.argus.math.Math.mod(num1, num2);
    }

    protected double mod(double num1, double num2) {
        return com.argus.math.Math.mod(num1, num2);
    }

    /***************************************************************************
     * 
     */
    public boolean isModified() {
        return modified;
    }

    public boolean isReady() {
        return (getInitialValue() >= 0.)
                && (!isIndexed() || (isIndexed() && getIndexRate() >= 0.))
                && (getTaxRate() >= 0.) && (getYears() >= 0.);
    }

    // any call to this method will trigger DATA_MODIFIED event
    // ( even if this object is already modified )
    // N.B. modified flag will be reset in getTargetValues() method
    public void setModified() {
        modified = true;
        toBeSaved = true;

        if (!enableUpdate)
            return;
        if (changeListeners == null)
            return;

        notifyChangeListeners(DATA_MODIFIED);

        // *
        if (isReady()) {
            // notifyChangeListeners( DATA_READY );
        }
        // **/

    }

    public boolean getSaved() {
        return toBeSaved;
    }

    public void setSaved() {
        toBeSaved = false;
    }

    public boolean calculate() {
        return false;
    }

    public GraphData getGraphData() {
        return graphData;
    }

    public int getCharType() {
        return graphData.getCharType();
    }

    public boolean is3DView() {
        return graphData.is3DView();
    }

    public boolean isAntiAliasing() {
        return graphData.isAntiAliasing();
    }

    /***************************************************************************
     * get/set
     */
    public double getInitialValue() {
        return initial;
    }

    public void setInitialValue(double value) {
        if (this.initial == value)
            return;

        initial = value;
        setModified();
    }

    /**
     * Regular contribution amount - this is the same as getDelta(), but I think
     * we should set the method in MoneyCalc rather than lower down in the class
     * hierarchy.
     */
    public double getRegularContribution() {
        return regularContribution;
    }

    public void setRegularContribution(double value) {
        if (this.regularContribution == value)
            return;

        regularContribution = value;
        this.setModified();
    }

    public Integer getSexCodeID() {
        return sexCodeID;
    }

    public void setSexCodeID(Integer value) {
        if (equals(sexCodeID, value))
            return;

        sexCodeID = value;
        setModified();
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean value) {
        if (indexed == value)
            return;

        indexed = value;
        setModified();
    }

    public double getIndexRate() {
        if (isIndexed())
            return indexRate;
        return 0;
    }

    public void setIndexRate(double value) {
        if (indexRate == value)
            return;

        if (value > 100.) {
            undo();
            return;
        }

        indexRate = value;
        setModified();
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double value) {
        if (this.taxRate == value)
            return;

        taxRate = value;
        setModified();
    }

    // years this calculation works
    public int getYearsInt() {
        return (int) Math.ceil(getYears());
    }

    public double getYears() {
        return years;
    }

    public void setYears(double value) {
        if (years == value)
            return;

        years = value;

        if (years > 0)
            targetValues = new double[(int) Math.ceil(years)];
        else
            targetValues = null;

        if (years >= 0)
            setModified(); // protected method
    }

    protected int getAge(java.util.Date dob) {
        Double ageD = DateTimeUtils.getAgeDouble(dob);
        if (ageD == null)
            return (int) UNKNOWN_VALUE;
        return ageD.intValue();
    }

    public int getAge() {
        return getAge(getDateOfBirth());
    }

    public java.util.Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(java.util.Date value) {
        if (equals(dateOfBirth, value))
            return;

        dateOfBirth = value;
        setModified();
    }

    /**
     * 
     */
    public double getTargetValue() {

        // re-calculate
        if (modified && isReady())
            getTargetValues();

        if (targetValues == null) // || targetValues.length == 0 )
            return getInitialValue() < 0 ? 0.0 : getInitialValue();

        return targetValues[targetValues.length - 1];

    }

    public double getValue(int index) {

        // re-calculate
        if (modified && isReady())
            getTargetValues();

        if (targetValues == null)
            return getInitialValue() < 0 ? 0.0 : getInitialValue();

        return (index >= 0 && index < targetValues.length) ? targetValues[index]
                : 0.0;

    }

    protected double[] getTargetValues() {
        return targetValues;
    }

    /**
     * HELPER METHODS
     */
    public static double[] arrayCopy(double[] src, int resizeBy) {

        if (src == null)
            throw new RuntimeException("arrayCopyOffset(null,..)");

        double[] values = new double[src.length + resizeBy];

        System.arraycopy(src, 0, values, 0, src.length);

        for (int i = src.length; i < values.length; i++)
            values[i] = MoneyCalc.HOLE;

        return values;

    }

    public static double[] arrayCopyOffset(double[] src, int offset) {

        if (src == null)
            throw new RuntimeException("arrayCopyOffset(null,..)");

        double[] values = new double[offset + src.length];

        System.arraycopy(src, 0, values, offset, src.length);

        for (int i = 0; i < offset; i++)
            values[i] = MoneyCalc.HOLE;

        return values;

    }

    public static void sum(MoneyCalc src, double[] dest) {

        double[] source = src.getTargetValues();
        if (source == null)
            return;

        // if ( dest.length != source.length )
        // throw new RuntimeException( "dest.length != source.length" );

        for (int i = 0; i < Math.min(dest.length, source.length); i++)
            dest[i] += source[i];

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static MoneyCalc getNewCalculator(Integer typeID) {
        return typeID == null ? null : getNewCalculator(typeID.intValue());
    }

    public static MoneyCalc getNewCalculator(int typeID) {

        MoneyCalc calc = null;

        switch (typeID) {

        case ModelType.iCURRENT_POSITION_CALC:
            calc = new CurrentPositionCalc();
            break;
        case ModelType.iINSURANCE_NEEDS:
            break;
        case ModelType.iPREMIUM_CALC:
            break;
        case ModelType.iINVESTMENT_GEARING:
            calc = new GearingCalc2();
            break;
        case ModelType.iPROJECTED_WEALTH:
            break;
        case ModelType.iINVESTMENT_PROPERTIES:
            break;
        case ModelType.iLOAN_MORTGAGE_CALC:
            //calc = new MortgageCalc();
            try {
                Class clazz = Class.forName("com.argus.financials.ui.projection.MortgageCalc");
                calc = (MoneyCalc) clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            break;
        case ModelType.iALLOCATED_PENSION:
            calc = new AllocatedPensionCalcNew();
            break;
        case ModelType.iETP_ROLLOVER:
            calc = new ETPCalcNew();
            break;
        case ModelType.iSUPERANNUATION_RBL:
            break;
        case ModelType.iRETIREMENT_CALC:
            break;
        case ModelType.iRETIREMENT_HOME:
            break;
        case ModelType.iPAYG_CALC:
            break;
        case ModelType.iCGT_CALC:
            break;
        case ModelType.iSOCIAL_SECURITY_CALC:
            calc = new DSSCalc2();
            break;
        default:
            ;
        }

        return calc;

    }

    /***************************************************************************
     * 
     **************************************************************************/
    public java.util.Collection getGeneratedFinancialItems(String desc) {
        if (!calculate())
            return null;
        return new java.util.ArrayList();
    }

    public Financial[] getGeneratedFinancials(String desc) {
        java.util.Collection c = getGeneratedFinancialItems(desc);
        return c == null ? null : (com.argus.financials.bean.Financial[]) c
                .toArray(new Financial[0]);
    }

}
