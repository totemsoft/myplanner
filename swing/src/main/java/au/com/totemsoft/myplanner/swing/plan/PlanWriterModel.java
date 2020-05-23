/*
 * PlanWriterModel.java
 *
 * Created on 18 February 2002, 17:34
 */

package au.com.totemsoft.myplanner.swing.plan;

import au.com.totemsoft.util.ReferenceCode;

public class PlanWriterModel extends PlanWriterTemplateModel implements
        javax.swing.event.ListSelectionListener, java.awt.event.ItemListener {

    public static final String TEMPLATE = "TEMPLATE";

    public static final ReferenceCode NONE_PLAN;

    static {
        NONE_PLAN = new ReferenceCode(0, NONE);

        java.util.Properties props = new java.util.Properties();
        props.setProperty(TEMPLATE, NONE);

        NONE_PLAN.setObject(props);
    }

    public static final String STRATEGY = "STRATEGY";

    public static final String GEARING = "GEARING";

    public static final String ETP = "ETP";

    public static final String AP = "AP";

    public static final String PAYG = "PAYG";

    public static final String MORTGAGE = "MORTGAGE";

    public static final String DSS = "DSS";

    public PlanWriterModel() {
        super();
    }

    public java.util.Properties getPlanFiles() {
        return (java.util.Properties) getPlan().getObject();
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

        if (evt.getValueIsAdjusting())
            return;

        final ReferenceCode newPlan = (ReferenceCode) ((javax.swing.JList) evt
                .getSource()).getSelectedValue();
        System.out.println("PlanWriterModel::valueChanged (): " + newPlan);

        // notify that plan changed (update source selection)
        // push it back in EventQueue
        // javax.swing.SwingUtilities.invokeLater( new Runnable() { public void
        // run() {
        setPlan(newPlan);
        // } } );

    }

    // *
    public void itemStateChanged(java.awt.event.ItemEvent evt) {

        if (evt.getStateChange() != evt.SELECTED)
            return;

        final ReferenceCode newTemplatePlan = (ReferenceCode) evt.getItem();
        System.out.println("PlanWriterModel::itemStateChanged (): "
                + newTemplatePlan);

        // notify that template cb changed (update source selection)
        // push it back in EventQueue
        // javax.swing.SwingUtilities.invokeLater( new Runnable() { public void
        // run() {
        // props.setProperty( TEMPLATE, newTemplatePlan );
        // } } );

    }
    // */
}