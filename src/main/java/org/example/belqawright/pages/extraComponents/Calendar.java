package org.example.belqawright.pages.extraComponents;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.validation.UIElement;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Calendar {
    private final Page page;

    public Calendar(Page page) {
        this.page = page;
    }

    @Step("Select single day")
    public void selectSingleDate(int year, String month, int day) {
        Locator calendarLocator = page.locator("nb-calendar[showweeknumber]");

        selectMonthEnsuringYear(year, month, calendarLocator);
        selectDay(day, calendarLocator);
    }

    @Step("Select day range")
    public void selectDateRange(int startYear, String startMonth, int startDay, int endYear, String endMonth, int endDay) {
        Locator calendarLocator = page.locator("nb-calendar-range[ng-reflect-range]");

        selectMonthEnsuringYear(startYear, startMonth, calendarLocator);
        selectDay(startDay, calendarLocator);

        selectMonthEnsuringYear(endYear, endMonth, calendarLocator);
        selectDay(endDay, calendarLocator);
    }

    @Step("Select day with price")
    public void selectPricedDate(int year, String month) {
        Locator calendarLocator = page.locator("nb-calendar[ng-reflect-day-cell-component]");

        selectMonthEnsuringYear(year, month, calendarLocator);
    }

    public List<UIElement> getEssentialUIElementsOnCalendar() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("div.calendar-container:has(nb-calendar[showweeknumber])",
                "Calendar with single date selection is not displayed"));
        elements.add(new UIElement("div.calendar-container:has(nb-calendar-range[ng-reflect-range])",
                "Calendar with date range selection is not displayed"));
        elements.add(new UIElement("div.calendar-container:has(nb-calendar[ng-reflect-day-cell-component])",
                "Calendar with price selection is not displayed"));

        return elements;
    }

    private void selectMonthEnsuringYear(int year, String month, Locator calendarLocator) {
        String currentYear = calendarLocator.locator("nb-calendar-view-mode button")
                .innerText().trim().split(" ")[1];

        if (!currentYear.equalsIgnoreCase(String.valueOf(year))) {
            selectYearAndMonth(year, month, calendarLocator);
        } else {
            selectMonth(month, calendarLocator);
        }
    }

    private void selectMonth(String month, Locator calendarLocator) {
        try {
            String[] months = new DateFormatSymbols().getMonths();
            Locator calendarPanel = calendarLocator.locator("nb-card:has(.calendar-navigation)");
            String currentMonth = calendarPanel.locator("nb-calendar-view-mode button")
                    .innerText().trim().split(" ")[0];

            if (!currentMonth.equalsIgnoreCase(month)) {
                int currentMonthIndex = findMonthIndex(months, currentMonth);
                int targetMonthIndex = findMonthIndex(months, month);
                int diff = Math.abs(targetMonthIndex - currentMonthIndex);

                Locator arrowLocator = (targetMonthIndex > currentMonthIndex)
                        ? calendarPanel.locator("nb-calendar-pageable-navigation button.next-month")
                        : calendarPanel.locator("nb-calendar-pageable-navigation button.prev-month");

                for (int i = 0; i < diff; i++) {
                    ActionUtils.performActionWithScreenshot(page, "Click on the arrow", arrowLocator::click, arrowLocator);
                }
            }
        } catch (Exception e) {
            LoggingUtils.logError("Error during choose month", e);
            throw new RuntimeException("Failed to choose month", e);
        }
    }

    private void selectYearAndMonth(int year, String month, Locator calendarLocator) {
        try {
            Locator calendarPanel = calendarLocator.locator("nb-card:has(.calendar-navigation)");
            Locator navigationPanel  = calendarPanel.locator(".calendar-navigation");
            Locator prevMonthButton = navigationPanel.locator("nb-calendar-pageable-navigation button.prev-month");
            Locator nextMonthButton = navigationPanel.locator("nb-calendar-pageable-navigation button.next-month");

            Locator yearCells = calendarLocator.toString().contains("nb-calendar-range") ?
                    calendarPanel.locator("nb-calendar-range-year-cell") :
                    calendarPanel.locator("nb-calendar-year-cell");
            Locator monthCells = calendarLocator.toString().contains("nb-calendar-range") ?
                    calendarPanel.locator("nb-calendar-range-month-cell") :
                    calendarPanel.locator("nb-calendar-month-cell");

            Locator targetYear = yearCells .filter(new Locator.FilterOptions().setHasText(String.valueOf(year)));
            Locator targetMonth = monthCells.filter(new Locator.FilterOptions().setHasText(month.substring(0, 3)));

            Locator dateRangeSelector = navigationPanel .locator("nb-calendar-view-mode button");
            ActionUtils.performActionWithScreenshot(page, "Click on the date range selector",
                    dateRangeSelector::click, dateRangeSelector);
            String[] years = getYearRange(navigationPanel );

            if (year < Integer.parseInt(years[0])) {
                moveToYear(prevMonthButton, navigationPanel , years, year);
            } else if (year > Integer.parseInt(years[1])) {
                moveToYear(nextMonthButton, navigationPanel , years, year);
            }

            ActionUtils.performActionWithScreenshot(page, "Click on the year button", targetYear::click, targetYear);
            ActionUtils.performActionWithScreenshot(page, "Click on the month button", targetMonth::click, targetMonth);
        } catch (Exception e) {
            LoggingUtils.logError("Error during choose month and year", e);
            throw new RuntimeException("Failed to choose month and year", e);
        }
    }

    private void selectDay(int day, Locator calendarLocator) {
        Locator calendarPanel = calendarLocator.locator("nb-card:has(.calendar-navigation)");
        Locator daysCells = calendarLocator.toString().contains("nb-calendar-range") ?
                calendarPanel.locator("nb-calendar-range-day-cell:not(.bounding-month)") :
                calendarPanel.locator("nb-calendar-day-cell:not(.bounding-month)");

        Locator targetDay = daysCells.filter(new Locator.FilterOptions().setHasText(String.valueOf(day))).first();

        ActionUtils.performActionWithScreenshot(page, "Click on the day button", targetDay::click, targetDay);
    }

    private int findMonthIndex(String[] months, String month) {
        return IntStream.range(0, months.length)
                .filter(i -> months[i].equalsIgnoreCase(month))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Month not found: " + month));
    }

    private String[] getYearRange(Locator navigationPanel ) {
        String range = navigationPanel .locator("nb-calendar-view-mode button").innerText().trim();
        if (!range.contains(" - ")) {
            throw new RuntimeException("Unexpected date range format: " + range);
        }
        return range.split(" - ");
    }

    private void moveToYear(Locator arrowButton, Locator navigationPanel , String[] years, int targetYear) {
        while ((targetYear < Integer.parseInt(years[0])) || (targetYear > Integer.parseInt(years[1]))) {
            ActionUtils.performActionWithScreenshot(page, "Click on the arrow", arrowButton::click, arrowButton);
            years = getYearRange(navigationPanel );
        }
    }
}
