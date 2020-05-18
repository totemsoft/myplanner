/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.financials.tax;

public abstract class TaxRequest {

    private PersonalData personalData = new PersonalData();

    public PersonalData getPersonalData() {
        return personalData;
    }
    
    public class PersonalData {
        private boolean hospitalCover;
        private boolean married;
        private int dependents;
        private int age;
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public int getDependents() {
            return dependents;
        }
        public void setDependents(int dependents) {
            this.dependents = dependents;
        }
        public boolean isHospitalCover() {
            return hospitalCover;
        }
        public void setHospitalCover(boolean hospitalCover) {
            this.hospitalCover = hospitalCover;
        }
        public boolean isMarried() {
            return married;
        }
        public void setMarried(boolean married) {
            this.married = married;
        }
    }

}

