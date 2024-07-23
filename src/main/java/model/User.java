package model;

/**
 * CREATE TABLE Users (
 * id INT AUTO_INCREMENT PRIMARY KEY,
 * userId VARCHAR(50) NOT NULL,
 * password VARCHAR(100) NOT NULL,
 * name VARCHAR(100) NOT NULL,
 * email VARCHAR(100) NOT NULL UNIQUE
 * );
 */
public class User {
    private int id;
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(int id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(String userId, String password, String name, String email) {
        this(0, userId, password, name, email);
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
