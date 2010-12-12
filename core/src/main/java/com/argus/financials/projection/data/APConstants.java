/*
 * APConstants.java
 *
 * Created on 6 August 2002, 15:59
 */

package com.argus.financials.projection.data;

/**
 * 
 * @author shibaevv
 * @version
 */

public class APConstants {

    public static final Integer MAX = new Integer(1);

    public static final Integer MIN = new Integer(2);

    public static final Integer OTHER = new Integer(3);

    public static final double maleRetireAge = 65;

    public static final double femaleRetireAge = 62.5;

    public static final int ge = 1;

    public static final int ne = 2;

    public static final int fr = 3;

    // General Exemption
    public static final double geTag1 = -1000000.;// G13

    public static final double geTag2 = 0.;// G14

    public static final double geTag3 = 6000.;// G15

    public static final double geTag4 = 20000.;// G16

    public static final double geTag5 = 50000.;// G17

    public static final double geTag6 = 60000.;// G18

    public static final double geTaxRate1 = 0.;// I13

    public static final double geTaxRate2 = 0.;// I14

    public static final double geTaxRate3 = .17;// I15

    public static final double geTaxRate4 = .30;// I16

    public static final double geTaxRate5 = .42;// I17

    public static final double geTaxRate6 = .47;// I18

    public static final double geTax1 = 0.;// H13

    public static final double geTax2 = 0.;// H14

    public static final double geTax3 = 0.;// H15

    // =(G16-G15)*I15
    public static final double geTax4 = (geTag4 - geTag3) * geTaxRate3;// H16

    // =((G17-G16)*I16)+H16
    public static final double geTax5 = (geTag5 - geTag4) * geTaxRate4 + geTax4;// H17

    // =((G18-G17)*I17)+H17
    public static final double geTax6 = (geTag6 - geTag5) * geTaxRate5 + geTax5;// H18

    // No Exemption
    public static final double neTag1 = -1000000.;// G20

    public static final double neTag2 = 0.;// G21

    public static final double neTag3 = 0.;// G22

    public static final double neTag4 = 20000.;// G23

    public static final double neTag5 = 50000.;// G24

    public static final double neTag6 = 60000.;// G25

    public static final double neTaxRate1 = 0.; // I20

    public static final double neTaxRate2 = .17;// I21

    public static final double neTaxRate3 = .17;// I22

    public static final double neTaxRate4 = .30;// I23

    public static final double neTaxRate5 = .42;// I24

    public static final double neTaxRate6 = .47;// I25

    public static final double neTax1 = 0.;// H20

    public static final double neTax2 = 0.;// H21

    public static final double neTax3 = 0.;// H22

    // =((G23-G22)*I22)+H22
    public static final double neTax4 = (neTag4 - neTag3) * neTaxRate3;// H23

    // =((G24-G23)*I23)+H23
    public static final double neTax5 = (neTag5 - neTag4) * neTaxRate4 + neTax4;// H24

    // =((G25-G24)*I24)+H24
    public static final double neTax6 = (neTag6 - neTag5) * neTaxRate5 + neTax5;// H25

    // Foreign Resident
    public static final double frTag1 = -1000000.;// G27

    public static final double frTag2 = 0.;// G28

    public static final double frTag3 = 0.;// G29

    public static final double frTag4 = 20000.;// G30

    public static final double frTag5 = 50000.;// G31

    public static final double frTag6 = 60000.;// G32

    public static final double frTaxRate1 = 0.; // I27

    public static final double frTaxRate2 = .29;// I28

    public static final double frTaxRate3 = .29;// I29

    public static final double frTaxRate4 = .30;// I30

    public static final double frTaxRate5 = .42;// I31

    public static final double frTaxRate6 = .47;// I32

    public static final double frTax1 = 0.;// H27

    public static final double frTax2 = 0.;// H28

    public static final double frTax3 = 0.;// H29

    // =((G23-G22)*I22)+H22
    public static final double frTax4 = (neTag4 - neTag3) * frTaxRate3;// H30

    // =((G24-G23)*I23)+H23
    public static final double frTax5 = (neTag5 - neTag4) * frTaxRate4 + frTax4;// H31

    // =((G25-G24)*I24)+H24
    public static final double frTax6 = (neTag6 - neTag5) * frTaxRate5 + frTax5;// H32

