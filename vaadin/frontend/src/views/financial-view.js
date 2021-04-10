import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-split-layout/src/vaadin-split-layout.js';
import '@vaadin/vaadin-grid/src/vaadin-grid-tree-column.js';
import '@vaadin/vaadin-grid/src/vaadin-grid.js';

class FinancialView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-split-layout style="width: 100%; height: 100%;">
 <vaadin-grid></vaadin-grid>
 <vaadin-grid-tree-column></vaadin-grid-tree-column>
</vaadin-split-layout>
`;
    }

    static get is() {
        return 'financial-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(FinancialView.is, FinancialView);
