package org.example.belqawright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.pages.auth.Register;
import org.example.belqawright.pages.extraComponents.Calendar;
import org.example.belqawright.pages.forms.FormLayouts;
import org.example.belqawright.pages.tablesAndData.SmartTable;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.validation.UIElement;

import java.util.ArrayList;
import java.util.List;

public class FeaturesToolbar {
    private final Page page;

    public FeaturesToolbar(Page page) {
        this.page = page;
    }

    @Step("Open Form Layout")
    public FormLayouts openFormLayout() {
        clickAndExpandFeature("Forms", "Form Layouts");
        return new FormLayouts(page);
    }

    @Step("Open Smart Table")
    public SmartTable openSmartTable() {
        clickAndExpandFeature("Tables & Data", "Smart Table");
        return new SmartTable(page);
    }

    @Step("Open Calendar")
    public Calendar openCalendar() {
        clickAndExpandFeature("Extra Components", "Calendar");
        return new Calendar(page);
    }

    @Step("Open Register page")
    public Register openRagisterPage() {
        clickAndExpandFeature("Auth", "Register");
        return new Register(page);
    }

    public List<UIElement> getEssentialUIElementsOnFeaturesToolbar() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("div.scrollable", "Features Toolbar container could not be located on the page"));
        elements.add(new UIElement("a[title='Forms']", "'Forms' section in the toolbar could not be expanded or located"));
        elements.add(new UIElement("a[title='Tables & Data']", "'Tables & Data' section in the toolbar could not be expanded or located"));
        elements.add(new UIElement("a[title='Extra Components']", "'Extra Components' section in the toolbar could not be expanded or located"));
        elements.add(new UIElement("a[title='Auth']", "'Forms' section in the toolbar could not be expanded or located"));

        return elements;
    }

    private void clickAndExpandFeature(String expandTitle, String targetTitle) {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Expanding and clicking " + targetTitle);

            Locator expandLocator = page.locator(String.format("a[title='%s']", expandTitle));
            Locator targetLocator = page.locator(String.format("a[title='%s']", targetTitle));

            ActionUtils.performActionWithScreenshot(page, "Click on " + expandTitle,
                    expandLocator::click, expandLocator);
            String ariaExpanded = expandLocator.getAttribute("aria-expanded");

            if("true".equals(ariaExpanded)) {
                ActionUtils.performActionWithScreenshot(page, "Click on " + targetTitle,
                        targetLocator::click, targetLocator);
            } else {
                LoggingUtils.logWarn(targetTitle + " is not expanded. aria-expanded=" + ariaExpanded);
            }
        } catch (Exception e) {
            LoggingUtils.logError("Error during expand and click " + targetTitle, e);
            throw new RuntimeException("Failed to expand and click " + targetTitle, e);
        }
    }
}