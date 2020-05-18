/*
 * NumberOfUnitsSharesDocumentListener.java
 *
 * Created on 24 July 2002, 10:33
 */

package com.argus.financials.assetinvestment;

import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * The class calculates a new value for the number of shares/untis * the
 * share/unit price
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version 0.01
 * 
 * @see com.argus.financials.ui.financials.AddAssetInvestmentView
 */
public class NumberOfUnitsSharesDocumentListener implements DocumentListener {

    private UpdateUnitSharePriceData _view = null;

    /** Creates new NumberOfUnitsSharesDocumentListener */
    public NumberOfUnitsSharesDocumentListener(UpdateUnitSharePriceData view) {
        _view = view;
    }

    public void removeUpdate(javax.swing.event.DocumentEvent documentEvent) {
        calculateValue(documentEvent);
    }

    public void insertUpdate(javax.swing.event.DocumentEvent documentEvent) {
        calculateValue(documentEvent);
    }

    public void changedUpdate(javax.swing.event.DocumentEvent documentEvent) {
        // we won't ever get this with a PlainDocument
    }

    private void calculateValue(javax.swing.event.DocumentEvent documentEvent) {
        Document whatsup = documentEvent.getDocument();

        // calculate number of shares/units * exit price
        java.math.BigDecimal unitShares = _view.getNumberOfUnitShares();
        java.math.BigDecimal closePrice = _view.getClosePrice();

        _view.setPriceDateValue(unitShares == null || closePrice == null ? null
                : unitShares.multiply(closePrice));
    }

}
