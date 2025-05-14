package org.example.belqawright.ui.tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestUiConstants;
import org.example.belqawright.pages.FeaturesToolbar;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.utils.WebTestUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Epic("UI Tests")
@Feature("Toolbar Features Tests")
public class TestFeaturesToolbar {
    private Page page;

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestUiConstants.BASE_URL, 5000);
        Assert.assertEquals(page.url(), TestUiConstants.IOT_DASHBOARD_URL, "Failed to navigate to the IoT dashboard URL");
        EssentialUIValidator.validateEssentialUIElements(page, new FeaturesToolbar(page).getEssentialUIElementsOnFeaturesToolbar());
    }

    @AfterClass
    public void tearDown() {
        BrowserManager.closeBrowser();
    }

    @Test(description = "Test: Open Form Layouts page")
    @Story("Form Layouts page")
    @Severity(SeverityLevel.BLOCKER)
    public void testOpenFormLayout() {
        FeaturesToolbar featuresToolbar = new FeaturesToolbar(page);
        featuresToolbar.openFormLayout();

        Locator usingTheGridHeader = page.locator("nb-card-header:text('Using the Grid')");
        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 3000, TestUiConstants.FORMS_LAYOUT_URL, usingTheGridHeader,
                "Failed to locate 'Using the Grid' header");
    }

    @Test(description = "Test: Open Smart Tables page")
    @Story("Smart Tables page")
    @Severity(SeverityLevel.BLOCKER)
    public void testOpenSmartTable() {
        FeaturesToolbar featuresToolbar = new FeaturesToolbar(page);
        featuresToolbar.openSmartTable();

        Locator smartTableHeader = page.locator("nb-card-header:text('Smart Table')");
        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 3000, TestUiConstants.SMART_TABLE_URL, smartTableHeader,
                "Failed to locate 'Smart Table' header");
    }

    @Test(description = "Test: Open Calendar page")
    @Story("Calendar page")
    @Severity(SeverityLevel.BLOCKER)
    public void testOpenCalendar() {
        FeaturesToolbar featuresToolbar = new FeaturesToolbar(page);
        featuresToolbar.openCalendar();

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        Locator selectDateHeader = page.locator("div.calendars div")
                .first()
                .locator("span", new Locator.LocatorOptions().setHasText(String.format("Selected date: %s", currentDate)));
        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 5000, TestUiConstants.CALENDAR_URL, selectDateHeader,
                "Failed to locate 'Selected date' header");
    }

    @Test(description = "Test: Open Register page")
    @Story("Register page")
    @Severity(SeverityLevel.BLOCKER)
    public void testOpenRegisterPage() {
        FeaturesToolbar featuresToolbar = new FeaturesToolbar(page);
        featuresToolbar.openRagisterPage();

        Locator registerHeader = page.locator("h1:text('Register')");
        WebTestUtils.verifyNavigationAndHeaderVisibility(page, 3000, TestUiConstants.REGISTER_URL, registerHeader,
                "Failed to locate 'Register' header");
    }
}
