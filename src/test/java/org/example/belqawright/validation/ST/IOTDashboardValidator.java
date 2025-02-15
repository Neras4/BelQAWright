package org.example.belqawright.validation.ST;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.belqawright.pages.IOTDashboard;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.utils.WebTestUtils;
import org.testng.Assert;

import java.util.function.Supplier;

public class IOTDashboardValidator {

    public static void validateToggleObject(Page page, String componentName, String expectedStatus) {
        LoggingUtils.logDebug(String.format("Validating toggle status for '%s', expected: %s", componentName, expectedStatus));

        expectedStatus = expectedStatus.trim().toUpperCase();
        String expectedAttributeStatus = expectedStatus.equals("OFF") ? "off" : "";

        Locator componentObject = page.locator(String.format("nb-card:has(div:text('%s'))", componentName));
        WebTestUtils.validateElementVisibility(componentObject, 3000, String.format("Failed to locate '%s' card", componentName));

        String classAttribute = componentObject.getAttribute("class");
        Assert.assertNotNull(classAttribute, String.format("Missing class attribute in '%s'", componentName));
        Assert.assertEquals(classAttribute, expectedAttributeStatus, String.format("Incorrect toggle state for '%s'", componentName));
        Assert.assertEquals(componentObject.locator("div.status.paragraph-2").innerText(), expectedStatus, String.format("Incorrect toggle text for '%s'", componentName));
    }

    public static void validateChooseRoom(Page page, String roomName) {
        LoggingUtils.logDebug(String.format("Validating room selection: '%s'", roomName));

        Locator room = page.locator("div.room-selector").locator(String.format("g:has(text:text('%s'))", roomName));
        WebTestUtils.validateElementVisibility(room, 3000, String.format("Failed to choose '%s' room", roomName));

        int selectedRoomCount = page.locator("g.selected-room").count();
        String classAttribute = room.getAttribute("class");

        Assert.assertEquals(selectedRoomCount, 1, String.format("More than one room is selected: expected 1, but found %d", selectedRoomCount));
        Assert.assertNotNull(classAttribute, String.format("Missing class attribute in '%s'", roomName));
        Assert.assertTrue(classAttribute.contains("selected-room"), String.format("Room '%s' is not selected", roomName));
    }

    public static void validateCameraInteraction(Page page, String typeOfView) {
        LoggingUtils.logDebug(String.format("Validating camera interaction: %s", typeOfView));

        int amountOfCameras = page.locator("ngx-security-cameras div.grid-container div.camera").count();
        String singleCameraViewAppearance = page.locator("nb-card-header").locator("button.single-view-button").getAttribute("ng-reflect-appearance");
        String wholeCamerasViewAppearance = page.locator("nb-card-header").locator("button.grid-view-button").getAttribute("ng-reflect-appearance");

        if (typeOfView.equals("Specific camera")) {
            assertCameraView(singleCameraViewAppearance, wholeCamerasViewAppearance, amountOfCameras, "filled", "outline", 1, "specific");
        } else if (typeOfView.equals("Whole cameras view")) {
            assertCameraView(singleCameraViewAppearance, wholeCamerasViewAppearance, amountOfCameras, "outline", "filled", 4, "whole view");
        } else {
            LoggingUtils.logDebug("Invalid camera view type: " + typeOfView);
            throw new IllegalArgumentException(String.format("Invalid camera view type: %s", typeOfView));
        }
    }

    private static void assertCameraView(String actualFilled, String actualOutline, int actualCameras,
                                  String expectedFilled, String expectedOutline, int expectedCameras,
                                  String mode) {
        LoggingUtils.logDebug(String.format("Asserting camera view mode: %s", mode));

        Assert.assertEquals(actualFilled, expectedFilled, String.format("Failed to choose %s camera view", mode));
        Assert.assertEquals(actualOutline, expectedOutline, String.format("Failed to choose %s camera view", mode));
        Assert.assertEquals(actualCameras, expectedCameras, String.format("Incorrect number of cameras in %s mode", mode));
    }

    public static void validateMusicStatus(Page page, String action, boolean isPlaying, String previousTrackName) {
        LoggingUtils.logDebug(String.format("Validating music player action: %s", action));

        String currentTrackName = page.locator("div.track-info >> h4").innerText();
        Locator button = switch (action) {
            case "Play", "Pause" -> page.locator("button:has(nb-icon.play)");
            case "Skip Forward" -> IOTDashboard.getMusicButton(page, "skip-forward-outline");
            case "Skip Back" -> IOTDashboard.getMusicButton(page, "skip-back-outline");
            default -> throw new RuntimeException(String.format("Invalid action: %s", action));
        };

        WebTestUtils.validateElementVisibility(button, 3000, String.format("%s button not found", action));

        Locator playButton = page.locator("button:has(nb-icon.play)");
        String playButtonIconAttribute = playButton.locator("nb-icon").getAttribute("ng-reflect-icon");
        String playButtonIconDataName = playButton.locator("g[data-name='Layer 2']").locator("g").getAttribute("data-name");

        String expectedIcon = isPlaying ? "pause-circle-outline" : "play-circle-outline";
        String expectedDataName = isPlaying ? "pause-circle" : "play-circle";

        Assert.assertEquals(playButtonIconAttribute, expectedIcon, "Unexpected play/pause button state");
        Assert.assertEquals(playButtonIconDataName, expectedDataName, "Unexpected play/pause icon state");

        if (action.equals("Play") || action.equals("Pause")) {
            Assert.assertEquals(previousTrackName, currentTrackName, "Failed to play music");
        } else {
            Assert.assertNotEquals(previousTrackName, currentTrackName, "Failed to skip music");
        }
    }

    public static void validateValueChange(String valueType, double initialValue, Supplier<Double> getValue) {
        LoggingUtils.logDebug(String.format("Validating %s change...", valueType));

        double newValue = getValue.get();

        Assert.assertNotEquals(newValue, initialValue, String.format("%s did not change!", valueType));
    }
}
