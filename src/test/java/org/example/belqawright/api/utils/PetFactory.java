package org.example.belqawright.api.utils;

public class PetFactory {
    public static String buildPetJson(int id, int categoryId, String name, int tagsId, String status) {
        return String.format("""
                {
                "id": %s,
                        "category": {
                            "id": %s,
                            "name": "string"
                        },
                        "name": "%s",
                        "photoUrls": [
                            "string"
                        ],
                        "tags": [
                            {
                                "id": %s,
                                "name": "string"
                            }
                        ],
                        "status": "%s"
                    }""", id, categoryId, name, tagsId, status);
    }

    public static String updatePetJson(String previousVersion, int id, String name, String status) {
        String updated = previousVersion;

        updated = updated.replaceAll("\"id\"\\s*:\\s*\\d+", "\"id\": " + id);
        updated = updated.replaceAll("\"name\"\\s*:\\s*\"[^\"]+\"", "\"name\": \"" + name + "\"");
        updated = updated.replaceAll("\"status\"\\s*:\\s*\"[^\"]+\"", "\"status\": \"" + status + "\"");

        return updated;
    }

}
