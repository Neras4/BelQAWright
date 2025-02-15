package org.example.belqawright.pages.forms;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.validation.UIElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormLayouts {
    private final Page page;

    public FormLayouts(Page page) {
        this.page = page;
    }

    @Step("Fill Inline form")
    public void fillInlineForm(String name, String email, boolean rememberMe) {
        fillAndSubmitForm("Inline form",
                Map.of("input[placeholder='Jane Doe']", name, "input[placeholder='Email']", email),
                Map.of("nb-checkbox .custom-checkbox", rememberMe),
                "Submit");
    }

    @Step("Fill Using the Grid form")
    public void fillGridForm(String email, String password, String option) {
        fillAndSubmitForm("Using the Grid",
                Map.of("#inputEmail1", email, "#inputPassword2", password),
                Map.of(String.format("nb-radio label:has(span:text('%s'))", option), true),
                "Sign in");
    }

    @Step("Fill Basic form")
    public void fillBasicForm(String email, String password, boolean checkMeOut) {
        fillAndSubmitForm("Basic form",
                Map.of("#exampleInputEmail1", email, "#exampleInputPassword1", password),
                Map.of("nb-checkbox .custom-checkbox", checkMeOut),
                "Submit");
    }

    @Step("Fill Block form")
    public void fillBlockForm(String firstName, String lastName, String email, String website) {
        fillAndSubmitForm("Block form",
                Map.of("#inputFirstName", firstName, "#inputLastName", lastName, "#inputEmail", email,
                        "#inputWebsite", website),
                Map.of(),
                "Submit");
    }

    @Step("Fill Form without labels")
    public void fillFormWithoutLabels(String recipients, String subject, String message) {
        fillAndSubmitForm("Form without labels",
                Map.of("input[placeholder='Recipients']", recipients, "input[placeholder='Subject']", subject,
                        "textarea[placeholder='Message']", message),
                Map.of(),
                "Send");
    }

    @Step("Fill Horizontal form")
    public void fillHorizontalForm(String email, String password, boolean rememberMe) {
        fillAndSubmitForm("Horizontal form",
                Map.of("#inputEmail3", email, "#inputPassword3", password),
                Map.of("nb-checkbox .custom-checkbox", rememberMe),
                "Sign in");
    }

    public List<UIElement> getEssentialUIElementsOnFormLayouts() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("nb-card-header:text('Inline form')",
                "Inline form could not be located on the page"));
        elements.add(new UIElement("nb-card-header:text('Using the Grid')",
                "Using the Grid form could not be located on the page"));
        elements.add(new UIElement("nb-card-header:text('Block form')",
                "Block form could not be located on the page"));
        elements.add(new UIElement("nb-card-header:text('Form without labels')",
                "Form without labels could not be located on the page"));
        elements.add(new UIElement("nb-card-header:text('Horizontal form')",
                "Horizontal form could not be located on the page"));

        return elements;
    }

    private void fillAndSubmitForm(String formName, Map<String, String> inputValues,
                                   Map<String, Boolean> toggleElements, String actionButtonText) {
        try {
            LoggingUtils.logInfoWithScreenshot(page, String.format("Fill %s", formName));
            Locator formCard = page.locator(String.format("nb-card:has(nb-card-header:text('%s'))", formName));

            for (Map.Entry<String, String> entry : inputValues.entrySet()) {
                Locator inputField = formCard.locator(entry.getKey());
                ActionUtils.performActionWithScreenshot(page, String.format("Fill %s field", entry.getKey()),
                        () -> inputField.fill(entry.getValue()), inputField);
            }

            for(Map.Entry<String, Boolean> entry : toggleElements.entrySet()) {
                Locator toggleElement = formCard.locator(entry.getKey());

                if(toggleElement.isChecked() != entry.getValue()) {
                    ActionUtils.performActionWithScreenshot(page, String.format("Toggle %s switch", entry.getKey()),
                            toggleElement::click, toggleElement);
                }
            }

            Locator submitButton = formCard.locator(String.format("button:text('%s')", actionButtonText));
            ActionUtils.performActionWithScreenshot(page, String.format("Click on '%s' button", actionButtonText),
                    submitButton::click, submitButton);
        } catch (Exception e) {
            LoggingUtils.logError(String.format("Error during %s submission", formName), e);
            throw new RuntimeException(String.format("Failed to fill and submit %s}", formName), e);
        }
    }
}
