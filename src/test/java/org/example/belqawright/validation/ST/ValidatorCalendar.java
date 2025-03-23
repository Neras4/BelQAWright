package org.example.belqawright.validation.ST;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.testng.Assert;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Objects;

public class ValidatorCalendar {

    public static void validateSelectSingleDate(Page page, int year, String month, int day) {
        LoggingUtils.logDebug("Validating single date");

        Locator calendarLocator = page.locator("div.calendar-container:has(nb-calendar[showweeknumber])");

        validateDate(calendarLocator, year, month, day);
        validateYearAndMonth(calendarLocator, year, month);
        validateDay(calendarLocator, day);
    }

    public static void validateSelectedPriceDay(Page page, int year, String month) {
        LoggingUtils.logDebug("Validating selected price day");

        Locator calendarLocator = page.locator("div.calendar-container:has(nb-calendar[ng-reflect-day-cell-component])");
        validateYearAndMonth(calendarLocator, year, month);
    }

    public static void validateTodayPriceSelection(Page page, int year, String month, int day) {
        LoggingUtils.logDebug("Validating selected price day");

        Locator calendarLocator = page.locator("div.calendar-container:has(nb-calendar[ng-reflect-day-cell-component])");
        validateDate(calendarLocator, year, month, day);
        validateYearAndMonth(calendarLocator, year, month);
        validateDay(calendarLocator, day);
    }

    public static void validateSelectedDateRange(Page page, int startYear, String startMonth, int startDay,
                                                 int endYear, String endMonth, int endDay) {
        LoggingUtils.logDebug("Validating selected date range");

        Locator calendarLocator = page.locator("div.calendar-container:has(nb-calendar-range[ng-reflect-range])");

        LoggingUtils.logDebug("Validating date");
        String displayedRange = calendarLocator.locator("span.subtitle").innerText().trim();
        String expectedRange = String.format("Selected range: %s %d, %d - %s %d, %d",
                startMonth.substring(0, 3), startDay, startYear, endMonth.substring(0, 3), endDay, endYear);

        Assert.assertEquals(displayedRange, expectedRange,
                String.format("Expected range '%s', but found '%s'", expectedRange, displayedRange));

        LoggingUtils.logDebug("Validating end and start date");

        int startMonthIndex = findMonthIndex(startMonth);
        int endMonthIndex = findMonthIndex(endMonth);
        int monthsToScroll = (endYear - startYear) * 12 + (endMonthIndex - startMonthIndex);
        boolean endDateFound = isDateSelected(page, calendarLocator, endDay);

        for (int i = 0; i < monthsToScroll; i++) {
            ActionUtils.performActionWithScreenshot(page, "Click on the prev month arrow",
                    calendarLocator.locator("button.prev-month")::click, calendarLocator.locator("button.prev-month"));
        }

        boolean startDateFound = isDateSelected(page, calendarLocator, startDay);

        Assert.assertTrue(startDateFound, String.format("Start date %s %d is not selected!", startMonth, startDay));
        Assert.assertTrue(endDateFound, String.format("End date %s %d is not selected!", endMonth, endDay));
    }

