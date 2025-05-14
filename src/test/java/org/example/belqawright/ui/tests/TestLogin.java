package org.example.belqawright.ui.tests;

import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestUiConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.auth.Login;
import org.example.belqawright.pages.auth.Register;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.ui.validation.ValidatorLogin;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("UI Tests")
@Feature("Login Page Tests")
public class TestLogin {
    private Page page;

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestUiConstants.LOGIN_URL, 5000);
        EssentialUIValidator.validateEssentialUIElements(page, new Login(page).getEssentialUIElementsOnLogin());
    }

    @AfterClass
    public void tearDown() {BrowserManager.closeBrowser();}

    @Test(description = "Test: email field")
    @Story("Login page")
    @Severity(SeverityLevel.CRITICAL)
    public void testEmailField() {
        Login login = new Login(page);

        login.fillEmail("");
        ValidatorLogin.validateErrorMessageIsPresent(page, "Email is required!");
        ValidatorLogin.validateLoginButtonIsActive(page, false);

        login.fillEmail("abcd");
        ValidatorLogin.validateErrorMessageIsPresent(page, "Email should be the real one!");
        ValidatorLogin.validateLoginButtonIsActive(page, false);

        login.fillEmail("{]&&@test");
        ValidatorLogin.validateErrorMessageIsPresent(page, "Email should be the real one!");

        login.fillEmail("abc@test");
        ValidatorLogin.validateErrorMessageIsPresent(page, "Email should be the real one!");

        login.fillEmail("gorod@test.com");
        ValidatorLogin.validateErrorMessageIsNotPresent(page, "Email should be the real one!");

        login.clickCheckbox(true);
        ValidatorLogin.validateLoginButtonIsActive(page, false);

        login.clickCheckbox(false);
        ValidatorLogin.validateLoginButtonIsActive(page, false);
    }

    @Test(description = "Test: password field")
    @Story("Login page")
    @Severity(SeverityLevel.CRITICAL)
    public void testPasswordField() {
        Login login = new Login(page);

        login.fillPassword("");
        ValidatorLogin.validateErrorMessageIsPresent(page, "Password is required!");
        ValidatorLogin.validateLoginButtonIsActive(page, false);

        login.fillPassword("abc");
        ValidatorLogin.validateErrorMessageIsPresent(page, "Password should contain from 4 to 50 characters");
        ValidatorLogin.validateLoginButtonIsActive(page, false);

        login.fillPassword("12345678901234567890123456789012345678901234567890");
        ValidatorLogin.validateErrorMessageIsNotPresent(page, "Password should contain from 4 to 50 characters");

        login.fillPassword("!@#$%^&*()_+");
        ValidatorLogin.validateErrorMessageIsNotPresent(page, "Password should contain from 4 to 50 characters");

        login.clickCheckbox(true);
        ValidatorLogin.validateLoginButtonIsActive(page, true);

        login.clickCheckbox(false);
        ValidatorLogin.validateLoginButtonIsActive(page, true);
    }

    @Test(description = "Test: Login with valid credentials")
    @Story("Login page")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginWithValidCredentials() {
        Login login = new Login(page);

        login.fillAllLoginFields("test@test.com", "123456");
        ValidatorLogin.validateLoginButtonIsActive(page, true);

        login.clickCheckbox(true);

        ValidatorLogin.validateLoginButtonIsActive(page, true);
        login.clickLoginButton();

        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 7000, TestUiConstants.IOT_DASHBOARD_URL,
                page.locator("a.logo"), "Login header is not visible");
    }

    @Test(description = "Test: Go to register page")
    @Story("Login page")
    @Severity(SeverityLevel.NORMAL)
    public void testGoToRegisterPage() {
        WebTestUtils.navigateToURL(page, TestUiConstants.LOGIN_URL, 5000);
        EssentialUIValidator.validateEssentialUIElements(page, new Login(page).getEssentialUIElementsOnLogin());

        Login login = new Login(page);
        Register register = login.goToRegisterPage();

        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 7000, TestUiConstants.REGISTER_URL,
                page.locator("h1"), "Register header is not visible");
        EssentialUIValidator.validateEssentialUIElements(page, register.getEssentialUIElementsOnRegister());
    }
}
