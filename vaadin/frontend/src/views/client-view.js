import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-split-layout/src/vaadin-split-layout.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-radio-button/src/vaadin-radio-group.js';
import '@vaadin/vaadin-radio-button/src/vaadin-radio-button.js';
import '@vaadin/vaadin-checkbox/src/vaadin-checkbox.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';

class ClientView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-split-layout style="width: 100%;">
 <vaadin-vertical-layout theme="spacing" style="width: 100%;">
  <div class="bordered" style="width: 100%;">
   Client Name
   <vaadin-combo-box id="title" style="align-self: stretch; width: 100%;" label="Title"></vaadin-combo-box>
   <vaadin-text-field label="First Name" id="firstname" style="width: 100%;" required value="[[client.firstname]]"></vaadin-text-field>
   <vaadin-text-field label="Surname" id="surname" style="width: 100%;" required value="[[client.surname]]"></vaadin-text-field>
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
 </vaadin-vertical-layout>
 <vaadin-vertical-layout theme="spacing" slot="secondary" style="width: 100%;">
  <div class="bordered" style="width: 100%;">
   Adviser Services
   <vaadin-checkbox id="clientAdviserActive" style="width: 100%;">
    Active
   </vaadin-checkbox>
   <vaadin-combo-box style="width: 100%;" label="Adviser"></vaadin-combo-box>
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
        };
    }
}

customElements.define(ClientView.is, ClientView);
