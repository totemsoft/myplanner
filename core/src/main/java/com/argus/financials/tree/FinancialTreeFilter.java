/*
 * GroupFilter.java
 *
 * Created on 26 March 2003, 14:19
 */

package com.argus.financials.tree;

/**
 * 
 * @author valeri chibaev
 */

import java.util.Vector;

import com.argus.financials.bean.Financial;
import com.argus.financials.bean.Regular;
import com.argus.financials.bean.RegularExpense;
import com.argus.financials.bean.RegularIncome;
import com.argus.financials.bean.TaxOffset;
import com.argus.financials.code.BaseCode;
import com.argus.financials.code.Code;
import com.argus.financials.code.CountryCode;
import com.argus.financials.code.FinancialClass;
import com.argus.financials.code.FinancialServiceCode;
import com.argus.financials.code.FinancialType;
import com.argus.financials.code.OwnerCode;
import com.argus.util.ReferenceCode;

public class FinancialTreeFilter {

    private FinancialTreeFilter parent;

    private FinancialTreeFilter associated;

    private Object group; // e.g. OwnerCode

    private Vector groupItems; // e.g. Client, Partner

    /** Creates a new instance of FinancialTreeFilter */
    public FinancialTreeFilter() {
        this.groupItems = new Vector();
    }

    public FinancialTreeFilter(FinancialTreeFilter filter) {
        this.parent = filter.parent;
        this.group = filter.group;
        this.groupItems = new Vector(filter.groupItems);
    }

    public FinancialTreeFilter(Object group) {
        this.group = group;
        this.groupItems = new Vector();
    }

    public FinancialTreeFilter(FinancialTreeFilter parent, Object group) {
        this.parent = parent;
        this.group = group;
        this.groupItems = new Vector();
    }

    public FinancialTreeFilter(Object group, Vector groupItems) {
        this.group = group;
        this.groupItems = new Vector();
    }

    public FinancialTreeFilter(FinancialTreeFilter parent, Object group,
            Object groupItem) {
        this.parent = parent;
        this.group = group;

        this.groupItems = new Vector();
        if (groupItem != null)
            this.groupItems.add(groupItem);
    }

    public FinancialTreeFilter(FinancialTreeFilter parent, Object group,
            Vector groupItems) {
        this.parent = parent;
        this.group = group;
        this.groupItems = groupItems == null ? new Vector() : groupItems;
    }

    public String toString() {
        int size = groupItems.size();
        if (groupItems == null || size == 0)
            return "<ALL>";

        String s = "";
        for (int i = 0; i < size; i++) {
            s += groupItems.elementAt(i).toString();
            if (i != size - 1)
                s += ", ";
        }
        return s;
    }