    // Low Income Rebate
    public static final double liTag1 = -1000000.;

    public static final double liTag2 = 0.;

    public static final double liTag3 = 20700.;

    public static final double liTag4 = 24450.;

    public static final double liValue1 = 0.;

    public static final double liValue2 = 150.;

    public static final double liValue3 = 150.;

    public static final double liValue4 = 0.;

    public static final double liRate1 = 0.;

    public static final double liRate2 = 0.;

    public static final double liRate3 = 0.04;

    public static final double liRate4 = 0.;

    // Medicare Levy
    public static final double mlTag1 = -1000000.;

    public static final double mlTag2 = 0.;

    public static final double mlTag3 = 13807.;

    public static final double mlTag4 = 14926.;

    public static final double mlRate1 = 0.;

    public static final double mlRate2 = 0.;

    public static final double mlRate3 = 0.;

    public static final double mlRate4 = 0.015;

    public static final double mlShadeIn1 = 0.;

    public static final double mlShadeIn2 = 0.;

    public static final double mlShadeIn3 = 0.2;

    public static final double mlShadeIn4 = 0.;

    // Medicare Levy Age PA
    public static final double mlpaTag1 = -1000000.;

    public static final double mlpaTag2 = 0.;

    public static final double mlpaTag3 = 20000.;

    public static final double mlpaTag4 = 21621.;

    public static final double mlpaRate1 = 0.;

    public static final double mlpaRate2 = 0.;

    public static final double mlpaRate3 = 0.;

    public static final double mlpaRate4 = 0.015;

    public static final double mlpaShadeIn1 = 0.;

    public static final double mlpaShadeIn2 = 0.;

    public static final double mlpaShadeIn3 = 0.2;

    public static final double mlpaShadeIn4 = 0.;

    /** Creates new APConstants */
    public APConstants() {
    }

    public static double vLookUp(double value, int exemptionType, int column,
            boolean notMatch) {

        switch (exemptionType) {
        case ge:
            if (notMatch) {
                if (column == 1) {
                    if (value < geTag2)
                        return geTag1;
                    else if (value < geTag3)
                        return geTag2;
                    else if (value < geTag4)
                        return geTag3;
                    else if (value < geTag5)
                        return geTag4;
                    else if (value < geTag6)
                        return geTag5;
                    else if (value >= geTag6)
                        return geTag6;
                } else if (column == 2) {
                    if (value < geTag2)
                        return geTax1;
                    else if (value < geTag3)
                        return geTax2;
                    else if (value < geTag4)
                        return geTax3;
                    else if (value < geTag5)
                        return geTax4;
                    else if (value < geTag6)
                        return geTax5;
                    else if (value >= geTag6)
                        return geTax6;

                } else if (column == 3) {
                    if (value < geTag2)
                        return geTaxRate1;
                    else if (value < geTag3)
                        return geTaxRate2;
                    else if (value < geTag4)
                        return geTaxRate3;
                    else if (value < geTag5)
                        return geTaxRate4;
                    else if (value < geTag6)
                        return geTaxRate5;
                    else if (value >= geTag6)
                        return geTaxRate6;
                }

            }

        case ne:
            if (notMatch) {
                if (column == 1) {
                    if (value < neTag2)
                        return neTag1;
                    else if (value < neTag3)
                        return neTag2;
                    else if (value < neTag4)
                        return neTag3;
                    else if (value < neTag5)
                        return neTag4;
                    else if (value < neTag6)
                        return neTag5;
                    else if (value >= neTag6)
                        return neTag6;
                } else if (column == 2) {
                    if (value < neTag2)
                        return neTax1;
                    else if (value < neTag3)
                        return neTax2;
                    else if (value < neTag4)
                        return neTax3;
                    else if (value < neTag5)
                        return neTax4;
                    else if (value < neTag6)
                        return neTax5;
                    else if (value >= neTag6)
                        return neTax6;

                } else if (column == 3) {
                    if (value < neTag2)
                        return neTaxRate1;
                    else if (value < neTag3)
                        return neTaxRate2;
                    else if (value < neTag4)
                        return neTaxRate3;
                    else if (value < neTag5)
                        return neTaxRate4;
                    else if (value < neTag6)
                        return neTaxRate5;
                    else if (value >= neTag6)
                        return neTaxRate6;
                }

            }

        case fr:
            if (notMatch) {
                if (column == 1) {
                    if (value < frTag2)
                        return frTag1;
                    else if (value < frTag3)
                        return frTag2;
                    else if (value < frTag4)
                        return frTag3;
                    else if (value < frTag5)
                        return frTag4;
                    else if (value < frTag6)
                        return frTag5;
                    else if (value >= frTag6)
                        return frTag6;
                } else if (column == 2) {
                    if (value < frTag2)
                        return frTax1;
                    else if (value < frTag3)
                        return frTax2;
                    else if (value < frTag4)
                        return frTax3;
                    else if (value < frTag5)
                        return frTax4;
                    else if (value < frTag6)
                        return frTax5;
                    else if (value >= frTag6)
                        return frTax6;

                } else if (column == 3) {
                    if (value < frTag2)
                        return frTaxRate1;
                    else if (value < frTag3)
                        return frTaxRate2;
                    else if (value < frTag4)
                        return frTaxRate3;
                    else if (value < frTag5)
                        return frTaxRate4;
                    else if (value < frTag6)
                        return frTaxRate5;
                    else if (value >= frTag6)
                        return frTaxRate6;
                }

            }

        default:
            return 0;
        }
    }

