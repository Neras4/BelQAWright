package org.example.belqawright.ui.tests;

import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestUiConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.tablesAndData.SmartTable;
import org.example.belqawright.testdata.StaticUsers;
import org.example.belqawright.utils.factories.User;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.utils.factories.UserFactory;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.ui.validation.ValidatorSmartTable;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("UI Tests")
@Feature("Smart Table Tests")
public class TestSmartTable {
    private Page page;
    private final User validUser = UserFactory.randomUser();
    private final User updatedUser = UserFactory.randomUser();
    private final User invalidUser = UserFactory.randomUser();

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestUiConstants.SMART_TABLE_URL, 5000);
        EssentialUIValidator.validateEssentialUIElements(page, new SmartTable(page).getEssentialUIElementsOnSmartTable());
    }

    @AfterClass
    public void tearDown() {BrowserManager.closeBrowser();}

    @Test(description = "Test: Submit new record", priority = 1)
    @Story("Smart Table page")
    @Severity(SeverityLevel.CRITICAL)
    public void testSubmitNewRecord() {
        SmartTable smartTable = new SmartTable(page);

        smartTable.submitNewRecord(validUser, true);
        ValidatorSmartTable.validateRecord(page, validUser, true);

        smartTable.submitNewRecord(invalidUser, false);
        ValidatorSmartTable.validateRecord(page, invalidUser, false);
    }

    @Test(description = "Test: Edit record", priority = 2)
    @Story("Smart Table page")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditRecord() {
        SmartTable smartTable = new SmartTable(page);

        smartTable.editRecord(validUser, updatedUser, false);
        ValidatorSmartTable.validateRecord(page, validUser, true);
        ValidatorSmartTable.validateRecord(page, updatedUser, false);

        smartTable.clearFilter();
        ValidatorSmartTable.validateRecord(page, validUser, true);
        ValidatorSmartTable.validateRecord(page, updatedUser, false);

        smartTable.editRecord(validUser, updatedUser, true);
        ValidatorSmartTable.validateRecord(page, validUser, true);

        smartTable.clearFilter();
    }

    @Test(description = "Test: Delete record", priority = 3)
    @Story("Smart Table page")
    @Severity(value = SeverityLevel.CRITICAL)
    public void testDeleteRecord() {
        SmartTable smartTable = new SmartTable(page);

        smartTable.deleteRecord(validUser, false);
        ValidatorSmartTable.validateRecord(page, validUser, true);

        smartTable.deleteRecord(validUser, true);
        ValidatorSmartTable.validateRecord(page, validUser, false);
    }

    @Test(description = "Test: Search and Filter", priority = 4)
    @Story("Smart Table page")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchAndFilter() {
        SmartTable smartTable = new SmartTable(page);

        smartTable.filterByUser(StaticUsers.USER_PAGE_5);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_5, true);
        ValidatorSmartTable.validateAmountRows(page, 1);

        smartTable.clearFilter();
        ValidatorSmartTable.validatePageNumber(page, 1);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_1, true);
        ValidatorSmartTable.validateAmountRows(page, 10);

        smartTable.navigateToPage("next");
        ValidatorSmartTable.validatePageNumber(page, 2);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_2, true);
        ValidatorSmartTable.validateAmountRows(page, 10);

        smartTable.navigateToPage("last");
        ValidatorSmartTable.validatePageNumber(page, 6);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_6, true);
        ValidatorSmartTable.validateAmountRows(page, 10);

        smartTable.navigateToPage("prev");
        ValidatorSmartTable.validatePageNumber(page, 5);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_5, true);
        ValidatorSmartTable.validateAmountRows(page, 10);

        smartTable.navigateToPage("first");
        ValidatorSmartTable.validatePageNumber(page, 1);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_1, true);
        ValidatorSmartTable.validateAmountRows(page, 10);

        smartTable.goToPage(2);
        ValidatorSmartTable.validatePageNumber(page, 2);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_2, true);
        ValidatorSmartTable.validateAmountRows(page, 10);

        smartTable.goToPage(6);
        ValidatorSmartTable.validatePageNumber(page, 6);
        ValidatorSmartTable.validateRecord(page, StaticUsers.USER_PAGE_6, true);
        ValidatorSmartTable.validateAmountRows(page, 10);
    }

}
