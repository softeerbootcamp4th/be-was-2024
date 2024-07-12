package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserUnitTest {

    @Test
    @DisplayName("Testing User Constructor and Getters - Success Case")
    public void testUserConstructorAndGetters() {
        // Given
        String userId = "john_doe";
        String password = "password123";
        String name = "John Doe";
        String email = "john.doe@example.com";
        User user = new User(userId, password, name, email);

        // When

        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Testing User toString Method - Success Case")
    public void testUserToString() {
        // Given
        String userId = "jane_smith";
        String password = "password456";
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        User user = new User(userId, password, name, email);
        String expectedToString = "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";

        // When

        // Then
        assertEquals(expectedToString, user.toString());
    }

    @Test
    @DisplayName("Testing User Constructor and Getters - Failure Case")
    public void testUserConstructorAndGettersFailure() {
        // Given
        String userId = "john_doe";
        String password = "password123";
        String name = "John Doe";
        String email = "john.doe@example.com";
        User user = new User(userId, password, name, email);

        // When

        // Then
        assertNotEquals("wrongUserId", user.getUserId());
        assertNotEquals("wrongPassword", user.getPassword());
        assertNotEquals("Wrong Name", user.getName());
        assertNotEquals("wrong.email@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Testing User toString Method - Failure Case")
    public void testUserToStringFailure() {
        // Given
        String userId = "jane_smith";
        String password = "password456";
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        User user = new User(userId, password, name, email);
        String notExpectedToString = "User [userId=" + userId + ", password=" + password + ", name=" + name + " ";

        // When

        // Then
        assertNotEquals(notExpectedToString, user.toString());
    }
}
