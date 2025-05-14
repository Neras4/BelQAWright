package org.example.belqawright.ui.validation;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.belqawright.utils.LoggingUtils;
import org.testng.Assert;

import java.util.Map;

public class ValidatorFormLayouts {
    public static void validateFormInputs(Page page, String formName, Map<String, String> inputValues, Map<String, Boolean> toggleElements) {
        LoggingUtils.logDebug(String.format("Validating Form layout: %s", formName));

        Locator formCard = page.locator(String.format("nb-card:has(nb-card-header:text('%s'))", formName));

        for (Map.Entry<String, String> entry : inputValues.entrySet()) {
            Locator inputField = formCard.locator(entry.getKey());
            Assert.assertEquals(inputField.inputValue(), entry.getValue(),
                    String.format("Expected value '%s' in input field '%s', but found '%s'", entry.getValue(),
                            entry.getKey(), inputField.inputValue()));

        }

        for (Map.Entry<String, Boolean> entry : toggleElements.entrySet()) {
            Locator toggleElement = formCard.locator(entry.getKey());
            boolean actualState = toggleElement.isChecked();
            Assert.assertEquals(actualState, entry.getValue(),
                    String.format("Expected state of toggle element '%s' to be '%s', but found '%s'",
                            entry.getKey(), entry.getValue(), actualState));
        }
    }
}
