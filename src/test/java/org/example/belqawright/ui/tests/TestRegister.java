package org.example.belqawright.ui.tests;

import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestUiConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.auth.Login;
import org.example.belqawright.pages.auth.Register;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.utils.factories.UserFactory;
import org.example.belqawright.utils.factories.User;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.ui.validation.ValidatorRegister;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("UI Tests")
@Feature("Register Page Tests")
public class TestRegister {
    private Page page;
    private final User validUser = UserFactory.randomUser();

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestUiConstants.REGISTER_URL, 5000);
        EssentialUIValidator.validateEssentialUIElements(page, new Register(page).getEssentialUIElementsOnRegister());
    }

    @AfterClass
    public void tearDown() {BrowserManager.closeBrowser();}

    @Test(description = "Test: Name field")
    @Story("Register page")
    @Severity(SeverityLevel.NORMAL)
    public void testNameField() {
        Register registerPage = new Register(page);

        registerPage.fillFullName("abc");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Full name should contains from 4 to 50 characters");

        registerPage.fillFullName("12345678901234567890123456789012345678901234567890");
        ValidatorRegister.validateErrorMessageIsNotPresent(page, "Full name should contains from 4 to 50 characters");

        registerPage.fillFullName("@@@[}78q");
        ValidatorRegister.validateErrorMessageIsNotPresent(page, "Full name should contains from 4 to 50 characters");

        registerPage.fillFullName(validUser.getFullName());
        ValidatorRegister.validateErrorMessageIsNotPresent(page, "Full name should contains from 4 to 50 characters");

        registerPage.clickCheckbox(true);
        ValidatorRegister.validateRegisterButtonIsActive(page, false);

        registerPage.clickCheckbox(false);
        ValidatorRegister.validateRegisterButtonIsActive(page, false);
    }

    @Test(description = "Test: Email field")
    @Story("Register page")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmailField() {
        Register registerPage = new Register(page);

        registerPage.fillEmail("");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Email is required!");

        registerPage.fillEmail("1234");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Email should be the real one!");

        registerPage.fillEmail("{]&&@te");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Email should be the real one!");

        registerPage.fillEmail(validUser.getEmail());
        ValidatorRegister.validateErrorMessageIsNotPresent(page, "Email is not valid");

        registerPage.clickCheckbox(true);
        ValidatorRegister.validateRegisterButtonIsActive(page, false);

        registerPage.clickCheckbox(false);
        ValidatorRegister.validateRegisterButtonIsActive(page, false);
    }

    @Test(description = "Test: Password field")
    @Story("Register page")
    @Severity(SeverityLevel.CRITICAL)
    public void testPasswordField() {
        Register registerPage = new Register(page);

        registerPage.fillPassword("");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Password is required!");

        registerPage.fillPassword("123");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Password should contain from 4 to 50 characters");

        registerPage.fillPassword("12345678901234567890123456789012345678901234567890");
        ValidatorRegister.validateErrorMessageIsNotPresent(page, "Password should contain from 4 to 50 characters");

        registerPage.fillPassword(validUser.getPassword());
        ValidatorRegister.validateErrorMessageIsNotPresent(page, "Password should contain from 4 to 50 characters");

        registerPage.clickCheckbox(true);
        ValidatorRegister.validateRegisterButtonIsActive(page, false);

        registerPage.clickCheckbox(false);
        ValidatorRegister.validateRegisterButtonIsActive(page, false);
    }

    @Test(description = "Test: Confirm password field")
    @Story("Register page")
    @Severity(SeverityLevel.CRITICAL)
    public void testConfirmPasswordField() {
        Register registerPage = new Register(page);

        registerPage.fillRepeatPassword("");
        ValidatorRegister.validateErrorMessageIsPresent(page, "Password confirmation is required!");

        registerPage.fillRepeatPassword("123");
        ValidatorRegister.validateConfirmationPassword(page, false);

        registerPage.fillRepeatPassword(validUser.getPassword());
        ValidatorRegister.validateConfirmationPassword(page, true);

        registerPage.clickCheckbox(true);
        ValidatorRegister.validateRegisterButtonIsActive(page, true);

        registerPage.clickCheckbox(false);
        ValidatorRegister.validateRegisterButtonIsActive(page, true);
    }

    @Test(description = "Test: Register new user")
    @Story("Register page")
    @Severity(SeverityLevel.BLOCKER)
    public void testRegisterNewUser() {
        Register registerPage = new Register(page);

        registerPage.fillAllRegistrationFields(validUser.getFullName(), validUser.getEmail(), validUser.getPassword());
        registerPage.clickCheckbox(true);

        ValidatorRegister.validateRegisterButtonIsActive(page, true);
        registerPage.clickRegisterButton();

        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 7000, TestUiConstants.IOT_DASHBOARD_URL,
                page.locator("a.logo"), "Login header is not visible");
    }

    @Test(description = "Test: Go to login page")
    @Story("Register page")
    @Severity(SeverityLevel.NORMAL)
    public void testGoToLoginPage() {
        WebTestUtils.navigateToURL(page, TestUiConstants.REGISTER_URL, 5000);
        EssentialUIValidator.validateEssentialUIElements(page, new Register(page).getEssentialUIElementsOnRegister());

        Register registerPage = new Register(page);
        Login login = registerPage.goToLoginPage();

        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 5000, TestUiConstants.LOGIN_URL,
                page.locator("h1:text('Login')"), "Login header is not visible");
        EssentialUIValidator.validateEssentialUIElements(page, login.getEssentialUIElementsOnLogin());
    }
}