    protected boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2))
                || (value2 != null && value2.equals(value1));
    }

    public FinancialTreeFilter getParent() {
        return parent;
    }

    public FinancialTreeFilter getAssociated() {
        return associated;
    }

    public void setAssociated(FinancialTreeFilter associated) {
        this.associated = associated;
    }

    public Object getGroup() {
        return group;
    }

    public Vector getGroupItems() {
        return groupItems;
    }

    public void clear() {
        groupItems.clear();
    }

    public boolean add(Object item) {
        return groupItems.add(item);
    }

    public boolean remove(Object item) {
        return groupItems.remove(item);
    }

    public boolean inGroup(Financial f) {

        if (f == null)
            return false;

        int size = groupItems == null ? 0 : groupItems.size();
        if (size == 0)
            return (parent == null) || (parent != null && parent.inGroup(f));

        boolean inGroup = false;
        Financial af = null;
        if (group == FinancialTreeStructure.ASSOCIATED_FINANCIAL_CLASS) {

            // get associated financial
            af = ((Regular) f).getAssociatedFinancial();
            if (af != null) {
                for (int i = 0; i < size; i++) {
                    Object item = groupItems.elementAt(i);

                    Integer objectTypeID = null;
                    if (item instanceof Integer) {
                        objectTypeID = (Integer) item;
                    } else if (item instanceof ReferenceCode) {
                        ReferenceCode refCode = (ReferenceCode) item;
                        objectTypeID = refCode == null ? null : refCode
                                .getCodeIDInteger();
                    }
                    inGroup = af.getObjectTypeID().equals(objectTypeID);

                    if (inGroup)
                        break;
                }

            }

        } else if (group == FinancialTreeStructure.FINANCIAL_CLASS
                || group instanceof FinancialClass) {

            for (int i = 0; i < size; i++) {
                Object item = groupItems.elementAt(i);

                Integer objectTypeID = null;
                if (item instanceof Integer) {
                    objectTypeID = (Integer) item;
                } else if (item instanceof ReferenceCode) {
                    ReferenceCode refCode = (ReferenceCode) item;
                    objectTypeID = refCode == null ? null : refCode
                            .getCodeIDInteger();
                }
                inGroup = f.getObjectTypeID().equals(objectTypeID);

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.ASSOCIATED_FINANCIAL_TYPE) {

            if (f instanceof RegularIncome || f instanceof RegularExpense
                    || f instanceof TaxOffset) {

                // get associated financial
                af = ((Regular) f).getAssociatedFinancial();
                if (af != null) {
                    for (int i = 0; i < size; i++) {
                        Object item = groupItems.elementAt(i);

                        if (item instanceof Integer) {
                            Integer financialTypeID1 = (Integer) item;
                            Integer financialTypeID2 = af.getFinancialTypeID();
                            inGroup = equals(financialTypeID1, financialTypeID2);
                        } else if (item instanceof ReferenceCode) {
                            ReferenceCode refCode1 = (ReferenceCode) item;
                            ReferenceCode refCode2 = af.getFinancialType();
                            inGroup = refCode1 != null
                                    && refCode2 != null
                                    && refCode1.getCodeID() == refCode2
                                            .getCodeID();
                        }

                        if (inGroup)
                            break;
                    }

                }

            }

        } else if (group == FinancialTreeStructure.FINANCIAL_TYPE
                || group instanceof FinancialType) {

            for (int i = 0; i < size; i++) {
                Object item = groupItems.elementAt(i);

                if (item instanceof Integer) {
                    Integer financialTypeID1 = (Integer) item;
                    Integer financialTypeID2 = f.getFinancialTypeID();
                    inGroup = equals(financialTypeID1, financialTypeID2);
                } else if (item instanceof ReferenceCode) {
                    ReferenceCode refCode1 = (ReferenceCode) item;
                    ReferenceCode refCode2 = f.getFinancialType();
                    inGroup = refCode1 != null && refCode2 != null
                            && refCode1.getCodeID() == refCode2.getCodeID();
                }

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.ASSOCIATED_FUND_TYPE) {

            // get associated financial
            af = ((Regular) f).getAssociatedFinancial();
            if (af != null) {
                for (int i = 0; i < size; i++) {
                    Object fund = groupItems.elementAt(i);
                    if (fund == null)
                        inGroup = true;
                    else if (fund instanceof String)
                        inGroup = fund.equals(af.getFundTypeDesc());
                    else if (fund instanceof Integer)
                        inGroup = fund.equals(af.getFundTypeID());
                    else if (fund instanceof ReferenceCode) {
                        fund = ((ReferenceCode) fund).getCodeIDInteger();
                        inGroup = fund.equals(af.getFundTypeID());
                    }

                    if (inGroup)
                        break;
                }

            }

        } else if (group == FinancialTreeStructure.FUND_TYPE) {

            for (int i = 0; i < size; i++) {
                Object fund = groupItems.elementAt(i);
                if (fund == null)
                    inGroup = true;
                else if (fund instanceof String)
                    inGroup = fund.equals(f.getFundTypeDesc());
                else if (fund instanceof Integer)
                    inGroup = fund.equals(f.getFundTypeID());
                else if (fund instanceof ReferenceCode) {
                    fund = ((ReferenceCode) fund).getCodeIDInteger();
                    inGroup = fund.equals(f.getFundTypeID());
                }

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.OWNER_CODE
                || group instanceof OwnerCode) {

            for (int i = 0; i < size; i++) {
                Object owner = groupItems.elementAt(i);
                if (owner == null)
                    inGroup = true;
                else if (owner instanceof String)
                    inGroup = owner.equals(f.getOwner());
                else if (owner instanceof Integer)
                    inGroup = owner.equals(f.getOwnerCodeID());

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.COUNTRY_CODE
                || group instanceof CountryCode) {
            // !!! this is VERY expensive operation (>200 countries) !!!
            for (int i = 0; i < size; i++) {
                Object country = groupItems.elementAt(i);
                if (country == null)
                    inGroup = true;
                else if (country instanceof String)
                    inGroup = country.equals(f.getCountry());
                else if (country instanceof Integer)
                    inGroup = country.equals(f.getCountryCodeID());

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.TAXABLE) {
            // System.out.println( "" + f + "\tf.isTaxable()=" + f.isTaxable()
            // );
            for (int i = 0; i < size; i++) {
                Object item = groupItems.elementAt(i);
                // System.out.println( "\titem=" + item );
                if (item == null)
                    inGroup = true;
                else if (item instanceof Boolean)
                    inGroup = ((Boolean) item).booleanValue() == f.isTaxable();

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.GENERATED) {
            for (int i = 0; i < size; i++) {
                Object item = groupItems.elementAt(i);
                if (item == null)
                    inGroup = true;
                else if (item instanceof Boolean)
                    inGroup = ((Boolean) item).booleanValue() == f
                            .isGenerated();

                if (inGroup)
                    break;
            }

        } else if (group == FinancialTreeStructure.SERVICE_CODE
                || group instanceof FinancialServiceCode) {

            for (int i = 0; i < size; i++) {
                Object service = groupItems.elementAt(i);
                // System.out.println( "" + f + " in " + service );
                if (service == null)
                    inGroup = true;
                else if (service instanceof String)
                    inGroup = service.equals(f.getFinancialServiceCode());
                else if (service instanceof ReferenceCode)
                    inGroup = service.equals(f.getFinancialService())
                            || (service == BaseCode.CODE_NONE && f
                                    .getFinancialService() == null);

                if (inGroup)
                    break;
            }

        }

        return inGroup
                && ((parent == null) || (parent != null && parent.inGroup(f)))
        // && ( ( af == null || associated == null ) || ( af != null &&
        // associated != null && associated.inGroup( af ) ) )
        ;

    }

    // e.g. ObjectType[] { AssetCash, AssetPersonal, .., Liability, ... }
    public static Object[] getGroupItems(Object group) {

        if (group instanceof BaseCode) { // ObjectType, FinancialType

            BaseCode baseCode = (BaseCode) group;
            return baseCode.getCodes().toArray();

        }

        if (group instanceof Code) { // Country

            Code code = (Code) group;
            return code.getKeys().toArray();

        }

        return new Object[0];

    }

}
