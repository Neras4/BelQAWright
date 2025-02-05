package org.example.belqawright.utils;

public class CommonUtils {
    public static String generateUniqueName(String base) {
        return base.replace(" ", "_")
                .replaceAll("[^a-zA-Z0-9_-]", "");
    }
}
