import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-split-layout/src/vaadin-split-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-radio-button/src/vaadin-radio-group.js';
import '@vaadin/vaadin-radio-button/src/vaadin-radio-button.js';
import '@vaadin/vaadin-checkbox/src/vaadin-checkbox.js';
import '@vaadin/vaadin-date-picker/src/vaadin-date-picker.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-text-field/src/vaadin-password-field.js';

class ClientView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-split-layout style="width: 100%; margin: var(--lumo-space-s); height: 100%;">
 <vaadin-vertical-layout theme="spacing" style="width: 100%; padding: var(--lumo-space-s);">
  <div class="bordered" style="width: 100%;" id="clientName">
   Client Name
   <vaadin-combo-box id="title" style="align-self: stretch; width: 100%;" label="Title"></vaadin-combo-box>
   <vaadin-text-field label="First Name" id="firstname" style="width: 100%;" required value="[[client.firstname]]" invalid></vaadin-text-field>
   <vaadin-text-field label="Surname" id="surname" style="width: 100%;" required value="[[client.surname]]" invalid></vaadin-text-field>
   <vaadin-text-field label="Other Given Names" id="otherGivenNames" style="width: 100%;"></vaadin-text-field>
   <vaadin-radio-group id="sex" style="width: 100%;" required>
    <vaadin-radio-button id="sexMale">
      Male 
    </vaadin-radio-button>
    <vaadin-radio-button id="sexFemale">
      Female 
    </vaadin-radio-button>
   </vaadin-radio-group>
   <vaadin-combo-box id="marital" style="width: 100%;" label="Marital Status"></vaadin-combo-box>
  </div>
  <div id="clientHistory" style="align-self: stretch; width: 100%;" content="">
   Client History
   <vaadin-date-picker label="Birthday" placeholder="Pick a date" id="dateOfBirth" style="width: 100%;"></vaadin-date-picker>
   <vaadin-combo-box id="residenceStatus" style="width: 100%;" label="Residence Status"></vaadin-combo-box>
   <vaadin-combo-box id="residenceCountry" style="width: 100%;" label="Residence Country"></vaadin-combo-box>
   <vaadin-password-field label="Tax File Number" placeholder="" value="" id="taxFileNumber" style="width: 100%;"></vaadin-password-field>
  </div>
  <div id="occupationDetails" style="align-self: stretch; width: 100%;">
   Occupation Details
   <vaadin-combo-box id="occupationCode" style="width: 100%;" label="Occupation"></vaadin-combo-box>
   <vaadin-text-field label="Employer Name" id="employerName" style="width: 100%;"></vaadin-text-field>
   <vaadin-combo-box id="employmentStatus" style="width: 100%;" label="Employment Status"></vaadin-combo-box>
   <vaadin-checkbox id="dssRecipient" style="width: 100%;">
    Centrelink Recipient
   </vaadin-checkbox>
  </div>
 </vaadin-vertical-layout>
 <vaadin-vertical-layout theme="spacing" slot="secondary" style="width: 100%; padding: var(--lumo-space-s);">
  <div class="bordered" style="width: 100%; align-self: stretch;" id="adviserServices">
   Adviser Services
   <vaadin-checkbox id="adviserActive" style="width: 100%;">
     Active 
   </vaadin-checkbox>
   <vaadin-combo-box style="width: 100%;" label="Adviser" id="adviser"></vaadin-combo-box>
   <vaadin-date-picker id="feeDate" style="width: 100%;" label="Date next Fee is Due"></vaadin-date-picker>
   <vaadin-date-picker id="reviewDate" style="width: 100%;" label="Date for Next Review"></vaadin-date-picker>
  </div>
  <div id="currentHealth" style="align-self: stretch; width: 100%;">
   Current Health
   <vaadin-text-field label="Age Next Birthday" placeholder="Placeholder" id="ageNextBirthday" style="width: 100%;" readonly></vaadin-text-field>
   <vaadin-checkbox id="hospitalCover" style="width: 100%;">
    Hospital Cover
   </vaadin-checkbox>
   <vaadin-combo-box id="smoker" style="width: 100%;" label="Smoker"></vaadin-combo-box>
   <vaadin-combo-box id="healthState" style="width: 100%;" label="State of Health"></vaadin-combo-box>
  </div>
  <div id="trustCompanyDIY" style="align-self: stretch; width: 100%;">
   Trust, Company, DIY
   <vaadin-combo-box id="trustStatus" style="width: 100%;" label="Trust Status"></vaadin-combo-box>
   <vaadin-combo-box id="companyStatus" style="width: 100%;" label="Company Status"></vaadin-combo-box>
   <vaadin-combo-box id="smsfStatus" style="width: 100%;" label="SMSF Status"></vaadin-combo-box>
  </div>
 </vaadin-vertical-layout>
</vaadin-split-layout>
`;
    }

    static get is() {
        return 'client-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
            //client: { type: Object }
        };
    }
}

customElements.define(ClientView.is, ClientView);
