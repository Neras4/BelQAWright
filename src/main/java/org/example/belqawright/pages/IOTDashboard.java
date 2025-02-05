package org.example.belqawright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.validation.UIElement;

import java.util.ArrayList;
import java.util.List;

public class IOTDashboard {
    private final Page page;

    public IOTDashboard(Page page) {
        this.page = page;
    }

    @Step("On and Off Smart House's components")
    public void toggleSmartHouseComponent(String componentName) {
        Locator statusCard = page.locator(String.format("ngx-status-card[ng-reflect-title='%s'] nb-card", componentName));
        clickOnIOTObject("Toggle " + componentName, statusCard);
    }

    @Step("Choose room on Public management")
    public void chooseRoom(String roomName) {
        Locator room = page.locator("div.room-selector")
                .locator(String.format("g:has(text:text('%s'))", roomName));
        clickOnIOTObject("Choose " + roomName + " in Room management", room);
    }

    @Step("Execute play music")
    public void executePlayMusic() {
        clickOnIOTObject("Play music", page.locator("button:has(nb-icon.play)"));
    }

    @Step("Skip forward music")
    public void skipForwardMusic () {
        clickOnIOTObject("Skip forward music", getMusicButton(page, "skip-forward-outline"));
    }

    @Step("Skip back music")
    public void skipBackMusic() {
        clickOnIOTObject("Skip back music", getMusicButton(page, "skip-back-outline"));
    }

    @Step("Choose camera view with name")
    public void chooseSpecificCamera(String cameraName) {
        Locator cameraObject = page.locator(String.format(".grid-view .camera:has-text('%s')", cameraName));
        clickOnIOTObject(String.format("Choose camera view with name %s", cameraName), cameraObject);
    }

    @Step("Show whole cameras view")
    public void showAllCamerasView  () {
        Locator returnToWholeCameras = page.locator("nb-card-header").locator("button.grid-view-button");
        clickOnIOTObject("Show whole cameras view", returnToWholeCameras);
    }

    @Step("Drag circle element")
    public void dragCircleFromTab(String tabName, Double deltaX, Double deltaY) {
        Locator tabElement = page.locator(String.format("li:has-text('%s')", tabName));
        Locator circleElement = page.locator(String.format("nb-tab[tabtitle='%s'] svg", tabName)).locator("circle");

        clickOnIOTObject(String.format("Choose %s tab", tabName), tabElement);
        dragCircle(circleElement, deltaX, deltaY);
    }

    public List<UIElement> getEssentialUIElementsOnIOTDashboard() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("ngx-dashboard.ng-star-inserted", "IoT Dashboard could not be located on the page"));
        elements.add(new UIElement("ngx-room-selector:has(nb-card-header:has-text('Room Management'))", "'Room Management' section in the page could not be located"));
        elements.add(new UIElement("ngx-player:has(nb-card-header:has-text('My Playlist'))", "'Tables & Data' section in the toolbar could not be located"));
        elements.add(new UIElement("nb-card:has(nb-card-header:has-text('Security Cameras'))", "'Extra Components' section in the toolbar could not be located"));
        elements.add(new UIElement("nb-tabset:has(nb-tab[tabtitle='Temperature'])", "'Temperature' section in the toolbar could not be located"));
        elements.add(new UIElement("nb-tabset:has(nb-tab[tabtitle='Humidity'])", "'Humidity' section in the toolbar could not be located"));

        return elements;
    }

    private void clickOnIOTObject(String description, Locator locator) {
        try {
            LoggingUtils.logInfoWithScreenshot(page, description);
            ActionUtils.performActionWithScreenshot(page, description, locator::click, locator);
        } catch (Exception e) {
            LoggingUtils.logError("Error during " + description, e);
            throw new RuntimeException("Failed to " + description, e);
        }
    }

    private void dragCircle(Locator circleElement, Double deltaX, Double deltaY) {
        try{
            LoggingUtils.logInfoWithScreenshot(page, "Drag circle element");
            ActionUtils.dragCircleWithDeltaAndScreenshot(page, circleElement, deltaX, deltaY);
        } catch (Exception e) {
            LoggingUtils.logError("Error during drag circle element", e);
            throw new RuntimeException("Failed to drag circle element", e);
        }
    }

    public double getCurrentValue(String valueType) {
        Locator valueElement = page.locator(String.format(".value.%s.h1", valueType));
        return Double.parseDouble(valueElement.innerText().trim());
    }

    public static Locator getMusicButton(Page page, String iconName) {
        return page.locator(String.format("button:has(nb-icon[ng-reflect-icon='%s'])", iconName));
    }
}
