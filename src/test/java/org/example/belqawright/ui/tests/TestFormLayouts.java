package org.example.belqawright.ui.tests;

import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestUiConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.forms.FormLayouts;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.ui.validation.ValidatorFormLayouts;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

@Epic("UI Tests")
@Feature("Form Layouts Tests")
public class TestFormLayouts {
    private Page page;
    private final String firstName  = "Aleksei";
    private final String lastName = "Gringo";
    private final String name = firstName + " " + lastName;
    private final String email = "alekesei.gringo@test.com";
    private final String password = "password";

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestUiConstants.FORMS_LAYOUT_URL, 5000);
        EssentialUIValidator.validateEssentialUIElements(page, new FormLayouts(page).getEssentialUIElementsOnFormLayouts());
    }

    @AfterClass
    public void tearDown() {BrowserManager.closeBrowser();}

    @Test(description = "Test: FIll Inline form")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.NORMAL)
    public void testFillInlineForm() {
        FormLayouts formLayouts = new FormLayouts(page);
        formLayouts.fillInlineForm(name, email, true);

        ValidatorFormLayouts.validateFormInputs(page, "Inline form",
                Map.of("input[placeholder='Jane Doe']", name, "input[placeholder='Email']", email),
                Map.of("nb-checkbox .custom-checkbox", true));
    }

    @Test(description = "Test: Using the Grid form")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.NORMAL)
    public void testFillGridForm() {
        String option = "Option 2";

        FormLayouts formLayouts = new FormLayouts(page);
        formLayouts.fillGridForm(email, password, option);

        ValidatorFormLayouts.validateFormInputs(page, "Using the Grid",
                Map.of("#inputEmail1", email, "#inputPassword2", password),
                Map.of(String.format("nb-radio:has(span:text('%s')) input", option), true));
    }

    @Test(description = "Test: Basic form")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.NORMAL)
    public void testFillBasicForm() {
        FormLayouts formLayouts = new FormLayouts(page);
        formLayouts.fillBasicForm(email, password, true);

        ValidatorFormLayouts.validateFormInputs(page, "Basic form",
                Map.of("#exampleInputEmail1", email, "#exampleInputPassword1", password),
                Map.of("nb-checkbox .custom-checkbox", true));
    }

    @Test(description = "Test: Block form form")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.NORMAL)
    public void testFillBlockForm() {
        String website = "https://aleksei.gringo.com";

        FormLayouts formLayouts = new FormLayouts(page);
        formLayouts.fillBlockForm(firstName, lastName, email, website);

        ValidatorFormLayouts.validateFormInputs(page, "Block form",
                Map.of("#inputFirstName", firstName, "#inputLastName", lastName, "#inputEmail", email,
                        "#inputWebsite", website),
                Map.of());
    }

    @Test(description = "Test: Form without labels")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.NORMAL)
    public void testFillFormWithoutLabels() {
        String subject = "Test Subject";

        FormLayouts formLayouts = new FormLayouts(page);
        formLayouts.fillFormWithoutLabels(name, subject, "Test Message");

        ValidatorFormLayouts.validateFormInputs(page, "Form without labels",
                Map.of("input[placeholder='Recipients']", name, "input[placeholder='Subject']", subject,
                        "textarea[placeholder='Message']", "Test Message"),
                Map.of());
    }

    @Test(description = "Test: Horizontal form")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.NORMAL)
    public void testFillHorizontalForm() {
        FormLayouts formLayouts = new FormLayouts(page);
        formLayouts.fillHorizontalForm(email, password, true);

        ValidatorFormLayouts.validateFormInputs(page, "Horizontal form",
                Map.of("#inputEmail3", email, "#inputPassword3", password),
                Map.of("nb-checkbox .custom-checkbox", true));
    }
}