    public static double vLookUpLI(double value, int column, boolean notMatch) {
        switch (column) {
        case 1:
            if (notMatch) {
                if (value < liTag2)
                    return liTag1;
                else if (value < liTag3)
                    return liTag2;
                else if (value < liTag4)
                    return liTag3;
                else if (value >= liTag4)
                    return liTag4;
            }
        case 2:
            if (notMatch) {
                if (value < liTag2)
                    return liValue1;
                else if (value < liTag3)
                    return liValue2;
                else if (value < liTag4)
                    return liValue3;
                else if (value >= liTag4)
                    return liValue4;
            }
        case 3:
            if (notMatch) {
                if (value < liTag2)
                    return liRate1;
                else if (value < liTag3)
                    return liRate2;
                else if (value < liTag4)
                    return liRate3;
                else if (value >= liTag4)
                    return liRate4;
            }
        default:
            return 0;

        }
    }

    public static double vLookUpML(double value, int column, boolean notMatch) {
        switch (column) {
        case 1:
            if (notMatch) {
                if (value < mlTag2)
                    return mlTag1;
                else if (value < mlTag3)
                    return mlTag2;
                else if (value < mlTag4)
                    return mlTag3;
                else if (value >= mlTag4)
                    return mlTag4;
            }
        case 2:
            if (notMatch) {
                if (value < mlTag2)
                    return mlRate1;
                else if (value < mlTag3)
                    return mlRate2;
                else if (value < mlTag4)
                    return mlRate3;
                else if (value >= mlTag4)
                    return mlRate4;
            }
        case 3:
            if (notMatch) {
                if (value < mlTag2)
                    return mlShadeIn1;
                else if (value < mlTag3)
                    return mlShadeIn2;
                else if (value < mlTag4)
                    return mlShadeIn3;
                else if (value >= mlTag4)
                    return mlShadeIn4;
            }
        default:
            return 0;

        }
    }

    public static double vLookUpMLAgePA(double value, int column,
            boolean notMatch) {
        switch (column) {
        case 1:
            if (notMatch) {
                if (value < mlpaTag2)
                    return mlpaTag1;
                else if (value < mlpaTag3)
                    return mlpaTag2;
                else if (value < mlpaTag4)
                    return mlpaTag3;
                else if (value >= mlpaTag4)
                    return mlpaTag4;
            }
        case 2:
            if (notMatch) {
                if (value < mlpaTag2)
                    return mlpaRate1;
                else if (value < mlpaTag3)
                    return mlpaRate2;
                else if (value < mlpaTag4)
                    return mlpaRate3;
                else if (value >= mlpaTag4)
                    return mlpaRate4;
            }
        case 3:
            if (notMatch) {
                if (value < mlpaTag2)
                    return mlpaShadeIn1;
                else if (value < mlpaTag3)
                    return mlpaShadeIn2;
                else if (value < mlpaTag4)
                    return mlpaShadeIn3;
                else if (value >= mlpaTag4)
                    return mlpaShadeIn4;
            }
        default:
            return 0;

        }
    }
}
