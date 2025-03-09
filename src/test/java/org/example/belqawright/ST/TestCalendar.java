package org.example.belqawright.ST;

import com.microsoft.playwright.Page;
import freemarker.template.utility.StringUtil;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.belqawright.constants.TestConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.extraComponents.Calendar;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.validation.ST.CalendarValidator;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class TestCalendar {
    private Page page;
    private final int currentYear = LocalDate.now().getYear();
    private final String currentMonth = StringUtil.capitalize(LocalDate.now().getMonth().name().toLowerCase());
    private final int currentDay = LocalDate.now().getDayOfMonth();

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestConstants.CALENDAR_URL, 5000);
        Assert.assertEquals(page.url(), TestConstants.CALENDAR_URL, "Failed to navigate to the IoT dashboard URL");
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

        CalendarValidator.validateSelectSingleDate(page, currentYear, currentMonth, currentDay);

        for (int day = 7; day <= 13; day++) {
            calendar.selectSingleDate(2028, "December", day);
            CalendarValidator.validateSelectSingleDate(page, 2028, "December", day);
        }

        calendar.selectSingleDate(2028,"December", 1);
        CalendarValidator.validateSelectSingleDate(page, 2028, "December", 1);

        calendar.selectSingleDate(2028,"December", 31);
        CalendarValidator.validateSelectSingleDate(page, 2028, "December", 31);

        calendar.selectSingleDate(2028,"March", 16);
        CalendarValidator.validateSelectSingleDate(page, 2028, "March", 16);

        calendar.selectSingleDate(2028,"May", 16);
        CalendarValidator.validateSelectSingleDate(page, 2028, "May", 16);

        calendar.selectSingleDate(2023,"December", 17);
        CalendarValidator.validateSelectSingleDate(page, 2023, "December", 17);

        calendar.selectSingleDate(2023,"December", 18);
        CalendarValidator.validateSelectSingleDate(page, 2023, "December", 18);
    }

    @Test(description = "Test: Select date range")
    @Story("Calendar page")
    @Severity(SeverityLevel.CRITICAL)
    public void testSelectDayRange() {
        Calendar calendar = new Calendar(page);

        calendar.selectDateRange(2028,"December", 15, 2029,"January", 20);
        CalendarValidator.validateSelectedDateRange(page, 2028, "December", 15, 2029, "January", 20);

        calendar.selectDateRange(2028,"December", 15, 2028,"December", 18);
        CalendarValidator.validateSelectedDateRange(page, 2028, "December", 15, 2028, "December", 18);

        calendar.selectDateRange(2023,"June", 15, 2023,"October", 15);
        CalendarValidator.validateSelectedDateRange(page, 2023, "June", 15, 2023, "October", 15);

        calendar.selectDateRange(2015,"November", 1, 2016,"February", 9);
        CalendarValidator.validateSelectedDateRange(page, 2015, "November", 1, 2016, "February", 9);

        calendar.selectDateRange(2015,"November", 5, 2016,"February", 12);
        CalendarValidator.validateSelectedDateRange(page, 2015, "November", 5, 2016, "February", 12);
    }

    @Test(description = "Test: Select priced date")
    @Story("Calendar page")
    @Severity(SeverityLevel.NORMAL)
    public void testSelectPricedDate() {
        Calendar calendar = new Calendar(page);

        CalendarValidator.validateTodayPriceSelection(page, currentYear, currentMonth, currentDay);

        calendar.selectPricedDate(2028,"December");
        CalendarValidator.validateSelectedPriceDay(page, 2028, "December");

        calendar.selectPricedDate(2028,"March");
        CalendarValidator.validateSelectedPriceDay(page, 2028, "March");

        calendar.selectPricedDate(2028,"May");
        CalendarValidator.validateSelectedPriceDay(page, 2028, "May");

        calendar.selectPricedDate(2023,"December");
        CalendarValidator.validateSelectedPriceDay(page, 2023, "December");

        calendar.selectPricedDate(2023,"June");
        CalendarValidator.validateSelectedPriceDay(page, 2023, "June");
    }


}
