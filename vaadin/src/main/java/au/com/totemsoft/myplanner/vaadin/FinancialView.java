package au.com.totemsoft.myplanner.vaadin;

import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the financial-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("financial-view")
@JsModule("./src/views/financial-view.js")
public class FinancialView extends PolymerTemplate<FinancialView.FinancialViewModel> {

    /**
     * Creates a new FinancialView.
     */
    public FinancialView() {
        // You can initialise any data required for the connected UI components here.
    }

    /**
     * This model binds properties between FinancialView and financial-view
     */
    public interface FinancialViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
