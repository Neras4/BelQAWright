package org.example.belqawright.utils.factories;

import com.github.javafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

public class UserFactory {

    private static final Faker faker = new Faker();

    public static User randomUser() {
        return new User(
                String.valueOf(ThreadLocalRandom.current().nextInt(100, 999)),
                faker.name().firstName(),
                faker.name().lastName(),
                "@" + faker.name().username(),
                faker.internet().emailAddress(),
                String.valueOf(ThreadLocalRandom.current().nextInt(18, 80))
        );
    }
}
