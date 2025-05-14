package org.example.belqawright.ui.validation;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.utils.factories.User;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class ValidatorSmartTable {
    public static void validateRecord(Page page, User user, boolean shouldExist) {
        Locator firstRow = page.locator("table tbody tr").nth(0);
        WebTestUtils.waitForElementVisible(firstRow, 4000);
        Locator cells = firstRow.locator("td");
        List<String> rowData = new ArrayList<>();

        System.out.println("cells count: " + cells.count());

        for (int i = 1; i < cells.count(); i++) {
            String cellText = cells.nth(i).locator("div.ng-star-inserted").innerText();
            rowData.add(cellText);
        }

        boolean isRecordExist = compareRowWithUser(rowData, user);

        if (shouldExist) {
            Assert.assertTrue(isRecordExist, "Record does not exist in the table");
        } else {
            Assert.assertFalse(isRecordExist, "Record exists in the table");
        }
    }

    public static void validatePageNumber(Page page, int pageNumber) {
        Locator pageButton = page.locator(String.format("ul.pagination li:has(span:text('%d'))", pageNumber));
        boolean isSelected = pageButton.getAttribute("class").contains("active");
        Assert.assertTrue(isSelected, "Page number is not selected");
    }

    public static void validateAmountRows(Page page, int expectedAmount) {
        Locator rows = page.locator("table tbody tr");
        int actualAmount = rows.count();
        Assert.assertEquals(actualAmount, expectedAmount, "Amount of rows is not as expected");
    }

    private static boolean compareRowWithUser(List<String> rowData, User user) {
        return rowData.get(0).equals(user.getId()) &&
                rowData.get(1).equals(user.getFirstName()) &&
                rowData.get(2).equals(user.getLastName()) &&
                rowData.get(3).equals(user.getUsername()) &&
                rowData.get(4).equals(user.getEmail()) &&
                rowData.get(5).equals(user.getAge());
    }
}
