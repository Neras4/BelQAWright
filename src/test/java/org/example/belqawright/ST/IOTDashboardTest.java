package org.example.belqawright.ST;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.example.belqawright.constants.TestConstants;
import org.example.belqawright.driver.BrowserManager;
import org.example.belqawright.pages.IOTDashboard;
import org.example.belqawright.utils.WebTestUtils;
import org.example.belqawright.validation.EssentialUIValidator;
import org.example.belqawright.validation.ST.IOTDashboardTestValidation;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("UI Tests")
@Feature("IoT Dashboard Features Tests")
public class IOTDashboardTest {
    private Page page;

    @BeforeClass
    public void setup() {
        page = BrowserManager.createPage();
        WebTestUtils.navigateToURL(page, TestConstants.BASE_URL, 5000);
        Assert.assertEquals(page.url(), TestConstants.IOT_DASHBOARD_URL, "Failed to navigate to the IoT dashboard URL");
        EssentialUIValidator.validateEssentialUIElements(page, new IOTDashboard(page).getEssentialUIElementsOnIOTDashboard());
    }

    @AfterClass
    public void tearDown() {
        BrowserManager.closeBrowser();
    }

    @Test(description = "Test: Toggle Smart House's components")
    @Story("IoT Dashboard page")
    @Severity(SeverityLevel.BLOCKER)
    public void testToggleSmartHouseComponent() {
        IOTDashboard iotDashboard = new IOTDashboard(page);

        iotDashboard.toggleSmartHouseComponent("Roller Shades");
        IOTDashboardTestValidation.validateToggleObject(page, "Roller Shades", "OFF");

        iotDashboard.toggleSmartHouseComponent("Roller Shades");
        IOTDashboardTestValidation.validateToggleObject(page,"Roller Shades", "ON");

        iotDashboard.toggleSmartHouseComponent("Coffee Maker");
        IOTDashboardTestValidation.validateToggleObject(page,"Coffee Maker", "OFF");

        iotDashboard.toggleSmartHouseComponent("Coffee Maker");
        IOTDashboardTestValidation.validateToggleObject(page,"Coffee Maker", "ON");
    }

    @Test(description = "Test: Choose room on Room Management")
    @Story("IoT Dashboard page")
    @Severity(SeverityLevel.CRITICAL)
    public void testChooseRoom() {
        IOTDashboard iotDashboard = new IOTDashboard(page);

        iotDashboard.chooseRoom("Kitchen");
        IOTDashboardTestValidation.validateChooseRoom(page,"Kitchen");

        iotDashboard.chooseRoom("Hallway");
        IOTDashboardTestValidation.validateChooseRoom(page,"Hallway");
    }

    @Test(description = "Test: Change temperature or humidity")
    @Story("IoT Dashboard page")
    @Severity(SeverityLevel.CRITICAL)
    public void testChangeTemperatureOrHumidity() {
        IOTDashboard iotDashboard = new IOTDashboard(page);

        double initialTemp = iotDashboard.getCurrentValue("temperature");
        iotDashboard.dragCircleFromTab("Temperature", 50.0, 50.5);
        IOTDashboardTestValidation.validateValueChange("Temperature", initialTemp,
                () -> iotDashboard.getCurrentValue("temperature"));

        double initialHumidity = iotDashboard.getCurrentValue("humidity");
        iotDashboard.dragCircleFromTab("Humidity", 50.0, 50.5);
        IOTDashboardTestValidation.validateValueChange("Humidity", initialHumidity,
                () -> iotDashboard.getCurrentValue("humidity"));
    }

    @Test(description = "Test: Validate Camera Interaction")
    @Story("IoT Dashboard page")
    @Severity(SeverityLevel.NORMAL)
    public void testInteractWithCamera() {
        IOTDashboard iotDashboard = new IOTDashboard(page);

        iotDashboard.chooseSpecificCamera("Camera #4");
        IOTDashboardTestValidation.validateCameraInteraction(page,"Specific camera");

        iotDashboard.showAllCamerasView();
        IOTDashboardTestValidation.validateCameraInteraction(page,"Whole cameras view");
    }

    @Test(description = "Test: Execute music player")
    @Story("IoT Dashboard page")
    @Severity(SeverityLevel.MINOR)
    public void testExecuteMusicPlayer() {
        IOTDashboard iotDashboard = new IOTDashboard(page);
        Locator trackObject = page.locator("div.track-info >> h4");
        String previousTrackName = trackObject.innerText();

        iotDashboard.executePlayMusic();
        IOTDashboardTestValidation.validateMusicStatus(page,"Play", true, previousTrackName);

        iotDashboard.skipForwardMusic();
        IOTDashboardTestValidation.validateMusicStatus(page,"Skip Forward", true, previousTrackName);
        previousTrackName = trackObject.innerText();

        iotDashboard.skipBackMusic();
        IOTDashboardTestValidation.validateMusicStatus(page,"Skip Back", true, previousTrackName);
        previousTrackName = trackObject.innerText();

        iotDashboard.executePlayMusic();
        IOTDashboardTestValidation.validateMusicStatus(page,"Pause", false, previousTrackName);
    }
}