    private static boolean isDateSelected(Page page, Locator calendarLocator, int day) {
        Locator calendarPanel = calendarLocator.locator("nb-card:has(.calendar-navigation)");
        Locator dayLocator = calendarPanel.locator("nb-calendar-range-day-cell:not(.bounding-month)");
        Locator currentDay = getDayLocator(dayLocator, day);
        Locator previousDay = getDayLocator(dayLocator, day - 1);
        Locator nextDay = getDayLocator(dayLocator, day + 1);

        String currentClasses = currentDay.getAttribute("class");
        boolean isNotBoundingMonth = !isBoundingMonth(currentDay, previousDay, nextDay);
        boolean isEnd = currentClasses.contains("end");
        boolean isStart = currentClasses.contains("start");
        boolean previousSelected = isSelected(previousDay);
        boolean nextSelected = isSelected(nextDay);

        Locator nextDayLocator = calendarPanel.locator("nb-calendar-range-day-cell").filter(new Locator.FilterOptions()
                .setHasText(String.valueOf(day + 1))).first();

        if (day - 1 == 0) {
            ActionUtils.performActionWithScreenshot(page, "Click on the prev month arrow",
                    calendarLocator.locator("button.prev-month")::click, calendarLocator.locator("button.prev-month"));
            int previousDayValue = Integer.parseInt(calendarPanel.locator("nb-calendar-range-day-cell:not(.bounding-month)")
                    .last().locator("div").innerText());
            previousDay = getDayLocator(dayLocator, previousDayValue);
            previousSelected = isSelected(previousDay);
            isNotBoundingMonth = !isBoundingMonth(currentDay, previousDay, nextDay);
        } else if (nextDayLocator.getAttribute("class").contains("bounding-month")) {
            ActionUtils.performActionWithScreenshot(page, "Click on the nex month arrow",
                    calendarLocator.locator("button.next-month")::click, calendarLocator.locator("button.next-month"));
            int nextDayValue = Integer.parseInt(calendarPanel.locator("nb-calendar-range-day-cell:not(.bounding-month)")
                    .first().locator("div").innerText());
            nextDay = getDayLocator(dayLocator, nextDayValue);
            nextSelected = isSelected(nextDay);
            isNotBoundingMonth = !isBoundingMonth(currentDay, previousDay, nextDay);
        }

        if (isEnd) return previousSelected && !nextSelected && isNotBoundingMonth;
        if (isStart) return !previousSelected && nextSelected && isNotBoundingMonth;
        return false;
    }

    private static Locator getDayLocator(Locator dayLocator, int day) {
        return dayLocator.filter(new Locator.FilterOptions().setHasText(String.valueOf(day))).first();
    }

    private static boolean isBoundingMonth(Locator... locators) {
        return Arrays.stream(locators)
                .filter(Objects::nonNull)
                .map(locator -> locator.getAttribute("class"))
                .filter(Objects::nonNull)
                .anyMatch(classes -> classes.contains("bounding-month"));
    }

    private static boolean isSelected(Locator day) {
        return day != null && day.getAttribute("class").contains("selected");
    }

    private static int findMonthIndex(String month) {
        return Arrays.asList(new DateFormatSymbols().getMonths()).indexOf(month);
    }

    private static void validateDate(Locator calendarLocator, int year, String month, int day) {
        LoggingUtils.logDebug("Validating date");

        String actualDate = calendarLocator.locator("span.subtitle").innerText();
        String expectedDate = String.format("%s %s, %s", month.substring(0, 3), day, year);

        Assert.assertTrue(actualDate.contains(expectedDate),
                String.format("Expected date '%s', but found '%s'", expectedDate, actualDate));
    }

    private static void validateYearAndMonth(Locator calendarLocator, int year, String month) {
        LoggingUtils.logDebug("Validating year and month");

        String actualMonthAndYear = calendarLocator.locator("nb-calendar-view-mode button").innerText().trim();
        String expectedMonthAndYear = String.format("%s %s", month.toUpperCase(), year);

        Assert.assertEquals(actualMonthAndYear, expectedMonthAndYear,
                String.format("Expected month and year '%s', but found '%s'", expectedMonthAndYear, actualMonthAndYear));
    }

    private static void validateDay(Locator calendarLocator, int expectedDay) {
        LoggingUtils.logDebug("Validating day");

        Locator calendarPanel = calendarLocator.locator("nb-card:has(.calendar-navigation)");
        Locator daysCells = switch (calendarLocator.toString()) {
            case String s when s.contains("nb-calendar-range") ->
                    calendarPanel.locator("nb-calendar-range-day-cell.selected");
            case String s when s.contains("ng-reflect-day-cell-component") ->
                    calendarPanel.locator("ngx-day-cell.selected");
            default ->
                    calendarPanel.locator("nb-calendar-day-cell.selected");
        };

        String actualDay = daysCells.first()
                .locator("div").innerText().trim();

        Assert.assertEquals(actualDay, String.valueOf(expectedDay),
                String.format("Expected date '%s', but found '%s'", expectedDay, actualDay));
    }
}
