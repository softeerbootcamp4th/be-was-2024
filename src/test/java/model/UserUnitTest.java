package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserUnitTest {

    @Test
    public void testUserConstructorAndGetters() {
        // Given
        String userId = "john_doe";
        String password = "password123";
        String name = "John Doe";
        String email = "john.doe@example.com";

        // When
        User user = new User(userId, password, name, email);

        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testUserToString() {
        // Given
        String userId = "jane_smith";
        String password = "password456";
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        User user = new User(userId, password, name, email);

        // When
        String expectedToString = "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";

        // Then
        assertEquals(expectedToString, user.toString());
    }
}
