package org.example.belqawright.ui.tests;

import com.microsoft.playwright.Page;
import freemarker.template.utility.StringUtil;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestUiConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.extraComponents.Calendar;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.ui.validation.ValidatorCalendar;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

@Epic("UI Tests")
@Feature("Calendar Tests")
public class TestCalendar {
    private Page page;
    private final int currentYear = LocalDate.now().getYear();
    private final String currentMonth = StringUtil.capitalize(LocalDate.now().getMonth().name().toLowerCase());
    private final int currentDay = LocalDate.now().getDayOfMonth();

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestUiConstants.CALENDAR_URL, 5000);
        Assert.assertEquals(page.url(), TestUiConstants.CALENDAR_URL, "Failed to navigate to the IoT dashboard URL");
        EssentialUIValidator.validateEssentialUIElements(page, new Calendar(page).getEssentialUIElementsOnCalendar());
    }

    @AfterClass
    public void tearDown() {
        BrowserManager.closeBrowser();
    }

    @Test(description = "Test: Select single date")
    @Story("Calendar page")
    @Severity(SeverityLevel.CRITICAL)
    public void testSelectSingleDate() {
        Calendar calendar = new Calendar(page);

        ValidatorCalendar.validateSelectSingleDate(page, currentYear, currentMonth, currentDay);

        for (int day = 7; day <= 13; day++) {
            calendar.selectSingleDate(2028, "December", day);
            ValidatorCalendar.validateSelectSingleDate(page, 2028, "December", day);
        }

        calendar.selectSingleDate(2028,"December", 1);
        ValidatorCalendar.validateSelectSingleDate(page, 2028, "December", 1);

        calendar.selectSingleDate(2028,"December", 31);
        ValidatorCalendar.validateSelectSingleDate(page, 2028, "December", 31);

        calendar.selectSingleDate(2028,"March", 16);
        ValidatorCalendar.validateSelectSingleDate(page, 2028, "March", 16);

        calendar.selectSingleDate(2028,"May", 16);
        ValidatorCalendar.validateSelectSingleDate(page, 2028, "May", 16);

        calendar.selectSingleDate(2023,"December", 17);
        ValidatorCalendar.validateSelectSingleDate(page, 2023, "December", 17);

        calendar.selectSingleDate(2023,"December", 18);
        ValidatorCalendar.validateSelectSingleDate(page, 2023, "December", 18);
    }

    @Test(description = "Test: Select date range")
    @Story("Calendar page")
    @Severity(SeverityLevel.CRITICAL)
    public void testSelectDayRange() {
        Calendar calendar = new Calendar(page);

        calendar.selectDateRange(2028,"December", 15, 2029,"January", 20);
        ValidatorCalendar.validateSelectedDateRange(page, 2028, "December", 15, 2029, "January", 20);

        calendar.selectDateRange(2028,"December", 15, 2028,"December", 18);
        ValidatorCalendar.validateSelectedDateRange(page, 2028, "December", 15, 2028, "December", 18);

        calendar.selectDateRange(2023,"June", 15, 2023,"October", 15);
        ValidatorCalendar.validateSelectedDateRange(page, 2023, "June", 15, 2023, "October", 15);

        calendar.selectDateRange(2015,"November", 1, 2016,"February", 9);
        ValidatorCalendar.validateSelectedDateRange(page, 2015, "November", 1, 2016, "February", 9);

        calendar.selectDateRange(2015,"November", 5, 2016,"February", 12);
        ValidatorCalendar.validateSelectedDateRange(page, 2015, "November", 5, 2016, "February", 12);
    }

    @Test(description = "Test: Select priced date")
    @Story("Calendar page")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectPricedDate() {
        Calendar calendar = new Calendar(page);

        ValidatorCalendar.validateTodayPriceSelection(page, currentYear, currentMonth, currentDay);

        calendar.selectPricedDate(2028,"December");
        ValidatorCalendar.validateSelectedPriceDay(page, 2028, "December");

        calendar.selectPricedDate(2028,"March");
        ValidatorCalendar.validateSelectedPriceDay(page, 2028, "March");

        calendar.selectPricedDate(2028,"May");
        ValidatorCalendar.validateSelectedPriceDay(page, 2028, "May");

        calendar.selectPricedDate(2023,"December");
        ValidatorCalendar.validateSelectedPriceDay(page, 2023, "December");

        calendar.selectPricedDate(2023,"June");
        ValidatorCalendar.validateSelectedPriceDay(page, 2023, "June");
    }


}
