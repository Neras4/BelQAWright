package org.example.belqawright.pages.tablesAndData;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.utils.factories.User;
import org.example.belqawright.validation.UIElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SmartTable {
    private final Page page;

    public SmartTable(Page page) {
        this.page = page;
    }

    @Step("Submit new record")
    public void submitNewRecord(User user, boolean accept) {
        LoggingUtils.logInfo("Submit new record: " + (accept ? "Accept" : "Reject"));

        Locator filterRow = page.locator("tr[ng2-st-thead-filters-row]");
        Locator newUserRow = page.locator("tr[ng2-st-thead-form-row]");

        clickAddButton(filterRow);
        fillRow(user, newUserRow);

        if (accept) clickAcceptButton(newUserRow);
        else clickRejectButton(newUserRow);
    }

    @Step("Edit record")
    public void editRecord(User userFirst, User userSecond, boolean accept) {
        LoggingUtils.logInfo("Edit record for " + userFirst.getUsername());

        filterByUser(userFirst);
        Locator userRow = page.locator("tbody tr.ng2-smart-row").filter(
                new Locator.FilterOptions().setHasText(String.valueOf(userFirst.getId())));

        clickEditButton(userRow);

        userRow = page.locator("tbody tr.ng2-smart-row");
        fillRow(userSecond, userRow);

        if (accept) {
            updateUserData(userFirst, userSecond);
            clickAcceptButton(userRow);
        } else clickRejectButton(userRow);
    }

    @Step("Delete record")
    public void deleteRecord(User user, boolean isDelete) {
        LoggingUtils.logInfo("Deleting record for " + user.getUsername());

        filterByUser(user);
        Locator userRow = page.locator("tbody tr.ng2-smart-row").filter(
                new Locator.FilterOptions().setHasText(String.valueOf(user.getId())));

        page.onceDialog(dialog -> {
            if ("confirm".equals(dialog.type())) {
                if (isDelete) {
                    dialog.accept();
                } else {
                    dialog.dismiss();
                }
            }
        });

        clickDeleteButton(userRow);
        clearFilter();
    }

    @Step("Filter by user")
    public void filterByUser(User user) {
        LoggingUtils.logInfo("Filter by user " + user.getUsername());

        Locator filterRow = page.locator("tr[ng2-st-thead-filters-row]");
        fillRow(user, filterRow);
    }

    @Step("Clear filter")
    public void clearFilter() {
        LoggingUtils.logInfo("Clearing filter");

        Locator filterRow = page.locator("tr[ng2-st-thead-filters-row]");
        clearRow(filterRow);
    }

    @Step("Go to certain page")
    public void goToPage(int pageNumber) {
        LoggingUtils.logInfo("Navigating to page number: " + pageNumber);

        while (true) {
            Locator pageButton = page.locator(String.format("ul.pagination li:has(span:text('%d'))", pageNumber));

            if (pageButton.count() > 0) {
                try {
                    ActionUtils.performActionWithScreenshot(page, "Navigate to page " + pageNumber,
                            pageButton::click, pageButton);
                    return;
                } catch (Exception e) {
                    LoggingUtils.logError("Failed to click page number " + pageNumber, e);
                    throw new RuntimeException("Failed to click page number " + pageNumber, e);
                }
            }
            navigateToPage("next");
        }
    }

    @Step("Navigate to page")
    public void navigateToPage(String action) {
        LoggingUtils.logInfo("Navigating to page: " + action);

        Locator button = switch (action.toLowerCase()) {
            case "next" -> page.locator("ul.pagination li:has(a[aria-label='Next']):not(.disabled) a");
            case "prev" -> page.locator("ul.pagination li:has(a[aria-label='Prev']):not(.disabled) a");
            case "first" -> page.locator("ul.pagination li:has(a[aria-label='First']):not(.disabled) a");
            case "last" -> page.locator("ul.pagination li:has(a[aria-label='Last']):not(.disabled) a");
            default -> throw new IllegalArgumentException("Unknown page navigation action: " + action);
        };

        if (button.count() == 0) {
            LoggingUtils.logWarn("Cannot navigate: " + action + " button is disabled or not found.");
            throw new RuntimeException("Cannot navigate: " + action + " button is disabled or not found.");
        }

        try {
            ActionUtils.performActionWithScreenshot(page, "Navigate to " + action, button::click, button);
        } catch (Exception e) {
            LoggingUtils.logError("Error clicking " + action + " button.", e);
            throw new RuntimeException("Error clicking " + action + " button.", e);
        }
    }

    public List<UIElement> getEssentialUIElementsOnSmartTable() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("table",
                "Table could not be located on the page"));
        elements.add(new UIElement("thead",
                "Table head could not be located on the page"));
        elements.add(new UIElement("tbody",
                "Table body could not be located on the page"));

        return elements;
    }

    private void updateUserData(User userOne, User userTwo) {
        userOne.setId(userTwo.getId());
        userOne.setFirstName(userTwo.getFirstName());
        userOne.setLastName(userTwo.getLastName());
        userOne.setUsername(userTwo.getUsername());
        userOne.setEmail(userTwo.getEmail());
        userOne.setAge(userTwo.getAge());
    }

    private void fillRow(User user, Locator row) {
        LoggingUtils.logInfo("Filling user row");

        Map<String, String> userFields = new LinkedHashMap<>();
        userFields.put("ID", user.getId());
        userFields.put("First Name", user.getFirstName());
        userFields.put("Last Name", user.getLastName());
        userFields.put("Username", user.getUsername());
        userFields.put("E-mail", user.getEmail());
        userFields.put("Age", user.getAge());

        userFields.forEach((placeholder, value) -> {
            if (value != null) fillCell(row, placeholder, value);
        });
    }

    private void clearRow(Locator row) {
        LoggingUtils.logInfo("Clearing user row");
        String[] placeholders = {"ID", "First Name", "Last Name", "Username", "E-mail", "Age"};

        for (String placeholder : placeholders) {
            fillCell(row, placeholder, "");
        }
    }

    private void fillCell(Locator row, String placeholder, String value) {
        try {
            Locator cell = row.locator("input[placeholder='%s']".formatted(placeholder));
            ActionUtils.performActionWithScreenshot(page, String.format("Fill %s with %s", placeholder, value), () -> cell.fill(value), cell);
        } catch (Exception e) {
            LoggingUtils.logError(String.format("Error during %s submission", placeholder), e);
            throw new RuntimeException(String.format("Failed to fill and submit %s}", placeholder), e);
        }
    }

    private void clickButton(Locator row, String iconClass, String actionName) {
        try {
            Locator button = row.locator("i." + iconClass);
            ActionUtils.performActionWithScreenshot(page, "Click " + actionName, button::click, button);
        } catch (Exception e) {
            LoggingUtils.logError(String.format("Error during click '%s'", actionName), e);
            throw new RuntimeException(String.format("Failed to click '%s'", actionName), e);
        }
    }


    private void clickAddButton(Locator row) { clickButton(row, "nb-plus", "Add button"); }
    private void clickAcceptButton(Locator row) { clickButton(row, "nb-checkmark", "Accept button"); }
    private void clickRejectButton(Locator row) { clickButton(row, "nb-close", "Reject button"); }
    private void clickEditButton(Locator row) { clickButton(row, "nb-edit", "Edit button"); }
    private void clickDeleteButton(Locator row) { clickButton(row, "nb-trash", "Delete button"); }
}