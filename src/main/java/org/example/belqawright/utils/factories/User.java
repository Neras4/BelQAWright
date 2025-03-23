package org.example.belqawright.utils.factories;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String age;

    public User(String id, String firstName, String lastName, String username, String email, String age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getAge() { return age; }

    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setAge(String age) { this.age = age; }
}

